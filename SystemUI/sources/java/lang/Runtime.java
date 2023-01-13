package java.lang;

import android.system.OsConstants;
import dalvik.system.BlockGuard;
import dalvik.system.DelegateLastClassLoader;
import dalvik.system.PathClassLoader;
import dalvik.system.VMRuntime;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import libcore.p030io.Libcore;
import libcore.util.EmptyArray;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class Runtime {
    private static Runtime currentRuntime = new Runtime();
    private static boolean finalizeOnExit;
    private volatile String[] mLibPaths = null;
    private List<Thread> shutdownHooks = new ArrayList();
    private boolean shuttingDown;

    private static native void nativeExit(int i);

    private native void nativeGc();

    private static native String nativeLoad(String str, ClassLoader classLoader, Class<?> cls);

    private static native void runFinalization0();

    public native long freeMemory();

    @Deprecated
    public InputStream getLocalizedInputStream(InputStream inputStream) {
        return inputStream;
    }

    @Deprecated
    public OutputStream getLocalizedOutputStream(OutputStream outputStream) {
        return outputStream;
    }

    public native long maxMemory();

    public native long totalMemory();

    public void traceInstructions(boolean z) {
    }

    public static Runtime getRuntime() {
        return currentRuntime;
    }

    private Runtime() {
    }

    public void exit(int i) {
        int size;
        Thread[] threadArr;
        synchronized (this) {
            if (!this.shuttingDown) {
                this.shuttingDown = true;
                synchronized (this.shutdownHooks) {
                    size = this.shutdownHooks.size();
                    threadArr = new Thread[size];
                    this.shutdownHooks.toArray(threadArr);
                }
                for (int i2 = 0; i2 < size; i2++) {
                    threadArr[i2].start();
                }
                for (int i3 = 0; i3 < size; i3++) {
                    try {
                        threadArr[i3].join();
                    } catch (InterruptedException unused) {
                    }
                }
                if (finalizeOnExit) {
                    runFinalization();
                }
                nativeExit(i);
            }
        }
    }

    public void addShutdownHook(Thread thread) {
        if (thread == null) {
            throw new NullPointerException("hook == null");
        } else if (this.shuttingDown) {
            throw new IllegalStateException("VM already shutting down");
        } else if (!thread.started) {
            synchronized (this.shutdownHooks) {
                if (!this.shutdownHooks.contains(thread)) {
                    this.shutdownHooks.add(thread);
                } else {
                    throw new IllegalArgumentException("Hook already registered.");
                }
            }
        } else {
            throw new IllegalArgumentException("Hook has already been started");
        }
    }

    public boolean removeShutdownHook(Thread thread) {
        boolean remove;
        if (thread == null) {
            throw new NullPointerException("hook == null");
        } else if (!this.shuttingDown) {
            synchronized (this.shutdownHooks) {
                remove = this.shutdownHooks.remove((Object) thread);
            }
            return remove;
        } else {
            throw new IllegalStateException("VM already shutting down");
        }
    }

    public void halt(int i) {
        nativeExit(i);
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean z) {
        finalizeOnExit = z;
    }

    public Process exec(String str) throws IOException {
        return exec(str, (String[]) null, (File) null);
    }

    public Process exec(String str, String[] strArr) throws IOException {
        return exec(str, strArr, (File) null);
    }

    public Process exec(String str, String[] strArr, File file) throws IOException {
        if (str.length() != 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            String[] strArr2 = new String[stringTokenizer.countTokens()];
            int i = 0;
            while (stringTokenizer.hasMoreTokens()) {
                strArr2[i] = stringTokenizer.nextToken();
                i++;
            }
            return exec(strArr2, strArr, file);
        }
        throw new IllegalArgumentException("Empty command");
    }

    public Process exec(String[] strArr) throws IOException {
        return exec(strArr, (String[]) null, (File) null);
    }

    public Process exec(String[] strArr, String[] strArr2) throws IOException {
        return exec(strArr, strArr2, (File) null);
    }

    public Process exec(String[] strArr, String[] strArr2, File file) throws IOException {
        return new ProcessBuilder(strArr).environment(strArr2).directory(file).start();
    }

    public int availableProcessors() {
        return (int) Libcore.f855os.sysconf(OsConstants._SC_NPROCESSORS_CONF);
    }

    /* renamed from: gc */
    public void mo59687gc() {
        BlockGuard.getThreadPolicy().onExplicitGc();
        nativeGc();
    }

    public void runFinalization() {
        VMRuntime.runFinalization(0);
    }

    public void traceMethodCalls(boolean z) {
        if (z) {
            throw new UnsupportedOperationException();
        }
    }

    @CallerSensitive
    public void load(String str) {
        load0(Reflection.getCallerClass(), str);
    }

    private void checkTargetSdkVersionForLoad(String str) {
        int targetSdkVersion = VMRuntime.getRuntime().getTargetSdkVersion();
        if (targetSdkVersion > 24) {
            throw new UnsupportedOperationException(str + " is not supported on SDK " + targetSdkVersion);
        }
    }

    /* access modifiers changed from: package-private */
    public void load(String str, ClassLoader classLoader) {
        checkTargetSdkVersionForLoad("java.lang.Runtime#load(String, ClassLoader)");
        System.logE("java.lang.Runtime#load(String, ClassLoader) is private and will be removed in a future Android release");
        if (str != null) {
            String nativeLoad = nativeLoad(str, classLoader);
            if (nativeLoad != null) {
                throw new UnsatisfiedLinkError(nativeLoad);
            }
            return;
        }
        throw new NullPointerException("absolutePath == null");
    }

    /* access modifiers changed from: package-private */
    public synchronized void load0(Class<?> cls, String str) {
        if (!new File(str).isAbsolute()) {
            throw new UnsatisfiedLinkError("Expecting an absolute path of the library: " + str);
        } else if (str != null) {
            String nativeLoad = nativeLoad(str, cls.getClassLoader());
            if (nativeLoad != null) {
                throw new UnsatisfiedLinkError(nativeLoad);
            }
        } else {
            throw new NullPointerException("filename == null");
        }
    }

    @CallerSensitive
    public void loadLibrary(String str) {
        loadLibrary0(Reflection.getCallerClass(), str);
    }

    /* access modifiers changed from: package-private */
    public void loadLibrary0(Class<?> cls, String str) {
        loadLibrary0(ClassLoader.getClassLoader(cls), cls, str);
    }

    public void loadLibrary(String str, ClassLoader classLoader) {
        checkTargetSdkVersionForLoad("java.lang.Runtime#loadLibrary(String, ClassLoader)");
        System.logE("java.lang.Runtime#loadLibrary(String, ClassLoader) is private and will be removed in a future Android release");
        loadLibrary0(classLoader, (Class<?>) null, str);
    }

    /* access modifiers changed from: package-private */
    public void loadLibrary0(ClassLoader classLoader, String str) {
        loadLibrary0(classLoader, (Class<?>) null, str);
    }

    private synchronized void loadLibrary0(ClassLoader classLoader, Class<?> cls, String str) {
        if (str.indexOf((int) File.separatorChar) != -1) {
            throw new UnsatisfiedLinkError("Directory separator should not appear in library name: " + str);
        } else if (classLoader == null || (classLoader instanceof BootClassLoader)) {
            getLibPaths();
            String nativeLoad = nativeLoad(System.mapLibraryName(str), classLoader, cls);
            if (nativeLoad != null) {
                throw new UnsatisfiedLinkError(nativeLoad);
            }
        } else {
            String findLibrary = classLoader.findLibrary(str);
            if (findLibrary == null && (classLoader.getClass() == PathClassLoader.class || classLoader.getClass() == DelegateLastClassLoader.class)) {
                findLibrary = System.mapLibraryName(str);
            }
            if (findLibrary != null) {
                String nativeLoad2 = nativeLoad(findLibrary, classLoader);
                if (nativeLoad2 != null) {
                    throw new UnsatisfiedLinkError(nativeLoad2);
                }
                return;
            }
            throw new UnsatisfiedLinkError(classLoader + " couldn't find \"" + System.mapLibraryName(str) + "\"");
        }
    }

    private String[] getLibPaths() {
        if (this.mLibPaths == null) {
            synchronized (this) {
                if (this.mLibPaths == null) {
                    this.mLibPaths = initLibPaths();
                }
            }
        }
        return this.mLibPaths;
    }

    private static String[] initLibPaths() {
        String property = System.getProperty("java.library.path");
        if (property == null) {
            return EmptyArray.STRING;
        }
        String[] split = property.split(":");
        for (int i = 0; i < split.length; i++) {
            if (!split[i].endsWith("/")) {
                split[i] = split[i] + "/";
            }
        }
        return split;
    }

    private static String nativeLoad(String str, ClassLoader classLoader) {
        return nativeLoad(str, classLoader, (Class<?>) null);
    }
}
