package libcore.util;

import android.icu.lang.UCharacter;
import com.android.i18n.timezone.ZoneInfoData;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Field;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public final class ZoneInfo extends TimeZone {
    private static final int[] LEAP = {0, 31, 60, 91, 121, 152, 182, 213, 244, UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F_ID, 305, 335};
    private static final long MILLISECONDS_PER_400_YEARS = 12622780800000L;
    private static final long MILLISECONDS_PER_DAY = 86400000;
    private static final int[] NORMAL = {0, 31, 59, 90, 120, 151, 181, 212, 243, UCharacter.UnicodeBlock.TANGUT_COMPONENTS_ID, 304, 334};
    private static final long UNIX_OFFSET = 62167219200000L;
    private static final ObjectStreamField[] serialPersistentFields;
    static final long serialVersionUID = -4598738130123921552L;
    private transient ZoneInfoData mDelegate;
    private final int mDstSavings;
    private final long[] mTransitions;
    private final boolean mUseDst;

    private interface FieldSetter {
        void set(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    static {
        int length = ZoneInfoData.ZONEINFO_SERIALIZED_FIELDS.length;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[(length + 2)];
        serialPersistentFields = objectStreamFieldArr;
        objectStreamFieldArr[0] = new ObjectStreamField("mDstSavings", Integer.TYPE);
        objectStreamFieldArr[1] = new ObjectStreamField("mUseDst", Boolean.TYPE);
        System.arraycopy((Object) ZoneInfoData.ZONEINFO_SERIALIZED_FIELDS, 0, (Object) objectStreamFieldArr, 2, length);
    }

    public static ZoneInfo createZoneInfo(ZoneInfoData zoneInfoData) {
        return createZoneInfo(zoneInfoData, System.currentTimeMillis());
    }

    public static ZoneInfo createZoneInfo(ZoneInfoData zoneInfoData, long j) {
        Integer latestDstSavingsMillis = zoneInfoData.getLatestDstSavingsMillis(j);
        int i = 0;
        boolean z = latestDstSavingsMillis != null;
        if (latestDstSavingsMillis != null) {
            i = latestDstSavingsMillis.intValue();
        }
        return new ZoneInfo(zoneInfoData, i, z);
    }

    private ZoneInfo(ZoneInfoData zoneInfoData, int i, boolean z) {
        this.mDelegate = zoneInfoData;
        this.mDstSavings = i;
        this.mUseDst = z;
        this.mTransitions = zoneInfoData.getTransitions();
        setID(zoneInfoData.getID());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.mDelegate = ZoneInfoData.createFromSerializationFields(getID(), readFields);
        int i = 0;
        boolean z = readFields.get("mUseDst", false);
        int i2 = readFields.get("mDstSavings", 0);
        long[] transitions = this.mDelegate.getTransitions();
        if (z || i2 == 0) {
            i = i2;
        }
        setFinalField("mDstSavings", new ZoneInfo$$ExternalSyntheticLambda0(this, i));
        setFinalField("mUseDst", new ZoneInfo$$ExternalSyntheticLambda1(this, z));
        setFinalField("mTransitions", new ZoneInfo$$ExternalSyntheticLambda2(this, transitions));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$readObject$0$libcore-util-ZoneInfo  reason: not valid java name */
    public /* synthetic */ void m5500lambda$readObject$0$libcoreutilZoneInfo(int i, Field field) throws IllegalArgumentException, IllegalAccessException {
        field.setInt(this, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$readObject$1$libcore-util-ZoneInfo  reason: not valid java name */
    public /* synthetic */ void m5501lambda$readObject$1$libcoreutilZoneInfo(boolean z, Field field) throws IllegalArgumentException, IllegalAccessException {
        field.setBoolean(this, z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$readObject$2$libcore-util-ZoneInfo  reason: not valid java name */
    public /* synthetic */ void m5502lambda$readObject$2$libcoreutilZoneInfo(long[] jArr, Field field) throws IllegalArgumentException, IllegalAccessException {
        field.set(this, jArr);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("mUseDst", this.mUseDst);
        putFields.put("mDstSavings", this.mDstSavings);
        this.mDelegate.writeToSerializationFields(putFields);
        objectOutputStream.writeFields();
    }

    private static void setFinalField(String str, FieldSetter fieldSetter) {
        try {
            Field declaredField = ZoneInfo.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            fieldSetter.set(declaredField);
        } catch (ReflectiveOperationException unused) {
        }
    }

    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i2 % 400;
        long j = (((long) (i2 / 400)) * MILLISECONDS_PER_400_YEARS) + (((long) i7) * 31536000000L) + (((long) ((i7 + 3) / 4)) * MILLISECONDS_PER_DAY);
        if (i7 > 0) {
            j -= ((long) ((i7 - 1) / 100)) * MILLISECONDS_PER_DAY;
        }
        return this.mDelegate.getOffset(((((j + (((long) (i7 == 0 || (i7 % 4 == 0 && i7 % 100 != 0) ? LEAP : NORMAL)[i3]) * MILLISECONDS_PER_DAY)) + (((long) (i4 - 1)) * MILLISECONDS_PER_DAY)) + ((long) i6)) - ((long) this.mDelegate.getRawOffset())) - UNIX_OFFSET);
    }

    public int getOffset(long j) {
        return this.mDelegate.getOffset(j);
    }

    public boolean inDaylightTime(Date date) {
        return this.mDelegate.isInDaylightTime(date.getTime());
    }

    public int getRawOffset() {
        return this.mDelegate.getRawOffset();
    }

    public void setRawOffset(int i) {
        this.mDelegate = this.mDelegate.createCopyWithRawOffset(i);
    }

    public int getDSTSavings() {
        return this.mDstSavings;
    }

    public boolean useDaylightTime() {
        return this.mUseDst;
    }

    public boolean hasSameRules(TimeZone timeZone) {
        if (!(timeZone instanceof ZoneInfo)) {
            return false;
        }
        ZoneInfo zoneInfo = (ZoneInfo) timeZone;
        boolean z = this.mUseDst;
        if (z != zoneInfo.mUseDst) {
            return false;
        }
        if (z) {
            return this.mDelegate.hasSameRules(zoneInfo.mDelegate);
        }
        if (this.mDelegate.getRawOffset() == zoneInfo.getRawOffset()) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ZoneInfo)) {
            return false;
        }
        ZoneInfo zoneInfo = (ZoneInfo) obj;
        if (!getID().equals(zoneInfo.getID()) || !hasSameRules(zoneInfo)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mUseDst), this.mDelegate);
    }

    public String toString() {
        return getClass().getName() + "[mDstSavings=" + this.mDstSavings + ",mUseDst=" + this.mUseDst + ",mDelegate=" + this.mDelegate.toString() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public Object clone() {
        return new ZoneInfo(this.mDelegate, this.mDstSavings, this.mUseDst);
    }

    public int getOffsetsByUtcTime(long j, int[] iArr) {
        return this.mDelegate.getOffsetsByUtcTime(j, iArr);
    }
}
