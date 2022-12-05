package com.android.systemui.classifier;

import android.net.Uri;
import android.os.Build;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class BrightLineFalsingManager implements FalsingManager {
    public static final boolean DEBUG = Log.isLoggable("FalsingManager", 3);
    private static final Queue<String> RECENT_INFO_LOG = new ArrayDeque(41);
    private static final Queue<DebugSwipeRecord> RECENT_SWIPES = new ArrayDeque(21);
    private AccessibilityManager mAccessibilityManager;
    private final HistoryTracker.BeliefListener mBeliefListener;
    private final Collection<FalsingClassifier> mClassifiers;
    private final FalsingDataProvider mDataProvider;
    private boolean mDestroyed;
    private final DoubleTapClassifier mDoubleTapClassifier;
    private final FalsingDataProvider.GestureFinalizedListener mGestureFinalizedListener;
    private final HistoryTracker mHistoryTracker;
    private int mIsFalseTouchCalls;
    private final KeyguardStateController mKeyguardStateController;
    private final MetricsLogger mMetricsLogger;
    private Collection<FalsingClassifier.Result> mPriorResults;
    private final FalsingDataProvider.SessionListener mSessionListener;
    private final SingleTapClassifier mSingleTapClassifier;
    private final boolean mTestHarness;
    private final List<FalsingManager.FalsingBeliefListener> mFalsingBeliefListeners = new ArrayList();
    private List<FalsingManager.FalsingTapListener> mFalsingTapListeners = new ArrayList();
    private int mPriorInteractionType = 7;

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isClassifierEnabled() {
        return true;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isReportingEnabled() {
        return false;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isUnlockingDisabled() {
        return false;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public Uri reportRejectedTouch() {
        return null;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean shouldEnforceBouncer() {
        return false;
    }

    /* renamed from: com.android.systemui.classifier.BrightLineFalsingManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass3 implements FalsingDataProvider.GestureFinalizedListener {
        AnonymousClass3() {
        }

        @Override // com.android.systemui.classifier.FalsingDataProvider.GestureFinalizedListener
        public void onGestureFinalized(long j) {
            if (BrightLineFalsingManager.this.mPriorResults != null) {
                boolean anyMatch = BrightLineFalsingManager.this.mPriorResults.stream().anyMatch(BrightLineFalsingManager$3$$ExternalSyntheticLambda2.INSTANCE);
                BrightLineFalsingManager.this.mPriorResults.forEach(BrightLineFalsingManager$3$$ExternalSyntheticLambda0.INSTANCE);
                if (Build.IS_ENG || Build.IS_USERDEBUG) {
                    BrightLineFalsingManager.RECENT_SWIPES.add(new DebugSwipeRecord(anyMatch, BrightLineFalsingManager.this.mPriorInteractionType, (List) BrightLineFalsingManager.this.mDataProvider.getRecentMotionEvents().stream().map(BrightLineFalsingManager$3$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList())));
                    while (BrightLineFalsingManager.RECENT_SWIPES.size() > 40) {
                        BrightLineFalsingManager.RECENT_SWIPES.remove();
                    }
                }
                BrightLineFalsingManager.this.mHistoryTracker.addResults(BrightLineFalsingManager.this.mPriorResults, j);
                BrightLineFalsingManager.this.mPriorResults = null;
                BrightLineFalsingManager.this.mPriorInteractionType = 7;
                return;
            }
            BrightLineFalsingManager.this.mHistoryTracker.addResults(Collections.singleton(FalsingClassifier.Result.falsed(BrightLineFalsingManager.this.mSingleTapClassifier.isTap(BrightLineFalsingManager.this.mDataProvider.getRecentMotionEvents(), 0.0d).isFalse() ? 0.7d : 0.8d, AnonymousClass3.class.getSimpleName(), "unclassified")), j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onGestureFinalized$0(FalsingClassifier.Result result) {
            String reason;
            if (!result.isFalse() || (reason = result.getReason()) == null) {
                return;
            }
            BrightLineFalsingManager.logInfo(reason);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ XYDt lambda$onGestureFinalized$1(MotionEvent motionEvent) {
            return new XYDt((int) motionEvent.getX(), (int) motionEvent.getY(), (int) (motionEvent.getEventTime() - motionEvent.getDownTime()));
        }
    }

    public BrightLineFalsingManager(FalsingDataProvider falsingDataProvider, MetricsLogger metricsLogger, Set<FalsingClassifier> set, SingleTapClassifier singleTapClassifier, DoubleTapClassifier doubleTapClassifier, HistoryTracker historyTracker, KeyguardStateController keyguardStateController, AccessibilityManager accessibilityManager, boolean z) {
        FalsingDataProvider.SessionListener sessionListener = new FalsingDataProvider.SessionListener() { // from class: com.android.systemui.classifier.BrightLineFalsingManager.1
            @Override // com.android.systemui.classifier.FalsingDataProvider.SessionListener
            public void onSessionEnded() {
                BrightLineFalsingManager.this.mClassifiers.forEach(BrightLineFalsingManager$1$$ExternalSyntheticLambda0.INSTANCE);
            }

            @Override // com.android.systemui.classifier.FalsingDataProvider.SessionListener
            public void onSessionStarted() {
                BrightLineFalsingManager.this.mClassifiers.forEach(BrightLineFalsingManager$1$$ExternalSyntheticLambda1.INSTANCE);
            }
        };
        this.mSessionListener = sessionListener;
        HistoryTracker.BeliefListener beliefListener = new HistoryTracker.BeliefListener() { // from class: com.android.systemui.classifier.BrightLineFalsingManager.2
            @Override // com.android.systemui.classifier.HistoryTracker.BeliefListener
            public void onBeliefChanged(double d) {
                BrightLineFalsingManager.logInfo(String.format("{belief=%s confidence=%s}", Double.valueOf(BrightLineFalsingManager.this.mHistoryTracker.falseBelief()), Double.valueOf(BrightLineFalsingManager.this.mHistoryTracker.falseConfidence())));
                if (d > 0.9d) {
                    BrightLineFalsingManager.this.mFalsingBeliefListeners.forEach(BrightLineFalsingManager$2$$ExternalSyntheticLambda0.INSTANCE);
                    BrightLineFalsingManager.logInfo("Triggering False Event (Threshold: 0.9)");
                }
            }
        };
        this.mBeliefListener = beliefListener;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3();
        this.mGestureFinalizedListener = anonymousClass3;
        this.mDataProvider = falsingDataProvider;
        this.mMetricsLogger = metricsLogger;
        this.mClassifiers = set;
        this.mSingleTapClassifier = singleTapClassifier;
        this.mDoubleTapClassifier = doubleTapClassifier;
        this.mHistoryTracker = historyTracker;
        this.mKeyguardStateController = keyguardStateController;
        this.mAccessibilityManager = accessibilityManager;
        this.mTestHarness = z;
        falsingDataProvider.addSessionListener(sessionListener);
        falsingDataProvider.addGestureCompleteListener(anonymousClass3);
        historyTracker.addBeliefListener(beliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isFalseTouch(final int i) {
        checkDestroyed();
        this.mPriorInteractionType = i;
        if (skipFalsing(i)) {
            this.mPriorResults = getPassedResult(1.0d);
            logDebug("Skipped falsing");
            return false;
        }
        final boolean[] zArr = {false};
        this.mPriorResults = (Collection) this.mClassifiers.stream().map(new Function() { // from class: com.android.systemui.classifier.BrightLineFalsingManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                FalsingClassifier.Result lambda$isFalseTouch$0;
                lambda$isFalseTouch$0 = BrightLineFalsingManager.this.lambda$isFalseTouch$0(i, zArr, (FalsingClassifier) obj);
                return lambda$isFalseTouch$0;
            }
        }).collect(Collectors.toList());
        logDebug("False Gesture: " + zArr[0]);
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ FalsingClassifier.Result lambda$isFalseTouch$0(int i, boolean[] zArr, FalsingClassifier falsingClassifier) {
        FalsingClassifier.Result classifyGesture = falsingClassifier.classifyGesture(i, this.mHistoryTracker.falseBelief(), this.mHistoryTracker.falseConfidence());
        zArr[0] = zArr[0] | classifyGesture.isFalse();
        return classifyGesture;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isSimpleTap() {
        checkDestroyed();
        FalsingClassifier.Result isTap = this.mSingleTapClassifier.isTap(this.mDataProvider.getRecentMotionEvents(), 0.0d);
        this.mPriorResults = Collections.singleton(isTap);
        return !isTap.isFalse();
    }

    private void checkDestroyed() {
        if (this.mDestroyed) {
            Log.wtf("FalsingManager", "Tried to use FalsingManager after being destroyed!");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x004f  */
    @Override // com.android.systemui.plugins.FalsingManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isFalseTap(int i) {
        double d;
        List<MotionEvent> recentMotionEvents;
        FalsingClassifier.Result isTap;
        checkDestroyed();
        if (skipFalsing(7)) {
            this.mPriorResults = getPassedResult(1.0d);
            logDebug("Skipped falsing");
            return false;
        }
        if (i != 0) {
            if (i == 1) {
                d = 0.1d;
            } else if (i == 2) {
                d = 0.3d;
            } else if (i == 3) {
                d = 0.6d;
            }
            SingleTapClassifier singleTapClassifier = this.mSingleTapClassifier;
            if (!this.mDataProvider.getRecentMotionEvents().isEmpty()) {
                recentMotionEvents = this.mDataProvider.getPriorMotionEvents();
            } else {
                recentMotionEvents = this.mDataProvider.getRecentMotionEvents();
            }
            isTap = singleTapClassifier.isTap(recentMotionEvents, d);
            this.mPriorResults = Collections.singleton(isTap);
            if (isTap.isFalse()) {
                if (this.mDataProvider.isJustUnlockedWithFace()) {
                    this.mPriorResults = getPassedResult(1.0d);
                    logDebug("False Single Tap: false (face detected)");
                    return false;
                } else if (!isFalseDoubleTap()) {
                    logDebug("False Single Tap: false (double tapped)");
                    return false;
                } else if (this.mHistoryTracker.falseBelief() > 0.7d) {
                    this.mPriorResults = Collections.singleton(FalsingClassifier.Result.falsed(0.0d, BrightLineFalsingManager.class.getSimpleName(), "bad history"));
                    logDebug("False Single Tap: true (bad history)");
                    this.mFalsingTapListeners.forEach(BrightLineFalsingManager$$ExternalSyntheticLambda2.INSTANCE);
                    return true;
                } else {
                    this.mPriorResults = getPassedResult(0.1d);
                    logDebug("False Single Tap: false (default)");
                    return false;
                }
            }
            logDebug("False Single Tap: " + isTap.isFalse() + " (simple)");
            return isTap.isFalse();
        }
        d = 0.0d;
        SingleTapClassifier singleTapClassifier2 = this.mSingleTapClassifier;
        if (!this.mDataProvider.getRecentMotionEvents().isEmpty()) {
        }
        isTap = singleTapClassifier2.isTap(recentMotionEvents, d);
        this.mPriorResults = Collections.singleton(isTap);
        if (isTap.isFalse()) {
        }
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isFalseDoubleTap() {
        checkDestroyed();
        if (skipFalsing(7)) {
            this.mPriorResults = getPassedResult(1.0d);
            logDebug("Skipped falsing");
            return false;
        }
        FalsingClassifier.Result classifyGesture = this.mDoubleTapClassifier.classifyGesture(7, this.mHistoryTracker.falseBelief(), this.mHistoryTracker.falseConfidence());
        this.mPriorResults = Collections.singleton(classifyGesture);
        logDebug("False Double Tap: " + classifyGesture.isFalse());
        return classifyGesture.isFalse();
    }

    private boolean skipFalsing(int i) {
        return i == 16 || !this.mKeyguardStateController.isShowing() || this.mTestHarness || this.mDataProvider.isJustUnlockedWithFace() || this.mDataProvider.isDocked() || this.mAccessibilityManager.isEnabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void onProximityEvent(final FalsingManager.ProximityEvent proximityEvent) {
        this.mClassifiers.forEach(new Consumer() { // from class: com.android.systemui.classifier.BrightLineFalsingManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FalsingClassifier) obj).onProximityEvent(FalsingManager.ProximityEvent.this);
            }
        });
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void onSuccessfulUnlock() {
        int i = this.mIsFalseTouchCalls;
        if (i != 0) {
            this.mMetricsLogger.histogram("falsing_success_after_attempts", i);
            this.mIsFalseTouchCalls = 0;
        }
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void addFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mFalsingBeliefListeners.add(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void removeFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mFalsingBeliefListeners.remove(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void addTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mFalsingTapListeners.add(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void removeTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mFalsingTapListeners.remove(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("BRIGHTLINE FALSING MANAGER");
        indentingPrintWriter.print("classifierEnabled=");
        indentingPrintWriter.println(isClassifierEnabled() ? 1 : 0);
        indentingPrintWriter.print("mJustUnlockedWithFace=");
        indentingPrintWriter.println(this.mDataProvider.isJustUnlockedWithFace() ? 1 : 0);
        indentingPrintWriter.print("isDocked=");
        indentingPrintWriter.println(this.mDataProvider.isDocked() ? 1 : 0);
        indentingPrintWriter.print("width=");
        indentingPrintWriter.println(this.mDataProvider.getWidthPixels());
        indentingPrintWriter.print("height=");
        indentingPrintWriter.println(this.mDataProvider.getHeightPixels());
        indentingPrintWriter.println();
        Queue<DebugSwipeRecord> queue = RECENT_SWIPES;
        if (queue.size() != 0) {
            indentingPrintWriter.println("Recent swipes:");
            indentingPrintWriter.increaseIndent();
            for (DebugSwipeRecord debugSwipeRecord : queue) {
                indentingPrintWriter.println(debugSwipeRecord.getString());
                indentingPrintWriter.println();
            }
            indentingPrintWriter.decreaseIndent();
        } else {
            indentingPrintWriter.println("No recent swipes");
        }
        indentingPrintWriter.println();
        indentingPrintWriter.println("Recent falsing info:");
        indentingPrintWriter.increaseIndent();
        for (String str : RECENT_INFO_LOG) {
            indentingPrintWriter.println(str);
        }
        indentingPrintWriter.println();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void cleanupInternal() {
        this.mDestroyed = true;
        this.mDataProvider.removeSessionListener(this.mSessionListener);
        this.mDataProvider.removeGestureCompleteListener(this.mGestureFinalizedListener);
        this.mClassifiers.forEach(BrightLineFalsingManager$$ExternalSyntheticLambda1.INSTANCE);
        this.mFalsingBeliefListeners.clear();
        this.mHistoryTracker.removeBeliefListener(this.mBeliefListener);
    }

    private static Collection<FalsingClassifier.Result> getPassedResult(double d) {
        return Collections.singleton(FalsingClassifier.Result.passed(d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logDebug(String str) {
        logDebug(str, null);
    }

    static void logDebug(String str, Throwable th) {
        if (DEBUG) {
            Log.d("FalsingManager", str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logInfo(String str) {
        Log.i("FalsingManager", str);
        RECENT_INFO_LOG.add(str);
        while (true) {
            Queue<String> queue = RECENT_INFO_LOG;
            if (queue.size() > 40) {
                queue.remove();
            } else {
                return;
            }
        }
    }

    /* loaded from: classes.dex */
    private static class DebugSwipeRecord {
        private final int mInteractionType;
        private final boolean mIsFalse;
        private final List<XYDt> mRecentMotionEvents;

        DebugSwipeRecord(boolean z, int i, List<XYDt> list) {
            this.mIsFalse = z;
            this.mInteractionType = i;
            this.mRecentMotionEvents = list;
        }

        String getString() {
            StringJoiner stringJoiner = new StringJoiner(",");
            stringJoiner.add(Integer.toString(1)).add(this.mIsFalse ? "1" : "0").add(Integer.toString(this.mInteractionType));
            for (XYDt xYDt : this.mRecentMotionEvents) {
                stringJoiner.add(xYDt.toString());
            }
            return stringJoiner.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class XYDt {
        private final int mDT;
        private final int mX;
        private final int mY;

        XYDt(int i, int i2, int i3) {
            this.mX = i;
            this.mY = i2;
            this.mDT = i3;
        }

        public String toString() {
            return this.mX + "," + this.mY + "," + this.mDT;
        }
    }
}
