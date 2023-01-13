package dalvik.system;

import android.system.ErrnoException;
import android.system.OsConstants;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.p026io.File;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import libcore.p030io.ClassPathURLStreamHandler;
import libcore.p030io.IoUtils;
import libcore.p030io.Libcore;

public final class DexPathList {
    private static final String DEX_SUFFIX = ".dex";
    private static final String zipSeparator = "!/";
    private final ClassLoader definingContext;
    private Element[] dexElements;
    private IOException[] dexElementsSuppressedExceptions;
    private final List<File> nativeLibraryDirectories;
    NativeLibraryElement[] nativeLibraryPathElements;
    private final List<File> systemNativeLibraryDirectories;

    static /* synthetic */ boolean lambda$initByteBufferDexPath$0(ByteBuffer byteBuffer) {
        return byteBuffer == null;
    }

    private List<File> getAllNativeLibraryDirectories() {
        ArrayList arrayList = new ArrayList(this.nativeLibraryDirectories);
        arrayList.addAll(this.systemNativeLibraryDirectories);
        return arrayList;
    }

    public DexPathList(ClassLoader classLoader, String str) {
        if (classLoader != null) {
            this.definingContext = classLoader;
            this.nativeLibraryDirectories = splitPaths(str, false);
            this.systemNativeLibraryDirectories = splitPaths(System.getProperty("java.library.path"), true);
            this.nativeLibraryPathElements = makePathElements(getAllNativeLibraryDirectories());
            return;
        }
        throw new NullPointerException("definingContext == null");
    }

    public DexPathList(ClassLoader classLoader, String str, String str2, File file) {
        this(classLoader, str, str2, file, false);
    }

    DexPathList(ClassLoader classLoader, String str, String str2, File file, boolean z) {
        if (classLoader == null) {
            throw new NullPointerException("definingContext == null");
        } else if (str != null) {
            if (file != null) {
                if (!file.exists()) {
                    throw new IllegalArgumentException("optimizedDirectory doesn't exist: " + file);
                } else if (!file.canRead() || !file.canWrite()) {
                    throw new IllegalArgumentException("optimizedDirectory not readable/writable: " + file);
                }
            }
            this.definingContext = classLoader;
            ArrayList arrayList = new ArrayList();
            this.dexElements = makeDexElements(splitDexPath(str), file, arrayList, classLoader, z);
            this.nativeLibraryDirectories = splitPaths(str2, false);
            this.systemNativeLibraryDirectories = splitPaths(System.getProperty("java.library.path"), true);
            this.nativeLibraryPathElements = makePathElements(getAllNativeLibraryDirectories());
            if (arrayList.size() > 0) {
                this.dexElementsSuppressedExceptions = (IOException[]) arrayList.toArray(new IOException[arrayList.size()]);
            } else {
                this.dexElementsSuppressedExceptions = null;
            }
        } else {
            throw new NullPointerException("dexPath == null");
        }
    }

