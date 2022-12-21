package android.net.wifi.p2p;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.LruCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SystemApi
public final class WifiP2pGroupList implements Parcelable {
    public static final Parcelable.Creator<WifiP2pGroupList> CREATOR = new Parcelable.Creator<WifiP2pGroupList>() {
        public WifiP2pGroupList createFromParcel(Parcel parcel) {
            WifiP2pGroupList wifiP2pGroupList = new WifiP2pGroupList();
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                wifiP2pGroupList.add((WifiP2pGroup) parcel.readParcelable((ClassLoader) null));
            }
            return wifiP2pGroupList;
        }

        public WifiP2pGroupList[] newArray(int i) {
            return new WifiP2pGroupList[i];
        }
    };
    private static final int CREDENTIAL_MAX_NUM = 32;
    /* access modifiers changed from: private */
    public boolean isClearCalled;
    private final LruCache<Integer, WifiP2pGroup> mGroups;
    /* access modifiers changed from: private */
    public final GroupDeleteListener mListener;

    public interface GroupDeleteListener {
        void onDeleteGroup(int i);
    }

    public int describeContents() {
        return 0;
    }

    public WifiP2pGroupList() {
        this((WifiP2pGroupList) null, (GroupDeleteListener) null);
    }

    public WifiP2pGroupList(WifiP2pGroupList wifiP2pGroupList, GroupDeleteListener groupDeleteListener) {
        this.isClearCalled = false;
        this.mListener = groupDeleteListener;
        this.mGroups = new LruCache<Integer, WifiP2pGroup>(32) {
            /* access modifiers changed from: protected */
            public void entryRemoved(boolean z, Integer num, WifiP2pGroup wifiP2pGroup, WifiP2pGroup wifiP2pGroup2) {
                if (WifiP2pGroupList.this.mListener != null && !WifiP2pGroupList.this.isClearCalled) {
                    WifiP2pGroupList.this.mListener.onDeleteGroup(wifiP2pGroup.getNetworkId());
                }
            }
        };
        if (wifiP2pGroupList != null) {
            for (Map.Entry next : wifiP2pGroupList.mGroups.snapshot().entrySet()) {
                this.mGroups.put((Integer) next.getKey(), (WifiP2pGroup) next.getValue());
            }
        }
    }

    public List<WifiP2pGroup> getGroupList() {
        return new ArrayList(this.mGroups.snapshot().values());
    }

    public void add(WifiP2pGroup wifiP2pGroup) {
        this.mGroups.put(Integer.valueOf(wifiP2pGroup.getNetworkId()), wifiP2pGroup);
    }

    public void remove(int i) {
        this.mGroups.remove(Integer.valueOf(i));
    }

    /* access modifiers changed from: package-private */
    public void remove(String str) {
        remove(getNetworkId(str));
    }

    public boolean clear() {
        if (this.mGroups.size() == 0) {
            return false;
        }
        this.isClearCalled = true;
        this.mGroups.evictAll();
        this.isClearCalled = false;
        return true;
    }

    public int getNetworkId(String str) {
        if (str == null) {
            return -1;
        }
        for (WifiP2pGroup next : this.mGroups.snapshot().values()) {
            if (str.equalsIgnoreCase(next.getOwner().deviceAddress)) {
                this.mGroups.get(Integer.valueOf(next.getNetworkId()));
                return next.getNetworkId();
            }
        }
        return -1;
    }

    public int getNetworkId(String str, String str2) {
        if (!(str == null || str2 == null)) {
            for (WifiP2pGroup next : this.mGroups.snapshot().values()) {
                if (str.equalsIgnoreCase(next.getOwner().deviceAddress) && str2.equals(next.getNetworkName())) {
                    this.mGroups.get(Integer.valueOf(next.getNetworkId()));
                    return next.getNetworkId();
                }
            }
        }
        return -1;
    }

    public String getOwnerAddr(int i) {
        WifiP2pGroup wifiP2pGroup = this.mGroups.get(Integer.valueOf(i));
        if (wifiP2pGroup != null) {
            return wifiP2pGroup.getOwner().deviceAddress;
        }
        return null;
    }

    public boolean contains(int i) {
        for (WifiP2pGroup networkId : this.mGroups.snapshot().values()) {
            if (i == networkId.getNetworkId()) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (WifiP2pGroup append : this.mGroups.snapshot().values()) {
            stringBuffer.append((Object) append).append("\n");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        Collection<WifiP2pGroup> values = this.mGroups.snapshot().values();
        parcel.writeInt(values.size());
        for (WifiP2pGroup writeParcelable : values) {
            parcel.writeParcelable(writeParcelable, i);
        }
    }
}
