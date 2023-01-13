package com.nothing.p023os.device;

import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.CharsKt;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u000e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0011J\u0014\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0016J\u0012\u0010\u001a\u001a\u00020\u00182\b\u0010\u001b\u001a\u0004\u0018\u00010\u0011H\u0002J\u0010\u0010\u001c\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u0016J$\u0010\u001e\u001a\u0016\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u001f2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0016J\u0010\u0010!\u001a\u00020\u000e2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eXT¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/nothing/os/device/DeviceParseUtil;", "", "()V", "AIRPODS_DATA_LENGTH", "", "AIRPODS_MANUFACTURER", "BYTE_LEN", "HEX_2", "HEX_C", "HEX_FF", "HEX_FORMAT", "MANUFACTURER_ID", "MIN_RSSI", "SERVER_UUID", "", "byteArrayToString", "bytes", "", "byteToHexString", "bArr", "decodeResult", "result", "Landroid/bluetooth/le/ScanResult;", "isAirpodsData", "", "scanResult", "isDataValid", "data", "isNothingData", "record", "parseAirpodsData", "Lkotlin/Triple;", "Landroid/bluetooth/BluetoothDevice;", "parseNothingMac", "osConnect_SnapshotRelease"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: com.nothing.os.device.DeviceParseUtil */
/* compiled from: DeviceParseUtil.kt */
public final class DeviceParseUtil {
    public static final int AIRPODS_DATA_LENGTH = 27;
    public static final int AIRPODS_MANUFACTURER = 76;
    public static final int BYTE_LEN = 30;
    private static final int HEX_2 = 2;
    private static final int HEX_C = 12;
    private static final int HEX_FF = 255;
    private static final int HEX_FORMAT = 16;
    public static final DeviceParseUtil INSTANCE = new DeviceParseUtil();
    private static final int MANUFACTURER_ID = 65535;
    public static final int MIN_RSSI = -66;
    private static final String SERVER_UUID = "0000fe2c-0000-1000-8000-00805f9b34fb";

