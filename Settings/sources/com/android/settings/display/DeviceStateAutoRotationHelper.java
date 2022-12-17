package com.android.settings.display;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import com.android.internal.view.RotationPolicy;
import com.android.settings.R$array;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.settingslib.search.SearchIndexableRaw;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeviceStateAutoRotationHelper {
    static void initControllers(Lifecycle lifecycle, List<DeviceStateAutoRotateSettingController> list) {
        for (DeviceStateAutoRotateSettingController init : list) {
            init.init(lifecycle);
        }
    }

    static ImmutableList<AbstractPreferenceController> createPreferenceControllers(Context context) {
        List<DeviceStateRotationLockSettingsManager.SettableDeviceState> settableDeviceStates = DeviceStateRotationLockSettingsManager.getInstance(context).getSettableDeviceStates();
        int size = settableDeviceStates.size();
        if (size == 0) {
            return ImmutableList.m25of();
        }
        String[] stringArray = context.getResources().getStringArray(R$array.config_settableAutoRotationDeviceStatesDescriptions);
        if (size != stringArray.length) {
            Log.wtf("DeviceStateAutoRotHelpr", "Mismatch between number of device states and device states descriptions.");
            return ImmutableList.m25of();
        }
        ImmutableList.Builder builderWithExpectedSize = ImmutableList.builderWithExpectedSize(size);
        for (int i = 0; i < size; i++) {
            DeviceStateRotationLockSettingsManager.SettableDeviceState settableDeviceState = settableDeviceStates.get(i);
            if (settableDeviceState.isSettable()) {
                builderWithExpectedSize.add((Object) new DeviceStateAutoRotateSettingController(context, settableDeviceState.getDeviceState(), stringArray[i], (-size) + i));
            }
        }
        return builderWithExpectedSize.build();
    }

    static List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean z) {
        ImmutableList<AbstractPreferenceController> createPreferenceControllers = createPreferenceControllers(context);
        ArrayList arrayList = new ArrayList();
        Iterator<AbstractPreferenceController> it = createPreferenceControllers.iterator();
        while (it.hasNext()) {
            ((BasePreferenceController) it.next()).updateRawDataToIndex(arrayList);
        }
        return arrayList;
    }

    public static boolean isDeviceStateRotationEnabled(Context context) {
        return RotationPolicy.isRotationLockToggleVisible(context) && DeviceStateRotationLockSettingsManager.isDeviceStateRotationLockEnabled(context);
    }

    public static boolean isDeviceStateRotationEnabledForA11y(Context context) {
        return RotationPolicy.isRotationSupported(context) && DeviceStateRotationLockSettingsManager.isDeviceStateRotationLockEnabled(context);
    }
}
