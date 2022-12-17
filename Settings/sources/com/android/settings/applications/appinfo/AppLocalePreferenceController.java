package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.AppLocaleUtil;
import com.android.settings.localepicker.AppLocalePickerActivity;
import java.util.List;

public class AppLocalePreferenceController extends AppInfoPreferenceControllerBase {
    private static final String TAG = "AppLocalePreferenceController";
    private final List<ResolveInfo> mListInfos;

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

    public AppLocalePreferenceController(Context context, String str) {
        super(context, str);
        this.mListInfos = context.getPackageManager().queryIntentActivities(AppLocaleUtil.LAUNCHER_ENTRY_INTENT, 128);
    }

    public int getAvailabilityStatus() {
        return (!FeatureFlagUtils.isEnabled(this.mContext, "settings_app_language_selection") || !canDisplayLocaleUi()) ? 2 : 0;
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return AppLocaleDetails.class;
    }

    public CharSequence getSummary() {
        return AppLocaleDetails.getSummary(this.mContext, this.mParent.getAppEntry());
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), this.mPreferenceKey)) {
            return false;
        }
        if (this.mParent != null) {
            Intent intent = new Intent(this.mContext, AppLocalePickerActivity.class);
            intent.setData(Uri.parse("package:" + this.mParent.getAppEntry().info.packageName));
            intent.putExtra("uid", this.mParent.getAppEntry().info.uid);
            this.mContext.startActivity(intent);
            return true;
        }
        Log.d(TAG, "mParent is null");
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean canDisplayLocaleUi() {
        return AppLocaleUtil.canDisplayLocaleUi(this.mContext, this.mParent.getAppEntry().info.packageName, this.mListInfos);
    }
}
