package sun.net.ftp.impl;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.p026io.BufferedInputStream;
import java.p026io.BufferedOutputStream;
import java.p026io.BufferedReader;
import java.p026io.Closeable;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.ObjectStreamConstants;
import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpDirParser;
import sun.net.ftp.FtpProtocolException;
import sun.net.ftp.FtpReplyCode;
import sun.util.logging.PlatformLogger;

public class FtpClient extends sun.net.ftp.FtpClient {
    private static String[] MDTMformats;
    private static SimpleDateFormat[] dateFormats;
    private static int defaultConnectTimeout;
    private static int defaultSoTimeout;
    private static String encoding;
    private static Pattern epsvPat = null;
    /* access modifiers changed from: private */
    public static Pattern linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
    private static final PlatformLogger logger = PlatformLogger.getLogger("sun.net.ftp.FtpClient");
    private static Pattern pasvPat = null;
    private static String[] patStrings = {"([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d\\d:\\d\\d)\\s*(\\p{Print}*)", "([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d{4})\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)"};
    /* access modifiers changed from: private */
    public static int[][] patternGroups = {new int[]{7, 4, 5, 6, 0, 1, 2, 3}, new int[]{7, 4, 5, 0, 6, 1, 2, 3}, new int[]{4, 3, 1, 2, 0, 0, 0, 0}, new int[]{4, 3, 1, 2, 0, 0, 0, 0}};
    /* access modifiers changed from: private */
    public static Pattern[] patterns = new Pattern[patStrings.length];
    private static Pattern transPat = null;
    private int connectTimeout = -1;
    /* access modifiers changed from: private */

    /* renamed from: df */
    public DateFormat f864df = DateFormat.getDateInstance(2, Locale.f700US);

    /* renamed from: in */
    private InputStream f865in;
    private String lastFileName;
    private FtpReplyCode lastReplyCode = null;
    private long lastTransSize = -1;
    private boolean loggedIn = false;
    private FtpDirParser mlsxParser = new MLSxParser();
    private Socket oldSocket;
    private PrintStream out;
    private FtpDirParser parser = new DefaultParser();
    private final boolean passiveMode = true;
    /* access modifiers changed from: private */
    public Proxy proxy;
    private int readTimeout = -1;
    private boolean replyPending = false;
    private long restartOffset = 0;
    /* access modifiers changed from: private */
    public Socket server;
    private InetSocketAddress serverAddr;
    private Vector<String> serverResponse = new Vector<>(1);
    private SSLSocketFactory sslFact;
    private FtpClient.TransferType type = FtpClient.TransferType.BINARY;
    private boolean useCrypto = false;
    private String welcomeMsg;

    public sun.net.ftp.FtpClient enablePassiveMode(boolean z) {
        return this;
    }

    public boolean isPassiveModeEnabled() {
        return true;
    }

    public sun.net.ftp.FtpClient useKerberos() throws FtpProtocolException, IOException {
        return this;
    }

