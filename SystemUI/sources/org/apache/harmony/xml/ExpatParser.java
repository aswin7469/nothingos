package org.apache.harmony.xml;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.Reader;
import libcore.p030io.IoUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

class ExpatParser {
    private static final int BUFFER_SIZE = 8096;
    static final String CHARACTER_ENCODING = "UTF-16";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String OUTSIDE_START_ELEMENT = "Attributes can only be used within the scope of startElement().";
    private static final int TIMEOUT = 20000;
    /* access modifiers changed from: private */
    public int attributeCount;
    /* access modifiers changed from: private */
    public long attributePointer;
    private final ExpatAttributes attributes;
    private final String encoding;
    /* access modifiers changed from: private */
    public boolean inStartElement;
    private final Locator locator;
    /* access modifiers changed from: private */
    public long pointer;
    /* access modifiers changed from: private */
    public final String publicId;
    /* access modifiers changed from: private */
    public final String systemId;
    private final ExpatReader xmlReader;

    private native void appendBytes(long j, byte[] bArr, int i, int i2) throws SAXException, ExpatException;

    private native void appendChars(long j, char[] cArr, int i, int i2) throws SAXException, ExpatException;

    private native void appendString(long j, String str, boolean z) throws SAXException, ExpatException;

    private static native long cloneAttributes(long j, int i);

    private static native int column(long j);

    private static native long createEntityParser(long j, String str);

    private native long initialize(String str, boolean z);

    private static native int line(long j);

    private native void release(long j);

    private static native void releaseParser(long j);

    private static native void staticInitialize(String str);

    ExpatParser(String encoding2, ExpatReader xmlReader2, boolean processNamespaces, String publicId2, String systemId2) {
        this.inStartElement = false;
        this.attributeCount = -1;
        this.attributePointer = 0;
        this.locator = new ExpatLocator();
        this.attributes = new CurrentAttributes();
        this.publicId = publicId2;
        this.systemId = systemId2;
        this.xmlReader = xmlReader2;
        String str = encoding2 == null ? DEFAULT_ENCODING : encoding2;
        this.encoding = str;
        this.pointer = initialize(str, processNamespaces);
    }

    private ExpatParser(String encoding2, ExpatReader xmlReader2, long pointer2, String publicId2, String systemId2) {
        this.inStartElement = false;
        this.attributeCount = -1;
        this.attributePointer = 0;
        this.locator = new ExpatLocator();
        this.attributes = new CurrentAttributes();
        this.encoding = encoding2;
        this.xmlReader = xmlReader2;
        this.pointer = pointer2;
        this.systemId = systemId2;
        this.publicId = publicId2;
    }

