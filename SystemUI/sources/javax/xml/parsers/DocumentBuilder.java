package javax.xml.parsers;

import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class DocumentBuilder {
    private static final boolean DEBUG = false;

    public abstract DOMImplementation getDOMImplementation();

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    public abstract Document newDocument();

    public abstract Document parse(InputSource inputSource) throws SAXException, IOException;

    public abstract void setEntityResolver(EntityResolver entityResolver);

    public abstract void setErrorHandler(ErrorHandler errorHandler);

    protected DocumentBuilder() {
    }

    public void reset() {
        throw new UnsupportedOperationException("This DocumentBuilder, \"" + getClass().getName() + "\", does not support the reset functionality.  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }

    public Document parse(InputStream inputStream) throws SAXException, IOException {
        if (inputStream != null) {
            return parse(new InputSource(inputStream));
        }
        throw new IllegalArgumentException("InputStream cannot be null");
    }

    public Document parse(InputStream inputStream, String str) throws SAXException, IOException {
        if (inputStream != null) {
            InputSource inputSource = new InputSource(inputStream);
            inputSource.setSystemId(str);
            return parse(inputSource);
        }
        throw new IllegalArgumentException("InputStream cannot be null");
    }

    public Document parse(String str) throws SAXException, IOException {
        if (str != null) {
            return parse(new InputSource(str));
        }
        throw new IllegalArgumentException("URI cannot be null");
    }

    public Document parse(File file) throws SAXException, IOException {
        if (file != null) {
            return parse(new InputSource(FilePathToURI.filepath2URI(file.getAbsolutePath())));
        }
        throw new IllegalArgumentException("File cannot be null");
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }

    public boolean isXIncludeAware() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }
}
