package com.android.keyguard;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: KeyguardListenQueue.kt */
/* loaded from: classes.dex */
public final class KeyguardListenQueue {
    @NotNull
    private final ArrayDeque<KeyguardFaceListenModel> faceQueue;
    @NotNull
    private final ArrayDeque<KeyguardFingerprintListenModel> fingerprintQueue;
    private final int sizePerModality;

    public KeyguardListenQueue() {
        this(0, 1, null);
    }

    public final void print(@NotNull PrintWriter writer) {
        Intrinsics.checkNotNullParameter(writer, "writer");
        print$default(this, writer, null, 2, null);
    }

    public KeyguardListenQueue(int i) {
        this.sizePerModality = i;
        this.faceQueue = new ArrayDeque<>();
        this.fingerprintQueue = new ArrayDeque<>();
    }

    public /* synthetic */ KeyguardListenQueue(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 20 : i);
    }

    @NotNull
    public final List<KeyguardListenModel> getModels() {
        List<KeyguardListenModel> plus;
        plus = CollectionsKt___CollectionsKt.plus((Collection) this.faceQueue, (Iterable) this.fingerprintQueue);
        return plus;
    }

    public final void add(@NotNull KeyguardListenModel model) {
        ArrayDeque arrayDeque;
        Intrinsics.checkNotNullParameter(model, "model");
        if (model instanceof KeyguardFaceListenModel) {
            arrayDeque = this.faceQueue;
            arrayDeque.add(model);
        } else if (!(model instanceof KeyguardFingerprintListenModel)) {
            throw new NoWhenBranchMatchedException();
        } else {
            arrayDeque = this.fingerprintQueue;
            arrayDeque.add(model);
        }
        if (arrayDeque.size() > this.sizePerModality) {
            arrayDeque.removeFirstOrNull();
        }
    }

    public static /* synthetic */ void print$default(KeyguardListenQueue keyguardListenQueue, PrintWriter printWriter, DateFormat dateFormat, int i, Object obj) {
        if ((i & 2) != 0) {
            dateFormat = KeyguardListenQueueKt.DEFAULT_FORMATTING;
        }
        keyguardListenQueue.print(printWriter, dateFormat);
    }

    public final void print(@NotNull PrintWriter writer, @NotNull DateFormat dateFormat) {
        Intrinsics.checkNotNullParameter(writer, "writer");
        Intrinsics.checkNotNullParameter(dateFormat, "dateFormat");
        KeyguardListenQueue$print$stringify$1 keyguardListenQueue$print$stringify$1 = new KeyguardListenQueue$print$stringify$1(dateFormat);
        writer.println("  Face listen results (last " + this.faceQueue.size() + " calls):");
        Iterator<KeyguardFaceListenModel> it = this.faceQueue.iterator();
        while (it.hasNext()) {
            writer.println(keyguardListenQueue$print$stringify$1.mo1949invoke((KeyguardListenQueue$print$stringify$1) it.next()));
        }
        writer.println("  Fingerprint listen results (last " + this.fingerprintQueue.size() + " calls):");
        Iterator<KeyguardFingerprintListenModel> it2 = this.fingerprintQueue.iterator();
        while (it2.hasNext()) {
            writer.println(keyguardListenQueue$print$stringify$1.mo1949invoke((KeyguardListenQueue$print$stringify$1) it2.next()));
        }
    }
}
