package com.android.systemui.media.muteawait;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.dagger.qualifiers.Main;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0015\n\u0000\u0018\u00002\u00020\u0001B/\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u001cJ\f\u0010\u001e\u001a\u00020\u001f*\u00020\u0012H\u0002J\f\u0010 \u001a\u00020!*\u00020\"H\u0002R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0017\u001a\u00020\u0018¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager;", "", "mainExecutor", "Ljava/util/concurrent/Executor;", "localMediaManager", "Lcom/android/settingslib/media/LocalMediaManager;", "context", "Landroid/content/Context;", "deviceIconUtil", "Lcom/android/settingslib/media/DeviceIconUtil;", "logger", "Lcom/android/systemui/media/muteawait/MediaMuteAwaitLogger;", "(Ljava/util/concurrent/Executor;Lcom/android/settingslib/media/LocalMediaManager;Landroid/content/Context;Lcom/android/settingslib/media/DeviceIconUtil;Lcom/android/systemui/media/muteawait/MediaMuteAwaitLogger;)V", "audioManager", "Landroid/media/AudioManager;", "getAudioManager", "()Landroid/media/AudioManager;", "currentMutedDevice", "Landroid/media/AudioDeviceAttributes;", "getCurrentMutedDevice", "()Landroid/media/AudioDeviceAttributes;", "setCurrentMutedDevice", "(Landroid/media/AudioDeviceAttributes;)V", "muteAwaitConnectionChangeListener", "Landroid/media/AudioManager$MuteAwaitConnectionCallback;", "getMuteAwaitConnectionChangeListener", "()Landroid/media/AudioManager$MuteAwaitConnectionCallback;", "startListening", "", "stopListening", "getIcon", "Landroid/graphics/drawable/Drawable;", "hasMedia", "", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaMuteAwaitConnectionManager.kt */
public final class MediaMuteAwaitConnectionManager {
    private final AudioManager audioManager;
    private final Context context;
    private AudioDeviceAttributes currentMutedDevice;
    private final DeviceIconUtil deviceIconUtil;
    /* access modifiers changed from: private */
    public final LocalMediaManager localMediaManager;
    /* access modifiers changed from: private */
    public final MediaMuteAwaitLogger logger;
    private final Executor mainExecutor;
    private final AudioManager.MuteAwaitConnectionCallback muteAwaitConnectionChangeListener;

    public MediaMuteAwaitConnectionManager(@Main Executor executor, LocalMediaManager localMediaManager2, Context context2, DeviceIconUtil deviceIconUtil2, MediaMuteAwaitLogger mediaMuteAwaitLogger) {
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(localMediaManager2, "localMediaManager");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(deviceIconUtil2, "deviceIconUtil");
        Intrinsics.checkNotNullParameter(mediaMuteAwaitLogger, "logger");
        this.mainExecutor = executor;
        this.localMediaManager = localMediaManager2;
        this.context = context2;
        this.deviceIconUtil = deviceIconUtil2;
        this.logger = mediaMuteAwaitLogger;
        Object systemService = context2.getSystemService("audio");
        if (systemService != null) {
            this.audioManager = (AudioManager) systemService;
            this.muteAwaitConnectionChangeListener = new C2237x5cc98e9e(this);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
    }

    public final AudioDeviceAttributes getCurrentMutedDevice() {
        return this.currentMutedDevice;
    }

    public final void setCurrentMutedDevice(AudioDeviceAttributes audioDeviceAttributes) {
        this.currentMutedDevice = audioDeviceAttributes;
    }

    public final AudioManager getAudioManager() {
        return this.audioManager;
    }

    public final AudioManager.MuteAwaitConnectionCallback getMuteAwaitConnectionChangeListener() {
        return this.muteAwaitConnectionChangeListener;
    }

    public final void startListening() {
        this.audioManager.registerMuteAwaitConnectionCallback(this.mainExecutor, this.muteAwaitConnectionChangeListener);
        AudioDeviceAttributes mutingExpectedDevice = this.audioManager.getMutingExpectedDevice();
        if (mutingExpectedDevice != null) {
            this.currentMutedDevice = mutingExpectedDevice;
            this.localMediaManager.dispatchAboutToConnectDeviceAdded(mutingExpectedDevice.getAddress(), mutingExpectedDevice.getName(), getIcon(mutingExpectedDevice));
        }
    }

    public final void stopListening() {
        this.audioManager.unregisterMuteAwaitConnectionCallback(this.muteAwaitConnectionChangeListener);
    }

    /* access modifiers changed from: private */
    public final Drawable getIcon(AudioDeviceAttributes audioDeviceAttributes) {
        Drawable iconFromAudioDeviceType = this.deviceIconUtil.getIconFromAudioDeviceType(audioDeviceAttributes.getType(), this.context);
        Intrinsics.checkNotNullExpressionValue(iconFromAudioDeviceType, "deviceIconUtil.getIconFr…eType(this.type, context)");
        return iconFromAudioDeviceType;
    }

    /* access modifiers changed from: private */
    public final boolean hasMedia(int[] iArr) {
        return ArraysKt.contains(iArr, 1);
    }
}
