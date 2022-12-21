package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000ø\u0001\u0001J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\u0014\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0003H\u0002ø\u0001\u0001J\u0014\u0010\t\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0003H\u0002ø\u0001\u0001\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\n"}, mo64987d2 = {"Lkotlin/time/TimeMark;", "", "elapsedNow", "Lkotlin/time/Duration;", "hasNotPassedNow", "", "hasPassedNow", "minus", "duration", "plus", "kotlin-stdlib"}, mo64988k = 1, mo64989mv = {1, 7, 1}, mo64991xi = 48)
/* compiled from: TimeSource.kt */
public interface TimeMark {
    /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
    long m5384elapsedNowUwyO8pc();

    boolean hasNotPassedNow();

    boolean hasPassedNow();

    /* renamed from: minus-LRDsOJo  reason: not valid java name */
    TimeMark m5385minusLRDsOJo(long j);

    /* renamed from: plus-LRDsOJo  reason: not valid java name */
    TimeMark m5386plusLRDsOJo(long j);

    @Metadata(mo64988k = 3, mo64989mv = {1, 7, 1}, mo64991xi = 48)
    /* compiled from: TimeSource.kt */
    public static final class DefaultImpls {
        /* renamed from: plus-LRDsOJo  reason: not valid java name */
        public static TimeMark m5388plusLRDsOJo(TimeMark timeMark, long j) {
            return new AdjustedTimeMark(timeMark, j, (DefaultConstructorMarker) null);
        }

        /* renamed from: minus-LRDsOJo  reason: not valid java name */
        public static TimeMark m5387minusLRDsOJo(TimeMark timeMark, long j) {
            return timeMark.m5386plusLRDsOJo(Duration.m5301unaryMinusUwyO8pc(j));
        }

        public static boolean hasPassedNow(TimeMark timeMark) {
            return !Duration.m5282isNegativeimpl(timeMark.m5384elapsedNowUwyO8pc());
        }

        public static boolean hasNotPassedNow(TimeMark timeMark) {
            return Duration.m5282isNegativeimpl(timeMark.m5384elapsedNowUwyO8pc());
        }
    }
}
