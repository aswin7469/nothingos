package java.nio.channels;

import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

public abstract class Pipe {
    public abstract SinkChannel sink();

    public abstract SourceChannel source();

    public static abstract class SourceChannel extends AbstractSelectableChannel implements ReadableByteChannel, ScatteringByteChannel {
        public final int validOps() {
            return 1;
        }

        protected SourceChannel(SelectorProvider selectorProvider) {
            super(selectorProvider);
        }
    }

    public static abstract class SinkChannel extends AbstractSelectableChannel implements WritableByteChannel, GatheringByteChannel {
        public final int validOps() {
            return 4;
        }

        protected SinkChannel(SelectorProvider selectorProvider) {
            super(selectorProvider);
        }
    }

    protected Pipe() {
    }

    public static Pipe open() throws IOException {
        return SelectorProvider.provider().openPipe();
    }
}
