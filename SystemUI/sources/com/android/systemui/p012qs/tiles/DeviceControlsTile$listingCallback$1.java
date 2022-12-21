package com.android.systemui.p012qs.tiles;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/qs/tiles/DeviceControlsTile$listingCallback$1", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.tiles.DeviceControlsTile$listingCallback$1 */
/* compiled from: DeviceControlsTile.kt */
public final class DeviceControlsTile$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ DeviceControlsTile this$0;

    DeviceControlsTile$listingCallback$1(DeviceControlsTile deviceControlsTile) {
        this.this$0 = deviceControlsTile;
    }

    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        if (this.this$0.hasControlsApps.compareAndSet(list.isEmpty(), !list.isEmpty())) {
            this.this$0.refreshState();
        }
    }
}
