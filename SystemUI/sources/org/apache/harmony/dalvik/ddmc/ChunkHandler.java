package org.apache.harmony.dalvik.ddmc;

import android.annotation.SystemApi;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public abstract class ChunkHandler {
    public static final int CHUNK_FAIL = type("FAIL");
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final ByteOrder CHUNK_ORDER = ByteOrder.BIG_ENDIAN;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract Chunk handleChunk(Chunk chunk);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract void onConnected();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract void onDisconnected();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Chunk createFailChunk(int i, String str) {
        if (str == null) {
            str = "";
        }
        ByteBuffer allocate = ByteBuffer.allocate((str.length() * 2) + 8);
        allocate.order(CHUNK_ORDER);
        allocate.putInt(i);
        allocate.putInt(str.length());
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            allocate.putChar(str.charAt(i2));
        }
        return new Chunk(CHUNK_FAIL, allocate);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static ByteBuffer wrapChunk(Chunk chunk) {
        ByteBuffer wrap = ByteBuffer.wrap(chunk.data, chunk.offset, chunk.length);
        wrap.order(CHUNK_ORDER);
        return wrap;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int type(String str) {
        if (str.length() == 4) {
            char c = 0;
            for (int i = 0; i < 4; i++) {
                c = (c << 8) | (str.charAt(i) & 255);
            }
            return c;
        }
        throw new IllegalArgumentException("Bad type name: " + str);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String name(int i) {
        return new String(new char[]{(char) ((i >> 24) & 255), (char) ((i >> 16) & 255), (char) ((i >> 8) & 255), (char) (i & 255)});
    }
}
