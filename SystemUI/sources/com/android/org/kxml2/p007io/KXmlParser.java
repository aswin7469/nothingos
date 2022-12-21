package com.android.org.kxml2.p007io;

import androidx.core.p004os.EnvironmentCompat;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.p026io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import kotlin.text.Typography;
import libcore.internal.StringPool;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;

/* renamed from: com.android.org.kxml2.io.KXmlParser */
public class KXmlParser implements XmlPullParser, Closeable {
    private static final char[] ANY = {'A', 'N', 'Y'};
    private static final int ATTLISTDECL = 13;
    private static final char[] COMMENT_DOUBLE_DASH = {'-', '-'};
    private static final Map<String, String> DEFAULT_ENTITIES;
    private static final char[] DOUBLE_QUOTE = {Typography.quote};
    private static final int ELEMENTDECL = 11;
    private static final char[] EMPTY = {'E', 'M', 'P', 'T', 'Y'};
    private static final char[] END_CDATA = {']', ']', Typography.greater};
    private static final char[] END_COMMENT = {'-', '-', Typography.greater};
    private static final char[] END_PROCESSING_INSTRUCTION = {'?', Typography.greater};
    private static final int ENTITYDECL = 12;
    private static final String FEATURE_RELAXED = "http://xmlpull.org/v1/doc/features.html#relaxed";
    private static final char[] FIXED = {'F', 'I', 'X', 'E', 'D'};
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private static final char[] IMPLIED = {'I', 'M', 'P', 'L', 'I', 'E', 'D'};
    private static final char[] NDATA = {'N', 'D', 'A', 'T', 'A'};
    private static final char[] NOTATION = {'N', 'O', 'T', 'A', 'T', 'I', 'O', 'N'};
    private static final int NOTATIONDECL = 14;
    private static final int PARAMETER_ENTITY_REF = 15;
    private static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
    private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
    private static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
    private static final char[] PUBLIC = {'P', 'U', 'B', 'L', 'I', 'C'};
    private static final char[] REQUIRED = {'R', 'E', 'Q', 'U', 'I', 'R', 'E', 'D'};
    private static final char[] SINGLE_QUOTE = {'\''};
    private static final char[] START_ATTLIST = {Typography.less, '!', 'A', 'T', 'T', 'L', 'I', 'S', 'T'};
    private static final char[] START_CDATA = {Typography.less, '!', '[', 'C', 'D', 'A', 'T', 'A', '['};
    private static final char[] START_COMMENT = {Typography.less, '!', '-', '-'};
    private static final char[] START_DOCTYPE = {Typography.less, '!', 'D', 'O', 'C', 'T', 'Y', 'P', 'E'};
    private static final char[] START_ELEMENT = {Typography.less, '!', 'E', 'L', 'E', 'M', 'E', 'N', 'T'};
    private static final char[] START_ENTITY = {Typography.less, '!', 'E', 'N', 'T', 'I', 'T', 'Y'};
    private static final char[] START_NOTATION = {Typography.less, '!', 'N', 'O', 'T', 'A', 'T', 'I', 'O', 'N'};
    private static final char[] START_PROCESSING_INSTRUCTION = {Typography.less, '?'};
    private static final char[] SYSTEM = {'S', 'Y', 'S', 'T', 'E', 'M'};
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final int XML_DECLARATION = 998;
    private int attributeCount;
    private String[] attributes = new String[16];
    private char[] buffer = new char[8192];
    private StringBuilder bufferCapture;
    private int bufferStartColumn;
    private int bufferStartLine;
    private Map<String, Map<String, String>> defaultAttributes;
    private boolean degenerated;
    private int depth;
    private Map<String, char[]> documentEntities;
    private String[] elementStack = new String[16];
    private String encoding;
    private String error;
    private String foundName = null;
    private String foundPrefix = null;
    private boolean isWhitespace;
    private boolean keepNamespaceAttributes;
    private int limit = 0;
    private String location;
    private String name;
    private String namespace;
    private ContentSource nextContentSource;
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private boolean parsedTopLevelStartTag;
    private int position = 0;
    private String prefix;
    private boolean processDocDecl;
    private boolean processNsp;
    private String publicId;
    private Reader reader;
    private boolean relaxed;
    private String rootElementName;
    private Boolean standalone;
    public final StringPool stringPool = new StringPool();
    private String systemId;
    private String text;
    private int type;
    private boolean unresolved;
    private String version;

    /* renamed from: com.android.org.kxml2.io.KXmlParser$ValueContext */
    enum ValueContext {
        ATTRIBUTE,
        TEXT,
        ENTITY_DECLARATION
    }

    public String getAttributeType(int i) {
        return "CDATA";
    }

    public boolean isAttributeDefault(int i) {
        return false;
    }

    static {
        HashMap hashMap = new HashMap();
        DEFAULT_ENTITIES = hashMap;
        hashMap.put("lt", "<");
        hashMap.put("gt", ">");
        hashMap.put("amp", "&");
        hashMap.put("apos", "'");
        hashMap.put("quot", "\"");
    }

    public void keepNamespaceAttributes() {
        this.keepNamespaceAttributes = true;
    }

    private boolean adjustNsp() throws XmlPullParserException {
        int i;
        String str;
        String str2;
        int i2 = 0;
        boolean z = false;
        while (true) {
            i = this.attributeCount;
            if (i2 >= (i << 2)) {
                break;
            }
            String str3 = this.attributes[i2 + 2];
            int indexOf = str3.indexOf(58);
            if (indexOf != -1) {
                String substring = str3.substring(0, indexOf);
                str2 = str3.substring(indexOf + 1);
                str3 = substring;
            } else if (str3.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                str2 = null;
            } else {
                i2 += 4;
            }
            if (str3.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                int[] iArr = this.nspCounts;
                int i3 = this.depth;
                int i4 = iArr[i3];
                iArr[i3] = i4 + 1;
                int i5 = i4 << 1;
                String[] ensureCapacity = ensureCapacity(this.nspStack, i5 + 2);
                this.nspStack = ensureCapacity;
                ensureCapacity[i5] = str2;
                String[] strArr = this.attributes;
                int i6 = i2 + 3;
                ensureCapacity[i5 + 1] = strArr[i6];
                if (str2 != null && strArr[i6].isEmpty()) {
                    checkRelaxed("illegal empty namespace");
                }
                if (this.keepNamespaceAttributes) {
                    this.attributes[i2] = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
                } else {
                    String[] strArr2 = this.attributes;
                    int i7 = this.attributeCount - 1;
                    this.attributeCount = i7;
                    System.arraycopy((Object) strArr2, i2 + 4, (Object) strArr2, i2, (i7 << 2) - i2);
                    i2 -= 4;
                    i2 += 4;
                }
            }
            z = true;
            i2 += 4;
        }
        if (z) {
            int i8 = (i << 2) - 4;
            while (i8 >= 0) {
                int i9 = i8 + 2;
                String str4 = this.attributes[i9];
                int indexOf2 = str4.indexOf(58);
                if (indexOf2 != 0 || this.relaxed) {
                    if (indexOf2 != -1) {
                        String substring2 = str4.substring(0, indexOf2);
                        String substring3 = str4.substring(indexOf2 + 1);
                        String namespace2 = getNamespace(substring2);
                        if (namespace2 != null || this.relaxed) {
                            String[] strArr3 = this.attributes;
                            strArr3[i8] = namespace2;
                            strArr3[i8 + 1] = substring2;
                            strArr3[i9] = substring3;
                        } else {
                            throw new RuntimeException("Undefined Prefix: " + substring2 + " in " + this);
                        }
                    }
                    i8 -= 4;
                } else {
                    throw new RuntimeException("illegal attribute name: " + str4 + " at " + this);
                }
            }
        }
        String str5 = this.foundPrefix;
        if (str5 == null || (str = this.foundName) == null) {
            int indexOf3 = this.name.indexOf(58);
            if (indexOf3 == 0) {
                checkRelaxed("illegal tag name: " + this.name);
            }
            if (indexOf3 != -1) {
                this.prefix = this.name.substring(0, indexOf3);
                this.name = this.name.substring(indexOf3 + 1);
            }
        } else {
            this.prefix = str5;
            this.name = str;
        }
        String namespace3 = getNamespace(this.prefix);
        this.namespace = namespace3;
        if (namespace3 == null) {
            if (this.prefix != null) {
                checkRelaxed("undefined prefix: " + this.prefix);
            }
            this.namespace = "";
        }
        return z;
    }

