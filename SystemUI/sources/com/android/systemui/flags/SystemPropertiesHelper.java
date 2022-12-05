package com.android.systemui.flags;

import android.os.SystemProperties;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SystemPropertiesHelper.kt */
/* loaded from: classes.dex */
public class SystemPropertiesHelper {
    public final boolean getBoolean(@NotNull String name, boolean z) {
        Intrinsics.checkNotNullParameter(name, "name");
        return SystemProperties.getBoolean(name, z);
    }
}
