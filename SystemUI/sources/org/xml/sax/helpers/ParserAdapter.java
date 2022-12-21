package org.xml.sax.helpers;

import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.xml.XMLConstants;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class ParserAdapter implements XMLReader, DocumentHandler {
    private static final String FEATURES = "http://xml.org/sax/features/";
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    private static final String XMLNS_URIs = "http://xml.org/sax/features/xmlns-uris";
    private AttributeListAdapter attAdapter;
    /* access modifiers changed from: private */
    public AttributesImpl atts = null;
    ContentHandler contentHandler = null;
    DTDHandler dtdHandler = null;
    EntityResolver entityResolver = null;
    ErrorHandler errorHandler = null;
    Locator locator;
    private String[] nameParts = new String[3];
    private boolean namespaces = true;
    private NamespaceSupport nsSupport;
    private Parser parser = null;
    private boolean parsing = false;
    private boolean prefixes = false;
    private boolean uris = false;

    public ParserAdapter() throws SAXException {
        String property = System.getProperty("org.xml.sax.parser");
        try {
            setup(ParserFactory.makeParser());
        } catch (ClassNotFoundException e) {
            throw new SAXException("Cannot find SAX1 driver class " + property, e);
        } catch (IllegalAccessException e2) {
            throw new SAXException("SAX1 driver class " + property + " found but cannot be loaded", e2);
        } catch (InstantiationException e3) {
            throw new SAXException("SAX1 driver class " + property + " loaded but cannot be instantiated", e3);
        } catch (ClassCastException unused) {
            throw new SAXException("SAX1 driver class " + property + " does not implement org.xml.sax.Parser");
        } catch (NullPointerException unused2) {
            throw new SAXException("System property org.xml.sax.parser not specified");
        }
    }

    public ParserAdapter(Parser parser2) {
        setup(parser2);
    }

    private void setup(Parser parser2) {
        if (parser2 != null) {
            this.parser = parser2;
            this.atts = new AttributesImpl();
            this.nsSupport = new NamespaceSupport();
            this.attAdapter = new AttributeListAdapter();
            return;
        }
        throw new NullPointerException("Parser argument must not be null");
    }

    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str.equals(NAMESPACES)) {
            checkNotParsing("feature", str);
            this.namespaces = z;
            if (!z && !this.prefixes) {
                this.prefixes = true;
            }
        } else if (str.equals(NAMESPACE_PREFIXES)) {
            checkNotParsing("feature", str);
            this.prefixes = z;
            if (!z && !this.namespaces) {
                this.namespaces = true;
            }
        } else if (str.equals(XMLNS_URIs)) {
            checkNotParsing("feature", str);
            this.uris = z;
        } else {
            throw new SAXNotRecognizedException("Feature: " + str);
        }
    }

    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str.equals(NAMESPACES)) {
            return this.namespaces;
        }
        if (str.equals(NAMESPACE_PREFIXES)) {
            return this.prefixes;
        }
        if (str.equals(XMLNS_URIs)) {
            return this.uris;
        }
        throw new SAXNotRecognizedException("Feature: " + str);
    }

    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + str);
    }

    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + str);
    }

    public void setEntityResolver(EntityResolver entityResolver2) {
        this.entityResolver = entityResolver2;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.dtdHandler = dTDHandler;
    }

    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
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

    public void parse(String str) throws IOException, SAXException {
        parse(new InputSource(str));
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        if (!this.parsing) {
            setupParser();
            this.parsing = true;
            try {
                this.parser.parse(inputSource);
            } finally {
                this.parsing = false;
            }
        } else {
            throw new SAXException("Parser is already in use");
        }
    }

    public void setDocumentLocator(Locator locator2) {
        this.locator = locator2;
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.setDocumentLocator(locator2);
        }
    }

    public void startDocument() throws SAXException {
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.startDocument();
        }
    }

    public void endDocument() throws SAXException {
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.endDocument();
        }
    }

    public void startElement(String str, AttributeList attributeList) throws SAXException {
        int i;
        String str2;
        String str3;
        AttributeList attributeList2 = attributeList;
        if (this.namespaces) {
            this.nsSupport.pushContext();
            int length = attributeList.getLength();
            int i2 = 0;
            while (true) {
                i = -1;
                if (i2 >= length) {
                    break;
                }
                String name = attributeList2.getName(i2);
                if (name.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) {
                    int indexOf = name.indexOf(58);
                    if (indexOf == -1 && name.length() == 5) {
                        str3 = "";
                    } else if (indexOf == 5) {
                        str3 = name.substring(indexOf + 1);
                    }
                    String value = attributeList2.getValue(i2);
                    if (!this.nsSupport.declarePrefix(str3, value)) {
                        reportError("Illegal Namespace prefix: " + str3);
                    } else {
                        ContentHandler contentHandler2 = this.contentHandler;
                        if (contentHandler2 != null) {
                            contentHandler2.startPrefixMapping(str3, value);
                        }
                    }
                }
                i2++;
            }
            this.atts.clear();
            int i3 = 0;
            ArrayList arrayList = null;
            while (i3 < length) {
                String name2 = attributeList2.getName(i3);
                String type = attributeList2.getType(i3);
                String value2 = attributeList2.getValue(i3);
                if (name2.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) {
                    int indexOf2 = name2.indexOf(58);
                    if (indexOf2 == i && name2.length() == 5) {
                        str2 = "";
                    } else if (indexOf2 != 5) {
                        str2 = null;
                    } else {
                        str2 = name2.substring(6);
                    }
                    if (str2 != null) {
                        if (this.prefixes) {
                            if (this.uris) {
                                this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", str2, name2.intern(), type, value2);
                            } else {
                                this.atts.addAttribute("", "", name2.intern(), type, value2);
                            }
                        }
                        i3++;
                        i = -1;
                    }
                }
                try {
                    String[] processName = processName(name2, true, true);
                    this.atts.addAttribute(processName[0], processName[1], processName[2], type, value2);
                } catch (SAXException e) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add((SAXParseException) e);
                    String str4 = name2;
                    this.atts.addAttribute("", str4, str4, type, value2);
                }
                i3++;
                i = -1;
            }
            if (!(arrayList == null || this.errorHandler == null)) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    this.errorHandler.error((SAXParseException) it.next());
                }
            }
            if (this.contentHandler != null) {
                String[] processName2 = processName(str, false, false);
                this.contentHandler.startElement(processName2[0], processName2[1], processName2[2], this.atts);
            }
        } else if (this.contentHandler != null) {
            this.attAdapter.setAttributeList(attributeList2);
            this.contentHandler.startElement("", "", str.intern(), this.attAdapter);
        }
    }

    public void endElement(String str) throws SAXException {
        if (!this.namespaces) {
            ContentHandler contentHandler2 = this.contentHandler;
            if (contentHandler2 != null) {
                contentHandler2.endElement("", "", str.intern());
                return;
            }
            return;
        }
        String[] processName = processName(str, false, false);
        ContentHandler contentHandler3 = this.contentHandler;
        if (contentHandler3 != null) {
            contentHandler3.endElement(processName[0], processName[1], processName[2]);
            Enumeration declaredPrefixes = this.nsSupport.getDeclaredPrefixes();
            while (declaredPrefixes.hasMoreElements()) {
                this.contentHandler.endPrefixMapping((String) declaredPrefixes.nextElement());
            }
        }
        this.nsSupport.popContext();
    }

    public void characters(char[] cArr, int i, int i2) throws SAXException {
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.characters(cArr, i, i2);
        }
    }

    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.ignorableWhitespace(cArr, i, i2);
        }
    }

    public void processingInstruction(String str, String str2) throws SAXException {
        ContentHandler contentHandler2 = this.contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.processingInstruction(str, str2);
        }
    }

    private void setupParser() {
        if (this.prefixes || this.namespaces) {
            this.nsSupport.reset();
            if (this.uris) {
                this.nsSupport.setNamespaceDeclUris(true);
            }
            EntityResolver entityResolver2 = this.entityResolver;
            if (entityResolver2 != null) {
                this.parser.setEntityResolver(entityResolver2);
            }
            DTDHandler dTDHandler = this.dtdHandler;
            if (dTDHandler != null) {
                this.parser.setDTDHandler(dTDHandler);
            }
            ErrorHandler errorHandler2 = this.errorHandler;
            if (errorHandler2 != null) {
                this.parser.setErrorHandler(errorHandler2);
            }
            this.parser.setDocumentHandler(this);
            this.locator = null;
            return;
        }
        throw new IllegalStateException();
    }

    private String[] processName(String str, boolean z, boolean z2) throws SAXException {
        String[] processName = this.nsSupport.processName(str, this.nameParts, z);
        if (processName != null) {
            return processName;
        }
        if (!z2) {
            reportError("Undeclared prefix: " + str);
            String[] strArr = new String[3];
            strArr[1] = "";
            strArr[0] = "";
            strArr[2] = str.intern();
            return strArr;
        }
        throw makeException("Undeclared prefix: " + str);
    }

    /* access modifiers changed from: package-private */
    public void reportError(String str) throws SAXException {
        ErrorHandler errorHandler2 = this.errorHandler;
        if (errorHandler2 != null) {
            errorHandler2.error(makeException(str));
        }
    }

    private SAXParseException makeException(String str) {
        Locator locator2 = this.locator;
        if (locator2 != null) {
            return new SAXParseException(str, locator2);
        }
        return new SAXParseException(str, (String) null, (String) null, -1, -1);
    }

    private void checkNotParsing(String str, String str2) throws SAXNotSupportedException {
        if (this.parsing) {
            throw new SAXNotSupportedException("Cannot change " + str + ' ' + str2 + " while parsing");
        }
    }

    final class AttributeListAdapter implements Attributes {
        private AttributeList qAtts;

        public int getIndex(String str, String str2) {
            return -1;
        }

        public String getLocalName(int i) {
            return "";
        }

        public String getType(String str, String str2) {
            return null;
        }

        public String getURI(int i) {
            return "";
        }

        public String getValue(String str, String str2) {
            return null;
        }

        AttributeListAdapter() {
        }

        /* access modifiers changed from: package-private */
        public void setAttributeList(AttributeList attributeList) {
            this.qAtts = attributeList;
        }

        public int getLength() {
            return this.qAtts.getLength();
        }

        public String getQName(int i) {
            return this.qAtts.getName(i).intern();
        }

        public String getType(int i) {
            return this.qAtts.getType(i).intern();
        }

        public String getValue(int i) {
            return this.qAtts.getValue(i);
        }

        public int getIndex(String str) {
            int length = ParserAdapter.this.atts.getLength();
            for (int i = 0; i < length; i++) {
                if (this.qAtts.getName(i).equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public String getType(String str) {
            return this.qAtts.getType(str).intern();
        }

        public String getValue(String str) {
            return this.qAtts.getValue(str);
        }
    }
}
