package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 H\u0016J\b\u0010\"\u001a\u00020\u001cH\u0016J\n\u0010#\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010&\u001a\u00020%H\u0002J\u0010\u0010'\u001a\u00020%2\u0006\u0010(\u001a\u00020\u001eH\u0016J\u0010\u0010)\u001a\u00020%2\u0006\u0010*\u001a\u00020\u001eH\u0016J \u0010+\u001a\u00020%2\u0006\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u001eH\u0016J\b\u00100\u001a\u00020%H\u0014R\u000e\u0010\t\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX.¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000¨\u00061"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/row/wrapper/NotificationConversationTemplateViewWrapper;", "Lcom/android/systemui/statusbar/notification/row/wrapper/NotificationTemplateViewWrapper;", "ctx", "Landroid/content/Context;", "view", "Landroid/view/View;", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "(Landroid/content/Context;Landroid/view/View;Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;)V", "appName", "conversationBadgeBg", "conversationIconContainer", "conversationIconView", "Lcom/android/internal/widget/CachingIconView;", "conversationLayout", "Lcom/android/internal/widget/ConversationLayout;", "conversationTitleView", "expandBtn", "expandBtnContainer", "facePileBottom", "facePileBottomBg", "facePileTop", "imageMessageContainer", "Landroid/view/ViewGroup;", "importanceRing", "messagingLinearLayout", "Lcom/android/internal/widget/MessagingLinearLayout;", "minHeightWithActions", "", "disallowSingleClick", "", "x", "", "y", "getMinLayoutHeight", "getShelfTransformationTarget", "onContentUpdated", "", "resolveViews", "setNotificationFaded", "faded", "setRemoteInputVisible", "visible", "updateExpandability", "expandable", "onClickListener", "Landroid/view/View$OnClickListener;", "requestLayout", "updateTransformedTypes", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationConversationTemplateViewWrapper.kt */
public final class NotificationConversationTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private View appName;
    private View conversationBadgeBg;
    private View conversationIconContainer;
    private CachingIconView conversationIconView;
    private final ConversationLayout conversationLayout;
    private View conversationTitleView;
    private View expandBtn;
    private View expandBtnContainer;
    private View facePileBottom;
    private View facePileBottomBg;
    private View facePileTop;
    private ViewGroup imageMessageContainer;
    private View importanceRing;
    private MessagingLinearLayout messagingLinearLayout;
    private final int minHeightWithActions;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationConversationTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        Intrinsics.checkNotNullParameter(context, "ctx");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, C1893R.dimen.notification_messaging_actions_min_height);
        this.conversationLayout = (ConversationLayout) view;
    }

    private final void resolveViews() {
        MessagingLinearLayout messagingLinearLayout2 = this.conversationLayout.getMessagingLinearLayout();
        Intrinsics.checkNotNullExpressionValue(messagingLinearLayout2, "conversationLayout.messagingLinearLayout");
        this.messagingLinearLayout = messagingLinearLayout2;
        ViewGroup imageMessageContainer2 = this.conversationLayout.getImageMessageContainer();
        Intrinsics.checkNotNullExpressionValue(imageMessageContainer2, "conversationLayout.imageMessageContainer");
        this.imageMessageContainer = imageMessageContainer2;
        ConversationLayout conversationLayout2 = this.conversationLayout;
        View requireViewById = conversationLayout2.requireViewById(16908927);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(com.andr…versation_icon_container)");
        this.conversationIconContainer = requireViewById;
        CachingIconView requireViewById2 = conversationLayout2.requireViewById(16908923);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(com.andr…l.R.id.conversation_icon)");
        this.conversationIconView = requireViewById2;
        View requireViewById3 = conversationLayout2.requireViewById(16908925);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById(com.andr…nversation_icon_badge_bg)");
        this.conversationBadgeBg = requireViewById3;
        View requireViewById4 = conversationLayout2.requireViewById(16908982);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById(com.andr…ernal.R.id.expand_button)");
        this.expandBtn = requireViewById4;
        View requireViewById5 = conversationLayout2.requireViewById(16908984);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById(com.andr….expand_button_container)");
        this.expandBtnContainer = requireViewById5;
        View requireViewById6 = conversationLayout2.requireViewById(16908926);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById(com.andr…ersation_icon_badge_ring)");
        this.importanceRing = requireViewById6;
        View requireViewById7 = conversationLayout2.requireViewById(16908784);
        Intrinsics.checkNotNullExpressionValue(requireViewById7, "requireViewById(com.andr…ernal.R.id.app_name_text)");
        this.appName = requireViewById7;
        View requireViewById8 = conversationLayout2.requireViewById(16908929);
        Intrinsics.checkNotNullExpressionValue(requireViewById8, "requireViewById(com.andr…l.R.id.conversation_text)");
        this.conversationTitleView = requireViewById8;
        this.facePileTop = conversationLayout2.findViewById(16908921);
        this.facePileBottom = conversationLayout2.findViewById(16908919);
        this.facePileBottomBg = conversationLayout2.findViewById(16908920);
    }

    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        View view = this.conversationTitleView;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationTitleView");
            view = null;
        }
        viewTransformationHelper.addTransformedView(1, view);
        View[] viewArr = new View[2];
        MessagingLinearLayout messagingLinearLayout2 = this.messagingLinearLayout;
        if (messagingLinearLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("messagingLinearLayout");
            messagingLinearLayout2 = null;
        }
        viewArr[0] = (View) messagingLinearLayout2;
        View view3 = this.appName;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appName");
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        ViewTransformationHelper viewTransformationHelper2 = this.mTransformationHelper;
        ViewGroup viewGroup = this.imageMessageContainer;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageMessageContainer");
            viewGroup = null;
        }
        NotificationMessagingTemplateViewWrapper.setCustomImageMessageTransform(viewTransformationHelper2, viewGroup);
        View[] viewArr2 = new View[7];
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
            view5 = null;
        }
        viewArr2[2] = view5;
        View view6 = this.importanceRing;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("importanceRing");
        } else {
            view2 = view6;
        }
        viewArr2[3] = view2;
        viewArr2[4] = this.facePileTop;
        viewArr2[5] = this.facePileBottom;
        viewArr2[6] = this.facePileBottomBg;
        addViewsTransformingToSimilar(viewArr2);
    }

    public View getShelfTransformationTarget() {
        if (!this.conversationLayout.isImportantConversation()) {
            return super.getShelfTransformationTarget();
        }
        CachingIconView cachingIconView = this.conversationIconView;
        View view = null;
        if (cachingIconView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
            cachingIconView = null;
        }
        if (cachingIconView.getVisibility() == 8) {
            return super.getShelfTransformationTarget();
        }
        View view2 = this.conversationIconView;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
        } else {
            view = view2;
        }
        return view;
    }

    public void setRemoteInputVisible(boolean z) {
        this.conversationLayout.showHistoricMessages(z);
    }

    public void updateExpandability(boolean z, View.OnClickListener onClickListener, boolean z2) {
        Intrinsics.checkNotNullParameter(onClickListener, "onClickListener");
        this.conversationLayout.updateExpandability(z, onClickListener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disallowSingleClick(float r6, float r7) {
        /*
            r5 = this;
            android.view.View r0 = r5.expandBtnContainer
            r1 = 0
            java.lang.String r2 = "expandBtnContainer"
            if (r0 != 0) goto L_0x000b
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            r0 = r1
        L_0x000b:
            int r0 = r0.getVisibility()
            r3 = 1
            r4 = 0
            if (r0 != 0) goto L_0x0024
            android.view.View r0 = r5.expandBtnContainer
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
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.wrapper.NotificationConversationTemplateViewWrapper.disallowSingleClick(float, float):boolean");
    }

    public int getMinLayoutHeight() {
        if (this.mActionsContainer == null || this.mActionsContainer.getVisibility() == 8) {
            return super.getMinLayoutHeight();
        }
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
