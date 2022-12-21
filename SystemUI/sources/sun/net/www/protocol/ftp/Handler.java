package sun.net.www.protocol.ftp;

import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.p026io.IOException;

public class Handler extends URLStreamHandler {
    /* access modifiers changed from: protected */
    public int getDefaultPort() {
        return 21;
    }

    /* access modifiers changed from: protected */
    public boolean equals(URL url, URL url2) {
        String userInfo = url.getUserInfo();
        String userInfo2 = url2.getUserInfo();
        return super.equals(url, url2) && (userInfo != null ? userInfo.equals(userInfo2) : userInfo2 == null);
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, (Proxy) null);
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new FtpURLConnection(url, proxy);
    }
}
