package com.android.settings.privacy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.dashboard.profileselector.ProfileSelectDialog;
import com.android.settings.utils.ContentCaptureUtils;
import java.util.ArrayList;
import java.util.List;

public final class EnableContentCaptureWithServiceSettingsPreferenceController extends TogglePreferenceController {
    private static final String TAG = "ContentCaptureController";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EnableContentCaptureWithServiceSettingsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return ContentCaptureUtils.isEnabledForUser(this.mContext);
    }

    public boolean setChecked(boolean z) {
        ContentCaptureUtils.setEnabledForUser(this.mContext, z);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ComponentName serviceSettingsComponentName = ContentCaptureUtils.getServiceSettingsComponentName();
        if (serviceSettingsComponentName != null) {
            preference.setIntent(new Intent("android.intent.action.MAIN").setComponent(serviceSettingsComponentName));
            return;
        }
        Log.w(TAG, "No component name for custom service settings");
        preference.setSelectable(false);
    }

    public int getAvailabilityStatus() {
        if (ContentCaptureUtils.isFeatureAvailable() && ContentCaptureUtils.getServiceSettingsComponentName() != null) {
            return 0;
        }
        return 3;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_privacy;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        show(preference);
        return true;
    }

    private void show(Preference preference) {
        List<UserInfo> users = UserManager.get(this.mContext).getUsers();
        ArrayList arrayList = new ArrayList(users.size());
        for (UserInfo userHandle : users) {
            arrayList.add(userHandle.getUserHandle());
        }
        Intent addFlags = preference.getIntent().addFlags(32768);
        if (arrayList.size() == 1) {
            this.mContext.startActivityAsUser(addFlags, (UserHandle) arrayList.get(0));
        } else {
            ProfileSelectDialog.createDialog(this.mContext, arrayList, new C1315xa5b4eea8(this, addFlags, arrayList)).show();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$show$0(Intent intent, ArrayList arrayList, int i) {
        this.mContext.startActivityAsUser(intent, (UserHandle) arrayList.get(i));
    }
}
