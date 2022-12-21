package com.android.systemui.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.BrightnessSensor;
import com.android.systemui.doze.dagger.DozeScope;
import com.android.systemui.doze.dagger.WrappedService;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.nothing.systemui.doze.DozeScreenBrightnessEx;
import java.p026io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;

@DozeScope
public class DozeScreenBrightness extends BroadcastReceiver implements DozeMachine.Part, SensorEventListener {
    protected static final String ACTION_AOD_BRIGHTNESS = "com.android.systemui.doze.AOD_BRIGHTNESS";
    protected static final String BRIGHTNESS_BUCKET = "brightness_bucket";
    private static final boolean DEBUG_AOD_BRIGHTNESS = SystemProperties.getBoolean("debug.aod_brightness", false);
    private final Context mContext;
    private int mDebugBrightnessBucket = -1;
    private int mDefaultDozeBrightness;
    /* access modifiers changed from: private */
    public int mDevicePosture;
    private final DevicePostureController.Callback mDevicePostureCallback;
    private final DevicePostureController mDevicePostureController;
    private final DozeHost mDozeHost;
    /* access modifiers changed from: private */
    public final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    private final DozeMachine.Service mDozeService;
    private final Handler mHandler;
    private int mLastSensorValue = -1;
    /* access modifiers changed from: private */
    public final Optional<Sensor>[] mLightSensorOptional;
    private boolean mPaused = false;
    /* access modifiers changed from: private */
    public boolean mRegistered;
    private final int mScreenBrightnessDim;
    private final float mScreenBrightnessMinimumDimAmountFloat;
    private boolean mScreenOff = false;
    private final SensorManager mSensorManager;
    private final int[] mSensorToBrightness;
    private final int[] mSensorToScrimOpacity;
    private DozeMachine.State mState = DozeMachine.State.UNINITIALIZED;
    private final WakefulnessLifecycle mWakefulnessLifecycle;

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Inject
    public DozeScreenBrightness(Context context, @WrappedService DozeMachine.Service service, AsyncSensorManager asyncSensorManager, @BrightnessSensor Optional<Sensor>[] optionalArr, DozeHost dozeHost, Handler handler, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, WakefulnessLifecycle wakefulnessLifecycle, DozeParameters dozeParameters, DevicePostureController devicePostureController, DozeLog dozeLog) {
        C20601 r0 = new DevicePostureController.Callback() {
            public void onPostureChanged(int i) {
                if (DozeScreenBrightness.this.mDevicePosture != i && DozeScreenBrightness.this.mLightSensorOptional.length >= 2 && i < DozeScreenBrightness.this.mLightSensorOptional.length) {
                    Sensor sensor = (Sensor) DozeScreenBrightness.this.mLightSensorOptional[DozeScreenBrightness.this.mDevicePosture].get();
                    Sensor sensor2 = (Sensor) DozeScreenBrightness.this.mLightSensorOptional[i].get();
                    if (Objects.equals(sensor, sensor2)) {
                        int unused = DozeScreenBrightness.this.mDevicePosture = i;
                        return;
                    }
                    if (DozeScreenBrightness.this.mRegistered) {
                        DozeScreenBrightness.this.setLightSensorEnabled(false);
                        int unused2 = DozeScreenBrightness.this.mDevicePosture = i;
                        DozeScreenBrightness.this.setLightSensorEnabled(true);
                    } else {
                        int unused3 = DozeScreenBrightness.this.mDevicePosture = i;
                    }
                    DozeScreenBrightness.this.mDozeLog.tracePostureChanged(DozeScreenBrightness.this.mDevicePosture, "DozeScreenBrightness swap {" + sensor + "} => {" + sensor2 + "}, mRegistered=" + DozeScreenBrightness.this.mRegistered);
                }
            }
        };
        this.mDevicePostureCallback = r0;
        this.mContext = context;
        this.mDozeService = service;
        this.mSensorManager = asyncSensorManager;
        this.mLightSensorOptional = optionalArr;
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mDozeParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mHandler = handler;
        this.mDozeLog = dozeLog;
        this.mScreenBrightnessMinimumDimAmountFloat = context.getResources().getFloat(17105101);
        this.mDefaultDozeBrightness = alwaysOnDisplayPolicy.defaultDozeBrightness;
        this.mScreenBrightnessDim = alwaysOnDisplayPolicy.dimBrightness;
        this.mSensorToBrightness = alwaysOnDisplayPolicy.screenBrightnessArray;
        this.mSensorToScrimOpacity = alwaysOnDisplayPolicy.dimmingScrimArray;
        devicePostureController.addCallback(r0);
    }

