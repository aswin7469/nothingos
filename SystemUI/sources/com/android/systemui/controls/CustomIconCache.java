package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.dagger.SysUISingleton;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u0005J \u0010\u000e\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0006R\u001c\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048\u0002X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/controls/CustomIconCache;", "", "()V", "cache", "", "", "Landroid/graphics/drawable/Icon;", "currentComponent", "Landroid/content/ComponentName;", "clear", "", "retrieve", "component", "controlId", "store", "icon", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CustomIconCache.kt */
public final class CustomIconCache {
    private final Map<String, Icon> cache = new LinkedHashMap();
    private ComponentName currentComponent;

    public final void store(ComponentName componentName, String str, Icon icon) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!Intrinsics.areEqual((Object) componentName, (Object) this.currentComponent)) {
            clear();
            this.currentComponent = componentName;
        }
        synchronized (this.cache) {
            if (icon != null) {
                Icon put = this.cache.put(str, icon);
            } else {
                Icon remove = this.cache.remove(str);
            }
        }
    }

    public final Icon retrieve(ComponentName componentName, String str) {
        Icon icon;
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!Intrinsics.areEqual((Object) componentName, (Object) this.currentComponent)) {
            return null;
        }
        synchronized (this.cache) {
            icon = this.cache.get(str);
        }
        return icon;
    }

    private final void clear() {
        synchronized (this.cache) {
            this.cache.clear();
            Unit unit = Unit.INSTANCE;
        }
    }
}
