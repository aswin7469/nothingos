package com.android.systemui.util.leak;

import android.os.SystemClock;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import javax.inject.Inject;

public class TrackedCollections {
    private static final long HALFWAY_DELAY = 1800000;
    private static final long MILLIS_IN_MINUTE = 60000;
    private final WeakIdentityHashMap<Collection<?>, CollectionState> mCollections = new WeakIdentityHashMap<>();

    @Inject
    TrackedCollections() {
    }

    public synchronized void track(Collection<?> collection, String str) {
        CollectionState collectionState = this.mCollections.get(collection);
        if (collectionState == null) {
            collectionState = new CollectionState();
            collectionState.tag = str;
            collectionState.startUptime = SystemClock.uptimeMillis();
            this.mCollections.put(collection, collectionState);
        }
        if (collectionState.halfwayCount == -1 && SystemClock.uptimeMillis() - collectionState.startUptime > HALFWAY_DELAY) {
            collectionState.halfwayCount = collectionState.lastCount;
        }
        collectionState.lastCount = collection.size();
        collectionState.lastUptime = SystemClock.uptimeMillis();
    }

    private static class CollectionState {
        int halfwayCount;
        int lastCount;
        long lastUptime;
        long startUptime;
        String tag;

        private float ratePerHour(long j, int i, long j2, int i2) {
            if (j >= j2 || i < 0 || i2 < 0) {
                return Float.NaN;
            }
            return ((((float) i2) - ((float) i)) / ((float) (j2 - j))) * 60.0f * 60000.0f;
        }

        private CollectionState() {
            this.halfwayCount = -1;
            this.lastCount = -1;
        }

        /* access modifiers changed from: package-private */
        public void dump(PrintWriter printWriter) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = this.startUptime;
            long j2 = uptimeMillis;
            printWriter.format("%s: %.2f (start-30min) / %.2f (30min-now) / %.2f (start-now) (growth rate in #/hour); %d (current size)", this.tag, Float.valueOf(ratePerHour(j, 0, j + TrackedCollections.HALFWAY_DELAY, this.halfwayCount)), Float.valueOf(ratePerHour(this.startUptime + TrackedCollections.HALFWAY_DELAY, this.halfwayCount, j2, this.lastCount)), Float.valueOf(ratePerHour(this.startUptime, 0, j2, this.lastCount)), Integer.valueOf(this.lastCount));
        }
    }

    public synchronized void dump(PrintWriter printWriter, Predicate<Collection<?>> predicate) {
        for (Map.Entry next : this.mCollections.entrySet()) {
            Collection collection = (Collection) ((WeakReference) next.getKey()).get();
            if (predicate == null || (collection != null && predicate.test(collection))) {
                ((CollectionState) next.getValue()).dump(printWriter);
                printWriter.println();
            }
        }
    }
}
