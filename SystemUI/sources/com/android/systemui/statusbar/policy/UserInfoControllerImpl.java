package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.util.Log;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.UserInfoController;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class UserInfoControllerImpl implements UserInfoController {
    private static final String TAG = "UserInfoController";
    private final ArrayList<UserInfoController.OnUserInfoChangedListener> mCallbacks = new ArrayList<>();
    /* access modifiers changed from: private */
    public final Context mContext;
    private final BroadcastReceiver mProfileReceiver;
    private final BroadcastReceiver mReceiver;
    /* access modifiers changed from: private */
    public String mUserAccount;
    /* access modifiers changed from: private */
    public Drawable mUserDrawable;
    /* access modifiers changed from: private */
    public AsyncTask<Void, Void, UserInfoQueryResult> mUserInfoTask;
    /* access modifiers changed from: private */
    public String mUserName;

    @Inject
    public UserInfoControllerImpl(Context context) {
        C31911 r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                    UserInfoControllerImpl.this.reloadUserInfo();
                }
            }
        };
        this.mReceiver = r0;
        C31922 r2 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.provider.Contacts.PROFILE_CHANGED".equals(action) || "android.intent.action.USER_INFO_CHANGED".equals(action)) {
                    try {
                        if (intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()) == ActivityManager.getService().getCurrentUser().id) {
                            UserInfoControllerImpl.this.reloadUserInfo();
                        }
                    } catch (RemoteException e) {
                        Log.e(UserInfoControllerImpl.TAG, "Couldn't get current user id for profile change", e);
                    }
                }
            }
        };
        this.mProfileReceiver = r2;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        context.registerReceiver(r0, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.provider.Contacts.PROFILE_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_INFO_CHANGED");
        context.registerReceiverAsUser(r2, UserHandle.ALL, intentFilter2, (String) null, (Handler) null, 2);
    }

    public void addCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        this.mCallbacks.add(onUserInfoChangedListener);
        onUserInfoChangedListener.onUserInfoChanged(this.mUserName, this.mUserDrawable, this.mUserAccount);
    }

    public void removeCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        this.mCallbacks.remove((Object) onUserInfoChangedListener);
    }

    public void reloadUserInfo() {
        AsyncTask<Void, Void, UserInfoQueryResult> asyncTask = this.mUserInfoTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mUserInfoTask = null;
        }
        queryForUserInformation();
    }

    private void queryForUserInformation() {
        try {
            UserInfo currentUser = ActivityManager.getService().getCurrentUser();
            final Context createPackageContextAsUser = this.mContext.createPackageContextAsUser("android", 0, new UserHandle(currentUser.id));
            final int i = currentUser.id;
            final boolean isGuest = currentUser.isGuest();
            final String str = currentUser.name;
            final boolean z = this.mContext.getThemeResId() != 2132018221;
            Resources resources = this.mContext.getResources();
            final int max = Math.max(resources.getDimensionPixelSize(C1893R.dimen.multi_user_avatar_expanded_size), resources.getDimensionPixelSize(C1893R.dimen.multi_user_avatar_keyguard_size));
            C31933 r6 = new AsyncTask<Void, Void, UserInfoQueryResult>() {
                /* access modifiers changed from: protected */
                public UserInfoQueryResult doInBackground(Void... voidArr) {
                    Drawable drawable;
                    Cursor query;
                    UserManager userManager = UserManager.get(UserInfoControllerImpl.this.mContext);
                    String str = str;
                    Bitmap userIcon = userManager.getUserIcon(i);
                    if (userIcon != null) {
                        drawable = new UserIconDrawable(max).setIcon(userIcon).setBadgeIfManagedUser(UserInfoControllerImpl.this.mContext, i).bake();
                    } else {
                        drawable = UserIcons.getDefaultUserIcon(createPackageContextAsUser.getResources(), isGuest ? -10000 : i, z);
                    }
                    if (userManager.getUsers().size() <= 1 && (query = createPackageContextAsUser.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, new String[]{"_id", "display_name"}, (String) null, (String[]) null, (String) null)) != null) {
                        try {
                            if (query.moveToFirst()) {
                                str = query.getString(query.getColumnIndex("display_name"));
                            }
                        } finally {
                            query.close();
                        }
                    }
                    return new UserInfoQueryResult(str, drawable, userManager.getUserAccount(i));
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(UserInfoQueryResult userInfoQueryResult) {
                    String unused = UserInfoControllerImpl.this.mUserName = userInfoQueryResult.getName();
                    Drawable unused2 = UserInfoControllerImpl.this.mUserDrawable = userInfoQueryResult.getAvatar();
                    String unused3 = UserInfoControllerImpl.this.mUserAccount = userInfoQueryResult.getUserAccount();
                    AsyncTask unused4 = UserInfoControllerImpl.this.mUserInfoTask = null;
                    UserInfoControllerImpl.this.notifyChanged();
                }
            };
            this.mUserInfoTask = r6;
            r6.execute(new Void[0]);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Couldn't create user context", e);
            throw new RuntimeException((Throwable) e);
        } catch (RemoteException e2) {
            Log.e(TAG, "Couldn't get user info", e2);
            throw new RuntimeException((Throwable) e2);
        }
    }

    /* access modifiers changed from: private */
    public void notifyChanged() {
        Iterator<UserInfoController.OnUserInfoChangedListener> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onUserInfoChanged(this.mUserName, this.mUserDrawable, this.mUserAccount);
        }
    }

    public void onDensityOrFontScaleChanged() {
        reloadUserInfo();
    }

    private static class UserInfoQueryResult {
        private Drawable mAvatar;
        private String mName;
        private String mUserAccount;

        public UserInfoQueryResult(String str, Drawable drawable, String str2) {
            this.mName = str;
            this.mAvatar = drawable;
            this.mUserAccount = str2;
        }

        public String getName() {
            return this.mName;
        }

        public Drawable getAvatar() {
            return this.mAvatar;
        }

        public String getUserAccount() {
            return this.mUserAccount;
        }
    }
}
