package android.net.wifi.hotspot2;

import android.annotation.SystemApi;
import android.net.Uri;
import android.net.wifi.WifiSsid;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@SystemApi
public final class OsuProvider implements Parcelable {
    public static final Parcelable.Creator<OsuProvider> CREATOR = new Parcelable.Creator<OsuProvider>() {
        public OsuProvider createFromParcel(Parcel parcel) {
            String readString = parcel.readString();
            ArrayList arrayList = new ArrayList();
            parcel.readList(arrayList, (ClassLoader) null);
            return new OsuProvider((WifiSsid) parcel.readParcelable((ClassLoader) null), (Map<String, String>) (HashMap) parcel.readBundle().getSerializable("friendlyNameMap"), parcel.readString(), (Uri) parcel.readParcelable((ClassLoader) null), readString, (List<Integer>) arrayList);
        }

        public OsuProvider[] newArray(int i) {
            return new OsuProvider[i];
        }
    };
    public static final int METHOD_OMA_DM = 0;
    public static final int METHOD_SOAP_XML_SPP = 1;
    private final Map<String, String> mFriendlyNames;
    private final List<Integer> mMethodList;
    private final String mNetworkAccessIdentifier;
    private WifiSsid mOsuSsid;
    private final Uri mServerUri;
    private final String mServiceDescription;

    public int describeContents() {
        return 0;
    }

    public OsuProvider(String str, Map<String, String> map, String str2, Uri uri, String str3, List<Integer> list) {
        this(WifiSsid.fromBytes(str.getBytes(StandardCharsets.UTF_8)), map, str2, uri, str3, list);
    }

    public OsuProvider(WifiSsid wifiSsid, Map<String, String> map, String str, Uri uri, String str2, List<Integer> list) {
        this.mOsuSsid = wifiSsid;
        this.mFriendlyNames = map;
        this.mServiceDescription = str;
        this.mServerUri = uri;
        this.mNetworkAccessIdentifier = str2;
        if (list == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(list);
        }
    }

    public OsuProvider(OsuProvider osuProvider) {
        if (osuProvider == null) {
            this.mOsuSsid = null;
            this.mFriendlyNames = null;
            this.mServiceDescription = null;
            this.mServerUri = null;
            this.mNetworkAccessIdentifier = null;
            this.mMethodList = new ArrayList();
            return;
        }
        this.mOsuSsid = osuProvider.mOsuSsid;
        this.mFriendlyNames = osuProvider.mFriendlyNames;
        this.mServiceDescription = osuProvider.mServiceDescription;
        this.mServerUri = osuProvider.mServerUri;
        this.mNetworkAccessIdentifier = osuProvider.mNetworkAccessIdentifier;
        if (osuProvider.mMethodList == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(osuProvider.mMethodList);
        }
    }

    public WifiSsid getOsuSsid() {
        return this.mOsuSsid;
    }

    public void setOsuSsid(WifiSsid wifiSsid) {
        this.mOsuSsid = wifiSsid;
    }

    public String getFriendlyName() {
        Map<String, String> map = this.mFriendlyNames;
        if (map == null || map.isEmpty()) {
            return null;
        }
        String str = this.mFriendlyNames.get(Locale.getDefault().getLanguage());
        if (str != null) {
            return str;
        }
        String str2 = this.mFriendlyNames.get("en");
        if (str2 != null) {
            return str2;
        }
        Map<String, String> map2 = this.mFriendlyNames;
        return map2.get(map2.keySet().stream().findFirst().get());
    }

    public Map<String, String> getFriendlyNameList() {
        return this.mFriendlyNames;
    }

    public String getServiceDescription() {
        return this.mServiceDescription;
    }

    public Uri getServerUri() {
        return this.mServerUri;
    }

    public String getNetworkAccessIdentifier() {
        return this.mNetworkAccessIdentifier;
    }

    public List<Integer> getMethodList() {
        return this.mMethodList;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mOsuSsid, i);
        parcel.writeString(this.mServiceDescription);
        parcel.writeParcelable(this.mServerUri, i);
        parcel.writeString(this.mNetworkAccessIdentifier);
        parcel.writeList(this.mMethodList);
        Bundle bundle = new Bundle();
        bundle.putSerializable("friendlyNameMap", (HashMap) this.mFriendlyNames);
        parcel.writeBundle(bundle);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OsuProvider)) {
            return false;
        }
        OsuProvider osuProvider = (OsuProvider) obj;
        if (!Objects.equals(this.mOsuSsid, osuProvider.mOsuSsid) || !Objects.equals(this.mFriendlyNames, osuProvider.mFriendlyNames) || !TextUtils.equals(this.mServiceDescription, osuProvider.mServiceDescription) || !Objects.equals(this.mServerUri, osuProvider.mServerUri) || !TextUtils.equals(this.mNetworkAccessIdentifier, osuProvider.mNetworkAccessIdentifier) || !Objects.equals(this.mMethodList, osuProvider.mMethodList)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mOsuSsid, this.mServiceDescription, this.mFriendlyNames, this.mServerUri, this.mNetworkAccessIdentifier, this.mMethodList);
    }

    public String toString() {
        return "OsuProvider{mOsuSsid=" + this.mOsuSsid + " mFriendlyNames=" + this.mFriendlyNames + " mServiceDescription=" + this.mServiceDescription + " mServerUri=" + this.mServerUri + " mNetworkAccessIdentifier=" + this.mNetworkAccessIdentifier + " mMethodList=" + this.mMethodList;
    }
}
