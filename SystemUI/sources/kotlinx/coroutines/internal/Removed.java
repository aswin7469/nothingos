package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LockFreeLinkedList.kt */
/* loaded from: classes2.dex */
public final class Removed {
    @NotNull
    public final LockFreeLinkedListNode ref;

    public Removed(@NotNull LockFreeLinkedListNode ref) {
        Intrinsics.checkParameterIsNotNull(ref, "ref");
        this.ref = ref;
    }

    @NotNull
    public String toString() {
        return "Removed[" + this.ref + ']';
    }
}
