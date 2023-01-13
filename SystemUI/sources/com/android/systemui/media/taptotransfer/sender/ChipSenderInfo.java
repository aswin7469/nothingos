package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007HÆ\u0003J)\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016HÖ\u0003J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001d"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/ChipSenderInfo;", "Lcom/android/systemui/media/taptotransfer/common/ChipInfoCommon;", "state", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "(Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;Landroid/media/MediaRoute2Info;Lcom/android/internal/statusbar/IUndoMediaTransferCallback;)V", "getRouteInfo", "()Landroid/media/MediaRoute2Info;", "getState", "()Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "getUndoCallback", "()Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "component1", "component2", "component3", "copy", "equals", "", "other", "", "getTimeoutMs", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttChipControllerSender.kt */
public final class ChipSenderInfo implements ChipInfoCommon {
    private final MediaRoute2Info routeInfo;
    private final ChipStateSender state;
    private final IUndoMediaTransferCallback undoCallback;

    public static /* synthetic */ ChipSenderInfo copy$default(ChipSenderInfo chipSenderInfo, ChipStateSender chipStateSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback, int i, Object obj) {
        if ((i & 1) != 0) {
            chipStateSender = chipSenderInfo.state;
        }
        if ((i & 2) != 0) {
            mediaRoute2Info = chipSenderInfo.routeInfo;
        }
        if ((i & 4) != 0) {
            iUndoMediaTransferCallback = chipSenderInfo.undoCallback;
        }
        return chipSenderInfo.copy(chipStateSender, mediaRoute2Info, iUndoMediaTransferCallback);
    }

    public final ChipStateSender component1() {
        return this.state;
    }

    public final MediaRoute2Info component2() {
        return this.routeInfo;
    }

    public final IUndoMediaTransferCallback component3() {
        return this.undoCallback;
    }

    public final ChipSenderInfo copy(ChipStateSender chipStateSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        Intrinsics.checkNotNullParameter(chipStateSender, AuthDialog.KEY_BIOMETRIC_STATE);
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        return new ChipSenderInfo(chipStateSender, mediaRoute2Info, iUndoMediaTransferCallback);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChipSenderInfo)) {
            return false;
        }
        ChipSenderInfo chipSenderInfo = (ChipSenderInfo) obj;
        return this.state == chipSenderInfo.state && Intrinsics.areEqual((Object) this.routeInfo, (Object) chipSenderInfo.routeInfo) && Intrinsics.areEqual((Object) this.undoCallback, (Object) chipSenderInfo.undoCallback);
    }

    public int hashCode() {
        int hashCode = ((this.state.hashCode() * 31) + this.routeInfo.hashCode()) * 31;
        IUndoMediaTransferCallback iUndoMediaTransferCallback = this.undoCallback;
        return hashCode + (iUndoMediaTransferCallback == null ? 0 : iUndoMediaTransferCallback.hashCode());
    }

    public String toString() {
        return "ChipSenderInfo(state=" + this.state + ", routeInfo=" + this.routeInfo + ", undoCallback=" + this.undoCallback + ')';
    }

    public ChipSenderInfo(ChipStateSender chipStateSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        Intrinsics.checkNotNullParameter(chipStateSender, AuthDialog.KEY_BIOMETRIC_STATE);
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        this.state = chipStateSender;
        this.routeInfo = mediaRoute2Info;
        this.undoCallback = iUndoMediaTransferCallback;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ChipSenderInfo(ChipStateSender chipStateSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(chipStateSender, mediaRoute2Info, (i & 4) != 0 ? null : iUndoMediaTransferCallback);
    }

    public final ChipStateSender getState() {
        return this.state;
    }

    public final MediaRoute2Info getRouteInfo() {
        return this.routeInfo;
    }

    public final IUndoMediaTransferCallback getUndoCallback() {
        return this.undoCallback;
    }

    public long getTimeoutMs() {
        return this.state.getTimeout();
    }
}
