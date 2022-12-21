package java.lang;

public class NumberFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -2848938806368998894L;

    public NumberFormatException() {
    }

    public NumberFormatException(String str) {
        super(str);
    }

    static NumberFormatException forInputString(String str) {
        return new NumberFormatException("For input string: \"" + str + "\"");
    }

    static NumberFormatException forCharSequence(CharSequence charSequence, int i, int i2, int i3) {
        return new NumberFormatException("Error at index " + (i3 - i) + " in: \"" + charSequence.subSequence(i, i2) + "\"");
    }
}
