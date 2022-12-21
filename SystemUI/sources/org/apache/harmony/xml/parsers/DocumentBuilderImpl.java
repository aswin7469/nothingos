package org.apache.harmony.xml.parsers;

import com.android.org.kxml2.p007io.KXmlParser;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.IOException;
import javax.xml.parsers.DocumentBuilder;
import libcore.p030io.IoUtils;
import org.apache.harmony.xml.dom.CDATASectionImpl;
import org.apache.harmony.xml.dom.DOMImplementationImpl;
import org.apache.harmony.xml.dom.DocumentImpl;
import org.apache.harmony.xml.dom.TextImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;

class DocumentBuilderImpl extends DocumentBuilder {
    private static DOMImplementationImpl dom = DOMImplementationImpl.getInstance();
    private boolean coalescing;
    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;
    private boolean ignoreComments;
    private boolean ignoreElementContentWhitespace;
    private boolean namespaceAware;

    public boolean isValidating() {
        return false;
    }

    DocumentBuilderImpl() {
    }

    public void reset() {
        this.coalescing = false;
        this.entityResolver = null;
        this.errorHandler = null;
        this.ignoreComments = false;
        this.ignoreElementContentWhitespace = false;
        this.namespaceAware = false;
    }

    public DOMImplementation getDOMImplementation() {
        return dom;
    }

    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }

    public Document newDocument() {
        return dom.createDocument((String) null, (String) null, (DocumentType) null);
    }

    public Document parse(InputSource inputSource) throws SAXException, IOException {
        if (inputSource != null) {
            String encoding = inputSource.getEncoding();
            String systemId = inputSource.getSystemId();
            DocumentImpl documentImpl = new DocumentImpl(dom, (String) null, (String) null, (DocumentType) null, encoding);
            documentImpl.setDocumentURI(systemId);
            KXmlParser kXmlParser = new KXmlParser();
            try {
                kXmlParser.keepNamespaceAttributes();
                kXmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, this.namespaceAware);
                if (inputSource.getByteStream() != null) {
                    kXmlParser.setInput(inputSource.getByteStream(), encoding);
                } else if (inputSource.getCharacterStream() != null) {
                    kXmlParser.setInput(inputSource.getCharacterStream());
                } else if (systemId != null) {
                    URLConnection openConnection = new URL(systemId).openConnection();
                    openConnection.connect();
                    kXmlParser.setInput(openConnection.getInputStream(), encoding);
                } else {
                    throw new SAXParseException("InputSource needs a stream, reader or URI", (Locator) null);
                }
                if (kXmlParser.nextToken() != 1) {
                    parse(kXmlParser, documentImpl, documentImpl, 1);
                    kXmlParser.require(1, (String) null, (String) null);
                    IoUtils.closeQuietly((AutoCloseable) kXmlParser);
                    return documentImpl;
                }
                throw new SAXParseException("Unexpected end of document", (Locator) null);
            } catch (XmlPullParserException e) {
                Throwable detail = e.getDetail();
                if (detail instanceof IOException) {
                    throw ((IOException) detail);
                } else if (!(detail instanceof RuntimeException)) {
                    LocatorImpl locatorImpl = new LocatorImpl();
                    locatorImpl.setPublicId(inputSource.getPublicId());
                    locatorImpl.setSystemId(systemId);
                    locatorImpl.setLineNumber(e.getLineNumber());
                    locatorImpl.setColumnNumber(e.getColumnNumber());
                    SAXParseException sAXParseException = new SAXParseException(e.getMessage(), locatorImpl);
                    ErrorHandler errorHandler2 = this.errorHandler;
                    if (errorHandler2 != null) {
                        errorHandler2.error(sAXParseException);
                    }
                    throw sAXParseException;
                } else {
                    throw ((RuntimeException) detail);
                }
            } catch (Throwable th) {
                IoUtils.closeQuietly((AutoCloseable) kXmlParser);
                throw th;
            }
        } else {
            throw new IllegalArgumentException("source == null");
        }
    }

    /* JADX WARNING: type inference failed for: r20v0, types: [org.w3c.dom.Node] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parse(com.android.org.kxml2.p007io.KXmlParser r18, org.apache.harmony.xml.dom.DocumentImpl r19, org.w3c.dom.Node r20, int r21) throws org.xmlpull.p032v1.XmlPullParserException, java.p026io.IOException {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r20
            int r4 = r18.getEventType()
            r5 = r21
        L_0x000e:
            if (r4 == r5) goto L_0x013c
            r6 = 1
            if (r4 == r6) goto L_0x013c
            r6 = 8
            r7 = 0
            java.lang.String r8 = ""
            if (r4 != r6) goto L_0x003e
            java.lang.String r4 = r18.getText()
            r6 = 32
            int r6 = r4.indexOf((int) r6)
            r9 = -1
            if (r6 == r9) goto L_0x002c
            java.lang.String r7 = r4.substring(r7, r6)
            goto L_0x002d
        L_0x002c:
            r7 = r4
        L_0x002d:
            if (r6 == r9) goto L_0x0035
            int r6 = r6 + 1
            java.lang.String r8 = r4.substring(r6)
        L_0x0035:
            org.apache.harmony.xml.dom.ProcessingInstructionImpl r4 = r2.createProcessingInstruction((java.lang.String) r7, (java.lang.String) r8)
            r3.appendChild(r4)
            goto L_0x0136
        L_0x003e:
            r6 = 10
            if (r4 != r6) goto L_0x0058
            java.lang.String r4 = r18.getRootElementName()
            java.lang.String r6 = r18.getPublicId()
            java.lang.String r7 = r18.getSystemId()
            org.apache.harmony.xml.dom.DocumentTypeImpl r8 = new org.apache.harmony.xml.dom.DocumentTypeImpl
            r8.<init>(r2, r4, r6, r7)
            r2.appendChild(r8)
            goto L_0x0136
        L_0x0058:
            r6 = 9
            if (r4 != r6) goto L_0x006d
            boolean r4 = r0.ignoreComments
            if (r4 != 0) goto L_0x0136
            java.lang.String r4 = r18.getText()
            org.apache.harmony.xml.dom.CommentImpl r4 = r2.createComment((java.lang.String) r4)
            r3.appendChild(r4)
            goto L_0x0136
        L_0x006d:
            r6 = 7
            if (r4 != r6) goto L_0x007f
            boolean r6 = r0.ignoreElementContentWhitespace
            if (r6 != 0) goto L_0x0136
            if (r2 == r3) goto L_0x0136
            java.lang.String r6 = r18.getText()
            r0.appendText(r2, r3, r4, r6)
            goto L_0x0136
        L_0x007f:
            r6 = 4
            if (r4 == r6) goto L_0x012f
            r6 = 5
            if (r4 != r6) goto L_0x0087
            goto L_0x012f
        L_0x0087:
            r6 = 6
            if (r4 != r6) goto L_0x00a2
            java.lang.String r6 = r18.getName()
            java.lang.String r7 = r0.resolvePredefinedOrCharacterEntity(r6)
            if (r7 == 0) goto L_0x0099
            r0.appendText(r2, r3, r4, r7)
            goto L_0x0136
        L_0x0099:
            org.apache.harmony.xml.dom.EntityReferenceImpl r4 = r2.createEntityReference((java.lang.String) r6)
            r3.appendChild(r4)
            goto L_0x0136
        L_0x00a2:
            r6 = 2
            if (r4 != r6) goto L_0x0136
            boolean r4 = r0.namespaceAware
            r6 = 3
            if (r4 == 0) goto L_0x00ff
            java.lang.String r4 = r18.getNamespace()
            java.lang.String r9 = r18.getName()
            java.lang.String r10 = r18.getPrefix()
            boolean r11 = r8.equals(r4)
            r12 = 0
            if (r11 == 0) goto L_0x00be
            r4 = r12
        L_0x00be:
            org.apache.harmony.xml.dom.ElementImpl r11 = r2.createElementNS((java.lang.String) r4, (java.lang.String) r9)
            r11.setPrefix(r10)
            r3.appendChild(r11)
        L_0x00c8:
            int r10 = r18.getAttributeCount()
            if (r7 >= r10) goto L_0x00f5
            java.lang.String r10 = r1.getAttributeNamespace(r7)
            java.lang.String r13 = r1.getAttributePrefix(r7)
            java.lang.String r14 = r1.getAttributeName(r7)
            java.lang.String r15 = r1.getAttributeValue(r7)
            boolean r16 = r8.equals(r10)
            if (r16 == 0) goto L_0x00e5
            r10 = r12
        L_0x00e5:
            org.apache.harmony.xml.dom.AttrImpl r10 = r2.createAttributeNS((java.lang.String) r10, (java.lang.String) r14)
            r10.setPrefix(r13)
            r10.setValue(r15)
            r11.setAttributeNodeNS(r10)
            int r7 = r7 + 1
            goto L_0x00c8
        L_0x00f5:
            r18.nextToken()
            r0.parse(r1, r2, r11, r6)
            r1.require(r6, r4, r9)
            goto L_0x0136
        L_0x00ff:
            java.lang.String r4 = r18.getName()
            org.apache.harmony.xml.dom.ElementImpl r9 = r2.createElement((java.lang.String) r4)
            r3.appendChild(r9)
        L_0x010a:
            int r10 = r18.getAttributeCount()
            if (r7 >= r10) goto L_0x0125
            java.lang.String r10 = r1.getAttributeName(r7)
            java.lang.String r11 = r1.getAttributeValue(r7)
            org.apache.harmony.xml.dom.AttrImpl r10 = r2.createAttribute((java.lang.String) r10)
            r10.setValue(r11)
            r9.setAttributeNode(r10)
            int r7 = r7 + 1
            goto L_0x010a
        L_0x0125:
            r18.nextToken()
            r0.parse(r1, r2, r9, r6)
            r1.require(r6, r8, r4)
            goto L_0x0136
        L_0x012f:
            java.lang.String r6 = r18.getText()
            r0.appendText(r2, r3, r4, r6)
        L_0x0136:
            int r4 = r18.nextToken()
            goto L_0x000e
        L_0x013c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.harmony.xml.parsers.DocumentBuilderImpl.parse(com.android.org.kxml2.io.KXmlParser, org.apache.harmony.xml.dom.DocumentImpl, org.w3c.dom.Node, int):void");
    }

    private void appendText(DocumentImpl documentImpl, Node node, int i, String str) {
        Node node2;
        Node lastChild;
        if (!str.isEmpty()) {
            if ((this.coalescing || i != 5) && (lastChild = node.getLastChild()) != null && lastChild.getNodeType() == 3) {
                ((Text) lastChild).appendData(str);
                return;
            }
            if (i == 5) {
                node2 = new CDATASectionImpl(documentImpl, str);
            } else {
                node2 = new TextImpl(documentImpl, str);
            }
            node.appendChild(node2);
        }
    }

    public void setEntityResolver(EntityResolver entityResolver2) {
        this.entityResolver = entityResolver2;
    }

    public void setErrorHandler(ErrorHandler errorHandler2) {
        this.errorHandler = errorHandler2;
    }

    public void setIgnoreComments(boolean z) {
        this.ignoreComments = z;
    }

    public void setCoalescing(boolean z) {
        this.coalescing = z;
    }

    public void setIgnoreElementContentWhitespace(boolean z) {
        this.ignoreElementContentWhitespace = z;
    }

    public void setNamespaceAware(boolean z) {
        this.namespaceAware = z;
    }

    private String resolvePredefinedOrCharacterEntity(String str) {
        if (str.startsWith("#x")) {
            return resolveCharacterReference(str.substring(2), 16);
        }
        if (str.startsWith("#")) {
            return resolveCharacterReference(str.substring(1), 10);
        }
        if ("lt".equals(str)) {
            return "<";
        }
        if ("gt".equals(str)) {
            return ">";
        }
        if ("amp".equals(str)) {
            return "&";
        }
        if ("apos".equals(str)) {
            return "'";
        }
        if ("quot".equals(str)) {
            return "\"";
        }
        return null;
    }

    private String resolveCharacterReference(String str, int i) {
        try {
            int parseInt = Integer.parseInt(str, i);
            if (Character.isBmpCodePoint(parseInt)) {
                return String.valueOf((char) parseInt);
            }
            return new String(Character.toChars(parseInt));
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
