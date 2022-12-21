package com.android.systemui.statusbar.notification.collection.render;

import android.content.Intent;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0001\u0018\u00002\u00020\u00012\u00020\u0002B5\b\u0007\u0012\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0001\u0010\u000b\u001a\u00020\u0004¢\u0006\u0002\u0010\fJ\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!H\u0016J\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u0010H\u0016J\u0010\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0012H\u0016R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u000e8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\u00020\u001a8VX\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001c¨\u0006&"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderNodeControllerImpl;", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "nodeLabel", "", "layoutInflater", "Landroid/view/LayoutInflater;", "headerTextResId", "", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "clickIntentAction", "(Ljava/lang/String;Landroid/view/LayoutInflater;ILcom/android/systemui/plugins/ActivityStarter;Ljava/lang/String;)V", "_view", "Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "clearAllButtonEnabled", "", "clearAllClickListener", "Landroid/view/View$OnClickListener;", "headerView", "getHeaderView", "()Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "getNodeLabel", "()Ljava/lang/String;", "onHeaderClickListener", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "onViewAdded", "", "reinflateView", "parent", "Landroid/view/ViewGroup;", "setClearSectionEnabled", "enabled", "setOnClearSectionClickListener", "listener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SectionHeaderController.kt */
public final class SectionHeaderNodeControllerImpl implements NodeController, SectionHeaderController {
    private SectionHeaderView _view;
    private final ActivityStarter activityStarter;
    private boolean clearAllButtonEnabled;
    private View.OnClickListener clearAllClickListener;
    private final String clickIntentAction;
    private final int headerTextResId;
    private final LayoutInflater layoutInflater;
    private final String nodeLabel;
    private final View.OnClickListener onHeaderClickListener = new SectionHeaderNodeControllerImpl$$ExternalSyntheticLambda0(this);

    @Inject
    public SectionHeaderNodeControllerImpl(String str, LayoutInflater layoutInflater2, int i, ActivityStarter activityStarter2, String str2) {
        Intrinsics.checkNotNullParameter(str, "nodeLabel");
        Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(str2, "clickIntentAction");
        this.nodeLabel = str;
        this.layoutInflater = layoutInflater2;
        this.headerTextResId = i;
        this.activityStarter = activityStarter2;
        this.clickIntentAction = str2;
    }

    public String getNodeLabel() {
        return this.nodeLabel;
    }

    /* access modifiers changed from: private */
    /* renamed from: onHeaderClickListener$lambda-0  reason: not valid java name */
    public static final void m3118onHeaderClickListener$lambda0(SectionHeaderNodeControllerImpl sectionHeaderNodeControllerImpl, View view) {
        Intrinsics.checkNotNullParameter(sectionHeaderNodeControllerImpl, "this$0");
        sectionHeaderNodeControllerImpl.activityStarter.startActivity(new Intent(sectionHeaderNodeControllerImpl.clickIntentAction), true, true, (int) NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reinflateView(android.view.ViewGroup r6) {
        /*
            r5 = this;
            java.lang.String r0 = "parent"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = r5._view
            r1 = -1
            if (r0 == 0) goto L_0x001e
            r0.removeFromTransientContainer()
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r6) goto L_0x001e
            android.view.View r0 = (android.view.View) r0
            int r2 = r6.indexOfChild(r0)
            r6.removeView(r0)
            goto L_0x001f
        L_0x001e:
            r2 = r1
        L_0x001f:
            android.view.LayoutInflater r0 = r5.layoutInflater
            r3 = 2131624469(0x7f0e0215, float:1.8876119E38)
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r6, r4)
            if (r0 == 0) goto L_0x0050
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = (com.android.systemui.statusbar.notification.stack.SectionHeaderView) r0
            int r3 = r5.headerTextResId
            r0.setHeaderText(r3)
            android.view.View$OnClickListener r3 = r5.onHeaderClickListener
            r0.setOnHeaderClickListener(r3)
            android.view.View$OnClickListener r3 = r5.clearAllClickListener
            if (r3 == 0) goto L_0x003e
            r0.setOnClearAllClickListener(r3)
        L_0x003e:
            if (r2 == r1) goto L_0x0046
            r1 = r0
            android.view.View r1 = (android.view.View) r1
            r6.addView(r1, r2)
        L_0x0046:
            r5._view = r0
            if (r0 == 0) goto L_0x004f
            boolean r5 = r5.clearAllButtonEnabled
            r0.setClearSectionButtonEnabled(r5)
        L_0x004f:
            return
        L_0x0050:
            java.lang.NullPointerException r5 = new java.lang.NullPointerException
            java.lang.String r6 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl.reinflateView(android.view.ViewGroup):void");
    }

    public SectionHeaderView getHeaderView() {
        return this._view;
    }

    public void setClearSectionEnabled(boolean z) {
        this.clearAllButtonEnabled = z;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setClearSectionButtonEnabled(z);
        }
    }

    public void setOnClearSectionClickListener(View.OnClickListener onClickListener) {
        Intrinsics.checkNotNullParameter(onClickListener, "listener");
        this.clearAllClickListener = onClickListener;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setOnClearAllClickListener(onClickListener);
        }
    }

    public void onViewAdded() {
        SectionHeaderView headerView = getHeaderView();
        if (headerView != null) {
            headerView.setContentVisible(true);
        }
    }

    public View getView() {
        SectionHeaderView sectionHeaderView = this._view;
        Intrinsics.checkNotNull(sectionHeaderView);
        return sectionHeaderView;
    }
}
