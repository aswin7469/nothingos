package sun.net.www.protocol.file;

import java.net.URL;
import java.p026io.BufferedInputStream;
import java.p026io.ByteArrayInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FilePermission;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.Permission;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import sun.net.ProgressMonitor;
import sun.net.ProgressSource;
import sun.net.www.MessageHeader;
import sun.net.www.MeteredStream;
import sun.net.www.ParseUtil;
import sun.net.www.URLConnection;

public class FileURLConnection extends URLConnection {
    static String CONTENT_LENGTH = "content-length";
    static String CONTENT_TYPE = "content-type";
    static String LAST_MODIFIED = "last-modified";
    static String TEXT_PLAIN = "text/plain";
    String contentType;
    boolean exists = false;
    File file;
    String filename;
    List<String> files;
    private boolean initializedHeaders = false;

    /* renamed from: is */
    InputStream f867is;
    boolean isDirectory = false;
    long lastModified = 0;
    long length = -1;
    Permission permission;

    protected FileURLConnection(URL url, File file2) {
        super(url);
        this.file = file2;
    }

    public void connect() throws IOException {
        if (!this.connected) {
            try {
                this.filename = this.file.toString();
                boolean isDirectory2 = this.file.isDirectory();
                this.isDirectory = isDirectory2;
                if (isDirectory2) {
                    String[] list = this.file.list();
                    if (list != null) {
                        this.files = Arrays.asList(list);
                    } else {
                        throw new FileNotFoundException(this.filename + " exists, but is not accessible");
                    }
                } else {
                    this.f867is = new BufferedInputStream(new FileInputStream(this.filename));
                    if (ProgressMonitor.getDefault().shouldMeterInput(this.url, "GET")) {
                        this.f867is = new MeteredStream(this.f867is, new ProgressSource(this.url, "GET", this.file.length()), this.file.length());
                    }
                }
                this.connected = true;
            } catch (IOException e) {
                throw e;
            }
        }
    }

    private void initializeHeaders() {
        try {
            connect();
            this.exists = this.file.exists();
        } catch (IOException unused) {
        }
        if (!this.initializedHeaders || !this.exists) {
            this.length = this.file.length();
            this.lastModified = this.file.lastModified();
            if (!this.isDirectory) {
                String contentTypeFor = java.net.URLConnection.getFileNameMap().getContentTypeFor(this.filename);
                this.contentType = contentTypeFor;
                if (contentTypeFor != null) {
                    this.properties.add(CONTENT_TYPE, this.contentType);
                }
                this.properties.add(CONTENT_LENGTH, String.valueOf(this.length));
                if (this.lastModified != 0) {
                    Date date = new Date(this.lastModified);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.f698US);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    this.properties.add(LAST_MODIFIED, simpleDateFormat.format(date));
                }
            } else {
                this.properties.add(CONTENT_TYPE, TEXT_PLAIN);
            }
            this.initializedHeaders = true;
        }
    }

    public String getHeaderField(String str) {
        initializeHeaders();
        return super.getHeaderField(str);
    }

    public String getHeaderField(int i) {
        initializeHeaders();
        return super.getHeaderField(i);
    }

    public int getContentLength() {
        initializeHeaders();
        long j = this.length;
        if (j > 2147483647L) {
            return -1;
        }
        return (int) j;
    }

    public long getContentLengthLong() {
        initializeHeaders();
        return this.length;
    }

    public String getHeaderFieldKey(int i) {
        initializeHeaders();
        return super.getHeaderFieldKey(i);
    }

    public MessageHeader getProperties() {
        initializeHeaders();
        return super.getProperties();
    }

    public long getLastModified() {
        initializeHeaders();
        return this.lastModified;
    }

    public synchronized InputStream getInputStream() throws IOException {
        connect();
        if (this.f867is == null) {
            if (this.isDirectory) {
                java.net.URLConnection.getFileNameMap();
                StringBuffer stringBuffer = new StringBuffer();
                List<String> list = this.files;
                if (list != null) {
                    Collections.sort(list, Collator.getInstance());
                    for (int i = 0; i < this.files.size(); i++) {
                        stringBuffer.append(this.files.get(i));
                        stringBuffer.append("\n");
                    }
                    this.f867is = new ByteArrayInputStream(stringBuffer.toString().getBytes());
                } else {
                    throw new FileNotFoundException(this.filename);
                }
            } else {
                throw new FileNotFoundException(this.filename);
            }
        }
        return this.f867is;
    }

    public Permission getPermission() throws IOException {
        if (this.permission == null) {
            String decode = ParseUtil.decode(this.url.getPath());
            if (File.separatorChar == '/') {
                this.permission = new FilePermission(decode, "read");
            } else {
                this.permission = new FilePermission(decode.replace('/', File.separatorChar), "read");
            }
        }
        return this.permission;
    }
}
