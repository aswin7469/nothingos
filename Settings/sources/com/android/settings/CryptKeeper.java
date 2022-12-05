package com.android.settings;

import android.app.Activity;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.storage.IStorageManager;
import android.provider.Settings;
import android.sysprop.VoldProperties;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.ImeAwareEditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import java.util.List;
/* loaded from: classes.dex */
public class CryptKeeper extends Activity implements TextView.OnEditorActionListener, View.OnKeyListener, View.OnTouchListener, TextWatcher {
    private AudioManager mAudioManager;
    private boolean mCorrupt;
    private boolean mEncryptionGoneBad;
    private LockPatternView mLockPatternView;
    private ImeAwareEditText mPasswordEntry;
    private StatusBarManager mStatusBar;
    private boolean mValidationComplete;
    private boolean mValidationRequested;
    PowerManager.WakeLock mWakeLock;
    private boolean mCooldown = false;
    private int mNotificationCountdown = 0;
    private int mReleaseWakeLockCountdown = 0;
    private int mStatusString = R.string.enter_password;
    private final Runnable mFakeUnlockAttemptRunnable = new Runnable() { // from class: com.android.settings.CryptKeeper.1
        @Override // java.lang.Runnable
        public void run() {
            CryptKeeper.this.handleBadAttempt(1);
        }
    };
    private final Runnable mClearPatternRunnable = new Runnable() { // from class: com.android.settings.CryptKeeper.2
        @Override // java.lang.Runnable
        public void run() {
            CryptKeeper.this.mLockPatternView.clearPattern();
        }
    };
    private final Handler mHandler = new Handler() { // from class: com.android.settings.CryptKeeper.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                CryptKeeper.this.updateProgress();
            } else if (i != 2) {
            } else {
                CryptKeeper.this.notifyUser();
            }
        }
    };
    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() { // from class: com.android.settings.CryptKeeper.6
        public void onPatternCellAdded(List<LockPatternView.Cell> list) {
        }

        public void onPatternCleared() {
        }

        public void onPatternStart() {
            CryptKeeper.this.mLockPatternView.removeCallbacks(CryptKeeper.this.mClearPatternRunnable);
        }

        public void onPatternDetected(List<LockPatternView.Cell> list) {
            CryptKeeper.this.mLockPatternView.setEnabled(false);
            if (list.size() >= 4) {
                new DecryptTask().execute(new String(LockPatternUtils.patternToByteArray(list)));
                return;
            }
            CryptKeeper cryptKeeper = CryptKeeper.this;
            cryptKeeper.fakeUnlockAttempt(cryptKeeper.mLockPatternView);
        }
    };

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.app.Activity
    public void onBackPressed() {
    }

    /* loaded from: classes.dex */
    private static class NonConfigurationInstanceState {
        final PowerManager.WakeLock wakelock;

        NonConfigurationInstanceState(PowerManager.WakeLock wakeLock) {
            this.wakelock = wakeLock;
        }
    }

    /* loaded from: classes.dex */
    private class DecryptTask extends AsyncTask<String, Void, Integer> {
        private DecryptTask() {
        }

        private void hide(int i) {
            View findViewById = CryptKeeper.this.findViewById(i);
            if (findViewById != null) {
                findViewById.setVisibility(8);
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            CryptKeeper.this.beginAttempt();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... strArr) {
            try {
                return Integer.valueOf(CryptKeeper.this.getStorageManager().decryptStorage(strArr[0]));
            } catch (Exception e) {
                Log.e("CryptKeeper", "Error while decrypting...", e);
                return -1;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            if (num.intValue() == 0) {
                if (CryptKeeper.this.mLockPatternView != null) {
                    CryptKeeper.this.mLockPatternView.removeCallbacks(CryptKeeper.this.mClearPatternRunnable);
                    CryptKeeper.this.mLockPatternView.postDelayed(CryptKeeper.this.mClearPatternRunnable, 500L);
                }
                ((TextView) CryptKeeper.this.findViewById(R.id.status)).setText(R.string.starting_android);
                hide(R.id.passwordEntry);
                hide(R.id.switch_ime_button);
                hide(R.id.lockPattern);
                hide(R.id.owner_info);
                hide(R.id.emergencyCallButton);
            } else if (num.intValue() == 30) {
                Intent intent = new Intent("android.intent.action.FACTORY_RESET");
                intent.setPackage("android");
                intent.addFlags(268435456);
                intent.putExtra("android.intent.extra.REASON", "CryptKeeper.MAX_FAILED_ATTEMPTS");
                CryptKeeper.this.sendBroadcast(intent);
            } else if (num.intValue() != -1) {
                CryptKeeper.this.handleBadAttempt(num);
            } else {
                CryptKeeper.this.setContentView(R.layout.crypt_keeper_progress);
                CryptKeeper.this.showFactoryReset(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void beginAttempt() {
        ((TextView) findViewById(R.id.status)).setText(R.string.checking_decryption);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBadAttempt(Integer num) {
        LockPatternView lockPatternView = this.mLockPatternView;
        if (lockPatternView != null) {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            this.mLockPatternView.removeCallbacks(this.mClearPatternRunnable);
            this.mLockPatternView.postDelayed(this.mClearPatternRunnable, 1500L);
        }
        if (num.intValue() % 10 == 0) {
            this.mCooldown = true;
            cooldown();
            return;
        }
        TextView textView = (TextView) findViewById(R.id.status);
        int intValue = 30 - num.intValue();
        int i = 0;
        if (intValue < 10) {
            textView.setText(TextUtils.expandTemplate(getText(R.string.crypt_keeper_warn_wipe), Integer.toString(intValue)));
        } else {
            try {
                i = getStorageManager().getPasswordType();
            } catch (Exception e) {
                Log.e("CryptKeeper", "Error calling mount service " + e);
            }
            if (i == 3) {
                textView.setText(R.string.cryptkeeper_wrong_pin);
            } else if (i == 2) {
                textView.setText(R.string.cryptkeeper_wrong_pattern);
            } else {
                textView.setText(R.string.cryptkeeper_wrong_password);
            }
        }
        LockPatternView lockPatternView2 = this.mLockPatternView;
        if (lockPatternView2 != null) {
            lockPatternView2.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            this.mLockPatternView.setEnabled(true);
        }
        ImeAwareEditText imeAwareEditText = this.mPasswordEntry;
        if (imeAwareEditText == null) {
            return;
        }
        imeAwareEditText.setEnabled(true);
        this.mPasswordEntry.scheduleShowSoftInput();
        setBackFunctionality(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ValidationTask extends AsyncTask<Void, Void, Boolean> {
        int state;

        private ValidationTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            IStorageManager storageManager = CryptKeeper.this.getStorageManager();
            try {
                Log.d("CryptKeeper", "Validating encryption state.");
                int encryptionState = storageManager.getEncryptionState();
                this.state = encryptionState;
                boolean z = true;
                if (encryptionState == 1) {
                    Log.w("CryptKeeper", "Unexpectedly in CryptKeeper even though there is no encryption.");
                    return Boolean.TRUE;
                }
                if (encryptionState != 0) {
                    z = false;
                }
                return Boolean.valueOf(z);
            } catch (RemoteException unused) {
                Log.w("CryptKeeper", "Unable to get encryption state properly");
                return Boolean.TRUE;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            boolean z = true;
            CryptKeeper.this.mValidationComplete = true;
            if (Boolean.FALSE.equals(bool)) {
                Log.w("CryptKeeper", "Incomplete, or corrupted encryption detected. Prompting user to wipe.");
                CryptKeeper.this.mEncryptionGoneBad = true;
                CryptKeeper cryptKeeper = CryptKeeper.this;
                if (this.state != -4) {
                    z = false;
                }
                cryptKeeper.mCorrupt = z;
            } else {
                Log.d("CryptKeeper", "Encryption state validated. Proceeding to configure UI");
            }
            CryptKeeper.this.setupUi();
        }
    }

    private boolean isDebugView() {
        return getIntent().hasExtra("com.android.settings.CryptKeeper.DEBUG_FORCE_VIEW");
    }

    private boolean isDebugView(String str) {
        return str.equals(getIntent().getStringExtra("com.android.settings.CryptKeeper.DEBUG_FORCE_VIEW"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUser() {
        int i = this.mNotificationCountdown;
        if (i > 0) {
            this.mNotificationCountdown = i - 1;
        } else {
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                try {
                    audioManager.playSoundEffect(5, 100);
                } catch (Exception e) {
                    Log.w("CryptKeeper", "notifyUser: Exception while playing sound: " + e);
                }
            }
        }
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, 5000L);
        if (this.mWakeLock.isHeld()) {
            int i2 = this.mReleaseWakeLockCountdown;
            if (i2 > 0) {
                this.mReleaseWakeLockCountdown = i2 - 1;
            } else {
                this.mWakeLock.release();
            }
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String str = (String) VoldProperties.decrypt().orElse("");
        if (!isDebugView() && ("".equals(str) || "trigger_restart_framework".equals(str))) {
            disableCryptKeeperComponent(this);
            finish();
            return;
        }
        try {
            if (getResources().getBoolean(R.bool.crypt_keeper_allow_rotation)) {
                setRequestedOrientation(-1);
            }
        } catch (Resources.NotFoundException unused) {
        }
        StatusBarManager statusBarManager = (StatusBarManager) getSystemService("statusbar");
        this.mStatusBar = statusBarManager;
        statusBarManager.disable(52887552);
        if (bundle != null) {
            this.mCooldown = bundle.getBoolean("cooldown");
        }
        setAirplaneModeIfNecessary();
        this.mAudioManager = (AudioManager) getSystemService("audio");
        Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
        if (!(lastNonConfigurationInstance instanceof NonConfigurationInstanceState)) {
            return;
        }
        this.mWakeLock = ((NonConfigurationInstanceState) lastNonConfigurationInstance).wakelock;
        Log.d("CryptKeeper", "Restoring wakelock from NonConfigurationInstanceState");
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("cooldown", this.mCooldown);
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        setupUi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupUi() {
        if (this.mEncryptionGoneBad || isDebugView("error")) {
            setContentView(R.layout.crypt_keeper_progress);
            showFactoryReset(this.mCorrupt);
        } else if (!"".equals((String) VoldProperties.encrypt_progress().orElse("")) || isDebugView("progress")) {
            setContentView(R.layout.crypt_keeper_progress);
            encryptionProgressInit();
        } else if (this.mValidationComplete || isDebugView("password")) {
            new AsyncTask<Void, Void, Void>() { // from class: com.android.settings.CryptKeeper.4
                String owner_info;
                int passwordType = 0;
                boolean password_visible;
                boolean pattern_visible;

                @Override // android.os.AsyncTask
                public Void doInBackground(Void... voidArr) {
                    try {
                        IStorageManager storageManager = CryptKeeper.this.getStorageManager();
                        this.passwordType = storageManager.getPasswordType();
                        this.owner_info = storageManager.getField("OwnerInfo");
                        boolean z = true;
                        this.pattern_visible = !"0".equals(storageManager.getField("PatternVisible"));
                        if ("0".equals(storageManager.getField("PasswordVisible"))) {
                            z = false;
                        }
                        this.password_visible = z;
                        return null;
                    } catch (Exception e) {
                        Log.e("CryptKeeper", "Error calling mount service " + e);
                        return null;
                    }
                }

                @Override // android.os.AsyncTask
                public void onPostExecute(Void r4) {
                    Settings.System.putInt(CryptKeeper.this.getContentResolver(), "show_password", this.password_visible ? 1 : 0);
                    int i = this.passwordType;
                    if (i == 3) {
                        CryptKeeper.this.setContentView(R.layout.crypt_keeper_pin_entry);
                        CryptKeeper.this.mStatusString = R.string.enter_pin;
                    } else if (i == 2) {
                        CryptKeeper.this.setContentView(R.layout.crypt_keeper_pattern_entry);
                        CryptKeeper.this.setBackFunctionality(false);
                        CryptKeeper.this.mStatusString = R.string.enter_pattern;
                    } else {
                        CryptKeeper.this.setContentView(R.layout.crypt_keeper_password_entry);
                        CryptKeeper.this.mStatusString = R.string.enter_password;
                    }
                    ((TextView) CryptKeeper.this.findViewById(R.id.status)).setText(CryptKeeper.this.mStatusString);
                    TextView textView = (TextView) CryptKeeper.this.findViewById(R.id.owner_info);
                    textView.setText(this.owner_info);
                    textView.setSelected(true);
                    CryptKeeper.this.passwordEntryInit();
                    CryptKeeper.this.findViewById(16908290).setSystemUiVisibility(4194304);
                    if (CryptKeeper.this.mLockPatternView != null) {
                        CryptKeeper.this.mLockPatternView.setInStealthMode(true ^ this.pattern_visible);
                    }
                    if (CryptKeeper.this.mCooldown) {
                        CryptKeeper.this.setBackFunctionality(false);
                        CryptKeeper.this.cooldown();
                    }
                }
            }.execute(new Void[0]);
        } else if (this.mValidationRequested) {
        } else {
            new ValidationTask().execute((Object[]) null);
            this.mValidationRequested = true;
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        NonConfigurationInstanceState nonConfigurationInstanceState = new NonConfigurationInstanceState(this.mWakeLock);
        Log.d("CryptKeeper", "Handing wakelock off to NonConfigurationInstanceState");
        this.mWakeLock = null;
        return nonConfigurationInstanceState;
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mWakeLock != null) {
            Log.d("CryptKeeper", "Releasing and destroying wakelock");
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
    }

    private void encryptionProgressInit() {
        Log.d("CryptKeeper", "Encryption progress screen initializing.");
        if (this.mWakeLock == null) {
            Log.d("CryptKeeper", "Acquiring wakelock.");
            PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(26, "CryptKeeper");
            this.mWakeLock = newWakeLock;
            newWakeLock.acquire();
        }
        ((ProgressBar) findViewById(R.id.progress_bar)).setIndeterminate(true);
        setBackFunctionality(false);
        updateProgress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFactoryReset(final boolean z) {
        findViewById(R.id.encroid).setVisibility(8);
        Button button = (Button) findViewById(R.id.factory_reset);
        button.setVisibility(0);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.CryptKeeper.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.FACTORY_RESET");
                intent.setPackage("android");
                intent.addFlags(268435456);
                intent.putExtra("android.intent.extra.REASON", "CryptKeeper.showFactoryReset() corrupt=" + z);
                CryptKeeper.this.sendBroadcast(intent);
            }
        });
        if (z) {
            ((TextView) findViewById(R.id.title)).setText(R.string.crypt_keeper_data_corrupt_title);
            ((TextView) findViewById(R.id.status)).setText(R.string.crypt_keeper_data_corrupt_summary);
        } else {
            ((TextView) findViewById(R.id.title)).setText(R.string.crypt_keeper_failed_title);
            ((TextView) findViewById(R.id.status)).setText(R.string.crypt_keeper_failed_summary);
        }
        View findViewById = findViewById(R.id.bottom_divider);
        if (findViewById != null) {
            findViewById.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgress() {
        int i;
        String str = (String) VoldProperties.encrypt_progress().orElse("");
        if ("error_partially_encrypted".equals(str)) {
            showFactoryReset(false);
            return;
        }
        CharSequence text = getText(R.string.crypt_keeper_setup_description);
        try {
            i = isDebugView() ? 50 : Integer.parseInt(str);
        } catch (Exception e) {
            Log.w("CryptKeeper", "Error parsing progress: " + e.toString());
            i = 0;
        }
        String num = Integer.toString(i);
        Log.v("CryptKeeper", "Encryption progress: " + num);
        try {
            int intValue = ((Integer) VoldProperties.encrypt_time_remaining().get()).intValue();
            if (intValue >= 0) {
                num = DateUtils.formatElapsedTime(((intValue + 9) / 10) * 10);
                text = getText(R.string.crypt_keeper_setup_time_remaining);
            }
        } catch (Exception unused) {
        }
        TextView textView = (TextView) findViewById(R.id.status);
        if (textView != null) {
            textView.setText(TextUtils.expandTemplate(text, num));
        }
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cooldown() {
        ImeAwareEditText imeAwareEditText = this.mPasswordEntry;
        if (imeAwareEditText != null) {
            imeAwareEditText.setEnabled(false);
        }
        LockPatternView lockPatternView = this.mLockPatternView;
        if (lockPatternView != null) {
            lockPatternView.setEnabled(false);
        }
        ((TextView) findViewById(R.id.status)).setText(R.string.crypt_keeper_force_power_cycle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setBackFunctionality(boolean z) {
        if (z) {
            this.mStatusBar.disable(52887552);
        } else {
            this.mStatusBar.disable(57081856);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fakeUnlockAttempt(View view) {
        beginAttempt();
        view.postDelayed(this.mFakeUnlockAttemptRunnable, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void passwordEntryInit() {
        View findViewById;
        ImeAwareEditText findViewById2 = findViewById(R.id.passwordEntry);
        this.mPasswordEntry = findViewById2;
        if (findViewById2 != null) {
            findViewById2.setOnEditorActionListener(this);
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntry.setOnKeyListener(this);
            this.mPasswordEntry.setOnTouchListener(this);
            this.mPasswordEntry.addTextChangedListener(this);
        }
        LockPatternView findViewById3 = findViewById(R.id.lockPattern);
        this.mLockPatternView = findViewById3;
        if (findViewById3 != null) {
            findViewById3.setOnPatternListener(this.mChooseNewLockPatternListener);
        }
        if (!getTelephonyManager().isVoiceCapable() && (findViewById = findViewById(R.id.emergencyCallButton)) != null) {
            Log.d("CryptKeeper", "Removing the emergency Call button");
            findViewById.setVisibility(8);
        }
        View findViewById4 = findViewById(R.id.switch_ime_button);
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        if (findViewById4 != null && hasMultipleEnabledIMEsOrSubtypes(inputMethodManager, false)) {
            findViewById4.setVisibility(0);
            findViewById4.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.CryptKeeper.7
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    inputMethodManager.showInputMethodPickerFromSystem(false, view.getDisplay().getDisplayId());
                }
            });
        }
        if (this.mWakeLock == null) {
            Log.d("CryptKeeper", "Acquiring wakelock.");
            PowerManager powerManager = (PowerManager) getSystemService("power");
            if (powerManager != null) {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(26, "CryptKeeper");
                this.mWakeLock = newWakeLock;
                newWakeLock.acquire();
                this.mReleaseWakeLockCountdown = 96;
            }
        }
        if (this.mLockPatternView == null && !this.mCooldown) {
            getWindow().setSoftInputMode(5);
            ImeAwareEditText imeAwareEditText = this.mPasswordEntry;
            if (imeAwareEditText != null) {
                imeAwareEditText.scheduleShowSoftInput();
            }
        }
        updateEmergencyCallButtonState();
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, 120000L);
        getWindow().addFlags(4718592);
    }

    private boolean hasMultipleEnabledIMEsOrSubtypes(InputMethodManager inputMethodManager, boolean z) {
        int i = 0;
        for (InputMethodInfo inputMethodInfo : inputMethodManager.getEnabledInputMethodList()) {
            if (i > 1) {
                return true;
            }
            List<InputMethodSubtype> enabledInputMethodSubtypeList = inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true);
            if (!enabledInputMethodSubtypeList.isEmpty()) {
                int i2 = 0;
                for (InputMethodSubtype inputMethodSubtype : enabledInputMethodSubtypeList) {
                    if (inputMethodSubtype.isAuxiliary()) {
                        i2++;
                    }
                }
                if (enabledInputMethodSubtypeList.size() - i2 <= 0) {
                    if (z && i2 > 1) {
                    }
                }
            }
            i++;
        }
        return i > 1 || inputMethodManager.getEnabledInputMethodSubtypeList(null, false).size() > 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IStorageManager getStorageManager() {
        IBinder service = ServiceManager.getService("mount");
        if (service != null) {
            return IStorageManager.Stub.asInterface(service);
        }
        return null;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 0 || i == 6) {
            String charSequence = textView.getText().toString();
            if (TextUtils.isEmpty(charSequence)) {
                return true;
            }
            textView.setText((CharSequence) null);
            this.mPasswordEntry.setEnabled(false);
            setBackFunctionality(false);
            if (charSequence.length() >= 4) {
                new DecryptTask().execute(charSequence);
            } else {
                fakeUnlockAttempt(this.mPasswordEntry);
            }
            return true;
        }
        return false;
    }

    private final void setAirplaneModeIfNecessary() {
        if (!getTelephonyManager().isLteCdmaEvdoGsmWcdmaEnabled()) {
            Log.d("CryptKeeper", "Going into airplane mode.");
            Settings.Global.putInt(getContentResolver(), "airplane_mode_on", 1);
            Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
            intent.putExtra("state", true);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private void updateEmergencyCallButtonState() {
        int i;
        Button button = (Button) findViewById(R.id.emergencyCallButton);
        if (button == null) {
            return;
        }
        if (isEmergencyCallCapable()) {
            button.setVisibility(0);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.CryptKeeper.8
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CryptKeeper.this.takeEmergencyCallAction();
                }
            });
            if (getTelecomManager().isInCall()) {
                i = R.string.cryptkeeper_return_to_call;
            } else {
                i = R.string.cryptkeeper_emergency_call;
            }
            button.setText(i);
            return;
        }
        button.setVisibility(8);
    }

    private boolean isEmergencyCallCapable() {
        return getTelephonyManager().isVoiceCapable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void takeEmergencyCallAction() {
        TelecomManager telecomManager = getTelecomManager();
        if (telecomManager.isInCall()) {
            telecomManager.showInCallScreen(false);
        } else {
            launchEmergencyDialer();
        }
    }

    private void launchEmergencyDialer() {
        Intent intent = new Intent("com.android.phone.EmergencyDialer.DIAL");
        intent.setFlags(276824064);
        setBackFunctionality(true);
        startActivity(intent);
    }

    private TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getSystemService("phone");
    }

    private TelecomManager getTelecomManager() {
        return (TelecomManager) getSystemService("telecom");
    }

    private void delayAudioNotification() {
        this.mNotificationCountdown = 20;
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        delayAudioNotification();
        return false;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        delayAudioNotification();
        return false;
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        delayAudioNotification();
    }

    private static void disableCryptKeeperComponent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, CryptKeeper.class);
        Log.d("CryptKeeper", "Disabling component " + componentName);
        packageManager.setComponentEnabledSetting(componentName, 2, 1);
    }
}