    /* access modifiers changed from: package-private */
    public void startElement(String uri, String localName, String qName, long attributePointer2, int attributeCount2) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            try {
                this.inStartElement = true;
                this.attributePointer = attributePointer2;
                this.attributeCount = attributeCount2;
                contentHandler.startElement(uri, localName, qName, this.attributes);
            } finally {
                this.inStartElement = false;
                this.attributeCount = -1;
                this.attributePointer = 0;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.endElement(uri, localName, qName);
        }
    }

    /* access modifiers changed from: package-private */
    public void text(char[] text, int length) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.characters(text, 0, length);
        }
    }

    /* access modifiers changed from: package-private */
    public void comment(char[] text, int length) throws SAXException {
        LexicalHandler lexicalHandler = this.xmlReader.lexicalHandler;
        if (lexicalHandler != null) {
            lexicalHandler.comment(text, 0, length);
        }
    }

    /* access modifiers changed from: package-private */
    public void startCdata() throws SAXException {
        LexicalHandler lexicalHandler = this.xmlReader.lexicalHandler;
        if (lexicalHandler != null) {
            lexicalHandler.startCDATA();
        }
    }

    /* access modifiers changed from: package-private */
    public void endCdata() throws SAXException {
        LexicalHandler lexicalHandler = this.xmlReader.lexicalHandler;
        if (lexicalHandler != null) {
            lexicalHandler.endCDATA();
        }
    }

    /* access modifiers changed from: package-private */
    public void startNamespace(String prefix, String uri) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.startPrefixMapping(prefix, uri);
        }
    }

    /* access modifiers changed from: package-private */
    public void endNamespace(String prefix) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.endPrefixMapping(prefix);
        }
    }

    /* access modifiers changed from: package-private */
    public void startDtd(String name, String publicId2, String systemId2) throws SAXException {
        LexicalHandler lexicalHandler = this.xmlReader.lexicalHandler;
        if (lexicalHandler != null) {
            lexicalHandler.startDTD(name, publicId2, systemId2);
        }
    }

    /* access modifiers changed from: package-private */
    public void endDtd() throws SAXException {
        LexicalHandler lexicalHandler = this.xmlReader.lexicalHandler;
        if (lexicalHandler != null) {
            lexicalHandler.endDTD();
        }
    }

    /* access modifiers changed from: package-private */
    public void processingInstruction(String target, String data) throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.processingInstruction(target, data);
        }
    }

    /* access modifiers changed from: package-private */
    public void notationDecl(String name, String publicId2, String systemId2) throws SAXException {
        DTDHandler dTDHandler = this.xmlReader.dtdHandler;
        if (dTDHandler != null) {
            dTDHandler.notationDecl(name, publicId2, systemId2);
        }
    }

    /* access modifiers changed from: package-private */
    public void unparsedEntityDecl(String name, String publicId2, String systemId2, String notationName) throws SAXException {
        DTDHandler dTDHandler = this.xmlReader.dtdHandler;
        if (dTDHandler != null) {
            dTDHandler.unparsedEntityDecl(name, publicId2, systemId2, notationName);
        }
    }

    /* access modifiers changed from: package-private */
    public void handleExternalEntity(String context, String publicId2, String systemId2) throws SAXException, IOException {
        String str = systemId2;
        EntityResolver entityResolver = this.xmlReader.entityResolver;
        if (entityResolver != null) {
            if (this.systemId != null) {
                try {
                    URI uri = new URI(str);
                    if (!uri.isAbsolute() && !uri.isOpaque()) {
                        str = new URI(this.systemId).resolve(uri).toString();
                    }
                } catch (Exception e) {
                    System.logI("Could not resolve '" + str + "' relative to '" + this.systemId + "' at " + this.locator, e);
                }
            }
            InputSource resolveEntity = entityResolver.resolveEntity(publicId2, str);
            if (resolveEntity != null) {
                String pickEncoding = pickEncoding(resolveEntity);
                long createEntityParser = createEntityParser(this.pointer, context);
                try {
                    parseExternalEntity(new EntityParser(pickEncoding, this.xmlReader, createEntityParser, resolveEntity.getPublicId(), resolveEntity.getSystemId()), resolveEntity);
                } finally {
                    releaseParser(createEntityParser);
                }
            }
        }
    }

    private String pickEncoding(InputSource inputSource) {
        if (inputSource.getCharacterStream() != null) {
            return CHARACTER_ENCODING;
        }
        String encoding2 = inputSource.getEncoding();
        return encoding2 == null ? DEFAULT_ENCODING : encoding2;
    }

    private void parseExternalEntity(ExpatParser entityParser, InputSource inputSource) throws IOException, SAXException {
        Reader characterStream = inputSource.getCharacterStream();
        if (characterStream != null) {
            try {
                entityParser.append("<externalEntity>");
                entityParser.parseFragment(characterStream);
                entityParser.append("</externalEntity>");
            } finally {
                IoUtils.closeQuietly((AutoCloseable) characterStream);
            }
        } else {
            InputStream byteStream = inputSource.getByteStream();
            if (byteStream != null) {
                try {
                    entityParser.append("<externalEntity>".getBytes(entityParser.encoding));
                    entityParser.parseFragment(byteStream);
                    entityParser.append("</externalEntity>".getBytes(entityParser.encoding));
                } finally {
                    IoUtils.closeQuietly((AutoCloseable) byteStream);
                }
            } else {
                String systemId2 = inputSource.getSystemId();
                if (systemId2 != null) {
                    InputStream openUrl = openUrl(systemId2);
                    try {
                        entityParser.append("<externalEntity>".getBytes(entityParser.encoding));
                        entityParser.parseFragment(openUrl);
                        entityParser.append("</externalEntity>".getBytes(entityParser.encoding));
                    } finally {
                        IoUtils.closeQuietly((AutoCloseable) openUrl);
                    }
                } else {
                    throw new ParseException("No input specified.", this.locator);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void append(String xml) throws SAXException {
        try {
            appendString(this.pointer, xml, false);
        } catch (ExpatException e) {
            throw new ParseException(e.getMessage(), this.locator);
        }
    }

    /* access modifiers changed from: package-private */
    public void append(char[] xml, int offset, int length) throws SAXException {
        try {
            appendChars(this.pointer, xml, offset, length);
        } catch (ExpatException e) {
            throw new ParseException(e.getMessage(), this.locator);
        }
    }

    /* access modifiers changed from: package-private */
    public void append(byte[] xml) throws SAXException {
        append(xml, 0, xml.length);
    }

    /* access modifiers changed from: package-private */
    public void append(byte[] xml, int offset, int length) throws SAXException {
        try {
            appendBytes(this.pointer, xml, offset, length);
        } catch (ExpatException e) {
            throw new ParseException(e.getMessage(), this.locator);
        }
    }

    /* access modifiers changed from: package-private */
    public void parseDocument(InputStream in) throws IOException, SAXException {
        startDocument();
        parseFragment(in);
        finish();
        endDocument();
    }

    /* access modifiers changed from: package-private */
    public void parseDocument(Reader in) throws IOException, SAXException {
        startDocument();
        parseFragment(in);
        finish();
        endDocument();
    }

    private void parseFragment(Reader in) throws IOException, SAXException {
        char[] cArr = new char[4048];
        while (true) {
            int read = in.read(cArr);
            int i = read;
            if (read != -1) {
                try {
                    appendChars(this.pointer, cArr, 0, i);
                } catch (ExpatException e) {
                    throw new ParseException(e.getMessage(), this.locator);
                }
            } else {
                return;
            }
        }
    }

    private void parseFragment(InputStream in) throws IOException, SAXException {
        byte[] bArr = new byte[BUFFER_SIZE];
        while (true) {
            int read = in.read(bArr);
            int i = read;
            if (read != -1) {
                try {
                    appendBytes(this.pointer, bArr, 0, i);
                } catch (ExpatException e) {
                    throw new ParseException(e.getMessage(), this.locator);
                }
            } else {
                return;
            }
        }
    }

    private void startDocument() throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.setDocumentLocator(this.locator);
            contentHandler.startDocument();
        }
    }

    private void endDocument() throws SAXException {
        ContentHandler contentHandler = this.xmlReader.contentHandler;
        if (contentHandler != null) {
            contentHandler.endDocument();
        }
    }

    /* access modifiers changed from: package-private */
    public void finish() throws SAXException {
        try {
            appendString(this.pointer, "", true);
        } catch (ExpatException e) {
            throw new ParseException(e.getMessage(), this.locator);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void finalize() throws Throwable {
        try {
            long j = this.pointer;
            if (j != 0) {
                release(j);
                this.pointer = 0;
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
    }

    static {
        staticInitialize("");
    }

    /* access modifiers changed from: private */
    public int line() {
        return line(this.pointer);
    }

    /* access modifiers changed from: private */
    public int column() {
        return column(this.pointer);
    }

    /* access modifiers changed from: package-private */
    public Attributes cloneAttributes() {
        if (this.inStartElement) {
            int i = this.attributeCount;
            if (i == 0) {
                return ClonedAttributes.EMPTY;
            }
            return new ClonedAttributes(this.pointer, cloneAttributes(this.attributePointer, i), this.attributeCount);
        }
        throw new IllegalStateException(OUTSIDE_START_ELEMENT);
    }

    private static class ClonedAttributes extends ExpatAttributes {
        /* access modifiers changed from: private */
        public static final Attributes EMPTY = new ClonedAttributes(0, 0, 0);
        private final int length;
        private final long parserPointer;
        private long pointer;

        private ClonedAttributes(long j, long j2, int i) {
            this.parserPointer = j;
            this.pointer = j2;
            this.length = i;
        }

        public long getParserPointer() {
            return this.parserPointer;
        }

        public long getPointer() {
            return this.pointer;
        }

        public int getLength() {
            return this.length;
        }

        /* access modifiers changed from: protected */
        public synchronized void finalize() throws Throwable {
            try {
                long j = this.pointer;
                if (j != 0) {
                    freeAttributes(j);
                    this.pointer = 0;
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
                throw th;
            }
        }
    }

    private class ExpatLocator implements Locator {
        private ExpatLocator() {
        }

        public String getPublicId() {
            return ExpatParser.this.publicId;
        }

        public String getSystemId() {
            return ExpatParser.this.systemId;
        }

        public int getLineNumber() {
            return ExpatParser.this.line();
        }

        public int getColumnNumber() {
            return ExpatParser.this.column();
        }

        public String toString() {
            return "Locator[publicId: " + ExpatParser.this.publicId + ", systemId: " + ExpatParser.this.systemId + ", line: " + getLineNumber() + ", column: " + getColumnNumber() + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    private class CurrentAttributes extends ExpatAttributes {
        private CurrentAttributes() {
        }

        public long getParserPointer() {
            return ExpatParser.this.pointer;
        }

        public long getPointer() {
            if (ExpatParser.this.inStartElement) {
                return ExpatParser.this.attributePointer;
            }
            throw new IllegalStateException(ExpatParser.OUTSIDE_START_ELEMENT);
        }

        public int getLength() {
            if (ExpatParser.this.inStartElement) {
                return ExpatParser.this.attributeCount;
            }
            throw new IllegalStateException(ExpatParser.OUTSIDE_START_ELEMENT);
        }
    }

    private static class ParseException extends SAXParseException {
        private ParseException(String str, Locator locator) {
            super(makeMessage(str, locator), locator);
        }

        private static String makeMessage(String str, Locator locator) {
            return makeMessage(str, locator.getLineNumber(), locator.getColumnNumber());
        }

        private static String makeMessage(String str, int i, int i2) {
            return "At line " + i + ", column " + i2 + ": " + str;
        }
    }

    static InputStream openUrl(String url) throws IOException {
        try {
            URLConnection openConnection = new URL(url).openConnection();
            openConnection.setConnectTimeout(TIMEOUT);
            openConnection.setReadTimeout(TIMEOUT);
            openConnection.setDoInput(true);
            openConnection.setDoOutput(false);
            return openConnection.getInputStream();
        } catch (Exception e) {
            IOException iOException = new IOException("Couldn't open " + url);
            iOException.initCause(e);
            throw iOException;
        }
    }

    private static class EntityParser extends ExpatParser {
        private int depth;

        private EntityParser(String str, ExpatReader expatReader, long j, String str2, String str3) {
            super(str, expatReader, j, str2, str3);
            this.depth = 0;
        }

        /* access modifiers changed from: package-private */
        public void startElement(String str, String str2, String str3, long j, int i) throws SAXException {
            int i2 = this.depth;
            this.depth = i2 + 1;
            if (i2 > 0) {
                ExpatParser.super.startElement(str, str2, str3, j, i);
            }
        }

        /* access modifiers changed from: package-private */
        public void endElement(String str, String str2, String str3) throws SAXException {
            int i = this.depth - 1;
            this.depth = i;
            if (i > 0) {
                ExpatParser.super.endElement(str, str2, str3);
            }
        }

        /* access modifiers changed from: protected */
        public synchronized void finalize() throws Throwable {
        }
    }
}
