package sun.nio.p033ch;

import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.PipeImpl */
class PipeImpl extends Pipe {
    private final Pipe.SinkChannel sink;
    private final Pipe.SourceChannel source;

    PipeImpl(SelectorProvider selectorProvider) throws IOException {
        long makePipe = IOUtil.makePipe(true);
        FileDescriptor fileDescriptor = new FileDescriptor();
        IOUtil.setfdVal(fileDescriptor, (int) (makePipe >>> 32));
        this.source = new SourceChannelImpl(selectorProvider, fileDescriptor);
        FileDescriptor fileDescriptor2 = new FileDescriptor();
        IOUtil.setfdVal(fileDescriptor2, (int) makePipe);
        this.sink = new SinkChannelImpl(selectorProvider, fileDescriptor2);
    }

    public Pipe.SourceChannel source() {
        return this.source;
    }

    public Pipe.SinkChannel sink() {
        return this.sink;
    }
}
