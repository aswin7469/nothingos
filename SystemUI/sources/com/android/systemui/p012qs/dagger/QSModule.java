package com.android.systemui.p012qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.media.dagger.MediaModule;
import com.android.systemui.p012qs.AutoAddTracker;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.ReduceBrightColorsController;
import com.android.systemui.p012qs.external.QSExternalModule;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module(includes = {MediaModule.class, QSExternalModule.class, QSFlagsModule.class}, subcomponents = {QSFragmentComponent.class})
/* renamed from: com.android.systemui.qs.dagger.QSModule */
public interface QSModule {
    @Binds
    QSHost provideQsHost(QSTileHost qSTileHost);

    @Provides
    static AutoTileManager provideAutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, @Background Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, SafetyController safetyController, @Named("rbc_available") boolean z) {
        AutoTileManager autoTileManager = new AutoTileManager(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, reduceBrightColorsController, deviceControlsController, walletController, safetyController, z);
        autoTileManager.init();
        return autoTileManager;
    }
}
