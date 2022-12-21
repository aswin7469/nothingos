package com.nothing.systemui.statusbar.phone;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.doze.LiftWakeGestureController;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;

public class CentralSurfacesImplEx {
    public static int DELAY_SHOW_AOD_DURATION = 400;
    private static final String MOTION_TRIGGER_SHOW_FP_ICON = "motion_trigger_show_fp_icon";
    private static String TAG = "CentralSurfacesImplEx";
    private static final String TAP_FILE = "/sys/devices/platform/soc/a94000.spi/spi_master/spi0/spi0.0/fts_parse_coordinate";
    /* access modifiers changed from: private */
    public AODController mAODController = null;
    private BiometricUnlockController mBiometricUnlockController;
    private CircleReveal mCircleReveal = null;
    private Context mContext;
    private boolean mCurShouldPlayOnOffAnimation = false;
    private DozeParameters mDozeParameters;
    private DozeServiceHost mDozeServiceHost;
    private boolean mIsNear = false;
    private boolean mIsTapSleep = false;
    private boolean mIsTapWakeUp = false;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private LiftWakeGestureController.LiftAndMotionCallback mLiftAndMotionCallback = new LiftWakeGestureController.LiftAndMotionCallback() {
        public void onLiftUp() {
            synchronized (this) {
                if (CentralSurfacesImplEx.this.mAODController.isLiftWakeEnable()) {
                    CentralSurfacesImplEx.this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:NODOZE");
                } else if (NTDependencyEx.get(DozeServiceHostEx.class) != null) {
                    ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireLiftWake();
                }
            }
        }

        public void onMotion() {
            if (NTDependencyEx.get(DozeServiceHostEx.class) != null) {
                ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireMotion();
            }
        }
    };
    private LiftWakeGestureController mLiftWakeGestureController = null;
    private Handler mMainHandler;
    private boolean mNeedDelayShowAod = false;
    /* access modifiers changed from: private */
    public NotificationPanelViewController mNotificationPanelViewController;
    /* access modifiers changed from: private */
    public PowerManager mPowerManager;
    private int mTapToSleepX = -1;
    private int mTapToSleepY = -1;

