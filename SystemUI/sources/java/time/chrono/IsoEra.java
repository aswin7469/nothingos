package java.time.chrono;

import java.time.DateTimeException;

public enum IsoEra implements Era {
    BCE,
    CE;

    /* renamed from: of */
    public static IsoEra m943of(int i) {
        if (i == 0) {
            return BCE;
        }
        if (i == 1) {
            return CE;
        }
        throw new DateTimeException("Invalid era: " + i);
    }

    public int getValue() {
        return ordinal();
    }
}
