package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b¨\u0006\t"}, mo64987d2 = {"createLoadDataObject", "Lcom/android/systemui/controls/controller/ControlsController$LoadData;", "allControls", "", "Lcom/android/systemui/controls/ControlStatus;", "favorites", "", "error", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsController.kt */
public final class ControlsControllerKt {
    public static /* synthetic */ ControlsController.LoadData createLoadDataObject$default(List list, List list2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return createLoadDataObject(list, list2, z);
    }

    public static final ControlsController.LoadData createLoadDataObject(List<ControlStatus> list, List<String> list2, boolean z) {
        Intrinsics.checkNotNullParameter(list, "allControls");
        Intrinsics.checkNotNullParameter(list2, "favorites");
        return new ControlsControllerKt$createLoadDataObject$1(list, list2, z);
    }
}
