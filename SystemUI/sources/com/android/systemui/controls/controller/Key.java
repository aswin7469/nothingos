package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.os.UserHandle;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/Key;", "", "component", "Landroid/content/ComponentName;", "user", "Landroid/os/UserHandle;", "(Landroid/content/ComponentName;Landroid/os/UserHandle;)V", "getComponent", "()Landroid/content/ComponentName;", "getUser", "()Landroid/os/UserHandle;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsBindingControllerImpl.kt */
final class Key {
    private final ComponentName component;
    private final UserHandle user;

    public static /* synthetic */ Key copy$default(Key key, ComponentName componentName, UserHandle userHandle, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName = key.component;
        }
        if ((i & 2) != 0) {
            userHandle = key.user;
        }
        return key.copy(componentName, userHandle);
    }

    public final ComponentName component1() {
        return this.component;
    }

    public final UserHandle component2() {
        return this.user;
    }

    public final Key copy(ComponentName componentName, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        return new Key(componentName, userHandle);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Key)) {
            return false;
        }
        Key key = (Key) obj;
        return Intrinsics.areEqual((Object) this.component, (Object) key.component) && Intrinsics.areEqual((Object) this.user, (Object) key.user);
    }

    public int hashCode() {
        return (this.component.hashCode() * 31) + this.user.hashCode();
    }

    public String toString() {
        return "Key(component=" + this.component + ", user=" + this.user + ')';
    }

    public Key(ComponentName componentName, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        this.component = componentName;
        this.user = userHandle;
    }

    public final ComponentName getComponent() {
        return this.component;
    }

    public final UserHandle getUser() {
        return this.user;
    }
}
