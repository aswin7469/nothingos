package com.nothingos.keyguard.weather;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.util.Utils;
import com.nothingos.keyguard.weather.KeyguardWeatherController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class KeyguardWeatherControllerImpl implements KeyguardWeatherController, Dumpable {
    private final Context mContext;
    private boolean mFirstTime;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LocalObserver mLocalObserver;
    private Handler mMainHandler;
    private final KeyguardUpdateMonitorCallback mMonitorCallback;
    private HandlerThread mWorker;
    private static final Uri mWeatherSwitchUri = Settings.Global.getUriFor("nothing_widget_one_glance_weather");
    private static final Uri mWeatherDataUri = Uri.parse("content://com.nothing.widget.weather/weather/geo");
    private final ArrayList<KeyguardWeatherController.Callback> mCallbacks = new ArrayList<>();
    private final Object mCallbacksLock = new Object();
    private boolean mSwitchOn = false;
    private WeatherData mWeatherData = new WeatherData();

    public KeyguardWeatherControllerImpl(Context context, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mFirstTime = true;
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.nothingos.keyguard.weather.KeyguardWeatherControllerImpl.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onUserUnlocked() {
                Log.i("KeyguardWeather", "onUserUnlocked, mFirstTime=" + KeyguardWeatherControllerImpl.this.mFirstTime);
                if (KeyguardWeatherControllerImpl.this.mFirstTime) {
                    KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
                    KeyguardWeatherControllerImpl.this.mFirstTime = false;
                    KeyguardWeatherControllerImpl.this.mKeyguardUpdateMonitor.removeCallback(KeyguardWeatherControllerImpl.this.mMonitorCallback);
                }
            }
        };
        this.mMonitorCallback = keyguardUpdateMonitorCallback;
        this.mContext = context;
        this.mMainHandler = handler;
        HandlerThread handlerThread = new HandlerThread("KeyguardWeather");
        this.mWorker = handlerThread;
        handlerThread.start();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(keyguardUpdateMonitorCallback);
        this.mFirstTime = true;
        LocalObserver localObserver = new LocalObserver(handler);
        this.mLocalObserver = localObserver;
        localObserver.register();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardWeatherControllerImpl:");
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(KeyguardWeatherController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(KeyguardWeatherController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove(callback);
        }
    }

    /* loaded from: classes2.dex */
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
            Log.i("KeyguardWeather", "register");
            this.mResolver.registerContentObserver(KeyguardWeatherControllerImpl.mWeatherSwitchUri, true, this);
            this.mResolver.registerContentObserver(KeyguardWeatherControllerImpl.mWeatherDataUri, true, this);
            this.mRegistered = true;
            KeyguardWeatherControllerImpl keyguardWeatherControllerImpl = KeyguardWeatherControllerImpl.this;
            keyguardWeatherControllerImpl.mSwitchOn = keyguardWeatherControllerImpl.isWeatherSwitchOn();
            KeyguardWeatherControllerImpl.this.fireWeatherSwitchChanged();
            KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            Log.i("KeyguardWeather", "onChange uri=" + uri);
            if (!KeyguardWeatherControllerImpl.mWeatherSwitchUri.equals(uri)) {
                if (!KeyguardWeatherControllerImpl.mWeatherDataUri.equals(uri)) {
                    return;
                }
                KeyguardWeatherControllerImpl.this.fireWeatherDataChanged();
                return;
            }
            KeyguardWeatherControllerImpl keyguardWeatherControllerImpl = KeyguardWeatherControllerImpl.this;
            keyguardWeatherControllerImpl.mSwitchOn = keyguardWeatherControllerImpl.isWeatherSwitchOn();
            KeyguardWeatherControllerImpl.this.fireWeatherSwitchChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWeatherSwitchOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "nothing_widget_one_glance_weather", 1) != 0;
    }

    @Override // com.nothingos.keyguard.weather.KeyguardWeatherController
    public boolean isKeyguardWeatherOn() {
        return this.mSwitchOn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireWeatherSwitchChanged() {
        Log.i("KeyguardWeather", "fireWeatherSwitchChanged =" + this.mSwitchOn);
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer() { // from class: com.nothingos.keyguard.weather.KeyguardWeatherControllerImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    KeyguardWeatherControllerImpl.this.lambda$fireWeatherSwitchChanged$0((KeyguardWeatherController.Callback) obj);
                }
            });
            fireWeatherDataChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fireWeatherSwitchChanged$0(KeyguardWeatherController.Callback callback) {
        callback.onWeatherSwitchChanged(this.mSwitchOn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireWeatherDataChanged() {
        Log.i("KeyguardWeather", "fireWeatherDataChanged =" + this.mSwitchOn);
        if (!this.mSwitchOn) {
            return;
        }
        updateDataAndNotify();
    }

    private void updateDataAndNotify() {
        this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.nothingos.keyguard.weather.KeyguardWeatherControllerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardWeatherControllerImpl.this.lambda$updateDataAndNotify$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataAndNotify$3() {
        Cursor query;
        synchronized (this.mCallbacksLock) {
            Cursor cursor = null;
            r1 = null;
            WeatherData weatherData = null;
            cursor = null;
            try {
                try {
                    query = this.mContext.getContentResolver().query(mWeatherDataUri, null, null, null, null);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                Log.d("KeyguardWeather", "update weather , cursor=" + query);
                if (query != null) {
                    Log.d("KeyguardWeather", "update weather , cursor=" + query + ", moveToFirst=" + query.moveToFirst());
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndexOrThrow("data"));
                        WeatherData fromJson = WeatherData.fromJson(string);
                        Log.d("KeyguardWeather", "updateDataAndNotify, jsonData=" + string);
                        if (fromJson != null) {
                            Log.d("KeyguardWeather", "updateDataAndNotify , data.getLastUpdateTime=" + fromJson.getLastUpdateTime() + ", " + fromJson.getLocationKey());
                        }
                        weatherData = fromJson;
                    }
                }
                Log.d("KeyguardWeather", "updateDataAndNotify, newWeatherData=" + weatherData);
                this.mWeatherData = weatherData;
                this.mMainHandler.post(new Runnable() { // from class: com.nothingos.keyguard.weather.KeyguardWeatherControllerImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardWeatherControllerImpl.this.lambda$updateDataAndNotify$2();
                    }
                });
                if (query != null) {
                    query.close();
                }
            } catch (Exception e2) {
                e = e2;
                cursor = query;
                Log.d("KeyguardWeather", "update weather , Exception=" + e.toString());
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataAndNotify$2() {
        Utils.safeForeach(this.mCallbacks, KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3.INSTANCE);
    }

    @Override // com.nothingos.keyguard.weather.KeyguardWeatherController
    public WeatherData getWeatherData() {
        Log.d("KeyguardWeather", "getWeatherData=" + this.mWeatherData);
        return this.mWeatherData;
    }
}
