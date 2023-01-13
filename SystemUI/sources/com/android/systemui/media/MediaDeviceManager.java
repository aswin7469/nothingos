package com.android.systemui.media;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcast;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcast;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.p026io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002:\u000267B]\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0001\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0001\u0010\u0013\u001a\u00020\u0012\u0012\u0006\u0010\u0014\u001a\u00020\u0015¢\u0006\u0002\u0010\u0016J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001dJ#\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00190&H\u0016¢\u0006\u0002\u0010'J:\u0010(\u001a\u00020\"2\u0006\u0010)\u001a\u00020\u00192\b\u0010*\u001a\u0004\u0018\u00010\u00192\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u001fH\u0016J\u0010\u00101\u001a\u00020\"2\u0006\u0010)\u001a\u00020\u0019H\u0016J$\u00102\u001a\u00020\"2\u0006\u0010)\u001a\u00020\u00192\b\u0010*\u001a\u0004\u0018\u00010\u00192\b\u00103\u001a\u0004\u0018\u000104H\u0003J\u000e\u00105\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001dR\u000e\u0010\u0013\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u0012\u0012\u0004\u0012\u00020\u0019\u0012\b\u0012\u00060\u001aR\u00020\u00000\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u00068"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDeviceManager;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "controllerFactory", "Lcom/android/systemui/media/MediaControllerFactory;", "localMediaManagerFactory", "Lcom/android/systemui/media/LocalMediaManagerFactory;", "mr2manager", "Landroid/media/MediaRouter2Manager;", "muteAwaitConnectionManagerFactory", "Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManagerFactory;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "localBluetoothManager", "Lcom/android/settingslib/bluetooth/LocalBluetoothManager;", "fgExecutor", "Ljava/util/concurrent/Executor;", "bgExecutor", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/media/MediaControllerFactory;Lcom/android/systemui/media/LocalMediaManagerFactory;Landroid/media/MediaRouter2Manager;Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManagerFactory;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/settingslib/bluetooth/LocalBluetoothManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/dump/DumpManager;)V", "entries", "", "", "Lcom/android/systemui/media/MediaDeviceManager$Entry;", "listeners", "", "Lcom/android/systemui/media/MediaDeviceManager$Listener;", "addListener", "", "listener", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "onMediaDataLoaded", "key", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "immediately", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "processDevice", "device", "Lcom/android/systemui/media/MediaDeviceData;", "removeListener", "Entry", "Listener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager implements MediaDataManager.Listener, Dumpable {
    /* access modifiers changed from: private */
    public final Executor bgExecutor;
    /* access modifiers changed from: private */
    public final ConfigurationController configurationController;
    /* access modifiers changed from: private */
    public final Context context;
    private final MediaControllerFactory controllerFactory;
    private final Map<String, Entry> entries = new LinkedHashMap();
    /* access modifiers changed from: private */
    public final Executor fgExecutor;
    private final Set<Listener> listeners = new LinkedHashSet();
    /* access modifiers changed from: private */
    public final LocalBluetoothManager localBluetoothManager;
    private final LocalMediaManagerFactory localMediaManagerFactory;
    /* access modifiers changed from: private */
    public final MediaRouter2Manager mr2manager;
    private final MediaMuteAwaitConnectionManagerFactory muteAwaitConnectionManagerFactory;

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J$\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\u0010\b\u001a\u0004\u0018\u00010\tH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDeviceManager$Listener;", "", "onKeyRemoved", "", "key", "", "onMediaDeviceChanged", "oldKey", "data", "Lcom/android/systemui/media/MediaDeviceData;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaDeviceManager.kt */
    public interface Listener {
        void onKeyRemoved(String str);

        void onMediaDeviceChanged(String str, String str2, MediaDeviceData mediaDeviceData);
    }

    @Inject
    public MediaDeviceManager(Context context2, MediaControllerFactory mediaControllerFactory, LocalMediaManagerFactory localMediaManagerFactory2, MediaRouter2Manager mediaRouter2Manager, MediaMuteAwaitConnectionManagerFactory mediaMuteAwaitConnectionManagerFactory, ConfigurationController configurationController2, LocalBluetoothManager localBluetoothManager2, @Main Executor executor, @Background Executor executor2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "controllerFactory");
        Intrinsics.checkNotNullParameter(localMediaManagerFactory2, "localMediaManagerFactory");
        Intrinsics.checkNotNullParameter(mediaRouter2Manager, "mr2manager");
        Intrinsics.checkNotNullParameter(mediaMuteAwaitConnectionManagerFactory, "muteAwaitConnectionManagerFactory");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(executor, "fgExecutor");
        Intrinsics.checkNotNullParameter(executor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context2;
        this.controllerFactory = mediaControllerFactory;
        this.localMediaManagerFactory = localMediaManagerFactory2;
        this.mr2manager = mediaRouter2Manager;
        this.muteAwaitConnectionManagerFactory = mediaMuteAwaitConnectionManagerFactory;
        this.configurationController = configurationController2;
        this.localBluetoothManager = localBluetoothManager2;
        this.fgExecutor = executor;
        this.bgExecutor = executor2;
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    public final boolean addListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    public final boolean removeListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.remove(listener);
    }

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        Entry remove;
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (!(str2 == null || Intrinsics.areEqual((Object) str2, (Object) str) || (remove = this.entries.remove(str2)) == null)) {
            remove.stop();
        }
        Entry entry = this.entries.get(str);
        if (entry == null || !Intrinsics.areEqual((Object) entry.getToken(), (Object) mediaData.getToken())) {
            if (entry != null) {
                entry.stop();
            }
            if (mediaData.getDevice() != null) {
                processDevice(str, str2, mediaData.getDevice());
                return;
            }
            MediaSession.Token token = mediaData.getToken();
            MediaController create = token != null ? this.controllerFactory.create(token) : null;
            LocalMediaManager create2 = this.localMediaManagerFactory.create(mediaData.getPackageName());
            Entry entry2 = new Entry(this, str, str2, create, create2, this.muteAwaitConnectionManagerFactory.create(create2));
            this.entries.put(str, entry2);
            entry2.start();
        }
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        Entry remove = this.entries.remove(str);
        if (remove != null) {
            remove.stop();
        }
        if (remove != null) {
            for (Listener onKeyRemoved : this.listeners) {
                onKeyRemoved.onKeyRemoved(str);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("MediaDeviceManager state:");
        for (Map.Entry next : this.entries.entrySet()) {
            printWriter.println("  key=" + ((String) next.getKey()));
            ((Entry) next.getValue()).dump(printWriter);
        }
    }

    /* access modifiers changed from: private */
    public final void processDevice(String str, String str2, MediaDeviceData mediaDeviceData) {
        for (Listener onMediaDeviceChanged : this.listeners) {
            onMediaDeviceChanged.onMediaDeviceChanged(str, str2, mediaDeviceData);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t*\u0001\u0012\b\u0004\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B3\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\u0002\u0010\rJ\u000e\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-J\u0010\u0010.\u001a\u00020+2\u0006\u0010/\u001a\u000200H\u0002J\b\u00101\u001a\u00020%H\u0002J\"\u00102\u001a\u00020+2\u0006\u00103\u001a\u00020\u00052\u0006\u00104\u001a\u00020\u00052\b\u00105\u001a\u0004\u0018\u000106H\u0016J\b\u00107\u001a\u00020+H\u0016J\u0012\u00108\u001a\u00020+2\b\u00109\u001a\u0004\u0018\u00010:H\u0017J\u0018\u0010;\u001a\u00020+2\u0006\u0010<\u001a\u00020#2\u0006\u0010=\u001a\u00020>H\u0016J\u0010\u0010?\u001a\u00020+2\u0006\u0010@\u001a\u00020#H\u0016J\u0018\u0010A\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0010\u0010B\u001a\u00020+2\u0006\u0010@\u001a\u00020#H\u0016J\u0018\u0010C\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0018\u0010D\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0018\u0010E\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0018\u0010F\u001a\u00020+2\u000e\u0010G\u001a\n\u0012\u0004\u0012\u00020I\u0018\u00010HH\u0016J\u0018\u0010J\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0018\u0010K\u001a\u00020+2\u0006\u0010@\u001a\u00020#2\u0006\u0010<\u001a\u00020#H\u0016J\u0018\u0010L\u001a\u00020+2\u0006\u0010M\u001a\u00020I2\u0006\u0010N\u001a\u00020#H\u0016J\b\u0010O\u001a\u00020+H\u0007J\b\u0010P\u001a\u00020+H\u0007J\b\u0010Q\u001a\u00020+H\u0003R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\"\u0010\u0018\u001a\u0004\u0018\u00010\u00172\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u000e\u0010\"\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u000e¢\u0006\u0002\n\u0000R\u0013\u0010&\u001a\u0004\u0018\u00010'8F¢\u0006\u0006\u001a\u0004\b(\u0010)¨\u0006R"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDeviceManager$Entry;", "Lcom/android/settingslib/media/LocalMediaManager$DeviceCallback;", "Landroid/media/session/MediaController$Callback;", "Landroid/bluetooth/BluetoothLeBroadcast$Callback;", "key", "", "oldKey", "controller", "Landroid/media/session/MediaController;", "localMediaManager", "Lcom/android/settingslib/media/LocalMediaManager;", "muteAwaitConnectionManager", "Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager;", "(Lcom/android/systemui/media/MediaDeviceManager;Ljava/lang/String;Ljava/lang/String;Landroid/media/session/MediaController;Lcom/android/settingslib/media/LocalMediaManager;Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager;)V", "aboutToConnectDeviceOverride", "Lcom/android/systemui/media/AboutToConnectDevice;", "broadcastDescription", "configListener", "com/android/systemui/media/MediaDeviceManager$Entry$configListener$1", "Lcom/android/systemui/media/MediaDeviceManager$Entry$configListener$1;", "getController", "()Landroid/media/session/MediaController;", "value", "Lcom/android/systemui/media/MediaDeviceData;", "current", "setCurrent", "(Lcom/android/systemui/media/MediaDeviceData;)V", "getKey", "()Ljava/lang/String;", "getLocalMediaManager", "()Lcom/android/settingslib/media/LocalMediaManager;", "getMuteAwaitConnectionManager", "()Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager;", "getOldKey", "playbackType", "", "started", "", "token", "Landroid/media/session/MediaSession$Token;", "getToken", "()Landroid/media/session/MediaSession$Token;", "dump", "", "pw", "Ljava/io/PrintWriter;", "getBroadcastingInfo", "bluetoothLeBroadcast", "Lcom/android/settingslib/bluetooth/LocalBluetoothLeBroadcast;", "isLeAudioBroadcastEnabled", "onAboutToConnectDeviceAdded", "deviceAddress", "deviceName", "deviceIcon", "Landroid/graphics/drawable/Drawable;", "onAboutToConnectDeviceRemoved", "onAudioInfoChanged", "info", "Landroid/media/session/MediaController$PlaybackInfo;", "onBroadcastMetadataChanged", "broadcastId", "metadata", "Landroid/bluetooth/BluetoothLeBroadcastMetadata;", "onBroadcastStartFailed", "reason", "onBroadcastStarted", "onBroadcastStopFailed", "onBroadcastStopped", "onBroadcastUpdateFailed", "onBroadcastUpdated", "onDeviceListUpdate", "devices", "", "Lcom/android/settingslib/media/MediaDevice;", "onPlaybackStarted", "onPlaybackStopped", "onSelectedDeviceStateChanged", "device", "state", "start", "stop", "updateCurrent", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaDeviceManager.kt */
    private final class Entry extends MediaController.Callback implements LocalMediaManager.DeviceCallback, BluetoothLeBroadcast.Callback {
        private AboutToConnectDevice aboutToConnectDeviceOverride;
        private String broadcastDescription;
        private final MediaDeviceManager$Entry$configListener$1 configListener = new MediaDeviceManager$Entry$configListener$1(this);
        private final MediaController controller;
        private MediaDeviceData current;
        private final String key;
        private final LocalMediaManager localMediaManager;
        private final MediaMuteAwaitConnectionManager muteAwaitConnectionManager;
        private final String oldKey;
        private int playbackType;
        private boolean started;
        final /* synthetic */ MediaDeviceManager this$0;

        public void onPlaybackStarted(int i, int i2) {
        }

        public void onPlaybackStopped(int i, int i2) {
        }

        public Entry(MediaDeviceManager mediaDeviceManager, String str, String str2, MediaController mediaController, LocalMediaManager localMediaManager2, MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager) {
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(localMediaManager2, "localMediaManager");
            this.this$0 = mediaDeviceManager;
            this.key = str;
            this.oldKey = str2;
            this.controller = mediaController;
            this.localMediaManager = localMediaManager2;
            this.muteAwaitConnectionManager = mediaMuteAwaitConnectionManager;
        }

        public final String getKey() {
            return this.key;
        }

        public final String getOldKey() {
            return this.oldKey;
        }

        public final MediaController getController() {
            return this.controller;
        }

        public final LocalMediaManager getLocalMediaManager() {
            return this.localMediaManager;
        }

        public final MediaMuteAwaitConnectionManager getMuteAwaitConnectionManager() {
            return this.muteAwaitConnectionManager;
        }

        public final MediaSession.Token getToken() {
            MediaController mediaController = this.controller;
            if (mediaController != null) {
                return mediaController.getSessionToken();
            }
            return null;
        }

        private final void setCurrent(MediaDeviceData mediaDeviceData) {
            boolean z = mediaDeviceData != null && mediaDeviceData.equalsWithoutIcon(this.current);
            if (!this.started || !z) {
                this.current = mediaDeviceData;
                this.this$0.fgExecutor.execute(new MediaDeviceManager$Entry$$ExternalSyntheticLambda1(this.this$0, this, mediaDeviceData));
            }
        }

        /* access modifiers changed from: private */
        /* renamed from: _set_current_$lambda-0  reason: not valid java name */
        public static final void m2809_set_current_$lambda0(MediaDeviceManager mediaDeviceManager, Entry entry, MediaDeviceData mediaDeviceData) {
            Intrinsics.checkNotNullParameter(mediaDeviceManager, "this$0");
            Intrinsics.checkNotNullParameter(entry, "this$1");
            mediaDeviceManager.processDevice(entry.key, entry.oldKey, mediaDeviceData);
        }

        public final void start() {
            this.this$0.bgExecutor.execute(new MediaDeviceManager$Entry$$ExternalSyntheticLambda2(this, this.this$0));
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0024, code lost:
            r0 = r0.getPlaybackInfo();
         */
        /* renamed from: start$lambda-1  reason: not valid java name */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static final void m2812start$lambda1(com.android.systemui.media.MediaDeviceManager.Entry r2, com.android.systemui.media.MediaDeviceManager r3) {
            /*
                java.lang.String r0 = "this$0"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
                java.lang.String r0 = "this$1"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
                com.android.settingslib.media.LocalMediaManager r0 = r2.localMediaManager
                r1 = r2
                com.android.settingslib.media.LocalMediaManager$DeviceCallback r1 = (com.android.settingslib.media.LocalMediaManager.DeviceCallback) r1
                r0.registerCallback(r1)
                com.android.settingslib.media.LocalMediaManager r0 = r2.localMediaManager
                r0.startScan()
                com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager r0 = r2.muteAwaitConnectionManager
                if (r0 == 0) goto L_0x0020
                r0.startListening()
            L_0x0020:
                android.media.session.MediaController r0 = r2.controller
                if (r0 == 0) goto L_0x002f
                android.media.session.MediaController$PlaybackInfo r0 = r0.getPlaybackInfo()
                if (r0 == 0) goto L_0x002f
                int r0 = r0.getPlaybackType()
                goto L_0x0030
            L_0x002f:
                r0 = 0
            L_0x0030:
                r2.playbackType = r0
                android.media.session.MediaController r0 = r2.controller
                if (r0 == 0) goto L_0x003c
                r1 = r2
                android.media.session.MediaController$Callback r1 = (android.media.session.MediaController.Callback) r1
                r0.registerCallback(r1)
            L_0x003c:
                r2.updateCurrent()
                r0 = 1
                r2.started = r0
                com.android.systemui.statusbar.policy.ConfigurationController r3 = r3.configurationController
                com.android.systemui.media.MediaDeviceManager$Entry$configListener$1 r2 = r2.configListener
                r3.addCallback(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDeviceManager.Entry.m2812start$lambda1(com.android.systemui.media.MediaDeviceManager$Entry, com.android.systemui.media.MediaDeviceManager):void");
        }

        public final void stop() {
            this.this$0.bgExecutor.execute(new MediaDeviceManager$Entry$$ExternalSyntheticLambda0(this, this.this$0));
        }

        /* access modifiers changed from: private */
        /* renamed from: stop$lambda-2  reason: not valid java name */
        public static final void m2813stop$lambda2(Entry entry, MediaDeviceManager mediaDeviceManager) {
            Intrinsics.checkNotNullParameter(entry, "this$0");
            Intrinsics.checkNotNullParameter(mediaDeviceManager, "this$1");
            entry.started = false;
            MediaController mediaController = entry.controller;
            if (mediaController != null) {
                mediaController.unregisterCallback(entry);
            }
            entry.localMediaManager.stopScan();
            entry.localMediaManager.unregisterCallback(entry);
            MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager = entry.muteAwaitConnectionManager;
            if (mediaMuteAwaitConnectionManager != null) {
                mediaMuteAwaitConnectionManager.stopListening();
            }
            mediaDeviceManager.configurationController.removeCallback(entry.configListener);
        }

        public final void dump(PrintWriter printWriter) {
            MediaController.PlaybackInfo playbackInfo;
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            MediaController mediaController = this.controller;
            Integer num = null;
            RoutingSessionInfo routingSessionForMediaController = mediaController != null ? this.this$0.mr2manager.getRoutingSessionForMediaController(mediaController) : null;
            List selectedRoutes = routingSessionForMediaController != null ? this.this$0.mr2manager.getSelectedRoutes(routingSessionForMediaController) : null;
            StringBuilder sb = new StringBuilder("    current device is ");
            MediaDeviceData mediaDeviceData = this.current;
            printWriter.println(sb.append((Object) mediaDeviceData != null ? mediaDeviceData.getName() : null).toString());
            MediaController mediaController2 = this.controller;
            if (!(mediaController2 == null || (playbackInfo = mediaController2.getPlaybackInfo()) == null)) {
                num = Integer.valueOf(playbackInfo.getPlaybackType());
            }
            printWriter.println("    PlaybackType=" + num + " (1 for local, 2 for remote) cached=" + this.playbackType);
            printWriter.println("    routingSession=" + routingSessionForMediaController);
            printWriter.println("    selectedRoutes=" + selectedRoutes);
        }

        public void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
            int playbackType2 = playbackInfo != null ? playbackInfo.getPlaybackType() : 0;
            if (playbackType2 != this.playbackType) {
                this.playbackType = playbackType2;
                updateCurrent();
            }
        }

        public void onDeviceListUpdate(List<? extends MediaDevice> list) {
            this.this$0.bgExecutor.execute(new MediaDeviceManager$Entry$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: private */
        /* renamed from: onDeviceListUpdate$lambda-6  reason: not valid java name */
        public static final void m2810onDeviceListUpdate$lambda6(Entry entry) {
            Intrinsics.checkNotNullParameter(entry, "this$0");
            entry.updateCurrent();
        }

        public void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
            Intrinsics.checkNotNullParameter(mediaDevice, "device");
            this.this$0.bgExecutor.execute(new MediaDeviceManager$Entry$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: private */
        /* renamed from: onSelectedDeviceStateChanged$lambda-7  reason: not valid java name */
        public static final void m2811onSelectedDeviceStateChanged$lambda7(Entry entry) {
            Intrinsics.checkNotNullParameter(entry, "this$0");
            entry.updateCurrent();
        }

        public void onAboutToConnectDeviceAdded(String str, String str2, Drawable drawable) {
            Intrinsics.checkNotNullParameter(str, "deviceAddress");
            Intrinsics.checkNotNullParameter(str2, "deviceName");
            this.aboutToConnectDeviceOverride = new AboutToConnectDevice(this.localMediaManager.getMediaDeviceById(str), new MediaDeviceData(true, drawable, str2, (PendingIntent) null, (String) null, false, 24, (DefaultConstructorMarker) null));
            updateCurrent();
        }

        public void onAboutToConnectDeviceRemoved() {
            this.aboutToConnectDeviceOverride = null;
            updateCurrent();
        }

        public void onBroadcastStarted(int i, int i2) {
            Log.d("MediaDeviceManager", "onBroadcastStarted(), reason = " + i + " , broadcastId = " + i2);
            updateCurrent();
        }

        public void onBroadcastStartFailed(int i) {
            Log.d("MediaDeviceManager", "onBroadcastStartFailed(), reason = " + i);
        }

        public void onBroadcastMetadataChanged(int i, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
            Intrinsics.checkNotNullParameter(bluetoothLeBroadcastMetadata, "metadata");
            Log.d("MediaDeviceManager", "onBroadcastMetadataChanged(), broadcastId = " + i + " , metadata = " + bluetoothLeBroadcastMetadata);
            updateCurrent();
        }

        public void onBroadcastStopped(int i, int i2) {
            Log.d("MediaDeviceManager", "onBroadcastStopped(), reason = " + i + " , broadcastId = " + i2);
            updateCurrent();
        }

        public void onBroadcastStopFailed(int i) {
            Log.d("MediaDeviceManager", "onBroadcastStopFailed(), reason = " + i);
        }

        public void onBroadcastUpdated(int i, int i2) {
            Log.d("MediaDeviceManager", "onBroadcastUpdated(), reason = " + i + " , broadcastId = " + i2);
            updateCurrent();
        }

        public void onBroadcastUpdateFailed(int i, int i2) {
            Log.d("MediaDeviceManager", "onBroadcastUpdateFailed(), reason = " + i + " , broadcastId = " + i2);
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x0091  */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x0097  */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x009e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void updateCurrent() {
            /*
                r13 = this;
                boolean r0 = r13.isLeAudioBroadcastEnabled()
                if (r0 == 0) goto L_0x002a
                com.android.systemui.media.MediaDeviceData r0 = new com.android.systemui.media.MediaDeviceData
                r2 = 1
                com.android.systemui.media.MediaDeviceManager r1 = r13.this$0
                android.content.Context r1 = r1.context
                r3 = 2131232796(0x7f08081c, float:1.8081711E38)
                android.graphics.drawable.Drawable r3 = r1.getDrawable(r3)
                java.lang.String r1 = r13.broadcastDescription
                r4 = r1
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4
                r5 = 0
                r6 = 0
                r7 = 1
                r8 = 16
                r9 = 0
                r1 = r0
                r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
                r13.setCurrent(r0)
                goto L_0x00ae
            L_0x002a:
                com.android.systemui.media.AboutToConnectDevice r0 = r13.aboutToConnectDeviceOverride
                if (r0 == 0) goto L_0x0042
                com.android.settingslib.media.MediaDevice r1 = r0.getFullMediaDevice()
                if (r1 != 0) goto L_0x0042
                com.android.systemui.media.MediaDeviceData r1 = r0.getBackupMediaDeviceData()
                if (r1 == 0) goto L_0x0042
                com.android.systemui.media.MediaDeviceData r0 = r0.getBackupMediaDeviceData()
                r13.setCurrent(r0)
                return
            L_0x0042:
                if (r0 == 0) goto L_0x004a
                com.android.settingslib.media.MediaDevice r0 = r0.getFullMediaDevice()
                if (r0 != 0) goto L_0x0050
            L_0x004a:
                com.android.settingslib.media.LocalMediaManager r0 = r13.localMediaManager
                com.android.settingslib.media.MediaDevice r0 = r0.getCurrentConnectedDevice()
            L_0x0050:
                android.media.session.MediaController r1 = r13.controller
                r2 = 0
                if (r1 == 0) goto L_0x0060
                com.android.systemui.media.MediaDeviceManager r3 = r13.this$0
                android.media.MediaRouter2Manager r3 = r3.mr2manager
                android.media.RoutingSessionInfo r1 = r3.getRoutingSessionForMediaController(r1)
                goto L_0x0061
            L_0x0060:
                r1 = r2
            L_0x0061:
                if (r0 == 0) goto L_0x006b
                android.media.session.MediaController r3 = r13.controller
                if (r3 == 0) goto L_0x0069
                if (r1 == 0) goto L_0x006b
            L_0x0069:
                r3 = 1
                goto L_0x006c
            L_0x006b:
                r3 = 0
            L_0x006c:
                r5 = r3
                android.media.session.MediaController r3 = r13.controller
                if (r3 == 0) goto L_0x0079
                if (r1 == 0) goto L_0x0074
                goto L_0x0079
            L_0x0074:
                r1 = r2
                java.lang.String r1 = (java.lang.String) r1
            L_0x0077:
                r1 = r2
                goto L_0x008d
            L_0x0079:
                if (r1 == 0) goto L_0x0087
                java.lang.CharSequence r1 = r1.getName()
                if (r1 == 0) goto L_0x0087
                java.lang.String r1 = r1.toString()
                if (r1 != 0) goto L_0x008d
            L_0x0087:
                if (r0 == 0) goto L_0x0077
                java.lang.String r1 = r0.getName()
            L_0x008d:
                com.android.systemui.media.MediaDeviceData r3 = new com.android.systemui.media.MediaDeviceData
                if (r0 == 0) goto L_0x0097
                android.graphics.drawable.Drawable r4 = r0.getIconWithoutBackground()
                r6 = r4
                goto L_0x0098
            L_0x0097:
                r6 = r2
            L_0x0098:
                r7 = r1
                java.lang.CharSequence r7 = (java.lang.CharSequence) r7
                r8 = 0
                if (r0 == 0) goto L_0x00a2
                java.lang.String r2 = r0.getId()
            L_0x00a2:
                r9 = r2
                r10 = 0
                r11 = 8
                r12 = 0
                r4 = r3
                r4.<init>(r5, r6, r7, r8, r9, r10, r11, r12)
                r13.setCurrent(r3)
            L_0x00ae:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDeviceManager.Entry.updateCurrent():void");
        }

        private final boolean isLeAudioBroadcastEnabled() {
            if (this.this$0.localBluetoothManager != null) {
                LocalBluetoothProfileManager profileManager = this.this$0.localBluetoothManager.getProfileManager();
                if (profileManager != null) {
                    LocalBluetoothLeBroadcast leAudioBroadcastProfile = profileManager.getLeAudioBroadcastProfile();
                    if (leAudioBroadcastProfile == null || !leAudioBroadcastProfile.isEnabled((BluetoothDevice) null)) {
                        Log.d("MediaDeviceManager", "Can not get LocalBluetoothLeBroadcast");
                        return false;
                    }
                    getBroadcastingInfo(leAudioBroadcastProfile);
                    return true;
                }
                Log.d("MediaDeviceManager", "Can not get LocalBluetoothProfileManager");
                return false;
            }
            Log.d("MediaDeviceManager", "Can not get LocalBluetoothManager");
            return false;
        }

        private final void getBroadcastingInfo(LocalBluetoothLeBroadcast localBluetoothLeBroadcast) {
            String appSourceName = localBluetoothLeBroadcast.getAppSourceName();
            if (TextUtils.equals(MediaDataUtils.getAppLabel(this.this$0.context, this.localMediaManager.getPackageName(), this.this$0.context.getString(C1894R.string.bt_le_audio_broadcast_dialog_unknown_name)), appSourceName)) {
                this.broadcastDescription = this.this$0.context.getString(C1894R.string.broadcasting_description_is_broadcasting);
            } else {
                this.broadcastDescription = appSourceName;
            }
        }
    }
}
