package com.android.systemui.controls;

import android.content.Context;
import android.content.pm.ServiceInfo;
import com.android.settingslib.applications.DefaultAppInfo;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsServiceInfo.kt */
/* loaded from: classes.dex */
public final class ControlsServiceInfo extends DefaultAppInfo {
    @NotNull
    private final ServiceInfo serviceInfo;

    @NotNull
    public final ServiceInfo getServiceInfo() {
        return this.serviceInfo;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsServiceInfo(@NotNull Context context, @NotNull ServiceInfo serviceInfo) {
        super(context, context.getPackageManager(), context.getUserId(), serviceInfo.getComponentName());
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(serviceInfo, "serviceInfo");
        this.serviceInfo = serviceInfo;
    }
}
