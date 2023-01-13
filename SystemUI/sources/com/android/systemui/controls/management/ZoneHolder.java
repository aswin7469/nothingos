package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ZoneHolder;", "Lcom/android/systemui/controls/management/Holder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "zone", "Landroid/widget/TextView;", "bindData", "", "wrapper", "Lcom/android/systemui/controls/management/ElementWrapper;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlAdapter.kt */
final class ZoneHolder extends Holder {
    private final TextView zone = ((TextView) this.itemView);

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ZoneHolder(View view) {
        super(view, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(view, "view");
    }

    public void bindData(ElementWrapper elementWrapper) {
        Intrinsics.checkNotNullParameter(elementWrapper, "wrapper");
        this.zone.setText(((ZoneNameWrapper) elementWrapper).getZoneName());
    }
}
