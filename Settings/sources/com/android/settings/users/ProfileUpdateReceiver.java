package com.android.settings.users;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settings.Utils;
import com.android.settingslib.R$string;
/* loaded from: classes.dex */
public class ProfileUpdateReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, Intent intent) {
        new Thread() { // from class: com.android.settings.users.ProfileUpdateReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                UserSettings.copyMeProfilePhoto(context, null);
                ProfileUpdateReceiver.copyProfileName(context);
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void copyProfileName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", 0);
        if (sharedPreferences.contains("name_copied_once")) {
            return;
        }
        int myUserId = UserHandle.myUserId();
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        String meProfileName = Utils.getMeProfileName(context, false);
        if (meProfileName == null || meProfileName.length() <= 0 || isCurrentNameInteresting(context, userManager)) {
            return;
        }
        userManager.setUserName(myUserId, meProfileName);
        sharedPreferences.edit().putBoolean("name_copied_once", true).commit();
    }

    private static boolean isCurrentNameInteresting(Context context, UserManager userManager) {
        String string;
        if (!userManager.isUserNameSet()) {
            return false;
        }
        String userName = userManager.getUserName();
        if (userManager.isRestrictedProfile() || userManager.isProfile()) {
            string = context.getString(R$string.user_new_profile_name);
        } else {
            string = context.getString(R$string.user_new_user_name);
        }
        return userName != null && !userName.equals(string);
    }
}
