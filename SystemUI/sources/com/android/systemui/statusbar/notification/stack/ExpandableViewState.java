package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;

public class ExpandableViewState extends ViewState {
    public static final int LOCATION_BOTTOM_STACK_HIDDEN = 16;
    public static final int LOCATION_BOTTOM_STACK_PEEKING = 8;
    public static final int LOCATION_FIRST_HUN = 1;
    public static final int LOCATION_GONE = 64;
    public static final int LOCATION_HIDDEN_TOP = 2;
    public static final int LOCATION_MAIN_AREA = 4;
    public static final int LOCATION_UNKNOWN = 0;
    private static final int TAG_ANIMATOR_BOTTOM_INSET = 2131427562;
    private static final int TAG_ANIMATOR_HEIGHT = 2131428061;
    private static final int TAG_ANIMATOR_TOP_INSET = 2131429065;
    private static final int TAG_END_BOTTOM_INSET = 2131427560;
    private static final int TAG_END_HEIGHT = 2131428059;
    private static final int TAG_END_TOP_INSET = 2131429063;
    private static final int TAG_START_BOTTOM_INSET = 2131427561;
    private static final int TAG_START_HEIGHT = 2131428060;
    private static final int TAG_START_TOP_INSET = 2131429064;
    public static final int VISIBLE_LOCATIONS = 5;
    public boolean belowSpeedBump;
    public int clipBottomAmount;
    public int clipTopAmount;
    public boolean dimmed;
    public boolean headsUpIsVisible;
    public int height;
    public boolean hideSensitive;
    public boolean inShelf;
    public int location;
    public int notGoneIndex;

    public void copyFrom(ViewState viewState) {
        super.copyFrom(viewState);
        if (viewState instanceof ExpandableViewState) {
            ExpandableViewState expandableViewState = (ExpandableViewState) viewState;
            this.height = expandableViewState.height;
            this.dimmed = expandableViewState.dimmed;
            this.hideSensitive = expandableViewState.hideSensitive;
            this.belowSpeedBump = expandableViewState.belowSpeedBump;
            this.clipTopAmount = expandableViewState.clipTopAmount;
            this.notGoneIndex = expandableViewState.notGoneIndex;
            this.location = expandableViewState.location;
            this.headsUpIsVisible = expandableViewState.headsUpIsVisible;
        }
    }

