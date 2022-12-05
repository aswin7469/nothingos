package com.android.internal.os;

import android.app.ApplicationLoaders;
import android.content.pm.SharedLibraryInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaMetrics;
import android.os.Build;
import android.os.Environment;
import android.os.IInstalld;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.ZygoteProcess;
import android.provider.SettingsStringUtil;
import android.security.keystore2.AndroidKeyStoreProvider;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructCapUserData;
import android.system.StructCapUserHeader;
import android.text.Hyphenator;
import android.util.Log;
import android.util.Slog;
import android.util.TimingsTraceLog;
import android.webkit.WebViewFactory;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;
import dalvik.system.ZygoteHooks;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import libcore.io.IoUtils;
/* loaded from: classes4.dex */
public class ZygoteInit {
    private static final String ABI_LIST_ARG = "--abi-list=";
    private static final int LOG_BOOT_PROGRESS_PRELOAD_END = 3030;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_START = 3020;
    private static final String PRELOADED_CLASSES = "/system/etc/preloaded-classes";
    private static final boolean PRELOAD_RESOURCES = true;
    private static final String PROPERTY_DISABLE_GRAPHICS_DRIVER_PRELOADING = "ro.zygote.disable_gl_preload";
    private static final int ROOT_GID = 0;
    private static final int ROOT_UID = 0;
    private static final String SOCKET_NAME_ARG = "--socket-name=";
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int UNPRIVILEGED_UID = 9999;
    private static Resources mResources;
    private static boolean sPreloadComplete;
    private static final String TAG = "Zygote";
    private static final boolean LOGGING_DEBUG = Log.isLoggable(TAG, 3);
    private static ClassLoader sCachedSystemServerClassLoader = null;

    private static native void nativePreloadAppProcessHALs();

    static native void nativePreloadGraphicsDriver();

