package com.android.systemui.controls.management;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.biometrics.AuthDialog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u00060\u0005R\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsEditingActivity$setUpList$1$1", "Landroidx/recyclerview/widget/GridLayoutManager;", "getRowCountForAccessibility", "", "recycler", "Landroidx/recyclerview/widget/RecyclerView$Recycler;", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$setUpList$1$1 extends GridLayoutManager {
    ControlsEditingActivity$setUpList$1$1(int i, Context context) {
        super(context, i);
    }

    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Intrinsics.checkNotNullParameter(recycler, "recycler");
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        int rowCountForAccessibility = super.getRowCountForAccessibility(recycler, state);
        return rowCountForAccessibility > 0 ? rowCountForAccessibility - 1 : rowCountForAccessibility;
    }
}
