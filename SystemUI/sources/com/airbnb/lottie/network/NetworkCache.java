package com.airbnb.lottie.network;

import com.airbnb.lottie.utils.Logger;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class NetworkCache {
    private final LottieNetworkCacheProvider cacheProvider;

    public NetworkCache(LottieNetworkCacheProvider lottieNetworkCacheProvider) {
        this.cacheProvider = lottieNetworkCacheProvider;
    }

    public void clear() {
        File parentDir = parentDir();
        if (parentDir.exists()) {
            File[] listFiles = parentDir.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File delete : parentDir.listFiles()) {
                    delete.delete();
                }
            }
            parentDir.delete();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<com.airbnb.lottie.network.FileExtension, java.p026io.InputStream> fetch(java.lang.String r5) {
        /*
            r4 = this;
            r0 = 0
            java.io.File r4 = r4.getCachedFile(r5)     // Catch:{ FileNotFoundException -> 0x0044 }
            if (r4 != 0) goto L_0x0008
            return r0
        L_0x0008:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{  }
            r1.<init>((java.p026io.File) r4)     // Catch:{  }
            java.lang.String r0 = r4.getAbsolutePath()
            java.lang.String r2 = ".zip"
            boolean r0 = r0.endsWith(r2)
            if (r0 == 0) goto L_0x001c
            com.airbnb.lottie.network.FileExtension r0 = com.airbnb.lottie.network.FileExtension.ZIP
            goto L_0x001e
        L_0x001c:
            com.airbnb.lottie.network.FileExtension r0 = com.airbnb.lottie.network.FileExtension.JSON
        L_0x001e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Cache hit for "
            r2.<init>((java.lang.String) r3)
            java.lang.StringBuilder r5 = r2.append((java.lang.String) r5)
            java.lang.String r2 = " at "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r2)
            java.lang.String r4 = r4.getAbsolutePath()
            java.lang.StringBuilder r4 = r5.append((java.lang.String) r4)
            java.lang.String r4 = r4.toString()
            com.airbnb.lottie.utils.Logger.debug(r4)
            android.util.Pair r4 = new android.util.Pair
            r4.<init>(r0, r1)
            return r4
        L_0x0044:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.network.NetworkCache.fetch(java.lang.String):android.util.Pair");
    }

    /* access modifiers changed from: package-private */
    public File writeTempCacheFile(String str, InputStream inputStream, FileExtension fileExtension) throws IOException {
        FileOutputStream fileOutputStream;
        File file = new File(parentDir(), filenameForUrl(str, fileExtension, true));
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    return file;
                }
            }
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void renameTempFile(String str, FileExtension fileExtension) {
        File file = new File(parentDir(), filenameForUrl(str, fileExtension, true));
        File file2 = new File(file.getAbsolutePath().replace((CharSequence) ".temp", (CharSequence) ""));
        boolean renameTo = file.renameTo(file2);
        Logger.debug("Copying temp file to real file (" + file2 + NavigationBarInflaterView.KEY_CODE_END);
        if (!renameTo) {
            Logger.warning("Unable to rename cache file " + file.getAbsolutePath() + " to " + file2.getAbsolutePath() + BaseIconCache.EMPTY_CLASS_NAME);
        }
    }

    private File getCachedFile(String str) throws FileNotFoundException {
        File file = new File(parentDir(), filenameForUrl(str, FileExtension.JSON, false));
        if (file.exists()) {
            return file;
        }
        File file2 = new File(parentDir(), filenameForUrl(str, FileExtension.ZIP, false));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }

    private File parentDir() {
        File cacheDir = this.cacheProvider.getCacheDir();
        if (cacheDir.isFile()) {
            cacheDir.delete();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    private static String filenameForUrl(String str, FileExtension fileExtension, boolean z) {
        return "lottie_cache_" + str.replaceAll("\\W+", "") + (z ? fileExtension.tempExtension() : fileExtension.extension);
    }
}
