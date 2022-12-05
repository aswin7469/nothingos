package android.app.admin;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FullyManagedDeviceProvisioningParams implements Parcelable {
    private static final String CAN_DEVICE_OWNER_GRANT_SENSOR_PERMISSIONS_PARAM = "CAN_DEVICE_OWNER_GRANT_SENSOR_PERMISSIONS";
    public static final Parcelable.Creator<FullyManagedDeviceProvisioningParams> CREATOR = new Parcelable.Creator<FullyManagedDeviceProvisioningParams>() { // from class: android.app.admin.FullyManagedDeviceProvisioningParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FullyManagedDeviceProvisioningParams mo3559createFromParcel(Parcel in) {
            ComponentName componentName = (ComponentName) in.readTypedObject(ComponentName.CREATOR);
            String ownerName = in.readString();
            boolean leaveAllSystemAppsEnabled = in.readBoolean();
            String timeZone = in.readString();
            long localtime = in.readLong();
            String locale = in.readString();
            boolean deviceOwnerCanGrantSensorsPermissions = in.readBoolean();
            return new FullyManagedDeviceProvisioningParams(componentName, ownerName, leaveAllSystemAppsEnabled, timeZone, localtime, locale, deviceOwnerCanGrantSensorsPermissions);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FullyManagedDeviceProvisioningParams[] mo3560newArray(int size) {
            return new FullyManagedDeviceProvisioningParams[size];
        }
    };
    private static final String LEAVE_ALL_SYSTEM_APPS_ENABLED_PARAM = "LEAVE_ALL_SYSTEM_APPS_ENABLED";
    private static final String LOCALE_PROVIDED_PARAM = "LOCALE_PROVIDED";
    private static final String TIME_ZONE_PROVIDED_PARAM = "TIME_ZONE_PROVIDED";
    private final ComponentName mDeviceAdminComponentName;
    private final boolean mDeviceOwnerCanGrantSensorsPermissions;
    private final boolean mLeaveAllSystemAppsEnabled;
    private final long mLocalTime;
    private final Locale mLocale;
    private final String mOwnerName;
    private final String mTimeZone;

    private FullyManagedDeviceProvisioningParams(ComponentName deviceAdminComponentName, String ownerName, boolean leaveAllSystemAppsEnabled, String timeZone, long localTime, Locale locale, boolean deviceOwnerCanGrantSensorsPermissions) {
        Objects.requireNonNull(deviceAdminComponentName);
        this.mDeviceAdminComponentName = deviceAdminComponentName;
        Objects.requireNonNull(ownerName);
        this.mOwnerName = ownerName;
        this.mLeaveAllSystemAppsEnabled = leaveAllSystemAppsEnabled;
        this.mTimeZone = timeZone;
        this.mLocalTime = localTime;
        this.mLocale = locale;
        this.mDeviceOwnerCanGrantSensorsPermissions = deviceOwnerCanGrantSensorsPermissions;
    }

    private FullyManagedDeviceProvisioningParams(ComponentName deviceAdminComponentName, String ownerName, boolean leaveAllSystemAppsEnabled, String timeZone, long localTime, String localeStr, boolean deviceOwnerCanGrantSensorsPermissions) {
        this(deviceAdminComponentName, ownerName, leaveAllSystemAppsEnabled, timeZone, localTime, getLocale(localeStr), deviceOwnerCanGrantSensorsPermissions);
    }

    private static Locale getLocale(String localeStr) {
        if (localeStr == null) {
            return null;
        }
        return Locale.forLanguageTag(localeStr);
    }

    public ComponentName getDeviceAdminComponentName() {
        return this.mDeviceAdminComponentName;
    }

    public String getOwnerName() {
        return this.mOwnerName;
    }

    public boolean isLeaveAllSystemAppsEnabled() {
        return this.mLeaveAllSystemAppsEnabled;
    }

    public String getTimeZone() {
        return this.mTimeZone;
    }

    public long getLocalTime() {
        return this.mLocalTime;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public boolean canDeviceOwnerGrantSensorsPermissions() {
        return this.mDeviceOwnerCanGrantSensorsPermissions;
    }

    public void logParams(String callerPackage) {
        Objects.requireNonNull(callerPackage);
        logParam(callerPackage, LEAVE_ALL_SYSTEM_APPS_ENABLED_PARAM, this.mLeaveAllSystemAppsEnabled);
        logParam(callerPackage, CAN_DEVICE_OWNER_GRANT_SENSOR_PERMISSIONS_PARAM, this.mDeviceOwnerCanGrantSensorsPermissions);
        boolean z = true;
        logParam(callerPackage, TIME_ZONE_PROVIDED_PARAM, this.mTimeZone != null);
        if (this.mLocale == null) {
            z = false;
        }
        logParam(callerPackage, LOCALE_PROVIDED_PARAM, z);
    }

    private void logParam(String callerPackage, String param, boolean value) {
        DevicePolicyEventLogger.createEvent(197).setStrings(callerPackage).setAdmin(this.mDeviceAdminComponentName).setStrings(param).setBoolean(value).write();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final ComponentName mDeviceAdminComponentName;
        boolean mDeviceOwnerCanGrantSensorsPermissions = true;
        private boolean mLeaveAllSystemAppsEnabled;
        private long mLocalTime;
        private Locale mLocale;
        private final String mOwnerName;
        private String mTimeZone;

        public Builder(ComponentName deviceAdminComponentName, String ownerName) {
            Objects.requireNonNull(deviceAdminComponentName);
            this.mDeviceAdminComponentName = deviceAdminComponentName;
            Objects.requireNonNull(ownerName);
            this.mOwnerName = ownerName;
        }

        public Builder setLeaveAllSystemAppsEnabled(boolean leaveAllSystemAppsEnabled) {
            this.mLeaveAllSystemAppsEnabled = leaveAllSystemAppsEnabled;
            return this;
        }

        public Builder setTimeZone(String timeZone) {
            this.mTimeZone = timeZone;
            return this;
        }

        public Builder setLocalTime(long localTime) {
            this.mLocalTime = localTime;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.mLocale = locale;
            return this;
        }

        public Builder setDeviceOwnerCanGrantSensorsPermissions(boolean mayGrant) {
            this.mDeviceOwnerCanGrantSensorsPermissions = mayGrant;
            return this;
        }

        public FullyManagedDeviceProvisioningParams build() {
            return new FullyManagedDeviceProvisioningParams(this.mDeviceAdminComponentName, this.mOwnerName, this.mLeaveAllSystemAppsEnabled, this.mTimeZone, this.mLocalTime, this.mLocale, this.mDeviceOwnerCanGrantSensorsPermissions);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.util.Locale] */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FullyManagedDeviceProvisioningParams{mDeviceAdminComponentName=");
        sb.append(this.mDeviceAdminComponentName);
        sb.append(", mOwnerName=");
        sb.append(this.mOwnerName);
        sb.append(", mLeaveAllSystemAppsEnabled=");
        sb.append(this.mLeaveAllSystemAppsEnabled);
        sb.append(", mTimeZone=");
        String str = this.mTimeZone;
        String str2 = "null";
        if (str == null) {
            str = str2;
        }
        sb.append(str);
        sb.append(", mLocalTime=");
        sb.append(this.mLocalTime);
        sb.append(", mLocale=");
        ?? r1 = this.mLocale;
        if (r1 != 0) {
            str2 = r1;
        }
        sb.append((Object) str2);
        sb.append(", mDeviceOwnerCanGrantSensorsPermissions=");
        sb.append(this.mDeviceOwnerCanGrantSensorsPermissions);
        sb.append('}');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mDeviceAdminComponentName, flags);
        dest.writeString(this.mOwnerName);
        dest.writeBoolean(this.mLeaveAllSystemAppsEnabled);
        dest.writeString(this.mTimeZone);
        dest.writeLong(this.mLocalTime);
        Locale locale = this.mLocale;
        dest.writeString(locale == null ? null : locale.toLanguageTag());
        dest.writeBoolean(this.mDeviceOwnerCanGrantSensorsPermissions);
    }
}
