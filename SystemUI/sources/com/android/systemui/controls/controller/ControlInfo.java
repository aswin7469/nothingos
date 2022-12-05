package com.android.systemui.controls.controller;

import android.service.controls.Control;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlInfo.kt */
/* loaded from: classes.dex */
public final class ControlInfo {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final String controlId;
    @NotNull
    private final CharSequence controlSubtitle;
    @NotNull
    private final CharSequence controlTitle;
    private final int deviceType;

    public static /* synthetic */ ControlInfo copy$default(ControlInfo controlInfo, String str, CharSequence charSequence, CharSequence charSequence2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = controlInfo.controlId;
        }
        if ((i2 & 2) != 0) {
            charSequence = controlInfo.controlTitle;
        }
        if ((i2 & 4) != 0) {
            charSequence2 = controlInfo.controlSubtitle;
        }
        if ((i2 & 8) != 0) {
            i = controlInfo.deviceType;
        }
        return controlInfo.copy(str, charSequence, charSequence2, i);
    }

    @NotNull
    public final ControlInfo copy(@NotNull String controlId, @NotNull CharSequence controlTitle, @NotNull CharSequence controlSubtitle, int i) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Intrinsics.checkNotNullParameter(controlTitle, "controlTitle");
        Intrinsics.checkNotNullParameter(controlSubtitle, "controlSubtitle");
        return new ControlInfo(controlId, controlTitle, controlSubtitle, i);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfo)) {
            return false;
        }
        ControlInfo controlInfo = (ControlInfo) obj;
        return Intrinsics.areEqual(this.controlId, controlInfo.controlId) && Intrinsics.areEqual(this.controlTitle, controlInfo.controlTitle) && Intrinsics.areEqual(this.controlSubtitle, controlInfo.controlSubtitle) && this.deviceType == controlInfo.deviceType;
    }

    public int hashCode() {
        return (((((this.controlId.hashCode() * 31) + this.controlTitle.hashCode()) * 31) + this.controlSubtitle.hashCode()) * 31) + Integer.hashCode(this.deviceType);
    }

    public ControlInfo(@NotNull String controlId, @NotNull CharSequence controlTitle, @NotNull CharSequence controlSubtitle, int i) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Intrinsics.checkNotNullParameter(controlTitle, "controlTitle");
        Intrinsics.checkNotNullParameter(controlSubtitle, "controlSubtitle");
        this.controlId = controlId;
        this.controlTitle = controlTitle;
        this.controlSubtitle = controlSubtitle;
        this.deviceType = i;
    }

    @NotNull
    public final String getControlId() {
        return this.controlId;
    }

    @NotNull
    public final CharSequence getControlTitle() {
        return this.controlTitle;
    }

    @NotNull
    public final CharSequence getControlSubtitle() {
        return this.controlSubtitle;
    }

    public final int getDeviceType() {
        return this.deviceType;
    }

    /* compiled from: ControlInfo.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final ControlInfo fromControl(@NotNull Control control) {
            Intrinsics.checkNotNullParameter(control, "control");
            String controlId = control.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
            CharSequence title = control.getTitle();
            Intrinsics.checkNotNullExpressionValue(title, "control.title");
            CharSequence subtitle = control.getSubtitle();
            Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
            return new ControlInfo(controlId, title, subtitle, control.getDeviceType());
        }
    }

    @NotNull
    public String toString() {
        return ':' + this.controlId + ':' + ((Object) this.controlTitle) + ':' + this.deviceType;
    }
}
