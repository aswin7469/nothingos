package com.android.systemui.doze.dagger;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeBrightnessHostForwarder;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenStatePreventingAdapter;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.doze.DozeSuppressor;
import com.android.systemui.doze.DozeSuspendScreenStatePreventingAdapter;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@Module
public abstract class DozeModule {
    @Provides
    static DozeMachine.Part[] providesDozeMachineParts(DozePauser dozePauser, DozeFalsingManagerAdapter dozeFalsingManagerAdapter, DozeTriggers dozeTriggers, DozeUi dozeUi, DozeScreenState dozeScreenState, DozeScreenBrightness dozeScreenBrightness, DozeWallpaperState dozeWallpaperState, DozeDockHandler dozeDockHandler, DozeAuthRemover dozeAuthRemover, DozeSuppressor dozeSuppressor) {
        return new DozeMachine.Part[]{dozePauser, dozeFalsingManagerAdapter, dozeTriggers, dozeUi, dozeScreenState, dozeScreenBrightness, dozeWallpaperState, dozeDockHandler, dozeAuthRemover, dozeSuppressor};
    }

    @DozeScope
    @WrappedService
    @Provides
    static DozeMachine.Service providesWrappedService(DozeMachine.Service service, DozeHost dozeHost, DozeParameters dozeParameters) {
        return DozeSuspendScreenStatePreventingAdapter.wrapIfNeeded(DozeScreenStatePreventingAdapter.wrapIfNeeded(new DozeBrightnessHostForwarder(service, dozeHost), dozeParameters), dozeParameters);
    }

    @DozeScope
    @Provides
    static WakeLock providesDozeWakeLock(DelayedWakeLock.Builder builder, @Main Handler handler) {
        return builder.setHandler(handler).setTag("Doze").build();
    }

    @BrightnessSensor
    @Provides
    static Optional<Sensor>[] providesBrightnessSensors(AsyncSensorManager asyncSensorManager, Context context, DozeParameters dozeParameters) {
        String[] brightnessNames = dozeParameters.brightnessNames();
        if (brightnessNames.length == 0 || brightnessNames == null) {
            return new Optional[]{Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(C1894R.string.doze_brightness_sensor_type), (String) null))};
        }
        Optional<Sensor>[] optionalArr = new Optional[5];
        Arrays.fill((Object[]) optionalArr, (Object) Optional.empty());
        HashMap hashMap = new HashMap();
        for (int i = 0; i < brightnessNames.length; i++) {
            String str = brightnessNames[i];
            if (!hashMap.containsKey(str)) {
                hashMap.put(str, Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(C1894R.string.doze_brightness_sensor_type), brightnessNames[i])));
            }
            optionalArr[i] = (Optional) hashMap.get(str);
        }
        return optionalArr;
    }
}
