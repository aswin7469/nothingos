package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.util.DeviceConfigProxy;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ForegroundServiceDismissalFeatureController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDismissalFeatureController {
    @NotNull
    private final Context context;
    @NotNull
    private final DeviceConfigProxy proxy;

    public ForegroundServiceDismissalFeatureController(@NotNull DeviceConfigProxy proxy, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(proxy, "proxy");
        Intrinsics.checkNotNullParameter(context, "context");
        this.proxy = proxy;
        this.context = context;
    }

    public final boolean isForegroundServiceDismissalEnabled() {
        boolean isEnabled;
        isEnabled = ForegroundServiceDismissalFeatureControllerKt.isEnabled(this.proxy);
        return isEnabled;
    }
}
