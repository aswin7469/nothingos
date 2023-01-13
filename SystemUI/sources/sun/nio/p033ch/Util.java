package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import sun.misc.C4752VM;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.security.action.GetPropertyAction;

/* renamed from: sun.nio.ch.Util */
public class Util {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long MAX_CACHED_BUFFER_SIZE = getMaxCachedBufferSize();
    /* access modifiers changed from: private */
    public static final int TEMP_BUF_POOL_SIZE = IOUtil.IOV_MAX;
    private static ThreadLocal<BufferCache> bufferCache = new ThreadLocal<BufferCache>() {
        /* access modifiers changed from: protected */
        public BufferCache initialValue() {
            return new BufferCache();
        }
    };
    private static volatile String bugLevel = null;
    private static Unsafe unsafe = Unsafe.getUnsafe();

    private static long getMaxCachedBufferSize() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty("jdk.nio.maxCachedBufferSize");
            }
        });
        if (str == null) {
            return Long.MAX_VALUE;
        }
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong >= 0) {
                return parseLong;
            }
            return Long.MAX_VALUE;
        } catch (NumberFormatException unused) {
            return Long.MAX_VALUE;
        }
    }

    /* access modifiers changed from: private */
    public static boolean isBufferTooLarge(int i) {
        return ((long) i) > MAX_CACHED_BUFFER_SIZE;
    }

    /* access modifiers changed from: private */
    public static boolean isBufferTooLarge(ByteBuffer byteBuffer) {
        return isBufferTooLarge(byteBuffer.capacity());
    }

    /* renamed from: sun.nio.ch.Util$BufferCache */
    private static class BufferCache {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private ByteBuffer[] buffers = new ByteBuffer[Util.TEMP_BUF_POOL_SIZE];
        private int count;
        private int start;

        static {
            Class<Util> cls = Util.class;
        }

        private int next(int i) {
            return (i + 1) % Util.TEMP_BUF_POOL_SIZE;
        }

        BufferCache() {
        }

        /* access modifiers changed from: package-private */
        public ByteBuffer get(int i) {
            ByteBuffer byteBuffer;
            if (this.count == 0) {
                return null;
            }
            ByteBuffer[] byteBufferArr = this.buffers;
            ByteBuffer byteBuffer2 = byteBufferArr[this.start];
            if (byteBuffer2.capacity() < i) {
                int i2 = this.start;
                while (true) {
                    i2 = next(i2);
                    if (i2 != this.start && (byteBuffer = byteBufferArr[i2]) != null) {
                        if (byteBuffer.capacity() >= i) {
                            break;
                        }
                    } else {
                        byteBuffer = null;
                    }
                }
                byteBuffer = null;
                if (byteBuffer == null) {
                    return null;
                }
                byteBufferArr[i2] = byteBufferArr[this.start];
                byteBuffer2 = byteBuffer;
            }
            int i3 = this.start;
            byteBufferArr[i3] = null;
            this.start = next(i3);
            this.count--;
            byteBuffer2.rewind();
            byteBuffer2.limit(i);
            return byteBuffer2;
        }

        /* access modifiers changed from: package-private */
        public boolean offerFirst(ByteBuffer byteBuffer) {
            if (this.count >= Util.TEMP_BUF_POOL_SIZE) {
                return false;
            }
            int r0 = ((this.start + Util.TEMP_BUF_POOL_SIZE) - 1) % Util.TEMP_BUF_POOL_SIZE;
            this.start = r0;
            this.buffers[r0] = byteBuffer;
            this.count++;
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean offerLast(ByteBuffer byteBuffer) {
            if (this.count >= Util.TEMP_BUF_POOL_SIZE) {
                return false;
            }
            this.buffers[(this.start + this.count) % Util.TEMP_BUF_POOL_SIZE] = byteBuffer;
            this.count++;
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.count == 0;
        }

        /* access modifiers changed from: package-private */
        public ByteBuffer removeFirst() {
            ByteBuffer[] byteBufferArr = this.buffers;
            int i = this.start;
            ByteBuffer byteBuffer = byteBufferArr[i];
            byteBufferArr[i] = null;
            this.start = next(i);
            this.count--;
            return byteBuffer;
        }
    }

    public static ByteBuffer getTemporaryDirectBuffer(int i) {
        if (isBufferTooLarge(i)) {
            return ByteBuffer.allocateDirect(i);
        }
        BufferCache bufferCache2 = bufferCache.get();
        ByteBuffer byteBuffer = bufferCache2.get(i);
        if (byteBuffer != null) {
            return byteBuffer;
        }
        if (!bufferCache2.isEmpty()) {
            free(bufferCache2.removeFirst());
        }
        return ByteBuffer.allocateDirect(i);
    }

    public static void releaseTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        offerFirstTemporaryDirectBuffer(byteBuffer);
    }

    static void offerFirstTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        if (isBufferTooLarge(byteBuffer)) {
            free(byteBuffer);
        } else if (!bufferCache.get().offerFirst(byteBuffer)) {
            free(byteBuffer);
        }
    }

    static void offerLastTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        if (isBufferTooLarge(byteBuffer)) {
            free(byteBuffer);
        } else if (!bufferCache.get().offerLast(byteBuffer)) {
            free(byteBuffer);
        }
    }

    private static void free(ByteBuffer byteBuffer) {
        Cleaner cleaner = ((DirectBuffer) byteBuffer).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }

    static ByteBuffer[] subsequence(ByteBuffer[] byteBufferArr, int i, int i2) {
        if (i == 0 && i2 == byteBufferArr.length) {
            return byteBufferArr;
        }
        ByteBuffer[] byteBufferArr2 = new ByteBuffer[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            byteBufferArr2[i3] = byteBufferArr[i + i3];
        }
        return byteBufferArr2;
    }

    static <E> Set<E> ungrowableSet(final Set<E> set) {
        return new Set<E>() {
            public int size() {
                return Set.this.size();
            }

            public boolean isEmpty() {
                return Set.this.isEmpty();
            }

            public boolean contains(Object obj) {
                return Set.this.contains(obj);
            }

            public Object[] toArray() {
                return Set.this.toArray();
            }

            public <T> T[] toArray(T[] tArr) {
                return Set.this.toArray(tArr);
            }

            public String toString() {
                return Set.this.toString();
            }

            public Iterator<E> iterator() {
                return Set.this.iterator();
            }

            public boolean equals(Object obj) {
                return Set.this.equals(obj);
            }

            public int hashCode() {
                return Set.this.hashCode();
            }

            public void clear() {
                Set.this.clear();
            }

            public boolean remove(Object obj) {
                return Set.this.remove(obj);
            }

            public boolean containsAll(Collection<?> collection) {
                return Set.this.containsAll(collection);
            }

            public boolean removeAll(Collection<?> collection) {
                return Set.this.removeAll(collection);
            }

            public boolean retainAll(Collection<?> collection) {
                return Set.this.retainAll(collection);
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static byte _get(long j) {
        return unsafe.getByte(j);
    }

    private static void _put(long j, byte b) {
        unsafe.putByte(j, b);
    }

    static void erase(ByteBuffer byteBuffer) {
        unsafe.setMemory(((DirectBuffer) byteBuffer).address(), (long) byteBuffer.capacity(), (byte) 0);
    }

    static Unsafe unsafe() {
        return unsafe;
    }

    static boolean atBugLevel(String str) {
        if (bugLevel == null) {
            if (!C4752VM.isBooted()) {
                return false;
            }
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.bugLevel"));
            if (str2 == null) {
                str2 = "";
            }
            bugLevel = str2;
        }
        return bugLevel.equals(str);
    }
}
