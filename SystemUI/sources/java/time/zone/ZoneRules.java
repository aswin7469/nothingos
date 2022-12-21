package java.time.zone;

import com.android.settingslib.datetime.ZoneGetter;
import com.android.settingslib.utils.StringUtil;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ZoneRules implements Serializable {
    private static final ZoneOffsetTransitionRule[] EMPTY_LASTRULES = new ZoneOffsetTransitionRule[0];
    private static final LocalDateTime[] EMPTY_LDT_ARRAY = new LocalDateTime[0];
    private static final long[] EMPTY_LONG_ARRAY = new long[0];
    private static final int LAST_CACHED_YEAR = 2100;
    private static final long serialVersionUID = 3044319355680032515L;
    private final ZoneOffsetTransitionRule[] lastRules;
    private final transient ConcurrentMap<Integer, ZoneOffsetTransition[]> lastRulesCache = new ConcurrentHashMap();
    private final long[] savingsInstantTransitions;
    private final LocalDateTime[] savingsLocalTransitions;
    private final ZoneOffset[] standardOffsets;
    private final long[] standardTransitions;
    private final ZoneOffset[] wallOffsets;

    /* renamed from: of */
    public static ZoneRules m963of(ZoneOffset zoneOffset, ZoneOffset zoneOffset2, List<ZoneOffsetTransition> list, List<ZoneOffsetTransition> list2, List<ZoneOffsetTransitionRule> list3) {
        Objects.requireNonNull(zoneOffset, "baseStandardOffset");
        Objects.requireNonNull(zoneOffset2, "baseWallOffset");
        Objects.requireNonNull(list, "standardOffsetTransitionList");
        Objects.requireNonNull(list2, "transitionList");
        Objects.requireNonNull(list3, "lastRules");
        return new ZoneRules(zoneOffset, zoneOffset2, list, list2, list3);
    }

    /* renamed from: of */
    public static ZoneRules m962of(ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        return new ZoneRules(zoneOffset);
    }

    ZoneRules(ZoneOffset zoneOffset, ZoneOffset zoneOffset2, List<ZoneOffsetTransition> list, List<ZoneOffsetTransition> list2, List<ZoneOffsetTransitionRule> list3) {
        this.standardTransitions = new long[list.size()];
        ZoneOffset[] zoneOffsetArr = new ZoneOffset[(list.size() + 1)];
        this.standardOffsets = zoneOffsetArr;
        zoneOffsetArr[0] = zoneOffset;
        int i = 0;
        while (i < list.size()) {
            this.standardTransitions[i] = list.get(i).toEpochSecond();
            int i2 = i + 1;
            this.standardOffsets[i2] = list.get(i).getOffsetAfter();
            i = i2;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(zoneOffset2);
        for (ZoneOffsetTransition next : list2) {
            if (next.isGap()) {
                arrayList.add(next.getDateTimeBefore());
                arrayList.add(next.getDateTimeAfter());
            } else {
                arrayList.add(next.getDateTimeAfter());
                arrayList.add(next.getDateTimeBefore());
            }
            arrayList2.add(next.getOffsetAfter());
        }
        this.savingsLocalTransitions = (LocalDateTime[]) arrayList.toArray(new LocalDateTime[arrayList.size()]);
        this.wallOffsets = (ZoneOffset[]) arrayList2.toArray(new ZoneOffset[arrayList2.size()]);
        this.savingsInstantTransitions = new long[list2.size()];
        for (int i3 = 0; i3 < list2.size(); i3++) {
            this.savingsInstantTransitions[i3] = list2.get(i3).toEpochSecond();
        }
        if (list3.size() <= 16) {
            this.lastRules = (ZoneOffsetTransitionRule[]) list3.toArray(new ZoneOffsetTransitionRule[list3.size()]);
            return;
        }
        throw new IllegalArgumentException("Too many transition rules");
    }

    private ZoneRules(long[] jArr, ZoneOffset[] zoneOffsetArr, long[] jArr2, ZoneOffset[] zoneOffsetArr2, ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr) {
        this.standardTransitions = jArr;
        this.standardOffsets = zoneOffsetArr;
        this.savingsInstantTransitions = jArr2;
        this.wallOffsets = zoneOffsetArr2;
        this.lastRules = zoneOffsetTransitionRuleArr;
        if (jArr2.length == 0) {
            this.savingsLocalTransitions = EMPTY_LDT_ARRAY;
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < jArr2.length) {
            int i2 = i + 1;
            ZoneOffsetTransition zoneOffsetTransition = new ZoneOffsetTransition(jArr2[i], zoneOffsetArr2[i], zoneOffsetArr2[i2]);
            if (zoneOffsetTransition.isGap()) {
                arrayList.add(zoneOffsetTransition.getDateTimeBefore());
                arrayList.add(zoneOffsetTransition.getDateTimeAfter());
            } else {
                arrayList.add(zoneOffsetTransition.getDateTimeAfter());
                arrayList.add(zoneOffsetTransition.getDateTimeBefore());
            }
            i = i2;
        }
        this.savingsLocalTransitions = (LocalDateTime[]) arrayList.toArray(new LocalDateTime[arrayList.size()]);
    }

    private ZoneRules(ZoneOffset zoneOffset) {
        ZoneOffset[] zoneOffsetArr = new ZoneOffset[1];
        this.standardOffsets = zoneOffsetArr;
        zoneOffsetArr[0] = zoneOffset;
        long[] jArr = EMPTY_LONG_ARRAY;
        this.standardTransitions = jArr;
        this.savingsInstantTransitions = jArr;
        this.savingsLocalTransitions = EMPTY_LDT_ARRAY;
        this.wallOffsets = zoneOffsetArr;
        this.lastRules = EMPTY_LASTRULES;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.standardTransitions.length);
        for (long writeEpochSec : this.standardTransitions) {
            Ser.writeEpochSec(writeEpochSec, dataOutput);
        }
        for (ZoneOffset writeOffset : this.standardOffsets) {
            Ser.writeOffset(writeOffset, dataOutput);
        }
        dataOutput.writeInt(this.savingsInstantTransitions.length);
        for (long writeEpochSec2 : this.savingsInstantTransitions) {
            Ser.writeEpochSec(writeEpochSec2, dataOutput);
        }
        for (ZoneOffset writeOffset2 : this.wallOffsets) {
            Ser.writeOffset(writeOffset2, dataOutput);
        }
        dataOutput.writeByte(this.lastRules.length);
        for (ZoneOffsetTransitionRule writeExternal : this.lastRules) {
            writeExternal.writeExternal(dataOutput);
        }
    }

    static ZoneRules readExternal(DataInput dataInput) throws IOException, ClassNotFoundException {
        long[] jArr;
        long[] jArr2;
        int readInt = dataInput.readInt();
        if (readInt <= 1024) {
            if (readInt == 0) {
                jArr = EMPTY_LONG_ARRAY;
            } else {
                jArr = new long[readInt];
            }
            long[] jArr3 = jArr;
            for (int i = 0; i < readInt; i++) {
                jArr3[i] = Ser.readEpochSec(dataInput);
            }
            int i2 = readInt + 1;
            ZoneOffset[] zoneOffsetArr = new ZoneOffset[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                zoneOffsetArr[i3] = Ser.readOffset(dataInput);
            }
            int readInt2 = dataInput.readInt();
            if (readInt2 <= 1024) {
                if (readInt2 == 0) {
                    jArr2 = EMPTY_LONG_ARRAY;
                } else {
                    jArr2 = new long[readInt2];
                }
                long[] jArr4 = jArr2;
                for (int i4 = 0; i4 < readInt2; i4++) {
                    jArr4[i4] = Ser.readEpochSec(dataInput);
                }
                int i5 = readInt2 + 1;
                ZoneOffset[] zoneOffsetArr2 = new ZoneOffset[i5];
                for (int i6 = 0; i6 < i5; i6++) {
                    zoneOffsetArr2[i6] = Ser.readOffset(dataInput);
                }
                int readByte = dataInput.readByte();
                if (readByte <= 16) {
                    ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr = readByte == 0 ? EMPTY_LASTRULES : new ZoneOffsetTransitionRule[readByte];
                    for (int i7 = 0; i7 < readByte; i7++) {
                        zoneOffsetTransitionRuleArr[i7] = ZoneOffsetTransitionRule.readExternal(dataInput);
                    }
                    return new ZoneRules(jArr3, zoneOffsetArr, jArr4, zoneOffsetArr2, zoneOffsetTransitionRuleArr);
                }
                throw new InvalidObjectException("Too many transition rules");
            }
            throw new InvalidObjectException("Too many saving offsets");
        }
        throw new InvalidObjectException("Too many transitions");
    }

    public boolean isFixedOffset() {
        return this.savingsInstantTransitions.length == 0;
    }

    public ZoneOffset getOffset(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        long epochSecond = instant.getEpochSecond();
        if (this.lastRules.length > 0) {
            long[] jArr = this.savingsInstantTransitions;
            if (epochSecond > jArr[jArr.length - 1]) {
                ZoneOffset[] zoneOffsetArr = this.wallOffsets;
                ZoneOffsetTransition[] findTransitionArray = findTransitionArray(findYear(epochSecond, zoneOffsetArr[zoneOffsetArr.length - 1]));
                ZoneOffsetTransition zoneOffsetTransition = null;
                for (int i = 0; i < findTransitionArray.length; i++) {
                    zoneOffsetTransition = findTransitionArray[i];
                    if (epochSecond < zoneOffsetTransition.toEpochSecond()) {
                        return zoneOffsetTransition.getOffsetBefore();
                    }
                }
                return zoneOffsetTransition.getOffsetAfter();
            }
        }
        int binarySearch = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 2;
        }
        return this.wallOffsets[binarySearch + 1];
    }

    public ZoneOffset getOffset(LocalDateTime localDateTime) {
        Object offsetInfo = getOffsetInfo(localDateTime);
        if (offsetInfo instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition) offsetInfo).getOffsetBefore();
        }
        return (ZoneOffset) offsetInfo;
    }

    public List<ZoneOffset> getValidOffsets(LocalDateTime localDateTime) {
        Object offsetInfo = getOffsetInfo(localDateTime);
        if (offsetInfo instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition) offsetInfo).getValidOffsets();
        }
        return Collections.singletonList((ZoneOffset) offsetInfo);
    }

    public ZoneOffsetTransition getTransition(LocalDateTime localDateTime) {
        Object offsetInfo = getOffsetInfo(localDateTime);
        if (offsetInfo instanceof ZoneOffsetTransition) {
            return (ZoneOffsetTransition) offsetInfo;
        }
        return null;
    }

    private Object getOffsetInfo(LocalDateTime localDateTime) {
        int i = 0;
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        if (this.lastRules.length > 0) {
            LocalDateTime[] localDateTimeArr = this.savingsLocalTransitions;
            if (localDateTime.isAfter(localDateTimeArr[localDateTimeArr.length - 1])) {
                ZoneOffsetTransition[] findTransitionArray = findTransitionArray(localDateTime.getYear());
                int length = findTransitionArray.length;
                Object obj = null;
                while (i < length) {
                    ZoneOffsetTransition zoneOffsetTransition = findTransitionArray[i];
                    Object findOffsetInfo = findOffsetInfo(localDateTime, zoneOffsetTransition);
                    if ((findOffsetInfo instanceof ZoneOffsetTransition) || findOffsetInfo.equals(zoneOffsetTransition.getOffsetBefore())) {
                        return findOffsetInfo;
                    }
                    i++;
                    obj = findOffsetInfo;
                }
                return obj;
            }
        }
        int binarySearch = Arrays.binarySearch((Object[]) this.savingsLocalTransitions, (Object) localDateTime);
        if (binarySearch == -1) {
            return this.wallOffsets[0];
        }
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 2;
        } else {
            LocalDateTime[] localDateTimeArr2 = this.savingsLocalTransitions;
            if (binarySearch < localDateTimeArr2.length - 1) {
                int i2 = binarySearch + 1;
                if (localDateTimeArr2[binarySearch].equals(localDateTimeArr2[i2])) {
                    binarySearch = i2;
                }
            }
        }
        if ((binarySearch & 1) != 0) {
            return this.wallOffsets[(binarySearch / 2) + 1];
        }
        LocalDateTime[] localDateTimeArr3 = this.savingsLocalTransitions;
        LocalDateTime localDateTime2 = localDateTimeArr3[binarySearch];
        LocalDateTime localDateTime3 = localDateTimeArr3[binarySearch + 1];
        ZoneOffset[] zoneOffsetArr = this.wallOffsets;
        int i3 = binarySearch / 2;
        ZoneOffset zoneOffset = zoneOffsetArr[i3];
        ZoneOffset zoneOffset2 = zoneOffsetArr[i3 + 1];
        if (zoneOffset2.getTotalSeconds() > zoneOffset.getTotalSeconds()) {
            return new ZoneOffsetTransition(localDateTime2, zoneOffset, zoneOffset2);
        }
        return new ZoneOffsetTransition(localDateTime3, zoneOffset, zoneOffset2);
    }

    private Object findOffsetInfo(LocalDateTime localDateTime, ZoneOffsetTransition zoneOffsetTransition) {
        LocalDateTime dateTimeBefore = zoneOffsetTransition.getDateTimeBefore();
        if (zoneOffsetTransition.isGap()) {
            if (localDateTime.isBefore(dateTimeBefore)) {
                return zoneOffsetTransition.getOffsetBefore();
            }
            if (localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter())) {
                return zoneOffsetTransition;
            }
            return zoneOffsetTransition.getOffsetAfter();
        } else if (!localDateTime.isBefore(dateTimeBefore)) {
            return zoneOffsetTransition.getOffsetAfter();
        } else {
            return localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter()) ? zoneOffsetTransition.getOffsetBefore() : zoneOffsetTransition;
        }
    }

    private ZoneOffsetTransition[] findTransitionArray(int i) {
        Integer valueOf = Integer.valueOf(i);
        ZoneOffsetTransition[] zoneOffsetTransitionArr = this.lastRulesCache.get(valueOf);
        if (zoneOffsetTransitionArr != null) {
            return zoneOffsetTransitionArr;
        }
        ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr = this.lastRules;
        ZoneOffsetTransition[] zoneOffsetTransitionArr2 = new ZoneOffsetTransition[zoneOffsetTransitionRuleArr.length];
        for (int i2 = 0; i2 < zoneOffsetTransitionRuleArr.length; i2++) {
            zoneOffsetTransitionArr2[i2] = zoneOffsetTransitionRuleArr[i2].createTransition(i);
        }
        if (i < 2100) {
            this.lastRulesCache.putIfAbsent(valueOf, zoneOffsetTransitionArr2);
        }
        return zoneOffsetTransitionArr2;
    }

    public ZoneOffset getStandardOffset(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        int binarySearch = Arrays.binarySearch(this.standardTransitions, instant.getEpochSecond());
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 2;
        }
        return this.standardOffsets[binarySearch + 1];
    }

    public Duration getDaylightSavings(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return Duration.ZERO;
        }
        return Duration.ofSeconds((long) (getOffset(instant).getTotalSeconds() - getStandardOffset(instant).getTotalSeconds()));
    }

    public boolean isDaylightSavings(Instant instant) {
        return !getStandardOffset(instant).equals(getOffset(instant));
    }

    public boolean isValidOffset(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return getValidOffsets(localDateTime).contains(zoneOffset);
    }

    public ZoneOffsetTransition nextTransition(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        long epochSecond = instant.getEpochSecond();
        long[] jArr = this.savingsInstantTransitions;
        if (epochSecond < jArr[jArr.length - 1]) {
            int binarySearch = Arrays.binarySearch(jArr, epochSecond);
            int i = binarySearch < 0 ? (-binarySearch) - 1 : binarySearch + 1;
            long j = this.savingsInstantTransitions[i];
            ZoneOffset[] zoneOffsetArr = this.wallOffsets;
            return new ZoneOffsetTransition(j, zoneOffsetArr[i], zoneOffsetArr[i + 1]);
        } else if (this.lastRules.length == 0) {
            return null;
        } else {
            ZoneOffset[] zoneOffsetArr2 = this.wallOffsets;
            int findYear = findYear(epochSecond, zoneOffsetArr2[zoneOffsetArr2.length - 1]);
            for (ZoneOffsetTransition zoneOffsetTransition : findTransitionArray(findYear)) {
                if (epochSecond < zoneOffsetTransition.toEpochSecond()) {
                    return zoneOffsetTransition;
                }
            }
            if (findYear < 999999999) {
                return findTransitionArray(findYear + 1)[0];
            }
            return null;
        }
    }

    public ZoneOffsetTransition previousTransition(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        long epochSecond = instant.getEpochSecond();
        if (instant.getNano() > 0 && epochSecond < Long.MAX_VALUE) {
            epochSecond++;
        }
        long[] jArr = this.savingsInstantTransitions;
        long j = jArr[jArr.length - 1];
        if (this.lastRules.length > 0 && epochSecond > j) {
            ZoneOffset[] zoneOffsetArr = this.wallOffsets;
            ZoneOffset zoneOffset = zoneOffsetArr[zoneOffsetArr.length - 1];
            int findYear = findYear(epochSecond, zoneOffset);
            ZoneOffsetTransition[] findTransitionArray = findTransitionArray(findYear);
            for (int length = findTransitionArray.length - 1; length >= 0; length--) {
                if (epochSecond > findTransitionArray[length].toEpochSecond()) {
                    return findTransitionArray[length];
                }
            }
            int i = findYear - 1;
            if (i > findYear(j, zoneOffset)) {
                ZoneOffsetTransition[] findTransitionArray2 = findTransitionArray(i);
                return findTransitionArray2[findTransitionArray2.length - 1];
            }
        }
        int binarySearch = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        if (binarySearch <= 0) {
            return null;
        }
        int i2 = binarySearch - 1;
        long j2 = this.savingsInstantTransitions[i2];
        ZoneOffset[] zoneOffsetArr2 = this.wallOffsets;
        return new ZoneOffsetTransition(j2, zoneOffsetArr2[i2], zoneOffsetArr2[binarySearch]);
    }

    private int findYear(long j, ZoneOffset zoneOffset) {
        return LocalDate.ofEpochDay(Math.floorDiv(j + ((long) zoneOffset.getTotalSeconds()), (int) StringUtil.SECONDS_PER_DAY)).getYear();
    }

    public List<ZoneOffsetTransition> getTransitions() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < this.savingsInstantTransitions.length) {
            long j = this.savingsInstantTransitions[i];
            ZoneOffset[] zoneOffsetArr = this.wallOffsets;
            ZoneOffset zoneOffset = zoneOffsetArr[i];
            i++;
            arrayList.add(new ZoneOffsetTransition(j, zoneOffset, zoneOffsetArr[i]));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<ZoneOffsetTransitionRule> getTransitionRules() {
        return List.m1733of((E[]) this.lastRules);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneRules)) {
            return false;
        }
        ZoneRules zoneRules = (ZoneRules) obj;
        if (!Arrays.equals(this.standardTransitions, zoneRules.standardTransitions) || !Arrays.equals((Object[]) this.standardOffsets, (Object[]) zoneRules.standardOffsets) || !Arrays.equals(this.savingsInstantTransitions, zoneRules.savingsInstantTransitions) || !Arrays.equals((Object[]) this.wallOffsets, (Object[]) zoneRules.wallOffsets) || !Arrays.equals((Object[]) this.lastRules, (Object[]) zoneRules.lastRules)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Arrays.hashCode((Object[]) this.lastRules) ^ (((Arrays.hashCode(this.standardTransitions) ^ Arrays.hashCode((Object[]) this.standardOffsets)) ^ Arrays.hashCode(this.savingsInstantTransitions)) ^ Arrays.hashCode((Object[]) this.wallOffsets));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ZoneRules[currentStandardOffset=");
        ZoneOffset[] zoneOffsetArr = this.standardOffsets;
        sb.append((Object) zoneOffsetArr[zoneOffsetArr.length - 1]);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
