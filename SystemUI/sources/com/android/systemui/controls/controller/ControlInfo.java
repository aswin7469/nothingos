package com.android.systemui.controls.controller;

import android.service.controls.Control;
import com.android.settingslib.accessibility.AccessibilityUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0005\b\b\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\bHÆ\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\bHÖ\u0001J\b\u0010\u001a\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001c"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ControlInfo;", "", "controlId", "", "controlTitle", "", "controlSubtitle", "deviceType", "", "(Ljava/lang/String;Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)V", "getControlId", "()Ljava/lang/String;", "getControlSubtitle", "()Ljava/lang/CharSequence;", "getControlTitle", "getDeviceType", "()I", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlInfo.kt */
public final class ControlInfo {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String SEPARATOR = ":";
    private final String controlId;
    private final CharSequence controlSubtitle;
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

    public final String component1() {
        return this.controlId;
    }

    public final CharSequence component2() {
        return this.controlTitle;
    }

    public final CharSequence component3() {
        return this.controlSubtitle;
    }

    public final int component4() {
        return this.deviceType;
    }

    public final ControlInfo copy(String str, CharSequence charSequence, CharSequence charSequence2, int i) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(charSequence, "controlTitle");
        Intrinsics.checkNotNullParameter(charSequence2, "controlSubtitle");
        return new ControlInfo(str, charSequence, charSequence2, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfo)) {
            return false;
        }
        ControlInfo controlInfo = (ControlInfo) obj;
        return Intrinsics.areEqual((Object) this.controlId, (Object) controlInfo.controlId) && Intrinsics.areEqual((Object) this.controlTitle, (Object) controlInfo.controlTitle) && Intrinsics.areEqual((Object) this.controlSubtitle, (Object) controlInfo.controlSubtitle) && this.deviceType == controlInfo.deviceType;
    }

    public int hashCode() {
        return (((((this.controlId.hashCode() * 31) + this.controlTitle.hashCode()) * 31) + this.controlSubtitle.hashCode()) * 31) + Integer.hashCode(this.deviceType);
    }

    public ControlInfo(String str, CharSequence charSequence, CharSequence charSequence2, int i) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(charSequence, "controlTitle");
        Intrinsics.checkNotNullParameter(charSequence2, "controlSubtitle");
        this.controlId = str;
        this.controlTitle = charSequence;
        this.controlSubtitle = charSequence2;
        this.deviceType = i;
    }

    public final String getControlId() {
        return this.controlId;
    }

    public final CharSequence getControlTitle() {
        return this.controlTitle;
    }

    public final CharSequence getControlSubtitle() {
        return this.controlSubtitle;
    }

    public final int getDeviceType() {
        return this.deviceType;
    }

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ControlInfo$Companion;", "", "()V", "SEPARATOR", "", "fromControl", "Lcom/android/systemui/controls/controller/ControlInfo;", "control", "Landroid/service/controls/Control;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlInfo.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ControlInfo fromControl(Control control) {
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

    public String toString() {
        return ":" + this.controlId + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + this.controlTitle + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + this.deviceType;
    }
}
