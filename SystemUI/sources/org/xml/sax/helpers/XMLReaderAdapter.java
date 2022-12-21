package org.xml.sax.helpers;

import java.p026io.IOException;
import java.util.Locale;
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
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XMLReaderAdapter implements Parser, ContentHandler {
    DocumentHandler documentHandler;
    AttributesAdapter qAtts;
    XMLReader xmlReader;

    public void endPrefixMapping(String str) {
    }

    public void skippedEntity(String str) throws SAXException {
    }

    public void startPrefixMapping(String str, String str2) {
    }

    public XMLReaderAdapter() throws SAXException {
        setup(XMLReaderFactory.createXMLReader());
    }

    public XMLReaderAdapter(XMLReader xMLReader) {
        setup(xMLReader);
    }

    private void setup(XMLReader xMLReader) {
        if (xMLReader != null) {
            this.xmlReader = xMLReader;
            this.qAtts = new AttributesAdapter();
            return;
        }
        throw new NullPointerException("XMLReader must not be null");
    }

    public void setLocale(Locale locale) throws SAXException {
        throw new SAXNotSupportedException("setLocale not supported");
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.xmlReader.setEntityResolver(entityResolver);
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.xmlReader.setDTDHandler(dTDHandler);
    }

    public void setDocumentHandler(DocumentHandler documentHandler2) {
        this.documentHandler = documentHandler2;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.xmlReader.setErrorHandler(errorHandler);
    }

    public void parse(String str) throws IOException, SAXException {
        parse(new InputSource(str));
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        setupXMLReader();
        this.xmlReader.parse(inputSource);
    }

    private void setupXMLReader() throws SAXException {
        this.xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        try {
            this.xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
        } catch (SAXException unused) {
        }
        this.xmlReader.setContentHandler(this);
    }

    public void setDocumentLocator(Locator locator) {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.setDocumentLocator(locator);
        }
    }

    public void startDocument() throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.startDocument();
        }
    }

    public void endDocument() throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.endDocument();
        }
    }

    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if (this.documentHandler != null) {
            this.qAtts.setAttributes(attributes);
            this.documentHandler.startElement(str3, this.qAtts);
        }
    }

    public void endElement(String str, String str2, String str3) throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.endElement(str3);
        }
    }

    public void characters(char[] cArr, int i, int i2) throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.characters(cArr, i, i2);
        }
    }

    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.ignorableWhitespace(cArr, i, i2);
        }
    }

    public void processingInstruction(String str, String str2) throws SAXException {
        DocumentHandler documentHandler2 = this.documentHandler;
        if (documentHandler2 != null) {
            documentHandler2.processingInstruction(str, str2);
        }
    }

    static final class AttributesAdapter implements AttributeList {
        private Attributes attributes;

        AttributesAdapter() {
        }

        /* access modifiers changed from: package-private */
        public void setAttributes(Attributes attributes2) {
            this.attributes = attributes2;
        }

        public int getLength() {
            return this.attributes.getLength();
        }

        public String getName(int i) {
            return this.attributes.getQName(i);
        }

        public String getType(int i) {
            return this.attributes.getType(i);
        }

        public String getValue(int i) {
            return this.attributes.getValue(i);
        }

        public String getType(String str) {
            return this.attributes.getType(str);
        }

        public String getValue(String str) {
            return this.attributes.getValue(str);
        }
    }
}
