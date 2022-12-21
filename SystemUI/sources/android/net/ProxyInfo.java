package android.net;

import android.net.connectivity.com.android.net.module.util.ProxyUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Locale;

public class ProxyInfo implements Parcelable {
    public static final Parcelable.Creator<ProxyInfo> CREATOR = new Parcelable.Creator<ProxyInfo>() {
        public ProxyInfo createFromParcel(Parcel parcel) {
            String str;
            int i;
            if (parcel.readByte() != 0) {
                return new ProxyInfo((Uri) Uri.CREATOR.createFromParcel(parcel), parcel.readInt());
            }
            if (parcel.readByte() != 0) {
                str = parcel.readString();
                i = parcel.readInt();
            } else {
                str = null;
                i = 0;
            }
            return new ProxyInfo(str, i, parcel.readString(), parcel.createStringArray());
        }

        public ProxyInfo[] newArray(int i) {
            return new ProxyInfo[i];
        }
    };
    public static final String LOCAL_EXCL_LIST = "";
    public static final String LOCAL_HOST = "localhost";
    public static final int LOCAL_PORT = -1;
    private final String mExclusionList;
    private final String mHost;
    private final Uri mPacFileUrl;
    private final String[] mParsedExclusionList;
    private final int mPort;

    public int describeContents() {
        return 0;
    }

    public static ProxyInfo buildDirectProxy(String str, int i) {
        return new ProxyInfo(str, i, (String) null);
    }

    public static ProxyInfo buildDirectProxy(String str, int i, List<String> list) {
        String[] strArr = (String[]) list.toArray(new String[list.size()]);
        return new ProxyInfo(str, i, TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, strArr), strArr);
    }

    public static ProxyInfo buildPacProxy(Uri uri) {
        return new ProxyInfo(uri);
    }

    public static ProxyInfo buildPacProxy(Uri uri, int i) {
        return new ProxyInfo(uri, i);
    }

    public ProxyInfo(String str, int i, String str2) {
        this.mHost = str;
        this.mPort = i;
        this.mExclusionList = str2;
        this.mParsedExclusionList = parseExclusionList(str2);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(Uri uri) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList("");
        uri.getClass();
        this.mPacFileUrl = uri;
    }

    public ProxyInfo(Uri uri, int i) {
        this.mHost = LOCAL_HOST;
        this.mPort = i;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList("");
        uri.getClass();
        this.mPacFileUrl = uri;
    }

    private static String[] parseExclusionList(String str) {
        return str == null ? new String[0] : str.toLowerCase(Locale.ROOT).split(NavigationBarInflaterView.BUTTON_SEPARATOR);
    }

    private ProxyInfo(String str, int i, String str2, String[] strArr) {
        this.mHost = str;
        this.mPort = i;
        this.mExclusionList = str2;
        this.mParsedExclusionList = strArr;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(ProxyInfo proxyInfo) {
        if (proxyInfo != null) {
            this.mHost = proxyInfo.getHost();
            this.mPort = proxyInfo.getPort();
            this.mPacFileUrl = proxyInfo.mPacFileUrl;
            this.mExclusionList = proxyInfo.getExclusionListAsString();
            this.mParsedExclusionList = proxyInfo.mParsedExclusionList;
            return;
        }
        this.mHost = null;
        this.mPort = 0;
        this.mExclusionList = null;
        this.mParsedExclusionList = null;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            return new InetSocketAddress(this.mHost, this.mPort);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public Uri getPacFileUrl() {
        return this.mPacFileUrl;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getPort() {
        return this.mPort;
    }

    public String[] getExclusionList() {
        return this.mParsedExclusionList;
    }

    public String getExclusionListAsString() {
        return this.mExclusionList;
    }

    public boolean isValid() {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            return true;
        }
        String str = this.mHost;
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        int i = this.mPort;
        String num = i == 0 ? str2 : Integer.toString(i);
        String str3 = this.mExclusionList;
        if (str3 != null) {
            str2 = str3;
        }
        if (ProxyUtils.validate(str, num, str2) == 0) {
            return true;
        }
        return false;
    }

    public Proxy makeProxy() {
        Proxy proxy = Proxy.NO_PROXY;
        if (this.mHost == null) {
            return proxy;
        }
        try {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.mHost, this.mPort));
        } catch (IllegalArgumentException unused) {
            return proxy;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append((Object) this.mPacFileUrl);
        }
        if (this.mHost != null) {
            sb.append(NavigationBarInflaterView.SIZE_MOD_START);
            sb.append(this.mHost);
            sb.append("] ");
            sb.append(Integer.toString(this.mPort));
            if (this.mExclusionList != null) {
                sb.append(" xl=");
                sb.append(this.mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ProxyInfo)) {
            return false;
        }
        ProxyInfo proxyInfo = (ProxyInfo) obj;
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            if (!this.mPacFileUrl.equals(proxyInfo.getPacFileUrl()) || this.mPort != proxyInfo.mPort) {
                return false;
            }
            return true;
        } else if (!Uri.EMPTY.equals(proxyInfo.mPacFileUrl)) {
            return false;
        } else {
            String str = this.mExclusionList;
            if (str != null && !str.equals(proxyInfo.getExclusionListAsString())) {
                return false;
            }
            if (this.mHost != null && proxyInfo.getHost() != null && !this.mHost.equals(proxyInfo.getHost())) {
                return false;
            }
            String str2 = this.mHost;
            if (str2 != null && proxyInfo.mHost == null) {
                return false;
            }
            if ((str2 != null || proxyInfo.mHost == null) && this.mPort == proxyInfo.mPort) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        String str = this.mHost;
        int i = 0;
        int hashCode = str == null ? 0 : str.hashCode();
        String str2 = this.mExclusionList;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i + this.mPort;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            parcel.writeByte((byte) 1);
            this.mPacFileUrl.writeToParcel(parcel, 0);
            parcel.writeInt(this.mPort);
            return;
        }
        parcel.writeByte((byte) 0);
        if (this.mHost != null) {
            parcel.writeByte((byte) 1);
            parcel.writeString(this.mHost);
            parcel.writeInt(this.mPort);
        } else {
            parcel.writeByte((byte) 0);
        }
        parcel.writeString(this.mExclusionList);
        parcel.writeStringArray(this.mParsedExclusionList);
    }
}
