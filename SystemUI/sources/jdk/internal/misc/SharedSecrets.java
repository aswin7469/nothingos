package jdk.internal.misc;

import java.p026io.ObjectInputStream;

public class SharedSecrets {
    private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;
    private static JavaObjectInputStreamAccess javaObjectInputStreamAccess;
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public static void setJavaIOFileDescriptorAccess(JavaIOFileDescriptorAccess javaIOFileDescriptorAccess2) {
        javaIOFileDescriptorAccess = javaIOFileDescriptorAccess2;
    }

    public static JavaIOFileDescriptorAccess getJavaIOFileDescriptorAccess() {
        if (javaIOFileDescriptorAccess == null) {
            try {
                Class.forName("java.io.FileDescriptor");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException((Throwable) e);
            }
        }
        return javaIOFileDescriptorAccess;
    }

    public static JavaObjectInputStreamAccess getJavaObjectInputStreamAccess() {
        if (javaObjectInputStreamAccess == null) {
            unsafe.ensureClassInitialized(ObjectInputStream.class);
        }
        return javaObjectInputStreamAccess;
    }

    public static void setJavaObjectInputStreamAccess(JavaObjectInputStreamAccess javaObjectInputStreamAccess2) {
        javaObjectInputStreamAccess = javaObjectInputStreamAccess2;
    }
}
