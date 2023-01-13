package dalvik.system;

import android.annotation.SystemApi;
import android.system.ErrnoException;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import dalvik.system.DexPathList;
import java.nio.ByteBuffer;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import libcore.p030io.Libcore;

@Deprecated
public final class DexFile {
    public static final int DEX2OAT_FOR_BOOT_IMAGE = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int DEX2OAT_FOR_FILTER = 3;
    public static final int DEX2OAT_FROM_SCRATCH = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int NO_DEXOPT_NEEDED = 0;
    /* access modifiers changed from: private */
    public Object mCookie;
    private final String mFileName;
    private Object mInternalCookie;

    private static native boolean closeDexFile(Object obj);

    private static native Class defineClassNative(String str, ClassLoader classLoader, Object obj, DexFile dexFile) throws ClassNotFoundException, NoClassDefFoundError;

    /* access modifiers changed from: private */
    public static native String[] getClassNameList(Object obj);

    private static native String[] getDexFileOptimizationStatus(String str, String str2) throws FileNotFoundException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native String[] getDexFileOutputPaths(String str, String str2) throws FileNotFoundException;

    public static native String getDexFileStatus(String str, String str2) throws FileNotFoundException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native int getDexOptNeeded(String str, String str2, String str3, String str4, boolean z, boolean z2) throws FileNotFoundException, IOException;

