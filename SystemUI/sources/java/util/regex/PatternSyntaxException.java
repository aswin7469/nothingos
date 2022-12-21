package java.util.regex;

public class PatternSyntaxException extends IllegalArgumentException {
    private static final long serialVersionUID = -3864639126226059218L;
    private final String desc;
    private final int index;
    private final String pattern;

    public PatternSyntaxException(String str, String str2, int i) {
        this.desc = str;
        this.pattern = str2;
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getMessage() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(this.desc);
        if (this.index >= 0) {
            sb.append(" near index ");
            sb.append(this.index);
        }
        sb.append(System.lineSeparator());
        sb.append(this.pattern);
        int i = this.index;
        if (i >= 0 && (str = this.pattern) != null && i < str.length()) {
            sb.append(System.lineSeparator());
            for (int i2 = 0; i2 < this.index; i2++) {
                sb.append(' ');
            }
            sb.append('^');
        }
        return sb.toString();
    }
}
