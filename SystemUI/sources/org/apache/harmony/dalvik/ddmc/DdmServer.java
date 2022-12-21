package org.apache.harmony.dalvik.ddmc;

import android.annotation.SystemApi;
import java.util.HashMap;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class DdmServer {
    private static final int CONNECTED = 1;
    private static final int DISCONNECTED = 2;
    private static HashMap<Integer, ChunkHandler> mHandlerMap = new HashMap<>();
    private static volatile boolean mRegistrationComplete = false;
    private static boolean mRegistrationTimedOut = false;

    private static native void nativeSendChunk(int i, byte[] bArr, int i2, int i3);

    private DdmServer() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void registerHandler(int i, ChunkHandler chunkHandler) {
        if (chunkHandler != null) {
            synchronized (mHandlerMap) {
                if (mHandlerMap.get(Integer.valueOf(i)) == null) {
                    mHandlerMap.put(Integer.valueOf(i), chunkHandler);
                } else {
                    throw new RuntimeException("type " + Integer.toHexString(i) + " already registered");
                }
            }
            return;
        }
        throw new NullPointerException("handler == null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static ChunkHandler unregisterHandler(int i) {
        ChunkHandler remove;
        synchronized (mHandlerMap) {
            remove = mHandlerMap.remove(Integer.valueOf(i));
        }
        return remove;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void registrationComplete() {
        synchronized (mHandlerMap) {
            mRegistrationComplete = true;
            mHandlerMap.notifyAll();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void sendChunk(Chunk chunk) {
        nativeSendChunk(chunk.type, chunk.data, chunk.offset, chunk.length);
    }

    private static void broadcast(int i) {
        synchronized (mHandlerMap) {
            for (ChunkHandler next : mHandlerMap.values()) {
                if (i == 1) {
                    next.onConnected();
                } else if (i == 2) {
                    next.onDisconnected();
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0003, code lost:
        continue;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0003 A[LOOP:0: B:2:0x0003->B:23:0x0003, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.apache.harmony.dalvik.ddmc.Chunk dispatch(int r4, byte[] r5, int r6, int r7) {
        /*
            java.util.HashMap<java.lang.Integer, org.apache.harmony.dalvik.ddmc.ChunkHandler> r0 = mHandlerMap
            monitor-enter(r0)
        L_0x0003:
            boolean r1 = mRegistrationComplete     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x001a
            boolean r1 = mRegistrationTimedOut     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x001a
            java.util.HashMap<java.lang.Integer, org.apache.harmony.dalvik.ddmc.ChunkHandler> r1 = mHandlerMap     // Catch:{ InterruptedException -> 0x0003 }
            r2 = 1000(0x3e8, double:4.94E-321)
            r1.wait(r2)     // Catch:{ InterruptedException -> 0x0003 }
            boolean r1 = mRegistrationComplete     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x0003
            r1 = 1
            mRegistrationTimedOut = r1     // Catch:{ all -> 0x0035 }
            goto L_0x0003
        L_0x001a:
            java.util.HashMap<java.lang.Integer, org.apache.harmony.dalvik.ddmc.ChunkHandler> r1 = mHandlerMap     // Catch:{ all -> 0x0035 }
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x0035 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0035 }
            org.apache.harmony.dalvik.ddmc.ChunkHandler r1 = (org.apache.harmony.dalvik.ddmc.ChunkHandler) r1     // Catch:{ all -> 0x0035 }
            monitor-exit(r0)     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x002b
            r4 = 0
            return r4
        L_0x002b:
            org.apache.harmony.dalvik.ddmc.Chunk r0 = new org.apache.harmony.dalvik.ddmc.Chunk
            r0.<init>(r4, r5, r6, r7)
            org.apache.harmony.dalvik.ddmc.Chunk r4 = r1.handleChunk(r0)
            return r4
        L_0x0035:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0035 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.harmony.dalvik.ddmc.DdmServer.dispatch(int, byte[], int, int):org.apache.harmony.dalvik.ddmc.Chunk");
    }
}
