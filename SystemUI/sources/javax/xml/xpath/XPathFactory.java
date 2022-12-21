package javax.xml.xpath;

public abstract class XPathFactory {
    public static final String DEFAULT_OBJECT_MODEL_URI = "http://java.sun.com/jaxp/xpath/dom";
    public static final String DEFAULT_PROPERTY_NAME = "javax.xml.xpath.XPathFactory";

    public abstract boolean getFeature(String str) throws XPathFactoryConfigurationException;

    public abstract boolean isObjectModelSupported(String str);

    public abstract XPath newXPath();

    public abstract void setFeature(String str, boolean z) throws XPathFactoryConfigurationException;

    public abstract void setXPathFunctionResolver(XPathFunctionResolver xPathFunctionResolver);

    public abstract void setXPathVariableResolver(XPathVariableResolver xPathVariableResolver);

    protected XPathFactory() {
    }

    public static final XPathFactory newInstance() {
        try {
            return newInstance("http://java.sun.com/jaxp/xpath/dom");
        } catch (XPathFactoryConfigurationException e) {
            throw new RuntimeException("XPathFactory#newInstance() failed to create an XPathFactory for the default object model: http://java.sun.com/jaxp/xpath/dom with the XPathFactoryConfigurationException: " + e.toString());
        }
    }

    public static final XPathFactory newInstance(String str) throws XPathFactoryConfigurationException {
        if (str == null) {
            throw new NullPointerException("uri == null");
        } else if (str.length() != 0) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = XPathFactory.class.getClassLoader();
            }
            XPathFactory newFactory = new XPathFactoryFinder(contextClassLoader).newFactory(str);
            if (newFactory != null) {
                return newFactory;
            }
            throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + str);
        } else {
            throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
        }
    }

    public static XPathFactory newInstance(String str, String str2, ClassLoader classLoader) throws XPathFactoryConfigurationException {
        if (str == null) {
            throw new NullPointerException("uri == null");
        } else if (str.length() == 0) {
            throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
        } else if (str2 != null) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            XPathFactory createInstance = new XPathFactoryFinder(classLoader).createInstance(str2);
            if (createInstance != null && createInstance.isObjectModelSupported(str)) {
                return createInstance;
            }
            throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + str);
        } else {
            throw new XPathFactoryConfigurationException("factoryClassName cannot be null.");
        }
    }
}
