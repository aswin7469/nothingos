package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Strings.kt */
/* loaded from: classes2.dex */
public final class DelimitedRangesSequence$iterator$1 implements Iterator<IntRange>, KMappedMarker {
    private int counter;
    private int currentStartIndex;
    @Nullable
    private IntRange nextItem;
    private int nextSearchIndex;
    private int nextState = -1;
    final /* synthetic */ DelimitedRangesSequence this$0;

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DelimitedRangesSequence$iterator$1(DelimitedRangesSequence delimitedRangesSequence) {
        int i;
        CharSequence charSequence;
        int coerceIn;
        this.this$0 = delimitedRangesSequence;
        i = delimitedRangesSequence.startIndex;
        charSequence = delimitedRangesSequence.input;
        coerceIn = RangesKt___RangesKt.coerceIn(i, 0, charSequence.length());
        this.currentStartIndex = coerceIn;
        this.nextSearchIndex = coerceIn;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0021, code lost:
        if (r0 < r4) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void calcNext() {
        int i;
        CharSequence charSequence;
        Function2 function2;
        CharSequence charSequence2;
        IntRange until;
        CharSequence charSequence3;
        CharSequence charSequence4;
        int i2;
        int i3 = 0;
        if (this.nextSearchIndex >= 0) {
            i = this.this$0.limit;
            if (i > 0) {
                int i4 = this.counter + 1;
                this.counter = i4;
                i2 = this.this$0.limit;
            }
            int i5 = this.nextSearchIndex;
            charSequence = this.this$0.input;
            if (i5 <= charSequence.length()) {
                function2 = this.this$0.getNextMatch;
                charSequence2 = this.this$0.input;
                Pair pair = (Pair) function2.mo1950invoke(charSequence2, Integer.valueOf(this.nextSearchIndex));
                if (pair == null) {
                    int i6 = this.currentStartIndex;
                    charSequence3 = this.this$0.input;
                    this.nextItem = new IntRange(i6, StringsKt__StringsKt.getLastIndex(charSequence3));
                    this.nextSearchIndex = -1;
                } else {
                    int intValue = ((Number) pair.component1()).intValue();
                    int intValue2 = ((Number) pair.component2()).intValue();
                    until = RangesKt___RangesKt.until(this.currentStartIndex, intValue);
                    this.nextItem = until;
                    int i7 = intValue + intValue2;
                    this.currentStartIndex = i7;
                    if (intValue2 == 0) {
                        i3 = 1;
                    }
                    this.nextSearchIndex = i7 + i3;
                }
                this.nextState = 1;
                return;
            }
            int i8 = this.currentStartIndex;
            charSequence4 = this.this$0.input;
            this.nextItem = new IntRange(i8, StringsKt__StringsKt.getLastIndex(charSequence4));
            this.nextSearchIndex = -1;
            this.nextState = 1;
            return;
        }
        this.nextState = 0;
        this.nextItem = null;
    }

    @Override // java.util.Iterator
    @NotNull
    public IntRange next() {
        if (this.nextState == -1) {
            calcNext();
        }
        if (this.nextState == 0) {
            throw new NoSuchElementException();
        }
        IntRange intRange = this.nextItem;
        Objects.requireNonNull(intRange, "null cannot be cast to non-null type kotlin.ranges.IntRange");
        this.nextItem = null;
        this.nextState = -1;
        return intRange;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.nextState == -1) {
            calcNext();
        }
        return this.nextState == 1;
    }
}
