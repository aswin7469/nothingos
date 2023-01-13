package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@SysUISingleton
public class HistoryTracker {
    private static final long DECAY_INTERVAL_MS = 100;
    /* access modifiers changed from: private */
    public static final double HISTORY_DECAY = Math.pow(10.0d, (Math.log10(MINIMUM_SCORE) / 10000.0d) * 100.0d);
    private static final long HISTORY_MAX_AGE_MS = 10000;
    private static final double MINIMUM_SCORE = 0.1d;
    private final List<BeliefListener> mBeliefListeners = new ArrayList();
    DelayQueue<CombinedResult> mResults = new DelayQueue<>();
    /* access modifiers changed from: private */
    public final SystemClock mSystemClock;

    interface BeliefListener {
        void onBeliefChanged(double d);
    }

    @Inject
    HistoryTracker(SystemClock systemClock) {
        this.mSystemClock = systemClock;
    }

    /* access modifiers changed from: package-private */
    public double falseBelief() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.5d;
        }
        return ((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda0(this.mSystemClock.uptimeMillis())).reduce(Double.valueOf(0.5d), new HistoryTracker$$ExternalSyntheticLambda1())).doubleValue();
    }

    /* access modifiers changed from: package-private */
    public double falseConfidence() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return HISTORY_DECAY;
        }
        return 1.0d - Math.sqrt(((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda5(((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda3()).reduce(Double.valueOf((double) HISTORY_DECAY), new HistoryTracker$$ExternalSyntheticLambda4())).doubleValue() / ((double) this.mResults.size()))).reduce(Double.valueOf((double) HISTORY_DECAY), new HistoryTracker$$ExternalSyntheticLambda4())).doubleValue() / ((double) this.mResults.size()));
    }

    /* access modifiers changed from: package-private */
    public void addResults(Collection<FalsingClassifier.Result> collection, long j) {
        double d = 0.0d;
        for (FalsingClassifier.Result next : collection) {
            d += ((next.isFalse() ? 0.5d : -0.5d) * next.getConfidence()) + 0.5d;
        }
        double size = d / ((double) collection.size());
        if (size == 1.0d) {
            size = 0.99999d;
        } else if (size == HISTORY_DECAY) {
            size = 1.0E-5d;
        }
        double d2 = size;
        do {
        } while (this.mResults.poll() != null);
        this.mResults.add(new CombinedResult(j, d2));
        this.mBeliefListeners.forEach(new HistoryTracker$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addResults$3$com-android-systemui-classifier-HistoryTracker */
    public /* synthetic */ void mo31277xdeda9ad3(BeliefListener beliefListener) {
        beliefListener.onBeliefChanged(falseBelief());
    }

    /* access modifiers changed from: package-private */
    public void addBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.add(beliefListener);
    }

    /* access modifiers changed from: package-private */
    public void removeBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.remove((Object) beliefListener);
    }

    private class CombinedResult implements Delayed {
        private final long mExpiryMs;
        private final double mScore;

        CombinedResult(long j, double d) {
            this.mExpiryMs = j + HistoryTracker.HISTORY_MAX_AGE_MS;
            this.mScore = d;
        }

        /* access modifiers changed from: package-private */
        public double getDecayedScore(long j) {
            return ((this.mScore - 0.5d) * Math.pow(HistoryTracker.HISTORY_DECAY, ((double) (HistoryTracker.HISTORY_MAX_AGE_MS - (this.mExpiryMs - j))) / 100.0d)) + 0.5d;
        }

        /* access modifiers changed from: package-private */
        public double getScore() {
            return this.mScore;
        }

        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.mExpiryMs - HistoryTracker.this.mSystemClock.uptimeMillis(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed delayed) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS), delayed.getDelay(TimeUnit.MILLISECONDS));
        }
    }
}
