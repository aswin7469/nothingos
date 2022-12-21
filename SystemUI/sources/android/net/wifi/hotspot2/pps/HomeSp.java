package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class HomeSp implements Parcelable {
    public static final Parcelable.Creator<HomeSp> CREATOR = new Parcelable.Creator<HomeSp>() {
        public HomeSp createFromParcel(Parcel parcel) {
            HomeSp homeSp = new HomeSp();
            homeSp.setFqdn(parcel.readString());
            homeSp.setFriendlyName(parcel.readString());
            homeSp.setIconUrl(parcel.readString());
            homeSp.setHomeNetworkIds(readHomeNetworkIds(parcel));
            homeSp.setMatchAllOis(parcel.createLongArray());
            homeSp.setMatchAnyOis(parcel.createLongArray());
            homeSp.setOtherHomePartners(parcel.createStringArray());
            homeSp.setRoamingConsortiumOis(parcel.createLongArray());
            return homeSp;
        }

        public HomeSp[] newArray(int i) {
            return new HomeSp[i];
        }

        private Map<String, Long> readHomeNetworkIds(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt == -1) {
                return null;
            }
            HashMap hashMap = new HashMap(readInt);
            for (int i = 0; i < readInt; i++) {
                String readString = parcel.readString();
                long readLong = parcel.readLong();
                hashMap.put(readString, readLong != -1 ? Long.valueOf(readLong) : null);
            }
            return hashMap;
        }
    };
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "HomeSp";
    private String mFqdn = null;
    private String mFriendlyName = null;
    private Map<String, Long> mHomeNetworkIds = null;
    private String mIconUrl = null;
    private long[] mMatchAllOis = null;
    private long[] mMatchAnyOis = null;
    private String[] mOtherHomePartners = null;
    private long[] mRoamingConsortiumOis = null;

    public int describeContents() {
        return 0;
    }

    public void setFqdn(String str) {
        this.mFqdn = str;
    }

    public String getFqdn() {
        return this.mFqdn;
    }

    public void setFriendlyName(String str) {
        this.mFriendlyName = str;
    }

    public String getFriendlyName() {
        return this.mFriendlyName;
    }

    public void setIconUrl(String str) {
        this.mIconUrl = str;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setHomeNetworkIds(Map<String, Long> map) {
        this.mHomeNetworkIds = map;
    }

    public Map<String, Long> getHomeNetworkIds() {
        return this.mHomeNetworkIds;
    }

    public void setMatchAllOis(long[] jArr) {
        this.mMatchAllOis = jArr;
    }

    public long[] getMatchAllOis() {
        return this.mMatchAllOis;
    }

    public void setMatchAnyOis(long[] jArr) {
        this.mMatchAnyOis = jArr;
    }

    public long[] getMatchAnyOis() {
        return this.mMatchAnyOis;
    }

    public void setOtherHomePartners(String[] strArr) {
        this.mOtherHomePartners = strArr;
    }

    public void setOtherHomePartnersList(Collection<String> collection) {
        if (collection != null) {
            this.mOtherHomePartners = (String[]) collection.toArray((T[]) new String[collection.size()]);
        }
    }

    public String[] getOtherHomePartners() {
        return this.mOtherHomePartners;
    }

    public Collection<String> getOtherHomePartnersList() {
        String[] strArr = this.mOtherHomePartners;
        if (strArr == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(strArr);
    }

    public void setRoamingConsortiumOis(long[] jArr) {
        this.mRoamingConsortiumOis = jArr;
    }

    public long[] getRoamingConsortiumOis() {
        return this.mRoamingConsortiumOis;
    }

    public HomeSp() {
    }

    public HomeSp(HomeSp homeSp) {
        if (homeSp != null) {
            this.mFqdn = homeSp.mFqdn;
            this.mFriendlyName = homeSp.mFriendlyName;
            this.mIconUrl = homeSp.mIconUrl;
            Map<String, Long> map = homeSp.mHomeNetworkIds;
            if (map != null) {
                this.mHomeNetworkIds = Collections.unmodifiableMap(map);
            }
            long[] jArr = homeSp.mMatchAllOis;
            if (jArr != null) {
                this.mMatchAllOis = Arrays.copyOf(jArr, jArr.length);
            }
            long[] jArr2 = homeSp.mMatchAnyOis;
            if (jArr2 != null) {
                this.mMatchAnyOis = Arrays.copyOf(jArr2, jArr2.length);
            }
            String[] strArr = homeSp.mOtherHomePartners;
            if (strArr != null) {
                this.mOtherHomePartners = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
            }
            long[] jArr3 = homeSp.mRoamingConsortiumOis;
            if (jArr3 != null) {
                this.mRoamingConsortiumOis = Arrays.copyOf(jArr3, jArr3.length);
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mFqdn);
        parcel.writeString(this.mFriendlyName);
        parcel.writeString(this.mIconUrl);
        writeHomeNetworkIds(parcel, this.mHomeNetworkIds);
        parcel.writeLongArray(this.mMatchAllOis);
        parcel.writeLongArray(this.mMatchAnyOis);
        parcel.writeStringArray(this.mOtherHomePartners);
        parcel.writeLongArray(this.mRoamingConsortiumOis);
    }

    public boolean equals(Object obj) {
        Map<String, Long> map;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HomeSp)) {
            return false;
        }
        HomeSp homeSp = (HomeSp) obj;
        if (!TextUtils.equals(this.mFqdn, homeSp.mFqdn) || !TextUtils.equals(this.mFriendlyName, homeSp.mFriendlyName) || !TextUtils.equals(this.mIconUrl, homeSp.mIconUrl) || ((map = this.mHomeNetworkIds) != null ? !map.equals(homeSp.mHomeNetworkIds) : homeSp.mHomeNetworkIds != null) || !Arrays.equals(this.mMatchAllOis, homeSp.mMatchAllOis) || !Arrays.equals(this.mMatchAnyOis, homeSp.mMatchAnyOis) || !Arrays.equals((Object[]) this.mOtherHomePartners, (Object[]) homeSp.mOtherHomePartners) || !Arrays.equals(this.mRoamingConsortiumOis, homeSp.mRoamingConsortiumOis)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mFqdn, this.mFriendlyName, this.mIconUrl, this.mHomeNetworkIds, Integer.valueOf(Arrays.hashCode(this.mMatchAllOis)), Integer.valueOf(Arrays.hashCode(this.mMatchAnyOis)), Integer.valueOf(Arrays.hashCode((Object[]) this.mOtherHomePartners)), Integer.valueOf(Arrays.hashCode(this.mRoamingConsortiumOis)));
    }

    public int getUniqueId() {
        return Objects.hash(this.mFqdn);
    }

    public String toString() {
        return "FQDN: " + this.mFqdn + "\nFriendlyName: " + this.mFriendlyName + "\nIconURL: " + this.mIconUrl + "\nHomeNetworkIDs: " + this.mHomeNetworkIds + "\nMatchAllOIs: " + this.mMatchAllOis + "\nMatchAnyOIs: " + this.mMatchAnyOis + "\nOtherHomePartners: " + this.mOtherHomePartners + "\nRoamingConsortiumOIs: " + this.mRoamingConsortiumOis + "\n";
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean validate() {
        /*
            r4 = this;
            java.lang.String r0 = r4.mFqdn
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r1 = 0
            java.lang.String r2 = "HomeSp"
            if (r0 == 0) goto L_0x0011
            java.lang.String r4 = "Missing FQDN"
            android.util.Log.d(r2, r4)
            return r1
        L_0x0011:
            java.lang.String r0 = r4.mFriendlyName
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x001f
            java.lang.String r4 = "Missing friendly name"
            android.util.Log.d(r2, r4)
            return r1
        L_0x001f:
            java.util.Map<java.lang.String, java.lang.Long> r4 = r4.mHomeNetworkIds
            if (r4 == 0) goto L_0x0054
            java.util.Set r4 = r4.entrySet()
            java.util.Iterator r4 = r4.iterator()
        L_0x002b:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x0054
            java.lang.Object r0 = r4.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r3 = r0.getKey()
            if (r3 == 0) goto L_0x004e
            java.lang.Object r0 = r0.getKey()
            java.lang.String r0 = (java.lang.String) r0
            java.nio.charset.Charset r3 = java.nio.charset.StandardCharsets.UTF_8
            byte[] r0 = r0.getBytes((java.nio.charset.Charset) r3)
            int r0 = r0.length
            r3 = 32
            if (r0 <= r3) goto L_0x002b
        L_0x004e:
            java.lang.String r4 = "Invalid SSID in HomeNetworkIDs"
            android.util.Log.d(r2, r4)
            return r1
        L_0x0054:
            r4 = 1
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.pps.HomeSp.validate():boolean");
    }

    private static void writeHomeNetworkIds(Parcel parcel, Map<String, Long> map) {
        if (map == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(map.size());
        for (Map.Entry next : map.entrySet()) {
            parcel.writeString((String) next.getKey());
            if (next.getValue() == null) {
                parcel.writeLong(-1);
            } else {
                parcel.writeLong(((Long) next.getValue()).longValue());
            }
        }
    }
}
