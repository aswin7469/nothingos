package com.android.systemui.controls.p010ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\"\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0016R\u0019\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0017"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ItemAdapter;", "Landroid/widget/ArrayAdapter;", "Lcom/android/systemui/controls/ui/SelectionItem;", "parentContext", "Landroid/content/Context;", "resource", "", "(Landroid/content/Context;I)V", "layoutInflater", "Landroid/view/LayoutInflater;", "kotlin.jvm.PlatformType", "getLayoutInflater", "()Landroid/view/LayoutInflater;", "getParentContext", "()Landroid/content/Context;", "getResource", "()I", "getView", "Landroid/view/View;", "position", "convertView", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ItemAdapter */
/* compiled from: ControlsUiControllerImpl.kt */
final class ItemAdapter extends ArrayAdapter<SelectionItem> {
    private final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private final Context parentContext;
    private final int resource;

    public final Context getParentContext() {
        return this.parentContext;
    }

    public final int getResource() {
        return this.resource;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ItemAdapter(Context context, int i) {
        super(context, i);
        Intrinsics.checkNotNullParameter(context, "parentContext");
        this.parentContext = context;
        this.resource = i;
    }

    public final LayoutInflater getLayoutInflater() {
        return this.layoutInflater;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        SelectionItem selectionItem = (SelectionItem) getItem(i);
        if (view == null) {
            view = this.layoutInflater.inflate(this.resource, viewGroup, false);
        }
        ((TextView) view.requireViewById(C1893R.C1897id.controls_spinner_item)).setText(selectionItem.getTitle());
        ((ImageView) view.requireViewById(C1893R.C1897id.app_icon)).setImageDrawable(selectionItem.getIcon());
        Intrinsics.checkNotNullExpressionValue(view, "view");
        return view;
    }
}
