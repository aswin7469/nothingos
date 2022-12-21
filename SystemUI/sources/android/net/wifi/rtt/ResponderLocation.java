package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.location.Address;
import android.location.Location;
import android.net.MacAddress;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.MimeTypeMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ResponderLocation implements Parcelable {
    public static final int ALTITUDE_FLOORS = 2;
    private static final int ALTITUDE_FRACTION_BITS = 8;
    public static final int ALTITUDE_METERS = 1;
    private static final int ALTITUDE_UNCERTAINTY_BASE = 21;
    public static final int ALTITUDE_UNDEFINED = 0;
    private static final int BYTES_IN_A_BSSID = 6;
    private static final int BYTE_MASK = 255;
    private static final int CIVIC_COUNTRY_CODE_INDEX = 0;
    private static final int CIVIC_TLV_LIST_INDEX = 2;
    public static final Parcelable.Creator<ResponderLocation> CREATOR = new Parcelable.Creator<ResponderLocation>() {
        public ResponderLocation createFromParcel(Parcel parcel) {
            return new ResponderLocation(parcel);
        }

        public ResponderLocation[] newArray(int i) {
            return new ResponderLocation[i];
        }
    };
    public static final int DATUM_NAD83_MLLW = 3;
    public static final int DATUM_NAD83_NAV88 = 2;
    public static final int DATUM_UNDEFINED = 0;
    public static final int DATUM_WGS84 = 1;
    private static final int LATLNG_FRACTION_BITS = 25;
    private static final int LATLNG_UNCERTAINTY_BASE = 8;
    private static final double LAT_ABS_LIMIT = 90.0d;
    public static final int LCI_VERSION_1 = 1;
    private static final byte[] LEAD_LCI_ELEMENT_BYTES = {1, 0, 8};
    private static final byte[] LEAD_LCR_ELEMENT_BYTES = {1, 0, 11};
    private static final double LNG_ABS_LIMIT = 180.0d;
    public static final int LOCATION_FIXED = 0;
    public static final int LOCATION_MOVEMENT_UNKNOWN = 2;
    private static final String LOCATION_PROVIDER = "WiFi Access Point";
    public static final int LOCATION_RESERVED = 3;
    public static final int LOCATION_VARIABLE = 1;
    private static final int LSB_IN_BYTE = 1;
    private static final int MAP_TYPE_URL_DEFINED = 0;
    private static final int MAX_BUFFER_SIZE = 256;
    private static final byte MEASUREMENT_REPORT_MODE = 0;
    private static final byte MEASUREMENT_TOKEN_AUTONOMOUS = 1;
    private static final byte MEASUREMENT_TYPE_LCI = 8;
    private static final byte MEASUREMENT_TYPE_LCR = 11;
    private static final int MIN_BUFFER_SIZE = 3;
    private static final int MSB_IN_BYTE = 128;
    private static final byte SUBELEMENT_BSSID_LIST = 7;
    private static final int SUBELEMENT_BSSID_LIST_INDEX = 1;
    private static final int SUBELEMENT_BSSID_LIST_MIN_BUFFER_LENGTH = 1;
    private static final int SUBELEMENT_BSSID_MAX_INDICATOR_INDEX = 0;
    private static final int SUBELEMENT_IMAGE_MAP_TYPE_INDEX = 0;
    private static final byte SUBELEMENT_LCI = 0;
    private static final int SUBELEMENT_LCI_ALT_INDEX = 6;
    private static final int SUBELEMENT_LCI_ALT_TYPE_INDEX = 4;
    private static final int SUBELEMENT_LCI_ALT_UNCERTAINTY_INDEX = 5;
    private static final int[] SUBELEMENT_LCI_BIT_FIELD_LENGTHS = {6, 34, 6, 34, 4, 6, 30, 3, 1, 1, 1, 2};
    private static final int SUBELEMENT_LCI_DATUM_INDEX = 7;
    private static final int SUBELEMENT_LCI_DEPENDENT_STA_INDEX = 10;
    private static final int SUBELEMENT_LCI_LAT_INDEX = 1;
    private static final int SUBELEMENT_LCI_LAT_UNCERTAINTY_INDEX = 0;
    private static final int SUBELEMENT_LCI_LENGTH = 16;
    private static final int SUBELEMENT_LCI_LNG_INDEX = 3;
    private static final int SUBELEMENT_LCI_LNG_UNCERTAINTY_INDEX = 2;
    private static final int SUBELEMENT_LCI_REGLOC_AGREEMENT_INDEX = 8;
    private static final int SUBELEMENT_LCI_REGLOC_DSE_INDEX = 9;
    private static final int SUBELEMENT_LCI_VERSION_INDEX = 11;
    private static final byte SUBELEMENT_LOCATION_CIVIC = 0;
    private static final int SUBELEMENT_LOCATION_CIVIC_MAX_LENGTH = 256;
    private static final int SUBELEMENT_LOCATION_CIVIC_MIN_LENGTH = 2;
    private static final byte SUBELEMENT_MAP_IMAGE = 5;
    private static final int SUBELEMENT_MAP_IMAGE_URL_MAX_LENGTH = 256;
    private static final byte SUBELEMENT_USAGE = 6;
    private static final int SUBELEMENT_USAGE_LENGTH1 = 1;
    private static final int SUBELEMENT_USAGE_LENGTH3 = 3;
    private static final int SUBELEMENT_USAGE_MASK_RETENTION_EXPIRES = 2;
    private static final int SUBELEMENT_USAGE_MASK_RETRANSMIT = 1;
    private static final int SUBELEMENT_USAGE_MASK_STA_LOCATION_POLICY = 4;
    private static final int SUBELEMENT_USAGE_PARAMS_INDEX = 0;
    private static final byte SUBELEMENT_Z = 4;
    private static final int[] SUBELEMENT_Z_BIT_FIELD_LENGTHS = {2, 14, 24, 8};
    private static final int SUBELEMENT_Z_FLOOR_NUMBER_INDEX = 1;
    private static final int SUBELEMENT_Z_HEIGHT_ABOVE_FLOOR_INDEX = 2;
    private static final int SUBELEMENT_Z_HEIGHT_ABOVE_FLOOR_UNCERTAINTY_INDEX = 3;
    private static final int SUBELEMENT_Z_LAT_EXPECTED_TO_MOVE_INDEX = 0;
    private static final int SUBELEMENT_Z_LENGTH = 6;
    private static final String[] SUPPORTED_IMAGE_FILE_EXTENSIONS = {"", "png", "gif", "jpg", "svg", "dxf", "dwg", "dwf", "cad", "tif", "gml", "kml", "bmp", "pgm", "ppm", "xbm", "xpm", "ico"};
    private static final int UNCERTAINTY_UNDEFINED = 0;
    private static final int Z_FLOOR_HEIGHT_FRACTION_BITS = 12;
    private static final int Z_FLOOR_NUMBER_FRACTION_BITS = 4;
    private static final int Z_MAX_HEIGHT_UNCERTAINTY_FACTOR = 25;
    private double mAltitude;
    private int mAltitudeType;
    private double mAltitudeUncertainty;
    private ArrayList<MacAddress> mBssidList;
    private CivicLocation mCivicLocation;
    private String mCivicLocationCountryCode;
    private String mCivicLocationString;
    private int mDatum;
    private int mExpectedToMove;
    private double mFloorNumber;
    private double mHeightAboveFloorMeters;
    private double mHeightAboveFloorUncertaintyMeters;
    private boolean mIsBssidListValid;
    private boolean mIsLciValid;
    private boolean mIsLocationCivicValid;
    private boolean mIsMapImageValid;
    private boolean mIsUsageValid;
    private final boolean mIsValid;
    private boolean mIsZValid;
    private double mLatitude;
    private double mLatitudeUncertainty;
    private boolean mLciDependentStation;
    private boolean mLciRegisteredLocationAgreement;
    private boolean mLciRegisteredLocationDse;
    private int mLciVersion;
    private double mLongitude;
    private double mLongitudeUncertainty;
    private int mMapImageType;
    private Uri mMapImageUri;
    private boolean mUsageExtraInfoOnAssociation;
    private boolean mUsageRetentionExpires;
    private boolean mUsageRetransmit;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AltitudeType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DatumType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExpectedToMoveType {
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ResponderLocation(byte[] r6, byte[] r7) {
        /*
            r5 = this;
            r5.<init>()
            r0 = 0
            r5.mIsLciValid = r0
            r5.mIsZValid = r0
            r1 = 1
            r5.mIsUsageValid = r1
            r5.mIsBssidListValid = r0
            r5.mIsLocationCivicValid = r0
            r5.mIsMapImageValid = r0
            r5.setLciSubelementDefaults()
            r5.setZaxisSubelementDefaults()
            r5.setUsageSubelementDefaults()
            r5.setBssidListSubelementDefaults()
            r5.setCivicLocationSubelementDefaults()
            r5.setMapImageSubelementDefaults()
            if (r6 == 0) goto L_0x0032
            int r2 = r6.length
            byte[] r3 = LEAD_LCI_ELEMENT_BYTES
            int r4 = r3.length
            if (r2 <= r4) goto L_0x0032
            r2 = 8
            boolean r6 = r5.parseInformationElementBuffer(r2, r6, r3)
            goto L_0x0033
        L_0x0032:
            r6 = r0
        L_0x0033:
            if (r7 == 0) goto L_0x0042
            int r2 = r7.length
            byte[] r3 = LEAD_LCR_ELEMENT_BYTES
            int r4 = r3.length
            if (r2 <= r4) goto L_0x0042
            r2 = 11
            boolean r7 = r5.parseInformationElementBuffer(r2, r7, r3)
            goto L_0x0043
        L_0x0042:
            r7 = r0
        L_0x0043:
            if (r6 == 0) goto L_0x0057
            boolean r6 = r5.mIsUsageValid
            if (r6 == 0) goto L_0x0057
            boolean r6 = r5.mIsLciValid
            if (r6 != 0) goto L_0x0055
            boolean r6 = r5.mIsZValid
            if (r6 != 0) goto L_0x0055
            boolean r6 = r5.mIsBssidListValid
            if (r6 == 0) goto L_0x0057
        L_0x0055:
            r6 = r1
            goto L_0x0058
        L_0x0057:
            r6 = r0
        L_0x0058:
            if (r7 == 0) goto L_0x0068
            boolean r7 = r5.mIsUsageValid
            if (r7 == 0) goto L_0x0068
            boolean r7 = r5.mIsLocationCivicValid
            if (r7 != 0) goto L_0x0066
            boolean r7 = r5.mIsMapImageValid
            if (r7 == 0) goto L_0x0068
        L_0x0066:
            r7 = r1
            goto L_0x0069
        L_0x0068:
            r7 = r0
        L_0x0069:
            if (r6 != 0) goto L_0x006d
            if (r7 == 0) goto L_0x006e
        L_0x006d:
            r0 = r1
        L_0x006e:
            r5.mIsValid = r0
            if (r0 != 0) goto L_0x007e
            r5.setLciSubelementDefaults()
            r5.setZaxisSubelementDefaults()
            r5.setCivicLocationSubelementDefaults()
            r5.setMapImageSubelementDefaults()
        L_0x007e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.rtt.ResponderLocation.<init>(byte[], byte[]):void");
    }

    private ResponderLocation(Parcel parcel) {
        boolean z = false;
        this.mIsLciValid = false;
        this.mIsZValid = false;
        this.mIsUsageValid = true;
        this.mIsBssidListValid = false;
        this.mIsLocationCivicValid = false;
        this.mIsMapImageValid = false;
        this.mIsValid = parcel.readByte() != 0;
        this.mIsLciValid = parcel.readByte() != 0;
        this.mIsZValid = parcel.readByte() != 0;
        this.mIsUsageValid = parcel.readByte() != 0;
        this.mIsBssidListValid = parcel.readByte() != 0;
        this.mIsLocationCivicValid = parcel.readByte() != 0;
        this.mIsMapImageValid = parcel.readByte() != 0;
        this.mLatitudeUncertainty = parcel.readDouble();
        this.mLatitude = parcel.readDouble();
        this.mLongitudeUncertainty = parcel.readDouble();
        this.mLongitude = parcel.readDouble();
        this.mAltitudeType = parcel.readInt();
        this.mAltitudeUncertainty = parcel.readDouble();
        this.mAltitude = parcel.readDouble();
        this.mDatum = parcel.readInt();
        this.mLciRegisteredLocationAgreement = parcel.readByte() != 0;
        this.mLciRegisteredLocationDse = parcel.readByte() != 0;
        this.mLciDependentStation = parcel.readByte() != 0;
        this.mLciVersion = parcel.readInt();
        this.mExpectedToMove = parcel.readInt();
        this.mFloorNumber = parcel.readDouble();
        this.mHeightAboveFloorMeters = parcel.readDouble();
        this.mHeightAboveFloorUncertaintyMeters = parcel.readDouble();
        this.mUsageRetransmit = parcel.readByte() != 0;
        this.mUsageRetentionExpires = parcel.readByte() != 0;
        this.mUsageExtraInfoOnAssociation = parcel.readByte() != 0 ? true : z;
        this.mBssidList = parcel.readArrayList(MacAddress.class.getClassLoader());
        this.mCivicLocationCountryCode = parcel.readString();
        this.mCivicLocationString = parcel.readString();
        this.mCivicLocation = (CivicLocation) parcel.readParcelable(getClass().getClassLoader());
        this.mMapImageType = parcel.readInt();
        String readString = parcel.readString();
        if (TextUtils.isEmpty(readString)) {
            this.mMapImageUri = null;
        } else {
            this.mMapImageUri = Uri.parse(readString);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mIsValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsLciValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsZValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsUsageValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsBssidListValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsLocationCivicValid ? (byte) 1 : 0);
        parcel.writeByte(this.mIsMapImageValid ? (byte) 1 : 0);
        parcel.writeDouble(this.mLatitudeUncertainty);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitudeUncertainty);
        parcel.writeDouble(this.mLongitude);
        parcel.writeInt(this.mAltitudeType);
        parcel.writeDouble(this.mAltitudeUncertainty);
        parcel.writeDouble(this.mAltitude);
        parcel.writeInt(this.mDatum);
        parcel.writeByte(this.mLciRegisteredLocationAgreement ? (byte) 1 : 0);
        parcel.writeByte(this.mLciRegisteredLocationDse ? (byte) 1 : 0);
        parcel.writeByte(this.mLciDependentStation ? (byte) 1 : 0);
        parcel.writeInt(this.mLciVersion);
        parcel.writeInt(this.mExpectedToMove);
        parcel.writeDouble(this.mFloorNumber);
        parcel.writeDouble(this.mHeightAboveFloorMeters);
        parcel.writeDouble(this.mHeightAboveFloorUncertaintyMeters);
        parcel.writeByte(this.mUsageRetransmit ? (byte) 1 : 0);
        parcel.writeByte(this.mUsageRetentionExpires ? (byte) 1 : 0);
        parcel.writeByte(this.mUsageExtraInfoOnAssociation ? (byte) 1 : 0);
        parcel.writeList(this.mBssidList);
        parcel.writeString(this.mCivicLocationCountryCode);
        parcel.writeString(this.mCivicLocationString);
        parcel.writeParcelable(this.mCivicLocation, i);
        parcel.writeInt(this.mMapImageType);
        Uri uri = this.mMapImageUri;
        if (uri != null) {
            parcel.writeString(uri.toString());
        } else {
            parcel.writeString("");
        }
    }

    private boolean parseInformationElementBuffer(int i, byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        if (length < 3 || length > 256 || !Arrays.equals(Arrays.copyOfRange(bArr, 0, bArr2.length), bArr2)) {
            return false;
        }
        int length2 = bArr2.length + 0;
        while (true) {
            int i2 = length2 + 1;
            if (i2 >= length) {
                return true;
            }
            byte b = bArr[length2];
            int i3 = i2 + 1;
            byte b2 = bArr[i2];
            int i4 = i3 + b2;
            if (i4 > length || b2 <= 0) {
                return false;
            }
            byte[] copyOfRange = Arrays.copyOfRange(bArr, i3, i4);
            if (i == 8) {
                if (b == 0) {
                    boolean parseSubelementLci = parseSubelementLci(copyOfRange);
                    this.mIsLciValid = parseSubelementLci;
                    if (!parseSubelementLci || this.mLciVersion != 1) {
                        setLciSubelementDefaults();
                    }
                } else if (b == 4) {
                    boolean parseSubelementZ = parseSubelementZ(copyOfRange);
                    this.mIsZValid = parseSubelementZ;
                    if (!parseSubelementZ) {
                        setZaxisSubelementDefaults();
                    }
                } else if (b == 6) {
                    this.mIsUsageValid = parseSubelementUsage(copyOfRange);
                } else if (b == 7) {
                    boolean parseSubelementBssidList = parseSubelementBssidList(copyOfRange);
                    this.mIsBssidListValid = parseSubelementBssidList;
                    if (!parseSubelementBssidList) {
                        setBssidListSubelementDefaults();
                    }
                }
            } else if (i == 11) {
                if (b == 0) {
                    boolean parseSubelementLocationCivic = parseSubelementLocationCivic(copyOfRange);
                    this.mIsLocationCivicValid = parseSubelementLocationCivic;
                    if (!parseSubelementLocationCivic) {
                        setCivicLocationSubelementDefaults();
                    }
                } else if (b == 5) {
                    boolean parseSubelementMapImage = parseSubelementMapImage(copyOfRange);
                    this.mIsMapImageValid = parseSubelementMapImage;
                    if (!parseSubelementMapImage) {
                        setMapImageSubelementDefaults();
                    }
                }
            }
            length2 = i4;
        }
        return false;
    }

    private boolean parseSubelementLci(byte[] bArr) {
        boolean z = false;
        if (bArr.length > 16) {
            return false;
        }
        swapEndianByteByByte(bArr);
        int[] iArr = SUBELEMENT_LCI_BIT_FIELD_LENGTHS;
        long[] fieldData = getFieldData(bArr, iArr);
        if (fieldData == null) {
            return false;
        }
        this.mLatitudeUncertainty = decodeLciLatLngUncertainty(fieldData[0]);
        int[] iArr2 = iArr;
        this.mLatitude = decodeLciLatLng(fieldData, iArr2, 1, LAT_ABS_LIMIT);
        this.mLongitudeUncertainty = decodeLciLatLngUncertainty(fieldData[2]);
        this.mLongitude = decodeLciLatLng(fieldData, iArr2, 3, LNG_ABS_LIMIT);
        this.mAltitudeType = ((int) fieldData[4]) & 255;
        this.mAltitudeUncertainty = decodeLciAltUncertainty(fieldData[5]);
        this.mAltitude = (double) Math.scalb((float) fieldData[6], -8);
        this.mDatum = ((int) fieldData[7]) & 255;
        this.mLciRegisteredLocationAgreement = fieldData[8] == 1;
        this.mLciRegisteredLocationDse = fieldData[9] == 1;
        if (fieldData[10] == 1) {
            z = true;
        }
        this.mLciDependentStation = z;
        this.mLciVersion = (int) fieldData[11];
        return true;
    }

    private double decodeLciLatLng(long[] jArr, int[] iArr, int i, double d) {
        double d2;
        if ((jArr[i] & ((long) Math.pow(2.0d, (double) (iArr[i] - 1)))) != 0) {
            d2 = Math.scalb(((double) jArr[i]) - Math.pow(2.0d, (double) iArr[i]), -25);
        } else {
            d2 = Math.scalb((double) jArr[i], -25);
        }
        if (d2 > d) {
            return d;
        }
        double d3 = -d;
        return d2 < d3 ? d3 : d2;
    }

    private double decodeLciLatLngUncertainty(long j) {
        return Math.pow(2.0d, (double) (8 - j));
    }

    private double decodeLciAltUncertainty(long j) {
        return Math.pow(2.0d, (double) (21 - j));
    }

    private boolean parseSubelementZ(byte[] bArr) {
        if (bArr.length != 6) {
            return false;
        }
        swapEndianByteByByte(bArr);
        int[] iArr = SUBELEMENT_Z_BIT_FIELD_LENGTHS;
        long[] fieldData = getFieldData(bArr, iArr);
        if (fieldData == null) {
            return false;
        }
        this.mExpectedToMove = ((int) fieldData[0]) & 255;
        this.mFloorNumber = decodeZUnsignedToSignedValue(fieldData, iArr, 1, 4);
        this.mHeightAboveFloorMeters = decodeZUnsignedToSignedValue(fieldData, iArr, 2, 12);
        long j = fieldData[3];
        if (j <= 0 || j >= 25) {
            return false;
        }
        this.mHeightAboveFloorUncertaintyMeters = Math.pow(2.0d, (double) ((12 - j) - 1));
        return true;
    }

    private double decodeZUnsignedToSignedValue(long[] jArr, int[] iArr, int i, int i2) {
        int i3 = (int) jArr[i];
        if (i3 > ((int) Math.pow(2.0d, (double) (iArr[i] - 1))) - 1) {
            i3 = (int) (((double) i3) - Math.pow(2.0d, (double) iArr[i]));
        }
        return (double) Math.scalb((float) i3, -i2);
    }

    private boolean parseSubelementUsage(byte[] bArr) {
        if (bArr.length != 1 && bArr.length != 3) {
            return false;
        }
        byte b = bArr[0];
        boolean z = (b & 1) != 0;
        this.mUsageRetransmit = z;
        boolean z2 = (b & 2) != 0;
        this.mUsageRetentionExpires = z2;
        this.mUsageExtraInfoOnAssociation = (b & 4) != 0;
        if (!z || z2) {
            return false;
        }
        return true;
    }

    private boolean parseSubelementBssidList(byte[] bArr) {
        int i = 0;
        if (bArr.length < 1 || (bArr.length - 1) % 6 != 0) {
            return false;
        }
        byte b = bArr[0];
        int length = (bArr.length - 1) / 6;
        int i2 = 1;
        while (i < length) {
            int i3 = i2 + 6;
            this.mBssidList.add(MacAddress.fromBytes(Arrays.copyOfRange(bArr, i2, i3)));
            i++;
            i2 = i3;
        }
        return true;
    }

    private boolean parseSubelementLocationCivic(byte[] bArr) {
        if (bArr.length < 2 || bArr.length > 256) {
            return false;
        }
        this.mCivicLocationCountryCode = new String(Arrays.copyOfRange(bArr, 0, 2)).toUpperCase();
        CivicLocation civicLocation = new CivicLocation(Arrays.copyOfRange(bArr, 2, bArr.length), this.mCivicLocationCountryCode);
        if (!civicLocation.isValid()) {
            return false;
        }
        this.mCivicLocation = civicLocation;
        this.mCivicLocationString = civicLocation.toString();
        return true;
    }

    private boolean parseSubelementMapImage(byte[] bArr) {
        if (bArr.length > 256) {
            return false;
        }
        byte b = bArr[0];
        int length = SUPPORTED_IMAGE_FILE_EXTENSIONS.length - 1;
        if (b < 0 || b > length) {
            return false;
        }
        this.mMapImageType = b;
        this.mMapImageUri = Uri.parse(new String(Arrays.copyOfRange(bArr, 1, bArr.length), StandardCharsets.UTF_8));
        return true;
    }

    private String imageTypeToMime(int i, String str) {
        String[] strArr = SUPPORTED_IMAGE_FILE_EXTENSIONS;
        int length = strArr.length - 1;
        if ((i == 0 && str == null) || i > length) {
            return null;
        }
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        if (i == 0) {
            return singleton.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
        }
        return singleton.getMimeTypeFromExtension(strArr[i]);
    }

    private long[] getFieldData(byte[] bArr, int[] iArr) {
        int length = bArr.length * 8;
        int i = 0;
        for (int i2 : iArr) {
            if (i2 > 64) {
                return null;
            }
            i += i2;
        }
        if (length != i) {
            return null;
        }
        long[] jArr = new long[iArr.length];
        int i3 = 0;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            int i5 = iArr[i4];
            long j = 0;
            for (int i6 = 0; i6 < i5; i6++) {
                j |= ((long) getBitAtBitOffsetInByteArray(bArr, i3 + i6)) << i6;
            }
            jArr[i4] = j;
            i3 += i5;
        }
        return jArr;
    }

    private int getBitAtBitOffsetInByteArray(byte[] bArr, int i) {
        return (bArr[i / 8] & (128 >> (i % 8))) == 0 ? 0 : 1;
    }

    private void swapEndianByteByByte(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            byte b2 = 1;
            byte b3 = 0;
            for (int i2 = 0; i2 < 8; i2++) {
                b3 = (byte) (b3 << 1);
                if ((b & b2) != 0) {
                    b3 = (byte) (b3 | 1);
                }
                b2 = (byte) (b2 << 1);
            }
            bArr[i] = b3;
        }
    }

    private void setLciSubelementDefaults() {
        this.mIsLciValid = false;
        this.mLatitudeUncertainty = 0.0d;
        this.mLatitude = 0.0d;
        this.mLongitudeUncertainty = 0.0d;
        this.mLongitude = 0.0d;
        this.mAltitudeType = 0;
        this.mAltitudeUncertainty = 0.0d;
        this.mAltitude = 0.0d;
        this.mDatum = 0;
        this.mLciRegisteredLocationAgreement = false;
        this.mLciRegisteredLocationDse = false;
        this.mLciDependentStation = false;
        this.mLciVersion = 0;
    }

    private void setZaxisSubelementDefaults() {
        this.mIsZValid = false;
        this.mExpectedToMove = 0;
        this.mFloorNumber = 0.0d;
        this.mHeightAboveFloorMeters = 0.0d;
        this.mHeightAboveFloorUncertaintyMeters = 0.0d;
    }

    private void setUsageSubelementDefaults() {
        this.mUsageRetransmit = true;
        this.mUsageRetentionExpires = false;
        this.mUsageExtraInfoOnAssociation = false;
    }

    private void setBssidListSubelementDefaults() {
        this.mIsBssidListValid = false;
        this.mBssidList = new ArrayList<>();
    }

    public void setCivicLocationSubelementDefaults() {
        this.mIsLocationCivicValid = false;
        this.mCivicLocationCountryCode = "";
        this.mCivicLocationString = "";
        this.mCivicLocation = null;
    }

    private void setMapImageSubelementDefaults() {
        this.mIsMapImageValid = false;
        this.mMapImageType = 0;
        this.mMapImageUri = null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ResponderLocation responderLocation = (ResponderLocation) obj;
        if (this.mIsValid == responderLocation.mIsValid && this.mIsLciValid == responderLocation.mIsLciValid && this.mIsZValid == responderLocation.mIsZValid && this.mIsUsageValid == responderLocation.mIsUsageValid && this.mIsBssidListValid == responderLocation.mIsBssidListValid && this.mIsLocationCivicValid == responderLocation.mIsLocationCivicValid && this.mIsMapImageValid == responderLocation.mIsMapImageValid && this.mLatitudeUncertainty == responderLocation.mLatitudeUncertainty && this.mLatitude == responderLocation.mLatitude && this.mLongitudeUncertainty == responderLocation.mLongitudeUncertainty && this.mLongitude == responderLocation.mLongitude && this.mAltitudeType == responderLocation.mAltitudeType && this.mAltitudeUncertainty == responderLocation.mAltitudeUncertainty && this.mAltitude == responderLocation.mAltitude && this.mDatum == responderLocation.mDatum && this.mLciRegisteredLocationAgreement == responderLocation.mLciRegisteredLocationAgreement && this.mLciRegisteredLocationDse == responderLocation.mLciRegisteredLocationDse && this.mLciDependentStation == responderLocation.mLciDependentStation && this.mLciVersion == responderLocation.mLciVersion && this.mExpectedToMove == responderLocation.mExpectedToMove && this.mFloorNumber == responderLocation.mFloorNumber && this.mHeightAboveFloorMeters == responderLocation.mHeightAboveFloorMeters && this.mHeightAboveFloorUncertaintyMeters == responderLocation.mHeightAboveFloorUncertaintyMeters && this.mUsageRetransmit == responderLocation.mUsageRetransmit && this.mUsageRetentionExpires == responderLocation.mUsageRetentionExpires && this.mUsageExtraInfoOnAssociation == responderLocation.mUsageExtraInfoOnAssociation && this.mBssidList.equals(responderLocation.mBssidList) && this.mCivicLocationCountryCode.equals(responderLocation.mCivicLocationCountryCode) && this.mCivicLocationString.equals(responderLocation.mCivicLocationString) && Objects.equals(this.mCivicLocation, responderLocation.mCivicLocation) && this.mMapImageType == responderLocation.mMapImageType && Objects.equals(this.mMapImageUri, responderLocation.mMapImageUri)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mIsValid), Boolean.valueOf(this.mIsLciValid), Boolean.valueOf(this.mIsZValid), Boolean.valueOf(this.mIsUsageValid), Boolean.valueOf(this.mIsBssidListValid), Boolean.valueOf(this.mIsLocationCivicValid), Boolean.valueOf(this.mIsMapImageValid), Double.valueOf(this.mLatitudeUncertainty), Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitudeUncertainty), Double.valueOf(this.mLongitude), Integer.valueOf(this.mAltitudeType), Double.valueOf(this.mAltitudeUncertainty), Double.valueOf(this.mAltitude), Integer.valueOf(this.mDatum), Boolean.valueOf(this.mLciRegisteredLocationAgreement), Boolean.valueOf(this.mLciRegisteredLocationDse), Boolean.valueOf(this.mLciDependentStation), Integer.valueOf(this.mLciVersion), Integer.valueOf(this.mExpectedToMove), Double.valueOf(this.mFloorNumber), Double.valueOf(this.mHeightAboveFloorMeters), Double.valueOf(this.mHeightAboveFloorUncertaintyMeters), Boolean.valueOf(this.mUsageRetransmit), Boolean.valueOf(this.mUsageRetentionExpires), Boolean.valueOf(this.mUsageExtraInfoOnAssociation), this.mBssidList, this.mCivicLocationCountryCode, this.mCivicLocationString, this.mCivicLocation, Integer.valueOf(this.mMapImageType), this.mMapImageUri);
    }

    public boolean isValid() {
        return this.mIsValid;
    }

    public boolean isLciSubelementValid() {
        return this.mIsLciValid;
    }

    public double getLatitudeUncertainty() {
        if (this.mIsLciValid) {
            return this.mLatitudeUncertainty;
        }
        throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
    }

    public double getLatitude() {
        if (this.mIsLciValid) {
            return this.mLatitude;
        }
        throw new IllegalStateException("getLatitude(): invoked on an invalid result: mIsLciValid = false.");
    }

    public double getLongitudeUncertainty() {
        if (this.mIsLciValid) {
            return this.mLongitudeUncertainty;
        }
        throw new IllegalStateException("getLongitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
    }

    public double getLongitude() {
        if (this.mIsLciValid) {
            return this.mLongitude;
        }
        throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
    }

    public int getAltitudeType() {
        if (this.mIsLciValid) {
            return this.mAltitudeType;
        }
        throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
    }

    public double getAltitudeUncertainty() {
        if (this.mIsLciValid) {
            return this.mAltitudeUncertainty;
        }
        throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
    }

    public double getAltitude() {
        if (this.mIsLciValid) {
            return this.mAltitude;
        }
        throw new IllegalStateException("getAltitude(): invoked on an invalid result: mIsLciValid = false.");
    }

    public int getDatum() {
        if (this.mIsLciValid) {
            return this.mDatum;
        }
        throw new IllegalStateException("getDatum(): invoked on an invalid result: mIsLciValid = false.");
    }

    public boolean getRegisteredLocationAgreementIndication() {
        if (this.mIsLciValid) {
            return this.mLciRegisteredLocationAgreement;
        }
        throw new IllegalStateException("getRegisteredLocationAgreementIndication(): invoked on an invalid result: mIsLciValid = false.");
    }

    public boolean getRegisteredLocationDseIndication() {
        if (this.mIsLciValid) {
            return this.mLciRegisteredLocationDse;
        }
        throw new IllegalStateException("getRegisteredLocationDseIndication(): invoked on an invalid result: mIsLciValid = false.");
    }

    public boolean getDependentStationIndication() {
        if (this.mIsLciValid) {
            return this.mLciDependentStation;
        }
        throw new IllegalStateException("getDependentStationIndication(): invoked on an invalid result: mIsLciValid = false.");
    }

    public int getLciVersion() {
        if (this.mIsLciValid) {
            return this.mLciVersion;
        }
        throw new IllegalStateException("getLciVersion(): invoked on an invalid result: mIsLciValid = false.");
    }

    public Location toLocation() {
        if (this.mIsLciValid) {
            Location location = new Location(LOCATION_PROVIDER);
            location.setLatitude(this.mLatitude);
            location.setLongitude(this.mLongitude);
            location.setAccuracy(((float) (this.mLatitudeUncertainty + this.mLongitudeUncertainty)) / 2.0f);
            location.setAltitude(this.mAltitude);
            location.setVerticalAccuracyMeters((float) this.mAltitudeUncertainty);
            location.setTime(System.currentTimeMillis());
            return location;
        }
        throw new IllegalStateException("toLocation(): invoked on an invalid result: mIsLciValid = false.");
    }

    public boolean isZaxisSubelementValid() {
        return this.mIsZValid;
    }

    public int getExpectedToMove() {
        if (this.mIsZValid) {
            return this.mExpectedToMove;
        }
        throw new IllegalStateException("getExpectedToMove(): invoked on an invalid result: mIsZValid = false.");
    }

    public double getFloorNumber() {
        if (this.mIsZValid) {
            return this.mFloorNumber;
        }
        throw new IllegalStateException("getFloorNumber(): invoked on an invalid result: mIsZValid = false)");
    }

    public double getHeightAboveFloorMeters() {
        if (this.mIsZValid) {
            return this.mHeightAboveFloorMeters;
        }
        throw new IllegalStateException("getHeightAboveFloorMeters(): invoked on an invalid result: mIsZValid = false)");
    }

    public double getHeightAboveFloorUncertaintyMeters() {
        if (this.mIsZValid) {
            return this.mHeightAboveFloorUncertaintyMeters;
        }
        throw new IllegalStateException("getHeightAboveFloorUncertaintyMeters():invoked on an invalid result: mIsZValid = false)");
    }

    public boolean getRetransmitPolicyIndication() {
        return this.mUsageRetransmit;
    }

    public boolean getRetentionExpiresIndication() {
        return this.mUsageRetentionExpires;
    }

    @SystemApi
    public boolean getExtraInfoOnAssociationIndication() {
        return this.mUsageExtraInfoOnAssociation;
    }

    public List<MacAddress> getColocatedBssids() {
        return Collections.unmodifiableList(this.mBssidList);
    }

    public Address toCivicLocationAddress() {
        CivicLocation civicLocation = this.mCivicLocation;
        if (civicLocation == null || !civicLocation.isValid()) {
            return null;
        }
        return this.mCivicLocation.toAddress();
    }

    public SparseArray<String> toCivicLocationSparseArray() {
        CivicLocation civicLocation = this.mCivicLocation;
        if (civicLocation == null || !civicLocation.isValid()) {
            return null;
        }
        return this.mCivicLocation.toSparseArray();
    }

    public String getCivicLocationCountryCode() {
        return this.mCivicLocationCountryCode;
    }

    public String getCivicLocationElementValue(int i) {
        return this.mCivicLocation.getCivicElementValue(i);
    }

    public String getMapImageMimeType() {
        Uri uri = this.mMapImageUri;
        if (uri == null) {
            return null;
        }
        return imageTypeToMime(this.mMapImageType, uri.toString());
    }

    public Uri getMapImageUri() {
        return this.mMapImageUri;
    }
}
