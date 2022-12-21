package com.android.systemui.controls.management;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001B\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0016\u0001\u0003\f\r\u000e¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/controls/management/Holder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "bindData", "", "wrapper", "Lcom/android/systemui/controls/management/ElementWrapper;", "updateFavorite", "favorite", "", "Lcom/android/systemui/controls/management/DividerHolder;", "Lcom/android/systemui/controls/management/ZoneHolder;", "Lcom/android/systemui/controls/management/ControlHolder;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlAdapter.kt */
public abstract class Holder extends RecyclerView.ViewHolder {
    public /* synthetic */ Holder(View view, DefaultConstructorMarker defaultConstructorMarker) {
        this(view);
    }

    public abstract void bindData(ElementWrapper elementWrapper);

    public void updateFavorite(boolean z) {
    }

    private Holder(View view) {
        super(view);
    }
}
