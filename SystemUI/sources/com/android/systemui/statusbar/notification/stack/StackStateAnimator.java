package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class StackStateAnimator {
    public static final int ANIMATION_DELAY_PER_ELEMENT_GO_TO_FULL_SHADE = 48;
    public static final int ANIMATION_DELAY_PER_ELEMENT_INTERRUPTING = 80;
    public static final int ANIMATION_DELAY_PER_ELEMENT_MANUAL = 32;
    public static final int ANIMATION_DURATION_APPEAR_DISAPPEAR = 464;
    public static final int ANIMATION_DURATION_BLOCKING_HELPER_FADE = 240;
    public static final int ANIMATION_DURATION_CLOSE_REMOTE_INPUT = 150;
    public static final int ANIMATION_DURATION_CORNER_RADIUS = 200;
    public static final int ANIMATION_DURATION_DIMMED_ACTIVATED = 220;
    public static final int ANIMATION_DURATION_FOLD_TO_AOD = 600;
    public static final int ANIMATION_DURATION_GO_TO_FULL_SHADE = 448;
    public static final int ANIMATION_DURATION_HEADS_UP_APPEAR = 400;
    public static final int ANIMATION_DURATION_HEADS_UP_DISAPPEAR = 400;
    public static final int ANIMATION_DURATION_PRIORITY_CHANGE = 500;
    public static final int ANIMATION_DURATION_PULSE_APPEAR = 550;
    public static final int ANIMATION_DURATION_STANDARD = 360;
    public static final int ANIMATION_DURATION_SWIPE = 200;
    public static final int ANIMATION_DURATION_WAKEUP = 500;
    public static final int ANIMATION_DURATION_WAKEUP_SCRIM = 667;
    public static final int DELAY_EFFECT_MAX_INDEX_DIFFERENCE = 2;
    private static final int MAX_STAGGER_COUNT = 5;
    /* access modifiers changed from: private */
    public AnimationFilter mAnimationFilter = new AnimationFilter();
    /* access modifiers changed from: private */
    public Stack<AnimatorListenerAdapter> mAnimationListenerPool = new Stack<>();
    private final AnimationProperties mAnimationProperties;
    /* access modifiers changed from: private */
    public HashSet<Animator> mAnimatorSet = new HashSet<>();
    /* access modifiers changed from: private */
    public ValueAnimator mBottomOverScrollAnimator;
    private long mCurrentAdditionalDelay;
    private long mCurrentLength;
    private final int mGoToFullShadeAppearingTranslation;
    private HashSet<View> mHeadsUpAppearChildren = new HashSet<>();
    private int mHeadsUpAppearHeightBottom;
    private HashSet<View> mHeadsUpDisappearChildren = new HashSet<>();
    public NotificationStackScrollLayout mHostLayout;
    private StackStateLogger mLogger;
    /* access modifiers changed from: private */
    public ArrayList<View> mNewAddChildren = new ArrayList<>();
    private ArrayList<NotificationStackScrollLayout.AnimationEvent> mNewEvents = new ArrayList<>();
    private final int mPulsingAppearingTranslation;
    private boolean mShadeExpanded;
    private NotificationShelf mShelf;
    private float mStatusBarIconLocation;
    private int[] mTmpLocation = new int[2];
    private final ExpandableViewState mTmpState = new ExpandableViewState();
    /* access modifiers changed from: private */
    public ValueAnimator mTopOverScrollAnimator;
    private ArrayList<ExpandableView> mTransientViewsToRemove = new ArrayList<>();

    public StackStateAnimator(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mHostLayout = notificationStackScrollLayout;
        this.mGoToFullShadeAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(C1893R.dimen.go_to_full_shade_appearing_translation);
        this.mPulsingAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(C1893R.dimen.pulsing_notification_appear_translation);
        this.mAnimationProperties = new AnimationProperties() {
            public AnimationFilter getAnimationFilter() {
                return StackStateAnimator.this.mAnimationFilter;
            }

            public AnimatorListenerAdapter getAnimationFinishListener(Property property) {
                return StackStateAnimator.this.getGlobalAnimationFinishedListener();
            }

            public boolean wasAdded(View view) {
                return StackStateAnimator.this.mNewAddChildren.contains(view);
            }
        };
    }

    /* access modifiers changed from: protected */
    public void setLogger(StackStateLogger stackStateLogger) {
        this.mLogger = stackStateLogger;
    }

    public boolean isRunning() {
        return !this.mAnimatorSet.isEmpty();
    }

    public void startAnimationForEvents(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList, long j) {
        processAnimationEvents(arrayList);
        int childCount = this.mHostLayout.getChildCount();
        this.mAnimationFilter.applyCombination(this.mNewEvents);
        this.mCurrentAdditionalDelay = j;
        this.mCurrentLength = NotificationStackScrollLayout.AnimationEvent.combineLength(this.mNewEvents);
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) this.mHostLayout.getChildAt(i2);
            ExpandableViewState viewState = expandableView.getViewState();
            if (!(viewState == null || expandableView.getVisibility() == 8 || applyWithoutAnimation(expandableView, viewState))) {
                if (this.mAnimationProperties.wasAdded(expandableView) && i < 5) {
                    i++;
                }
                initAnimationProperties(expandableView, viewState, i);
                viewState.animateTo(expandableView, this.mAnimationProperties);
            }
        }
        if (!isRunning()) {
            onAnimationFinished();
        }
        this.mHeadsUpAppearChildren.clear();
        this.mHeadsUpDisappearChildren.clear();
        this.mNewEvents.clear();
        this.mNewAddChildren.clear();
    }

    private void initAnimationProperties(ExpandableView expandableView, ExpandableViewState expandableViewState, int i) {
        boolean wasAdded = this.mAnimationProperties.wasAdded(expandableView);
        this.mAnimationProperties.duration = this.mCurrentLength;
        adaptDurationWhenGoingToFullShade(expandableView, expandableViewState, wasAdded, i);
        this.mAnimationProperties.delay = 0;
        if (!wasAdded) {
            if (!this.mAnimationFilter.hasDelays) {
                return;
            }
            if (expandableViewState.yTranslation == expandableView.getTranslationY() && expandableViewState.zTranslation == expandableView.getTranslationZ() && expandableViewState.alpha == expandableView.getAlpha() && expandableViewState.height == expandableView.getActualHeight() && expandableViewState.clipTopAmount == expandableView.getClipTopAmount()) {
                return;
            }
        }
        this.mAnimationProperties.delay = this.mCurrentAdditionalDelay + calculateChildAnimationDelay(expandableViewState, i);
    }

    private void adaptDurationWhenGoingToFullShade(ExpandableView expandableView, ExpandableViewState expandableViewState, boolean z, int i) {
        boolean z2 = expandableView instanceof StackScrollerDecorView;
        int i2 = 0;
        if ((z || z2) && this.mAnimationFilter.hasGoToFullShadeEvent) {
            if (!z2) {
                i2 = this.mGoToFullShadeAppearingTranslation;
                this.mAnimationProperties.duration = ((long) (((float) Math.pow((double) i, 0.699999988079071d)) * 100.0f)) + 514;
            }
            expandableView.setTranslationY(expandableViewState.yTranslation + ((float) i2));
        }
    }

    private boolean applyWithoutAnimation(ExpandableView expandableView, ExpandableViewState expandableViewState) {
        if (this.mShadeExpanded || ViewState.isAnimatingY(expandableView) || this.mHeadsUpDisappearChildren.contains(expandableView) || this.mHeadsUpAppearChildren.contains(expandableView) || NotificationStackScrollLayout.isPinnedHeadsUp(expandableView)) {
            return false;
        }
        expandableViewState.applyToView(expandableView);
        return true;
    }

    private long calculateChildAnimationDelay(ExpandableViewState expandableViewState, int i) {
        ExpandableView expandableView;
        if (this.mAnimationFilter.hasGoToFullShadeEvent) {
            return calculateDelayGoToFullShade(expandableViewState, i);
        }
        if (this.mAnimationFilter.customDelay != -1) {
            return this.mAnimationFilter.customDelay;
        }
        Iterator<NotificationStackScrollLayout.AnimationEvent> it = this.mNewEvents.iterator();
        long j = 0;
        while (it.hasNext()) {
            NotificationStackScrollLayout.AnimationEvent next = it.next();
            int i2 = next.animationType;
            long j2 = 80;
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 == 2) {
                        j2 = 32;
                    }
                }
                int i3 = expandableViewState.notGoneIndex;
                if (next.viewAfterChangingView == null) {
                    expandableView = this.mHostLayout.getLastChildNotGone();
                } else {
                    expandableView = (ExpandableView) next.viewAfterChangingView;
                }
                if (expandableView != null) {
                    int i4 = expandableView.getViewState().notGoneIndex;
                    if (i3 >= i4) {
                        i3++;
                    }
                    j = Math.max(((long) Math.max(0, Math.min(2, Math.abs(i3 - i4) - 1))) * j2, j);
                }
            } else {
                j = Math.max(((long) (2 - Math.max(0, Math.min(2, Math.abs(expandableViewState.notGoneIndex - next.mChangingView.getViewState().notGoneIndex) - 1)))) * 80, j);
            }
        }
        return j;
    }

    private long calculateDelayGoToFullShade(ExpandableViewState expandableViewState, int i) {
        int notGoneIndex = this.mShelf.getNotGoneIndex();
        float f = (float) expandableViewState.notGoneIndex;
        float f2 = (float) notGoneIndex;
        long j = 0;
        if (f > f2) {
            j = 0 + ((long) (((double) (((float) Math.pow((double) i, 0.699999988079071d)) * 48.0f)) * 0.25d));
            f = f2;
        }
        return j + ((long) (((float) Math.pow((double) f, 0.699999988079071d)) * 48.0f));
    }

    /* access modifiers changed from: private */
    public AnimatorListenerAdapter getGlobalAnimationFinishedListener() {
        if (!this.mAnimationListenerPool.empty()) {
            return this.mAnimationListenerPool.pop();
        }
        return new AnimatorListenerAdapter() {
            private boolean mWasCancelled;

            public void onAnimationEnd(Animator animator) {
                StackStateAnimator.this.mAnimatorSet.remove(animator);
                if (StackStateAnimator.this.mAnimatorSet.isEmpty() && !this.mWasCancelled) {
                    StackStateAnimator.this.onAnimationFinished();
                }
                StackStateAnimator.this.mAnimationListenerPool.push(this);
            }

            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }

            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                StackStateAnimator.this.mAnimatorSet.add(animator);
            }
        };
    }

    /* access modifiers changed from: private */
    public void onAnimationFinished() {
        this.mHostLayout.onChildAnimationFinished();
        Iterator<ExpandableView> it = this.mTransientViewsToRemove.iterator();
        while (it.hasNext()) {
            it.next().removeFromTransientContainer();
        }
        this.mTransientViewsToRemove.clear();
    }

    /* JADX WARNING: Removed duplicated region for block: B:84:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void processAnimationEvents(java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.AnimationEvent> r14) {
        /*
            r13 = this;
            java.util.Iterator r14 = r14.iterator()
        L_0x0004:
            boolean r0 = r14.hasNext()
            if (r0 == 0) goto L_0x01fd
            java.lang.Object r0 = r14.next()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent r0 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.AnimationEvent) r0
            com.android.systemui.statusbar.notification.row.ExpandableView r8 = r0.mChangingView
            boolean r1 = r8 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            r2 = 1
            r3 = 0
            r4 = 0
            if (r1 == 0) goto L_0x002f
            com.android.systemui.statusbar.notification.stack.StackStateLogger r5 = r13.mLogger
            if (r5 == 0) goto L_0x002f
            r5 = r8
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r5
            boolean r6 = r5.isHeadsUp()
            com.android.systemui.statusbar.notification.collection.NotificationEntry r5 = r5.getEntry()
            java.lang.String r5 = r5.getKey()
            r10 = r2
            r9 = r5
            goto L_0x0032
        L_0x002f:
            r9 = r3
            r6 = r4
            r10 = r6
        L_0x0032:
            int r5 = r0.animationType
            if (r5 != 0) goto L_0x0054
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r1 = r8.getViewState()
            if (r1 == 0) goto L_0x0004
            boolean r2 = r1.gone
            if (r2 == 0) goto L_0x0041
            goto L_0x0004
        L_0x0041:
            if (r10 == 0) goto L_0x004a
            if (r6 == 0) goto L_0x004a
            com.android.systemui.statusbar.notification.stack.StackStateLogger r2 = r13.mLogger
            r2.logHUNViewAppearingWithAddEvent(r9)
        L_0x004a:
            r1.applyToView(r8)
            java.util.ArrayList<android.view.View> r1 = r13.mNewAddChildren
            r1.add(r8)
            goto L_0x01f6
        L_0x0054:
            int r5 = r0.animationType
            if (r5 != r2) goto L_0x00dd
            int r2 = r8.getVisibility()
            if (r2 == 0) goto L_0x0062
            r8.removeFromTransientContainer()
            goto L_0x0004
        L_0x0062:
            android.view.View r2 = r0.viewAfterChangingView
            r3 = -1082130432(0xffffffffbf800000, float:-1.0)
            if (r2 == 0) goto L_0x00b4
            float r2 = r8.getTranslationY()
            if (r1 == 0) goto L_0x0091
            android.view.View r1 = r0.viewAfterChangingView
            boolean r1 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r1 == 0) goto L_0x0091
            r1 = r8
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
            android.view.View r4 = r0.viewAfterChangingView
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r4
            boolean r5 = r1.isRemoved()
            if (r5 == 0) goto L_0x0091
            boolean r5 = r1.wasChildInGroupWhenRemoved()
            if (r5 == 0) goto L_0x0091
            boolean r4 = r4.isChildInGroup()
            if (r4 != 0) goto L_0x0091
            float r2 = r1.getTranslationWhenRemoved()
        L_0x0091:
            int r1 = r8.getActualHeight()
            android.view.View r4 = r0.viewAfterChangingView
            com.android.systemui.statusbar.notification.row.ExpandableView r4 = (com.android.systemui.statusbar.notification.row.ExpandableView) r4
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r4 = r4.getViewState()
            float r4 = r4.yTranslation
            float r1 = (float) r1
            r5 = 1073741824(0x40000000, float:2.0)
            float r7 = r1 / r5
            float r2 = r2 + r7
            float r4 = r4 - r2
            float r4 = r4 * r5
            float r4 = r4 / r1
            r1 = 1065353216(0x3f800000, float:1.0)
            float r1 = java.lang.Math.min((float) r4, (float) r1)
            float r1 = java.lang.Math.max((float) r1, (float) r3)
            r7 = r1
            goto L_0x00b5
        L_0x00b4:
            r7 = r3
        L_0x00b5:
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda0 r1 = new com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda0
            r1.<init>(r8)
            if (r10 == 0) goto L_0x00cb
            if (r6 == 0) goto L_0x00cb
            com.android.systemui.statusbar.notification.stack.StackStateLogger r1 = r13.mLogger
            r1.logHUNViewDisappearingWithRemoveEvent(r9)
            com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda1 r1 = new com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda1
            r1.<init>(r13, r9, r8)
        L_0x00cb:
            r9 = r1
            r2 = 464(0x1d0, double:2.29E-321)
            r4 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r1 = r8
            r6 = r7
            r7 = r10
            r8 = r11
            r10 = r12
            r1.performRemoveAnimation(r2, r4, r6, r7, r8, r9, r10)
            goto L_0x01f6
        L_0x00dd:
            int r5 = r0.animationType
            r6 = 2
            if (r5 != r6) goto L_0x00ef
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r13.mHostLayout
            boolean r1 = r1.isFullySwipedOut(r8)
            if (r1 == 0) goto L_0x01f6
            r8.removeFromTransientContainer()
            goto L_0x01f6
        L_0x00ef:
            int r5 = r0.animationType
            r6 = 10
            if (r5 != r6) goto L_0x00fe
            com.android.systemui.statusbar.notification.row.ExpandableView r1 = r0.mChangingView
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
            r1.prepareExpansionChanged()
            goto L_0x01f6
        L_0x00fe:
            int r5 = r0.animationType
            r6 = 11
            if (r5 != r6) goto L_0x013f
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r1 = r8.getViewState()
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r2 = r13.mTmpState
            r2.copyFrom(r1)
            boolean r1 = r0.headsUpFromBottom
            if (r1 == 0) goto L_0x0119
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r1 = r13.mTmpState
            int r2 = r13.mHeadsUpAppearHeightBottom
            float r2 = (float) r2
            r1.yTranslation = r2
            goto L_0x012c
        L_0x0119:
            if (r10 == 0) goto L_0x0122
            com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda2 r1 = new com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda2
            r1.<init>(r13, r9)
            r7 = r1
            goto L_0x0123
        L_0x0122:
            r7 = r3
        L_0x0123:
            r2 = 0
            r4 = 400(0x190, double:1.976E-321)
            r6 = 1
            r1 = r8
            r1.performAddAnimation(r2, r4, r6, r7)
        L_0x012c:
            java.util.HashSet<android.view.View> r1 = r13.mHeadsUpAppearChildren
            r1.add(r8)
            if (r10 == 0) goto L_0x0138
            com.android.systemui.statusbar.notification.stack.StackStateLogger r1 = r13.mLogger
            r1.logHUNViewAppearing(r9)
        L_0x0138:
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r1 = r13.mTmpState
            r1.applyToView(r8)
            goto L_0x01f6
        L_0x013f:
            int r5 = r0.animationType
            r6 = 12
            if (r5 == r6) goto L_0x014b
            int r5 = r0.animationType
            r6 = 13
            if (r5 != r6) goto L_0x01f6
        L_0x014b:
            java.util.HashSet<android.view.View> r5 = r13.mHeadsUpDisappearChildren
            r5.add(r8)
            android.view.ViewParent r5 = r8.getParent()
            if (r5 != 0) goto L_0x016d
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r3 = r13.mHostLayout
            r3.addTransientView(r8, r4)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r3 = r13.mHostLayout
            r8.setTransientContainer(r3)
            com.android.systemui.statusbar.notification.stack.ExpandableViewState r3 = r13.mTmpState
            r3.initFrom(r8)
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda0 r3 = new com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda0
            r3.<init>(r8)
        L_0x016d:
            r5 = 0
            if (r1 == 0) goto L_0x01c7
            r1 = r8
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
            boolean r6 = r1.isDismissed()
            r2 = r2 ^ r6
            com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r1.getEntry()
            com.android.systemui.statusbar.notification.icon.IconPack r6 = r1.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r6 = r6.getStatusBarIcon()
            com.android.systemui.statusbar.notification.icon.IconPack r1 = r1.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r1 = r1.getCenteredIcon()
            if (r1 == 0) goto L_0x0195
            android.view.ViewParent r7 = r1.getParent()
            if (r7 == 0) goto L_0x0195
            r6 = r1
        L_0x0195:
            android.view.ViewParent r1 = r6.getParent()
            if (r1 == 0) goto L_0x01c7
            int[] r1 = r13.mTmpLocation
            r6.getLocationOnScreen(r1)
            int[] r1 = r13.mTmpLocation
            r1 = r1[r4]
            float r1 = (float) r1
            float r5 = r6.getTranslationX()
            float r1 = r1 - r5
            float r5 = com.android.systemui.statusbar.notification.stack.ViewState.getFinalTranslationX(r6)
            float r1 = r1 + r5
            int r5 = r6.getWidth()
            float r5 = (float) r5
            r6 = 1048576000(0x3e800000, float:0.25)
            float r5 = r5 * r6
            float r1 = r1 + r5
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r5 = r13.mHostLayout
            int[] r6 = r13.mTmpLocation
            r5.getLocationOnScreen(r6)
            int[] r5 = r13.mTmpLocation
            r4 = r5[r4]
            float r4 = (float) r4
            float r1 = r1 - r4
            r11 = r1
            goto L_0x01c8
        L_0x01c7:
            r11 = r5
        L_0x01c8:
            if (r2 == 0) goto L_0x01f1
            if (r10 == 0) goto L_0x01d8
            com.android.systemui.statusbar.notification.stack.StackStateLogger r1 = r13.mLogger
            r1.logHUNViewDisappearing(r9)
            com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda3 r1 = new com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda3
            r1.<init>(r13, r9, r3)
            r9 = r1
            goto L_0x01d9
        L_0x01d8:
            r9 = r3
        L_0x01d9:
            r2 = 400(0x190, double:1.976E-321)
            r4 = 0
            r6 = 0
            r7 = 1
            android.animation.AnimatorListenerAdapter r10 = r13.getGlobalAnimationFinishedListener()
            r1 = r8
            r8 = r11
            long r1 = r1.performRemoveAnimation(r2, r4, r6, r7, r8, r9, r10)
            com.android.systemui.statusbar.notification.stack.AnimationProperties r3 = r13.mAnimationProperties
            long r4 = r3.delay
            long r4 = r4 + r1
            r3.delay = r4
            goto L_0x01f6
        L_0x01f1:
            if (r3 == 0) goto L_0x01f6
            r3.run()
        L_0x01f6:
            java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent> r1 = r13.mNewEvents
            r1.add(r0)
            goto L_0x0004
        L_0x01fd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.StackStateAnimator.processAnimationEvents(java.util.ArrayList):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$processAnimationEvents$0$com-android-systemui-statusbar-notification-stack-StackStateAnimator */
    public /* synthetic */ void mo42623x5b6566b8(String str, ExpandableView expandableView) {
        this.mLogger.disappearAnimationEnded(str);
        expandableView.removeFromTransientContainer();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$processAnimationEvents$1$com-android-systemui-statusbar-notification-stack-StackStateAnimator */
    public /* synthetic */ void mo42624x9445c757(String str) {
        this.mLogger.appearAnimationEnded(str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$processAnimationEvents$2$com-android-systemui-statusbar-notification-stack-StackStateAnimator */
    public /* synthetic */ void mo42625xcd2627f6(String str, Runnable runnable) {
        this.mLogger.disappearAnimationEnded(str);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void animateOverScrollToAmount(float f, final boolean z, final boolean z2) {
        float currentOverScrollAmount = this.mHostLayout.getCurrentOverScrollAmount(z);
        if (f != currentOverScrollAmount) {
            cancelOverScrollAnimators(z);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{currentOverScrollAmount, f});
            ofFloat.setDuration(360);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StackStateAnimator.this.mHostLayout.setOverScrollAmount(((Float) valueAnimator.getAnimatedValue()).floatValue(), z, false, false, z2);
                }
            });
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (z) {
                        ValueAnimator unused = StackStateAnimator.this.mTopOverScrollAnimator = null;
                    } else {
                        ValueAnimator unused2 = StackStateAnimator.this.mBottomOverScrollAnimator = null;
                    }
                }
            });
            ofFloat.start();
            if (z) {
                this.mTopOverScrollAnimator = ofFloat;
            } else {
                this.mBottomOverScrollAnimator = ofFloat;
            }
        }
    }

    public void cancelOverScrollAnimators(boolean z) {
        ValueAnimator valueAnimator = z ? this.mTopOverScrollAnimator : this.mBottomOverScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void setHeadsUpAppearHeightBottom(int i) {
        this.mHeadsUpAppearHeightBottom = i;
    }

    public void setShadeExpanded(boolean z) {
        this.mShadeExpanded = z;
    }

    public void setShelf(NotificationShelf notificationShelf) {
        this.mShelf = notificationShelf;
    }
}
