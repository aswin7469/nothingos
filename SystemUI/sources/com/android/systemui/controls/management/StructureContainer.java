package com.android.systemui.controls.management;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class StructureContainer {
    @NotNull
    private final ControlsModel model;
    @NotNull
    private final CharSequence structureName;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureContainer)) {
            return false;
        }
        StructureContainer structureContainer = (StructureContainer) obj;
        return Intrinsics.areEqual(this.structureName, structureContainer.structureName) && Intrinsics.areEqual(this.model, structureContainer.model);
    }

    public int hashCode() {
        return (this.structureName.hashCode() * 31) + this.model.hashCode();
    }

    @NotNull
    public String toString() {
        return "StructureContainer(structureName=" + ((Object) this.structureName) + ", model=" + this.model + ')';
    }

    public StructureContainer(@NotNull CharSequence structureName, @NotNull ControlsModel model) {
        Intrinsics.checkNotNullParameter(structureName, "structureName");
        Intrinsics.checkNotNullParameter(model, "model");
        this.structureName = structureName;
        this.model = model;
    }

    @NotNull
    public final ControlsModel getModel() {
        return this.model;
    }

    @NotNull
    public final CharSequence getStructureName() {
        return this.structureName;
    }
}
