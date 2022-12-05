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
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class DozeSensors {
    private static final boolean DEBUG = DozeService.DEBUG;
    private static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    private final Callback mCallback;
    private final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    private long mDebounceFrom;
    private final Handler mHandler;
    private boolean mListening;
    private boolean mListeningProxSensors;
    private boolean mListeningTouchScreenSensors;
    private final Consumer<Boolean> mProxCallback;
    private final ProximitySensor mProximitySensor;
    private final boolean mScreenOffUdfpsEnabled;
    private final SecureSettings mSecureSettings;
    private boolean mSelectivelyRegisterProxSensors;
    private final AsyncSensorManager mSensorManager;
    protected TriggerSensor[] mSensors;
    private boolean mSettingRegistered;
    private final ContentObserver mSettingsObserver;
    private final WakeLock mWakeLock;

    /* loaded from: classes.dex */
    public interface Callback {
        void onSensorPulse(int i, float f, float f2, float[] fArr);
    }

    /* loaded from: classes.dex */
    public enum DozeSensorsUiEvent implements UiEventLogger.UiEventEnum {
        ACTION_AMBIENT_GESTURE_PICKUP(459);
        
        private final int mId;

        DozeSensorsUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DozeSensors(Context context, AsyncSensorManager asyncSensorManager, DozeParameters dozeParameters, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, Callback callback, Consumer<Boolean> consumer, DozeLog dozeLog, ProximitySensor proximitySensor, SecureSettings secureSettings, AuthController authController) {
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) { // from class: com.android.systemui.doze.DozeSensors.1
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (i2 != ActivityManager.getCurrentUser()) {
                    return;
                }
                for (TriggerSensor triggerSensor : DozeSensors.this.mSensors) {
                    triggerSensor.updateListening();
                }
            }
        };
        this.mContext = context;
        this.mSensorManager = asyncSensorManager;
        this.mConfig = ambientDisplayConfiguration;
        this.mWakeLock = wakeLock;
        this.mProxCallback = consumer;
        this.mSecureSettings = secureSettings;
        this.mCallback = callback;
        this.mProximitySensor = proximitySensor;
        proximitySensor.setTag("DozeSensors");
        boolean selectivelyRegisterSensorsUsingProx = dozeParameters.getSelectivelyRegisterSensorsUsingProx();
        this.mSelectivelyRegisterProxSensors = selectivelyRegisterSensorsUsingProx;
        this.mListeningProxSensors = !selectivelyRegisterSensorsUsingProx;
        boolean screenOffUdfpsEnabled = ambientDisplayConfiguration.screenOffUdfpsEnabled(KeyguardUpdateMonitor.getCurrentUser());
        this.mScreenOffUdfpsEnabled = screenOffUdfpsEnabled;
        boolean isUdfpsEnrolled = authController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
        boolean alwaysOnEnabled = ambientDisplayConfiguration.alwaysOnEnabled(-2);
        TriggerSensor[] triggerSensorArr = new TriggerSensor[9];
        triggerSensorArr[0] = new TriggerSensor(this, asyncSensorManager.getDefaultSensor(17), null, dozeParameters.getPulseOnSigMotion(), 2, false, false, dozeLog);
        triggerSensorArr[1] = new TriggerSensor(asyncSensorManager.getDefaultSensor(25), "doze_pulse_on_pick_up", true, ambientDisplayConfiguration.dozePickupSensorAvailable(), 3, false, false, false, false, dozeLog);
        triggerSensorArr[2] = new TriggerSensor(this, findSensorWithType(ambientDisplayConfiguration.doubleTapSensorType()), "doze_pulse_on_double_tap", true, 4, dozeParameters.doubleTapReportsTouchCoordinates(), true, dozeLog);
        triggerSensorArr[3] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.tapSensorType()), "doze_tap_gesture", true, true, 9, false, true, false, dozeParameters.singleTapUsesProx(), dozeLog);
        triggerSensorArr[4] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.longPressSensorType()), "doze_pulse_on_long_press", false, true, 5, true, true, false, dozeParameters.longPressUsesProx(), dozeLog);
        triggerSensorArr[5] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.udfpsLongPressSensorType()), "doze_pulse_on_auth", true, isUdfpsEnrolled && (alwaysOnEnabled || screenOffUdfpsEnabled), 10, true, true, false, dozeParameters.longPressUsesProx(), dozeLog);
        triggerSensorArr[6] = new PluginSensor(this, new SensorManagerPlugin.Sensor(2), "doze_wake_display_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable() && alwaysOnEnabled, 7, false, false, dozeLog);
        triggerSensorArr[7] = new PluginSensor(new SensorManagerPlugin.Sensor(1), "doze_wake_screen_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable(), 8, false, false, ambientDisplayConfiguration.getWakeLockScreenDebounce(), dozeLog);
        triggerSensorArr[8] = new TriggerSensor(this, findSensorWithType(ambientDisplayConfiguration.quickPickupSensorType()), "doze_quick_pickup_gesture", true, ambientDisplayConfiguration.quickPickupSensorEnabled(KeyguardUpdateMonitor.getCurrentUser()) && isUdfpsEnrolled, 11, false, false, dozeLog);
        this.mSensors = triggerSensorArr;
        setProxListening(false);
        proximitySensor.register(new ThresholdSensor.Listener() { // from class: com.android.systemui.doze.DozeSensors$$ExternalSyntheticLambda0
            @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
            public final void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
                DozeSensors.this.lambda$new$0(thresholdSensorEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
        if (thresholdSensorEvent != null) {
            this.mProxCallback.accept(Boolean.valueOf(!thresholdSensorEvent.getBelow()));
        }
    }

    public void destroy() {
        for (TriggerSensor triggerSensor : this.mSensors) {
            triggerSensor.setListening(false);
        }
        this.mProximitySensor.pause();
    }

    public void requestTemporaryDisable() {
        this.mDebounceFrom = SystemClock.uptimeMillis();
    }

    private Sensor findSensorWithType(String str) {
        return findSensorWithType(this.mSensorManager, str);
    }

    public static Sensor findSensorWithType(SensorManager sensorManager, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Sensor sensor : sensorManager.getSensorList(-1)) {
            if (str.equals(sensor.getStringType())) {
                return sensor;
            }
        }
        return null;
    }

    public void setListening(boolean z, boolean z2) {
        if (this.mListening == z && this.mListeningTouchScreenSensors == z2) {
            return;
        }
        this.mListening = z;
        this.mListeningTouchScreenSensors = z2;
        updateListening();
    }

    public void setListening(boolean z, boolean z2, boolean z3) {
        boolean z4 = !this.mSelectivelyRegisterProxSensors || z3;
        if (this.mListening == z && this.mListeningTouchScreenSensors == z2 && this.mListeningProxSensors == z4) {
            return;
        }
        this.mListening = z;
        this.mListeningTouchScreenSensors = z2;
        this.mListeningProxSensors = z4;
        updateListening();
    }

    private void updateListening() {
        TriggerSensor[] triggerSensorArr;
        boolean z = false;
        for (TriggerSensor triggerSensor : this.mSensors) {
            boolean z2 = this.mListening && (!triggerSensor.mRequiresTouchscreen || this.mListeningTouchScreenSensors) && (!triggerSensor.mRequiresProx || this.mListeningProxSensors);
            triggerSensor.setListening(z2);
            if (z2) {
                z = true;
            }
        }
        if (!z) {
            this.mSecureSettings.unregisterContentObserver(this.mSettingsObserver);
        } else if (!this.mSettingRegistered) {
            for (TriggerSensor triggerSensor2 : this.mSensors) {
                triggerSensor2.registerSettingsObserver(this.mSettingsObserver);
            }
        }
        this.mSettingRegistered = z;
    }

    public void onUserSwitched() {
        for (TriggerSensor triggerSensor : this.mSensors) {
            triggerSensor.updateListening();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onScreenState(int i) {
        ProximitySensor proximitySensor = this.mProximitySensor;
        boolean z = true;
        if (i != 3 && i != 4 && i != 1) {
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

    public void dump(PrintWriter printWriter) {
        TriggerSensor[] triggerSensorArr;
        printWriter.println("mListening=" + this.mListening);
        printWriter.println("mListeningTouchScreenSensors=" + this.mListeningTouchScreenSensors);
        printWriter.println("mSelectivelyRegisterProxSensors=" + this.mSelectivelyRegisterProxSensors);
        printWriter.println("mListeningProxSensors=" + this.mListeningProxSensors);
        printWriter.println("mScreenOffUdfpsEnabled=" + this.mScreenOffUdfpsEnabled);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        for (TriggerSensor triggerSensor : this.mSensors) {
            indentingPrintWriter.println("Sensor: " + triggerSensor.toString());
        }
        indentingPrintWriter.println("ProxSensor: " + this.mProximitySensor.toString());
    }

    public Boolean isProximityCurrentlyNear() {
        return this.mProximitySensor.isNear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class TriggerSensor extends TriggerEventListener {
        final boolean mConfigured;
        protected boolean mDisabled;
        protected final DozeLog mDozeLog;
        protected boolean mIgnoresSetting;
        final int mPulseReason;
        protected boolean mRegistered;
        private final boolean mReportsTouchCoordinates;
        protected boolean mRequested;
        private final boolean mRequiresProx;
        private final boolean mRequiresTouchscreen;
        final Sensor mSensor;
        private final String mSetting;
        private final boolean mSettingDefault;

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, DozeLog dozeLog) {
            this(dozeSensors, sensor, str, true, z, i, z2, z3, dozeLog);
        }

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, DozeLog dozeLog) {
            this(sensor, str, z, z2, i, z3, z4, false, false, dozeLog);
        }

        private TriggerSensor(Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6, DozeLog dozeLog) {
            this.mSensor = sensor;
            this.mSetting = str;
            this.mSettingDefault = z;
            this.mConfigured = z2;
            this.mPulseReason = i;
            this.mReportsTouchCoordinates = z3;
            this.mRequiresTouchscreen = z4;
            this.mIgnoresSetting = z5;
            this.mRequiresProx = z6;
            this.mDozeLog = dozeLog;
        }

        public void setListening(boolean z) {
            if (this.mRequested == z) {
                return;
            }
            this.mRequested = z;
            updateListening();
        }

        public void updateListening() {
            if (!this.mConfigured || this.mSensor == null) {
                return;
            }
            if (this.mRequested && !this.mDisabled && (enabledBySetting() || this.mIgnoresSetting)) {
                if (!this.mRegistered) {
                    this.mRegistered = DozeSensors.this.mSensorManager.requestTriggerSensor(this, this.mSensor);
                    if (!DozeSensors.DEBUG) {
                        return;
                    }
                    Log.d("DozeSensors", "requestTriggerSensor[" + this.mSensor + "] " + this.mRegistered);
                } else if (!DozeSensors.DEBUG) {
                } else {
                    Log.d("DozeSensors", "requestTriggerSensor[" + this.mSensor + "] already registered");
                }
            } else if (!this.mRegistered) {
            } else {
                boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, this.mSensor);
                if (DozeSensors.DEBUG) {
                    Log.d("DozeSensors", "cancelTriggerSensor[" + this.mSensor + "] " + cancelTriggerSensor);
                }
                this.mRegistered = false;
            }
        }

        protected boolean enabledBySetting() {
            if (!DozeSensors.this.mConfig.enabled(-2)) {
                return false;
            }
            return TextUtils.isEmpty(this.mSetting) || DozeSensors.this.mSecureSettings.getIntForUser(this.mSetting, this.mSettingDefault ? 1 : 0, -2) != 0;
        }

        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mSensor + "}";
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(final TriggerEvent triggerEvent) {
            this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new Runnable() { // from class: com.android.systemui.doze.DozeSensors$TriggerSensor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DozeSensors.TriggerSensor.this.lambda$onTrigger$0(triggerEvent);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:16:0x005c  */
        /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onTrigger$0(TriggerEvent triggerEvent) {
            float f;
            if (DozeSensors.DEBUG) {
                Log.d("DozeSensors", "onTrigger: " + triggerEventToString(triggerEvent));
            }
            Sensor sensor = this.mSensor;
            if (sensor != null && sensor.getType() == 25) {
                DozeSensors.UI_EVENT_LOGGER.log(DozeSensorsUiEvent.ACTION_AMBIENT_GESTURE_PICKUP);
            }
            this.mRegistered = false;
            float f2 = -1.0f;
            if (this.mReportsTouchCoordinates) {
                float[] fArr = triggerEvent.values;
                if (fArr.length >= 2) {
                    f2 = fArr[0];
                    f = fArr[1];
                    DozeSensors.this.mCallback.onSensorPulse(this.mPulseReason, f2, f, triggerEvent.values);
                    if (!this.mRegistered) {
                        return;
                    }
                    updateListening();
                    return;
                }
            }
            f = -1.0f;
            DozeSensors.this.mCallback.onSensorPulse(this.mPulseReason, f2, f, triggerEvent.values);
            if (!this.mRegistered) {
            }
        }

        public void registerSettingsObserver(ContentObserver contentObserver) {
            if (!this.mConfigured || TextUtils.isEmpty(this.mSetting)) {
                return;
            }
            DozeSensors.this.mSecureSettings.registerContentObserverForUser(this.mSetting, DozeSensors.this.mSettingsObserver, -1);
        }

        protected String triggerEventToString(TriggerEvent triggerEvent) {
            if (triggerEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("SensorEvent[");
            sb.append(triggerEvent.timestamp);
            sb.append(',');
            sb.append(triggerEvent.sensor.getName());
            if (triggerEvent.values != null) {
                for (int i = 0; i < triggerEvent.values.length; i++) {
                    sb.append(',');
                    sb.append(triggerEvent.values[i]);
                }
            }
            sb.append(']');
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class PluginSensor extends TriggerSensor implements SensorManagerPlugin.SensorEventListener {
        private long mDebounce;
        final SensorManagerPlugin.Sensor mPluginSensor;

        PluginSensor(DozeSensors dozeSensors, SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, DozeLog dozeLog) {
            this(sensor, str, z, i, z2, z3, 0L, dozeLog);
        }

        PluginSensor(SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, long j, DozeLog dozeLog) {
            super(DozeSensors.this, null, str, z, i, z2, z3, dozeLog);
            this.mPluginSensor = sensor;
            this.mDebounce = j;
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor
        public void updateListening() {
            if (!this.mConfigured) {
                return;
            }
            AsyncSensorManager asyncSensorManager = DozeSensors.this.mSensorManager;
            if (this.mRequested && !this.mDisabled && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                asyncSensorManager.registerPluginListener(this.mPluginSensor, this);
                this.mRegistered = true;
                if (!DozeSensors.DEBUG) {
                    return;
                }
                Log.d("DozeSensors", "registerPluginListener");
            } else if (!this.mRegistered) {
            } else {
                asyncSensorManager.unregisterPluginListener(this.mPluginSensor, this);
                this.mRegistered = false;
                if (!DozeSensors.DEBUG) {
                    return;
                }
                Log.d("DozeSensors", "unregisterPluginListener");
            }
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor
        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mPluginSensor + "}";
        }

        private String triggerEventToString(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (sensorEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("PluginTriggerEvent[");
            sb.append(sensorEvent.getSensor());
            sb.append(',');
            sb.append(sensorEvent.getVendorType());
            if (sensorEvent.getValues() != null) {
                for (int i = 0; i < sensorEvent.getValues().length; i++) {
                    sb.append(',');
                    sb.append(sensorEvent.getValues()[i]);
                }
            }
            sb.append(']');
            return sb.toString();
        }

        @Override // com.android.systemui.plugins.SensorManagerPlugin.SensorEventListener
        public void onSensorChanged(final SensorManagerPlugin.SensorEvent sensorEvent) {
            this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new Runnable() { // from class: com.android.systemui.doze.DozeSensors$PluginSensor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DozeSensors.PluginSensor.this.lambda$onSensorChanged$0(sensorEvent);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSensorChanged$0(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (SystemClock.uptimeMillis() >= DozeSensors.this.mDebounceFrom + this.mDebounce) {
                if (DozeSensors.DEBUG) {
                    Log.d("DozeSensors", "onSensorEvent: " + triggerEventToString(sensorEvent));
                }
                DozeSensors.this.mCallback.onSensorPulse(this.mPulseReason, -1.0f, -1.0f, sensorEvent.getValues());
                return;
            }
            Log.d("DozeSensors", "onSensorEvent dropped: " + triggerEventToString(sensorEvent));
        }
    }
}
