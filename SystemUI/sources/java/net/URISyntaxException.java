package java.net;

public class URISyntaxException extends Exception {
    private static final long serialVersionUID = 2137979680897488891L;
    private int index;
    private String input;

    public URISyntaxException(String str, String str2, int i) {
        super(str2);
        if (str == null || str2 == null) {
            throw null;
        } else if (i >= -1) {
            this.input = str;
            this.index = i;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public URISyntaxException(String str, String str2) {
        this(str, str2, -1);
    }

    public String getInput() {
        return this.input;
    }

    public String getReason() {
        return super.getMessage();
    }

    public int getIndex() {
        return this.index;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getReason());
        if (this.index > -1) {
            sb.append(" at index ");
            sb.append(this.index);
        }
        sb.append(": ");
        sb.append(this.input);
        return sb.toString();
    }
}
