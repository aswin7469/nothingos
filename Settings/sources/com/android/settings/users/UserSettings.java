package com.android.settings.users;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.util.UserIcons;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.users.MultiUserSwitchBarController;
import com.android.settings.users.UserSettings;
import com.android.settings.widget.MainSwitchBarController;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.R$string;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.Utils;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.settingslib.users.EditUserInfoController;
import com.android.settingslib.users.UserCreatingDialog;
import com.android.settingslib.utils.ThreadUtils;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class UserSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceClickListener, MultiUserSwitchBarController.OnMultiUserSwitchChangedListener, DialogInterface.OnDismissListener {
    private static final IntentFilter USER_REMOVED_INTENT_FILTER;
    RestrictedPreference mAddGuest;
    RestrictedPreference mAddUser;
    private AddUserWhenLockedPreferenceController mAddUserWhenLockedPreferenceController;
    private boolean mAddingUser;
    private String mAddingUserName;
    private Drawable mDefaultIconDrawable;
    private boolean mGuestUserAutoCreated;
    UserPreference mMePreference;
    private MultiUserTopIntroPreferenceController mMultiUserTopIntroPreferenceController;
    private Drawable mPendingUserIcon;
    private CharSequence mPendingUserName;
    private MultiUserSwitchBarController mSwitchBarController;
    private UserCapabilities mUserCaps;
    private UserCreatingDialog mUserCreatingDialog;
    PreferenceGroup mUserListCategory;
    private UserManager mUserManager;
    private static SparseArray<Bitmap> sDarkDefaultUserBitmapCache = new SparseArray<>();
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.user_settings) { // from class: com.android.settings.users.UserSettings.12
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public boolean isPageSearchEnabled(Context context) {
            return UserCapabilities.create(context).mEnabled;
        }

        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<String> getNonIndexableKeysFromXml(Context context, int i, boolean z) {
            List<String> nonIndexableKeysFromXml = super.getNonIndexableKeysFromXml(context, i, z);
            new AddUserWhenLockedPreferenceController(context, "user_settings_add_users_when_locked").updateNonIndexableKeys(nonIndexableKeysFromXml);
            new AutoSyncDataPreferenceController(context, null).updateNonIndexableKeys(nonIndexableKeysFromXml);
            new AutoSyncPersonalDataPreferenceController(context, null).updateNonIndexableKeys(nonIndexableKeysFromXml);
            new AutoSyncWorkDataPreferenceController(context, null).updateNonIndexableKeys(nonIndexableKeysFromXml);
            return nonIndexableKeysFromXml;
        }
    };
    SparseArray<Bitmap> mUserIcons = new SparseArray<>();
    private int mRemovingUserId = -1;
    private boolean mShouldUpdateUserList = true;
    private final Object mUserLock = new Object();
    private EditUserInfoController mEditUserInfoController = new EditUserInfoController("com.android.settings.files");
    private final AtomicBoolean mGuestCreationScheduled = new AtomicBoolean();
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler() { // from class: com.android.settings.users.UserSettings.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                UserSettings.this.updateUserList();
            } else if (i != 2) {
            } else {
                UserSettings.this.onUserCreated(message.arg1);
            }
        }
    };
    private BroadcastReceiver mUserChangeReceiver = new BroadcastReceiver() { // from class: com.android.settings.users.UserSettings.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra;
            if (intent.getAction().equals("android.intent.action.USER_REMOVED")) {
                UserSettings.this.mRemovingUserId = -1;
            } else if (intent.getAction().equals("android.intent.action.USER_INFO_CHANGED") && (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1)) != -1) {
                UserSettings.this.mUserIcons.remove(intExtra);
            }
            UserSettings.this.mHandler.sendEmptyMessage(1);
        }
    };

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        if (i != 1) {
            if (i == 2) {
                return 595;
            }
            switch (i) {
                case 5:
                    return 594;
                case 6:
                    return 598;
                case 7:
                    return 599;
                case 8:
                case 12:
                    return 600;
                case 9:
                case 10:
                case 11:
                    return 601;
                default:
                    return 0;
            }
        }
        return 591;
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 96;
    }

    static {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.USER_REMOVED");
        USER_REMOVED_INTENT_FILTER = intentFilter;
        intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R.string.multiple_users_main_switch_title));
        switchBar.show();
        this.mSwitchBarController = new MultiUserSwitchBarController(settingsActivity, new MainSwitchBarController(switchBar), this);
        getSettingsLifecycle().addObserver(this.mSwitchBarController);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.user_settings);
        FragmentActivity activity = getActivity();
        if (!WizardManagerHelper.isDeviceProvisioned(activity)) {
            activity.finish();
            return;
        }
        this.mGuestUserAutoCreated = getPrefContext().getResources().getBoolean(17891567);
        this.mAddUserWhenLockedPreferenceController = new AddUserWhenLockedPreferenceController(activity, "user_settings_add_users_when_locked");
        this.mMultiUserTopIntroPreferenceController = new MultiUserTopIntroPreferenceController(activity, "multiuser_top_intro");
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mAddUserWhenLockedPreferenceController.displayPreference(preferenceScreen);
        this.mMultiUserTopIntroPreferenceController.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(this.mAddUserWhenLockedPreferenceController.getPreferenceKey()).setOnPreferenceChangeListener(this.mAddUserWhenLockedPreferenceController);
        if (bundle != null) {
            if (bundle.containsKey("removing_user")) {
                this.mRemovingUserId = bundle.getInt("removing_user");
            }
            this.mEditUserInfoController.onRestoreInstanceState(bundle);
        }
        this.mUserCaps = UserCapabilities.create(activity);
        this.mUserManager = (UserManager) activity.getSystemService("user");
        if (!this.mUserCaps.mEnabled) {
            return;
        }
        int myUserId = UserHandle.myUserId();
        this.mUserListCategory = (PreferenceGroup) findPreference("user_list");
        UserPreference userPreference = new UserPreference(getPrefContext(), null, myUserId);
        this.mMePreference = userPreference;
        userPreference.setKey("user_me");
        this.mMePreference.setOnPreferenceClickListener(this);
        if (this.mUserCaps.mIsAdmin) {
            this.mMePreference.setSummary(R.string.user_admin);
        }
        RestrictedPreference restrictedPreference = (RestrictedPreference) findPreference("guest_add");
        this.mAddGuest = restrictedPreference;
        restrictedPreference.setOnPreferenceClickListener(this);
        RestrictedPreference restrictedPreference2 = (RestrictedPreference) findPreference("user_add");
        this.mAddUser = restrictedPreference2;
        if (!this.mUserCaps.mCanAddRestrictedProfile) {
            restrictedPreference2.setTitle(R.string.user_add_user_menu);
        }
        this.mAddUser.setOnPreferenceClickListener(this);
        activity.registerReceiverAsUser(this.mUserChangeReceiver, UserHandle.ALL, USER_REMOVED_INTENT_FILTER, null, this.mHandler);
        updateUI();
        this.mShouldUpdateUserList = false;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.mUserCaps.mEnabled) {
            return;
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        AddUserWhenLockedPreferenceController addUserWhenLockedPreferenceController = this.mAddUserWhenLockedPreferenceController;
        addUserWhenLockedPreferenceController.updateState(preferenceScreen.findPreference(addUserWhenLockedPreferenceController.getPreferenceKey()));
        if (!this.mShouldUpdateUserList) {
            return;
        }
        updateUI();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        this.mShouldUpdateUserList = true;
        super.onPause();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        UserCapabilities userCapabilities = this.mUserCaps;
        if (userCapabilities == null || !userCapabilities.mEnabled) {
            return;
        }
        getActivity().unregisterReceiver(this.mUserChangeReceiver);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        this.mEditUserInfoController.onSaveInstanceState(bundle);
        bundle.putInt("removing_user", this.mRemovingUserId);
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void startActivityForResult(Intent intent, int i) {
        this.mEditUserInfoController.startingActivityForResult();
        super.startActivityForResult(intent, i);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (!this.mUserCaps.mIsAdmin && canSwitchUserNow() && (!isCurrentUserGuest() || !this.mGuestUserAutoCreated)) {
            MenuItem add = menu.add(0, 1, 0, getResources().getString(R.string.user_remove_user_menu, this.mUserManager.getUserName()));
            add.setShowAsAction(0);
            RestrictedLockUtilsInternal.setMenuItemAsDisabledByAdmin(getContext(), add, RestrictedLockUtilsInternal.checkIfRestrictionEnforced(getContext(), "no_remove_user", UserHandle.myUserId()));
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 1) {
            onRemoveUserClicked(UserHandle.myUserId());
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.android.settings.users.MultiUserSwitchBarController.OnMultiUserSwitchChangedListener
    public void onMultiUserSwitchChanged(boolean z) {
        updateUI();
    }

    private void updateUI() {
        this.mUserCaps.updateAddUserCapabilities(getActivity());
        loadProfile();
        updateUserList();
    }

    private void loadProfile() {
        if (isCurrentUserGuest()) {
            this.mMePreference.setIcon(getEncircledDefaultIcon());
            this.mMePreference.setTitle(this.mGuestUserAutoCreated ? R$string.guest_reset_guest : R.string.user_exit_guest_title);
            this.mMePreference.setSelectable(true);
            this.mMePreference.setEnabled(canSwitchUserNow());
            return;
        }
        new AsyncTask<Void, Void, String>() { // from class: com.android.settings.users.UserSettings.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(String str) {
                UserSettings.this.finishLoadProfile(str);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public String doInBackground(Void... voidArr) {
                UserInfo userInfo = UserSettings.this.mUserManager.getUserInfo(UserHandle.myUserId());
                String str = userInfo.iconPath;
                if (str == null || str.equals("")) {
                    UserSettings.copyMeProfilePhoto(UserSettings.this.getActivity(), userInfo);
                }
                return userInfo.name;
            }
        }.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishLoadProfile(String str) {
        if (getActivity() == null) {
            return;
        }
        this.mMePreference.setTitle(getString(R.string.user_you, str));
        int myUserId = UserHandle.myUserId();
        Bitmap userIcon = this.mUserManager.getUserIcon(myUserId);
        if (userIcon == null) {
            return;
        }
        this.mMePreference.setIcon(encircle(userIcon));
        this.mUserIcons.put(myUserId, userIcon);
    }

    private boolean hasLockscreenSecurity() {
        return new LockPatternUtils(getActivity()).isSecure(UserHandle.myUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchChooseLockscreen() {
        Intent intent = new Intent("android.app.action.SET_NEW_PASSWORD");
        intent.putExtra("hide_insecure_options", true);
        startActivityForResult(intent, 10);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10) {
            if (i2 == 0 || !hasLockscreenSecurity()) {
                return;
            }
            addUserNow(2);
        } else if (this.mGuestUserAutoCreated && i == 11 && i2 == 100) {
            scheduleGuestCreation();
        } else {
            this.mEditUserInfoController.onActivityResult(i, i2, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddUserClicked(int i) {
        synchronized (this.mUserLock) {
            if (this.mRemovingUserId == -1 && !this.mAddingUser) {
                if (i == 1) {
                    showDialog(2);
                } else if (i == 2) {
                    if (hasLockscreenSecurity()) {
                        showDialog(11);
                    } else {
                        showDialog(7);
                    }
                }
            }
        }
    }

    private void onRemoveUserClicked(int i) {
        synchronized (this.mUserLock) {
            if (this.mRemovingUserId == -1 && !this.mAddingUser) {
                this.mRemovingUserId = i;
                showDialog(1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserCreated(int i) {
        hideUserCreatingDialog();
        if (getContext() == null) {
            return;
        }
        this.mAddingUser = false;
        openUserDetails(this.mUserManager.getUserInfo(i), true);
    }

    private void hideUserCreatingDialog() {
        UserCreatingDialog userCreatingDialog = this.mUserCreatingDialog;
        if (userCreatingDialog == null || !userCreatingDialog.isShowing()) {
            return;
        }
        this.mUserCreatingDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserCreationFailed() {
        Toast.makeText(getContext(), R$string.add_user_failed, 0).show();
        hideUserCreatingDialog();
    }

    private void openUserDetails(UserInfo userInfo, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userInfo.id);
        bundle.putBoolean("new_user", z);
        Context context = getContext();
        SubSettingLauncher sourceMetricsCategory = new SubSettingLauncher(context).setDestination(UserDetailsSettings.class.getName()).setArguments(bundle).setTitleText(getUserName(context, userInfo)).setSourceMetricsCategory(getMetricsCategory());
        if (this.mGuestUserAutoCreated && userInfo.isGuest()) {
            sourceMetricsCategory.setResultListener(this, 11);
        }
        sourceMetricsCategory.launch();
    }

    @Override // com.android.settings.SettingsPreferenceFragment
    public void onDialogShowing() {
        super.onDialogShowing();
        setOnDismissListener(this);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        int i2;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return null;
        }
        if (i == 1) {
            return UserDialogs.createRemoveDialog(getActivity(), this.mRemovingUserId, new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    UserSettings.this.removeUserNow();
                }
            });
        }
        if (i != 2) {
            switch (i) {
                case 5:
                    return new AlertDialog.Builder(activity).setMessage(R.string.user_cannot_manage_message).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
                case 6:
                    ArrayList arrayList = new ArrayList();
                    HashMap hashMap = new HashMap();
                    hashMap.put("title", getString(R$string.user_add_user_item_title));
                    hashMap.put("summary", getString(R$string.user_add_user_item_summary));
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("title", getString(R$string.user_add_profile_item_title));
                    hashMap2.put("summary", getString(R$string.user_add_profile_item_summary));
                    arrayList.add(hashMap);
                    arrayList.add(hashMap2);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    SimpleAdapter simpleAdapter = new SimpleAdapter(builder.getContext(), arrayList, R.layout.two_line_list_item, new String[]{"title", "summary"}, new int[]{R.id.title, R.id.summary});
                    builder.setTitle(R$string.user_add_user_type_title);
                    builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings.6
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            UserSettings.this.onAddUserClicked(i3 == 0 ? 1 : 2);
                        }
                    });
                    return builder.create();
                case 7:
                    return new AlertDialog.Builder(activity).setMessage(R$string.user_need_lock_message).setPositiveButton(R$string.user_set_lock_button, new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings.7
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            UserSettings.this.launchChooseLockscreen();
                        }
                    }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
                case 8:
                    return new AlertDialog.Builder(activity).setTitle(R.string.user_exit_guest_confirm_title).setMessage(R.string.user_exit_guest_confirm_message).setPositiveButton(R.string.user_exit_guest_dialog_remove, new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings.8
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            UserSettings.this.exitGuest();
                        }
                    }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
                case 9:
                    return buildEditCurrentUserDialog();
                case 10:
                    synchronized (this.mUserLock) {
                        this.mPendingUserName = getString(R$string.user_new_user_name);
                        this.mPendingUserIcon = null;
                    }
                    return buildAddUserDialog(1);
                case 11:
                    synchronized (this.mUserLock) {
                        this.mPendingUserName = getString(R$string.user_new_profile_name);
                        this.mPendingUserIcon = null;
                    }
                    return buildAddUserDialog(2);
                case 12:
                    return UserDialogs.createResetGuestDialog(getActivity(), new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i3) {
                            UserSettings.this.lambda$onCreateDialog$0(dialogInterface, i3);
                        }
                    });
                default:
                    return null;
            }
        }
        final SharedPreferences preferences = getActivity().getPreferences(0);
        final boolean z = preferences.getBoolean("key_add_user_long_message_displayed", false);
        if (z) {
            i2 = R$string.user_add_user_message_short;
        } else {
            i2 = R$string.user_add_user_message_long;
        }
        return new AlertDialog.Builder(activity).setTitle(R$string.user_add_user_title).setMessage(i2).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.settings.users.UserSettings.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                UserSettings.this.showDialog(10);
                if (!z) {
                    preferences.edit().putBoolean("key_add_user_long_message_displayed", true).apply();
                }
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        resetGuest();
    }

    private Dialog buildEditCurrentUserDialog() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return null;
        }
        final UserInfo userInfo = this.mUserManager.getUserInfo(Process.myUserHandle().getIdentifier());
        final Drawable userIcon = Utils.getUserIcon(activity, this.mUserManager, userInfo);
        return this.mEditUserInfoController.createDialog(activity, new UserSettings$$ExternalSyntheticLambda1(this), userIcon, userInfo.name, getString(R$string.profile_info_settings_title), new BiConsumer() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                UserSettings.this.lambda$buildEditCurrentUserDialog$2(userIcon, userInfo, (String) obj, (Drawable) obj2);
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildEditCurrentUserDialog$2(Drawable drawable, final UserInfo userInfo, String str, final Drawable drawable2) {
        if (drawable2 != drawable) {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UserSettings.this.lambda$buildEditCurrentUserDialog$1(userInfo, drawable2);
                }
            });
            this.mMePreference.setIcon(drawable2);
        }
        if (TextUtils.isEmpty(str) || str.equals(userInfo.name)) {
            return;
        }
        this.mMePreference.setTitle(str);
        this.mUserManager.setUserName(userInfo.id, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildEditCurrentUserDialog$1(UserInfo userInfo, Drawable drawable) {
        this.mUserManager.setUserIcon(userInfo.id, UserIcons.convertToBitmap(drawable));
    }

    private Dialog buildAddUserDialog(final int i) {
        int i2;
        Dialog createDialog;
        synchronized (this.mUserLock) {
            EditUserInfoController editUserInfoController = this.mEditUserInfoController;
            FragmentActivity activity = getActivity();
            UserSettings$$ExternalSyntheticLambda1 userSettings$$ExternalSyntheticLambda1 = new UserSettings$$ExternalSyntheticLambda1(this);
            String charSequence = this.mPendingUserName.toString();
            if (i == 1) {
                i2 = R$string.user_info_settings_title;
            } else {
                i2 = R$string.profile_info_settings_title;
            }
            createDialog = editUserInfoController.createDialog(activity, userSettings$$ExternalSyntheticLambda1, null, charSequence, getString(i2), new BiConsumer() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda5
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    UserSettings.this.lambda$buildAddUserDialog$3(i, (String) obj, (Drawable) obj2);
                }
            }, new Runnable() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UserSettings.this.lambda$buildAddUserDialog$4();
                }
            });
        }
        return createDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildAddUserDialog$3(int i, String str, Drawable drawable) {
        this.mPendingUserIcon = drawable;
        this.mPendingUserName = str;
        addUserNow(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildAddUserDialog$4() {
        synchronized (this.mUserLock) {
            this.mPendingUserIcon = null;
            this.mPendingUserName = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUserNow() {
        if (this.mRemovingUserId == UserHandle.myUserId()) {
            removeThisUser();
        } else {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settings.users.UserSettings.9
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (UserSettings.this.mUserLock) {
                        UserSettings.this.mUserManager.removeUser(UserSettings.this.mRemovingUserId);
                        UserSettings.this.mHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private void removeThisUser() {
        if (!canSwitchUserNow()) {
            Log.w("UserSettings", "Cannot remove current user when switching is disabled");
            return;
        }
        try {
            ((UserManager) getContext().getSystemService(UserManager.class)).removeUserOrSetEphemeral(UserHandle.myUserId(), false);
            ActivityManager.getService().switchUser(0);
        } catch (RemoteException unused) {
            Log.e("UserSettings", "Unable to remove self user");
        }
    }

    private void addUserNow(int i) {
        String charSequence;
        synchronized (this.mUserLock) {
            this.mAddingUser = true;
            if (i == 1) {
                CharSequence charSequence2 = this.mPendingUserName;
                charSequence = charSequence2 != null ? charSequence2.toString() : getString(R.string.user_new_user_name);
            } else {
                CharSequence charSequence3 = this.mPendingUserName;
                charSequence = charSequence3 != null ? charSequence3.toString() : getString(R.string.user_new_profile_name);
            }
            this.mAddingUserName = charSequence;
        }
        UserCreatingDialog userCreatingDialog = new UserCreatingDialog(getActivity());
        this.mUserCreatingDialog = userCreatingDialog;
        userCreatingDialog.show();
        ThreadUtils.postOnBackgroundThread(new AnonymousClass10(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.settings.users.UserSettings$10  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass10 implements Runnable {
        final /* synthetic */ int val$userType;

        AnonymousClass10(int i) {
            this.val$userType = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            String str;
            synchronized (UserSettings.this.mUserLock) {
                str = UserSettings.this.mAddingUserName;
            }
            UserInfo createUser = this.val$userType == 1 ? UserSettings.this.mUserManager.createUser(str, 0) : UserSettings.this.mUserManager.createRestrictedProfile(str);
            synchronized (UserSettings.this.mUserLock) {
                if (createUser == null) {
                    UserSettings.this.mAddingUser = false;
                    UserSettings.this.mPendingUserIcon = null;
                    UserSettings.this.mPendingUserName = null;
                    ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settings.users.UserSettings$10$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            UserSettings.AnonymousClass10.this.lambda$run$0();
                        }
                    });
                    return;
                }
                Drawable drawable = UserSettings.this.mPendingUserIcon;
                if (drawable == null) {
                    drawable = UserIcons.getDefaultUserIcon(UserSettings.this.getResources(), createUser.id, false);
                }
                UserSettings.this.mUserManager.setUserIcon(createUser.id, UserIcons.convertToBitmap(drawable));
                if (this.val$userType == 1) {
                    UserSettings.this.mHandler.sendEmptyMessage(1);
                }
                UserSettings.this.mHandler.sendMessage(UserSettings.this.mHandler.obtainMessage(2, createUser.id, createUser.serialNumber));
                UserSettings.this.mPendingUserIcon = null;
                UserSettings.this.mPendingUserName = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            UserSettings.this.onUserCreationFailed();
        }
    }

    void exitGuest() {
        if (!isCurrentUserGuest()) {
            return;
        }
        this.mMetricsFeatureProvider.action(getActivity(), 1763, new Pair[0]);
        removeThisUser();
    }

    void resetGuest() {
        if (!isCurrentUserGuest()) {
            return;
        }
        int myUserId = UserHandle.myUserId();
        if (!this.mUserManager.markGuestForDeletion(myUserId)) {
            Log.w("UserSettings", "Couldn't mark the guest for deletion for user " + myUserId);
            return;
        }
        exitGuest();
        scheduleGuestCreation();
    }

    void scheduleGuestCreation() {
        if (this.mGuestCreationScheduled.compareAndSet(false, true)) {
            this.mHandler.sendEmptyMessage(1);
            this.mExecutor.execute(new Runnable() { // from class: com.android.settings.users.UserSettings$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UserSettings.this.lambda$scheduleGuestCreation$5();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGuestCreation$5() {
        UserInfo createGuest = this.mUserManager.createGuest(getContext(), getString(R$string.user_guest));
        this.mGuestCreationScheduled.set(false);
        if (createGuest == null) {
            Log.e("UserSettings", "Unable to automatically recreate guest user");
        }
        this.mHandler.sendEmptyMessage(1);
    }

    void updateUserList() {
        Preference preference;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        List<UserInfo> aliveUsers = this.mUserManager.getAliveUsers();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(this.mMePreference);
        boolean z = this.mUserCaps.mIsAdmin || (canSwitchUserNow() && !this.mUserCaps.mDisallowSwitchUser);
        for (UserInfo userInfo : aliveUsers) {
            if (userInfo.supportsSwitchToByUser()) {
                if (userInfo.id == UserHandle.myUserId()) {
                    preference = this.mMePreference;
                } else {
                    Context prefContext = getPrefContext();
                    UserPreference userPreference = new UserPreference(prefContext, null, userInfo.id);
                    userPreference.setTitle(getUserName(prefContext, userInfo));
                    arrayList2.add(userPreference);
                    userPreference.setOnPreferenceClickListener(this);
                    userPreference.setEnabled(z);
                    userPreference.setSelectable(true);
                    if (userInfo.isGuest()) {
                        userPreference.setIcon(getEncircledDefaultIcon());
                        userPreference.setKey("user_guest");
                        if (this.mUserCaps.mDisallowSwitchUser) {
                            userPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.getDeviceOwner(activity));
                        } else {
                            userPreference.setDisabledByAdmin(null);
                        }
                    } else {
                        userPreference.setKey("id=" + userInfo.id);
                        if (userInfo.isAdmin()) {
                            userPreference.setSummary(R.string.user_admin);
                        }
                    }
                    preference = userPreference;
                }
                if (preference != null) {
                    if (userInfo.id != UserHandle.myUserId() && !userInfo.isGuest() && !userInfo.isInitialized()) {
                        if (userInfo.isRestricted()) {
                            preference.setSummary(R.string.user_summary_restricted_not_set_up);
                        } else {
                            preference.setSummary(R.string.user_summary_not_set_up);
                            preference.setEnabled(!this.mUserCaps.mDisallowSwitchUser && canSwitchUserNow());
                        }
                    } else if (userInfo.isRestricted()) {
                        preference.setSummary(R.string.user_summary_restricted_profile);
                    }
                    if (userInfo.iconPath != null) {
                        if (this.mUserIcons.get(userInfo.id) == null) {
                            arrayList.add(Integer.valueOf(userInfo.id));
                            preference.setIcon(getEncircledDefaultIcon());
                        } else {
                            setPhotoId(preference, userInfo);
                        }
                    } else {
                        preference.setIcon(getEncircledDefaultIcon());
                    }
                }
            }
        }
        if (this.mAddingUser) {
            UserPreference userPreference2 = new UserPreference(getPrefContext(), null, -10);
            userPreference2.setEnabled(false);
            userPreference2.setTitle(this.mAddingUserName);
            userPreference2.setIcon(getEncircledDefaultIcon());
            arrayList2.add(userPreference2);
        }
        Collections.sort(arrayList2, UserPreference.SERIAL_NUMBER_COMPARATOR);
        getActivity().invalidateOptionsMenu();
        if (arrayList.size() > 0) {
            loadIconsAsync(arrayList);
        }
        if (this.mUserCaps.mCanAddRestrictedProfile) {
            this.mUserListCategory.setTitle(R.string.user_list_title);
        } else {
            this.mUserListCategory.setTitle((CharSequence) null);
        }
        this.mUserListCategory.removeAll();
        this.mAddUserWhenLockedPreferenceController.updateState(getPreferenceScreen().findPreference(this.mAddUserWhenLockedPreferenceController.getPreferenceKey()));
        this.mMultiUserTopIntroPreferenceController.updateState(getPreferenceScreen().findPreference(this.mMultiUserTopIntroPreferenceController.getPreferenceKey()));
        this.mUserListCategory.setVisible(this.mUserCaps.mUserSwitcherEnabled);
        updateAddGuest(activity, aliveUsers.stream().anyMatch(UserSettings$$ExternalSyntheticLambda7.INSTANCE));
        updateAddUser(activity);
        if (!this.mUserCaps.mUserSwitcherEnabled) {
            return;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            UserPreference userPreference3 = (UserPreference) it.next();
            userPreference3.setOrder(Integer.MAX_VALUE);
            this.mUserListCategory.addPreference(userPreference3);
        }
    }

    private boolean isCurrentUserGuest() {
        return this.mUserCaps.mIsGuest;
    }

    private boolean canSwitchUserNow() {
        return this.mUserManager.getUserSwitchability() == 0;
    }

    private void updateAddGuest(Context context, boolean z) {
        if (!z && this.mUserCaps.mCanAddGuest && WizardManagerHelper.isDeviceProvisioned(context) && this.mUserCaps.mUserSwitcherEnabled) {
            this.mAddGuest.setVisible(true);
            this.mAddGuest.setIcon(getEncircledDefaultIcon());
            this.mAddGuest.setSelectable(true);
            if (this.mGuestUserAutoCreated && this.mGuestCreationScheduled.get()) {
                this.mAddGuest.setTitle(R$string.user_guest);
                this.mAddGuest.setSummary(R.string.guest_resetting);
                this.mAddGuest.setEnabled(false);
                return;
            }
            this.mAddGuest.setTitle(R$string.guest_new_guest);
            this.mAddGuest.setEnabled(canSwitchUserNow());
            return;
        }
        this.mAddGuest.setVisible(false);
    }

    private void updateAddUser(Context context) {
        UserCapabilities userCapabilities = this.mUserCaps;
        if ((userCapabilities.mCanAddUser || userCapabilities.mDisallowAddUserSetByAdmin) && WizardManagerHelper.isDeviceProvisioned(context) && this.mUserCaps.mUserSwitcherEnabled) {
            this.mAddUser.setVisible(true);
            this.mAddUser.setSelectable(true);
            boolean canAddMoreUsers = this.mUserManager.canAddMoreUsers();
            this.mAddUser.setEnabled(canAddMoreUsers && !this.mAddingUser && canSwitchUserNow());
            RestrictedLockUtils.EnforcedAdmin enforcedAdmin = null;
            if (!canAddMoreUsers) {
                this.mAddUser.setSummary(getString(R.string.user_add_max_count, Integer.valueOf(getRealUsersCount())));
            } else {
                this.mAddUser.setSummary((CharSequence) null);
            }
            if (!this.mAddUser.isEnabled()) {
                return;
            }
            RestrictedPreference restrictedPreference = this.mAddUser;
            UserCapabilities userCapabilities2 = this.mUserCaps;
            if (userCapabilities2.mDisallowAddUser) {
                enforcedAdmin = userCapabilities2.mEnforcedAdmin;
            }
            restrictedPreference.setDisabledByAdmin(enforcedAdmin);
            return;
        }
        this.mAddUser.setVisible(false);
    }

    int getRealUsersCount() {
        return (int) this.mUserManager.getUsers().stream().filter(UserSettings$$ExternalSyntheticLambda8.INSTANCE).count();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getRealUsersCount$6(UserInfo userInfo) {
        return !userInfo.isGuest() && !userInfo.isProfile();
    }

    private void loadIconsAsync(List<Integer> list) {
        new AsyncTask<List<Integer>, Void, Void>() { // from class: com.android.settings.users.UserSettings.11
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void r1) {
                UserSettings.this.updateUserList();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(List<Integer>... listArr) {
                for (Integer num : listArr[0]) {
                    int intValue = num.intValue();
                    Bitmap userIcon = UserSettings.this.mUserManager.getUserIcon(intValue);
                    if (userIcon == null) {
                        userIcon = UserSettings.getDefaultUserIconAsBitmap(UserSettings.this.getContext().getResources(), intValue);
                    }
                    UserSettings.this.mUserIcons.append(intValue, userIcon);
                }
                return null;
            }
        }.execute(list);
    }

    private Drawable getEncircledDefaultIcon() {
        if (this.mDefaultIconDrawable == null) {
            this.mDefaultIconDrawable = encircle(getDefaultUserIconAsBitmap(getContext().getResources(), -10000));
        }
        return this.mDefaultIconDrawable;
    }

    private void setPhotoId(Preference preference, UserInfo userInfo) {
        Bitmap bitmap = this.mUserIcons.get(userInfo.id);
        if (bitmap != null) {
            preference.setIcon(encircle(bitmap));
        }
    }

    public static String getUserName(Context context, UserInfo userInfo) {
        if (userInfo.isGuest()) {
            return context.getString(R.string.user_guest);
        }
        return userInfo.name;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mMePreference) {
            if (isCurrentUserGuest()) {
                if (this.mGuestUserAutoCreated) {
                    showDialog(12);
                } else {
                    showDialog(8);
                }
            } else {
                showDialog(9);
            }
            return true;
        } else if (preference instanceof UserPreference) {
            openUserDetails(this.mUserManager.getUserInfo(((UserPreference) preference).getUserId()), false);
            return true;
        } else if (preference == this.mAddUser) {
            if (this.mUserCaps.mCanAddRestrictedProfile) {
                showDialog(6);
            } else {
                onAddUserClicked(1);
            }
            return true;
        } else {
            RestrictedPreference restrictedPreference = this.mAddGuest;
            if (preference != restrictedPreference) {
                return false;
            }
            restrictedPreference.setEnabled(false);
            this.mMetricsFeatureProvider.action(getActivity(), 1764, new Pair[0]);
            UserInfo createGuest = this.mUserManager.createGuest(getContext(), getString(R$string.user_guest));
            if (createGuest == null) {
                Toast.makeText(getContext(), R$string.add_user_failed, 0).show();
                return true;
            }
            openUserDetails(createGuest, true);
            return true;
        }
    }

    private Drawable encircle(Bitmap bitmap) {
        return CircleFramedDrawable.getInstance(getActivity(), bitmap);
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        synchronized (this.mUserLock) {
            this.mRemovingUserId = -1;
            updateUserList();
        }
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_url_users;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap getDefaultUserIconAsBitmap(Resources resources, int i) {
        Bitmap bitmap = sDarkDefaultUserBitmapCache.get(i);
        if (bitmap == null) {
            Bitmap convertToBitmap = UserIcons.convertToBitmap(UserIcons.getDefaultUserIcon(resources, i, false));
            sDarkDefaultUserBitmapCache.put(i, convertToBitmap);
            return convertToBitmap;
        }
        return bitmap;
    }

    static boolean assignDefaultPhoto(Context context, int i) {
        if (context == null) {
            return false;
        }
        ((UserManager) context.getSystemService("user")).setUserIcon(i, getDefaultUserIconAsBitmap(context.getResources(), i));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void copyMeProfilePhoto(Context context, UserInfo userInfo) {
        Uri uri = ContactsContract.Profile.CONTENT_URI;
        int myUserId = userInfo != null ? userInfo.id : UserHandle.myUserId();
        InputStream openContactPhotoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri, true);
        if (openContactPhotoInputStream == null) {
            assignDefaultPhoto(context, myUserId);
            return;
        }
        ((UserManager) context.getSystemService("user")).setUserIcon(myUserId, BitmapFactory.decodeStream(openContactPhotoInputStream));
        try {
            openContactPhotoInputStream.close();
        } catch (IOException unused) {
        }
    }
}
