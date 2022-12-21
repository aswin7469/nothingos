package java.math;

import java.util.Random;

class BitSieve {
    private static BitSieve smallSieve = new BitSieve();
    private long[] bits;
    private int length;

    private static long bit(int i) {
        return 1 << (i & 63);
    }

    private static int unitIndex(int i) {
        return i >>> 6;
    }

    private BitSieve() {
        this.length = 9600;
        this.bits = new long[(unitIndex(9600 - 1) + 1)];
        set(0);
        int i = 3;
        int i2 = 1;
        do {
            sieveSingle(this.length, i2 + i, i);
            i2 = sieveSearch(this.length, i2 + 1);
            i = (i2 * 2) + 1;
            if (i2 <= 0 || i >= this.length) {
            }
            sieveSingle(this.length, i2 + i, i);
            i2 = sieveSearch(this.length, i2 + 1);
            i = (i2 * 2) + 1;
            return;
        } while (i >= this.length);
    }

    BitSieve(BigInteger bigInteger, int i) {
        this.bits = new long[(unitIndex(i - 1) + 1)];
        this.length = i;
        BitSieve bitSieve = smallSieve;
        int sieveSearch = bitSieve.sieveSearch(bitSieve.length, 0);
        int i2 = (sieveSearch * 2) + 1;
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        do {
            int divideOneWord = i2 - mutableBigInteger.divideOneWord(i2, mutableBigInteger2);
            sieveSingle(i, ((divideOneWord % 2 == 0 ? divideOneWord + i2 : divideOneWord) - 1) / 2, i2);
            BitSieve bitSieve2 = smallSieve;
            sieveSearch = bitSieve2.sieveSearch(bitSieve2.length, sieveSearch + 1);
            i2 = (sieveSearch * 2) + 1;
        } while (sieveSearch > 0);
    }

    private boolean get(int i) {
        return (bit(i) & this.bits[unitIndex(i)]) != 0;
    }

    private void set(int i) {
        int unitIndex = unitIndex(i);
        long[] jArr = this.bits;
        jArr[unitIndex] = jArr[unitIndex] | bit(i);
    }

    private int sieveSearch(int i, int i2) {
        if (i2 >= i) {
            return -1;
        }
        while (get(i2)) {
            i2++;
            if (i2 >= i - 1) {
                return -1;
            }
        }
        return i2;
    }

    private void sieveSingle(int i, int i2, int i3) {
        while (i2 < i) {
            set(i2);
            i2 += i3;
        }
    }

    /* access modifiers changed from: package-private */
    public BigInteger retrieve(BigInteger bigInteger, int i, Random random) {
        int i2 = 1;
        int i3 = 0;
        while (true) {
            long[] jArr = this.bits;
            if (i3 >= jArr.length) {
                return null;
            }
            long j = ~jArr[i3];
            for (int i4 = 0; i4 < 64; i4++) {
                if ((j & 1) == 1) {
                    BigInteger add = bigInteger.add(BigInteger.valueOf((long) i2));
                    if (add.primeToCertainty(i, random)) {
                        return add;
                    }
                }
                j >>>= 1;
                i2 += 2;
            }
            i3++;
        }
    }
}
