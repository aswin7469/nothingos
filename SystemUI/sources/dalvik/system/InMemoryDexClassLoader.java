package dalvik.system;

import java.nio.ByteBuffer;

public final class InMemoryDexClassLoader extends BaseDexClassLoader {
    public InMemoryDexClassLoader(ByteBuffer[] byteBufferArr, String str, ClassLoader classLoader) {
        super(byteBufferArr, str, classLoader);
    }

    public InMemoryDexClassLoader(ByteBuffer[] byteBufferArr, ClassLoader classLoader) {
        this(byteBufferArr, (String) null, classLoader);
    }

    public InMemoryDexClassLoader(ByteBuffer byteBuffer, ClassLoader classLoader) {
        this(new ByteBuffer[]{byteBuffer}, classLoader);
    }
}
