package com.android.systemui.controls.dagger;

import android.content.pm.PackageManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsModule.kt */
/* loaded from: classes.dex */
public abstract class ControlsModule {
    @NotNull
    public static final Companion Companion = new Companion(null);

    public static final boolean providesControlsFeatureEnabled(@NotNull PackageManager packageManager) {
        return Companion.providesControlsFeatureEnabled(packageManager);
    }

    /* compiled from: ControlsModule.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean providesControlsFeatureEnabled(@NotNull PackageManager pm) {
            Intrinsics.checkNotNullParameter(pm, "pm");
            return pm.hasSystemFeature("android.software.controls");
        }
    }
}
