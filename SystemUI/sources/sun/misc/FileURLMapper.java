package sun.misc;

import android.net.ProxyInfo;
import java.net.URL;
import java.p026io.File;
import sun.net.www.ParseUtil;

public class FileURLMapper {
    String path;
    URL url;

    public FileURLMapper(URL url2) {
        this.url = url2;
    }

    public String getPath() {
        String str = this.path;
        if (str != null) {
            return str;
        }
        String host = this.url.getHost();
        if (host == null || "".equals(host) || ProxyInfo.LOCAL_HOST.equalsIgnoreCase(host)) {
            String file = this.url.getFile();
            this.path = file;
            this.path = ParseUtil.decode(file);
        }
        return this.path;
    }

    public boolean exists() {
        String path2 = getPath();
        if (path2 == null) {
            return false;
        }
        return new File(path2).exists();
    }
}
