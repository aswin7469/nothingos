package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.EmptyShadeView;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.animation.NTShadeInterpolation;
import com.nothing.systemui.statusbar.notification.stack.AmbientStateEx;
import com.nothing.utils.NTSystemUIUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StackScrollAlgorithm {
    private static final Boolean DEBUG = false;
    public static final float START_FRACTION = 0.5f;
    private static final String TAG = "StackScrollAlgorithm";
    private boolean mClipNotificationScrollToTop;
    private int mCollapsedSize;
    private float mGapHeight;
    private float mGapHeightOnLockscreen;
    float mHeadsUpInset;
    private final ViewGroup mHostView;
    private boolean mIsExpanded;
    private int mMarginBottom;
    private float mNotificationScrimPadding;
    private float mPaddingBetweenElements;
    private int mPinnedZTranslationExtra;
    private StackScrollAlgorithmState mTempAlgorithmState = new StackScrollAlgorithmState();

    public interface BypassController {
        boolean isBypassEnabled();
    }

    public interface SectionProvider {
        boolean beginsSection(View view, View view2);
    }

    public StackScrollAlgorithm(Context context, ViewGroup viewGroup) {
        this.mHostView = viewGroup;
        initView(context);
    }

    public void initView(Context context) {
        initConstants(context);
    }

    private void initConstants(Context context) {
        Resources resources = context.getResources();
        this.mPaddingBetweenElements = (float) resources.getDimensionPixelSize(C1894R.dimen.notification_divider_height);
        this.mCollapsedSize = resources.getDimensionPixelSize(C1894R.dimen.notification_min_height);
        this.mClipNotificationScrollToTop = resources.getBoolean(C1894R.bool.config_clipNotificationScrollToTop);
        this.mHeadsUpInset = (float) (SystemBarUtils.getStatusBarHeight(context) + resources.getDimensionPixelSize(C1894R.dimen.heads_up_status_bar_padding));
        this.mPinnedZTranslationExtra = resources.getDimensionPixelSize(C1894R.dimen.heads_up_pinned_elevation);
        this.mGapHeight = (float) resources.getDimensionPixelSize(C1894R.dimen.notification_section_divider_height);
        this.mGapHeightOnLockscreen = (float) resources.getDimensionPixelSize(C1894R.dimen.notification_section_divider_height_lockscreen);
        this.mNotificationScrimPadding = (float) resources.getDimensionPixelSize(C1894R.dimen.notification_side_paddings);
        this.mMarginBottom = resources.getDimensionPixelSize(C1894R.dimen.notification_panel_margin_bottom);
    }

    public void resetViewStates(AmbientState ambientState, int i) {
        StackScrollAlgorithmState stackScrollAlgorithmState = this.mTempAlgorithmState;
        resetChildViewStates();
        initAlgorithmState(stackScrollAlgorithmState, ambientState);
        updatePositionsForState(stackScrollAlgorithmState, ambientState);
        updateZValuesForState(stackScrollAlgorithmState, ambientState);
        updateHeadsUpStates(stackScrollAlgorithmState, ambientState);
        updatePulsingStates(stackScrollAlgorithmState, ambientState);
        updateDimmedActivatedHideSensitive(ambientState, stackScrollAlgorithmState);
        updateClipping(stackScrollAlgorithmState, ambientState);
        updateSpeedBumpState(stackScrollAlgorithmState, i);
        updateShelfState(stackScrollAlgorithmState, ambientState);
        getNotificationChildrenStates(stackScrollAlgorithmState, ambientState);
    }

    public float getNotificationSquishinessFraction(AmbientState ambientState) {
        return getExpansionFractionWithoutShelf(this.mTempAlgorithmState, ambientState);
    }

    public static void log(String str) {
        if (DEBUG.booleanValue()) {
            Log.i(TAG, str);
        }
    }

    public static void logView(View view, String str) {
        String str2;
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            str2 = expandableNotificationRow.getEntry() == null ? "ExpandableNotificationRow has null NotificationEntry" : expandableNotificationRow.getEntry().getSbn().getId() + "";
        } else if (view == null) {
            str2 = "View is null";
        } else if (view instanceof SectionHeaderView) {
            str2 = "SectionHeaderView";
        } else if (view instanceof FooterView) {
            str2 = "FooterView";
        } else if (view instanceof MediaContainerView) {
            str2 = "MediaContainerView";
        } else if (view instanceof EmptyShadeView) {
            str2 = "EmptyShadeView";
        } else {
            str2 = view.toString();
        }
        log(str2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + str);
    }

    private void resetChildViewStates() {
        int childCount = this.mHostView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((ExpandableView) this.mHostView.getChildAt(i)).resetViewState();
        }
    }

    private void getNotificationChildrenStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) expandableView).updateChildrenStates(ambientState);
            }
        }
    }

    private void updateSpeedBumpState(StackScrollAlgorithmState stackScrollAlgorithmState, int i) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        int i2 = 0;
        while (i2 < size) {
            stackScrollAlgorithmState.visibleChildren.get(i2).getViewState().belowSpeedBump = i2 >= i;
            i2++;
        }
    }

    private void updateShelfState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        NotificationShelf shelf = ambientState.getShelf();
        if (shelf != null) {
            shelf.updateState(stackScrollAlgorithmState, ambientState);
            if (!shelf.getViewState().hidden) {
                float f = shelf.getViewState().yTranslation;
                Iterator<ExpandableView> it = stackScrollAlgorithmState.visibleChildren.iterator();
                while (it.hasNext()) {
                    ExpandableView next = it.next();
                    if (next.getViewState().yTranslation >= f) {
                        next.getViewState().alpha = 0.0f;
                    }
                }
            }
        }
    }

    private void updateClipping(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        float f;
        boolean z;
        StackScrollAlgorithm stackScrollAlgorithm;
        StackScrollAlgorithmState stackScrollAlgorithmState2 = stackScrollAlgorithmState;
        float f2 = 0.0f;
        if (ambientState.isOnKeyguard()) {
            f = 0.0f;
        } else {
            f = ambientState.getStackY() - ((float) ambientState.getScrollY());
        }
        int size = stackScrollAlgorithmState2.visibleChildren.size();
        float f3 = 0.0f;
        boolean z2 = true;
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState2.visibleChildren.get(i);
            ExpandableViewState viewState = expandableView.getViewState();
            if (!expandableView.mustStayOnScreen() || viewState.headsUpIsVisible) {
                f2 = Math.max(f, f2);
            }
            float f4 = viewState.yTranslation;
            float f5 = ((float) viewState.height) + f4;
            if (!(expandableView instanceof ExpandableNotificationRow) || !expandableView.isPinned()) {
                stackScrollAlgorithm = this;
                z = false;
            } else {
                stackScrollAlgorithm = this;
                z = true;
            }
            if (!stackScrollAlgorithm.mClipNotificationScrollToTop || (((!z || z2) && !expandableView.isHeadsUpAnimatingAway()) || f5 <= f3 || ambientState.isShadeExpanded())) {
                viewState.clipBottomAmount = 0;
            } else {
                viewState.clipBottomAmount = (int) (f5 - f3);
            }
            if (z2) {
                f3 = f5;
            }
            if (z) {
                z2 = false;
            }
            if (!expandableView.isTransparent()) {
                if (!z) {
                    f4 = f5;
                }
                f2 = Math.max(f2, f4);
            }
        }
    }

    private void updateDimmedActivatedHideSensitive(AmbientState ambientState, StackScrollAlgorithmState stackScrollAlgorithmState) {
        boolean isDimmed = ambientState.isDimmed();
        boolean isHideSensitive = ambientState.isHideSensitive();
        ActivatableNotificationView activatedChild = ambientState.getActivatedChild();
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            ExpandableViewState viewState = expandableView.getViewState();
            viewState.dimmed = isDimmed;
            viewState.hideSensitive = isHideSensitive;
            boolean z = activatedChild == expandableView;
            if (isDimmed && z) {
                viewState.zTranslation += ((float) ambientState.getZDistanceBetweenElements()) * 2.0f;
            }
        }
    }

    private void initAlgorithmState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        stackScrollAlgorithmState.scrollY = ambientState.getScrollY();
        float unused = stackScrollAlgorithmState.mCurrentYPosition = (float) (-stackScrollAlgorithmState.scrollY);
        float unused2 = stackScrollAlgorithmState.mCurrentExpandedYPosition = (float) (-stackScrollAlgorithmState.scrollY);
        int childCount = this.mHostView.getChildCount();
        stackScrollAlgorithmState.visibleChildren.clear();
        stackScrollAlgorithmState.visibleChildren.ensureCapacity(childCount);
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) this.mHostView.getChildAt(i2);
            if (!(expandableView.getVisibility() == 8 || expandableView == ambientState.getShelf())) {
                i = updateNotGoneIndex(stackScrollAlgorithmState, i, expandableView);
                if (expandableView instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                    List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                    if (expandableNotificationRow.isSummaryWithChildren() && attachedChildren != null) {
                        for (ExpandableNotificationRow next : attachedChildren) {
                            if (next.getVisibility() != 8) {
                                next.getViewState().notGoneIndex = i;
                                i++;
                            }
                        }
                    }
                }
            }
        }
        float f = (float) (-ambientState.getScrollY());
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            f += this.mNotificationScrimPadding;
        }
        stackScrollAlgorithmState.firstViewInShelf = null;
        for (int i3 = 0; i3 < stackScrollAlgorithmState.visibleChildren.size(); i3++) {
            ExpandableView expandableView2 = stackScrollAlgorithmState.visibleChildren.get(i3);
            if (childNeedsGapHeight(ambientState.getSectionProvider(), i3, expandableView2, getPreviousView(i3, stackScrollAlgorithmState))) {
                f += getGapForLocation(ambientState.getFractionToShade(), ambientState.isOnKeyguard());
            }
            if (ambientState.getShelf() != null && f >= (ambientState.getStackEndHeight() - ((float) ambientState.getShelf().getIntrinsicHeight())) - this.mPaddingBetweenElements && !(expandableView2 instanceof FooterView) && stackScrollAlgorithmState.firstViewInShelf == null) {
                stackScrollAlgorithmState.firstViewInShelf = expandableView2;
            }
            f = f + ((float) getMaxAllowedChildHeight(expandableView2)) + this.mPaddingBetweenElements;
        }
    }

    private int updateNotGoneIndex(StackScrollAlgorithmState stackScrollAlgorithmState, int i, ExpandableView expandableView) {
        expandableView.getViewState().notGoneIndex = i;
        stackScrollAlgorithmState.visibleChildren.add(expandableView);
        return i + 1;
    }

    private ExpandableView getPreviousView(int i, StackScrollAlgorithmState stackScrollAlgorithmState) {
        if (i > 0) {
            return stackScrollAlgorithmState.visibleChildren.get(i - 1);
        }
        return null;
    }

    private void updatePositionsForState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, this.mNotificationScrimPadding);
            StackScrollAlgorithmState.access$116(stackScrollAlgorithmState, this.mNotificationScrimPadding);
        }
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            updateChild(i, stackScrollAlgorithmState, ambientState);
        }
    }

    private void setLocation(ExpandableViewState expandableViewState, float f, int i) {
        expandableViewState.location = 4;
        if (f <= 0.0f) {
            expandableViewState.location = 2;
        }
    }

    private float getExpansionFractionWithoutShelf(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        float f;
        float intrinsicHeight = ambientState.getShelf() != null && stackScrollAlgorithmState.firstViewInShelf != null ? (float) ambientState.getShelf().getIntrinsicHeight() : 0.0f;
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            f = this.mNotificationScrimPadding;
        } else {
            f = 0.0f;
        }
        float stackHeight = (ambientState.getStackHeight() - intrinsicHeight) - f;
        float stackEndHeight = (ambientState.getStackEndHeight() - intrinsicHeight) - f;
        if (stackEndHeight == 0.0f) {
            return 0.0f;
        }
        return stackHeight / stackEndHeight;
    }

    public boolean hasOngoingNotifs(StackScrollAlgorithmState stackScrollAlgorithmState) {
        for (int i = 0; i < stackScrollAlgorithmState.visibleChildren.size(); i++) {
            View view = stackScrollAlgorithmState.visibleChildren.get(i);
            if ((view instanceof ExpandableNotificationRow) && !((ExpandableNotificationRow) view).canViewBeDismissed()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void updateChild(int i, StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        float f;
        ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
        ExpandableViewState viewState = expandableView.getViewState();
        boolean z = false;
        viewState.location = 0;
        if (!(ambientState.isShadeExpanded() && expandableView == ambientState.getTrackedHeadsUpRow())) {
            if (ambientState.isOnKeyguard()) {
                viewState.alpha = 1.0f - ambientState.getHideAmount();
            } else if (ambientState.isExpansionChanging()) {
                viewState.alpha = NTShadeInterpolation.getNotificationScrimAlpha(((AmbientStateEx) NTDependencyEx.get(AmbientStateEx.class)).getAlphaFraction(), true, NTSystemUIUtils.getInstance().isSplitShadeEnabled());
            }
        }
        if (ambientState.isShadeExpanded() && expandableView.mustStayOnScreen() && viewState.yTranslation >= 0.0f) {
            viewState.headsUpIsVisible = (viewState.yTranslation + ((float) viewState.height)) + ambientState.getStackY() < ambientState.getMaxHeadsUpTranslation();
        }
        float expansionFractionWithoutShelf = getExpansionFractionWithoutShelf(stackScrollAlgorithmState, ambientState);
        if (childNeedsGapHeight(ambientState.getSectionProvider(), i, expandableView, getPreviousView(i, stackScrollAlgorithmState))) {
            float gapForLocation = getGapForLocation(ambientState.getFractionToShade(), ambientState.isOnKeyguard());
            StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, expansionFractionWithoutShelf * gapForLocation);
            StackScrollAlgorithmState.access$116(stackScrollAlgorithmState, gapForLocation);
        }
        viewState.yTranslation = stackScrollAlgorithmState.mCurrentYPosition;
        if (expandableView instanceof FooterView) {
            boolean z2 = !ambientState.isShadeExpanded();
            boolean z3 = stackScrollAlgorithmState.firstViewInShelf != null;
            if (z2) {
                viewState.hidden = true;
            } else {
                boolean z4 = stackScrollAlgorithmState.mCurrentExpandedYPosition + ((float) expandableView.getIntrinsicHeight()) > ambientState.getStackEndHeight();
                FooterView.FooterViewState footerViewState = (FooterView.FooterViewState) viewState;
                if (z3 || z4 || (ambientState.isClearAllInProgress() && !hasOngoingNotifs(stackScrollAlgorithmState))) {
                    z = true;
                }
                footerViewState.hideContent = z;
            }
        } else {
            if (expandableView instanceof EmptyShadeView) {
                viewState.yTranslation = ((((float) (ambientState.getLayoutMaxHeight() + this.mMarginBottom)) - ambientState.getStackY()) - ((float) getMaxAllowedChildHeight(expandableView))) / 2.0f;
            } else if (expandableView != ambientState.getTrackedHeadsUpRow()) {
                if (ambientState.isExpansionChanging()) {
                    viewState.hidden = false;
                    if (stackScrollAlgorithmState.firstViewInShelf != null && i >= stackScrollAlgorithmState.visibleChildren.indexOf(stackScrollAlgorithmState.firstViewInShelf)) {
                        z = true;
                    }
                    viewState.inShelf = z;
                } else if (ambientState.getShelf() != null) {
                    if (ambientState.isBypassEnabled() && ambientState.isOnKeyguard() && !ambientState.isPulseExpanding()) {
                        z = true;
                    }
                    if (!ambientState.isShadeExpanded() || ambientState.getDozeAmount() == 1.0f || z) {
                        f = (float) ambientState.getInnerHeight();
                    } else {
                        f = ambientState.getStackHeight();
                    }
                    updateViewWithShelf(expandableView, viewState, (f - ((float) ambientState.getShelf().getIntrinsicHeight())) - this.mPaddingBetweenElements);
                }
            }
            viewState.height = (int) (((float) getMaxAllowedChildHeight(expandableView)) * expansionFractionWithoutShelf);
        }
        StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, expansionFractionWithoutShelf * (((float) getMaxAllowedChildHeight(expandableView)) + this.mPaddingBetweenElements));
        StackScrollAlgorithmState.access$116(stackScrollAlgorithmState, ((float) expandableView.getIntrinsicHeight()) + this.mPaddingBetweenElements);
        setLocation(expandableView.getViewState(), stackScrollAlgorithmState.mCurrentYPosition, i);
        viewState.yTranslation += ambientState.getStackY();
    }

    /* access modifiers changed from: package-private */
    public void updateViewWithShelf(ExpandableView expandableView, ExpandableViewState expandableViewState, float f) {
        expandableViewState.yTranslation = Math.min(expandableViewState.yTranslation, f);
        if (expandableViewState.yTranslation >= f) {
            expandableViewState.hidden = !expandableView.isExpandAnimationRunning() && !expandableView.hasExpandingChild();
            expandableViewState.inShelf = true;
            expandableViewState.headsUpIsVisible = false;
        }
    }

    public float getGapHeightForChild(SectionProvider sectionProvider, int i, View view, View view2, float f, boolean z) {
        if (childNeedsGapHeight(sectionProvider, i, view, view2)) {
            return getGapForLocation(f, z);
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getGapForLocation(float f, boolean z) {
        if (f > 0.0f) {
            return MathUtils.lerp(this.mGapHeightOnLockscreen, this.mGapHeight, f);
        }
        if (z) {
            return this.mGapHeightOnLockscreen;
        }
        return this.mGapHeight;
    }

    private boolean childNeedsGapHeight(SectionProvider sectionProvider, int i, View view, View view2) {
        return sectionProvider.beginsSection(view, view2) && i > 0 && !(view2 instanceof SectionHeaderView) && !(view instanceof FooterView);
    }

    private void updatePulsingStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            View view = stackScrollAlgorithmState.visibleChildren.get(i);
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.showingPulsing() && (i != 0 || !ambientState.isPulseExpanding())) {
                    expandableNotificationRow.getViewState().hidden = false;
                }
            }
        }
    }

    private void updateHeadsUpStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        ExpandableViewState expandableViewState;
        ExpandableViewState viewState;
        int size = stackScrollAlgorithmState.visibleChildren.size();
        float stackTopMargin = this.mHeadsUpInset - ((float) ambientState.getStackTopMargin());
        ExpandableNotificationRow trackedHeadsUpRow = ambientState.getTrackedHeadsUpRow();
        if (!(trackedHeadsUpRow == null || (viewState = trackedHeadsUpRow.getViewState()) == null)) {
            viewState.yTranslation = MathUtils.lerp(stackTopMargin, viewState.yTranslation - ambientState.getStackTranslation(), ambientState.getAppearFraction());
        }
        ExpandableNotificationRow expandableNotificationRow = null;
        for (int i = 0; i < size; i++) {
            View view = stackScrollAlgorithmState.visibleChildren.get(i);
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) view;
                if (expandableNotificationRow2.isHeadsUp() || expandableNotificationRow2.isHeadsUpAnimatingAway()) {
                    ExpandableViewState viewState2 = expandableNotificationRow2.getViewState();
                    boolean z = true;
                    if (expandableNotificationRow == null && expandableNotificationRow2.mustStayOnScreen() && !viewState2.headsUpIsVisible) {
                        viewState2.location = 1;
                        expandableNotificationRow = expandableNotificationRow2;
                    }
                    if (expandableNotificationRow != expandableNotificationRow2) {
                        z = false;
                    }
                    float f = viewState2.yTranslation + ((float) viewState2.height);
                    if (this.mIsExpanded && expandableNotificationRow2.mustStayOnScreen() && !viewState2.headsUpIsVisible && !expandableNotificationRow2.showingPulsing()) {
                        clampHunToTop(ambientState, expandableNotificationRow2, viewState2);
                        if (z && expandableNotificationRow2.isAboveShelf()) {
                            clampHunToMaxTranslation(ambientState, expandableNotificationRow2, viewState2);
                            viewState2.hidden = false;
                        }
                    }
                    if (expandableNotificationRow2.isPinned()) {
                        viewState2.yTranslation = Math.max(viewState2.yTranslation, stackTopMargin);
                        viewState2.height = Math.max(expandableNotificationRow2.getIntrinsicHeight(), viewState2.height);
                        viewState2.hidden = false;
                        if (expandableNotificationRow == null) {
                            expandableViewState = null;
                        } else {
                            expandableViewState = expandableNotificationRow.getViewState();
                        }
                        if (expandableViewState != null && !z && (!this.mIsExpanded || f > expandableViewState.yTranslation + ((float) expandableViewState.height))) {
                            viewState2.height = expandableNotificationRow2.getIntrinsicHeight();
                        }
                        if (!this.mIsExpanded && z && ambientState.getScrollY() > 0) {
                            viewState2.yTranslation -= (float) ambientState.getScrollY();
                        }
                        viewState2.zTranslation = (float) (ambientState.getBaseZHeight() + stackScrollAlgorithmState.visibleChildren.size());
                    }
                    if (expandableNotificationRow2.isHeadsUpAnimatingAway()) {
                        viewState2.yTranslation = Math.max(viewState2.yTranslation, this.mHeadsUpInset);
                        viewState2.hidden = false;
                    }
                }
            }
        }
    }

    private void clampHunToTop(AmbientState ambientState, ExpandableNotificationRow expandableNotificationRow, ExpandableViewState expandableViewState) {
        float max = Math.max(ambientState.getTopPadding() + ambientState.getStackTranslation(), expandableViewState.yTranslation);
        expandableViewState.height = (int) Math.max(((float) expandableViewState.height) - (max - expandableViewState.yTranslation), (float) expandableNotificationRow.getCollapsedHeight());
        expandableViewState.yTranslation = max;
    }

    private void clampHunToMaxTranslation(AmbientState ambientState, ExpandableNotificationRow expandableNotificationRow, ExpandableViewState expandableViewState) {
        float min = Math.min(ambientState.getMaxHeadsUpTranslation(), ((float) ambientState.getInnerHeight()) + ambientState.getTopPadding() + ambientState.getStackTranslation());
        float min2 = Math.min(expandableViewState.yTranslation, min - ((float) expandableNotificationRow.getCollapsedHeight()));
        expandableViewState.height = (int) Math.min((float) expandableViewState.height, min - min2);
        expandableViewState.yTranslation = min2;
    }

    /* access modifiers changed from: protected */
    public int getMaxAllowedChildHeight(View view) {
        if (view instanceof ExpandableView) {
            return ((ExpandableView) view).getIntrinsicHeight();
        }
        return view == null ? this.mCollapsedSize : view.getHeight();
    }

    private void updateZValuesForState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                i = -1;
                break;
            }
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if ((expandableView instanceof ActivatableNotificationView) && (expandableView.isAboveShelf() || expandableView.showingPulsing())) {
                break;
            }
            i++;
        }
        int i2 = size - 1;
        float f = 0.0f;
        while (i2 >= 0) {
            f = updateChildZValue(i2, f, stackScrollAlgorithmState, ambientState, i2 == i);
            i2--;
        }
    }

    /* access modifiers changed from: protected */
    public float updateChildZValue(int i, float f, StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState, boolean z) {
        int i2;
        ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
        ExpandableViewState viewState = expandableView.getViewState();
        int zDistanceBetweenElements = ambientState.getZDistanceBetweenElements();
        float baseZHeight = (float) ambientState.getBaseZHeight();
        if (expandableView.mustStayOnScreen() && !viewState.headsUpIsVisible && !ambientState.isDozingAndNotPulsing(expandableView) && viewState.yTranslation < ambientState.getTopPadding() + ambientState.getStackTranslation()) {
            if (f != 0.0f) {
                f += 1.0f;
            } else {
                f += Math.min(1.0f, ((ambientState.getTopPadding() + ambientState.getStackTranslation()) - viewState.yTranslation) / ((float) viewState.height));
            }
            viewState.zTranslation = baseZHeight + (((float) zDistanceBetweenElements) * f);
        } else if (z) {
            if (ambientState.getShelf() == null) {
                i2 = 0;
            } else {
                i2 = ambientState.getShelf().getIntrinsicHeight();
            }
            float innerHeight = ((float) (ambientState.getInnerHeight() - i2)) + ambientState.getTopPadding() + ambientState.getStackTranslation();
            float intrinsicHeight = viewState.yTranslation + ((float) expandableView.getIntrinsicHeight()) + this.mPaddingBetweenElements;
            if (innerHeight > intrinsicHeight) {
                viewState.zTranslation = baseZHeight;
            } else {
                viewState.zTranslation = baseZHeight + (Math.min((intrinsicHeight - innerHeight) / ((float) i2), 1.0f) * ((float) zDistanceBetweenElements));
            }
        } else {
            viewState.zTranslation = baseZHeight;
        }
        viewState.zTranslation += (1.0f - expandableView.getHeaderVisibleAmount()) * ((float) this.mPinnedZTranslationExtra);
        return f;
    }

    public void setIsExpanded(boolean z) {
        this.mIsExpanded = z;
    }

    public static class StackScrollAlgorithmState {
        public ExpandableView firstViewInShelf;
        /* access modifiers changed from: private */
        public float mCurrentExpandedYPosition;
        /* access modifiers changed from: private */
        public float mCurrentYPosition;
        public int scrollY;
        public final ArrayList<ExpandableView> visibleChildren = new ArrayList<>();

        static /* synthetic */ float access$016(StackScrollAlgorithmState stackScrollAlgorithmState, float f) {
            float f2 = stackScrollAlgorithmState.mCurrentYPosition + f;
            stackScrollAlgorithmState.mCurrentYPosition = f2;
            return f2;
        }

        static /* synthetic */ float access$116(StackScrollAlgorithmState stackScrollAlgorithmState, float f) {
            float f2 = stackScrollAlgorithmState.mCurrentExpandedYPosition + f;
            stackScrollAlgorithmState.mCurrentExpandedYPosition = f2;
            return f2;
        }
    }
}
