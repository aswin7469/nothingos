package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDeviceManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaDeviceManager.kt */
/* loaded from: classes.dex */
public final class MediaDeviceManager implements MediaDataManager.Listener, Dumpable {
    @NotNull
    private final Executor bgExecutor;
    @NotNull
    private final MediaControllerFactory controllerFactory;
    @NotNull
    private final Executor fgExecutor;
    @NotNull
    private final LocalMediaManagerFactory localMediaManagerFactory;
    @NotNull
    private final MediaRouter2Manager mr2manager;
    @NotNull
    private final Set<Listener> listeners = new LinkedHashSet();
    @NotNull
    private final Map<String, Entry> entries = new LinkedHashMap();

    /* compiled from: MediaDeviceManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onKeyRemoved(@NotNull String str);

        void onMediaDeviceChanged(@NotNull String str, @Nullable String str2, @Nullable MediaDeviceData mediaDeviceData);
    }

    public MediaDeviceManager(@NotNull MediaControllerFactory controllerFactory, @NotNull LocalMediaManagerFactory localMediaManagerFactory, @NotNull MediaRouter2Manager mr2manager, @NotNull Executor fgExecutor, @NotNull Executor bgExecutor, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(controllerFactory, "controllerFactory");
        Intrinsics.checkNotNullParameter(localMediaManagerFactory, "localMediaManagerFactory");
        Intrinsics.checkNotNullParameter(mr2manager, "mr2manager");
        Intrinsics.checkNotNullParameter(fgExecutor, "fgExecutor");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.controllerFactory = controllerFactory;
        this.localMediaManagerFactory = localMediaManagerFactory;
        this.mr2manager = mr2manager;
        this.fgExecutor = fgExecutor;
        this.bgExecutor = bgExecutor;
        String name = MediaDeviceManager.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    public final boolean addListener(@NotNull Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
        Entry remove;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        if (str != null && !Intrinsics.areEqual(str, key) && (remove = this.entries.remove(str)) != null) {
            remove.stop();
        }
        Entry entry = this.entries.get(key);
        if (entry == null || !Intrinsics.areEqual(entry.getToken(), data.getToken())) {
            if (entry != null) {
                entry.stop();
            }
            MediaSession.Token token = data.getToken();
            Entry entry2 = new Entry(this, key, str, token == null ? null : this.controllerFactory.create(token), this.localMediaManagerFactory.create(data.getPackageName()));
            this.entries.put(key, entry2);
            entry2.start();
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        Entry remove = this.entries.remove(key);
        if (remove != null) {
            remove.stop();
        }
        if (remove == null) {
            return;
        }
        for (Listener listener : this.listeners) {
            listener.onKeyRemoved(key);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull final FileDescriptor fd, @NotNull final PrintWriter pw, @NotNull final String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("MediaDeviceManager state:");
        this.entries.forEach(new BiConsumer<String, Entry>() { // from class: com.android.systemui.media.MediaDeviceManager$dump$1$1
            @Override // java.util.function.BiConsumer
            public final void accept(@NotNull String key, @NotNull MediaDeviceManager.Entry entry) {
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(entry, "entry");
                pw.println(Intrinsics.stringPlus("  key=", key));
                entry.dump(fd, pw, args);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processDevice(String str, String str2, MediaDevice mediaDevice) {
        boolean z = mediaDevice != null;
        String str3 = null;
        Drawable iconWithoutBackground = mediaDevice == null ? null : mediaDevice.getIconWithoutBackground();
        if (mediaDevice != null) {
            str3 = mediaDevice.getName();
        }
        MediaDeviceData mediaDeviceData = new MediaDeviceData(z, iconWithoutBackground, str3);
        for (Listener listener : this.listeners) {
            listener.onMediaDeviceChanged(str, str2, mediaDeviceData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MediaDeviceManager.kt */
    /* loaded from: classes.dex */
    public final class Entry extends MediaController.Callback implements LocalMediaManager.DeviceCallback {
        @Nullable
        private final MediaController controller;
        @Nullable
        private MediaDevice current;
        @NotNull
        private final String key;
        @NotNull
        private final LocalMediaManager localMediaManager;
        @Nullable
        private final String oldKey;
        private int playbackType;
        private boolean started;
        final /* synthetic */ MediaDeviceManager this$0;

        public Entry(@NotNull MediaDeviceManager this$0, @Nullable String key, @Nullable String str, @NotNull MediaController mediaController, LocalMediaManager localMediaManager) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(localMediaManager, "localMediaManager");
            this.this$0 = this$0;
            this.key = key;
            this.oldKey = str;
            this.controller = mediaController;
            this.localMediaManager = localMediaManager;
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        @Nullable
        public final String getOldKey() {
            return this.oldKey;
        }

        @Nullable
        public final MediaController getController() {
            return this.controller;
        }

