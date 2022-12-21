package org.xml.sax.ext;

import java.p026io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DefaultHandler2 extends DefaultHandler implements LexicalHandler, DeclHandler, EntityResolver2 {
    public void attributeDecl(String str, String str2, String str3, String str4, String str5) throws SAXException {
    }

    public void comment(char[] cArr, int i, int i2) throws SAXException {
    }

    public void elementDecl(String str, String str2) throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void endEntity(String str) throws SAXException {
    }

    public void externalEntityDecl(String str, String str2, String str3) throws SAXException {
    }

    public InputSource getExternalSubset(String str, String str2) throws SAXException, IOException {
        return null;
    }

    public void internalEntityDecl(String str, String str2) throws SAXException {
    }

    public InputSource resolveEntity(String str, String str2, String str3, String str4) throws SAXException, IOException {
        return null;
    }

    public void startCDATA() throws SAXException {
    }

    public void startDTD(String str, String str2, String str3) throws SAXException {
    }

    public void startEntity(String str) throws SAXException {
    }

    public InputSource resolveEntity(String str, String str2) throws SAXException, IOException {
        return resolveEntity((String) null, str, (String) null, str2);
    }
}
