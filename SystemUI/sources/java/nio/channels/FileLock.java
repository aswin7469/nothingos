package java.nio.channels;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;

public abstract class FileLock implements AutoCloseable {
    private final Channel channel;
    private final long position;
    private final boolean shared;
    private final long size;

    public abstract boolean isValid();

    public abstract void release() throws IOException;

    protected FileLock(FileChannel fileChannel, long j, long j2, boolean z) {
        if (j < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (j2 < 0) {
            throw new IllegalArgumentException("Negative size");
        } else if (j + j2 >= 0) {
            this.channel = fileChannel;
            this.position = j;
            this.size = j2;
            this.shared = z;
        } else {
            throw new IllegalArgumentException("Negative position + size");
        }
    }

    protected FileLock(AsynchronousFileChannel asynchronousFileChannel, long j, long j2, boolean z) {
        if (j < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (j2 < 0) {
            throw new IllegalArgumentException("Negative size");
        } else if (j + j2 >= 0) {
            this.channel = asynchronousFileChannel;
            this.position = j;
            this.size = j2;
            this.shared = z;
        } else {
            throw new IllegalArgumentException("Negative position + size");
        }
    }

    public final FileChannel channel() {
        Channel channel2 = this.channel;
        if (channel2 instanceof FileChannel) {
            return (FileChannel) channel2;
        }
        return null;
    }

    public Channel acquiredBy() {
        return this.channel;
    }

    public final long position() {
        return this.position;
    }

    public final long size() {
        return this.size;
    }

    public final boolean isShared() {
        return this.shared;
    }

    public final boolean overlaps(long j, long j2) {
        long j3 = this.position;
        if (j2 + j > j3 && j3 + this.size > j) {
            return true;
        }
        return false;
    }

    public final void close() throws IOException {
        release();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(NavigationBarInflaterView.SIZE_MOD_START);
        sb.append(this.position);
        sb.append(":");
        sb.append(this.size);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.shared ? "shared" : "exclusive");
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(isValid() ? "valid" : "invalid");
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
