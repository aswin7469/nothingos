package org.xml.sax;

@Deprecated
public class HandlerBase implements EntityResolver, DTDHandler, DocumentHandler, ErrorHandler {
    public void characters(char[] cArr, int i, int i2) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String str) throws SAXException {
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
    }

    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
    }

    public void notationDecl(String str, String str2, String str3) {
    }

    public void processingInstruction(String str, String str2) throws SAXException {
    }

    public InputSource resolveEntity(String str, String str2) throws SAXException {
        return null;
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void startElement(String str, AttributeList attributeList) throws SAXException {
    }

    public void unparsedEntityDecl(String str, String str2, String str3, String str4) {
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }
}