    private DeviceParseUtil() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        r0 = (r0 = r3.getScanRecord()).getServiceData();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isNothingData(android.bluetooth.le.ScanResult r3) {
        /*
            r2 = this;
            r2 = 0
            if (r3 != 0) goto L_0x0005
        L_0x0003:
            r0 = r2
            goto L_0x001f
        L_0x0005:
            android.bluetooth.le.ScanRecord r0 = r3.getScanRecord()
            if (r0 != 0) goto L_0x000c
            goto L_0x0003
        L_0x000c:
            java.util.Map r0 = r0.getServiceData()
            if (r0 != 0) goto L_0x0013
            goto L_0x0003
        L_0x0013:
            java.lang.String r1 = "0000fe2c-0000-1000-8000-00805f9b34fb"
            android.os.ParcelUuid r1 = android.os.ParcelUuid.fromString(r1)
            java.lang.Object r0 = r0.get(r1)
            byte[] r0 = (byte[]) r0
        L_0x001f:
            r1 = 0
            if (r0 != 0) goto L_0x0023
            return r1
        L_0x0023:
            android.bluetooth.le.ScanRecord r3 = r3.getScanRecord()
            if (r3 != 0) goto L_0x002a
            goto L_0x0031
        L_0x002a:
            r2 = 65535(0xffff, float:9.1834E-41)
            byte[] r2 = r3.getManufacturerSpecificData(r2)
        L_0x0031:
            if (r2 != 0) goto L_0x0034
            return r1
        L_0x0034:
            r2 = 1
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.p023os.device.DeviceParseUtil.isNothingData(android.bluetooth.le.ScanResult):boolean");
    }

    private final boolean isDataValid(byte[] bArr) {
        return bArr != null && bArr.length == 27;
    }

    private final String decodeResult(ScanResult scanResult) {
        if (scanResult == null || scanResult.getScanRecord() == null) {
            return null;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        Intrinsics.checkNotNull(scanRecord);
        byte[] manufacturerSpecificData = scanRecord.getManufacturerSpecificData(76);
        if (manufacturerSpecificData == null || !isDataValid(manufacturerSpecificData)) {
            return null;
        }
        return byteToHexString(manufacturerSpecificData);
    }

    public final String byteToHexString(byte[] bArr) {
        Intrinsics.checkNotNullParameter(bArr, "bArr");
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            byte b = bArr[i];
            i++;
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format("%02X", Arrays.copyOf((T[]) new Object[]{Byte.valueOf(b)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            sb.append(format);
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "ret.toString()");
        return sb2;
    }

    public final boolean isAirpodsData(ScanResult scanResult) {
        return parseAirpodsData(scanResult) != null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0005, code lost:
        r1 = r4.getScanRecord();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlin.Triple<android.bluetooth.BluetoothDevice, java.lang.String, java.lang.Integer> parseAirpodsData(android.bluetooth.le.ScanResult r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0005
        L_0x0003:
            r1 = r0
            goto L_0x0010
        L_0x0005:
            android.bluetooth.le.ScanRecord r1 = r4.getScanRecord()
            if (r1 != 0) goto L_0x000c
            goto L_0x0003
        L_0x000c:
            byte[] r1 = r1.getBytes()
        L_0x0010:
            if (r1 != 0) goto L_0x0013
            return r0
        L_0x0013:
            java.lang.String r3 = r3.decodeResult(r4)
            if (r3 != 0) goto L_0x001a
            return r0
        L_0x001a:
            int r1 = r1.length
            r2 = 30
            if (r1 > r2) goto L_0x0020
            return r0
        L_0x0020:
            int r1 = r4.getRssi()
            r2 = -66
            if (r1 > r2) goto L_0x0029
            return r0
        L_0x0029:
            android.bluetooth.BluetoothDevice r4 = r4.getDevice()
            if (r4 != 0) goto L_0x0030
            return r0
        L_0x0030:
            kotlin.Triple r0 = new kotlin.Triple
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
            r0.<init>(r4, r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.p023os.device.DeviceParseUtil.parseAirpodsData(android.bluetooth.le.ScanResult):kotlin.Triple");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        r0 = (r0 = r4.getScanRecord()).getServiceData();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String parseNothingMac(android.bluetooth.le.ScanResult r4) {
        /*
            r3 = this;
            r3 = 0
            if (r4 != 0) goto L_0x0005
        L_0x0003:
            r0 = r3
            goto L_0x001f
        L_0x0005:
            android.bluetooth.le.ScanRecord r0 = r4.getScanRecord()
            if (r0 != 0) goto L_0x000c
            goto L_0x0003
        L_0x000c:
            java.util.Map r0 = r0.getServiceData()
            if (r0 != 0) goto L_0x0013
            goto L_0x0003
        L_0x0013:
            java.lang.String r1 = "0000fe2c-0000-1000-8000-00805f9b34fb"
            android.os.ParcelUuid r1 = android.os.ParcelUuid.fromString(r1)
            java.lang.Object r0 = r0.get(r1)
            byte[] r0 = (byte[]) r0
        L_0x001f:
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0024
            return r1
        L_0x0024:
            android.bluetooth.le.ScanRecord r4 = r4.getScanRecord()
            if (r4 != 0) goto L_0x002c
            r4 = r3
            goto L_0x0033
        L_0x002c:
            r0 = 65535(0xffff, float:9.1834E-41)
            byte[] r4 = r4.getManufacturerSpecificData(r0)
        L_0x0033:
            if (r4 != 0) goto L_0x0036
            goto L_0x003c
        L_0x0036:
            com.nothing.os.device.DeviceParseUtil r3 = INSTANCE
            java.lang.String r3 = r3.byteArrayToString(r4)
        L_0x003c:
            r4 = 0
            if (r3 != 0) goto L_0x0041
            r0 = r4
            goto L_0x0045
        L_0x0041:
            int r0 = r3.length()
        L_0x0045:
            r2 = 12
            if (r0 < r2) goto L_0x006b
            if (r3 != 0) goto L_0x004c
            goto L_0x0050
        L_0x004c:
            int r4 = r3.length()
        L_0x0050:
            if (r4 < r2) goto L_0x006b
            if (r3 != 0) goto L_0x0055
            goto L_0x006b
        L_0x0055:
            int r4 = r3.length()
            int r4 = r4 - r2
            int r0 = r3.length()
            java.lang.String r3 = r3.substring(r4, r0)
            java.lang.String r4 = "(this as java.lang.Strin…ing(startIndex, endIndex)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r4)
            if (r3 != 0) goto L_0x006a
            goto L_0x006b
        L_0x006a:
            r1 = r3
        L_0x006b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.p023os.device.DeviceParseUtil.parseNothingMac(android.bluetooth.le.ScanResult):java.lang.String");
    }

    private final String byteArrayToString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String num = Integer.toString(b & 255, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(num, "java.lang.Integer.toStri…(this, checkRadix(radix))");
            if (num.length() < 2) {
                num = Intrinsics.stringPlus("0", num);
            }
            sb.append(num);
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }
}
