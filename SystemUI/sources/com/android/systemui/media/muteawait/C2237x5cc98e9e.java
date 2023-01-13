package com.android.systemui.media.muteawait;

import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J \u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\u000b"}, mo65043d2 = {"com/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1", "Landroid/media/AudioManager$MuteAwaitConnectionCallback;", "onMutedUntilConnection", "", "device", "Landroid/media/AudioDeviceAttributes;", "mutedUsages", "", "onUnmutedEvent", "unmuteEvent", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1 */
/* compiled from: MediaMuteAwaitConnectionManager.kt */
public final class C2237x5cc98e9e extends AudioManager.MuteAwaitConnectionCallback {
    final /* synthetic */ MediaMuteAwaitConnectionManager this$0;

    C2237x5cc98e9e(MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager) {
        this.this$0 = mediaMuteAwaitConnectionManager;
    }

    public void onMutedUntilConnection(AudioDeviceAttributes audioDeviceAttributes, int[] iArr) {
        Intrinsics.checkNotNullParameter(audioDeviceAttributes, "device");
        Intrinsics.checkNotNullParameter(iArr, "mutedUsages");
        MediaMuteAwaitLogger access$getLogger$p = this.this$0.logger;
        String address = audioDeviceAttributes.getAddress();
        Intrinsics.checkNotNullExpressionValue(address, "device.address");
        String name = audioDeviceAttributes.getName();
        Intrinsics.checkNotNullExpressionValue(name, "device.name");
        access$getLogger$p.logMutedDeviceAdded(address, name, this.this$0.hasMedia(iArr));
        if (this.this$0.hasMedia(iArr)) {
            this.this$0.setCurrentMutedDevice(audioDeviceAttributes);
            this.this$0.localMediaManager.dispatchAboutToConnectDeviceAdded(audioDeviceAttributes.getAddress(), audioDeviceAttributes.getName(), this.this$0.getIcon(audioDeviceAttributes));
        }
    }

    public void onUnmutedEvent(int i, AudioDeviceAttributes audioDeviceAttributes, int[] iArr) {
        Intrinsics.checkNotNullParameter(audioDeviceAttributes, "device");
        Intrinsics.checkNotNullParameter(iArr, "mutedUsages");
        boolean areEqual = Intrinsics.areEqual((Object) this.this$0.getCurrentMutedDevice(), (Object) audioDeviceAttributes);
        MediaMuteAwaitLogger access$getLogger$p = this.this$0.logger;
        String address = audioDeviceAttributes.getAddress();
        Intrinsics.checkNotNullExpressionValue(address, "device.address");
        String name = audioDeviceAttributes.getName();
        Intrinsics.checkNotNullExpressionValue(name, "device.name");
        access$getLogger$p.logMutedDeviceRemoved(address, name, this.this$0.hasMedia(iArr), areEqual);
        if (areEqual && this.this$0.hasMedia(iArr)) {
            this.this$0.setCurrentMutedDevice((AudioDeviceAttributes) null);
            this.this$0.localMediaManager.dispatchAboutToConnectDeviceRemoved();
        }
    }
}
