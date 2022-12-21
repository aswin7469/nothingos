package com.android.systemui.media.dagger;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.dagger.MediaTttReceiverLogBuffer;
import com.android.systemui.log.dagger.MediaTttSenderLogBuffer;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverLogger;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.media.taptotransfer.sender.MediaTttSenderLogger;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Named;

@Module(subcomponents = {MediaComplicationComponent.class})
public interface MediaModule {
    public static final String DREAM = "dream";
    public static final String KEYGUARD = "media_keyguard";
    public static final String QS_PANEL = "media_qs_panel";
    public static final String QUICK_QS_PANEL = "media_quick_qs_panel";

    @SysUISingleton
    @Provides
    @Named("media_qs_panel")
    static MediaHost providesQSMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    @SysUISingleton
    @Provides
    @Named("media_quick_qs_panel")
    static MediaHost providesQuickQSMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    @SysUISingleton
    @Provides
    @Named("media_keyguard")
    static MediaHost providesKeyguardMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    @SysUISingleton
    @Provides
    @Named("dream")
    static MediaHost providesDreamMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    @SysUISingleton
    @Provides
    static Optional<MediaTttChipControllerSender> providesMediaTttChipControllerSender(MediaTttFlags mediaTttFlags, Lazy<MediaTttChipControllerSender> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.m1745of(lazy.get());
    }

    @SysUISingleton
    @Provides
    static Optional<MediaTttChipControllerReceiver> providesMediaTttChipControllerReceiver(MediaTttFlags mediaTttFlags, Lazy<MediaTttChipControllerReceiver> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.m1745of(lazy.get());
    }

    @SysUISingleton
    @MediaTttSenderLogger
    @Provides
    static MediaTttLogger providesMediaTttSenderLogger(@MediaTttSenderLogBuffer LogBuffer logBuffer) {
        return new MediaTttLogger("Sender", logBuffer);
    }

    @SysUISingleton
    @MediaTttReceiverLogger
    @Provides
    static MediaTttLogger providesMediaTttReceiverLogger(@MediaTttReceiverLogBuffer LogBuffer logBuffer) {
        return new MediaTttLogger("Receiver", logBuffer);
    }

    @SysUISingleton
    @Provides
    static Optional<MediaTttCommandLineHelper> providesMediaTttCommandLineHelper(MediaTttFlags mediaTttFlags, Lazy<MediaTttCommandLineHelper> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.m1745of(lazy.get());
    }

    @SysUISingleton
    @Provides
    static Optional<MediaMuteAwaitConnectionCli> providesMediaMuteAwaitConnectionCli(MediaFlags mediaFlags, Lazy<MediaMuteAwaitConnectionCli> lazy) {
        if (!mediaFlags.areMuteAwaitConnectionsEnabled()) {
            return Optional.empty();
        }
        return Optional.m1745of(lazy.get());
    }

    @SysUISingleton
    @Provides
    static Optional<NearbyMediaDevicesManager> providesNearbyMediaDevicesManager(MediaFlags mediaFlags, Lazy<NearbyMediaDevicesManager> lazy) {
        if (!mediaFlags.areNearbyMediaDevicesEnabled()) {
            return Optional.empty();
        }
        return Optional.m1745of(lazy.get());
    }
}
