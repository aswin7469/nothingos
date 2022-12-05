package android.app.admin;

import android.accounts.Account;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ManagedProfileProvisioningParams implements Parcelable {
    private static final String ACCOUNT_TO_MIGRATE_PROVIDED_PARAM = "ACCOUNT_TO_MIGRATE_PROVIDED";
    public static final Parcelable.Creator<ManagedProfileProvisioningParams> CREATOR = new Parcelable.Creator<ManagedProfileProvisioningParams>() { // from class: android.app.admin.ManagedProfileProvisioningParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ManagedProfileProvisioningParams mo3559createFromParcel(Parcel in) {
            ComponentName componentName = (ComponentName) in.readTypedObject(ComponentName.CREATOR);
            String ownerName = in.readString();
            String profileName = in.readString();
            Account account = (Account) in.readTypedObject(Account.CREATOR);
            boolean leaveAllSystemAppsEnabled = in.readBoolean();
            boolean organizationOwnedProvisioning = in.readBoolean();
            boolean keepAccountMigrated = in.readBoolean();
            return new ManagedProfileProvisioningParams(componentName, ownerName, profileName, account, leaveAllSystemAppsEnabled, organizationOwnedProvisioning, keepAccountMigrated);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ManagedProfileProvisioningParams[] mo3560newArray(int size) {
            return new ManagedProfileProvisioningParams[size];
        }
    };
    private static final String KEEP_MIGRATED_ACCOUNT_PARAM = "KEEP_MIGRATED_ACCOUNT";
    private static final String LEAVE_ALL_SYSTEM_APPS_ENABLED_PARAM = "LEAVE_ALL_SYSTEM_APPS_ENABLED";
    private static final String ORGANIZATION_OWNED_PROVISIONING_PARAM = "ORGANIZATION_OWNED_PROVISIONING";
    private final Account mAccountToMigrate;
    private final boolean mKeepAccountMigrated;
    private final boolean mLeaveAllSystemAppsEnabled;
    private final boolean mOrganizationOwnedProvisioning;
    private final String mOwnerName;
    private final ComponentName mProfileAdminComponentName;
    private final String mProfileName;

    private ManagedProfileProvisioningParams(ComponentName profileAdminComponentName, String ownerName, String profileName, Account accountToMigrate, boolean leaveAllSystemAppsEnabled, boolean organizationOwnedProvisioning, boolean keepAccountMigrated) {
        Objects.requireNonNull(profileAdminComponentName);
        this.mProfileAdminComponentName = profileAdminComponentName;
        Objects.requireNonNull(ownerName);
        this.mOwnerName = ownerName;
        this.mProfileName = profileName;
        this.mAccountToMigrate = accountToMigrate;
        this.mLeaveAllSystemAppsEnabled = leaveAllSystemAppsEnabled;
        this.mOrganizationOwnedProvisioning = organizationOwnedProvisioning;
        this.mKeepAccountMigrated = keepAccountMigrated;
    }

    public ComponentName getProfileAdminComponentName() {
        return this.mProfileAdminComponentName;
    }

    public String getOwnerName() {
        return this.mOwnerName;
    }

    public String getProfileName() {
        return this.mProfileName;
    }

    public Account getAccountToMigrate() {
        return this.mAccountToMigrate;
    }

    public boolean isLeaveAllSystemAppsEnabled() {
        return this.mLeaveAllSystemAppsEnabled;
    }

    public boolean isOrganizationOwnedProvisioning() {
        return this.mOrganizationOwnedProvisioning;
    }

    public boolean isKeepAccountMigrated() {
        return this.mKeepAccountMigrated;
    }

    public void logParams(String callerPackage) {
        Objects.requireNonNull(callerPackage);
        logParam(callerPackage, LEAVE_ALL_SYSTEM_APPS_ENABLED_PARAM, this.mLeaveAllSystemAppsEnabled);
        logParam(callerPackage, ORGANIZATION_OWNED_PROVISIONING_PARAM, this.mOrganizationOwnedProvisioning);
        logParam(callerPackage, KEEP_MIGRATED_ACCOUNT_PARAM, this.mKeepAccountMigrated);
        logParam(callerPackage, ACCOUNT_TO_MIGRATE_PROVIDED_PARAM, this.mAccountToMigrate != null);
    }

    private void logParam(String callerPackage, String param, boolean value) {
        DevicePolicyEventLogger.createEvent(197).setStrings(callerPackage).setAdmin(this.mProfileAdminComponentName).setStrings(param).setBoolean(value).write();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private Account mAccountToMigrate;
        private boolean mKeepAccountMigrated;
        private boolean mLeaveAllSystemAppsEnabled;
        private boolean mOrganizationOwnedProvisioning;
        private final String mOwnerName;
        private final ComponentName mProfileAdminComponentName;
        private String mProfileName;

        public Builder(ComponentName profileAdminComponentName, String ownerName) {
            Objects.requireNonNull(profileAdminComponentName);
            Objects.requireNonNull(ownerName);
            this.mProfileAdminComponentName = profileAdminComponentName;
            this.mOwnerName = ownerName;
        }

        public Builder setProfileName(String profileName) {
            this.mProfileName = profileName;
            return this;
        }

        public Builder setAccountToMigrate(Account accountToMigrate) {
            this.mAccountToMigrate = accountToMigrate;
            return this;
        }

        public Builder setLeaveAllSystemAppsEnabled(boolean leaveAllSystemAppsEnabled) {
            this.mLeaveAllSystemAppsEnabled = leaveAllSystemAppsEnabled;
            return this;
        }

        public Builder setOrganizationOwnedProvisioning(boolean organizationOwnedProvisioning) {
            this.mOrganizationOwnedProvisioning = organizationOwnedProvisioning;
            return this;
        }

        public Builder setKeepAccountMigrated(boolean keepAccountMigrated) {
            this.mKeepAccountMigrated = keepAccountMigrated;
            return this;
        }

        public ManagedProfileProvisioningParams build() {
            return new ManagedProfileProvisioningParams(this.mProfileAdminComponentName, this.mOwnerName, this.mProfileName, this.mAccountToMigrate, this.mLeaveAllSystemAppsEnabled, this.mOrganizationOwnedProvisioning, this.mKeepAccountMigrated);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v8, types: [android.accounts.Account] */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ManagedProfileProvisioningParams{mProfileAdminComponentName=");
        sb.append(this.mProfileAdminComponentName);
        sb.append(", mOwnerName=");
        sb.append(this.mOwnerName);
        sb.append(", mProfileName=");
        String str = this.mProfileName;
        String str2 = "null";
        if (str == null) {
            str = str2;
        }
        sb.append(str);
        sb.append(", mAccountToMigrate=");
        ?? r1 = this.mAccountToMigrate;
        if (r1 != 0) {
            str2 = r1;
        }
        sb.append((Object) str2);
        sb.append(", mLeaveAllSystemAppsEnabled=");
        sb.append(this.mLeaveAllSystemAppsEnabled);
        sb.append(", mOrganizationOwnedProvisioning=");
        sb.append(this.mOrganizationOwnedProvisioning);
        sb.append(", mKeepAccountMigrated=");
        sb.append(this.mKeepAccountMigrated);
        sb.append('}');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mProfileAdminComponentName, flags);
        dest.writeString(this.mOwnerName);
        dest.writeString(this.mProfileName);
        dest.writeTypedObject(this.mAccountToMigrate, flags);
        dest.writeBoolean(this.mLeaveAllSystemAppsEnabled);
        dest.writeBoolean(this.mOrganizationOwnedProvisioning);
        dest.writeBoolean(this.mKeepAccountMigrated);
    }
}
