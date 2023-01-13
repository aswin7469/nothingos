package com.android.systemui.unfold;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0006HÆ\u0003J'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/unfold/FoldStateChange;", "", "previous", "", "current", "dtMillis", "", "(IIJ)V", "getCurrent", "()I", "getDtMillis", "()J", "getPrevious", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FoldStateLoggingProvider.kt */
public final class FoldStateChange {
    private final int current;
    private final long dtMillis;
    private final int previous;

    public static /* synthetic */ FoldStateChange copy$default(FoldStateChange foldStateChange, int i, int i2, long j, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = foldStateChange.previous;
        }
        if ((i3 & 2) != 0) {
            i2 = foldStateChange.current;
        }
        if ((i3 & 4) != 0) {
            j = foldStateChange.dtMillis;
        }
        return foldStateChange.copy(i, i2, j);
    }

    public final int component1() {
        return this.previous;
    }

    public final int component2() {
        return this.current;
    }

    public final long component3() {
        return this.dtMillis;
    }

    public final FoldStateChange copy(int i, int i2, long j) {
        return new FoldStateChange(i, i2, j);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FoldStateChange)) {
            return false;
        }
        FoldStateChange foldStateChange = (FoldStateChange) obj;
        return this.previous == foldStateChange.previous && this.current == foldStateChange.current && this.dtMillis == foldStateChange.dtMillis;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.previous) * 31) + Integer.hashCode(this.current)) * 31) + Long.hashCode(this.dtMillis);
    }

    public String toString() {
        return "FoldStateChange(previous=" + this.previous + ", current=" + this.current + ", dtMillis=" + this.dtMillis + ')';
    }

    public FoldStateChange(int i, int i2, long j) {
        this.previous = i;
        this.current = i2;
        this.dtMillis = j;
    }

    public final int getPrevious() {
        return this.previous;
    }

    public final int getCurrent() {
        return this.current;
    }

    public final long getDtMillis() {
        return this.dtMillis;
    }
}
