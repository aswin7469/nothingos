package org.apache.harmony.dalvik.ddmc;

import android.annotation.SystemApi;
import java.nio.ByteBuffer;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class Chunk {
    public byte[] data;
    public int length;
    public int offset;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int type;

    public Chunk() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Chunk(int i, byte[] bArr, int i2, int i3) {
        this.type = i;
        this.data = bArr;
        this.offset = i2;
        this.length = i3;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Chunk(int i, ByteBuffer byteBuffer) {
        this.type = i;
        this.data = byteBuffer.array();
        this.offset = byteBuffer.arrayOffset();
        this.length = byteBuffer.position();
    }
}