    static {
        encoding = "ISO8859_1";
        final int[] iArr = {0, 0};
        final String[] strArr = {null};
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                iArr[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 0).intValue();
                iArr[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 0).intValue();
                strArr[0] = System.getProperty("file.encoding", "ISO8859_1");
                return null;
            }
        });
        int i = iArr[0];
        if (i == 0) {
            defaultSoTimeout = -1;
        } else {
            defaultSoTimeout = i;
        }
        int i2 = iArr[1];
        if (i2 == 0) {
            defaultConnectTimeout = -1;
        } else {
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
        int i3 = 0;
        while (true) {
            String[] strArr2 = patStrings;
            if (i3 >= strArr2.length) {
                break;
            }
            patterns[i3] = Pattern.compile(strArr2[i3]);
            i3++;
        }
        String[] strArr3 = {"yyyyMMddHHmmss.SSS", "yyyyMMddHHmmss"};
        MDTMformats = strArr3;
        dateFormats = new SimpleDateFormat[strArr3.length];
        for (int i4 = 0; i4 < MDTMformats.length; i4++) {
            dateFormats[i4] = new SimpleDateFormat(MDTMformats[i4]);
            dateFormats[i4].setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    }

    private static boolean isASCIISuperset(String str) throws Exception {
        return Arrays.equals("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,".getBytes(str), new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, ObjectStreamConstants.TC_REFERENCE, ObjectStreamConstants.TC_CLASSDESC, ObjectStreamConstants.TC_OBJECT, ObjectStreamConstants.TC_STRING, ObjectStreamConstants.TC_ARRAY, ObjectStreamConstants.TC_CLASS, ObjectStreamConstants.TC_BLOCKDATA, ObjectStreamConstants.TC_ENDBLOCKDATA, ObjectStreamConstants.TC_RESET, ObjectStreamConstants.TC_BLOCKDATALONG, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63, 58, 64, 38, 61, 43, 36, 44});
    }

    private class DefaultParser implements FtpDirParser {
        private DefaultParser() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x00ac  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x00c3  */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x00e0  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x00f7  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x010b  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x010d A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public sun.net.ftp.FtpDirEntry parseLine(java.lang.String r19) {
            /*
                r18 = this;
                r0 = r19
                java.util.Calendar r1 = java.util.Calendar.getInstance()
                r2 = 1
                int r3 = r1.get(r2)
                r5 = 0
                r6 = r5
                r13 = r6
                r7 = 0
                r8 = 0
                r9 = 0
                r10 = 0
                r11 = 0
                r12 = 0
                r14 = 0
            L_0x0015:
                java.util.regex.Pattern[] r15 = sun.net.ftp.impl.FtpClient.patterns
                int r15 = r15.length
                r4 = 3
                if (r6 >= r15) goto L_0x0113
                java.util.regex.Pattern[] r15 = sun.net.ftp.impl.FtpClient.patterns
                r15 = r15[r6]
                java.util.regex.Matcher r15 = r15.matcher(r0)
                boolean r17 = r15.find()
                if (r17 == 0) goto L_0x010d
                int[][] r7 = sun.net.ftp.impl.FtpClient.patternGroups
                r7 = r7[r6]
                r7 = r7[r5]
                java.lang.String r7 = r15.group((int) r7)
                int[][] r8 = sun.net.ftp.impl.FtpClient.patternGroups
                r8 = r8[r6]
                r8 = r8[r2]
                java.lang.String r10 = r15.group((int) r8)
                int[][] r8 = sun.net.ftp.impl.FtpClient.patternGroups
                r8 = r8[r6]
                r16 = 2
                r8 = r8[r16]
                java.lang.String r8 = r15.group((int) r8)
                int[][] r16 = sun.net.ftp.impl.FtpClient.patternGroups
                r16 = r16[r6]
                r17 = 4
                r16 = r16[r17]
                java.lang.String r2 = ", "
                if (r16 <= 0) goto L_0x0081
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                r5.append((java.lang.String) r8)
                r5.append((java.lang.String) r2)
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r17]
                java.lang.String r2 = r15.group((int) r2)
                r5.append((java.lang.String) r2)
                java.lang.String r2 = r5.toString()
            L_0x007f:
                r8 = r2
                goto L_0x00a2
            L_0x0081:
                int[][] r5 = sun.net.ftp.impl.FtpClient.patternGroups
                r5 = r5[r6]
                r5 = r5[r4]
                if (r5 <= 0) goto L_0x00a2
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                r5.append((java.lang.String) r8)
                r5.append((java.lang.String) r2)
                java.lang.String r2 = java.lang.String.valueOf((int) r3)
                r5.append((java.lang.String) r2)
                java.lang.String r2 = r5.toString()
                goto L_0x007f
            L_0x00a2:
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r4]
                if (r2 <= 0) goto L_0x00b8
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r4]
                java.lang.String r9 = r15.group((int) r2)
            L_0x00b8:
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r4 = 5
                r2 = r2[r4]
                if (r2 <= 0) goto L_0x00d5
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r4]
                java.lang.String r14 = r15.group((int) r2)
                java.lang.String r2 = "d"
                boolean r13 = r14.startsWith(r2)
            L_0x00d5:
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r4 = 6
                r2 = r2[r4]
                if (r2 <= 0) goto L_0x00ec
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r4]
                java.lang.String r11 = r15.group((int) r2)
            L_0x00ec:
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r4 = 7
                r2 = r2[r4]
                if (r2 <= 0) goto L_0x0103
                int[][] r2 = sun.net.ftp.impl.FtpClient.patternGroups
                r2 = r2[r6]
                r2 = r2[r4]
                java.lang.String r12 = r15.group((int) r2)
            L_0x0103:
                java.lang.String r2 = "<DIR>"
                boolean r2 = r2.equals(r10)
                if (r2 == 0) goto L_0x010d
                r10 = 0
                r13 = 1
            L_0x010d:
                int r6 = r6 + 1
                r2 = 1
                r5 = 0
                goto L_0x0015
            L_0x0113:
                if (r7 == 0) goto L_0x01c3
                r2 = r18
                sun.net.ftp.impl.FtpClient r2 = sun.net.ftp.impl.FtpClient.this     // Catch:{ Exception -> 0x0122 }
                java.text.DateFormat r2 = r2.f864df     // Catch:{ Exception -> 0x0122 }
                java.util.Date r2 = r2.parse(r8)     // Catch:{ Exception -> 0x0122 }
                goto L_0x0123
            L_0x0122:
                r2 = 0
            L_0x0123:
                if (r2 == 0) goto L_0x0151
                if (r9 == 0) goto L_0x0151
                java.lang.String r3 = ":"
                int r3 = r9.indexOf((java.lang.String) r3)
                r1.setTime(r2)
                r2 = 0
                java.lang.String r5 = r9.substring(r2, r3)
                int r2 = java.lang.Integer.parseInt(r5)
                r5 = 10
                r1.set(r5, r2)
                r2 = 1
                int r3 = r3 + r2
                java.lang.String r2 = r9.substring(r3)
                int r2 = java.lang.Integer.parseInt(r2)
                r3 = 12
                r1.set(r3, r2)
                java.util.Date r2 = r1.getTime()
            L_0x0151:
                java.util.regex.Pattern r1 = sun.net.ftp.impl.FtpClient.linkp
                java.util.regex.Matcher r1 = r1.matcher(r7)
                boolean r3 = r1.find()
                if (r3 == 0) goto L_0x0165
                r3 = 1
                java.lang.String r7 = r1.group((int) r3)
                goto L_0x0166
            L_0x0165:
                r3 = 1
            L_0x0166:
                r1 = 2
                int[] r1 = new int[r1]
                r1 = {3, 3} // fill-array
                java.lang.Class<java.lang.Boolean> r5 = java.lang.Boolean.TYPE
                java.lang.Object r1 = java.lang.reflect.Array.newInstance((java.lang.Class<?>) r5, (int[]) r1)
                boolean[][] r1 = (boolean[][]) r1
                r5 = 0
            L_0x0175:
                if (r5 >= r4) goto L_0x0192
                r6 = 0
            L_0x0178:
                if (r6 >= r4) goto L_0x018f
                r8 = r1[r5]
                int r9 = r5 * 3
                int r9 = r9 + r6
                char r9 = r14.charAt(r9)
                r15 = 45
                if (r9 == r15) goto L_0x0189
                r9 = r3
                goto L_0x018a
            L_0x0189:
                r9 = 0
            L_0x018a:
                r8[r6] = r9
                int r6 = r6 + 1
                goto L_0x0178
            L_0x018f:
                int r5 = r5 + 1
                goto L_0x0175
            L_0x0192:
                sun.net.ftp.FtpDirEntry r3 = new sun.net.ftp.FtpDirEntry
                r3.<init>(r7)
                sun.net.ftp.FtpDirEntry r4 = r3.setUser(r11)
                r4.setGroup(r12)
                long r4 = java.lang.Long.parseLong(r10)
                sun.net.ftp.FtpDirEntry r4 = r3.setSize(r4)
                r4.setLastModified(r2)
                r3.setPermissions(r1)
                if (r13 == 0) goto L_0x01b1
                sun.net.ftp.FtpDirEntry$Type r0 = sun.net.ftp.FtpDirEntry.Type.DIR
                goto L_0x01bf
            L_0x01b1:
                r1 = 0
                char r0 = r0.charAt(r1)
                r1 = 108(0x6c, float:1.51E-43)
                if (r0 != r1) goto L_0x01bd
                sun.net.ftp.FtpDirEntry$Type r0 = sun.net.ftp.FtpDirEntry.Type.LINK
                goto L_0x01bf
            L_0x01bd:
                sun.net.ftp.FtpDirEntry$Type r0 = sun.net.ftp.FtpDirEntry.Type.FILE
            L_0x01bf:
                r3.setType(r0)
                return r3
            L_0x01c3:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.net.ftp.impl.FtpClient.DefaultParser.parseLine(java.lang.String):sun.net.ftp.FtpDirEntry");
        }
    }

    private class MLSxParser implements FtpDirParser {

        /* renamed from: df */
        private SimpleDateFormat f867df;

        private MLSxParser() {
            this.f867df = new SimpleDateFormat("yyyyMMddhhmmss");
        }

        public FtpDirEntry parseLine(String str) {
            String str2;
            String str3;
            Date date;
            String str4;
            int lastIndexOf = str.lastIndexOf(NavigationBarInflaterView.GRAVITY_SEPARATOR);
            if (lastIndexOf > 0) {
                str3 = str.substring(lastIndexOf + 1).trim();
                str2 = str.substring(0, lastIndexOf);
            } else {
                str3 = str.trim();
                str2 = "";
            }
            FtpDirEntry ftpDirEntry = new FtpDirEntry(str3);
            while (!str2.isEmpty()) {
                int indexOf = str2.indexOf(NavigationBarInflaterView.GRAVITY_SEPARATOR);
                if (indexOf > 0) {
                    String substring = str2.substring(0, indexOf);
                    str4 = str2.substring(indexOf + 1);
                    str2 = substring;
                } else {
                    str4 = "";
                }
                int indexOf2 = str2.indexOf("=");
                if (indexOf2 > 0) {
                    ftpDirEntry.addFact(str2.substring(0, indexOf2), str2.substring(indexOf2 + 1));
                }
                str2 = str4;
            }
            String fact = ftpDirEntry.getFact("Size");
            if (fact != null) {
                ftpDirEntry.setSize(Long.parseLong(fact));
            }
            String fact2 = ftpDirEntry.getFact("Modify");
            Date date2 = null;
            if (fact2 != null) {
                try {
                    date = this.f867df.parse(fact2);
                } catch (ParseException unused) {
                    date = null;
                }
                if (date != null) {
                    ftpDirEntry.setLastModified(date);
                }
            }
            String fact3 = ftpDirEntry.getFact("Create");
            if (fact3 != null) {
                try {
                    date2 = this.f867df.parse(fact3);
                } catch (ParseException unused2) {
                }
                if (date2 != null) {
                    ftpDirEntry.setCreated(date2);
                }
            }
            String fact4 = ftpDirEntry.getFact("Type");
            if (fact4 != null) {
                if (fact4.equalsIgnoreCase("file")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.FILE);
                }
                if (fact4.equalsIgnoreCase("dir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.DIR);
                }
                if (fact4.equalsIgnoreCase("cdir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.CDIR);
                }
                if (fact4.equalsIgnoreCase("pdir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.PDIR);
                }
            }
            return ftpDirEntry;
        }
    }

    private void getTransferSize() {
        this.lastTransSize = -1;
        String lastResponseString = getLastResponseString();
        if (transPat == null) {
            transPat = Pattern.compile("150 Opening .*\\((\\d+) bytes\\).");
        }
        Matcher matcher = transPat.matcher(lastResponseString);
        if (matcher.find()) {
            this.lastTransSize = Long.parseLong(matcher.group(1));
        }
    }

    private void getTransferName() {
        this.lastFileName = null;
        String lastResponseString = getLastResponseString();
        int indexOf = lastResponseString.indexOf("unique file name:");
        int lastIndexOf = lastResponseString.lastIndexOf(41);
        if (indexOf >= 0) {
            this.lastFileName = lastResponseString.substring(indexOf + 17, lastIndexOf);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int readServerResponse() throws java.p026io.IOException {
        /*
            r10 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r1 = 32
            r0.<init>((int) r1)
            java.util.Vector<java.lang.String> r1 = r10.serverResponse
            r2 = 0
            r1.setSize(r2)
            r1 = -1
            r3 = r1
        L_0x000f:
            java.io.InputStream r4 = r10.f865in
            int r4 = r4.read()
            if (r4 == r1) goto L_0x002e
            r5 = 10
            r6 = 13
            if (r4 != r6) goto L_0x0028
            java.io.InputStream r4 = r10.f865in
            int r4 = r4.read()
            if (r4 == r5) goto L_0x0028
            r0.append((char) r6)
        L_0x0028:
            char r6 = (char) r4
            r0.append((char) r6)
            if (r4 != r5) goto L_0x000f
        L_0x002e:
            java.lang.String r4 = r0.toString()
            r0.setLength(r2)
            sun.util.logging.PlatformLogger r5 = logger
            sun.util.logging.PlatformLogger$Level r6 = sun.util.logging.PlatformLogger.Level.FINEST
            boolean r6 = r5.isLoggable(r6)
            if (r6 == 0) goto L_0x005a
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "Server ["
            r6.<init>((java.lang.String) r7)
            java.net.InetSocketAddress r7 = r10.serverAddr
            r6.append((java.lang.Object) r7)
            java.lang.String r7 = "] --> "
            r6.append((java.lang.String) r7)
            r6.append((java.lang.String) r4)
            java.lang.String r6 = r6.toString()
            r5.finest(r6)
        L_0x005a:
            int r5 = r4.length()
            r6 = 3
            if (r5 != 0) goto L_0x0063
        L_0x0061:
            r5 = r1
            goto L_0x006b
        L_0x0063:
            java.lang.String r5 = r4.substring(r2, r6)     // Catch:{ NumberFormatException -> 0x0061, StringIndexOutOfBoundsException -> 0x000f }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ NumberFormatException -> 0x0061, StringIndexOutOfBoundsException -> 0x000f }
        L_0x006b:
            java.util.Vector<java.lang.String> r7 = r10.serverResponse
            r7.addElement(r4)
            r7 = 45
            r8 = 4
            if (r3 == r1) goto L_0x0084
            if (r5 != r3) goto L_0x000f
            int r9 = r4.length()
            if (r9 < r8) goto L_0x0093
            char r4 = r4.charAt(r6)
            if (r4 != r7) goto L_0x0093
            goto L_0x000f
        L_0x0084:
            int r3 = r4.length()
            if (r3 < r8) goto L_0x0093
            char r3 = r4.charAt(r6)
            if (r3 != r7) goto L_0x0093
            r3 = r5
            goto L_0x000f
        L_0x0093:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.ftp.impl.FtpClient.readServerResponse():int");
    }

    private void sendServer(String str) {
        this.out.print(str);
        PlatformLogger platformLogger = logger;
        if (platformLogger.isLoggable(PlatformLogger.Level.FINEST)) {
            platformLogger.finest("Server [" + this.serverAddr + "] <-- " + str);
        }
    }

    private String getResponseString() {
        return this.serverResponse.elementAt(0);
    }

    private Vector<String> getResponseStrings() {
        return this.serverResponse;
    }

    private boolean readReply() throws IOException {
        FtpReplyCode find = FtpReplyCode.find(readServerResponse());
        this.lastReplyCode = find;
        if (find.isPositivePreliminary()) {
            this.replyPending = true;
            return true;
        } else if (!this.lastReplyCode.isPositiveCompletion() && !this.lastReplyCode.isPositiveIntermediate()) {
            return false;
        } else {
            if (this.lastReplyCode == FtpReplyCode.CLOSING_DATA_CONNECTION) {
                getTransferName();
            }
            return true;
        }
    }

    private boolean issueCommand(String str) throws IOException, FtpProtocolException {
        if (isConnected()) {
            if (this.replyPending) {
                try {
                    completePending();
                } catch (FtpProtocolException unused) {
                }
            }
            if (str.indexOf(10) == -1) {
                sendServer(str + "\r\n");
                return readReply();
            }
            FtpProtocolException ftpProtocolException = new FtpProtocolException("Illegal FTP command");
            ftpProtocolException.initCause(new IllegalArgumentException("Illegal carriage return"));
            throw ftpProtocolException;
        }
        throw new IllegalStateException("Not connected");
    }

    private void issueCommandCheck(String str) throws FtpProtocolException, IOException {
        if (!issueCommand(str)) {
            throw new FtpProtocolException(str + ":" + getResponseString(), getLastReplyCode());
        }
    }

    private Socket openPassiveDataConnection(String str) throws FtpProtocolException, IOException {
        InetSocketAddress inetSocketAddress;
        Socket socket;
        if (issueCommand("EPSV ALL")) {
            issueCommandCheck("EPSV");
            String responseString = getResponseString();
            if (epsvPat == null) {
                epsvPat = Pattern.compile("^229 .* \\(\\|\\|\\|(\\d+)\\|\\)");
            }
            Matcher matcher = epsvPat.matcher(responseString);
            if (matcher.find()) {
                int parseInt = Integer.parseInt(matcher.group(1));
                InetAddress inetAddress = this.server.getInetAddress();
                if (inetAddress != null) {
                    inetSocketAddress = new InetSocketAddress(inetAddress, parseInt);
                } else {
                    inetSocketAddress = InetSocketAddress.createUnresolved(this.serverAddr.getHostName(), parseInt);
                }
            } else {
                throw new FtpProtocolException("EPSV failed : " + responseString);
            }
        } else {
            issueCommandCheck("PASV");
            String responseString2 = getResponseString();
            if (pasvPat == null) {
                pasvPat = Pattern.compile("227 .* \\(?(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)?");
            }
            Matcher matcher2 = pasvPat.matcher(responseString2);
            if (matcher2.find()) {
                inetSocketAddress = new InetSocketAddress(matcher2.group(1).replace(',', '.'), Integer.parseInt(matcher2.group(3)) + (Integer.parseInt(matcher2.group(2)) << 8));
            } else {
                throw new FtpProtocolException("PASV failed : " + responseString2);
            }
        }
        Proxy proxy2 = this.proxy;
        if (proxy2 == null) {
            socket = new Socket();
        } else if (proxy2.type() == Proxy.Type.SOCKS) {
            socket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() {
                public Socket run() {
                    return new Socket(FtpClient.this.proxy);
                }
            });
        } else {
            socket = new Socket(Proxy.NO_PROXY);
        }
        socket.bind(new InetSocketAddress((InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() {
            public InetAddress run() {
                return FtpClient.this.server.getLocalAddress();
            }
        }), 0));
        int i = this.connectTimeout;
        if (i >= 0) {
            socket.connect(inetSocketAddress, i);
        } else {
            int i2 = defaultConnectTimeout;
            if (i2 > 0) {
                socket.connect(inetSocketAddress, i2);
            } else {
                socket.connect(inetSocketAddress);
            }
        }
        int i3 = this.readTimeout;
        if (i3 >= 0) {
            socket.setSoTimeout(i3);
        } else {
            int i4 = defaultSoTimeout;
            if (i4 > 0) {
                socket.setSoTimeout(i4);
            }
        }
        if (this.useCrypto) {
            try {
                socket = this.sslFact.createSocket(socket, inetSocketAddress.getHostName(), inetSocketAddress.getPort(), true);
            } catch (Exception e) {
                throw new FtpProtocolException("Can't open secure data channel: " + e);
            }
        }
        if (issueCommand(str)) {
            return socket;
        }
        socket.close();
        if (getLastReplyCode() == FtpReplyCode.FILE_UNAVAILABLE) {
            throw new FileNotFoundException(str);
        }
        throw new FtpProtocolException(str + ":" + getResponseString(), getLastReplyCode());
    }

    private Socket openDataConnection(String str) throws FtpProtocolException, IOException {
        ServerSocket serverSocket;
        try {
            return openPassiveDataConnection(str);
        } catch (FtpProtocolException e) {
            String message = e.getMessage();
            if (message.startsWith("PASV") || message.startsWith("EPSV")) {
                Proxy proxy2 = this.proxy;
                if (proxy2 == null || proxy2.type() != Proxy.Type.SOCKS) {
                    serverSocket = new ServerSocket(0, 1, this.server.getLocalAddress());
                    InetAddress inetAddress = serverSocket.getInetAddress();
                    if (inetAddress.isAnyLocalAddress()) {
                        inetAddress = this.server.getLocalAddress();
                    }
                    StringBuilder sb = new StringBuilder("EPRT |");
                    sb.append(inetAddress instanceof Inet6Address ? "2" : "1");
                    sb.append("|");
                    sb.append(inetAddress.getHostAddress());
                    sb.append("|");
                    sb.append(serverSocket.getLocalPort());
                    sb.append("|");
                    if (!issueCommand(sb.toString()) || !issueCommand(str)) {
                        String str2 = "PORT ";
                        byte[] address = inetAddress.getAddress();
                        for (int i = 0; i < address.length; i++) {
                            str2 = str2 + (address[i] & 255) + NavigationBarInflaterView.BUTTON_SEPARATOR;
                        }
                        issueCommandCheck(str2 + ((serverSocket.getLocalPort() >>> 8) & 255) + NavigationBarInflaterView.BUTTON_SEPARATOR + (serverSocket.getLocalPort() & 255));
                        issueCommandCheck(str);
                    }
                    int i2 = this.connectTimeout;
                    if (i2 >= 0) {
                        serverSocket.setSoTimeout(i2);
                    } else {
                        int i3 = defaultConnectTimeout;
                        if (i3 > 0) {
                            serverSocket.setSoTimeout(i3);
                        }
                    }
                    Socket accept = serverSocket.accept();
                    int i4 = this.readTimeout;
                    if (i4 >= 0) {
                        accept.setSoTimeout(i4);
                    } else {
                        int i5 = defaultSoTimeout;
                        if (i5 > 0) {
                            accept.setSoTimeout(i5);
                        }
                    }
                    serverSocket.close();
                    if (!this.useCrypto) {
                        return accept;
                    }
                    try {
                        return this.sslFact.createSocket(accept, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
                    } catch (Exception e2) {
                        throw new IOException(e2.getLocalizedMessage());
                    }
                } else {
                    throw new FtpProtocolException("Passive mode failed");
                }
            } else {
                throw e;
            }
        } catch (Throwable th) {
            serverSocket.close();
            throw th;
        }
    }

    private InputStream createInputStream(InputStream inputStream) {
        return this.type == FtpClient.TransferType.ASCII ? new TelnetInputStream(inputStream, false) : inputStream;
    }

    private OutputStream createOutputStream(OutputStream outputStream) {
        return this.type == FtpClient.TransferType.ASCII ? new TelnetOutputStream(outputStream, false) : outputStream;
    }

    protected FtpClient() {
    }

    public static sun.net.ftp.FtpClient create() {
        return new FtpClient();
    }

    public sun.net.ftp.FtpClient setConnectTimeout(int i) {
        this.connectTimeout = i;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public sun.net.ftp.FtpClient setReadTimeout(int i) {
        this.readTimeout = i;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public sun.net.ftp.FtpClient setProxy(Proxy proxy2) {
        this.proxy = proxy2;
        return this;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    private void tryConnect(InetSocketAddress inetSocketAddress, int i) throws IOException {
        if (isConnected()) {
            disconnect();
        }
        Socket doConnect = doConnect(inetSocketAddress, i);
        this.server = doConnect;
        try {
            this.out = new PrintStream((OutputStream) new BufferedOutputStream(doConnect.getOutputStream()), true, encoding);
            this.f865in = new BufferedInputStream(this.server.getInputStream());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(encoding + "encoding not found", e);
        }
    }

    private Socket doConnect(InetSocketAddress inetSocketAddress, int i) throws IOException {
        Socket socket;
        Proxy proxy2 = this.proxy;
        if (proxy2 == null) {
            socket = new Socket();
        } else if (proxy2.type() == Proxy.Type.SOCKS) {
            socket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() {
                public Socket run() {
                    return new Socket(FtpClient.this.proxy);
                }
            });
        } else {
            socket = new Socket(Proxy.NO_PROXY);
        }
        if (i >= 0) {
            socket.connect(inetSocketAddress, i);
        } else {
            int i2 = this.connectTimeout;
            if (i2 >= 0) {
                socket.connect(inetSocketAddress, i2);
            } else {
                int i3 = defaultConnectTimeout;
                if (i3 > 0) {
                    socket.connect(inetSocketAddress, i3);
                } else {
                    socket.connect(inetSocketAddress);
                }
            }
        }
        int i4 = this.readTimeout;
        if (i4 >= 0) {
            socket.setSoTimeout(i4);
        } else {
            int i5 = defaultSoTimeout;
            if (i5 > 0) {
                socket.setSoTimeout(i5);
            }
        }
        return socket;
    }

    private void disconnect() throws IOException {
        if (isConnected()) {
            this.server.close();
        }
        this.server = null;
        this.f865in = null;
        this.out = null;
        this.lastTransSize = -1;
        this.lastFileName = null;
        this.restartOffset = 0;
        this.welcomeMsg = null;
        this.lastReplyCode = null;
        this.serverResponse.setSize(0);
    }

    public boolean isConnected() {
        return this.server != null;
    }

    public SocketAddress getServerAddress() {
        Socket socket = this.server;
        if (socket == null) {
            return null;
        }
        return socket.getRemoteSocketAddress();
    }

    public sun.net.ftp.FtpClient connect(SocketAddress socketAddress) throws FtpProtocolException, IOException {
        return connect(socketAddress, -1);
    }

    public sun.net.ftp.FtpClient connect(SocketAddress socketAddress, int i) throws FtpProtocolException, IOException {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            this.serverAddr = inetSocketAddress;
            tryConnect(inetSocketAddress, i);
            if (readReply()) {
                this.welcomeMsg = getResponseString().substring(4);
                return this;
            }
            throw new FtpProtocolException("Welcome message: " + getResponseString(), this.lastReplyCode);
        }
        throw new IllegalArgumentException("Wrong address type");
    }

    private void tryLogin(String str, char[] cArr) throws FtpProtocolException, IOException {
        issueCommandCheck("USER " + str);
        if (this.lastReplyCode == FtpReplyCode.NEED_PASSWORD && cArr != null && cArr.length > 0) {
            issueCommandCheck("PASS " + String.valueOf(cArr));
        }
    }

    public sun.net.ftp.FtpClient login(String str, char[] cArr) throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("User name can't be null or empty");
        } else {
            tryLogin(str, cArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < this.serverResponse.size(); i++) {
                String elementAt = this.serverResponse.elementAt(i);
                if (elementAt != null) {
                    if (elementAt.length() >= 4 && elementAt.startsWith("230")) {
                        elementAt = elementAt.substring(4);
                    }
                    stringBuffer.append(elementAt);
                }
            }
            this.welcomeMsg = stringBuffer.toString();
            this.loggedIn = true;
            return this;
        }
    }

    public sun.net.ftp.FtpClient login(String str, char[] cArr, String str2) throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("User name can't be null or empty");
        } else {
            tryLogin(str, cArr);
            if (this.lastReplyCode == FtpReplyCode.NEED_ACCOUNT) {
                issueCommandCheck("ACCT " + str2);
            }
            StringBuffer stringBuffer = new StringBuffer();
            Vector<String> vector = this.serverResponse;
            if (vector != null) {
                Iterator<String> it = vector.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (next != null) {
                        if (next.length() >= 4 && next.startsWith("230")) {
                            next = next.substring(4);
                        }
                        stringBuffer.append(next);
                    }
                }
            }
            this.welcomeMsg = stringBuffer.toString();
            this.loggedIn = true;
            return this;
        }
    }

    public void close() throws IOException {
        if (isConnected()) {
            try {
                issueCommand("QUIT");
            } catch (FtpProtocolException unused) {
            }
            this.loggedIn = false;
        }
        disconnect();
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public sun.net.ftp.FtpClient changeDirectory(String str) throws FtpProtocolException, IOException {
        if (str == null || "".equals(str)) {
            throw new IllegalArgumentException("directory can't be null or empty");
        }
        issueCommandCheck("CWD " + str);
        return this;
    }

    public sun.net.ftp.FtpClient changeToParentDirectory() throws FtpProtocolException, IOException {
        issueCommandCheck("CDUP");
        return this;
    }

    public String getWorkingDirectory() throws FtpProtocolException, IOException {
        issueCommandCheck("PWD");
        String responseString = getResponseString();
        if (!responseString.startsWith("257")) {
            return null;
        }
        return responseString.substring(5, responseString.lastIndexOf(34));
    }

    public sun.net.ftp.FtpClient setRestartOffset(long j) {
        if (j >= 0) {
            this.restartOffset = j;
            return this;
        }
        throw new IllegalArgumentException("offset can't be negative");
    }

    /* JADX INFO: finally extract failed */
    public sun.net.ftp.FtpClient getFile(String str, OutputStream outputStream) throws FtpProtocolException, IOException {
        if (this.restartOffset > 0) {
            try {
                Socket openDataConnection = openDataConnection("REST " + this.restartOffset);
                this.restartOffset = 0;
                issueCommandCheck("RETR " + str);
                getTransferSize();
                InputStream createInputStream = createInputStream(openDataConnection.getInputStream());
                byte[] bArr = new byte[15000];
                while (true) {
                    int read = createInputStream.read(bArr);
                    if (read < 0) {
                        break;
                    } else if (read > 0) {
                        outputStream.write(bArr, 0, read);
                    }
                }
                createInputStream.close();
            } catch (Throwable th) {
                this.restartOffset = 0;
                throw th;
            }
        } else {
            Socket openDataConnection2 = openDataConnection("RETR " + str);
            getTransferSize();
            InputStream createInputStream2 = createInputStream(openDataConnection2.getInputStream());
            byte[] bArr2 = new byte[15000];
            while (true) {
                int read2 = createInputStream2.read(bArr2);
                if (read2 < 0) {
                    break;
                } else if (read2 > 0) {
                    outputStream.write(bArr2, 0, read2);
                }
            }
            createInputStream2.close();
        }
        return completePending();
    }

    public InputStream getFileStream(String str) throws FtpProtocolException, IOException {
        if (this.restartOffset > 0) {
            try {
                Socket openDataConnection = openDataConnection("REST " + this.restartOffset);
                if (openDataConnection == null) {
                    return null;
                }
                issueCommandCheck("RETR " + str);
                getTransferSize();
                return createInputStream(openDataConnection.getInputStream());
            } finally {
                this.restartOffset = 0;
            }
        } else {
            Socket openDataConnection2 = openDataConnection("RETR " + str);
            if (openDataConnection2 == null) {
                return null;
            }
            getTransferSize();
            return createInputStream(openDataConnection2.getInputStream());
        }
    }

    public OutputStream putFileStream(String str, boolean z) throws FtpProtocolException, IOException {
        String str2 = z ? "STOU " : "STOR ";
        Socket openDataConnection = openDataConnection(str2 + str);
        if (openDataConnection == null) {
            return null;
        }
        return new TelnetOutputStream(openDataConnection.getOutputStream(), this.type == FtpClient.TransferType.BINARY);
    }

    public sun.net.ftp.FtpClient putFile(String str, InputStream inputStream, boolean z) throws FtpProtocolException, IOException {
        String str2 = z ? "STOU " : "STOR ";
        if (this.type == FtpClient.TransferType.BINARY) {
            OutputStream createOutputStream = createOutputStream(openDataConnection(str2 + str).getOutputStream());
            byte[] bArr = new byte[15000];
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    break;
                } else if (read > 0) {
                    createOutputStream.write(bArr, 0, read);
                }
            }
            createOutputStream.close();
        }
        return completePending();
    }

    public sun.net.ftp.FtpClient appendFile(String str, InputStream inputStream) throws FtpProtocolException, IOException {
        OutputStream createOutputStream = createOutputStream(openDataConnection("APPE " + str).getOutputStream());
        byte[] bArr = new byte[15000];
        while (true) {
            int read = inputStream.read(bArr);
            if (read < 0) {
                createOutputStream.close();
                return completePending();
            } else if (read > 0) {
                createOutputStream.write(bArr, 0, read);
            }
        }
    }

    public sun.net.ftp.FtpClient rename(String str, String str2) throws FtpProtocolException, IOException {
        issueCommandCheck("RNFR " + str);
        issueCommandCheck("RNTO " + str2);
        return this;
    }

    public sun.net.ftp.FtpClient deleteFile(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("DELE " + str);
        return this;
    }

    public sun.net.ftp.FtpClient makeDirectory(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("MKD " + str);
        return this;
    }

    public sun.net.ftp.FtpClient removeDirectory(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("RMD " + str);
        return this;
    }

    public sun.net.ftp.FtpClient noop() throws FtpProtocolException, IOException {
        issueCommandCheck("NOOP");
        return this;
    }

    public String getStatus(String str) throws FtpProtocolException, IOException {
        String str2;
        if (str == null) {
            str2 = "STAT";
        } else {
            str2 = "STAT " + str;
        }
        issueCommandCheck(str2);
        Vector<String> responseStrings = getResponseStrings();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < responseStrings.size() - 1; i++) {
            stringBuffer.append(responseStrings.get(i));
        }
        return stringBuffer.toString();
    }

    public List<String> getFeatures() throws FtpProtocolException, IOException {
        ArrayList arrayList = new ArrayList();
        issueCommandCheck("FEAT");
        Vector<String> responseStrings = getResponseStrings();
        for (int i = 1; i < responseStrings.size() - 1; i++) {
            String str = responseStrings.get(i);
            arrayList.add(str.substring(1, str.length() - 1));
        }
        return arrayList;
    }

    public sun.net.ftp.FtpClient abort() throws FtpProtocolException, IOException {
        issueCommandCheck("ABOR");
        return this;
    }

    public sun.net.ftp.FtpClient completePending() throws FtpProtocolException, IOException {
        while (this.replyPending) {
            this.replyPending = false;
            if (!readReply()) {
                throw new FtpProtocolException(getLastResponseString(), this.lastReplyCode);
            }
        }
        return this;
    }

    public sun.net.ftp.FtpClient reInit() throws FtpProtocolException, IOException {
        issueCommandCheck("REIN");
        this.loggedIn = false;
        if (this.useCrypto) {
            Socket socket = this.server;
            if (socket instanceof SSLSocket) {
                ((SSLSocket) socket).getSession().invalidate();
                Socket socket2 = this.oldSocket;
                this.server = socket2;
                this.oldSocket = null;
                try {
                    this.out = new PrintStream((OutputStream) new BufferedOutputStream(socket2.getOutputStream()), true, encoding);
                    this.f865in = new BufferedInputStream(this.server.getInputStream());
                } catch (UnsupportedEncodingException e) {
                    throw new InternalError(encoding + "encoding not found", e);
                }
            }
        }
        this.useCrypto = false;
        return this;
    }

    public sun.net.ftp.FtpClient setType(FtpClient.TransferType transferType) throws FtpProtocolException, IOException {
        this.type = transferType;
        String str = transferType == FtpClient.TransferType.ASCII ? "TYPE A" : "NOOP";
        if (transferType == FtpClient.TransferType.BINARY) {
            str = "TYPE I";
        }
        if (transferType == FtpClient.TransferType.EBCDIC) {
            str = "TYPE E";
        }
        issueCommandCheck(str);
        return this;
    }

    public InputStream list(String str) throws FtpProtocolException, IOException {
        String str2;
        if (str == null) {
            str2 = "LIST";
        } else {
            str2 = "LIST " + str;
        }
        Socket openDataConnection = openDataConnection(str2);
        if (openDataConnection != null) {
            return createInputStream(openDataConnection.getInputStream());
        }
        return null;
    }

    public InputStream nameList(String str) throws FtpProtocolException, IOException {
        String str2;
        if (str == null) {
            str2 = "NLST";
        } else {
            str2 = "NLST " + str;
        }
        Socket openDataConnection = openDataConnection(str2);
        if (openDataConnection != null) {
            return createInputStream(openDataConnection.getInputStream());
        }
        return null;
    }

    public long getSize(String str) throws FtpProtocolException, IOException {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("path can't be null or empty");
        }
        issueCommandCheck("SIZE " + str);
        if (this.lastReplyCode != FtpReplyCode.FILE_STATUS) {
            return -1;
        }
        String responseString = getResponseString();
        return Long.parseLong(responseString.substring(4, responseString.length() - 1));
    }

    public Date getLastModified(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("MDTM " + str);
        if (this.lastReplyCode == FtpReplyCode.FILE_STATUS) {
            String substring = getResponseString().substring(4);
            Date date = null;
            for (SimpleDateFormat parse : dateFormats) {
                try {
                    date = parse.parse(substring);
                } catch (ParseException unused) {
                }
                if (date != null) {
                    return date;
                }
            }
        }
        return null;
    }

    public sun.net.ftp.FtpClient setDirParser(FtpDirParser ftpDirParser) {
        this.parser = ftpDirParser;
        return this;
    }

    private class FtpFileIterator implements Iterator<FtpDirEntry>, Closeable {
        private boolean eof = false;
        private FtpDirParser fparser;

        /* renamed from: in */
        private BufferedReader f866in;
        private FtpDirEntry nextFile = null;

        public FtpFileIterator(FtpDirParser ftpDirParser, BufferedReader bufferedReader) {
            this.f866in = bufferedReader;
            this.fparser = ftpDirParser;
            readNext();
        }

        private void readNext() {
            String readLine;
            this.nextFile = null;
            if (!this.eof) {
                do {
                    try {
                        readLine = this.f866in.readLine();
                        if (readLine != null) {
                            FtpDirEntry parseLine = this.fparser.parseLine(readLine);
                            this.nextFile = parseLine;
                            if (parseLine != null) {
                                return;
                            }
                        }
                    } catch (IOException unused) {
                    }
                } while (readLine != null);
                this.f866in.close();
                this.eof = true;
            }
        }

        public boolean hasNext() {
            return this.nextFile != null;
        }

        public FtpDirEntry next() {
            FtpDirEntry ftpDirEntry = this.nextFile;
            readNext();
            return ftpDirEntry;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void close() throws IOException {
            BufferedReader bufferedReader = this.f866in;
            if (bufferedReader != null && !this.eof) {
                bufferedReader.close();
            }
            this.eof = true;
            this.nextFile = null;
        }
    }

    public Iterator<FtpDirEntry> listFiles(String str) throws FtpProtocolException, IOException {
        Socket socket;
        String str2;
        String str3;
        if (str == null) {
            str3 = "MLSD";
        } else {
            try {
                str3 = "MLSD " + str;
            } catch (FtpProtocolException unused) {
                socket = null;
            }
        }
        socket = openDataConnection(str3);
        if (socket != null) {
            return new FtpFileIterator(this.mlsxParser, new BufferedReader(new InputStreamReader(socket.getInputStream())));
        }
        if (str == null) {
            str2 = "LIST";
        } else {
            str2 = "LIST " + str;
        }
        Socket openDataConnection = openDataConnection(str2);
        if (openDataConnection == null) {
            return null;
        }
        return new FtpFileIterator(this.parser, new BufferedReader(new InputStreamReader(openDataConnection.getInputStream())));
    }

    private boolean sendSecurityData(byte[] bArr) throws IOException, FtpProtocolException {
        String encode = new BASE64Encoder().encode(bArr);
        return issueCommand("ADAT " + encode);
    }

    private byte[] getSecurityData() {
        String lastResponseString = getLastResponseString();
        if (!lastResponseString.substring(4, 9).equalsIgnoreCase("ADAT=")) {
            return null;
        }
        try {
            return new BASE64Decoder().decodeBuffer(lastResponseString.substring(9, lastResponseString.length() - 1));
        } catch (IOException unused) {
            return null;
        }
    }

    public String getWelcomeMsg() {
        return this.welcomeMsg;
    }

    public FtpReplyCode getLastReplyCode() {
        return this.lastReplyCode;
    }

    public String getLastResponseString() {
        StringBuffer stringBuffer = new StringBuffer();
        Vector<String> vector = this.serverResponse;
        if (vector != null) {
            Iterator<String> it = vector.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next != null) {
                    stringBuffer.append(next);
                }
            }
        }
        return stringBuffer.toString();
    }

    public long getLastTransferSize() {
        return this.lastTransSize;
    }

    public String getLastFileName() {
        return this.lastFileName;
    }

    public sun.net.ftp.FtpClient startSecureSession() throws FtpProtocolException, IOException {
        if (isConnected()) {
            if (this.sslFact == null) {
                try {
                    this.sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
                } catch (Exception e) {
                    throw new IOException(e.getLocalizedMessage());
                }
            }
            issueCommandCheck("AUTH TLS");
            try {
                Socket createSocket = this.sslFact.createSocket(this.server, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
                this.oldSocket = this.server;
                this.server = createSocket;
                try {
                    this.out = new PrintStream((OutputStream) new BufferedOutputStream(createSocket.getOutputStream()), true, encoding);
                    this.f865in = new BufferedInputStream(this.server.getInputStream());
                    issueCommandCheck("PBSZ 0");
                    issueCommandCheck("PROT P");
                    this.useCrypto = true;
                    return this;
                } catch (UnsupportedEncodingException e2) {
                    throw new InternalError(encoding + "encoding not found", e2);
                }
            } catch (SSLException e3) {
                try {
                    disconnect();
                } catch (Exception unused) {
                }
                throw e3;
            }
        } else {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        }
    }

    public sun.net.ftp.FtpClient endSecureSession() throws FtpProtocolException, IOException {
        if (!this.useCrypto) {
            return this;
        }
        issueCommandCheck("CCC");
        issueCommandCheck("PROT C");
        this.useCrypto = false;
        Socket socket = this.oldSocket;
        this.server = socket;
        this.oldSocket = null;
        try {
            this.out = new PrintStream((OutputStream) new BufferedOutputStream(socket.getOutputStream()), true, encoding);
            this.f865in = new BufferedInputStream(this.server.getInputStream());
            return this;
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(encoding + "encoding not found", e);
        }
    }

    public sun.net.ftp.FtpClient allocate(long j) throws FtpProtocolException, IOException {
        issueCommandCheck("ALLO " + j);
        return this;
    }

    public sun.net.ftp.FtpClient structureMount(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("SMNT " + str);
        return this;
    }

    public String getSystem() throws FtpProtocolException, IOException {
        issueCommandCheck("SYST");
        return getResponseString().substring(4);
    }

    public String getHelp(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("HELP " + str);
        Vector<String> responseStrings = getResponseStrings();
        if (responseStrings.size() == 1) {
            return responseStrings.get(0).substring(4);
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < responseStrings.size() - 1; i++) {
            stringBuffer.append(responseStrings.get(i).substring(3));
        }
        return stringBuffer.toString();
    }

    public sun.net.ftp.FtpClient siteCmd(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("SITE " + str);
        return this;
    }
}
