package java.nio.channels.spi;

import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Channel;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.InterruptibleChannel;
import java.p026io.IOException;
import sun.nio.p033ch.Interruptible;

public abstract class AbstractInterruptibleChannel implements Channel, InterruptibleChannel {
    /* access modifiers changed from: private */
    public final Object closeLock = new Object();
    /* access modifiers changed from: private */
    public volatile Thread interrupted;
    private Interruptible interruptor;
    /* access modifiers changed from: private */
    public volatile boolean open = true;

    /* access modifiers changed from: protected */
    public abstract void implCloseChannel() throws IOException;

    protected AbstractInterruptibleChannel() {
    }

    public final void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.open) {
                this.open = false;
                implCloseChannel();
            }
        }
    }

    public final boolean isOpen() {
        return this.open;
    }

    /* access modifiers changed from: protected */
    public final void begin() {
        if (this.interruptor == null) {
            this.interruptor = new Interruptible() {
                /* JADX WARNING: Can't wrap try/catch for region: R(6:7|8|9|10|11|12) */
                /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0021 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void interrupt(java.lang.Thread r4) {
                    /*
                        r3 = this;
                        java.nio.channels.spi.AbstractInterruptibleChannel r0 = java.nio.channels.spi.AbstractInterruptibleChannel.this
                        java.lang.Object r0 = r0.closeLock
                        monitor-enter(r0)
                        java.nio.channels.spi.AbstractInterruptibleChannel r1 = java.nio.channels.spi.AbstractInterruptibleChannel.this     // Catch:{ all -> 0x0023 }
                        boolean r1 = r1.open     // Catch:{ all -> 0x0023 }
                        if (r1 != 0) goto L_0x0011
                        monitor-exit(r0)     // Catch:{ all -> 0x0023 }
                        return
                    L_0x0011:
                        java.nio.channels.spi.AbstractInterruptibleChannel r1 = java.nio.channels.spi.AbstractInterruptibleChannel.this     // Catch:{ all -> 0x0023 }
                        r2 = 0
                        r1.open = r2     // Catch:{ all -> 0x0023 }
                        java.nio.channels.spi.AbstractInterruptibleChannel r1 = java.nio.channels.spi.AbstractInterruptibleChannel.this     // Catch:{ all -> 0x0023 }
                        r1.interrupted = r4     // Catch:{ all -> 0x0023 }
                        java.nio.channels.spi.AbstractInterruptibleChannel r3 = java.nio.channels.spi.AbstractInterruptibleChannel.this     // Catch:{ IOException -> 0x0021 }
                        r3.implCloseChannel()     // Catch:{ IOException -> 0x0021 }
                    L_0x0021:
                        monitor-exit(r0)     // Catch:{ all -> 0x0023 }
                        return
                    L_0x0023:
                        r3 = move-exception
                        monitor-exit(r0)     // Catch:{ all -> 0x0023 }
                        throw r3
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.spi.AbstractInterruptibleChannel.C43611.interrupt(java.lang.Thread):void");
                }
            };
        }
        blockedOn(this.interruptor);
        Thread currentThread = Thread.currentThread();
        if (currentThread.isInterrupted()) {
            this.interruptor.interrupt(currentThread);
        }
    }

    /* access modifiers changed from: protected */
    public final void end(boolean z) throws AsynchronousCloseException {
        blockedOn((Interruptible) null);
        Thread thread = this.interrupted;
        if (thread != null && thread == Thread.currentThread()) {
            throw new ClosedByInterruptException();
        } else if (!z && !this.open) {
            throw new AsynchronousCloseException();
        }
    }

    static void blockedOn(Interruptible interruptible) {
        Thread.currentThread().blockedOn(interruptible);
    }
}