    public String toString() {
        return "DexPathList[" + Arrays.toString((Object[]) this.dexElements) + ",nativeLibraryDirectories=" + Arrays.toString(getAllNativeLibraryDirectories().toArray()) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public List<File> getNativeLibraryDirectories() {
        return this.nativeLibraryDirectories;
    }

    public void addDexPath(String str, File file) {
        addDexPath(str, file, false);
    }

    public void addDexPath(String str, File file, boolean z) {
        ArrayList arrayList = new ArrayList();
        Element[] makeDexElements = makeDexElements(splitDexPath(str), file, arrayList, this.definingContext, z);
        if (makeDexElements != null && makeDexElements.length > 0) {
            this.dexElements = (Element[]) concat(Element.class, this.dexElements, makeDexElements);
        }
        if (arrayList.size() > 0) {
            IOException[] iOExceptionArr = (IOException[]) arrayList.toArray(new IOException[arrayList.size()]);
            IOException[] iOExceptionArr2 = this.dexElementsSuppressedExceptions;
            if (iOExceptionArr2 != null) {
                iOExceptionArr = (IOException[]) concat(IOException.class, iOExceptionArr2, iOExceptionArr);
            }
            this.dexElementsSuppressedExceptions = iOExceptionArr;
        }
    }

    private static <T> T[] concat(Class<T> cls, T[] tArr, T[] tArr2) {
        T[] tArr3 = (Object[]) Array.newInstance((Class<?>) cls, tArr.length + tArr2.length);
        System.arraycopy((Object) tArr, 0, (Object) tArr3, 0, tArr.length);
        System.arraycopy((Object) tArr2, 0, (Object) tArr3, tArr.length, tArr2.length);
        return tArr3;
    }

    /* access modifiers changed from: package-private */
    public void initByteBufferDexPath(ByteBuffer[] byteBufferArr) {
        if (byteBufferArr == null) {
            throw new NullPointerException("dexFiles == null");
        } else if (Arrays.stream((T[]) byteBufferArr).anyMatch(new DexPathList$$ExternalSyntheticLambda0())) {
            throw new NullPointerException("dexFiles contains a null Buffer!");
        } else if (this.dexElements == null && this.dexElementsSuppressedExceptions == null) {
            ArrayList arrayList = new ArrayList();
            try {
                this.dexElements = new Element[]{new Element(new DexFile(byteBufferArr, this.definingContext, (Element[]) null))};
            } catch (IOException e) {
                System.logE("Unable to load dex files", e);
                arrayList.add(e);
                this.dexElements = new Element[0];
            }
            if (arrayList.size() > 0) {
                this.dexElementsSuppressedExceptions = (IOException[]) arrayList.toArray(new IOException[arrayList.size()]);
            }
        } else {
            throw new IllegalStateException("Should only be called once");
        }
    }

    /* access modifiers changed from: package-private */
    public void maybeRunBackgroundVerification(ClassLoader classLoader) {
        for (Element element : this.dexElements) {
            if (element.dexFile != null && !element.dexFile.isBackedByOatFile()) {
                element.dexFile.verifyInBackground(classLoader);
            }
        }
    }

    private static List<File> splitDexPath(String str) {
        return splitPaths(str, false);
    }

    private static List<File> splitPaths(String str, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (str != null) {
            for (String str2 : str.split(File.pathSeparator)) {
                if (z) {
                    try {
                        if (!OsConstants.S_ISDIR(Libcore.f855os.stat(str2).st_mode)) {
                        }
                    } catch (ErrnoException unused) {
                    }
                }
                arrayList.add(new File(str2));
            }
        }
        return arrayList;
    }

    public static Element[] makeInMemoryDexElements(ByteBuffer[] byteBufferArr, List<IOException> list) {
        int i;
        IOException e;
        int length = byteBufferArr.length;
        Element[] elementArr = new Element[length];
        int i2 = 0;
        for (ByteBuffer byteBuffer : byteBufferArr) {
            try {
                i = i2 + 1;
                try {
                    elementArr[i2] = new Element(new DexFile(new ByteBuffer[]{byteBuffer}, (ClassLoader) null, (Element[]) null));
                } catch (IOException e2) {
                    e = e2;
                }
            } catch (IOException e3) {
                i = i2;
                e = e3;
                System.logE("Unable to load dex file: " + byteBuffer, e);
                list.add(e);
                i2 = i;
            }
            i2 = i;
        }
        return i2 != length ? (Element[]) Arrays.copyOf((T[]) elementArr, i2) : elementArr;
    }

    private static Element[] makeDexElements(List<File> list, File file, List<IOException> list2, ClassLoader classLoader) {
        return makeDexElements(list, file, list2, classLoader, false);
    }

    private static Element[] makeDexElements(List<File> list, File file, List<IOException> list2, ClassLoader classLoader, boolean z) {
        DexFile dexFile;
        int i;
        int i2;
        IOException e;
        int size = list.size();
        Element[] elementArr = new Element[size];
        int i3 = 0;
        for (File next : list) {
            if (next.isDirectory()) {
                elementArr[i3] = new Element(next);
                i3++;
            } else if (next.isFile()) {
                DexFile dexFile2 = null;
                if (next.getName().endsWith(DEX_SUFFIX)) {
                    try {
                        dexFile = loadDexFile(next, file, classLoader, elementArr);
                        if (dexFile != null) {
                            i2 = i3 + 1;
                            try {
                                elementArr[i3] = new Element(dexFile, (File) null);
                            } catch (IOException e2) {
                                e = e2;
                                dexFile2 = dexFile;
                            }
                            i3 = i2;
                        }
                    } catch (IOException e3) {
                        i2 = i3;
                        e = e3;
                        System.logE("Unable to load dex file: " + next, e);
                        list2.add(e);
                        dexFile = dexFile2;
                        i3 = i2;
                        dexFile.setTrusted();
                    }
                } else {
                    try {
                        dexFile2 = loadDexFile(next, file, classLoader, elementArr);
                    } catch (IOException e4) {
                        list2.add(e4);
                    }
                    if (dexFile2 == null) {
                        i = i3 + 1;
                        elementArr[i3] = new Element(next);
                    } else {
                        i = i3 + 1;
                        elementArr[i3] = new Element(dexFile2, next);
                    }
                    i3 = i;
                    dexFile = dexFile2;
                }
                if (dexFile != null && z) {
                    dexFile.setTrusted();
                }
            } else {
                System.logW("ClassLoader referenced unknown path: " + next);
            }
        }
        return i3 != size ? (Element[]) Arrays.copyOf((T[]) elementArr, i3) : elementArr;
    }

    private static DexFile loadDexFile(File file, File file2, ClassLoader classLoader, Element[] elementArr) throws IOException {
        if (file2 == null) {
            return new DexFile(file, classLoader, elementArr);
        }
        return DexFile.loadDex(file.getPath(), optimizedPathFor(file, file2), 0, classLoader, elementArr);
    }

    private static String optimizedPathFor(File file, File file2) {
        String name = file.getName();
        if (!name.endsWith(DEX_SUFFIX)) {
            int lastIndexOf = name.lastIndexOf(BaseIconCache.EMPTY_CLASS_NAME);
            if (lastIndexOf < 0) {
                name = name + DEX_SUFFIX;
            } else {
                StringBuilder sb = new StringBuilder(lastIndexOf + 4);
                sb.append((CharSequence) name, 0, lastIndexOf);
                sb.append(DEX_SUFFIX);
                name = sb.toString();
            }
        }
        return new File(file2, name).getPath();
    }

    private static Element[] makePathElements(List<File> list, File file, List<IOException> list2) {
        return makeDexElements(list, file, list2, (ClassLoader) null);
    }

    private static NativeLibraryElement[] makePathElements(List<File> list) {
        int size = list.size();
        NativeLibraryElement[] nativeLibraryElementArr = new NativeLibraryElement[size];
        int i = 0;
        for (File next : list) {
            String path = next.getPath();
            if (path.contains(zipSeparator)) {
                String[] split = path.split(zipSeparator, 2);
                nativeLibraryElementArr[i] = new NativeLibraryElement(new File(split[0]), split[1]);
                i++;
            } else if (next.isDirectory()) {
                nativeLibraryElementArr[i] = new NativeLibraryElement(next);
                i++;
            }
        }
        return i != size ? (NativeLibraryElement[]) Arrays.copyOf((T[]) nativeLibraryElementArr, i) : nativeLibraryElementArr;
    }

    public Class<?> findClass(String str, List<Throwable> list) {
        for (Element findClass : this.dexElements) {
            Class<?> findClass2 = findClass.findClass(str, this.definingContext, list);
            if (findClass2 != null) {
                return findClass2;
            }
        }
        IOException[] iOExceptionArr = this.dexElementsSuppressedExceptions;
        if (iOExceptionArr == null) {
            return null;
        }
        list.addAll(Arrays.asList(iOExceptionArr));
        return null;
    }

    public URL findResource(String str) {
        for (Element findResource : this.dexElements) {
            URL findResource2 = findResource.findResource(str);
            if (findResource2 != null) {
                return findResource2;
            }
        }
        return null;
    }

    public Enumeration<URL> findResources(String str) {
        ArrayList arrayList = new ArrayList();
        for (Element findResource : this.dexElements) {
            URL findResource2 = findResource.findResource(str);
            if (findResource2 != null) {
                arrayList.add(findResource2);
            }
        }
        return Collections.enumeration(arrayList);
    }

    public String findLibrary(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        for (NativeLibraryElement findNativeLibrary : this.nativeLibraryPathElements) {
            String findNativeLibrary2 = findNativeLibrary.findNativeLibrary(mapLibraryName);
            if (findNativeLibrary2 != null) {
                return findNativeLibrary2;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public List<String> getDexPaths() {
        ArrayList arrayList = new ArrayList();
        for (Element r3 : this.dexElements) {
            String r32 = r3.getDexPath();
            if (r32 != null) {
                arrayList.add(r32);
            }
        }
        return arrayList;
    }

    public void addNativePath(Collection<String> collection) {
        if (!collection.isEmpty()) {
            ArrayList arrayList = new ArrayList(collection.size());
            for (String file : collection) {
                arrayList.add(new File(file));
            }
            ArrayList arrayList2 = new ArrayList(this.nativeLibraryPathElements.length + collection.size());
            arrayList2.addAll(Arrays.asList(this.nativeLibraryPathElements));
            for (NativeLibraryElement nativeLibraryElement : makePathElements(arrayList)) {
                if (!arrayList2.contains(nativeLibraryElement)) {
                    arrayList2.add(nativeLibraryElement);
                }
            }
            this.nativeLibraryPathElements = (NativeLibraryElement[]) arrayList2.toArray(new NativeLibraryElement[arrayList2.size()]);
        }
    }

    static class Element {
        /* access modifiers changed from: private */
        public final DexFile dexFile;
        private boolean initialized;
        private final File path;
        private final Boolean pathIsDirectory;
        private ClassPathURLStreamHandler urlHandler;

        public Element(DexFile dexFile2, File file) {
            Boolean bool;
            if (dexFile2 == null && file == null) {
                throw new NullPointerException("Either dexFile or path must be non-null");
            }
            this.dexFile = dexFile2;
            this.path = file;
            if (file == null) {
                bool = null;
            } else {
                bool = Boolean.valueOf(file.isDirectory());
            }
            this.pathIsDirectory = bool;
        }

        public Element(DexFile dexFile2) {
            this(dexFile2, (File) null);
        }

        public Element(File file) {
            this((DexFile) null, file);
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        @Deprecated
        public Element(File file, boolean z, File file2, DexFile dexFile2) {
            this(file != null ? null : dexFile2, file != null ? file : file2);
            System.err.println("Warning: Using deprecated Element constructor. Do not use internal APIs, this constructor will be removed in the future.");
            if (file != null && (file2 != null || dexFile2 != null)) {
                throw new IllegalArgumentException("Using dir and zip|dexFile no longer supported.");
            } else if (!z) {
            } else {
                if (file2 != null || dexFile2 != null) {
                    throw new IllegalArgumentException("Unsupported argument combination.");
                }
            }
        }

        /* access modifiers changed from: private */
        public String getDexPath() {
            File file = this.path;
            if (file == null) {
                DexFile dexFile2 = this.dexFile;
                if (dexFile2 != null) {
                    return dexFile2.getName();
                }
                return null;
            } else if (file.isDirectory()) {
                return null;
            } else {
                return this.path.getAbsolutePath();
            }
        }

        public String toString() {
            String str = "zip file \"";
            if (this.dexFile == null) {
                StringBuilder sb = new StringBuilder();
                if (this.pathIsDirectory.booleanValue()) {
                    str = "directory \"";
                }
                sb.append(str);
                sb.append((Object) this.path);
                sb.append("\"");
                return sb.toString();
            } else if (this.path == null) {
                return "dex file \"" + this.dexFile + "\"";
            } else {
                return str + this.path + "\"";
            }
        }

        public synchronized void maybeInit() {
            if (!this.initialized) {
                if (this.path == null || this.pathIsDirectory.booleanValue()) {
                    this.initialized = true;
                    return;
                }
                try {
                    this.urlHandler = new ClassPathURLStreamHandler(this.path.getPath());
                } catch (IOException e) {
                    System.logE("Unable to open zip file: " + this.path, e);
                    this.urlHandler = null;
                }
                this.initialized = true;
            }
        }

        public Class<?> findClass(String str, ClassLoader classLoader, List<Throwable> list) {
            DexFile dexFile2 = this.dexFile;
            if (dexFile2 != null) {
                return dexFile2.loadClassBinaryName(str, classLoader, list);
            }
            return null;
        }

        public URL findResource(String str) {
            maybeInit();
            ClassPathURLStreamHandler classPathURLStreamHandler = this.urlHandler;
            if (classPathURLStreamHandler != null) {
                return classPathURLStreamHandler.getEntryUrlOrNull(str);
            }
            File file = this.path;
            if (file == null || !file.isDirectory()) {
                return null;
            }
            File file2 = new File(this.path, str);
            if (!file2.exists()) {
                return null;
            }
            try {
                return file2.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException((Throwable) e);
            }
        }
    }

    static class NativeLibraryElement {
        private boolean initialized;
        private final File path;
        private ClassPathURLStreamHandler urlHandler;
        private final String zipDir;

        public NativeLibraryElement(File file) {
            this.path = file;
            this.zipDir = null;
        }

        public NativeLibraryElement(File file, String str) {
            this.path = file;
            this.zipDir = str;
            if (str == null) {
                throw new IllegalArgumentException();
            }
        }

        public String toString() {
            String str;
            if (this.zipDir == null) {
                return "directory \"" + this.path + "\"";
            }
            StringBuilder sb = new StringBuilder("zip file \"");
            sb.append((Object) this.path);
            sb.append("\"");
            if (!this.zipDir.isEmpty()) {
                str = ", dir \"" + this.zipDir + "\"";
            } else {
                str = "";
            }
            sb.append(str);
            return sb.toString();
        }

        public synchronized void maybeInit() {
            if (!this.initialized) {
                if (this.zipDir == null) {
                    this.initialized = true;
                    return;
                }
                try {
                    this.urlHandler = new ClassPathURLStreamHandler(this.path.getPath());
                } catch (IOException e) {
                    System.logE("Unable to open zip file: " + this.path, e);
                    this.urlHandler = null;
                }
                this.initialized = true;
            }
        }

        public String findNativeLibrary(String str) {
            maybeInit();
            if (this.zipDir == null) {
                String path2 = new File(this.path, str).getPath();
                if (IoUtils.canOpenReadOnly(path2)) {
                    return path2;
                }
                return null;
            } else if (this.urlHandler == null) {
                return null;
            } else {
                String str2 = this.zipDir + '/' + str;
                if (!this.urlHandler.isEntryStored(str2)) {
                    return null;
                }
                return this.path.getPath() + DexPathList.zipSeparator + str2;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof NativeLibraryElement)) {
                return false;
            }
            NativeLibraryElement nativeLibraryElement = (NativeLibraryElement) obj;
            if (!Objects.equals(this.path, nativeLibraryElement.path) || !Objects.equals(this.zipDir, nativeLibraryElement.zipDir)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.path, this.zipDir);
        }
    }
}
