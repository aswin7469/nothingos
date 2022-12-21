package android.net;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SystemApi
public final class OemNetworkPreferences implements Parcelable {
    public static final Parcelable.Creator<OemNetworkPreferences> CREATOR = new Parcelable.Creator<OemNetworkPreferences>() {
        public OemNetworkPreferences[] newArray(int i) {
            return new OemNetworkPreferences[i];
        }

        public OemNetworkPreferences createFromParcel(Parcel parcel) {
            return new OemNetworkPreferences(parcel.readBundle(getClass().getClassLoader()));
        }
    };
    public static final int OEM_NETWORK_PREFERENCE_MAX = 4;
    public static final int OEM_NETWORK_PREFERENCE_OEM_PAID = 1;
    public static final int OEM_NETWORK_PREFERENCE_OEM_PAID_NO_FALLBACK = 2;
    public static final int OEM_NETWORK_PREFERENCE_OEM_PAID_ONLY = 3;
    public static final int OEM_NETWORK_PREFERENCE_OEM_PRIVATE_ONLY = 4;
    public static final int OEM_NETWORK_PREFERENCE_TEST = -1;
    public static final int OEM_NETWORK_PREFERENCE_TEST_ONLY = -2;
    public static final int OEM_NETWORK_PREFERENCE_UNINITIALIZED = 0;
    /* access modifiers changed from: private */
    public final Bundle mNetworkMappings;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OemNetworkPreference {
    }

    public int describeContents() {
        return 0;
    }

    public boolean isEmpty() {
        return this.mNetworkMappings.keySet().size() == 0;
    }

    public Map<String, Integer> getNetworkPreferences() {
        return convertToUnmodifiableMap(this.mNetworkMappings);
    }

    private OemNetworkPreferences(Bundle bundle) {
        Objects.requireNonNull(bundle);
        this.mNetworkMappings = (Bundle) bundle.clone();
    }

    public String toString() {
        return "OemNetworkPreferences{mNetworkMappings=" + getNetworkPreferences() + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OemNetworkPreferences oemNetworkPreferences = (OemNetworkPreferences) obj;
        if (this.mNetworkMappings.size() != oemNetworkPreferences.mNetworkMappings.size() || !this.mNetworkMappings.toString().equals(oemNetworkPreferences.mNetworkMappings.toString())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mNetworkMappings);
    }

    public static final class Builder {
        private final Bundle mNetworkMappings;

        public Builder() {
            this.mNetworkMappings = new Bundle();
        }

        public Builder(OemNetworkPreferences oemNetworkPreferences) {
            Objects.requireNonNull(oemNetworkPreferences);
            this.mNetworkMappings = (Bundle) oemNetworkPreferences.mNetworkMappings.clone();
        }

        public Builder addNetworkPreference(String str, int i) {
            Objects.requireNonNull(str);
            this.mNetworkMappings.putInt(str, i);
            return this;
        }

        public Builder clearNetworkPreference(String str) {
            Objects.requireNonNull(str);
            this.mNetworkMappings.remove(str);
            return this;
        }

        public OemNetworkPreferences build() {
            return new OemNetworkPreferences(this.mNetworkMappings);
        }
    }

    private static Map<String, Integer> convertToUnmodifiableMap(Bundle bundle) {
        HashMap hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, Integer.valueOf(bundle.getInt(str)));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static String oemNetworkPreferenceToString(int i) {
        switch (i) {
            case -2:
                return "OEM_NETWORK_PREFERENCE_TEST_ONLY";
            case -1:
                return "OEM_NETWORK_PREFERENCE_TEST";
            case 0:
                return "OEM_NETWORK_PREFERENCE_UNINITIALIZED";
            case 1:
                return "OEM_NETWORK_PREFERENCE_OEM_PAID";
            case 2:
                return "OEM_NETWORK_PREFERENCE_OEM_PAID_NO_FALLBACK";
            case 3:
                return "OEM_NETWORK_PREFERENCE_OEM_PAID_ONLY";
            case 4:
                return "OEM_NETWORK_PREFERENCE_OEM_PRIVATE_ONLY";
            default:
                return Integer.toHexString(i);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mNetworkMappings);
    }
}
