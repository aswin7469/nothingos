package com.android.systemui.statusbar.phone;

import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserManager;
import android.service.notification.ZenModeConfig;
import android.telecom.TelecomManager;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.systemui.C1894R;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.p012qs.tiles.RotationLockTile;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.CommandQueue;
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
import com.nothing.systemui.statusbar.phone.PhoneStatusBarPolicyEx;
import java.p026io.PrintWriter;
import java.p026io.StringWriter;
import java.p026io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class PhoneStatusBarPolicy implements BluetoothController.Callback, CommandQueue.Callbacks, RotationLockController.RotationLockControllerCallback, DataSaverController.Listener, ZenModeController.Callback, DeviceProvisionedController.DeviceProvisionedListener, KeyguardStateController.Callback, PrivacyItemController.Callback, LocationController.LocationChangeCallback, RecordingController.RecordingStateChangeCallback {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final int LOCATION_STATUS_ICON_ID = PrivacyType.TYPE_LOCATION.getIconId();
    private static final String TAG = "PhoneStatusBarPolicy";
    private final AlarmManager mAlarmManager;
    private BluetoothController mBluetooth;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CastController mCast;
    private final CastController.Callback mCastCallback = new CastController.Callback() {
        public void onCastDevicesChanged() {
            PhoneStatusBarPolicy.this.updateCast();
        }
    };
    private final CommandQueue mCommandQueue;
    private boolean mCurrentUserSetup;
    private final DataSaverController mDataSaver;
    private final DateFormatUtil mDateFormatUtil;
    private final DevicePolicyManager mDevicePolicyManager;
    private final int mDisplayId;
    PhoneStatusBarPolicyEx mEx;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private final HotspotController mHotspot;
    private final HotspotController.Callback mHotspotCallback = new HotspotController.Callback() {
        public void onHotspotChanged(boolean z, int i) {
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotHotspot, z);
        }

        public void onHotspotChanged(boolean z, int i, int i2) {
            PhoneStatusBarPolicy.this.updateHotspotIcon(i2);
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotHotspot, z);
        }
    };
    private final IActivityManager mIActivityManager;
    /* access modifiers changed from: private */
    public final StatusBarIconController mIconController;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
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
    private final KeyguardStateController mKeyguardStateController;
    private final LocationController mLocationController;
    private boolean mManagedProfileIconVisible = false;
    private boolean mMuteVisible;
    /* access modifiers changed from: private */
    public AlarmManager.AlarmClockInfo mNextAlarm;
    private final NextAlarmController.NextAlarmChangeCallback mNextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback() {
        public void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            AlarmManager.AlarmClockInfo unused = PhoneStatusBarPolicy.this.mNextAlarm = alarmClockInfo;
            PhoneStatusBarPolicy.this.updateAlarm();
        }
    };
    private final NextAlarmController mNextAlarmController;
    private final PrivacyItemController mPrivacyItemController;
    private final PrivacyLogger mPrivacyLogger;
    private final DeviceProvisionedController mProvisionedController;
    private final RecordingController mRecordingController;
    private Runnable mRemoveCastIconRunnable = new Runnable() {
        public void run() {
            if (PhoneStatusBarPolicy.DEBUG) {
                Log.v(PhoneStatusBarPolicy.TAG, "updateCast: hiding icon NOW");
            }
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotCast, false);
        }
    };
    private final Resources mResources;
    private final RingerModeTracker mRingerModeTracker;
    private final RotationLockController mRotationLockController;
    private final SensorPrivacyController mSensorPrivacyController;
    private final SensorPrivacyController.OnSensorPrivacyChangedListener mSensorPrivacyListener = new SensorPrivacyController.OnSensorPrivacyChangedListener() {
        public void onSensorPrivacyChanged(boolean z) {
            PhoneStatusBarPolicy.this.mHandler.post(new PhoneStatusBarPolicy$5$$ExternalSyntheticLambda0(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSensorPrivacyChanged$0$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy$5 */
        public /* synthetic */ void mo44990x5d39614b(boolean z) {
            PhoneStatusBarPolicy.this.mIconController.setIconVisibility(PhoneStatusBarPolicy.this.mSlotSensorsOff, z);
        }
    };
    private final SharedPreferences mSharedPreferences;
    private final String mSlotAlarmClock;
    private final String mSlotBluetooth;
    private final String mSlotCamera;
    /* access modifiers changed from: private */
    public final String mSlotCast;
    private final String mSlotDataSaver;
    private final String mSlotHeadset;
    /* access modifiers changed from: private */
    public final String mSlotHotspot;
    private final String mSlotLocation;
    private final String mSlotManagedProfile;
    private final String mSlotMicrophone;
    private final String mSlotMute;
    private final String mSlotRotate;
    private final String mSlotScreenRecord;
    /* access modifiers changed from: private */
    public final String mSlotSensorsOff;
    private final String mSlotTty;
    private final String mSlotVibrate;
    private final String mSlotZen;
    private final TelecomManager mTelecomManager;
    private final Executor mUiBgExecutor;
    /* access modifiers changed from: private */
    public final UserInfoController mUserInfoController;
    private final UserManager mUserManager;
    private final SynchronousUserSwitchObserver mUserSwitchListener = new SynchronousUserSwitchObserver() {
        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSwitching$0$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy$1 */
        public /* synthetic */ void mo44986x87306a4c() {
            PhoneStatusBarPolicy.this.mUserInfoController.reloadUserInfo();
        }

        public void onUserSwitching(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new PhoneStatusBarPolicy$1$$ExternalSyntheticLambda0(this));
        }

        public void onUserSwitchComplete(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new PhoneStatusBarPolicy$1$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSwitchComplete$1$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy$1 */
        public /* synthetic */ void mo44985x263abe1a() {
            PhoneStatusBarPolicy.this.updateAlarm();
            PhoneStatusBarPolicy.this.updateManagedProfile();
        }
    };
    private boolean mVibrateVisible;
    private final ZenModeController mZenController;
    private boolean mZenVisible;

    @Inject
    public PhoneStatusBarPolicy(StatusBarIconController statusBarIconController, CommandQueue commandQueue, BroadcastDispatcher broadcastDispatcher, @UiBackground Executor executor, @Main Resources resources, CastController castController, HotspotController hotspotController, BluetoothController bluetoothController, NextAlarmController nextAlarmController, UserInfoController userInfoController, RotationLockController rotationLockController, DataSaverController dataSaverController, ZenModeController zenModeController, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, LocationController locationController, SensorPrivacyController sensorPrivacyController, IActivityManager iActivityManager, AlarmManager alarmManager, UserManager userManager, DevicePolicyManager devicePolicyManager, RecordingController recordingController, TelecomManager telecomManager, @DisplayId int i, @Main SharedPreferences sharedPreferences, DateFormatUtil dateFormatUtil, RingerModeTracker ringerModeTracker, PrivacyItemController privacyItemController, PrivacyLogger privacyLogger) {
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
        this.mDevicePolicyManager = devicePolicyManager;
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
        this.mSlotCast = resources.getString(17041559);
        this.mSlotHotspot = resources.getString(17041566);
        this.mSlotBluetooth = resources.getString(17041556);
        this.mSlotTty = resources.getString(17041584);
        this.mSlotZen = resources.getString(17041588);
        this.mSlotMute = resources.getString(17041572);
        this.mSlotVibrate = resources.getString(17041585);
        this.mSlotAlarmClock = resources.getString(17041554);
        this.mSlotManagedProfile = resources.getString(17041569);
        this.mSlotRotate = resources.getString(17041577);
        this.mSlotHeadset = resources.getString(17041565);
        this.mSlotDataSaver = resources.getString(17041563);
        this.mSlotLocation = resources.getString(17041568);
        this.mSlotMicrophone = resources.getString(17041570);
        this.mSlotCamera = resources.getString(17041558);
        this.mSlotSensorsOff = resources.getString(17041580);
        this.mSlotScreenRecord = resources.getString(17041578);
        this.mDisplayId = i;
        this.mSharedPreferences = sharedPreferences;
        this.mDateFormatUtil = dateFormatUtil;
    }

    public void init(Context context) {
        this.mEx = new PhoneStatusBarPolicyEx(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.telecom.action.CURRENT_TTY_MODE_CHANGED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mIntentReceiver, intentFilter, this.mHandler);
        PhoneStatusBarPolicy$$ExternalSyntheticLambda4 phoneStatusBarPolicy$$ExternalSyntheticLambda4 = new PhoneStatusBarPolicy$$ExternalSyntheticLambda4(this);
        this.mRingerModeTracker.getRingerMode().observeForever(phoneStatusBarPolicy$$ExternalSyntheticLambda4);
        this.mRingerModeTracker.getRingerModeInternal().observeForever(phoneStatusBarPolicy$$ExternalSyntheticLambda4);
        try {
            this.mIActivityManager.registerUserSwitchObserver(this.mUserSwitchListener, TAG);
        } catch (RemoteException unused) {
        }
        updateTTY();
        updateBluetooth();
        this.mIconController.setIcon(this.mSlotAlarmClock, C1894R.C1896drawable.stat_sys_alarm, (CharSequence) null);
        this.mIconController.setIconVisibility(this.mSlotAlarmClock, false);
        this.mIconController.setIcon(this.mSlotZen, C1894R.C1896drawable.stat_sys_dnd, (CharSequence) null);
        this.mIconController.setIconVisibility(this.mSlotZen, false);
        this.mIconController.setIcon(this.mSlotVibrate, C1894R.C1896drawable.stat_sys_ringer_vibrate, this.mResources.getString(C1894R.string.accessibility_ringer_vibrate));
        this.mIconController.setIconVisibility(this.mSlotVibrate, false);
        this.mIconController.setIcon(this.mSlotMute, C1894R.C1896drawable.stat_sys_ringer_silent, this.mResources.getString(C1894R.string.accessibility_ringer_silent));
        this.mIconController.setIconVisibility(this.mSlotMute, false);
        updateVolumeZen();
        this.mIconController.setIcon(this.mSlotCast, C1894R.C1896drawable.stat_sys_cast, (CharSequence) null);
        this.mIconController.setIconVisibility(this.mSlotCast, false);
        updateManagedProfile();
        this.mIconController.setIcon(this.mSlotDataSaver, C1894R.C1896drawable.stat_sys_data_saver, this.mResources.getString(C1894R.string.accessibility_data_saver_on));
        this.mIconController.setIconVisibility(this.mSlotDataSaver, false);
        String string = this.mResources.getString(PrivacyType.TYPE_MICROPHONE.getNameId());
        this.mIconController.setIcon(this.mSlotMicrophone, PrivacyType.TYPE_MICROPHONE.getIconId(), this.mResources.getString(C1894R.string.ongoing_privacy_chip_content_multiple_apps, new Object[]{string}));
        this.mIconController.setIconVisibility(this.mSlotMicrophone, false);
        String string2 = this.mResources.getString(PrivacyType.TYPE_CAMERA.getNameId());
        this.mIconController.setIcon(this.mSlotCamera, PrivacyType.TYPE_CAMERA.getIconId(), this.mResources.getString(C1894R.string.ongoing_privacy_chip_content_multiple_apps, new Object[]{string2}));
        this.mIconController.setIconVisibility(this.mSlotCamera, false);
        this.mIconController.setIcon(this.mSlotLocation, LOCATION_STATUS_ICON_ID, this.mResources.getString(C1894R.string.accessibility_location_active));
        this.mIconController.setIconVisibility(this.mSlotLocation, false);
        this.mIconController.setIcon(this.mSlotSensorsOff, C1894R.C1896drawable.stat_sys_sensors_off, this.mResources.getString(C1894R.string.accessibility_sensors_off_active));
        this.mIconController.setIconVisibility(this.mSlotSensorsOff, this.mSensorPrivacyController.isSensorPrivacyEnabled());
        this.mIconController.setIcon(this.mSlotScreenRecord, C1894R.C1896drawable.stat_sys_screen_record, (CharSequence) null);
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
        this.mPrivacyItemController.addCallback((PrivacyItemController.Callback) this);
        this.mSensorPrivacyController.addCallback(this.mSensorPrivacyListener);
        this.mLocationController.addCallback(this);
        this.mRecordingController.addCallback((RecordingController.RecordingStateChangeCallback) this);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$0$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44977x439b9625(Integer num) {
        this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda5(this));
    }

    private String getManagedProfileAccessibilityString() {
        return this.mDevicePolicyManager.getResources().getString("SystemUi.STATUS_BAR_WORK_ICON_ACCESSIBILITY", new PhoneStatusBarPolicy$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagedProfileAccessibilityString$1$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ String mo44976xc8809635() {
        return this.mResources.getString(C1894R.string.accessibility_managed_profile);
    }

    public void onZenChanged(int i) {
        updateVolumeZen();
    }

    public void onConfigChanged(ZenModeConfig zenModeConfig) {
        updateVolumeZen();
    }

    /* access modifiers changed from: private */
    public void updateAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(-2);
        boolean z = true;
        boolean z2 = nextAlarmClock != null && nextAlarmClock.getTriggerTime() > 0;
        this.mIconController.setIcon(this.mSlotAlarmClock, this.mZenController.getZen() == 2 ? C1894R.C1896drawable.stat_sys_alarm_dim : C1894R.C1896drawable.stat_sys_alarm, buildAlarmContentDescription());
        StatusBarIconController statusBarIconController = this.mIconController;
        String str = this.mSlotAlarmClock;
        if (!this.mCurrentUserSetup || !z2) {
            z = false;
        }
        statusBarIconController.setIconVisibility(str, z);
    }

    private String buildAlarmContentDescription() {
        if (this.mNextAlarm == null) {
            return this.mResources.getString(C1894R.string.status_bar_alarm);
        }
        return this.mResources.getString(C1894R.string.accessibility_quick_settings_alarm, new Object[]{DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mDateFormatUtil.is24HourFormat() ? "EHm" : "Ehma"), this.mNextAlarm.getTriggerTime()).toString()});
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateVolumeZen() {
        /*
            r8 = this;
            com.android.systemui.statusbar.policy.ZenModeController r0 = r8.mZenController
            int r0 = r0.getZen()
            android.content.SharedPreferences r1 = r8.mSharedPreferences
            boolean r1 = com.android.systemui.p012qs.tiles.DndTile.isVisible(r1)
            r2 = 2131232826(0x7f08083a, float:1.8081772E38)
            r3 = 1
            r4 = 0
            if (r1 != 0) goto L_0x003c
            android.content.SharedPreferences r1 = r8.mSharedPreferences
            boolean r1 = com.android.systemui.p012qs.tiles.DndTile.isCombinedIcon(r1)
            if (r1 == 0) goto L_0x001c
            goto L_0x003c
        L_0x001c:
            r1 = 2
            if (r0 != r1) goto L_0x002b
            android.content.res.Resources r1 = r8.mResources
            r5 = 2131952472(0x7f130358, float:1.9541388E38)
            java.lang.String r1 = r1.getString(r5)
        L_0x0028:
            r5 = r1
            r1 = r3
            goto L_0x004a
        L_0x002b:
            if (r0 != r3) goto L_0x0037
            android.content.res.Resources r1 = r8.mResources
            r5 = 2131952475(0x7f13035b, float:1.9541394E38)
            java.lang.String r1 = r1.getString(r5)
            goto L_0x0028
        L_0x0037:
            r1 = 0
            r5 = r1
            r1 = r4
            r2 = r1
            goto L_0x004a
        L_0x003c:
            if (r0 == 0) goto L_0x0040
            r1 = r3
            goto L_0x0041
        L_0x0040:
            r1 = r4
        L_0x0041:
            android.content.res.Resources r5 = r8.mResources
            r6 = 2131953147(0x7f1305fb, float:1.9542757E38)
            java.lang.String r5 = r5.getString(r6)
        L_0x004a:
            com.android.systemui.statusbar.policy.ZenModeController r6 = r8.mZenController
            android.app.NotificationManager$Policy r6 = r6.getConsolidatedPolicy()
            boolean r0 = android.service.notification.ZenModeConfig.isZenOverridingRinger(r0, r6)
            if (r0 != 0) goto L_0x0075
            com.android.systemui.util.RingerModeTracker r0 = r8.mRingerModeTracker
            androidx.lifecycle.LiveData r0 = r0.getRingerModeInternal()
            java.lang.Object r0 = r0.getValue()
            java.lang.Integer r0 = (java.lang.Integer) r0
            if (r0 == 0) goto L_0x0075
            int r6 = r0.intValue()
            if (r6 != r3) goto L_0x006b
            goto L_0x0076
        L_0x006b:
            int r0 = r0.intValue()
            if (r0 != 0) goto L_0x0075
            r7 = r4
            r4 = r3
            r3 = r7
            goto L_0x0076
        L_0x0075:
            r3 = r4
        L_0x0076:
            if (r1 == 0) goto L_0x007f
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r6 = r8.mSlotZen
            r0.setIcon(r6, r2, r5)
        L_0x007f:
            boolean r0 = r8.mZenVisible
            if (r1 == r0) goto L_0x008c
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r2 = r8.mSlotZen
            r0.setIconVisibility(r2, r1)
            r8.mZenVisible = r1
        L_0x008c:
            boolean r0 = r8.mVibrateVisible
            if (r3 == r0) goto L_0x0099
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r1 = r8.mSlotVibrate
            r0.setIconVisibility(r1, r3)
            r8.mVibrateVisible = r3
        L_0x0099:
            boolean r0 = r8.mMuteVisible
            if (r4 == r0) goto L_0x00a6
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r1 = r8.mSlotMute
            r0.setIconVisibility(r1, r4)
            r8.mMuteVisible = r4
        L_0x00a6:
            r8.updateAlarm()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.updateVolumeZen():void");
    }

    public void onBluetoothDevicesChanged() {
        updateBluetooth();
    }

    public void onBluetoothStateChange(boolean z) {
        updateBluetooth();
    }

    private final void updateBluetooth() {
        boolean z;
        String string = this.mResources.getString(C1894R.string.accessibility_quick_settings_bluetooth_on);
        BluetoothController bluetoothController = this.mBluetooth;
        if (bluetoothController == null || !bluetoothController.isBluetoothConnected() || (!this.mBluetooth.isBluetoothAudioActive() && this.mBluetooth.isBluetoothAudioProfileOnly())) {
            z = false;
        } else {
            string = this.mResources.getString(C1894R.string.accessibility_bluetooth_connected);
            z = this.mBluetooth.isBluetoothEnabled();
        }
        this.mEx.startOrStopTether(this.mBluetooth);
        this.mIconController.setIcon(this.mSlotBluetooth, C1894R.C1896drawable.stat_sys_data_bluetooth_connected, string);
        this.mIconController.setIconVisibility(this.mSlotBluetooth, z);
    }

    private final void updateTTY() {
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager == null) {
            updateTTY(0);
        } else {
            updateTTY(telecomManager.getCurrentTtyMode());
        }
    }

    /* access modifiers changed from: private */
    public final void updateTTY(int i) {
        boolean z = i != 0;
        boolean z2 = DEBUG;
        if (z2) {
            Log.v(TAG, "updateTTY: enabled: " + z);
        }
        if (z) {
            if (z2) {
                Log.v(TAG, "updateTTY: set TTY on");
            }
            this.mIconController.setIcon(this.mSlotTty, C1894R.C1896drawable.stat_sys_tty_mode, this.mResources.getString(C1894R.string.accessibility_tty_enabled));
            this.mIconController.setIconVisibility(this.mSlotTty, true);
            return;
        }
        if (z2) {
            Log.v(TAG, "updateTTY: set TTY off");
        }
        this.mIconController.setIconVisibility(this.mSlotTty, false);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0022 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCast() {
        /*
            r6 = this;
            com.android.systemui.statusbar.policy.CastController r0 = r6.mCast
            java.util.List r0 = r0.getCastDevices()
            java.util.Iterator r0 = r0.iterator()
        L_0x000a:
            boolean r1 = r0.hasNext()
            r2 = 1
            if (r1 == 0) goto L_0x0022
            java.lang.Object r1 = r0.next()
            com.android.systemui.statusbar.policy.CastController$CastDevice r1 = (com.android.systemui.statusbar.policy.CastController.CastDevice) r1
            int r3 = r1.state
            if (r3 == r2) goto L_0x0020
            int r1 = r1.state
            r3 = 2
            if (r1 != r3) goto L_0x000a
        L_0x0020:
            r0 = r2
            goto L_0x0023
        L_0x0022:
            r0 = 0
        L_0x0023:
            boolean r1 = DEBUG
            java.lang.String r3 = "PhoneStatusBarPolicy"
            if (r1 == 0) goto L_0x003c
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "updateCast: isCasting: "
            r4.<init>((java.lang.String) r5)
            java.lang.StringBuilder r4 = r4.append((boolean) r0)
            java.lang.String r4 = r4.toString()
            android.util.Log.v(r3, r4)
        L_0x003c:
            android.os.Handler r4 = r6.mHandler
            java.lang.Runnable r5 = r6.mRemoveCastIconRunnable
            r4.removeCallbacks(r5)
            if (r0 == 0) goto L_0x0068
            com.android.systemui.screenrecord.RecordingController r0 = r6.mRecordingController
            boolean r0 = r0.isRecording()
            if (r0 != 0) goto L_0x0068
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r6.mIconController
            java.lang.String r1 = r6.mSlotCast
            android.content.res.Resources r3 = r6.mResources
            r4 = 2131951692(0x7f13004c, float:1.9539806E38)
            java.lang.String r3 = r3.getString(r4)
            r4 = 2131232823(0x7f080837, float:1.8081766E38)
            r0.setIcon(r1, r4, r3)
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r6.mIconController
            java.lang.String r6 = r6.mSlotCast
            r0.setIconVisibility(r6, r2)
            goto L_0x0079
        L_0x0068:
            if (r1 == 0) goto L_0x0070
            java.lang.String r0 = "updateCast: hiding icon in 3 sec..."
            android.util.Log.v(r3, r0)
        L_0x0070:
            android.os.Handler r0 = r6.mHandler
            java.lang.Runnable r6 = r6.mRemoveCastIconRunnable
            r1 = 3000(0xbb8, double:1.482E-320)
            r0.postDelayed(r6, r1)
        L_0x0079:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.updateCast():void");
    }

    /* access modifiers changed from: private */
    public void updateManagedProfile() {
        this.mUiBgExecutor.execute(new PhoneStatusBarPolicy$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateManagedProfile$3$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44983xda54710b() {
        try {
            this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda6(this, this.mUserManager.isManagedProfile(ActivityTaskManager.getService().getLastResumedActivityUserId()), getManagedProfileAccessibilityString()));
        } catch (RemoteException e) {
            Log.w(TAG, "updateManagedProfile: ", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateManagedProfile$2$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44982xc99ea44a(boolean z, String str) {
        boolean z2;
        if (!z || (this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isOccluded())) {
            z2 = false;
        } else {
            this.mIconController.setIcon(this.mSlotManagedProfile, C1894R.C1896drawable.stat_sys_managed_profile_status, str);
            z2 = true;
        }
        if (this.mManagedProfileIconVisible != z2) {
            this.mIconController.setIconVisibility(this.mSlotManagedProfile, z2);
            this.mManagedProfileIconVisible = z2;
        }
    }

    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mDisplayId == i) {
            updateManagedProfile();
        }
    }

    public void onKeyguardShowingChanged() {
        updateManagedProfile();
    }

    public void onUserSetupChanged() {
        boolean isCurrentUserSetup = this.mProvisionedController.isCurrentUserSetup();
        if (this.mCurrentUserSetup != isCurrentUserSetup) {
            this.mCurrentUserSetup = isCurrentUserSetup;
            updateAlarm();
        }
    }

    public void onRotationLockStateChanged(boolean z, boolean z2) {
        boolean isCurrentOrientationLockPortrait = RotationLockTile.isCurrentOrientationLockPortrait(this.mRotationLockController, this.mResources);
        if (z) {
            if (isCurrentOrientationLockPortrait) {
                this.mIconController.setIcon(this.mSlotRotate, C1894R.C1896drawable.stat_sys_rotate_portrait, this.mResources.getString(C1894R.string.accessibility_rotation_lock_on_portrait));
            } else {
                this.mIconController.setIcon(this.mSlotRotate, C1894R.C1896drawable.stat_sys_rotate_landscape, this.mResources.getString(C1894R.string.accessibility_rotation_lock_on_landscape));
            }
            this.mIconController.setIconVisibility(this.mSlotRotate, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotRotate, false);
    }

    /* access modifiers changed from: private */
    public void updateHeadsetPlug(Intent intent) {
        boolean z = intent.getIntExtra(AuthDialog.KEY_BIOMETRIC_STATE, 0) != 0;
        boolean z2 = intent.getIntExtra("microphone", 0) != 0;
        if (z) {
            this.mIconController.setIcon(this.mSlotHeadset, z2 ? C1894R.C1896drawable.stat_sys_headset_mic : C1894R.C1896drawable.stat_sys_headset, this.mResources.getString(z2 ? C1894R.string.accessibility_status_bar_headset : C1894R.string.accessibility_status_bar_headphones));
            this.mIconController.setIconVisibility(this.mSlotHeadset, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotHeadset, false);
    }

    public void onDataSaverChanged(boolean z) {
        this.mIconController.setIconVisibility(this.mSlotDataSaver, z);
    }

    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        updatePrivacyItems(list);
    }

    private void updatePrivacyItems(List<PrivacyItem> list) {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (PrivacyItem next : list) {
            if (next != null) {
                int i = C30708.$SwitchMap$com$android$systemui$privacy$PrivacyType[next.getPrivacyType().ordinal()];
                if (i == 1) {
                    z = true;
                } else if (i == 2) {
                    z3 = true;
                } else if (i == 3) {
                    z2 = true;
                }
            } else {
                Log.e(TAG, "updatePrivacyItems - null item found");
                StringWriter stringWriter = new StringWriter();
                this.mPrivacyItemController.dump(new PrintWriter((Writer) stringWriter), (String[]) null);
                throw new NullPointerException(stringWriter.toString());
            }
        }
        this.mPrivacyLogger.logStatusBarIconsVisible(z, z2, z3);
    }

    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$8 */
    static /* synthetic */ class C30708 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$privacy$PrivacyType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.android.systemui.privacy.PrivacyType[] r0 = com.android.systemui.privacy.PrivacyType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$privacy$PrivacyType = r0
                com.android.systemui.privacy.PrivacyType r1 = com.android.systemui.privacy.PrivacyType.TYPE_CAMERA     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$privacy$PrivacyType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.privacy.PrivacyType r1 = com.android.systemui.privacy.PrivacyType.TYPE_LOCATION     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$privacy$PrivacyType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.privacy.PrivacyType r1 = com.android.systemui.privacy.PrivacyType.TYPE_MICROPHONE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.C30708.<clinit>():void");
        }
    }

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

    public void onCountdown(long j) {
        if (DEBUG) {
            Log.d(TAG, "screenrecord: countdown " + j);
        }
        int floorDiv = (int) Math.floorDiv(j + 500, 1000);
        this.mIconController.setIcon(this.mSlotScreenRecord, floorDiv != 1 ? floorDiv != 2 ? floorDiv != 3 ? C1894R.C1896drawable.stat_sys_screen_record : C1894R.C1896drawable.stat_sys_screen_record_3 : C1894R.C1896drawable.stat_sys_screen_record_2 : C1894R.C1896drawable.stat_sys_screen_record_1, Integer.toString(floorDiv));
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, true);
        this.mIconController.setIconAccessibilityLiveRegion(this.mSlotScreenRecord, 2);
    }

    public void onCountdownEnd() {
        if (DEBUG) {
            Log.d(TAG, "screenrecord: hiding icon during countdown");
        }
        this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda7(this));
        this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda8(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCountdownEnd$4$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44978x6ce8d4c2() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCountdownEnd$5$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44979x7d9ea183() {
        this.mIconController.setIconAccessibilityLiveRegion(this.mSlotScreenRecord, 0);
    }

    public void onRecordingStart() {
        if (DEBUG) {
            Log.d(TAG, "screenrecord: showing icon");
        }
        this.mIconController.setIcon(this.mSlotScreenRecord, C1894R.C1896drawable.stat_sys_screen_record, this.mResources.getString(C1894R.string.screenrecord_ongoing_screen_only));
        this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onRecordingStart$6$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44981x8cce6cb() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, true);
    }

    public void onRecordingEnd() {
        if (DEBUG) {
            Log.d(TAG, "screenrecord: hiding icon");
        }
        this.mHandler.post(new PhoneStatusBarPolicy$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onRecordingEnd$7$com-android-systemui-statusbar-phone-PhoneStatusBarPolicy */
    public /* synthetic */ void mo44980x6ce51b45() {
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, false);
    }

    /* access modifiers changed from: private */
    public void updateHotspotIcon(int i) {
        if (i == 6) {
            this.mIconController.setIcon(this.mSlotHotspot, C1894R.C1896drawable.stat_sys_wifi_6_hotspot, this.mResources.getString(C1894R.string.accessibility_status_bar_hotspot));
        } else if (i == 5) {
            this.mIconController.setIcon(this.mSlotHotspot, C1894R.C1896drawable.stat_sys_wifi_5_hotspot, this.mResources.getString(C1894R.string.accessibility_status_bar_hotspot));
        } else if (i == 4) {
            this.mIconController.setIcon(this.mSlotHotspot, C1894R.C1896drawable.stat_sys_wifi_4_hotspot, this.mResources.getString(C1894R.string.accessibility_status_bar_hotspot));
        } else {
            this.mIconController.setIcon(this.mSlotHotspot, C1894R.C1896drawable.stat_sys_hotspot, this.mResources.getString(C1894R.string.accessibility_status_bar_hotspot));
        }
    }
}