    public void applyToView(View view) {
        super.applyToView(view);
        if (view instanceof ExpandableView) {
            ExpandableView expandableView = (ExpandableView) view;
            int actualHeight = expandableView.getActualHeight();
            int i = this.height;
            if (actualHeight != i) {
                expandableView.setActualHeight(i, false);
            }
            expandableView.setDimmed(this.dimmed, false);
            expandableView.setHideSensitive(this.hideSensitive, false, 0, 0);
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            int i2 = this.clipTopAmount;
            if (((float) expandableView.getClipTopAmount()) != ((float) i2)) {
                expandableView.setClipTopAmount(i2);
            }
            int i3 = this.clipBottomAmount;
            if (((float) expandableView.getClipBottomAmount()) != ((float) i3)) {
                expandableView.setClipBottomAmount(i3);
            }
            expandableView.setTransformingInShelf(false);
            expandableView.setInShelf(this.inShelf);
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    public void animateTo(View view, AnimationProperties animationProperties) {
        super.animateTo(view, animationProperties);
        if (view instanceof ExpandableView) {
            ExpandableView expandableView = (ExpandableView) view;
            AnimationFilter animationFilter = animationProperties.getAnimationFilter();
            if (this.height != expandableView.getActualHeight()) {
                startHeightAnimation(expandableView, animationProperties);
            } else {
                abortAnimation(view, C1893R.C1897id.height_animator_tag);
            }
            if (this.clipTopAmount != expandableView.getClipTopAmount()) {
                startClipAnimation(expandableView, animationProperties, true);
            } else {
                abortAnimation(view, C1893R.C1897id.top_inset_animator_tag);
            }
            if (this.clipBottomAmount != expandableView.getClipBottomAmount()) {
                startClipAnimation(expandableView, animationProperties, false);
            } else {
                abortAnimation(view, C1893R.C1897id.bottom_inset_animator_tag);
            }
            expandableView.setDimmed(this.dimmed, animationFilter.animateDimmed);
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            expandableView.setHideSensitive(this.hideSensitive, animationFilter.animateHideSensitive, animationProperties.delay, animationProperties.duration);
            if (animationProperties.wasAdded(view) && !this.hidden) {
                expandableView.performAddAnimation(animationProperties.delay, animationProperties.duration, false);
            }
            if (!expandableView.isInShelf() && this.inShelf) {
                expandableView.setTransformingInShelf(true);
            }
            expandableView.setInShelf(this.inShelf);
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    private void startHeightAnimation(final ExpandableView expandableView, AnimationProperties animationProperties) {
        Integer num = (Integer) getChildTag(expandableView, C1893R.C1897id.height_animator_start_value_tag);
        Integer num2 = (Integer) getChildTag(expandableView, C1893R.C1897id.height_animator_end_value_tag);
        int i = this.height;
        if (num2 == null || num2.intValue() != i) {
            ValueAnimator valueAnimator = (ValueAnimator) getChildTag(expandableView, C1893R.C1897id.height_animator_tag);
            if (animationProperties.getAnimationFilter().animateHeight) {
                ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{expandableView.getActualHeight(), i});
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        expandableView.setActualHeight(((Integer) valueAnimator.getAnimatedValue()).intValue(), false);
                    }
                });
                ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofInt.setDuration(cancelAnimatorAndGetNewDuration(animationProperties.duration, valueAnimator));
                if (animationProperties.delay > 0 && (valueAnimator == null || valueAnimator.getAnimatedFraction() == 0.0f)) {
                    ofInt.setStartDelay(animationProperties.delay);
                }
                AnimatorListenerAdapter animationFinishListener = animationProperties.getAnimationFinishListener((Property) null);
                if (animationFinishListener != null) {
                    ofInt.addListener(animationFinishListener);
                }
                ofInt.addListener(new AnimatorListenerAdapter() {
                    boolean mWasCancelled;

                    public void onAnimationEnd(Animator animator) {
                        expandableView.setTag(C1893R.C1897id.height_animator_tag, (Object) null);
                        expandableView.setTag(C1893R.C1897id.height_animator_start_value_tag, (Object) null);
                        expandableView.setTag(C1893R.C1897id.height_animator_end_value_tag, (Object) null);
                        expandableView.setActualHeightAnimating(false);
                        if (!this.mWasCancelled) {
                            ExpandableView expandableView = expandableView;
                            if (expandableView instanceof ExpandableNotificationRow) {
                                ((ExpandableNotificationRow) expandableView).setGroupExpansionChanging(false);
                            }
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        this.mWasCancelled = false;
                    }

                    public void onAnimationCancel(Animator animator) {
                        this.mWasCancelled = true;
                    }
                });
                startAnimator(ofInt, animationFinishListener);
                expandableView.setTag(C1893R.C1897id.height_animator_tag, ofInt);
                expandableView.setTag(C1893R.C1897id.height_animator_start_value_tag, Integer.valueOf(expandableView.getActualHeight()));
                expandableView.setTag(C1893R.C1897id.height_animator_end_value_tag, Integer.valueOf(i));
                expandableView.setActualHeightAnimating(true);
            } else if (valueAnimator != null) {
                PropertyValuesHolder[] values = valueAnimator.getValues();
                int intValue = num.intValue() + (i - num2.intValue());
                values[0].setIntValues(new int[]{intValue, i});
                expandableView.setTag(C1893R.C1897id.height_animator_start_value_tag, Integer.valueOf(intValue));
                expandableView.setTag(C1893R.C1897id.height_animator_end_value_tag, Integer.valueOf(i));
                valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
            } else {
                expandableView.setActualHeight(i, false);
            }
        }
    }

    private void startClipAnimation(ExpandableView expandableView, AnimationProperties animationProperties, boolean z) {
        final ExpandableView expandableView2 = expandableView;
        AnimationProperties animationProperties2 = animationProperties;
        final boolean z2 = z;
        Integer num = (Integer) getChildTag(expandableView2, z2 ? C1893R.C1897id.top_inset_animator_start_value_tag : C1893R.C1897id.bottom_inset_animator_start_value_tag);
        int i = C1893R.C1897id.top_inset_animator_end_value_tag;
        Integer num2 = (Integer) getChildTag(expandableView2, z2 ? C1893R.C1897id.top_inset_animator_end_value_tag : C1893R.C1897id.bottom_inset_animator_end_value_tag);
        int i2 = z2 ? this.clipTopAmount : this.clipBottomAmount;
        if (num2 == null || num2.intValue() != i2) {
            int i3 = C1893R.C1897id.top_inset_animator_tag;
            ValueAnimator valueAnimator = (ValueAnimator) getChildTag(expandableView2, z2 ? C1893R.C1897id.top_inset_animator_tag : C1893R.C1897id.bottom_inset_animator_tag);
            AnimationFilter animationFilter = animationProperties.getAnimationFilter();
            if ((!z2 || animationFilter.animateTopInset) && z2) {
                int[] iArr = new int[2];
                iArr[0] = z2 ? expandableView.getClipTopAmount() : expandableView.getClipBottomAmount();
                iArr[1] = i2;
                ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                ofInt.addUpdateListener(new ExpandableViewState$$ExternalSyntheticLambda0(z2, expandableView2));
                ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofInt.setDuration(cancelAnimatorAndGetNewDuration(animationProperties2.duration, valueAnimator));
                if (animationProperties2.delay > 0 && (valueAnimator == null || valueAnimator.getAnimatedFraction() == 0.0f)) {
                    ofInt.setStartDelay(animationProperties2.delay);
                }
                AnimatorListenerAdapter animationFinishListener = animationProperties2.getAnimationFinishListener((Property) null);
                if (animationFinishListener != null) {
                    ofInt.addListener(animationFinishListener);
                }
                ofInt.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        expandableView2.setTag(z2 ? C1893R.C1897id.top_inset_animator_tag : C1893R.C1897id.bottom_inset_animator_tag, (Object) null);
                        expandableView2.setTag(z2 ? C1893R.C1897id.top_inset_animator_start_value_tag : C1893R.C1897id.bottom_inset_animator_start_value_tag, (Object) null);
                        expandableView2.setTag(z2 ? C1893R.C1897id.top_inset_animator_end_value_tag : C1893R.C1897id.bottom_inset_animator_end_value_tag, (Object) null);
                    }
                });
                startAnimator(ofInt, animationFinishListener);
                if (!z2) {
                    i3 = C1893R.C1897id.bottom_inset_animator_tag;
                }
                expandableView2.setTag(i3, ofInt);
                expandableView2.setTag(z2 ? C1893R.C1897id.top_inset_animator_start_value_tag : C1893R.C1897id.bottom_inset_animator_start_value_tag, Integer.valueOf(z2 ? expandableView.getClipTopAmount() : expandableView.getClipBottomAmount()));
                if (!z2) {
                    i = C1893R.C1897id.bottom_inset_animator_end_value_tag;
                }
                expandableView2.setTag(i, Integer.valueOf(i2));
            } else if (valueAnimator != null) {
                PropertyValuesHolder[] values = valueAnimator.getValues();
                int intValue = num.intValue() + (i2 - num2.intValue());
                values[0].setIntValues(new int[]{intValue, i2});
                expandableView2.setTag(z2 ? C1893R.C1897id.top_inset_animator_start_value_tag : C1893R.C1897id.bottom_inset_animator_start_value_tag, Integer.valueOf(intValue));
                if (!z2) {
                    i = C1893R.C1897id.bottom_inset_animator_end_value_tag;
                }
                expandableView2.setTag(i, Integer.valueOf(i2));
                valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
            } else if (z2) {
                expandableView2.setClipTopAmount(i2);
            } else {
                expandableView2.setClipBottomAmount(i2);
            }
        }
    }

    static /* synthetic */ void lambda$startClipAnimation$0(boolean z, ExpandableView expandableView, ValueAnimator valueAnimator) {
        if (z) {
            expandableView.setClipTopAmount(((Integer) valueAnimator.getAnimatedValue()).intValue());
        } else {
            expandableView.setClipBottomAmount(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    public static int getFinalActualHeight(ExpandableView expandableView) {
        if (expandableView == null) {
            return 0;
        }
        if (((ValueAnimator) getChildTag(expandableView, C1893R.C1897id.height_animator_tag)) == null) {
            return expandableView.getActualHeight();
        }
        return ((Integer) getChildTag(expandableView, C1893R.C1897id.height_animator_end_value_tag)).intValue();
    }

    public void cancelAnimations(View view) {
        super.cancelAnimations(view);
        Animator animator = (Animator) getChildTag(view, C1893R.C1897id.height_animator_tag);
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = (Animator) getChildTag(view, C1893R.C1897id.top_inset_animator_tag);
        if (animator2 != null) {
            animator2.cancel();
        }
    }
}
