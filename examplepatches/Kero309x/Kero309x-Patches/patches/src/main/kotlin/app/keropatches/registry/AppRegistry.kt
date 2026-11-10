// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 Kero309x

package app.keropatches.registry

import app.keropatches.anghami.AnghamiApp
import app.keropatches.core.AppDefinition

/**
 * Every application this bundle supports.
 *
 * This is the only file that knows about concrete applications, which keeps the
 * shared layer independent of them. Adding an application means adding its
 * package, one entry here, and its behaviour specs.
 */
object AppRegistry {
    val applications: List<AppDefinition> = listOf(
        AnghamiApp.definition,
    )

    fun byPackageName(packageName: String): AppDefinition? =
        applications.firstOrNull { it.packageName == packageName }

    fun requireById(id: String): AppDefinition =
        applications.firstOrNull { it.id == id }
            ?: error("Unknown application id '$id'. Known ids: ${applications.joinToString { it.id }}")
}
