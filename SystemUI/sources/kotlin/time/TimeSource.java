package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.JvmInline;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u0000 \u00042\u00020\u0001:\u0002\u0004\u0005J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0006"}, mo65043d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "kotlin-stdlib"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
/* compiled from: TimeSource.kt */
public interface TimeSource {
    public static final Companion Companion = Companion.$$INSTANCE;

    TimeMark markNow();

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\n"}, mo65043d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
    /* compiled from: TimeSource.kt */
    public static final class Monotonic implements TimeSource {
        public static final Monotonic INSTANCE = new Monotonic();

        private Monotonic() {
        }

        public /* bridge */ /* synthetic */ TimeMark markNow() {
            return ValueTimeMark.m5408boximpl(m5407markNowz9LOYto());
        }

        /* renamed from: markNow-z9LOYto  reason: not valid java name */
        public long m5407markNowz9LOYto() {
            return MonotonicTimeSource.INSTANCE.m5399markNowz9LOYto();
        }

        public String toString() {
            return MonotonicTimeSource.INSTANCE.toString();
        }

        @JvmInline
        @Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\b@\u0018\u00002\u00020\u0001B\u0018\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0015\u0010\u0007\u001a\u00020\bH\u0016ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\t\u0010\u0006J\u001a\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0011\u0010\u0012J\u000f\u0010\u0013\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0014\u0010\u0012J\u0010\u0010\u0015\u001a\u00020\u0016HÖ\u0001¢\u0006\u0004\b\u0017\u0010\u0018J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001cJ\u001b\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001cJ\u0010\u0010\u001f\u001a\u00020 HÖ\u0001¢\u0006\u0004\b!\u0010\"R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0004¢\u0006\u0002\n\u0000\u0001\u0002\u0001\u00060\u0003j\u0002`\u0004ø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006#"}, mo65043d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/TimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "other", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", "hashCode", "", "hashCode-impl", "(J)I", "minus", "duration", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
        /* compiled from: TimeSource.kt */
        public static final class ValueTimeMark implements TimeMark {
            private final long reading;

            /* renamed from: box-impl  reason: not valid java name */
            public static final /* synthetic */ ValueTimeMark m5408boximpl(long j) {
                return new ValueTimeMark(j);
            }

            /* renamed from: constructor-impl  reason: not valid java name */
            public static long m5409constructorimpl(long j) {
                return j;
            }

            /* renamed from: equals-impl  reason: not valid java name */
            public static boolean m5411equalsimpl(long j, Object obj) {
                return (obj instanceof ValueTimeMark) && j == ((ValueTimeMark) obj).m5424unboximpl();
            }

            /* renamed from: equals-impl0  reason: not valid java name */
            public static final boolean m5412equalsimpl0(long j, long j2) {
                return j == j2;
            }

            /* renamed from: hashCode-impl  reason: not valid java name */
            public static int m5415hashCodeimpl(long j) {
                return (int) (j ^ (j >>> 32));
            }

            /* renamed from: toString-impl  reason: not valid java name */
            public static String m5418toStringimpl(long j) {
                return "ValueTimeMark(reading=" + j + ')';
            }

            public boolean equals(Object obj) {
                return m5411equalsimpl(this.reading, obj);
            }

            public int hashCode() {
                return m5415hashCodeimpl(this.reading);
            }

            public String toString() {
                return m5418toStringimpl(this.reading);
            }

            /* renamed from: unbox-impl  reason: not valid java name */
            public final /* synthetic */ long m5424unboximpl() {
                return this.reading;
            }

            private /* synthetic */ ValueTimeMark(long j) {
                this.reading = j;
            }

            /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
            public static long m5410elapsedNowUwyO8pc(long j) {
                return MonotonicTimeSource.INSTANCE.m5398elapsedFrom6eNON_k(j);
            }

            /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
            public long m5419elapsedNowUwyO8pc() {
                return m5410elapsedNowUwyO8pc(this.reading);
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public static long m5417plusLRDsOJo(long j, long j2) {
                return MonotonicTimeSource.INSTANCE.m5397adjustReading6QKq23U(j, j2);
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public long m5422plusLRDsOJo(long j) {
                return m5417plusLRDsOJo(this.reading, j);
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public static long m5416minusLRDsOJo(long j, long j2) {
                return MonotonicTimeSource.INSTANCE.m5397adjustReading6QKq23U(j, Duration.m5319unaryMinusUwyO8pc(j2));
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public long m5420minusLRDsOJo(long j) {
                return m5416minusLRDsOJo(this.reading, j);
            }

            /* renamed from: hasPassedNow-impl  reason: not valid java name */
            public static boolean m5414hasPassedNowimpl(long j) {
                return !Duration.m5300isNegativeimpl(m5410elapsedNowUwyO8pc(j));
            }

            public boolean hasPassedNow() {
                return m5414hasPassedNowimpl(this.reading);
            }

            /* renamed from: hasNotPassedNow-impl  reason: not valid java name */
            public static boolean m5413hasNotPassedNowimpl(long j) {
                return Duration.m5300isNegativeimpl(m5410elapsedNowUwyO8pc(j));
            }

            public boolean hasNotPassedNow() {
                return m5413hasNotPassedNowimpl(this.reading);
            }
        }
    }

    @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo65043d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
    /* compiled from: TimeSource.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }
    }
}
