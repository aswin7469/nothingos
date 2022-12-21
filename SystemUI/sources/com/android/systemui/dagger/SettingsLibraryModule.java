package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.UserHandle;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.dagger.qualifiers.Background;
import dagger.Module;
import dagger.Provides;

@Module
public class SettingsLibraryModule {
    @SysUISingleton
    @Provides
    static LocalBluetoothManager provideLocalBluetoothController(Context context, @Background Handler handler) {
        return LocalBluetoothManager.create(context, handler, UserHandle.ALL);
    }
}
