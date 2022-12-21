package javax.crypto;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamClass;
import java.p026io.StreamCorruptedException;

/* compiled from: SealedObject */
final class extObjectInputStream extends ObjectInputStream {
    private static ClassLoader systemClassLoader;

    extObjectInputStream(InputStream inputStream) throws IOException, StreamCorruptedException {
        super(inputStream);
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        try {
            return super.resolveClass(objectStreamClass);
        } catch (ClassNotFoundException unused) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                if (systemClassLoader == null) {
                    systemClassLoader = ClassLoader.getSystemClassLoader();
                }
                contextClassLoader = systemClassLoader;
                if (contextClassLoader == null) {
                    throw new ClassNotFoundException(objectStreamClass.getName());
                }
            }
            return Class.forName(objectStreamClass.getName(), false, contextClassLoader);
        }
    }
}
