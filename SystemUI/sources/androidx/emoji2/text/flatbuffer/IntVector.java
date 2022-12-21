package androidx.emoji2.text.flatbuffer;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.nio.ByteBuffer;

public final class IntVector extends BaseVector {
    public IntVector __assign(int i, ByteBuffer byteBuffer) {
        __reset(i, 4, byteBuffer);
        return this;
    }

    public int get(int i) {
        return this.f135bb.getInt(__element(i));
    }

    public long getAsUnsigned(int i) {
        return ((long) get(i)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }
}
