package com.android.wm.shell.bubbles.animation;

import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Set;
/* loaded from: classes2.dex */
public class ExpandedAnimationController extends PhysicsAnimationLayout.PhysicsAnimationController {
    private Runnable mAfterCollapse;
    private Runnable mAfterExpand;
    private float mBubblePaddingTop;
    private float mBubbleSizePx;
    private int mBubblesMaxRendered;
    private PointF mCollapsePoint;
    private int mExpandedViewPadding;
    private Runnable mLeadBubbleEndAction;
    private MagnetizedObject<View> mMagnetizedBubbleDraggingOut;
    private Runnable mOnBubbleAnimatedOutAction;
    private BubblePositioner mPositioner;
    private float mSpaceBetweenBubbles;
    private float mStackOffsetPx;
    private final PhysicsAnimator.SpringConfig mAnimateOutSpringConfig = new PhysicsAnimator.SpringConfig(1000.0f, 1.0f);
    private boolean mAnimatingExpand = false;
    private boolean mPreparingToCollapse = false;
    private boolean mAnimatingCollapse = false;
    private boolean mSpringingBubbleToTouch = false;
    private boolean mSpringToTouchOnNextMotionEvent = false;
    private boolean mBubbleDraggedOutEnough = false;

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i) {
        return -1;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i) {
        return 0.0f;
    }

    public ExpandedAnimationController(BubblePositioner bubblePositioner, int i, Runnable runnable) {
        this.mPositioner = bubblePositioner;
        updateResources();
        this.mExpandedViewPadding = i;
        this.mOnBubbleAnimatedOutAction = runnable;
        this.mCollapsePoint = this.mPositioner.getDefaultStartPosition();
    }

    public void expandFromStack(Runnable runnable, Runnable runnable2) {
        this.mPreparingToCollapse = false;
        this.mAnimatingCollapse = false;
        this.mAnimatingExpand = true;
        this.mAfterExpand = runnable;
        this.mLeadBubbleEndAction = runnable2;
        startOrUpdatePathAnimation(true);
    }

    public void expandFromStack(Runnable runnable) {
        expandFromStack(runnable, null);
    }

    public void notifyPreparingToCollapse() {
        this.mPreparingToCollapse = true;
    }

    public void collapseBackToStack(PointF pointF, Runnable runnable) {
        this.mAnimatingExpand = false;
        this.mPreparingToCollapse = false;
        this.mAnimatingCollapse = true;
        this.mAfterCollapse = runnable;
        this.mCollapsePoint = pointF;
        startOrUpdatePathAnimation(false);
    }

    public void updateResources() {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout == null) {
            return;
        }
        Resources resources = physicsAnimationLayout.getContext().getResources();
        this.mBubblePaddingTop = resources.getDimensionPixelSize(R.dimen.bubble_padding_top);
        this.mStackOffsetPx = resources.getDimensionPixelSize(R.dimen.bubble_stack_offset);
        this.mBubbleSizePx = this.mPositioner.getBubbleSize();
        this.mBubblesMaxRendered = this.mPositioner.getMaxBubbles();
        this.mSpaceBetweenBubbles = resources.getDimensionPixelSize(R.dimen.bubble_spacing);
    }

    private void startOrUpdatePathAnimation(final boolean z) {
        Runnable runnable;
        if (z) {
            runnable = new Runnable() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ExpandedAnimationController.this.lambda$startOrUpdatePathAnimation$0();
                }
            };
        } else {
            runnable = new Runnable() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ExpandedAnimationController.this.lambda$startOrUpdatePathAnimation$1();
                }
            };
        }
        animationsForChildrenFromIndex(0, new PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator
            public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
                ExpandedAnimationController.this.lambda$startOrUpdatePathAnimation$3(z, i, physicsPropertyAnimator);
            }
        }).startAll(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$0() {
        this.mAnimatingExpand = false;
        Runnable runnable = this.mAfterExpand;
        if (runnable != null) {
            runnable.run();
        }
        this.mAfterExpand = null;
        updateBubblePositions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$1() {
        this.mAnimatingCollapse = false;
        Runnable runnable = this.mAfterCollapse;
        if (runnable != null) {
            runnable.run();
        }
        this.mAfterCollapse = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$3(boolean z, int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        float expandedY;
        float f;
        View childAt = this.mLayout.getChildAt(i);
        Path path = new Path();
        path.moveTo(childAt.getTranslationX(), childAt.getTranslationY());
        if (this.mPositioner.showBubblesVertically()) {
            expandedY = getBubbleXOrYForOrientation(i);
        } else {
            expandedY = getExpandedY();
        }
        if (z) {
            path.lineTo(childAt.getTranslationX(), expandedY);
            if (this.mPositioner.showBubblesVertically()) {
                Rect availableRect = this.mPositioner.getAvailableRect();
                PointF pointF = this.mCollapsePoint;
                if (pointF != null && pointF.x < ((float) availableRect.width()) / 2.0f) {
                    f = availableRect.left;
                } else {
                    f = availableRect.right - this.mBubbleSizePx;
                }
                path.lineTo(f, getBubbleXOrYForOrientation(i));
            } else {
                path.lineTo(getBubbleXOrYForOrientation(i), expandedY);
            }
        } else {
            float f2 = this.mCollapsePoint.x;
            path.lineTo(f2, expandedY);
            path.lineTo(f2, this.mCollapsePoint.y + (Math.min(i, 1) * this.mStackOffsetPx));
        }
        boolean z2 = (z && !this.mLayout.isFirstChildXLeftOfCenter(childAt.getTranslationX())) || (!z && this.mLayout.isFirstChildXLeftOfCenter(this.mCollapsePoint.x));
        int childCount = z2 ? i * 10 : (this.mLayout.getChildCount() - i) * 10;
        boolean z3 = (z2 && i == 0) || (!z2 && i == this.mLayout.getChildCount() - 1);
        Interpolator interpolator = Interpolators.LINEAR;
        Runnable[] runnableArr = new Runnable[2];
        runnableArr[0] = z3 ? this.mLeadBubbleEndAction : null;
        runnableArr[1] = new Runnable() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ExpandedAnimationController.this.lambda$startOrUpdatePathAnimation$2();
            }
        };
        physicsPropertyAnimator.followAnimatedTargetAlongPath(path, 175, interpolator, runnableArr).withStartDelay(childCount).withStiffness(1000.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOrUpdatePathAnimation$2() {
        this.mLeadBubbleEndAction = null;
    }

    public void onUnstuckFromTarget() {
        this.mSpringToTouchOnNextMotionEvent = true;
    }

    public void prepareForBubbleDrag(final View view, MagnetizedObject.MagneticTarget magneticTarget, MagnetizedObject.MagnetListener magnetListener) {
        this.mLayout.cancelAnimationsOnView(view);
        view.setTranslationZ(32767.0f);
        MagnetizedObject<View> magnetizedObject = new MagnetizedObject<View>(this.mLayout.getContext(), view, DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y) { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController.1
            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
            public float getWidth(View view2) {
                return ExpandedAnimationController.this.mBubbleSizePx;
            }

            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
            public float getHeight(View view2) {
                return ExpandedAnimationController.this.mBubbleSizePx;
            }

            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
            public void getLocationOnScreen(View view2, int[] iArr) {
                iArr[0] = (int) view.getTranslationX();
                iArr[1] = (int) view.getTranslationY();
            }
        };
        this.mMagnetizedBubbleDraggingOut = magnetizedObject;
        magnetizedObject.addTarget(magneticTarget);
        this.mMagnetizedBubbleDraggingOut.setMagnetListener(magnetListener);
        this.mMagnetizedBubbleDraggingOut.setHapticsEnabled(true);
        this.mMagnetizedBubbleDraggingOut.setFlingToTargetMinVelocity(6000.0f);
    }

    private void springBubbleTo(View view, float f, float f2) {
        animationForChild(view).translationX(f, new Runnable[0]).translationY(f2, new Runnable[0]).withStiffness(10000.0f).start(new Runnable[0]);
    }

    public void dragBubbleOut(View view, float f, float f2) {
        boolean z = true;
        if (this.mSpringToTouchOnNextMotionEvent) {
            springBubbleTo(this.mMagnetizedBubbleDraggingOut.getUnderlyingObject(), f, f2);
            this.mSpringToTouchOnNextMotionEvent = false;
            this.mSpringingBubbleToTouch = true;
        } else if (this.mSpringingBubbleToTouch) {
            if (this.mLayout.arePropertiesAnimatingOnView(view, DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y)) {
                springBubbleTo(this.mMagnetizedBubbleDraggingOut.getUnderlyingObject(), f, f2);
            } else {
                this.mSpringingBubbleToTouch = false;
            }
        }
        if (!this.mSpringingBubbleToTouch && !this.mMagnetizedBubbleDraggingOut.getObjectStuckToTarget()) {
            view.setTranslationX(f);
            view.setTranslationY(f2);
        }
        if (f2 <= getExpandedY() + this.mBubbleSizePx && f2 >= getExpandedY() - this.mBubbleSizePx) {
            z = false;
        }
        if (z != this.mBubbleDraggedOutEnough) {
            updateBubblePositions();
            this.mBubbleDraggedOutEnough = z;
        }
    }

    public void dismissDraggedOutBubble(View view, float f, Runnable runnable) {
        if (view == null) {
            return;
        }
        animationForChild(view).withStiffness(10000.0f).scaleX(0.0f, new Runnable[0]).scaleY(0.0f, new Runnable[0]).translationY(view.getTranslationY() + f, new Runnable[0]).alpha(0.0f, runnable).start(new Runnable[0]);
        updateBubblePositions();
    }

    public View getDraggedOutBubble() {
        MagnetizedObject<View> magnetizedObject = this.mMagnetizedBubbleDraggingOut;
        if (magnetizedObject == null) {
            return null;
        }
        return magnetizedObject.getUnderlyingObject();
    }

    public MagnetizedObject<View> getMagnetizedBubbleDraggingOut() {
        return this.mMagnetizedBubbleDraggingOut;
    }

    public void snapBubbleBack(final View view, float f, float f2) {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout == null) {
            return;
        }
        int indexOfChild = physicsAnimationLayout.indexOfChild(view);
        animationForChildAtIndex(indexOfChild).position(getBubbleXOrYForOrientation(indexOfChild), getExpandedY(), new Runnable[0]).withPositionStartVelocities(f, f2).start(new Runnable() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                view.setTranslationZ(0.0f);
            }
        });
        this.mMagnetizedBubbleDraggingOut = null;
        updateBubblePositions();
    }

    public void onGestureFinished() {
        this.mBubbleDraggedOutEnough = false;
        this.mMagnetizedBubbleDraggingOut = null;
        updateBubblePositions();
    }

    public float getExpandedY() {
        return this.mPositioner.getAvailableRect().top + this.mBubblePaddingTop;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ExpandedAnimationController state:");
        printWriter.print("  isActive:          ");
        printWriter.println(isActiveController());
        printWriter.print("  animatingExpand:   ");
        printWriter.println(this.mAnimatingExpand);
        printWriter.print("  animatingCollapse: ");
        printWriter.println(this.mAnimatingCollapse);
        printWriter.print("  springingBubble:   ");
        printWriter.println(this.mSpringingBubbleToTouch);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout) {
        updateResources();
        this.mLayout.setVisibility(0);
        animationsForChildrenFromIndex(0, ExpandedAnimationController$$ExternalSyntheticLambda1.INSTANCE).startAll(new Runnable[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onActiveControllerForLayout$6(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        physicsPropertyAnimator.scaleX(1.0f, new Runnable[0]).scaleY(1.0f, new Runnable[0]).alpha(1.0f, new Runnable[0]);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    Set<DynamicAnimation.ViewProperty> getAnimatedProperties() {
        return Sets.newHashSet(new DynamicAnimation.ViewProperty[]{DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y, DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y, DynamicAnimation.ALPHA});
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    SpringForce getSpringForce(DynamicAnimation.ViewProperty viewProperty, View view) {
        return new SpringForce().setDampingRatio(0.65f).setStiffness(200.0f);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildAdded(View view, int i) {
        float f;
        float f2;
        boolean z = true;
        if (this.mAnimatingExpand) {
            startOrUpdatePathAnimation(true);
        } else if (this.mAnimatingCollapse) {
            startOrUpdatePathAnimation(false);
        } else if (this.mPositioner.showBubblesVertically()) {
            view.setTranslationY(getBubbleXOrYForOrientation(i));
            if (this.mPreparingToCollapse) {
                return;
            }
            Rect availableRect = this.mPositioner.getAvailableRect();
            PointF pointF = this.mCollapsePoint;
            if (pointF == null || pointF.x >= availableRect.width() / 2.0f) {
                z = false;
            }
            if (z) {
                f = (-this.mBubbleSizePx) * 4.0f;
            } else {
                f = availableRect.right + (this.mBubbleSizePx * 4.0f);
            }
            if (z) {
                f2 = availableRect.left + this.mExpandedViewPadding;
            } else {
                f2 = (availableRect.right - this.mBubbleSizePx) - this.mExpandedViewPadding;
            }
            animationForChild(view).translationX(f, f2, new Runnable[0]).start(new Runnable[0]);
            updateBubblePositions();
        } else {
            view.setTranslationX(getBubbleXOrYForOrientation(i));
            if (this.mPreparingToCollapse) {
                return;
            }
            animationForChild(view).translationY(getExpandedY() - (this.mBubbleSizePx * 4.0f), getExpandedY(), new Runnable[0]).start(new Runnable[0]);
            updateBubblePositions();
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildRemoved(View view, int i, Runnable runnable) {
        if (view.equals(getDraggedOutBubble())) {
            this.mMagnetizedBubbleDraggingOut = null;
            runnable.run();
            this.mOnBubbleAnimatedOutAction.run();
        } else {
            PhysicsAnimator.getInstance(view).spring(DynamicAnimation.ALPHA, 0.0f).spring(DynamicAnimation.SCALE_X, 0.0f, this.mAnimateOutSpringConfig).spring(DynamicAnimation.SCALE_Y, 0.0f, this.mAnimateOutSpringConfig).withEndActions(runnable, this.mOnBubbleAnimatedOutAction).start();
        }
        updateBubblePositions();
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildReordered(View view, int i, int i2) {
        if (this.mPreparingToCollapse) {
            return;
        }
        if (this.mAnimatingCollapse) {
            startOrUpdatePathAnimation(false);
        } else {
            updateBubblePositions();
        }
    }

    private void updateBubblePositions() {
        float f;
        if (this.mAnimatingExpand || this.mAnimatingCollapse) {
            return;
        }
        for (int i = 0; i < this.mLayout.getChildCount(); i++) {
            View childAt = this.mLayout.getChildAt(i);
            if (childAt.equals(getDraggedOutBubble())) {
                return;
            }
            if (this.mPositioner.showBubblesVertically()) {
                Rect availableRect = this.mPositioner.getAvailableRect();
                PointF pointF = this.mCollapsePoint;
                boolean z = pointF != null && pointF.x < ((float) availableRect.width()) / 2.0f;
                PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(childAt);
                if (z) {
                    f = availableRect.left;
                } else {
                    f = availableRect.right - this.mBubbleSizePx;
                }
                animationForChild.translationX(f, new Runnable[0]).translationY(getBubbleXOrYForOrientation(i), new Runnable[0]).start(new Runnable[0]);
            } else {
                animationForChild(childAt).translationX(getBubbleXOrYForOrientation(i), new Runnable[0]).translationY(getExpandedY(), new Runnable[0]).start(new Runnable[0]);
            }
        }
    }

    public float getBubbleXOrYForOrientation(int i) {
        int centerX;
        if (this.mLayout == null) {
            return 0.0f;
        }
        float f = i * (this.mBubbleSizePx + this.mSpaceBetweenBubbles);
        Rect availableRect = this.mPositioner.getAvailableRect();
        boolean showBubblesVertically = this.mPositioner.showBubblesVertically();
        float childCount = (this.mLayout.getChildCount() * this.mBubbleSizePx) + ((this.mLayout.getChildCount() - 1) * this.mSpaceBetweenBubbles);
        if (showBubblesVertically) {
            centerX = availableRect.centerY();
        } else {
            centerX = availableRect.centerX();
        }
        return (centerX - (childCount / 2.0f)) + f;
    }
}
