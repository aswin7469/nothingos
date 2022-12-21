package org.apache.harmony.xml.parsers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";

    public Object getAttribute(String str) throws IllegalArgumentException {
        throw new IllegalArgumentException(str);
    }

    public boolean getFeature(String str) throws ParserConfigurationException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (NAMESPACES.equals(str)) {
            return isNamespaceAware();
        } else {
            if (VALIDATION.equals(str)) {
                return isValidating();
            }
            throw new ParserConfigurationException(str);
        }
    }

    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        if (!isValidating()) {
            DocumentBuilderImpl documentBuilderImpl = new DocumentBuilderImpl();
            documentBuilderImpl.setCoalescing(isCoalescing());
            documentBuilderImpl.setIgnoreComments(isIgnoringComments());
            documentBuilderImpl.setIgnoreElementContentWhitespace(isIgnoringElementContentWhitespace());
            documentBuilderImpl.setNamespaceAware(isNamespaceAware());
            return documentBuilderImpl;
        }
        throw new ParserConfigurationException("No validating DocumentBuilder implementation available");
    }

    public void setAttribute(String str, Object obj) throws IllegalArgumentException {
        throw new IllegalArgumentException(str);
    }

    public void setFeature(String str, boolean z) throws ParserConfigurationException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (NAMESPACES.equals(str)) {
            setNamespaceAware(z);
        } else if (VALIDATION.equals(str)) {
            setValidating(z);
        } else {
            throw new ParserConfigurationException(str);
        }
    }
}
