package com.android.okhttp.internal;

import android.net.ssl.SSLSockets;
import com.android.okhttp.Protocol;
import com.android.okhttp.internal.tls.RealTrustRootIndex;
import com.android.okhttp.internal.tls.TrustRootIndex;
import com.android.okhttp.okio.Buffer;
import dalvik.system.SocketTagger;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.p026io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class Platform {
    private static final OptionalMethod<Socket> GET_ALPN_SELECTED_PROTOCOL;
    private static final AtomicReference<Platform> INSTANCE_HOLDER = new AtomicReference<>(new Platform());
    private static final OptionalMethod<Socket> SET_ALPN_PROTOCOLS;
    private static final OptionalMethod<Socket> SET_HOSTNAME = new OptionalMethod<>((Class<?>) null, "setHostname", String.class);
    private static final OptionalMethod<Socket> SET_USE_SESSION_TICKETS = new OptionalMethod<>((Class<?>) null, "setUseSessionTickets", Boolean.TYPE);

    public void afterHandshake(SSLSocket sSLSocket) {
    }

    public String getPrefix() {
        return "X-Android";
    }

    static {
        Class<byte[]> cls = byte[].class;
        GET_ALPN_SELECTED_PROTOCOL = new OptionalMethod<>(cls, "getAlpnSelectedProtocol", new Class[0]);
        SET_ALPN_PROTOCOLS = new OptionalMethod<>((Class<?>) null, "setAlpnProtocols", cls);
    }

    protected Platform() {
    }

    public static Platform get() {
        return INSTANCE_HOLDER.get();
    }

    public static Platform getAndSetForTest(Platform platform) {
        platform.getClass();
        return INSTANCE_HOLDER.getAndSet(platform);
    }

    public void logW(String str) {
        System.logW(str);
    }

    public void tagSocket(Socket socket) throws SocketException {
        SocketTagger.get().tag(socket);
    }

    public void untagSocket(Socket socket) throws SocketException {
        SocketTagger.get().untag(socket);
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
        SSLParameters sSLParameters = sSLSocket.getSSLParameters();
        if (str != null) {
            if (SSLSockets.isSupportedSocket(sSLSocket)) {
                SSLSockets.setUseSessionTickets(sSLSocket, true);
            } else {
                SET_USE_SESSION_TICKETS.invokeOptionalWithoutCheckedException(sSLSocket, true);
            }
            try {
                sSLParameters.setServerNames(Collections.singletonList(new SNIHostName(str)));
            } catch (IllegalArgumentException unused) {
            }
            if (!isPlatformSocket(sSLSocket)) {
                SET_HOSTNAME.invokeOptionalWithoutCheckedException(sSLSocket, str);
            }
        }
        sSLParameters.setApplicationProtocols(getProtocolIds(list));
        if (!isPlatformSocket(sSLSocket)) {
            OptionalMethod<Socket> optionalMethod = SET_ALPN_PROTOCOLS;
            if (optionalMethod.isSupported(sSLSocket)) {
                optionalMethod.invokeWithoutCheckedException(sSLSocket, concatLengthPrefixed(list));
            }
        }
        sSLSocket.setSSLParameters(sSLParameters);
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        byte[] bArr;
        try {
            return sSLSocket.getApplicationProtocol();
        } catch (UnsupportedOperationException unused) {
            if (GET_ALPN_SELECTED_PROTOCOL.isSupported(sSLSocket) && (bArr = (byte[]) GET_ALPN_SELECTED_PROTOCOL.invokeWithoutCheckedException(sSLSocket, new Object[0])) != null) {
                return new String(bArr, Util.UTF_8);
            }
            return null;
        }
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int i) throws IOException {
        socket.connect(inetSocketAddress, i);
    }

    public X509TrustManager trustManager(SSLSocketFactory sSLSocketFactory) {
        try {
            return (X509TrustManager) readFieldOrNull(readFieldOrNull(sSLSocketFactory, Class.forName("com.android.org.conscrypt.SSLParametersImpl"), "sslParameters"), X509TrustManager.class, "x509TrustManager");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public TrustRootIndex trustRootIndex(X509TrustManager x509TrustManager) {
        return new RealTrustRootIndex(x509TrustManager.getAcceptedIssuers());
    }

    private static <T> T readFieldOrNull(Object obj, Class<T> cls, String str) {
        Object readFieldOrNull;
        Class cls2 = obj.getClass();
        while (cls2 != Object.class) {
            try {
                Field declaredField = cls2.getDeclaredField(str);
                declaredField.setAccessible(true);
                Object obj2 = declaredField.get(obj);
                if (obj2 != null) {
                    if (cls.isInstance(obj2)) {
                        return cls.cast(obj2);
                    }
                }
                return null;
            } catch (NoSuchFieldException unused) {
                cls2 = cls2.getSuperclass();
            } catch (IllegalAccessException unused2) {
                throw new AssertionError();
            }
        }
        if (str.equals("delegate") || (readFieldOrNull = readFieldOrNull(obj, Object.class, "delegate")) == null) {
            return null;
        }
        return readFieldOrNull(readFieldOrNull, cls, str);
    }

    private static boolean isPlatformSocket(SSLSocket sSLSocket) {
        return sSLSocket.getClass().getName().startsWith("com.android.org.conscrypt");
    }

    private static String[] getProtocolIds(List<Protocol> list) {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strArr[i] = list.get(i).toString();
        }
        return strArr;
    }

    static byte[] concatLengthPrefixed(List<Protocol> list) {
        Buffer buffer = new Buffer();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Protocol protocol = list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                buffer.writeByte(protocol.toString().length());
                buffer.writeUtf8(protocol.toString());
            }
        }
        return buffer.readByteArray();
    }
}
