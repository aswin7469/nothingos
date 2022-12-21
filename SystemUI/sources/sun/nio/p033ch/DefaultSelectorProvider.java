package sun.nio.p033ch;

import java.nio.channels.spi.SelectorProvider;

/* renamed from: sun.nio.ch.DefaultSelectorProvider */
public class DefaultSelectorProvider {
    private DefaultSelectorProvider() {
    }

    public static SelectorProvider create() {
        return new PollSelectorProvider();
    }
}
