package java.nio.channels;

import java.nio.channels.spi.SelectorProvider;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Selector implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public abstract void close() throws IOException;

    public abstract boolean isOpen();

    public abstract Set<SelectionKey> keys();

    public abstract SelectorProvider provider();

    public abstract int select() throws IOException;

    public abstract int select(long j) throws IOException;

    public abstract int selectNow() throws IOException;

    public abstract Set<SelectionKey> selectedKeys();

    public abstract Selector wakeup();

    protected Selector() {
    }

    public static Selector open() throws IOException {
        return SelectorProvider.provider().openSelector();
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.util.function.Consumer<java.nio.channels.SelectionKey>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int select(java.util.function.Consumer<java.nio.channels.SelectionKey> r3, long r4) throws java.p026io.IOException {
        /*
            r2 = this;
            r0 = 0
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x0011
            java.lang.Object r3 = java.util.Objects.requireNonNull(r3)
            java.util.function.Consumer r3 = (java.util.function.Consumer) r3
            int r2 = r2.doSelect(r3, r4)
            return r2
        L_0x0011:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Negative timeout"
            r2.<init>((java.lang.String) r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.Selector.select(java.util.function.Consumer, long):int");
    }

    public int select(Consumer<SelectionKey> consumer) throws IOException {
        return select(consumer, 0);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.util.function.Consumer<java.nio.channels.SelectionKey>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int selectNow(java.util.function.Consumer<java.nio.channels.SelectionKey> r3) throws java.p026io.IOException {
        /*
            r2 = this;
            java.lang.Object r3 = java.util.Objects.requireNonNull(r3)
            java.util.function.Consumer r3 = (java.util.function.Consumer) r3
            r0 = -1
            int r2 = r2.doSelect(r3, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.Selector.selectNow(java.util.function.Consumer):int");
    }

    private int doSelect(Consumer<SelectionKey> consumer, long j) throws IOException {
        int i;
        synchronized (this) {
            Set<SelectionKey> selectedKeys = selectedKeys();
            synchronized (selectedKeys) {
                selectedKeys.clear();
                if (j < 0) {
                    i = selectNow();
                } else {
                    i = select(j);
                }
                Set<SelectionKey> copyOf = Set.copyOf(selectedKeys);
                selectedKeys.clear();
                copyOf.forEach(new Selector$$ExternalSyntheticLambda0(this, consumer));
            }
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$doSelect$0$java-nio-channels-Selector  reason: not valid java name */
    public /* synthetic */ void m3724lambda$doSelect$0$javaniochannelsSelector(Consumer consumer, SelectionKey selectionKey) {
        consumer.accept(selectionKey);
        if (!isOpen()) {
            throw new ClosedSelectorException();
        }
    }
}
