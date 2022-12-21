package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.widget.NotificationActionListLayout;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.TransformState;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;

public class NotificationTemplateViewWrapper extends NotificationHeaderViewWrapper {
    private NotificationActionListLayout mActions;
    protected View mActionsContainer;
    private final boolean mAllowHideHeader;
    private boolean mCanHideHeader;
    private ArraySet<PendingIntent> mCancelledPendingIntents = new ArraySet<>();
    private int mContentHeight;
    private final int mFullHeaderTranslation;
    private float mHeaderTranslation;
    protected ImageView mLeftIcon;
    private int mMinHeightHint;
    private ProgressBar mProgressBar;
    private View mRemoteInputHistory;
    protected ImageView mRightIcon;
    protected View mSmartReplyContainer;
    private TextView mText;
    private TextView mTitle;
    /* access modifiers changed from: private */
    public UiOffloadThread mUiOffloadThread;

    protected NotificationTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.mAllowHideHeader = context.getResources().getBoolean(C1893R.bool.heads_up_notification_hides_header);
        this.mTransformationHelper.setCustomTransformation(new ViewTransformationHelper.CustomTransformation() {
            public boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                if (!(transformableView instanceof HybridNotificationView)) {
                    return false;
                }
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeOut(transformState.getTransformedView(), f);
                if (currentState != null) {
                    transformState.transformViewVerticalTo(currentState, this, f);
                    currentState.recycle();
                }
                return true;
            }

            public boolean customTransformTarget(TransformState transformState, TransformState transformState2) {
                transformState.setTransformationEndY(getTransformationY(transformState, transformState2));
                return true;
            }

            public boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                if (!(transformableView instanceof HybridNotificationView)) {
                    return false;
                }
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeIn(transformState.getTransformedView(), f, true);
                if (currentState != null) {
                    transformState.transformViewVerticalFrom(currentState, this, f);
                    currentState.recycle();
                }
                return true;
            }

            public boolean initTransformation(TransformState transformState, TransformState transformState2) {
                transformState.setTransformationStartY(getTransformationY(transformState, transformState2));
                return true;
            }

