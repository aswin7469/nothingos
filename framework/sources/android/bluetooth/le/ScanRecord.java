package android.bluetooth.le;

import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    public static int DATA_TYPE_GROUP_AD_TYPE = 0;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
    private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
    private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_128_BIT = 21;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_16_BIT = 20;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_32_BIT = 31;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TRANSPORT_DISCOVERY_DATA = 38;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private final byte[] mGroupIdentifierData;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceSolicitationUuids;
    private final List<ParcelUuid> mServiceUuids;
    private final byte[] mTDSData;
    private final int mTxPowerLevel;

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public List<ParcelUuid> getServiceSolicitationUuids() {
        return this.mServiceSolicitationUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        SparseArray<byte[]> sparseArray = this.mManufacturerSpecificData;
        if (sparseArray == null) {
            return null;
        }
        return sparseArray.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        Map<ParcelUuid, byte[]> map;
        if (serviceDataUuid == null || (map = this.mServiceData) == null) {
            return null;
        }
        return map.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getTDSData() {
        return this.mTDSData;
    }

    public byte[] getGroupIdentifierData() {
        return this.mGroupIdentifierData;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public boolean matchesAnyField(Predicate<byte[]> matcher) {
        int length;
        int pos = 0;
        while (true) {
            byte[] bArr = this.mBytes;
            if (pos < bArr.length && (length = bArr[pos] & 255) != 0) {
                if (matcher.test(Arrays.copyOfRange(bArr, pos, pos + length + 1))) {
                    return true;
                }
                pos += length + 1;
            } else {
                return false;
            }
        }
    }

    private ScanRecord(List<ParcelUuid> serviceUuids, List<ParcelUuid> serviceSolicitationUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] tdsData, byte[] groupIdentifierData, byte[] bytes) {
        this.mServiceSolicitationUuids = serviceSolicitationUuids;
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mTDSData = tdsData;
        this.mGroupIdentifierData = groupIdentifierData;
        this.mBytes = bytes;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:(4:(7:9|10|11|12|(11:60|61|62|63|(1:65)(1:77)|66|67|68|69|70|71)(5:14|15|16|17|23)|6|7)|69|70|71)|62|63|(0)(0)|66|67|68) */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ScanRecord parseFromBytes(byte[] scanRecord) {
        List<ParcelUuid> serviceUuids;
        if (scanRecord == null) {
            return null;
        }
        List<ParcelUuid> serviceUuids2 = new ArrayList<>();
        List<ParcelUuid> serviceSolicitationUuids = new ArrayList<>();
        SparseArray<byte[]> manufacturerData = new SparseArray<>();
        Map<ParcelUuid, byte[]> serviceData = new ArrayMap<>();
        int advertiseFlag = -1;
        String localName = null;
        int txPowerLevel = -2147483648;
        byte[] tdsData = null;
        byte[] groupIdentifierData = null;
        int advertiseFlag2 = 0;
        try {
            try {
                while (advertiseFlag2 < scanRecord.length) {
                    try {
                        int currentPos = advertiseFlag2 + 1;
                        try {
                            int length = scanRecord[advertiseFlag2] & 255;
                            if (length != 0) {
                                int dataLength = length - 1;
                                int currentPos2 = currentPos + 1;
                                try {
                                    int fieldType = scanRecord[currentPos] & 255;
                                    switch (fieldType) {
                                        case 1:
                                            int advertiseFlag3 = scanRecord[currentPos2] & 255;
                                            advertiseFlag = advertiseFlag3;
                                            break;
                                        case 2:
                                        case 3:
                                            parseServiceUuid(scanRecord, currentPos2, dataLength, 2, serviceUuids2);
                                            break;
                                        case 4:
                                        case 5:
                                            parseServiceUuid(scanRecord, currentPos2, dataLength, 4, serviceUuids2);
                                            break;
                                        case 6:
                                        case 7:
                                            parseServiceUuid(scanRecord, currentPos2, dataLength, 16, serviceUuids2);
                                            break;
                                        case 8:
                                        case 9:
                                            String localName2 = new String(extractBytes(scanRecord, currentPos2, dataLength));
                                            localName = localName2;
                                            break;
                                        case 10:
                                            int txPowerLevel2 = scanRecord[currentPos2];
                                            txPowerLevel = txPowerLevel2;
                                            break;
                                        case 20:
                                            parseServiceSolicitationUuid(scanRecord, currentPos2, dataLength, 2, serviceSolicitationUuids);
                                            break;
                                        case 21:
                                            parseServiceSolicitationUuid(scanRecord, currentPos2, dataLength, 16, serviceSolicitationUuids);
                                            break;
                                        case 22:
                                        case 32:
                                        case 33:
                                            int serviceUuidLength = 2;
                                            if (fieldType == 32) {
                                                serviceUuidLength = 4;
                                            } else if (fieldType == 33) {
                                                serviceUuidLength = 16;
                                            }
                                            byte[] serviceDataUuidBytes = extractBytes(scanRecord, currentPos2, serviceUuidLength);
                                            ParcelUuid serviceDataUuid = BluetoothUuid.parseUuidFrom(serviceDataUuidBytes);
                                            byte[] serviceDataArray = extractBytes(scanRecord, currentPos2 + serviceUuidLength, dataLength - serviceUuidLength);
                                            serviceData.put(serviceDataUuid, serviceDataArray);
                                            break;
                                        case 31:
                                            parseServiceSolicitationUuid(scanRecord, currentPos2, dataLength, 4, serviceSolicitationUuids);
                                            break;
                                        case 38:
                                            byte[] tdsData2 = extractBytes(scanRecord, currentPos2, dataLength);
                                            tdsData = tdsData2;
                                            break;
                                        case 255:
                                            int manufacturerId = ((scanRecord[currentPos2 + 1] & 255) << 8) + (scanRecord[currentPos2] & 255);
                                            byte[] manufacturerDataBytes = extractBytes(scanRecord, currentPos2 + 2, dataLength - 2);
                                            manufacturerData.put(manufacturerId, manufacturerDataBytes);
                                            break;
                                        default:
                                            int advertiseFlag4 = DATA_TYPE_GROUP_AD_TYPE;
                                            if (fieldType != advertiseFlag4) {
                                                break;
                                            } else {
                                                Log.d(TAG, "Parsing Group Identifier data");
                                                groupIdentifierData = extractBytes(scanRecord, currentPos2, dataLength);
                                                break;
                                            }
                                    }
                                    advertiseFlag2 = dataLength + currentPos2;
                                } catch (Exception e) {
                                    Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
                                    return new ScanRecord(null, null, null, null, -1, Integer.MIN_VALUE, null, null, null, scanRecord);
                                }
                            } else {
                                if (serviceUuids2.isEmpty()) {
                                    serviceUuids = serviceUuids2;
                                } else {
                                    serviceUuids = null;
                                }
                                return new ScanRecord(serviceUuids, serviceSolicitationUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, tdsData, groupIdentifierData, scanRecord);
                            }
                        } catch (Exception e2) {
                        }
                    } catch (Exception e3) {
                    }
                }
                return new ScanRecord(serviceUuids, serviceSolicitationUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, tdsData, groupIdentifierData, scanRecord);
            } catch (Exception e4) {
                Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
                return new ScanRecord(null, null, null, null, -1, Integer.MIN_VALUE, null, null, null, scanRecord);
            }
            if (serviceUuids2.isEmpty()) {
            }
        } catch (Exception e5) {
        }
    }

    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + this.mAdvertiseFlags + ", mServiceUuids=" + this.mServiceUuids + ", mServiceSolicitationUuids=" + this.mServiceSolicitationUuids + ", mManufacturerSpecificData=" + BluetoothLeUtils.toString(this.mManufacturerSpecificData) + ", mServiceData=" + BluetoothLeUtils.toString(this.mServiceData) + ", mTxPowerLevel=" + this.mTxPowerLevel + ", mDeviceName=" + this.mDeviceName + ", mTDSData=" + BluetoothLeUtils.toString(this.mTDSData) + "]";
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos, uuidLength);
            serviceUuids.add(BluetoothUuid.parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static int parseServiceSolicitationUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceSolicitationUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos, uuidLength);
            serviceSolicitationUuids.add(BluetoothUuid.parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }
}
