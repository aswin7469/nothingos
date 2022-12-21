package sun.nio.p033ch;

import java.nio.channels.Channel;
import java.nio.channels.spi.AbstractSelector;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.PollSelectorProvider */
public class PollSelectorProvider extends SelectorProviderImpl {
    public Channel inheritedChannel() throws IOException {
        return null;
    }

    public AbstractSelector openSelector() throws IOException {
        return new PollSelectorImpl(this);
    }
}
