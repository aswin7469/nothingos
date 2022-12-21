package com.android.systemui.controls.management;

import android.content.Context;
import com.android.settingslib.applications.ServiceListing;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¨\u0006\u0004"}, mo64987d2 = {"createServiceListing", "Lcom/android/settingslib/applications/ServiceListing;", "context", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsListingControllerImpl.kt */
public final class ControlsListingControllerImplKt {
    /* access modifiers changed from: private */
    public static final ServiceListing createServiceListing(Context context) {
        ServiceListing.Builder builder = new ServiceListing.Builder(context);
        builder.setIntentAction("android.service.controls.ControlsProviderService");
        builder.setPermission("android.permission.BIND_CONTROLS");
        builder.setNoun("Controls Provider");
        builder.setSetting("controls_providers");
        builder.setTag("controls_providers");
        builder.setAddDeviceLockedFlags(true);
        ServiceListing build = builder.build();
        Intrinsics.checkNotNullExpressionValue(build, "Builder(context).apply {…Flags(true)\n    }.build()");
        return build;
    }
}
