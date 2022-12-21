package java.util;

public class StringTokenizer implements Enumeration<Object> {
    private int currentPosition;
    private int[] delimiterCodePoints;
    private String delimiters;
    private boolean delimsChanged;
    private boolean hasSurrogates;
    private int maxDelimCodePoint;
    private int maxPosition;
    private int newPosition;
    private boolean retDelims;
    private String str;

    private void setMaxDelimCodePoint() {
        int i = 0;
        if (this.delimiters == null) {
            this.maxDelimCodePoint = 0;
            return;
        }
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < this.delimiters.length()) {
            int charAt = this.delimiters.charAt(i2);
            if (charAt >= 55296 && charAt <= 57343) {
                charAt = this.delimiters.codePointAt(i2);
                this.hasSurrogates = true;
            }
            if (i3 < charAt) {
                i3 = charAt;
            }
            i4++;
            i2 += Character.charCount(charAt);
        }
        this.maxDelimCodePoint = i3;
        if (this.hasSurrogates) {
            this.delimiterCodePoints = new int[i4];
            int i5 = 0;
            while (i < i4) {
                int codePointAt = this.delimiters.codePointAt(i5);
                this.delimiterCodePoints[i] = codePointAt;
                i++;
                i5 += Character.charCount(codePointAt);
            }
        }
    }

    public StringTokenizer(String str2, String str3, boolean z) {
        this.hasSurrogates = false;
        this.currentPosition = 0;
        this.newPosition = -1;
        this.delimsChanged = false;
        this.str = str2;
        this.maxPosition = str2.length();
        this.delimiters = str3;
        this.retDelims = z;
        setMaxDelimCodePoint();
    }

    public StringTokenizer(String str2, String str3) {
        this(str2, str3, false);
    }

    public StringTokenizer(String str2) {
        this(str2, " \t\n\r\f", false);
    }

    private int skipDelimiters(int i) {
        this.delimiters.getClass();
        while (!this.retDelims && i < this.maxPosition) {
            if (this.hasSurrogates) {
                int codePointAt = this.str.codePointAt(i);
                if (codePointAt > this.maxDelimCodePoint || !isDelimiter(codePointAt)) {
                    break;
                }
                i += Character.charCount(codePointAt);
            } else {
                char charAt = this.str.charAt(i);
                if (charAt > this.maxDelimCodePoint || this.delimiters.indexOf((int) charAt) < 0) {
                    break;
                }
                i++;
            }
        }
        return i;
    }

    private int scanToken(int i) {
        int i2 = i;
        while (i2 < this.maxPosition) {
            if (this.hasSurrogates) {
                int codePointAt = this.str.codePointAt(i2);
                if (codePointAt <= this.maxDelimCodePoint && isDelimiter(codePointAt)) {
                    break;
                }
                i2 += Character.charCount(codePointAt);
            } else {
                char charAt = this.str.charAt(i2);
                if (charAt <= this.maxDelimCodePoint && this.delimiters.indexOf((int) charAt) >= 0) {
                    break;
                }
                i2++;
            }
        }
        if (!this.retDelims || i != i2) {
            return i2;
        }
        if (!this.hasSurrogates) {
            char charAt2 = this.str.charAt(i2);
            if (charAt2 > this.maxDelimCodePoint || this.delimiters.indexOf((int) charAt2) < 0) {
                return i2;
            }
            return i2 + 1;
        }
        int codePointAt2 = this.str.codePointAt(i2);
        return (codePointAt2 > this.maxDelimCodePoint || !isDelimiter(codePointAt2)) ? i2 : i2 + Character.charCount(codePointAt2);
    }

    private boolean isDelimiter(int i) {
        for (int i2 : this.delimiterCodePoints) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMoreTokens() {
        int skipDelimiters = skipDelimiters(this.currentPosition);
        this.newPosition = skipDelimiters;
        return skipDelimiters < this.maxPosition;
    }

    public String nextToken() {
        int i = this.newPosition;
        if (i < 0 || this.delimsChanged) {
            i = skipDelimiters(this.currentPosition);
        }
        this.currentPosition = i;
        this.delimsChanged = false;
        this.newPosition = -1;
        if (i < this.maxPosition) {
            int scanToken = scanToken(i);
            this.currentPosition = scanToken;
            return this.str.substring(i, scanToken);
        }
        throw new NoSuchElementException();
    }

    public String nextToken(String str2) {
        this.delimiters = str2;
        this.delimsChanged = true;
        setMaxDelimCodePoint();
        return nextToken();
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public Object nextElement() {
        return nextToken();
    }

    public int countTokens() {
        int skipDelimiters;
        int i = this.currentPosition;
        int i2 = 0;
        while (i < this.maxPosition && (skipDelimiters = skipDelimiters(i)) < this.maxPosition) {
            i = scanToken(skipDelimiters);
            i2++;
        }
        return i2;
    }
}
