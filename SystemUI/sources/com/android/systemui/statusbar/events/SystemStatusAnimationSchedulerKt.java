package com.android.systemui.statusbar.events;

import android.view.animation.PathInterpolator;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0010\u0010\r\u001a\u00020\u000e8\u0006X\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u000f\u001a\u00020\u000e8\u0006X\u0004¢\u0006\u0002\n\u0000\"\u0011\u0010\u0010\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0011\u0010\u0013\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012\"\u0011\u0010\u0015\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0011\u0010\u0017\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012\"\u0011\u0010\u0019\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012\"\u000e\u0010\u001b\u001a\u00020\u001cXT¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo65043d2 = {"ANIMATING_IN", "", "ANIMATING_OUT", "ANIMATION_QUEUED", "DEBOUNCE_DELAY", "", "DEBUG", "", "DISPLAY_LENGTH", "IDLE", "MIN_UPTIME", "RUNNING_CHIP_ANIM", "SHOWING_PERSISTENT_DOT", "STATUS_BAR_X_MOVE_IN", "Landroid/view/animation/PathInterpolator;", "STATUS_BAR_X_MOVE_OUT", "STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1", "getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1", "()Landroid/view/animation/PathInterpolator;", "STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2", "getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2", "STATUS_CHIP_MOVE_TO_DOT", "getSTATUS_CHIP_MOVE_TO_DOT", "STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1", "getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1", "STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2", "getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationSchedulerKt {
    public static final int ANIMATING_IN = 2;
    public static final int ANIMATING_OUT = 4;
    public static final int ANIMATION_QUEUED = 1;
    private static final long DEBOUNCE_DELAY = 100;
    private static final boolean DEBUG = false;
    private static final long DISPLAY_LENGTH = 1000;
    public static final int IDLE = 0;
    private static final long MIN_UPTIME = 5000;
    public static final int RUNNING_CHIP_ANIM = 3;
    public static final int SHOWING_PERSISTENT_DOT = 5;
    public static final PathInterpolator STATUS_BAR_X_MOVE_IN = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    public static final PathInterpolator STATUS_BAR_X_MOVE_OUT = new PathInterpolator(0.33f, 0.0f, 0.0f, 1.0f);
    private static final PathInterpolator STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1 = new PathInterpolator(0.4f, 0.0f, 0.17f, 1.0f);
    private static final PathInterpolator STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2 = new PathInterpolator(0.3f, 0.0f, 0.0f, 1.0f);
    private static final PathInterpolator STATUS_CHIP_MOVE_TO_DOT = new PathInterpolator(0.0f, 0.0f, 0.05f, 1.0f);
    private static final PathInterpolator STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1 = new PathInterpolator(0.44f, 0.0f, 0.25f, 1.0f);
    private static final PathInterpolator STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2 = new PathInterpolator(0.3f, 0.0f, 0.26f, 1.0f);
    private static final String TAG = "SystemStatusAnimationScheduler";

    public static final PathInterpolator getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1() {
        return STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1;
    }

    public static final PathInterpolator getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2() {
        return STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2;
    }

    public static final PathInterpolator getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1() {
        return STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1;
    }

    public static final PathInterpolator getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2() {
        return STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2;
    }

    public static final PathInterpolator getSTATUS_CHIP_MOVE_TO_DOT() {
        return STATUS_CHIP_MOVE_TO_DOT;
    }
}
