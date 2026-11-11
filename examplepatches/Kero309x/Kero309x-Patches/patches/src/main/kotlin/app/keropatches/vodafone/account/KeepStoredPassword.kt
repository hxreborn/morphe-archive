// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 Kero309x

package app.keropatches.vodafone.account

import app.keropatches.core.PatchCategory
import app.keropatches.core.replaceCallWithBlock
import app.keropatches.vodafone.VodafoneApp
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch

/**
 * The mapper that turns a login response into the user the application keeps.
 *
 * It reads the signed token out of the response, builds the user from it and
 * then writes the password of this login over the one the token carries. The
 * class and the method are both renamed by the shrinker, so the class and the
 * full signature together identify it.
 */
private object LoginUserMapperFingerprint : Fingerprint(
    definingClass = "Lo/ContentPainterModifiermeasure1;",
    name = "AnimatedBarChartKtAnimatedBarChart1111",
    isStatic = false,
    returnType = "Lvodafone/vis/engezly/data/models/jwt/UserInfo;",
    parameters = listOf(
        "Lcom/vodafone/networklayer/dxl/models/LoginResponseModel;",
        "Z",
        "Ljava/lang/String;",
    ),
)

/**
 * Keeps the password the application already knows for an account.
 *
 * The application stores the password of every account, encrypted, in its own
 * database, and uses it to log in again when the account is opened. The
 * password reaches the stored record through the mapper that turns a login
 * response into the user the application keeps: the mapper reads the user out
 * of the signed token and then writes the password of this login over the one
 * the token carries.
 *
 * That overwrite is what makes the application forget. A login that carries no
 * password of its own - one that was started by a link, by a token that was
 * refreshed, or by any flow that does not ask for one - writes an empty
 * password over the stored one, and the next time the account is opened the
 * application asks for the password again.
 *
 * The patch writes the password only when the login carries one. A login that
 * does not carries one keeps the password the signed token holds, which is the
 * password the server holds for that account, so the application can log in
 * with it without asking.
 *
 * The rest of the mapper is untouched: the token information and the user
 * configuration are still written as they were, and the user is still built
 * from the token in the first place.
 */
@Suppress("unused")
val keepStoredPasswordPatch = bytecodePatch(
    name = "Keep the password of each account",
    description = "Stops the app forgetting the password of an account, so it stops asking for it every time.",
    default = true,
) {
    compatibleWith(VodafoneApp.compatibility)
    category(PatchCategory.ENTITLEMENT.label)

    execute {
        LoginUserMapperFingerprint.method.replaceCallWithBlock(
            "Lvodafone/vis/engezly/data/models/jwt/UserInfo;->setPassword(Ljava/lang/String;)V",
            """
            if-eqz p3, :kero_keep_token_password
            invoke-virtual {p3}, Ljava/lang/String;->isEmpty()Z
            move-result p2
            if-nez p2, :kero_keep_token_password
            invoke-virtual {v0, p3}, Lvodafone/vis/engezly/data/models/jwt/UserInfo;->setPassword(Ljava/lang/String;)V
            :kero_keep_token_password
            """.trimIndent(),
        )
    }
}
