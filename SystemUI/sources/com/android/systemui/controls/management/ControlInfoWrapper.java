package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\r\n\u0002\b\n\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u00012\u00020\u0002B;\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u001a\u0010\t\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\n¢\u0006\u0002\u0010\rB\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\u000eJ\t\u0010*\u001a\u00020\u0004HÆ\u0003J\t\u0010+\u001a\u00020\u0006HÆ\u0003J\t\u0010,\u001a\u00020\bHÆ\u0003J'\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010.\u001a\u00020\b2\b\u0010/\u001a\u0004\u0018\u000100HÖ\u0003J\t\u00101\u001a\u00020\u001dHÖ\u0001J\t\u00102\u001a\u00020\u000bHÖ\u0001R\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u000b8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0016\u001a\u0004\u0018\u00010\f8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018RF\u0010\t\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\n2\u001a\u0010\u0019\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\n@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u001d8VX\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0014\u0010$\u001a\u00020%8VX\u0004¢\u0006\u0006\u001a\u0004\b&\u0010'R\u0014\u0010(\u001a\u00020%8VX\u0004¢\u0006\u0006\u001a\u0004\b)\u0010'¨\u00063"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlInfoWrapper;", "Lcom/android/systemui/controls/management/ElementWrapper;", "Lcom/android/systemui/controls/ControlInterface;", "component", "Landroid/content/ComponentName;", "controlInfo", "Lcom/android/systemui/controls/controller/ControlInfo;", "favorite", "", "customIconGetter", "Lkotlin/Function2;", "", "Landroid/graphics/drawable/Icon;", "(Landroid/content/ComponentName;Lcom/android/systemui/controls/controller/ControlInfo;ZLkotlin/jvm/functions/Function2;)V", "(Landroid/content/ComponentName;Lcom/android/systemui/controls/controller/ControlInfo;Z)V", "getComponent", "()Landroid/content/ComponentName;", "controlId", "getControlId", "()Ljava/lang/String;", "getControlInfo", "()Lcom/android/systemui/controls/controller/ControlInfo;", "customIcon", "getCustomIcon", "()Landroid/graphics/drawable/Icon;", "<set-?>", "getCustomIconGetter", "()Lkotlin/jvm/functions/Function2;", "deviceType", "", "getDeviceType", "()I", "getFavorite", "()Z", "setFavorite", "(Z)V", "subtitle", "", "getSubtitle", "()Ljava/lang/CharSequence;", "title", "getTitle", "component1", "component2", "component3", "copy", "equals", "other", "", "hashCode", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsModel.kt */
public final class ControlInfoWrapper extends ElementWrapper implements ControlInterface {
    private final ComponentName component;
    private final ControlInfo controlInfo;
    private Function2<? super ComponentName, ? super String, Icon> customIconGetter;
    private boolean favorite;

    public static /* synthetic */ ControlInfoWrapper copy$default(ControlInfoWrapper controlInfoWrapper, ComponentName componentName, ControlInfo controlInfo2, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName = controlInfoWrapper.getComponent();
        }
        if ((i & 2) != 0) {
            controlInfo2 = controlInfoWrapper.controlInfo;
        }
        if ((i & 4) != 0) {
            z = controlInfoWrapper.getFavorite();
        }
        return controlInfoWrapper.copy(componentName, controlInfo2, z);
    }

    public final ComponentName component1() {
        return getComponent();
    }

    public final ControlInfo component2() {
        return this.controlInfo;
    }

    public final boolean component3() {
        return getFavorite();
    }

    public final ControlInfoWrapper copy(ComponentName componentName, ControlInfo controlInfo2, boolean z) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(controlInfo2, "controlInfo");
        return new ControlInfoWrapper(componentName, controlInfo2, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfoWrapper)) {
            return false;
        }
        ControlInfoWrapper controlInfoWrapper = (ControlInfoWrapper) obj;
        return Intrinsics.areEqual((Object) getComponent(), (Object) controlInfoWrapper.getComponent()) && Intrinsics.areEqual((Object) this.controlInfo, (Object) controlInfoWrapper.controlInfo) && getFavorite() == controlInfoWrapper.getFavorite();
    }

    public int hashCode() {
        int hashCode = ((getComponent().hashCode() * 31) + this.controlInfo.hashCode()) * 31;
        boolean favorite2 = getFavorite();
        if (favorite2) {
            favorite2 = true;
        }
        return hashCode + (favorite2 ? 1 : 0);
    }

    public String toString() {
        return "ControlInfoWrapper(component=" + getComponent() + ", controlInfo=" + this.controlInfo + ", favorite=" + getFavorite() + ')';
    }

    public ComponentName getComponent() {
        return this.component;
    }

    public final ControlInfo getControlInfo() {
        return this.controlInfo;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(ComponentName componentName, ControlInfo controlInfo2, boolean z) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(controlInfo2, "controlInfo");
        this.component = componentName;
        this.controlInfo = controlInfo2;
        this.favorite = z;
        this.customIconGetter = ControlInfoWrapper$customIconGetter$1.INSTANCE;
    }

    public final Function2<ComponentName, String, Icon> getCustomIconGetter() {
        return this.customIconGetter;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(ComponentName componentName, ControlInfo controlInfo2, boolean z, Function2<? super ComponentName, ? super String, Icon> function2) {
        this(componentName, controlInfo2, z);
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(controlInfo2, "controlInfo");
        Intrinsics.checkNotNullParameter(function2, "customIconGetter");
        this.customIconGetter = function2;
    }

    public String getControlId() {
        return this.controlInfo.getControlId();
    }

    public CharSequence getTitle() {
        return this.controlInfo.getControlTitle();
    }

    public CharSequence getSubtitle() {
        return this.controlInfo.getControlSubtitle();
    }

    public int getDeviceType() {
        return this.controlInfo.getDeviceType();
    }

    public Icon getCustomIcon() {
        return this.customIconGetter.invoke(getComponent(), getControlId());
    }
}
