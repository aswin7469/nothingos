package com.android.systemui.screenshot;

import com.android.internal.logging.UiEventLogger;
/* loaded from: classes.dex */
public enum ScreenshotEvent implements UiEventLogger.UiEventEnum {
    SCREENSHOT_REQUESTED_GLOBAL_ACTIONS(302),
    SCREENSHOT_REQUESTED_KEY_CHORD(303),
    SCREENSHOT_REQUESTED_KEY_OTHER(384),
    SCREENSHOT_REQUESTED_OVERVIEW(304),
    SCREENSHOT_REQUESTED_ACCESSIBILITY_ACTIONS(382),
    SCREENSHOT_REQUESTED_VENDOR_GESTURE(638),
    SCREENSHOT_REQUESTED_OTHER(305),
    SCREENSHOT_SAVED(306),
    SCREENSHOT_NOT_SAVED(336),
    SCREENSHOT_PREVIEW_TAPPED(307),
    SCREENSHOT_EDIT_TAPPED(308),
    SCREENSHOT_SHARE_TAPPED(309),
    SCREENSHOT_SMART_ACTION_TAPPED(374),
    SCREENSHOT_SCROLL_TAPPED(373),
    SCREENSHOT_INTERACTION_TIMEOUT(310),
    SCREENSHOT_EXPLICIT_DISMISSAL(311),
    SCREENSHOT_SWIPE_DISMISSED(656),
    SCREENSHOT_REENTERED(640),
    SCREENSHOT_LONG_SCREENSHOT_IMPRESSION(687),
    SCREENSHOT_LONG_SCREENSHOT_REQUESTED(688),
    SCREENSHOT_LONG_SCREENSHOT_SHARE(689),
    SCREENSHOT_LONG_SCREENSHOT_EDIT(690),
    SCREENSHOT_LONG_SCREENSHOT_STARTED(880),
    SCREENSHOT_LONG_SCREENSHOT_FAILURE(881),
    SCREENSHOT_LONG_SCREENSHOT_COMPLETED(882),
    SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_STARTED(889),
    SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_CACHED_IMAGE_LOADED(890),
    SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_FINISHED(891);
    
    private final int mId;

    ScreenshotEvent(int i) {
        this.mId = i;
    }

    public int getId() {
        return this.mId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ScreenshotEvent getScreenshotSource(int i) {
        if (i != 0) {
            if (i == 1) {
                return SCREENSHOT_REQUESTED_KEY_CHORD;
            }
            if (i == 2) {
                return SCREENSHOT_REQUESTED_KEY_OTHER;
            }
            if (i == 3) {
                return SCREENSHOT_REQUESTED_OVERVIEW;
            }
            if (i == 4) {
                return SCREENSHOT_REQUESTED_ACCESSIBILITY_ACTIONS;
            }
            if (i == 6) {
                return SCREENSHOT_REQUESTED_VENDOR_GESTURE;
            }
            return SCREENSHOT_REQUESTED_OTHER;
        }
        return SCREENSHOT_REQUESTED_GLOBAL_ACTIONS;
    }
}
