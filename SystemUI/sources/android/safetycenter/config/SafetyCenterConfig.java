package android.safetycenter.config;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetyCenterConfig implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterConfig> CREATOR = new Parcelable.Creator<SafetyCenterConfig>() {
        public SafetyCenterConfig createFromParcel(Parcel parcel) {
            List list = (List) Objects.requireNonNull(parcel.createTypedArrayList(SafetySourcesGroup.CREATOR));
            Builder builder = new Builder();
            for (int i = 0; i < list.size(); i++) {
                builder.addSafetySourcesGroup((SafetySourcesGroup) list.get(i));
            }
            return builder.build();
        }

        public SafetyCenterConfig[] newArray(int i) {
            return new SafetyCenterConfig[i];
        }
    };
    private final List<SafetySourcesGroup> mSafetySourcesGroups;

    public int describeContents() {
        return 0;
    }

    private SafetyCenterConfig(List<SafetySourcesGroup> list) {
        this.mSafetySourcesGroups = list;
    }

    public List<SafetySourcesGroup> getSafetySourcesGroups() {
        return this.mSafetySourcesGroups;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterConfig)) {
            return false;
        }
        return Objects.equals(this.mSafetySourcesGroups, ((SafetyCenterConfig) obj).mSafetySourcesGroups);
    }

    public int hashCode() {
        return Objects.hash(this.mSafetySourcesGroups);
    }

    public String toString() {
        return "SafetyCenterConfig{mSafetySourcesGroups=" + this.mSafetySourcesGroups + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.mSafetySourcesGroups);
    }

    public static final class Builder {
        private final List<SafetySourcesGroup> mSafetySourcesGroups = new ArrayList();

        public Builder addSafetySourcesGroup(SafetySourcesGroup safetySourcesGroup) {
            this.mSafetySourcesGroups.add((SafetySourcesGroup) Objects.requireNonNull(safetySourcesGroup));
            return this;
        }

        public SafetyCenterConfig build() {
            List unmodifiableList = Collections.unmodifiableList(new ArrayList(this.mSafetySourcesGroups));
            if (!unmodifiableList.isEmpty()) {
                HashSet hashSet = new HashSet();
                HashSet hashSet2 = new HashSet();
                int size = unmodifiableList.size();
                int i = 0;
                while (i < size) {
                    SafetySourcesGroup safetySourcesGroup = (SafetySourcesGroup) unmodifiableList.get(i);
                    String id = safetySourcesGroup.getId();
                    if (!hashSet2.contains(id)) {
                        hashSet2.add(id);
                        List<SafetySource> safetySources = safetySourcesGroup.getSafetySources();
                        int size2 = safetySources.size();
                        int i2 = 0;
                        while (i2 < size2) {
                            String id2 = safetySources.get(i2).getId();
                            if (!hashSet.contains(id2)) {
                                hashSet.add(id2);
                                i2++;
                            } else {
                                throw new IllegalStateException(String.format("Duplicate id %s among safety sources", id2));
                            }
                        }
                        i++;
                    } else {
                        throw new IllegalStateException(String.format("Duplicate id %s among safety sources groups", id));
                    }
                }
                return new SafetyCenterConfig(unmodifiableList);
            }
            throw new IllegalStateException("No safety sources groups present");
        }
    }
}
