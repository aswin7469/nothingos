package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.CallLayout;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J\b\u0010\u0019\u001a\u00020\u0013H\u0016J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\u001c\u001a\u00020\u001bH\u0002J\u0010\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u0015H\u0016J\b\u0010\u001f\u001a\u00020\u001bH\u0014R\u000e\u0010\t\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/row/wrapper/NotificationCallTemplateViewWrapper;", "Lcom/android/systemui/statusbar/notification/row/wrapper/NotificationTemplateViewWrapper;", "ctx", "Landroid/content/Context;", "view", "Landroid/view/View;", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "(Landroid/content/Context;Landroid/view/View;Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;)V", "appName", "callLayout", "Lcom/android/internal/widget/CallLayout;", "conversationBadgeBg", "conversationIconContainer", "conversationIconView", "Lcom/android/internal/widget/CachingIconView;", "conversationTitleView", "expandBtn", "minHeightWithActions", "", "disallowSingleClick", "", "x", "", "y", "getMinLayoutHeight", "onContentUpdated", "", "resolveViews", "setNotificationFaded", "faded", "updateTransformedTypes", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationCallTemplateViewWrapper.kt */
public final class NotificationCallTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private View appName;
    private final CallLayout callLayout;
    private View conversationBadgeBg;
    private View conversationIconContainer;
    private CachingIconView conversationIconView;
    private View conversationTitleView;
    private View expandBtn;
    private final int minHeightWithActions;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationCallTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        Intrinsics.checkNotNullParameter(context, "ctx");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, C1893R.dimen.notification_max_height);
        this.callLayout = (CallLayout) view;
    }

    private final void resolveViews() {
        CallLayout callLayout2 = this.callLayout;
        View requireViewById = callLayout2.requireViewById(16908927);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(com.andr…versation_icon_container)");
        this.conversationIconContainer = requireViewById;
        CachingIconView requireViewById2 = callLayout2.requireViewById(16908923);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(com.andr…l.R.id.conversation_icon)");
        this.conversationIconView = requireViewById2;
        View requireViewById3 = callLayout2.requireViewById(16908925);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById(com.andr…nversation_icon_badge_bg)");
        this.conversationBadgeBg = requireViewById3;
        View requireViewById4 = callLayout2.requireViewById(16908982);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById(com.andr…ernal.R.id.expand_button)");
        this.expandBtn = requireViewById4;
        View requireViewById5 = callLayout2.requireViewById(16908784);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById(com.andr…ernal.R.id.app_name_text)");
        this.appName = requireViewById5;
        View requireViewById6 = callLayout2.requireViewById(16908929);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById(com.andr…l.R.id.conversation_text)");
        this.conversationTitleView = requireViewById6;
    }

    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        View[] viewArr = new View[2];
        View view = this.appName;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appName");
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.conversationTitleView;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationTitleView");
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        View[] viewArr2 = new View[3];
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
            cachingIconView = null;
        }
        viewArr2[0] = (View) cachingIconView;
        View view4 = this.conversationBadgeBg;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationBadgeBg");
            view4 = null;
        }
        viewArr2[1] = view4;
        View view5 = this.expandBtn;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("expandBtn");
        } else {
            view2 = view5;
        }
        viewArr2[2] = view2;
        addViewsTransformingToSimilar(viewArr2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disallowSingleClick(float r6, float r7) {
        /*
            r5 = this;
            android.view.View r0 = r5.expandBtn
            r1 = 0
            java.lang.String r2 = "expandBtn"
            if (r0 != 0) goto L_0x000b
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            r0 = r1
        L_0x000b:
            int r0 = r0.getVisibility()
            r3 = 1
            r4 = 0
            if (r0 != 0) goto L_0x0024
            android.view.View r0 = r5.expandBtn
            if (r0 != 0) goto L_0x001b
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            goto L_0x001c
        L_0x001b:
            r1 = r0
        L_0x001c:
            boolean r0 = r5.isOnView(r1, r6, r7)
            if (r0 == 0) goto L_0x0024
            r0 = r3
            goto L_0x0025
        L_0x0024:
            r0 = r4
        L_0x0025:
            if (r0 != 0) goto L_0x002f
            boolean r5 = super.disallowSingleClick(r6, r7)
            if (r5 == 0) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r3 = r4
        L_0x002f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.wrapper.NotificationCallTemplateViewWrapper.disallowSingleClick(float, float):boolean");
    }

    public int getMinLayoutHeight() {
        return this.minHeightWithActions;
    }

    public void setNotificationFaded(boolean z) {
        View view = this.expandBtn;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("expandBtn");
            view = null;
        }
        NotificationFadeAware.setLayerTypeForFaded(view, z);
        View view3 = this.conversationIconContainer;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconContainer");
        } else {
            view2 = view3;
        }
        NotificationFadeAware.setLayerTypeForFaded(view2, z);
    }
}
