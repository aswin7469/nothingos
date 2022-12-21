package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.Range;
import java.util.Collection;
import java.util.Set;
import sun.util.locale.LanguageTag;

public final class UidRange implements Parcelable {
    public static final Parcelable.Creator<UidRange> CREATOR = new Parcelable.Creator<UidRange>() {
        public UidRange createFromParcel(Parcel parcel) {
            return new UidRange(parcel.readInt(), parcel.readInt());
        }

        public UidRange[] newArray(int i) {
            return new UidRange[i];
        }
    };
    public final int start;
    public final int stop;

    public int describeContents() {
        return 0;
    }

    public UidRange(int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("Invalid start UID.");
        } else if (i2 < 0) {
            throw new IllegalArgumentException("Invalid stop UID.");
        } else if (i <= i2) {
            this.start = i;
            this.stop = i2;
        } else {
            throw new IllegalArgumentException("Invalid UID range.");
        }
    }

    public static UidRange createForUser(UserHandle userHandle) {
        return new UidRange(userHandle.getUid(0), UserHandle.of(userHandle.getIdentifier() + 1).getUid(0) - 1);
    }

    public int getStartUser() {
        return UserHandle.getUserHandleForUid(this.start).getIdentifier();
    }

    public int getEndUser() {
        return UserHandle.getUserHandleForUid(this.stop).getIdentifier();
    }

    public boolean contains(int i) {
        return this.start <= i && i <= this.stop;
    }

    public int count() {
        return (this.stop + 1) - this.start;
    }

    public boolean containsRange(UidRange uidRange) {
        return this.start <= uidRange.start && uidRange.stop <= this.stop;
    }

    public int hashCode() {
        return ((527 + this.start) * 31) + this.stop;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UidRange)) {
            return false;
        }
        UidRange uidRange = (UidRange) obj;
        if (this.start == uidRange.start && this.stop == uidRange.stop) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.start + LanguageTag.SEP + this.stop;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.start);
        parcel.writeInt(this.stop);
    }

    public static boolean containsUid(Collection<UidRange> collection, int i) {
        if (collection == null) {
            return false;
        }
        for (UidRange contains : collection) {
            if (contains.contains(i)) {
                return true;
            }
        }
        return false;
    }

    public static ArraySet<UidRange> fromIntRanges(Set<Range<Integer>> set) {
        if (set == null) {
            return null;
        }
        ArraySet<UidRange> arraySet = new ArraySet<>();
        for (Range next : set) {
            arraySet.add(new UidRange(((Integer) next.getLower()).intValue(), ((Integer) next.getUpper()).intValue()));
        }
        return arraySet;
    }

    public static ArraySet<Range<Integer>> toIntRanges(Set<UidRange> set) {
        if (set == null) {
            return null;
        }
        ArraySet<Range<Integer>> arraySet = new ArraySet<>();
        for (UidRange next : set) {
            arraySet.add(new Range(Integer.valueOf(next.start), Integer.valueOf(next.stop)));
        }
        return arraySet;
    }

    public static boolean hasSameUids(Set<UidRange> set, Set<UidRange> set2) {
        if (set == null) {
            return set2 == null;
        }
        if (set2 == null) {
            return false;
        }
        ArraySet arraySet = new ArraySet(set2);
        for (UidRange next : set) {
            if (!arraySet.contains(next)) {
                return false;
            }
            arraySet.remove(next);
        }
        return arraySet.isEmpty();
    }
}
