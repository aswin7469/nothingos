package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH&J\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH&R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "", "headerView", "Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "getHeaderView", "()Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "reinflateView", "", "parent", "Landroid/view/ViewGroup;", "setClearSectionEnabled", "enabled", "", "setOnClearSectionClickListener", "listener", "Landroid/view/View$OnClickListener;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SectionHeaderController.kt */
public interface SectionHeaderController {
    SectionHeaderView getHeaderView();

    void reinflateView(ViewGroup viewGroup);

    void setClearSectionEnabled(boolean z);

    void setOnClearSectionClickListener(View.OnClickListener onClickListener);
}
