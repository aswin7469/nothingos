package kotlin.ranges;

import java.lang.Comparable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo14007d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\bg\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0005H\u0016J\u001d\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00028\u00002\u0006\u0010\u000b\u001a\u00028\u0000H&¢\u0006\u0002\u0010\f¨\u0006\r"}, mo14008d2 = {"Lkotlin/ranges/ClosedFloatingPointRange;", "T", "", "Lkotlin/ranges/ClosedRange;", "contains", "", "value", "(Ljava/lang/Comparable;)Z", "isEmpty", "lessThanOrEquals", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Z", "kotlin-stdlib"}, mo14009k = 1, mo14010mv = {1, 6, 0}, mo14012xi = 48)
/* compiled from: Ranges.kt */
public interface ClosedFloatingPointRange<T extends Comparable<? super T>> extends ClosedRange<T> {
    boolean contains(T t);

    boolean isEmpty();

    boolean lessThanOrEquals(T t, T t2);

    @Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
    /* compiled from: Ranges.kt */
    public static final class DefaultImpls {
        public static <T extends Comparable<? super T>> boolean contains(ClosedFloatingPointRange<T> closedFloatingPointRange, T t) {
            Intrinsics.checkNotNullParameter(closedFloatingPointRange, "this");
            Intrinsics.checkNotNullParameter(t, "value");
            return closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), t) && closedFloatingPointRange.lessThanOrEquals(t, closedFloatingPointRange.getEndInclusive());
        }

        public static <T extends Comparable<? super T>> boolean isEmpty(ClosedFloatingPointRange<T> closedFloatingPointRange) {
            Intrinsics.checkNotNullParameter(closedFloatingPointRange, "this");
            return !closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), closedFloatingPointRange.getEndInclusive());
        }
    }
}
