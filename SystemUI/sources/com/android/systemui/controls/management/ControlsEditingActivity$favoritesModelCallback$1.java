package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import com.android.systemui.controls.management.FavoritesModel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsEditingActivity$favoritesModelCallback$1", "Lcom/android/systemui/controls/management/FavoritesModel$FavoritesModelCallback;", "onFirstChange", "", "onNoneChanged", "showNoFavorites", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$favoritesModelCallback$1 implements FavoritesModel.FavoritesModelCallback {
    final /* synthetic */ ControlsEditingActivity this$0;

    ControlsEditingActivity$favoritesModelCallback$1(ControlsEditingActivity controlsEditingActivity) {
        this.this$0 = controlsEditingActivity;
    }

    public void onNoneChanged(boolean z) {
        TextView textView = null;
        if (z) {
            TextView access$getSubtitle$p = this.this$0.subtitle;
            if (access$getSubtitle$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("subtitle");
            } else {
                textView = access$getSubtitle$p;
            }
            textView.setText(ControlsEditingActivity.EMPTY_TEXT_ID);
            return;
        }
        TextView access$getSubtitle$p2 = this.this$0.subtitle;
        if (access$getSubtitle$p2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("subtitle");
        } else {
            textView = access$getSubtitle$p2;
        }
        textView.setText(ControlsEditingActivity.SUBTITLE_ID);
    }

    public void onFirstChange() {
        View access$getSaveButton$p = this.this$0.saveButton;
        if (access$getSaveButton$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("saveButton");
            access$getSaveButton$p = null;
        }
        access$getSaveButton$p.setEnabled(true);
    }
}
