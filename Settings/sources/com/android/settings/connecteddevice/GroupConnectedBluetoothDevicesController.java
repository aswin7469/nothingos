package com.android.settings.connecteddevice;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.bluetooth.GroupBluetoothSettingsPreference;
import com.android.settings.bluetooth.GroupConnectedBluetoothDeviceUpdater;
import com.android.settings.bluetooth.GroupUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.GearPreference;
import com.android.settings.widget.GroupPreferenceCategory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GroupConnectedBluetoothDevicesController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback, GearPreference.OnGearClickListener {
    private static final String KEY_EIGHT = "group_eight";
    private static final String KEY_FIVE = "group_five";
    private static final String KEY_FOUR = "group_four";
    private static final String KEY_GROUP = "group_connected_device_list";
    private static final String KEY_NINE = "group_nine";
    private static final String KEY_ONE = "group_one";
    private static final String KEY_REMAINING = "group_remaining";
    private static final String KEY_SEVEN = "group_seven";
    private static final String KEY_SIX = "group_six";
    private static final String KEY_THREE = "group_three";
    private static final String KEY_TWO = "group_two";
    private static final String TAG = "GroupConnectedBluetoothDevicesController";
    private GroupConnectedBluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private ArrayList<GroupPreferenceCategory> mGroupList = new ArrayList<>();
    private GroupBluetoothSettingsPreference mGroupSettings;
    private GroupUtils mGroupUtils;
    private Context mPerfCtx;
    private PreferenceGroup mPreferenceGroup;
    private GroupPreferenceCategory mPreferenceGroupEight;
    private GroupPreferenceCategory mPreferenceGroupFive;
    private GroupPreferenceCategory mPreferenceGroupFour;
    private GroupPreferenceCategory mPreferenceGroupNine;
    private GroupPreferenceCategory mPreferenceGroupOne;
    private GroupPreferenceCategory mPreferenceGroupRemaining;
    private GroupPreferenceCategory mPreferenceGroupSeven;
    private GroupPreferenceCategory mPreferenceGroupSix;
    private GroupPreferenceCategory mPreferenceGroupThree;
    private GroupPreferenceCategory mPreferenceGroupTwo;
    private Preference mPreferenceSeeAll;

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
        return KEY_ONE;
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

    public GroupConnectedBluetoothDevicesController(Context context) {
        super(context, KEY_ONE);
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
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(KEY_GROUP);
        this.mPreferenceGroupOne = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_ONE);
        this.mPreferenceGroupTwo = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_TWO);
        this.mPreferenceGroupThree = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_THREE);
        this.mPreferenceGroupFour = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_FOUR);
        this.mPreferenceGroupFive = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_FIVE);
        this.mPreferenceGroupSix = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_SIX);
        this.mPreferenceGroupSeven = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_SEVEN);
        this.mPreferenceGroupEight = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_EIGHT);
        this.mPreferenceGroupNine = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_NINE);
        this.mPreferenceGroupRemaining = (GroupPreferenceCategory) preferenceScreen.findPreference(KEY_REMAINING);
        this.mGroupList.add(this.mPreferenceGroupOne);
        this.mGroupList.add(this.mPreferenceGroupTwo);
        this.mGroupList.add(this.mPreferenceGroupThree);
        this.mGroupList.add(this.mPreferenceGroupFour);
        this.mGroupList.add(this.mPreferenceGroupFive);
        this.mGroupList.add(this.mPreferenceGroupSix);
        this.mGroupList.add(this.mPreferenceGroupSeven);
        this.mGroupList.add(this.mPreferenceGroupEight);
        this.mGroupList.add(this.mPreferenceGroupNine);
        this.mGroupList.add(this.mPreferenceGroupRemaining);
        if (isAvailable()) {
            Context context = preferenceScreen.getContext();
            this.mGroupUtils = new GroupUtils(context);
            this.mBluetoothDeviceUpdater.setPrefContext(context);
            this.mBluetoothDeviceUpdater.forceUpdate();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 1 : 3;
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceAdded(Preference preference) {
        this.mGroupUtils.addPreference(this.mGroupList, preference, this);
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceRemoved(Preference preference) {
        this.mGroupUtils.removePreference(this.mGroupList, preference);
    }

    public void init(DashboardFragment dashboardFragment) {
        this.mBluetoothDeviceUpdater = new GroupConnectedBluetoothDeviceUpdater(dashboardFragment.getContext(), dashboardFragment, this);
    }

    @Override // com.android.settings.widget.GearPreference.OnGearClickListener
    public void onGearClick(GearPreference gearPreference) {
        this.mBluetoothDeviceUpdater.launchgroupOptions((GroupBluetoothSettingsPreference) gearPreference);
    }
}
