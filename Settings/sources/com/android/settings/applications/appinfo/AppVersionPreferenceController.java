package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.text.BidiFormatter;
import com.android.settings.R$string;

public class AppVersionPreferenceController extends AppInfoPreferenceControllerBase {
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

    public AppVersionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        PackageInfo packageInfo = this.mParent.getPackageInfo();
        if (packageInfo == null) {
            return null;
        }
        return this.mContext.getString(R$string.version_text, new Object[]{BidiFormatter.getInstance().unicodeWrap(packageInfo.versionName)});
    }
}
