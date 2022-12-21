package sun.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.p026io.BufferedInputStream;
import java.p026io.BufferedOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectStreamConstants;
import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

public class NetworkClient {
    public static final int DEFAULT_CONNECT_TIMEOUT = -1;
    public static final int DEFAULT_READ_TIMEOUT = -1;
    protected static int defaultConnectTimeout;
    protected static int defaultSoTimeout;
    protected static String encoding;
    protected int connectTimeout = -1;
    protected Proxy proxy = Proxy.NO_PROXY;
    protected int readTimeout = -1;
    public InputStream serverInput;
    public PrintStream serverOutput;
    protected Socket serverSocket = null;

    static {
        final int[] iArr = {0, 0};
        final String[] strArr = {null};
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                iArr[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 0).intValue();
                iArr[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 0).intValue();
                strArr[0] = System.getProperty("file.encoding", "ISO8859_1");
                return null;
            }
        });
        int i = iArr[0];
        if (i != 0) {
            defaultSoTimeout = i;
        }
        int i2 = iArr[1];
        if (i2 != 0) {
            defaultConnectTimeout = i2;
        }
        String str = strArr[0];
        encoding = str;
        try {
            if (!isASCIISuperset(str)) {
                encoding = "ISO8859_1";
            }
        } catch (Exception unused) {
            encoding = "ISO8859_1";
        }
    }

    private static boolean isASCIISuperset(String str) throws Exception {
        return Arrays.equals("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,".getBytes(str), new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, ObjectStreamConstants.TC_REFERENCE, ObjectStreamConstants.TC_CLASSDESC, ObjectStreamConstants.TC_OBJECT, ObjectStreamConstants.TC_STRING, ObjectStreamConstants.TC_ARRAY, ObjectStreamConstants.TC_CLASS, ObjectStreamConstants.TC_BLOCKDATA, ObjectStreamConstants.TC_ENDBLOCKDATA, ObjectStreamConstants.TC_RESET, ObjectStreamConstants.TC_BLOCKDATALONG, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63, 58, 64, 38, 61, 43, 36, 44});
    }

    public void openServer(String str, int i) throws IOException, UnknownHostException {
        if (this.serverSocket != null) {
            closeServer();
        }
        Socket doConnect = doConnect(str, i);
        this.serverSocket = doConnect;
        try {
            this.serverOutput = new PrintStream((OutputStream) new BufferedOutputStream(doConnect.getOutputStream()), true, encoding);
            this.serverInput = new BufferedInputStream(this.serverSocket.getInputStream());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(encoding + "encoding not found", e);
        }
    }

    /* access modifiers changed from: protected */
    public Socket doConnect(String str, int i) throws IOException, UnknownHostException {
        Socket socket;
        Proxy proxy2 = this.proxy;
        if (proxy2 == null) {
            socket = createSocket();
        } else if (proxy2.type() == Proxy.Type.SOCKS) {
            socket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() {
                public Socket run() {
                    return new Socket(NetworkClient.this.proxy);
                }
            });
        } else if (this.proxy.type() == Proxy.Type.DIRECT) {
            socket = createSocket();
        } else {
            socket = new Socket(Proxy.NO_PROXY);
        }
        if (this.connectTimeout >= 0) {
            socket.connect(new InetSocketAddress(str, i), this.connectTimeout);
        } else if (defaultConnectTimeout > 0) {
            socket.connect(new InetSocketAddress(str, i), defaultConnectTimeout);
        } else {
            socket.connect(new InetSocketAddress(str, i));
        }
        int i2 = this.readTimeout;
        if (i2 >= 0) {
            socket.setSoTimeout(i2);
        } else {
            int i3 = defaultSoTimeout;
            if (i3 > 0) {
                socket.setSoTimeout(i3);
            }
        }
        return socket;
    }

    /* access modifiers changed from: protected */
    public Socket createSocket() throws IOException {
        return new Socket();
    }

    /* access modifiers changed from: protected */
    public InetAddress getLocalAddress() throws IOException {
        if (this.serverSocket != null) {
            return (InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() {
                public InetAddress run() {
                    return NetworkClient.this.serverSocket.getLocalAddress();
                }
            });
        }
        throw new IOException("not connected");
    }

    public void closeServer() throws IOException {
        if (serverIsOpen()) {
            this.serverSocket.close();
            this.serverSocket = null;
            this.serverInput = null;
            this.serverOutput = null;
        }
    }

    public boolean serverIsOpen() {
        return this.serverSocket != null;
    }

    public NetworkClient(String str, int i) throws IOException {
        openServer(str, i);
    }

    public NetworkClient() {
    }

    public void setConnectTimeout(int i) {
        this.connectTimeout = i;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setReadTimeout(int i) {
        if (i == -1) {
            i = defaultSoTimeout;
        }
        Socket socket = this.serverSocket;
        if (socket != null && i >= 0) {
            try {
                socket.setSoTimeout(i);
            } catch (IOException unused) {
            }
        }
        this.readTimeout = i;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }
}