    @Inject
    public CentralSurfacesImplEx(Context context, PowerManager powerManager, KeyguardUpdateMonitor keyguardUpdateMonitor, AODController aODController, LiftWakeGestureController liftWakeGestureController) {
        this.mContext = context;
        this.mPowerManager = powerManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAODController = aODController;
        this.mLiftWakeGestureController = liftWakeGestureController;
        this.mCircleReveal = new CircleReveal(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void init(Handler handler, BiometricUnlockController biometricUnlockController, DozeServiceHost dozeServiceHost, DozeParameters dozeParameters) {
        this.mMainHandler = handler;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mDozeParameters = dozeParameters;
        this.mLiftWakeGestureController.setCallback(this.mLiftAndMotionCallback);
    }

    public void setNotificationPanelViewController(NotificationPanelViewController notificationPanelViewController) {
        this.mNotificationPanelViewController = notificationPanelViewController;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x016b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateRevealEffectEx(com.android.systemui.statusbar.SysuiStatusBarStateController r15, android.util.DisplayMetrics r16, com.android.systemui.keyguard.WakefulnessLifecycle r17, boolean r18, android.graphics.PointF r19, com.android.systemui.statusbar.LightRevealScrim r20, com.android.systemui.statusbar.PowerButtonReveal r21, boolean r22) {
        /*
            r14 = this;
            r0 = r14
            r1 = r16
            r2 = r18
            r3 = r19
            r8 = r20
            r9 = r22
            r10 = 0
            r0.mIsTapSleep = r10
            int r11 = r17.getLastWakeReason()
            int r12 = r17.getLastSleepReason()
            java.lang.String r4 = TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "wakeReason = "
            r5.<init>((java.lang.String) r6)
            java.lang.StringBuilder r5 = r5.append((int) r11)
            java.lang.String r6 = ", sleepReason: "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            java.lang.StringBuilder r5 = r5.append((int) r12)
            java.lang.String r6 = ", isWakingUp: "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            java.lang.StringBuilder r5 = r5.append((boolean) r9)
            java.lang.String r5 = r5.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)
            if (r9 == 0) goto L_0x00d9
            if (r3 == 0) goto L_0x0071
            java.lang.String r4 = TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "wakingUpComingFromTouch: "
            r5.<init>((java.lang.String) r6)
            java.lang.StringBuilder r5 = r5.append((boolean) r2)
            java.lang.String r6 = ", x= "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            float r6 = r3.x
            java.lang.StringBuilder r5 = r5.append((float) r6)
            java.lang.String r6 = "y= "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            float r6 = r3.y
            java.lang.StringBuilder r5 = r5.append((float) r6)
            java.lang.String r5 = r5.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)
        L_0x0071:
            java.lang.String r4 = TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "getXY= "
            r5.<init>((java.lang.String) r6)
            android.graphics.Point r6 = r14.getTapXY()
            java.lang.StringBuilder r5 = r5.append((java.lang.Object) r6)
            java.lang.String r5 = r5.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)
            if (r2 == 0) goto L_0x0092
            float r2 = r3.x
            float r3 = r3.y
            r4 = r2
            r5 = r3
            goto L_0x009e
        L_0x0092:
            android.graphics.Point r2 = r14.getTapXY()
            int r3 = r2.x
            float r3 = (float) r3
            int r2 = r2.y
            float r2 = (float) r2
            r5 = r2
            r4 = r3
        L_0x009e:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r6 = "updateRevealEffect:  x= "
            r3.<init>((java.lang.String) r6)
            java.lang.StringBuilder r3 = r3.append((float) r4)
            java.lang.String r6 = " y = "
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r6)
            java.lang.StringBuilder r3 = r3.append((float) r5)
            java.lang.String r3 = r3.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r2, r3)
            com.android.systemui.statusbar.CircleReveal r2 = r0.mCircleReveal
            r6 = 0
            int r3 = r1.widthPixels
            float r3 = (float) r3
            float r3 = r3 - r4
            float r3 = java.lang.Math.max((float) r4, (float) r3)
            int r7 = r1.heightPixels
            float r7 = (float) r7
            float r7 = r7 - r5
            float r7 = java.lang.Math.max((float) r5, (float) r7)
            float r7 = java.lang.Math.max((float) r3, (float) r7)
            r3 = r20
            r2.updateCircleReveal(r3, r4, r5, r6, r7)
        L_0x00d9:
            r2 = 4
            r7 = 1065353216(0x3f800000, float:1.0)
            r13 = 1
            if (r9 == 0) goto L_0x0110
            if (r11 != r13) goto L_0x00ef
            r20.setRevealEffect(r21)
            float r1 = r15.getDozeAmount()
            float r7 = r7 - r1
            r8.setRevealAmount(r7)
        L_0x00ec:
            r10 = r13
            goto L_0x0167
        L_0x00ef:
            r1 = 6
            if (r11 != r1) goto L_0x0102
            r0.mIsTapWakeUp = r10
            com.android.systemui.statusbar.CircleReveal r1 = r0.mCircleReveal
            r8.setRevealEffect(r1)
            float r1 = r15.getDozeAmount()
            float r7 = r7 - r1
            r8.setRevealAmount(r7)
            goto L_0x00ec
        L_0x0102:
            if (r11 != r2) goto L_0x010a
            com.android.systemui.statusbar.LiftReveal r1 = com.android.systemui.statusbar.LiftReveal.INSTANCE
            r8.setRevealEffect(r1)
            goto L_0x00ec
        L_0x010a:
            com.android.systemui.statusbar.LiftReveal r1 = com.android.systemui.statusbar.LiftReveal.INSTANCE
            r8.setRevealEffect(r1)
            goto L_0x0167
        L_0x0110:
            if (r9 != 0) goto L_0x0167
            if (r12 != r2) goto L_0x0120
            r20.setRevealEffect(r21)
            float r1 = r15.getDozeAmount()
            float r7 = r7 - r1
            r8.setRevealAmount(r7)
            goto L_0x00ec
        L_0x0120:
            if (r12 != 0) goto L_0x0162
            int r2 = r0.mTapToSleepX
            r3 = -1
            if (r2 == r3) goto L_0x0167
            int r4 = r0.mTapToSleepY
            if (r4 == r3) goto L_0x0167
            com.android.systemui.statusbar.CircleReveal r3 = r0.mCircleReveal
            float r5 = (float) r2
            float r4 = (float) r4
            r6 = 0
            int r9 = r1.widthPixels
            int r10 = r0.mTapToSleepX
            int r9 = r9 - r10
            int r2 = java.lang.Math.max((int) r2, (int) r9)
            int r9 = r0.mTapToSleepY
            int r1 = r1.heightPixels
            int r10 = r0.mTapToSleepY
            int r1 = r1 - r10
            int r1 = java.lang.Math.max((int) r9, (int) r1)
            int r1 = java.lang.Math.max((int) r2, (int) r1)
            float r9 = (float) r1
            r1 = r3
            r2 = r20
            r3 = r5
            r5 = r6
            r6 = r9
            r1.updateCircleReveal(r2, r3, r4, r5, r6)
            r0.mIsTapSleep = r13
            com.android.systemui.statusbar.CircleReveal r1 = r0.mCircleReveal
            r8.setRevealEffect(r1)
            float r1 = r15.getDozeAmount()
            float r7 = r7 - r1
            r8.setRevealAmount(r7)
            goto L_0x00ec
        L_0x0162:
            com.android.systemui.statusbar.LiftReveal r1 = com.android.systemui.statusbar.LiftReveal.INSTANCE
            r8.setRevealEffect(r1)
        L_0x0167:
            boolean r1 = r0.mCurShouldPlayOnOffAnimation
            if (r1 == r10) goto L_0x0172
            r0.mCurShouldPlayOnOffAnimation = r10
            com.android.systemui.statusbar.phone.DozeParameters r0 = r0.mDozeParameters
            r0.updateControlScreenOff()
        L_0x0172:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx.updateRevealEffectEx(com.android.systemui.statusbar.SysuiStatusBarStateController, android.util.DisplayMetrics, com.android.systemui.keyguard.WakefulnessLifecycle, boolean, android.graphics.PointF, com.android.systemui.statusbar.LightRevealScrim, com.android.systemui.statusbar.PowerButtonReveal, boolean):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v18 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00f1 A[SYNTHETIC, Splitter:B:38:0x00f1] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00fb A[SYNTHETIC, Splitter:B:43:0x00fb] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0108 A[SYNTHETIC, Splitter:B:50:0x0108] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0112 A[SYNTHETIC, Splitter:B:55:0x0112] */
    /* JADX WARNING: Removed duplicated region for block: B:66:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Point getTapXY() {
        /*
            r10 = this;
            android.graphics.Point r10 = new android.graphics.Point
            r10.<init>()
            r0 = 0
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
            java.lang.String r3 = "/sys/devices/platform/soc/a94000.spi/spi_master/spi0/spi0.0/fts_parse_coordinate"
            r2.<init>((java.lang.String) r3)     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00dc, all -> 0x00d9 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x00dc, all -> 0x00d9 }
            r3 = 0
            r4 = r3
            r3 = r0
        L_0x001a:
            java.lang.String r5 = r2.readLine()     // Catch:{ Exception -> 0x00d7 }
            if (r5 == 0) goto L_0x0044
            java.lang.String r6 = TAG     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d7 }
            r7.<init>()     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r8 = "tempText = "
            java.lang.StringBuilder r7 = r7.append((java.lang.String) r8)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r7 = r7.append((java.lang.String) r5)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00d7 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r6, r7)     // Catch:{ Exception -> 0x00d7 }
            if (r4 != 0) goto L_0x003d
            r0 = r5
            goto L_0x0041
        L_0x003d:
            r6 = 1
            if (r4 != r6) goto L_0x0041
            r3 = r5
        L_0x0041:
            int r4 = r4 + 1
            goto L_0x001a
        L_0x0044:
            java.lang.String r4 = TAG     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d7 }
            r5.<init>()     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = "xText = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r0)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = " ytext = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r3)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x00d7 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)     // Catch:{ Exception -> 0x00d7 }
            r4 = 2
            java.lang.String r0 = r0.substring(r4)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r3 = r3.substring(r4)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r4 = TAG     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d7 }
            r5.<init>()     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = "x = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r0)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = " y = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r3)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x00d7 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)     // Catch:{ Exception -> 0x00d7 }
            r4 = 16
            int r0 = java.lang.Integer.parseInt(r0, r4)     // Catch:{ Exception -> 0x00d7 }
            int r3 = java.lang.Integer.parseInt(r3, r4)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r4 = TAG     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d7 }
            r5.<init>()     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = "tapx = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((int) r0)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r6 = " tapy = "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)     // Catch:{ Exception -> 0x00d7 }
            java.lang.StringBuilder r5 = r5.append((int) r3)     // Catch:{ Exception -> 0x00d7 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x00d7 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r5)     // Catch:{ Exception -> 0x00d7 }
            android.graphics.Point r4 = new android.graphics.Point     // Catch:{ Exception -> 0x00d7 }
            r4.<init>(r0, r3)     // Catch:{ Exception -> 0x00d7 }
            r1.close()     // Catch:{ IOException -> 0x00c9 }
            goto L_0x00cd
        L_0x00c9:
            r10 = move-exception
            r10.printStackTrace()
        L_0x00cd:
            r2.close()     // Catch:{ IOException -> 0x00d1 }
            goto L_0x00d5
        L_0x00d1:
            r10 = move-exception
            r10.printStackTrace()
        L_0x00d5:
            r10 = r4
            goto L_0x0103
        L_0x00d7:
            r0 = move-exception
            goto L_0x00e8
        L_0x00d9:
            r10 = move-exception
            r2 = r0
            goto L_0x0105
        L_0x00dc:
            r2 = move-exception
            r9 = r2
            r2 = r0
            r0 = r9
            goto L_0x00e8
        L_0x00e1:
            r10 = move-exception
            r2 = r0
            goto L_0x0106
        L_0x00e4:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L_0x00e8:
            java.lang.String r3 = TAG     // Catch:{ all -> 0x0104 }
            java.lang.String r4 = "getTapXY "
            android.util.Log.d(r3, r4, r0)     // Catch:{ all -> 0x0104 }
            if (r1 == 0) goto L_0x00f9
            r1.close()     // Catch:{ IOException -> 0x00f5 }
            goto L_0x00f9
        L_0x00f5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00f9:
            if (r2 == 0) goto L_0x0103
            r2.close()     // Catch:{ IOException -> 0x00ff }
            goto L_0x0103
        L_0x00ff:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0103:
            return r10
        L_0x0104:
            r10 = move-exception
        L_0x0105:
            r0 = r1
        L_0x0106:
            if (r0 == 0) goto L_0x0110
            r0.close()     // Catch:{ IOException -> 0x010c }
            goto L_0x0110
        L_0x010c:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0110:
            if (r2 == 0) goto L_0x011a
            r2.close()     // Catch:{ IOException -> 0x0116 }
            goto L_0x011a
        L_0x0116:
            r0 = move-exception
            r0.printStackTrace()
        L_0x011a:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx.getTapXY():android.graphics.Point");
    }

    public void setTapWakeUp(boolean z) {
        this.mIsTapWakeUp = z;
    }

    public void onTapWakeUp() {
        this.mIsTapWakeUp = true;
        if (((LiftWakeGestureController) NTDependencyEx.get(LiftWakeGestureController.class)).isNear()) {
            NTLogUtil.m1682i(TAG, "retrun on onTapWake, don't wake up.");
        } else if (((AODController) NTDependencyEx.get(AODController.class)).isTapWakeEnable()) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 6, "com.android.systemui:NODOZE");
        } else if (this.mDozeServiceHost != null) {
            ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireTapWakeUp();
        }
    }

    public void onFinishedGoingToSleep() {
        NTLogUtil.m1682i(TAG, "onFinishedGoingToSleep: " + this.mLiftWakeGestureController);
        if (this.mLiftWakeGestureController != null && shouldEnableProximity()) {
            this.mLiftWakeGestureController.requestProximityTrigger();
        }
        LiftWakeGestureController liftWakeGestureController = this.mLiftWakeGestureController;
        if (liftWakeGestureController != null) {
            liftWakeGestureController.requestWakeUpTrigger();
        }
        if (this.mLiftWakeGestureController != null && shouldEnableMotionGestureLp()) {
            this.mLiftWakeGestureController.requestMotionTrigger();
        }
        resetTapSleepPoint();
    }

    public void onStartedGoingToSleep() {
        this.mIsTapWakeUp = false;
    }

    public void onStartedWakingUp() {
        NTLogUtil.m1682i(TAG, "onStartedWakingUp: " + this.mLiftWakeGestureController);
        LiftWakeGestureController liftWakeGestureController = this.mLiftWakeGestureController;
        if (liftWakeGestureController != null) {
            liftWakeGestureController.cancelProximityTrigger();
            this.mLiftWakeGestureController.cancelWakeUpTrigger();
            this.mLiftWakeGestureController.cancelMotionTrigger();
        }
        this.mNeedDelayShowAod = false;
        resetTapSleepPoint();
    }

    private boolean shouldEnableProximity() {
        return this.mAODController.isLiftWakeEnable() || this.mAODController.isTapWakeEnable() || this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
    }

    private boolean shouldEnableMotionGestureLp() {
        boolean z = Settings.Secure.getInt(this.mContext.getContentResolver(), MOTION_TRIGGER_SHOW_FP_ICON, 0) == 1;
        if (!this.mKeyguardUpdateMonitor.isUdfpsEnrolled() || !this.mLiftWakeGestureController.isMotionSensorSupported() || !z) {
            return false;
        }
        return true;
    }

    public void setNotificationPanelViewAlpha(final float f) {
        if (this.mNotificationPanelViewController != null) {
            NTLogUtil.m1680d(TAG, "setNotificationPanelViewAlpha:  alpha = " + f + ", cb: " + Debug.getCallers(3));
            this.mMainHandler.post(new Runnable() {
                public void run() {
                    CentralSurfacesImplEx.this.mNotificationPanelViewController.setAlpha(f);
                }
            });
        }
    }

    public void onFingerprintDown() {
        ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireFingerprintDown();
    }

    public void onFingerprintUp() {
        ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireFingerprintUp();
    }

    public void onDozeFingerprintRunningStateChanged() {
        ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).fireDozeFingerprintRunningStateChanged();
    }

    public boolean isNear() {
        return this.mIsNear;
    }

    public void onThresholdCrossed(boolean z) {
        this.mIsNear = z;
    }

    public boolean isWakeAndUnlock() {
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController != null) {
            return biometricUnlockController.isWakeAndUnlock();
        }
        return false;
    }

    public boolean isTapWakeUp() {
        return this.mIsTapWakeUp;
    }

    public void onDozingChanged(boolean z, int i) {
        boolean shouldShowAODView = ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView();
        NTLogUtil.m1680d(TAG, "onDozingChanged:  showAod = " + shouldShowAODView);
        if (z) {
            if (!shouldShowAODView) {
                setNotificationPanelViewAlpha(0.0f);
            } else if (this.mNeedDelayShowAod) {
                setNotificationPanelViewAlpha(0.0f);
                this.mMainHandler.postDelayed(new Runnable() {
                    public void run() {
                        CentralSurfacesImplEx.this.setNotificationPanelViewAlpha(1.0f);
                    }
                }, (long) DELAY_SHOW_AOD_DURATION);
            } else {
                setNotificationPanelViewAlpha(1.0f);
            }
        } else if (i != 0 && !isWakeAndUnlock()) {
            setNotificationPanelViewAlpha(1.0f);
        }
    }

    public NotificationPanelViewController getNotificationPanelViewController() {
        return this.mNotificationPanelViewController;
    }

    public void setTapGoingToSleep(int i, int i2) {
        this.mTapToSleepX = i;
        this.mTapToSleepY = i2;
    }

    private void resetTapSleepPoint() {
        this.mTapToSleepX = -1;
        this.mTapToSleepY = -1;
    }

    public boolean isTapSleep() {
        return this.mIsTapSleep;
    }

    public boolean shouldPlayOnOffAnimation() {
        return this.mCurShouldPlayOnOffAnimation;
    }

    public void setDelayToShowAod(boolean z) {
        this.mNeedDelayShowAod = z;
    }
}
