package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.nothingos.utils.SystemUIUtils;
/* loaded from: classes.dex */
public class NotificationShelf extends ActivatableNotificationView implements View.OnLayoutChangeListener, StatusBarStateController.StateListener {
    private AmbientState mAmbientState;
    private NotificationIconContainer mCollapsedIcons;
    private NotificationShelfController mController;
    private float mCornerAnimationDistance;
    private float mFirstElementRoundness;
    private boolean mHasItemsInStableShelf;
    private boolean mHideBackground;
    private NotificationStackScrollLayoutController mHostLayoutController;
    private boolean mInteractive;
    private int mNotGoneIndex;
    private int mPaddingBetweenElements;
    private int mScrollFastThreshold;
    private NotificationIconContainer mShelfIcons;
    private boolean mShowNotificationShelf;
    private int mStatusBarHeight;
    private int mStatusBarState;
    private static final int TAG_CONTINUOUS_CLIPPING = R$id.continuous_clipping_tag;
    private static final Interpolator ICON_ALPHA_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.6f, 0.0f);
    private int[] mTmp = new int[2];
    private boolean mAnimationsEnabled = true;
    private Rect mClipRect = new Rect();
    private int mIndexOfFirstViewInShelf = -1;

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean hasNoContentHeight() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return false;
    }

    public NotificationShelf(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View
    @VisibleForTesting
    public void onFinishInflate() {
        super.onFinishInflate();
        NotificationIconContainer notificationIconContainer = (NotificationIconContainer) findViewById(R$id.content);
        this.mShelfIcons = notificationIconContainer;
        notificationIconContainer.setClipChildren(false);
        this.mShelfIcons.setClipToPadding(false);
        setClipToActualHeight(false);
        setClipChildren(false);
        setClipToPadding(false);
        this.mShelfIcons.setIsStaticLayout(false);
        setBottomRoundness(1.0f, false);
        setTopRoundness(1.0f, false);
        setFirstInSection(true);
        initDimens();
    }

    public void bind(AmbientState ambientState, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.mAmbientState = ambientState;
        this.mHostLayoutController = notificationStackScrollLayoutController;
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mStatusBarHeight = resources.getDimensionPixelOffset(R$dimen.status_bar_height);
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(R$dimen.notification_divider_height);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = resources.getDimensionPixelOffset(R$dimen.notification_shelf_height);
        setLayoutParams(layoutParams);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R$dimen.shelf_icon_container_padding);
        this.mShelfIcons.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mScrollFastThreshold = resources.getDimensionPixelOffset(R$dimen.scroll_fast_threshold);
        this.mShowNotificationShelf = resources.getBoolean(R$bool.config_showNotificationShelf);
        this.mCornerAnimationDistance = resources.getDimensionPixelSize(R$dimen.notification_corner_animation_distance);
        this.mShelfIcons.setInNotificationIconShelf(true);
        if (!this.mShowNotificationShelf) {
            setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected View getContentView() {
        return this.mShelfIcons;
    }

    public NotificationIconContainer getShelfIcons() {
        return this.mShelfIcons;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public ExpandableViewState createExpandableViewState() {
        return new ShelfState();
    }

    public void updateState(StackScrollAlgorithm.StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        ExpandableView lastVisibleBackgroundChild = ambientState.getLastVisibleBackgroundChild();
        ShelfState shelfState = (ShelfState) getViewState();
        boolean z = false;
        if (this.mShowNotificationShelf && lastVisibleBackgroundChild != null) {
            ExpandableViewState viewState = lastVisibleBackgroundChild.getViewState();
            shelfState.copyFrom(viewState);
            shelfState.height = getIntrinsicHeight();
            shelfState.zTranslation = ambientState.getBaseZHeight();
            shelfState.clipTopAmount = 0;
            if (ambientState.isExpansionChanging() && !ambientState.isOnKeyguard()) {
                shelfState.alpha = Interpolators.getNotificationScrimAlpha(ambientState.getAlphaFraction(), true, SystemUIUtils.getInstance().shouldUseSplitNotificationShade());
            } else {
                shelfState.alpha = 1.0f - ambientState.getHideAmount();
            }
            shelfState.belowSpeedBump = this.mHostLayoutController.getSpeedBumpIndex() == 0;
            shelfState.hideSensitive = false;
            shelfState.xTranslation = getTranslationX();
            shelfState.hasItemsInStableShelf = viewState.inShelf;
            shelfState.firstViewInShelf = stackScrollAlgorithmState.firstViewInShelf;
            int i = this.mNotGoneIndex;
            if (i != -1) {
                shelfState.notGoneIndex = Math.min(shelfState.notGoneIndex, i);
            }
            if (!this.mAmbientState.isShadeExpanded() || ((!SystemUIUtils.getInstance().shouldUseSplitNotificationShade() && this.mAmbientState.isQsCustomizerShowing()) || stackScrollAlgorithmState.firstViewInShelf == null)) {
                z = true;
            }
            shelfState.hidden = z;
            int indexOf = stackScrollAlgorithmState.visibleChildren.indexOf(stackScrollAlgorithmState.firstViewInShelf);
            if (this.mAmbientState.isExpansionChanging() && stackScrollAlgorithmState.firstViewInShelf != null && indexOf > 0 && stackScrollAlgorithmState.visibleChildren.get(indexOf - 1).getViewState().hidden) {
                shelfState.hidden = true;
            }
            shelfState.yTranslation = (ambientState.getStackY() + ambientState.getStackHeight()) - shelfState.height;
            return;
        }
        shelfState.hidden = true;
        shelfState.location = 64;
        shelfState.hasItemsInStableShelf = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x016c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0173  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateAppearance() {
        int i;
        float f;
        int i2;
        ExpandableView expandableView;
        boolean z;
        int i3;
        float f2;
        int i4;
        int i5;
        float f3;
        float f4;
        float f5;
        int i6;
        int i7;
        boolean z2;
        int i8;
        int i9;
        NotificationIconContainer.IconState iconState;
        if (!this.mShowNotificationShelf) {
            return;
        }
        this.mShelfIcons.resetViewStates();
        float translationY = getTranslationY();
        ExpandableView lastVisibleBackgroundChild = this.mAmbientState.getLastVisibleBackgroundChild();
        this.mNotGoneIndex = -1;
        boolean z3 = this.mHideBackground && !((ShelfState) getViewState()).hasItemsInStableShelf;
        boolean z4 = this.mAmbientState.getCurrentScrollVelocity() > ((float) this.mScrollFastThreshold) || (this.mAmbientState.isExpansionChanging() && Math.abs(this.mAmbientState.getExpandingVelocity()) > ((float) this.mScrollFastThreshold));
        boolean z5 = this.mAmbientState.isExpansionChanging() && !this.mAmbientState.isPanelTracking();
        int baseZHeight = this.mAmbientState.getBaseZHeight();
        int i10 = 0;
        int i11 = 0;
        float f6 = 0.0f;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        float f7 = 0.0f;
        float f8 = 0.0f;
        int i16 = 0;
        while (i13 < this.mHostLayoutController.getChildCount()) {
            ExpandableView childAt = this.mHostLayoutController.getChildAt(i13);
            if (childAt.needsClippingToShelf()) {
                int i17 = i10;
                if (childAt.getVisibility() == 8) {
                    i = i11;
                    f = f6;
                    i2 = i13;
                    expandableView = lastVisibleBackgroundChild;
                    z = z4;
                    f2 = f8;
                    i4 = i17;
                    i5 = i15;
                    i3 = baseZHeight;
                } else {
                    boolean z6 = ViewState.getFinalTranslationZ(childAt) > ((float) baseZHeight) || childAt.isPinned();
                    boolean z7 = childAt == lastVisibleBackgroundChild;
                    float translationY2 = childAt.getTranslationY();
                    expandableView = lastVisibleBackgroundChild;
                    i4 = i17;
                    i3 = baseZHeight;
                    int i18 = i11;
                    float f9 = f6;
                    int i19 = i15;
                    int i20 = i12;
                    i2 = i13;
                    z = z4;
                    float updateShelfTransformation = updateShelfTransformation(i13, childAt, z4, z5, z7);
                    if ((z7 && !childAt.isInShelf()) || z6 || z3) {
                        f3 = getIntrinsicHeight() + translationY;
                    } else {
                        f3 = translationY - this.mPaddingBetweenElements;
                    }
                    i12 = Math.max(updateNotificationClipHeight(childAt, f3, i4), i20);
                    if (childAt instanceof ExpandableNotificationRow) {
                        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                        f7 += updateShelfTransformation;
                        int backgroundColorWithoutTint = expandableNotificationRow.getBackgroundColorWithoutTint();
                        if (translationY2 >= translationY) {
                            i7 = -1;
                            if (this.mNotGoneIndex == -1) {
                                this.mNotGoneIndex = i4;
                                setTintColor(i14);
                                f5 = f8;
                                setOverrideTintColor(i19, f5);
                                i6 = i19;
                                i14 = i6;
                                f8 = f5;
                                if (!z7 && this.mController.canModifyColorOfNotifications()) {
                                    i8 = i16 == 0 ? backgroundColorWithoutTint : i16;
                                    expandableNotificationRow.setOverrideTintColor(i8, updateShelfTransformation);
                                    z2 = false;
                                } else {
                                    z2 = false;
                                    expandableNotificationRow.setOverrideTintColor(0, 0.0f);
                                    i8 = backgroundColorWithoutTint;
                                }
                                if (i4 == 0 || !z6) {
                                    expandableNotificationRow.setAboveShelf(z2);
                                }
                                if (i4 == 0 || (iconState = getIconState(expandableNotificationRow.getEntry().getIcons().getShelfIcon())) == null || iconState.clampedAppearAmount != 1.0f) {
                                    i9 = i18;
                                    f4 = f9;
                                } else {
                                    i9 = (int) (childAt.getTranslationY() - getTranslationY());
                                    f4 = expandableNotificationRow.getCurrentTopRoundness();
                                }
                                i4++;
                                i16 = i8;
                                i15 = i14;
                                i14 = backgroundColorWithoutTint;
                                i11 = i9;
                            } else {
                                f5 = f8;
                                i6 = i19;
                            }
                        } else {
                            f5 = f8;
                            i6 = i19;
                            i7 = -1;
                        }
                        if (this.mNotGoneIndex == i7) {
                            f8 = updateShelfTransformation;
                            if (!z7) {
                            }
                            z2 = false;
                            expandableNotificationRow.setOverrideTintColor(0, 0.0f);
                            i8 = backgroundColorWithoutTint;
                            if (i4 == 0) {
                            }
                            expandableNotificationRow.setAboveShelf(z2);
                            if (i4 == 0) {
                            }
                            i9 = i18;
                            f4 = f9;
                            i4++;
                            i16 = i8;
                            i15 = i14;
                            i14 = backgroundColorWithoutTint;
                            i11 = i9;
                        }
                        i14 = i6;
                        f8 = f5;
                        if (!z7) {
                        }
                        z2 = false;
                        expandableNotificationRow.setOverrideTintColor(0, 0.0f);
                        i8 = backgroundColorWithoutTint;
                        if (i4 == 0) {
                        }
                        expandableNotificationRow.setAboveShelf(z2);
                        if (i4 == 0) {
                        }
                        i9 = i18;
                        f4 = f9;
                        i4++;
                        i16 = i8;
                        i15 = i14;
                        i14 = backgroundColorWithoutTint;
                        i11 = i9;
                    } else {
                        i15 = i19;
                        i11 = i18;
                        f4 = f9;
                        f8 = f8;
                    }
                    if (childAt instanceof ActivatableNotificationView) {
                        updateCornerRoundnessOnScroll((ActivatableNotificationView) childAt, translationY2, translationY);
                    }
                    f6 = f4;
                    i13 = i2 + 1;
                    i10 = i4;
                    z4 = z;
                    baseZHeight = i3;
                    lastVisibleBackgroundChild = expandableView;
                }
            } else {
                i = i11;
                f = f6;
                i2 = i13;
                expandableView = lastVisibleBackgroundChild;
                z = z4;
                i3 = baseZHeight;
                f2 = f8;
                i4 = i10;
                i5 = i15;
            }
            i12 = i12;
            i15 = i5;
            i11 = i;
            f6 = f;
            f8 = f2;
            i13 = i2 + 1;
            i10 = i4;
            z4 = z;
            baseZHeight = i3;
            lastVisibleBackgroundChild = expandableView;
        }
        int i21 = i10;
        int i22 = i11;
        float f10 = f6;
        int i23 = i12;
        clipTransientViews();
        setClipTopAmount(i23);
        boolean z8 = getViewState().hidden || i23 >= getIntrinsicHeight() || !this.mShowNotificationShelf || f7 < 1.0f;
        setVisibility(z8 ? 4 : 0);
        setBackgroundTop(i22);
        setFirstElementRoundness(f10);
        this.mShelfIcons.setSpeedBumpIndex(this.mHostLayoutController.getSpeedBumpIndex());
        this.mShelfIcons.calculateIconTranslations();
        this.mShelfIcons.applyIconStates();
        for (int i24 = 0; i24 < this.mHostLayoutController.getChildCount(); i24++) {
            ExpandableView childAt2 = this.mHostLayoutController.getChildAt(i24);
            if ((childAt2 instanceof ExpandableNotificationRow) && childAt2.getVisibility() != 8) {
                updateContinuousClipping((ExpandableNotificationRow) childAt2);
            }
        }
        setHideBackground(z8);
        if (this.mNotGoneIndex != -1) {
            return;
        }
        this.mNotGoneIndex = i21;
    }

    private void updateCornerRoundnessOnScroll(ActivatableNotificationView activatableNotificationView, float f, float f2) {
        boolean z = true;
        boolean z2 = !this.mAmbientState.isOnKeyguard() && !this.mAmbientState.isShadeExpanded() && (activatableNotificationView instanceof ExpandableNotificationRow) && ((ExpandableNotificationRow) activatableNotificationView).isHeadsUp();
        boolean z3 = this.mAmbientState.isShadeExpanded() && activatableNotificationView == this.mAmbientState.getTrackedHeadsUpRow();
        if (f >= f2 || this.mHostLayoutController.isViewAffectedBySwipe(activatableNotificationView) || z2 || z3 || activatableNotificationView.isAboveShelf() || this.mAmbientState.isPulsing() || this.mAmbientState.isDozing()) {
            z = false;
        }
        if (!z) {
            return;
        }
        float dimension = getResources().getDimension(R$dimen.notification_corner_radius_small) / getResources().getDimension(R$dimen.notification_corner_radius);
        float actualHeight = activatableNotificationView.getActualHeight() + f;
        float expansionFraction = this.mCornerAnimationDistance * this.mAmbientState.getExpansionFraction();
        float f3 = f2 - expansionFraction;
        float f4 = 1.0f;
        if (actualHeight >= f3) {
            float saturate = MathUtils.saturate((actualHeight - f3) / expansionFraction);
            if (activatableNotificationView.isLastInSection()) {
                saturate = 1.0f;
            }
            activatableNotificationView.setBottomRoundness(saturate, false);
        } else if (actualHeight < f3) {
            activatableNotificationView.setBottomRoundness(activatableNotificationView.isLastInSection() ? 1.0f : dimension, false);
        }
        if (f >= f3) {
            float saturate2 = MathUtils.saturate((f - f3) / expansionFraction);
            if (!activatableNotificationView.isFirstInSection()) {
                f4 = saturate2;
            }
            activatableNotificationView.setTopRoundness(f4, false);
        } else if (f >= f3) {
        } else {
            if (activatableNotificationView.isFirstInSection()) {
                dimension = 1.0f;
            }
            activatableNotificationView.setTopRoundness(dimension, false);
        }
    }

    private void clipTransientViews() {
        for (int i = 0; i < this.mHostLayoutController.getTransientViewCount(); i++) {
            View transientView = this.mHostLayoutController.getTransientView(i);
            if (transientView instanceof ExpandableView) {
                updateNotificationClipHeight((ExpandableView) transientView, getTranslationY(), -1);
            }
        }
    }

    private void setFirstElementRoundness(float f) {
        if (this.mFirstElementRoundness != f) {
            this.mFirstElementRoundness = f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIconClipAmount(ExpandableNotificationRow expandableNotificationRow) {
        float translationY = expandableNotificationRow.getTranslationY();
        if (getClipTopAmount() != 0) {
            translationY = Math.max(translationY, getTranslationY() + getClipTopAmount());
        }
        StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        float translationY2 = getTranslationY() + shelfIcon.getTop() + shelfIcon.getTranslationY();
        if (translationY2 < translationY && !this.mAmbientState.isFullyHidden()) {
            int i = (int) (translationY - translationY2);
            shelfIcon.setClipBounds(new Rect(0, i, shelfIcon.getWidth(), Math.max(i, shelfIcon.getHeight())));
            return;
        }
        shelfIcon.setClipBounds(null);
    }

    private void updateContinuousClipping(final ExpandableNotificationRow expandableNotificationRow) {
        final StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        boolean z = true;
        boolean z2 = ViewState.isAnimatingY(shelfIcon) && !this.mAmbientState.isDozing();
        int i = TAG_CONTINUOUS_CLIPPING;
        if (shelfIcon.getTag(i) == null) {
            z = false;
        }
        if (!z2 || z) {
            return;
        }
        final ViewTreeObserver viewTreeObserver = shelfIcon.getViewTreeObserver();
        final ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.NotificationShelf.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (ViewState.isAnimatingY(shelfIcon)) {
                    NotificationShelf.this.updateIconClipAmount(expandableNotificationRow);
                    return true;
                }
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnPreDrawListener(this);
                }
                shelfIcon.setTag(NotificationShelf.TAG_CONTINUOUS_CLIPPING, null);
                return true;
            }
        };
        viewTreeObserver.addOnPreDrawListener(onPreDrawListener);
        shelfIcon.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.NotificationShelf.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                if (view == shelfIcon) {
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
                    }
                    shelfIcon.setTag(NotificationShelf.TAG_CONTINUOUS_CLIPPING, null);
                }
            }
        });
        shelfIcon.setTag(i, onPreDrawListener);
    }

    private int updateNotificationClipHeight(ExpandableView expandableView, float f, int i) {
        float translationY = expandableView.getTranslationY() + expandableView.getActualHeight();
        boolean z = true;
        boolean z2 = (expandableView.isPinned() || expandableView.isHeadsUpAnimatingAway()) && !this.mAmbientState.isDozingAndNotPulsing(expandableView);
        if (!this.mAmbientState.isPulseExpanding()) {
            z = expandableView.showingPulsing();
        } else if (i != 0) {
            z = false;
        }
        if (translationY > f && !z && (this.mAmbientState.isShadeExpanded() || !z2)) {
            int i2 = (int) (translationY - f);
            if (z2) {
                i2 = Math.min(expandableView.getIntrinsicHeight() - expandableView.getCollapsedHeight(), i2);
            }
            expandableView.setClipBottomAmount(i2);
        } else {
            expandableView.setClipBottomAmount(0);
        }
        if (z) {
            return (int) (translationY - getTranslationY());
        }
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        if (!this.mHasItemsInStableShelf) {
            f = 0.0f;
        }
        super.setFakeShadowIntensity(f, f2, i, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x005a, code lost:
        if (r12 >= r1) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float updateShelfTransformation(int i, ExpandableView expandableView, boolean z, boolean z2, boolean z3) {
        float constrain;
        float translationY = expandableView.getTranslationY();
        int actualHeight = expandableView.getActualHeight() + this.mPaddingBetweenElements;
        float calculateIconTransformationStart = calculateIconTransformationStart(expandableView);
        float min = Math.min((actualHeight + translationY) - calculateIconTransformationStart, getIntrinsicHeight());
        if (z3) {
            actualHeight = Math.min(actualHeight, expandableView.getMinHeight() - getIntrinsicHeight());
            min = Math.min(min, expandableView.getMinHeight() - getIntrinsicHeight());
        }
        float f = actualHeight;
        float f2 = translationY + f;
        float translationY2 = getTranslationY();
        float f3 = 0.0f;
        if (this.mAmbientState.isExpansionChanging() && !this.mAmbientState.isOnKeyguard()) {
            int i2 = this.mIndexOfFirstViewInShelf;
            if (i2 != -1) {
            }
            constrain = 0.0f;
        } else {
            if (f2 >= translationY2 && ((!this.mAmbientState.isUnlockHintRunning() || expandableView.isInShelf()) && (this.mAmbientState.isShadeExpanded() || (!expandableView.isPinned() && !expandableView.isHeadsUpAnimatingAway())))) {
                if (translationY < translationY2) {
                    float f4 = translationY2 - translationY;
                    f3 = 1.0f - Math.min(1.0f, f4 / f);
                    constrain = 1.0f - MathUtils.constrain(z3 ? f4 / (calculateIconTransformationStart - translationY) : (translationY2 - calculateIconTransformationStart) / min, 0.0f, 1.0f);
                }
                constrain = 1.0f;
                f3 = 1.0f;
            }
            constrain = 0.0f;
        }
        updateIconPositioning(expandableView, constrain, z, z2, z3);
        return f3;
    }

    private float calculateIconTransformationStart(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        if (shelfTransformationTarget == null) {
            return expandableView.getTranslationY();
        }
        return (expandableView.getTranslationY() + expandableView.getRelativeTopPadding(shelfTransformationTarget)) - expandableView.getShelfIcon().getTop();
    }

    private void updateIconPositioning(ExpandableView expandableView, float f, boolean z, boolean z2, boolean z3) {
        StatusBarIconView shelfIcon = expandableView.getShelfIcon();
        NotificationIconContainer.IconState iconState = getIconState(shelfIcon);
        if (iconState == null) {
            return;
        }
        boolean z4 = false;
        float f2 = (f > 0.5f ? 1 : (f == 0.5f ? 0 : -1)) > 0 || isTargetClipped(expandableView) ? 1.0f : 0.0f;
        if (f == f2) {
            iconState.noAnimations = (z || z2) && !z3;
        }
        if (!z3 && (z || (z2 && !ViewState.isAnimatingY(shelfIcon)))) {
            iconState.cancelAnimations(shelfIcon);
            iconState.noAnimations = true;
        }
        if (this.mAmbientState.isHiddenAtAll() && !expandableView.isInShelf()) {
            f = this.mAmbientState.isFullyHidden() ? 1.0f : 0.0f;
        } else {
            if (iconState.clampedAppearAmount != f2) {
                z4 = true;
            }
            iconState.needsCannedAnimation = z4;
        }
        iconState.clampedAppearAmount = f2;
        setIconTransformationAmount(expandableView, f);
    }

    private boolean isTargetClipped(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        return shelfTransformationTarget != null && ((expandableView.getTranslationY() + expandableView.getContentTranslation()) + ((float) expandableView.getRelativeTopPadding(shelfTransformationTarget))) + ((float) shelfTransformationTarget.getHeight()) >= getTranslationY() - ((float) this.mPaddingBetweenElements);
    }

    private void setIconTransformationAmount(ExpandableView expandableView, float f) {
        ExpandableNotificationRow expandableNotificationRow;
        StatusBarIconView shelfIcon;
        NotificationIconContainer.IconState iconState;
        boolean z = expandableView instanceof ExpandableNotificationRow;
        if (z && (iconState = getIconState((shelfIcon = (expandableNotificationRow = (ExpandableNotificationRow) expandableView).getShelfIcon()))) != null) {
            iconState.alpha = ICON_ALPHA_INTERPOLATOR.getInterpolation(f);
            boolean z2 = true;
            boolean z3 = (expandableNotificationRow.isDrawingAppearAnimation() && !expandableNotificationRow.isInShelf()) || (z && expandableNotificationRow.isLowPriority() && this.mShelfIcons.hasMaxNumDot()) || ((f == 0.0f && !iconState.isAnimating(shelfIcon)) || expandableNotificationRow.isAboveShelf() || expandableNotificationRow.showingPulsing() || expandableNotificationRow.getTranslationZ() > ((float) this.mAmbientState.getBaseZHeight()));
            iconState.hidden = z3;
            if (z3) {
                f = 0.0f;
            }
            iconState.iconAppearAmount = f;
            iconState.xTranslation = this.mShelfIcons.getActualPaddingStart();
            if (!expandableNotificationRow.isInShelf() || expandableNotificationRow.isTransformingIntoShelf()) {
                z2 = false;
            }
            if (z2) {
                iconState.iconAppearAmount = 1.0f;
                iconState.alpha = 1.0f;
                iconState.hidden = false;
            }
            int contrastedStaticDrawableColor = shelfIcon.getContrastedStaticDrawableColor(getBackgroundColorWithoutTint());
            if (expandableNotificationRow.isShowingIcon() && contrastedStaticDrawableColor != 0) {
                contrastedStaticDrawableColor = NotificationUtils.interpolateColors(expandableNotificationRow.getOriginalIconColor(), contrastedStaticDrawableColor, iconState.iconAppearAmount);
            }
            iconState.iconColor = contrastedStaticDrawableColor;
        }
    }

    private NotificationIconContainer.IconState getIconState(StatusBarIconView statusBarIconView) {
        return this.mShelfIcons.getIconState(statusBarIconView);
    }

    private void setHideBackground(boolean z) {
        if (this.mHideBackground != z) {
            this.mHideBackground = z;
            updateOutline();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public boolean needsOutline() {
        return !this.mHideBackground && super.needsOutline();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateRelativeOffset();
        int i5 = getResources().getDisplayMetrics().heightPixels;
        this.mClipRect.set(0, -i5, getWidth(), i5);
        this.mShelfIcons.setClipBounds(this.mClipRect);
    }

    private void updateRelativeOffset() {
        this.mCollapsedIcons.getLocationOnScreen(this.mTmp);
        getLocationOnScreen(this.mTmp);
    }

    public int getNotGoneIndex() {
        return this.mNotGoneIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHasItemsInStableShelf(boolean z) {
        if (this.mHasItemsInStableShelf != z) {
            this.mHasItemsInStableShelf = z;
            updateInteractiveness();
        }
    }

    public void setCollapsedIcons(NotificationIconContainer notificationIconContainer) {
        this.mCollapsedIcons = notificationIconContainer;
        notificationIconContainer.addOnLayoutChangeListener(this);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        this.mStatusBarState = i;
        updateInteractiveness();
    }

    private void updateInteractiveness() {
        int i = 1;
        boolean z = this.mStatusBarState == 1 && this.mHasItemsInStableShelf;
        this.mInteractive = z;
        setClickable(z);
        setFocusable(this.mInteractive);
        if (!this.mInteractive) {
            i = 4;
        }
        setImportantForAccessibility(i);
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
        if (!z) {
            this.mShelfIcons.setAnimationsEnabled(false);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mInteractive) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, getContext().getString(R$string.accessibility_overflow_action)));
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateRelativeOffset();
    }

    public void setController(NotificationShelfController notificationShelfController) {
        this.mController = notificationShelfController;
    }

    public void setIndexOfFirstViewInShelf(ExpandableView expandableView) {
        this.mIndexOfFirstViewInShelf = this.mHostLayoutController.indexOfChild(expandableView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ShelfState extends ExpandableViewState {
        private ExpandableView firstViewInShelf;
        private boolean hasItemsInStableShelf;

        private ShelfState() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            if (!NotificationShelf.this.mShowNotificationShelf) {
                return;
            }
            super.applyToView(view);
            NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
            NotificationShelf.this.updateAppearance();
            NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
            NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void animateTo(View view, AnimationProperties animationProperties) {
            if (!NotificationShelf.this.mShowNotificationShelf) {
                return;
            }
            super.animateTo(view, animationProperties);
            NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
            NotificationShelf.this.updateAppearance();
            NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
            NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
        }
    }
}
