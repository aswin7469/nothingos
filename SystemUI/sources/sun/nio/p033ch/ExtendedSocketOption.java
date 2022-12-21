package sun.nio.p033ch;

import java.net.SocketOption;

/* renamed from: sun.nio.ch.ExtendedSocketOption */
class ExtendedSocketOption {
    static final SocketOption<Boolean> SO_OOBINLINE = new SocketOption<Boolean>() {
        public String name() {
            return "SO_OOBINLINE";
        }

        public Class<Boolean> type() {
            return Boolean.class;
        }

        public String toString() {
            return name();
        }
    };

    private ExtendedSocketOption() {
    }
}
