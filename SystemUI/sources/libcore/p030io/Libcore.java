package libcore.p030io;

import java.util.Objects;

/* renamed from: libcore.io.Libcore */
public final class Libcore {

    /* renamed from: os */
    public static volatile C4711Os f855os;
    public static final C4711Os rawOs;

    private Libcore() {
    }

    static {
        Linux linux = new Linux();
        rawOs = linux;
        f855os = new BlockGuardOs(linux);
    }

    public static C4711Os getOs() {
        return f855os;
    }

    public static boolean compareAndSetOs(C4711Os os, C4711Os os2) {
        Objects.requireNonNull(os2);
        boolean z = false;
        if (f855os != os) {
            return false;
        }
        synchronized (Libcore.class) {
            if (f855os == os) {
                z = true;
            }
            if (z) {
                f855os = os2;
            }
        }
        return z;
    }
}
