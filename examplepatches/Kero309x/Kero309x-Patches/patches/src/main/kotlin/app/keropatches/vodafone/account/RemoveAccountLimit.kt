// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 Kero309x

package app.keropatches.vodafone.account

import app.keropatches.core.PatchCategory
import app.keropatches.core.answerIntCallsWithZero
import app.keropatches.vodafone.VodafoneApp
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * The check that decides whether the account list is full.
 *
 * The limit is five saved accounts, or six when the first of them is a seamless
 * one. The method is private and static, so the class and the signature
 * together identify it.
 */
private object AccountLimitFingerprint : Fingerprint(
    definingClass = "Lvodafone/vis/engezly/account/switchaccount/presentation/ui/SwitchAccountOverlayKt;",
    name = "checkIfAccountsMaxLimit",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Ljava/util/List;"),
)

/**
 * The second place that counts the saved accounts.
 *
 * The overlay that asks which account to switch to compares the size of the
 * saved list with five and shows the "too many accounts" screen when the list
 * is at least that long. The comparison is inline, so the count is answered
 * instead of the branch.
 */
private object SavedAccountCountFingerprint : Fingerprint(
    definingClass = "Lvodafone/vis/engezly/account/login/presentation/credential/ui/overlay/CredentialsLoginOverlayKt;",
    name = "onSwitchAccountClicked",
    isStatic = true,
    returnType = "V",
    parameters = listOf(
        "Lvodafone/vis/engezly/account/login/presentation/model/SavedAccountsUiState;",
        "Lkotlin/jvm/functions/Function0;",
        "Lkotlin/jvm/functions/Function0;",
    ),
)

/**
 * Removes the limit on the number of saved accounts.
 *
 * The stock build keeps at most five accounts, or six when the first of them is
 * a seamless one, and refuses to switch to another account once the list is
 * that long. The limit is checked in two places: the overlay that lists the
 * accounts, and the credential-login overlay that asks which account to switch
 * to. Both are answered here.
 *
 * The first check is a method of its own, so it is answered with "the list is
 * not full". The second check compares the size of the list with five inline;
 * the size is answered with zero instead, which is the answer the comparison
 * takes for a list that is not full. The account that is being switched to is
 * found in the list in the same way as before, so the account that is opened
 * does not change.
 *
 * The accounts themselves are stored the way they always were. The patch only
 * removes the check, so nothing about how an account is remembered, logged in
 * or removed changes.
 */
@Suppress("unused")
val removeAccountLimitPatch = bytecodePatch(
    name = "Remove the saved account limit",
    description = "Lets the app keep more than five accounts and switch between all of them.",
    default = true,
) {
    compatibleWith(VodafoneApp.compatibility)
    category(PatchCategory.ENTITLEMENT.label)

    execute {
        AccountLimitFingerprint.method.returnEarly(false)
        SavedAccountCountFingerprint.method.answerIntCallsWithZero("Ljava/util/List;->size()I")
    }
}
