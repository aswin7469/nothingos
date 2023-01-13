package com.android.systemui.controls.p010ui;

import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, mo65043d2 = {"com/android/systemui/controls/ui/ControlsUiControllerImpl$createMenu$1$onClick$1$1", "Landroid/widget/AdapterView$OnItemClickListener;", "onItemClick", "", "parent", "Landroid/widget/AdapterView;", "view", "Landroid/view/View;", "pos", "", "id", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1 */
/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createMenu$1$onClick$1$1 implements AdapterView.OnItemClickListener {
    final /* synthetic */ GlobalActionsPopupMenu $this_apply;
    final /* synthetic */ ControlsUiControllerImpl this$0;

    ControlsUiControllerImpl$createMenu$1$onClick$1$1(ControlsUiControllerImpl controlsUiControllerImpl, GlobalActionsPopupMenu globalActionsPopupMenu) {
        this.this$0 = controlsUiControllerImpl;
        this.$this_apply = globalActionsPopupMenu;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Intrinsics.checkNotNullParameter(adapterView, "parent");
        Intrinsics.checkNotNullParameter(view, "view");
        if (i == 0) {
            ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
            controlsUiControllerImpl.startFavoritingActivity(controlsUiControllerImpl.selectedStructure);
        } else if (i == 1) {
            ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
            controlsUiControllerImpl2.startEditingActivity(controlsUiControllerImpl2.selectedStructure);
        }
        this.$this_apply.dismiss();
    }
}