    /* renamed from: com.android.systemui.doze.DozeScreenBrightness$2 */
    static /* synthetic */ class C20612 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.systemui.doze.DozeMachine$State[] r0 = com.android.systemui.doze.DozeMachine.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$doze$DozeMachine$State = r0
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_REQUEST_PULSE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_DOCKED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenBrightness.C20612.<clinit>():void");
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        this.mState = state2;
        boolean z = true;
        switch (C20612.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                resetBrightnessToDefault();
                break;
            case 2:
            case 3:
            case 4:
                setLightSensorEnabled(true);
                break;
            case 5:
                setLightSensorEnabled(false);
                resetBrightnessToDefault();
                break;
            case 6:
                setLightSensorEnabled(false);
                break;
            case 7:
                onDestroy();
                break;
        }
        if (state2 != DozeMachine.State.FINISH) {
            setScreenOff(state2 == DozeMachine.State.DOZE);
            if (state2 != DozeMachine.State.DOZE_AOD_PAUSED) {
                z = false;
            }
            setPaused(z);
        }
    }

    private void onDestroy() {
        setLightSensorEnabled(false);
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        Trace.beginSection("DozeScreenBrightness.onSensorChanged" + sensorEvent.values[0]);
        try {
            if (this.mRegistered) {
                this.mLastSensorValue = (int) sensorEvent.values[0];
                updateBrightnessAndReady(false);
            }
        } finally {
            Trace.endSection();
        }
    }

    public void updateBrightnessAndReady(boolean z) {
        int i;
        int i2 = -1;
        if (z || this.mRegistered || this.mDebugBrightnessBucket != -1) {
            int i3 = this.mDebugBrightnessBucket;
            if (i3 == -1) {
                i = this.mLastSensorValue;
            } else {
                i = i3;
            }
            boolean updateDozeBrightness = DozeScreenBrightnessEx.updateDozeBrightness(this.mDozeService, i3, this.mLastSensorValue);
            if (!isLightSensorPresent()) {
                i2 = 0;
            } else if (updateDozeBrightness) {
                i2 = computeScrimOpacity(i);
            }
            if (i2 >= 0) {
                this.mDozeHost.setAodDimmingScrim(((float) i2) / 255.0f);
            }
        }
    }

    private boolean lightSensorSupportsCurrentPosture() {
        Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
        return optionalArr != null && this.mDevicePosture < optionalArr.length;
    }

    private boolean isLightSensorPresent() {
        if (lightSensorSupportsCurrentPosture()) {
            return this.mLightSensorOptional[this.mDevicePosture].isPresent();
        }
        Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
        return optionalArr != null && optionalArr[0].isPresent();
    }

    private Sensor getLightSensor() {
        if (!lightSensorSupportsCurrentPosture()) {
            return null;
        }
        return this.mLightSensorOptional[this.mDevicePosture].get();
    }

    private int computeScrimOpacity(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToScrimOpacity;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    private int computeBrightness(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToBrightness;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    private void resetBrightnessToDefault() {
        this.mDozeService.setDozeScreenBrightness(clampToDimBrightnessForScreenOff(clampToUserSetting(this.mDefaultDozeBrightness)));
        this.mDozeHost.setAodDimmingScrim(0.0f);
    }

    private int clampToUserSetting(int i) {
        return Math.min(i, Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness", Integer.MAX_VALUE, -2));
    }

    private int clampToDimBrightnessForScreenOff(int i) {
        return (!((this.mDozeParameters.shouldClampToDimBrightness() || this.mWakefulnessLifecycle.getWakefulness() == 3) && this.mState == DozeMachine.State.INITIALIZED) || this.mWakefulnessLifecycle.getLastSleepReason() != 2) ? i : Math.max(0, Math.min(i - ((int) Math.floor((double) (this.mScreenBrightnessMinimumDimAmountFloat * 255.0f))), this.mScreenBrightnessDim));
    }

    /* access modifiers changed from: private */
    public void setLightSensorEnabled(boolean z) {
        if (z && !this.mRegistered && isLightSensorPresent()) {
            this.mRegistered = this.mSensorManager.registerListener(this, getLightSensor(), 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else if (!z && this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mLastSensorValue = -1;
        }
    }

    private void setPaused(boolean z) {
        if (this.mPaused != z) {
            this.mPaused = z;
            updateBrightnessAndReady(false);
        }
    }

    private void setScreenOff(boolean z) {
        if (this.mScreenOff != z) {
            this.mScreenOff = z;
            updateBrightnessAndReady(true);
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.mDebugBrightnessBucket = intent.getIntExtra(BRIGHTNESS_BUCKET, -1);
        updateBrightnessAndReady(false);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeScreenBrightness:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("registered=" + this.mRegistered);
        indentingPrintWriter.println("posture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
    }
}
