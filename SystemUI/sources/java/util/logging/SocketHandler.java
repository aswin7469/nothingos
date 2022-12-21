package java.util.logging;

import java.net.Socket;
import java.p026io.BufferedOutputStream;
import java.p026io.IOException;
import java.p026io.PrintStream;
import libcore.net.NetworkSecurityPolicy;

public class SocketHandler extends StreamHandler {
    private String host;
    private int port;
    private Socket sock;

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|3|4|5|7) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0075 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void configure() {
        /*
            r5 = this;
            java.util.logging.LogManager r0 = java.util.logging.LogManager.getLogManager()
            java.lang.Class r1 = r5.getClass()
            java.lang.String r1 = r1.getName()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".level"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            java.util.logging.Level r3 = java.util.logging.Level.ALL
            java.util.logging.Level r2 = r0.getLevelProperty(r2, r3)
            r5.setLevel(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".filter"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            java.util.logging.Filter r2 = r0.getFilterProperty(r2, r3)
            r5.setFilter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".formatter"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            java.util.logging.XMLFormatter r4 = new java.util.logging.XMLFormatter
            r4.<init>()
            java.util.logging.Formatter r2 = r0.getFormatterProperty(r2, r4)
            r5.setFormatter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0075 }
            r2.<init>()     // Catch:{ Exception -> 0x0075 }
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r4 = ".encoding"
            r2.append((java.lang.String) r4)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0075 }
            java.lang.String r2 = r0.getStringProperty(r2, r3)     // Catch:{ Exception -> 0x0075 }
            r5.setEncoding(r2)     // Catch:{ Exception -> 0x0075 }
            goto L_0x0078
        L_0x0075:
            r5.setEncoding(r3)     // Catch:{ Exception -> 0x0078 }
        L_0x0078:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".port"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            r4 = 0
            int r2 = r0.getIntProperty(r2, r4)
            r5.port = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r1 = ".host"
            r2.append((java.lang.String) r1)
            java.lang.String r1 = r2.toString()
            java.lang.String r0 = r0.getStringProperty(r1, r3)
            r5.host = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.SocketHandler.configure():void");
    }

    public SocketHandler() throws IOException {
        this.sealed = false;
        configure();
        try {
            connect();
            this.sealed = true;
        } catch (IOException e) {
            PrintStream printStream = System.err;
            printStream.println("SocketHandler: connect failed to " + this.host + ":" + this.port);
            throw e;
        }
    }

    public SocketHandler(String str, int i) throws IOException {
        this.sealed = false;
        configure();
        this.sealed = true;
        this.port = i;
        this.host = str;
        connect();
    }

    private void connect() throws IOException {
        if (this.port == 0) {
            throw new IllegalArgumentException("Bad port: " + this.port);
        } else if (this.host == null) {
            throw new IllegalArgumentException("Null host name: " + this.host);
        } else if (NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
            Socket socket = new Socket(this.host, this.port);
            this.sock = socket;
            setOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } else {
            throw new IOException("Cleartext traffic not permitted");
        }
    }

    public synchronized void close() throws SecurityException {
        super.close();
        Socket socket = this.sock;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException unused) {
            }
        }
        this.sock = null;
    }

    public synchronized void publish(LogRecord logRecord) {
        if (isLoggable(logRecord)) {
            super.publish(logRecord);
            flush();
        }
    }
}
