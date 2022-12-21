package libcore.util;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class SneakyThrow {
    private SneakyThrow() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void sneakyThrow(Throwable th) {
        sneakyThrow_(th);
    }

    private static <T extends Throwable> void sneakyThrow_(Throwable th) throws Throwable {
        throw th;
    }
}
