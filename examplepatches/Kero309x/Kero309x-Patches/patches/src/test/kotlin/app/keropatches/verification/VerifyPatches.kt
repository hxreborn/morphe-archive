// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 Kero309x

package app.keropatches.verification

import app.keropatches.registry.PatchRegistry
import app.morphe.patcher.Patcher
import app.morphe.patcher.PatcherConfig
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess

/**
 * Applies every registered patch to an unmodified APK and reports the result.
 *
 * This is a development tool, not part of the shipped bundle. It exists because a
 * fingerprint that cannot be found must fail here, on a developer's machine,
 * rather than during a user's patch run.
 *
 * Usage:
 * ```
 * gradlew verifyPatches -Papk=C:\path\to\base.apk
 * gradlew verifyPatches -Papk=... -Pout=C:\path\to\patched.apk
 * ```
 *
 * Without `-Pout` the run stops after the execute blocks, which is the point where
 * every fingerprint has been resolved. With `-Pout` it also compiles the result,
 * which is the only way to prove that injected instructions assemble and that
 * every register they name exists. The output is unsigned, so it is a build proof
 * rather than something to install.
 */
fun main() {
    val property = System.getProperty("keropatches.apk")
    if (property.isNullOrBlank()) {
        System.err.println("No APK supplied. Pass -Papk=<path to an unmodified APK>.")
        exitProcess(2)
    }

    val apk = File(property).absoluteFile
    if (!apk.isFile) {
        System.err.println("APK not found: $apk")
        exitProcess(2)
    }

    val outputPath = System.getProperty("keropatches.out").takeUnless { it.isNullOrBlank() }
    val output = outputPath?.let { File(it).absoluteFile }
    output?.parentFile?.mkdirs()

    val workspace = createTempDir(prefix = "keropatches-verify-")
    val failures = mutableListOf<String>()
    var executed = 0

    try {
        val config = PatcherConfig(
            apkFile = apk,
            temporaryFilesPath = workspace.resolve("temp"),
            validate = true,
            targetPatcherVersion = "2.2.0",
        )

        Patcher(config).use { patcher ->
            patcher += PatchRegistry.all

            runBlocking {
                patcher().collect { result ->
                    val name = result.patch.name ?: result.patch.toString()
                    val exception = result.exception
                    if (exception == null) {
                        executed++
                        println("PASS  $name")
                    } else {
                        failures += name
                        println("FAIL  $name")
                        exception.printStackTrace(System.out)
                    }
                }
            }

            if (failures.isEmpty() && output != null) {
                // Compiling rewrites every modified DEX file, so a malformed
                // injected instruction fails here instead of on a user's device.
                patcher.writeApk(output)
                println()
                println("Wrote patched APK to $output (unsigned)")
            }
        }
    } finally {
        workspace.deleteRecursively()
    }

    println()
    if (failures.isEmpty() && executed == PatchRegistry.all.size) {
        println("All $executed registered patches applied to $apk")
    } else {
        println("Executed $executed of ${PatchRegistry.all.size}; failed: ${failures.joinToString()}")
        exitProcess(1)
    }
}

private fun createTempDir(prefix: String): File {
    val dir = File(System.getProperty("java.io.tmpdir"), "$prefix${System.nanoTime()}")
    check(dir.mkdirs()) { "Could not create a working directory at $dir" }
    return dir
}
