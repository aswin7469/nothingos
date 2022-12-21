package com.nothing.experience.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class StringUtils {
    public static final String alphanum;
    public static final String digits = "0123456789";
    public static final String lower;
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final char[] buf;
    private final Random random;
    private final char[] symbols;

    static {
        String lowerCase = upper.toLowerCase(Locale.ROOT);
        lower = lowerCase;
        alphanum = upper + lowerCase + digits;
    }

    public StringUtils(int i, Random random2, String str) {
        if (i < 1) {
            throw new IllegalArgumentException();
        } else if (str.length() >= 2) {
            Objects.requireNonNull(random2);
            Random random3 = random2;
            this.random = random2;
            this.symbols = str.toCharArray();
            this.buf = new char[i];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public StringUtils(int i, Random random2) {
        this(i, random2, alphanum);
    }

    public StringUtils(int i) {
        this(i, new SecureRandom());
    }

    public StringUtils() {
        this(21);
    }

    public String getString() {
        int i = 0;
        while (true) {
            char[] cArr = this.buf;
            if (i >= cArr.length) {
                return new String(cArr);
            }
            char[] cArr2 = this.symbols;
            cArr[i] = cArr2[this.random.nextInt(cArr2.length)];
            i++;
        }
    }
}
