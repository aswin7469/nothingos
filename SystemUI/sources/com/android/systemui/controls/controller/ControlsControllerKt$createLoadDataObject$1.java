package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001R\u001a\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0006¨\u0006\u000e"}, mo64987d2 = {"com/android/systemui/controls/controller/ControlsControllerKt$createLoadDataObject$1", "Lcom/android/systemui/controls/controller/ControlsController$LoadData;", "allControls", "", "Lcom/android/systemui/controls/ControlStatus;", "getAllControls", "()Ljava/util/List;", "errorOnLoad", "", "getErrorOnLoad", "()Z", "favoritesIds", "", "getFavoritesIds", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsController.kt */
public final class ControlsControllerKt$createLoadDataObject$1 implements ControlsController.LoadData {
    private final List<ControlStatus> allControls;
    private final boolean errorOnLoad;
    private final List<String> favoritesIds;

    ControlsControllerKt$createLoadDataObject$1(List<ControlStatus> list, List<String> list2, boolean z) {
        this.allControls = list;
        this.favoritesIds = list2;
        this.errorOnLoad = z;
    }

    public List<ControlStatus> getAllControls() {
        return this.allControls;
    }

    public List<String> getFavoritesIds() {
        return this.favoritesIds;
    }

    public boolean getErrorOnLoad() {
        return this.errorOnLoad;
    }
}
