package com.android.settings.accounts;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.password.ChooseLockSettingsHelper;
import java.io.IOException;
/* loaded from: classes.dex */
public class AddAccountSettings extends Activity {
    private PendingIntent mPendingIntent;
    private UserHandle mUserHandle;
    private final AccountManagerCallback<Bundle> mCallback = new AccountManagerCallback<Bundle>() { // from class: com.android.settings.accounts.AddAccountSettings.1
        @Override // android.accounts.AccountManagerCallback
        public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
            boolean z = true;
            try {
                try {
                    try {
                        try {
                            Bundle result = accountManagerFuture.getResult();
                            Intent intent = (Intent) result.get("intent");
                            if (intent != null) {
                                z = false;
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("pendingIntent", AddAccountSettings.this.mPendingIntent);
                                bundle.putBoolean("hasMultipleUsers", Utils.hasMultipleUsers(AddAccountSettings.this));
                                bundle.putParcelable("android.intent.extra.USER", AddAccountSettings.this.mUserHandle);
                                intent.putExtras(bundle).addFlags(268435456).addFlags(32768);
                                AddAccountSettings addAccountSettings = AddAccountSettings.this;
                                addAccountSettings.startActivityForResultAsUser(intent, 2, addAccountSettings.mUserHandle);
                            } else {
                                AddAccountSettings.this.setResult(-1);
                                if (AddAccountSettings.this.mPendingIntent != null) {
                                    AddAccountSettings.this.mPendingIntent.cancel();
                                    AddAccountSettings.this.mPendingIntent = null;
                                }
                            }
                            if (Log.isLoggable("AddAccountSettings", 2)) {
                                Log.v("AddAccountSettings", "account added: " + result);
                            }
                            if (!z) {
                                return;
                            }
                        } catch (OperationCanceledException unused) {
                            if (Log.isLoggable("AddAccountSettings", 2)) {
                                Log.v("AddAccountSettings", "addAccount was canceled");
                            }
                            if (!z) {
                                return;
                            }
                        }
                    } catch (IOException e) {
                        if (Log.isLoggable("AddAccountSettings", 2)) {
                            Log.v("AddAccountSettings", "addAccount failed: " + e);
                        }
                        if (!z) {
                            return;
                        }
                    }
                } catch (AuthenticatorException e2) {
                    if (Log.isLoggable("AddAccountSettings", 2)) {
                        Log.v("AddAccountSettings", "addAccount failed: " + e2);
                    }
                    if (!z) {
                        return;
                    }
                }
                AddAccountSettings.this.finish();
            } catch (Throwable th) {
                if (z) {
                    AddAccountSettings.this.finish();
                }
                throw th;
            }
        }
    };
    private boolean mAddAccountCalled = false;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mAddAccountCalled = bundle.getBoolean("AddAccountCalled");
            if (Log.isLoggable("AddAccountSettings", 2)) {
                Log.v("AddAccountSettings", "restored");
            }
        }
        UserManager userManager = (UserManager) getSystemService("user");
        UserHandle secureTargetUser = Utils.getSecureTargetUser(getActivityToken(), userManager, null, getIntent().getExtras());
        this.mUserHandle = secureTargetUser;
        if (userManager.hasUserRestriction("no_modify_accounts", secureTargetUser)) {
            Toast.makeText(this, R.string.user_cannot_add_accounts_message, 1).show();
            finish();
        } else if (this.mAddAccountCalled) {
            finish();
        } else if (Utils.startQuietModeDialogIfNecessary(this, userManager, this.mUserHandle.getIdentifier())) {
            finish();
        } else if (userManager.isUserUnlocked(this.mUserHandle)) {
            requestChooseAccount();
        } else if (new ChooseLockSettingsHelper.Builder(this).setRequestCode(3).setTitle(getString(R.string.unlock_set_unlock_launch_picker_title)).setUserId(this.mUserHandle.getIdentifier()).show()) {
        } else {
            requestChooseAccount();
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            if (i2 == 0) {
                if (intent != null) {
                    startActivityAsUser(intent, this.mUserHandle);
                }
                setResult(i2);
                finish();
                return;
            }
            addAccount(intent.getStringExtra("selected_account"));
        } else if (i != 2) {
            if (i != 3) {
                return;
            }
            if (i2 == -1) {
                requestChooseAccount();
            } else {
                finish();
            }
        } else {
            setResult(i2);
            PendingIntent pendingIntent = this.mPendingIntent;
            if (pendingIntent != null) {
                pendingIntent.cancel();
                this.mPendingIntent = null;
            }
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("AddAccountCalled", this.mAddAccountCalled);
        if (Log.isLoggable("AddAccountSettings", 2)) {
            Log.v("AddAccountSettings", "saved");
        }
    }

    private void requestChooseAccount() {
        String[] stringArrayExtra = getIntent().getStringArrayExtra("authorities");
        String[] stringArrayExtra2 = getIntent().getStringArrayExtra("account_types");
        Intent intent = new Intent(this, Settings.ChooseAccountActivity.class);
        if (stringArrayExtra != null) {
            intent.putExtra("authorities", stringArrayExtra);
        }
        if (stringArrayExtra2 != null) {
            intent.putExtra("account_types", stringArrayExtra2);
        }
        intent.putExtra("android.intent.extra.USER", this.mUserHandle);
        startActivityForResult(intent, 1);
    }

    private void addAccount(String str) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("SHOULDN'T RESOLVE!", "SHOULDN'T RESOLVE!"));
        intent.setAction("SHOULDN'T RESOLVE!");
        intent.addCategory("SHOULDN'T RESOLVE!");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, intent, 67108864);
        this.mPendingIntent = broadcast;
        bundle.putParcelable("pendingIntent", broadcast);
        bundle.putBoolean("hasMultipleUsers", Utils.hasMultipleUsers(this));
        AccountManager.get(this).addAccountAsUser(str, null, null, bundle, null, this.mCallback, null, this.mUserHandle);
        this.mAddAccountCalled = true;
    }
}