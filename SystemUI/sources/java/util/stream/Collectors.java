package java.util.stream;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.EnumSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

public final class Collectors {
    static final Set<Collector.Characteristics> CH_CONCURRENT_ID = Collections.unmodifiableSet(EnumSet.m1724of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_CONCURRENT_NOID = Collections.unmodifiableSet(EnumSet.m1723of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
    static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.m1722of(Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
    static final Set<Collector.Characteristics> CH_UNORDERED_ID = Collections.unmodifiableSet(EnumSet.m1723of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_UNORDERED_NOID = Collections.unmodifiableSet(EnumSet.m1722of(Collector.Characteristics.UNORDERED));

    static /* synthetic */ double[] lambda$averagingDouble$38() {
        return new double[4];
    }

    static /* synthetic */ long[] lambda$averagingInt$30() {
        return new long[2];
    }

    static /* synthetic */ long[] lambda$averagingLong$34() {
        return new long[2];
    }

    static /* synthetic */ Object[] lambda$boxSupplier$45(Object obj) {
        return new Object[]{obj};
    }

    static /* synthetic */ Object lambda$castingIdentity$2(Object obj) {
        return obj;
    }

    static /* synthetic */ long lambda$counting$17(Object obj) {
        return 1;
    }

    static /* synthetic */ double[] lambda$summingDouble$26() {
        return new double[3];
    }

    static /* synthetic */ int[] lambda$summingInt$18() {
        return new int[1];
    }

    static /* synthetic */ long[] lambda$summingLong$22() {
        return new long[1];
    }

    private Collectors() {
    }

    private static IllegalStateException duplicateKeyException(Object obj, Object obj2, Object obj3) {
        return new IllegalStateException(String.format("Duplicate key %s (attempted merging values %s and %s)", obj, obj2, obj3));
    }

    private static <K, V, M extends Map<K, V>> BinaryOperator<M> uniqKeysMapMerger() {
        return new Collectors$$ExternalSyntheticLambda93();
    }

    static /* synthetic */ Map lambda$uniqKeysMapMerger$0(Map map, Map map2) {
        for (Map.Entry entry : map2.entrySet()) {
            Object key = entry.getKey();
            Object requireNonNull = Objects.requireNonNull(entry.getValue());
            Object putIfAbsent = map.putIfAbsent(key, requireNonNull);
            if (putIfAbsent != null) {
                throw duplicateKeyException(key, putIfAbsent, requireNonNull);
            }
        }
        return map;
    }

    private static <T, K, V> BiConsumer<Map<K, V>, T> uniqKeysMapAccumulator(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return new Collectors$$ExternalSyntheticLambda32(function, function2);
    }

    static /* synthetic */ void lambda$uniqKeysMapAccumulator$1(Function function, Function function2, Map map, Object obj) {
        Object apply = function.apply(obj);
        Object requireNonNull = Objects.requireNonNull(function2.apply(obj));
        Object putIfAbsent = map.putIfAbsent(apply, requireNonNull);
        if (putIfAbsent != null) {
            throw duplicateKeyException(apply, putIfAbsent, requireNonNull);
        }
    }

    /* access modifiers changed from: private */
    public static <I, R> Function<I, R> castingIdentity() {
        return new Collectors$$ExternalSyntheticLambda6();
    }

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final BiConsumer<A, T> accumulator;
        private final Set<Collector.Characteristics> characteristics;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Supplier<A> supplier;

        CollectorImpl(Supplier<A> supplier2, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Function<A, R> function, Set<Collector.Characteristics> set) {
            this.supplier = supplier2;
            this.accumulator = biConsumer;
            this.combiner = binaryOperator;
            this.finisher = function;
            this.characteristics = set;
        }

        CollectorImpl(Supplier<A> supplier2, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Set<Collector.Characteristics> set) {
            this(supplier2, biConsumer, binaryOperator, Collectors.castingIdentity(), set);
        }

        public BiConsumer<A, T> accumulator() {
            return this.accumulator;
        }

        public Supplier<A> supplier() {
            return this.supplier;
        }

        public BinaryOperator<A> combiner() {
            return this.combiner;
        }

        public Function<A, R> finisher() {
            return this.finisher;
        }

        public Set<Collector.Characteristics> characteristics() {
            return this.characteristics;
        }
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> supplier) {
        return new CollectorImpl(supplier, new Collectors$$ExternalSyntheticLambda0(), new Collectors$$ExternalSyntheticLambda11(), CH_ID);
    }

    public static <T> Collector<T, ?, List<T>> toList() {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda71(), new Collectors$$ExternalSyntheticLambda72(), new Collectors$$ExternalSyntheticLambda73(), CH_ID);
    }

    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda71(), new Collectors$$ExternalSyntheticLambda72(), new Collectors$$ExternalSyntheticLambda74(), new Collectors$$ExternalSyntheticLambda75(), CH_NOID);
    }

    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda35(), new Collectors$$ExternalSyntheticLambda36(), new Collectors$$ExternalSyntheticLambda85(), CH_UNORDERED_ID);
    }

    static /* synthetic */ Set lambda$toSet$7(Set set, Set set2) {
        if (set.size() < set2.size()) {
            set2.addAll(set);
            return set2;
        }
        set.addAll(set2);
        return set;
    }

    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda35(), new Collectors$$ExternalSyntheticLambda36(), new Collectors$$ExternalSyntheticLambda37(), new Collectors$$ExternalSyntheticLambda38(), CH_UNORDERED_NOID);
    }

    static /* synthetic */ Set lambda$toUnmodifiableSet$8(Set set, Set set2) {
        if (set.size() < set2.size()) {
            set2.addAll(set);
            return set2;
        }
        set.addAll(set2);
        return set;
    }

    public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda81(), new Collectors$$ExternalSyntheticLambda82(), new Collectors$$ExternalSyntheticLambda83(), new Collectors$$ExternalSyntheticLambda84(), CH_NOID);
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence charSequence) {
        return joining(charSequence, "", "");
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda45(charSequence, charSequence2, charSequence3), new Collectors$$ExternalSyntheticLambda46(), new Collectors$$ExternalSyntheticLambda47(), new Collectors$$ExternalSyntheticLambda48(), CH_NOID);
    }

    static /* synthetic */ StringJoiner lambda$joining$11(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        return new StringJoiner(charSequence, charSequence2, charSequence3);
    }

    private static <K, V, M extends Map<K, V>> BinaryOperator<M> mapMerger(BinaryOperator<V> binaryOperator) {
        return new Collectors$$ExternalSyntheticLambda16(binaryOperator);
    }

    static /* synthetic */ Map lambda$mapMerger$12(BinaryOperator binaryOperator, Map map, Map map2) {
        for (Map.Entry entry : map2.entrySet()) {
            map.merge(entry.getKey(), entry.getValue(), binaryOperator);
        }
        return map;
    }

    public static <T, U, A, R> Collector<T, ?, R> mapping(Function<? super T, ? extends U> function, Collector<? super U, A, R> collector) {
        return new CollectorImpl(collector.supplier(), new Collectors$$ExternalSyntheticLambda27(collector.accumulator(), function), collector.combiner(), collector.finisher(), collector.characteristics());
    }

    public static <T, U, A, R> Collector<T, ?, R> flatMapping(Function<? super T, ? extends Stream<? extends U>> function, Collector<? super U, A, R> collector) {
        return new CollectorImpl(collector.supplier(), new Collectors$$ExternalSyntheticLambda76(function, collector.accumulator()), collector.combiner(), collector.finisher(), collector.characteristics());
    }

    static /* synthetic */ void lambda$flatMapping$15(Function function, BiConsumer biConsumer, Object obj, Object obj2) {
        Stream stream = (Stream) function.apply(obj2);
        if (stream != null) {
            try {
                ((Stream) stream.sequential()).forEach(new Collectors$$ExternalSyntheticLambda86(biConsumer, obj));
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        if (stream != null) {
            stream.close();
            return;
        }
        return;
        throw th;
    }

    public static <T, A, R> Collector<T, ?, R> filtering(Predicate<? super T> predicate, Collector<? super T, A, R> collector) {
        return new CollectorImpl(collector.supplier(), new Collectors$$ExternalSyntheticLambda28(predicate, collector.accumulator()), collector.combiner(), collector.finisher(), collector.characteristics());
    }

    static /* synthetic */ void lambda$filtering$16(Predicate predicate, BiConsumer biConsumer, Object obj, Object obj2) {
        if (predicate.test(obj2)) {
            biConsumer.accept(obj, obj2);
        }
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [java.util.stream.Collector<T, A, R>, java.util.stream.Collector] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, A, R, RR> java.util.stream.Collector<T, A, RR> collectingAndThen(java.util.stream.Collector<T, A, R> r7, java.util.function.Function<R, RR> r8) {
        /*
            java.util.Set r0 = r7.characteristics()
            java.util.stream.Collector$Characteristics r1 = java.util.stream.Collector.Characteristics.IDENTITY_FINISH
            boolean r1 = r0.contains(r1)
            if (r1 == 0) goto L_0x0023
            int r1 = r0.size()
            r2 = 1
            if (r1 != r2) goto L_0x0016
            java.util.Set<java.util.stream.Collector$Characteristics> r0 = CH_NOID
            goto L_0x0023
        L_0x0016:
            java.util.EnumSet r0 = java.util.EnumSet.copyOf(r0)
            java.util.stream.Collector$Characteristics r1 = java.util.stream.Collector.Characteristics.IDENTITY_FINISH
            r0.remove(r1)
            java.util.Set r0 = java.util.Collections.unmodifiableSet(r0)
        L_0x0023:
            r6 = r0
            java.util.stream.Collectors$CollectorImpl r0 = new java.util.stream.Collectors$CollectorImpl
            java.util.function.Supplier r2 = r7.supplier()
            java.util.function.BiConsumer r3 = r7.accumulator()
            java.util.function.BinaryOperator r4 = r7.combiner()
            java.util.function.Function r7 = r7.finisher()
            java.util.function.Function r5 = r7.andThen(r8)
            r1 = r0
            r1.<init>(r2, r3, r4, r5, r6)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.Collectors.collectingAndThen(java.util.stream.Collector, java.util.function.Function):java.util.stream.Collector");
    }

    public static <T> Collector<T, ?, Long> counting() {
        return summingLong(new Collectors$$ExternalSyntheticLambda79());
    }

    public static <T> Collector<T, ?, Optional<T>> minBy(Comparator<? super T> comparator) {
        return reducing(BinaryOperator.minBy(comparator));
    }

    public static <T> Collector<T, ?, Optional<T>> maxBy(Comparator<? super T> comparator) {
        return reducing(BinaryOperator.maxBy(comparator));
    }

    public static <T> Collector<T, ?, Integer> summingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda39(), new Collectors$$ExternalSyntheticLambda40(toIntFunction), new Collectors$$ExternalSyntheticLambda41(), new Collectors$$ExternalSyntheticLambda42(), CH_NOID);
    }

    static /* synthetic */ void lambda$summingInt$19(ToIntFunction toIntFunction, int[] iArr, Object obj) {
        iArr[0] = iArr[0] + toIntFunction.applyAsInt(obj);
    }

    static /* synthetic */ int[] lambda$summingInt$20(int[] iArr, int[] iArr2) {
        iArr[0] = iArr[0] + iArr2[0];
        return iArr;
    }

    public static <T> Collector<T, ?, Long> summingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda87(), new Collectors$$ExternalSyntheticLambda89(toLongFunction), new Collectors$$ExternalSyntheticLambda90(), new Collectors$$ExternalSyntheticLambda91(), CH_NOID);
    }

    static /* synthetic */ void lambda$summingLong$23(ToLongFunction toLongFunction, long[] jArr, Object obj) {
        jArr[0] = jArr[0] + toLongFunction.applyAsLong(obj);
    }

    static /* synthetic */ long[] lambda$summingLong$24(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] + jArr2[0];
        return jArr;
    }

    public static <T> Collector<T, ?, Double> summingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda62(), new Collectors$$ExternalSyntheticLambda63(toDoubleFunction), new Collectors$$ExternalSyntheticLambda64(), new Collectors$$ExternalSyntheticLambda65(), CH_NOID);
    }

    static /* synthetic */ void lambda$summingDouble$27(ToDoubleFunction toDoubleFunction, double[] dArr, Object obj) {
        double applyAsDouble = toDoubleFunction.applyAsDouble(obj);
        sumWithCompensation(dArr, applyAsDouble);
        dArr[2] = dArr[2] + applyAsDouble;
    }

    static /* synthetic */ double[] lambda$summingDouble$28(double[] dArr, double[] dArr2) {
        sumWithCompensation(dArr, dArr2[0]);
        dArr[2] = dArr[2] + dArr2[2];
        return sumWithCompensation(dArr, dArr2[1]);
    }

    static double[] sumWithCompensation(double[] dArr, double d) {
        double d2 = d - dArr[1];
        double d3 = dArr[0];
        double d4 = d3 + d2;
        dArr[1] = (d4 - d3) - d2;
        dArr[0] = d4;
        return dArr;
    }

    static double computeFinalSum(double[] dArr) {
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        return (!Double.isNaN(d) || !Double.isInfinite(d2)) ? d : d2;
    }

    public static <T> Collector<T, ?, Double> averagingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda94(), new Collectors$$ExternalSyntheticLambda1(toIntFunction), new Collectors$$ExternalSyntheticLambda2(), new Collectors$$ExternalSyntheticLambda3(), CH_NOID);
    }

    static /* synthetic */ void lambda$averagingInt$31(ToIntFunction toIntFunction, long[] jArr, Object obj) {
        jArr[0] = jArr[0] + ((long) toIntFunction.applyAsInt(obj));
        jArr[1] = jArr[1] + 1;
    }

    static /* synthetic */ long[] lambda$averagingInt$32(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] + jArr2[0];
        jArr[1] = jArr[1] + jArr2[1];
        return jArr;
    }

    static /* synthetic */ Double lambda$averagingInt$33(long[] jArr) {
        long j = jArr[1];
        return Double.valueOf(j == 0 ? 0.0d : ((double) jArr[0]) / ((double) j));
    }

    public static <T> Collector<T, ?, Double> averagingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda57(), new Collectors$$ExternalSyntheticLambda58(toLongFunction), new Collectors$$ExternalSyntheticLambda59(), new Collectors$$ExternalSyntheticLambda60(), CH_NOID);
    }

    static /* synthetic */ void lambda$averagingLong$35(ToLongFunction toLongFunction, long[] jArr, Object obj) {
        jArr[0] = jArr[0] + toLongFunction.applyAsLong(obj);
        jArr[1] = jArr[1] + 1;
    }

    static /* synthetic */ long[] lambda$averagingLong$36(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] + jArr2[0];
        jArr[1] = jArr[1] + jArr2[1];
        return jArr;
    }

    static /* synthetic */ Double lambda$averagingLong$37(long[] jArr) {
        long j = jArr[1];
        return Double.valueOf(j == 0 ? 0.0d : ((double) jArr[0]) / ((double) j));
    }

    public static <T> Collector<T, ?, Double> averagingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda67(), new Collectors$$ExternalSyntheticLambda68(toDoubleFunction), new Collectors$$ExternalSyntheticLambda69(), new Collectors$$ExternalSyntheticLambda70(), CH_NOID);
    }

    static /* synthetic */ void lambda$averagingDouble$39(ToDoubleFunction toDoubleFunction, double[] dArr, Object obj) {
        double applyAsDouble = toDoubleFunction.applyAsDouble(obj);
        sumWithCompensation(dArr, applyAsDouble);
        dArr[2] = dArr[2] + 1.0d;
        dArr[3] = dArr[3] + applyAsDouble;
    }

    static /* synthetic */ double[] lambda$averagingDouble$40(double[] dArr, double[] dArr2) {
        sumWithCompensation(dArr, dArr2[0]);
        sumWithCompensation(dArr, dArr2[1]);
        dArr[2] = dArr[2] + dArr2[2];
        dArr[3] = dArr[3] + dArr2[3];
        return dArr;
    }

    static /* synthetic */ Double lambda$averagingDouble$41(double[] dArr) {
        double d = 0.0d;
        if (dArr[2] != 0.0d) {
            d = computeFinalSum(dArr) / dArr[2];
        }
        return Double.valueOf(d);
    }

    public static <T> Collector<T, ?, T> reducing(T t, BinaryOperator<T> binaryOperator) {
        return new CollectorImpl(boxSupplier(t), new Collectors$$ExternalSyntheticLambda24(binaryOperator), new Collectors$$ExternalSyntheticLambda25(binaryOperator), new Collectors$$ExternalSyntheticLambda26(), CH_NOID);
    }

    static /* synthetic */ void lambda$reducing$42(BinaryOperator binaryOperator, Object[] objArr, Object obj) {
        objArr[0] = binaryOperator.apply(objArr[0], obj);
    }

    static /* synthetic */ Object[] lambda$reducing$43(BinaryOperator binaryOperator, Object[] objArr, Object[] objArr2) {
        objArr[0] = binaryOperator.apply(objArr[0], objArr2[0]);
        return objArr;
    }

    static /* synthetic */ Object lambda$reducing$44(Object[] objArr) {
        return objArr[0];
    }

    private static <T> Supplier<T[]> boxSupplier(T t) {
        return new Collectors$$ExternalSyntheticLambda88(t);
    }

    public static <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> binaryOperator) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda7(binaryOperator), new Collectors$$ExternalSyntheticLambda8(), new Collectors$$ExternalSyntheticLambda9(), new Collectors$$ExternalSyntheticLambda10(), CH_NOID);
    }

    static /* synthetic */ AnonymousClass1OptionalBox lambda$reducing$46(final BinaryOperator binaryOperator) {
        return new Consumer<T>() {
            boolean present = false;
            T value = null;

            public void accept(T t) {
                if (this.present) {
                    this.value = BinaryOperator.this.apply(this.value, t);
                    return;
                }
                this.value = t;
                this.present = true;
            }
        };
    }

    static /* synthetic */ AnonymousClass1OptionalBox lambda$reducing$47(AnonymousClass1OptionalBox r1, AnonymousClass1OptionalBox r2) {
        if (r2.present) {
            r1.accept(r2.value);
        }
        return r1;
    }

    public static <T, U> Collector<T, ?, U> reducing(U u, Function<? super T, ? extends U> function, BinaryOperator<U> binaryOperator) {
        return new CollectorImpl(boxSupplier(u), new Collectors$$ExternalSyntheticLambda29(binaryOperator, function), new Collectors$$ExternalSyntheticLambda30(binaryOperator), new Collectors$$ExternalSyntheticLambda31(), CH_NOID);
    }

    static /* synthetic */ void lambda$reducing$49(BinaryOperator binaryOperator, Function function, Object[] objArr, Object obj) {
        objArr[0] = binaryOperator.apply(objArr[0], function.apply(obj));
    }

    static /* synthetic */ Object[] lambda$reducing$50(BinaryOperator binaryOperator, Object[] objArr, Object[] objArr2) {
        objArr[0] = binaryOperator.apply(objArr[0], objArr2[0]);
        return objArr;
    }

    static /* synthetic */ Object lambda$reducing$51(Object[] objArr) {
        return objArr[0];
    }

    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> function) {
        return groupingBy(function, toList());
    }

    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> function, Collector<? super T, A, D> collector) {
        return groupingBy(function, new Collectors$$ExternalSyntheticLambda61(), collector);
    }

    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> function, Supplier<M> supplier, Collector<? super T, A, D> collector) {
        Collectors$$ExternalSyntheticLambda51 collectors$$ExternalSyntheticLambda51 = new Collectors$$ExternalSyntheticLambda51(function, collector.supplier(), collector.accumulator());
        BinaryOperator<M> mapMerger = mapMerger(collector.combiner());
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(supplier, collectors$$ExternalSyntheticLambda51, mapMerger, CH_ID);
        }
        return new CollectorImpl(supplier, collectors$$ExternalSyntheticLambda51, mapMerger, new Collectors$$ExternalSyntheticLambda52(collector.finisher()), CH_NOID);
    }

    public static <T, K> Collector<T, ?, ConcurrentMap<K, List<T>>> groupingByConcurrent(Function<? super T, ? extends K> function) {
        return groupingByConcurrent(function, new Collectors$$ExternalSyntheticLambda15(), toList());
    }

    public static <T, K, A, D> Collector<T, ?, ConcurrentMap<K, D>> groupingByConcurrent(Function<? super T, ? extends K> function, Collector<? super T, A, D> collector) {
        return groupingByConcurrent(function, new Collectors$$ExternalSyntheticLambda15(), collector);
    }

    public static <T, K, A, D, M extends ConcurrentMap<K, D>> Collector<T, ?, M> groupingByConcurrent(Function<? super T, ? extends K> function, Supplier<M> supplier, Collector<? super T, A, D> collector) {
        BiConsumer biConsumer;
        Supplier<A> supplier2 = collector.supplier();
        BiConsumer<A, ? super T> accumulator = collector.accumulator();
        BinaryOperator<M> mapMerger = mapMerger(collector.combiner());
        if (collector.characteristics().contains(Collector.Characteristics.CONCURRENT)) {
            biConsumer = new Collectors$$ExternalSyntheticLambda12(function, supplier2, accumulator);
        } else {
            biConsumer = new Collectors$$ExternalSyntheticLambda13(function, supplier2, accumulator);
        }
        BiConsumer biConsumer2 = biConsumer;
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(supplier, biConsumer2, mapMerger, CH_CONCURRENT_ID);
        }
        return new CollectorImpl(supplier, biConsumer2, mapMerger, new Collectors$$ExternalSyntheticLambda14(collector.finisher()), CH_CONCURRENT_NOID);
    }

    static /* synthetic */ void lambda$groupingByConcurrent$59(Function function, Supplier supplier, BiConsumer biConsumer, ConcurrentMap concurrentMap, Object obj) {
        Object computeIfAbsent = concurrentMap.computeIfAbsent(Objects.requireNonNull(function.apply(obj), "element cannot be mapped to a null key"), new Collectors$$ExternalSyntheticLambda4(supplier));
        synchronized (computeIfAbsent) {
            biConsumer.accept(computeIfAbsent, obj);
        }
    }

    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate) {
        return partitioningBy(predicate, toList());
    }

    public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> collector) {
        Collectors$$ExternalSyntheticLambda18 collectors$$ExternalSyntheticLambda18 = new Collectors$$ExternalSyntheticLambda18(collector.accumulator(), predicate);
        Collectors$$ExternalSyntheticLambda19 collectors$$ExternalSyntheticLambda19 = new Collectors$$ExternalSyntheticLambda19(collector.combiner());
        Collectors$$ExternalSyntheticLambda20 collectors$$ExternalSyntheticLambda20 = new Collectors$$ExternalSyntheticLambda20(collector);
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(collectors$$ExternalSyntheticLambda20, collectors$$ExternalSyntheticLambda18, collectors$$ExternalSyntheticLambda19, CH_ID);
        }
        return new CollectorImpl(collectors$$ExternalSyntheticLambda20, collectors$$ExternalSyntheticLambda18, collectors$$ExternalSyntheticLambda19, new Collectors$$ExternalSyntheticLambda21(collector), CH_NOID);
    }

    static /* synthetic */ void lambda$partitioningBy$62(BiConsumer biConsumer, Predicate predicate, Partition partition, Object obj) {
        biConsumer.accept(predicate.test(obj) ? partition.forTrue : partition.forFalse, obj);
    }

    static /* synthetic */ Partition lambda$partitioningBy$63(BinaryOperator binaryOperator, Partition partition, Partition partition2) {
        return new Partition(binaryOperator.apply(partition.forTrue, partition2.forTrue), binaryOperator.apply(partition.forFalse, partition2.forFalse));
    }

    static /* synthetic */ Partition lambda$partitioningBy$64(Collector collector) {
        return new Partition(collector.supplier().get(), collector.supplier().get());
    }

    static /* synthetic */ Map lambda$partitioningBy$65(Collector collector, Partition partition) {
        return new Partition(collector.finisher().apply(partition.forTrue), collector.finisher().apply(partition.forFalse));
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda61(), uniqKeysMapAccumulator(function, function2), uniqKeysMapMerger(), CH_ID);
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toUnmodifiableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2) {
        Objects.requireNonNull(function, "keyMapper");
        Objects.requireNonNull(function2, "valueMapper");
        return collectingAndThen(toMap(function, function2), new Collectors$$ExternalSyntheticLambda23());
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator) {
        return toMap(function, function2, binaryOperator, new Collectors$$ExternalSyntheticLambda61());
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toUnmodifiableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator) {
        Objects.requireNonNull(function, "keyMapper");
        Objects.requireNonNull(function2, "valueMapper");
        Objects.requireNonNull(binaryOperator, "mergeFunction");
        return collectingAndThen(toMap(function, function2, binaryOperator, new Collectors$$ExternalSyntheticLambda49()), new Collectors$$ExternalSyntheticLambda50());
    }

    public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator, Supplier<M> supplier) {
        return new CollectorImpl(supplier, new Collectors$$ExternalSyntheticLambda43(function, function2, binaryOperator), mapMerger(binaryOperator), CH_ID);
    }

    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda17(), uniqKeysMapAccumulator(function, function2), uniqKeysMapMerger(), CH_CONCURRENT_ID);
    }

    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator) {
        return toConcurrentMap(function, function2, binaryOperator, new Collectors$$ExternalSyntheticLambda15());
    }

    public static <T, K, U, M extends ConcurrentMap<K, U>> Collector<T, ?, M> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator, Supplier<M> supplier) {
        return new CollectorImpl(supplier, new Collectors$$ExternalSyntheticLambda5(function, function2, binaryOperator), mapMerger(binaryOperator), CH_CONCURRENT_ID);
    }

    public static <T> Collector<T, ?, IntSummaryStatistics> summarizingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda53(), new Collectors$$ExternalSyntheticLambda54(toIntFunction), new Collectors$$ExternalSyntheticLambda56(), CH_ID);
    }

    public static <T> Collector<T, ?, LongSummaryStatistics> summarizingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda55(), new Collectors$$ExternalSyntheticLambda66(toLongFunction), new Collectors$$ExternalSyntheticLambda77(), CH_ID);
    }

    public static <T> Collector<T, ?, DoubleSummaryStatistics> summarizingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(new Collectors$$ExternalSyntheticLambda22(), new Collectors$$ExternalSyntheticLambda33(toDoubleFunction), new Collectors$$ExternalSyntheticLambda44(), CH_ID);
    }

    private static final class Partition<T> extends AbstractMap<Boolean, T> implements Map<Boolean, T> {
        final T forFalse;
        final T forTrue;

        Partition(T t, T t2) {
            this.forTrue = t;
            this.forFalse = t2;
        }

        public Set<Map.Entry<Boolean, T>> entrySet() {
            return new AbstractSet<Map.Entry<Boolean, T>>() {
                public int size() {
                    return 2;
                }

                public Iterator<Map.Entry<Boolean, T>> iterator() {
                    return List.m1730of(new AbstractMap.SimpleImmutableEntry(false, Partition.this.forFalse), new AbstractMap.SimpleImmutableEntry(true, Partition.this.forTrue)).iterator();
                }
            };
        }
    }
}
