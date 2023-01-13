package com.android.systemui.media;

import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0017\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001BU\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t¢\u0006\u0002\u0010\u000bJ\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\tHÆ\u0003J\t\u0010\u001b\u001a\u00020\tHÆ\u0003JY\u0010\u001c\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001d\u001a\u00020\t2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\u00032\u0006\u0010 \u001a\u00020!J\t\u0010\"\u001a\u00020!HÖ\u0001J\t\u0010#\u001a\u00020$HÖ\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\n\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013¨\u0006%"}, mo65043d2 = {"Lcom/android/systemui/media/MediaButton;", "", "playOrPause", "Lcom/android/systemui/media/MediaAction;", "nextOrCustom", "prevOrCustom", "custom0", "custom1", "reserveNext", "", "reservePrev", "(Lcom/android/systemui/media/MediaAction;Lcom/android/systemui/media/MediaAction;Lcom/android/systemui/media/MediaAction;Lcom/android/systemui/media/MediaAction;Lcom/android/systemui/media/MediaAction;ZZ)V", "getCustom0", "()Lcom/android/systemui/media/MediaAction;", "getCustom1", "getNextOrCustom", "getPlayOrPause", "getPrevOrCustom", "getReserveNext", "()Z", "getReservePrev", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "getActionById", "id", "", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaData.kt */
public final class MediaButton {
    private final MediaAction custom0;
    private final MediaAction custom1;
    private final MediaAction nextOrCustom;
    private final MediaAction playOrPause;
    private final MediaAction prevOrCustom;
    private final boolean reserveNext;
    private final boolean reservePrev;

    public MediaButton() {
        this((MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, 127, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ MediaButton copy$default(MediaButton mediaButton, MediaAction mediaAction, MediaAction mediaAction2, MediaAction mediaAction3, MediaAction mediaAction4, MediaAction mediaAction5, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            mediaAction = mediaButton.playOrPause;
        }
        if ((i & 2) != 0) {
            mediaAction2 = mediaButton.nextOrCustom;
        }
        MediaAction mediaAction6 = mediaAction2;
        if ((i & 4) != 0) {
            mediaAction3 = mediaButton.prevOrCustom;
        }
        MediaAction mediaAction7 = mediaAction3;
        if ((i & 8) != 0) {
            mediaAction4 = mediaButton.custom0;
        }
        MediaAction mediaAction8 = mediaAction4;
        if ((i & 16) != 0) {
            mediaAction5 = mediaButton.custom1;
        }
        MediaAction mediaAction9 = mediaAction5;
        if ((i & 32) != 0) {
            z = mediaButton.reserveNext;
        }
        boolean z3 = z;
        if ((i & 64) != 0) {
            z2 = mediaButton.reservePrev;
        }
        return mediaButton.copy(mediaAction, mediaAction6, mediaAction7, mediaAction8, mediaAction9, z3, z2);
    }

    public final MediaAction component1() {
        return this.playOrPause;
    }

    public final MediaAction component2() {
        return this.nextOrCustom;
    }

    public final MediaAction component3() {
        return this.prevOrCustom;
    }

    public final MediaAction component4() {
        return this.custom0;
    }

    public final MediaAction component5() {
        return this.custom1;
    }

    public final boolean component6() {
        return this.reserveNext;
    }

    public final boolean component7() {
        return this.reservePrev;
    }

    public final MediaButton copy(MediaAction mediaAction, MediaAction mediaAction2, MediaAction mediaAction3, MediaAction mediaAction4, MediaAction mediaAction5, boolean z, boolean z2) {
        return new MediaButton(mediaAction, mediaAction2, mediaAction3, mediaAction4, mediaAction5, z, z2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaButton)) {
            return false;
        }
        MediaButton mediaButton = (MediaButton) obj;
        return Intrinsics.areEqual((Object) this.playOrPause, (Object) mediaButton.playOrPause) && Intrinsics.areEqual((Object) this.nextOrCustom, (Object) mediaButton.nextOrCustom) && Intrinsics.areEqual((Object) this.prevOrCustom, (Object) mediaButton.prevOrCustom) && Intrinsics.areEqual((Object) this.custom0, (Object) mediaButton.custom0) && Intrinsics.areEqual((Object) this.custom1, (Object) mediaButton.custom1) && this.reserveNext == mediaButton.reserveNext && this.reservePrev == mediaButton.reservePrev;
    }

    public int hashCode() {
        MediaAction mediaAction = this.playOrPause;
        int i = 0;
        int hashCode = (mediaAction == null ? 0 : mediaAction.hashCode()) * 31;
        MediaAction mediaAction2 = this.nextOrCustom;
        int hashCode2 = (hashCode + (mediaAction2 == null ? 0 : mediaAction2.hashCode())) * 31;
        MediaAction mediaAction3 = this.prevOrCustom;
        int hashCode3 = (hashCode2 + (mediaAction3 == null ? 0 : mediaAction3.hashCode())) * 31;
        MediaAction mediaAction4 = this.custom0;
        int hashCode4 = (hashCode3 + (mediaAction4 == null ? 0 : mediaAction4.hashCode())) * 31;
        MediaAction mediaAction5 = this.custom1;
        if (mediaAction5 != null) {
            i = mediaAction5.hashCode();
        }
        int i2 = (hashCode4 + i) * 31;
        boolean z = this.reserveNext;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i3 = (i2 + (z ? 1 : 0)) * 31;
        boolean z3 = this.reservePrev;
        if (!z3) {
            z2 = z3;
        }
        return i3 + (z2 ? 1 : 0);
    }

    public String toString() {
        return "MediaButton(playOrPause=" + this.playOrPause + ", nextOrCustom=" + this.nextOrCustom + ", prevOrCustom=" + this.prevOrCustom + ", custom0=" + this.custom0 + ", custom1=" + this.custom1 + ", reserveNext=" + this.reserveNext + ", reservePrev=" + this.reservePrev + ')';
    }

    public MediaButton(MediaAction mediaAction, MediaAction mediaAction2, MediaAction mediaAction3, MediaAction mediaAction4, MediaAction mediaAction5, boolean z, boolean z2) {
        this.playOrPause = mediaAction;
        this.nextOrCustom = mediaAction2;
        this.prevOrCustom = mediaAction3;
        this.custom0 = mediaAction4;
        this.custom1 = mediaAction5;
        this.reserveNext = z;
        this.reservePrev = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaButton(MediaAction mediaAction, MediaAction mediaAction2, MediaAction mediaAction3, MediaAction mediaAction4, MediaAction mediaAction5, boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : mediaAction, (i & 2) != 0 ? null : mediaAction2, (i & 4) != 0 ? null : mediaAction3, (i & 8) != 0 ? null : mediaAction4, (i & 16) != 0 ? null : mediaAction5, (i & 32) != 0 ? false : z, (i & 64) != 0 ? false : z2);
    }

    public final MediaAction getPlayOrPause() {
        return this.playOrPause;
    }

    public final MediaAction getNextOrCustom() {
        return this.nextOrCustom;
    }

    public final MediaAction getPrevOrCustom() {
        return this.prevOrCustom;
    }

    public final MediaAction getCustom0() {
        return this.custom0;
    }

    public final MediaAction getCustom1() {
        return this.custom1;
    }

    public final boolean getReserveNext() {
        return this.reserveNext;
    }

    public final boolean getReservePrev() {
        return this.reservePrev;
    }

    public final MediaAction getActionById(int i) {
        switch (i) {
            case C1894R.C1898id.action0:
                return this.custom0;
            case C1894R.C1898id.action1:
                return this.custom1;
            case C1894R.C1898id.actionNext:
                return this.nextOrCustom;
            case C1894R.C1898id.actionPlayPause:
                return this.playOrPause;
            case C1894R.C1898id.actionPrev:
                return this.prevOrCustom;
            default:
                return null;
        }
    }
}
