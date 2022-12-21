package com.android.systemui.util.sensors;

import android.content.Context;
import android.hardware.HardwareBuffer;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorDirectChannel;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Handler;
import android.os.MemoryFile;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.concurrency.ThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class AsyncSensorManager extends SensorManager implements PluginListener<SensorManagerPlugin> {
    private static final String TAG = "AsyncSensorManager";
    private final Executor mExecutor;
    private final SensorManager mInner;
    private final List<SensorManagerPlugin> mPlugins = new ArrayList();
    private final List<Sensor> mSensorCache;

    @Inject
    public AsyncSensorManager(SensorManager sensorManager, ThreadFactory threadFactory, PluginManager pluginManager) {
        this.mInner = sensorManager;
        this.mExecutor = threadFactory.buildExecutorOnNewThread("async_sensor");
        this.mSensorCache = sensorManager.getSensorList(-1);
        if (pluginManager != null) {
            pluginManager.addPluginListener(this, SensorManagerPlugin.class, true);
        }
    }

    /* access modifiers changed from: protected */
    public List<Sensor> getFullSensorList() {
        return this.mSensorCache;
    }

    /* access modifiers changed from: protected */
    public List<Sensor> getFullDynamicSensorList() {
        return this.mInner.getSensorList(-1);
    }

    /* access modifiers changed from: protected */
    public boolean registerListenerImpl(SensorEventListener sensorEventListener, Sensor sensor, int i, Handler handler, int i2, int i3) {
        if (sensor == null) {
            Log.e(TAG, "sensor cannot be null \n" + Log.getStackTraceString(new Throwable()));
            return false;
        }
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda0(this, sensor, sensorEventListener, i, i2, handler));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerListenerImpl$0$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47045xd8b2ada(Sensor sensor, SensorEventListener sensorEventListener, int i, int i2, Handler handler) {
        if (sensor == null) {
            Log.e(TAG, "sensor cannot be null");
        }
        if (!this.mInner.registerListener(sensorEventListener, sensor, i, i2, handler)) {
            Log.e(TAG, "Registering " + sensorEventListener + " for " + sensor + " failed.");
        }
    }

    /* access modifiers changed from: protected */
    public boolean flushImpl(SensorEventListener sensorEventListener) {
        return this.mInner.flush(sensorEventListener);
    }

    /* access modifiers changed from: protected */
    public SensorDirectChannel createDirectChannelImpl(MemoryFile memoryFile, HardwareBuffer hardwareBuffer) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: protected */
    public void destroyDirectChannelImpl(SensorDirectChannel sensorDirectChannel) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: protected */
    public int configureDirectChannelImpl(SensorDirectChannel sensorDirectChannel, Sensor sensor, int i) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerDynamicSensorCallbackImpl$1$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47044x60d4d5af(SensorManager.DynamicSensorCallback dynamicSensorCallback, Handler handler) {
        this.mInner.registerDynamicSensorCallback(dynamicSensorCallback, handler);
    }

    /* access modifiers changed from: protected */
    public void registerDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback dynamicSensorCallback, Handler handler) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda4(this, dynamicSensorCallback, handler));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unregisterDynamicSensorCallbackImpl$2$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47049xc3ae84a7(SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.mInner.unregisterDynamicSensorCallback(dynamicSensorCallback);
    }

    /* access modifiers changed from: protected */
    public void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda8(this, dynamicSensorCallback));
    }

    /* access modifiers changed from: protected */
    public boolean requestTriggerSensorImpl(TriggerEventListener triggerEventListener, Sensor sensor) {
        if (triggerEventListener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        } else if (sensor != null) {
            this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda7(this, sensor, triggerEventListener));
            return true;
        } else {
            throw new IllegalArgumentException("sensor cannot be null");
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$requestTriggerSensorImpl$3$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47047xd630a8eb(Sensor sensor, TriggerEventListener triggerEventListener) {
        if (sensor == null) {
            Log.e(TAG, "sensor cannot be null");
        }
        if (!this.mInner.requestTriggerSensor(triggerEventListener, sensor)) {
            Log.e(TAG, "Requesting " + triggerEventListener + " for " + sensor + " failed.");
        }
    }

    /* access modifiers changed from: protected */
    public boolean cancelTriggerSensorImpl(TriggerEventListener triggerEventListener, Sensor sensor, boolean z) {
        Preconditions.checkArgument(z);
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda5(this, triggerEventListener, sensor));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$cancelTriggerSensorImpl$4$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47043x22e43dcf(TriggerEventListener triggerEventListener, Sensor sensor) {
        if (!this.mInner.cancelTriggerSensor(triggerEventListener, sensor)) {
            Log.e(TAG, "Canceling " + triggerEventListener + " for " + sensor + " failed.");
        }
    }

    public boolean registerPluginListener(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        if (this.mPlugins.isEmpty()) {
            Log.w(TAG, "No plugins registered");
            return false;
        }
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda1(this, sensor, sensorEventListener));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerPluginListener$5$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47046xb8e97682(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        for (int i = 0; i < this.mPlugins.size(); i++) {
            this.mPlugins.get(i).registerListener(sensor, sensorEventListener);
        }
    }

    public void unregisterPluginListener(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda6(this, sensor, sensorEventListener));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unregisterPluginListener$6$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47051x9b218268(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        for (int i = 0; i < this.mPlugins.size(); i++) {
            this.mPlugins.get(i).unregisterListener(sensor, sensorEventListener);
        }
    }

    /* access modifiers changed from: protected */
    public boolean initDataInjectionImpl(boolean z) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: protected */
    public boolean injectSensorDataImpl(Sensor sensor, float[] fArr, int i, long j) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setOperationParameterImpl$7$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47048xae291c20(SensorAdditionalInfo sensorAdditionalInfo) {
        this.mInner.setOperationParameter(sensorAdditionalInfo);
    }

    /* access modifiers changed from: protected */
    public boolean setOperationParameterImpl(SensorAdditionalInfo sensorAdditionalInfo) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda3(this, sensorAdditionalInfo));
        return true;
    }

    /* access modifiers changed from: protected */
    public void unregisterListenerImpl(SensorEventListener sensorEventListener, Sensor sensor) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda2(this, sensor, sensorEventListener));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unregisterListenerImpl$8$com-android-systemui-util-sensors-AsyncSensorManager */
    public /* synthetic */ void mo47050xd90452d9(Sensor sensor, SensorEventListener sensorEventListener) {
        if (sensor == null) {
            this.mInner.unregisterListener(sensorEventListener);
        } else {
            this.mInner.unregisterListener(sensorEventListener, sensor);
        }
    }

    public void onPluginConnected(SensorManagerPlugin sensorManagerPlugin, Context context) {
        this.mPlugins.add(sensorManagerPlugin);
    }

    public void onPluginDisconnected(SensorManagerPlugin sensorManagerPlugin) {
        this.mPlugins.remove((Object) sensorManagerPlugin);
    }
}
