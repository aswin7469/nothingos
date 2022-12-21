package org.xmlpull.p032v1.sax2;

import com.android.settingslib.accessibility.AccessibilityUtils;
import java.p026io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;
import org.xmlpull.p032v1.XmlPullParserFactory;

/* renamed from: org.xmlpull.v1.sax2.Driver */
public class Driver implements Locator, XMLReader, Attributes {
    protected static final String APACHE_DYNAMIC_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/dynamic";
    protected static final String APACHE_SCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
    protected static final String DECLARATION_HANDLER_PROPERTY = "http://xml.org/sax/properties/declaration-handler";
    protected static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    protected static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    protected static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    protected ContentHandler contentHandler = new DefaultHandler();
    protected ErrorHandler errorHandler = new DefaultHandler();

    /* renamed from: pp */
    protected XmlPullParser f859pp;
    protected String systemId;

    public DTDHandler getDTDHandler() {
        return null;
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public String getPublicId() {
        return null;
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
    }

    public void setEntityResolver(EntityResolver entityResolver) {
    }

    public Driver() throws XmlPullParserException {
        XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
        newInstance.setNamespaceAware(true);
        this.f859pp = newInstance.newPullParser();
    }

    public Driver(XmlPullParser xmlPullParser) throws XmlPullParserException {
        this.f859pp = xmlPullParser;
    }

    public int getLength() {
        return this.f859pp.getAttributeCount();
    }

    public String getURI(int i) {
        return this.f859pp.getAttributeNamespace(i);
    }

    public String getLocalName(int i) {
        return this.f859pp.getAttributeName(i);
    }

    public String getQName(int i) {
        String attributePrefix = this.f859pp.getAttributePrefix(i);
        if (attributePrefix == null) {
            return this.f859pp.getAttributeName(i);
        }
        return attributePrefix + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + this.f859pp.getAttributeName(i);
    }

    public String getType(int i) {
        return this.f859pp.getAttributeType(i);
    }

    public String getValue(int i) {
        return this.f859pp.getAttributeValue(i);
    }

    public int getIndex(String str, String str2) {
        for (int i = 0; i < this.f859pp.getAttributeCount(); i++) {
            if (this.f859pp.getAttributeNamespace(i).equals(str) && this.f859pp.getAttributeName(i).equals(str2)) {
                return i;
            }
        }
        return -1;
    }

    public int getIndex(String str) {
        for (int i = 0; i < this.f859pp.getAttributeCount(); i++) {
            if (this.f859pp.getAttributeName(i).equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public String getType(String str, String str2) {
        for (int i = 0; i < this.f859pp.getAttributeCount(); i++) {
            if (this.f859pp.getAttributeNamespace(i).equals(str) && this.f859pp.getAttributeName(i).equals(str2)) {
                return this.f859pp.getAttributeType(i);
            }
        }
        return null;
    }

    public String getType(String str) {
        for (int i = 0; i < this.f859pp.getAttributeCount(); i++) {
            if (this.f859pp.getAttributeName(i).equals(str)) {
                return this.f859pp.getAttributeType(i);
            }
        }
        return null;
    }

    public String getValue(String str, String str2) {
        return this.f859pp.getAttributeValue(str, str2);
    }

    public String getValue(String str) {
        return this.f859pp.getAttributeValue((String) null, str);
    }

    public String getSystemId() {
        return this.systemId;
    }

    public int getLineNumber() {
        return this.f859pp.getLineNumber();
    }

    public int getColumnNumber() {
        return this.f859pp.getColumnNumber();
    }

    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (NAMESPACES_FEATURE.equals(str)) {
            return this.f859pp.getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
        }
        if (NAMESPACE_PREFIXES_FEATURE.equals(str)) {
            return this.f859pp.getFeature(XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES);
        }
        if (VALIDATION_FEATURE.equals(str)) {
            return this.f859pp.getFeature(XmlPullParser.FEATURE_VALIDATION);
        }
        return this.f859pp.getFeature(str);
    }

    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (NAMESPACES_FEATURE.equals(str)) {
                this.f859pp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, z);
            } else if (NAMESPACE_PREFIXES_FEATURE.equals(str)) {
                if (this.f859pp.getFeature(XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES) != z) {
                    this.f859pp.setFeature(XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES, z);
                }
            } else if (VALIDATION_FEATURE.equals(str)) {
                this.f859pp.setFeature(XmlPullParser.FEATURE_VALIDATION, z);
            } else {
                this.f859pp.setFeature(str, z);
            }
        } catch (XmlPullParserException unused) {
        }
    }

    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!DECLARATION_HANDLER_PROPERTY.equals(str) && !LEXICAL_HANDLER_PROPERTY.equals(str)) {
            return this.f859pp.getProperty(str);
        }
        return null;
    }

    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (DECLARATION_HANDLER_PROPERTY.equals(str)) {
            throw new SAXNotSupportedException("not supported setting property " + str);
        } else if (!LEXICAL_HANDLER_PROPERTY.equals(str)) {
            try {
                this.f859pp.setProperty(str, obj);
            } catch (XmlPullParserException e) {
                throw new SAXNotSupportedException("not supported set property " + str + ": " + e);
            }
        } else {
            throw new SAXNotSupportedException("not supported setting property " + str);
        }
    }

    public void setContentHandler(ContentHandler contentHandler2) {
        this.contentHandler = contentHandler2;
    }

    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler2) {
        this.errorHandler = errorHandler2;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2 = new java.p026io.FileInputStream(r5.systemId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004a, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r5.errorHandler.fatalError(new org.xml.sax.SAXParseException("could not open file with systemId " + r5.systemId, r5, r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0063, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parse(org.xml.sax.InputSource r6) throws org.xml.sax.SAXException, java.p026io.IOException {
        /*
            r5 = this;
            java.lang.String r0 = "could not open file with systemId "
            java.lang.String r1 = "expected start tag not"
            java.lang.String r2 = r6.getSystemId()
            r5.systemId = r2
            org.xml.sax.ContentHandler r2 = r5.contentHandler
            r2.setDocumentLocator(r5)
            java.io.Reader r2 = r6.getCharacterStream()
            java.lang.String r3 = "parsing initialization error: "
            if (r2 != 0) goto L_0x006a
            java.io.InputStream r2 = r6.getByteStream()     // Catch:{ XmlPullParserException -> 0x00c2 }
            java.lang.String r4 = r6.getEncoding()     // Catch:{ XmlPullParserException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            java.lang.String r6 = r6.getSystemId()     // Catch:{ XmlPullParserException -> 0x00c2 }
            r5.systemId = r6     // Catch:{ XmlPullParserException -> 0x00c2 }
            if (r6 != 0) goto L_0x0036
            org.xml.sax.SAXParseException r6 = new org.xml.sax.SAXParseException     // Catch:{ XmlPullParserException -> 0x00c2 }
            java.lang.String r0 = "null source systemId"
            r6.<init>(r0, r5)     // Catch:{ XmlPullParserException -> 0x00c2 }
            org.xml.sax.ErrorHandler r0 = r5.errorHandler     // Catch:{ XmlPullParserException -> 0x00c2 }
            r0.fatalError(r6)     // Catch:{ XmlPullParserException -> 0x00c2 }
            return
        L_0x0036:
            java.net.URL r6 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0042 }
            java.lang.String r2 = r5.systemId     // Catch:{ MalformedURLException -> 0x0042 }
            r6.<init>(r2)     // Catch:{ MalformedURLException -> 0x0042 }
            java.io.InputStream r2 = r6.openStream()     // Catch:{ MalformedURLException -> 0x0042 }
            goto L_0x0064
        L_0x0042:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x004a }
            java.lang.String r6 = r5.systemId     // Catch:{ FileNotFoundException -> 0x004a }
            r2.<init>((java.lang.String) r6)     // Catch:{ FileNotFoundException -> 0x004a }
            goto L_0x0064
        L_0x004a:
            r6 = move-exception
            org.xml.sax.SAXParseException r1 = new org.xml.sax.SAXParseException     // Catch:{ XmlPullParserException -> 0x00c2 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x00c2 }
            r2.<init>((java.lang.String) r0)     // Catch:{ XmlPullParserException -> 0x00c2 }
            java.lang.String r0 = r5.systemId     // Catch:{ XmlPullParserException -> 0x00c2 }
            r2.append((java.lang.String) r0)     // Catch:{ XmlPullParserException -> 0x00c2 }
            java.lang.String r0 = r2.toString()     // Catch:{ XmlPullParserException -> 0x00c2 }
            r1.<init>(r0, r5, r6)     // Catch:{ XmlPullParserException -> 0x00c2 }
            org.xml.sax.ErrorHandler r6 = r5.errorHandler     // Catch:{ XmlPullParserException -> 0x00c2 }
            r6.fatalError(r1)     // Catch:{ XmlPullParserException -> 0x00c2 }
            return
        L_0x0064:
            org.xmlpull.v1.XmlPullParser r6 = r5.f859pp     // Catch:{ XmlPullParserException -> 0x00c2 }
            r6.setInput(r2, r4)     // Catch:{ XmlPullParserException -> 0x00c2 }
            goto L_0x006f
        L_0x006a:
            org.xmlpull.v1.XmlPullParser r6 = r5.f859pp     // Catch:{ XmlPullParserException -> 0x00c2 }
            r6.setInput(r2)     // Catch:{ XmlPullParserException -> 0x00c2 }
        L_0x006f:
            org.xml.sax.ContentHandler r6 = r5.contentHandler     // Catch:{ XmlPullParserException -> 0x00aa }
            r6.startDocument()     // Catch:{ XmlPullParserException -> 0x00aa }
            org.xmlpull.v1.XmlPullParser r6 = r5.f859pp     // Catch:{ XmlPullParserException -> 0x00aa }
            r6.next()     // Catch:{ XmlPullParserException -> 0x00aa }
            org.xmlpull.v1.XmlPullParser r6 = r5.f859pp     // Catch:{ XmlPullParserException -> 0x00aa }
            int r6 = r6.getEventType()     // Catch:{ XmlPullParserException -> 0x00aa }
            r0 = 2
            if (r6 == r0) goto L_0x009f
            org.xml.sax.SAXParseException r6 = new org.xml.sax.SAXParseException     // Catch:{ XmlPullParserException -> 0x00aa }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x00aa }
            r0.<init>((java.lang.String) r1)     // Catch:{ XmlPullParserException -> 0x00aa }
            org.xmlpull.v1.XmlPullParser r1 = r5.f859pp     // Catch:{ XmlPullParserException -> 0x00aa }
            java.lang.String r1 = r1.getPositionDescription()     // Catch:{ XmlPullParserException -> 0x00aa }
            r0.append((java.lang.String) r1)     // Catch:{ XmlPullParserException -> 0x00aa }
            java.lang.String r0 = r0.toString()     // Catch:{ XmlPullParserException -> 0x00aa }
            r6.<init>(r0, r5)     // Catch:{ XmlPullParserException -> 0x00aa }
            org.xml.sax.ErrorHandler r0 = r5.errorHandler     // Catch:{ XmlPullParserException -> 0x00aa }
            r0.fatalError(r6)     // Catch:{ XmlPullParserException -> 0x00aa }
            return
        L_0x009f:
            org.xmlpull.v1.XmlPullParser r6 = r5.f859pp
            r5.parseSubTree(r6)
            org.xml.sax.ContentHandler r5 = r5.contentHandler
            r5.endDocument()
            return
        L_0x00aa:
            r6 = move-exception
            org.xml.sax.SAXParseException r0 = new org.xml.sax.SAXParseException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r3)
            r1.append((java.lang.Object) r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1, r5, r6)
            org.xml.sax.ErrorHandler r5 = r5.errorHandler
            r5.fatalError(r0)
            return
        L_0x00c2:
            r6 = move-exception
            org.xml.sax.SAXParseException r0 = new org.xml.sax.SAXParseException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r3)
            r1.append((java.lang.Object) r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1, r5, r6)
            org.xml.sax.ErrorHandler r5 = r5.errorHandler
            r5.fatalError(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.p032v1.sax2.Driver.parse(org.xml.sax.InputSource):void");
    }

    public void parse(String str) throws SAXException, IOException {
        parse(new InputSource(str));
    }

    public void parseSubTree(XmlPullParser xmlPullParser) throws SAXException, IOException {
        String str;
        String str2;
        this.f859pp = xmlPullParser;
        boolean feature = xmlPullParser.getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
        try {
            if (xmlPullParser.getEventType() == 2) {
                int[] iArr = new int[2];
                StringBuilder sb = new StringBuilder(16);
                int depth = xmlPullParser.getDepth() - 1;
                int i = 2;
                while (i != 1) {
                    int i2 = 0;
                    if (i != 2) {
                        if (i != 3) {
                            if (i == 4) {
                                this.contentHandler.characters(xmlPullParser.getTextCharacters(iArr), iArr[0], iArr[1]);
                            }
                        } else if (feature) {
                            String name = xmlPullParser.getName();
                            String prefix = xmlPullParser.getPrefix();
                            if (prefix != null) {
                                sb.setLength(0);
                                sb.append(prefix);
                                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                                sb.append(name);
                            }
                            ContentHandler contentHandler2 = this.contentHandler;
                            String namespace = xmlPullParser.getNamespace();
                            if (prefix != null) {
                                str2 = name;
                            } else {
                                str2 = sb.toString();
                            }
                            contentHandler2.endElement(namespace, name, str2);
                            if (depth > xmlPullParser.getDepth()) {
                                i2 = xmlPullParser.getNamespaceCount(xmlPullParser.getDepth());
                            }
                            for (int namespaceCount = xmlPullParser.getNamespaceCount(xmlPullParser.getDepth() - 1) - 1; namespaceCount >= i2; namespaceCount--) {
                                this.contentHandler.endPrefixMapping(xmlPullParser.getNamespacePrefix(namespaceCount));
                            }
                        } else {
                            this.contentHandler.endElement(xmlPullParser.getNamespace(), xmlPullParser.getName(), xmlPullParser.getName());
                        }
                    } else if (feature) {
                        int depth2 = xmlPullParser.getDepth() - 1;
                        int namespaceCount2 = xmlPullParser.getNamespaceCount(depth2 + 1);
                        for (int namespaceCount3 = depth > depth2 ? xmlPullParser.getNamespaceCount(depth2) : 0; namespaceCount3 < namespaceCount2; namespaceCount3++) {
                            this.contentHandler.startPrefixMapping(xmlPullParser.getNamespacePrefix(namespaceCount3), xmlPullParser.getNamespaceUri(namespaceCount3));
                        }
                        String name2 = xmlPullParser.getName();
                        String prefix2 = xmlPullParser.getPrefix();
                        if (prefix2 != null) {
                            sb.setLength(0);
                            sb.append(prefix2);
                            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                            sb.append(name2);
                        }
                        String namespace2 = xmlPullParser.getNamespace();
                        if (prefix2 == null) {
                            str = name2;
                        } else {
                            str = sb.toString();
                        }
                        startElement(namespace2, name2, str);
                    } else {
                        startElement(xmlPullParser.getNamespace(), xmlPullParser.getName(), xmlPullParser.getName());
                    }
                    i = xmlPullParser.next();
                    if (xmlPullParser.getDepth() <= depth) {
                        return;
                    }
                }
                return;
            }
            throw new SAXException("start tag must be read before skiping subtree" + xmlPullParser.getPositionDescription());
        } catch (XmlPullParserException e) {
            SAXParseException sAXParseException = new SAXParseException("parsing error: " + e, this, e);
            e.printStackTrace();
            this.errorHandler.fatalError(sAXParseException);
        }
    }

    /* access modifiers changed from: protected */
    public void startElement(String str, String str2, String str3) throws SAXException {
        this.contentHandler.startElement(str, str2, str3, this);
    }
}
