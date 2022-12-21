package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import java.util.List;
import java.util.Objects;

@SystemApi
public abstract class NearbyDevice {
    private final List<Integer> mMediums;
    private final String mName;
    private final int mRssi;

    public @interface Medium {
        public static final int BLE = 1;
        public static final int BLUETOOTH = 2;
    }

    public static boolean isValidMedium(int i) {
        return i == 1 || i == 2;
    }

    static String mediumToString(int i) {
        return i != 1 ? i != 2 ? FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN : "Bluetooth Classic" : "BLE";
    }

    public NearbyDevice(String str, List<Integer> list, int i) {
        for (Integer intValue : list) {
            int intValue2 = intValue.intValue();
            boolean isValidMedium = isValidMedium(intValue2);
            Preconditions.checkState(isValidMedium, "Not supported medium: " + intValue2 + ", scan medium must be one of NearbyDevice#Medium.");
        }
        this.mName = str;
        this.mMediums = list;
        this.mRssi = i;
    }

    public String getName() {
        return this.mName;
    }

    public List<Integer> getMediums() {
        return this.mMediums;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NearbyDevice [");
        String str = this.mName;
        if (str != null && !str.isEmpty()) {
            sb.append("name=");
            sb.append(this.mName);
            sb.append(", ");
        }
        sb.append("medium={");
        for (Integer intValue : this.mMediums) {
            sb.append(mediumToString(intValue.intValue()));
        }
        sb.append("} rssi=");
        sb.append(this.mRssi);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NearbyDevice)) {
            return false;
        }
        NearbyDevice nearbyDevice = (NearbyDevice) obj;
        if (Objects.equals(this.mName, nearbyDevice.mName) && this.mMediums == nearbyDevice.mMediums && this.mRssi == nearbyDevice.mRssi) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mName, this.mMediums, Integer.valueOf(this.mRssi));
    }
}
