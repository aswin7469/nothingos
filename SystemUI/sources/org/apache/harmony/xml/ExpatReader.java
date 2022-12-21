package org.apache.harmony.xml;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.Reader;
import libcore.p030io.IoUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public class ExpatReader implements XMLReader {
    private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    ContentHandler contentHandler;
    DTDHandler dtdHandler;
    EntityResolver entityResolver;
    ErrorHandler errorHandler;
    LexicalHandler lexicalHandler;
    private boolean processNamespacePrefixes = false;
    private boolean processNamespaces = true;

    private static class Feature {
        private static final String BASE_URI = "http://xml.org/sax/features/";
        private static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
        private static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
        private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
        private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
        private static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
        private static final String VALIDATION = "http://xml.org/sax/features/validation";

        private Feature() {
        }
    }

    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (str.equals("http://xml.org/sax/features/validation") || str.equals("http://xml.org/sax/features/external-general-entities") || str.equals("http://xml.org/sax/features/external-parameter-entities")) {
            return false;
        } else {
            if (str.equals("http://xml.org/sax/features/namespaces")) {
                return this.processNamespaces;
            }
            if (str.equals("http://xml.org/sax/features/namespace-prefixes")) {
                return this.processNamespacePrefixes;
            }
            if (str.equals("http://xml.org/sax/features/string-interning")) {
                return true;
            }
            throw new SAXNotRecognizedException(str);
        }
    }

    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (str.equals("http://xml.org/sax/features/validation") || str.equals("http://xml.org/sax/features/external-general-entities") || str.equals("http://xml.org/sax/features/external-parameter-entities")) {
            if (z) {
                throw new SAXNotSupportedException("Cannot enable " + str);
            }
        } else if (str.equals("http://xml.org/sax/features/namespaces")) {
            this.processNamespaces = z;
        } else if (str.equals("http://xml.org/sax/features/namespace-prefixes")) {
            this.processNamespacePrefixes = z;
        } else if (!str.equals("http://xml.org/sax/features/string-interning")) {
            throw new SAXNotRecognizedException(str);
        } else if (!z) {
            throw new SAXNotSupportedException("Cannot disable " + str);
        }
    }

    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (str.equals(LEXICAL_HANDLER_PROPERTY)) {
            return this.lexicalHandler;
        } else {
            throw new SAXNotRecognizedException(str);
        }
    }

    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (!str.equals(LEXICAL_HANDLER_PROPERTY)) {
            throw new SAXNotRecognizedException(str);
        } else if ((obj instanceof LexicalHandler) || obj == null) {
            this.lexicalHandler = (LexicalHandler) obj;
        } else {
            throw new SAXNotSupportedException("value doesn't implement org.xml.sax.ext.LexicalHandler");
        }
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

    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler2) {
        this.lexicalHandler = lexicalHandler2;
    }

    public boolean isNamespaceProcessingEnabled() {
        return this.processNamespaces;
    }

    public void setNamespaceProcessingEnabled(boolean z) {
        this.processNamespaces = z;
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        if (!this.processNamespacePrefixes || !this.processNamespaces) {
            Reader characterStream = inputSource.getCharacterStream();
            if (characterStream != null) {
                try {
                    parse(characterStream, inputSource.getPublicId(), inputSource.getSystemId());
                } finally {
                    IoUtils.closeQuietly((AutoCloseable) characterStream);
                }
            } else {
                InputStream byteStream = inputSource.getByteStream();
                String encoding = inputSource.getEncoding();
                if (byteStream != null) {
                    try {
                        parse(byteStream, encoding, inputSource.getPublicId(), inputSource.getSystemId());
                    } finally {
                        IoUtils.closeQuietly((AutoCloseable) byteStream);
                    }
                } else {
                    String systemId = inputSource.getSystemId();
                    if (systemId != null) {
                        InputStream openUrl = ExpatParser.openUrl(systemId);
                        try {
                            parse(openUrl, encoding, inputSource.getPublicId(), systemId);
                        } finally {
                            IoUtils.closeQuietly((AutoCloseable) openUrl);
                        }
                    } else {
                        throw new SAXException("No input specified.");
                    }
                }
            }
        } else {
            throw new SAXNotSupportedException("The 'namespace-prefix' feature is not supported while the 'namespaces' feature is enabled.");
        }
    }

    private void parse(Reader reader, String str, String str2) throws IOException, SAXException {
        new ExpatParser("UTF-16", this, this.processNamespaces, str, str2).parseDocument(reader);
    }

    private void parse(InputStream inputStream, String str, String str2, String str3) throws IOException, SAXException {
        new ExpatParser(str, this, this.processNamespaces, str2, str3).parseDocument(inputStream);
    }

    public void parse(String str) throws IOException, SAXException {
        parse(new InputSource(str));
    }
}
