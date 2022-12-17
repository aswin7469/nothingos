package com.android.settings.print;

import android.content.Context;
import android.content.IntentFilter;
import android.print.PrintJob;
import android.text.TextUtils;

public class PrintJobMessagePreferenceController extends PrintJobPreferenceControllerBase {
    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PrintJobMessagePreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public void updateUi() {
        PrintJob printJob = getPrintJob();
        if (printJob == null) {
            this.mFragment.finish();
        } else if (printJob.isCancelled() || printJob.isCompleted()) {
            this.mFragment.finish();
        } else {
            CharSequence status = printJob.getInfo().getStatus(this.mContext.getPackageManager());
            this.mPreference.setVisible(!TextUtils.isEmpty(status));
            this.mPreference.setSummary(status);
        }
    }
}
