package com.android.systemui.volume.dagger;

import android.content.Context;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.volume.VolumeDialogComponent;
import com.android.systemui.volume.VolumeDialogImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public interface VolumeModule {
    @Binds
    VolumeComponent provideVolumeComponent(VolumeDialogComponent volumeDialogComponent);

    @Provides
    static VolumeDialog provideVolumeDialog(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter, InteractionJankMonitor interactionJankMonitor) {
        VolumeDialogImpl volumeDialogImpl = new VolumeDialogImpl(context, volumeDialogController, accessibilityManagerWrapper, deviceProvisionedController, configurationController, mediaOutputDialogFactory, activityStarter, interactionJankMonitor);
        volumeDialogImpl.setStreamImportant(1, false);
        volumeDialogImpl.setAutomute(true);
        volumeDialogImpl.setSilentMode(false);
        return volumeDialogImpl;
    }
}
