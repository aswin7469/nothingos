package org.apache.harmony.xml.parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import org.apache.harmony.xml.ExpatReader;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;

final class SAXParserImpl extends SAXParser {
    private Map<String, Boolean> initialFeatures;
    private Parser parser;
    private XMLReader reader;

    public boolean isValidating() {
        return false;
    }

    SAXParserImpl(Map<String, Boolean> map) throws SAXNotRecognizedException, SAXNotSupportedException {
        Map<String, Boolean> map2;
        if (map.isEmpty()) {
            map2 = Collections.emptyMap();
        } else {
            map2 = new HashMap<>(map);
        }
        this.initialFeatures = map2;
        resetInternal();
    }

    private void resetInternal() throws SAXNotSupportedException, SAXNotRecognizedException {
        this.reader = new ExpatReader();
        for (Map.Entry next : this.initialFeatures.entrySet()) {
            this.reader.setFeature((String) next.getKey(), ((Boolean) next.getValue()).booleanValue());
        }
    }

    public void reset() {
        try {
            resetInternal();
        } catch (SAXNotRecognizedException unused) {
            throw new AssertionError();
        } catch (SAXNotSupportedException unused2) {
            throw new AssertionError();
        }
    }

    public Parser getParser() {
        if (this.parser == null) {
            this.parser = new XMLReaderAdapter(this.reader);
        }
        return this.parser;
    }

    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.reader.getProperty(str);
    }

    public XMLReader getXMLReader() {
        return this.reader;
    }

    public boolean isNamespaceAware() {
        try {
            return this.reader.getFeature("http://xml.org/sax/features/namespaces");
        } catch (SAXException unused) {
            return false;
        }
    }

    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        this.reader.setProperty(str, obj);
    }
}
