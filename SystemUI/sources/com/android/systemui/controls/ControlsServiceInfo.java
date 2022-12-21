package com.android.systemui.controls;

import android.content.Context;
import android.content.pm.ServiceInfo;
import com.android.settingslib.applications.DefaultAppInfo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/controls/ControlsServiceInfo;", "Lcom/android/settingslib/applications/DefaultAppInfo;", "context", "Landroid/content/Context;", "serviceInfo", "Landroid/content/pm/ServiceInfo;", "(Landroid/content/Context;Landroid/content/pm/ServiceInfo;)V", "getServiceInfo", "()Landroid/content/pm/ServiceInfo;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsServiceInfo.kt */
public final class ControlsServiceInfo extends DefaultAppInfo {
    private final ServiceInfo serviceInfo;

    public final ServiceInfo getServiceInfo() {
        return this.serviceInfo;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlsServiceInfo(Context context, ServiceInfo serviceInfo2) {
        super(context, context.getPackageManager(), context.getUserId(), serviceInfo2.getComponentName());
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(serviceInfo2, "serviceInfo");
        this.serviceInfo = serviceInfo2;
    }
}
