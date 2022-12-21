package com.google.zxing.oned.rss.expanded;

import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import java.util.Objects;

final class ExpandedPair {
    private final FinderPattern finderPattern;
    private final DataCharacter leftChar;
    private final DataCharacter rightChar;

    ExpandedPair(DataCharacter dataCharacter, DataCharacter dataCharacter2, FinderPattern finderPattern2) {
        this.leftChar = dataCharacter;
        this.rightChar = dataCharacter2;
        this.finderPattern = finderPattern2;
    }

    /* access modifiers changed from: package-private */
    public DataCharacter getLeftChar() {
        return this.leftChar;
    }

    /* access modifiers changed from: package-private */
    public DataCharacter getRightChar() {
        return this.rightChar;
    }

    /* access modifiers changed from: package-private */
    public FinderPattern getFinderPattern() {
        return this.finderPattern;
    }

    /* access modifiers changed from: package-private */
    public boolean mustBeLast() {
        return this.rightChar == null;
    }

    public String toString() {
        Object obj;
        StringBuilder append = new StringBuilder("[ ").append((Object) this.leftChar).append(" , ").append((Object) this.rightChar).append(" : ");
        FinderPattern finderPattern2 = this.finderPattern;
        if (finderPattern2 == null) {
            obj = "null";
        } else {
            obj = Integer.valueOf(finderPattern2.getValue());
        }
        return append.append(obj).append(" ]").toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExpandedPair)) {
            return false;
        }
        ExpandedPair expandedPair = (ExpandedPair) obj;
        if (!Objects.equals(this.leftChar, expandedPair.leftChar) || !Objects.equals(this.rightChar, expandedPair.rightChar) || !Objects.equals(this.finderPattern, expandedPair.finderPattern)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hashCode(this.finderPattern) ^ (Objects.hashCode(this.leftChar) ^ Objects.hashCode(this.rightChar));
    }
}
