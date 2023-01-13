package com.android.keyguard;

import java.p026io.PrintWriter;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000eJ\u001a\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r8G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u001b"}, mo65043d2 = {"Lcom/android/keyguard/KeyguardListenQueue;", "", "sizePerModality", "", "(I)V", "activeUnlockQueue", "Lkotlin/collections/ArrayDeque;", "Lcom/android/keyguard/KeyguardActiveUnlockModel;", "faceQueue", "Lcom/android/keyguard/KeyguardFaceListenModel;", "fingerprintQueue", "Lcom/android/keyguard/KeyguardFingerprintListenModel;", "models", "", "Lcom/android/keyguard/KeyguardListenModel;", "getModels", "()Ljava/util/List;", "getSizePerModality", "()I", "add", "", "model", "print", "writer", "Ljava/io/PrintWriter;", "dateFormat", "Ljava/text/DateFormat;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardListenQueue.kt */
public final class KeyguardListenQueue {
    private final ArrayDeque<KeyguardActiveUnlockModel> activeUnlockQueue;
    private final ArrayDeque<KeyguardFaceListenModel> faceQueue;
    private final ArrayDeque<KeyguardFingerprintListenModel> fingerprintQueue;
    private final int sizePerModality;

    public KeyguardListenQueue() {
        this(0, 1, (DefaultConstructorMarker) null);
    }

    public final void print(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "writer");
        print$default(this, printWriter, (DateFormat) null, 2, (Object) null);
    }

    public KeyguardListenQueue(int i) {
        this.sizePerModality = i;
        this.faceQueue = new ArrayDeque<>();
        this.fingerprintQueue = new ArrayDeque<>();
        this.activeUnlockQueue = new ArrayDeque<>();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ KeyguardListenQueue(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 20 : i);
    }

    public final int getSizePerModality() {
        return this.sizePerModality;
    }

    public final List<KeyguardListenModel> getModels() {
        return CollectionsKt.plus(CollectionsKt.plus(this.faceQueue, this.fingerprintQueue), this.activeUnlockQueue);
    }

    public final void add(KeyguardListenModel keyguardListenModel) {
        ArrayDeque arrayDeque;
        Intrinsics.checkNotNullParameter(keyguardListenModel, "model");
        if (keyguardListenModel instanceof KeyguardFaceListenModel) {
            arrayDeque = this.faceQueue;
            arrayDeque.add(keyguardListenModel);
        } else if (keyguardListenModel instanceof KeyguardFingerprintListenModel) {
            arrayDeque = this.fingerprintQueue;
            arrayDeque.add(keyguardListenModel);
        } else if (keyguardListenModel instanceof KeyguardActiveUnlockModel) {
            arrayDeque = this.activeUnlockQueue;
            arrayDeque.add(keyguardListenModel);
        } else {
            throw new NoWhenBranchMatchedException();
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

    public final void print(PrintWriter printWriter, DateFormat dateFormat) {
        Intrinsics.checkNotNullParameter(printWriter, "writer");
        Intrinsics.checkNotNullParameter(dateFormat, "dateFormat");
        Function1 keyguardListenQueue$print$stringify$1 = new KeyguardListenQueue$print$stringify$1(dateFormat);
        printWriter.println("  Face listen results (last " + this.faceQueue.size() + " calls):");
        Iterator<KeyguardFaceListenModel> it = this.faceQueue.iterator();
        while (it.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it.next()));
        }
        printWriter.println("  Fingerprint listen results (last " + this.fingerprintQueue.size() + " calls):");
        Iterator<KeyguardFingerprintListenModel> it2 = this.fingerprintQueue.iterator();
        while (it2.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it2.next()));
        }
        printWriter.println("  Active unlock triggers (last " + this.activeUnlockQueue.size() + " calls):");
        Iterator<KeyguardActiveUnlockModel> it3 = this.activeUnlockQueue.iterator();
        while (it3.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it3.next()));
        }
    }
}
