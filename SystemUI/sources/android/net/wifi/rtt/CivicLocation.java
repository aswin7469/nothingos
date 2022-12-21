package android.net.wifi.rtt;

import android.location.Address;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

public final class CivicLocation implements Parcelable {
    private static final int ADDRESS_LINE_0_ROOM_DESK_FLOOR = 0;
    private static final int ADDRESS_LINE_1_NUMBER_ROAD_SUFFIX_APT = 1;
    private static final int ADDRESS_LINE_2_CITY = 2;
    private static final int ADDRESS_LINE_3_STATE_POSTAL_CODE = 3;
    private static final int ADDRESS_LINE_4_COUNTRY = 4;
    private static final int BYTE_MASK = 255;
    private static final int COUNTRY_CODE_LENGTH = 2;
    public static final Parcelable.Creator<CivicLocation> CREATOR = new Parcelable.Creator<CivicLocation>() {
        public CivicLocation createFromParcel(Parcel parcel) {
            return new CivicLocation(parcel);
        }

        public CivicLocation[] newArray(int i) {
            return new CivicLocation[i];
        }
    };
    private static final int MAX_CIVIC_BUFFER_SIZE = 256;
    private static final int MIN_CIVIC_BUFFER_SIZE = 3;
    private static final int TLV_LENGTH_INDEX = 1;
    private static final int TLV_TYPE_INDEX = 0;
    private static final int TLV_VALUE_INDEX = 2;
    private SparseArray<String> mCivicAddressElements;
    private final String mCountryCode;
    private final boolean mIsValid;

    public int describeContents() {
        return 0;
    }

    public CivicLocation(byte[] bArr, String str) {
        this.mCivicAddressElements = new SparseArray<>(3);
        this.mCountryCode = str;
        boolean z = false;
        if (str == null || str.length() != 2) {
            this.mIsValid = false;
            return;
        }
        if (bArr != null && bArr.length >= 3 && bArr.length < 256) {
            z = parseCivicTLVs(bArr);
        }
        this.mIsValid = z;
    }

    private CivicLocation(Parcel parcel) {
        this.mCivicAddressElements = new SparseArray<>(3);
        this.mIsValid = parcel.readByte() != 0;
        this.mCountryCode = parcel.readString();
        this.mCivicAddressElements = parcel.readSparseArray(getClass().getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mIsValid ? (byte) 1 : 0);
        parcel.writeString(this.mCountryCode);
        parcel.writeSparseArray(this.mCivicAddressElements);
    }

    private boolean parseCivicTLVs(byte[] bArr) {
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            byte b = bArr[i + 0] & 255;
            byte b2 = bArr[i + 1];
            if (b2 != 0) {
                int i2 = i + 2;
                if (i2 + b2 > length) {
                    return false;
                }
                this.mCivicAddressElements.put(b, new String(bArr, i2, (int) b2, StandardCharsets.UTF_8));
            }
            i += b2 + 2;
        }
        return true;
    }

    public String getCivicElementValue(int i) {
        return this.mCivicAddressElements.get(i);
    }

    public SparseArray<String> toSparseArray() {
        return this.mCivicAddressElements;
    }

    public String toString() {
        return this.mCivicAddressElements.toString();
    }

    public Address toAddress() {
        if (!this.mIsValid) {
            return null;
        }
        Address address = new Address(Locale.f700US);
        String formatAddressElement = formatAddressElement("Room: ", getCivicElementValue(28));
        String formatAddressElement2 = formatAddressElement(" Desk: ", getCivicElementValue(33));
        String formatAddressElement3 = formatAddressElement(", Flr: ", getCivicElementValue(27));
        String formatAddressElement4 = formatAddressElement("", getCivicElementValue(19));
        String formatAddressElement5 = formatAddressElement("", getCivicElementValue(20));
        String formatAddressElement6 = formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(34));
        String formatAddressElement7 = formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(18));
        String formatAddressElement8 = formatAddressElement(", Apt: ", getCivicElementValue(26));
        String formatAddressElement9 = formatAddressElement("", getCivicElementValue(3));
        String str = formatAddressElement + formatAddressElement2 + formatAddressElement3;
        String str2 = formatAddressElement("", getCivicElementValue(1)) + formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(24));
        String str3 = this.mCountryCode;
        address.setAddressLine(0, str);
        address.setAddressLine(1, formatAddressElement4 + formatAddressElement5 + formatAddressElement6 + formatAddressElement7 + formatAddressElement8);
        address.setAddressLine(2, formatAddressElement9);
        address.setAddressLine(3, str2);
        address.setAddressLine(4, str3);
        address.setFeatureName(getCivicElementValue(23));
        address.setSubThoroughfare(getCivicElementValue(19));
        address.setThoroughfare(getCivicElementValue(34));
        address.setSubLocality(getCivicElementValue(5));
        address.setSubAdminArea(getCivicElementValue(2));
        address.setAdminArea(getCivicElementValue(1));
        address.setPostalCode(getCivicElementValue(24));
        address.setCountryCode(this.mCountryCode);
        return address;
    }

    private String formatAddressElement(String str, String str2) {
        if (str2 == null) {
            return "";
        }
        return str + str2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CivicLocation)) {
            return false;
        }
        CivicLocation civicLocation = (CivicLocation) obj;
        if (this.mIsValid != civicLocation.mIsValid || !Objects.equals(this.mCountryCode, civicLocation.mCountryCode) || !isSparseArrayStringEqual(this.mCivicAddressElements, civicLocation.mCivicAddressElements)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mIsValid), this.mCountryCode, getSparseArrayKeys(this.mCivicAddressElements), getSparseArrayValues(this.mCivicAddressElements));
    }

    public boolean isValid() {
        return this.mIsValid;
    }

    private boolean isSparseArrayStringEqual(SparseArray<String> sparseArray, SparseArray<String> sparseArray2) {
        int size = sparseArray.size();
        if (size != sparseArray2.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!sparseArray.valueAt(i).equals(sparseArray2.valueAt(i))) {
                return false;
            }
        }
        return true;
    }

    private int[] getSparseArrayKeys(SparseArray<String> sparseArray) {
        int size = sparseArray.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = sparseArray.keyAt(i);
        }
        return iArr;
    }

    private String[] getSparseArrayValues(SparseArray<String> sparseArray) {
        int size = sparseArray.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = sparseArray.valueAt(i);
        }
        return strArr;
    }
}
