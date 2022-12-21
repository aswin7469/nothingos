package com.android.launcher3.icons;

import android.content.pm.LauncherActivityInfo;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class IconProvider$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ LauncherActivityInfo f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ IconProvider$$ExternalSyntheticLambda1(LauncherActivityInfo launcherActivityInfo, int i) {
        this.f$0 = launcherActivityInfo;
        this.f$1 = i;
    }

    public final Object get() {
        return this.f$0.getIcon(this.f$1);
    }
}
