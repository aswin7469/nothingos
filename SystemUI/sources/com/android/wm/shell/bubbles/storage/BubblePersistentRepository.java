package com.android.wm.shell.bubbles.storage;

import android.content.Context;
import android.util.AtomicFile;
import android.util.Log;
import android.util.SparseArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: BubblePersistentRepository.kt */
/* loaded from: classes2.dex */
public final class BubblePersistentRepository {
    @NotNull
    private final AtomicFile bubbleFile;

    public BubblePersistentRepository(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.bubbleFile = new AtomicFile(new File(context.getFilesDir(), "overflow_bubbles.xml"), "overflow-bubbles");
    }

    public final boolean persistsToDisk(@NotNull SparseArray<List<BubbleEntity>> bubbles) {
        Intrinsics.checkNotNullParameter(bubbles, "bubbles");
        synchronized (this.bubbleFile) {
            try {
                FileOutputStream startWrite = this.bubbleFile.startWrite();
                Intrinsics.checkNotNullExpressionValue(startWrite, "{ bubbleFile.startWrite() }");
                try {
                    BubbleXmlHelperKt.writeXml(startWrite, bubbles);
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

    @NotNull
    public final SparseArray<List<BubbleEntity>> readFromDisk() {
        synchronized (this.bubbleFile) {
            if (!this.bubbleFile.exists()) {
                return new SparseArray<>();
            }
            th = null;
            try {
                return BubbleXmlHelperKt.readXml(this.bubbleFile.openRead());
            } finally {
            }
        }
    }
}
