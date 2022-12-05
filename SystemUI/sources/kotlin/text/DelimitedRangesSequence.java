package kotlin.text;

import java.util.Iterator;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Strings.kt */
/* loaded from: classes2.dex */
public final class DelimitedRangesSequence implements Sequence<IntRange> {
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public DelimitedRangesSequence(@NotNull CharSequence input, int i, int i2, @NotNull Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> getNextMatch) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(getNextMatch, "getNextMatch");
        this.input = input;
        this.startIndex = i;
        this.limit = i2;
        this.getNextMatch = getNextMatch;
    }

    @Override // kotlin.sequences.Sequence
    @NotNull
    public Iterator<IntRange> iterator() {
        return new DelimitedRangesSequence$iterator$1(this);
    }
}
