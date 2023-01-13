package com.android.systemui.media;

import com.android.settingslib.media.MediaDevice;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/media/AboutToConnectDevice;", "", "fullMediaDevice", "Lcom/android/settingslib/media/MediaDevice;", "backupMediaDeviceData", "Lcom/android/systemui/media/MediaDeviceData;", "(Lcom/android/settingslib/media/MediaDevice;Lcom/android/systemui/media/MediaDeviceData;)V", "getBackupMediaDeviceData", "()Lcom/android/systemui/media/MediaDeviceData;", "getFullMediaDevice", "()Lcom/android/settingslib/media/MediaDevice;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDeviceManager.kt */
final class AboutToConnectDevice {
    private final MediaDeviceData backupMediaDeviceData;
    private final MediaDevice fullMediaDevice;

    public AboutToConnectDevice() {
        this((MediaDevice) null, (MediaDeviceData) null, 3, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ AboutToConnectDevice copy$default(AboutToConnectDevice aboutToConnectDevice, MediaDevice mediaDevice, MediaDeviceData mediaDeviceData, int i, Object obj) {
        if ((i & 1) != 0) {
            mediaDevice = aboutToConnectDevice.fullMediaDevice;
        }
        if ((i & 2) != 0) {
            mediaDeviceData = aboutToConnectDevice.backupMediaDeviceData;
        }
        return aboutToConnectDevice.copy(mediaDevice, mediaDeviceData);
    }

    public final MediaDevice component1() {
        return this.fullMediaDevice;
    }

    public final MediaDeviceData component2() {
        return this.backupMediaDeviceData;
    }

    public final AboutToConnectDevice copy(MediaDevice mediaDevice, MediaDeviceData mediaDeviceData) {
        return new AboutToConnectDevice(mediaDevice, mediaDeviceData);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AboutToConnectDevice)) {
            return false;
        }
        AboutToConnectDevice aboutToConnectDevice = (AboutToConnectDevice) obj;
        return Intrinsics.areEqual((Object) this.fullMediaDevice, (Object) aboutToConnectDevice.fullMediaDevice) && Intrinsics.areEqual((Object) this.backupMediaDeviceData, (Object) aboutToConnectDevice.backupMediaDeviceData);
    }

    public int hashCode() {
        MediaDevice mediaDevice = this.fullMediaDevice;
        int i = 0;
        int hashCode = (mediaDevice == null ? 0 : mediaDevice.hashCode()) * 31;
        MediaDeviceData mediaDeviceData = this.backupMediaDeviceData;
        if (mediaDeviceData != null) {
            i = mediaDeviceData.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "AboutToConnectDevice(fullMediaDevice=" + this.fullMediaDevice + ", backupMediaDeviceData=" + this.backupMediaDeviceData + ')';
    }

    public AboutToConnectDevice(MediaDevice mediaDevice, MediaDeviceData mediaDeviceData) {
        this.fullMediaDevice = mediaDevice;
        this.backupMediaDeviceData = mediaDeviceData;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AboutToConnectDevice(MediaDevice mediaDevice, MediaDeviceData mediaDeviceData, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : mediaDevice, (i & 2) != 0 ? null : mediaDeviceData);
    }

    public final MediaDevice getFullMediaDevice() {
        return this.fullMediaDevice;
    }

    public final MediaDeviceData getBackupMediaDeviceData() {
        return this.backupMediaDeviceData;
    }
}
