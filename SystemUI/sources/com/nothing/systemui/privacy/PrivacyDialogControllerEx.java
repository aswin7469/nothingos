package com.nothing.systemui.privacy;

import android.util.ArrayMap;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 \u000b2\u00020\u0001:\u0002\u000b\fB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004J\u001a\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004R\u001c\u0010\u0003\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx;", "", "()V", "micModeInfos", "Landroid/util/ArrayMap;", "", "Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$MicModeInfo;", "getMicModeInfos", "setMicModeInfo", "", "infos", "Companion", "MicModeInfo", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDialogControllerEx.kt */
public final class PrivacyDialogControllerEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "PrivacyDialogControllerEx";
    private ArrayMap<String, MicModeInfo> micModeInfos;

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PrivacyDialogControllerEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J'\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000f¨\u0006\u001b"}, mo64987d2 = {"Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$MicModeInfo;", "", "showUI", "", "micMode", "", "speakerMode", "(ZIZ)V", "getMicMode", "()I", "setMicMode", "(I)V", "getShowUI", "()Z", "setShowUI", "(Z)V", "getSpeakerMode", "setSpeakerMode", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PrivacyDialogControllerEx.kt */
    public static final class MicModeInfo {
        private int micMode;
        private boolean showUI;
        private boolean speakerMode;

        public static /* synthetic */ MicModeInfo copy$default(MicModeInfo micModeInfo, boolean z, int i, boolean z2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = micModeInfo.showUI;
            }
            if ((i2 & 2) != 0) {
                i = micModeInfo.micMode;
            }
            if ((i2 & 4) != 0) {
                z2 = micModeInfo.speakerMode;
            }
            return micModeInfo.copy(z, i, z2);
        }

        public final boolean component1() {
            return this.showUI;
        }

        public final int component2() {
            return this.micMode;
        }

        public final boolean component3() {
            return this.speakerMode;
        }

        public final MicModeInfo copy(boolean z, int i, boolean z2) {
            return new MicModeInfo(z, i, z2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MicModeInfo)) {
                return false;
            }
            MicModeInfo micModeInfo = (MicModeInfo) obj;
            return this.showUI == micModeInfo.showUI && this.micMode == micModeInfo.micMode && this.speakerMode == micModeInfo.speakerMode;
        }

        public int hashCode() {
            boolean z = this.showUI;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int hashCode = (((z ? 1 : 0) * true) + Integer.hashCode(this.micMode)) * 31;
            boolean z3 = this.speakerMode;
            if (!z3) {
                z2 = z3;
            }
            return hashCode + (z2 ? 1 : 0);
        }

        public String toString() {
            return "MicModeInfo(showUI=" + this.showUI + ", micMode=" + this.micMode + ", speakerMode=" + this.speakerMode + ')';
        }

        public MicModeInfo(boolean z, int i, boolean z2) {
            this.showUI = z;
            this.micMode = i;
            this.speakerMode = z2;
        }

        public final int getMicMode() {
            return this.micMode;
        }

        public final boolean getShowUI() {
            return this.showUI;
        }

        public final boolean getSpeakerMode() {
            return this.speakerMode;
        }

        public final void setMicMode(int i) {
            this.micMode = i;
        }

        public final void setShowUI(boolean z) {
            this.showUI = z;
        }

        public final void setSpeakerMode(boolean z) {
            this.speakerMode = z;
        }
    }

    public final void setMicModeInfo(ArrayMap<String, MicModeInfo> arrayMap) {
        Intrinsics.checkNotNullParameter(arrayMap, "infos");
        this.micModeInfos = arrayMap;
    }

    public final ArrayMap<String, MicModeInfo> getMicModeInfos() {
        return this.micModeInfos;
    }
}
