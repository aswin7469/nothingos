package com.nothing.systemui.p024qs;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.nothing.systemui.util.NTLogUtil;
import java.util.concurrent.Executor;

/* renamed from: com.nothing.systemui.qs.ForegroundServiceManagerDialog */
public class ForegroundServiceManagerDialog extends SystemUIDialog {
    private static final int ACTIVE_APPS_MAX_DISPLAY_COUNT = 4;
    private static final String TAG = "FGSManagerDialog";
    private int mAppsCount;
    private Context mContext;
    private int mLastOrientation = 0;
    private Executor mMainExecutor;

    public ForegroundServiceManagerDialog(Executor executor, int i, Context context) {
        super(context);
        this.mContext = context;
        this.mMainExecutor = executor;
        this.mAppsCount = i;
        this.mLastOrientation = context.getResources().getConfiguration().orientation;
        getWindow().setBackgroundDrawable(this.mContext.getResources().getDrawable(C1894R.C1896drawable.nt_fgs_dialog_background));
        NTLogUtil.m1686d(TAG, "Init ForegroundServiceManagerDialog appsCount: " + this.mAppsCount);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NTLogUtil.m1686d(TAG, "onCreate");
        mo57629x400f6ae();
    }

    public void onStart() {
        super.onStart();
        NTLogUtil.m1686d(TAG, "onStart");
    }

    public void onStop() {
        super.onStop();
        NTLogUtil.m1686d(TAG, "onStop");
    }

    public void onConfigurationChanged(Configuration configuration) {
        NTLogUtil.m1686d(TAG, "onConfigurationChanged");
        int i = this.mContext.getResources().getConfiguration().orientation;
        if (this.mLastOrientation != i) {
            this.mMainExecutor.execute(new ForegroundServiceManagerDialog$$ExternalSyntheticLambda0(this));
            this.mLastOrientation = i;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWindowSize */
    public void mo57629x400f6ae() {
        int integer = this.mContext.getResources().getInteger(C1894R.integer.fgs_scroll_enabled_apps_count);
        getWindow().setLayout(this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.settings_panel_width), this.mAppsCount > integer ? this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.fgs_dialog_height) : -2);
        NTLogUtil.m1686d(TAG, "updateWindowSize scrollEnabledCount: " + integer);
    }
}
