package java.util;

import java.util.concurrent.CountedCompleter;

class ArraysParallelSortHelpers {
    ArraysParallelSortHelpers() {
    }

    static final class EmptyCompleter extends CountedCompleter<Void> {
        static final long serialVersionUID = 2446542900576103244L;

        public final void compute() {
        }

        EmptyCompleter(CountedCompleter<?> countedCompleter) {
            super(countedCompleter);
        }
    }

    static final class Relay extends CountedCompleter<Void> {
        static final long serialVersionUID = 2446542900576103244L;
        final CountedCompleter<?> task;

        public final void compute() {
        }

        Relay(CountedCompleter<?> countedCompleter) {
            super((CountedCompleter<?>) null, 1);
            this.task = countedCompleter;
        }

        public final void onCompletion(CountedCompleter<?> countedCompleter) {
            this.task.compute();
        }
    }

    static final class FJObject {
        FJObject() {
        }

        static final class Sorter<T> extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final T[] f629a;
            final int base;
            Comparator<? super T> comparator;
            final int gran;
            final int size;

            /* renamed from: w */
            final T[] f630w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, T[] tArr, T[] tArr2, int i, int i2, int i3, int i4, Comparator<? super T> comparator2) {
                super(countedCompleter);
                this.f629a = tArr;
                this.f630w = tArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
                this.comparator = comparator2;
            }

