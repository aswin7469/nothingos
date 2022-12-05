package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ControlInfoWrapper extends ElementWrapper implements ControlInterface {
    @NotNull
    private final ComponentName component;
    @NotNull
    private final ControlInfo controlInfo;
    @NotNull
    private Function2<? super ComponentName, ? super String, Icon> customIconGetter;
    private boolean favorite;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfoWrapper)) {
            return false;
        }
        ControlInfoWrapper controlInfoWrapper = (ControlInfoWrapper) obj;
        return Intrinsics.areEqual(getComponent(), controlInfoWrapper.getComponent()) && Intrinsics.areEqual(this.controlInfo, controlInfoWrapper.controlInfo) && getFavorite() == controlInfoWrapper.getFavorite();
    }

    public int hashCode() {
        int hashCode = ((getComponent().hashCode() * 31) + this.controlInfo.hashCode()) * 31;
        boolean favorite = getFavorite();
        if (favorite) {
            favorite = true;
        }
        int i = favorite ? 1 : 0;
        int i2 = favorite ? 1 : 0;
        return hashCode + i;
    }

    @NotNull
    public String toString() {
        return "ControlInfoWrapper(component=" + getComponent() + ", controlInfo=" + this.controlInfo + ", favorite=" + getFavorite() + ')';
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getRemoved() {
        return ControlInterface.DefaultImpls.getRemoved(this);
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public ComponentName getComponent() {
        return this.component;
    }

    @NotNull
    public final ControlInfo getControlInfo() {
        return this.controlInfo;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(@NotNull ComponentName component, @NotNull ControlInfo controlInfo, boolean z) {
        super(null);
        Intrinsics.checkNotNullParameter(component, "component");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        this.component = component;
        this.controlInfo = controlInfo;
        this.favorite = z;
        this.customIconGetter = ControlInfoWrapper$customIconGetter$1.INSTANCE;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(@NotNull ComponentName component, @NotNull ControlInfo controlInfo, boolean z, @NotNull Function2<? super ComponentName, ? super String, Icon> customIconGetter) {
        this(component, controlInfo, z);
        Intrinsics.checkNotNullParameter(component, "component");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(customIconGetter, "customIconGetter");
        this.customIconGetter = customIconGetter;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public String getControlId() {
        return this.controlInfo.getControlId();
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public CharSequence getTitle() {
        return this.controlInfo.getControlTitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public CharSequence getSubtitle() {
        return this.controlInfo.getControlSubtitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public int getDeviceType() {
        return this.controlInfo.getDeviceType();
    }

    @Override // com.android.systemui.controls.ControlInterface
    @Nullable
    public Icon getCustomIcon() {
        return this.customIconGetter.mo1950invoke(getComponent(), getControlId());
    }
}
