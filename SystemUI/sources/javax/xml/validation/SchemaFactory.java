package javax.xml.validation;

import java.net.URL;
import java.p026io.File;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.p031ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class SchemaFactory {
    public abstract ErrorHandler getErrorHandler();

    public abstract LSResourceResolver getResourceResolver();

    public abstract boolean isSchemaLanguageSupported(String str);

    public abstract Schema newSchema() throws SAXException;

    public abstract Schema newSchema(Source[] sourceArr) throws SAXException;

    public abstract void setErrorHandler(ErrorHandler errorHandler);

    public abstract void setResourceResolver(LSResourceResolver lSResourceResolver);

    protected SchemaFactory() {
    }

    public static SchemaFactory newInstance(String str) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = SchemaFactory.class.getClassLoader();
        }
        SchemaFactory newFactory = new SchemaFactoryFinder(contextClassLoader).newFactory(str);
        if (newFactory != null) {
            return newFactory;
        }
        throw new IllegalArgumentException(str);
    }

    public static SchemaFactory newInstance(String str, String str2, ClassLoader classLoader) {
        Class<?> cls;
        if (str == null) {
            throw new NullPointerException("schemaLanguage == null");
        } else if (str2 != null) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            if (classLoader != null) {
                try {
                    cls = classLoader.loadClass(str2);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException((Throwable) e);
                } catch (InstantiationException e2) {
                    throw new IllegalArgumentException((Throwable) e2);
                } catch (IllegalAccessException e3) {
                    throw new IllegalArgumentException((Throwable) e3);
                }
            } else {
                cls = Class.forName(str2);
            }
            SchemaFactory schemaFactory = (SchemaFactory) cls.newInstance();
            if (schemaFactory != null && schemaFactory.isSchemaLanguageSupported(str)) {
                return schemaFactory;
            }
            throw new IllegalArgumentException(str);
        } else {
            throw new NullPointerException("factoryClassName == null");
        }
    }

    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        throw new SAXNotRecognizedException(str);
    }

    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        throw new SAXNotRecognizedException(str);
    }

    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        throw new SAXNotRecognizedException(str);
    }

    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        throw new SAXNotRecognizedException(str);
    }

    public Schema newSchema(Source source) throws SAXException {
        return newSchema(new Source[]{source});
    }

    public Schema newSchema(File file) throws SAXException {
        return newSchema((Source) new StreamSource(file));
    }

    public Schema newSchema(URL url) throws SAXException {
        return newSchema((Source) new StreamSource(url.toExternalForm()));
    }
}
