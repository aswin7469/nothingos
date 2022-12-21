package sun.nio.p033ch;

import sun.misc.Cleaner;

/* renamed from: sun.nio.ch.DirectBuffer */
public interface DirectBuffer {
    long address();

    Object attachment();

    Cleaner cleaner();
}
