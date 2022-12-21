package android.system;

import java.lang.ref.Cleaner;
import jdk.internal.ref.CleanerFactory;

public final class SystemCleaner {
    private SystemCleaner() {
    }

    public static Cleaner cleaner() {
        return CleanerFactory.cleaner();
    }
}
