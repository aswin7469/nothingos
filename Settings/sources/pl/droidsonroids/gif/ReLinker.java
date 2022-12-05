package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/* loaded from: classes2.dex */
class ReLinker {
    private static final String MAPPED_BASE_LIB_NAME = System.mapLibraryName("pl_droidsonroids_gif");

    private ReLinker() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public static void loadLibrary(Context context) {
        synchronized (ReLinker.class) {
            System.load(unpackLibrary(context).getAbsolutePath());
        }
    }

    private static File unpackLibrary(Context context) {
        ZipFile zipFile;
        Throwable th;
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        Throwable th2;
        String str = MAPPED_BASE_LIB_NAME + "1.2.15";
        int i = 0;
        File file = new File(context.getDir("lib", 0), str);
        if (file.isFile()) {
            return file;
        }
        File file2 = new File(context.getCacheDir(), str);
        if (file2.isFile()) {
            return file2;
        }
        final String mapLibraryName = System.mapLibraryName("pl_droidsonroids_gif_surface");
        FilenameFilter filenameFilter = new FilenameFilter() { // from class: pl.droidsonroids.gif.ReLinker.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file3, String str2) {
                return str2.startsWith(ReLinker.MAPPED_BASE_LIB_NAME) || str2.startsWith(mapLibraryName);
            }
        };
        clearOldLibraryFiles(file, filenameFilter);
        clearOldLibraryFiles(file2, filenameFilter);
        try {
            zipFile = openZipFile(new File(context.getApplicationInfo().sourceDir));
            while (true) {
                int i2 = i + 1;
                if (i >= 5) {
                    break;
                }
                try {
                    ZipEntry findLibraryEntry = findLibraryEntry(zipFile);
                    if (findLibraryEntry == null) {
                        throw new IllegalStateException("Library " + MAPPED_BASE_LIB_NAME + " for supported ABIs not found in APK file");
                    }
                    try {
                        inputStream = zipFile.getInputStream(findLibraryEntry);
                    } catch (IOException unused) {
                        inputStream = null;
                        fileOutputStream = null;
                    } catch (Throwable th3) {
                        th = th3;
                        inputStream = null;
                        fileOutputStream = null;
                    }
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            copy(inputStream, fileOutputStream);
                            closeSilently(inputStream);
                            closeSilently(fileOutputStream);
                            setFilePermissions(file);
                            break;
                        } catch (IOException unused2) {
                            if (i2 > 2) {
                                file = file2;
                            }
                            closeSilently(inputStream);
                            closeSilently(fileOutputStream);
                            i = i2;
                        } catch (Throwable th4) {
                            th2 = th4;
                            closeSilently(inputStream);
                            closeSilently(fileOutputStream);
                            throw th2;
                        }
                    } catch (IOException unused3) {
                        fileOutputStream = null;
                    } catch (Throwable th5) {
                        th = th5;
                        fileOutputStream = null;
                        th2 = th;
                        closeSilently(inputStream);
                        closeSilently(fileOutputStream);
                        throw th2;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException unused4) {
                        }
                    }
                    throw th;
                }
                closeSilently(inputStream);
                closeSilently(fileOutputStream);
                i = i2;
            }
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException unused5) {
                }
            }
            return file;
        } catch (Throwable th7) {
            zipFile = null;
            th = th7;
        }
    }

    private static ZipEntry findLibraryEntry(ZipFile zipFile) {
        for (String str : getSupportedABIs()) {
            ZipEntry entry = getEntry(zipFile, str);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    private static String[] getSupportedABIs() {
        return Build.VERSION.SDK_INT >= 21 ? Build.SUPPORTED_ABIS : new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    private static ZipEntry getEntry(ZipFile zipFile, String str) {
        return zipFile.getEntry("lib/" + str + "/" + MAPPED_BASE_LIB_NAME);
    }

    private static ZipFile openZipFile(File file) {
        ZipFile zipFile;
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i >= 5) {
                zipFile = null;
                break;
            }
            try {
                zipFile = new ZipFile(file, 1);
                break;
            } catch (IOException unused) {
                i = i2;
            }
        }
        if (zipFile != null) {
            return zipFile;
        }
        throw new IllegalStateException("Could not open APK file: " + file.getAbsolutePath());
    }

    private static void clearOldLibraryFiles(File file, FilenameFilter filenameFilter) {
        File[] listFiles = file.getParentFile().listFiles(filenameFilter);
        if (listFiles != null) {
            for (File file2 : listFiles) {
                file2.delete();
            }
        }
    }

    @SuppressLint({"SetWorldReadable"})
    private static void setFilePermissions(File file) {
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true);
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return;
            }
            outputStream.write(bArr, 0, read);
        }
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
