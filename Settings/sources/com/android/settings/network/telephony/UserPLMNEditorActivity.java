package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
/* loaded from: classes.dex */
public class UserPLMNEditorActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, TextWatcher {
    private IntentFilter mIntentFilter;
    private EditText mNWIDText;
    private String mNoSet = null;
    private boolean mAirplaneModeOn = false;
    private Preference mNWIDPref = null;
    private EditTextPreference mPRIpref = null;
    private ListPreference mNWMPref = null;
    private AlertDialog mNWIDDialog = null;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.UserPLMNEditorActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                UserPLMNEditorActivity.this.mAirplaneModeOn = intent.getBooleanExtra("state", false);
                UserPLMNEditorActivity.this.setScreenEnabled();
            }
        }
    };
    private DialogInterface.OnClickListener mNWIDPrefListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.network.telephony.UserPLMNEditorActivity.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -1) {
                UserPLMNEditorActivity userPLMNEditorActivity = UserPLMNEditorActivity.this;
                String genText = userPLMNEditorActivity.genText(userPLMNEditorActivity.mNWIDText.getText().toString());
                Log.d("UserPLMNEditorActivity", "input network id is " + genText);
                UserPLMNEditorActivity.this.mNWIDPref.setSummary(genText);
                UserPLMNEditorActivity.this.mNWMPref.setEntries(UserPLMNEditorActivity.this.getResources().getTextArray(UserPLMNEditorActivity.this.selectNetworkChoices(genText)));
            }
        }
    };

    public static int convertApMode2EF(int i) {
        if (i == 3) {
            return 13;
        }
        if (i == 2) {
            return 8;
        }
        return i == 1 ? 4 : 1;
    }

    public static int convertEFMode2Ap(int i) {
        if (i == 13) {
            return 3;
        }
        if (i == 4) {
            return 1;
        }
        return i == 8 ? 2 : 0;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.uplmn_editor);
        this.mNoSet = getResources().getString(R.string.voicemail_number_not_set);
        this.mNWIDPref = findPreference("network_id_key");
        this.mPRIpref = (EditTextPreference) findPreference("priority_key");
        this.mNWMPref = (ListPreference) findPreference("network_mode_key");
        this.mPRIpref.setOnPreferenceChangeListener(this);
        this.mNWMPref.setOnPreferenceChangeListener(this);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        this.mIntentFilter = intentFilter;
        registerReceiver(this.mReceiver, intentFilter);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        displayNetworkInfo(getIntent());
        boolean z = false;
        if (Settings.System.getInt(getContentResolver(), "airplane_mode_on", 0) == 1) {
            z = true;
        }
        this.mAirplaneModeOn = z;
        setScreenEnabled();
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        String obj2 = obj.toString();
        EditTextPreference editTextPreference = this.mPRIpref;
        if (preference == editTextPreference) {
            editTextPreference.setSummary(genText(obj2));
            return true;
        }
        ListPreference listPreference = this.mNWMPref;
        if (preference != listPreference) {
            return true;
        }
        listPreference.setValue(obj2);
        this.mNWMPref.setSummary(getResources().getStringArray(selectNetworkChoices(this.mNWIDPref.getSummary().toString()))[Integer.parseInt(obj2)]);
        return true;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!getIntent().getBooleanExtra("uplmn_add", false)) {
            menu.add(0, 1, 0, 17040099);
        }
        menu.add(0, 2, 0, R.string.save);
        menu.add(0, 3, 0, 17039360);
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuOpened(int i, Menu menu) {
        super.onMenuOpened(i, menu);
        boolean z = false;
        boolean z2 = this.mNoSet.equals(this.mNWIDPref.getSummary()) || this.mNoSet.equals(this.mPRIpref.getSummary());
        if (menu != null) {
            menu.setGroupEnabled(0, !this.mAirplaneModeOn);
            if (getIntent().getBooleanExtra("uplmn_add", true)) {
                MenuItem item = menu.getItem(0);
                if (!this.mAirplaneModeOn && !z2) {
                    z = true;
                }
                item.setEnabled(z);
            } else {
                MenuItem item2 = menu.getItem(1);
                if (!this.mAirplaneModeOn && !z2) {
                    z = true;
                }
                item2.setEnabled(z);
            }
        }
        return true;
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            setRemovedNWInfo();
        } else if (itemId == 2) {
            setSavedNWInfo();
        } else if (itemId == 16908332) {
            finish();
            return true;
        }
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    private void setSavedNWInfo() {
        Intent intent = new Intent(this, UserPLMNListActivity.class);
        genNWInfoToIntent(intent);
        setResult(R$styleable.Constraint_layout_goneMarginRight, intent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0030, code lost:
        if (r2 > r1) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void genNWInfoToIntent(Intent intent) {
        int i;
        int intExtra = getIntent().getIntExtra("uplmn_size", 0);
        try {
            i = Integer.parseInt(String.valueOf(this.mPRIpref.getSummary()));
        } catch (NumberFormatException unused) {
            Log.d("UserPLMNEditorActivity", "parse value of basband error");
            i = 0;
        }
        if (!getIntent().getBooleanExtra("uplmn_add", false)) {
            if (i >= intExtra) {
                intExtra--;
            }
            intExtra = i;
        }
        intent.putExtra("uplmn_priority", intExtra);
        try {
            intent.putExtra("uplmn_service", convertApMode2EF(Integer.parseInt(String.valueOf(this.mNWMPref.getValue()))));
        } catch (NumberFormatException unused2) {
            intent.putExtra("uplmn_service", convertApMode2EF(0));
        }
        intent.putExtra("uplmn_code", this.mNWIDPref.getSummary());
    }

    private void setRemovedNWInfo() {
        Intent intent = new Intent(this, UserPLMNListActivity.class);
        genNWInfoToIntent(intent);
        setResult(R$styleable.Constraint_layout_goneMarginStart, intent);
    }

    private void displayNetworkInfo(Intent intent) {
        String stringExtra = intent.getStringExtra("uplmn_code");
        this.mNWIDPref.setSummary(genText(stringExtra));
        int i = 0;
        int intExtra = intent.getIntExtra("uplmn_priority", 0);
        this.mPRIpref.setSummary(String.valueOf(intExtra));
        this.mPRIpref.setText(String.valueOf(intExtra));
        int intExtra2 = intent.getIntExtra("uplmn_service", 0);
        Log.d("UserPLMNEditorActivity", "act = " + intExtra2);
        int convertEFMode2Ap = convertEFMode2Ap(intExtra2);
        if (convertEFMode2Ap >= 0 && convertEFMode2Ap <= 3) {
            i = convertEFMode2Ap;
        }
        this.mNWMPref.setEntries(getResources().getTextArray(selectNetworkChoices(stringExtra)));
        this.mNWMPref.setSummary(getResources().getStringArray(selectNetworkChoices(stringExtra))[i]);
        this.mNWMPref.setValue(String.valueOf(i));
    }

    public int selectNetworkChoices(String str) {
        Log.d("UserPLMNEditorActivity", "plmn = " + str);
        for (String str2 : getResources().getStringArray(R.array.uplmn_cu_mcc_mnc_values)) {
            if (str.equals(str2)) {
                return R.array.uplmn_prefer_network_mode_w_choices;
            }
        }
        return R.array.uplmn_prefer_network_mode_td_choices;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String genText(String str) {
        return (str == null || str.length() == 0) ? this.mNoSet : str;
    }

    public void buttonEnabled() {
        int length = this.mNWIDText.getText().toString().length();
        boolean z = length >= 5 && length <= 6;
        AlertDialog alertDialog = this.mNWIDDialog;
        if (alertDialog != null) {
            alertDialog.getButton(-1).setEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScreenEnabled() {
        getPreferenceScreen().setEnabled(!this.mAirplaneModeOn);
        invalidateOptionsMenu();
    }

    @Override // android.preference.PreferenceActivity
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.mNWIDPref) {
            removeDialog(0);
            showDialog(0);
            buttonEnabled();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        if (i == 0) {
            this.mNWIDText = new EditText(this);
            if (!this.mNoSet.equals(this.mNWIDPref.getSummary())) {
                this.mNWIDText.setText(this.mNWIDPref.getSummary());
            }
            this.mNWIDText.addTextChangedListener(this);
            this.mNWIDText.setInputType(2);
            AlertDialog create = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.network_id)).setView(this.mNWIDText).setPositiveButton(getResources().getString(17039370), this.mNWIDPrefListener).setNegativeButton(getResources().getString(17039360), (DialogInterface.OnClickListener) null).create();
            this.mNWIDDialog = create;
            create.getWindow().setSoftInputMode(4);
            return this.mNWIDDialog;
        }
        return null;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        buttonEnabled();
    }
}
