package android.icu.text;

import java.util.Locale;

public abstract class CaseMap {
    public abstract CaseMap omitUnchangedText();

    private CaseMap() {
        throw new RuntimeException("Stub!");
    }

    public static Lower toLower() {
        throw new RuntimeException("Stub!");
    }

    public static Upper toUpper() {
        throw new RuntimeException("Stub!");
    }

    public static Title toTitle() {
        throw new RuntimeException("Stub!");
    }

    public static Fold fold() {
        throw new RuntimeException("Stub!");
    }

    public static final class Fold extends CaseMap {
        private Fold() {
            super();
            throw new RuntimeException("Stub!");
        }

        public Fold omitUnchangedText() {
            throw new RuntimeException("Stub!");
        }

        public Fold turkic() {
            throw new RuntimeException("Stub!");
        }

        public String apply(CharSequence charSequence) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A apply(CharSequence charSequence, A a, Edits edits) {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class Lower extends CaseMap {
        private Lower() {
            super();
            throw new RuntimeException("Stub!");
        }

        public Lower omitUnchangedText() {
            throw new RuntimeException("Stub!");
        }

        public String apply(Locale locale, CharSequence charSequence) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A apply(Locale locale, CharSequence charSequence, A a, Edits edits) {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class Title extends CaseMap {
        private Title() {
            super();
            throw new RuntimeException("Stub!");
        }

        public Title wholeString() {
            throw new RuntimeException("Stub!");
        }

        public Title sentences() {
            throw new RuntimeException("Stub!");
        }

        public Title omitUnchangedText() {
            throw new RuntimeException("Stub!");
        }

        public Title noLowercase() {
            throw new RuntimeException("Stub!");
        }

        public Title noBreakAdjustment() {
            throw new RuntimeException("Stub!");
        }

        public Title adjustToCased() {
            throw new RuntimeException("Stub!");
        }

        public String apply(Locale locale, BreakIterator breakIterator, CharSequence charSequence) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A apply(Locale locale, BreakIterator breakIterator, CharSequence charSequence, A a, Edits edits) {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class Upper extends CaseMap {
        private Upper() {
            super();
            throw new RuntimeException("Stub!");
        }

        public Upper omitUnchangedText() {
            throw new RuntimeException("Stub!");
        }

        public String apply(Locale locale, CharSequence charSequence) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A apply(Locale locale, CharSequence charSequence, A a, Edits edits) {
            throw new RuntimeException("Stub!");
        }
    }
}
