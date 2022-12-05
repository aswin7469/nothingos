package com.android.wm.shell.hidedisplaycutout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemProperties;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class HideDisplayCutoutController {
    private final Context mContext;
    boolean mEnabled;
    private final HideDisplayCutoutImpl mImpl = new HideDisplayCutoutImpl();
    private final ShellExecutor mMainExecutor;
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

    void updateStatus() {
        boolean z = this.mContext.getResources().getBoolean(17891575);
        if (z == this.mEnabled) {
            return;
        }
        this.mEnabled = z;
        if (z) {
            this.mOrganizer.enableHideDisplayCutout();
        } else {
            this.mOrganizer.disableHideDisplayCutout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigurationChanged(Configuration configuration) {
        updateStatus();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("HideDisplayCutoutController");
        printWriter.println(" states: ");
        printWriter.print("  ");
        printWriter.print("mEnabled=");
        printWriter.println(this.mEnabled);
        this.mOrganizer.dump(printWriter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class HideDisplayCutoutImpl implements HideDisplayCutout {
        private HideDisplayCutoutImpl() {
        }

        @Override // com.android.wm.shell.hidedisplaycutout.HideDisplayCutout
        public void onConfigurationChanged(final Configuration configuration) {
            HideDisplayCutoutController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController$HideDisplayCutoutImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HideDisplayCutoutController.HideDisplayCutoutImpl.this.lambda$onConfigurationChanged$0(configuration);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$0(Configuration configuration) {
            HideDisplayCutoutController.this.onConfigurationChanged(configuration);
        }
    }
}