        @NotNull
        public final LocalMediaManager getLocalMediaManager() {
            return this.localMediaManager;
        }

        @Nullable
        public final MediaSession.Token getToken() {
            MediaController mediaController = this.controller;
            if (mediaController == null) {
                return null;
            }
            return mediaController.getSessionToken();
        }

        private final void setCurrent(final MediaDevice mediaDevice) {
            if (!this.started || !Intrinsics.areEqual(mediaDevice, this.current)) {
                this.current = mediaDevice;
                Executor executor = this.this$0.fgExecutor;
                final MediaDeviceManager mediaDeviceManager = this.this$0;
                executor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$current$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDeviceManager.this.processDevice(this.getKey(), this.getOldKey(), mediaDevice);
                    }
                });
            }
        }

        public final void start() {
            this.this$0.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$start$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.getLocalMediaManager().registerCallback(MediaDeviceManager.Entry.this);
                    MediaDeviceManager.Entry.this.getLocalMediaManager().startScan();
                    MediaDeviceManager.Entry entry = MediaDeviceManager.Entry.this;
                    MediaController controller = entry.getController();
                    MediaController.PlaybackInfo playbackInfo = controller == null ? null : controller.getPlaybackInfo();
                    entry.playbackType = playbackInfo == null ? 0 : playbackInfo.getPlaybackType();
                    MediaController controller2 = MediaDeviceManager.Entry.this.getController();
                    if (controller2 != null) {
                        controller2.registerCallback(MediaDeviceManager.Entry.this);
                    }
                    MediaDeviceManager.Entry.this.updateCurrent();
                    MediaDeviceManager.Entry.this.started = true;
                }
            });
        }

        public final void stop() {
            this.this$0.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$stop$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.started = false;
                    MediaController controller = MediaDeviceManager.Entry.this.getController();
                    if (controller != null) {
                        controller.unregisterCallback(MediaDeviceManager.Entry.this);
                    }
                    MediaDeviceManager.Entry.this.getLocalMediaManager().stopScan();
                    MediaDeviceManager.Entry.this.getLocalMediaManager().unregisterCallback(MediaDeviceManager.Entry.this);
                }
            });
        }

        public final void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
            MediaController.PlaybackInfo playbackInfo;
            Intrinsics.checkNotNullParameter(fd, "fd");
            Intrinsics.checkNotNullParameter(pw, "pw");
            Intrinsics.checkNotNullParameter(args, "args");
            MediaController mediaController = this.controller;
            Integer num = null;
            RoutingSessionInfo routingSessionForMediaController = mediaController == null ? null : this.this$0.mr2manager.getRoutingSessionForMediaController(mediaController);
            List selectedRoutes = routingSessionForMediaController == null ? null : this.this$0.mr2manager.getSelectedRoutes(routingSessionForMediaController);
            MediaDevice mediaDevice = this.current;
            pw.println(Intrinsics.stringPlus("    current device is ", mediaDevice == null ? null : mediaDevice.getName()));
            MediaController controller = getController();
            if (controller != null && (playbackInfo = controller.getPlaybackInfo()) != null) {
                num = Integer.valueOf(playbackInfo.getPlaybackType());
            }
            pw.println("    PlaybackType=" + num + " (1 for local, 2 for remote) cached=" + this.playbackType);
            pw.println(Intrinsics.stringPlus("    routingSession=", routingSessionForMediaController));
            pw.println(Intrinsics.stringPlus("    selectedRoutes=", selectedRoutes));
        }

        @Override // android.media.session.MediaController.Callback
        public void onAudioInfoChanged(@Nullable MediaController.PlaybackInfo playbackInfo) {
            int playbackType = playbackInfo == null ? 0 : playbackInfo.getPlaybackType();
            if (playbackType == this.playbackType) {
                return;
            }
            this.playbackType = playbackType;
            updateCurrent();
        }

        @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
        public void onDeviceListUpdate(@Nullable List<? extends MediaDevice> list) {
            this.this$0.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$onDeviceListUpdate$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.updateCurrent();
                }
            });
        }

        @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
        public void onSelectedDeviceStateChanged(@NotNull MediaDevice device, int i) {
            Intrinsics.checkNotNullParameter(device, "device");
            this.this$0.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$onSelectedDeviceStateChanged$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.updateCurrent();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void updateCurrent() {
            MediaDevice currentConnectedDevice = this.localMediaManager.getCurrentConnectedDevice();
            MediaController mediaController = this.controller;
            Unit unit = null;
            MediaDevice mediaDevice = null;
            if (mediaController != null) {
                if (this.this$0.mr2manager.getRoutingSessionForMediaController(mediaController) != null) {
                    mediaDevice = currentConnectedDevice;
                }
                setCurrent(mediaDevice);
                unit = Unit.INSTANCE;
            }
            if (unit == null) {
                setCurrent(currentConnectedDevice);
            }
        }
    }
}
