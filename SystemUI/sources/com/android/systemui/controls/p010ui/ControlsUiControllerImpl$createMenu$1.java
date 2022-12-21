package com.android.systemui.controls.p010ui;

import android.icu.text.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/controls/ui/ControlsUiControllerImpl$createMenu$1", "Landroid/view/View$OnClickListener;", "onClick", "", "v", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1 */
/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createMenu$1 implements View.OnClickListener {
    final /* synthetic */ Ref.ObjectRef<ArrayAdapter<String>> $adapter;
    final /* synthetic */ ImageView $anchor;
    final /* synthetic */ ControlsUiControllerImpl this$0;

    ControlsUiControllerImpl$createMenu$1(ControlsUiControllerImpl controlsUiControllerImpl, ImageView imageView, Ref.ObjectRef<ArrayAdapter<String>> objectRef) {
        this.this$0 = controlsUiControllerImpl;
        this.$anchor = imageView;
        this.$adapter = objectRef;
    }

    public void onClick(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
        GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(this.this$0.popupThemedContext, false);
        ImageView imageView = this.$anchor;
        Ref.ObjectRef<ArrayAdapter<String>> objectRef = this.$adapter;
        ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
        globalActionsPopupMenu.setAnchorView(imageView);
        globalActionsPopupMenu.setAdapter((ListAdapter) objectRef.element);
        globalActionsPopupMenu.setOnItemClickListener(new ControlsUiControllerImpl$createMenu$1$onClick$1$1(controlsUiControllerImpl2, globalActionsPopupMenu));
        globalActionsPopupMenu.show();
        controlsUiControllerImpl.popup = globalActionsPopupMenu;
    }
}
