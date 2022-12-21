package sun.net;

import java.net.URL;

/* compiled from: ProgressMonitor */
class DefaultProgressMeteringPolicy implements ProgressMeteringPolicy {
    public int getProgressUpdateThreshold() {
        return 8192;
    }

    public boolean shouldMeterInput(URL url, String str) {
        return false;
    }

    DefaultProgressMeteringPolicy() {
    }
}
