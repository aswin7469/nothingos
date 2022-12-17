package com.android.settings.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.os.UserHandle;
import android.view.accessibility.AccessibilityManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.net.module.util.CollectionUtils;
import com.android.settings.accessibility.RestrictedPreferenceHelper;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BluetoothDetailsRelatedToolsController extends BluetoothDetailsController {
    private PreferenceCategory mPreferenceCategory;

    public String getPreferenceKey() {
        return "bluetooth_related_tools";
    }

    /* access modifiers changed from: protected */
    public void refresh() {
    }

    public BluetoothDetailsRelatedToolsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        lifecycle.addObserver(this);
    }

    public boolean isAvailable() {
        return this.mCachedDevice.isHearingAidDevice();
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        if (this.mCachedDevice.isHearingAidDevice()) {
            this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
            Preference findPreference = preferenceScreen.findPreference("live_caption");
            if (!findPreference.isVisible()) {
                this.mPreferenceCategory.removePreference(findPreference);
            }
            List<ComponentName> relatedTools = FeatureFactory.getFactory(this.mContext).getBluetoothFeatureProvider().getRelatedTools();
            if (!CollectionUtils.isEmpty(relatedTools)) {
                addAccessibilityInstalledRelatedPreference(relatedTools);
            }
            if (this.mPreferenceCategory.getPreferenceCount() == 0) {
                preferenceScreen.removePreference(this.mPreferenceCategory);
            }
        }
    }

    private void addAccessibilityInstalledRelatedPreference(List<ComponentName> list) {
        AccessibilityManager instance = AccessibilityManager.getInstance(this.mContext);
        RestrictedPreferenceHelper restrictedPreferenceHelper = new RestrictedPreferenceHelper(this.mContext);
        for (RestrictedPreference restrictedPreference : (List) Stream.of(new List[]{restrictedPreferenceHelper.createAccessibilityServicePreferenceList((List) instance.getInstalledAccessibilityServiceList().stream().filter(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda0(list)).collect(Collectors.toList())), restrictedPreferenceHelper.createAccessibilityActivityPreferenceList((List) instance.getInstalledAccessibilityShortcutListAsUser(this.mContext, UserHandle.myUserId()).stream().filter(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda1(list)).collect(Collectors.toList()))}).flatMap(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2()).collect(Collectors.toList())) {
            restrictedPreference.setOrder(99);
            this.mPreferenceCategory.addPreference(restrictedPreference);
        }
    }
}
