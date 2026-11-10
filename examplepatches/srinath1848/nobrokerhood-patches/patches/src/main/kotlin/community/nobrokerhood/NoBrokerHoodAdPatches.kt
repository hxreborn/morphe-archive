package community.nobrokerhood

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.matchSingle
import app.morphe.patcher.util.returnEarly

/**
 * NoBrokerHood 3.6.11 (versionCode 712) experimental community patches.
 * Fingerprints were checked against the user-supplied 3.6.11 XAPK.
 * NOT compiled or runtime-tested on a device for this revision.
 *
 * Security-sensitive limitation:
 * - No patches to Firebase token creation/registration or message delivery.
 * - No patches to DoorPush.hasBanner or DisplayEntryBannerFragment.onViewCreated.
 * - No attempted spoof of notification token diagnostics.
 *
 * Test the post-approval ad patch separately from the homepage patches.
 * Keep all experimental patches off until notification delivery is verified.
 */
private val NOBROKERHOOD_3611 = Compatibility(
    name = "NoBrokerHood",
    packageName = "com.app.nobrokerhood",
    targets = listOf(AppTarget(version = "3.6.11")),
)

/** Visitor carousel only; does not change the visitor approval workflow. */
@Suppress("unused")
val hideVisitorAdsPatch = bytecodePatch(
    name = "NoBrokerHood: Hide visitor-flow ads",
    description = "Hide the visitor-flow promotional carousel (3.6.11).",
    default = true,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        Fingerprint(
            definingClass = "Lcom/app/nobrokerhood/features/visitorflow/presentation/composables/AdBannerKt;",
            name = "AdBannerSection",
            returnType = "V",
            parameters = listOf(
                "Lcom/app/nobrokerhood/features/visitorflow/presentation/VisitorFlowViewModel;",
                "Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "Ljava/lang/String;",
                "Lcom/app/nobrokerhood/features/visitorflow/presentation/UiState;",
                "Lkotlin/jvm/functions/Function1;", "Li3/r;", "I", "I",
            ),
        ).matchSingle().method.returnEarly()
    }
}

/** Forum/feed advertisements, not the homepage top billboard. */
@Suppress("unused")
val hideHomeFeedAdsPatch = bytecodePatch(
    name = "NoBrokerHood: Hide feed ad cards",
    description = "Hide forum/home-feed ad cards (3.6.11).",
    default = true,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        Fingerprint(
            definingClass = "Lcom/app/nobrokerhood/features/home_feed/ui/HomeFeedAdBannerKt;",
            name = "HomeFeedAdBanner",
            returnType = "V",
            parameters = listOf(
                "Lc4/m;",
                "Lcom/app/nobrokerhood/features/forum_new/home/model/FeedAdCard;",
                "Z", "Z", "Lkotlin/jvm/functions/Function1;",
                "Lkotlin/jvm/functions/Function3;",
                "Lkotlin/jvm/functions/Function3;",
                "Z", "Li3/r;", "I", "I",
            ),
        ).matchSingle().method.returnEarly()
    }
}

/** App-owned advertisement popup, not the genuine visit-approval UI. */
@Suppress("unused")
val hidePromotionDialogsPatch = bytecodePatch(
    name = "NoBrokerHood: Hide promotional popups",
    description = "Suppress app-level marketing dialogs (3.6.11).",
    default = true,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        Fingerprint(
            definingClass = "Lcom/app/nobrokerhood/activities/ya;",
            name = "showAdvertisement",
            returnType = "V",
            parameters = listOf(
                "Lcom/app/nobrokerhood/newnobrokerhood/promotions/data/PromotionsResponse;"
            ),
        ).matchSingle().method.returnEarly()
    }
}

/**
 * EXPERIMENTAL: Hide the ad card shown AFTER visitor approval/pre-approval.
 *
 * Traced in 3.6.11:
 * PreApprovalBannerActivityNew.R1 obtains
 * PostApprovalCardResponse.getTickets().firstOrNull() and passes the
 * optional card to PreApprovalBannerScreen. That screen checks for null
 * before rendering the ad card. Returning an empty list hides that card
 * without skipping the separate success/visitor UI or approval action.
 *
 * This does not modify push notifications, token registration or the
 * actual approval request. Runtime validation is still required.
 */
