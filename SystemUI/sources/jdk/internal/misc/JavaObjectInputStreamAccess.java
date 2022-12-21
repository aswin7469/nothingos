package jdk.internal.misc;

import java.p026io.InvalidClassException;
import java.p026io.ObjectInputStream;

@FunctionalInterface
public interface JavaObjectInputStreamAccess {
    void checkArray(ObjectInputStream objectInputStream, Class<?> cls, int i) throws InvalidClassException;
}
