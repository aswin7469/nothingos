package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsFavoritingActivity$listingCallback$1", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    ControlsFavoritingActivity$listingCallback$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        if (list.size() > 1) {
            View access$getOtherAppsButton$p = this.this$0.otherAppsButton;
            if (access$getOtherAppsButton$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
                access$getOtherAppsButton$p = null;
            }
            access$getOtherAppsButton$p.post(new C2034xce6261df(this.this$0));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onServicesUpdated$lambda-0  reason: not valid java name */
    public static final void m2651onServicesUpdated$lambda0(ControlsFavoritingActivity controlsFavoritingActivity) {
        Intrinsics.checkNotNullParameter(controlsFavoritingActivity, "this$0");
        View access$getOtherAppsButton$p = controlsFavoritingActivity.otherAppsButton;
        if (access$getOtherAppsButton$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
            access$getOtherAppsButton$p = null;
        }
        access$getOtherAppsButton$p.setVisibility(0);
    }
}
