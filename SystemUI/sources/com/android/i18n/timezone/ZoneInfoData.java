package com.android.i18n.timezone;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;

public final class ZoneInfoData {
    public static final ObjectStreamField[] ZONEINFO_SERIALIZED_FIELDS = new ObjectStreamField[0];

    private ZoneInfoData() {
        throw new RuntimeException("Stub!");
    }

    public static ZoneInfoData createFromSerializationFields(String str, ObjectInputStream.GetField getField) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void writeToSerializationFields(ObjectOutputStream.PutField putField) {
        throw new RuntimeException("Stub!");
    }

    public int getOffsetsByUtcTime(long j, int[] iArr) {
        throw new RuntimeException("Stub!");
    }

    public int getOffset(long j) {
        throw new RuntimeException("Stub!");
    }

    public boolean isInDaylightTime(long j) {
        throw new RuntimeException("Stub!");
    }

    public int getRawOffset() {
        throw new RuntimeException("Stub!");
    }

    public Integer getLatestDstSavingsMillis(long j) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasSameRules(ZoneInfoData zoneInfoData) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public String getID() {
        throw new RuntimeException("Stub!");
    }

    public ZoneInfoData createCopyWithRawOffset(int i) {
        throw new RuntimeException("Stub!");
    }

    public long[] getTransitions() {
        throw new RuntimeException("Stub!");
    }

    public static ZoneInfoData createInstance(String str, long[] jArr, byte[] bArr, int[] iArr, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }
}