@Suppress("unused")
val hidePostApprovalAdsPatch = bytecodePatch(
    name = "NoBrokerHood: Hide post-approval ad card (experimental)",
    description = "Hide the sponsored card shown after entry approval or pre-approval (3.6.11).",
    default = false,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        Fingerprint(
            definingClass =
                "Lcom/app/nobrokerhood/newnobrokerhood/postApprovalCard/data/model/PostApprovalCardResponse;",
            name = "getTickets",
            returnType = "Ljava/util/List;",
            parameters = emptyList(),
        ).matchSingle().method.addInstructions(
            0,
            """
            invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;
            move-result-object v0
            return-object v0
            """.trimIndent(),
        )
    }
}

/**
 * EXPERIMENTAL top homepage ad state. Return an empty ad list rather than null;
 * also stop displaying the skeleton/loading and expansion state.
 * The surrounding layout may still reserve space; runtime testing required.
 */
@Suppress("unused")
val hideHomepageTopAdsPatch = bytecodePatch(
    name = "NoBrokerHood: Hide homepage top ads (experimental)",
    description = "Supply an empty top-ad list and disable its loading/expanded state.",
    default = false,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        val model = "Lcom/app/nobrokerhood/features/homepage/data/remote/models/TopAdState;"
        Fingerprint(
            definingClass = model,
            name = "getList",
            returnType = "Ljava/util/List;",
            parameters = emptyList(),
        ).matchSingle().method.addInstructions(
            0,
            """
            invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;
            move-result-object v0
            return-object v0
            """.trimIndent(),
        )
        Fingerprint(
            definingClass = model,
            name = "isLoading",
            returnType = "Z",
            parameters = emptyList(),
        ).matchSingle().method.returnEarly(false)
        Fingerprint(
            definingClass = model,
            name = "isExpanded",
            returnType = "Z",
            parameters = emptyList(),
        ).matchSingle().method.returnEarly(false)
    }
}

/**
 * EXPERIMENTAL: secondary app-bar promotional banner state.
 * Only modifies the advertising model; not push, FCM or entry/exit logic.
 */
@Suppress("unused")
val hideAppBarBannersPatch = bytecodePatch(
    name = "NoBrokerHood: Hide app-bar banners (experimental)",
    description = "Empty app-bar banner entries; may leave a layout placeholder.",
    default = false,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        val model = "Lcom/app/nobrokerhood/features/app_bar_banner/data/models/BannerState;"
        Fingerprint(
            definingClass = model,
            name = "getList",
            returnType = "Ljava/util/List;",
            parameters = emptyList(),
        ).matchSingle().method.addInstructions(
            0,
            """
            invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;
            move-result-object v0
            return-object v0
            """.trimIndent(),
        )
        Fingerprint(
            definingClass = model,
            name = "isLoading",
            returnType = "Z",
            parameters = emptyList(),
        ).matchSingle().method.returnEarly(false)
        Fingerprint(
            definingClass = model,
            name = "isExpanded",
            returnType = "Z",
            parameters = emptyList(),
        ).matchSingle().method.returnEarly(false)
    }
}


/**
 * EXPERIMENTAL: Remove the large image from expanded Android notifications.
 *
 * In 3.6.11, DoorFirebaseMessagingService.J reads the "bigImageUrl" payload
 * and calls U(...) to build a NotificationCompat Style. U's THIRD String
 * argument (p3) controls its image-loading/BigPictureStyle branch; when empty,
 * the existing function falls back to BigTextStyle.
 *
 * This patch clears only that third String argument (p3) at the start of U(...),
 * allowing the original code to construct and post its notification.
 * It does NOT block incoming Firebase messages, modify registration tokens,
 * disable any notification channel, or suppress the notification builder.
 *
 * LIMITATION: Any other notification using U(...) to show a legitimate
 * large image will also lose its expanded picture. Notification text and
 * delivery still need testing on a real device.
 */
@Suppress("unused")
val hideExpandedNotificationImagesPatch = bytecodePatch(
    name = "NoBrokerHood: Hide expanded notification images (experimental)",
    description = "Hide large banner images in entry/exit notifications while retaining text (3.6.11).",
    default = false,
) {
    compatibleWith(NOBROKERHOOD_3611)
    execute {
        Fingerprint(
            definingClass =
                "Lcom/app/nobrokerhood/notifications/DoorFirebaseMessagingService;",
            name = "U",
            returnType = "Landroidx/core/app/o\$j;",
            parameters = listOf(
                "Ljava/lang/String;",
                "Ljava/lang/String;",
                "Ljava/lang/String;",
                "Z",
                "Ljava/lang/String;",
            ),
        ).matchSingle().method.addInstructions(0, "const-string p3, \"\"")
    }
}
