package org.xml.sax.helpers;

import java.nio.charset.StandardCharsets;
import java.p026io.BufferedReader;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class XMLReaderFactory {
    private static final String property = "org.xml.sax.driver";

    private XMLReaderFactory() {
    }

    public static XMLReader createXMLReader() throws SAXException {
        String str;
        InputStream inputStream;
        ClassLoader classLoader = NewInstance.getClassLoader();
        try {
            str = System.getProperty(property);
        } catch (RuntimeException unused) {
            str = null;
        }
        if (str == null) {
            if (classLoader == null) {
                try {
                    inputStream = ClassLoader.getSystemResourceAsStream("META-INF/services/org.xml.sax.driver");
                } catch (Exception unused2) {
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            } else {
                inputStream = classLoader.getResourceAsStream("META-INF/services/org.xml.sax.driver");
            }
            if (inputStream != null) {
                str = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readLine();
                inputStream.close();
            }
        }
        if (str != null) {
            return loadClass(classLoader, str);
        }
        try {
            return new ParserAdapter(ParserFactory.makeParser());
        } catch (Exception unused3) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
        }
    }

    public static XMLReader createXMLReader(String str) throws SAXException {
        return loadClass(NewInstance.getClassLoader(), str);
    }

    private static XMLReader loadClass(ClassLoader classLoader, String str) throws SAXException {
        try {
            return (XMLReader) NewInstance.newInstance(classLoader, str);
        } catch (ClassNotFoundException e) {
            throw new SAXException("SAX2 driver class " + str + " not found", e);
        } catch (IllegalAccessException e2) {
            throw new SAXException("SAX2 driver class " + str + " found but cannot be loaded", e2);
        } catch (InstantiationException e3) {
            throw new SAXException("SAX2 driver class " + str + " loaded but cannot be instantiated (no empty public constructor?)", e3);
        } catch (ClassCastException e4) {
            throw new SAXException("SAX2 driver class " + str + " does not implement XMLReader", e4);
        }
    }
}
