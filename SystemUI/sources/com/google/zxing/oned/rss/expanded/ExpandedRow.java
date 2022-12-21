package com.google.zxing.oned.rss.expanded;

import java.util.ArrayList;
import java.util.List;

final class ExpandedRow {
    private final List<ExpandedPair> pairs;
    private final int rowNumber;

    ExpandedRow(List<ExpandedPair> list, int i) {
        this.pairs = new ArrayList(list);
        this.rowNumber = i;
    }

    /* access modifiers changed from: package-private */
    public List<ExpandedPair> getPairs() {
        return this.pairs;
    }

    /* access modifiers changed from: package-private */
    public int getRowNumber() {
        return this.rowNumber;
    }

    /* access modifiers changed from: package-private */
    public boolean isEquivalent(List<ExpandedPair> list) {
        return this.pairs.equals(list);
    }

    public String toString() {
        return "{ " + this.pairs + " }";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExpandedRow)) {
            return false;
        }
        return this.pairs.equals(((ExpandedRow) obj).pairs);
    }

    public int hashCode() {
        return this.pairs.hashCode();
    }
}
