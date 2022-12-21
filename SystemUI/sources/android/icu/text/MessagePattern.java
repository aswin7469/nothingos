package android.icu.text;

import android.icu.util.Freezable;

public final class MessagePattern implements Cloneable, Freezable<MessagePattern> {
    public static final int ARG_NAME_NOT_NUMBER = -1;
    public static final int ARG_NAME_NOT_VALID = -2;
    public static final double NO_NUMERIC_VALUE = -1.23456789E8d;

    public enum ApostropheMode {
        DOUBLE_OPTIONAL,
        DOUBLE_REQUIRED
    }

    public MessagePattern() {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern(ApostropheMode apostropheMode) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern parse(String str) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern parseChoiceStyle(String str) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern parsePluralStyle(String str) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern parseSelectStyle(String str) {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
        throw new RuntimeException("Stub!");
    }

    public void clearPatternAndSetApostropheMode(ApostropheMode apostropheMode) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public ApostropheMode getApostropheMode() {
        throw new RuntimeException("Stub!");
    }

    public String getPatternString() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasNamedArguments() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasNumberedArguments() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public static int validateArgumentName(String str) {
        throw new RuntimeException("Stub!");
    }

    public String autoQuoteApostropheDeep() {
        throw new RuntimeException("Stub!");
    }

    public int countParts() {
        throw new RuntimeException("Stub!");
    }

    public Part getPart(int i) {
        throw new RuntimeException("Stub!");
    }

    public Part.Type getPartType(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getPatternIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public String getSubstring(Part part) {
        throw new RuntimeException("Stub!");
    }

    public boolean partSubstringMatches(Part part, String str) {
        throw new RuntimeException("Stub!");
    }

    public double getNumericValue(Part part) {
        throw new RuntimeException("Stub!");
    }

    public double getPluralOffset(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getLimitPartIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern cloneAsThawed() {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern freeze() {
        throw new RuntimeException("Stub!");
    }

    public boolean isFrozen() {
        throw new RuntimeException("Stub!");
    }

    public enum ArgType {
        NONE,
        SIMPLE,
        CHOICE,
        PLURAL,
        SELECT,
        SELECTORDINAL;

        public boolean hasPluralStyle() {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class Part {
        private Part() {
            throw new RuntimeException("Stub!");
        }

        public Type getType() {
            throw new RuntimeException("Stub!");
        }

        public int getIndex() {
            throw new RuntimeException("Stub!");
        }

        public int getLength() {
            throw new RuntimeException("Stub!");
        }

        public int getLimit() {
            throw new RuntimeException("Stub!");
        }

        public int getValue() {
            throw new RuntimeException("Stub!");
        }

        public ArgType getArgType() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object obj) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }

        public enum Type {
            MSG_START,
            MSG_LIMIT,
            SKIP_SYNTAX,
            INSERT_CHAR,
            REPLACE_NUMBER,
            ARG_START,
            ARG_LIMIT,
            ARG_NUMBER,
            ARG_NAME,
            ARG_TYPE,
            ARG_STYLE,
            ARG_SELECTOR,
            ARG_INT,
            ARG_DOUBLE;

            public boolean hasNumericValue() {
                throw new RuntimeException("Stub!");
            }
        }
    }
}
