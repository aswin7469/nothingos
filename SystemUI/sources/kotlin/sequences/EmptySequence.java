package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.EmptyIterator;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class EmptySequence implements Sequence, DropTakeSequence {
    @NotNull
    public static final EmptySequence INSTANCE = new EmptySequence();

    private EmptySequence() {
    }

    @Override // kotlin.sequences.Sequence
    @NotNull
    public Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }

    @Override // kotlin.sequences.DropTakeSequence
    @NotNull
    /* renamed from: take */
    public EmptySequence mo1948take(int i) {
        return INSTANCE;
    }
}