    private static native void nativeZygoteInit();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void preload(TimingsTraceLog bootTimingsTraceLog) {
        Log.d(TAG, "begin preload");
        bootTimingsTraceLog.traceBegin("BeginPreload");
        beginPreload();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadClasses");
        preloadClasses();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("CacheNonBootClasspathClassLoaders");
        cacheNonBootClasspathClassLoaders();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadResources");
        preloadResources();
        bootTimingsTraceLog.traceEnd();
        Trace.traceBegin(16384L, "PreloadAppProcessHALs");
        nativePreloadAppProcessHALs();
        Trace.traceEnd(16384L);
        Trace.traceBegin(16384L, "PreloadGraphicsDriver");
        maybePreloadGraphicsDriver();
        Trace.traceEnd(16384L);
        preloadSharedLibraries();
        preloadTextResources();
        WebViewFactory.prepareWebViewInZygote();
        endPreload();
        warmUpJcaProviders();
        Log.d(TAG, "end preload");
        sPreloadComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lazyPreload() {
        Preconditions.checkState(!sPreloadComplete);
        Log.i(TAG, "Lazily preloading resources.");
        preload(new TimingsTraceLog("ZygoteInitTiming_lazy", 16384L));
    }

    private static void beginPreload() {
        Log.i(TAG, "Calling ZygoteHooks.beginPreload()");
        ZygoteHooks.onBeginPreload();
    }

    private static void endPreload() {
        ZygoteHooks.onEndPreload();
        Log.i(TAG, "Called ZygoteHooks.endPreload()");
    }

    private static void preloadSharedLibraries() {
        Log.i(TAG, "Preloading shared libraries...");
        System.loadLibrary("android");
        System.loadLibrary("compiler_rt");
        System.loadLibrary("jnigraphics");
        try {
            System.loadLibrary("qti_performance");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Couldn't load qti_performance");
        }
    }

    private static void maybePreloadGraphicsDriver() {
        if (!SystemProperties.getBoolean(PROPERTY_DISABLE_GRAPHICS_DRIVER_PRELOADING, false)) {
            nativePreloadGraphicsDriver();
        }
    }

    private static void preloadTextResources() {
        Hyphenator.init();
        TextView.preloadFontCache();
    }

    private static void warmUpJcaProviders() {
        Provider[] providers;
        long startTime = SystemClock.uptimeMillis();
        Trace.traceBegin(16384L, "Starting installation of AndroidKeyStoreProvider");
        AndroidKeyStoreProvider.install();
        Log.i(TAG, "Installed AndroidKeyStoreProvider in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
        Trace.traceEnd(16384L);
        long startTime2 = SystemClock.uptimeMillis();
        Trace.traceBegin(16384L, "Starting warm up of JCA providers");
        for (Provider p : Security.getProviders()) {
            p.warmUpServiceProvision();
        }
        Log.i(TAG, "Warmed up JCA providers in " + (SystemClock.uptimeMillis() - startTime2) + "ms.");
        Trace.traceEnd(16384L);
    }

    /* JADX WARN: Removed duplicated region for block: B:86:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0241  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void preloadClasses() {
        String str;
        String prop;
        String str2 = "Failed to restore root";
        String str3 = "ResetJitCounters";
        VMRuntime runtime = VMRuntime.getRuntime();
        try {
            InputStream is = new FileInputStream(PRELOADED_CLASSES);
            Log.i(TAG, "Preloading classes...");
            long startTime = SystemClock.uptimeMillis();
            int reuid = Os.getuid();
            int regid = Os.getgid();
            boolean droppedPriviliges = false;
            if (reuid == 0 && regid == 0) {
                try {
                    Os.setregid(0, 9999);
                    Os.setreuid(0, 9999);
                    droppedPriviliges = true;
                } catch (ErrnoException ex) {
                    throw new RuntimeException("Failed to drop root", ex);
                }
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is), 256);
                int missingLambdaCount = 0;
                int count = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    String line2 = line.trim();
                    BufferedReader br2 = br;
                    if (line2.startsWith("#") || line2.equals("")) {
                        br = br2;
                        str2 = str2;
                        str3 = str3;
                    } else {
                        String str4 = str2;
                        String str5 = str3;
                        try {
                            Trace.traceBegin(16384L, line2);
                            try {
                                Class.forName(line2, true, null);
                                count++;
                            } catch (ClassNotFoundException e) {
                                if (!line2.contains("$$Lambda$")) {
                                    Log.w(TAG, "Class not found for preloading: " + line2);
                                } else if (LOGGING_DEBUG) {
                                    missingLambdaCount++;
                                }
                            } catch (UnsatisfiedLinkError e2) {
                                Log.w(TAG, "Problem preloading " + line2 + ": " + e2);
                            } catch (Throwable t) {
                                Log.e(TAG, "Error preloading " + line2 + MediaMetrics.SEPARATOR, t);
                                if (t instanceof Error) {
                                    throw ((Error) t);
                                }
                                if (!(t instanceof RuntimeException)) {
                                    throw new RuntimeException(t);
                                }
                                throw ((RuntimeException) t);
                            }
                            Trace.traceEnd(16384L);
                            br = br2;
                            str2 = str4;
                            str3 = str5;
                        } catch (IOException e3) {
                            e = e3;
                            str = str4;
                            str3 = str5;
                            try {
                                Log.e(TAG, "Error reading /system/etc/preloaded-classes.", e);
                                IoUtils.closeQuietly(is);
                                Trace.traceBegin(16384L, "PreloadDexCaches");
                                runtime.preloadDexCaches();
                                Trace.traceEnd(16384L);
                                String prop2 = SystemProperties.get("persist.device_config.runtime_native_boot.profilebootclasspath", "");
                                if ("true".equals(prop2.length() == 0 ? SystemProperties.get("dalvik.vm.profilebootclasspath", "") : prop2)) {
                                    Trace.traceBegin(16384L, str3);
                                    VMRuntime.resetJitCounters();
                                    Trace.traceEnd(16384L);
                                }
                                if (!droppedPriviliges) {
                                    return;
                                }
                                try {
                                    Os.setreuid(0, 0);
                                    Os.setregid(0, 0);
                                    return;
                                } catch (ErrnoException ex2) {
                                    throw new RuntimeException(str, ex2);
                                }
                            } catch (Throwable th) {
                                ex = th;
                                IoUtils.closeQuietly(is);
                                Trace.traceBegin(16384L, "PreloadDexCaches");
                                runtime.preloadDexCaches();
                                Trace.traceEnd(16384L);
                                prop = SystemProperties.get("persist.device_config.runtime_native_boot.profilebootclasspath", "");
                                if (prop.length() == 0) {
                                    prop = SystemProperties.get("dalvik.vm.profilebootclasspath", "");
                                }
                                if ("true".equals(prop)) {
                                    Trace.traceBegin(16384L, str3);
                                    VMRuntime.resetJitCounters();
                                    Trace.traceEnd(16384L);
                                }
                                if (droppedPriviliges) {
                                    try {
                                        Os.setreuid(0, 0);
                                        Os.setregid(0, 0);
                                    } catch (ErrnoException ex3) {
                                        throw new RuntimeException(str, ex3);
                                    }
                                }
                                throw ex;
                            }
                        } catch (Throwable th2) {
                            ex = th2;
                            str = str4;
                            str3 = str5;
                            IoUtils.closeQuietly(is);
                            Trace.traceBegin(16384L, "PreloadDexCaches");
                            runtime.preloadDexCaches();
                            Trace.traceEnd(16384L);
                            prop = SystemProperties.get("persist.device_config.runtime_native_boot.profilebootclasspath", "");
                            if (prop.length() == 0) {
                            }
                            if ("true".equals(prop)) {
                            }
                            if (droppedPriviliges) {
                            }
                            throw ex;
                        }
                    }
                }
                String str6 = str2;
                String str7 = str3;
                Log.i(TAG, "...preloaded " + count + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
                if (LOGGING_DEBUG && missingLambdaCount != 0) {
                    Log.i(TAG, "Unresolved lambda preloads: " + missingLambdaCount);
                }
                IoUtils.closeQuietly(is);
                Trace.traceBegin(16384L, "PreloadDexCaches");
                runtime.preloadDexCaches();
                Trace.traceEnd(16384L);
                String prop3 = SystemProperties.get("persist.device_config.runtime_native_boot.profilebootclasspath", "");
                if ("true".equals(prop3.length() == 0 ? SystemProperties.get("dalvik.vm.profilebootclasspath", "") : prop3)) {
                    Trace.traceBegin(16384L, str7);
                    VMRuntime.resetJitCounters();
                    Trace.traceEnd(16384L);
                }
                if (!droppedPriviliges) {
                    return;
                }
                try {
                    Os.setreuid(0, 0);
                    Os.setregid(0, 0);
                } catch (ErrnoException ex4) {
                    throw new RuntimeException(str6, ex4);
                }
            } catch (IOException e4) {
                e = e4;
                str = str2;
            } catch (Throwable th3) {
                ex = th3;
                str = str2;
            }
        } catch (FileNotFoundException e5) {
            Log.e(TAG, "Couldn't find /system/etc/preloaded-classes.");
        }
    }

    private static void cacheNonBootClasspathClassLoaders() {
        SharedLibraryInfo hidlBase = new SharedLibraryInfo("/system/framework/android.hidl.base-V1.0-java.jar", null, null, null, 0L, 0, null, null, null, false);
        SharedLibraryInfo hidlManager = new SharedLibraryInfo("/system/framework/android.hidl.manager-V1.0-java.jar", null, null, null, 0L, 0, null, null, null, false);
        SharedLibraryInfo androidTestBase = new SharedLibraryInfo("/system/framework/android.test.base.jar", null, null, null, 0L, 0, null, null, null, false);
        ApplicationLoaders.getDefault().createAndCacheNonBootclasspathSystemClassLoaders(new SharedLibraryInfo[]{hidlBase, hidlManager, androidTestBase});
    }

    private static void preloadResources() {
        try {
            Resources system = Resources.getSystem();
            mResources = system;
            system.startPreloading();
            Log.i(TAG, "Preloading resources...");
            long startTime = SystemClock.uptimeMillis();
            TypedArray ar = mResources.obtainTypedArray(R.array.preloaded_drawables);
            int N = preloadDrawables(ar);
            ar.recycle();
            Log.i(TAG, "...preloaded " + N + " resources in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
            long startTime2 = SystemClock.uptimeMillis();
            TypedArray ar2 = mResources.obtainTypedArray(R.array.preloaded_color_state_lists);
            int N2 = preloadColorStateLists(ar2);
            ar2.recycle();
            Log.i(TAG, "...preloaded " + N2 + " resources in " + (SystemClock.uptimeMillis() - startTime2) + "ms.");
            if (mResources.getBoolean(R.bool.config_freeformWindowManagement)) {
                long startTime3 = SystemClock.uptimeMillis();
                TypedArray ar3 = mResources.obtainTypedArray(R.array.preloaded_freeform_multi_window_drawables);
                int N3 = preloadDrawables(ar3);
                ar3.recycle();
                Log.i(TAG, "...preloaded " + N3 + " resource in " + (SystemClock.uptimeMillis() - startTime3) + "ms.");
            }
            mResources.finishPreloading();
        } catch (RuntimeException e) {
            Log.w(TAG, "Failure preloading resources", e);
        }
    }

    private static int preloadColorStateLists(TypedArray ar) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getColorStateList(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded color resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    private static int preloadDrawables(TypedArray ar) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getDrawable(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded drawable resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    private static void gcAndFinalize() {
        ZygoteHooks.gcAndFinalize();
    }

    private static boolean shouldProfileSystemServer() {
        boolean defaultValue = SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false);
        return SystemProperties.getBoolean("persist.device_config.runtime_native_boot.profilesystemserver", defaultValue);
    }

    private static Runnable handleSystemServerProcess(ZygoteArguments parsedArgs) {
        Os.umask(OsConstants.S_IRWXG | OsConstants.S_IRWXO);
        if (parsedArgs.mNiceName != null) {
            Process.setArgV0(parsedArgs.mNiceName);
        }
        String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
        if (systemServerClasspath != null) {
            performSystemServerDexOpt(systemServerClasspath);
            if (shouldProfileSystemServer() && (Build.IS_USERDEBUG || Build.IS_ENG)) {
                try {
                    Log.d(TAG, "Preparing system server profile");
                    prepareSystemServerProfile(systemServerClasspath);
                } catch (Exception e) {
                    Log.wtf(TAG, "Failed to set up system server profile", e);
                }
            }
        }
        if (parsedArgs.mInvokeWith != null) {
            String[] args = parsedArgs.mRemainingArgs;
            if (systemServerClasspath != null) {
                String[] amendedArgs = new String[args.length + 2];
                amendedArgs[0] = "-cp";
                amendedArgs[1] = systemServerClasspath;
                System.arraycopy(args, 0, amendedArgs, 2, args.length);
                args = amendedArgs;
            }
            WrapperInit.execApplication(parsedArgs.mInvokeWith, parsedArgs.mNiceName, parsedArgs.mTargetSdkVersion, VMRuntime.getCurrentInstructionSet(), null, args);
            throw new IllegalStateException("Unexpected return from WrapperInit.execApplication");
        }
        ClassLoader cl = getOrCreateSystemServerClassLoader();
        if (cl != null) {
            Thread.currentThread().setContextClassLoader(cl);
        }
        return zygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mDisabledCompatChanges, parsedArgs.mRemainingArgs, cl);
    }

    private static ClassLoader getOrCreateSystemServerClassLoader() {
        String systemServerClasspath;
        if (sCachedSystemServerClassLoader == null && (systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH")) != null) {
            sCachedSystemServerClassLoader = createPathClassLoader(systemServerClasspath, 10000);
        }
        return sCachedSystemServerClassLoader;
    }

    private static void prepareSystemServerProfile(String systemServerClasspath) throws RemoteException {
        if (systemServerClasspath.isEmpty()) {
            return;
        }
        String[] codePaths = systemServerClasspath.split(SettingsStringUtil.DELIMITER);
        IInstalld installd = IInstalld.Stub.asInterface(ServiceManager.getService("installd"));
        installd.prepareAppProfile("android", 0, UserHandle.getAppId(1000), "primary.prof", codePaths[0], null);
        File curProfileDir = Environment.getDataProfilesDePackageDirectory(0, "android");
        String curProfilePath = new File(curProfileDir, "primary.prof").getAbsolutePath();
        File refProfileDir = Environment.getDataProfilesDePackageDirectory(0, "android");
        String refProfilePath = new File(refProfileDir, "primary.prof").getAbsolutePath();
        VMRuntime.registerAppInfo("android", curProfilePath, refProfilePath, codePaths, 1);
    }

    public static void setApiDenylistExemptions(String[] exemptions) {
        VMRuntime.getRuntime().setHiddenApiExemptions(exemptions);
    }

    public static void setHiddenApiAccessLogSampleRate(int percent) {
        VMRuntime.getRuntime().setHiddenApiAccessLogSamplingRate(percent);
    }

    public static void setHiddenApiUsageLogger(VMRuntime.HiddenApiUsageLogger logger) {
        VMRuntime.getRuntime();
        VMRuntime.setHiddenApiUsageLogger(logger);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader createPathClassLoader(String classPath, int targetSdkVersion) {
        String libraryPath = System.getProperty("java.library.path");
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        return ClassLoaderFactory.createClassLoader(classPath, libraryPath, libraryPath, parent, targetSdkVersion, true, null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:16:0x009c
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:82)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:40)
        */
    private static void performSystemServerDexOpt(java.lang.String r36) {
        /*
            Method dump skipped, instructions count: 240
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteInit.performSystemServerDexOpt(java.lang.String):void");
    }

    private static String getSystemServerClassLoaderContext(String classPath) {
        if (classPath == null) {
            return "PCL[]";
        }
        return "PCL[" + classPath + "]";
    }

    private static String encodeSystemServerClassPath(String classPath, String newElement) {
        if (classPath == null || classPath.isEmpty()) {
            return newElement;
        }
        return classPath + SettingsStringUtil.DELIMITER + newElement;
    }

    private static Runnable forkSystemServer(String abiList, String socketName, ZygoteServer zygoteServer) {
        long capabilities = posixCapabilitiesAsBits(OsConstants.CAP_IPC_LOCK, OsConstants.CAP_KILL, OsConstants.CAP_NET_ADMIN, OsConstants.CAP_NET_BIND_SERVICE, OsConstants.CAP_NET_BROADCAST, OsConstants.CAP_NET_RAW, OsConstants.CAP_SYS_MODULE, OsConstants.CAP_SYS_NICE, OsConstants.CAP_SYS_PTRACE, OsConstants.CAP_SYS_TIME, OsConstants.CAP_SYS_TTY_CONFIG, OsConstants.CAP_WAKE_ALARM, OsConstants.CAP_BLOCK_SUSPEND);
        StructCapUserHeader header = new StructCapUserHeader(OsConstants._LINUX_CAPABILITY_VERSION_3, 0);
        try {
            StructCapUserData[] data = Os.capget(header);
            long capabilities2 = capabilities & ((data[1].effective << 32) | data[0].effective);
            String[] args = {"--setuid=1000", "--setgid=1000", "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,1021,1023,1024,1032,1065,3001,3002,3003,3006,3007,3009,3010,3011", "--capabilities=" + capabilities2 + "," + capabilities2, "--nice-name=system_server", "--runtime-args", "--target-sdk-version=10000", "com.android.server.SystemServer"};
            try {
                ZygoteCommandBuffer commandBuffer = new ZygoteCommandBuffer(args);
                try {
                    ZygoteArguments parsedArgs = ZygoteArguments.getInstance(commandBuffer);
                    commandBuffer.close();
                    Zygote.applyDebuggerSystemProperty(parsedArgs);
                    Zygote.applyInvokeWithSystemProperty(parsedArgs);
                    if (Zygote.nativeSupportsMemoryTagging()) {
                        String mode = SystemProperties.get("arm64.memtag.process.system_server", "async");
                        if (mode.equals("async")) {
                            parsedArgs.mRuntimeFlags |= 1048576;
                        } else if (mode.equals("sync")) {
                            parsedArgs.mRuntimeFlags |= 1572864;
                        } else if (!mode.equals("off")) {
                            parsedArgs.mRuntimeFlags |= Zygote.nativeCurrentTaggingLevel();
                            Slog.e(TAG, "Unknown memory tag level for the system server: \"" + mode + "\"");
                        }
                    } else if (Zygote.nativeSupportsTaggedPointers()) {
                        parsedArgs.mRuntimeFlags |= 524288;
                    }
                    parsedArgs.mRuntimeFlags |= 2097152;
                    if (shouldProfileSystemServer()) {
                        parsedArgs.mRuntimeFlags |= 16384;
                    }
                    int pid = Zygote.forkSystemServer(parsedArgs.mUid, parsedArgs.mGid, parsedArgs.mGids, parsedArgs.mRuntimeFlags, null, parsedArgs.mPermittedCapabilities, parsedArgs.mEffectiveCapabilities);
                    if (pid == 0) {
                        if (hasSecondZygote(abiList)) {
                            waitForSecondaryZygote(socketName);
                        }
                        zygoteServer.closeServerSocket();
                        return handleSystemServerProcess(parsedArgs);
                    }
                    return null;
                } catch (EOFException e) {
                    throw new AssertionError("Unexpected argument error for forking system server", e);
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ErrnoException ex2) {
            throw new RuntimeException("Failed to capget()", ex2);
        }
    }

    private static long posixCapabilitiesAsBits(int... capabilities) {
        long result = 0;
        for (int capability : capabilities) {
            if (capability < 0 || capability > OsConstants.CAP_LAST_CAP) {
                throw new IllegalArgumentException(String.valueOf(capability));
            }
            result |= 1 << capability;
        }
        return result;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:68:0x002a
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:82)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:40)
        */
    public static void main(java.lang.String[] r18) {
        /*
            Method dump skipped, instructions count: 355
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteInit.main(java.lang.String[]):void");
    }

    private static boolean hasSecondZygote(String abiList) {
        return !SystemProperties.get("ro.product.cpu.abilist").equals(abiList);
    }

    private static void waitForSecondaryZygote(String socketName) {
        String otherZygoteName = Zygote.PRIMARY_SOCKET_NAME;
        if (otherZygoteName.equals(socketName)) {
            otherZygoteName = Zygote.SECONDARY_SOCKET_NAME;
        }
        ZygoteProcess.waitForConnectionToZygote(otherZygoteName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isPreloadComplete() {
        return sPreloadComplete;
    }

    private ZygoteInit() {
    }

    public static Runnable zygoteInit(int targetSdkVersion, long[] disabledCompatChanges, String[] argv, ClassLoader classLoader) {
        Trace.traceBegin(64L, "ZygoteInit");
        RuntimeInit.redirectLogStreams();
        RuntimeInit.commonInit();
        nativeZygoteInit();
        return RuntimeInit.applicationInit(targetSdkVersion, disabledCompatChanges, argv, classLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Runnable childZygoteInit(String[] argv) {
        RuntimeInit.Arguments args = new RuntimeInit.Arguments(argv);
        return RuntimeInit.findStaticMain(args.startClass, args.startArgs, null);
    }
}
