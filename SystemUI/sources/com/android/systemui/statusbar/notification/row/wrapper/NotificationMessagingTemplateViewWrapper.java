package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.MessagingLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.TransformState;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;

public class NotificationMessagingTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private ViewGroup mImageMessageContainer;
    private MessagingLayout mMessagingLayout;
    private MessagingLinearLayout mMessagingLinearLayout;
    private final int mMinHeightWithActions;
    private final View mTitle = this.mView.findViewById(16908310);
    private final View mTitleInHeader = this.mView.findViewById(16909075);

    protected NotificationMessagingTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.mMessagingLayout = (MessagingLayout) view;
        this.mMinHeightWithActions = NotificationUtils.getFontScaledHeight(context, C1894R.dimen.notification_messaging_actions_min_height);
    }

    private void resolveViews() {
        this.mMessagingLinearLayout = this.mMessagingLayout.getMessagingLinearLayout();
        this.mImageMessageContainer = this.mMessagingLayout.getImageMessageContainer();
    }

    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        if (this.mMessagingLinearLayout != null) {
            this.mTransformationHelper.addTransformedView(this.mMessagingLinearLayout);
        }
        if (this.mTitle == null && this.mTitleInHeader != null) {
            this.mTransformationHelper.addTransformedView(1, this.mTitleInHeader);
        }
        setCustomImageMessageTransform(this.mTransformationHelper, this.mImageMessageContainer);
    }

    static void setCustomImageMessageTransform(ViewTransformationHelper viewTransformationHelper, ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewTransformationHelper.setCustomTransformation(new ViewTransformationHelper.CustomTransformation() {
                public boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                    if (transformableView instanceof HybridNotificationView) {
                        return false;
                    }
                    transformState.ensureVisible();
                    return true;
                }

                public boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                    return transformTo(transformState, transformableView, f);
                }
            }, viewGroup.getId());
        }
    }

    public void setRemoteInputVisible(boolean z) {
        this.mMessagingLayout.showHistoricMessages(z);
    }

    public int getMinLayoutHeight() {
        if (this.mActionsContainer == null || this.mActionsContainer.getVisibility() == 8) {
            return super.getMinLayoutHeight();
        }
        return this.mMinHeightWithActions;
    }
}
