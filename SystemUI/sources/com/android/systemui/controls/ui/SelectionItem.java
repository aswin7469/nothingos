package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class SelectionItem {
    @NotNull
    private final CharSequence appName;
    @NotNull
    private final ComponentName componentName;
    @NotNull
    private final Drawable icon;
    @NotNull
    private final CharSequence structure;
    private final int uid;

    public static /* synthetic */ SelectionItem copy$default(SelectionItem selectionItem, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = selectionItem.appName;
        }
        if ((i2 & 2) != 0) {
            charSequence2 = selectionItem.structure;
        }
        CharSequence charSequence3 = charSequence2;
        if ((i2 & 4) != 0) {
            drawable = selectionItem.icon;
        }
        Drawable drawable2 = drawable;
        if ((i2 & 8) != 0) {
            componentName = selectionItem.componentName;
        }
        ComponentName componentName2 = componentName;
        if ((i2 & 16) != 0) {
            i = selectionItem.uid;
        }
        return selectionItem.copy(charSequence, charSequence3, drawable2, componentName2, i);
    }

    @NotNull
    public final SelectionItem copy(@NotNull CharSequence appName, @NotNull CharSequence structure, @NotNull Drawable icon, @NotNull ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(structure, "structure");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        return new SelectionItem(appName, structure, icon, componentName, i);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SelectionItem)) {
            return false;
        }
        SelectionItem selectionItem = (SelectionItem) obj;
        return Intrinsics.areEqual(this.appName, selectionItem.appName) && Intrinsics.areEqual(this.structure, selectionItem.structure) && Intrinsics.areEqual(this.icon, selectionItem.icon) && Intrinsics.areEqual(this.componentName, selectionItem.componentName) && this.uid == selectionItem.uid;
    }

    public int hashCode() {
        return (((((((this.appName.hashCode() * 31) + this.structure.hashCode()) * 31) + this.icon.hashCode()) * 31) + this.componentName.hashCode()) * 31) + Integer.hashCode(this.uid);
    }

    @NotNull
    public String toString() {
        return "SelectionItem(appName=" + ((Object) this.appName) + ", structure=" + ((Object) this.structure) + ", icon=" + this.icon + ", componentName=" + this.componentName + ", uid=" + this.uid + ')';
    }

    public SelectionItem(@NotNull CharSequence appName, @NotNull CharSequence structure, @NotNull Drawable icon, @NotNull ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(structure, "structure");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.appName = appName;
        this.structure = structure;
        this.icon = icon;
        this.componentName = componentName;
        this.uid = i;
    }

    @NotNull
    public final CharSequence getStructure() {
        return this.structure;
    }

    @NotNull
    public final Drawable getIcon() {
        return this.icon;
    }

    @NotNull
    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final int getUid() {
        return this.uid;
    }

    @NotNull
    public final CharSequence getTitle() {
        return this.structure.length() == 0 ? this.appName : this.structure;
    }
}
