package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.concurrent.Executor;

@ExternalThread
/* renamed from: com.android.wm.shell.splitscreen.SplitScreen */
public interface SplitScreen {
    public static final int STAGE_TYPE_MAIN = 0;
    public static final int STAGE_TYPE_SIDE = 1;
    public static final int STAGE_TYPE_UNDEFINED = -1;

    /* renamed from: com.android.wm.shell.splitscreen.SplitScreen$SplitScreenListener */
    public interface SplitScreenListener {
        void onSplitVisibilityChanged(boolean z) {
        }

        void onStagePositionChanged(int i, int i2) {
        }

        void onTaskStageChanged(int i, int i2, boolean z) {
        }
    }

    /* renamed from: com.android.wm.shell.splitscreen.SplitScreen$StageType */
    public @interface StageType {
    }

    ISplitScreen createExternalInterface() {
        return null;
    }

    void onFinishedWakingUp();

    void onKeyguardVisibilityChanged(boolean z);

    void registerSplitScreenListener(SplitScreenListener splitScreenListener, Executor executor);

    void unregisterSplitScreenListener(SplitScreenListener splitScreenListener);

    static String stageTypeToString(int i) {
        if (i == -1) {
            return "UNDEFINED";
        }
        if (i != 0) {
            return i != 1 ? "UNKNOWN(" + i + NavigationBarInflaterView.KEY_CODE_END : "SIDE";
        }
        return "MAIN";
    }
}
