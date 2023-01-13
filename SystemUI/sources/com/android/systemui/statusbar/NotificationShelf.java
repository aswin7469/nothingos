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
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1894R;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
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
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.animation.NTShadeInterpolation;
import com.nothing.systemui.statusbar.notification.stack.AmbientStateEx;
import com.nothing.utils.NTSystemUIUtils;

public class NotificationShelf extends ActivatableNotificationView implements View.OnLayoutChangeListener, StatusBarStateController.StateListener {
    private static final Interpolator ICON_ALPHA_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.6f, 0.0f);
    private static final String TAG = "NotificationShelf";
    private static final int TAG_CONTINUOUS_CLIPPING = 2131427743;
    private float mActualWidth = -1.0f;
    private AmbientState mAmbientState;
    /* access modifiers changed from: private */
    public boolean mAnimationsEnabled = true;
    private Rect mClipRect = new Rect();
    private NotificationIconContainer mCollapsedIcons;
    private NotificationShelfController mController;
    private float mCornerAnimationDistance;
    private float mFirstElementRoundness;
    private boolean mHasItemsInStableShelf;
    private boolean mHideBackground;
    private NotificationStackScrollLayoutController mHostLayoutController;
    private int mIndexOfFirstViewInShelf = -1;
    private boolean mInteractive;
    private int mNotGoneIndex;
    private int mPaddingBetweenElements;
    private int mScrollFastThreshold;
    /* access modifiers changed from: private */
    public NotificationIconContainer mShelfIcons;
    /* access modifiers changed from: private */
    public boolean mShowNotificationShelf;
    private int mStatusBarHeight;
    private int mStatusBarState;
    private int[] mTmp = new int[2];

    public boolean hasNoContentHeight() {
        return true;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isXInView(float f, float f2, float f3, float f4) {
        return f3 - f2 <= f && f < f4 + f2;
    }

    public boolean isYInView(float f, float f2, float f3, float f4) {
        return f3 - f2 <= f && f < f4 + f2;
    }

    public boolean needsClippingToShelf() {
        return false;
    }

    public NotificationShelf(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        NotificationIconContainer notificationIconContainer = (NotificationIconContainer) findViewById(C1894R.C1898id.content);
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
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(C1894R.dimen.notification_divider_height);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = resources.getDimensionPixelOffset(C1894R.dimen.notification_shelf_height);
        setLayoutParams(layoutParams);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(C1894R.dimen.shelf_icon_container_padding);
        this.mShelfIcons.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mScrollFastThreshold = resources.getDimensionPixelOffset(C1894R.dimen.scroll_fast_threshold);
        this.mShowNotificationShelf = resources.getBoolean(C1894R.bool.config_showNotificationShelf);
        this.mCornerAnimationDistance = (float) resources.getDimensionPixelSize(C1894R.dimen.notification_corner_animation_distance);
        this.mShelfIcons.setInNotificationIconShelf(true);
        if (!this.mShowNotificationShelf) {
            setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    /* access modifiers changed from: protected */
    public View getContentView() {
        return this.mShelfIcons;
    }

    public NotificationIconContainer getShelfIcons() {
        return this.mShelfIcons;
    }

    public ExpandableViewState createExpandableViewState() {
        return new ShelfState();
    }

    public void updateState(StackScrollAlgorithm.StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        ExpandableView lastVisibleBackgroundChild = ambientState.getLastVisibleBackgroundChild();
        ShelfState shelfState = (ShelfState) getViewState();
        boolean z = false;
        if (!this.mShowNotificationShelf || lastVisibleBackgroundChild == null) {
            shelfState.hidden = true;
            shelfState.location = 64;
            boolean unused = shelfState.hasItemsInStableShelf = false;
            return;
        }
        ExpandableViewState viewState = lastVisibleBackgroundChild.getViewState();
        shelfState.copyFrom(viewState);
        shelfState.height = getIntrinsicHeight();
        shelfState.zTranslation = (float) ambientState.getBaseZHeight();
        shelfState.clipTopAmount = 0;
        if (!ambientState.isExpansionChanging() || ambientState.isOnKeyguard()) {
            shelfState.alpha = 1.0f - ambientState.getHideAmount();
        } else {
            ambientState.getExpansionFraction();
            shelfState.alpha = NTShadeInterpolation.getNotificationScrimAlpha(((AmbientStateEx) NTDependencyEx.get(AmbientStateEx.class)).getAlphaFraction(), true, NTSystemUIUtils.getInstance().isSplitShadeEnabled());
        }
        shelfState.belowSpeedBump = this.mHostLayoutController.getSpeedBumpIndex() == 0;
        shelfState.hideSensitive = false;
        shelfState.xTranslation = getTranslationX();
        boolean unused2 = shelfState.hasItemsInStableShelf = viewState.inShelf;
        ExpandableView unused3 = shelfState.firstViewInShelf = stackScrollAlgorithmState.firstViewInShelf;
        if (this.mNotGoneIndex != -1) {
            shelfState.notGoneIndex = Math.min(shelfState.notGoneIndex, this.mNotGoneIndex);
        }
        if (!this.mAmbientState.isShadeExpanded() || stackScrollAlgorithmState.firstViewInShelf == null) {
            z = true;
        }
        shelfState.hidden = z;
        int indexOf = stackScrollAlgorithmState.visibleChildren.indexOf(stackScrollAlgorithmState.firstViewInShelf);
        if (this.mAmbientState.isExpansionChanging() && stackScrollAlgorithmState.firstViewInShelf != null && indexOf > 0 && stackScrollAlgorithmState.visibleChildren.get(indexOf - 1).getViewState().hidden) {
            shelfState.hidden = true;
        }
        shelfState.yTranslation = (ambientState.getStackY() + ambientState.getStackHeight()) - ((float) shelfState.height);
    }

    public void updateActualWidth(float f, float f2) {
        float f3;
        if (this.mAmbientState.isOnKeyguard()) {
            f3 = MathUtils.lerp(f2, (float) getWidth(), f);
        } else {
            f3 = (float) getWidth();
        }
        int i = (int) f3;
        setBackgroundWidth(i);
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        if (notificationIconContainer != null) {
            notificationIconContainer.setActualLayoutWidth(i);
        }
        this.mActualWidth = f3;
    }

    public int getActualWidth() {
        float f = this.mActualWidth;
        return f > -1.0f ? (int) f : getWidth();
    }

    public boolean pointInView(float f, float f2, float f3) {
        float width = (float) getWidth();
        float actualWidth = (float) getActualWidth();
        float f4 = isLayoutRtl() ? width - actualWidth : 0.0f;
        if (!isLayoutRtl()) {
            width = actualWidth;
        }
        return isXInView(f, f3, f4, width) && isYInView(f2, f3, (float) this.mClipTopAmount, (float) getActualHeight());
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x019f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateAppearance() {
        /*
            r32 = this;
            r7 = r32
            boolean r0 = r7.mShowNotificationShelf
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            com.android.systemui.statusbar.phone.NotificationIconContainer r0 = r7.mShelfIcons
            r0.resetViewStates()
            float r8 = r32.getTranslationY()
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            com.android.systemui.statusbar.notification.row.ExpandableView r9 = r0.getLastVisibleBackgroundChild()
            r10 = -1
            r7.mNotGoneIndex = r10
            boolean r0 = r7.mHideBackground
            if (r0 == 0) goto L_0x002b
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r0 = r32.getViewState()
            com.android.systemui.statusbar.NotificationShelf$ShelfState r0 = (com.android.systemui.statusbar.NotificationShelf.ShelfState) r0
            boolean r0 = r0.hasItemsInStableShelf
            if (r0 != 0) goto L_0x002b
            r13 = 1
            goto L_0x002c
        L_0x002b:
            r13 = 0
        L_0x002c:
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            float r0 = r0.getCurrentScrollVelocity()
            int r1 = r7.mScrollFastThreshold
            float r1 = (float) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L_0x0055
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            boolean r0 = r0.isExpansionChanging()
            if (r0 == 0) goto L_0x0053
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            float r0 = r0.getExpandingVelocity()
            float r0 = java.lang.Math.abs((float) r0)
            int r1 = r7.mScrollFastThreshold
            float r1 = (float) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x0053
            goto L_0x0055
        L_0x0053:
            r14 = 0
            goto L_0x0056
        L_0x0055:
            r14 = 1
        L_0x0056:
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            boolean r0 = r0.isExpansionChanging()
            if (r0 == 0) goto L_0x0068
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            boolean r0 = r0.isPanelTracking()
            if (r0 != 0) goto L_0x0068
            r15 = 1
            goto L_0x0069
        L_0x0068:
            r15 = 0
        L_0x0069:
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r7.mAmbientState
            int r6 = r0.getBaseZHeight()
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = 0
            r11 = 0
            r12 = 0
            r16 = 0
            r17 = 0
            r18 = 0
        L_0x007c:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r5 = r7.mHostLayoutController
            int r5 = r5.getChildCount()
            r20 = 1065353216(0x3f800000, float:1.0)
            r10 = 8
            if (r4 >= r5) goto L_0x0204
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r5 = r7.mHostLayoutController
            com.android.systemui.statusbar.notification.row.ExpandableView r5 = r5.getChildAt(r4)
            boolean r21 = r5.needsClippingToShelf()
            if (r21 == 0) goto L_0x01d2
            r21 = r0
            int r0 = r5.getVisibility()
            if (r0 != r10) goto L_0x009e
            goto L_0x01d4
        L_0x009e:
            float r0 = com.android.systemui.statusbar.notification.stack.ViewState.getFinalTranslationZ(r5)
            float r10 = (float) r6
            int r0 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r0 > 0) goto L_0x00b0
            boolean r0 = r5.isPinned()
            if (r0 == 0) goto L_0x00ae
            goto L_0x00b0
        L_0x00ae:
            r10 = 0
            goto L_0x00b1
        L_0x00b0:
            r10 = 1
        L_0x00b1:
            if (r5 != r9) goto L_0x00b6
            r22 = 1
            goto L_0x00b8
        L_0x00b6:
            r22 = 0
        L_0x00b8:
            float r0 = r5.getTranslationY()
            float r23 = r32.getTranslationY()
            r24 = r0
            int r0 = r7.mPaddingBetweenElements
            float r0 = (float) r0
            float r23 = r23 - r0
            r25 = r9
            r9 = r24
            r0 = r32
            r26 = r1
            r1 = r4
            r24 = r12
            r12 = r2
            r2 = r5
            r27 = r9
            r9 = r3
            r3 = r14
            r28 = r4
            r4 = r15
            r29 = r5
            r19 = r14
            r14 = 0
            r5 = r22
            r30 = r6
            r6 = r23
            float r0 = r0.getAmountInShelf(r1, r2, r3, r4, r5, r6)
            if (r22 == 0) goto L_0x00f2
            boolean r1 = r29.isInShelf()
            if (r1 == 0) goto L_0x00fd
        L_0x00f2:
            if (r10 != 0) goto L_0x00fd
            if (r13 == 0) goto L_0x00f7
            goto L_0x00fd
        L_0x00f7:
            int r1 = r7.mPaddingBetweenElements
            float r1 = (float) r1
            float r1 = r8 - r1
            goto L_0x0103
        L_0x00fd:
            int r1 = r32.getIntrinsicHeight()
            float r1 = (float) r1
            float r1 = r1 + r8
        L_0x0103:
            r2 = r29
            int r3 = r7.updateNotificationClipHeight(r2, r1, r11)
            int r3 = java.lang.Math.max((int) r3, (int) r9)
            boolean r4 = r2 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r4 == 0) goto L_0x01a9
            r5 = r2
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r5
            float r4 = r12 + r0
            int r6 = r5.getBackgroundColorWithoutTint()
            int r9 = (r27 > r8 ? 1 : (r27 == r8 ? 0 : -1))
            if (r9 < 0) goto L_0x013b
            int r9 = r7.mNotGoneIndex
            r12 = -1
            if (r9 != r12) goto L_0x0136
            r7.mNotGoneIndex = r11
            r9 = r24
            r7.setTintColor(r9)
            r9 = r16
            r14 = r17
            r7.setOverrideTintColor(r9, r14)
            r23 = r1
            r17 = r9
            goto L_0x014c
        L_0x0136:
            r14 = r17
            r9 = r24
            goto L_0x0140
        L_0x013b:
            r14 = r17
            r9 = r24
            r12 = -1
        L_0x0140:
            r17 = r16
            r23 = r1
            int r1 = r7.mNotGoneIndex
            if (r1 != r12) goto L_0x014c
            r17 = r0
            r12 = r9
            goto L_0x0150
        L_0x014c:
            r12 = r17
            r17 = r14
        L_0x0150:
            if (r22 == 0) goto L_0x0167
            com.android.systemui.statusbar.NotificationShelfController r1 = r7.mController
            boolean r1 = r1.canModifyColorOfNotifications()
            if (r1 == 0) goto L_0x0167
            if (r18 != 0) goto L_0x015e
            r1 = r6
            goto L_0x0160
        L_0x015e:
            r1 = r18
        L_0x0160:
            r5.setOverrideTintColor(r1, r0)
            r9 = r1
            r0 = 0
            r1 = 0
            goto L_0x016d
        L_0x0167:
            r0 = 0
            r1 = 0
            r5.setOverrideTintColor(r1, r0)
            r9 = r6
        L_0x016d:
            if (r11 != 0) goto L_0x0171
            if (r10 != 0) goto L_0x0174
        L_0x0171:
            r5.setAboveShelf(r1)
        L_0x0174:
            if (r11 != 0) goto L_0x019f
            com.android.systemui.statusbar.notification.collection.NotificationEntry r10 = r5.getEntry()
            com.android.systemui.statusbar.notification.icon.IconPack r10 = r10.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r10 = r10.getShelfIcon()
            com.android.systemui.statusbar.phone.NotificationIconContainer$IconState r10 = r7.getIconState(r10)
            if (r10 == 0) goto L_0x019f
            float r10 = r10.clampedAppearAmount
            int r10 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r10 != 0) goto L_0x019f
            float r10 = r2.getTranslationY()
            float r14 = r32.getTranslationY()
            float r10 = r10 - r14
            int r10 = (int) r10
            float r5 = r5.getCurrentTopRoundness()
            r21 = r10
            goto L_0x01a1
        L_0x019f:
            r5 = r26
        L_0x01a1:
            int r11 = r11 + 1
            r18 = r9
            r16 = r12
            r12 = r6
            goto L_0x01b9
        L_0x01a9:
            r23 = r1
            r0 = r14
            r14 = r17
            r9 = r24
            r1 = 0
            r17 = r16
            r4 = r12
            r5 = r26
            r12 = r9
            r17 = r14
        L_0x01b9:
            boolean r6 = r2 instanceof com.android.systemui.statusbar.notification.row.ActivatableNotificationView
            if (r6 == 0) goto L_0x01d0
            com.android.systemui.statusbar.notification.row.ActivatableNotificationView r2 = (com.android.systemui.statusbar.notification.row.ActivatableNotificationView) r2
            com.android.systemui.statusbar.notification.stack.AmbientState r6 = r7.mAmbientState
            boolean r6 = r6.isOnKeyguard()
            if (r6 == 0) goto L_0x01ca
            r6 = r23
            goto L_0x01cb
        L_0x01ca:
            r6 = r8
        L_0x01cb:
            r9 = r27
            r7.updateCornerRoundnessOnScroll(r2, r9, r6)
        L_0x01d0:
            r2 = r4
            goto L_0x01f6
        L_0x01d2:
            r21 = r0
        L_0x01d4:
            r26 = r1
            r28 = r4
            r30 = r6
            r25 = r9
            r19 = r14
            r14 = r17
            r0 = 0
            r1 = 0
            r9 = r3
            r17 = r16
            r31 = r12
            r12 = r2
            r2 = r31
            r3 = r9
            r16 = r17
            r5 = r26
            r17 = r14
            r31 = r12
            r12 = r2
            r2 = r31
        L_0x01f6:
            int r4 = r28 + 1
            r1 = r5
            r14 = r19
            r0 = r21
            r9 = r25
            r6 = r30
            r10 = -1
            goto L_0x007c
        L_0x0204:
            r21 = r0
            r26 = r1
            r12 = r2
            r9 = r3
            r1 = 0
            r32.clipTransientViews()
            r7.setClipTopAmount(r9)
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r0 = r32.getViewState()
            boolean r0 = r0.hidden
            if (r0 != 0) goto L_0x022a
            int r0 = r32.getIntrinsicHeight()
            if (r9 >= r0) goto L_0x022a
            boolean r0 = r7.mShowNotificationShelf
            if (r0 == 0) goto L_0x022a
            int r0 = (r12 > r20 ? 1 : (r12 == r20 ? 0 : -1))
            if (r0 >= 0) goto L_0x0228
            goto L_0x022a
        L_0x0228:
            r0 = r1
            goto L_0x022b
        L_0x022a:
            r0 = 1
        L_0x022b:
            android.view.animation.Interpolator r2 = com.android.systemui.animation.Interpolators.STANDARD
            com.android.systemui.statusbar.notification.stack.AmbientState r3 = r7.mAmbientState
            float r3 = r3.getFractionToShade()
            float r2 = r2.getInterpolation(r3)
            com.android.systemui.statusbar.phone.NotificationIconContainer r3 = r7.mShelfIcons
            float r3 = r3.calculateWidthFor(r12)
            r7.updateActualWidth(r2, r3)
            if (r0 == 0) goto L_0x0244
            r2 = 4
            goto L_0x0245
        L_0x0244:
            r2 = r1
        L_0x0245:
            r7.setVisibility(r2)
            r12 = r21
            r7.setBackgroundTop(r12)
            r5 = r26
            r7.setFirstElementRoundness(r5)
            com.android.systemui.statusbar.phone.NotificationIconContainer r2 = r7.mShelfIcons
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r3 = r7.mHostLayoutController
            int r3 = r3.getSpeedBumpIndex()
            r2.setSpeedBumpIndex(r3)
            com.android.systemui.statusbar.phone.NotificationIconContainer r2 = r7.mShelfIcons
            r2.calculateIconXTranslations()
            com.android.systemui.statusbar.phone.NotificationIconContainer r2 = r7.mShelfIcons
            r2.applyIconStates()
            r12 = r1
        L_0x0268:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r1 = r7.mHostLayoutController
            int r1 = r1.getChildCount()
            if (r12 >= r1) goto L_0x0289
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r1 = r7.mHostLayoutController
            com.android.systemui.statusbar.notification.row.ExpandableView r1 = r1.getChildAt(r12)
            boolean r2 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r2 == 0) goto L_0x0286
            int r2 = r1.getVisibility()
            if (r2 != r10) goto L_0x0281
            goto L_0x0286
        L_0x0281:
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
            r7.updateContinuousClipping(r1)
        L_0x0286:
            int r12 = r12 + 1
            goto L_0x0268
        L_0x0289:
            r7.setHideBackground(r0)
            int r0 = r7.mNotGoneIndex
            r1 = -1
            if (r0 != r1) goto L_0x0293
            r7.mNotGoneIndex = r11
        L_0x0293:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.updateAppearance():void");
    }

    private void updateCornerRoundnessOnScroll(ActivatableNotificationView activatableNotificationView, float f, float f2) {
        boolean z = true;
        boolean z2 = !this.mAmbientState.isOnKeyguard() && !this.mAmbientState.isShadeExpanded() && (activatableNotificationView instanceof ExpandableNotificationRow) && ((ExpandableNotificationRow) activatableNotificationView).isHeadsUp();
        boolean z3 = this.mAmbientState.isShadeExpanded() && activatableNotificationView == this.mAmbientState.getTrackedHeadsUpRow();
        if (f >= f2 || this.mHostLayoutController.isViewAffectedBySwipe(activatableNotificationView) || z2 || z3 || activatableNotificationView.isAboveShelf() || this.mAmbientState.isPulsing() || this.mAmbientState.isDozing()) {
            z = false;
        }
        if (z) {
            float dimension = getResources().getDimension(C1894R.dimen.notification_corner_radius_small) / getResources().getDimension(C1894R.dimen.notification_corner_radius);
            float actualHeight = ((float) activatableNotificationView.getActualHeight()) + f;
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
                if (activatableNotificationView.isChildInGroup()) {
                    activatableNotificationView.setTopRoundness(0.0f, false);
                    return;
                }
                if (activatableNotificationView.isFirstInSection()) {
                    dimension = 1.0f;
                }
                activatableNotificationView.setTopRoundness(dimension, false);
            }
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

    /* access modifiers changed from: private */
    public void updateIconClipAmount(ExpandableNotificationRow expandableNotificationRow) {
        float translationY = expandableNotificationRow.getTranslationY();
        if (getClipTopAmount() != 0) {
            translationY = Math.max(translationY, getTranslationY() + ((float) getClipTopAmount()));
        }
        StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        float translationY2 = getTranslationY() + ((float) shelfIcon.getTop()) + shelfIcon.getTranslationY();
        if (translationY2 >= translationY || this.mAmbientState.isFullyHidden()) {
            shelfIcon.setClipBounds((Rect) null);
            return;
        }
        int i = (int) (translationY - translationY2);
        shelfIcon.setClipBounds(new Rect(0, i, shelfIcon.getWidth(), Math.max(i, shelfIcon.getHeight())));
    }

    private void updateContinuousClipping(final ExpandableNotificationRow expandableNotificationRow) {
        final StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        boolean z = true;
        boolean z2 = ViewState.isAnimatingY(shelfIcon) && !this.mAmbientState.isDozing();
        if (shelfIcon.getTag(C1894R.C1898id.continuous_clipping_tag) == null) {
            z = false;
        }
        if (z2 && !z) {
            final ViewTreeObserver viewTreeObserver = shelfIcon.getViewTreeObserver();
            final C25901 r2 = new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (!ViewState.isAnimatingY(shelfIcon)) {
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.removeOnPreDrawListener(this);
                        }
                        shelfIcon.setTag(C1894R.C1898id.continuous_clipping_tag, (Object) null);
                        return true;
                    }
                    NotificationShelf.this.updateIconClipAmount(expandableNotificationRow);
                    return true;
                }
            };
            viewTreeObserver.addOnPreDrawListener(r2);
            shelfIcon.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View view) {
                }

                public void onViewDetachedFromWindow(View view) {
                    if (view == shelfIcon) {
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.removeOnPreDrawListener(r2);
                        }
                        shelfIcon.setTag(C1894R.C1898id.continuous_clipping_tag, (Object) null);
                    }
                }
            });
            shelfIcon.setTag(C1894R.C1898id.continuous_clipping_tag, r2);
        }
    }

    private int updateNotificationClipHeight(ExpandableView expandableView, float f, int i) {
        float translationY = expandableView.getTranslationY() + ((float) expandableView.getActualHeight());
        boolean z = true;
        boolean z2 = (expandableView.isPinned() || expandableView.isHeadsUpAnimatingAway()) && !this.mAmbientState.isDozingAndNotPulsing(expandableView);
        if (!this.mAmbientState.isPulseExpanding()) {
            z = expandableView.showingPulsing();
        } else if (i != 0) {
            z = false;
        }
        if (!z2) {
            if (translationY <= f || z) {
                expandableView.setClipBottomAmount(0);
            } else {
                expandableView.setClipBottomAmount((int) (translationY - f));
            }
        }
        if (z) {
            return (int) (translationY - getTranslationY());
        }
        return 0;
    }

    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        if (!this.mHasItemsInStableShelf) {
            f = 0.0f;
        }
        super.setFakeShadowIntensity(f, f2, i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0056, code lost:
        if (r11 >= r1) goto L_0x0058;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float getAmountInShelf(int r11, com.android.systemui.statusbar.notification.row.ExpandableView r12, boolean r13, boolean r14, boolean r15, float r16) {
        /*
            r10 = this;
            r0 = r10
            float r1 = r12.getTranslationY()
            int r2 = r12.getActualHeight()
            int r3 = r0.mPaddingBetweenElements
            int r2 = r2 + r3
            r3 = r12
            float r4 = r10.calculateIconTransformationStart(r12)
            float r5 = (float) r2
            float r5 = r5 + r1
            float r5 = r5 - r4
            int r6 = r10.getIntrinsicHeight()
            float r6 = (float) r6
            float r5 = java.lang.Math.min((float) r5, (float) r6)
            if (r15 == 0) goto L_0x003a
            int r6 = r12.getMinHeight()
            int r7 = r10.getIntrinsicHeight()
            int r6 = r6 - r7
            int r2 = java.lang.Math.min((int) r2, (int) r6)
            int r6 = r12.getMinHeight()
            int r7 = r10.getIntrinsicHeight()
            int r6 = r6 - r7
            float r6 = (float) r6
            float r5 = java.lang.Math.min((float) r5, (float) r6)
        L_0x003a:
            float r2 = (float) r2
            float r6 = r1 + r2
            com.android.systemui.statusbar.notification.stack.AmbientState r7 = r0.mAmbientState
            boolean r7 = r7.isExpansionChanging()
            r8 = 0
            r9 = 1065353216(0x3f800000, float:1.0)
            if (r7 == 0) goto L_0x005b
            com.android.systemui.statusbar.notification.stack.AmbientState r7 = r0.mAmbientState
            boolean r7 = r7.isOnKeyguard()
            if (r7 != 0) goto L_0x005b
            int r1 = r0.mIndexOfFirstViewInShelf
            r2 = -1
            if (r1 == r2) goto L_0x00ad
            r2 = r11
            if (r2 < r1) goto L_0x00ad
        L_0x0058:
            r2 = r9
            r8 = r2
            goto L_0x00ae
        L_0x005b:
            int r6 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r6 < 0) goto L_0x00ad
            com.android.systemui.statusbar.notification.stack.AmbientState r6 = r0.mAmbientState
            boolean r6 = r6.isUnlockHintRunning()
            if (r6 == 0) goto L_0x006d
            boolean r6 = r12.isInShelf()
            if (r6 == 0) goto L_0x00ad
        L_0x006d:
            com.android.systemui.statusbar.notification.stack.AmbientState r6 = r0.mAmbientState
            boolean r6 = r6.isShadeExpanded()
            if (r6 != 0) goto L_0x0081
            boolean r6 = r12.isPinned()
            if (r6 != 0) goto L_0x00ad
            boolean r6 = r12.isHeadsUpAnimatingAway()
            if (r6 != 0) goto L_0x00ad
        L_0x0081:
            int r6 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r6 >= 0) goto L_0x0058
            float r6 = r1 - r16
            float r6 = java.lang.Math.abs((float) r6)
            r7 = 981668463(0x3a83126f, float:0.001)
            int r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1))
            if (r6 <= 0) goto L_0x0058
            float r6 = r16 - r1
            float r2 = r6 / r2
            float r2 = java.lang.Math.min((float) r9, (float) r2)
            float r2 = r9 - r2
            if (r15 == 0) goto L_0x00a1
            float r4 = r4 - r1
            float r6 = r6 / r4
            goto L_0x00a5
        L_0x00a1:
            float r1 = r16 - r4
            float r6 = r1 / r5
        L_0x00a5:
            float r1 = android.util.MathUtils.constrain(r6, r8, r9)
            float r9 = r9 - r1
            r8 = r2
            r2 = r9
            goto L_0x00ae
        L_0x00ad:
            r2 = r8
        L_0x00ae:
            r0 = r10
            r1 = r12
            r3 = r13
            r4 = r14
            r5 = r15
            r0.updateIconPositioning(r1, r2, r3, r4, r5)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.getAmountInShelf(int, com.android.systemui.statusbar.notification.row.ExpandableView, boolean, boolean, boolean, float):float");
    }

    private float calculateIconTransformationStart(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        if (shelfTransformationTarget == null) {
            return expandableView.getTranslationY();
        }
        return (expandableView.getTranslationY() + ((float) expandableView.getRelativeTopPadding(shelfTransformationTarget))) - ((float) expandableView.getShelfIcon().getTop());
    }

    private void updateIconPositioning(ExpandableView expandableView, float f, boolean z, boolean z2, boolean z3) {
        StatusBarIconView shelfIcon = expandableView.getShelfIcon();
        NotificationIconContainer.IconState iconState = getIconState(shelfIcon);
        if (iconState != null) {
            boolean z4 = false;
            float f2 = (f > 0.5f ? 1 : (f == 0.5f ? 0 : -1)) > 0 || isTargetClipped(expandableView) ? 1.0f : 0.0f;
            if (f == f2) {
                iconState.noAnimations = (z || z2) && !z3;
            }
            if (!z3 && (z || (z2 && !ViewState.isAnimatingY(shelfIcon)))) {
                iconState.cancelAnimations(shelfIcon);
                iconState.noAnimations = true;
            }
            if (!this.mAmbientState.isHiddenAtAll() || expandableView.isInShelf()) {
                if (iconState.clampedAppearAmount != f2) {
                    z4 = true;
                }
                iconState.needsCannedAnimation = z4;
            } else {
                f = this.mAmbientState.isFullyHidden() ? 1.0f : 0.0f;
            }
            iconState.clampedAppearAmount = f2;
            setIconTransformationAmount(expandableView, f);
        }
    }

    private boolean isTargetClipped(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        if (shelfTransformationTarget != null && expandableView.getTranslationY() + expandableView.getContentTranslation() + ((float) expandableView.getRelativeTopPadding(shelfTransformationTarget)) + ((float) shelfTransformationTarget.getHeight()) >= getTranslationY() - ((float) this.mPaddingBetweenElements)) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0005, code lost:
        r8 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setIconTransformationAmount(com.android.systemui.statusbar.notification.row.ExpandableView r8, float r9) {
        /*
            r7 = this;
            boolean r0 = r8 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r8 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r8
            com.android.systemui.statusbar.StatusBarIconView r1 = r8.getShelfIcon()
            com.android.systemui.statusbar.phone.NotificationIconContainer$IconState r2 = r7.getIconState(r1)
            if (r2 != 0) goto L_0x0012
            return
        L_0x0012:
            android.view.animation.Interpolator r3 = ICON_ALPHA_INTERPOLATOR
            float r3 = r3.getInterpolation(r9)
            r2.alpha = r3
            boolean r3 = r8.isDrawingAppearAnimation()
            r4 = 1
            r5 = 0
            if (r3 == 0) goto L_0x002a
            boolean r3 = r8.isInShelf()
            if (r3 != 0) goto L_0x002a
            r3 = r4
            goto L_0x002b
        L_0x002a:
            r3 = r5
        L_0x002b:
            r6 = 0
            if (r3 != 0) goto L_0x0066
            if (r0 == 0) goto L_0x003e
            boolean r0 = r8.isLowPriority()
            if (r0 == 0) goto L_0x003e
            com.android.systemui.statusbar.phone.NotificationIconContainer r0 = r7.mShelfIcons
            boolean r0 = r0.hasMaxNumDot()
            if (r0 != 0) goto L_0x0066
        L_0x003e:
            int r0 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0048
            boolean r0 = r2.isAnimating(r1)
            if (r0 == 0) goto L_0x0066
        L_0x0048:
            boolean r0 = r8.isAboveShelf()
            if (r0 != 0) goto L_0x0066
            boolean r0 = r8.showingPulsing()
            if (r0 != 0) goto L_0x0066
            float r0 = r8.getTranslationZ()
            com.android.systemui.statusbar.notification.stack.AmbientState r3 = r7.mAmbientState
            int r3 = r3.getBaseZHeight()
            float r3 = (float) r3
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0064
            goto L_0x0066
        L_0x0064:
            r0 = r5
            goto L_0x0067
        L_0x0066:
            r0 = r4
        L_0x0067:
            r2.hidden = r0
            boolean r0 = r2.hidden
            if (r0 == 0) goto L_0x006e
            r9 = r6
        L_0x006e:
            r2.iconAppearAmount = r9
            com.android.systemui.statusbar.phone.NotificationIconContainer r9 = r7.mShelfIcons
            float r9 = r9.getActualPaddingStart()
            r2.xTranslation = r9
            boolean r9 = r8.isInShelf()
            if (r9 == 0) goto L_0x0085
            boolean r9 = r8.isTransformingIntoShelf()
            if (r9 != 0) goto L_0x0085
            goto L_0x0086
        L_0x0085:
            r4 = r5
        L_0x0086:
            if (r4 == 0) goto L_0x0090
            r9 = 1065353216(0x3f800000, float:1.0)
            r2.iconAppearAmount = r9
            r2.alpha = r9
            r2.hidden = r5
        L_0x0090:
            int r7 = r7.getBackgroundColorWithoutTint()
            int r7 = r1.getContrastedStaticDrawableColor(r7)
            boolean r9 = r8.isShowingIcon()
            if (r9 == 0) goto L_0x00aa
            if (r7 == 0) goto L_0x00aa
            int r8 = r8.getOriginalIconColor()
            float r9 = r2.iconAppearAmount
            int r7 = com.android.systemui.statusbar.notification.NotificationUtils.interpolateColors(r8, r7, r9)
        L_0x00aa:
            r2.iconColor = r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.setIconTransformationAmount(com.android.systemui.statusbar.notification.row.ExpandableView, float):void");
    }

    private NotificationIconContainer.IconState getIconState(StatusBarIconView statusBarIconView) {
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        if (notificationIconContainer == null) {
            return null;
        }
        return notificationIconContainer.getIconState(statusBarIconView);
    }

    private float getFullyClosedTranslation() {
        return (float) ((-(getIntrinsicHeight() - this.mStatusBarHeight)) / 2);
    }

    private void setHideBackground(boolean z) {
        if (this.mHideBackground != z) {
            this.mHideBackground = z;
            updateOutline();
        }
    }

    /* access modifiers changed from: protected */
    public boolean needsOutline() {
        return !this.mHideBackground && super.needsOutline();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateRelativeOffset();
        int i5 = getResources().getDisplayMetrics().heightPixels;
        this.mClipRect.set(0, -i5, getWidth(), i5);
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        if (notificationIconContainer != null) {
            notificationIconContainer.setClipBounds(this.mClipRect);
        }
    }

    private void updateRelativeOffset() {
        NotificationIconContainer notificationIconContainer = this.mCollapsedIcons;
        if (notificationIconContainer != null) {
            notificationIconContainer.getLocationOnScreen(this.mTmp);
        }
        getLocationOnScreen(this.mTmp);
    }

    public int getNotGoneIndex() {
        return this.mNotGoneIndex;
    }

    /* access modifiers changed from: private */
    public void setHasItemsInStableShelf(boolean z) {
        if (this.mHasItemsInStableShelf != z) {
            this.mHasItemsInStableShelf = z;
            updateInteractiveness();
        }
    }

    public boolean hasItemsInStableShelf() {
        return this.mHasItemsInStableShelf;
    }

    public void setCollapsedIcons(NotificationIconContainer notificationIconContainer) {
        this.mCollapsedIcons = notificationIconContainer;
        notificationIconContainer.addOnLayoutChangeListener(this);
    }

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

    /* access modifiers changed from: protected */
    public boolean isInteractive() {
        return this.mInteractive;
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
        if (!z) {
            this.mShelfIcons.setAnimationsEnabled(false);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mInteractive) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, getContext().getString(C1894R.string.accessibility_overflow_action)));
        }
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateRelativeOffset();
    }

    public void setController(NotificationShelfController notificationShelfController) {
        this.mController = notificationShelfController;
    }

    public void setIndexOfFirstViewInShelf(ExpandableView expandableView) {
        this.mIndexOfFirstViewInShelf = this.mHostLayoutController.indexOfChild(expandableView);
    }

    public class ShelfState extends ExpandableViewState {
        /* access modifiers changed from: private */
        public ExpandableView firstViewInShelf;
        /* access modifiers changed from: private */
        public boolean hasItemsInStableShelf;

        public ShelfState() {
        }

        public void applyToView(View view) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.applyToView(view);
                NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
                NotificationShelf.this.updateAppearance();
                NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
                NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
            }
        }

        public void animateTo(View view, AnimationProperties animationProperties) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.animateTo(view, animationProperties);
                NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
                NotificationShelf.this.updateAppearance();
                NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
                NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
            }
        }
    }
}
