package java.util;

import sun.util.ResourceBundleEnumeration;

public abstract class ListResourceBundle extends ResourceBundle {
    private volatile Map<String, Object> lookup = null;

    /* access modifiers changed from: protected */
    public abstract Object[][] getContents();

    public final Object handleGetObject(String str) {
        if (this.lookup == null) {
            loadLookup();
        }
        str.getClass();
        return this.lookup.get(str);
    }

    public Enumeration<String> getKeys() {
        if (this.lookup == null) {
            loadLookup();
        }
        ResourceBundle resourceBundle = this.parent;
        return new ResourceBundleEnumeration(this.lookup.keySet(), resourceBundle != null ? resourceBundle.getKeys() : null);
    }

    /* access modifiers changed from: protected */
    public Set<String> handleKeySet() {
        if (this.lookup == null) {
            loadLookup();
        }
        return this.lookup.keySet();
    }

    private synchronized void loadLookup() {
        if (this.lookup == null) {
            Object[][] contents = getContents();
            HashMap hashMap = new HashMap(contents.length);
            for (Object[] objArr : contents) {
                String str = (String) objArr[0];
                Object obj = objArr[1];
                if (str == null || obj == null) {
                    throw new NullPointerException();
                }
                hashMap.put(str, obj);
            }
            this.lookup = hashMap;
        }
    }
}
