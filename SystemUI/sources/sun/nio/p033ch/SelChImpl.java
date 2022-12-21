package sun.nio.p033ch;

import java.nio.channels.Channel;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.SelChImpl */
public interface SelChImpl extends Channel {
    FileDescriptor getFD();

    int getFDVal();

    void kill() throws IOException;

    void translateAndSetInterestOps(int i, SelectionKeyImpl selectionKeyImpl);

    boolean translateAndSetReadyOps(int i, SelectionKeyImpl selectionKeyImpl);

    boolean translateAndUpdateReadyOps(int i, SelectionKeyImpl selectionKeyImpl);

    int validOps();
}
