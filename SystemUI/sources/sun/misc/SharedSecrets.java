package sun.misc;

public class SharedSecrets {
    private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;

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
}
