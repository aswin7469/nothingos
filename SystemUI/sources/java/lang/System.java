package java.lang;

import android.system.ErrnoException;
import android.system.StructUtsname;
import androidx.core.p004os.EnvironmentCompat;
import dalvik.system.VMRuntime;
import java.nio.channels.Channel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.BufferedInputStream;
import java.p026io.BufferedOutputStream;
import java.p026io.Console;
import java.p026io.FileDescriptor;
import java.p026io.FileInputStream;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyPermission;
import libcore.icu.ICU;
import libcore.p030io.Libcore;
import sun.misc.C4740VM;
import sun.misc.Version;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public final class System {
    private static final int ARRAYCOPY_SHORT_BOOLEAN_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_BYTE_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_CHAR_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_DOUBLE_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_FLOAT_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_INT_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_LONG_ARRAY_THRESHOLD = 32;
    private static final int ARRAYCOPY_SHORT_SHORT_ARRAY_THRESHOLD = 32;
    private static final Object LOCK = new Object();
    private static volatile Console cons;
    public static final PrintStream err;

    /* renamed from: in */
    public static final InputStream f550in;
    private static boolean justRanFinalization;
    private static String lineSeparator = props.getProperty("line.separator");
    public static final PrintStream out;
    private static Properties props = initProperties();
    private static boolean runGC;
    private static Properties unchangeableProps = initUnchangeableSystemProperties();

    public static native void arraycopy(Object obj, int i, Object obj2, int i2, int i3);

    private static native void arraycopyBooleanUnchecked(boolean[] zArr, int i, boolean[] zArr2, int i2, int i3);

    private static native void arraycopyByteUnchecked(byte[] bArr, int i, byte[] bArr2, int i2, int i3);

    private static native void arraycopyCharUnchecked(char[] cArr, int i, char[] cArr2, int i2, int i3);

    private static native void arraycopyDoubleUnchecked(double[] dArr, int i, double[] dArr2, int i2, int i3);

    private static native void arraycopyFloatUnchecked(float[] fArr, int i, float[] fArr2, int i2, int i3);

    private static native void arraycopyIntUnchecked(int[] iArr, int i, int[] iArr2, int i2, int i3);

    private static native void arraycopyLongUnchecked(long[] jArr, int i, long[] jArr2, int i2, int i3);

    private static native void arraycopyShortUnchecked(short[] sArr, int i, short[] sArr2, int i2, int i3);

    public static native long currentTimeMillis();

    public static SecurityManager getSecurityManager() {
        return null;
    }

    private static native void log(char c, String str, Throwable th);

    public static native String mapLibraryName(String str);

    public static native long nanoTime();

    private static native void setErr0(PrintStream printStream);

    private static native void setIn0(InputStream inputStream);

    private static native void setOut0(PrintStream printStream);

    private static native String[] specialProperties();

    private System() {
    }

    static {
        addLegacyLocaleSystemProperties();
        Version.initSystemProperties();
        FileInputStream fileInputStream = new FileInputStream(FileDescriptor.f518in);
        FileOutputStream fileOutputStream = new FileOutputStream(FileDescriptor.out);
        FileOutputStream fileOutputStream2 = new FileOutputStream(FileDescriptor.err);
        f550in = new BufferedInputStream(fileInputStream, 128);
        out = newPrintStream(fileOutputStream, props.getProperty("sun.stdout.encoding"));
        err = newPrintStream(fileOutputStream2, props.getProperty("sun.stderr.encoding"));
        C4740VM.initializeOSEnvironment();
        C4740VM.booted();
    }

    public static void setIn(InputStream inputStream) {
        setIn0(inputStream);
    }

    public static void setOut(PrintStream printStream) {
        setOut0(printStream);
    }

    public static void setErr(PrintStream printStream) {
        setErr0(printStream);
    }

    public static Console console() {
        if (cons == null) {
            synchronized (System.class) {
                if (cons == null) {
                    cons = Console.console();
                }
            }
        }
        return cons;
    }

    public static Channel inheritedChannel() throws IOException {
        return SelectorProvider.provider().inheritedChannel();
    }

    public static void setSecurityManager(SecurityManager securityManager) {
        if (securityManager != null) {
            throw new SecurityException();
        }
    }

    private static void arraycopy(char[] cArr, int i, char[] cArr2, int i2, int i3) {
        if (cArr == null) {
            throw new NullPointerException("src == null");
        } else if (cArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > cArr.length - i3 || i2 > cArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + cArr.length + " srcPos=" + i + " dst.length=" + cArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyCharUnchecked(cArr, i, cArr2, i2, i3);
        } else if (cArr != cArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                cArr2[i2 + i4] = cArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                cArr2[i2 + i5] = cArr[i + i5];
            }
        }
    }

    private static void arraycopy(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException("src == null");
        } else if (bArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > bArr.length - i3 || i2 > bArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + bArr.length + " srcPos=" + i + " dst.length=" + bArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyByteUnchecked(bArr, i, bArr2, i2, i3);
        } else if (bArr != bArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                bArr2[i2 + i4] = bArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                bArr2[i2 + i5] = bArr[i + i5];
            }
        }
    }

    private static void arraycopy(short[] sArr, int i, short[] sArr2, int i2, int i3) {
        if (sArr == null) {
            throw new NullPointerException("src == null");
        } else if (sArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > sArr.length - i3 || i2 > sArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + sArr.length + " srcPos=" + i + " dst.length=" + sArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyShortUnchecked(sArr, i, sArr2, i2, i3);
        } else if (sArr != sArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                sArr2[i2 + i4] = sArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                sArr2[i2 + i5] = sArr[i + i5];
            }
        }
    }

    private static void arraycopy(int[] iArr, int i, int[] iArr2, int i2, int i3) {
        if (iArr == null) {
            throw new NullPointerException("src == null");
        } else if (iArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > iArr.length - i3 || i2 > iArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + iArr.length + " srcPos=" + i + " dst.length=" + iArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyIntUnchecked(iArr, i, iArr2, i2, i3);
        } else if (iArr != iArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                iArr2[i2 + i4] = iArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                iArr2[i2 + i5] = iArr[i + i5];
            }
        }
    }

    private static void arraycopy(long[] jArr, int i, long[] jArr2, int i2, int i3) {
        if (jArr == null) {
            throw new NullPointerException("src == null");
        } else if (jArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > jArr.length - i3 || i2 > jArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + jArr.length + " srcPos=" + i + " dst.length=" + jArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyLongUnchecked(jArr, i, jArr2, i2, i3);
        } else if (jArr != jArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                jArr2[i2 + i4] = jArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                jArr2[i2 + i5] = jArr[i + i5];
            }
        }
    }

    private static void arraycopy(float[] fArr, int i, float[] fArr2, int i2, int i3) {
        if (fArr == null) {
            throw new NullPointerException("src == null");
        } else if (fArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > fArr.length - i3 || i2 > fArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + fArr.length + " srcPos=" + i + " dst.length=" + fArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyFloatUnchecked(fArr, i, fArr2, i2, i3);
        } else if (fArr != fArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                fArr2[i2 + i4] = fArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                fArr2[i2 + i5] = fArr[i + i5];
            }
        }
    }

    private static void arraycopy(double[] dArr, int i, double[] dArr2, int i2, int i3) {
        if (dArr == null) {
            throw new NullPointerException("src == null");
        } else if (dArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > dArr.length - i3 || i2 > dArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + dArr.length + " srcPos=" + i + " dst.length=" + dArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyDoubleUnchecked(dArr, i, dArr2, i2, i3);
        } else if (dArr != dArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                dArr2[i2 + i4] = dArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                dArr2[i2 + i5] = dArr[i + i5];
            }
        }
    }

    private static void arraycopy(boolean[] zArr, int i, boolean[] zArr2, int i2, int i3) {
        if (zArr == null) {
            throw new NullPointerException("src == null");
        } else if (zArr2 == null) {
            throw new NullPointerException("dst == null");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i > zArr.length - i3 || i2 > zArr2.length - i3) {
            throw new ArrayIndexOutOfBoundsException("src.length=" + zArr.length + " srcPos=" + i + " dst.length=" + zArr2.length + " dstPos=" + i2 + " length=" + i3);
        } else if (i3 > 32) {
            arraycopyBooleanUnchecked(zArr, i, zArr2, i2, i3);
        } else if (zArr != zArr2 || i >= i2 || i2 >= i + i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                zArr2[i2 + i4] = zArr[i + i4];
            }
        } else {
            for (int i5 = i3 - 1; i5 >= 0; i5--) {
                zArr2[i2 + i5] = zArr[i + i5];
            }
        }
    }

    public static int identityHashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Object.identityHashCode(obj);
    }

    static final class PropertiesWithNonOverrideableDefaults extends Properties {
        PropertiesWithNonOverrideableDefaults(Properties properties) {
            super(properties);
        }

        public Object put(Object obj, Object obj2) {
            if (!this.defaults.containsKey(obj)) {
                return super.put(obj, obj2);
            }
            System.logE("Ignoring attempt to set property \"" + obj + "\" to value \"" + obj2 + "\".");
            return this.defaults.get(obj);
        }

        public Object remove(Object obj) {
            if (!this.defaults.containsKey(obj)) {
                return super.remove(obj);
            }
            System.logE("Ignoring attempt to remove property \"" + obj + "\".");
            return null;
        }
    }

    private static void parsePropertyAssignments(Properties properties, String[] strArr) {
        for (String str : strArr) {
            int indexOf = str.indexOf(61);
            properties.put(str.substring(0, indexOf), str.substring(indexOf + 1));
        }
    }

    private static Properties initUnchangeableSystemProperties() {
        String str;
        VMRuntime runtime = VMRuntime.getRuntime();
        Properties properties = new Properties();
        properties.put("java.boot.class.path", runtime.bootClassPath());
        properties.put("java.class.path", runtime.classPath());
        String str2 = getenv("ANDROID_ART_ROOT");
        if (str2 == null) {
            str2 = "/apex/com.android.art";
        }
        properties.put("java.home", str2);
        properties.put("java.vm.version", runtime.vmVersion());
        try {
            str = Libcore.f857os.getpwuid(Libcore.f857os.getuid()).pw_name;
        } catch (ErrnoException unused) {
            str = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        properties.put("user.name", str);
        StructUtsname uname = Libcore.f857os.uname();
        properties.put("os.arch", uname.machine);
        properties.put("os.name", uname.sysname);
        properties.put("os.version", uname.release);
        properties.put("android.icu.library.version", ICU.getIcuVersion());
        properties.put("android.icu.unicode.version", ICU.getUnicodeVersion());
        properties.put("android.icu.cldr.version", ICU.getCldrVersion());
        parsePropertyAssignments(properties, specialProperties());
        parsePropertyAssignments(properties, runtime.properties());
        for (String[] strArr : AndroidHardcodedSystemProperties.STATIC_PROPERTIES) {
            if (properties.containsKey(strArr[0])) {
                logE("Ignoring command line argument: -D" + strArr[0]);
            }
            String str3 = strArr[1];
            if (str3 == null) {
                properties.remove(strArr[0]);
            } else {
                properties.put(strArr[0], str3);
            }
        }
        return properties;
    }

    private static Properties initProperties() {
        PropertiesWithNonOverrideableDefaults propertiesWithNonOverrideableDefaults = new PropertiesWithNonOverrideableDefaults(unchangeableProps);
        setDefaultChangeableProperties(propertiesWithNonOverrideableDefaults);
        return propertiesWithNonOverrideableDefaults;
    }

    private static Properties setDefaultChangeableProperties(Properties properties) {
        if (!unchangeableProps.containsKey("java.io.tmpdir")) {
            properties.put("java.io.tmpdir", "/tmp");
        }
        if (!unchangeableProps.containsKey("user.home")) {
            properties.put("user.home", "");
        }
        return properties;
    }

    public static void setUnchangeableSystemProperty(String str, String str2) {
        checkKey(str);
        unchangeableProps.put(str, str2);
    }

    private static void addLegacyLocaleSystemProperties() {
        String property = getProperty("user.locale", "");
        if (!property.isEmpty()) {
            Locale forLanguageTag = Locale.forLanguageTag(property);
            setUnchangeableSystemProperty("user.language", forLanguageTag.getLanguage());
            setUnchangeableSystemProperty("user.region", forLanguageTag.getCountry());
            setUnchangeableSystemProperty("user.variant", forLanguageTag.getVariant());
            return;
        }
        String property2 = getProperty("user.language", "");
        String property3 = getProperty("user.region", "");
        if (property2.isEmpty()) {
            setUnchangeableSystemProperty("user.language", "en");
        }
        if (property3.isEmpty()) {
            setUnchangeableSystemProperty("user.region", "US");
        }
    }

    public static Properties getProperties() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        return props;
    }

    public static String lineSeparator() {
        return lineSeparator;
    }

    public static void setProperties(Properties properties) {
        PropertiesWithNonOverrideableDefaults propertiesWithNonOverrideableDefaults = new PropertiesWithNonOverrideableDefaults(unchangeableProps);
        if (properties != null) {
            propertiesWithNonOverrideableDefaults.putAll(properties);
        } else {
            setDefaultChangeableProperties(propertiesWithNonOverrideableDefaults);
        }
        props = propertiesWithNonOverrideableDefaults;
    }

    public static String getProperty(String str) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess(str);
        }
        return props.getProperty(str);
    }

    public static String getProperty(String str, String str2) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess(str);
        }
        return props.getProperty(str, str2);
    }

    public static String setProperty(String str, String str2) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission(str, "write"));
        }
        return (String) props.setProperty(str, str2);
    }

    public static String clearProperty(String str) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission(str, "write"));
        }
        return (String) props.remove(str);
    }

    private static void checkKey(String str) {
        if (str == null) {
            throw new NullPointerException("key can't be null");
        } else if (str.equals("")) {
            throw new IllegalArgumentException("key can't be empty");
        }
    }

    public static String getenv(String str) {
        if (str != null) {
            return Libcore.f857os.getenv(str);
        }
        throw new NullPointerException("name == null");
    }

    public static Map<String, String> getenv() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getenv.*"));
        }
        return ProcessEnvironment.getenv();
    }

    public static void exit(int i) {
        Runtime.getRuntime().exit(i);
    }

    /* renamed from: gc */
    public static void m1693gc() {
        boolean z;
        synchronized (LOCK) {
            z = justRanFinalization;
            if (z) {
                justRanFinalization = false;
            } else {
                runGC = true;
            }
        }
        if (z) {
            Runtime.getRuntime().mo59631gc();
        }
    }

    public static void runFinalization() {
        boolean z;
        Object obj = LOCK;
        synchronized (obj) {
            z = runGC;
            runGC = false;
        }
        if (z) {
            Runtime.getRuntime().mo59631gc();
        }
        Runtime.getRuntime().runFinalization();
        synchronized (obj) {
            justRanFinalization = true;
        }
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean z) {
        Runtime.runFinalizersOnExit(z);
    }

    @CallerSensitive
    public static void load(String str) {
        Runtime.getRuntime().load0(Reflection.getCallerClass(), str);
    }

    @CallerSensitive
    public static void loadLibrary(String str) {
        Runtime.getRuntime().loadLibrary0(Reflection.getCallerClass(), str);
    }

    private static PrintStream newPrintStream(FileOutputStream fileOutputStream, String str) {
        if (str != null) {
            try {
                return new PrintStream((OutputStream) new BufferedOutputStream(fileOutputStream, 128), true, str);
            } catch (UnsupportedEncodingException unused) {
            }
        }
        return new PrintStream((OutputStream) new BufferedOutputStream(fileOutputStream, 128), true);
    }

    public static void logE(String str) {
        log('E', str, (Throwable) null);
    }

    public static void logE(String str, Throwable th) {
        log('E', str, th);
    }

    public static void logI(String str) {
        log('I', str, (Throwable) null);
    }

    public static void logI(String str, Throwable th) {
        log('I', str, th);
    }

    public static void logW(String str) {
        log('W', str, (Throwable) null);
    }

    public static void logW(String str, Throwable th) {
        log('W', str, th);
    }
}
