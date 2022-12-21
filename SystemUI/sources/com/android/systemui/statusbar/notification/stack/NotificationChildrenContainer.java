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
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.NotificationHeaderView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.widget.NotificationExpandButton;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationGroupingUtil;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.HybridGroupManager;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import java.util.ArrayList;
import java.util.List;

public class NotificationChildrenContainer extends ViewGroup implements NotificationFadeAware {
    private static final AnimationProperties ALPHA_FADE_IN = new AnimationProperties() {
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    public static final int NUMBER_OF_CHILDREN_WHEN_CHILDREN_EXPANDED = 8;
    static final int NUMBER_OF_CHILDREN_WHEN_COLLAPSED = 2;
    static final int NUMBER_OF_CHILDREN_WHEN_SYSTEM_EXPANDED = 5;
    private static final String TAG = "NotificationChildrenContainer";
    private int mActualHeight;
    private final List<ExpandableNotificationRow> mAttachedChildren;
    private int mChildPadding;
    private boolean mChildrenExpanded;
    private int mClipBottomAmount;
    private float mCollapsedBottomPadding;
    private ExpandableNotificationRow mContainingNotification;
    private boolean mContainingNotificationIsFaded;
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
    private int mNotificationTopPadding;
    private TextView mOverflowNumber;
    private int mRealHeight;
    private boolean mShowDividersWhenExpanded;
    private boolean mShowGroupCountInExpander;
    private int mTranslationForHeader;
    private int mUntruncatedChildCount;
    private boolean mUserLocked;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void prepareExpansionChanged() {
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public NotificationChildrenContainer(Context context) {
        this(context, (AttributeSet) null);
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
        this.mContainingNotificationIsFaded = false;
        this.mHybridGroupManager = new HybridGroupManager(getContext());
        initDimens();
        setClipChildren(false);
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mChildPadding = resources.getDimensionPixelOffset(C1893R.dimen.notification_children_padding);
        this.mDividerHeight = resources.getDimensionPixelOffset(C1893R.dimen.notification_children_container_divider_height);
        this.mDividerAlpha = resources.getFloat(C1893R.dimen.notification_divider_alpha);
        this.mNotificationHeaderMargin = resources.getDimensionPixelOffset(C1893R.dimen.notification_children_container_margin_top);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(C1893R.dimen.notification_children_container_top_padding);
        this.mNotificationTopPadding = dimensionPixelOffset;
        this.mHeaderHeight = this.mNotificationHeaderMargin + dimensionPixelOffset;
        this.mCollapsedBottomPadding = (float) resources.getDimensionPixelOffset(C1893R.dimen.notification_children_collapsed_bottom_padding);
        this.mEnableShadowOnChildNotifications = resources.getBoolean(C1893R.bool.config_enableShadowOnChildNotifications);
        this.mShowGroupCountInExpander = resources.getBoolean(C1893R.bool.config_showNotificationGroupCountInExpander);
        this.mShowDividersWhenExpanded = resources.getBoolean(C1893R.bool.config_showDividersWhenGroupNotificationExpanded);
        this.mHideDividersDuringExpand = resources.getBoolean(C1893R.bool.config_hideDividersDuringExpand);
        this.mTranslationForHeader = resources.getDimensionPixelOffset(17105382) - this.mNotificationHeaderMargin;
        this.mHybridGroupManager.initDimens();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int min = Math.min(this.mAttachedChildren.size(), 8);
        for (int i6 = 0; i6 < min; i6++) {
            View view = this.mAttachedChildren.get(i6);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            this.mDividers.get(i6).layout(0, 0, getWidth(), this.mDividerHeight);
        }
        if (this.mOverflowNumber != null) {
            boolean z2 = true;
            if (getLayoutDirection() != 1) {
                z2 = false;
            }
            if (z2) {
                i5 = 0;
            } else {
                i5 = getWidth() - this.mOverflowNumber.getMeasuredWidth();
            }
            TextView textView = this.mOverflowNumber;
            textView.layout(i5, 0, this.mOverflowNumber.getMeasuredWidth() + i5, textView.getMeasuredHeight());
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

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        TextView textView;
        int i4 = i;
        int mode = View.MeasureSpec.getMode(i2);
        boolean z = mode == 1073741824;
        boolean z2 = mode == Integer.MIN_VALUE;
        int size = View.MeasureSpec.getSize(i2);
        if (z || z2) {
            i3 = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        } else {
            i3 = i2;
        }
        int size2 = View.MeasureSpec.getSize(i);
        TextView textView2 = this.mOverflowNumber;
        if (textView2 != null) {
            textView2.measure(View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE), i3);
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mDividerHeight, 1073741824);
        int i5 = this.mNotificationHeaderMargin + this.mNotificationTopPadding;
        int min = Math.min(this.mAttachedChildren.size(), 8);
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int i6 = min > maxAllowedVisibleChildren ? maxAllowedVisibleChildren - 1 : -1;
        int i7 = 0;
        while (i7 < min) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i7);
            expandableNotificationRow.setSingleLineWidthIndention((!(i7 == i6) || (textView = this.mOverflowNumber) == null) ? 0 : textView.getMeasuredWidth());
            expandableNotificationRow.measure(i4, i3);
            this.mDividers.get(i7).measure(i4, makeMeasureSpec);
            if (expandableNotificationRow.getVisibility() != 8) {
                i5 += expandableNotificationRow.getMeasuredHeight() + this.mDividerHeight;
            }
            i7++;
        }
        this.mRealHeight = i5;
        if (mode != 0) {
            i5 = Math.min(i5, size);
        }
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mHeaderHeight, 1073741824);
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            notificationHeaderView.measure(i4, makeMeasureSpec2);
        }
        NotificationHeaderView notificationHeaderView2 = this.mNotificationHeaderLowPriority;
        if (notificationHeaderView2 != null) {
            notificationHeaderView2.measure(i4, makeMeasureSpec2);
        }
        setMeasuredDimension(size2, i5);
    }

    public boolean pointInView(float f, float f2, float f3) {
        float f4 = -f3;
        return f >= f4 && f2 >= f4 && f < ((float) (this.mRight - this.mLeft)) + f3 && f2 < ((float) this.mRealHeight) + f3;
    }

    public void setUntruncatedChildCount(int i) {
        this.mUntruncatedChildCount = i;
        updateGroupOverflow();
    }

    public void addNotification(ExpandableNotificationRow expandableNotificationRow, int i) {
        ensureRemovedFromTransientContainer(expandableNotificationRow);
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
        expandableNotificationRow.setNotificationFaded(this.mContainingNotificationIsFaded);
        ExpandableViewState viewState = expandableNotificationRow.getViewState();
        if (viewState != null) {
            viewState.cancelAnimations(expandableNotificationRow);
            expandableNotificationRow.cancelAppearDrawing();
        }
    }

    private void ensureRemovedFromTransientContainer(View view) {
        if (view.getParent() != null && (view instanceof ExpandableView)) {
            ((ExpandableView) view).removeFromTransientContainerForAdditionTo(this);
        }
    }

    public void removeNotification(ExpandableNotificationRow expandableNotificationRow) {
        int indexOf = this.mAttachedChildren.indexOf(expandableNotificationRow);
        this.mAttachedChildren.remove((Object) expandableNotificationRow);
        removeView(expandableNotificationRow);
        final View remove = this.mDividers.remove(indexOf);
        removeView(remove);
        getOverlay().add(remove);
        CrossFadeHelper.fadeOut(remove, (Runnable) new Runnable() {
            public void run() {
                NotificationChildrenContainer.this.getOverlay().remove(remove);
            }
        });
        expandableNotificationRow.setSystemChildExpanded(false);
        expandableNotificationRow.setNotificationFaded(false);
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
            apply.findViewById(16908982).setVisibility(0);
            this.mNotificationHeader.setOnClickListener(this.mHeaderClickListener);
            this.mNotificationHeaderWrapper = NotificationViewWrapper.wrap(getContext(), this.mNotificationHeader, this.mContainingNotification);
            addView(this.mNotificationHeader, 0);
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
                apply.findViewById(16908982).setVisibility(0);
                this.mNotificationHeaderLowPriority.setOnClickListener(this.mHeaderClickListener);
                this.mNotificationHeaderWrapperLowPriority = NotificationViewWrapper.wrap(getContext(), this.mNotificationHeaderLowPriority, this.mContainingNotification);
                addView(this.mNotificationHeaderLowPriority, 0);
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
            if (this.mGroupOverFlowState == null) {
                this.mGroupOverFlowState = new ViewState();
                this.mNeverAppliedGroupState = true;
                return;
            }
            return;
        }
        TextView textView = this.mOverflowNumber;
        if (textView != null) {
            removeView(textView);
            if (isShown() && isAttachedToWindow()) {
                final TextView textView2 = this.mOverflowNumber;
                addTransientView(textView2, getTransientViewCount());
                CrossFadeHelper.fadeOut((View) textView2, (Runnable) new Runnable() {
                    public void run() {
                        NotificationChildrenContainer.this.removeTransientView(textView2);
                    }
                });
            }
            this.mOverflowNumber = null;
            this.mGroupOverFlowState = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateGroupOverflow();
    }

    private View inflateDivider() {
        return LayoutInflater.from(this.mContext).inflate(C1893R.layout.notification_children_divider, this, false);
    }

    public List<ExpandableNotificationRow> getAttachedChildren() {
        return this.mAttachedChildren;
    }

    public boolean applyChildOrder(List<ExpandableNotificationRow> list, VisualStabilityManager visualStabilityManager, VisualStabilityManager.Callback callback) {
        if (list == null) {
            return false;
        }
        int i = 0;
        boolean z = false;
        while (i < this.mAttachedChildren.size() && i < list.size()) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            ExpandableNotificationRow expandableNotificationRow2 = list.get(i);
            if (expandableNotificationRow != expandableNotificationRow2) {
                if (visualStabilityManager.canReorderNotification(expandableNotificationRow2)) {
                    this.mAttachedChildren.remove((Object) expandableNotificationRow2);
                    this.mAttachedChildren.add(i, expandableNotificationRow2);
                    z = true;
                } else {
                    visualStabilityManager.addReorderingAllowedCallback(callback, false);
                }
            }
            i++;
        }
        updateExpansionStates();
        return z;
    }

    public void updateExpansionStates() {
        boolean z;
        if (!this.mChildrenExpanded && !this.mUserLocked) {
            int size = this.mAttachedChildren.size();
            for (int i = 0; i < size; i++) {
                ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
                if (i == 0) {
                    z = true;
                    if (size == 1) {
                        expandableNotificationRow.setSystemChildExpanded(z);
                    }
                }
                z = false;
                expandableNotificationRow.setSystemChildExpanded(z);
            }
        }
    }

    public int getIntrinsicHeight() {
        return getIntrinsicHeight((float) getMaxAllowedVisibleChildren());
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
        for (int i4 = 0; i4 < size && ((float) i3) < f; i4++) {
            if (z2) {
                if (this.mUserLocked) {
                    i = (int) (((float) i2) + NotificationUtils.interpolate(0.0f, (float) (this.mNotificationTopPadding + this.mDividerHeight), groupExpandFraction));
                } else {
                    i = i2 + (z ? this.mNotificationTopPadding + this.mDividerHeight : 0);
                }
                z2 = false;
            } else if (this.mUserLocked) {
                i = (int) (((float) i2) + NotificationUtils.interpolate((float) this.mChildPadding, (float) this.mDividerHeight, groupExpandFraction));
            } else {
                i = i2 + (z ? this.mDividerHeight : this.mChildPadding);
            }
            i2 = i + this.mAttachedChildren.get(i4).getIntrinsicHeight();
            i3++;
        }
        if (this.mUserLocked) {
            f2 = (float) i2;
            f3 = NotificationUtils.interpolate(this.mCollapsedBottomPadding, 0.0f, groupExpandFraction);
        } else if (z) {
            return i2;
        } else {
            f2 = (float) i2;
            f3 = this.mCollapsedBottomPadding;
        }
        return (int) (f2 + f3);
    }

    /* JADX WARNING: type inference failed for: r4v8, types: [android.widget.TextView, android.view.View] */
    /* JADX WARNING: type inference failed for: r4v12, types: [android.widget.TextView] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0162  */
    /* JADX WARNING: Removed duplicated region for block: B:83:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateState(com.android.systemui.statusbar.notification.stack.ExpandableViewState r19, com.android.systemui.statusbar.notification.stack.AmbientState r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            java.util.List<com.android.systemui.statusbar.notification.row.ExpandableNotificationRow> r2 = r0.mAttachedChildren
            int r2 = r2.size()
            int r3 = r0.mNotificationHeaderMargin
            int r4 = r0.mCurrentHeaderTranslation
            int r3 = r3 + r4
            int r4 = r18.getMaxAllowedVisibleChildren()
            r5 = 1
            int r4 = r4 - r5
            int r6 = r4 + 1
            boolean r7 = r0.mUserLocked
            r8 = 0
            if (r7 == 0) goto L_0x0024
            boolean r7 = r18.showingAsLowPriority()
            if (r7 != 0) goto L_0x0024
            r7 = r5
            goto L_0x0025
        L_0x0024:
            r7 = r8
        L_0x0025:
            boolean r9 = r0.mUserLocked
            r10 = 0
            if (r9 == 0) goto L_0x0038
            float r6 = r18.getGroupExpandFraction()
            int r9 = r0.getMaxAllowedVisibleChildren(r5)
            r17 = r9
            r9 = r6
            r6 = r17
            goto L_0x0039
        L_0x0038:
            r9 = r10
        L_0x0039:
            boolean r11 = r0.mChildrenExpanded
            if (r11 == 0) goto L_0x0047
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r11 = r0.mContainingNotification
            boolean r11 = r11.isGroupExpansionChanging()
            if (r11 != 0) goto L_0x0047
            r11 = r5
            goto L_0x0048
        L_0x0047:
            r11 = r8
        L_0x0048:
            r13 = r5
            r12 = r8
        L_0x004a:
            if (r12 >= r2) goto L_0x00f8
            java.util.List<com.android.systemui.statusbar.notification.row.ExpandableNotificationRow> r14 = r0.mAttachedChildren
            java.lang.Object r14 = r14.get(r12)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r14 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r14
            if (r13 != 0) goto L_0x0071
            if (r7 == 0) goto L_0x0066
            float r3 = (float) r3
            int r15 = r0.mChildPadding
            float r15 = (float) r15
            int r5 = r0.mDividerHeight
            float r5 = (float) r5
            float r5 = com.android.systemui.statusbar.notification.NotificationUtils.interpolate(r15, r5, r9)
            float r3 = r3 + r5
            int r3 = (int) r3
            goto L_0x008e
        L_0x0066:
            boolean r5 = r0.mChildrenExpanded
            if (r5 == 0) goto L_0x006d
            int r5 = r0.mDividerHeight
            goto L_0x006f
        L_0x006d:
            int r5 = r0.mChildPadding
        L_0x006f:
            int r3 = r3 + r5
            goto L_0x008e
        L_0x0071:
            if (r7 == 0) goto L_0x0081
            float r3 = (float) r3
            int r5 = r0.mNotificationTopPadding
            int r13 = r0.mDividerHeight
            int r5 = r5 + r13
            float r5 = (float) r5
            float r5 = com.android.systemui.statusbar.notification.NotificationUtils.interpolate(r10, r5, r9)
            float r3 = r3 + r5
            int r3 = (int) r3
            goto L_0x008d
        L_0x0081:
            boolean r5 = r0.mChildrenExpanded
            if (r5 == 0) goto L_0x008b
            int r5 = r0.mNotificationTopPadding
            int r13 = r0.mDividerHeight
            int r5 = r5 + r13
            goto L_0x008c
        L_0x008b:
            r5 = r8
        L_0x008c:
            int r3 = r3 + r5
        L_0x008d:
            r13 = r8
        L_0x008e:
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r5 = r14.getViewState()
            int r14 = r14.getIntrinsicHeight()
            r5.height = r14
            int r15 = r3 + 0
            float r15 = (float) r15
            r5.yTranslation = r15
            r5.hidden = r8
            if (r11 == 0) goto L_0x00a8
            boolean r15 = r0.mEnableShadowOnChildNotifications
            if (r15 == 0) goto L_0x00a8
            float r15 = r1.zTranslation
            goto L_0x00a9
        L_0x00a8:
            r15 = r10
        L_0x00a9:
            r5.zTranslation = r15
            boolean r15 = r1.dimmed
            r5.dimmed = r15
            boolean r15 = r1.hideSensitive
            r5.hideSensitive = r15
            boolean r15 = r1.belowSpeedBump
            r5.belowSpeedBump = r15
            r5.clipTopAmount = r8
            r5.alpha = r10
            r15 = 1065353216(0x3f800000, float:1.0)
            if (r12 >= r6) goto L_0x00c9
            boolean r16 = r18.showingAsLowPriority()
            if (r16 == 0) goto L_0x00c6
            r15 = r9
        L_0x00c6:
            r5.alpha = r15
            goto L_0x00e8
        L_0x00c9:
            int r16 = (r9 > r15 ? 1 : (r9 == r15 ? 0 : -1))
            if (r16 != 0) goto L_0x00e8
            if (r12 > r4) goto L_0x00e8
            int r8 = r0.mActualHeight
            float r8 = (float) r8
            float r10 = r5.yTranslation
            float r8 = r8 - r10
            int r10 = r5.height
            float r10 = (float) r10
            float r8 = r8 / r10
            r5.alpha = r8
            float r8 = r5.alpha
            float r8 = java.lang.Math.min((float) r15, (float) r8)
            r10 = 0
            float r8 = java.lang.Math.max((float) r10, (float) r8)
            r5.alpha = r8
        L_0x00e8:
            int r8 = r1.location
            r5.location = r8
            boolean r8 = r1.inShelf
            r5.inShelf = r8
            int r3 = r3 + r14
            int r12 = r12 + 1
            r5 = 1
            r8 = 0
            r10 = 0
            goto L_0x004a
        L_0x00f8:
            android.widget.TextView r3 = r0.mOverflowNumber
            if (r3 == 0) goto L_0x015d
            java.util.List<com.android.systemui.statusbar.notification.row.ExpandableNotificationRow> r3 = r0.mAttachedChildren
            r4 = 1
            int r5 = r0.getMaxAllowedVisibleChildren(r4)
            int r2 = java.lang.Math.min((int) r5, (int) r2)
            int r2 = r2 - r4
            java.lang.Object r2 = r3.get(r2)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r2 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r2
            com.android.systemui.statusbar.notification.stack.ViewState r3 = r0.mGroupOverFlowState
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r4 = r2.getViewState()
            r3.copyFrom(r4)
            boolean r3 = r0.mChildrenExpanded
            if (r3 != 0) goto L_0x014d
            com.android.systemui.statusbar.notification.row.HybridNotificationView r3 = r2.getSingleLineView()
            if (r3 == 0) goto L_0x015d
            android.widget.TextView r4 = r3.getTextView()
            int r5 = r4.getVisibility()
            r6 = 8
            if (r5 != r6) goto L_0x0131
            android.widget.TextView r4 = r3.getTitleView()
        L_0x0131:
            int r5 = r4.getVisibility()
            if (r5 != r6) goto L_0x0138
            goto L_0x0139
        L_0x0138:
            r3 = r4
        L_0x0139:
            com.android.systemui.statusbar.notification.stack.ViewState r4 = r0.mGroupOverFlowState
            float r5 = r3.getAlpha()
            r4.alpha = r5
            com.android.systemui.statusbar.notification.stack.ViewState r4 = r0.mGroupOverFlowState
            float r5 = r4.yTranslation
            float r2 = com.android.systemui.statusbar.notification.NotificationUtils.getRelativeYOffset(r3, r2)
            float r5 = r5 + r2
            r4.yTranslation = r5
            goto L_0x015d
        L_0x014d:
            com.android.systemui.statusbar.notification.stack.ViewState r2 = r0.mGroupOverFlowState
            float r3 = r2.yTranslation
            int r4 = r0.mNotificationHeaderMargin
            float r4 = (float) r4
            float r3 = r3 + r4
            r2.yTranslation = r3
            com.android.systemui.statusbar.notification.stack.ViewState r2 = r0.mGroupOverFlowState
            r3 = 0
            r2.alpha = r3
            goto L_0x015e
        L_0x015d:
            r3 = 0
        L_0x015e:
            android.view.NotificationHeaderView r2 = r0.mNotificationHeader
            if (r2 == 0) goto L_0x0190
            com.android.systemui.statusbar.notification.stack.ViewState r2 = r0.mHeaderViewState
            if (r2 != 0) goto L_0x016d
            com.android.systemui.statusbar.notification.stack.ViewState r2 = new com.android.systemui.statusbar.notification.stack.ViewState
            r2.<init>()
            r0.mHeaderViewState = r2
        L_0x016d:
            com.android.systemui.statusbar.notification.stack.ViewState r2 = r0.mHeaderViewState
            android.view.NotificationHeaderView r4 = r0.mNotificationHeader
            r2.initFrom(r4)
            com.android.systemui.statusbar.notification.stack.ViewState r2 = r0.mHeaderViewState
            if (r11 == 0) goto L_0x017b
            float r10 = r1.zTranslation
            goto L_0x017c
        L_0x017b:
            r10 = r3
        L_0x017c:
            r2.zTranslation = r10
            com.android.systemui.statusbar.notification.stack.ViewState r1 = r0.mHeaderViewState
            int r2 = r0.mCurrentHeaderTranslation
            float r2 = (float) r2
            r1.yTranslation = r2
            com.android.systemui.statusbar.notification.stack.ViewState r1 = r0.mHeaderViewState
            float r2 = r0.mHeaderVisibleAmount
            r1.alpha = r2
            com.android.systemui.statusbar.notification.stack.ViewState r0 = r0.mHeaderViewState
            r1 = 0
            r0.hidden = r1
        L_0x0190:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer.updateState(com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.AmbientState):void");
    }

    private boolean updateChildStateForExpandedGroup(ExpandableNotificationRow expandableNotificationRow, int i, ExpandableViewState expandableViewState, int i2) {
        int clipTopAmount = i2 + expandableNotificationRow.getClipTopAmount();
        int intrinsicHeight = expandableNotificationRow.getIntrinsicHeight();
        int max = clipTopAmount + intrinsicHeight >= i ? Math.max(i - clipTopAmount, 0) : intrinsicHeight;
        expandableViewState.hidden = max == 0;
        expandableViewState.height = max;
        if (expandableViewState.height == intrinsicHeight || expandableViewState.hidden) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public int getMaxAllowedVisibleChildren() {
        return getMaxAllowedVisibleChildren(false);
    }

    /* access modifiers changed from: package-private */
    public int getMaxAllowedVisibleChildren(boolean z) {
        if (!z && ((this.mChildrenExpanded || this.mContainingNotification.isUserLocked()) && !showingAsLowPriority())) {
            return 8;
        }
        if (this.mIsLowPriority) {
            return 5;
        }
        if (this.mContainingNotification.isOnKeyguard() || !this.mContainingNotification.isExpanded()) {
            return (!this.mContainingNotification.isHeadsUpState() || !this.mContainingNotification.canShowHeadsUp()) ? 2 : 5;
        }
        return 5;
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
            viewState.yTranslation = viewState2.yTranslation - ((float) this.mDividerHeight);
            float f = (!this.mChildrenExpanded || viewState2.alpha == 0.0f) ? 0.0f : this.mDividerAlpha;
            if (this.mUserLocked && !showingAsLowPriority() && viewState2.alpha != 0.0f) {
                f = NotificationUtils.interpolate(0.0f, this.mDividerAlpha, Math.min(viewState2.alpha, groupExpandFraction));
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
        boolean z;
        int i;
        if (!this.mContainingNotification.hasExpandingChild()) {
            int size = this.mAttachedChildren.size();
            int actualHeight = this.mContainingNotification.getActualHeight() - this.mClipBottomAmount;
            for (int i2 = 0; i2 < size; i2++) {
                ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i2);
                if (expandableNotificationRow.getVisibility() != 8) {
                    float translationY = expandableNotificationRow.getTranslationY();
                    float actualHeight2 = ((float) expandableNotificationRow.getActualHeight()) + translationY;
                    float f = (float) actualHeight;
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
            viewState.yTranslation = viewState2.yTranslation - ((float) this.mDividerHeight);
            float f = (!this.mChildrenExpanded || viewState2.alpha == 0.0f) ? 0.0f : this.mDividerAlpha;
            if (this.mUserLocked && !showingAsLowPriority() && viewState2.alpha != 0.0f) {
                f = NotificationUtils.interpolate(0.0f, this.mDividerAlpha, Math.min(viewState2.alpha, groupExpandFraction));
            }
            viewState.hidden = !z;
            viewState.alpha = f;
            viewState.animateTo(view, animationProperties);
            expandableNotificationRow.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
        }
        if (this.mOverflowNumber != null) {
            if (this.mNeverAppliedGroupState) {
                float f2 = this.mGroupOverFlowState.alpha;
                this.mGroupOverFlowState.alpha = 0.0f;
                this.mGroupOverFlowState.applyToView(this.mOverflowNumber);
                this.mGroupOverFlowState.alpha = f2;
                this.mNeverAppliedGroupState = false;
            }
            this.mGroupOverFlowState.animateTo(this.mOverflowNumber, animationProperties);
        }
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            this.mHeaderViewState.applyToView(notificationHeaderView);
        }
        updateChildrenClipping();
    }

    public ExpandableNotificationRow getViewAtPosition(float f) {
        int size = this.mAttachedChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            float translationY = expandableNotificationRow.getTranslationY();
            float max = ((float) Math.max(0, expandableNotificationRow.getClipTopAmount())) + translationY;
            float actualHeight = translationY + ((float) expandableNotificationRow.getActualHeight());
            if (f >= max && f <= actualHeight) {
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

    public ExpandableNotificationRow getContainingNotification() {
        return this.mContainingNotification;
    }

    public NotificationViewWrapper getNotificationViewWrapper() {
        return this.mNotificationHeaderWrapper;
    }

    public NotificationViewWrapper getLowPriorityViewWrapper() {
        return this.mNotificationHeaderWrapperLowPriority;
    }

    public ViewGroup getCurrentHeaderView() {
        return this.mCurrentHeader;
    }

    private void updateHeaderVisibility(boolean z) {
        NotificationHeaderView notificationHeaderView = this.mCurrentHeader;
        NotificationHeaderView calculateDesiredHeader = calculateDesiredHeader();
        if (notificationHeaderView != calculateDesiredHeader) {
            if (z) {
                if (calculateDesiredHeader == null || notificationHeaderView == null) {
                    z = false;
                } else {
                    notificationHeaderView.setVisibility(0);
                    calculateDesiredHeader.setVisibility(0);
                    NotificationViewWrapper wrapperForView = getWrapperForView(calculateDesiredHeader);
                    NotificationViewWrapper wrapperForView2 = getWrapperForView(notificationHeaderView);
                    wrapperForView.transformFrom(wrapperForView2);
                    wrapperForView2.transformTo((TransformableView) wrapperForView, (Runnable) new NotificationChildrenContainer$$ExternalSyntheticLambda0(this));
                    startChildAlphaAnimations(calculateDesiredHeader == this.mNotificationHeader);
                }
            }
            if (!z) {
                if (calculateDesiredHeader != null) {
                    getWrapperForView(calculateDesiredHeader).setVisible(true);
                    calculateDesiredHeader.setVisibility(0);
                }
                if (notificationHeaderView != null) {
                    NotificationViewWrapper wrapperForView3 = getWrapperForView(notificationHeaderView);
                    if (wrapperForView3 != null) {
                        wrapperForView3.setVisible(false);
                    }
                    notificationHeaderView.setVisibility(4);
                }
            }
            resetHeaderVisibilityIfNeeded(this.mNotificationHeader, calculateDesiredHeader);
            resetHeaderVisibilityIfNeeded(this.mNotificationHeaderLowPriority, calculateDesiredHeader);
            this.mCurrentHeader = calculateDesiredHeader;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateHeaderVisibility$0$com-android-systemui-statusbar-notification-stack-NotificationChildrenContainer */
    public /* synthetic */ void mo41970x29284b69() {
        updateHeaderVisibility(false);
    }

    private void resetHeaderVisibilityIfNeeded(View view, View view2) {
        if (view != null) {
            if (!(view == this.mCurrentHeader || view == view2)) {
                getWrapperForView(view).setVisible(false);
                view.setVisibility(4);
            }
            if (view == view2 && view.getVisibility() != 0) {
                getWrapperForView(view).setVisible(true);
                view.setVisibility(0);
            }
        }
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
        int i = 0;
        while (i < size && i < 5) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i);
            expandableNotificationRow.setAlpha(f2);
            ViewState viewState = new ViewState();
            viewState.initFrom(expandableNotificationRow);
            viewState.alpha = f;
            AnimationProperties animationProperties = ALPHA_FADE_IN;
            animationProperties.setDelay((long) (i * 50));
            viewState.animateTo(expandableNotificationRow, animationProperties);
            i++;
        }
    }

    private void updateHeaderTransformation() {
        if (this.mUserLocked && showingAsLowPriority()) {
            float groupExpandFraction = getGroupExpandFraction();
            this.mNotificationHeaderWrapper.transformFrom(this.mNotificationHeaderWrapperLowPriority, groupExpandFraction);
            this.mNotificationHeader.setVisibility(0);
            this.mNotificationHeaderWrapperLowPriority.transformTo((TransformableView) this.mNotificationHeaderWrapper, groupExpandFraction);
        }
    }

    private NotificationViewWrapper getWrapperForView(View view) {
        if (view == this.mNotificationHeader) {
            return this.mNotificationHeaderWrapper;
        }
        return this.mNotificationHeaderWrapperLowPriority;
    }

    public void updateHeaderForExpansion(boolean z) {
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView == null) {
            return;
        }
        if (z) {
            ColorDrawable colorDrawable = new ColorDrawable();
            colorDrawable.setColor(this.mContainingNotification.calculateBgColor());
            this.mNotificationHeader.setHeaderBackgroundDrawable(colorDrawable);
            return;
        }
        notificationHeaderView.setHeaderBackgroundDrawable((Drawable) null);
    }

    public int getMaxContentHeight() {
        int i;
        if (showingAsLowPriority()) {
            return getMinHeight(5, true);
        }
        int i2 = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificationTopPadding;
        int size = this.mAttachedChildren.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size && i3 < 8; i4++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i4);
            if (expandableNotificationRow.isExpanded(true)) {
                i = expandableNotificationRow.getMaxExpandHeight();
            } else {
                i = expandableNotificationRow.getShowingLayout().getMinHeight(true);
            }
            i2 = (int) (((float) i2) + ((float) i));
            i3++;
        }
        return i3 > 0 ? i2 + (i3 * this.mDividerHeight) : i2;
    }

    public void setActualHeight(int i) {
        int minHeight;
        if (this.mUserLocked) {
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
                float f = (float) minHeight;
                if (i2 < maxAllowedVisibleChildren) {
                    expandableNotificationRow.setActualHeight((int) NotificationUtils.interpolate((float) expandableNotificationRow.getShowingLayout().getMinHeight(false), f, groupExpandFraction), false);
                } else {
                    expandableNotificationRow.setActualHeight((int) f, false);
                }
            }
        }
    }

    public float getGroupExpandFraction() {
        int i;
        if (showingAsLowPriority()) {
            i = getMaxContentHeight();
        } else {
            i = getVisibleChildrenExpandHeight();
        }
        int collapsedHeight = getCollapsedHeight();
        return Math.max(0.0f, Math.min(1.0f, ((float) (this.mActualHeight - collapsedHeight)) / ((float) (i - collapsedHeight))));
    }

    private int getVisibleChildrenExpandHeight() {
        int i;
        int i2 = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificationTopPadding + this.mDividerHeight;
        int size = this.mAttachedChildren.size();
        int maxAllowedVisibleChildren = getMaxAllowedVisibleChildren(true);
        int i3 = 0;
        for (int i4 = 0; i4 < size && i3 < maxAllowedVisibleChildren; i4++) {
            ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i4);
            if (expandableNotificationRow.isExpanded(true)) {
                i = expandableNotificationRow.getMaxExpandHeight();
            } else {
                i = expandableNotificationRow.getShowingLayout().getMinHeight(true);
            }
            i2 = (int) (((float) i2) + ((float) i));
            i3++;
        }
        return i2;
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
        if (z || !showingAsLowPriority()) {
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
                ExpandableNotificationRow expandableNotificationRow = this.mAttachedChildren.get(i5);
                HybridNotificationView singleLineView = expandableNotificationRow.getSingleLineView();
                if (singleLineView != null) {
                    i3 += singleLineView.getHeight();
                } else {
                    Log.e(TAG, "getMinHeight: child " + expandableNotificationRow + " single line view is null", new Exception());
                }
                i4++;
            }
            return (int) (((float) i3) + this.mCollapsedBottomPadding);
        }
        NotificationHeaderView notificationHeaderView = this.mNotificationHeaderLowPriority;
        if (notificationHeaderView != null) {
            return notificationHeaderView.getHeight();
        }
        Log.e(TAG, "getMinHeight: low priority header is null", new Exception());
        return 0;
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
        if (!this.mShowGroupCountInExpander) {
            int notificationColor = this.mContainingNotification.getNotificationColor();
            TypedArray obtainStyledAttributes = new ContextThemeWrapper(this.mContext, 16974563).getTheme().obtainStyledAttributes(new int[]{16843829});
            try {
                int color = obtainStyledAttributes.getColor(0, notificationColor);
                if (obtainStyledAttributes != null) {
                    obtainStyledAttributes.close();
                }
                this.mHybridGroupManager.setOverflowNumberColor(this.mOverflowNumber, color);
                return;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            return;
        }
        throw th;
    }

    public int getPositionInLinearLayout(View view) {
        int i = this.mNotificationHeaderMargin + this.mCurrentHeaderTranslation + this.mNotificationTopPadding;
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
            recreateLowPriorityHeader((Notification.Builder) null, this.mIsConversation);
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
        this.mCurrentHeaderTranslation = (int) ((1.0f - f) * ((float) this.mTranslationForHeader));
    }

    public void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        NotificationViewWrapper notificationViewWrapper = this.mNotificationHeaderWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setFeedbackIcon(feedbackIcon);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mNotificationHeaderWrapperLowPriority;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setFeedbackIcon(feedbackIcon);
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

    public void setNotificationFaded(boolean z) {
        this.mContainingNotificationIsFaded = z;
        NotificationViewWrapper notificationViewWrapper = this.mNotificationHeaderWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setNotificationFaded(z);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mNotificationHeaderWrapperLowPriority;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setNotificationFaded(z);
        }
        for (ExpandableNotificationRow notificationFaded : this.mAttachedChildren) {
            notificationFaded.setNotificationFaded(z);
        }
    }
}
