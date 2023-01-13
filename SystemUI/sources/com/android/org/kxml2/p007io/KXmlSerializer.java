package com.android.org.kxml2.p007io;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.p026io.Writer;
import java.util.Locale;
import javax.xml.XMLConstants;
import kotlin.text.Typography;
import org.xmlpull.p032v1.XmlSerializer;

/* renamed from: com.android.org.kxml2.io.KXmlSerializer */
public class KXmlSerializer implements XmlSerializer {
    private static final int BUFFER_LEN = 8192;
    private int auto;
    private int depth;
    private String[] elementStack = new String[12];
    private String encoding;
    private boolean[] indent = new boolean[4];
    private int mPos;
    private final char[] mText = new char[8192];
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private boolean pending;
    private boolean unicode;
    private Writer writer;

    private void append(char c) throws IOException {
        if (this.mPos >= 8192) {
            flushBuffer();
        }
        char[] cArr = this.mText;
        int i = this.mPos;
        this.mPos = i + 1;
        cArr[i] = c;
    }

    private void append(String str, int i, int i2) throws IOException {
        while (i2 > 0) {
            if (this.mPos == 8192) {
                flushBuffer();
            }
            int i3 = this.mPos;
            int i4 = 8192 - i3;
            if (i4 > i2) {
                i4 = i2;
            }
            int i5 = i + i4;
            str.getChars(i, i5, this.mText, i3);
            i2 -= i4;
            this.mPos += i4;
            i = i5;
        }
    }

    private void append(String str) throws IOException {
        append(str, 0, str.length());
    }

    private final void flushBuffer() throws IOException {
        int i = this.mPos;
        if (i > 0) {
            this.writer.write(this.mText, 0, i);
            this.writer.flush();
            this.mPos = 0;
        }
    }

