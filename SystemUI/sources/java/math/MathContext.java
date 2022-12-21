package java.math;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.p026io.StreamCorruptedException;

public final class MathContext implements Serializable {
    public static final MathContext DECIMAL128 = new MathContext(34, RoundingMode.HALF_EVEN);
    public static final MathContext DECIMAL32 = new MathContext(7, RoundingMode.HALF_EVEN);
    public static final MathContext DECIMAL64 = new MathContext(16, RoundingMode.HALF_EVEN);
    private static final int DEFAULT_DIGITS = 9;
    private static final RoundingMode DEFAULT_ROUNDINGMODE = RoundingMode.HALF_UP;
    private static final int MIN_DIGITS = 0;
    public static final MathContext UNLIMITED = new MathContext(0, RoundingMode.HALF_UP);
    private static final long serialVersionUID = 5579720004786848255L;
    final int precision;
    final RoundingMode roundingMode;

    public MathContext(int i) {
        this(i, DEFAULT_ROUNDINGMODE);
    }

    public MathContext(int i, RoundingMode roundingMode2) {
        if (i < 0) {
            throw new IllegalArgumentException("Digits < 0");
        } else if (roundingMode2 != null) {
            this.precision = i;
            this.roundingMode = roundingMode2;
        } else {
            throw new NullPointerException("null RoundingMode");
        }
    }

    public MathContext(String str) {
        if (str != null) {
            try {
                if (str.startsWith("precision=")) {
                    int indexOf = str.indexOf(32);
                    int parseInt = Integer.parseInt(str.substring(10, indexOf));
                    int i = indexOf + 1;
                    if (str.startsWith("roundingMode=", i)) {
                        this.roundingMode = RoundingMode.valueOf(str.substring(i + 13, str.length()));
                        if (parseInt >= 0) {
                            this.precision = parseInt;
                            return;
                        }
                        throw new IllegalArgumentException("Digits < 0");
                    }
                    throw new RuntimeException();
                }
                throw new RuntimeException();
            } catch (RuntimeException unused) {
                throw new IllegalArgumentException("bad string format");
            }
        } else {
            throw new NullPointerException("null String");
        }
    }

    public int getPrecision() {
        return this.precision;
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MathContext)) {
            return false;
        }
        MathContext mathContext = (MathContext) obj;
        if (mathContext.precision == this.precision && mathContext.roundingMode == this.roundingMode) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.precision + (this.roundingMode.hashCode() * 59);
    }

    public String toString() {
        return "precision=" + this.precision + " roundingMode=" + this.roundingMode.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.precision < 0) {
            throw new StreamCorruptedException("MathContext: invalid digits in stream");
        } else if (this.roundingMode == null) {
            throw new StreamCorruptedException("MathContext: null roundingMode in stream");
        }
    }
}
