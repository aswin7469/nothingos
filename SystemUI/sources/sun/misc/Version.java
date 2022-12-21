package sun.misc;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintStream;
import sun.util.locale.LanguageTag;

public class Version {
    private static final String java_profile_name = "";
    private static final String java_runtime_name = "Android Runtime";
    private static final String java_runtime_version = "0.9";
    private static final String java_version = "0";
    private static int jdk_build_number = 0;
    private static int jdk_major_version = 0;
    private static int jdk_micro_version = 0;
    private static int jdk_minor_version = 0;
    private static String jdk_special_version = null;
    private static int jdk_update_version = 0;
    private static boolean jvmVersionInfoAvailable = false;
    private static int jvm_build_number = 0;
    private static int jvm_major_version = 0;
    private static int jvm_micro_version = 0;
    private static int jvm_minor_version = 0;
    private static String jvm_special_version = null;
    private static int jvm_update_version = 0;
    private static final String launcher_name = "";
    private static boolean versionsInitialized = false;

    public static native String getJdkSpecialVersion();

    private static native void getJdkVersionInfo();

    public static native String getJvmSpecialVersion();

    private static native boolean getJvmVersionInfo();

    public static void initSystemProperties() {
        System.setUnchangeableSystemProperty("java.version", "0");
        System.setUnchangeableSystemProperty("java.runtime.version", java_runtime_version);
        System.setUnchangeableSystemProperty("java.runtime.name", java_runtime_name);
    }

    public static void print() {
        print(System.err);
    }

    public static void println() {
        print(System.err);
        System.err.println();
    }

    public static void print(PrintStream printStream) {
        String property = System.getProperty("java.awt.headless");
        if (property != null) {
            boolean equalsIgnoreCase = property.equalsIgnoreCase("true");
        }
        printStream.println(" version \"0\"");
        printStream.print("Android Runtime (build 0.9");
        printStream.println(')');
        String property2 = System.getProperty("java.vm.name");
        String property3 = System.getProperty("java.vm.version");
        String property4 = System.getProperty("java.vm.info");
        printStream.println(property2 + " (build " + property3 + ", " + property4 + NavigationBarInflaterView.KEY_CODE_END);
    }

    public static synchronized int jvmMajorVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jvm_major_version;
        }
        return i;
    }

    public static synchronized int jvmMinorVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jvm_minor_version;
        }
        return i;
    }

    public static synchronized int jvmMicroVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jvm_micro_version;
        }
        return i;
    }

    public static synchronized int jvmUpdateVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jvm_update_version;
        }
        return i;
    }

    public static synchronized String jvmSpecialVersion() {
        String str;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            if (jvm_special_version == null) {
                jvm_special_version = getJvmSpecialVersion();
            }
            str = jvm_special_version;
        }
        return str;
    }

    public static synchronized int jvmBuildNumber() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jvm_build_number;
        }
        return i;
    }

    public static synchronized int jdkMajorVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jdk_major_version;
        }
        return i;
    }

    public static synchronized int jdkMinorVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jdk_minor_version;
        }
        return i;
    }

    public static synchronized int jdkMicroVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jdk_micro_version;
        }
        return i;
    }

    public static synchronized int jdkUpdateVersion() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jdk_update_version;
        }
        return i;
    }

    public static synchronized String jdkSpecialVersion() {
        String str;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            if (jdk_special_version == null) {
                jdk_special_version = getJdkSpecialVersion();
            }
            str = jdk_special_version;
        }
        return str;
    }

    public static synchronized int jdkBuildNumber() {
        int i;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                initVersions();
            }
            i = jdk_build_number;
        }
        return i;
    }

    private static synchronized void initVersions() {
        char charAt;
        synchronized (Version.class) {
            if (!versionsInitialized) {
                boolean jvmVersionInfo = getJvmVersionInfo();
                jvmVersionInfoAvailable = jvmVersionInfo;
                if (!jvmVersionInfo) {
                    String property = System.getProperty("java.vm.version");
                    if (property.length() >= 5 && Character.isDigit(property.charAt(0)) && property.charAt(1) == '.' && Character.isDigit(property.charAt(2)) && property.charAt(3) == '.') {
                        int i = 4;
                        if (Character.isDigit(property.charAt(4))) {
                            jvm_major_version = Character.digit(property.charAt(0), 10);
                            jvm_minor_version = Character.digit(property.charAt(2), 10);
                            jvm_micro_version = Character.digit(property.charAt(4), 10);
                            CharSequence subSequence = property.subSequence(5, property.length());
                            if (subSequence.charAt(0) == '_' && subSequence.length() >= 3) {
                                if (!Character.isDigit(subSequence.charAt(1)) || !Character.isDigit(subSequence.charAt(2)) || !Character.isDigit(subSequence.charAt(3))) {
                                    i = (!Character.isDigit(subSequence.charAt(1)) || !Character.isDigit(subSequence.charAt(2))) ? 0 : 3;
                                }
                                try {
                                    jvm_update_version = Integer.valueOf(subSequence.subSequence(1, i).toString()).intValue();
                                    int i2 = i + 1;
                                    if (subSequence.length() >= i2 && (charAt = subSequence.charAt(i)) >= 'a' && charAt <= 'z') {
                                        jvm_special_version = Character.toString(charAt);
                                        i = i2;
                                    }
                                    subSequence = subSequence.subSequence(i, subSequence.length());
                                } catch (NumberFormatException unused) {
                                    return;
                                }
                            }
                            if (subSequence.charAt(0) == '-') {
                                String[] split = subSequence.subSequence(1, subSequence.length()).toString().split(LanguageTag.SEP);
                                int length = split.length;
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= length) {
                                        break;
                                    }
                                    String str = split[i3];
                                    if (str.charAt(0) == 'b' && str.length() == 3 && Character.isDigit(str.charAt(1)) && Character.isDigit(str.charAt(2))) {
                                        jvm_build_number = Integer.valueOf(str.substring(1, 3)).intValue();
                                        break;
                                    }
                                    i3++;
                                }
                            }
                        }
                    }
                }
                getJdkVersionInfo();
                versionsInitialized = true;
            }
        }
    }
}
