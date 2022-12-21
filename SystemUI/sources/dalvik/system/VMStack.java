package dalvik.system;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class VMStack {
    public static native int fillStackTraceElements(Thread thread, StackTraceElement[] stackTraceElementArr);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native AnnotatedStackTraceElement[] getAnnotatedThreadStackTrace(Thread thread);

    @Deprecated
    public static native ClassLoader getCallingClassLoader();

    public static native ClassLoader getClosestUserClassLoader();

    public static native Class<?> getStackClass2();

    public static native StackTraceElement[] getThreadStackTrace(Thread thread);

    private VMStack() {
    }

    @Deprecated
    public static Class<?> getStackClass1() {
        return getStackClass2();
    }
}
