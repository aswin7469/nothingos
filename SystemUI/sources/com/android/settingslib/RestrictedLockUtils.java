package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.os.UserManager;
import java.util.Objects;

public class RestrictedLockUtils {
    public static EnforcedAdmin getProfileOrDeviceOwner(Context context, UserHandle userHandle) {
        return getProfileOrDeviceOwner(context, (String) null, userHandle);
    }

    public static EnforcedAdmin getProfileOrDeviceOwner(Context context, String str, UserHandle userHandle) {
        DevicePolicyManager devicePolicyManager;
        ComponentName deviceOwnerComponentOnAnyUser;
        if (userHandle == null || (devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        try {
            ComponentName profileOwner = ((DevicePolicyManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService(DevicePolicyManager.class)).getProfileOwner();
            if (profileOwner != null) {
                return new EnforcedAdmin(profileOwner, str, userHandle);
            }
            if (!Objects.equals(devicePolicyManager.getDeviceOwnerUser(), userHandle) || (deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == null) {
                return null;
            }
            return new EnforcedAdmin(deviceOwnerComponentOnAnyUser, str, userHandle);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    public static void sendShowAdminSupportDetailsIntent(Context context, EnforcedAdmin enforcedAdmin) {
        Intent showAdminSupportDetailsIntent = getShowAdminSupportDetailsIntent(context, enforcedAdmin);
        int myUserId = UserHandle.myUserId();
        if (enforcedAdmin != null) {
            if (enforcedAdmin.user != null && isCurrentUserOrProfile(context, enforcedAdmin.user.getIdentifier())) {
                myUserId = enforcedAdmin.user.getIdentifier();
            }
            showAdminSupportDetailsIntent.putExtra("android.app.extra.RESTRICTION", enforcedAdmin.enforcedRestriction);
        }
        context.startActivityAsUser(showAdminSupportDetailsIntent, UserHandle.of(myUserId));
    }

    public static Intent getShowAdminSupportDetailsIntent(Context context, EnforcedAdmin enforcedAdmin) {
        Intent intent = new Intent("android.settings.SHOW_ADMIN_SUPPORT_DETAILS");
        if (enforcedAdmin != null) {
            if (enforcedAdmin.component != null) {
                intent.putExtra("android.app.extra.DEVICE_ADMIN", enforcedAdmin.component);
            }
            intent.putExtra("android.intent.extra.USER", enforcedAdmin.user);
        }
        return intent;
    }

    public static boolean isCurrentUserOrProfile(Context context, int i) {
        return ((UserManager) context.getSystemService(UserManager.class)).getUserProfiles().contains(UserHandle.of(i));
    }

    public static class EnforcedAdmin {
        public static final EnforcedAdmin MULTIPLE_ENFORCED_ADMIN = new EnforcedAdmin();
        public ComponentName component;
        public String enforcedRestriction;
        public UserHandle user;

        public static EnforcedAdmin createDefaultEnforcedAdminWithRestriction(String str) {
            EnforcedAdmin enforcedAdmin = new EnforcedAdmin();
            enforcedAdmin.enforcedRestriction = str;
            return enforcedAdmin;
        }

        public EnforcedAdmin(ComponentName componentName, UserHandle userHandle) {
            this.enforcedRestriction = null;
            this.component = componentName;
            this.user = userHandle;
        }

        public EnforcedAdmin(ComponentName componentName, String str, UserHandle userHandle) {
            this.component = componentName;
            this.enforcedRestriction = str;
            this.user = userHandle;
        }

        public EnforcedAdmin(EnforcedAdmin enforcedAdmin) {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
            if (enforcedAdmin != null) {
                this.component = enforcedAdmin.component;
                this.enforcedRestriction = enforcedAdmin.enforcedRestriction;
                this.user = enforcedAdmin.user;
                return;
            }
            throw new IllegalArgumentException();
        }

        public EnforcedAdmin() {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
        }

        public static EnforcedAdmin combine(EnforcedAdmin enforcedAdmin, EnforcedAdmin enforcedAdmin2) {
            if (enforcedAdmin == null) {
                return enforcedAdmin2;
            }
            if (enforcedAdmin2 == null || enforcedAdmin.equals(enforcedAdmin2)) {
                return enforcedAdmin;
            }
            if (enforcedAdmin.enforcedRestriction.equals(enforcedAdmin2.enforcedRestriction)) {
                return MULTIPLE_ENFORCED_ADMIN;
            }
            throw new IllegalArgumentException("Admins with different restriction cannot be combined");
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EnforcedAdmin enforcedAdmin = (EnforcedAdmin) obj;
            if (!Objects.equals(this.user, enforcedAdmin.user) || !Objects.equals(this.component, enforcedAdmin.component) || !Objects.equals(this.enforcedRestriction, enforcedAdmin.enforcedRestriction)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.component, this.enforcedRestriction, this.user);
        }

        public String toString() {
            return "EnforcedAdmin{component=" + this.component + ", enforcedRestriction='" + this.enforcedRestriction + ", user=" + this.user + '}';
        }
    }
}
