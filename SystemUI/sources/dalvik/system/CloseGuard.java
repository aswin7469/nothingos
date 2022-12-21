package dalvik.system;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class CloseGuard {
    private static final String MESSAGE = "A resource was acquired at attached stack trace but never released. See java.io.Closeable for information on avoiding resource leaks.";
    private static volatile Tracker currentTracker = null;
    private static volatile Reporter reporter = new DefaultReporter();
    private static volatile boolean stackAndTrackingEnabled = true;
    private Object closerNameOrAllocationInfo;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface Reporter {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void report(String str) {
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void report(String str, Throwable th);
    }

    public interface Tracker {
        void close(Throwable th);

        void open(Throwable th);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static CloseGuard get() {
        return new CloseGuard();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setEnabled(boolean z) {
        stackAndTrackingEnabled = z;
    }

    public static boolean isEnabled() {
        return stackAndTrackingEnabled;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setReporter(Reporter reporter2) {
        if (reporter2 != null) {
            reporter = reporter2;
            return;
        }
        throw new NullPointerException("reporter == null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Reporter getReporter() {
        return reporter;
    }

    public static void setTracker(Tracker tracker) {
        currentTracker = tracker;
    }

    public static Tracker getTracker() {
        return currentTracker;
    }

    private CloseGuard() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void open(String str) {
        openWithCallSite(str, (String) null);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void openWithCallSite(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("closer == null");
        } else if (!stackAndTrackingEnabled) {
            this.closerNameOrAllocationInfo = str;
        } else {
            Tracker tracker = currentTracker;
            if (str2 == null || tracker != null) {
                Throwable th = new Throwable("Explicit termination method '" + str + "' not called");
                this.closerNameOrAllocationInfo = th;
                if (tracker != null) {
                    tracker.open(th);
                    return;
                }
                return;
            }
            this.closerNameOrAllocationInfo = str2;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void close() {
        Tracker tracker = currentTracker;
        if (tracker != null) {
            Object obj = this.closerNameOrAllocationInfo;
            if (obj instanceof Throwable) {
                tracker.close((Throwable) obj);
            }
        }
        this.closerNameOrAllocationInfo = null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void warnIfOpen() {
        Object obj = this.closerNameOrAllocationInfo;
        if (obj == null) {
            return;
        }
        if (obj instanceof Throwable) {
            reporter.report(MESSAGE, (Throwable) this.closerNameOrAllocationInfo);
        } else if (stackAndTrackingEnabled) {
            Reporter reporter2 = reporter;
            reporter2.report("A resource was acquired at attached stack trace but never released. See java.io.Closeable for information on avoiding resource leaks. Callsite: " + this.closerNameOrAllocationInfo);
        } else {
            System.logW("A resource failed to call " + ((String) this.closerNameOrAllocationInfo) + ". ");
        }
    }

    private static final class DefaultReporter implements Reporter {
        private DefaultReporter() {
        }

        public void report(String str, Throwable th) {
            System.logW(str, th);
        }

        public void report(String str) {
            System.logW(str);
        }
    }
}
