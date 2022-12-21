package java.util.stream;

import java.security.AccessController;
import sun.util.logging.PlatformLogger;

final class Tripwire {
    static final boolean ENABLED = ((Boolean) AccessController.doPrivileged(new Tripwire$$ExternalSyntheticLambda0())).booleanValue();
    private static final String TRIPWIRE_PROPERTY = "org.openjdk.java.util.stream.tripwire";

    private Tripwire() {
    }

    static void trip(Class<?> cls, String str) {
        PlatformLogger.getLogger(cls.getName()).warning(str, cls.getName());
    }
}
