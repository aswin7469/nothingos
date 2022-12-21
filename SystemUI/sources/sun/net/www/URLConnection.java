package sun.net.www;

import java.net.URL;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class URLConnection extends java.net.URLConnection {
    private static HashMap<String, Void> proxiedHosts = new HashMap<>();
    private int contentLength = -1;
    private String contentType;
    protected MessageHeader properties = new MessageHeader();

    public URLConnection(URL url) {
        super(url);
    }

    public MessageHeader getProperties() {
        return this.properties;
    }

    public void setProperties(MessageHeader messageHeader) {
        this.properties = messageHeader;
    }

    public void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalAccessError("Already connected");
        } else if (str != null) {
            this.properties.set(str, str2);
        } else {
            throw new NullPointerException("key cannot be null");
        }
    }

    public void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (str == null) {
            throw new NullPointerException("key is null");
        }
    }

    public String getRequestProperty(String str) {
        if (!this.connected) {
            return null;
        }
        throw new IllegalStateException("Already connected");
    }

    public Map<String, List<String>> getRequestProperties() {
        if (!this.connected) {
            return Collections.emptyMap();
        }
        throw new IllegalStateException("Already connected");
    }

    public String getHeaderField(String str) {
        try {
            getInputStream();
            MessageHeader messageHeader = this.properties;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.findValue(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public String getHeaderFieldKey(int i) {
        try {
            getInputStream();
            MessageHeader messageHeader = this.properties;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.getKey(i);
        } catch (Exception unused) {
            return null;
        }
    }

    public String getHeaderField(int i) {
        try {
            getInputStream();
            MessageHeader messageHeader = this.properties;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.getValue(i);
        } catch (Exception unused) {
            return null;
        }
    }

    public String getContentType() {
        String str;
        if (this.contentType == null) {
            this.contentType = getHeaderField("content-type");
        }
        if (this.contentType == null) {
            try {
                str = guessContentTypeFromStream(getInputStream());
            } catch (IOException unused) {
                str = null;
            }
            String findValue = this.properties.findValue("content-encoding");
            if (str == null && (str = this.properties.findValue("content-type")) == null) {
                if (this.url.getFile().endsWith("/")) {
                    str = "text/html";
                } else {
                    str = guessContentTypeFromName(this.url.getFile());
                }
            }
            if (str == null || (findValue != null && !findValue.equalsIgnoreCase("7bit") && !findValue.equalsIgnoreCase("8bit") && !findValue.equalsIgnoreCase("binary"))) {
                str = "content/unknown";
            }
            setContentType(str);
        }
        return this.contentType;
    }

    public void setContentType(String str) {
        this.contentType = str;
        this.properties.set("content-type", str);
    }

    public int getContentLength() {
        try {
            getInputStream();
            int i = this.contentLength;
            if (i >= 0) {
                return i;
            }
            try {
                i = Integer.parseInt(this.properties.findValue("content-length"));
                setContentLength(i);
                return i;
            } catch (Exception unused) {
                return i;
            }
        } catch (Exception unused2) {
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public void setContentLength(int i) {
        this.contentLength = i;
        this.properties.set("content-length", String.valueOf(i));
    }

    public boolean canCache() {
        return this.url.getFile().indexOf(63) < 0;
    }

    public void close() {
        this.url = null;
    }

    public static synchronized void setProxiedHost(String str) {
        synchronized (URLConnection.class) {
            proxiedHosts.put(str.toLowerCase(), null);
        }
    }

    public static synchronized boolean isProxiedHost(String str) {
        boolean containsKey;
        synchronized (URLConnection.class) {
            containsKey = proxiedHosts.containsKey(str.toLowerCase());
        }
        return containsKey;
    }
}
