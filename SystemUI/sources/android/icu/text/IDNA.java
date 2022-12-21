package android.icu.text;

import java.util.Set;

public abstract class IDNA {
    public static final int CHECK_BIDI = 4;
    public static final int CHECK_CONTEXTJ = 8;
    public static final int CHECK_CONTEXTO = 64;
    public static final int DEFAULT = 0;
    public static final int NONTRANSITIONAL_TO_ASCII = 16;
    public static final int NONTRANSITIONAL_TO_UNICODE = 32;
    public static final int USE_STD3_RULES = 2;

    public enum Error {
        EMPTY_LABEL,
        LABEL_TOO_LONG,
        DOMAIN_NAME_TOO_LONG,
        LEADING_HYPHEN,
        TRAILING_HYPHEN,
        HYPHEN_3_4,
        LEADING_COMBINING_MARK,
        DISALLOWED,
        PUNYCODE,
        LABEL_HAS_DOT,
        INVALID_ACE_LABEL,
        BIDI,
        CONTEXTJ,
        CONTEXTO_PUNCTUATION,
        CONTEXTO_DIGITS
    }

    public abstract StringBuilder labelToASCII(CharSequence charSequence, StringBuilder sb, Info info);

    public abstract StringBuilder labelToUnicode(CharSequence charSequence, StringBuilder sb, Info info);

    public abstract StringBuilder nameToASCII(CharSequence charSequence, StringBuilder sb, Info info);

    public abstract StringBuilder nameToUnicode(CharSequence charSequence, StringBuilder sb, Info info);

    IDNA() {
        throw new RuntimeException("Stub!");
    }

    public static IDNA getUTS46Instance(int i) {
        throw new RuntimeException("Stub!");
    }

    public static final class Info {
        public Info() {
            throw new RuntimeException("Stub!");
        }

        public boolean hasErrors() {
            throw new RuntimeException("Stub!");
        }

        public Set<Error> getErrors() {
            throw new RuntimeException("Stub!");
        }

        public boolean isTransitionalDifferent() {
            throw new RuntimeException("Stub!");
        }
    }
}