            public final void compute() {
                Comparator<? super T> comparator2 = this.comparator;
                T[] tArr = this.f629a;
                T[] tArr2 = this.f630w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    T[] tArr3 = tArr2;
                    int i11 = i4;
                    Merger merger2 = new Merger(countedCompleter, tArr2, tArr, i3, i6, i9, i5 - i6, i, i4, comparator2);
                    Relay relay = new Relay(merger);
                    int i12 = i + i6;
                    int i13 = i + i8;
                    int i14 = i5 - i8;
                    T[] tArr4 = tArr;
                    T[] tArr5 = tArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    Comparator<? super T> comparator3 = comparator2;
                    Merger merger4 = new Merger(relay, tArr4, tArr5, i12, i7, i13, i14, i9, i11, comparator3);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i11;
                    Comparator<? super T> comparator4 = comparator2;
                    new Sorter(relay3, tArr4, tArr5, i13, i14, i10 + i8, i15, comparator4).fork();
                    int i16 = i7;
                    new Sorter(relay3, tArr4, tArr5, i12, i16, i9, i15, comparator4).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, tArr4, tArr5, i, i16, i17, i18, i10, i11, comparator3));
                    new Sorter(relay4, tArr4, tArr5, i17, i18, i10 + i7, i11, comparator2).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    tArr2 = tArr3;
                    i3 = i10;
                    i4 = i11;
                }
                int i19 = i5;
                TimSort.sort(tArr, i, i + i19, comparator2, tArr2, i3, i19);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger<T> extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final T[] f627a;
            Comparator<? super T> comparator;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final T[] f628w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, T[] tArr, T[] tArr2, int i, int i2, int i3, int i4, int i5, int i6, Comparator<? super T> comparator2) {
                super(countedCompleter);
                this.f627a = tArr;
                this.f628w = tArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
                this.comparator = comparator2;
            }

            public final void compute() {
                int i;
                int i2;
                Comparator<? super T> comparator2 = this.comparator;
                T[] tArr = this.f627a;
                T[] tArr2 = this.f628w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (tArr == null || tArr2 == null || i3 < 0 || i5 < 0 || i7 < 0 || comparator2 == null) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    int i10 = 1;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i11 = i4 >>> 1;
                        T t = tArr[i11 + i3];
                        int i12 = i6;
                        while (i9 < i12) {
                            int i13 = (i9 + i12) >>> i10;
                            if (comparator2.compare(t, tArr[i13 + i5]) <= 0) {
                                i12 = i13;
                            } else {
                                i9 = i13 + 1;
                            }
                            i10 = 1;
                        }
                        i2 = i11;
                        i = i12;
                        int i14 = i6 - i;
                        int i15 = i8;
                        Merger merger = new Merger(this, tArr, tArr2, i3 + i2, i4 - i2, i5 + i, i14, i7 + i2 + i, i15, comparator2);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i8 = i15;
                        i6 = i;
                        i7 = i7;
                        tArr = tArr;
                        i5 = i5;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i16 = i6 >>> 1;
                        T t2 = tArr[i16 + i5];
                        int i17 = i4;
                        while (i9 < i17) {
                            int i18 = (i9 + i17) >>> 1;
                            if (comparator2.compare(t2, tArr[i18 + i3]) <= 0) {
                                i17 = i18;
                            } else {
                                i9 = i18 + 1;
                            }
                        }
                        i = i16;
                        i2 = i17;
                        int i142 = i6 - i;
                        int i152 = i8;
                        Merger merger2 = new Merger(this, tArr, tArr2, i3 + i2, i4 - i2, i5 + i, i142, i7 + i2 + i, i152, comparator2);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i8 = i152;
                        i6 = i;
                        i7 = i7;
                        tArr = tArr;
                        i5 = i5;
                    }
                }
                int i19 = i4 + i3;
                int i20 = i6 + i5;
                while (i3 < i19 && i5 < i20) {
                    T t3 = tArr[i3];
                    T t4 = tArr[i5];
                    if (comparator2.compare(t3, t4) <= 0) {
                        i3++;
                    } else {
                        i5++;
                        t3 = t4;
                    }
                    tArr2[i7] = t3;
                    i7++;
                }
                if (i5 < i20) {
                    System.arraycopy((Object) tArr, i5, (Object) tArr2, i7, i20 - i5);
                } else if (i3 < i19) {
                    System.arraycopy((Object) tArr, i3, (Object) tArr2, i7, i19 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJByte {
        FJByte() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final byte[] f605a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final byte[] f606w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f605a = bArr;
                this.f606w = bArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                byte[] bArr = this.f605a;
                byte[] bArr2 = this.f606w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                int i5 = i2;
                CountedCompleter countedCompleter = this;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    byte[] bArr3 = bArr2;
                    Merger merger2 = new Merger(countedCompleter, bArr2, bArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    byte[] bArr4 = bArr;
                    byte[] bArr5 = bArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, bArr4, bArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, bArr4, bArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, bArr4, bArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, bArr4, bArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, bArr4, bArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    bArr2 = bArr3;
                }
                DualPivotQuicksort.sort(bArr, i, (i5 + i) - 1);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final byte[] f603a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final byte[] f604w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f603a = bArr;
                this.f604w = bArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                byte[] bArr = this.f603a;
                byte[] bArr2 = this.f604w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (bArr == null || bArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        byte b = bArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (b <= bArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, bArr, bArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        bArr = bArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        byte b2 = bArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (b2 <= bArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, bArr, bArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        bArr = bArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    byte b3 = bArr[i3];
                    byte b4 = bArr[i5];
                    if (b3 <= b4) {
                        i3++;
                    } else {
                        i5++;
                        b3 = b4;
                    }
                    bArr2[i7] = b3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) bArr, i5, (Object) bArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) bArr, i3, (Object) bArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJChar {
        FJChar() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final char[] f609a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final char[] f610w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, char[] cArr, char[] cArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f609a = cArr;
                this.f610w = cArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                char[] cArr = this.f609a;
                char[] cArr2 = this.f610w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    char[] cArr3 = cArr2;
                    Merger merger2 = new Merger(countedCompleter, cArr2, cArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    char[] cArr4 = cArr;
                    char[] cArr5 = cArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, cArr4, cArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, cArr4, cArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, cArr4, cArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, cArr4, cArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, cArr4, cArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    cArr2 = cArr3;
                }
                DualPivotQuicksort.sort(cArr, i, (i + i5) - 1, cArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final char[] f607a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final char[] f608w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, char[] cArr, char[] cArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f607a = cArr;
                this.f608w = cArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                char[] cArr = this.f607a;
                char[] cArr2 = this.f608w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (cArr == null || cArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        char c = cArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (c <= cArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, cArr, cArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        cArr = cArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        char c2 = cArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (c2 <= cArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, cArr, cArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        cArr = cArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    char c3 = cArr[i3];
                    char c4 = cArr[i5];
                    if (c3 <= c4) {
                        i3++;
                    } else {
                        i5++;
                        c3 = c4;
                    }
                    cArr2[i7] = c3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) cArr, i5, (Object) cArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) cArr, i3, (Object) cArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJShort {
        FJShort() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final short[] f633a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final short[] f634w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, short[] sArr, short[] sArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f633a = sArr;
                this.f634w = sArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                short[] sArr = this.f633a;
                short[] sArr2 = this.f634w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    short[] sArr3 = sArr2;
                    Merger merger2 = new Merger(countedCompleter, sArr2, sArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    short[] sArr4 = sArr;
                    short[] sArr5 = sArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, sArr4, sArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, sArr4, sArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, sArr4, sArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, sArr4, sArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, sArr4, sArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    sArr2 = sArr3;
                }
                DualPivotQuicksort.sort(sArr, i, (i + i5) - 1, sArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final short[] f631a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final short[] f632w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, short[] sArr, short[] sArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f631a = sArr;
                this.f632w = sArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                short[] sArr = this.f631a;
                short[] sArr2 = this.f632w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (sArr == null || sArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        short s = sArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (s <= sArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, sArr, sArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        sArr = sArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        short s2 = sArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (s2 <= sArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, sArr, sArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        sArr = sArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    short s3 = sArr[i3];
                    short s4 = sArr[i5];
                    if (s3 <= s4) {
                        i3++;
                    } else {
                        i5++;
                        s3 = s4;
                    }
                    sArr2[i7] = s3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) sArr, i5, (Object) sArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) sArr, i3, (Object) sArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJInt {
        FJInt() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final int[] f621a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final int[] f622w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, int[] iArr, int[] iArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f621a = iArr;
                this.f622w = iArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                int[] iArr = this.f621a;
                int[] iArr2 = this.f622w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    int[] iArr3 = iArr2;
                    Merger merger2 = new Merger(countedCompleter, iArr2, iArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    int[] iArr4 = iArr;
                    int[] iArr5 = iArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, iArr4, iArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, iArr4, iArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, iArr4, iArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, iArr4, iArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, iArr4, iArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    iArr2 = iArr3;
                }
                DualPivotQuicksort.sort(iArr, i, (i + i5) - 1, iArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final int[] f619a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final int[] f620w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, int[] iArr, int[] iArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f619a = iArr;
                this.f620w = iArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                int[] iArr = this.f619a;
                int[] iArr2 = this.f620w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (iArr == null || iArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        int i11 = iArr[i10 + i3];
                        int i12 = i6;
                        while (i9 < i12) {
                            int i13 = (i9 + i12) >>> 1;
                            if (i11 <= iArr[i13 + i5]) {
                                i12 = i13;
                            } else {
                                i9 = i13 + 1;
                            }
                        }
                        i2 = i10;
                        i = i12;
                        int i14 = i6 - i;
                        int i15 = i8;
                        Merger merger = new Merger(this, iArr, iArr2, i3 + i2, i4 - i2, i5 + i, i14, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        iArr = iArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i16 = i6 >>> 1;
                        int i17 = iArr[i16 + i5];
                        int i18 = i4;
                        while (i9 < i18) {
                            int i19 = (i9 + i18) >>> 1;
                            if (i17 <= iArr[i19 + i3]) {
                                i18 = i19;
                            } else {
                                i9 = i19 + 1;
                            }
                        }
                        i = i16;
                        i2 = i18;
                        int i142 = i6 - i;
                        int i152 = i8;
                        Merger merger2 = new Merger(this, iArr, iArr2, i3 + i2, i4 - i2, i5 + i, i142, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        iArr = iArr;
                    }
                }
                int i20 = i4 + i3;
                int i21 = i6 + i5;
                while (i3 < i20 && i5 < i21) {
                    int i22 = iArr[i3];
                    int i23 = iArr[i5];
                    if (i22 <= i23) {
                        i3++;
                    } else {
                        i5++;
                        i22 = i23;
                    }
                    iArr2[i7] = i22;
                    i7++;
                }
                if (i5 < i21) {
                    System.arraycopy((Object) iArr, i5, (Object) iArr2, i7, i21 - i5);
                } else if (i3 < i20) {
                    System.arraycopy((Object) iArr, i3, (Object) iArr2, i7, i20 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJLong {
        FJLong() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final long[] f625a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final long[] f626w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, long[] jArr, long[] jArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f625a = jArr;
                this.f626w = jArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                long[] jArr = this.f625a;
                long[] jArr2 = this.f626w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    long[] jArr3 = jArr2;
                    Merger merger2 = new Merger(countedCompleter, jArr2, jArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    long[] jArr4 = jArr;
                    long[] jArr5 = jArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, jArr4, jArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, jArr4, jArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, jArr4, jArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, jArr4, jArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, jArr4, jArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    jArr2 = jArr3;
                }
                DualPivotQuicksort.sort(jArr, i, (i + i5) - 1, jArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final long[] f623a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final long[] f624w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, long[] jArr, long[] jArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f623a = jArr;
                this.f624w = jArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                long[] jArr = this.f623a;
                long[] jArr2 = this.f624w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (jArr == null || jArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        long j = jArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (j <= jArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, jArr, jArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        jArr = jArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        long j2 = jArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (j2 <= jArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, jArr, jArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        jArr = jArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    long j3 = jArr[i3];
                    long j4 = jArr[i5];
                    if (j3 <= j4) {
                        i3++;
                    } else {
                        i5++;
                        j3 = j4;
                    }
                    jArr2[i7] = j3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) jArr, i5, (Object) jArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) jArr, i3, (Object) jArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJFloat {
        FJFloat() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final float[] f617a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final float[] f618w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, float[] fArr, float[] fArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f617a = fArr;
                this.f618w = fArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                float[] fArr = this.f617a;
                float[] fArr2 = this.f618w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    float[] fArr3 = fArr2;
                    Merger merger2 = new Merger(countedCompleter, fArr2, fArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    float[] fArr4 = fArr;
                    float[] fArr5 = fArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, fArr4, fArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, fArr4, fArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, fArr4, fArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, fArr4, fArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, fArr4, fArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    fArr2 = fArr3;
                }
                DualPivotQuicksort.sort(fArr, i, (i + i5) - 1, fArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final float[] f615a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final float[] f616w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, float[] fArr, float[] fArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f615a = fArr;
                this.f616w = fArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                float[] fArr = this.f615a;
                float[] fArr2 = this.f616w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (fArr == null || fArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        float f = fArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (f <= fArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, fArr, fArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        fArr = fArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        float f2 = fArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (f2 <= fArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, fArr, fArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        fArr = fArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    float f3 = fArr[i3];
                    float f4 = fArr[i5];
                    if (f3 <= f4) {
                        i3++;
                    } else {
                        i5++;
                        f3 = f4;
                    }
                    fArr2[i7] = f3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) fArr, i5, (Object) fArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) fArr, i3, (Object) fArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }

    static final class FJDouble {
        FJDouble() {
        }

        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final double[] f613a;
            final int base;
            final int gran;
            final int size;

            /* renamed from: w */
            final double[] f614w;
            final int wbase;

            Sorter(CountedCompleter<?> countedCompleter, double[] dArr, double[] dArr2, int i, int i2, int i3, int i4) {
                super(countedCompleter);
                this.f613a = dArr;
                this.f614w = dArr2;
                this.base = i;
                this.size = i2;
                this.wbase = i3;
                this.gran = i4;
            }

            public final void compute() {
                double[] dArr = this.f613a;
                double[] dArr2 = this.f614w;
                int i = this.base;
                int i2 = this.size;
                int i3 = this.wbase;
                int i4 = this.gran;
                CountedCompleter countedCompleter = this;
                int i5 = i2;
                while (i5 > i4) {
                    int i6 = i5 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    int i9 = i3 + i6;
                    int i10 = i3;
                    Merger merger = r0;
                    double[] dArr3 = dArr2;
                    Merger merger2 = new Merger(countedCompleter, dArr2, dArr, i3, i6, i9, i5 - i6, i, i4);
                    Relay relay = new Relay(merger);
                    int i11 = i + i6;
                    int i12 = i + i8;
                    int i13 = i5 - i8;
                    double[] dArr4 = dArr;
                    double[] dArr5 = dArr3;
                    Relay relay2 = relay;
                    Merger merger3 = r0;
                    int i14 = i4;
                    Merger merger4 = new Merger(relay, dArr4, dArr5, i11, i7, i12, i13, i9, i14);
                    Relay relay3 = new Relay(merger3);
                    int i15 = i4;
                    new Sorter(relay3, dArr4, dArr5, i12, i13, i10 + i8, i15).fork();
                    int i16 = i7;
                    new Sorter(relay3, dArr4, dArr5, i11, i16, i9, i15).fork();
                    int i17 = i + i7;
                    int i18 = i6 - i7;
                    Relay relay4 = new Relay(new Merger(relay2, dArr4, dArr5, i, i16, i17, i18, i10, i14));
                    new Sorter(relay4, dArr4, dArr5, i17, i18, i10 + i7, i4).fork();
                    countedCompleter = new EmptyCompleter(relay4);
                    i5 = i7;
                    i3 = i10;
                    dArr2 = dArr3;
                }
                DualPivotQuicksort.sort(dArr, i, (i + i5) - 1, dArr2, i3, i5);
                countedCompleter.tryComplete();
            }
        }

        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a */
            final double[] f611a;
            final int gran;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;

            /* renamed from: w */
            final double[] f612w;
            final int wbase;

            Merger(CountedCompleter<?> countedCompleter, double[] dArr, double[] dArr2, int i, int i2, int i3, int i4, int i5, int i6) {
                super(countedCompleter);
                this.f611a = dArr;
                this.f612w = dArr2;
                this.lbase = i;
                this.lsize = i2;
                this.rbase = i3;
                this.rsize = i4;
                this.wbase = i5;
                this.gran = i6;
            }

            public final void compute() {
                int i;
                int i2;
                double[] dArr = this.f611a;
                double[] dArr2 = this.f612w;
                int i3 = this.lbase;
                int i4 = this.lsize;
                int i5 = this.rbase;
                int i6 = this.rsize;
                int i7 = this.wbase;
                int i8 = this.gran;
                if (dArr == null || dArr2 == null || i3 < 0 || i5 < 0 || i7 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    int i9 = 0;
                    if (i4 >= i6) {
                        if (i4 <= i8) {
                            break;
                        }
                        int i10 = i4 >>> 1;
                        double d = dArr[i10 + i3];
                        int i11 = i6;
                        while (i9 < i11) {
                            int i12 = (i9 + i11) >>> 1;
                            if (d <= dArr[i12 + i5]) {
                                i11 = i12;
                            } else {
                                i9 = i12 + 1;
                            }
                        }
                        i2 = i10;
                        i = i11;
                        int i13 = i6 - i;
                        int i14 = i8;
                        Merger merger = new Merger(this, dArr, dArr2, i3 + i2, i4 - i2, i5 + i, i13, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger.fork();
                        i4 = i2;
                        i6 = i;
                        dArr = dArr;
                    } else if (i6 <= i8) {
                        break;
                    } else {
                        int i15 = i6 >>> 1;
                        double d2 = dArr[i15 + i5];
                        int i16 = i4;
                        while (i9 < i16) {
                            int i17 = (i9 + i16) >>> 1;
                            if (d2 <= dArr[i17 + i3]) {
                                i16 = i17;
                            } else {
                                i9 = i17 + 1;
                            }
                        }
                        i = i15;
                        i2 = i16;
                        int i132 = i6 - i;
                        int i142 = i8;
                        Merger merger2 = new Merger(this, dArr, dArr2, i3 + i2, i4 - i2, i5 + i, i132, i7 + i2 + i, i8);
                        addToPendingCount(1);
                        merger2.fork();
                        i4 = i2;
                        i6 = i;
                        dArr = dArr;
                    }
                }
                int i18 = i4 + i3;
                int i19 = i6 + i5;
                while (i3 < i18 && i5 < i19) {
                    double d3 = dArr[i3];
                    double d4 = dArr[i5];
                    if (d3 <= d4) {
                        i3++;
                    } else {
                        i5++;
                        d3 = d4;
                    }
                    dArr2[i7] = d3;
                    i7++;
                }
                if (i5 < i19) {
                    System.arraycopy((Object) dArr, i5, (Object) dArr2, i7, i19 - i5);
                } else if (i3 < i18) {
                    System.arraycopy((Object) dArr, i3, (Object) dArr2, i7, i18 - i3);
                }
                tryComplete();
            }
        }
    }
}
