package com.android.systemui.doze;

import android.app.ActivityManager;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class DozeSensors {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = DozeService.DEBUG;
    private static final String TAG = "DozeSensors";
    /* access modifiers changed from: private */
    public static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    /* access modifiers changed from: private */
    public final AuthController mAuthController;
    private final AuthController.Callback mAuthControllerCallback;
    /* access modifiers changed from: private */
    public final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    /* access modifiers changed from: private */
    public long mDebounceFrom;
    /* access modifiers changed from: private */
    public int mDevicePosture;
    private final DevicePostureController.Callback mDevicePostureCallback;
    private final DevicePostureController mDevicePostureController;
    /* access modifiers changed from: private */
    public final DozeLog mDozeLog;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private boolean mListening;
    private boolean mListeningProxSensors;
    private boolean mListeningTouchScreenSensors;
    private final Consumer<Boolean> mProxCallback;
    private final ProximitySensor mProximitySensor;
    private final boolean mScreenOffUdfpsEnabled;
    /* access modifiers changed from: private */
    public final SecureSettings mSecureSettings;
    private boolean mSelectivelyRegisterProxSensors;
    /* access modifiers changed from: private */
    public final Callback mSensorCallback;
    /* access modifiers changed from: private */
    public final AsyncSensorManager mSensorManager;
    private boolean mSettingRegistered;
    /* access modifiers changed from: private */
    public final ContentObserver mSettingsObserver;
    protected TriggerSensor[] mTriggerSensors;
    /* access modifiers changed from: private */
    public boolean mUdfpsEnrolled;
    /* access modifiers changed from: private */
    public final WakeLock mWakeLock;

    public interface Callback {
        void onSensorPulse(int i, float f, float f2, float[] fArr);
    }

    public enum DozeSensorsUiEvent implements UiEventLogger.UiEventEnum {
        ACTION_AMBIENT_GESTURE_PICKUP(459);
        
        private final int mId;

        private DozeSensorsUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    DozeSensors(Context context, AsyncSensorManager asyncSensorManager, DozeParameters dozeParameters, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, Callback callback, Consumer<Boolean> consumer, DozeLog dozeLog, ProximitySensor proximitySensor, SecureSettings secureSettings, AuthController authController, DevicePostureController devicePostureController) {
        AsyncSensorManager asyncSensorManager2 = asyncSensorManager;
        AmbientDisplayConfiguration ambientDisplayConfiguration2 = ambientDisplayConfiguration;
        ProximitySensor proximitySensor2 = proximitySensor;
        AuthController authController2 = authController;
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (i2 == ActivityManager.getCurrentUser()) {
                    for (TriggerSensor updateListening : DozeSensors.this.mTriggerSensors) {
                        updateListening.updateListening();
                    }
                }
            }
        };
        DozeSensors$$ExternalSyntheticLambda0 dozeSensors$$ExternalSyntheticLambda0 = new DozeSensors$$ExternalSyntheticLambda0(this);
        this.mDevicePostureCallback = dozeSensors$$ExternalSyntheticLambda0;
        C20662 r1 = new AuthController.Callback() {
            public void onAllAuthenticatorsRegistered() {
                updateUdfpsEnrolled();
            }

            public void onEnrollmentsChanged() {
                updateUdfpsEnrolled();
            }

            private void updateUdfpsEnrolled() {
                DozeSensors dozeSensors = DozeSensors.this;
                boolean unused = dozeSensors.mUdfpsEnrolled = dozeSensors.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
                for (TriggerSensor triggerSensor : DozeSensors.this.mTriggerSensors) {
                    if (11 == triggerSensor.mPulseReason) {
                        triggerSensor.setConfigured(DozeSensors.this.quickPickUpConfigured());
                    } else if (10 == triggerSensor.mPulseReason) {
                        triggerSensor.setConfigured(DozeSensors.this.udfpsLongPressConfigured());
                    }
                }
            }
        };
        this.mAuthControllerCallback = r1;
        this.mContext = context;
        this.mSensorManager = asyncSensorManager2;
        this.mConfig = ambientDisplayConfiguration2;
        this.mWakeLock = wakeLock;
        this.mProxCallback = consumer;
        this.mSecureSettings = secureSettings;
        this.mSensorCallback = callback;
        this.mDozeLog = dozeLog;
        this.mProximitySensor = proximitySensor2;
        proximitySensor2.setTag(TAG);
        boolean selectivelyRegisterSensorsUsingProx = dozeParameters.getSelectivelyRegisterSensorsUsingProx();
        this.mSelectivelyRegisterProxSensors = selectivelyRegisterSensorsUsingProx;
        this.mListeningProxSensors = !selectivelyRegisterSensorsUsingProx;
        this.mScreenOffUdfpsEnabled = ambientDisplayConfiguration2.screenOffUdfpsEnabled(KeyguardUpdateMonitor.getCurrentUser());
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        this.mAuthController = authController2;
        this.mUdfpsEnrolled = authController2.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
        authController2.addCallback(r1);
        TriggerSensor[] triggerSensorArr = new TriggerSensor[9];
        triggerSensorArr[0] = new TriggerSensor(this, asyncSensorManager2.getDefaultSensor(17), (String) null, dozeParameters.getPulseOnSigMotion(), 2, false, false);
        TriggerSensor[] triggerSensorArr2 = triggerSensorArr;
        triggerSensorArr2[1] = new TriggerSensor(this, asyncSensorManager2.getDefaultSensor(25), "doze_pulse_on_pick_up", true, ambientDisplayConfiguration.dozePickupSensorAvailable(), 3, false, false, false, false);
        triggerSensorArr2[2] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.doubleTapSensorType()), "doze_pulse_on_double_tap", true, 4, dozeParameters.doubleTapReportsTouchCoordinates(), true);
        TriggerSensor[] triggerSensorArr3 = triggerSensorArr2;
        DozeSensors$$ExternalSyntheticLambda0 dozeSensors$$ExternalSyntheticLambda02 = dozeSensors$$ExternalSyntheticLambda0;
        triggerSensorArr3[3] = new TriggerSensor(findSensors(ambientDisplayConfiguration.tapSensorTypeMapping()), "doze_tap_gesture", true, true, 9, false, true, false, dozeParameters.singleTapUsesProx(this.mDevicePosture), this.mDevicePosture);
        triggerSensorArr3[4] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.longPressSensorType()), "doze_pulse_on_long_press", false, true, 5, true, true, false, dozeParameters.longPressUsesProx());
        triggerSensorArr3[5] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.udfpsLongPressSensorType()), "doze_pulse_on_auth", true, udfpsLongPressConfigured(), 10, true, true, false, dozeParameters.longPressUsesProx());
        triggerSensorArr3[6] = new PluginSensor(this, new SensorManagerPlugin.Sensor(2), "doze_wake_display_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable() && ambientDisplayConfiguration2.alwaysOnEnabled(-2), 7, false, false);
        triggerSensorArr3[7] = new PluginSensor(this, new SensorManagerPlugin.Sensor(1), "doze_wake_screen_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable(), 8, false, false, ambientDisplayConfiguration.getWakeLockScreenDebounce());
        triggerSensorArr3[8] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.quickPickupSensorType()), "doze_quick_pickup_gesture", true, quickPickUpConfigured(), 11, false, false, false, false);
        this.mTriggerSensors = triggerSensorArr3;
        setProxListening(false);
        proximitySensor2.register(new DozeSensors$$ExternalSyntheticLambda1(this));
        devicePostureController.addCallback(dozeSensors$$ExternalSyntheticLambda02);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-doze-DozeSensors  reason: not valid java name */
    public /* synthetic */ void m2741lambda$new$0$comandroidsystemuidozeDozeSensors(ThresholdSensorEvent thresholdSensorEvent) {
        if (thresholdSensorEvent != null) {
            this.mProxCallback.accept(Boolean.valueOf(!thresholdSensorEvent.getBelow()));
        }
    }

    /* access modifiers changed from: private */
    public boolean udfpsLongPressConfigured() {
        return this.mUdfpsEnrolled && (this.mConfig.alwaysOnEnabled(-2) || this.mScreenOffUdfpsEnabled);
    }

    /* access modifiers changed from: private */
    public boolean quickPickUpConfigured() {
        return this.mUdfpsEnrolled && this.mConfig.quickPickupSensorEnabled(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void destroy() {
        for (TriggerSensor listening : this.mTriggerSensors) {
            listening.setListening(false);
        }
        this.mProximitySensor.destroy();
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
    }

    public void requestTemporaryDisable() {
        this.mDebounceFrom = SystemClock.uptimeMillis();
    }

    private Sensor findSensor(String str) {
        return findSensor(this.mSensorManager, str, (String) null);
    }

    private Sensor[] findSensors(String[] strArr) {
        Sensor[] sensorArr = new Sensor[5];
        HashMap hashMap = new HashMap();
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            if (!hashMap.containsKey(str)) {
                hashMap.put(str, findSensor(str));
            }
            sensorArr[i] = (Sensor) hashMap.get(str);
        }
        return sensorArr;
    }

    public static Sensor findSensor(SensorManager sensorManager, String str, String str2) {
        boolean z = !TextUtils.isEmpty(str2);
        boolean z2 = !TextUtils.isEmpty(str);
        if (!z && !z2) {
            return null;
        }
        for (Sensor next : sensorManager.getSensorList(-1)) {
            if ((!z || str2.equals(next.getName())) && (!z2 || str.equals(next.getStringType()))) {
                return next;
            }
        }
        return null;
    }

    public void setListening(boolean z, boolean z2) {
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            updateListening();
        }
    }

    public void setListening(boolean z, boolean z2, boolean z3) {
        boolean z4 = !this.mSelectivelyRegisterProxSensors || z3;
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2 || this.mListeningProxSensors != z4) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            this.mListeningProxSensors = z4;
            updateListening();
        }
    }

    private void updateListening() {
        boolean z = false;
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            boolean z2 = this.mListening && (!triggerSensor.mRequiresTouchscreen || this.mListeningTouchScreenSensors) && (!triggerSensor.mRequiresProx || this.mListeningProxSensors);
            triggerSensor.setListening(z2);
            if (z2) {
                z = true;
            }
        }
        if (!z) {
            this.mSecureSettings.unregisterContentObserver(this.mSettingsObserver);
        } else if (!this.mSettingRegistered) {
            for (TriggerSensor registerSettingsObserver : this.mTriggerSensors) {
                registerSettingsObserver.registerSettingsObserver(this.mSettingsObserver);
            }
        }
        this.mSettingRegistered = z;
    }

    public void setTouchscreenSensorsListening(boolean z) {
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            if (triggerSensor.mRequiresTouchscreen) {
                triggerSensor.setListening(z);
            }
        }
    }

    public void onUserSwitched() {
        for (TriggerSensor updateListening : this.mTriggerSensors) {
            updateListening.updateListening();
        }
    }

    /* access modifiers changed from: package-private */
    public void onScreenState(int i) {
        ProximitySensor proximitySensor = this.mProximitySensor;
        boolean z = true;
        if (!(i == 3 || i == 4 || i == 1)) {
            z = false;
        }
        proximitySensor.setSecondarySafe(z);
    }

    public void setProxListening(boolean z) {
        if (this.mProximitySensor.isRegistered() && z) {
            this.mProximitySensor.alertListeners();
        } else if (z) {
            this.mProximitySensor.resume();
        } else {
            this.mProximitySensor.pause();
        }
    }

    public void ignoreTouchScreenSensorsSettingInterferingWithDocking(boolean z) {
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            if (triggerSensor.mRequiresTouchscreen) {
                triggerSensor.ignoreSetting(z);
            }
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("mListening=" + this.mListening);
        printWriter.println("mDevicePosture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
        printWriter.println("mListeningTouchScreenSensors=" + this.mListeningTouchScreenSensors);
        printWriter.println("mSelectivelyRegisterProxSensors=" + this.mSelectivelyRegisterProxSensors);
        printWriter.println("mListeningProxSensors=" + this.mListeningProxSensors);
        printWriter.println("mScreenOffUdfpsEnabled=" + this.mScreenOffUdfpsEnabled);
        printWriter.println("mUdfpsEnrolled=" + this.mUdfpsEnrolled);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        TriggerSensor[] triggerSensorArr = this.mTriggerSensors;
        int length = triggerSensorArr.length;
        for (int i = 0; i < length; i++) {
            indentingPrintWriter.println("Sensor: " + triggerSensorArr[i].toString());
        }
        indentingPrintWriter.println("ProxSensor: " + this.mProximitySensor.toString());
    }

    public Boolean isProximityCurrentlyNear() {
        return this.mProximitySensor.isNear();
    }

    class TriggerSensor extends TriggerEventListener {
        boolean mConfigured;
        protected boolean mDisabled;
        protected boolean mIgnoresSetting;
        private int mPosture;
        final int mPulseReason;
        protected boolean mRegistered;
        private final boolean mReportsTouchCoordinates;
        protected boolean mRequested;
        /* access modifiers changed from: private */
        public final boolean mRequiresProx;
        /* access modifiers changed from: private */
        public final boolean mRequiresTouchscreen;
        final Sensor[] mSensors;
        private final String mSetting;
        private final boolean mSettingDefault;

        TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3) {
            this(dozeSensors, sensor, str, true, z, i, z2, z3, false, false);
        }

        TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6) {
            this(new Sensor[]{sensor}, str, z, z2, i, z3, z4, z5, z6, 0);
        }

        TriggerSensor(Sensor[] sensorArr, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6, int i2) {
            this.mSensors = sensorArr;
            this.mSetting = str;
            this.mSettingDefault = z;
            this.mConfigured = z2;
            this.mPulseReason = i;
            this.mReportsTouchCoordinates = z3;
            this.mRequiresTouchscreen = z4;
            this.mIgnoresSetting = z5;
            this.mRequiresProx = z6;
            this.mPosture = i2;
        }

        public boolean setPosture(int i) {
            int i2 = this.mPosture;
            if (i2 != i) {
                Sensor[] sensorArr = this.mSensors;
                if (sensorArr.length >= 2 && i < sensorArr.length) {
                    Sensor sensor = sensorArr[i2];
                    Sensor sensor2 = sensorArr[i];
                    if (Objects.equals(sensor, sensor2)) {
                        this.mPosture = i;
                        return false;
                    }
                    if (this.mRegistered) {
                        boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, sensor);
                        if (DozeSensors.DEBUG) {
                            Log.d(DozeSensors.TAG, "posture changed, cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                        }
                        this.mRegistered = false;
                    }
                    this.mPosture = i;
                    updateListening();
                    DozeSensors.this.mDozeLog.tracePostureChanged(this.mPosture, "DozeSensors swap {" + sensor + "} => {" + sensor2 + "}, mRegistered=" + this.mRegistered);
                    return true;
                }
            }
            return false;
        }

        public void setListening(boolean z) {
            if (this.mRequested != z) {
                this.mRequested = z;
                updateListening();
            }
        }

        public void setDisabled(boolean z) {
            if (this.mDisabled != z) {
                this.mDisabled = z;
                updateListening();
            }
        }

        public void ignoreSetting(boolean z) {
            if (this.mIgnoresSetting != z) {
                this.mIgnoresSetting = z;
                updateListening();
            }
        }

        public void setConfigured(boolean z) {
            if (this.mConfigured != z) {
                this.mConfigured = z;
                updateListening();
            }
        }

        public void updateListening() {
            Sensor sensor = this.mSensors[this.mPosture];
            if (this.mConfigured && sensor != null) {
                if (!this.mRequested || this.mDisabled || (!enabledBySetting() && !this.mIgnoresSetting)) {
                    if (this.mRegistered) {
                        boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, sensor);
                        if (DozeSensors.DEBUG) {
                            Log.d(DozeSensors.TAG, "cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                        }
                        this.mRegistered = false;
                    }
                } else if (!this.mRegistered) {
                    this.mRegistered = DozeSensors.this.mSensorManager.requestTriggerSensor(this, sensor);
                    if (DozeSensors.DEBUG) {
                        Log.d(DozeSensors.TAG, "requestTriggerSensor[" + sensor + "] " + this.mRegistered);
                    }
                } else if (DozeSensors.DEBUG) {
                    Log.d(DozeSensors.TAG, "requestTriggerSensor[" + sensor + "] already registered");
                }
            }
        }

        /* access modifiers changed from: protected */
        public boolean enabledBySetting() {
            if (!DozeSensors.this.mConfig.enabled(-2)) {
                return false;
            }
            if (TextUtils.isEmpty(this.mSetting)) {
                return true;
            }
            if (DozeSensors.this.mSecureSettings.getIntForUser(this.mSetting, this.mSettingDefault ? 1 : 0, -2) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("{mRegistered=");
            sb.append(this.mRegistered).append(", mRequested=").append(this.mRequested).append(", mDisabled=").append(this.mDisabled).append(", mConfigured=").append(this.mConfigured).append(", mIgnoresSetting=").append(this.mIgnoresSetting).append(", mSensors=").append(Arrays.toString((Object[]) this.mSensors));
            if (this.mSensors.length > 2) {
                sb.append(", mPosture=").append(DevicePostureController.devicePostureToString(DozeSensors.this.mDevicePosture));
            }
            return sb.append("}").toString();
        }

        public void onTrigger(TriggerEvent triggerEvent) {
            Sensor sensor = this.mSensors[this.mPosture];
            DozeSensors.this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new DozeSensors$TriggerSensor$$ExternalSyntheticLambda0(this, triggerEvent, sensor)));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onTrigger$0$com-android-systemui-doze-DozeSensors$TriggerSensor */
        public /* synthetic */ void mo32459xcd5a5dfe(TriggerEvent triggerEvent, Sensor sensor) {
            float f;
            float f2;
            if (DozeSensors.DEBUG) {
                Log.d(DozeSensors.TAG, "onTrigger: " + triggerEventToString(triggerEvent));
            }
            if (sensor != null && sensor.getType() == 25) {
                DozeSensors.UI_EVENT_LOGGER.log(DozeSensorsUiEvent.ACTION_AMBIENT_GESTURE_PICKUP);
            }
            this.mRegistered = false;
            if (!this.mReportsTouchCoordinates || triggerEvent.values.length < 2) {
                f = -1.0f;
                f2 = -1.0f;
            } else {
                f = triggerEvent.values[0];
                f2 = triggerEvent.values[1];
            }
            DozeSensors.this.mSensorCallback.onSensorPulse(this.mPulseReason, f, f2, triggerEvent.values);
            if (!this.mRegistered) {
                updateListening();
            }
        }

        public void registerSettingsObserver(ContentObserver contentObserver) {
            if (this.mConfigured && !TextUtils.isEmpty(this.mSetting)) {
                DozeSensors.this.mSecureSettings.registerContentObserverForUser(this.mSetting, DozeSensors.this.mSettingsObserver, -1);
            }
        }

        /* access modifiers changed from: protected */
        public String triggerEventToString(TriggerEvent triggerEvent) {
            if (triggerEvent == null) {
                return null;
            }
            StringBuilder append = new StringBuilder("SensorEvent[").append(triggerEvent.timestamp).append(',').append(triggerEvent.sensor.getName());
            if (triggerEvent.values != null) {
                for (float append2 : triggerEvent.values) {
                    append.append(',').append(append2);
                }
            }
            return append.append(']').toString();
        }
    }

    class PluginSensor extends TriggerSensor implements SensorManagerPlugin.SensorEventListener {
        private long mDebounce;
        final SensorManagerPlugin.Sensor mPluginSensor;
        final /* synthetic */ DozeSensors this$0;

        PluginSensor(DozeSensors dozeSensors, SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3) {
            this(dozeSensors, sensor, str, z, i, z2, z3, 0);
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        PluginSensor(com.android.systemui.doze.DozeSensors r10, com.android.systemui.plugins.SensorManagerPlugin.Sensor r11, java.lang.String r12, boolean r13, int r14, boolean r15, boolean r16, long r17) {
            /*
                r9 = this;
                r8 = r9
                r1 = r10
                r8.this$0 = r1
                r2 = 0
                r0 = r9
                r3 = r12
                r4 = r13
                r5 = r14
                r6 = r15
                r7 = r16
                r0.<init>(r1, r2, r3, r4, r5, r6, r7)
                r0 = r11
                r8.mPluginSensor = r0
                r0 = r17
                r8.mDebounce = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors.PluginSensor.<init>(com.android.systemui.doze.DozeSensors, com.android.systemui.plugins.SensorManagerPlugin$Sensor, java.lang.String, boolean, int, boolean, boolean, long):void");
        }

        public void updateListening() {
            if (this.mConfigured) {
                AsyncSensorManager access$200 = this.this$0.mSensorManager;
                if (this.mRequested && !this.mDisabled && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                    access$200.registerPluginListener(this.mPluginSensor, this);
                    this.mRegistered = true;
                    if (DozeSensors.DEBUG) {
                        Log.d(DozeSensors.TAG, "registerPluginListener");
                    }
                } else if (this.mRegistered) {
                    access$200.unregisterPluginListener(this.mPluginSensor, this);
                    this.mRegistered = false;
                    if (DozeSensors.DEBUG) {
                        Log.d(DozeSensors.TAG, "unregisterPluginListener");
                    }
                }
            }
        }

        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mPluginSensor + "}";
        }

        private String triggerEventToString(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (sensorEvent == null) {
                return null;
            }
            StringBuilder append = new StringBuilder("PluginTriggerEvent[").append((Object) sensorEvent.getSensor()).append(',').append(sensorEvent.getVendorType());
            if (sensorEvent.getValues() != null) {
                for (float append2 : sensorEvent.getValues()) {
                    append.append(',').append(append2);
                }
            }
            return append.append(']').toString();
        }

        public void onSensorChanged(SensorManagerPlugin.SensorEvent sensorEvent) {
            this.this$0.mDozeLog.traceSensor(this.mPulseReason);
            this.this$0.mHandler.post(this.this$0.mWakeLock.wrap(new DozeSensors$PluginSensor$$ExternalSyntheticLambda0(this, sensorEvent)));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSensorChanged$0$com-android-systemui-doze-DozeSensors$PluginSensor */
        public /* synthetic */ void mo32454x4d4cfd3f(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (SystemClock.uptimeMillis() < this.this$0.mDebounceFrom + this.mDebounce) {
                Log.d(DozeSensors.TAG, "onSensorEvent dropped: " + triggerEventToString(sensorEvent));
                return;
            }
            if (DozeSensors.DEBUG) {
                Log.d(DozeSensors.TAG, "onSensorEvent: " + triggerEventToString(sensorEvent));
            }
            this.this$0.mSensorCallback.onSensorPulse(this.mPulseReason, -1.0f, -1.0f, sensorEvent.getValues());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-doze-DozeSensors  reason: not valid java name */
    public /* synthetic */ void m2742lambda$new$1$comandroidsystemuidozeDozeSensors(int i) {
        if (this.mDevicePosture != i) {
            this.mDevicePosture = i;
            for (TriggerSensor posture : this.mTriggerSensors) {
                posture.setPosture(this.mDevicePosture);
            }
        }
    }
}