    private final void check(boolean z) throws IOException {
        if (this.pending) {
            int i = this.depth + 1;
            this.depth = i;
            this.pending = false;
            boolean[] zArr = this.indent;
            if (zArr.length <= i) {
                boolean[] zArr2 = new boolean[(i + 4)];
                System.arraycopy((Object) zArr, 0, (Object) zArr2, 0, i);
                this.indent = zArr2;
            }
            boolean[] zArr3 = this.indent;
            int i2 = this.depth;
            zArr3[i2] = zArr3[i2 - 1];
            int i3 = this.nspCounts[i2 - 1];
            while (true) {
                int[] iArr = this.nspCounts;
                int i4 = this.depth;
                if (i3 < iArr[i4]) {
                    append(" xmlns");
                    int i5 = i3 * 2;
                    if (!this.nspStack[i5].isEmpty()) {
                        append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                        append(this.nspStack[i5]);
                    } else if (getNamespace().isEmpty() && !this.nspStack[i5 + 1].isEmpty()) {
                        throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                    }
                    append("=\"");
                    writeEscaped(this.nspStack[i5 + 1], 34);
                    append((char) Typography.quote);
                    i3++;
                } else {
                    if (iArr.length <= i4 + 1) {
                        int[] iArr2 = new int[(i4 + 8)];
                        System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, i4 + 1);
                        this.nspCounts = iArr2;
                    }
                    int[] iArr3 = this.nspCounts;
                    int i6 = this.depth;
                    iArr3[i6 + 1] = iArr3[i6];
                    if (z) {
                        append(" />");
                        return;
                    } else {
                        append((char) Typography.greater);
                        return;
                    }
                }
            }
        }
    }

    private final void writeEscaped(String str, int i) throws IOException {
        int i2 = 0;
        while (i2 < str.length()) {
            char charAt = str.charAt(i2);
            if (charAt == 9 || charAt == 10 || charAt == 13) {
                if (i == -1) {
                    append(charAt);
                } else {
                    append("&#" + charAt + ';');
                }
            } else if (charAt == '&') {
                append("&amp;");
            } else if (charAt == '<') {
                append("&lt;");
            } else if (charAt == '>') {
                append("&gt;");
            } else if (charAt == i) {
                append(charAt == '\"' ? "&quot;" : "&apos;");
            } else {
                if ((charAt >= ' ' && charAt <= 55295) || (charAt >= 57344 && charAt <= 65533)) {
                    if (this.unicode || charAt < 127) {
                        append(charAt);
                    } else {
                        append("&#" + charAt + NavigationBarInflaterView.GRAVITY_SEPARATOR);
                    }
                } else if (!Character.isHighSurrogate(charAt) || i2 >= str.length() - 1) {
                    reportInvalidCharacter(charAt);
                } else {
                    i2++;
                    writeSurrogate(charAt, str.charAt(i2));
                }
            }
            i2++;
        }
    }

    private static void reportInvalidCharacter(char c) {
        throw new IllegalArgumentException("Illegal character (U+" + Integer.toHexString(c) + NavigationBarInflaterView.KEY_CODE_END);
    }

    public void docdecl(String str) throws IOException {
        append("<!DOCTYPE");
        append(str);
        append((char) Typography.greater);
    }

    public void endDocument() throws IOException {
        while (true) {
            int i = this.depth;
            if (i > 0) {
                String[] strArr = this.elementStack;
                endTag(strArr[(i * 3) - 3], strArr[(i * 3) - 1]);
            } else {
                flush();
                return;
            }
        }
    }

    public void entityRef(String str) throws IOException {
        check(false);
        append((char) Typography.amp);
        append(str);
        append(';');
    }

    public boolean getFeature(String str) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(str)) {
            return this.indent[this.depth];
        }
        return false;
    }

    public String getPrefix(String str, boolean z) {
        try {
            return getPrefix(str, false, z);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private final String getPrefix(String str, boolean z, boolean z2) throws IOException {
        String str2;
        int i = this.nspCounts[this.depth + 1] * 2;
        while (true) {
            i -= 2;
            String str3 = null;
            if (i >= 0) {
                if (this.nspStack[i + 1].equals(str) && (z || !this.nspStack[i].isEmpty())) {
                    String str4 = this.nspStack[i];
                    int i2 = i + 2;
                    while (true) {
                        if (i2 >= this.nspCounts[this.depth + 1] * 2) {
                            str3 = str4;
                            break;
                        } else if (this.nspStack[i2].equals(str4)) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (str3 != null) {
                        return str3;
                    }
                }
            } else if (!z2) {
                return null;
            } else {
                if (str.isEmpty()) {
                    str2 = "";
                } else {
                    do {
                        StringBuilder sb = new StringBuilder("n");
                        int i3 = this.auto;
                        this.auto = i3 + 1;
                        sb.append(i3);
                        str2 = sb.toString();
                        int i4 = (this.nspCounts[this.depth + 1] * 2) - 2;
                        while (true) {
                            if (i4 < 0) {
                                break;
                            } else if (str2.equals(this.nspStack[i4])) {
                                str2 = null;
                                continue;
                                break;
                            } else {
                                i4 -= 2;
                            }
                        }
                    } while (str2 == null);
                }
                boolean z3 = this.pending;
                this.pending = false;
                setPrefix(str2, str);
                this.pending = z3;
                return str2;
            }
        }
    }

    public Object getProperty(String str) {
        throw new RuntimeException("Unsupported property");
    }

    public void ignorableWhitespace(String str) throws IOException {
        text(str);
    }

    public void setFeature(String str, boolean z) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(str)) {
            this.indent[this.depth] = z;
            return;
        }
        throw new RuntimeException("Unsupported Feature");
    }

    public void setProperty(String str, Object obj) {
        throw new RuntimeException("Unsupported Property:" + obj);
    }

    public void setPrefix(String str, String str2) throws IOException {
        check(false);
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        if (!str.equals(getPrefix(str2, true, false))) {
            int[] iArr = this.nspCounts;
            int i = this.depth + 1;
            int i2 = iArr[i];
            iArr[i] = i2 + 1;
            int i3 = i2 << 1;
            String[] strArr = this.nspStack;
            int i4 = i3 + 1;
            if (strArr.length < i4) {
                String[] strArr2 = new String[(strArr.length + 16)];
                System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, i3);
                this.nspStack = strArr2;
            }
            String[] strArr3 = this.nspStack;
            strArr3[i3] = str;
            strArr3[i4] = str2;
        }
    }

    public void setOutput(Writer writer2) {
        this.writer = writer2;
        int[] iArr = this.nspCounts;
        iArr[0] = 2;
        iArr[1] = 2;
        String[] strArr = this.nspStack;
        strArr[0] = "";
        strArr[1] = "";
        strArr[2] = XMLConstants.XML_NS_PREFIX;
        strArr[3] = "http://www.w3.org/XML/1998/namespace";
        this.pending = false;
        this.auto = 0;
        this.depth = 0;
        this.unicode = false;
    }

    public void setOutput(OutputStream outputStream, String str) throws IOException {
        OutputStreamWriter outputStreamWriter;
        if (outputStream != null) {
            if (str == null) {
                outputStreamWriter = new OutputStreamWriter(outputStream);
            } else {
                outputStreamWriter = new OutputStreamWriter(outputStream, str);
            }
            setOutput(outputStreamWriter);
            this.encoding = str;
            if (str != null && str.toLowerCase(Locale.f698US).startsWith("utf")) {
                this.unicode = true;
                return;
            }
            return;
        }
        throw new IllegalArgumentException("os == null");
    }

    public void startDocument(String str, Boolean bool) throws IOException {
        append("<?xml version='1.0' ");
        if (str != null) {
            this.encoding = str;
            if (str.toLowerCase(Locale.f698US).startsWith("utf")) {
                this.unicode = true;
            }
        }
        if (this.encoding != null) {
            append("encoding='");
            append(this.encoding);
            append("' ");
        }
        if (bool != null) {
            append("standalone='");
            append(bool.booleanValue() ? "yes" : "no");
            append("' ");
        }
        append("?>");
    }

    public XmlSerializer startTag(String str, String str2) throws IOException {
        String str3;
        check(false);
        if (this.indent[this.depth]) {
            append("\r\n");
            for (int i = 0; i < this.depth; i++) {
                append("  ");
            }
        }
        int i2 = this.depth * 3;
        String[] strArr = this.elementStack;
        if (strArr.length < i2 + 3) {
            String[] strArr2 = new String[(strArr.length + 12)];
            System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, i2);
            this.elementStack = strArr2;
        }
        if (str == null) {
            str3 = "";
        } else {
            str3 = getPrefix(str, true, true);
        }
        if (str != null && str.isEmpty()) {
            int i3 = this.nspCounts[this.depth];
            while (i3 < this.nspCounts[this.depth + 1]) {
                int i4 = i3 * 2;
                if (!this.nspStack[i4].isEmpty() || this.nspStack[i4 + 1].isEmpty()) {
                    i3++;
                } else {
                    throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                }
            }
        }
        String[] strArr3 = this.elementStack;
        int i5 = i2 + 1;
        strArr3[i2] = str;
        strArr3[i5] = str3;
        strArr3[i5 + 1] = str2;
        append((char) Typography.less);
        if (!str3.isEmpty()) {
            append(str3);
            append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        append(str2);
        this.pending = true;
        return this;
    }

    public XmlSerializer attribute(String str, String str2, String str3) throws IOException {
        if (this.pending) {
            String str4 = "";
            if (str == null) {
                str = str4;
            }
            if (!str.isEmpty()) {
                str4 = getPrefix(str, false, true);
            }
            append(' ');
            if (!str4.isEmpty()) {
                append(str4);
                append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            }
            append(str2);
            append('=');
            char c = Typography.quote;
            if (str3.indexOf(34) != -1) {
                c = '\'';
            }
            append(c);
            writeEscaped(str3, c);
            append(c);
            return this;
        }
        throw new IllegalStateException("illegal position for attribute");
    }

    public void flush() throws IOException {
        check(false);
        flushBuffer();
    }

    public XmlSerializer endTag(String str, String str2) throws IOException {
        if (!this.pending) {
            this.depth--;
        }
        if ((str != null || this.elementStack[this.depth * 3] == null) && ((str == null || str.equals(this.elementStack[this.depth * 3])) && this.elementStack[(this.depth * 3) + 2].equals(str2))) {
            if (this.pending) {
                check(true);
                this.depth--;
            } else {
                if (this.indent[this.depth + 1]) {
                    append("\r\n");
                    for (int i = 0; i < this.depth; i++) {
                        append("  ");
                    }
                }
                append("</");
                String str3 = this.elementStack[(this.depth * 3) + 1];
                if (!str3.isEmpty()) {
                    append(str3);
                    append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                }
                append(str2);
                append((char) Typography.greater);
            }
            int[] iArr = this.nspCounts;
            int i2 = this.depth;
            iArr[i2 + 1] = iArr[i2];
            return this;
        }
        throw new IllegalArgumentException("</{" + str + "}" + str2 + "> does not match start");
    }

    public String getNamespace() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 3];
    }

    public String getName() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 1];
    }

    public int getDepth() {
        boolean z = this.pending;
        int i = this.depth;
        return z ? i + 1 : i;
    }

    public XmlSerializer text(String str) throws IOException {
        check(false);
        this.indent[this.depth] = false;
        writeEscaped(str, -1);
        return this;
    }

    public XmlSerializer text(char[] cArr, int i, int i2) throws IOException {
        text(new String(cArr, i, i2));
        return this;
    }

    public void cdsect(String str) throws IOException {
        check(false);
        String replace = str.replace((CharSequence) "]]>", (CharSequence) "]]]]><![CDATA[>");
        append("<![CDATA[");
        int i = 0;
        while (i < replace.length()) {
            char charAt = replace.charAt(i);
            if ((charAt >= ' ' && charAt <= 55295) || charAt == 9 || charAt == 10 || charAt == 13 || (charAt >= 57344 && charAt <= 65533)) {
                append(charAt);
            } else if (!Character.isHighSurrogate(charAt) || i >= replace.length() - 1) {
                reportInvalidCharacter(charAt);
            } else {
                append("]]>");
                i++;
                writeSurrogate(charAt, replace.charAt(i));
                append("<![CDATA[");
            }
            i++;
        }
        append("]]>");
    }

    private void writeSurrogate(char c, char c2) throws IOException {
        if (Character.isLowSurrogate(c2)) {
            int codePoint = Character.toCodePoint(c, c2);
            append("&#" + codePoint + NavigationBarInflaterView.GRAVITY_SEPARATOR);
            return;
        }
        throw new IllegalArgumentException("Bad surrogate pair (U+" + Integer.toHexString(c) + " U+" + Integer.toHexString(c2) + NavigationBarInflaterView.KEY_CODE_END);
    }

    public void comment(String str) throws IOException {
        check(false);
        append("<!--");
        append(str);
        append("-->");
    }

    public void processingInstruction(String str) throws IOException {
        check(false);
        append("<?");
        append(str);
        append("?>");
    }
}