            private float getTransformationY(TransformState transformState, TransformState transformState2) {
                return ((float) ((transformState2.getLaidOutLocationOnScreen()[1] + transformState2.getTransformedView().getHeight()) - transformState.getLaidOutLocationOnScreen()[1])) * 0.33f;
            }
        }, 2);
        this.mFullHeaderTranslation = context.getResources().getDimensionPixelSize(17105382) - context.getResources().getDimensionPixelSize(17105385);
    }

    private void resolveTemplateViews(StatusBarNotification statusBarNotification) {
        ImageView imageView = (ImageView) this.mView.findViewById(16909431);
        this.mRightIcon = imageView;
        if (imageView != null) {
            imageView.setTag(C1893R.C1897id.image_icon_tag, getRightIcon(statusBarNotification.getNotification()));
            this.mRightIcon.setTag(C1893R.C1897id.align_transform_end_tag, true);
        }
        ImageView imageView2 = (ImageView) this.mView.findViewById(16909175);
        this.mLeftIcon = imageView2;
        if (imageView2 != null) {
            imageView2.setTag(C1893R.C1897id.image_icon_tag, getLargeIcon(statusBarNotification.getNotification()));
        }
        this.mTitle = (TextView) this.mView.findViewById(16908310);
        this.mText = (TextView) this.mView.findViewById(16909579);
        View findViewById = this.mView.findViewById(16908301);
        if (findViewById instanceof ProgressBar) {
            this.mProgressBar = (ProgressBar) findViewById;
        } else {
            this.mProgressBar = null;
        }
        this.mSmartReplyContainer = this.mView.findViewById(16909511);
        this.mActionsContainer = this.mView.findViewById(16908744);
        this.mActions = this.mView.findViewById(16908743);
        this.mRemoteInputHistory = this.mView.findViewById(16909287);
        updatePendingIntentCancellations();
    }

    /* access modifiers changed from: protected */
    public final Icon getLargeIcon(Notification notification) {
        Icon largeIcon = notification.getLargeIcon();
        return (largeIcon != null || notification.largeIcon == null) ? largeIcon : Icon.createWithBitmap(notification.largeIcon);
    }

    /* access modifiers changed from: protected */
    public final Icon getRightIcon(Notification notification) {
        Icon pictureIcon;
        if (!notification.extras.getBoolean(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED) || !notification.isStyle(Notification.BigPictureStyle.class) || (pictureIcon = Notification.BigPictureStyle.getPictureIcon(notification.extras)) == null) {
            return getLargeIcon(notification);
        }
        return pictureIcon;
    }

    private void updatePendingIntentCancellations() {
        NotificationActionListLayout notificationActionListLayout = this.mActions;
        if (notificationActionListLayout != null) {
            int childCount = notificationActionListLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                Button button = (Button) this.mActions.getChildAt(i);
                performOnPendingIntentCancellation(button, new NotificationTemplateViewWrapper$$ExternalSyntheticLambda3(this, button));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updatePendingIntentCancellations$0$com-android-systemui-statusbar-notification-row-wrapper-NotificationTemplateViewWrapper */
    public /* synthetic */ void mo41789xed444a91(Button button) {
        if (button.isEnabled()) {
            button.setEnabled(false);
            ColorStateList textColors = button.getTextColors();
            int[] colors = textColors.getColors();
            int[] iArr = new int[colors.length];
            float f = this.mView.getResources().getFloat(17105367);
            for (int i = 0; i < colors.length; i++) {
                iArr[i] = blendColorWithBackground(colors[i], f);
            }
            button.setTextColor(new ColorStateList(textColors.getStates(), iArr));
        }
    }

    private int blendColorWithBackground(int i, float f) {
        return ContrastColorUtil.compositeColors(Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i)), resolveBackgroundColor());
    }

    private void performOnPendingIntentCancellation(View view, Runnable runnable) {
        final PendingIntent pendingIntent = (PendingIntent) view.getTag(16909336);
        if (pendingIntent != null) {
            if (this.mCancelledPendingIntents.contains(pendingIntent)) {
                runnable.run();
                return;
            }
            final NotificationTemplateViewWrapper$$ExternalSyntheticLambda1 notificationTemplateViewWrapper$$ExternalSyntheticLambda1 = new NotificationTemplateViewWrapper$$ExternalSyntheticLambda1(this, pendingIntent, runnable);
            if (this.mUiOffloadThread == null) {
                this.mUiOffloadThread = (UiOffloadThread) Dependency.get(UiOffloadThread.class);
            }
            if (view.isAttachedToWindow()) {
                this.mUiOffloadThread.execute(new NotificationTemplateViewWrapper$$ExternalSyntheticLambda2(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda1));
            }
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View view) {
                    NotificationTemplateViewWrapper.this.mUiOffloadThread.execute(new NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda1(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda1));
                }

                public void onViewDetachedFromWindow(View view) {
                    NotificationTemplateViewWrapper.this.mUiOffloadThread.execute(new NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda0(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda1));
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$performOnPendingIntentCancellation$2$com-android-systemui-statusbar-notification-row-wrapper-NotificationTemplateViewWrapper */
    public /* synthetic */ void mo41788x8c7348a3(PendingIntent pendingIntent, Runnable runnable, PendingIntent pendingIntent2) {
        this.mView.post(new NotificationTemplateViewWrapper$$ExternalSyntheticLambda0(this, pendingIntent, runnable));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$performOnPendingIntentCancellation$1$com-android-systemui-statusbar-notification-row-wrapper-NotificationTemplateViewWrapper */
    public /* synthetic */ void mo41787xa36b83a2(PendingIntent pendingIntent, Runnable runnable) {
        this.mCancelledPendingIntents.add(pendingIntent);
        runnable.run();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
        r0 = r2.mRightIcon;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onContentUpdated(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3) {
        /*
            r2 = this;
            com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r3.getEntry()
            android.service.notification.StatusBarNotification r0 = r0.getSbn()
            r2.resolveTemplateViews(r0)
            super.onContentUpdated(r3)
            boolean r0 = r2.mAllowHideHeader
            if (r0 == 0) goto L_0x0022
            android.view.NotificationHeaderView r0 = r2.mNotificationHeader
            if (r0 == 0) goto L_0x0022
            android.widget.ImageView r0 = r2.mRightIcon
            if (r0 == 0) goto L_0x0020
            int r0 = r0.getVisibility()
            if (r0 == 0) goto L_0x0022
        L_0x0020:
            r0 = 1
            goto L_0x0023
        L_0x0022:
            r0 = 0
        L_0x0023:
            r2.mCanHideHeader = r0
            float r0 = r3.getHeaderVisibleAmount()
            r1 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L_0x0036
            float r3 = r3.getHeaderVisibleAmount()
            r2.setHeaderVisibleAmount(r3)
        L_0x0036:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper.onContentUpdated(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow):void");
    }

    /* access modifiers changed from: protected */
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        if (this.mTitle != null) {
            this.mTransformationHelper.addTransformedView(1, this.mTitle);
        }
        if (this.mText != null) {
            this.mTransformationHelper.addTransformedView(2, this.mText);
        }
        if (this.mRightIcon != null) {
            this.mTransformationHelper.addTransformedView(3, this.mRightIcon);
        }
        if (this.mProgressBar != null) {
            this.mTransformationHelper.addTransformedView(4, this.mProgressBar);
        }
        addViewsTransformingToSimilar(this.mLeftIcon);
        addTransformedViews(this.mSmartReplyContainer);
    }

    public void setContentHeight(int i, int i2) {
        super.setContentHeight(i, i2);
        this.mContentHeight = i;
        this.mMinHeightHint = i2;
        updateActionOffset();
    }

    public boolean shouldClipToRounding(boolean z, boolean z2) {
        View view;
        if (super.shouldClipToRounding(z, z2)) {
            return true;
        }
        if (!z2 || (view = this.mActionsContainer) == null || view.getVisibility() == 8) {
            return false;
        }
        return true;
    }

    private void updateActionOffset() {
        if (this.mActionsContainer != null) {
            this.mActionsContainer.setTranslationY((float) ((Math.max(this.mContentHeight, this.mMinHeightHint) - this.mView.getHeight()) - getHeaderTranslation(false)));
        }
    }

    public int getHeaderTranslation(boolean z) {
        return (!z || !this.mCanHideHeader) ? (int) this.mHeaderTranslation : this.mFullHeaderTranslation;
    }

    public void setHeaderVisibleAmount(float f) {
        float f2;
        super.setHeaderVisibleAmount(f);
        if (!this.mCanHideHeader || this.mNotificationHeader == null) {
            f2 = 0.0f;
        } else {
            this.mNotificationHeader.setAlpha(f);
            f2 = (1.0f - f) * ((float) this.mFullHeaderTranslation);
        }
        this.mHeaderTranslation = f2;
        this.mView.setTranslationY(this.mHeaderTranslation);
    }

    public int getExtraMeasureHeight() {
        NotificationActionListLayout notificationActionListLayout = this.mActions;
        int extraMeasureHeight = notificationActionListLayout != null ? notificationActionListLayout.getExtraMeasureHeight() : 0;
        View view = this.mRemoteInputHistory;
        if (!(view == null || view.getVisibility() == 8)) {
            extraMeasureHeight += this.mRow.getContext().getResources().getDimensionPixelSize(C1893R.dimen.remote_input_history_extra_height);
        }
        return extraMeasureHeight + super.getExtraMeasureHeight();
    }
}
