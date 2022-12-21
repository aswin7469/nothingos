package java.net;

/* compiled from: URL */
final class UrlDeserializedState {
    private final String authority;
    private final String file;
    private final int hashCode;
    private final String host;
    private final int port;
    private final String protocol;
    private final String ref;

    public UrlDeserializedState(String str, String str2, int i, String str3, String str4, String str5, int i2) {
        this.protocol = str;
        this.host = str2;
        this.port = i;
        this.authority = str3;
        this.file = str4;
        this.ref = str5;
        this.hashCode = i2;
    }

    /* access modifiers changed from: package-private */
    public String getProtocol() {
        return this.protocol;
    }

    /* access modifiers changed from: package-private */
    public String getHost() {
        return this.host;
    }

    /* access modifiers changed from: package-private */
    public String getAuthority() {
        return this.authority;
    }

    /* access modifiers changed from: package-private */
    public int getPort() {
        return this.port;
    }

    /* access modifiers changed from: package-private */
    public String getFile() {
        return this.file;
    }

    /* access modifiers changed from: package-private */
    public String getRef() {
        return this.ref;
    }

    /* access modifiers changed from: package-private */
    public int getHashCode() {
        return this.hashCode;
    }

    /* access modifiers changed from: package-private */
    public String reconstituteUrlString() {
        int length = this.protocol.length() + 1;
        String str = this.authority;
        if (str != null && str.length() > 0) {
            length += this.authority.length() + 2;
        }
        String str2 = this.file;
        if (str2 != null) {
            length += str2.length();
        }
        String str3 = this.ref;
        if (str3 != null) {
            length += str3.length() + 1;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(this.protocol);
        sb.append(":");
        String str4 = this.authority;
        if (str4 != null && str4.length() > 0) {
            sb.append("//");
            sb.append(this.authority);
        }
        String str5 = this.file;
        if (str5 != null) {
            sb.append(str5);
        }
        if (this.ref != null) {
            sb.append("#");
            sb.append(this.ref);
        }
        return sb.toString();
    }
}
