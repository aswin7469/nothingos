package java.lang;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.lang.Enum;
import java.lang.reflect.InvocationTargetException;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamException;
import java.p026io.Serializable;
import java.util.Objects;
import libcore.util.BasicLruCache;

public abstract class Enum<E extends Enum<E>> implements Comparable<E>, Serializable {
    private static final BasicLruCache<Class<? extends Enum>, Object[]> sharedConstantsCache = new BasicLruCache<Class<? extends Enum>, Object[]>(64) {
        /* access modifiers changed from: protected */
        public Object[] create(Class<? extends Enum> cls) {
            return Enum.enumValues(cls);
        }
    };
    private final String name;
    private final int ordinal;

    public final boolean equals(Object obj) {
        return this == obj;
    }

    /* access modifiers changed from: protected */
    public final void finalize() {
    }

    public final String name() {
        return this.name;
    }

    public final int ordinal() {
        return this.ordinal;
    }

    protected Enum(String str, int i) {
        this.name = str;
        this.ordinal = i;
    }

    public String toString() {
        return this.name;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    /* access modifiers changed from: protected */
    public final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public final int compareTo(E e) {
        if (getClass() == e.getClass() || getDeclaringClass() == e.getDeclaringClass()) {
            return this.ordinal - e.ordinal;
        }
        throw new ClassCastException();
    }

    public final Class<E> getDeclaringClass() {
        Class<?> cls = getClass();
        Class<? super Object> superclass = cls.getSuperclass();
        return superclass == Enum.class ? cls : superclass;
    }

    public static <T extends Enum<T>> T valueOf(Class<T> cls, String str) {
        Objects.requireNonNull(cls, "enumType == null");
        Objects.requireNonNull(str, "name == null");
        T[] sharedConstants = getSharedConstants(cls);
        if (sharedConstants != null) {
            for (int length = sharedConstants.length - 1; length >= 0; length--) {
                T t = sharedConstants[length];
                if (str.equals(t.name())) {
                    return t;
                }
            }
            throw new IllegalArgumentException("No enum constant " + cls.getCanonicalName() + BaseIconCache.EMPTY_CLASS_NAME + str);
        }
        throw new IllegalArgumentException(cls.toString() + " is not an enum type.");
    }

    /* access modifiers changed from: private */
    public static Object[] enumValues(Class<? extends Enum> cls) {
        if (!cls.isEnum()) {
            return null;
        }
        try {
            return (Object[]) cls.getDeclaredMethod("values", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Enum<T>> T[] getSharedConstants(Class<T> cls) {
        return (Enum[]) sharedConstantsCache.get(cls);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }
}
