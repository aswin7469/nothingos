package com.android.systemui.flags;

import android.os.SystemProperties;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tJ\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006J\u0016\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\t¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/flags/SystemPropertiesHelper;", "", "()V", "erase", "", "name", "", "get", "getBoolean", "", "default", "set", "value", "", "setBoolean", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SystemPropertiesHelper.kt */
public class SystemPropertiesHelper {
    public final String get(String str) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        String str2 = SystemProperties.get(str);
        Intrinsics.checkNotNullExpressionValue(str2, "get(name)");
        return str2;
    }

    public final boolean getBoolean(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        return SystemProperties.getBoolean(str, z);
    }

    public final void setBoolean(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        SystemProperties.set(str, z ? "1" : "0");
    }

    public final void set(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(str2, "value");
        SystemProperties.set(str, str2);
    }

    public final void set(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        set(str, String.valueOf(i));
    }

    public final void erase(String str) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        set(str, "");
    }
}
