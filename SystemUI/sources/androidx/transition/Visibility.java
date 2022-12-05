package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.AnimatorUtils;
import androidx.transition.Transition;
/* loaded from: classes.dex */
public abstract class Visibility extends Transition {
    private static final String[] sTransitionProperties = {"android:visibility:visibility", "android:visibility:parent"};
    private int mMode = 3;

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        VisibilityInfo() {
        }
    }

    public Visibility() {
    }

    @SuppressLint({"RestrictedApi"})
    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.VISIBILITY_TRANSITION);
        int namedInt = TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionVisibilityMode", 0, 0);
        obtainStyledAttributes.recycle();
        if (namedInt != 0) {
            setMode(namedInt);
        }
    }

    public void setMode(int i) {
        if ((i & (-4)) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        this.mMode = i;
    }

    public int getMode() {
        return this.mMode;
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put("android:visibility:visibility", Integer.valueOf(transitionValues.view.getVisibility()));
        transitionValues.values.put("android:visibility:parent", transitionValues.view.getParent());
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put("android:visibility:screenLocation", iArr);
    }

    @Override // androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues != null && transitionValues.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mStartVisibility = ((Integer) transitionValues.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mStartParent = (ViewGroup) transitionValues.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        }
        if (transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mEndVisibility = ((Integer) transitionValues2.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mEndParent = (ViewGroup) transitionValues2.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        }
        if (transitionValues != null && transitionValues2 != null) {
            int i = visibilityInfo.mStartVisibility;
            int i2 = visibilityInfo.mEndVisibility;
            if (i == i2 && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (i != i2) {
                if (i == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (i2 == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        } else if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
            visibilityInfo.mFadeIn = true;
            visibilityInfo.mVisibilityChange = true;
        } else if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
            visibilityInfo.mFadeIn = false;
            visibilityInfo.mVisibilityChange = true;
        }
        return visibilityInfo;
    }

    @Override // androidx.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (visibilityChangeInfo.mVisibilityChange) {
            if (visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null) {
                return null;
            }
            if (visibilityChangeInfo.mFadeIn) {
                return onAppear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
            }
            return onDisappear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
        }
        return null;
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        if ((this.mMode & 1) != 1 || transitionValues2 == null) {
            return null;
        }
        if (transitionValues == null) {
            View view = (View) transitionValues2.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(view, false), getTransitionValues(view, false)).mVisibilityChange) {
                return null;
            }
        }
        return onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0089, code lost:
        if (r17.mCanRemoveViews != false) goto L52;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Animator onDisappear(final ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        View view;
        boolean z;
        boolean z2;
        View view2;
        if ((this.mMode & 2) == 2 && transitionValues != null) {
            final View view3 = transitionValues.view;
            View view4 = transitionValues2 != null ? transitionValues2.view : null;
            int i3 = R$id.save_overlay_view;
            final View view5 = (View) view3.getTag(i3);
            if (view5 != null) {
                view2 = null;
                z2 = true;
            } else if (view4 == null || view4.getParent() == null) {
                if (view4 != null) {
                    view = null;
                    z = false;
                    if (z) {
                        if (view3.getParent() != null) {
                            if (view3.getParent() instanceof View) {
                                View view6 = (View) view3.getParent();
                                if (!getVisibilityChangeInfo(getTransitionValues(view6, true), getMatchedTransitionValues(view6, true)).mVisibilityChange) {
                                    view4 = TransitionUtils.copyViewImage(viewGroup, view3, view6);
                                } else {
                                    int id = view6.getId();
                                    if (view6.getParent() == null) {
                                        if (id != -1) {
                                            if (viewGroup.findViewById(id) != null) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        view2 = view;
                        z2 = false;
                        view5 = view3;
                    }
                    z2 = false;
                    View view7 = view;
                    view5 = view4;
                    view2 = view7;
                }
                view4 = null;
                view = null;
                z = true;
                if (z) {
                }
                z2 = false;
                View view72 = view;
                view5 = view4;
                view2 = view72;
            } else {
                if (i2 == 4 || view3 == view4) {
                    view = view4;
                    z = false;
                    view4 = null;
                    if (z) {
                    }
                    z2 = false;
                    View view722 = view;
                    view5 = view4;
                    view2 = view722;
                }
                view4 = null;
                view = null;
                z = true;
                if (z) {
                }
                z2 = false;
                View view7222 = view;
                view5 = view4;
                view2 = view7222;
            }
            if (view5 == null) {
                if (view2 == null) {
                    return null;
                }
                int visibility = view2.getVisibility();
                ViewUtils.setTransitionVisibility(view2, 0);
                Animator onDisappear = onDisappear(viewGroup, view2, transitionValues, transitionValues2);
                if (onDisappear != null) {
                    DisappearListener disappearListener = new DisappearListener(view2, i2, true);
                    onDisappear.addListener(disappearListener);
                    AnimatorUtils.addPauseListener(onDisappear, disappearListener);
                    mo158addListener(disappearListener);
                } else {
                    ViewUtils.setTransitionVisibility(view2, visibility);
                }
                return onDisappear;
            }
            if (!z2) {
                int[] iArr = (int[]) transitionValues.values.get("android:visibility:screenLocation");
                int i4 = iArr[0];
                int i5 = iArr[1];
                int[] iArr2 = new int[2];
                viewGroup.getLocationOnScreen(iArr2);
                view5.offsetLeftAndRight((i4 - iArr2[0]) - view5.getLeft());
                view5.offsetTopAndBottom((i5 - iArr2[1]) - view5.getTop());
                ViewGroupUtils.getOverlay(viewGroup).add(view5);
            }
            Animator onDisappear2 = onDisappear(viewGroup, view5, transitionValues, transitionValues2);
            if (!z2) {
                if (onDisappear2 == null) {
                    ViewGroupUtils.getOverlay(viewGroup).remove(view5);
                } else {
                    view3.setTag(i3, view5);
                    mo158addListener(new TransitionListenerAdapter() { // from class: androidx.transition.Visibility.1
                        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.TransitionListener
                        public void onTransitionPause(Transition transition) {
                            ViewGroupUtils.getOverlay(viewGroup).remove(view5);
                        }

                        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.TransitionListener
                        public void onTransitionResume(Transition transition) {
                            if (view5.getParent() == null) {
                                ViewGroupUtils.getOverlay(viewGroup).add(view5);
                            } else {
                                Visibility.this.cancel();
                            }
                        }

                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionEnd(Transition transition) {
                            view3.setTag(R$id.save_overlay_view, null);
                            ViewGroupUtils.getOverlay(viewGroup).remove(view5);
                            transition.mo161removeListener(this);
                        }
                    });
                }
            }
            return onDisappear2;
        }
        return null;
    }

    @Override // androidx.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null && transitionValues2 == null) {
            return false;
        }
        if (transitionValues != null && transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility") != transitionValues.values.containsKey("android:visibility:visibility")) {
            return false;
        }
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (!visibilityChangeInfo.mVisibilityChange) {
            return false;
        }
        return visibilityChangeInfo.mStartVisibility == 0 || visibilityChangeInfo.mEndVisibility == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // androidx.transition.Transition.TransitionListener
        public void onTransitionCancel(Transition transition) {
        }

        @Override // androidx.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
        }

        DisappearListener(View view, int i, boolean z) {
            this.mView = view;
            this.mFinalVisibility = i;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = z;
            suppressLayout(true);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, androidx.transition.AnimatorUtils.AnimatorPauseListenerCompat
        public void onAnimationPause(Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, androidx.transition.AnimatorUtils.AnimatorPauseListenerCompat
        public void onAnimationResume(Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.mCanceled = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            hideViewWhenNotCanceled();
        }

        @Override // androidx.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
            transition.mo161removeListener(this);
        }

        @Override // androidx.transition.Transition.TransitionListener
        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        @Override // androidx.transition.Transition.TransitionListener
        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean z) {
            ViewGroup viewGroup;
            if (!this.mSuppressLayout || this.mLayoutSuppressed == z || (viewGroup = this.mParent) == null) {
                return;
            }
            this.mLayoutSuppressed = z;
            ViewGroupUtils.suppressLayout(viewGroup, z);
        }
    }
}
