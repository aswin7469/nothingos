package android.net.wifi.aware;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Characteristics implements Parcelable {
    public static final Parcelable.Creator<Characteristics> CREATOR = new Parcelable.Creator<Characteristics>() {
        public Characteristics createFromParcel(Parcel parcel) {
            return new Characteristics(parcel.readBundle());
        }

        public Characteristics[] newArray(int i) {
            return new Characteristics[i];
        }
    };
    public static final String KEY_IS_INSTANT_COMMUNICATION_MODE_SUPPORTED = "key_is_instant_communication_mode_supported";
    public static final String KEY_MAX_MATCH_FILTER_LENGTH = "key_max_match_filter_length";
    public static final String KEY_MAX_NDI_NUMBER = "key_max_ndi_number";
    public static final String KEY_MAX_NDP_NUMBER = "key_max_ndp_number";
    public static final String KEY_MAX_PUBLISH_NUMBER = "key_max_publish_number";
    public static final String KEY_MAX_SERVICE_NAME_LENGTH = "key_max_service_name_length";
    public static final String KEY_MAX_SERVICE_SPECIFIC_INFO_LENGTH = "key_max_service_specific_info_length";
    public static final String KEY_MAX_SUBSCRIBE_NUMBER = "key_max_subscribe_number";
    public static final String KEY_SUPPORTED_CIPHER_SUITES = "key_supported_cipher_suites";
    public static final int WIFI_AWARE_CIPHER_SUITE_NCS_PK_128 = 4;
    public static final int WIFI_AWARE_CIPHER_SUITE_NCS_PK_256 = 8;
    public static final int WIFI_AWARE_CIPHER_SUITE_NCS_SK_128 = 1;
    public static final int WIFI_AWARE_CIPHER_SUITE_NCS_SK_256 = 2;
    public static final int WIFI_AWARE_CIPHER_SUITE_NONE = 0;
    private final Bundle mCharacteristics;

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiAwareCipherSuites {
    }

    public int describeContents() {
        return 0;
    }

    public Characteristics(Bundle bundle) {
        this.mCharacteristics = bundle;
    }

    public int getMaxServiceNameLength() {
        return this.mCharacteristics.getInt(KEY_MAX_SERVICE_NAME_LENGTH);
    }

    public int getMaxServiceSpecificInfoLength() {
        return this.mCharacteristics.getInt(KEY_MAX_SERVICE_SPECIFIC_INFO_LENGTH);
    }

    public int getMaxMatchFilterLength() {
        return this.mCharacteristics.getInt(KEY_MAX_MATCH_FILTER_LENGTH);
    }

    public int getNumberOfSupportedDataInterfaces() {
        return this.mCharacteristics.getInt(KEY_MAX_NDI_NUMBER);
    }

    public int getNumberOfSupportedPublishSessions() {
        return this.mCharacteristics.getInt(KEY_MAX_PUBLISH_NUMBER);
    }

    public int getNumberOfSupportedSubscribeSessions() {
        return this.mCharacteristics.getInt(KEY_MAX_SUBSCRIBE_NUMBER);
    }

    public int getNumberOfSupportedDataPaths() {
        return this.mCharacteristics.getInt(KEY_MAX_NDP_NUMBER);
    }

    public boolean isInstantCommunicationModeSupported() {
        if (SdkLevel.isAtLeastS()) {
            return this.mCharacteristics.getBoolean(KEY_IS_INSTANT_COMMUNICATION_MODE_SUPPORTED);
        }
        throw new UnsupportedOperationException();
    }

    public int getSupportedCipherSuites() {
        return this.mCharacteristics.getInt(KEY_SUPPORTED_CIPHER_SUITES);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mCharacteristics);
    }
}
