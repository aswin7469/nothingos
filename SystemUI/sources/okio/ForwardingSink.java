package okio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;

public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        if (sink != null) {
            this.delegate = sink;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    public final Sink delegate() {
        return this.delegate;
    }

    public void write(Buffer buffer, long j) throws IOException {
        this.delegate.write(buffer, j);
    }

    public void flush() throws IOException {
        this.delegate.flush();
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