    public static native String getNonProfileGuidedCompilerFilter(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native String getSafeModeCompilerFilter(String str);

    private static native long getStaticSizeOfDexFile(Object obj);

    private static native boolean isBackedByOatFile(Object obj);

    public static native boolean isDexOptNeeded(String str) throws FileNotFoundException, IOException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native boolean isProfileGuidedCompilerFilter(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native boolean isValidCompilerFilter(String str);

    private static native Object openDexFileNative(String str, String str2, int i, ClassLoader classLoader, DexPathList.Element[] elementArr);

    private static native Object openInMemoryDexFilesNative(ByteBuffer[] byteBufferArr, byte[][] bArr, int[] iArr, int[] iArr2, ClassLoader classLoader, DexPathList.Element[] elementArr);

    private static native void setTrusted(Object obj);

    private static native void verifyInBackgroundNative(Object obj, ClassLoader classLoader);

    @Deprecated
    public DexFile(File file) throws IOException {
        this(file.getPath());
    }

    DexFile(File file, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        this(file.getPath(), loader, elements);
    }

    @Deprecated
    public DexFile(String fileName) throws IOException {
        this(fileName, (ClassLoader) null, (DexPathList.Element[]) null);
    }

    DexFile(String fileName, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        Object openDexFile = openDexFile(fileName, (String) null, 0, loader, elements);
        this.mCookie = openDexFile;
        this.mInternalCookie = openDexFile;
        this.mFileName = fileName;
    }

    DexFile(ByteBuffer[] bufs, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        Object openInMemoryDexFiles = openInMemoryDexFiles(bufs, loader, elements);
        this.mCookie = openInMemoryDexFiles;
        this.mInternalCookie = openInMemoryDexFiles;
        this.mFileName = null;
    }

    private DexFile(String sourceName, String outputName, int flags, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        if (outputName != null) {
            try {
                String parent = new File(outputName).getParent();
                if (Libcore.f855os.getuid() != Libcore.f855os.stat(parent).st_uid) {
                    throw new IllegalArgumentException("Optimized data directory " + parent + " is not owned by the current user. Shared storage cannot protect your application from code injection attacks.");
                }
            } catch (ErrnoException e) {
            }
        }
        Object openDexFile = openDexFile(sourceName, outputName, flags, loader, elements);
        this.mCookie = openDexFile;
        this.mInternalCookie = openDexFile;
        this.mFileName = sourceName;
    }

    @Deprecated
    public static DexFile loadDex(String sourcePathName, String outputPathName, int flags) throws IOException {
        return loadDex(sourcePathName, outputPathName, flags, (ClassLoader) null, (DexPathList.Element[]) null);
    }

    static DexFile loadDex(String sourcePathName, String outputPathName, int flags, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        return new DexFile(sourcePathName, outputPathName, flags, loader, elements);
    }

    public String getName() {
        return this.mFileName;
    }

    public String toString() {
        if (this.mFileName != null) {
            return getName();
        }
        return "InMemoryDexFile[cookie=" + Arrays.toString((long[]) this.mCookie) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void close() throws IOException {
        Object obj = this.mInternalCookie;
        if (obj != null) {
            if (closeDexFile(obj)) {
                this.mInternalCookie = null;
            }
            this.mCookie = null;
        }
    }

    public Class loadClass(String name, ClassLoader loader) {
        return loadClassBinaryName(name.replace('.', '/'), loader, (List<Throwable>) null);
    }

    public Class loadClassBinaryName(String name, ClassLoader loader, List<Throwable> list) {
        return defineClass(name, loader, this.mCookie, this, list);
    }

    private static Class defineClass(String name, ClassLoader loader, Object cookie, DexFile dexFile, List<Throwable> list) {
        try {
            return defineClassNative(name, loader, cookie, dexFile);
        } catch (NoClassDefFoundError e) {
            if (list == null) {
                return null;
            }
            list.add(e);
            return null;
        } catch (ClassNotFoundException e2) {
            if (list == null) {
                return null;
            }
            list.add(e2);
            return null;
        }
    }

    public Enumeration<String> entries() {
        return new DFEnum(this);
    }

    private static class DFEnum implements Enumeration<String> {
        private int mIndex = 0;
        private String[] mNameList;

        DFEnum(DexFile dexFile) {
            this.mNameList = DexFile.getClassNameList(dexFile.mCookie);
        }

        public boolean hasMoreElements() {
            return this.mIndex < this.mNameList.length;
        }

        public String nextElement() {
            String[] strArr = this.mNameList;
            int i = this.mIndex;
            this.mIndex = i + 1;
            return strArr[i];
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            Object obj = this.mInternalCookie;
            if (obj != null) {
                if (!closeDexFile(obj)) {
                    throw new AssertionError((Object) "Failed to close dex file in finalizer.");
                }
            }
            this.mInternalCookie = null;
            this.mCookie = null;
        } finally {
            super.finalize();
        }
    }

    private static Object openDexFile(String sourceName, String outputName, int flags, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        String str;
        String absolutePath = new File(sourceName).getAbsolutePath();
        if (outputName == null) {
            str = null;
        } else {
            str = new File(outputName).getAbsolutePath();
        }
        return openDexFileNative(absolutePath, str, flags, loader, elements);
    }

    private static Object openInMemoryDexFiles(ByteBuffer[] bufs, ClassLoader loader, DexPathList.Element[] elements) throws IOException {
        byte[][] bArr = new byte[bufs.length][];
        int[] iArr = new int[bufs.length];
        int[] iArr2 = new int[bufs.length];
        for (int i = 0; i < bufs.length; i++) {
            bArr[i] = bufs[i].isDirect() ? null : bufs[i].array();
            iArr[i] = bufs[i].position();
            iArr2[i] = bufs[i].limit();
        }
        return openInMemoryDexFilesNative(bufs, bArr, iArr, iArr2, loader, elements);
    }

    /* access modifiers changed from: package-private */
    public void verifyInBackground(ClassLoader classLoader) {
        verifyInBackgroundNative(this.mCookie, classLoader);
    }

    /* access modifiers changed from: package-private */
    public boolean isBackedByOatFile() {
        return isBackedByOatFile(this.mCookie);
    }

    /* access modifiers changed from: package-private */
    public void setTrusted() {
        setTrusted(this.mCookie);
    }

    public static int getDexOptNeeded(String fileName, String instructionSet, String compilerFilter, boolean newProfile, boolean downgrade) throws FileNotFoundException, IOException {
        return getDexOptNeeded(fileName, instructionSet, compilerFilter, (String) null, newProfile, downgrade);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final class OptimizationInfo {
        private final String reason;
        private final String status;

        private OptimizationInfo(String str, String str2) {
            this.status = str;
            this.reason = str2;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public String getStatus() {
            return this.status;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public String getReason() {
            return this.reason;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static OptimizationInfo getDexFileOptimizationInfo(String fileName, String instructionSet) throws FileNotFoundException {
        String[] dexFileOptimizationStatus = getDexFileOptimizationStatus(fileName, instructionSet);
        return new OptimizationInfo(dexFileOptimizationStatus[0], dexFileOptimizationStatus[1]);
    }

    public long getStaticSizeOfDexFile() {
        return getStaticSizeOfDexFile(this.mCookie);
    }
}
