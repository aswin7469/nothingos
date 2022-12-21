package com.android.systemui.controls.controller;

import android.content.ComponentName;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0003J-\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/StructureInfo;", "", "componentName", "Landroid/content/ComponentName;", "structure", "", "controls", "", "Lcom/android/systemui/controls/controller/ControlInfo;", "(Landroid/content/ComponentName;Ljava/lang/CharSequence;Ljava/util/List;)V", "getComponentName", "()Landroid/content/ComponentName;", "getControls", "()Ljava/util/List;", "getStructure", "()Ljava/lang/CharSequence;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StructureInfo.kt */
public final class StructureInfo {
    private final ComponentName componentName;
    private final List<ControlInfo> controls;
    private final CharSequence structure;

    public static /* synthetic */ StructureInfo copy$default(StructureInfo structureInfo, ComponentName componentName2, CharSequence charSequence, List<ControlInfo> list, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName2 = structureInfo.componentName;
        }
        if ((i & 2) != 0) {
            charSequence = structureInfo.structure;
        }
        if ((i & 4) != 0) {
            list = structureInfo.controls;
        }
        return structureInfo.copy(componentName2, charSequence, list);
    }

    public final ComponentName component1() {
        return this.componentName;
    }

    public final CharSequence component2() {
        return this.structure;
    }

    public final List<ControlInfo> component3() {
        return this.controls;
    }

    public final StructureInfo copy(ComponentName componentName2, CharSequence charSequence, List<ControlInfo> list) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence, "structure");
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        return new StructureInfo(componentName2, charSequence, list);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureInfo)) {
            return false;
        }
        StructureInfo structureInfo = (StructureInfo) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) structureInfo.componentName) && Intrinsics.areEqual((Object) this.structure, (Object) structureInfo.structure) && Intrinsics.areEqual((Object) this.controls, (Object) structureInfo.controls);
    }

    public int hashCode() {
        return (((this.componentName.hashCode() * 31) + this.structure.hashCode()) * 31) + this.controls.hashCode();
    }

    public String toString() {
        return "StructureInfo(componentName=" + this.componentName + ", structure=" + this.structure + ", controls=" + this.controls + ')';
    }

    public StructureInfo(ComponentName componentName2, CharSequence charSequence, List<ControlInfo> list) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence, "structure");
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        this.componentName = componentName2;
        this.structure = charSequence;
        this.controls = list;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final CharSequence getStructure() {
        return this.structure;
    }

    public final List<ControlInfo> getControls() {
        return this.controls;
    }
}
