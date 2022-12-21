package com.nothing.systemui.keyguard.weather;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.Utils;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class KeyguardWeatherControllerImpl implements KeyguardWeatherController, Dumpable {
    private static final String ONE_GLANCE_WEATHER_DATA = "content://com.nothing.widget.weather/weather/geo";
    private static final String TAG = "KeyguardWeather";
    public static final String WIDGET_ONE_GLANCE_TEMP_UNIT_OPTION = "nothing_widget_one_glance_weather_temp_option";
    private static final String WIDGET_ONE_GLANCE_WEATHER_SWITCH = "nothing_widget_one_glance_weather";
    /* access modifiers changed from: private */
    public static final Uri mWeatherDataUri = Uri.parse(ONE_GLANCE_WEATHER_DATA);
    /* access modifiers changed from: private */
    public static final Uri mWeatherSwitchUri = Settings.Global.getUriFor(WIDGET_ONE_GLANCE_WEATHER_SWITCH);
    /* access modifiers changed from: private */
    public static final Uri mWeatherTempOptionUri = Settings.Global.getUriFor(WIDGET_ONE_GLANCE_TEMP_UNIT_OPTION);
    private final ArrayList<KeyguardWeatherController.Callback> mCallbacks = new ArrayList<>();
    private final Object mCallbacksLock = new Object();
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public boolean mFirstTime = true;
    /* access modifiers changed from: private */
    public boolean mIsCelsiusUnit = true;
    /* access modifiers changed from: private */
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LocalObserver mLocalObserver;
    private Handler mMainHandler;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitorCallback mMonitorCallback;
    private boolean mRegistered;
    /* access modifiers changed from: private */
    public boolean mSwitchOn = false;
    private int mUserId;
    private WeatherData mWeatherData;
    private HandlerThread mWorker;

    @Inject
    public KeyguardWeatherControllerImpl(Context context, @Main Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        C41741 r1 = new KeyguardUpdateMonitorCallback() {
            public void onUserUnlocked() {
                NTLogUtil.m1682i(KeyguardWeatherControllerImpl.TAG, "onUserUnlocked, mFirstTime=" + KeyguardWeatherControllerImpl.this.mFirstTime);
                if (KeyguardWeatherControllerImpl.this.mFirstTime) {
                    KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
                    boolean unused = KeyguardWeatherControllerImpl.this.mFirstTime = false;
                    KeyguardWeatherControllerImpl.this.mKeyguardUpdateMonitor.removeCallback(KeyguardWeatherControllerImpl.this.mMonitorCallback);
                }
            }
        };
        this.mMonitorCallback = r1;
        this.mContext = context;
        this.mMainHandler = handler;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(r1);
        this.mFirstTime = true;
        this.mWeatherData = new WeatherData();
        LocalObserver localObserver = new LocalObserver(handler);
        this.mLocalObserver = localObserver;
        localObserver.register();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardWeatherControllerImpl:");
    }

    public void addCallback(KeyguardWeatherController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(KeyguardWeatherController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove((Object) callback);
        }
    }

    private final class LocalObserver extends ContentObserver {
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public LocalObserver(Handler handler) {
            super(handler);
            this.mResolver = KeyguardWeatherControllerImpl.this.mContext.getContentResolver();
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            NTLogUtil.m1682i(KeyguardWeatherControllerImpl.TAG, "register");
            this.mResolver.registerContentObserver(KeyguardWeatherControllerImpl.mWeatherSwitchUri, true, this);
            this.mResolver.registerContentObserver(KeyguardWeatherControllerImpl.mWeatherDataUri, true, this);
            this.mResolver.registerContentObserver(KeyguardWeatherControllerImpl.mWeatherTempOptionUri, true, this);
            this.mRegistered = true;
            KeyguardWeatherControllerImpl keyguardWeatherControllerImpl = KeyguardWeatherControllerImpl.this;
            boolean unused = keyguardWeatherControllerImpl.mSwitchOn = keyguardWeatherControllerImpl.isWeatherSwitchOn();
            KeyguardWeatherControllerImpl keyguardWeatherControllerImpl2 = KeyguardWeatherControllerImpl.this;
            boolean unused2 = keyguardWeatherControllerImpl2.mIsCelsiusUnit = keyguardWeatherControllerImpl2.isCelsiusUnitOn();
            KeyguardWeatherControllerImpl.this.fireWeatherSwitchChanged();
            KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
        }

        public void onChange(boolean z, Uri uri) {
            NTLogUtil.m1682i(KeyguardWeatherControllerImpl.TAG, "onChange uri=" + uri);
            if (KeyguardWeatherControllerImpl.mWeatherSwitchUri.equals(uri)) {
                KeyguardWeatherControllerImpl keyguardWeatherControllerImpl = KeyguardWeatherControllerImpl.this;
                boolean unused = keyguardWeatherControllerImpl.mSwitchOn = keyguardWeatherControllerImpl.isWeatherSwitchOn();
                KeyguardWeatherControllerImpl.this.fireWeatherSwitchChanged();
            } else if (KeyguardWeatherControllerImpl.mWeatherDataUri.equals(uri)) {
                KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
            } else if (KeyguardWeatherControllerImpl.mWeatherTempOptionUri.equals(uri)) {
                KeyguardWeatherControllerImpl keyguardWeatherControllerImpl2 = KeyguardWeatherControllerImpl.this;
                boolean unused2 = keyguardWeatherControllerImpl2.mIsCelsiusUnit = keyguardWeatherControllerImpl2.isCelsiusUnitOn();
                KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isWeatherSwitchOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), WIDGET_ONE_GLANCE_WEATHER_SWITCH, 1) != 0;
    }

    /* access modifiers changed from: private */
    public boolean isCelsiusUnitOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), WIDGET_ONE_GLANCE_TEMP_UNIT_OPTION, 1) == 1;
    }

    public boolean isKeyguardWeatherOn() {
        return this.mSwitchOn;
    }

    public boolean isKeyguardCelsiusUnitOn() {
        return this.mIsCelsiusUnit;
    }

    /* access modifiers changed from: private */
    public void fireWeatherSwitchChanged() {
        NTLogUtil.m1682i(TAG, "fireWeatherSwitchChanged =" + this.mSwitchOn);
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new KeyguardWeatherControllerImpl$$ExternalSyntheticLambda0(this));
            fireWeatherDataChanged();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fireWeatherSwitchChanged$0$com-nothing-systemui-keyguard-weather-KeyguardWeatherControllerImpl */
    public /* synthetic */ void mo57465x5218f0f(KeyguardWeatherController.Callback callback) {
        callback.onWeatherSwitchChanged(this.mSwitchOn);
    }

    /* access modifiers changed from: private */
    public void fireWeatherDataChanged() {
        NTLogUtil.m1682i(TAG, "fireWeatherDataChanged =" + this.mSwitchOn);
        if (this.mSwitchOn) {
            updateDataAndNotify();
        }
    }

    private void updateDataAndNotify() {
        this.mWorker.getThreadHandler().post(new KeyguardWeatherControllerImpl$$ExternalSyntheticLambda1(this));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: com.nothing.systemui.keyguard.weather.WeatherData} */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r7v3, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ee A[SYNTHETIC, Splitter:B:30:0x00ee] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00f5  */
    /* renamed from: lambda$updateDataAndNotify$3$com-nothing-systemui-keyguard-weather-KeyguardWeatherControllerImpl */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo57467xfcac0ac3() {
        /*
            r14 = this;
            java.lang.String r0 = "updateDataAndNotify , data.getLastUpdateTime="
            java.lang.String r1 = "updateDataAndNotify, jsonData="
            java.lang.String r2 = "update weather , cursor="
            java.lang.String r3 = "updateDataAndNotify, newWeatherData="
            java.lang.String r4 = "update weather , cursor="
            java.lang.String r5 = "update weather , Exception="
            java.lang.Object r6 = r14.mCallbacksLock
            monitor-enter(r6)
            android.content.Context r7 = r14.mContext     // Catch:{ all -> 0x00f9 }
            android.content.ContentResolver r8 = r7.getContentResolver()     // Catch:{ all -> 0x00f9 }
            r7 = 0
            android.net.Uri r9 = mWeatherDataUri     // Catch:{ Exception -> 0x00d2 }
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
            android.database.Cursor r8 = r8.query(r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x00d2 }
            java.lang.String r9 = "KeyguardWeather"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r10.<init>((java.lang.String) r4)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r4 = r10.append((java.lang.Object) r8)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.util.NTLogUtil.m1680d(r9, r4)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            if (r8 == 0) goto L_0x00a6
            java.lang.String r4 = "KeyguardWeather"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r9.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r2 = r9.append((java.lang.Object) r8)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r9 = ", moveToFirst="
            java.lang.StringBuilder r2 = r2.append((java.lang.String) r9)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            boolean r9 = r8.moveToFirst()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r2 = r2.append((boolean) r9)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            boolean r2 = r8.moveToFirst()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            if (r2 == 0) goto L_0x00a6
            java.lang.String r2 = "data"
            int r2 = r8.getColumnIndexOrThrow(r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r2 = r8.getString(r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.keyguard.weather.WeatherData r7 = com.nothing.systemui.keyguard.weather.WeatherData.fromJson(r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r4 = "KeyguardWeather"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r9.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r1 = r9.append((java.lang.String) r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.util.NTLogUtil.m1680d(r4, r1)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            if (r7 == 0) goto L_0x00a6
            java.lang.String r1 = "KeyguardWeather"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r2.<init>((java.lang.String) r0)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            long r9 = r7.getLastUpdateTime()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r0 = r2.append((long) r9)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r2 = ", "
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r2 = r7.getLocationKey()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r2)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.util.NTLogUtil.m1680d(r1, r0)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
        L_0x00a6:
            java.lang.String r0 = "KeyguardWeather"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r1.<init>((java.lang.String) r3)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.StringBuilder r1 = r1.append((java.lang.Object) r7)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.util.NTLogUtil.m1680d(r0, r1)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r14.mWeatherData = r7     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            android.os.Handler r0 = r14.mMainHandler     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl$$ExternalSyntheticLambda2 r1 = new com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl$$ExternalSyntheticLambda2     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r1.<init>(r14)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            r0.post(r1)     // Catch:{ Exception -> 0x00cd, all -> 0x00ca }
            if (r8 == 0) goto L_0x00f1
            r8.close()     // Catch:{ all -> 0x00f9 }
            goto L_0x00f1
        L_0x00ca:
            r14 = move-exception
            r7 = r8
            goto L_0x00f3
        L_0x00cd:
            r14 = move-exception
            r7 = r8
            goto L_0x00d3
        L_0x00d0:
            r14 = move-exception
            goto L_0x00f3
        L_0x00d2:
            r14 = move-exception
        L_0x00d3:
            java.lang.String r0 = "KeyguardWeather"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d0 }
            r1.<init>((java.lang.String) r5)     // Catch:{ all -> 0x00d0 }
            java.lang.String r2 = r14.toString()     // Catch:{ all -> 0x00d0 }
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)     // Catch:{ all -> 0x00d0 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00d0 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r0, r1)     // Catch:{ all -> 0x00d0 }
            r14.printStackTrace()     // Catch:{ all -> 0x00d0 }
            if (r7 == 0) goto L_0x00f1
            r7.close()     // Catch:{ all -> 0x00f9 }
        L_0x00f1:
            monitor-exit(r6)     // Catch:{ all -> 0x00f9 }
            return
        L_0x00f3:
            if (r7 == 0) goto L_0x00f8
            r7.close()     // Catch:{ all -> 0x00f9 }
        L_0x00f8:
            throw r14     // Catch:{ all -> 0x00f9 }
        L_0x00f9:
            r14 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00f9 }
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl.mo57467xfcac0ac3():void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateDataAndNotify$2$com-nothing-systemui-keyguard-weather-KeyguardWeatherControllerImpl */
    public /* synthetic */ void mo57466xb5a7b42() {
        Utils.safeForeach(this.mCallbacks, new KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3());
    }

    public WeatherData getWeatherData() {
        NTLogUtil.m1680d(TAG, "getWeatherData=" + this.mWeatherData);
        return this.mWeatherData;
    }
}
