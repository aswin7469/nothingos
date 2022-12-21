package java.util.jar;

import java.beans.PropertyChangeListener;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.util.SortedMap;
import sun.security.action.GetPropertyAction;

public abstract class Pack200 {
    private static final String PACK_PROVIDER = "java.util.jar.Pack200.Packer";
    private static final String UNPACK_PROVIDER = "java.util.jar.Pack200.Unpacker";
    private static Class<?> packerImpl;
    private static Class<?> unpackerImpl;

    public interface Packer {
        public static final String CLASS_ATTRIBUTE_PFX = "pack.class.attribute.";
        public static final String CODE_ATTRIBUTE_PFX = "pack.code.attribute.";
        public static final String DEFLATE_HINT = "pack.deflate.hint";
        public static final String EFFORT = "pack.effort";
        public static final String ERROR = "error";
        public static final String FALSE = "false";
        public static final String FIELD_ATTRIBUTE_PFX = "pack.field.attribute.";
        public static final String KEEP = "keep";
        public static final String KEEP_FILE_ORDER = "pack.keep.file.order";
        public static final String LATEST = "latest";
        public static final String METHOD_ATTRIBUTE_PFX = "pack.method.attribute.";
        public static final String MODIFICATION_TIME = "pack.modification.time";
        public static final String PASS = "pass";
        public static final String PASS_FILE_PFX = "pack.pass.file.";
        public static final String PROGRESS = "pack.progress";
        public static final String SEGMENT_LIMIT = "pack.segment.limit";
        public static final String STRIP = "strip";
        public static final String TRUE = "true";
        public static final String UNKNOWN_ATTRIBUTE = "pack.unknown.attribute";

        @Deprecated
        void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        void pack(JarFile jarFile, OutputStream outputStream) throws IOException;

        void pack(JarInputStream jarInputStream, OutputStream outputStream) throws IOException;

        SortedMap<String, String> properties();

        @Deprecated
        void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }
    }

    public interface Unpacker {
        public static final String DEFLATE_HINT = "unpack.deflate.hint";
        public static final String FALSE = "false";
        public static final String KEEP = "keep";
        public static final String PROGRESS = "unpack.progress";
        public static final String TRUE = "true";

        @Deprecated
        void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        SortedMap<String, String> properties();

        @Deprecated
        void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        void unpack(File file, JarOutputStream jarOutputStream) throws IOException;

        void unpack(InputStream inputStream, JarOutputStream jarOutputStream) throws IOException;
    }

    private Pack200() {
    }

    public static synchronized Packer newPacker() {
        Packer packer;
        synchronized (Pack200.class) {
            packer = (Packer) newInstance(PACK_PROVIDER);
        }
        return packer;
    }

    public static Unpacker newUnpacker() {
        return (Unpacker) newInstance(UNPACK_PROVIDER);
    }

    private static synchronized Object newInstance(String str) {
        Object newInstance;
        synchronized (Pack200.class) {
            String str2 = "(unknown)";
            try {
                Class<?> cls = PACK_PROVIDER.equals(str) ? packerImpl : unpackerImpl;
                if (cls == null) {
                    String str3 = (String) AccessController.doPrivileged(new GetPropertyAction(str, ""));
                    if (str3 != null) {
                        try {
                            if (!str3.equals("")) {
                                cls = Class.forName(str3);
                            }
                        } catch (ClassNotFoundException e) {
                            e = e;
                            str2 = str3;
                            throw new Error("Class not found: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
                        } catch (InstantiationException e2) {
                            e = e2;
                            str2 = str3;
                            throw new Error("Could not instantiate: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
                        } catch (IllegalAccessException e3) {
                            e = e3;
                            str2 = str3;
                            throw new Error("Cannot access class: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
                        }
                    }
                    str2 = str3;
                }
                newInstance = cls.newInstance();
            } catch (ClassNotFoundException e4) {
                e = e4;
                throw new Error("Class not found: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
            } catch (InstantiationException e5) {
                e = e5;
                throw new Error("Could not instantiate: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
            } catch (IllegalAccessException e6) {
                e = e6;
                throw new Error("Cannot access class: " + str2 + ":\ncheck property " + str + " in your properties file.", e);
            }
        }
        return newInstance;
    }
}
