package android.hardware.usb;

import android.util.Log;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: classes2.dex */
public class UsbRequest {
    static final int MAX_USBFS_BUFFER_SIZE = 16384;
    private static final String TAG = "UsbRequest";
    private ByteBuffer mBuffer;
    private Object mClientData;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpoint;
    private boolean mIsUsingNewQueue;
    private int mLength;
    private long mNativeContext;
    private ByteBuffer mTempBuffer;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final Object mLock = new Object();

    private native boolean native_cancel();

    private native void native_close();

    private native int native_dequeue_array(byte[] bArr, int i, boolean z);

    private native int native_dequeue_direct();

    private native boolean native_init(UsbDeviceConnection usbDeviceConnection, int i, int i2, int i3, int i4);

    private native boolean native_queue(ByteBuffer byteBuffer, int i, int i2);

    private native boolean native_queue_array(byte[] bArr, int i, boolean z);

    private native boolean native_queue_direct(ByteBuffer byteBuffer, int i, boolean z);

    public boolean initialize(UsbDeviceConnection connection, UsbEndpoint endpoint) {
        this.mEndpoint = endpoint;
        Objects.requireNonNull(connection, "connection");
        this.mConnection = connection;
        boolean wasInitialized = native_init(connection, endpoint.getAddress(), endpoint.getAttributes(), endpoint.getMaxPacketSize(), endpoint.getInterval());
        if (wasInitialized) {
            this.mCloseGuard.open("close");
        }
        return wasInitialized;
    }

    public void close() {
        if (this.mNativeContext != 0) {
            this.mEndpoint = null;
            this.mConnection = null;
            native_close();
            this.mCloseGuard.close();
        }
    }

    protected void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public UsbEndpoint getEndpoint() {
        return this.mEndpoint;
    }

    public Object getClientData() {
        return this.mClientData;
    }

    public void setClientData(Object data) {
        this.mClientData = data;
    }

    @Deprecated
    public boolean queue(ByteBuffer buffer, int length) {
        boolean result;
        boolean out = this.mEndpoint.getDirection() == 0;
        if (this.mConnection.getContext().getApplicationInfo().targetSdkVersion < 28 && length > 16384) {
            length = 16384;
        }
        synchronized (this.mLock) {
            this.mBuffer = buffer;
            this.mLength = length;
            if (buffer.isDirect()) {
                result = native_queue_direct(buffer, length, out);
            } else {
                boolean result2 = buffer.hasArray();
                if (result2) {
                    result = native_queue_array(buffer.array(), length, out);
                } else {
                    throw new IllegalArgumentException("buffer is not direct and has no array");
                }
            }
            if (!result) {
                this.mBuffer = null;
                this.mLength = 0;
            }
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0069 A[Catch: all -> 0x00a8, TryCatch #0 {, blocks: (B:10:0x002a, B:12:0x002f, B:13:0x009e, B:19:0x0036, B:21:0x0046, B:22:0x0052, B:27:0x005e, B:29:0x0069, B:31:0x0077, B:32:0x008d, B:33:0x0090), top: B:9:0x002a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean queue(ByteBuffer buffer) {
        boolean z;
        boolean wasQueued;
        Preconditions.checkState(this.mNativeContext != 0, "request is not initialized");
        Preconditions.checkState(!this.mIsUsingNewQueue, "this request is currently queued");
        boolean isSend = this.mEndpoint.getDirection() == 0;
        synchronized (this.mLock) {
            this.mBuffer = buffer;
            if (buffer == null) {
                this.mIsUsingNewQueue = true;
                wasQueued = native_queue(null, 0, 0);
            } else {
                if (this.mConnection.getContext().getApplicationInfo().targetSdkVersion < 28) {
                    Preconditions.checkArgumentInRange(buffer.remaining(), 0, 16384, "number of remaining bytes");
                }
                if (buffer.isReadOnly() && !isSend) {
                    z = false;
                    Preconditions.checkArgument(z, "buffer can not be read-only when receiving data");
                    if (!buffer.isDirect()) {
                        this.mTempBuffer = ByteBuffer.allocateDirect(this.mBuffer.remaining());
                        if (isSend) {
                            this.mBuffer.mark();
                            this.mTempBuffer.put(this.mBuffer);
                            this.mTempBuffer.flip();
                            this.mBuffer.reset();
                        }
                        buffer = this.mTempBuffer;
                    }
                    this.mIsUsingNewQueue = true;
                    wasQueued = native_queue(buffer, buffer.position(), buffer.remaining());
                }
                z = true;
                Preconditions.checkArgument(z, "buffer can not be read-only when receiving data");
                if (!buffer.isDirect()) {
                }
                this.mIsUsingNewQueue = true;
                wasQueued = native_queue(buffer, buffer.position(), buffer.remaining());
            }
        }
        if (!wasQueued) {
            this.mIsUsingNewQueue = false;
            this.mTempBuffer = null;
            this.mBuffer = null;
        }
        return wasQueued;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dequeue(boolean useBufferOverflowInsteadOfIllegalArg) {
        int bytesTransferred;
        boolean isSend = this.mEndpoint.getDirection() == 0;
        synchronized (this.mLock) {
            if (this.mIsUsingNewQueue) {
                int bytesTransferred2 = native_dequeue_direct();
                this.mIsUsingNewQueue = false;
                ByteBuffer byteBuffer = this.mBuffer;
                if (byteBuffer != null) {
                    ByteBuffer byteBuffer2 = this.mTempBuffer;
                    if (byteBuffer2 == null) {
                        byteBuffer.position(byteBuffer.position() + bytesTransferred2);
                    } else {
                        byteBuffer2.limit(bytesTransferred2);
                        if (isSend) {
                            ByteBuffer byteBuffer3 = this.mBuffer;
                            byteBuffer3.position(byteBuffer3.position() + bytesTransferred2);
                        } else {
                            this.mBuffer.put(this.mTempBuffer);
                        }
                        this.mTempBuffer = null;
                    }
                }
            } else {
                if (this.mBuffer.isDirect()) {
                    bytesTransferred = native_dequeue_direct();
                } else {
                    bytesTransferred = native_dequeue_array(this.mBuffer.array(), this.mLength, isSend);
                }
                if (bytesTransferred >= 0) {
                    int bytesToStore = Math.min(bytesTransferred, this.mLength);
                    try {
                        this.mBuffer.position(bytesToStore);
                    } catch (IllegalArgumentException e) {
                        if (!useBufferOverflowInsteadOfIllegalArg) {
                            throw e;
                        }
                        Log.e(TAG, "Buffer " + this.mBuffer + " does not have enough space to read " + bytesToStore + " bytes", e);
                        throw new BufferOverflowException();
                    }
                }
            }
            this.mBuffer = null;
            this.mLength = 0;
        }
    }

    public boolean cancel() {
        UsbDeviceConnection usbDeviceConnection = this.mConnection;
        if (usbDeviceConnection == null) {
            return false;
        }
        return usbDeviceConnection.cancelRequest(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cancelIfOpen() {
        UsbDeviceConnection usbDeviceConnection;
        if (this.mNativeContext == 0 || ((usbDeviceConnection = this.mConnection) != null && !usbDeviceConnection.isOpen())) {
            Log.w(TAG, "Detected attempt to cancel a request on a connection which isn't open");
            return false;
        }
        return native_cancel();
    }
}
