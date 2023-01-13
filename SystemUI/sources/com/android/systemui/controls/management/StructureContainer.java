package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/controls/management/StructureContainer;", "", "structureName", "", "model", "Lcom/android/systemui/controls/management/ControlsModel;", "(Ljava/lang/CharSequence;Lcom/android/systemui/controls/management/ControlsModel;)V", "getModel", "()Lcom/android/systemui/controls/management/ControlsModel;", "getStructureName", "()Ljava/lang/CharSequence;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class StructureContainer {
    private final ControlsModel model;
    private final CharSequence structureName;

    public static /* synthetic */ StructureContainer copy$default(StructureContainer structureContainer, CharSequence charSequence, ControlsModel controlsModel, int i, Object obj) {
        if ((i & 1) != 0) {
            charSequence = structureContainer.structureName;
        }
        if ((i & 2) != 0) {
            controlsModel = structureContainer.model;
        }
        return structureContainer.copy(charSequence, controlsModel);
    }

    public final CharSequence component1() {
        return this.structureName;
    }

    public final ControlsModel component2() {
        return this.model;
    }

    public final StructureContainer copy(CharSequence charSequence, ControlsModel controlsModel) {
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        Intrinsics.checkNotNullParameter(controlsModel, "model");
        return new StructureContainer(charSequence, controlsModel);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureContainer)) {
            return false;
        }
        StructureContainer structureContainer = (StructureContainer) obj;
        return Intrinsics.areEqual((Object) this.structureName, (Object) structureContainer.structureName) && Intrinsics.areEqual((Object) this.model, (Object) structureContainer.model);
    }

    public int hashCode() {
        return (this.structureName.hashCode() * 31) + this.model.hashCode();
    }

    public String toString() {
        return "StructureContainer(structureName=" + this.structureName + ", model=" + this.model + ')';
    }

    public StructureContainer(CharSequence charSequence, ControlsModel controlsModel) {
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        Intrinsics.checkNotNullParameter(controlsModel, "model");
        this.structureName = charSequence;
        this.model = controlsModel;
    }

    public final ControlsModel getModel() {
        return this.model;
    }

    public final CharSequence getStructureName() {
        return this.structureName;
    }
}
