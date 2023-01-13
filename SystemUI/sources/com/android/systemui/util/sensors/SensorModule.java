package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import java.util.HashMap;

@Module
public class SensorModule {
    @PrimaryProxSensor
    @Provides
    static ThresholdSensor providePrimaryProximitySensor(SensorManager sensorManager, ThresholdSensorImpl.Builder builder) {
        try {
            return builder.setSensorDelay(3).setSensorResourceId(C1894R.string.proximity_sensor_type, true).setThresholdResourceId(C1894R.dimen.proximity_sensor_threshold).setThresholdLatchResourceId(C1894R.dimen.proximity_sensor_threshold_latch).build();
        } catch (IllegalStateException unused) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(8, true);
            return builder.setSensor(defaultSensor).setThresholdValue(defaultSensor != null ? defaultSensor.getMaximumRange() : 0.0f).build();
        }
    }

    @SecondaryProxSensor
    @Provides
    static ThresholdSensor provideSecondaryProximitySensor(ThresholdSensorImpl.Builder builder) {
        try {
            return builder.setSensorResourceId(C1894R.string.proximity_sensor_secondary_type, true).setThresholdResourceId(C1894R.dimen.proximity_sensor_secondary_threshold).setThresholdLatchResourceId(C1894R.dimen.proximity_sensor_secondary_threshold_latch).build();
        } catch (IllegalStateException unused) {
            return builder.setSensor((Sensor) null).setThresholdValue(0.0f).build();
        }
    }

    @Provides
    static ProximitySensor provideProximitySensor(@Main Resources resources, Lazy<PostureDependentProximitySensor> lazy, Lazy<ProximitySensorImpl> lazy2) {
        if (hasPostureSupport(resources.getStringArray(C1894R.array.proximity_sensor_posture_mapping))) {
            return lazy.get();
        }
        return lazy2.get();
    }

    @Provides
    static ProximityCheck provideProximityCheck(ProximitySensor proximitySensor, @Main DelayableExecutor delayableExecutor) {
        return new ProximityCheck(proximitySensor, delayableExecutor);
    }

    @PrimaryProxSensor
    @Provides
    static ThresholdSensor[] providePostureToProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, @Main Resources resources) {
        return createPostureToSensorMapping(builderFactory, resources.getStringArray(C1894R.array.proximity_sensor_posture_mapping), C1894R.dimen.proximity_sensor_threshold, C1894R.dimen.proximity_sensor_threshold_latch);
    }

    @SecondaryProxSensor
    @Provides
    static ThresholdSensor[] providePostureToSecondaryProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, @Main Resources resources) {
        return createPostureToSensorMapping(builderFactory, resources.getStringArray(C1894R.array.proximity_sensor_secondary_posture_mapping), C1894R.dimen.proximity_sensor_secondary_threshold, C1894R.dimen.proximity_sensor_secondary_threshold_latch);
    }

    private static ThresholdSensor[] createPostureToSensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, String[] strArr, int i, int i2) {
        ThresholdSensor[] thresholdSensorArr = new ThresholdSensor[5];
        Arrays.fill((Object[]) thresholdSensorArr, (Object) builderFactory.createBuilder().setSensor((Sensor) null).setThresholdValue(0.0f).build());
        if (!hasPostureSupport(strArr)) {
            Log.e("SensorModule", "config doesn't support postures, but attempting to retrieve proxSensorMapping");
            return thresholdSensorArr;
        }
        HashMap hashMap = new HashMap();
        for (int i3 = 0; i3 < strArr.length; i3++) {
            try {
                String str = strArr[i3];
                if (hashMap.containsKey(str)) {
                    thresholdSensorArr[i3] = (ThresholdSensor) hashMap.get(str);
                } else {
                    ThresholdSensor build = builderFactory.createBuilder().setSensorType(strArr[i3], true).setThresholdResourceId(i).setThresholdLatchResourceId(i2).build();
                    thresholdSensorArr[i3] = build;
                    hashMap.put(str, build);
                }
            } catch (IllegalStateException unused) {
            }
        }
        return thresholdSensorArr;
    }

    private static boolean hasPostureSupport(String[] strArr) {
        if (!(strArr == null || strArr.length == 0)) {
            for (String isEmpty : strArr) {
                if (!TextUtils.isEmpty(isEmpty)) {
                    return true;
                }
            }
        }
        return false;
    }
}
