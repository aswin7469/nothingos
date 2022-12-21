package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.notification.stack.AnimationFilter;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import java.util.ArrayList;
import java.util.List;

public class StatusIconContainer extends AlphaOptimizedLinearLayout {
    /* access modifiers changed from: private */
    public static final AnimationProperties ADD_ICON_PROPERTIES = new AnimationProperties() {
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200).setDelay(50);
    /* access modifiers changed from: private */
    public static final AnimationProperties ANIMATE_ALL_PROPERTIES = new AnimationProperties() {
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX().animateY().animateAlpha().animateScale();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_OVERFLOW = false;
    private static final int MAX_DOTS = 0;
    private static int MAX_ICONS = 50;
    private static final String TAG = "StatusIconContainer";
    /* access modifiers changed from: private */
    public static final AnimationProperties X_ANIMATION_PROPERTIES = new AnimationProperties() {
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    private int mDotPadding;
    private int mIconDotFrameWidth;
    private int mIconSpacing;
    private ArrayList<String> mIgnoredSlots;
    private ArrayList<StatusIconState> mLayoutStates;
    private ArrayList<View> mMeasureViews;
    private boolean mNeedsUnderflow;
    private boolean mShouldRestrictIcons;
    private int mStaticDotDiameter;
    private int mUnderflowStart;
    private int mUnderflowWidth;

    public StatusIconContainer(Context context) {
        this(context, (AttributeSet) null);
    }

    public StatusIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUnderflowStart = 0;
        this.mShouldRestrictIcons = true;
        this.mLayoutStates = new ArrayList<>();
        this.mMeasureViews = new ArrayList<>();
        this.mIgnoredSlots = new ArrayList<>();
        initDimens();
        setWillNotDraw(true);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setShouldRestrictIcons(boolean z) {
        this.mShouldRestrictIcons = z;
    }

    public boolean isRestrictingIcons() {
        return this.mShouldRestrictIcons;
    }

    private void initDimens() {
        this.mIconDotFrameWidth = getResources().getDimensionPixelSize(17105552);
        this.mDotPadding = getResources().getDimensionPixelSize(C1893R.dimen.overflow_icon_dot_padding);
        this.mIconSpacing = getResources().getDimensionPixelSize(C1893R.dimen.status_bar_system_icon_spacing);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.overflow_dot_radius) * 2;
        this.mStaticDotDiameter = dimensionPixelSize;
        this.mUnderflowWidth = this.mIconDotFrameWidth + ((dimensionPixelSize + this.mDotPadding) * -1);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float height = ((float) getHeight()) / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i6 = (int) (height - (((float) measuredHeight) / 2.0f));
            childAt.layout(0, i6, measuredWidth, measuredHeight + i6);
        }
        resetViewStates();
        calculateIconTranslations();
        applyIconStates();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        this.mMeasureViews.clear();
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) getChildAt(i5);
            if (statusIconDisplayable.isIconVisible() && !statusIconDisplayable.isIconBlocked() && !this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                this.mMeasureViews.add((View) statusIconDisplayable);
            }
        }
        int size2 = this.mMeasureViews.size();
        int i6 = MAX_ICONS;
        if (size2 > i6) {
            i6--;
        }
        int i7 = this.mPaddingLeft + this.mPaddingRight;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 0);
        this.mNeedsUnderflow = this.mShouldRestrictIcons && size2 > MAX_ICONS;
        boolean z = true;
        for (int i8 = 0; i8 < size2; i8++) {
            View view = this.mMeasureViews.get((size2 - i8) - 1);
            measureChild(view, makeMeasureSpec, i2);
            if (i8 == size2 - 1) {
                i3 = 0;
            } else {
                i3 = this.mIconSpacing;
            }
            if (!this.mShouldRestrictIcons) {
                i4 = getViewTotalMeasuredWidth(view);
            } else if (i8 >= i6 || !z) {
                if (z) {
                    i7 += this.mUnderflowWidth;
                    z = false;
                }
            } else {
                i4 = getViewTotalMeasuredWidth(view);
            }
            i7 += i4 + i3;
        }
        if (mode == 1073741824) {
            if (!this.mNeedsUnderflow && i7 > size) {
                this.mNeedsUnderflow = true;
            }
            setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
            return;
        }
        if (mode != Integer.MIN_VALUE || i7 <= size) {
            size = i7;
        } else {
            this.mNeedsUnderflow = true;
        }
        setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        StatusIconState statusIconState = new StatusIconState();
        statusIconState.justAdded = true;
        view.setTag(C1893R.C1897id.status_bar_view_state_tag, statusIconState);
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        view.setTag(C1893R.C1897id.status_bar_view_state_tag, (Object) null);
    }

    public void addIgnoredSlot(String str) {
        if (addIgnoredSlotInternal(str)) {
            requestLayout();
        }
    }

    public void addIgnoredSlots(List<String> list) {
        boolean z = false;
        for (String addIgnoredSlotInternal : list) {
            z |= addIgnoredSlotInternal(addIgnoredSlotInternal);
        }
        if (z) {
            requestLayout();
        }
    }

    private boolean addIgnoredSlotInternal(String str) {
        if (this.mIgnoredSlots.contains(str)) {
            return false;
        }
        this.mIgnoredSlots.add(str);
        return true;
    }

    public void removeIgnoredSlot(String str) {
        if (this.mIgnoredSlots.remove((Object) str)) {
            requestLayout();
        }
    }

    public void removeIgnoredSlots(List<String> list) {
        boolean z = false;
        for (String remove : list) {
            z |= this.mIgnoredSlots.remove((Object) remove);
        }
        if (z) {
            requestLayout();
        }
    }

    public void setIgnoredSlots(List<String> list) {
        this.mIgnoredSlots.clear();
        addIgnoredSlots(list);
    }

    public View getViewForSlot(String str) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if ((childAt instanceof StatusIconDisplayable) && ((StatusIconDisplayable) childAt).getSlot().equals(str)) {
                return childAt;
            }
        }
        return null;
    }

    private void calculateIconTranslations() {
        int i;
        this.mLayoutStates.clear();
        float width = (float) getWidth();
        float paddingEnd = width - ((float) getPaddingEnd());
        float paddingStart = (float) getPaddingStart();
        int childCount = getChildCount();
        int i2 = childCount - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            View childAt = getChildAt(i2);
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) childAt;
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (!statusIconDisplayable.isIconVisible() || statusIconDisplayable.isIconBlocked() || this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                viewStateFromChild.visibleState = 2;
            } else {
                float viewTotalWidth = paddingEnd - ((float) getViewTotalWidth(childAt));
                viewStateFromChild.visibleState = 0;
                viewStateFromChild.xTranslation = viewTotalWidth;
                this.mLayoutStates.add(0, viewStateFromChild);
                paddingEnd = viewTotalWidth - ((float) this.mIconSpacing);
            }
            i2--;
        }
        int size = this.mLayoutStates.size();
        int i3 = MAX_ICONS;
        if (size > i3) {
            i3--;
        }
        this.mUnderflowStart = 0;
        int i4 = size - 1;
        int i5 = 0;
        while (true) {
            if (i4 < 0) {
                i4 = -1;
                break;
            }
            StatusIconState statusIconState = this.mLayoutStates.get(i4);
            if ((this.mNeedsUnderflow && statusIconState.xTranslation < ((float) this.mUnderflowWidth) + paddingStart) || (this.mShouldRestrictIcons && i5 >= i3)) {
                break;
            }
            this.mUnderflowStart = (int) Math.max(paddingStart, (statusIconState.xTranslation - ((float) this.mUnderflowWidth)) - ((float) this.mIconSpacing));
            i5++;
            i4--;
        }
        if (i4 != -1) {
            while (i4 >= 0) {
                this.mLayoutStates.get(i4).visibleState = 2;
                i4--;
            }
        }
        if (isLayoutRtl()) {
            for (i = 0; i < childCount; i++) {
                View childAt2 = getChildAt(i);
                StatusIconState viewStateFromChild2 = getViewStateFromChild(childAt2);
                viewStateFromChild2.xTranslation = (width - viewStateFromChild2.xTranslation) - ((float) childAt2.getWidth());
            }
        }
    }

    private void applyIconStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (viewStateFromChild != null) {
                viewStateFromChild.applyToView(childAt);
            }
        }
    }

    private void resetViewStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (viewStateFromChild != null) {
                viewStateFromChild.initFrom(childAt);
                viewStateFromChild.alpha = 1.0f;
                viewStateFromChild.hidden = false;
            }
        }
    }

    private static StatusIconState getViewStateFromChild(View view) {
        return (StatusIconState) view.getTag(C1893R.C1897id.status_bar_view_state_tag);
    }

    private static int getViewTotalMeasuredWidth(View view) {
        return view.getMeasuredWidth() + view.getPaddingStart() + view.getPaddingEnd();
    }

    private static int getViewTotalWidth(View view) {
        return view.getWidth() + view.getPaddingStart() + view.getPaddingEnd();
    }

    public static class StatusIconState extends ViewState {
        float distanceToViewEnd = -1.0f;
        public boolean justAdded = true;
        public int visibleState = 0;

        public void applyToView(View view) {
            AnimationProperties animationProperties;
            float width = (view.getParent() instanceof View ? (float) ((View) view.getParent()).getWidth() : 0.0f) - this.xTranslation;
            if (view instanceof StatusIconDisplayable) {
                StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) view;
                boolean z = true;
                if (this.justAdded || (statusIconDisplayable.getVisibleState() == 2 && this.visibleState == 0)) {
                    super.applyToView(view);
                    view.setAlpha(0.0f);
                    statusIconDisplayable.setVisibleState(2);
                    animationProperties = StatusIconContainer.ADD_ICON_PROPERTIES;
                } else {
                    int visibleState2 = statusIconDisplayable.getVisibleState();
                    int i = this.visibleState;
                    animationProperties = null;
                    if (visibleState2 != i) {
                        if (statusIconDisplayable.getVisibleState() == 0 && this.visibleState == 2) {
                            z = false;
                        } else {
                            animationProperties = StatusIconContainer.ANIMATE_ALL_PROPERTIES;
                        }
                    } else if (!(i == 2 || this.distanceToViewEnd == width)) {
                        animationProperties = StatusIconContainer.X_ANIMATION_PROPERTIES;
                    }
                }
                statusIconDisplayable.setVisibleState(this.visibleState, z);
                if (animationProperties != null) {
                    animateTo(view, animationProperties);
                } else {
                    super.applyToView(view);
                }
                this.justAdded = false;
                this.distanceToViewEnd = width;
            }
        }
    }

    public void setMaxIconsToDisplay(int i) {
        MAX_ICONS = i;
    }
}
