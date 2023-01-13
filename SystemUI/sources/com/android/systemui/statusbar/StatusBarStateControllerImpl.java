package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.format.DateFormat;
import android.util.FloatProperty;
import android.util.Log;
import android.view.Choreographer;
import android.view.InsetsFlags;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.Interpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class StatusBarStateControllerImpl implements SysuiStatusBarStateController, CallbackController<StatusBarStateController.StateListener>, Dumpable {
    private static final boolean DEBUG_IMMERSIVE_APPS = SystemProperties.getBoolean("persist.debug.immersive_apps", false);
    private static final int HISTORY_SIZE = 32;
    private static final int MAX_STATE = 2;
    private static final int MIN_STATE = 0;
    private static final FloatProperty<StatusBarStateControllerImpl> SET_DARK_AMOUNT_PROPERTY = new FloatProperty<StatusBarStateControllerImpl>("mDozeAmount") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(StatusBarStateControllerImpl statusBarStateControllerImpl, float f) {
            statusBarStateControllerImpl.setDozeAmountInternal(f);
        }

        public Float get(StatusBarStateControllerImpl statusBarStateControllerImpl) {
            return Float.valueOf(statusBarStateControllerImpl.mDozeAmount);
        }
    };
    private static final String TAG = "SbStateController";
    private static final Comparator<SysuiStatusBarStateController.RankedListener> sComparator = Comparator.comparingInt(new StatusBarStateControllerImpl$$ExternalSyntheticLambda2());
    private ValueAnimator mDarkAnimator;
    /* access modifiers changed from: private */
    public float mDozeAmount;
    /* access modifiers changed from: private */
    public float mDozeAmountTarget;
    private Interpolator mDozeInterpolator;
    private HistoricalState[] mHistoricalRecords;
    private int mHistoryIndex;
    private final InteractionJankMonitor mInteractionJankMonitor;
    private boolean mIsDozing;
    private boolean mIsExpanded;
    private boolean mIsFullscreen;
    private boolean mKeyguardRequested;
    private int mLastState;
    private boolean mLeaveOpenOnKeyguardHide;
    private final ArrayList<SysuiStatusBarStateController.RankedListener> mListeners = new ArrayList<>();
    private boolean mPulsing;
    private int mState;
    private final UiEventLogger mUiEventLogger;
    private int mUpcomingState;
    private View mView;

    @Inject
    public StatusBarStateControllerImpl(UiEventLogger uiEventLogger, DumpManager dumpManager, InteractionJankMonitor interactionJankMonitor) {
        this.mHistoryIndex = 0;
        this.mHistoricalRecords = new HistoricalState[32];
        this.mIsFullscreen = false;
        this.mDozeInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        this.mUiEventLogger = uiEventLogger;
        this.mInteractionJankMonitor = interactionJankMonitor;
        for (int i = 0; i < 32; i++) {
            this.mHistoricalRecords[i] = new HistoricalState();
        }
        dumpManager.registerDumpable(this);
    }

    public int getState() {
        return this.mState;
    }

    public boolean setState(int i, boolean z) {
        if (i > 2 || i < 0) {
            throw new IllegalArgumentException("Invalid state " + i);
        } else if (!z && i == this.mState && i == this.mUpcomingState) {
            return false;
        } else {
            if (i != this.mUpcomingState) {
                Log.d(TAG, "setState: requested state " + StatusBarState.toString(i) + "!= upcomingState: " + StatusBarState.toString(this.mUpcomingState) + ". This usually means the status bar state transition was interrupted before the upcoming state could be applied.");
            }
            recordHistoricalState(i, this.mState, false);
            if (this.mState == 0 && i == 2) {
                Log.e(TAG, "Invalid state transition: SHADE -> SHADE_LOCKED", new Throwable());
            }
            synchronized (this.mListeners) {
                String str = getClass().getSimpleName() + "#setState(" + i + NavigationBarInflaterView.KEY_CODE_END;
                DejankUtils.startDetectingBlockingIpcs(str);
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onStatePreChange(this.mState, i);
                }
                this.mLastState = this.mState;
                this.mState = i;
                updateUpcomingState(i);
                this.mUiEventLogger.log(StatusBarStateEvent.fromState(this.mState));
                Trace.instantForTrack(4096, "UI Events", "StatusBarState " + str);
                Iterator it2 = new ArrayList(this.mListeners).iterator();
                while (it2.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it2.next()).mListener.onStateChanged(this.mState);
                }
                Iterator it3 = new ArrayList(this.mListeners).iterator();
                while (it3.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it3.next()).mListener.onStatePostChange();
                }
                DejankUtils.stopDetectingBlockingIpcs(str);
            }
            return true;
        }
    }

    public void setUpcomingState(int i) {
        recordHistoricalState(i, this.mState, true);
        updateUpcomingState(i);
    }

    private void updateUpcomingState(int i) {
        if (this.mUpcomingState != i) {
            this.mUpcomingState = i;
            Iterator it = new ArrayList(this.mListeners).iterator();
            while (it.hasNext()) {
                ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onUpcomingStateChanged(this.mUpcomingState);
            }
        }
    }

    public int getCurrentOrUpcomingState() {
        return this.mUpcomingState;
    }

    public boolean isDozing() {
        return this.mIsDozing;
    }

    public boolean isPulsing() {
        return this.mPulsing;
    }

    public float getDozeAmount() {
        return this.mDozeAmount;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public boolean setPanelExpanded(boolean z) {
        if (this.mIsExpanded == z) {
            return false;
        }
        this.mIsExpanded = z;
        String str = getClass().getSimpleName() + "#setIsExpanded";
        DejankUtils.startDetectingBlockingIpcs(str);
        Iterator it = new ArrayList(this.mListeners).iterator();
        while (it.hasNext()) {
            ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onExpandedChanged(this.mIsExpanded);
        }
        DejankUtils.stopDetectingBlockingIpcs(str);
        return true;
    }

    public float getInterpolatedDozeAmount() {
        return this.mDozeInterpolator.getInterpolation(this.mDozeAmount);
    }

    public boolean setIsDozing(boolean z) {
        if (this.mIsDozing == z) {
            return false;
        }
        this.mIsDozing = z;
        synchronized (this.mListeners) {
            String str = getClass().getSimpleName() + "#setIsDozing";
            DejankUtils.startDetectingBlockingIpcs(str);
            Iterator it = new ArrayList(this.mListeners).iterator();
            while (it.hasNext()) {
                ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozingChanged(z);
            }
            DejankUtils.stopDetectingBlockingIpcs(str);
        }
        return true;
    }

    public void setDozeAmount(float f, boolean z) {
        setAndInstrumentDozeAmount((View) null, f, z);
    }

    public void setAndInstrumentDozeAmount(View view, float f, boolean z) {
        ValueAnimator valueAnimator = this.mDarkAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            if (!z || this.mDozeAmountTarget != f) {
                this.mDarkAnimator.cancel();
            } else {
                return;
            }
        }
        View view2 = this.mView;
        if ((view2 == null || !view2.isAttachedToWindow()) && view != null && view.isAttachedToWindow()) {
            this.mView = view;
        }
        this.mDozeAmountTarget = f;
        if (z) {
            startDozeAnimation();
        } else {
            setDozeAmountInternal(f);
        }
    }

    private void startDozeAnimation() {
        Interpolator interpolator;
        float f = this.mDozeAmount;
        if (f == 0.0f || f == 1.0f) {
            if (this.mIsDozing) {
                interpolator = Interpolators.FAST_OUT_SLOW_IN;
            } else {
                interpolator = Interpolators.TOUCH_RESPONSE_REVERSE;
            }
            this.mDozeInterpolator = interpolator;
        }
        if (this.mDozeAmount == 1.0f && !this.mIsDozing) {
            setDozeAmountInternal(0.99f);
        }
        this.mDarkAnimator = createDarkAnimator();
    }

    /* access modifiers changed from: protected */
    public ObjectAnimator createDarkAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, SET_DARK_AMOUNT_PROPERTY, new float[]{this.mDozeAmountTarget});
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.setDuration(500);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                NTLogUtil.m1686d(StatusBarStateControllerImpl.TAG, "onAnimationCancel mDozeAmount: " + StatusBarStateControllerImpl.this.mDozeAmount);
                StatusBarStateControllerImpl.this.cancelInteractionJankMonitor();
            }

            public void onAnimationEnd(Animator animator) {
                NTLogUtil.m1686d(StatusBarStateControllerImpl.TAG, "onAnimationEnd mDozeAmount: " + StatusBarStateControllerImpl.this.mDozeAmount);
                StatusBarStateControllerImpl.this.endInteractionJankMonitor();
            }

            public void onAnimationStart(Animator animator) {
                NTLogUtil.m1686d(StatusBarStateControllerImpl.TAG, "onAnimationStart mDozeAmount: " + StatusBarStateControllerImpl.this.mDozeAmount + " mDozeAmountTarget: " + StatusBarStateControllerImpl.this.mDozeAmountTarget);
                StatusBarStateControllerImpl.this.beginInteractionJankMonitor();
            }
        });
        ofFloat.start();
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public void setDozeAmountInternal(float f) {
        if (Float.compare(f, this.mDozeAmount) != 0) {
            this.mDozeAmount = f;
            float interpolation = this.mDozeInterpolator.getInterpolation(f);
            synchronized (this.mListeners) {
                String str = getClass().getSimpleName() + "#setDozeAmount";
                DejankUtils.startDetectingBlockingIpcs(str);
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozeAmountChanged(this.mDozeAmount, interpolation);
                }
                DejankUtils.stopDetectingBlockingIpcs(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public void beginInteractionJankMonitor() {
        View view;
        boolean z = this.mIsDozing;
        boolean z2 = (z && this.mDozeAmount == 0.0f) || (!z && this.mDozeAmount == 1.0f);
        if (this.mInteractionJankMonitor != null && (view = this.mView) != null && view.isAttachedToWindow()) {
            if (z2) {
                Choreographer.getInstance().postCallback(1, new StatusBarStateControllerImpl$$ExternalSyntheticLambda1(this), (Object) null);
            } else {
                this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(getCujType(), this.mView).setDeferMonitorForAnimationStart(false));
            }
        }
    }

    /* access modifiers changed from: private */
    public void endInteractionJankMonitor() {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            interactionJankMonitor.end(getCujType());
        }
    }

    /* access modifiers changed from: private */
    public void cancelInteractionJankMonitor() {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            interactionJankMonitor.cancel(getCujType());
        }
    }

    private int getCujType() {
        return this.mIsDozing ? 24 : 23;
    }

    public boolean goingToFullShade() {
        return this.mState == 0 && this.mLeaveOpenOnKeyguardHide;
    }

    public void setLeaveOpenOnKeyguardHide(boolean z) {
        this.mLeaveOpenOnKeyguardHide = z;
    }

    public boolean leaveOpenOnKeyguardHide() {
        return this.mLeaveOpenOnKeyguardHide;
    }

    public boolean fromShadeLocked() {
        return this.mLastState == 2;
    }

    public void addCallback(StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, Integer.MAX_VALUE);
        }
    }

    @Deprecated
    public void addCallback(StatusBarStateController.StateListener stateListener, int i) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, i);
        }
    }

    private void addListenerInternalLocked(StatusBarStateController.StateListener stateListener, int i) {
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (it.next().mListener.equals(stateListener)) {
                return;
            }
        }
        this.mListeners.add(new SysuiStatusBarStateController.RankedListener(stateListener, i));
        this.mListeners.sort(sComparator);
    }

    public void removeCallback(StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            this.mListeners.removeIf(new StatusBarStateControllerImpl$$ExternalSyntheticLambda0(stateListener));
        }
    }

    public void setKeyguardRequested(boolean z) {
        this.mKeyguardRequested = z;
    }

    public boolean isKeyguardRequested() {
        return this.mKeyguardRequested;
    }

    public void setSystemBarAttributes(int i, int i2, InsetsVisibilities insetsVisibilities, String str) {
        boolean z = false;
        boolean z2 = !insetsVisibilities.getVisibility(0) || !insetsVisibilities.getVisibility(1);
        if (this.mIsFullscreen != z2) {
            this.mIsFullscreen = z2;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onFullscreenStateChanged(z2);
                }
            }
        }
        if (DEBUG_IMMERSIVE_APPS) {
            if ((i & 4) != 0) {
                z = true;
            }
            String flagsToString = ViewDebug.flagsToString(InsetsFlags.class, "behavior", i2);
            String insetsVisibilities2 = insetsVisibilities.toString();
            if (insetsVisibilities2.isEmpty()) {
                insetsVisibilities2 = "none";
            }
            Log.d(TAG, str + " dim=" + z + " behavior=" + flagsToString + " requested visibilities: " + insetsVisibilities2);
        }
    }

    public void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onPulsingChanged(z);
                }
            }
        }
    }

    public static String describe(int i) {
        return StatusBarState.toString(i);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarStateController: ");
        printWriter.println(" mState=" + this.mState + " (" + describe(this.mState) + NavigationBarInflaterView.KEY_CODE_END);
        printWriter.println(" mLastState=" + this.mLastState + " (" + describe(this.mLastState) + NavigationBarInflaterView.KEY_CODE_END);
        printWriter.println(" mLeaveOpenOnKeyguardHide=" + this.mLeaveOpenOnKeyguardHide);
        printWriter.println(" mKeyguardRequested=" + this.mKeyguardRequested);
        printWriter.println(" mIsDozing=" + this.mIsDozing);
        printWriter.println(" mListeners{" + this.mListeners.size() + "}=");
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            printWriter.println("    " + it.next().mListener);
        }
        printWriter.println(" Historical states:");
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            if (this.mHistoricalRecords[i2].mTimestamp != 0) {
                i++;
            }
        }
        for (int i3 = this.mHistoryIndex + 32; i3 >= ((this.mHistoryIndex + 32) - i) + 1; i3--) {
            printWriter.println("  (" + (((this.mHistoryIndex + 32) - i3) + 1) + NavigationBarInflaterView.KEY_CODE_END + this.mHistoricalRecords[i3 & 31]);
        }
    }

    private void recordHistoricalState(int i, int i2, boolean z) {
        Trace.traceCounter(4096, "statusBarState", i);
        int i3 = (this.mHistoryIndex + 1) % 32;
        this.mHistoryIndex = i3;
        HistoricalState historicalState = this.mHistoricalRecords[i3];
        historicalState.mNewState = i;
        historicalState.mLastState = i2;
        historicalState.mTimestamp = System.currentTimeMillis();
        historicalState.mUpcoming = z;
    }

    private static class HistoricalState {
        int mLastState;
        int mNewState;
        long mTimestamp;
        boolean mUpcoming;

        private HistoricalState() {
        }

        public String toString() {
            if (this.mTimestamp == 0) {
                return "Empty " + getClass().getSimpleName();
            }
            StringBuilder sb = new StringBuilder();
            if (this.mUpcoming) {
                sb.append("upcoming-");
            }
            sb.append("newState=").append(this.mNewState).append(NavigationBarInflaterView.KEY_CODE_START).append(StatusBarStateControllerImpl.describe(this.mNewState)).append(") lastState=");
            sb.append(this.mLastState).append(NavigationBarInflaterView.KEY_CODE_START).append(StatusBarStateControllerImpl.describe(this.mLastState)).append(") timestamp=");
            sb.append(DateFormat.format("MM-dd HH:mm:ss", this.mTimestamp));
            return sb.toString();
        }
    }
}
