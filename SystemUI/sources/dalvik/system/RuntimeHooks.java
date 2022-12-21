package dalvik.system;

import android.annotation.SystemApi;
import java.lang.Thread;
import java.util.function.Supplier;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class RuntimeHooks {
    private static Supplier<String> zoneIdSupplier;

    private RuntimeHooks() {
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.function.Supplier<java.lang.String>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.MODULE_LIBRARIES)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setTimeZoneIdSupplier(java.util.function.Supplier<java.lang.String> r1) {
        /*
            java.util.function.Supplier<java.lang.String> r0 = zoneIdSupplier
            if (r0 != 0) goto L_0x0011
            java.lang.Object r1 = java.util.Objects.requireNonNull(r1)
            java.util.function.Supplier r1 = (java.util.function.Supplier) r1
            zoneIdSupplier = r1
            r1 = 0
            java.util.TimeZone.setDefault(r1)
            return
        L_0x0011:
            java.lang.UnsupportedOperationException r1 = new java.lang.UnsupportedOperationException
            java.lang.String r0 = "zoneIdSupplier instance already set"
            r1.<init>((java.lang.String) r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dalvik.system.RuntimeHooks.setTimeZoneIdSupplier(java.util.function.Supplier):void");
    }

    public static void clearTimeZoneIdSupplier() {
        zoneIdSupplier = null;
    }

    public static Supplier<String> getTimeZoneIdSupplier() {
        return zoneIdSupplier;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setUncaughtExceptionPreHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        Thread.setUncaughtExceptionPreHandler(uncaughtExceptionHandler);
    }
}
