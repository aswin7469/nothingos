package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.PowerExemptionManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaOutputController_Factory implements Factory<MediaOutputController> {
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    private final Provider<LocalBluetoothManager> lbmProvider;
    private final Provider<MediaSessionManager> mediaSessionManagerProvider;
    private final Provider<Optional<NearbyMediaDevicesManager>> nearbyMediaDevicesManagerOptionalProvider;
    private final Provider<CommonNotifCollection> notifCollectionProvider;
    private final Provider<String> packageNameProvider;
    private final Provider<PowerExemptionManager> powerExemptionManagerProvider;
    private final Provider<ActivityStarter> starterProvider;

    public MediaOutputController_Factory(Provider<Context> provider, Provider<String> provider2, Provider<MediaSessionManager> provider3, Provider<LocalBluetoothManager> provider4, Provider<ActivityStarter> provider5, Provider<CommonNotifCollection> provider6, Provider<DialogLaunchAnimator> provider7, Provider<Optional<NearbyMediaDevicesManager>> provider8, Provider<AudioManager> provider9, Provider<PowerExemptionManager> provider10) {
        this.contextProvider = provider;
        this.packageNameProvider = provider2;
        this.mediaSessionManagerProvider = provider3;
        this.lbmProvider = provider4;
        this.starterProvider = provider5;
        this.notifCollectionProvider = provider6;
        this.dialogLaunchAnimatorProvider = provider7;
        this.nearbyMediaDevicesManagerOptionalProvider = provider8;
        this.audioManagerProvider = provider9;
        this.powerExemptionManagerProvider = provider10;
    }

    public MediaOutputController get() {
        return newInstance(this.contextProvider.get(), this.packageNameProvider.get(), this.mediaSessionManagerProvider.get(), this.lbmProvider.get(), this.starterProvider.get(), this.notifCollectionProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.nearbyMediaDevicesManagerOptionalProvider.get(), this.audioManagerProvider.get(), this.powerExemptionManagerProvider.get());
    }

    public static MediaOutputController_Factory create(Provider<Context> provider, Provider<String> provider2, Provider<MediaSessionManager> provider3, Provider<LocalBluetoothManager> provider4, Provider<ActivityStarter> provider5, Provider<CommonNotifCollection> provider6, Provider<DialogLaunchAnimator> provider7, Provider<Optional<NearbyMediaDevicesManager>> provider8, Provider<AudioManager> provider9, Provider<PowerExemptionManager> provider10) {
        return new MediaOutputController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static MediaOutputController newInstance(Context context, String str, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ActivityStarter activityStarter, CommonNotifCollection commonNotifCollection, DialogLaunchAnimator dialogLaunchAnimator, Optional<NearbyMediaDevicesManager> optional, AudioManager audioManager, PowerExemptionManager powerExemptionManager) {
        return new MediaOutputController(context, str, mediaSessionManager, localBluetoothManager, activityStarter, commonNotifCollection, dialogLaunchAnimator, optional, audioManager, powerExemptionManager);
    }
}
