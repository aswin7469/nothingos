package com.android.p019wm.shell.hidedisplaycutout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemProperties;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController */
public class HideDisplayCutoutController {
    private static final String TAG = "HideDisplayCutoutController";
    private final Context mContext;
    boolean mEnabled;
    private final HideDisplayCutoutImpl mImpl = new HideDisplayCutoutImpl();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final HideDisplayCutoutOrganizer mOrganizer;

    public static HideDisplayCutoutController create(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        if (!SystemProperties.getBoolean("ro.support_hide_display_cutout", false)) {
            return null;
        }
        return new HideDisplayCutoutController(context, new HideDisplayCutoutOrganizer(context, displayController, shellExecutor), shellExecutor);
    }

    HideDisplayCutoutController(Context context, HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mOrganizer = hideDisplayCutoutOrganizer;
        this.mMainExecutor = shellExecutor;
        updateStatus();
    }

    public HideDisplayCutout asHideDisplayCutout() {
        return this.mImpl;
    }

    /* access modifiers changed from: package-private */
    public void updateStatus() {
        boolean z = this.mContext.getResources().getBoolean(17891680);
        if (z != this.mEnabled) {
            this.mEnabled = z;
            if (z) {
                this.mOrganizer.enableHideDisplayCutout();
            } else {
                this.mOrganizer.disableHideDisplayCutout();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConfigurationChanged(Configuration configuration) {
        updateStatus();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print(TAG);
        printWriter.println(" states: ");
        printWriter.print("  ");
        printWriter.print("mEnabled=");
        printWriter.println(this.mEnabled);
        this.mOrganizer.dump(printWriter);
    }

    /* renamed from: com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController$HideDisplayCutoutImpl */
    private class HideDisplayCutoutImpl implements HideDisplayCutout {
        private HideDisplayCutoutImpl() {
        }

        public void onConfigurationChanged(Configuration configuration) {
            HideDisplayCutoutController.this.mMainExecutor.execute(new C3473x58c373ff(this, configuration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConfigurationChanged$0$com-android-wm-shell-hidedisplaycutout-HideDisplayCutoutController$HideDisplayCutoutImpl */
        public /* synthetic */ void mo49579x72f8b0c3(Configuration configuration) {
            HideDisplayCutoutController.this.onConfigurationChanged(configuration);
        }
    }
}
