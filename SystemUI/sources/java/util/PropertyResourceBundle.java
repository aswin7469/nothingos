package java.util;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.Reader;
import sun.util.ResourceBundleEnumeration;

public class PropertyResourceBundle extends ResourceBundle {
    private final Map<String, Object> lookup;

    public PropertyResourceBundle(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        this.lookup = new HashMap(properties);
    }

    public PropertyResourceBundle(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        this.lookup = new HashMap(properties);
    }

    public Object handleGetObject(String str) {
        str.getClass();
        return this.lookup.get(str);
    }

    public Enumeration<String> getKeys() {
        ResourceBundle resourceBundle = this.parent;
        return new ResourceBundleEnumeration(this.lookup.keySet(), resourceBundle != null ? resourceBundle.getKeys() : null);
    }

    /* access modifiers changed from: protected */
    public Set<String> handleKeySet() {
        return this.lookup.keySet();
    }
}
