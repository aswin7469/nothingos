package javax.xml.parsers;

import javax.xml.validation.Schema;
import org.apache.harmony.xml.parsers.DocumentBuilderFactoryImpl;

public abstract class DocumentBuilderFactory {
    private boolean coalescing = false;
    private boolean expandEntityRef = true;
    private boolean ignoreComments = false;
    private boolean namespaceAware = false;
    private boolean validating = false;
    private boolean whitespace = false;

    public abstract Object getAttribute(String str) throws IllegalArgumentException;

    public abstract boolean getFeature(String str) throws ParserConfigurationException;

    public abstract DocumentBuilder newDocumentBuilder() throws ParserConfigurationException;

    public abstract void setAttribute(String str, Object obj) throws IllegalArgumentException;

    public abstract void setFeature(String str, boolean z) throws ParserConfigurationException;

    protected DocumentBuilderFactory() {
    }

    public static DocumentBuilderFactory newInstance() {
        return new DocumentBuilderFactoryImpl();
    }

    public static DocumentBuilderFactory newInstance(String str, ClassLoader classLoader) {
        Class<?> cls;
        if (str != null) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            if (classLoader != null) {
                try {
                    cls = classLoader.loadClass(str);
                } catch (ClassNotFoundException e) {
                    throw new FactoryConfigurationError((Exception) e);
                } catch (InstantiationException e2) {
                    throw new FactoryConfigurationError((Exception) e2);
                } catch (IllegalAccessException e3) {
                    throw new FactoryConfigurationError((Exception) e3);
                }
            } else {
                cls = Class.forName(str);
            }
            return (DocumentBuilderFactory) cls.newInstance();
        }
        throw new FactoryConfigurationError("factoryClassName == null");
    }

    public void setNamespaceAware(boolean z) {
        this.namespaceAware = z;
    }

    public void setValidating(boolean z) {
        this.validating = z;
    }

    public void setIgnoringElementContentWhitespace(boolean z) {
        this.whitespace = z;
    }

    public void setExpandEntityReferences(boolean z) {
        this.expandEntityRef = z;
    }

    public void setIgnoringComments(boolean z) {
        this.ignoreComments = z;
    }

    public void setCoalescing(boolean z) {
        this.coalescing = z;
    }

    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }

    public boolean isValidating() {
        return this.validating;
    }

    public boolean isIgnoringElementContentWhitespace() {
        return this.whitespace;
    }

    public boolean isExpandEntityReferences() {
        return this.expandEntityRef;
    }

    public boolean isIgnoringComments() {
        return this.ignoreComments;
    }

    public boolean isCoalescing() {
        return this.coalescing;
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }

    public void setSchema(Schema schema) {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }

    public void setXIncludeAware(boolean z) {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }

    public boolean isXIncludeAware() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
    }
}
