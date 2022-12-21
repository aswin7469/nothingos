package org.apache.harmony.xml.parsers;

import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXNotRecognizedException;

public class SAXParserFactoryImpl extends SAXParserFactory {
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    private Map<String, Boolean> features = new HashMap();

    public boolean getFeature(String str) throws SAXNotRecognizedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (str.startsWith("http://xml.org/sax/features/")) {
            return Boolean.TRUE.equals(this.features.get(str));
        } else {
            throw new SAXNotRecognizedException(str);
        }
    }

    public boolean isNamespaceAware() {
        try {
            return getFeature(NAMESPACES);
        } catch (SAXNotRecognizedException e) {
            throw new AssertionError((Object) e);
        }
    }

    public boolean isValidating() {
        try {
            return getFeature(VALIDATION);
        } catch (SAXNotRecognizedException e) {
            throw new AssertionError((Object) e);
        }
    }

    public SAXParser newSAXParser() throws ParserConfigurationException {
        if (!isValidating()) {
            try {
                return new SAXParserImpl(this.features);
            } catch (Exception e) {
                throw new ParserConfigurationException(e.toString());
            }
        } else {
            throw new ParserConfigurationException("No validating SAXParser implementation available");
        }
    }

    public void setFeature(String str, boolean z) throws SAXNotRecognizedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (!str.startsWith("http://xml.org/sax/features/")) {
            throw new SAXNotRecognizedException(str);
        } else if (z) {
            this.features.put(str, Boolean.TRUE);
        } else {
            this.features.put(str, Boolean.FALSE);
        }
    }

    public void setNamespaceAware(boolean z) {
        try {
            setFeature(NAMESPACES, z);
        } catch (SAXNotRecognizedException e) {
            throw new AssertionError((Object) e);
        }
    }

    public void setValidating(boolean z) {
        try {
            setFeature(VALIDATION, z);
        } catch (SAXNotRecognizedException e) {
            throw new AssertionError((Object) e);
        }
    }
}
