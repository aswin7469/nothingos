package android.safetycenter.config;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetySourcesGroup implements Parcelable {
    public static final Parcelable.Creator<SafetySourcesGroup> CREATOR = new Parcelable.Creator<SafetySourcesGroup>() {
        public SafetySourcesGroup createFromParcel(Parcel parcel) {
            Builder statelessIconType = new Builder().setId(parcel.readString()).setTitleResId(parcel.readInt()).setSummaryResId(parcel.readInt()).setStatelessIconType(parcel.readInt());
            List list = (List) Objects.requireNonNull(parcel.createTypedArrayList(SafetySource.CREATOR));
            for (int i = 0; i < list.size(); i++) {
                statelessIconType.addSafetySource((SafetySource) list.get(i));
            }
            return statelessIconType.build();
        }

        public SafetySourcesGroup[] newArray(int i) {
            return new SafetySourcesGroup[i];
        }
    };
    public static final int SAFETY_SOURCES_GROUP_TYPE_COLLAPSIBLE = 0;
    public static final int SAFETY_SOURCES_GROUP_TYPE_HIDDEN = 2;
    public static final int SAFETY_SOURCES_GROUP_TYPE_RIGID = 1;
    public static final int STATELESS_ICON_TYPE_NONE = 0;
    public static final int STATELESS_ICON_TYPE_PRIVACY = 1;
    private final String mId;
    private final List<SafetySource> mSafetySources;
    private final int mStatelessIconType;
    private final int mSummaryResId;
    private final int mTitleResId;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SafetySourceGroupType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StatelessIconType {
    }

    public int describeContents() {
        return 0;
    }

    private SafetySourcesGroup(String str, int i, int i2, int i3, List<SafetySource> list) {
        this.mId = str;
        this.mTitleResId = i;
        this.mSummaryResId = i2;
        this.mStatelessIconType = i3;
        this.mSafetySources = list;
    }

    public int getType() {
        if (this.mTitleResId == 0) {
            return 2;
        }
        return (this.mSummaryResId == 0 && this.mStatelessIconType == 0) ? 1 : 0;
    }

    public String getId() {
        return this.mId;
    }

    public int getTitleResId() {
        return this.mTitleResId;
    }

    public int getSummaryResId() {
        return this.mSummaryResId;
    }

    public int getStatelessIconType() {
        return this.mStatelessIconType;
    }

    public List<SafetySource> getSafetySources() {
        return this.mSafetySources;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySourcesGroup)) {
            return false;
        }
        SafetySourcesGroup safetySourcesGroup = (SafetySourcesGroup) obj;
        if (Objects.equals(this.mId, safetySourcesGroup.mId) && this.mTitleResId == safetySourcesGroup.mTitleResId && this.mSummaryResId == safetySourcesGroup.mSummaryResId && this.mStatelessIconType == safetySourcesGroup.mStatelessIconType && Objects.equals(this.mSafetySources, safetySourcesGroup.mSafetySources)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mId, Integer.valueOf(this.mTitleResId), Integer.valueOf(this.mSummaryResId), Integer.valueOf(this.mStatelessIconType), this.mSafetySources);
    }

    public String toString() {
        return "SafetySourcesGroup{mId='" + this.mId + "', mTitleResId=" + this.mTitleResId + ", mSummaryResId=" + this.mSummaryResId + ", mStatelessIconType=" + this.mStatelessIconType + ", mSafetySources=" + this.mSafetySources + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        parcel.writeInt(this.mTitleResId);
        parcel.writeInt(this.mSummaryResId);
        parcel.writeInt(this.mStatelessIconType);
        parcel.writeTypedList(this.mSafetySources);
    }

    public static final class Builder {
        private String mId;
        private final List<SafetySource> mSafetySources = new ArrayList();
        private Integer mStatelessIconType;
        private Integer mSummaryResId;
        private Integer mTitleResId;

        public Builder setId(String str) {
            this.mId = str;
            return this;
        }

        public Builder setTitleResId(int i) {
            this.mTitleResId = Integer.valueOf(i);
            return this;
        }

        public Builder setSummaryResId(int i) {
            this.mSummaryResId = Integer.valueOf(i);
            return this;
        }

        public Builder setStatelessIconType(int i) {
            this.mStatelessIconType = Integer.valueOf(i);
            return this;
        }

        public Builder addSafetySource(SafetySource safetySource) {
            this.mSafetySources.add((SafetySource) Objects.requireNonNull(safetySource));
            return this;
        }

        public SafetySourcesGroup build() {
            boolean z = true;
            BuilderUtils.validateAttribute(this.mId, "id", true, false);
            List unmodifiableList = Collections.unmodifiableList(new ArrayList(this.mSafetySources));
            if (!unmodifiableList.isEmpty()) {
                int size = unmodifiableList.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        z = false;
                        break;
                    } else if (((SafetySource) unmodifiableList.get(i)).getType() != 3) {
                        break;
                    } else {
                        i++;
                    }
                }
                return new SafetySourcesGroup(this.mId, BuilderUtils.validateResId(this.mTitleResId, "title", z, false), BuilderUtils.validateResId(this.mSummaryResId, "summary", false, false), BuilderUtils.validateIntDef(this.mStatelessIconType, "statelessIconType", false, false, 0, 0, 1), unmodifiableList);
            }
            throw new IllegalStateException("Safety sources group empty");
        }
    }
}
