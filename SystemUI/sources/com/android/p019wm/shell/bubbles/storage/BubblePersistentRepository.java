package com.android.p019wm.shell.bubbles.storage;

import android.content.Context;
import android.util.AtomicFile;
import android.util.Log;
import android.util.SparseArray;
import java.p026io.File;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nJ\u0012\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/storage/BubblePersistentRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "bubbleFile", "Landroid/util/AtomicFile;", "persistsToDisk", "", "bubbles", "Landroid/util/SparseArray;", "", "Lcom/android/wm/shell/bubbles/storage/BubbleEntity;", "readFromDisk", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.storage.BubblePersistentRepository */
/* compiled from: BubblePersistentRepository.kt */
public final class BubblePersistentRepository {
    private final AtomicFile bubbleFile;

    public BubblePersistentRepository(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.bubbleFile = new AtomicFile(new File(context.getFilesDir(), "overflow_bubbles.xml"), "overflow-bubbles");
    }

    public final boolean persistsToDisk(SparseArray<List<BubbleEntity>> sparseArray) {
        Intrinsics.checkNotNullParameter(sparseArray, "bubbles");
        synchronized (this.bubbleFile) {
            try {
                FileOutputStream startWrite = this.bubbleFile.startWrite();
                Intrinsics.checkNotNullExpressionValue(startWrite, "{ bubbleFile.startWrite() }");
                try {
                    BubbleXmlHelperKt.writeXml(startWrite, sparseArray);
                    this.bubbleFile.finishWrite(startWrite);
                } catch (Exception e) {
                    Log.e("BubblePersistentRepository", "Failed to save bubble file, restoring backup", e);
                    this.bubbleFile.failWrite(startWrite);
                    Unit unit = Unit.INSTANCE;
                    return false;
                }
            } catch (IOException e2) {
                Log.e("BubblePersistentRepository", "Failed to save bubble file", e2);
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0029, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        kotlin.p028io.CloseableKt.closeFinally(r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002d, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.util.SparseArray<java.util.List<com.android.p019wm.shell.bubbles.storage.BubbleEntity>> readFromDisk() {
        /*
            r3 = this;
            android.util.AtomicFile r0 = r3.bubbleFile
            monitor-enter(r0)
            android.util.AtomicFile r1 = r3.bubbleFile     // Catch:{ all -> 0x003d }
            boolean r1 = r1.exists()     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x0012
            android.util.SparseArray r3 = new android.util.SparseArray     // Catch:{ all -> 0x003d }
            r3.<init>()     // Catch:{ all -> 0x003d }
            monitor-exit(r0)
            return r3
        L_0x0012:
            android.util.AtomicFile r3 = r3.bubbleFile     // Catch:{ all -> 0x002e }
            java.io.FileInputStream r3 = r3.openRead()     // Catch:{ all -> 0x002e }
            java.io.Closeable r3 = (java.p026io.Closeable) r3     // Catch:{ all -> 0x002e }
            r1 = r3
            java.io.InputStream r1 = (java.p026io.InputStream) r1     // Catch:{ all -> 0x0027 }
            android.util.SparseArray r1 = com.android.p019wm.shell.bubbles.storage.BubbleXmlHelperKt.readXml(r1)     // Catch:{ all -> 0x0027 }
            r2 = 0
            kotlin.p028io.CloseableKt.closeFinally(r3, r2)     // Catch:{ all -> 0x002e }
            monitor-exit(r0)
            return r1
        L_0x0027:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0029 }
        L_0x0029:
            r2 = move-exception
            kotlin.p028io.CloseableKt.closeFinally(r3, r1)     // Catch:{ all -> 0x002e }
            throw r2     // Catch:{ all -> 0x002e }
        L_0x002e:
            r3 = move-exception
            java.lang.String r1 = "BubblePersistentRepository"
            java.lang.String r2 = "Failed to open bubble file"
            android.util.Log.e(r1, r2, r3)     // Catch:{ all -> 0x003d }
            android.util.SparseArray r3 = new android.util.SparseArray     // Catch:{ all -> 0x003d }
            r3.<init>()     // Catch:{ all -> 0x003d }
            monitor-exit(r0)
            return r3
        L_0x003d:
            r3 = move-exception
            monitor-exit(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.bubbles.storage.BubblePersistentRepository.readFromDisk():android.util.SparseArray");
    }
}
