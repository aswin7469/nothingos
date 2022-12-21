package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import com.android.launcher3.icons.cache.BaseIconCache;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0018\u001a\u00020\bHÆ\u0003J\t\u0010\u0019\u001a\u00020\nHÆ\u0003J;\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0006\u0010\u001e\u001a\u00020\u0003J\t\u0010\u001f\u001a\u00020\nHÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006\""}, mo64987d2 = {"Lcom/android/systemui/controls/ui/SelectionItem;", "", "appName", "", "structure", "icon", "Landroid/graphics/drawable/Drawable;", "componentName", "Landroid/content/ComponentName;", "uid", "", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Landroid/graphics/drawable/Drawable;Landroid/content/ComponentName;I)V", "getAppName", "()Ljava/lang/CharSequence;", "getComponentName", "()Landroid/content/ComponentName;", "getIcon", "()Landroid/graphics/drawable/Drawable;", "getStructure", "getUid", "()I", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "getTitle", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.SelectionItem */
/* compiled from: ControlsUiControllerImpl.kt */
final class SelectionItem {
    private final CharSequence appName;
    private final ComponentName componentName;
    private final Drawable icon;
    private final CharSequence structure;
    private final int uid;

    public static /* synthetic */ SelectionItem copy$default(SelectionItem selectionItem, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName2, int i, int i2, Object obj) {
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
            componentName2 = selectionItem.componentName;
        }
        ComponentName componentName3 = componentName2;
        if ((i2 & 16) != 0) {
            i = selectionItem.uid;
        }
        return selectionItem.copy(charSequence, charSequence3, drawable2, componentName3, i);
    }

    public final CharSequence component1() {
        return this.appName;
    }

    public final CharSequence component2() {
        return this.structure;
    }

    public final Drawable component3() {
        return this.icon;
    }

    public final ComponentName component4() {
        return this.componentName;
    }

    public final int component5() {
        return this.uid;
    }

    public final SelectionItem copy(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName2, int i) {
        Intrinsics.checkNotNullParameter(charSequence, "appName");
        Intrinsics.checkNotNullParameter(charSequence2, "structure");
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        return new SelectionItem(charSequence, charSequence2, drawable, componentName2, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SelectionItem)) {
            return false;
        }
        SelectionItem selectionItem = (SelectionItem) obj;
        return Intrinsics.areEqual((Object) this.appName, (Object) selectionItem.appName) && Intrinsics.areEqual((Object) this.structure, (Object) selectionItem.structure) && Intrinsics.areEqual((Object) this.icon, (Object) selectionItem.icon) && Intrinsics.areEqual((Object) this.componentName, (Object) selectionItem.componentName) && this.uid == selectionItem.uid;
    }

    public int hashCode() {
        return (((((((this.appName.hashCode() * 31) + this.structure.hashCode()) * 31) + this.icon.hashCode()) * 31) + this.componentName.hashCode()) * 31) + Integer.hashCode(this.uid);
    }

    public String toString() {
        return "SelectionItem(appName=" + this.appName + ", structure=" + this.structure + ", icon=" + this.icon + ", componentName=" + this.componentName + ", uid=" + this.uid + ')';
    }

    public SelectionItem(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName2, int i) {
        Intrinsics.checkNotNullParameter(charSequence, "appName");
        Intrinsics.checkNotNullParameter(charSequence2, "structure");
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        this.appName = charSequence;
        this.structure = charSequence2;
        this.icon = drawable;
        this.componentName = componentName2;
        this.uid = i;
    }

    public final CharSequence getAppName() {
        return this.appName;
    }

    public final CharSequence getStructure() {
        return this.structure;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final int getUid() {
        return this.uid;
    }

    public final CharSequence getTitle() {
        return this.structure.length() == 0 ? this.appName : this.structure;
    }
}
