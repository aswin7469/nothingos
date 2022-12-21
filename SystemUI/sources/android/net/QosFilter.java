package android.net;

import android.annotation.SystemApi;
import java.net.InetAddress;

@SystemApi
public abstract class QosFilter {
    public abstract Network getNetwork();

    public abstract boolean matchesLocalAddress(InetAddress inetAddress, int i, int i2);

    public abstract boolean matchesRemoteAddress(InetAddress inetAddress, int i, int i2);

    public abstract int validate();

    QosFilter() {
    }
}
