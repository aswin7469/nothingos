package com.android.systemui.statusbar.notification.stack;

import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.NotificationHeaderView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.NotificationExpandButton;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationGroupingUtil;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.HybridGroupManager;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class NotificationChildrenContainer extends ViewGroup {
    private static final AnimationProperties ALPHA_FADE_IN = new AnimationProperties() { // from class: com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer.1
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    @VisibleForTesting
    static final int NUMBER_OF_CHILDREN_WHEN_COLLAPSED = 2;
    @VisibleForTesting
    static final int NUMBER_OF_CHILDREN_WHEN_SYSTEM_EXPANDED = 5;
    private int mActualHeight;
    private final List<ExpandableNotificationRow> mAttachedChildren;
    private int mChildPadding;
    private boolean mChildrenExpanded;
    private int mClipBottomAmount;
    private float mCollapsedBottompadding;
    private ExpandableNotificationRow mContainingNotification;
    private ViewGroup mCurrentHeader;
    private int mCurrentHeaderTranslation;
    private float mDividerAlpha;
    private int mDividerHeight;
    private final List<View> mDividers;
    private boolean mEnableShadowOnChildNotifications;
    private ViewState mGroupOverFlowState;
    private NotificationGroupingUtil mGroupingUtil;
    private View.OnClickListener mHeaderClickListener;
    private int mHeaderHeight;
    private ViewState mHeaderViewState;
    private float mHeaderVisibleAmount;
    private boolean mHideDividersDuringExpand;
    private final HybridGroupManager mHybridGroupManager;
    private boolean mIsConversation;
    private boolean mIsLowPriority;
    private boolean mNeverAppliedGroupState;
    private NotificationHeaderView mNotificationHeader;
    private NotificationHeaderView mNotificationHeaderLowPriority;
    private int mNotificationHeaderMargin;
    private NotificationViewWrapper mNotificationHeaderWrapper;
    private NotificationViewWrapper mNotificationHeaderWrapperLowPriority;
    private int mNotificatonTopPadding;
    private TextView mOverflowNumber;
    private int mRealHeight;
    private boolean mShowDividersWhenExpanded;
    private boolean mShowGroupCountInExpander;
    private int mTranslationForHeader;
    private int mUntruncatedChildCount;
    private boolean mUserLocked;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void prepareExpansionChanged() {
    }

    public NotificationChildrenContainer(Context context) {
        this(context, null);
    }

    public NotificationChildrenContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NotificationChildrenContainer(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NotificationChildrenContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDividers = new ArrayList();
        this.mAttachedChildren = new ArrayList();
        this.mCurrentHeaderTranslation = 0;
        this.mHeaderVisibleAmount = 1.0f;
        this.mHybridGroupManager = new HybridGroupManager(getContext());
        initDimens();
        setClipChildren(false);
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mChildPadding = resources.getDimensionPixelSize(R$dimen.notification_children_padding);
        this.mDividerHeight = resources.getDimensionPixelSize(R$dimen.notification_children_container_divider_height);
        this.mDividerAlpha = resources.getFloat(R$dimen.notification_divider_alpha);
        this.mNotificationHeaderMargin = resources.getDimensionPixelSize(R$dimen.notification_children_container_margin_top);
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.notification_children_container_top_padding);
        this.mNotificatonTopPadding = dimensionPixelSize;
        this.mHeaderHeight = this.mNotificationHeaderMargin + dimensionPixelSize;
        this.mCollapsedBottompadding = resources.getDimensionPixelSize(17105377);
        this.mEnableShadowOnChildNotifications = resources.getBoolean(R$bool.config_enableShadowOnChildNotifications);
        this.mShowGroupCountInExpander = resources.getBoolean(R$bool.config_showNotificationGroupCountInExpander);
        this.mShowDividersWhenExpanded = resources.getBoolean(R$bool.config_showDividersWhenGroupNotificationExpanded);
        this.mHideDividersDuringExpand = resources.getBoolean(R$bool.config_hideDividersDuringExpand);
        this.mTranslationForHeader = resources.getDimensionPixelSize(17105377) - this.mNotificationHeaderMargin;
        this.mHybridGroupManager.initDimens();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int min = Math.min(this.mAttachedChildren.size(), 8);
        for (int i5 = 0; i5 < min; i5++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i5);
            expandableNotificationRow.layout(0, 0, expandableNotificationRow.getMeasuredWidth(), expandableNotificationRow.getMeasuredHeight());
            this.mDividers.get(i5).layout(0, 0, getWidth(), this.mDividerHeight);
        }
        if (this.mOverflowNumber != null) {
            boolean z2 = true;
            if (getLayoutDirection() != 1) {
                z2 = false;
            }
            int width = z2 ? 0 : getWidth() - this.mOverflowNumber.getMeasuredWidth();
            TextView textView = this.mOverflowNumber;
            textView.layout(width, 0, this.mOverflowNumber.getMeasuredWidth() + width, textView.getMeasuredHeight());
        }
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            notificationHeaderView.layout(0, 0, notificationHeaderView.getMeasuredWidth(), this.mNotificationHeader.getMeasuredHeight());
        }
        NotificationHeaderView notificationHeaderView2 = this.mNotificationHeaderLowPriority;
        if (notificationHeaderView2 != null) {
            notificationHeaderView2.layout(0, 0, notificationHeaderView2.getMeasuredWidth(), this.mNotificationHeaderLowPriority.getMeasuredHeight());
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        TextView textView;
        int mode = View.MeasureSpec.getMode(i2);
        boolean z = mode == 1073741824;
        boolean z2 = mode == Integer.MIN_VALUE;
        int size = View.MeasureSpec.getSize(i2);
        int makeMeasureSpec = (z || z2) ? View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE) : i2;
        int size2 = View.MeasureSpec.getSize(i);
        TextView textView2 = this.mOverflowNumber;
        if (textView2 != null) {
            textView2.measure(View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE), makeMeasureSpec);
        }
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mDividerHeight, 1073741824);
        int i3 = this.mNotificationHeaderMargin + this.mNotificatonTopPadding;
        int min = Math.min(this.mAttachedChildren.size(), 8);
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int i4 = min > maxAllowedVisibleChildren ? maxAllowedVisibleChildren - 1 : -1;
        int i5 = 0;
        while (i5 < min) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i5);
            expandableNotificationRow.setSingleLineWidthIndention((!(i5 == i4) || (textView = this.mOverflowNumber) == null) ? 0 : textView.getMeasuredWidth());
            expandableNotificationRow.measure(i, makeMeasureSpec);
            this.mDividers.get(i5).measure(i, makeMeasureSpec2);
            if (expandableNotificationRow.getVisibility() != 8) {
                i3 += expandableNotificationRow.getMeasuredHeight() + this.mDividerHeight;
            }
            i5++;
        }
        this.mRealHeight = i3;
        if (mode != 0) {
            i3 = Math.min(i3, size);
        }
        int makeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(this.mHeaderHeight, 1073741824);
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            notificationHeaderView.measure(i, makeMeasureSpec3);
        }
        NotificationHeaderView notificationHeaderView2 = this.mNotificationHeaderLowPriority;
        if (notificationHeaderView2 != null) {
            notificationHeaderView2.measure(i, makeMeasureSpec3);
        }
        setMeasuredDimension(size2, i3);
    }

    public boolean pointInView(float f, float f2, float f3) {
        float f4 = -f3;
        return f >= f4 && f2 >= f4 && f < ((float) (((ViewGroup) this).mRight - ((ViewGroup) this).mLeft)) + f3 && f2 < ((float) this.mRealHeight) + f3;
    }

    public void setUntruncatedChildCount(int i) {
        this.mUntruncatedChildCount = i;
        updateGroupOverflow();
    }

    public void addNotification(ExpandableNotificationRow expandableNotificationRow, int i) {
        if (i < 0) {
            i = this.mAttachedChildren.size();
        }
        this.mAttachedChildren.add(i, expandableNotificationRow);
        addView(expandableNotificationRow);
        expandableNotificationRow.setUserLocked(this.mUserLocked);
        View inflateDivider = inflateDivider();
        addView(inflateDivider);
        this.mDividers.add(i, inflateDivider);
        expandableNotificationRow.setContentTransformationAmount(0.0f, false);
        ExpandableViewState viewState = expandableNotificationRow.getViewState();
        if (viewState != null) {
            viewState.cancelAnimations(expandableNotificationRow);
            expandableNotificationRow.cancelAppearDrawing();
        }
    }

    public void removeNotification(ExpandableNotificationRow expandableNotificationRow) {
        int indexOf = this.mAttachedChildren.indexOf(expandableNotificationRow);
        this.mAttachedChildren.remove(expandableNotificationRow);
        removeView(expandableNotificationRow);
        final View remove = this.mDividers.remove(indexOf);
        removeView(remove);
        getOverlay().add(remove);
        CrossFadeHelper.fadeOut(remove, new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer.2
            @Override // java.lang.Runnable
            public void run() {
                NotificationChildrenContainer.this.getOverlay().remove(remove);
            }
        });
        expandableNotificationRow.setSystemChildExpanded(false);
        expandableNotificationRow.setUserLocked(false);
        if (!expandableNotificationRow.isRemoved()) {
            this.mGroupingUtil.restoreChildNotification(expandableNotificationRow);
        }
    }

    public int getNotificationChildCount() {
        return this.mAttachedChildren.size();
    }

    public void recreateNotificationHeader(View.OnClickListener onClickListener, boolean z) {
        this.mHeaderClickListener = onClickListener;
        this.mIsConversation = z;
        Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(getContext(), this.mContainingNotification.getEntry().getSbn().getNotification());
        RemoteViews makeNotificationGroupHeader = recoverBuilder.makeNotificationGroupHeader();
        if (this.mNotificationHeader == null) {
            NotificationHeaderView apply = makeNotificationGroupHeader.apply(getContext(), this);
            this.mNotificationHeader = apply;
            apply.findViewById(16908955).setVisibility(0);
            this.mNotificationHeader.setOnClickListener(this.mHeaderClickListener);
            this.mNotificationHeaderWrapper = NotificationViewWrapper.wrap(getContext(), this.mNotificationHeader, this.mContainingNotification);
            addView((View) this.mNotificationHeader, 0);
            invalidate();
        } else {
            makeNotificationGroupHeader.reapply(getContext(), this.mNotificationHeader);
        }
        this.mNotificationHeaderWrapper.setExpanded(this.mChildrenExpanded);
        this.mNotificationHeaderWrapper.onContentUpdated(this.mContainingNotification);
        recreateLowPriorityHeader(recoverBuilder, z);
        updateHeaderVisibility(false);
        updateChildrenAppearance();
    }

    private void recreateLowPriorityHeader(Notification.Builder builder, boolean z) {
        StatusBarNotification sbn = this.mContainingNotification.getEntry().getSbn();
        if (this.mIsLowPriority) {
            if (builder == null) {
                builder = Notification.Builder.recoverBuilder(getContext(), sbn.getNotification());
            }
            RemoteViews makeLowPriorityContentView = builder.makeLowPriorityContentView(true);
            if (this.mNotificationHeaderLowPriority == null) {
                NotificationHeaderView apply = makeLowPriorityContentView.apply(getContext(), this);
                this.mNotificationHeaderLowPriority = apply;
                apply.findViewById(16908955).setVisibility(0);
                this.mNotificationHeaderLowPriority.setOnClickListener(this.mHeaderClickListener);
                this.mNotificationHeaderWrapperLowPriority = NotificationViewWrapper.wrap(getContext(), this.mNotificationHeaderLowPriority, this.mContainingNotification);
                addView((View) this.mNotificationHeaderLowPriority, 0);
                invalidate();
            } else {
                makeLowPriorityContentView.reapply(getContext(), this.mNotificationHeaderLowPriority);
            }
            this.mNotificationHeaderWrapperLowPriority.onContentUpdated(this.mContainingNotification);
            resetHeaderVisibilityIfNeeded(this.mNotificationHeaderLowPriority, calculateDesiredHeader());
            return;
        }
        removeView(this.mNotificationHeaderLowPriority);
        this.mNotificationHeaderLowPriority = null;
        this.mNotificationHeaderWrapperLowPriority = null;
    }

    public void updateChildrenAppearance() {
        this.mGroupingUtil.updateChildrenAppearance();
    }

    private void setExpandButtonNumber(NotificationViewWrapper notificationViewWrapper) {
        View expandButton = notificationViewWrapper == null ? null : notificationViewWrapper.getExpandButton();
        if (expandButton instanceof NotificationExpandButton) {
            ((NotificationExpandButton) expandButton).setNumber(this.mUntruncatedChildCount);
        }
    }

    public void updateGroupOverflow() {
        if (this.mShowGroupCountInExpander) {
            setExpandButtonNumber(this.mNotificationHeaderWrapper);
            setExpandButtonNumber(this.mNotificationHeaderWrapperLowPriority);
            return;
        }
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int i = this.mUntruncatedChildCount;
        if (i > maxAllowedVisibleChildren) {
            this.mOverflowNumber = this.mHybridGroupManager.bindOverflowNumber(this.mOverflowNumber, i - maxAllowedVisibleChildren, this);
            if (this.mGroupOverFlowState != null) {
                return;
            }
            this.mGroupOverFlowState = new ViewState();
            this.mNeverAppliedGroupState = true;
            return;
        }
        TextView textView = this.mOverflowNumber;
        if (textView == null) {
            return;
        }
        removeView(textView);
        if (isShown() && isAttachedToWindow()) {
            final TextView textView2 = this.mOverflowNumber;
            addTransientView(textView2, getTransientViewCount());
            CrossFadeHelper.fadeOut(textView2, new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer.3
                @Override // java.lang.Runnable
                public void run() {
                    NotificationChildrenContainer.this.removeTransientView(textView2);
                }
            });
        }
        this.mOverflowNumber = null;
        this.mGroupOverFlowState = null;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateGroupOverflow();
    }

    private View inflateDivider() {
        return LayoutInflater.from(((ViewGroup) this).mContext).inflate(R$layout.notification_children_divider, (ViewGroup) this, false);
    }

    public List<ExpandableNotificationRow> getAttachedChildren() {
        return this.mAttachedChildren;
    }

    public boolean applyChildOrder(List<ExpandableNotificationRow> list, VisualStabilityManager visualStabilityManager, VisualStabilityManager.Callback callback) {
        if (list == null) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < this.mAttachedChildren.size() && i < list.size(); i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            ExpandableNotificationRow expandableNotificationRow2 = list.get(i);
            if (expandableNotificationRow != expandableNotificationRow2) {
                if (visualStabilityManager.canReorderNotification(expandableNotificationRow2)) {
                    this.mAttachedChildren.remove(expandableNotificationRow2);
                    this.mAttachedChildren.add(i, expandableNotificationRow2);
                    z = true;
                } else {
                    visualStabilityManager.addReorderingAllowedCallback(callback, false);
                }
            }
        }
        updateExpansionStates();
        return z;
    }

    private void updateExpansionStates() {
        if (this.mChildrenExpanded || this.mUserLocked) {
            return;
        }
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            boolean z = true;
            if (i != 0 || size != 1) {
                z = false;
            }
            expandableNotificationRow.setSystemChildExpanded(z);
        }
    }

    public int getIntrinsicHeight() {
        return getIntrinsicHeight(getMaxAllowedVisibleChildren());
    }

    private int getIntrinsicHeight(float f) {
        float f2;
        float f3;
        int i;
        if (showingAsLowPriority()) {
            return this.mNotificationHeaderLowPriority.getHeight();
        }
        int i2 = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation;
        int size = this.mAttachedChildren.size();
        float groupExpandFraction = this.mUserLocked ? getGroupExpandFraction() : 0.0f;
        boolean z = this.mChildrenExpanded;
        boolean z2 = true;
        int i3 = 0;
        for (int i4 = 0; i4 < size && i3 < f; i4++) {
            if (!z2) {
                if (this.mUserLocked) {
                    i = (int) (i2 + NotificationUtils.interpolate(this.mChildPadding, this.mDividerHeight, groupExpandFraction));
                } else {
                    i = i2 + (z ? this.mDividerHeight : this.mChildPadding);
                }
            } else {
                if (this.mUserLocked) {
                    i = (int) (i2 + NotificationUtils.interpolate(0.0f, this.mNotificatonTopPadding + this.mDividerHeight, groupExpandFraction));
                } else {
                    i = i2 + (z ? this.mNotificatonTopPadding + this.mDividerHeight : 0);
                }
                z2 = false;
            }
            i2 = i + this.mAttachedChildren.get(i4).getIntrinsicHeight();
            i3++;
        }
        if (this.mUserLocked) {
            f2 = i2;
            f3 = NotificationUtils.interpolate(this.mCollapsedBottompadding, 0.0f, groupExpandFraction);
        } else if (z) {
            return i2;
        } else {
            f2 = i2;
            f3 = this.mCollapsedBottompadding;
        }
        return (int) (f2 + f3);
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:81:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateState(ExpandableViewState expandableViewState, AmbientState ambientState) {
        float f;
        float f2;
        int i;
        int i2;
        int size = this.mAttachedChildren.size();
        int i3 = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation;
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren() - 1;
        int i4 = maxAllowedVisibleChildren + 1;
        boolean z = false;
        Object[] objArr = (!this.mUserLocked || showingAsLowPriority()) ? null : 1;
        float f3 = 0.0f;
        if (this.mUserLocked) {
            f = getGroupExpandFraction();
            i4 = getMaxAllowedVisibleChildren(true);
        } else {
            f = 0.0f;
        }
        Object[] objArr2 = (!this.mChildrenExpanded || this.mContainingNotification.isGroupExpansionChanging()) ? null : 1;
        boolean z2 = true;
        int i5 = 0;
        while (i5 < size) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i5);
            if (z2) {
                if (objArr != null) {
                    i2 = (int) (i3 + NotificationUtils.interpolate(f3, this.mNotificatonTopPadding + this.mDividerHeight, f));
                } else {
                    if (this.mChildrenExpanded) {
                        i = this.mNotificatonTopPadding + this.mDividerHeight;
                    } else {
                        int i6 = z ? 1 : 0;
                        Object[] objArr3 = z ? 1 : 0;
                        i = i6;
                    }
                    i2 = i3 + i;
                }
                z2 = z;
            } else if (objArr != null) {
                i2 = (int) (i3 + NotificationUtils.interpolate(this.mChildPadding, this.mDividerHeight, f));
            } else {
                i2 = i3 + (this.mChildrenExpanded ? this.mDividerHeight : this.mChildPadding);
            }
            ExpandableViewState viewState = expandableNotificationRow.getViewState();
            int intrinsicHeight = expandableNotificationRow.getIntrinsicHeight();
            viewState.height = intrinsicHeight;
            float f4 = i2 + 0;
            viewState.yTranslation = f4;
            viewState.hidden = z;
            viewState.zTranslation = (objArr2 == null || !this.mEnableShadowOnChildNotifications) ? 0.0f : expandableViewState.zTranslation;
            viewState.dimmed = expandableViewState.dimmed;
            viewState.hideSensitive = expandableViewState.hideSensitive;
            viewState.belowSpeedBump = expandableViewState.belowSpeedBump;
            int i7 = z ? 1 : 0;
            int i8 = z ? 1 : 0;
            viewState.clipTopAmount = i7;
            viewState.alpha = 0.0f;
            float f5 = 1.0f;
            if (i5 < i4) {
                if (showingAsLowPriority()) {
                    f5 = f;
                }
                viewState.alpha = f5;
            } else if (f == 1.0f && i5 <= maxAllowedVisibleChildren) {
                float f6 = (this.mActualHeight - f4) / intrinsicHeight;
                viewState.alpha = f6;
                viewState.alpha = Math.max(0.0f, Math.min(1.0f, f6));
            }
            viewState.location = expandableViewState.location;
            viewState.inShelf = expandableViewState.inShelf;
            i3 = i2 + intrinsicHeight;
            i5++;
            z = false;
            f3 = 0.0f;
        }
        if (this.mOverflowNumber != null) {
            ExpandableNotificationRow expandableNotificationRow2 = this.mAttachedChildren.get(Math.min(getMaxAllowedVisibleChildren(true), size) - 1);
            this.mGroupOverFlowState.copyFrom(expandableNotificationRow2.getViewState());
            if (!this.mChildrenExpanded) {
                HybridNotificationView singleLineView = expandableNotificationRow2.getSingleLineView();
                if (singleLineView != null) {
                    TextView textView = singleLineView.getTextView();
                    if (textView.getVisibility() == 8) {
                        textView = singleLineView.getTitleView();
                    }
                    if (textView.getVisibility() != 8) {
                        singleLineView = textView;
                    }
                    this.mGroupOverFlowState.alpha = singleLineView.getAlpha();
                    this.mGroupOverFlowState.yTranslation += NotificationUtils.getRelativeYOffset(singleLineView, expandableNotificationRow2);
                }
            } else {
                ViewState viewState2 = this.mGroupOverFlowState;
                viewState2.yTranslation += this.mNotificationHeaderMargin;
                f2 = 0.0f;
                viewState2.alpha = 0.0f;
                if (this.mNotificationHeader != null) {
                    return;
                }
                if (this.mHeaderViewState == null) {
                    this.mHeaderViewState = new ViewState();
                }
                this.mHeaderViewState.initFrom(this.mNotificationHeader);
                ViewState viewState3 = this.mHeaderViewState;
                viewState3.zTranslation = objArr2 != null ? expandableViewState.zTranslation : f2;
                viewState3.yTranslation = this.mCurrentHeaderTranslation;
                viewState3.alpha = this.mHeaderVisibleAmount;
                viewState3.hidden = false;
                return;
            }
        }
        f2 = 0.0f;
        if (this.mNotificationHeader != null) {
        }
    }

    @VisibleForTesting
    int getMaxAllowedVisibleChildren() {
        return getMaxAllowedVisibleChildren(false);
    }

    @VisibleForTesting
    int getMaxAllowedVisibleChildren(boolean z) {
        if (z || ((!this.mChildrenExpanded && !this.mContainingNotification.isUserLocked()) || showingAsLowPriority())) {
            if (this.mIsLowPriority) {
                return 5;
            }
            if (!this.mContainingNotification.isOnKeyguard() && this.mContainingNotification.isExpanded()) {
                return 5;
            }
            return (!this.mContainingNotification.isHeadsUpState() || !this.mContainingNotification.canShowHeadsUp()) ? 2 : 5;
        }
        return 8;
    }

    public void applyState() {
        int size = this.mAttachedChildren.size();
        ViewState viewState = new ViewState();
        float groupExpandFraction = this.mUserLocked ? getGroupExpandFraction() : 0.0f;
        boolean z = (this.mChildrenExpanded && this.mShowDividersWhenExpanded) || ((!showingAsLowPriority() && (this.mUserLocked || this.mContainingNotification.isGroupExpansionChanging())) && !this.mHideDividersDuringExpand);
        for (int i = 0; i < size; i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            ExpandableViewState viewState2 = expandableNotificationRow.getViewState();
            viewState2.applyToView(expandableNotificationRow);
            View view = this.mDividers.get(i);
            viewState.initFrom(view);
            viewState.yTranslation = viewState2.yTranslation - this.mDividerHeight;
            float f = (!this.mChildrenExpanded || viewState2.alpha == 0.0f) ? 0.0f : this.mDividerAlpha;
            if (this.mUserLocked && !showingAsLowPriority()) {
                float f2 = viewState2.alpha;
                if (f2 != 0.0f) {
                    f = NotificationUtils.interpolate(0.0f, this.mDividerAlpha, Math.min(f2, groupExpandFraction));
                }
            }
            viewState.hidden = !z;
            viewState.alpha = f;
            viewState.applyToView(view);
            expandableNotificationRow.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
        }
        ViewState viewState3 = this.mGroupOverFlowState;
        if (viewState3 != null) {
            viewState3.applyToView(this.mOverflowNumber);
            this.mNeverAppliedGroupState = false;
        }
        ViewState viewState4 = this.mHeaderViewState;
        if (viewState4 != null) {
            viewState4.applyToView(this.mNotificationHeader);
        }
        updateChildrenClipping();
    }

    private void updateChildrenClipping() {
        int i;
        boolean z;
        if (this.mContainingNotification.hasExpandingChild()) {
            return;
        }
        int size = this.mAttachedChildren.size();
        int actualHeight = this.mContainingNotification.getActualHeight() - this.mClipBottomAmount;
        for (int i2 = 0; i2 < size; i2++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i2);
            if (expandableNotificationRow.getVisibility() != 8) {
                float translationY = expandableNotificationRow.getTranslationY();
                float actualHeight2 = expandableNotificationRow.getActualHeight() + translationY;
                float f = actualHeight;
                boolean z2 = true;
                if (translationY > f) {
                    i = 0;
                    z = false;
                } else {
                    i = actualHeight2 > f ? (int) (actualHeight2 - f) : 0;
                    z = true;
                }
                if (expandableNotificationRow.getVisibility() != 0) {
                    z2 = false;
                }
                if (z != z2) {
                    expandableNotificationRow.setVisibility(z ? 0 : 4);
                }
                expandableNotificationRow.setClipBottomAmount(i);
            }
        }
    }

    public void startAnimationToState(AnimationProperties animationProperties) {
        int size = this.mAttachedChildren.size();
        ViewState viewState = new ViewState();
        float groupExpandFraction = getGroupExpandFraction();
        boolean z = (this.mChildrenExpanded && this.mShowDividersWhenExpanded) || ((!showingAsLowPriority() && (this.mUserLocked || this.mContainingNotification.isGroupExpansionChanging())) && !this.mHideDividersDuringExpand);
        for (int i = size - 1; i >= 0; i--) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            ExpandableViewState viewState2 = expandableNotificationRow.getViewState();
            viewState2.animateTo(expandableNotificationRow, animationProperties);
            View view = this.mDividers.get(i);
            viewState.initFrom(view);
            viewState.yTranslation = viewState2.yTranslation - this.mDividerHeight;
            float f = (!this.mChildrenExpanded || viewState2.alpha == 0.0f) ? 0.0f : this.mDividerAlpha;
            if (this.mUserLocked && !showingAsLowPriority()) {
                float f2 = viewState2.alpha;
                if (f2 != 0.0f) {
                    f = NotificationUtils.interpolate(0.0f, this.mDividerAlpha, Math.min(f2, groupExpandFraction));
                }
            }
            viewState.hidden = !z;
            viewState.alpha = f;
            viewState.animateTo(view, animationProperties);
            expandableNotificationRow.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
        }
        TextView textView = this.mOverflowNumber;
        if (textView != null) {
            if (this.mNeverAppliedGroupState) {
                ViewState viewState3 = this.mGroupOverFlowState;
                float f3 = viewState3.alpha;
                viewState3.alpha = 0.0f;
                viewState3.applyToView(textView);
                this.mGroupOverFlowState.alpha = f3;
                this.mNeverAppliedGroupState = false;
            }
            this.mGroupOverFlowState.animateTo(this.mOverflowNumber, animationProperties);
        }
        View view2 = this.mNotificationHeader;
        if (view2 != null) {
            this.mHeaderViewState.applyToView(view2);
        }
        updateChildrenClipping();
    }

    public ExpandableNotificationRow getViewAtPosition(float f) {
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            float translationY = expandableNotificationRow.getTranslationY();
            float clipTopAmount = expandableNotificationRow.getClipTopAmount() + translationY;
            float actualHeight = translationY + expandableNotificationRow.getActualHeight();
            if (f >= clipTopAmount && f <= actualHeight) {
                return expandableNotificationRow;
            }
        }
        return null;
    }

    public void setChildrenExpanded(boolean z) {
        this.mChildrenExpanded = z;
        updateExpansionStates();
        NotificationViewWrapper notificationViewWrapper = this.mNotificationHeaderWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setExpanded(z);
        }
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size; i++) {
            this.mAttachedChildren.get(i).setChildrenExpanded(z, false);
        }
        updateHeaderTouchability();
    }

    public void setContainingNotification(ExpandableNotificationRow expandableNotificationRow) {
        this.mContainingNotification = expandableNotificationRow;
        this.mGroupingUtil = new NotificationGroupingUtil(this.mContainingNotification);
    }

    public NotificationViewWrapper getNotificationViewWrapper() {
        return this.mNotificationHeaderWrapper;
    }

    public NotificationViewWrapper getLowPriorityViewWrapper() {
        return this.mNotificationHeaderWrapperLowPriority;
    }

    @VisibleForTesting
    public ViewGroup getCurrentHeaderView() {
        return this.mCurrentHeader;
    }

    private void updateHeaderVisibility(boolean z) {
        ViewGroup viewGroup = this.mCurrentHeader;
        ViewGroup calculateDesiredHeader = calculateDesiredHeader();
        if (viewGroup == calculateDesiredHeader) {
            return;
        }
        if (z) {
            if (calculateDesiredHeader == null || viewGroup == null) {
                z = false;
            } else {
                viewGroup.setVisibility(0);
                calculateDesiredHeader.setVisibility(0);
                TransformableView wrapperForView = getWrapperForView(calculateDesiredHeader);
                TransformableView wrapperForView2 = getWrapperForView(viewGroup);
                wrapperForView.transformFrom(wrapperForView2);
                wrapperForView2.transformTo(wrapperForView, new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationChildrenContainer.this.lambda$updateHeaderVisibility$0();
                    }
                });
                startChildAlphaAnimations(calculateDesiredHeader == this.mNotificationHeader);
            }
        }
        if (!z) {
            if (calculateDesiredHeader != null) {
                getWrapperForView(calculateDesiredHeader).setVisible(true);
                calculateDesiredHeader.setVisibility(0);
            }
            if (viewGroup != null) {
                NotificationViewWrapper wrapperForView3 = getWrapperForView(viewGroup);
                if (wrapperForView3 != null) {
                    wrapperForView3.setVisible(false);
                }
                viewGroup.setVisibility(4);
            }
        }
        resetHeaderVisibilityIfNeeded(this.mNotificationHeader, calculateDesiredHeader);
        resetHeaderVisibilityIfNeeded(this.mNotificationHeaderLowPriority, calculateDesiredHeader);
        this.mCurrentHeader = calculateDesiredHeader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateHeaderVisibility$0() {
        updateHeaderVisibility(false);
    }

    private void resetHeaderVisibilityIfNeeded(View view, View view2) {
        if (view == null) {
            return;
        }
        if (view != this.mCurrentHeader && view != view2) {
            getWrapperForView(view).setVisible(false);
            view.setVisibility(4);
        }
        if (view != view2 || view.getVisibility() == 0) {
            return;
        }
        getWrapperForView(view).setVisible(true);
        view.setVisibility(0);
    }

    private ViewGroup calculateDesiredHeader() {
        if (showingAsLowPriority()) {
            return this.mNotificationHeaderLowPriority;
        }
        return this.mNotificationHeader;
    }

    private void startChildAlphaAnimations(boolean z) {
        float f = z ? 1.0f : 0.0f;
        float f2 = 1.0f - f;
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size && i < 5; i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            expandableNotificationRow.setAlpha(f2);
            ViewState viewState = new ViewState();
            viewState.initFrom(expandableNotificationRow);
            viewState.alpha = f;
            AnimationProperties animationProperties = ALPHA_FADE_IN;
            animationProperties.setDelay(i * 50);
            viewState.animateTo(expandableNotificationRow, animationProperties);
        }
    }

    private void updateHeaderTransformation() {
        if (!this.mUserLocked || !showingAsLowPriority()) {
            return;
        }
        float groupExpandFraction = getGroupExpandFraction();
        this.mNotificationHeaderWrapper.transformFrom(this.mNotificationHeaderWrapperLowPriority, groupExpandFraction);
        this.mNotificationHeader.setVisibility(0);
        this.mNotificationHeaderWrapperLowPriority.transformTo(this.mNotificationHeaderWrapper, groupExpandFraction);
    }

    private NotificationViewWrapper getWrapperForView(View view) {
        if (view == this.mNotificationHeader) {
            return this.mNotificationHeaderWrapper;
        }
        return this.mNotificationHeaderWrapperLowPriority;
    }

    public void updateHeaderForExpansion(boolean z) {
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            if (z) {
                ColorDrawable colorDrawable = new ColorDrawable();
                colorDrawable.setColor(this.mContainingNotification.calculateBgColor());
                this.mNotificationHeader.setHeaderBackgroundDrawable(colorDrawable);
                return;
            }
            notificationHeaderView.setHeaderBackgroundDrawable((Drawable) null);
        }
    }

    public int getMaxContentHeight() {
        int minHeight;
        if (showingAsLowPriority()) {
            return getMinHeight(5, true);
        }
        int i = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificatonTopPadding;
        int size = this.mAttachedChildren.size();
        int i2 = 0;
        for (int i3 = 0; i3 < size && i2 < 8; i3++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i3);
            if (expandableNotificationRow.isExpanded(true)) {
                minHeight = expandableNotificationRow.getMaxExpandHeight();
            } else {
                minHeight = expandableNotificationRow.getShowingLayout().getMinHeight(true);
            }
            i = (int) (i + minHeight);
            i2++;
        }
        return i2 > 0 ? i + (i2 * this.mDividerHeight) : i;
    }

    public void setActualHeight(int i) {
        int minHeight;
        if (!this.mUserLocked) {
            return;
        }
        this.mActualHeight = i;
        float groupExpandFraction = getGroupExpandFraction();
        boolean showingAsLowPriority = showingAsLowPriority();
        updateHeaderTransformation();
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int size = this.mAttachedChildren.size();
        for (int i2 = 0; i2 < size; i2++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i2);
            if (showingAsLowPriority) {
                minHeight = expandableNotificationRow.getShowingLayout().getMinHeight(false);
            } else if (expandableNotificationRow.isExpanded(true)) {
                minHeight = expandableNotificationRow.getMaxExpandHeight();
            } else {
                minHeight = expandableNotificationRow.getShowingLayout().getMinHeight(true);
            }
            float f = minHeight;
            if (i2 < maxAllowedVisibleChildren) {
                expandableNotificationRow.setActualHeight((int) NotificationUtils.interpolate(expandableNotificationRow.getShowingLayout().getMinHeight(false), f, groupExpandFraction), false);
            } else {
                expandableNotificationRow.setActualHeight((int) f, false);
            }
        }
    }

    public float getGroupExpandFraction() {
        int maxContentHeight = showingAsLowPriority() ? getMaxContentHeight() : getVisibleChildrenExpandHeight();
        int collapsedHeight = getCollapsedHeight();
        return Math.max(0.0f, Math.min(1.0f, (this.mActualHeight - collapsedHeight) / (maxContentHeight - collapsedHeight)));
    }

    private int getVisibleChildrenExpandHeight() {
        int minHeight;
        int i = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificatonTopPadding + this.mDividerHeight;
        int size = this.mAttachedChildren.size();
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int i2 = 0;
        for (int i3 = 0; i3 < size && i2 < maxAllowedVisibleChildren; i3++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i3);
            if (expandableNotificationRow.isExpanded(true)) {
                minHeight = expandableNotificationRow.getMaxExpandHeight();
            } else {
                minHeight = expandableNotificationRow.getShowingLayout().getMinHeight(true);
            }
            i = (int) (i + minHeight);
            i2++;
        }
        return i;
    }

    public int getMinHeight() {
        return getMinHeight(2, false);
    }

    public int getCollapsedHeight() {
        return getMinHeight(getMaxAllowedVisibleChildren(true), false);
    }

    public int getCollapsedHeightWithoutHeader() {
        return getMinHeight(getMaxAllowedVisibleChildren(true), false, 0);
    }

    private int getMinHeight(int i, boolean z) {
        return getMinHeight(i, z, this.mCurrentHeaderTranslation);
    }

    private int getMinHeight(int i, boolean z, int i2) {
        if (!z && showingAsLowPriority()) {
            return this.mNotificationHeaderLowPriority.getHeight();
        }
        int i3 = this.mNotificationHeaderMargin + i2;
        int size = this.mAttachedChildren.size();
        boolean z2 = true;
        int i4 = 0;
        for (int i5 = 0; i5 < size && i4 < i; i5++) {
            if (!z2) {
                i3 += this.mChildPadding;
            } else {
                z2 = false;
            }
            i3 += this.mAttachedChildren.get(i5).getSingleLineView().getHeight();
            i4++;
        }
        return (int) (i3 + this.mCollapsedBottompadding);
    }

    public boolean showingAsLowPriority() {
        return this.mIsLowPriority && !this.mContainingNotification.isExpanded();
    }

    public void reInflateViews(View.OnClickListener onClickListener, StatusBarNotification statusBarNotification) {
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            removeView(notificationHeaderView);
            this.mNotificationHeader = null;
        }
        NotificationHeaderView notificationHeaderView2 = this.mNotificationHeaderLowPriority;
        if (notificationHeaderView2 != null) {
            removeView(notificationHeaderView2);
            this.mNotificationHeaderLowPriority = null;
        }
        recreateNotificationHeader(onClickListener, this.mIsConversation);
        initDimens();
        for (int i = 0; i < this.mDividers.size(); i++) {
            View view = this.mDividers.get(i);
            int indexOfChild = indexOfChild(view);
            removeView(view);
            View inflateDivider = inflateDivider();
            addView(inflateDivider, indexOfChild);
            this.mDividers.set(i, inflateDivider);
        }
        removeView(this.mOverflowNumber);
        this.mOverflowNumber = null;
        this.mGroupOverFlowState = null;
        updateGroupOverflow();
    }

    public void setUserLocked(boolean z) {
        this.mUserLocked = z;
        if (!z) {
            updateHeaderVisibility(false);
        }
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size; i++) {
            this.mAttachedChildren.get(i).setUserLocked(z && !showingAsLowPriority());
        }
        updateHeaderTouchability();
    }

    private void updateHeaderTouchability() {
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            notificationHeaderView.setAcceptAllTouches(this.mChildrenExpanded || this.mUserLocked);
        }
    }

    public void onNotificationUpdated() {
        if (this.mShowGroupCountInExpander) {
            return;
        }
        int notificationColor = this.mContainingNotification.getNotificationColor();
        TypedArray obtainStyledAttributes = new ContextThemeWrapper(((ViewGroup) this).mContext, 16974563).getTheme().obtainStyledAttributes(new int[]{16843829});
        try {
            int color = obtainStyledAttributes.getColor(0, notificationColor);
            obtainStyledAttributes.close();
            this.mHybridGroupManager.setOverflowNumberColor(this.mOverflowNumber, color);
        } catch (Throwable th) {
            if (obtainStyledAttributes != null) {
                try {
                    obtainStyledAttributes.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public int getPositionInLinearLayout(View view) {
        int i = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificatonTopPadding;
        for (int i2 = 0; i2 < this.mAttachedChildren.size(); i2++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i2);
            boolean z = expandableNotificationRow.getVisibility() != 8;
            if (z) {
                i += this.mDividerHeight;
            }
            if (expandableNotificationRow == view) {
                return i;
            }
            if (z) {
                i += expandableNotificationRow.getIntrinsicHeight();
            }
        }
        return 0;
    }

    public void setClipBottomAmount(int i) {
        this.mClipBottomAmount = i;
        updateChildrenClipping();
    }

    public void setIsLowPriority(boolean z) {
        this.mIsLowPriority = z;
        if (this.mContainingNotification != null) {
            recreateLowPriorityHeader(null, this.mIsConversation);
            updateHeaderVisibility(false);
        }
        boolean z2 = this.mUserLocked;
        if (z2) {
            setUserLocked(z2);
        }
    }

    public NotificationViewWrapper getVisibleWrapper() {
        if (showingAsLowPriority()) {
            return this.mNotificationHeaderWrapperLowPriority;
        }
        return this.mNotificationHeaderWrapper;
    }

    public void onExpansionChanged() {
        if (this.mIsLowPriority) {
            boolean z = this.mUserLocked;
            if (z) {
                setUserLocked(z);
            }
            updateHeaderVisibility(true);
        }
    }

    @VisibleForTesting
    public boolean isUserLocked() {
        return this.mUserLocked;
    }

    public void setCurrentBottomRoundness(float f) {
        boolean z = true;
        for (int size = this.mAttachedChildren.size() - 1; size >= 0; size--) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(size);
            if (expandableNotificationRow.getVisibility() != 8) {
                expandableNotificationRow.setBottomRoundness(z ? f : 0.0f, isShown());
                z = false;
            }
        }
    }

    public void setHeaderVisibleAmount(float f) {
        this.mHeaderVisibleAmount = f;
        this.mCurrentHeaderTranslation = (int) ((1.0f - f) * this.mTranslationForHeader);
    }

    public void showFeedbackIcon(boolean z, Pair<Integer, Integer> pair) {
        NotificationViewWrapper notificationViewWrapper = this.mNotificationHeaderWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.showFeedbackIcon(z, pair);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mNotificationHeaderWrapperLowPriority;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.showFeedbackIcon(z, pair);
        }
    }

    public void setRecentlyAudiblyAlerted(boolean z) {
        NotificationViewWrapper notificationViewWrapper = this.mNotificationHeaderWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setRecentlyAudiblyAlerted(z);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mNotificationHeaderWrapperLowPriority;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setRecentlyAudiblyAlerted(z);
        }
    }
}
