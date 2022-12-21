package com.android.systemui.dreams.smartspace;

import com.android.systemui.smartspace.SmartspaceTargetFilter;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/dreams/smartspace/DreamsSmartspaceController$filterListener$1", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "onCriteriaChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DreamsSmartspaceController.kt */
public final class DreamsSmartspaceController$filterListener$1 implements SmartspaceTargetFilter.Listener {
    final /* synthetic */ DreamsSmartspaceController this$0;

    DreamsSmartspaceController$filterListener$1(DreamsSmartspaceController dreamsSmartspaceController) {
        this.this$0 = dreamsSmartspaceController;
    }

    public void onCriteriaChanged() {
        this.this$0.reloadSmartspace();
    }
}
