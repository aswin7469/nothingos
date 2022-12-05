package com.android.systemui.statusbar.phone;

import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserManager;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.telecom.TelecomManager;
import android.text.format.DateFormat;
import android.util.Log;
import androidx.lifecycle.Observer;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.time.DateFormatUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class PhoneStatusBarPolicy implements BluetoothController.Callback, CommandQueue.Callbacks, RotationLockController.RotationLockControllerCallback, DataSaverController.Listener, ZenModeController.Callback, DeviceProvisionedController.DeviceProvisionedListener, KeyguardStateController.Callback, PrivacyItemController.Callback, LocationController.LocationChangeCallback, RecordingController.RecordingStateChangeCallback {
    private static final boolean DEBUG = Log.isLoggable("PhoneStatusBarPolicy", 3);
    static final int LOCATION_STATUS_ICON_ID = PrivacyType.TYPE_LOCATION.getIconId();
    private static final String TESLA_MAC_ADDRESS = "4C:FC:AA".toLowerCase();
    private final AlarmManager mAlarmManager;
    private BluetoothController mBluetooth;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CastController mCast;
    private final CommandQueue mCommandQueue;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private boolean mCurrentUserSetup;
    private final DataSaverController mDataSaver;
    private final DateFormatUtil mDateFormatUtil;
    private final int mDisplayId;
    private final HotspotController mHotspot;
    private final IActivityManager mIActivityManager;
    private final StatusBarIconController mIconController;
    private final KeyguardStateController mKeyguardStateController;
    private final LocationController mLocationController;
    private boolean mMuteVisible;
    private AlarmManager.AlarmClockInfo mNextAlarm;
    private final NextAlarmController mNextAlarmController;
    private final PrivacyItemController mPrivacyItemController;
    private final PrivacyLogger mPrivacyLogger;
    private final DeviceProvisionedController mProvisionedController;
    private final RecordingController mRecordingController;
    private final Resources mResources;
    private final RingerModeTracker mRingerModeTracker;
    private final RotationLockController mRotationLockController;
    private final SensorPrivacyController mSensorPrivacyController;
    private final SharedPreferences mSharedPreferences;
    private final String mSlotAlarmClock;
    private final String mSlotBluetooth;
    private final String mSlotCamera;
    private final String mSlotCast;
    private final String mSlotDataSaver;
    private final String mSlotHeadset;
    private final String mSlotHotspot;
    private final String mSlotLocation;
    private final String mSlotManagedProfile;
    private final String mSlotMicrophone;
    private final String mSlotMute;
    private final String mSlotRotate;
    private final String mSlotScreenRecord;
    private final String mSlotSensorsOff;
    private final String mSlotTty;
    private final String mSlotVibrate;
    private final String mSlotZen;
    private final TelecomManager mTelecomManager;
    private final Executor mUiBgExecutor;
    private final UserInfoController mUserInfoController;
    private final UserManager mUserManager;
    private boolean mVibrateVisible;
    private final ZenModeController mZenController;
    private boolean mZenVisible;
    private final Handler mHandler = new Handler();
    private boolean mManagedProfileIconVisible = false;
    final ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback = new ConnectivityManager.OnStartTetheringCallback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.1
        public void onTetheringFailed() {
            super.onTetheringFailed();
        }
    };
    private final SynchronousUserSwitchObserver mUserSwitchListener = new AnonymousClass2();
    private final HotspotController.Callback mHotspotCallback = new HotspotController.Callback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.3
        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public void onHotspotChanged(boolean z, int i) {
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotHotspot, z);
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public void onHotspotChanged(boolean z, int i, int i2) {
            PhoneStatusBarPolicy.this.updateHotspotIcon(i2);
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotHotspot, z);
        }
    };
    private final CastController.Callback mCastCallback = new CastController.Callback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.4
        @Override // com.android.systemui.statusbar.policy.CastController.Callback
        public void onCastDevicesChanged() {
            PhoneStatusBarPolicy.this.updateCast();
        }
    };
    private final NextAlarmController.NextAlarmChangeCallback mNextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.5
        @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
        public void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            PhoneStatusBarPolicy.this.mNextAlarm = alarmClockInfo;
            PhoneStatusBarPolicy.this.updateAlarm();
        }
    };
    private final SensorPrivacyController.OnSensorPrivacyChangedListener mSensorPrivacyListener = new AnonymousClass6();
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.7
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1676458352:
                    if (action.equals("android.intent.action.HEADSET_PLUG")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1238404651:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        c = 1;
                        break;
                    }
                    break;
                case -864107122:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        c = 2;
                        break;
                    }
                    break;
                case -229777127:
                    if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1051344550:
                    if (action.equals("android.telecom.action.CURRENT_TTY_MODE_CHANGED")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1051477093:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    PhoneStatusBarPolicy.this.updateHeadsetPlug(intent);
                    return;
                case 1:
                case 2:
                case 5:
                    PhoneStatusBarPolicy.this.updateManagedProfile();
                    return;
                case 3:
                    intent.getBooleanExtra("rebroadcastOnUnlock", false);
                    return;
                case 4:
                    PhoneStatusBarPolicy.this.updateTTY(intent.getIntExtra("android.telecom.extra.CURRENT_TTY_MODE", 0));
                    return;
                default:
                    return;
            }
        }
    };
    private Runnable mRemoveCastIconRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.8
        @Override // java.lang.Runnable
        public void run() {
            if (PhoneStatusBarPolicy.DEBUG) {
                Log.v("PhoneStatusBarPolicy", "updateCast: hiding icon NOW");
            }
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotCast, false);
        }
    };

    public PhoneStatusBarPolicy(StatusBarIconController statusBarIconController, CommandQueue commandQueue, BroadcastDispatcher broadcastDispatcher, Executor executor, Resources resources, CastController castController, HotspotController hotspotController, BluetoothController bluetoothController, NextAlarmController nextAlarmController, UserInfoController userInfoController, RotationLockController rotationLockController, DataSaverController dataSaverController, ZenModeController zenModeController, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, LocationController locationController, SensorPrivacyController sensorPrivacyController, IActivityManager iActivityManager, AlarmManager alarmManager, UserManager userManager, RecordingController recordingController, TelecomManager telecomManager, int i, SharedPreferences sharedPreferences, DateFormatUtil dateFormatUtil, RingerModeTracker ringerModeTracker, PrivacyItemController privacyItemController, PrivacyLogger privacyLogger) {
        this.mIconController = statusBarIconController;
        this.mCommandQueue = commandQueue;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mResources = resources;
        this.mCast = castController;
        this.mHotspot = hotspotController;
        this.mBluetooth = bluetoothController;
        this.mNextAlarmController = nextAlarmController;
        this.mAlarmManager = alarmManager;
        this.mUserInfoController = userInfoController;
        this.mIActivityManager = iActivityManager;
        this.mUserManager = userManager;
        this.mRotationLockController = rotationLockController;
        this.mDataSaver = dataSaverController;
        this.mZenController = zenModeController;
        this.mProvisionedController = deviceProvisionedController;
        this.mKeyguardStateController = keyguardStateController;
        this.mLocationController = locationController;
        this.mPrivacyItemController = privacyItemController;
        this.mSensorPrivacyController = sensorPrivacyController;
        this.mRecordingController = recordingController;
        this.mUiBgExecutor = executor;
        this.mTelecomManager = telecomManager;
        this.mRingerModeTracker = ringerModeTracker;
        this.mPrivacyLogger = privacyLogger;
        this.mSlotCast = resources.getString(17041442);
        this.mSlotHotspot = resources.getString(17041449);
        this.mSlotBluetooth = resources.getString(17041439);
        this.mSlotTty = resources.getString(17041467);
        this.mSlotZen = resources.getString(17041471);
        this.mSlotMute = resources.getString(17041455);
        this.mSlotVibrate = resources.getString(17041468);
        this.mSlotAlarmClock = resources.getString(17041437);
        this.mSlotManagedProfile = resources.getString(17041452);
        this.mSlotRotate = resources.getString(17041460);
        this.mSlotHeadset = resources.getString(17041448);
        this.mSlotDataSaver = resources.getString(17041446);
        this.mSlotLocation = resources.getString(17041451);
        this.mSlotMicrophone = resources.getString(17041453);
        this.mSlotCamera = resources.getString(17041441);
        this.mSlotSensorsOff = resources.getString(17041463);
        this.mSlotScreenRecord = resources.getString(17041461);
        this.mDisplayId = i;
        this.mSharedPreferences = sharedPreferences;
        this.mDateFormatUtil = dateFormatUtil;
    }

    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.telecom.action.CURRENT_TTY_MODE_CHANGED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mIntentReceiver, intentFilter, this.mHandler);
        Observer<? super Integer> observer = new Observer() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                PhoneStatusBarPolicy.this.lambda$init$0((Integer) obj);
            }
        };
        this.mRingerModeTracker.getRingerMode().observeForever(observer);
        this.mRingerModeTracker.getRingerModeInternal().observeForever(observer);
        try {
            this.mIActivityManager.registerUserSwitchObserver(this.mUserSwitchListener, "PhoneStatusBarPolicy");
        } catch (RemoteException unused) {
        }
        updateTTY();
        updateBluetooth();
        this.mIconController.setIcon(this.mSlotAlarmClock, R$drawable.stat_sys_alarm, null);
        this.mIconController.setIconVisibility(this.mSlotAlarmClock, false);
        this.mIconController.setIcon(this.mSlotZen, R$drawable.stat_sys_dnd, null);
        this.mIconController.setIconVisibility(this.mSlotZen, false);
        this.mIconController.setIcon(this.mSlotVibrate, R$drawable.stat_sys_ringer_vibrate, this.mResources.getString(R$string.accessibility_ringer_vibrate));
        this.mIconController.setIconVisibility(this.mSlotVibrate, false);
        this.mIconController.setIcon(this.mSlotMute, R$drawable.stat_sys_ringer_silent, this.mResources.getString(R$string.accessibility_ringer_silent));
        this.mIconController.setIconVisibility(this.mSlotMute, false);
        updateVolumeZen();
        this.mIconController.setIcon(this.mSlotCast, R$drawable.stat_sys_cast, null);
        this.mIconController.setIconVisibility(this.mSlotCast, false);
        this.mIconController.setIcon(this.mSlotManagedProfile, R$drawable.stat_sys_managed_profile_status, this.mResources.getString(R$string.accessibility_managed_profile));
        this.mIconController.setIconVisibility(this.mSlotManagedProfile, this.mManagedProfileIconVisible);
        this.mIconController.setIcon(this.mSlotDataSaver, R$drawable.stat_sys_data_saver, this.mResources.getString(R$string.accessibility_data_saver_on));
        this.mIconController.setIconVisibility(this.mSlotDataSaver, false);
        Resources resources = this.mResources;
        PrivacyType privacyType = PrivacyType.TYPE_MICROPHONE;
        String string = resources.getString(privacyType.getNameId());
        Resources resources2 = this.mResources;
        int i = R$string.ongoing_privacy_chip_content_multiple_apps;
        this.mIconController.setIcon(this.mSlotMicrophone, privacyType.getIconId(), resources2.getString(i, string));
        this.mIconController.setIconVisibility(this.mSlotMicrophone, false);
        Resources resources3 = this.mResources;
        PrivacyType privacyType2 = PrivacyType.TYPE_CAMERA;
        this.mIconController.setIcon(this.mSlotCamera, privacyType2.getIconId(), this.mResources.getString(i, resources3.getString(privacyType2.getNameId())));
        this.mIconController.setIconVisibility(this.mSlotCamera, false);
        this.mIconController.setIcon(this.mSlotLocation, LOCATION_STATUS_ICON_ID, this.mResources.getString(R$string.accessibility_location_active));
        this.mIconController.setIconVisibility(this.mSlotLocation, false);
        this.mIconController.setIcon(this.mSlotSensorsOff, R$drawable.stat_sys_sensors_off, this.mResources.getString(R$string.accessibility_sensors_off_active));
        this.mIconController.setIconVisibility(this.mSlotSensorsOff, this.mSensorPrivacyController.isSensorPrivacyEnabled());
        this.mIconController.setIcon(this.mSlotScreenRecord, R$drawable.stat_sys_screen_record, null);
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, false);
        this.mRotationLockController.addCallback(this);
        this.mBluetooth.addCallback(this);
        this.mProvisionedController.addCallback(this);
        this.mZenController.addCallback(this);
        this.mCast.addCallback(this.mCastCallback);
        this.mHotspot.addCallback(this.mHotspotCallback);
        this.mNextAlarmController.addCallback(this.mNextAlarmCallback);
        this.mDataSaver.addCallback(this);
        this.mKeyguardStateController.addCallback(this);
        this.mPrivacyItemController.addCallback(this);
        this.mSensorPrivacyController.addCallback(this.mSensorPrivacyListener);
        this.mLocationController.addCallback(this);
        this.mRecordingController.addCallback((RecordingController.RecordingStateChangeCallback) this);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(Integer num) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.updateVolumeZen();
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public void onZenChanged(int i) {
        updateVolumeZen();
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public void onConfigChanged(ZenModeConfig zenModeConfig) {
        updateVolumeZen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(-2);
        boolean z = true;
        boolean z2 = nextAlarmClock != null && nextAlarmClock.getTriggerTime() > 0;
        this.mIconController.setIcon(this.mSlotAlarmClock, this.mZenController.getZen() == 2 ? R$drawable.stat_sys_alarm_dim : R$drawable.stat_sys_alarm, buildAlarmContentDescription());
        StatusBarIconController statusBarIconController = this.mIconController;
        String str = this.mSlotAlarmClock;
        if (!this.mCurrentUserSetup || !z2) {
            z = false;
        }
        statusBarIconController.setIconVisibility(str, z);
    }

    private String buildAlarmContentDescription() {
        if (this.mNextAlarm == null) {
            return this.mResources.getString(R$string.status_bar_alarm);
        }
        return this.mResources.getString(R$string.accessibility_quick_settings_alarm, DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mDateFormatUtil.is24HourFormat() ? "EHm" : "Ehma"), this.mNextAlarm.getTriggerTime()).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0073, code lost:
        if (r0.intValue() == 0) goto L16;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void updateVolumeZen() {
        boolean z;
        int i;
        String string;
        Integer mo1438getValue;
        int i2;
        String string2;
        int zen = this.mZenController.getZen();
        boolean z2 = false;
        boolean z3 = true;
        if (DndTile.isVisible(this.mSharedPreferences) || DndTile.isCombinedIcon(this.mSharedPreferences)) {
            z = zen != 0;
            i = R$drawable.stat_sys_dnd;
            string = this.mResources.getString(R$string.quick_settings_dnd_label);
        } else {
            if (zen == 2) {
                i2 = R$drawable.stat_sys_dnd;
                string2 = this.mResources.getString(R$string.interruption_level_none);
            } else if (zen == 1) {
                i2 = R$drawable.stat_sys_dnd;
                string2 = this.mResources.getString(R$string.interruption_level_priority);
            } else {
                z = false;
                string = null;
                i = 0;
            }
            string = string2;
            i = i2;
            z = true;
        }
        if (!ZenModeConfig.isZenOverridingRinger(zen, this.mZenController.getConsolidatedPolicy()) && (mo1438getValue = this.mRingerModeTracker.getRingerModeInternal().mo1438getValue()) != null) {
            if (mo1438getValue.intValue() == 1) {
                z3 = false;
                z2 = true;
            }
            if (z) {
                this.mIconController.setIcon(this.mSlotZen, i, string);
            }
            if (z != this.mZenVisible) {
                this.mIconController.setIconVisibility(this.mSlotZen, z);
                this.mZenVisible = z;
            }
            if (z2 != this.mVibrateVisible) {
                this.mIconController.setIconVisibility(this.mSlotVibrate, z2);
                this.mVibrateVisible = z2;
            }
            if (z3 != this.mMuteVisible) {
                this.mIconController.setIconVisibility(this.mSlotMute, z3);
                this.mMuteVisible = z3;
            }
            updateAlarm();
        }
        z3 = false;
        if (z) {
        }
        if (z != this.mZenVisible) {
        }
        if (z2 != this.mVibrateVisible) {
        }
        if (z3 != this.mMuteVisible) {
        }
        updateAlarm();
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
    public void onBluetoothDevicesChanged() {
        updateBluetooth();
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
    public void onBluetoothStateChange(boolean z) {
        updateBluetooth();
    }

    private final void updateBluetooth() {
        boolean z;
        int i = R$drawable.stat_sys_data_bluetooth_connected;
        String string = this.mResources.getString(R$string.accessibility_quick_settings_bluetooth_on);
        BluetoothController bluetoothController = this.mBluetooth;
        if (bluetoothController == null || !bluetoothController.isBluetoothConnected() || (!this.mBluetooth.isBluetoothAudioActive() && this.mBluetooth.isBluetoothAudioProfileOnly())) {
            z = false;
        } else {
            string = this.mResources.getString(R$string.accessibility_bluetooth_connected);
            z = this.mBluetooth.isBluetoothEnabled();
        }
        startOrStopTether();
        this.mIconController.setIcon(this.mSlotBluetooth, i, string);
        this.mIconController.setIconVisibility(this.mSlotBluetooth, z);
    }

    public void setupContext(Context context) {
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    void startOrStopTether() {
        boolean z;
        BluetoothController bluetoothController = this.mBluetooth;
        if (bluetoothController == null || !bluetoothController.isBluetoothConnected()) {
            return;
        }
        if (Settings.System.getInt(this.mContext.getContentResolver(), "tesla_hotspot") == 0) {
            z = true;
            if (!z || !isTesla()) {
            }
            this.mConnectivityManager.startTethering(0, true, this.mOnStartTetheringCallback, new Handler(Looper.getMainLooper()));
            return;
        }
        z = false;
        if (!z) {
        }
    }

    private boolean isTesla() {
        List<CachedBluetoothDevice> connectedDevices;
        BluetoothController bluetoothController = this.mBluetooth;
        if (bluetoothController == null || (connectedDevices = bluetoothController.getConnectedDevices()) == null || connectedDevices.size() <= 0) {
            return false;
        }
        for (CachedBluetoothDevice cachedBluetoothDevice : connectedDevices) {
            String address = cachedBluetoothDevice.getAddress();
            if (address != null && address.toLowerCase().startsWith(TESLA_MAC_ADDRESS)) {
                return true;
            }
        }
        return false;
    }

    private final void updateTTY() {
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager == null) {
            updateTTY(0);
        } else {
            updateTTY(telecomManager.getCurrentTtyMode());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateTTY(int i) {
        boolean z = i != 0;
        boolean z2 = DEBUG;
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateTTY: enabled: " + z);
        }
        if (z) {
            if (z2) {
                Log.v("PhoneStatusBarPolicy", "updateTTY: set TTY on");
            }
            this.mIconController.setIcon(this.mSlotTty, R$drawable.stat_sys_tty_mode, this.mResources.getString(R$string.accessibility_tty_enabled));
            this.mIconController.setIconVisibility(this.mSlotTty, true);
            return;
        }
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateTTY: set TTY off");
        }
        this.mIconController.setIconVisibility(this.mSlotTty, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCast() {
        boolean z;
        for (CastController.CastDevice castDevice : this.mCast.getCastDevices()) {
            int i = castDevice.state;
            if (i == 1 || i == 2) {
                z = true;
                break;
            }
            while (r0.hasNext()) {
            }
        }
        z = false;
        boolean z2 = DEBUG;
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateCast: isCasting: " + z);
        }
        this.mHandler.removeCallbacks(this.mRemoveCastIconRunnable);
        if (z && !this.mRecordingController.isRecording()) {
            this.mIconController.setIcon(this.mSlotCast, R$drawable.stat_sys_cast, this.mResources.getString(R$string.accessibility_casting));
            this.mIconController.setIconVisibility(this.mSlotCast, true);
            return;
        }
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateCast: hiding icon in 3 sec...");
        }
        this.mHandler.postDelayed(this.mRemoveCastIconRunnable, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateManagedProfile() {
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.lambda$updateManagedProfile$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateManagedProfile$2() {
        try {
            final boolean isManagedProfile = this.mUserManager.isManagedProfile(ActivityTaskManager.getService().getLastResumedActivityUserId());
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStatusBarPolicy.this.lambda$updateManagedProfile$1(isManagedProfile);
                }
            });
        } catch (RemoteException e) {
            Log.w("PhoneStatusBarPolicy", "updateManagedProfile: ", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateManagedProfile$1(boolean z) {
        boolean z2;
        if (!z || (this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isOccluded())) {
            z2 = false;
        } else {
            z2 = true;
            this.mIconController.setIcon(this.mSlotManagedProfile, R$drawable.stat_sys_managed_profile_status, this.mResources.getString(R$string.accessibility_managed_profile));
        }
        if (this.mManagedProfileIconVisible != z2) {
            this.mIconController.setIconVisibility(this.mSlotManagedProfile, z2);
            this.mManagedProfileIconVisible = z2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends SynchronousUserSwitchObserver {
        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitching$0() {
            PhoneStatusBarPolicy.this.mUserInfoController.reloadUserInfo();
        }

        public void onUserSwitching(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStatusBarPolicy.AnonymousClass2.this.lambda$onUserSwitching$0();
                }
            });
        }

        public void onUserSwitchComplete(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStatusBarPolicy.AnonymousClass2.this.lambda$onUserSwitchComplete$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitchComplete$1() {
            PhoneStatusBarPolicy.this.updateAlarm();
            PhoneStatusBarPolicy.this.updateManagedProfile();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements SensorPrivacyController.OnSensorPrivacyChangedListener {
        AnonymousClass6() {
        }

        @Override // com.android.systemui.statusbar.policy.SensorPrivacyController.OnSensorPrivacyChangedListener
        public void onSensorPrivacyChanged(final boolean z) {
            PhoneStatusBarPolicy.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStatusBarPolicy.AnonymousClass6.this.lambda$onSensorPrivacyChanged$0(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSensorPrivacyChanged$0(boolean z) {
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotSensorsOff, z);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mDisplayId == i) {
            updateManagedProfile();
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardShowingChanged() {
        updateManagedProfile();
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public void onUserSetupChanged() {
        DeviceProvisionedController deviceProvisionedController = this.mProvisionedController;
        boolean isUserSetup = deviceProvisionedController.isUserSetup(deviceProvisionedController.getCurrentUser());
        if (this.mCurrentUserSetup == isUserSetup) {
            return;
        }
        this.mCurrentUserSetup = isUserSetup;
        updateAlarm();
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController.RotationLockControllerCallback
    public void onRotationLockStateChanged(boolean z, boolean z2) {
        boolean isCurrentOrientationLockPortrait = RotationLockTile.isCurrentOrientationLockPortrait(this.mRotationLockController, this.mResources);
        if (z) {
            if (isCurrentOrientationLockPortrait) {
                this.mIconController.setIcon(this.mSlotRotate, R$drawable.stat_sys_rotate_portrait, this.mResources.getString(R$string.accessibility_rotation_lock_on_portrait));
            } else {
                this.mIconController.setIcon(this.mSlotRotate, R$drawable.stat_sys_rotate_landscape, this.mResources.getString(R$string.accessibility_rotation_lock_on_landscape));
            }
            this.mIconController.setIconVisibility(this.mSlotRotate, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotRotate, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeadsetPlug(Intent intent) {
        int i;
        boolean z = intent.getIntExtra("state", 0) != 0;
        boolean z2 = intent.getIntExtra("microphone", 0) != 0;
        if (z) {
            Resources resources = this.mResources;
            if (z2) {
                i = R$string.accessibility_status_bar_headset;
            } else {
                i = R$string.accessibility_status_bar_headphones;
            }
            this.mIconController.setIcon(this.mSlotHeadset, z2 ? R$drawable.stat_sys_headset_mic : R$drawable.stat_sys_headset, resources.getString(i));
            this.mIconController.setIconVisibility(this.mSlotHeadset, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotHeadset, false);
    }

    @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
    public void onDataSaverChanged(boolean z) {
        this.mIconController.setIconVisibility(this.mSlotDataSaver, z);
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        updatePrivacyItems(list);
    }

    private void updatePrivacyItems(List<PrivacyItem> list) {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (PrivacyItem privacyItem : list) {
            if (privacyItem == null) {
                Log.e("PhoneStatusBarPolicy", "updatePrivacyItems - null item found");
                StringWriter stringWriter = new StringWriter();
                this.mPrivacyItemController.dump(null, new PrintWriter(stringWriter), null);
                throw new NullPointerException(stringWriter.toString());
            }
            int i = AnonymousClass9.$SwitchMap$com$android$systemui$privacy$PrivacyType[privacyItem.getPrivacyType().ordinal()];
            if (i == 1) {
                z = true;
            } else if (i == 2) {
                z3 = true;
            } else if (i == 3) {
                z2 = true;
            }
        }
        this.mPrivacyLogger.logStatusBarIconsVisible(z, z2, z3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$9  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$privacy$PrivacyType;

        static {
            int[] iArr = new int[PrivacyType.valuesCustom().length];
            $SwitchMap$com$android$systemui$privacy$PrivacyType = iArr;
            try {
                iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$privacy$PrivacyType[PrivacyType.TYPE_LOCATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$privacy$PrivacyType[PrivacyType.TYPE_MICROPHONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.LocationController.LocationChangeCallback
    public void onLocationActiveChanged(boolean z) {
        if (!this.mPrivacyItemController.getLocationAvailable()) {
            updateLocationFromController();
        }
    }

    private void updateLocationFromController() {
        if (this.mLocationController.isLocationActive()) {
            this.mIconController.setIconVisibility(this.mSlotLocation, true);
        } else {
            this.mIconController.setIconVisibility(this.mSlotLocation, false);
        }
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public void onCountdown(long j) {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: countdown " + j);
        }
        int floorDiv = (int) Math.floorDiv(j + 500, 1000);
        int i = R$drawable.stat_sys_screen_record;
        String num = Integer.toString(floorDiv);
        if (floorDiv == 1) {
            i = R$drawable.stat_sys_screen_record_1;
        } else if (floorDiv == 2) {
            i = R$drawable.stat_sys_screen_record_2;
        } else if (floorDiv == 3) {
            i = R$drawable.stat_sys_screen_record_3;
        }
        this.mIconController.setIcon(this.mSlotScreenRecord, i, num);
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, true);
        this.mIconController.setIconAccessibilityLiveRegion(this.mSlotScreenRecord, 2);
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public void onCountdownEnd() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: hiding icon during countdown");
        }
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.lambda$onCountdownEnd$3();
            }
        });
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.lambda$onCountdownEnd$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCountdownEnd$3() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCountdownEnd$4() {
        this.mIconController.setIconAccessibilityLiveRegion(this.mSlotScreenRecord, 0);
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public void onRecordingStart() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: showing icon");
        }
        this.mIconController.setIcon(this.mSlotScreenRecord, R$drawable.stat_sys_screen_record, this.mResources.getString(R$string.screenrecord_ongoing_screen_only));
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.lambda$onRecordingStart$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecordingStart$5() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, true);
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public void onRecordingEnd() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: hiding icon");
        }
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PhoneStatusBarPolicy.this.lambda$onRecordingEnd$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecordingEnd$6() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHotspotIcon(int i) {
        if (i == 6) {
            this.mIconController.setIcon(this.mSlotHotspot, R$drawable.stat_sys_wifi_6_hotspot, this.mResources.getString(R$string.accessibility_status_bar_hotspot));
        } else if (i == 5) {
            this.mIconController.setIcon(this.mSlotHotspot, R$drawable.stat_sys_wifi_5_hotspot, this.mResources.getString(R$string.accessibility_status_bar_hotspot));
        } else if (i == 4) {
            this.mIconController.setIcon(this.mSlotHotspot, R$drawable.stat_sys_wifi_4_hotspot, this.mResources.getString(R$string.accessibility_status_bar_hotspot));
        } else {
            this.mIconController.setIcon(this.mSlotHotspot, R$drawable.stat_sys_hotspot, this.mResources.getString(R$string.accessibility_status_bar_hotspot));
        }
    }
}
