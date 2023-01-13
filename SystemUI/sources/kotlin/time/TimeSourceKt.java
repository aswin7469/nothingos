package kotlin.time;

import android.icu.text.PluralRules;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\n\u001a\u001d\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\nø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, mo65043d2 = {"compareTo", "", "Lkotlin/time/TimeMark;", "other", "minus", "Lkotlin/time/Duration;", "(Lkotlin/time/TimeMark;Lkotlin/time/TimeMark;)J", "kotlin-stdlib"}, mo65044k = 2, mo65045mv = {1, 7, 1}, mo65047xi = 48)
/* compiled from: TimeSource.kt */
public final class TimeSourceKt {
    @Deprecated(level = DeprecationLevel.ERROR, message = "Subtracting one TimeMark from another is not a well defined operation because these time marks could have been obtained from the different time sources.")
    private static final long minus(TimeMark timeMark, TimeMark timeMark2) {
        Intrinsics.checkNotNullParameter(timeMark, "<this>");
        Intrinsics.checkNotNullParameter(timeMark2, PluralRules.KEYWORD_OTHER);
        throw new Error("Operation is disallowed.");
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Comparing one TimeMark to another is not a well defined operation because these time marks could have been obtained from the different time sources.")
    private static final int compareTo(TimeMark timeMark, TimeMark timeMark2) {
        Intrinsics.checkNotNullParameter(timeMark, "<this>");
        Intrinsics.checkNotNullParameter(timeMark2, PluralRules.KEYWORD_OTHER);
        throw new Error("Operation is disallowed.");
    }
}
