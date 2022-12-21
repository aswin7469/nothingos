package java.util.prefs;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.Permission;
import java.util.Iterator;
import java.util.ServiceLoader;

public abstract class Preferences {
    public static final int MAX_KEY_LENGTH = 80;
    public static final int MAX_NAME_LENGTH = 80;
    public static final int MAX_VALUE_LENGTH = 8192;
    private static PreferencesFactory factory = factory();
    private static Permission prefsPerm = new RuntimePermission("preferences");

    public abstract String absolutePath();

    public abstract void addNodeChangeListener(NodeChangeListener nodeChangeListener);

    public abstract void addPreferenceChangeListener(PreferenceChangeListener preferenceChangeListener);

    public abstract String[] childrenNames() throws BackingStoreException;

    public abstract void clear() throws BackingStoreException;

    public abstract void exportNode(OutputStream outputStream) throws IOException, BackingStoreException;

    public abstract void exportSubtree(OutputStream outputStream) throws IOException, BackingStoreException;

    public abstract void flush() throws BackingStoreException;

    public abstract String get(String str, String str2);

    public abstract boolean getBoolean(String str, boolean z);

    public abstract byte[] getByteArray(String str, byte[] bArr);

    public abstract double getDouble(String str, double d);

    public abstract float getFloat(String str, float f);

    public abstract int getInt(String str, int i);

    public abstract long getLong(String str, long j);

    public abstract boolean isUserNode();

    public abstract String[] keys() throws BackingStoreException;

    public abstract String name();

    public abstract Preferences node(String str);

    public abstract boolean nodeExists(String str) throws BackingStoreException;

    public abstract Preferences parent();

    public abstract void put(String str, String str2);

    public abstract void putBoolean(String str, boolean z);

    public abstract void putByteArray(String str, byte[] bArr);

    public abstract void putDouble(String str, double d);

    public abstract void putFloat(String str, float f);

    public abstract void putInt(String str, int i);

    public abstract void putLong(String str, long j);

    public abstract void remove(String str);

    public abstract void removeNode() throws BackingStoreException;

    public abstract void removeNodeChangeListener(NodeChangeListener nodeChangeListener);

    public abstract void removePreferenceChangeListener(PreferenceChangeListener preferenceChangeListener);

    public abstract void sync() throws BackingStoreException;

    public abstract String toString();

    private static PreferencesFactory factory() {
        PreferencesFactory preferencesFactory = (PreferencesFactory) ServiceLoader.loadFromSystemProperty(PreferencesFactory.class);
        if (preferencesFactory != null) {
            return preferencesFactory;
        }
        Iterator<S> it = ServiceLoader.load(PreferencesFactory.class).iterator();
        if (it.hasNext()) {
            return (PreferencesFactory) it.next();
        }
        return new FileSystemPreferencesFactory();
    }

    public static PreferencesFactory setPreferencesFactory(PreferencesFactory preferencesFactory) {
        PreferencesFactory preferencesFactory2 = factory;
        factory = preferencesFactory;
        return preferencesFactory2;
    }

    public static Preferences userNodeForPackage(Class<?> cls) {
        return userRoot().node(nodeName(cls));
    }

    public static Preferences systemNodeForPackage(Class<?> cls) {
        return systemRoot().node(nodeName(cls));
    }

    private static String nodeName(Class<?> cls) {
        if (!cls.isArray()) {
            String name = cls.getName();
            int lastIndexOf = name.lastIndexOf(46);
            if (lastIndexOf < 0) {
                return "/<unnamed>";
            }
            String substring = name.substring(0, lastIndexOf);
            return "/" + substring.replace('.', '/');
        }
        throw new IllegalArgumentException("Arrays have no associated preferences node.");
    }

    public static Preferences userRoot() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(prefsPerm);
        }
        return factory.userRoot();
    }

    public static Preferences systemRoot() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(prefsPerm);
        }
        return factory.systemRoot();
    }

    protected Preferences() {
    }

    public static void importPreferences(InputStream inputStream) throws IOException, InvalidPreferencesFormatException {
        XmlSupport.importPreferences(inputStream);
    }
}
