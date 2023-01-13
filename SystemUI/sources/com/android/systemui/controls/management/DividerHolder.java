package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0005\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/controls/management/DividerHolder;", "Lcom/android/systemui/controls/management/Holder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "divider", "frame", "bindData", "", "wrapper", "Lcom/android/systemui/controls/management/ElementWrapper;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlAdapter.kt */
final class DividerHolder extends Holder {
    private final View divider;
    private final View frame;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DividerHolder(View view) {
        super(view, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(view, "view");
        View requireViewById = this.itemView.requireViewById(C1894R.C1898id.frame);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.frame)");
        this.frame = requireViewById;
        View requireViewById2 = this.itemView.requireViewById(C1894R.C1898id.divider);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.divider)");
        this.divider = requireViewById2;
    }

    public void bindData(ElementWrapper elementWrapper) {
        Intrinsics.checkNotNullParameter(elementWrapper, "wrapper");
        DividerWrapper dividerWrapper = (DividerWrapper) elementWrapper;
        int i = 0;
        this.frame.setVisibility(dividerWrapper.getShowNone() ? 0 : 8);
        View view = this.divider;
        if (!dividerWrapper.getShowDivider()) {
            i = 8;
        }
        view.setVisibility(i);
    }
}
