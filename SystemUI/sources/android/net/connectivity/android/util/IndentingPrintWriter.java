package android.net.connectivity.android.util;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.PrintWriter;
import java.p026io.Writer;
import java.util.Arrays;

public class IndentingPrintWriter extends PrintWriter {
    private char[] mCurrentIndent;
    private int mCurrentLength;
    private boolean mEmptyLine;
    private StringBuilder mIndentBuilder;
    private char[] mSingleChar;
    private final String mSingleIndent;
    private final int mWrapLength;

    public IndentingPrintWriter(Writer writer) {
        this(writer, "  ", -1);
    }

    public IndentingPrintWriter(Writer writer, String str) {
        this(writer, str, (String) null, -1);
    }

    public IndentingPrintWriter(Writer writer, String str, String str2) {
        this(writer, str, str2, -1);
    }

    public IndentingPrintWriter(Writer writer, String str, int i) {
        this(writer, str, (String) null, i);
    }

    public IndentingPrintWriter(Writer writer, String str, String str2, int i) {
        super(writer);
        StringBuilder sb = new StringBuilder();
        this.mIndentBuilder = sb;
        this.mEmptyLine = true;
        this.mSingleChar = new char[1];
        this.mSingleIndent = str;
        this.mWrapLength = i;
        if (str2 != null) {
            sb.append(str2);
        }
    }

    @Deprecated
    public IndentingPrintWriter setIndent(String str) {
        this.mIndentBuilder.setLength(0);
        this.mIndentBuilder.append(str);
        this.mCurrentIndent = null;
        return this;
    }

    @Deprecated
    public IndentingPrintWriter setIndent(int i) {
        this.mIndentBuilder.setLength(0);
        for (int i2 = 0; i2 < i; i2++) {
            increaseIndent();
        }
        return this;
    }

    public IndentingPrintWriter increaseIndent() {
        this.mIndentBuilder.append(this.mSingleIndent);
        this.mCurrentIndent = null;
        return this;
    }

    public IndentingPrintWriter decreaseIndent() {
        this.mIndentBuilder.delete(0, this.mSingleIndent.length());
        this.mCurrentIndent = null;
        return this;
    }

    public IndentingPrintWriter print(String str, Object obj) {
        String str2;
        if (obj == null) {
            str2 = "null";
        } else if (!obj.getClass().isArray()) {
            str2 = String.valueOf(obj);
        } else if (obj.getClass() == boolean[].class) {
            str2 = Arrays.toString((boolean[]) obj);
        } else if (obj.getClass() == byte[].class) {
            str2 = Arrays.toString((byte[]) obj);
        } else if (obj.getClass() == char[].class) {
            str2 = Arrays.toString((char[]) obj);
        } else if (obj.getClass() == double[].class) {
            str2 = Arrays.toString((double[]) obj);
        } else if (obj.getClass() == float[].class) {
            str2 = Arrays.toString((float[]) obj);
        } else if (obj.getClass() == int[].class) {
            str2 = Arrays.toString((int[]) obj);
        } else if (obj.getClass() == long[].class) {
            str2 = Arrays.toString((long[]) obj);
        } else if (obj.getClass() == short[].class) {
            str2 = Arrays.toString((short[]) obj);
        } else {
            str2 = Arrays.toString((Object[]) obj);
        }
        print(str + "=" + str2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        return this;
    }

    public IndentingPrintWriter printHexInt(String str, int i) {
        print(str + "=0x" + Integer.toHexString(i) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        return this;
    }

    public void println() {
        write(10);
    }

    public void write(int i) {
        char[] cArr = this.mSingleChar;
        cArr[0] = (char) i;
        write(cArr, 0, 1);
    }

    public void write(String str, int i, int i2) {
        char[] cArr = new char[i2];
        str.getChars(i, i2 - i, cArr, 0);
        write(cArr, 0, i2);
    }

    public void write(char[] cArr, int i, int i2) {
        int length = this.mIndentBuilder.length();
        int i3 = i2 + i;
        int i4 = i;
        while (i < i3) {
            int i5 = i + 1;
            char c = cArr[i];
            this.mCurrentLength++;
            if (c == 10) {
                maybeWriteIndent();
                super.write(cArr, i4, i5 - i4);
                this.mEmptyLine = true;
                this.mCurrentLength = 0;
                i4 = i5;
            }
            int i6 = this.mWrapLength;
            if (i6 > 0 && this.mCurrentLength >= i6 - length) {
                if (!this.mEmptyLine) {
                    super.write(10);
                    this.mEmptyLine = true;
                    this.mCurrentLength = i5 - i4;
                } else {
                    maybeWriteIndent();
                    super.write(cArr, i4, i5 - i4);
                    super.write(10);
                    this.mEmptyLine = true;
                    this.mCurrentLength = 0;
                    i4 = i5;
                }
            }
            i = i5;
        }
        if (i4 != i) {
            maybeWriteIndent();
            super.write(cArr, i4, i - i4);
        }
    }

    private void maybeWriteIndent() {
        if (this.mEmptyLine) {
            this.mEmptyLine = false;
            if (this.mIndentBuilder.length() != 0) {
                if (this.mCurrentIndent == null) {
                    this.mCurrentIndent = this.mIndentBuilder.toString().toCharArray();
                }
                char[] cArr = this.mCurrentIndent;
                super.write(cArr, 0, cArr.length);
            }
        }
    }
}
