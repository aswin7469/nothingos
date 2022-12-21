package sun.util.resources;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import sun.util.ResourceBundleEnumeration;

public abstract class OpenListResourceBundle extends ResourceBundle {
    private volatile Set<String> keyset;
    private volatile Map<String, Object> lookup = null;

    /* access modifiers changed from: protected */
    public abstract Object[][] getContents();

    protected OpenListResourceBundle() {
    }

    /* access modifiers changed from: protected */
    public Object handleGetObject(String str) {
        str.getClass();
        loadLookupTablesIfNecessary();
        return this.lookup.get(str);
    }

    public Enumeration<String> getKeys() {
        ResourceBundle resourceBundle = this.parent;
        return new ResourceBundleEnumeration(handleKeySet(), resourceBundle != null ? resourceBundle.getKeys() : null);
    }

    /* access modifiers changed from: protected */
    public Set<String> handleKeySet() {
        loadLookupTablesIfNecessary();
        return this.lookup.keySet();
    }

    public Set<String> keySet() {
        if (this.keyset != null) {
            return this.keyset;
        }
        Set<String> createSet = createSet();
        createSet.addAll(handleKeySet());
        if (this.parent != null) {
            createSet.addAll(this.parent.keySet());
        }
        synchronized (this) {
            if (this.keyset == null) {
                this.keyset = createSet;
            }
        }
        return this.keyset;
    }

    /* access modifiers changed from: package-private */
    public void loadLookupTablesIfNecessary() {
        if (this.lookup == null) {
            loadLookup();
        }
    }

    private void loadLookup() {
        Object[][] contents = getContents();
        Map<String, Object> createMap = createMap(contents.length);
        for (Object[] objArr : contents) {
            String str = (String) objArr[0];
            Object obj = objArr[1];
            if (str == null || obj == null) {
                throw null;
            }
            createMap.put(str, obj);
        }
        synchronized (this) {
            if (this.lookup == null) {
                this.lookup = createMap;
            }
        }
    }

    /* access modifiers changed from: protected */
    public <K, V> Map<K, V> createMap(int i) {
        return new HashMap(i);
    }

    /* access modifiers changed from: protected */
    public <E> Set<E> createSet() {
        return new HashSet();
    }
}
