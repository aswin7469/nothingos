package java.lang.reflect;

import android.net.wifi.WifiEnterpriseConfig;
import java.util.StringJoiner;

public class Modifier {
    public static final int ABSTRACT = 1024;
    static final int ACCESS_MODIFIERS = 7;
    static final int ANNOTATION = 8192;
    static final int BRIDGE = 64;
    private static final int CLASS_MODIFIERS = 3103;
    public static final int CONSTRUCTOR = 65536;
    private static final int CONSTRUCTOR_MODIFIERS = 7;
    public static final int DEFAULT = 4194304;
    static final int ENUM = 16384;
    private static final int FIELD_MODIFIERS = 223;
    public static final int FINAL = 16;
    public static final int INTERFACE = 512;
    private static final int INTERFACE_MODIFIERS = 3087;
    static final int MANDATED = 32768;
    private static final int METHOD_MODIFIERS = 3391;
    public static final int NATIVE = 256;
    private static final int PARAMETER_MODIFIERS = 16;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    public static final int PUBLIC = 1;
    public static final int STATIC = 8;
    public static final int STRICT = 2048;
    public static final int SYNCHRONIZED = 32;
    public static final int SYNTHETIC = 4096;
    public static final int TRANSIENT = 128;
    static final int VARARGS = 128;
    public static final int VOLATILE = 64;

    public static int classModifiers() {
        return CLASS_MODIFIERS;
    }

    public static int constructorModifiers() {
        return 7;
    }

    public static int fieldModifiers() {
        return 223;
    }

    public static int interfaceModifiers() {
        return INTERFACE_MODIFIERS;
    }

    public static boolean isAbstract(int i) {
        return (i & 1024) != 0;
    }

    public static boolean isConstructor(int i) {
        return (i & 65536) != 0;
    }

    public static boolean isFinal(int i) {
        return (i & 16) != 0;
    }

    public static boolean isInterface(int i) {
        return (i & 512) != 0;
    }

    static boolean isMandated(int i) {
        return (i & 32768) != 0;
    }

    public static boolean isNative(int i) {
        return (i & 256) != 0;
    }

    public static boolean isPrivate(int i) {
        return (i & 2) != 0;
    }

    public static boolean isProtected(int i) {
        return (i & 4) != 0;
    }

    public static boolean isPublic(int i) {
        return (i & 1) != 0;
    }

    public static boolean isStatic(int i) {
        return (i & 8) != 0;
    }

    public static boolean isStrict(int i) {
        return (i & 2048) != 0;
    }

    public static boolean isSynchronized(int i) {
        return (i & 32) != 0;
    }

    static boolean isSynthetic(int i) {
        return (i & 4096) != 0;
    }

    public static boolean isTransient(int i) {
        return (i & 128) != 0;
    }

    public static boolean isVolatile(int i) {
        return (i & 64) != 0;
    }

    public static int methodModifiers() {
        return METHOD_MODIFIERS;
    }

    public static int parameterModifiers() {
        return 16;
    }

    public static String toString(int i) {
        StringJoiner stringJoiner = new StringJoiner(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if ((i & 1) != 0) {
            stringJoiner.add("public");
        }
        if ((i & 4) != 0) {
            stringJoiner.add("protected");
        }
        if ((i & 2) != 0) {
            stringJoiner.add("private");
        }
        if ((i & 1024) != 0) {
            stringJoiner.add("abstract");
        }
        if ((i & 8) != 0) {
            stringJoiner.add("static");
        }
        if ((i & 16) != 0) {
            stringJoiner.add("final");
        }
        if ((i & 128) != 0) {
            stringJoiner.add("transient");
        }
        if ((i & 64) != 0) {
            stringJoiner.add("volatile");
        }
        if ((i & 32) != 0) {
            stringJoiner.add("synchronized");
        }
        if ((i & 256) != 0) {
            stringJoiner.add("native");
        }
        if ((i & 2048) != 0) {
            stringJoiner.add("strictfp");
        }
        if ((i & 512) != 0) {
            stringJoiner.add("interface");
        }
        return stringJoiner.toString();
    }
}
