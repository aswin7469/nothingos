package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import com.android.systemui.C1893R;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class NearestTouchFrame extends FrameLayout {
    private final List<View> mAttachedChildren;
    private final Comparator<View> mChildRegionComparator;
    private final List<View> mClickableChildren;
    private final boolean mIsActive;
    private boolean mIsVertical;
    private final int[] mOffset;
    private final int[] mTmpInt;
    private final Map<View, Rect> mTouchableRegions;
    private View mTouchingChild;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-buttons-NearestTouchFrame */
    public /* synthetic */ int mo35019x2049347(View view, View view2) {
        char c = this.mIsVertical;
        view.getLocationInWindow(this.mTmpInt);
        int[] iArr = this.mTmpInt;
        view2.getLocationInWindow(iArr);
        return (iArr[c] - this.mOffset[c]) - (this.mTmpInt[c] - this.mOffset[c]);
    }

    public NearestTouchFrame(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, context.getResources().getConfiguration());
    }

    NearestTouchFrame(Context context, AttributeSet attributeSet, Configuration configuration) {
        super(context, attributeSet);
        this.mClickableChildren = new ArrayList();
        this.mAttachedChildren = new ArrayList();
        this.mTmpInt = new int[2];
        this.mOffset = new int[2];
        this.mTouchableRegions = new HashMap();
        this.mChildRegionComparator = new NearestTouchFrame$$ExternalSyntheticLambda3(this);
        this.mIsActive = configuration.smallestScreenWidthDp < 600;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{C1893R.attr.isVertical});
        this.mIsVertical = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mClickableChildren.clear();
        this.mAttachedChildren.clear();
        this.mTouchableRegions.clear();
        addClickableChildren(this);
        getLocationInWindow(this.mOffset);
        cacheClosestChildLocations();
    }

    private void cacheClosestChildLocations() {
        if (getWidth() != 0 && getHeight() != 0) {
            this.mClickableChildren.sort(this.mChildRegionComparator);
            Stream<View> filter = this.mClickableChildren.stream().filter(new NearestTouchFrame$$ExternalSyntheticLambda0());
            List<View> list = this.mAttachedChildren;
            Objects.requireNonNull(list);
            filter.forEachOrdered(new NearestTouchFrame$$ExternalSyntheticLambda1(list));
            for (int i = 0; i < this.mAttachedChildren.size(); i++) {
                View view = this.mAttachedChildren.get(i);
                if (view.isAttachedToWindow()) {
                    Rect childsBounds = getChildsBounds(view);
                    if (i == 0) {
                        if (this.mIsVertical) {
                            childsBounds.top = 0;
                        } else {
                            childsBounds.left = 0;
                        }
                        this.mTouchableRegions.put(view, childsBounds);
                    } else {
                        Rect rect = this.mTouchableRegions.get(this.mAttachedChildren.get(i - 1));
                        if (this.mIsVertical) {
                            int i2 = childsBounds.top - rect.bottom;
                            int i3 = i2 / 2;
                            childsBounds.top -= i3;
                            rect.bottom += i3 - (i2 % 2 == 0 ? 1 : 0);
                        } else {
                            int i4 = childsBounds.left - rect.right;
                            int i5 = i4 / 2;
                            childsBounds.left -= i5;
                            rect.right += i5 - (i4 % 2 == 0 ? 1 : 0);
                        }
                        if (i == this.mClickableChildren.size() - 1) {
                            if (this.mIsVertical) {
                                childsBounds.bottom = getHeight();
                            } else {
                                childsBounds.right = getWidth();
                            }
                        }
                        this.mTouchableRegions.put(view, childsBounds);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setIsVertical(boolean z) {
        this.mIsVertical = z;
    }

    private Rect getChildsBounds(View view) {
        view.getLocationInWindow(this.mTmpInt);
        int[] iArr = this.mTmpInt;
        int i = iArr[0];
        int[] iArr2 = this.mOffset;
        int i2 = i - iArr2[0];
        int i3 = iArr[1] - iArr2[1];
        return new Rect(i2, i3, view.getWidth() + i2, view.getHeight() + i3);
    }

    private void addClickableChildren(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.isClickable()) {
                this.mClickableChildren.add(childAt);
            } else if (childAt instanceof ViewGroup) {
                addClickableChildren((ViewGroup) childAt);
            }
        }
    }

    public Map<View, Rect> getFullTouchableChildRegions() {
        HashMap hashMap = new HashMap(this.mTouchableRegions.size());
        getLocationOnScreen(this.mTmpInt);
        for (Map.Entry next : this.mTouchableRegions.entrySet()) {
            Rect rect = new Rect((Rect) next.getValue());
            int[] iArr = this.mTmpInt;
            rect.offset(iArr[0], iArr[1]);
            hashMap.put((View) next.getKey(), rect);
        }
        return hashMap;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mIsActive) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                this.mTouchingChild = this.mClickableChildren.stream().filter(new NearestTouchFrame$$ExternalSyntheticLambda0()).filter(new NearestTouchFrame$$ExternalSyntheticLambda2(this, x, y)).findFirst().orElse(null);
            }
            View view = this.mTouchingChild;
            if (view != null) {
                motionEvent.offsetLocation((float) ((view.getWidth() / 2) - x), (float) ((this.mTouchingChild.getHeight() / 2) - y));
                return this.mTouchingChild.getVisibility() == 0 && this.mTouchingChild.dispatchTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTouchEvent$1$com-android-systemui-navigationbar-buttons-NearestTouchFrame */
    public /* synthetic */ boolean mo35020x13cd0412(int i, int i2, View view) {
        return this.mTouchableRegions.get(view).contains(i, i2);
    }
}