    private String[] ensureCapacity(String[] strArr, int i) {
        if (strArr.length >= i) {
            return strArr;
        }
        String[] strArr2 = new String[(i + 16)];
        System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, strArr.length);
        return strArr2;
    }

    private void checkRelaxed(String str) throws XmlPullParserException {
        if (!this.relaxed) {
            throw new XmlPullParserException(str, this, (Throwable) null);
        } else if (this.error == null) {
            this.error = "Error: " + str;
        }
    }

    public int next() throws XmlPullParserException, IOException {
        return next(false);
    }

    public int nextToken() throws XmlPullParserException, IOException {
        return next(true);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int next(boolean r10) throws java.p026io.IOException, org.xmlpull.p032v1.XmlPullParserException {
        /*
            r9 = this;
            java.io.Reader r0 = r9.reader
            r1 = 0
            if (r0 == 0) goto L_0x00fe
            int r0 = r9.type
            r2 = 3
            r3 = 1
            if (r0 != r2) goto L_0x0010
            int r0 = r9.depth
            int r0 = r0 - r3
            r9.depth = r0
        L_0x0010:
            boolean r0 = r9.degenerated
            r4 = 0
            if (r0 == 0) goto L_0x001a
            r9.degenerated = r4
            r9.type = r2
            return r2
        L_0x001a:
            java.lang.String r0 = r9.error
            if (r0 == 0) goto L_0x002b
            if (r10 == 0) goto L_0x0029
            r9.text = r0
            r10 = 9
            r9.type = r10
            r9.error = r1
            return r10
        L_0x0029:
            r9.error = r1
        L_0x002b:
            int r0 = r9.peekType(r4)
            r9.type = r0
            r2 = 998(0x3e6, float:1.398E-42)
            if (r0 != r2) goto L_0x003e
            r9.readXmlDeclaration()
            int r0 = r9.peekType(r4)
            r9.type = r0
        L_0x003e:
            r9.text = r1
            r9.isWhitespace = r3
            r9.prefix = r1
            r9.name = r1
            r9.namespace = r1
            r0 = -1
            r9.attributeCount = r0
            r0 = r10 ^ 1
        L_0x004d:
            int r2 = r9.type
            r5 = 7
            java.lang.String r6 = "Unexpected token"
            switch(r2) {
                case 1: goto L_0x00fd;
                case 2: goto L_0x00f7;
                case 3: goto L_0x00f1;
                case 4: goto L_0x00a3;
                case 5: goto L_0x0095;
                case 6: goto L_0x0082;
                case 7: goto L_0x0055;
                case 8: goto L_0x0072;
                case 9: goto L_0x0069;
                case 10: goto L_0x005b;
                default: goto L_0x0055;
            }
        L_0x0055:
            org.xmlpull.v1.XmlPullParserException r10 = new org.xmlpull.v1.XmlPullParserException
            r10.<init>(r6, r9, r1)
            throw r10
        L_0x005b:
            r9.readDoctype(r10)
            boolean r2 = r9.parsedTopLevelStartTag
            if (r2 != 0) goto L_0x0063
            goto L_0x00b9
        L_0x0063:
            org.xmlpull.v1.XmlPullParserException r10 = new org.xmlpull.v1.XmlPullParserException
            r10.<init>(r6, r9, r1)
            throw r10
        L_0x0069:
            java.lang.String r2 = r9.readComment(r10)
            if (r10 == 0) goto L_0x00b9
            r9.text = r2
            goto L_0x00b9
        L_0x0072:
            char[] r2 = START_PROCESSING_INSTRUCTION
            r9.read((char[]) r2)
            char[] r2 = END_PROCESSING_INSTRUCTION
            java.lang.String r2 = r9.readUntil(r2, r10)
            if (r10 == 0) goto L_0x00b9
            r9.text = r2
            goto L_0x00b9
        L_0x0082:
            if (r10 == 0) goto L_0x00a3
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            com.android.org.kxml2.io.KXmlParser$ValueContext r7 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.TEXT
            r9.readEntity(r2, r3, r0, r7)
            java.lang.String r2 = r2.toString()
            r9.text = r2
            goto L_0x00b9
        L_0x0095:
            char[] r2 = START_CDATA
            r9.read((char[]) r2)
            char[] r2 = END_CDATA
            java.lang.String r2 = r9.readUntil(r2, r3)
            r9.text = r2
            goto L_0x00b9
        L_0x00a3:
            r2 = r10 ^ 1
            com.android.org.kxml2.io.KXmlParser$ValueContext r7 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.TEXT
            r8 = 60
            java.lang.String r2 = r9.readValue(r8, r2, r0, r7)
            r9.text = r2
            int r2 = r9.depth
            if (r2 != 0) goto L_0x00b9
            boolean r2 = r9.isWhitespace
            if (r2 == 0) goto L_0x00b9
            r9.type = r5
        L_0x00b9:
            int r2 = r9.depth
            r7 = 4
            if (r2 != 0) goto L_0x00cf
            int r2 = r9.type
            r8 = 6
            if (r2 == r8) goto L_0x00c9
            if (r2 == r7) goto L_0x00c9
            r8 = 5
            if (r2 == r8) goto L_0x00c9
            goto L_0x00cf
        L_0x00c9:
            org.xmlpull.v1.XmlPullParserException r10 = new org.xmlpull.v1.XmlPullParserException
            r10.<init>(r6, r9, r1)
            throw r10
        L_0x00cf:
            if (r10 == 0) goto L_0x00d4
            int r9 = r9.type
            return r9
        L_0x00d4:
            int r2 = r9.type
            if (r2 != r5) goto L_0x00da
            r9.text = r1
        L_0x00da:
            int r2 = r9.peekType(r4)
            java.lang.String r5 = r9.text
            if (r5 == 0) goto L_0x00ed
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x00ed
            if (r2 >= r7) goto L_0x00ed
            r9.type = r7
            return r7
        L_0x00ed:
            r9.type = r2
            goto L_0x004d
        L_0x00f1:
            r9.readEndTag()
            int r9 = r9.type
            return r9
        L_0x00f7:
            r9.parseStartTag(r4, r0)
            int r9 = r9.type
            return r9
        L_0x00fd:
            return r2
        L_0x00fe:
            org.xmlpull.v1.XmlPullParserException r10 = new org.xmlpull.v1.XmlPullParserException
            java.lang.String r0 = "setInput() must be called first."
            r10.<init>(r0, r9, r1)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.kxml2.p007io.KXmlParser.next(boolean):int");
    }

    private String readUntil(char[] cArr, boolean z) throws IOException, XmlPullParserException {
        StringBuilder sb;
        int i = this.position;
        if (!z || this.text == null) {
            sb = null;
        } else {
            sb = new StringBuilder();
            sb.append(this.text);
        }
        while (true) {
            int i2 = this.position;
            if (cArr.length + i2 > this.limit) {
                if (i < i2 && z) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append(this.buffer, i, this.position - i);
                }
                if (!fillBuffer(cArr.length)) {
                    checkRelaxed(UNEXPECTED_EOF);
                    this.type = 9;
                    return null;
                }
                i = this.position;
            }
            int i3 = 0;
            while (i3 < cArr.length) {
                char[] cArr2 = this.buffer;
                int i4 = this.position;
                if (cArr2[i4 + i3] != cArr[i3]) {
                    this.position = i4 + 1;
                } else {
                    i3++;
                }
            }
            int i5 = this.position;
            this.position = cArr.length + i5;
            if (!z) {
                return null;
            }
            if (sb == null) {
                return this.stringPool.get(this.buffer, i, i5 - i);
            }
            sb.append(this.buffer, i, i5 - i);
            return sb.toString();
        }
    }

    private void readXmlDeclaration() throws IOException, XmlPullParserException {
        if (!(this.bufferStartLine == 0 && this.bufferStartColumn == 0 && this.position == 0)) {
            checkRelaxed("processing instructions must not start with xml");
        }
        read(START_PROCESSING_INSTRUCTION);
        parseStartTag(true, true);
        int i = 2;
        if (this.attributeCount < 1 || !"version".equals(this.attributes[2])) {
            checkRelaxed("version expected");
        }
        String[] strArr = this.attributes;
        this.version = strArr[3];
        if (1 >= this.attributeCount || !OutputKeys.ENCODING.equals(strArr[6])) {
            i = 1;
        } else {
            this.encoding = this.attributes[7];
        }
        if (i < this.attributeCount) {
            int i2 = i * 4;
            if (OutputKeys.STANDALONE.equals(this.attributes[i2 + 2])) {
                String str = this.attributes[i2 + 3];
                if ("yes".equals(str)) {
                    this.standalone = Boolean.TRUE;
                } else if ("no".equals(str)) {
                    this.standalone = Boolean.FALSE;
                } else {
                    checkRelaxed("illegal standalone value: " + str);
                }
                i++;
            }
        }
        if (i != this.attributeCount) {
            checkRelaxed("unexpected attributes in XML declaration");
        }
        this.isWhitespace = true;
        this.text = null;
    }

    private String readComment(boolean z) throws IOException, XmlPullParserException {
        read(START_COMMENT);
        if (this.relaxed) {
            return readUntil(END_COMMENT, z);
        }
        String readUntil = readUntil(COMMENT_DOUBLE_DASH, z);
        if (peekCharacter() == 62) {
            this.position++;
            return readUntil;
        }
        throw new XmlPullParserException("Comments may not contain --", this, (Throwable) null);
    }

    private void readDoctype(boolean z) throws IOException, XmlPullParserException {
        int i;
        read(START_DOCTYPE);
        if (z) {
            this.bufferCapture = new StringBuilder();
            i = this.position;
        } else {
            i = -1;
        }
        try {
            skip();
            this.rootElementName = readName();
            readExternalId(true, true);
            skip();
            if (peekCharacter() == 91) {
                readInternalSubset();
            }
            skip();
            read((char) Typography.greater);
            skip();
        } finally {
            if (z) {
                this.bufferCapture.append(this.buffer, 0, this.position);
                this.bufferCapture.delete(0, i);
                this.text = this.bufferCapture.toString();
                this.bufferCapture = null;
            }
        }
    }

    private boolean readExternalId(boolean z, boolean z2) throws IOException, XmlPullParserException {
        int peekCharacter;
        skip();
        int peekCharacter2 = peekCharacter();
        if (peekCharacter2 == 83) {
            read(SYSTEM);
        } else if (peekCharacter2 != 80) {
            return false;
        } else {
            read(PUBLIC);
            skip();
            if (z2) {
                this.publicId = readQuotedId(true);
            } else {
                readQuotedId(false);
            }
        }
        skip();
        if (!z && (peekCharacter = peekCharacter()) != 34 && peekCharacter != 39) {
            return true;
        }
        if (z2) {
            this.systemId = readQuotedId(true);
        } else {
            readQuotedId(false);
        }
        return true;
    }

    private String readQuotedId(boolean z) throws IOException, XmlPullParserException {
        char[] cArr;
        int peekCharacter = peekCharacter();
        if (peekCharacter == 34) {
            cArr = DOUBLE_QUOTE;
        } else if (peekCharacter == 39) {
            cArr = SINGLE_QUOTE;
        } else {
            throw new XmlPullParserException("Expected a quoted string", this, (Throwable) null);
        }
        this.position++;
        return readUntil(cArr, z);
    }

    private void readInternalSubset() throws IOException, XmlPullParserException {
        read('[');
        while (true) {
            skip();
            if (peekCharacter() == 93) {
                this.position++;
                return;
            }
            switch (peekType(true)) {
                case 8:
                    read(START_PROCESSING_INSTRUCTION);
                    readUntil(END_PROCESSING_INSTRUCTION, false);
                    break;
                case 9:
                    readComment(false);
                    break;
                case 11:
                    readElementDeclaration();
                    break;
                case 12:
                    readEntityDeclaration();
                    break;
                case 13:
                    readAttributeListDeclaration();
                    break;
                case 14:
                    readNotationDeclaration();
                    break;
                case 15:
                    throw new XmlPullParserException("Parameter entity references are not supported", this, (Throwable) null);
                default:
                    throw new XmlPullParserException("Unexpected token", this, (Throwable) null);
            }
        }
    }

    private void readElementDeclaration() throws IOException, XmlPullParserException {
        read(START_ELEMENT);
        skip();
        readName();
        readContentSpec();
        skip();
        read((char) Typography.greater);
    }

    private void readContentSpec() throws IOException, XmlPullParserException {
        skip();
        int peekCharacter = peekCharacter();
        int i = 0;
        if (peekCharacter == 40) {
            do {
                if (peekCharacter == 40) {
                    i++;
                } else if (peekCharacter == 41) {
                    i--;
                } else if (peekCharacter == -1) {
                    throw new XmlPullParserException("Unterminated element content spec", this, (Throwable) null);
                }
                this.position++;
                peekCharacter = peekCharacter();
            } while (i > 0);
            if (peekCharacter == 42 || peekCharacter == 63 || peekCharacter == 43) {
                this.position++;
                return;
            }
            return;
        }
        char[] cArr = EMPTY;
        if (peekCharacter == cArr[0]) {
            read(cArr);
            return;
        }
        char[] cArr2 = ANY;
        if (peekCharacter == cArr2[0]) {
            read(cArr2);
            return;
        }
        throw new XmlPullParserException("Expected element content spec", this, (Throwable) null);
    }

    private void readAttributeListDeclaration() throws IOException, XmlPullParserException {
        read(START_ATTLIST);
        skip();
        String readName = readName();
        while (true) {
            skip();
            if (peekCharacter() == 62) {
                this.position++;
                return;
            }
            String readName2 = readName();
            skip();
            if (this.position + 1 < this.limit || fillBuffer(2)) {
                char[] cArr = this.buffer;
                int i = this.position;
                char c = cArr[i];
                char[] cArr2 = NOTATION;
                if (c == cArr2[0] && cArr[i + 1] == cArr2[1]) {
                    read(cArr2);
                    skip();
                }
                if (peekCharacter() == 40) {
                    this.position++;
                    while (true) {
                        skip();
                        readName();
                        skip();
                        int peekCharacter = peekCharacter();
                        if (peekCharacter == 41) {
                            this.position++;
                            break;
                        } else if (peekCharacter == 124) {
                            this.position++;
                        } else {
                            throw new XmlPullParserException("Malformed attribute type", this, (Throwable) null);
                        }
                    }
                } else {
                    readName();
                }
                skip();
                int peekCharacter2 = peekCharacter();
                if (peekCharacter2 == 35) {
                    this.position++;
                    int peekCharacter3 = peekCharacter();
                    if (peekCharacter3 == 82) {
                        read(REQUIRED);
                    } else if (peekCharacter3 == 73) {
                        read(IMPLIED);
                    } else if (peekCharacter3 == 70) {
                        read(FIXED);
                    } else {
                        throw new XmlPullParserException("Malformed attribute type", this, (Throwable) null);
                    }
                    skip();
                    peekCharacter2 = peekCharacter();
                }
                if (peekCharacter2 == 34 || peekCharacter2 == 39) {
                    this.position++;
                    String readValue = readValue((char) peekCharacter2, true, true, ValueContext.ATTRIBUTE);
                    if (peekCharacter() == peekCharacter2) {
                        this.position++;
                    }
                    defineAttributeDefault(readName, readName2, readValue);
                }
            } else {
                throw new XmlPullParserException("Malformed attribute list", this, (Throwable) null);
            }
        }
    }

    private void defineAttributeDefault(String str, String str2, String str3) {
        if (this.defaultAttributes == null) {
            this.defaultAttributes = new HashMap();
        }
        Map map = this.defaultAttributes.get(str);
        if (map == null) {
            map = new HashMap();
            this.defaultAttributes.put(str, map);
        }
        map.put(str2, str3);
    }

    private void readEntityDeclaration() throws IOException, XmlPullParserException {
        boolean z;
        String str;
        read(START_ENTITY);
        skip();
        if (peekCharacter() == 37) {
            this.position++;
            skip();
            z = false;
        } else {
            z = true;
        }
        String readName = readName();
        skip();
        int peekCharacter = peekCharacter();
        if (peekCharacter == 34 || peekCharacter == 39) {
            this.position++;
            str = readValue((char) peekCharacter, true, false, ValueContext.ENTITY_DECLARATION);
            if (peekCharacter() == peekCharacter) {
                this.position++;
            }
        } else if (readExternalId(true, false)) {
            skip();
            int peekCharacter2 = peekCharacter();
            char[] cArr = NDATA;
            if (peekCharacter2 == cArr[0]) {
                read(cArr);
                skip();
                readName();
            }
            str = "";
        } else {
            throw new XmlPullParserException("Expected entity value or external ID", this, (Throwable) null);
        }
        if (z && this.processDocDecl) {
            if (this.documentEntities == null) {
                this.documentEntities = new HashMap();
            }
            this.documentEntities.put(readName, str.toCharArray());
        }
        skip();
        read((char) Typography.greater);
    }

    private void readNotationDeclaration() throws IOException, XmlPullParserException {
        read(START_NOTATION);
        skip();
        readName();
        if (readExternalId(false, false)) {
            skip();
            read((char) Typography.greater);
            return;
        }
        throw new XmlPullParserException("Expected external ID or public ID for notation", this, (Throwable) null);
    }

    private void readEndTag() throws IOException, XmlPullParserException {
        int i = (this.depth - 1) * 4;
        read((char) Typography.less);
        read('/');
        if (this.depth == 0) {
            this.name = readName();
        } else {
            this.name = readExpectedName(this.elementStack[i + 3]);
        }
        skip();
        read((char) Typography.greater);
        if (this.depth == 0) {
            checkRelaxed("read end tag " + this.name + " with no tags open");
            this.type = 9;
            return;
        }
        int i2 = i + 3;
        if (this.name.equals(this.elementStack[i2])) {
            String[] strArr = this.elementStack;
            this.namespace = strArr[i];
            this.prefix = strArr[i + 1];
            this.name = strArr[i + 2];
        } else if (!this.relaxed) {
            throw new XmlPullParserException("expected: /" + this.elementStack[i2] + " read: " + this.name, this, (Throwable) null);
        }
    }

    private int peekType(boolean z) throws IOException, XmlPullParserException {
        if (this.position >= this.limit && !fillBuffer(1)) {
            return 1;
        }
        char[] cArr = this.buffer;
        int i = this.position;
        char c = cArr[i];
        if (c != '%') {
            if (c == '&') {
                return 6;
            }
            if (c != '<') {
                return 4;
            }
            if (i + 3 < this.limit || fillBuffer(4)) {
                char[] cArr2 = this.buffer;
                int i2 = this.position;
                char c2 = cArr2[i2 + 1];
                if (c2 == '!') {
                    char c3 = cArr2[i2 + 2];
                    if (c3 == '-') {
                        return 9;
                    }
                    if (c3 == 'A') {
                        return 13;
                    }
                    if (c3 == 'N') {
                        return 14;
                    }
                    if (c3 == '[') {
                        return 5;
                    }
                    if (c3 == 'D') {
                        return 10;
                    }
                    if (c3 == 'E') {
                        char c4 = cArr2[i2 + 3];
                        if (c4 == 'L') {
                            return 11;
                        }
                        if (c4 == 'N') {
                            return 12;
                        }
                    }
                    throw new XmlPullParserException("Unexpected <!", this, (Throwable) null);
                } else if (c2 == '/') {
                    return 3;
                } else {
                    if (c2 != '?') {
                        return 2;
                    }
                    if (i2 + 5 >= this.limit && !fillBuffer(6)) {
                        return 8;
                    }
                    char[] cArr3 = this.buffer;
                    int i3 = this.position;
                    if (cArr3[i3 + 2] != 'x' && cArr3[i3 + 2] != 'X') {
                        return 8;
                    }
                    if (cArr3[i3 + 3] != 'm' && cArr3[i3 + 3] != 'M') {
                        return 8;
                    }
                    if ((cArr3[i3 + 4] == 'l' || cArr3[i3 + 4] == 'L') && cArr3[i3 + 5] == ' ') {
                        return XML_DECLARATION;
                    }
                    return 8;
                }
            } else {
                throw new XmlPullParserException("Dangling <", this, (Throwable) null);
            }
        } else if (z) {
            return 15;
        } else {
            return 4;
        }
    }

    private void parseStartTag(boolean z, boolean z2) throws IOException, XmlPullParserException {
        Map map;
        if (!z) {
            read((char) Typography.less);
        }
        this.name = readName(true);
        this.attributeCount = 0;
        while (true) {
            skip();
            if (this.position < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.position;
                char c = cArr[i];
                if (!z) {
                    if (c != '/') {
                        if (c == '>') {
                            this.position = i + 1;
                            break;
                        }
                    } else {
                        this.degenerated = true;
                        this.position = i + 1;
                        skip();
                        read((char) Typography.greater);
                        break;
                    }
                } else if (c == '?') {
                    this.position = i + 1;
                    read((char) Typography.greater);
                    return;
                }
                String readName = readName();
                int i2 = this.attributeCount;
                this.attributeCount = i2 + 1;
                int i3 = i2 * 4;
                String[] ensureCapacity = ensureCapacity(this.attributes, i3 + 4);
                this.attributes = ensureCapacity;
                ensureCapacity[i3] = "";
                ensureCapacity[i3 + 1] = null;
                ensureCapacity[i3 + 2] = readName;
                skip();
                if (this.position < this.limit || fillBuffer(1)) {
                    char[] cArr2 = this.buffer;
                    int i4 = this.position;
                    if (cArr2[i4] == '=') {
                        this.position = i4 + 1;
                        skip();
                        if (this.position < this.limit || fillBuffer(1)) {
                            char[] cArr3 = this.buffer;
                            int i5 = this.position;
                            char c2 = cArr3[i5];
                            if (c2 == '\'' || c2 == '\"') {
                                this.position = i5 + 1;
                            } else if (this.relaxed) {
                                c2 = ' ';
                            } else {
                                throw new XmlPullParserException("attr value delimiter missing!", this, (Throwable) null);
                            }
                            this.attributes[i3 + 3] = readValue(c2, true, z2, ValueContext.ATTRIBUTE);
                            if (c2 != ' ' && peekCharacter() == c2) {
                                this.position++;
                            }
                        } else {
                            checkRelaxed(UNEXPECTED_EOF);
                            return;
                        }
                    } else if (this.relaxed) {
                        this.attributes[i3 + 3] = readName;
                    } else {
                        checkRelaxed("Attr.value missing f. " + readName);
                        this.attributes[i3 + 3] = readName;
                    }
                } else {
                    checkRelaxed(UNEXPECTED_EOF);
                    return;
                }
            } else {
                checkRelaxed(UNEXPECTED_EOF);
                return;
            }
        }
        int i6 = this.depth;
        int i7 = i6 + 1;
        this.depth = i7;
        int i8 = i6 * 4;
        if (i7 == 1) {
            this.parsedTopLevelStartTag = true;
        }
        String[] ensureCapacity2 = ensureCapacity(this.elementStack, i8 + 4);
        this.elementStack = ensureCapacity2;
        ensureCapacity2[i8 + 3] = this.name;
        int i9 = this.depth;
        int[] iArr = this.nspCounts;
        if (i9 >= iArr.length) {
            int[] iArr2 = new int[(i9 + 4)];
            System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, iArr.length);
            this.nspCounts = iArr2;
        }
        int[] iArr3 = this.nspCounts;
        int i10 = this.depth;
        iArr3[i10] = iArr3[i10 - 1];
        if (this.processNsp) {
            adjustNsp();
        } else {
            this.namespace = "";
        }
        Map<String, Map<String, String>> map2 = this.defaultAttributes;
        if (!(map2 == null || (map = map2.get(this.name)) == null)) {
            for (Map.Entry entry : map.entrySet()) {
                if (getAttributeValue((String) null, (String) entry.getKey()) == null) {
                    int i11 = this.attributeCount;
                    this.attributeCount = i11 + 1;
                    int i12 = i11 * 4;
                    String[] ensureCapacity3 = ensureCapacity(this.attributes, i12 + 4);
                    this.attributes = ensureCapacity3;
                    ensureCapacity3[i12] = "";
                    ensureCapacity3[i12 + 1] = null;
                    ensureCapacity3[i12 + 2] = (String) entry.getKey();
                    this.attributes[i12 + 3] = (String) entry.getValue();
                }
            }
        }
        String[] strArr = this.elementStack;
        strArr[i8] = this.namespace;
        strArr[i8 + 1] = this.prefix;
        strArr[i8 + 2] = this.name;
    }

    private void readEntity(StringBuilder sb, boolean z, boolean z2, ValueContext valueContext) throws IOException, XmlPullParserException {
        char[] cArr;
        int i;
        int length = sb.length();
        char[] cArr2 = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        if (cArr2[i2] == '&') {
            sb.append((char) Typography.amp);
            while (true) {
                int peekCharacter = peekCharacter();
                if (peekCharacter == 59) {
                    sb.append(';');
                    this.position++;
                    String substring = sb.substring(length + 1, sb.length() - 1);
                    if (z) {
                        this.name = substring;
                    }
                    if (substring.startsWith("#")) {
                        try {
                            if (substring.startsWith("#x")) {
                                i = Integer.parseInt(substring.substring(2), 16);
                            } else {
                                i = Integer.parseInt(substring.substring(1));
                            }
                            sb.delete(length, sb.length());
                            sb.appendCodePoint(i);
                            this.unresolved = false;
                            return;
                        } catch (NumberFormatException unused) {
                            throw new XmlPullParserException("Invalid character reference: &" + substring);
                        } catch (IllegalArgumentException unused2) {
                            throw new XmlPullParserException("Invalid character reference: &" + substring);
                        }
                    } else if (valueContext != ValueContext.ENTITY_DECLARATION) {
                        String str = DEFAULT_ENTITIES.get(substring);
                        if (str != null) {
                            sb.delete(length, sb.length());
                            this.unresolved = false;
                            sb.append(str);
                            return;
                        }
                        Map<String, char[]> map = this.documentEntities;
                        if (map != null && (cArr = map.get(substring)) != null) {
                            sb.delete(length, sb.length());
                            this.unresolved = false;
                            if (this.processDocDecl) {
                                pushContentSource(cArr);
                                return;
                            } else {
                                sb.append(cArr);
                                return;
                            }
                        } else if (this.systemId != null) {
                            sb.delete(length, sb.length());
                            return;
                        } else {
                            this.unresolved = true;
                            if (z2) {
                                checkRelaxed("unresolved: &" + substring + NavigationBarInflaterView.GRAVITY_SEPARATOR);
                                return;
                            }
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (peekCharacter >= 128 || ((peekCharacter >= 48 && peekCharacter <= 57) || ((peekCharacter >= 97 && peekCharacter <= 122) || ((peekCharacter >= 65 && peekCharacter <= 90) || peekCharacter == 95 || peekCharacter == 45 || peekCharacter == 35)))) {
                    this.position++;
                    sb.append((char) peekCharacter);
                } else if (!this.relaxed) {
                    throw new XmlPullParserException("unterminated entity ref", this, (Throwable) null);
                } else {
                    return;
                }
            }
        } else {
            throw new AssertionError();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:88:0x0135  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x014c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String readValue(char r17, boolean r18, boolean r19, com.android.org.kxml2.p007io.KXmlParser.ValueContext r20) throws java.p026io.IOException, org.xmlpull.p032v1.XmlPullParserException {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r20
            int r3 = r0.position
            com.android.org.kxml2.io.KXmlParser$ValueContext r4 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.TEXT
            if (r2 != r4) goto L_0x001b
            java.lang.String r4 = r0.text
            if (r4 == 0) goto L_0x001b
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = r0.text
            r4.append((java.lang.String) r6)
            goto L_0x001c
        L_0x001b:
            r4 = 0
        L_0x001c:
            int r6 = r0.position
            int r7 = r0.limit
            r8 = 1
            if (r6 < r7) goto L_0x0046
            if (r3 >= r6) goto L_0x0034
            if (r4 != 0) goto L_0x002c
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
        L_0x002c:
            char[] r6 = r0.buffer
            int r7 = r0.position
            int r7 = r7 - r3
            r4.append((char[]) r6, (int) r3, (int) r7)
        L_0x0034:
            boolean r3 = r0.fillBuffer(r8)
            if (r3 != 0) goto L_0x0044
            if (r4 == 0) goto L_0x0041
            java.lang.String r0 = r4.toString()
            goto L_0x0043
        L_0x0041:
            java.lang.String r0 = ""
        L_0x0043:
            return r0
        L_0x0044:
            int r3 = r0.position
        L_0x0046:
            char[] r6 = r0.buffer
            int r7 = r0.position
            char r9 = r6[r7]
            if (r9 == r1) goto L_0x0133
            r10 = 62
            r11 = 32
            if (r1 != r11) goto L_0x0058
            if (r9 <= r11) goto L_0x0133
            if (r9 == r10) goto L_0x0133
        L_0x0058:
            r12 = 38
            if (r9 != r12) goto L_0x0060
            if (r18 != 0) goto L_0x0060
            goto L_0x0133
        L_0x0060:
            r6 = 37
            r7 = 60
            r13 = 13
            r14 = 93
            r15 = 10
            r5 = 0
            if (r9 == r13) goto L_0x0091
            if (r9 != r15) goto L_0x0073
            com.android.org.kxml2.io.KXmlParser$ValueContext r10 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.ATTRIBUTE
            if (r2 == r10) goto L_0x0091
        L_0x0073:
            if (r9 == r12) goto L_0x0091
            if (r9 == r7) goto L_0x0091
            if (r9 != r14) goto L_0x007d
            com.android.org.kxml2.io.KXmlParser$ValueContext r10 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.TEXT
            if (r2 == r10) goto L_0x0091
        L_0x007d:
            if (r9 != r6) goto L_0x0083
            com.android.org.kxml2.io.KXmlParser$ValueContext r10 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.ENTITY_DECLARATION
            if (r2 == r10) goto L_0x0091
        L_0x0083:
            boolean r6 = r0.isWhitespace
            if (r9 > r11) goto L_0x0088
            r5 = r8
        L_0x0088:
            r5 = r5 & r6
            r0.isWhitespace = r5
            int r5 = r0.position
            int r5 = r5 + r8
            r0.position = r5
            goto L_0x001c
        L_0x0091:
            if (r4 != 0) goto L_0x0098
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
        L_0x0098:
            char[] r10 = r0.buffer
            int r11 = r0.position
            int r11 = r11 - r3
            r4.append((char[]) r10, (int) r3, (int) r11)
            r3 = 2
            if (r9 != r13) goto L_0x00c9
            int r5 = r0.position
            int r5 = r5 + r8
            int r6 = r0.limit
            if (r5 < r6) goto L_0x00b0
            boolean r3 = r0.fillBuffer(r3)
            if (r3 == 0) goto L_0x00be
        L_0x00b0:
            char[] r3 = r0.buffer
            int r5 = r0.position
            int r6 = r5 + 1
            char r3 = r3[r6]
            if (r3 != r15) goto L_0x00be
            int r5 = r5 + 1
            r0.position = r5
        L_0x00be:
            com.android.org.kxml2.io.KXmlParser$ValueContext r3 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.ATTRIBUTE
            if (r2 != r3) goto L_0x00c5
            r9 = 32
            goto L_0x00c6
        L_0x00c5:
            r9 = r15
        L_0x00c6:
            r10 = r19
            goto L_0x0116
        L_0x00c9:
            if (r9 != r15) goto L_0x00d0
            r10 = r19
            r9 = 32
            goto L_0x0116
        L_0x00d0:
            if (r9 != r12) goto L_0x00dd
            r0.isWhitespace = r5
            r10 = r19
            r0.readEntity(r4, r5, r10, r2)
            int r3 = r0.position
            goto L_0x001c
        L_0x00dd:
            r10 = r19
            if (r9 != r7) goto L_0x00ed
            com.android.org.kxml2.io.KXmlParser$ValueContext r3 = com.android.org.kxml2.p007io.KXmlParser.ValueContext.ATTRIBUTE
            if (r2 != r3) goto L_0x00ea
            java.lang.String r3 = "Illegal: \"<\" inside attribute value"
            r0.checkRelaxed(r3)
        L_0x00ea:
            r0.isWhitespace = r5
            goto L_0x0116
        L_0x00ed:
            if (r9 != r14) goto L_0x0122
            int r6 = r0.position
            int r6 = r6 + r3
            int r3 = r0.limit
            if (r6 < r3) goto L_0x00fd
            r3 = 3
            boolean r3 = r0.fillBuffer(r3)
            if (r3 == 0) goto L_0x0114
        L_0x00fd:
            char[] r3 = r0.buffer
            int r6 = r0.position
            int r7 = r6 + 1
            char r7 = r3[r7]
            if (r7 != r14) goto L_0x0114
            int r6 = r6 + 2
            char r3 = r3[r6]
            r6 = 62
            if (r3 != r6) goto L_0x0114
            java.lang.String r3 = "Illegal: \"]]>\" outside CDATA section"
            r0.checkRelaxed(r3)
        L_0x0114:
            r0.isWhitespace = r5
        L_0x0116:
            int r3 = r0.position
            int r3 = r3 + r8
            r0.position = r3
            r4.append((char) r9)
            int r3 = r0.position
            goto L_0x001c
        L_0x0122:
            if (r9 != r6) goto L_0x012d
            org.xmlpull.v1.XmlPullParserException r1 = new org.xmlpull.v1.XmlPullParserException
            java.lang.String r2 = "This parser doesn't support parameter entities"
            r3 = 0
            r1.<init>(r2, r0, r3)
            throw r1
        L_0x012d:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L_0x0133:
            if (r4 != 0) goto L_0x014c
            boolean r1 = r0.isWhitespace
            if (r1 == 0) goto L_0x0141
            libcore.internal.StringPool r0 = r0.stringPool
            int r7 = r7 - r3
            java.lang.String r0 = r0.get(r6, r3, r7)
            return r0
        L_0x0141:
            java.lang.String r1 = new java.lang.String
            char[] r2 = r0.buffer
            int r0 = r0.position
            int r0 = r0 - r3
            r1.<init>((char[]) r2, (int) r3, (int) r0)
            return r1
        L_0x014c:
            int r7 = r7 - r3
            r4.append((char[]) r6, (int) r3, (int) r7)
            java.lang.String r0 = r4.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.kxml2.p007io.KXmlParser.readValue(char, boolean, boolean, com.android.org.kxml2.io.KXmlParser$ValueContext):java.lang.String");
    }

    private void read(char c) throws IOException, XmlPullParserException {
        int peekCharacter = peekCharacter();
        if (peekCharacter != c) {
            checkRelaxed("expected: '" + c + "' actual: '" + ((char) peekCharacter) + "'");
            if (peekCharacter == -1) {
                return;
            }
        }
        this.position++;
    }

    private void read(char[] cArr) throws IOException, XmlPullParserException {
        if (this.position + cArr.length <= this.limit || fillBuffer(cArr.length)) {
            for (int i = 0; i < cArr.length; i++) {
                if (this.buffer[this.position + i] != cArr[i]) {
                    checkRelaxed("expected: \"" + new String(cArr) + "\" but was \"" + new String(this.buffer, this.position, cArr.length) + "...\"");
                }
            }
            this.position += cArr.length;
            return;
        }
        checkRelaxed("expected: '" + new String(cArr) + "' but was EOF");
    }

    private int peekCharacter() throws IOException, XmlPullParserException {
        if (this.position < this.limit || fillBuffer(1)) {
            return this.buffer[this.position];
        }
        return -1;
    }

    private boolean fillBuffer(int i) throws IOException, XmlPullParserException {
        int i2;
        int i3;
        while (this.nextContentSource != null) {
            if (this.position >= this.limit) {
                popContentSource();
                if (this.limit - this.position >= i) {
                    return true;
                }
            } else {
                throw new XmlPullParserException("Unbalanced entity!", this, (Throwable) null);
            }
        }
        int i4 = 0;
        while (true) {
            i2 = this.position;
            if (i4 >= i2) {
                break;
            }
            if (this.buffer[i4] == 10) {
                this.bufferStartLine++;
                this.bufferStartColumn = 0;
            } else {
                this.bufferStartColumn++;
            }
            i4++;
        }
        StringBuilder sb = this.bufferCapture;
        if (sb != null) {
            sb.append(this.buffer, 0, i2);
        }
        int i5 = this.limit;
        int i6 = this.position;
        if (i5 != i6) {
            int i7 = i5 - i6;
            this.limit = i7;
            char[] cArr = this.buffer;
            System.arraycopy((Object) cArr, i6, (Object) cArr, 0, i7);
        } else {
            this.limit = 0;
        }
        this.position = 0;
        do {
            Reader reader2 = this.reader;
            char[] cArr2 = this.buffer;
            int i8 = this.limit;
            int read = reader2.read(cArr2, i8, cArr2.length - i8);
            if (read == -1) {
                return false;
            }
            i3 = this.limit + read;
            this.limit = i3;
        } while (i3 < i);
        return true;
    }

    private String readExpectedName(String str) throws IOException, XmlPullParserException {
        int length = str.length();
        if (this.position + length < this.limit) {
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = true;
                    break;
                } else if (this.buffer[this.position + i] != str.charAt(i)) {
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                this.position += length;
                return str;
            }
        }
        return readName();
    }

    private String readName() throws IOException, XmlPullParserException {
        return readName(false);
    }

    private String readName(boolean z) throws IOException, XmlPullParserException {
        if (this.position < this.limit || fillBuffer(1)) {
            int i = this.position;
            StringBuilder sb = null;
            this.foundPrefix = null;
            this.foundName = null;
            char c = this.buffer[i];
            if ((c < 'a' || c > 'z') && ((c < 'A' || c > 'Z') && c != '_' && c != ':' && c < 192 && !this.relaxed)) {
                checkRelaxed("name expected");
                return "";
            }
            this.position = i + 1;
            int i2 = -1;
            while (true) {
                if (this.position >= this.limit) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append(this.buffer, i, this.position - i);
                    if (!fillBuffer(1)) {
                        return sb.toString();
                    }
                    i = this.position;
                }
                char[] cArr = this.buffer;
                int i3 = this.position;
                char c2 = cArr[i3];
                if ((c2 >= 'a' && c2 <= 'z') || ((c2 >= 'A' && c2 <= 'Z') || ((c2 >= '0' && c2 <= '9') || c2 == '_' || c2 == '-' || c2 == ':' || c2 == '.' || c2 >= 183))) {
                    if (c2 == ':' && z && this.foundPrefix == null) {
                        this.foundPrefix = this.stringPool.get(cArr, i, i3 - i);
                        i2 = this.position + 1;
                    }
                    this.position++;
                } else if (sb == null) {
                    if (!(this.foundPrefix == null || i3 == i2)) {
                        this.foundName = this.stringPool.get(cArr, i2, i3 - i2);
                    }
                    return this.stringPool.get(this.buffer, i, this.position - i);
                } else {
                    sb.append(cArr, i, i3 - i);
                    return sb.toString();
                }
            }
        } else {
            checkRelaxed("name expected");
            return "";
        }
    }

    private void skip() throws IOException, XmlPullParserException {
        while (true) {
            if (this.position < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.position;
                if (cArr[i] <= ' ') {
                    this.position = i + 1;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void setInput(Reader reader2) throws XmlPullParserException {
        this.reader = reader2;
        this.type = 0;
        this.parsedTopLevelStartTag = false;
        this.name = null;
        this.namespace = null;
        this.degenerated = false;
        this.attributeCount = -1;
        this.encoding = null;
        this.version = null;
        this.standalone = null;
        if (reader2 != null) {
            this.position = 0;
            this.limit = 0;
            this.bufferStartLine = 0;
            this.bufferStartColumn = 0;
            this.depth = 0;
            this.documentEntities = null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r4 = r14.read();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0050, code lost:
        if (r4 != -1) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
        r5 = r13.buffer;
        r6 = r13.limit;
        r13.limit = r6 + 1;
        r5[r6] = (char) r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0061, code lost:
        if (r4 != 62) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0063, code lost:
        r4 = new java.lang.String(r13.buffer, 0, r13.limit);
        r5 = r4.indexOf(javax.xml.transform.OutputKeys.ENCODING);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        if (r5 == -1) goto L_0x00f2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007a, code lost:
        if (r4.charAt(r5) == '\"') goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0082, code lost:
        if (r4.charAt(r5) == '\'') goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0084, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0087, code lost:
        r15 = r5 + 1;
        r15 = r4.substring(r15, r4.indexOf((int) r4.charAt(r5), r15));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x009e, code lost:
        r15 = "UTF-16LE";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00af, code lost:
        r15 = "UTF-16BE";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b3, code lost:
        r15 = "UTF-32BE";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00be, code lost:
        r15 = "UTF-32LE";
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInput(java.p026io.InputStream r14, java.lang.String r15) throws org.xmlpull.p032v1.XmlPullParserException {
        /*
            r13 = this;
            r0 = 0
            r13.position = r0
            r13.limit = r0
            r1 = 1
            if (r15 != 0) goto L_0x000a
            r2 = r1
            goto L_0x000b
        L_0x000a:
            r2 = r0
        L_0x000b:
            if (r14 == 0) goto L_0x012f
            java.lang.String r3 = "UTF-8"
            if (r2 == 0) goto L_0x00f2
            r4 = r0
        L_0x0012:
            int r5 = r13.limit     // Catch:{ Exception -> 0x011a }
            r6 = 4
            r7 = -1
            if (r5 >= r6) goto L_0x002e
            int r5 = r14.read()     // Catch:{ Exception -> 0x011a }
            if (r5 != r7) goto L_0x001f
            goto L_0x002e
        L_0x001f:
            int r4 = r4 << 8
            r4 = r4 | r5
            char[] r6 = r13.buffer     // Catch:{ Exception -> 0x011a }
            int r7 = r13.limit     // Catch:{ Exception -> 0x011a }
            int r8 = r7 + 1
            r13.limit = r8     // Catch:{ Exception -> 0x011a }
            char r5 = (char) r5     // Catch:{ Exception -> 0x011a }
            r6[r7] = r5     // Catch:{ Exception -> 0x011a }
            goto L_0x0012
        L_0x002e:
            int r5 = r13.limit     // Catch:{ Exception -> 0x011a }
            if (r5 != r6) goto L_0x00f2
            r5 = 63
            java.lang.String r6 = "UTF-16LE"
            java.lang.String r8 = "UTF-16BE"
            java.lang.String r9 = "UTF-32BE"
            java.lang.String r10 = "UTF-32LE"
            r11 = 2
            r12 = 60
            switch(r4) {
                case -131072: goto L_0x00bc;
                case 60: goto L_0x00b5;
                case 65279: goto L_0x00b1;
                case 3932223: goto L_0x00a7;
                case 1006632960: goto L_0x00a0;
                case 1006649088: goto L_0x0096;
                case 1010792557: goto L_0x004c;
                default: goto L_0x0042;
            }
        L_0x0042:
            r5 = -65536(0xffffffffffff0000, float:NaN)
            r5 = r5 & r4
            r7 = -16842752(0xfffffffffeff0000, float:-1.6947657E38)
            r9 = 3
            if (r5 != r7) goto L_0x00cf
            goto L_0x00c0
        L_0x004c:
            int r4 = r14.read()     // Catch:{ Exception -> 0x011a }
            if (r4 != r7) goto L_0x0054
            goto L_0x00f2
        L_0x0054:
            char[] r5 = r13.buffer     // Catch:{ Exception -> 0x011a }
            int r6 = r13.limit     // Catch:{ Exception -> 0x011a }
            int r8 = r6 + 1
            r13.limit = r8     // Catch:{ Exception -> 0x011a }
            char r8 = (char) r4     // Catch:{ Exception -> 0x011a }
            r5[r6] = r8     // Catch:{ Exception -> 0x011a }
            r5 = 62
            if (r4 != r5) goto L_0x004c
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x011a }
            char[] r5 = r13.buffer     // Catch:{ Exception -> 0x011a }
            int r6 = r13.limit     // Catch:{ Exception -> 0x011a }
            r4.<init>((char[]) r5, (int) r0, (int) r6)     // Catch:{ Exception -> 0x011a }
            java.lang.String r5 = "encoding"
            int r5 = r4.indexOf((java.lang.String) r5)     // Catch:{ Exception -> 0x011a }
            if (r5 == r7) goto L_0x00f2
        L_0x0074:
            char r15 = r4.charAt(r5)     // Catch:{ Exception -> 0x011a }
            r6 = 34
            if (r15 == r6) goto L_0x0087
            char r15 = r4.charAt(r5)     // Catch:{ Exception -> 0x011a }
            r6 = 39
            if (r15 == r6) goto L_0x0087
            int r5 = r5 + 1
            goto L_0x0074
        L_0x0087:
            int r15 = r5 + 1
            char r5 = r4.charAt(r5)     // Catch:{ Exception -> 0x011a }
            int r5 = r4.indexOf((int) r5, (int) r15)     // Catch:{ Exception -> 0x011a }
            java.lang.String r15 = r4.substring(r15, r5)     // Catch:{ Exception -> 0x011a }
            goto L_0x00f2
        L_0x0096:
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            r15[r0] = r12     // Catch:{ Exception -> 0x011a }
            r15[r1] = r5     // Catch:{ Exception -> 0x011a }
            r13.limit = r11     // Catch:{ Exception -> 0x011a }
        L_0x009e:
            r15 = r6
            goto L_0x00f2
        L_0x00a0:
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            r15[r0] = r12     // Catch:{ Exception -> 0x011a }
            r13.limit = r1     // Catch:{ Exception -> 0x011a }
            goto L_0x00be
        L_0x00a7:
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            r15[r0] = r12     // Catch:{ Exception -> 0x011a }
            r15[r1] = r5     // Catch:{ Exception -> 0x011a }
            r13.limit = r11     // Catch:{ Exception -> 0x011a }
        L_0x00af:
            r15 = r8
            goto L_0x00f2
        L_0x00b1:
            r13.limit = r0     // Catch:{ Exception -> 0x011a }
        L_0x00b3:
            r15 = r9
            goto L_0x00f2
        L_0x00b5:
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            r15[r0] = r12     // Catch:{ Exception -> 0x011a }
            r13.limit = r1     // Catch:{ Exception -> 0x011a }
            goto L_0x00b3
        L_0x00bc:
            r13.limit = r0     // Catch:{ Exception -> 0x011a }
        L_0x00be:
            r15 = r10
            goto L_0x00f2
        L_0x00c0:
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            char r4 = r15[r11]     // Catch:{ Exception -> 0x011a }
            int r4 = r4 << 8
            char r5 = r15[r9]     // Catch:{ Exception -> 0x011a }
            r4 = r4 | r5
            char r4 = (char) r4     // Catch:{ Exception -> 0x011a }
            r15[r0] = r4     // Catch:{ Exception -> 0x011a }
            r13.limit = r1     // Catch:{ Exception -> 0x011a }
            goto L_0x00af
        L_0x00cf:
            r7 = -131072(0xfffffffffffe0000, float:NaN)
            if (r5 != r7) goto L_0x00e2
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            char r4 = r15[r9]     // Catch:{ Exception -> 0x011a }
            int r4 = r4 << 8
            char r5 = r15[r11]     // Catch:{ Exception -> 0x011a }
            r4 = r4 | r5
            char r4 = (char) r4     // Catch:{ Exception -> 0x011a }
            r15[r0] = r4     // Catch:{ Exception -> 0x011a }
            r13.limit = r1     // Catch:{ Exception -> 0x011a }
            goto L_0x009e
        L_0x00e2:
            r4 = r4 & -256(0xffffffffffffff00, float:NaN)
            r5 = -272908544(0xffffffffefbbbf00, float:-1.162092E29)
            if (r4 != r5) goto L_0x00f2
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            char r4 = r15[r9]     // Catch:{ Exception -> 0x011a }
            r15[r0] = r4     // Catch:{ Exception -> 0x011a }
            r13.limit = r1     // Catch:{ Exception -> 0x011a }
            r15 = r3
        L_0x00f2:
            if (r15 != 0) goto L_0x00f5
            goto L_0x00f6
        L_0x00f5:
            r3 = r15
        L_0x00f6:
            int r15 = r13.limit     // Catch:{ Exception -> 0x011a }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x011a }
            r4.<init>((java.p026io.InputStream) r14, (java.lang.String) r3)     // Catch:{ Exception -> 0x011a }
            r13.setInput(r4)     // Catch:{ Exception -> 0x011a }
            r13.encoding = r3     // Catch:{ Exception -> 0x011a }
            r13.limit = r15     // Catch:{ Exception -> 0x011a }
            if (r2 != 0) goto L_0x0119
            int r14 = r13.peekCharacter()     // Catch:{ Exception -> 0x011a }
            r15 = 65279(0xfeff, float:9.1475E-41)
            if (r14 != r15) goto L_0x0119
            int r14 = r13.limit     // Catch:{ Exception -> 0x011a }
            int r14 = r14 - r1
            r13.limit = r14     // Catch:{ Exception -> 0x011a }
            char[] r15 = r13.buffer     // Catch:{ Exception -> 0x011a }
            java.lang.System.arraycopy((java.lang.Object) r15, (int) r1, (java.lang.Object) r15, (int) r0, (int) r14)     // Catch:{ Exception -> 0x011a }
        L_0x0119:
            return
        L_0x011a:
            r14 = move-exception
            org.xmlpull.v1.XmlPullParserException r15 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Invalid stream or encoding: "
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.Object) r14)
            java.lang.String r0 = r0.toString()
            r15.<init>(r0, r13, r14)
            throw r15
        L_0x012f:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException
            java.lang.String r14 = "is == null"
            r13.<init>((java.lang.String) r14)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.kxml2.p007io.KXmlParser.setInput(java.io.InputStream, java.lang.String):void");
    }

    public void close() throws IOException {
        Reader reader2 = this.reader;
        if (reader2 != null) {
            reader2.close();
        }
    }

    public boolean getFeature(String str) {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            return this.processNsp;
        }
        if (FEATURE_RELAXED.equals(str)) {
            return this.relaxed;
        }
        if (XmlPullParser.FEATURE_PROCESS_DOCDECL.equals(str)) {
            return this.processDocDecl;
        }
        return false;
    }

    public String getInputEncoding() {
        return this.encoding;
    }

    public void defineEntityReplacementText(String str, String str2) throws XmlPullParserException {
        if (this.processDocDecl) {
            throw new IllegalStateException("Entity replacement text may not be defined with DOCTYPE processing enabled.");
        } else if (this.reader != null) {
            if (this.documentEntities == null) {
                this.documentEntities = new HashMap();
            }
            this.documentEntities.put(str, str2.toCharArray());
        } else {
            throw new IllegalStateException("Entity replacement text must be defined after setInput()");
        }
    }

    public Object getProperty(String str) {
        if (str.equals(PROPERTY_XMLDECL_VERSION)) {
            return this.version;
        }
        if (str.equals(PROPERTY_XMLDECL_STANDALONE)) {
            return this.standalone;
        }
        if (!str.equals(PROPERTY_LOCATION)) {
            return null;
        }
        String str2 = this.location;
        return str2 != null ? str2 : this.reader.toString();
    }

    public String getRootElementName() {
        return this.rootElementName;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public int getNamespaceCount(int i) {
        if (i <= this.depth) {
            return this.nspCounts[i];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getNamespacePrefix(int i) {
        return this.nspStack[i * 2];
    }

    public String getNamespaceUri(int i) {
        return this.nspStack[(i * 2) + 1];
    }

    public String getNamespace(String str) {
        if (XMLConstants.XML_NS_PREFIX.equals(str)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (XMLConstants.XMLNS_ATTRIBUTE.equals(str)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        }
        for (int namespaceCount = (getNamespaceCount(this.depth) << 1) - 2; namespaceCount >= 0; namespaceCount -= 2) {
            if (str == null) {
                String[] strArr = this.nspStack;
                if (strArr[namespaceCount] == null) {
                    return strArr[namespaceCount + 1];
                }
            } else if (str.equals(this.nspStack[namespaceCount])) {
                return this.nspStack[namespaceCount + 1];
            }
        }
        return null;
    }

    public int getDepth() {
        return this.depth;
    }

    public String getPositionDescription() {
        StringBuilder sb = new StringBuilder(this.type < TYPES.length ? TYPES[this.type] : EnvironmentCompat.MEDIA_UNKNOWN);
        sb.append(' ');
        int i = this.type;
        if (i == 2 || i == 3) {
            if (this.degenerated) {
                sb.append("(empty) ");
            }
            sb.append((char) Typography.less);
            if (this.type == 3) {
                sb.append('/');
            }
            if (this.prefix != null) {
                sb.append("{" + this.namespace + "}" + this.prefix + ":");
            }
            sb.append(this.name);
            int i2 = this.attributeCount * 4;
            for (int i3 = 0; i3 < i2; i3 += 4) {
                sb.append(' ');
                int i4 = i3 + 1;
                if (this.attributes[i4] != null) {
                    sb.append("{" + this.attributes[i3] + "}" + this.attributes[i4] + ":");
                }
                sb.append(this.attributes[i3 + 2] + "='" + this.attributes[i3 + 3] + "'");
            }
            sb.append((char) Typography.greater);
        } else if (i != 7) {
            if (i != 4) {
                sb.append(getText());
            } else if (this.isWhitespace) {
                sb.append("(whitespace)");
            } else {
                String text2 = getText();
                if (text2.length() > 16) {
                    text2 = text2.substring(0, 16) + "...";
                }
                sb.append(text2);
            }
        }
        sb.append("@" + getLineNumber() + ":" + getColumnNumber());
        if (this.location != null) {
            sb.append(" in ");
            sb.append(this.location);
        } else if (this.reader != null) {
            sb.append(" in ");
            sb.append(this.reader.toString());
        }
        return sb.toString();
    }

    public int getLineNumber() {
        int i = this.bufferStartLine;
        for (int i2 = 0; i2 < this.position; i2++) {
            if (this.buffer[i2] == 10) {
                i++;
            }
        }
        return i + 1;
    }

    public int getColumnNumber() {
        int i = this.bufferStartColumn;
        for (int i2 = 0; i2 < this.position; i2++) {
            i = this.buffer[i2] == 10 ? 0 : i + 1;
        }
        return i + 1;
    }

    public boolean isWhitespace() throws XmlPullParserException {
        int i = this.type;
        if (i == 4 || i == 7 || i == 5) {
            return this.isWhitespace;
        }
        throw new XmlPullParserException(ILLEGAL_TYPE, this, (Throwable) null);
    }

    public String getText() {
        int i = this.type;
        if (i < 4) {
            return null;
        }
        if (i == 6 && this.unresolved) {
            return null;
        }
        String str = this.text;
        return str == null ? "" : str;
    }

    public char[] getTextCharacters(int[] iArr) {
        String text2 = getText();
        if (text2 == null) {
            iArr[0] = -1;
            iArr[1] = -1;
            return null;
        }
        char[] charArray = text2.toCharArray();
        iArr[0] = 0;
        iArr[1] = charArray.length;
        return charArray;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.type == 2) {
            return this.degenerated;
        }
        throw new XmlPullParserException(ILLEGAL_TYPE, this, (Throwable) null);
    }

    public int getAttributeCount() {
        return this.attributeCount;
    }

    public String getAttributeNamespace(int i) {
        if (i < this.attributeCount) {
            return this.attributes[i * 4];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeName(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i * 4) + 2];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributePrefix(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i * 4) + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeValue(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i * 4) + 3];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeValue(String str, String str2) {
        for (int i = (this.attributeCount * 4) - 4; i >= 0; i -= 4) {
            if (this.attributes[i + 2].equals(str2) && (str == null || this.attributes[i].equals(str))) {
                return this.attributes[i + 3];
            }
        }
        return null;
    }

    public int getEventType() throws XmlPullParserException {
        return this.type;
    }

    public int nextTag() throws XmlPullParserException, IOException {
        next();
        if (this.type == 4 && this.isWhitespace) {
            next();
        }
        int i = this.type;
        if (i == 3 || i == 2) {
            return i;
        }
        throw new XmlPullParserException("unexpected type", this, (Throwable) null);
    }

    public void require(int i, String str, String str2) throws XmlPullParserException, IOException {
        if (i != this.type || ((str != null && !str.equals(getNamespace())) || (str2 != null && !str2.equals(getName())))) {
            throw new XmlPullParserException("expected: " + TYPES[i] + " {" + str + "}" + str2, this, (Throwable) null);
        }
    }

    public String nextText() throws XmlPullParserException, IOException {
        String str;
        if (this.type == 2) {
            next();
            if (this.type == 4) {
                str = getText();
                next();
            } else {
                str = "";
            }
            if (this.type == 3) {
                return str;
            }
            throw new XmlPullParserException("END_TAG expected", this, (Throwable) null);
        }
        throw new XmlPullParserException("precondition: START_TAG", this, (Throwable) null);
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            this.processNsp = z;
        } else if (XmlPullParser.FEATURE_PROCESS_DOCDECL.equals(str)) {
            this.processDocDecl = z;
        } else if (FEATURE_RELAXED.equals(str)) {
            this.relaxed = z;
        } else {
            throw new XmlPullParserException("unsupported feature: " + str, this, (Throwable) null);
        }
    }

    public void setProperty(String str, Object obj) throws XmlPullParserException {
        if (str.equals(PROPERTY_LOCATION)) {
            this.location = String.valueOf(obj);
            return;
        }
        throw new XmlPullParserException("unsupported property: " + str);
    }

    /* renamed from: com.android.org.kxml2.io.KXmlParser$ContentSource */
    static class ContentSource {
        /* access modifiers changed from: private */
        public final char[] buffer;
        /* access modifiers changed from: private */
        public final int limit;
        /* access modifiers changed from: private */
        public final ContentSource next;
        /* access modifiers changed from: private */
        public final int position;

        ContentSource(ContentSource contentSource, char[] cArr, int i, int i2) {
            this.next = contentSource;
            this.buffer = cArr;
            this.position = i;
            this.limit = i2;
        }
    }

    private void pushContentSource(char[] cArr) {
        this.nextContentSource = new ContentSource(this.nextContentSource, this.buffer, this.position, this.limit);
        this.buffer = cArr;
        this.position = 0;
        this.limit = cArr.length;
    }

    private void popContentSource() {
        this.buffer = this.nextContentSource.buffer;
        this.position = this.nextContentSource.position;
        this.limit = this.nextContentSource.limit;
        this.nextContentSource = this.nextContentSource.next;
    }
}
