package com.android.okhttp.okio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;

public abstract class ForwardingSource implements Source {
    private final Source delegate;

    public ForwardingSource(Source source) {
        if (source != null) {
            this.delegate = source;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    public final Source delegate() {
        return this.delegate;
    }

    public long read(Buffer buffer, long j) throws IOException {
        return this.delegate.read(buffer, j);
    }

    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public void close() throws IOException {
        this.delegate.close();
    }

    public String toString() {
        return getClass().getSimpleName() + NavigationBarInflaterView.KEY_CODE_START + this.delegate.toString() + NavigationBarInflaterView.KEY_CODE_END;
    }
}
