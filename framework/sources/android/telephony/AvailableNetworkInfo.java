package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes3.dex */
public final class AvailableNetworkInfo implements Parcelable {
    public static final Parcelable.Creator<AvailableNetworkInfo> CREATOR = new Parcelable.Creator<AvailableNetworkInfo>() { // from class: android.telephony.AvailableNetworkInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public AvailableNetworkInfo mo3559createFromParcel(Parcel in) {
            return new AvailableNetworkInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public AvailableNetworkInfo[] mo3560newArray(int size) {
            return new AvailableNetworkInfo[size];
        }
    };
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = 3;
    public static final int PRIORITY_MED = 2;
    @Deprecated
    private ArrayList<Integer> mBands;
    private ArrayList<String> mMccMncs;
    private int mPriority;
    private ArrayList<RadioAccessSpecifier> mRadioAccessSpecifiers;
    private int mSubId;

    public int getSubId() {
        return this.mSubId;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public List<String> getMccMncs() {
        return (List) this.mMccMncs.clone();
    }

    public List<Integer> getBands() {
        return (List) this.mBands.clone();
    }

    public List<RadioAccessSpecifier> getRadioAccessSpecifiers() {
        return (List) this.mRadioAccessSpecifiers.clone();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSubId);
        dest.writeInt(this.mPriority);
        dest.writeStringList(this.mMccMncs);
        dest.writeList(this.mBands);
        dest.writeList(this.mRadioAccessSpecifiers);
    }

    private AvailableNetworkInfo(Parcel in) {
        this.mSubId = in.readInt();
        this.mPriority = in.readInt();
        ArrayList<String> arrayList = new ArrayList<>();
        this.mMccMncs = arrayList;
        in.readStringList(arrayList);
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        this.mBands = arrayList2;
        in.readList(arrayList2, Integer.class.getClassLoader());
        ArrayList<RadioAccessSpecifier> arrayList3 = new ArrayList<>();
        this.mRadioAccessSpecifiers = arrayList3;
        in.readList(arrayList3, RadioAccessSpecifier.class.getClassLoader());
    }

    public AvailableNetworkInfo(int subId, int priority, List<String> mccMncs, List<Integer> bands) {
        this(subId, priority, mccMncs, bands, new ArrayList());
    }

    private AvailableNetworkInfo(int subId, int priority, List<String> mccMncs, List<Integer> bands, List<RadioAccessSpecifier> radioAccessSpecifiers) {
        this.mSubId = subId;
        this.mPriority = priority;
        this.mMccMncs = new ArrayList<>(mccMncs);
        this.mBands = new ArrayList<>(bands);
        this.mRadioAccessSpecifiers = new ArrayList<>(radioAccessSpecifiers);
    }

    public boolean equals(Object o) {
        ArrayList<String> arrayList;
        try {
            AvailableNetworkInfo ani = (AvailableNetworkInfo) o;
            return o != null && this.mSubId == ani.mSubId && this.mPriority == ani.mPriority && (arrayList = this.mMccMncs) != null && arrayList.equals(ani.mMccMncs) && this.mBands.equals(ani.mBands) && this.mRadioAccessSpecifiers.equals(ani.getRadioAccessSpecifiers());
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mSubId), Integer.valueOf(this.mPriority), this.mMccMncs, this.mBands, this.mRadioAccessSpecifiers);
    }

    public String toString() {
        return "AvailableNetworkInfo: mSubId: " + this.mSubId + " mPriority: " + this.mPriority + " mMccMncs: " + Arrays.toString(this.mMccMncs.toArray()) + " mBands: " + Arrays.toString(this.mBands.toArray()) + " mRadioAccessSpecifiers: " + Arrays.toString(this.mRadioAccessSpecifiers.toArray());
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        private int mSubId = Integer.MIN_VALUE;
        private int mPriority = 3;
        private ArrayList<String> mMccMncs = new ArrayList<>();
        private ArrayList<Integer> mBands = new ArrayList<>();
        private ArrayList<RadioAccessSpecifier> mRadioAccessSpecifiers = new ArrayList<>();

        public Builder setSubId(int subId) {
            this.mSubId = subId;
            return this;
        }

        public Builder setPriority(int priority) {
            if (priority > 3 || priority < 1) {
                throw new IllegalArgumentException("A valid priority must be set");
            }
            this.mPriority = priority;
            return this;
        }

        public Builder setMccMncs(ArrayList<String> mccMncs) {
            Objects.requireNonNull(mccMncs, "A non-null ArrayList of mccmncs must be set. An empty list is still accepted. Please read documentation in AvailableNetworkService to see consequences of an empty Arraylist.");
            this.mMccMncs = mccMncs;
            return this;
        }

        public Builder setRadioAccessSpecifiers(ArrayList<RadioAccessSpecifier> radioAccessSpecifiers) {
            Objects.requireNonNull(radioAccessSpecifiers, "A non-null ArrayList of RadioAccessSpecifiers must be set. An empty list is still accepted. Please read documentation in AvailableNetworkService to see consequences of an empty Arraylist.");
            this.mRadioAccessSpecifiers = radioAccessSpecifiers;
            return this;
        }

        public AvailableNetworkInfo build() {
            if (this.mSubId == Integer.MIN_VALUE) {
                throw new IllegalArgumentException("A valid subId must be set");
            }
            return new AvailableNetworkInfo(this.mSubId, this.mPriority, this.mMccMncs, this.mBands, this.mRadioAccessSpecifiers);
        }
    }
}
