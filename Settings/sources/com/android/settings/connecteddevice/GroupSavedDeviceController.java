package com.android.settings.connecteddevice;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.bluetooth.GroupBluetoothSettingsPreference;
import com.android.settings.bluetooth.GroupSavedBluetoothDeviceUpdater;
import com.android.settings.bluetooth.GroupUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.GearPreference;
import com.android.settings.widget.GroupPreferenceCategory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GroupSavedDeviceController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback, GearPreference.OnGearClickListener {
    private static final String KEY = "group_saved_device_list";
    private static final String KEY_PCG_EIGHT = "pcg_eight";
    private static final String KEY_PCG_FIVE = "pcg_five";
    private static final String KEY_PCG_FOUR = "pcg_four";
    private static final String KEY_PCG_NINE = "pcg_nine";
    private static final String KEY_PCG_ONE = "pcg_one";
    private static final String KEY_PCG_REMAINING = "pcg_remaining";
    private static final String KEY_PCG_SEVEN = "pcg_seven";
    private static final String KEY_PCG_SIX = "pcg_six";
    private static final String KEY_PCG_THREE = "pcg_three";
    private static final String KEY_PCG_TWO = "pcg_two";
    private static final String TAG = "GroupSavedDeviceController";
    private GroupSavedBluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private GroupBluetoothSettingsPreference mGroupPreference;
    private GroupUtils mGroupUtils;
    private ArrayList<GroupPreferenceCategory> mListCategories = new ArrayList<>();
    private PreferenceGroup mPreferenceGroup;
    GroupPreferenceCategory mPreferenceGroupRemaining;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GroupSavedDeviceController(Context context) {
        super(context, KEY);
        FeatureFactory.getFactory(context).getDockUpdaterFeatureProvider();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mBluetoothDeviceUpdater.registerCallback();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mBluetoothDeviceUpdater.unregisterCallback();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(KEY);
        this.mPreferenceGroup = preferenceGroup;
        preferenceGroup.setVisible(false);
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_ONE));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_TWO));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_THREE));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_FOUR));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_FIVE));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_SIX));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_SEVEN));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_EIGHT));
        this.mListCategories.add((GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_NINE));
        GroupPreferenceCategory groupPreferenceCategory = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_PCG_REMAINING);
        this.mPreferenceGroupRemaining = groupPreferenceCategory;
        groupPreferenceCategory.setGroupId(-99);
        this.mListCategories.add(this.mPreferenceGroupRemaining);
        if (isAvailable()) {
            Context context = preferenceScreen.getContext();
            this.mGroupUtils = new GroupUtils(context);
            this.mBluetoothDeviceUpdater.setPrefContext(context);
            this.mBluetoothDeviceUpdater.forceUpdate();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 0 : 3;
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceAdded(Preference preference) {
        this.mGroupUtils.addPreference(this.mListCategories, preference, this);
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceRemoved(Preference preference) {
        this.mGroupUtils.removePreference(this.mListCategories, preference);
    }

    public void init(DashboardFragment dashboardFragment) {
        this.mBluetoothDeviceUpdater = new GroupSavedBluetoothDeviceUpdater(dashboardFragment.getContext(), dashboardFragment, this);
    }

    @Override // com.android.settings.widget.GearPreference.OnGearClickListener
    public void onGearClick(GearPreference gearPreference) {
        this.mBluetoothDeviceUpdater.launchgroupOptions((GroupBluetoothSettingsPreference) gearPreference);
    }
}