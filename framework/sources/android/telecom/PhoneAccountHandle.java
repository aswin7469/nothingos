package android.telecom;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.UserHandle;
import java.util.Objects;
/* loaded from: classes3.dex */
public final class PhoneAccountHandle implements Parcelable {
    public static final Parcelable.Creator<PhoneAccountHandle> CREATOR = new Parcelable.Creator<PhoneAccountHandle>() { // from class: android.telecom.PhoneAccountHandle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public PhoneAccountHandle mo3559createFromParcel(Parcel in) {
            return new PhoneAccountHandle(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public PhoneAccountHandle[] mo3560newArray(int size) {
            return new PhoneAccountHandle[size];
        }
    };
    private final ComponentName mComponentName;
    private final String mId;
    private final UserHandle mUserHandle;

    public PhoneAccountHandle(ComponentName componentName, String id) {
        this(componentName, id, Process.myUserHandle());
    }

    public PhoneAccountHandle(ComponentName componentName, String id, UserHandle userHandle) {
        checkParameters(componentName, userHandle);
        this.mComponentName = componentName;
        this.mId = id;
        this.mUserHandle = userHandle;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public String getId() {
        return this.mId;
    }

    public UserHandle getUserHandle() {
        return this.mUserHandle;
    }

    public int hashCode() {
        return Objects.hash(this.mComponentName, this.mId, this.mUserHandle);
    }

    public String toString() {
        return this.mComponentName + ", " + Log.pii(this.mId) + ", " + this.mUserHandle;
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof PhoneAccountHandle) && Objects.equals(((PhoneAccountHandle) other).getComponentName(), getComponentName()) && Objects.equals(((PhoneAccountHandle) other).getId(), getId()) && Objects.equals(((PhoneAccountHandle) other).getUserHandle(), getUserHandle());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mComponentName.writeToParcel(out, flags);
        out.writeString(this.mId);
        this.mUserHandle.writeToParcel(out, flags);
    }

    private void checkParameters(ComponentName componentName, UserHandle userHandle) {
        if (componentName == null) {
            android.util.Log.w("PhoneAccountHandle", new Exception("PhoneAccountHandle has been created with null ComponentName!"));
        }
        if (userHandle == null) {
            android.util.Log.w("PhoneAccountHandle", new Exception("PhoneAccountHandle has been created with null UserHandle!"));
        }
    }

    private PhoneAccountHandle(Parcel in) {
        this(ComponentName.CREATOR.mo3559createFromParcel(in), in.readString(), UserHandle.CREATOR.mo3559createFromParcel(in));
    }

    public static boolean areFromSamePackage(PhoneAccountHandle a, PhoneAccountHandle b) {
        String bPackageName = null;
        String aPackageName = a != null ? a.getComponentName().getPackageName() : null;
        if (b != null) {
            bPackageName = b.getComponentName().getPackageName();
        }
        return Objects.equals(aPackageName, bPackageName);
    }
}