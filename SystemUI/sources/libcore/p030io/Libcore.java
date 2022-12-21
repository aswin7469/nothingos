package libcore.p030io;

import java.util.Objects;

/* renamed from: libcore.io.Libcore */
public final class Libcore {

    /* renamed from: os */
    public static volatile C4699Os f857os;
    public static final C4699Os rawOs;

    private Libcore() {
    }

    static {
        Linux linux = new Linux();
        rawOs = linux;
        f857os = new BlockGuardOs(linux);
    }

    public static C4699Os getOs() {
        return f857os;
    }

    public static boolean compareAndSetOs(C4699Os os, C4699Os os2) {
        Objects.requireNonNull(os2);
        boolean z = false;
        if (f857os != os) {
            return false;
        }
        synchronized (Libcore.class) {
            if (f857os == os) {
                z = true;
            }
            if (z) {
                f857os = os2;
            }
        }
        return z;
    }
}
