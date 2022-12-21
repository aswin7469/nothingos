package com.android.systemui.statusbar.notification.people;

import android.graphics.drawable.Drawable;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.settingslib.datetime.ZoneGetter;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0003J-\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "", "name", "", "icon", "Landroid/graphics/drawable/Drawable;", "onClick", "Lkotlin/Function0;", "", "(Ljava/lang/CharSequence;Landroid/graphics/drawable/Drawable;Lkotlin/jvm/functions/Function0;)V", "getIcon", "()Landroid/graphics/drawable/Drawable;", "getName", "()Ljava/lang/CharSequence;", "getOnClick", "()Lkotlin/jvm/functions/Function0;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHub.kt */
public final class PersonViewModel {
    private final Drawable icon;
    private final CharSequence name;
    private final Function0<Unit> onClick;

    public static /* synthetic */ PersonViewModel copy$default(PersonViewModel personViewModel, CharSequence charSequence, Drawable drawable, Function0<Unit> function0, int i, Object obj) {
        if ((i & 1) != 0) {
            charSequence = personViewModel.name;
        }
        if ((i & 2) != 0) {
            drawable = personViewModel.icon;
        }
        if ((i & 4) != 0) {
            function0 = personViewModel.onClick;
        }
        return personViewModel.copy(charSequence, drawable, function0);
    }

    public final CharSequence component1() {
        return this.name;
    }

    public final Drawable component2() {
        return this.icon;
    }

    public final Function0<Unit> component3() {
        return this.onClick;
    }

    public final PersonViewModel copy(CharSequence charSequence, Drawable drawable, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(charSequence, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(function0, "onClick");
        return new PersonViewModel(charSequence, drawable, function0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PersonViewModel)) {
            return false;
        }
        PersonViewModel personViewModel = (PersonViewModel) obj;
        return Intrinsics.areEqual((Object) this.name, (Object) personViewModel.name) && Intrinsics.areEqual((Object) this.icon, (Object) personViewModel.icon) && Intrinsics.areEqual((Object) this.onClick, (Object) personViewModel.onClick);
    }

    public int hashCode() {
        return (((this.name.hashCode() * 31) + this.icon.hashCode()) * 31) + this.onClick.hashCode();
    }

    public String toString() {
        return "PersonViewModel(name=" + this.name + ", icon=" + this.icon + ", onClick=" + this.onClick + ')';
    }

    public PersonViewModel(CharSequence charSequence, Drawable drawable, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(charSequence, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(function0, "onClick");
        this.name = charSequence;
        this.icon = drawable;
        this.onClick = function0;
    }

    public final CharSequence getName() {
        return this.name;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final Function0<Unit> getOnClick() {
        return this.onClick;
    }
}
