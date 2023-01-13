package com.android.systemui.statusbar.notification.people;

import android.graphics.drawable.Drawable;
import com.android.settingslib.datetime.ZoneGetter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B1\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\r\u0010\u0018\u001a\u00060\u0003j\u0002`\u0004HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001a\u001a\u00020\bHÆ\u0003J\t\u0010\u001b\u001a\u00020\nHÆ\u0003J\t\u0010\u001c\u001a\u00020\fHÆ\u0003J?\u0010\u001d\u001a\u00020\u00002\f\b\u0002\u0010\u0002\u001a\u00060\u0003j\u0002`\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fHÆ\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\u0006HÖ\u0001J\t\u0010\"\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PersonModel;", "", "key", "", "Lcom/android/systemui/statusbar/notification/people/PersonKey;", "userId", "", "name", "", "avatar", "Landroid/graphics/drawable/Drawable;", "clickRunnable", "Ljava/lang/Runnable;", "(Ljava/lang/String;ILjava/lang/CharSequence;Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;)V", "getAvatar", "()Landroid/graphics/drawable/Drawable;", "getClickRunnable", "()Ljava/lang/Runnable;", "getKey", "()Ljava/lang/String;", "getName", "()Ljava/lang/CharSequence;", "getUserId", "()I", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHub.kt */
public final class PersonModel {
    private final Drawable avatar;
    private final Runnable clickRunnable;
    private final String key;
    private final CharSequence name;
    private final int userId;

    public static /* synthetic */ PersonModel copy$default(PersonModel personModel, String str, int i, CharSequence charSequence, Drawable drawable, Runnable runnable, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = personModel.key;
        }
        if ((i2 & 2) != 0) {
            i = personModel.userId;
        }
        int i3 = i;
        if ((i2 & 4) != 0) {
            charSequence = personModel.name;
        }
        CharSequence charSequence2 = charSequence;
        if ((i2 & 8) != 0) {
            drawable = personModel.avatar;
        }
        Drawable drawable2 = drawable;
        if ((i2 & 16) != 0) {
            runnable = personModel.clickRunnable;
        }
        return personModel.copy(str, i3, charSequence2, drawable2, runnable);
    }

    public final String component1() {
        return this.key;
    }

    public final int component2() {
        return this.userId;
    }

    public final CharSequence component3() {
        return this.name;
    }

    public final Drawable component4() {
        return this.avatar;
    }

    public final Runnable component5() {
        return this.clickRunnable;
    }

    public final PersonModel copy(String str, int i, CharSequence charSequence, Drawable drawable, Runnable runnable) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(charSequence, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(drawable, "avatar");
        Intrinsics.checkNotNullParameter(runnable, "clickRunnable");
        return new PersonModel(str, i, charSequence, drawable, runnable);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PersonModel)) {
            return false;
        }
        PersonModel personModel = (PersonModel) obj;
        return Intrinsics.areEqual((Object) this.key, (Object) personModel.key) && this.userId == personModel.userId && Intrinsics.areEqual((Object) this.name, (Object) personModel.name) && Intrinsics.areEqual((Object) this.avatar, (Object) personModel.avatar) && Intrinsics.areEqual((Object) this.clickRunnable, (Object) personModel.clickRunnable);
    }

    public int hashCode() {
        return (((((((this.key.hashCode() * 31) + Integer.hashCode(this.userId)) * 31) + this.name.hashCode()) * 31) + this.avatar.hashCode()) * 31) + this.clickRunnable.hashCode();
    }

    public String toString() {
        return "PersonModel(key=" + this.key + ", userId=" + this.userId + ", name=" + this.name + ", avatar=" + this.avatar + ", clickRunnable=" + this.clickRunnable + ')';
    }

    public PersonModel(String str, int i, CharSequence charSequence, Drawable drawable, Runnable runnable) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(charSequence, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(drawable, "avatar");
        Intrinsics.checkNotNullParameter(runnable, "clickRunnable");
        this.key = str;
        this.userId = i;
        this.name = charSequence;
        this.avatar = drawable;
        this.clickRunnable = runnable;
    }

    public final String getKey() {
        return this.key;
    }

    public final int getUserId() {
        return this.userId;
    }

    public final CharSequence getName() {
        return this.name;
    }

    public final Drawable getAvatar() {
        return this.avatar;
    }

    public final Runnable getClickRunnable() {
        return this.clickRunnable;
    }
}
