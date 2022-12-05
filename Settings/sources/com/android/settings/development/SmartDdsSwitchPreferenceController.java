package com.android.settings.development;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Token;
/* loaded from: classes.dex */
public class SmartDdsSwitchPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    private static SmartDdsSwitchPreferenceController mInstance;
    private Client mClient;
    private Context mContext;
    private ExtTelephonyManager mExtTelephonyManager;
    private String mPackageName;
    private final TelephonyManager mTelephonyManager;
    private final String TAG = "SmartDdsSwitchPreferenceController";
    private final int EVENT_SET_DEFAULT_TOGGLE_STATE_RESPONSE = 1;
    private final int SETTING_VALUE_ON = 1;
    private final int SETTING_VALUE_OFF = 0;
    private boolean mFeatureAvailable = false;
    private boolean mServiceConnected = false;
    private boolean mSwitchEnabled = false;
    private ServiceCallback mExtTelManagerServiceCallback = new ServiceCallback() { // from class: com.android.settings.development.SmartDdsSwitchPreferenceController.1
        public void onConnected() {
            Log.d("SmartDdsSwitchPreferenceController", "mExtTelManagerServiceCallback: service connected");
            SmartDdsSwitchPreferenceController.this.mServiceConnected = true;
            SmartDdsSwitchPreferenceController smartDdsSwitchPreferenceController = SmartDdsSwitchPreferenceController.this;
            smartDdsSwitchPreferenceController.mClient = smartDdsSwitchPreferenceController.mExtTelephonyManager.registerCallback(SmartDdsSwitchPreferenceController.this.mPackageName, SmartDdsSwitchPreferenceController.this.mCallback);
            Log.d("SmartDdsSwitchPreferenceController", "Client = " + SmartDdsSwitchPreferenceController.this.mClient);
        }

        public void onDisconnected() {
            Log.d("SmartDdsSwitchPreferenceController", "mExtTelManagerServiceCallback: service disconnected");
            SmartDdsSwitchPreferenceController.this.mServiceConnected = false;
            SmartDdsSwitchPreferenceController.this.mClient = null;
        }
    };
    private ExtPhoneCallbackBase mCallback = new ExtPhoneCallbackBase() { // from class: com.android.settings.development.SmartDdsSwitchPreferenceController.2
        public void setSmartDdsSwitchToggleResponse(Token token, boolean z) throws RemoteException {
            Log.d("SmartDdsSwitchPreferenceController", "setSmartDdsSwitchToggleResponse: token = " + token + " result = " + z);
            if (z) {
                SmartDdsSwitchPreferenceController.this.mHandler.sendMessage(SmartDdsSwitchPreferenceController.this.mHandler.obtainMessage(1));
            }
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.settings.development.SmartDdsSwitchPreferenceController.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Log.d("SmartDdsSwitchPreferenceController", "EVENT_SET_DEFAULT_TOGGLE_STATE_RESPONSE");
                SmartDdsSwitchPreferenceController.this.updateUi(SmartDdsSwitchPreferenceController.this.mContext.getResources().getString(R.string.smart_dds_switch_summary), true);
                SmartDdsSwitchPreferenceController smartDdsSwitchPreferenceController = SmartDdsSwitchPreferenceController.this;
                smartDdsSwitchPreferenceController.putSwitchValue(smartDdsSwitchPreferenceController.mSwitchEnabled ? 1 : 0);
                return;
            }
            Log.e("SmartDdsSwitchPreferenceController", "Unsupported action");
        }
    };

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "smart_dds_switch";
    }

    private SmartDdsSwitchPreferenceController(Context context) {
        super(context);
        Log.d("SmartDdsSwitchPreferenceController", "Constructor");
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mPackageName = applicationContext.getPackageName();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        ExtTelephonyManager extTelephonyManager = ExtTelephonyManager.getInstance(this.mContext);
        this.mExtTelephonyManager = extTelephonyManager;
        extTelephonyManager.connectService(this.mExtTelManagerServiceCallback);
    }

    public static SmartDdsSwitchPreferenceController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SmartDdsSwitchPreferenceController(context);
        }
        return mInstance;
    }

    public void cleanUp() {
        Log.d("SmartDdsSwitchPreferenceController", "Disconnecting ExtTelephonyService");
        this.mExtTelephonyManager.disconnectService();
        mInstance = null;
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        Client client;
        this.mSwitchEnabled = ((Boolean) obj).booleanValue();
        Log.d("SmartDdsSwitchPreferenceController", "onPreferenceChange: isEnabled = " + this.mSwitchEnabled);
        updateUi(this.mContext.getResources().getString(R.string.smart_dds_switch_wait_summary), false);
        if (this.mServiceConnected && (client = this.mClient) != null) {
            try {
                this.mExtTelephonyManager.setSmartDdsSwitchToggle(this.mSwitchEnabled, client);
                return true;
            } catch (Exception e) {
                Log.e("SmartDdsSwitchPreferenceController", "Exception " + e);
                return false;
            }
        }
        Log.e("SmartDdsSwitchPreferenceController", "ExtTelephonyManager service not connected");
        return false;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        if (this.mPreference != null) {
            ((SwitchPreference) this.mPreference).setChecked(getSwitchValue() != 0);
        }
        if (this.mServiceConnected) {
            try {
                this.mFeatureAvailable = this.mExtTelephonyManager.isSmartDdsSwitchFeatureAvailable();
            } catch (Exception e) {
                Log.e("SmartDdsSwitchPreferenceController", "Exception " + e);
            }
            Log.d("SmartDdsSwitchPreferenceController", "mFeatureAvailable: " + this.mFeatureAvailable);
            if (this.mFeatureAvailable) {
                updateUi(this.mContext.getResources().getString(R.string.smart_dds_switch_summary), true);
                return;
            }
            Log.d("SmartDdsSwitchPreferenceController", "Feature unavailable");
            preference.setVisible(false);
            return;
        }
        Log.d("SmartDdsSwitchPreferenceController", "Service not connected");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUi(String str, boolean z) {
        Log.d("SmartDdsSwitchPreferenceController", "updateUi enable: " + z);
        Preference preference = this.mPreference;
        if (preference != null) {
            ((SwitchPreference) preference).setVisible(true);
            ((SwitchPreference) this.mPreference).setSummary(str);
            ((SwitchPreference) this.mPreference).setEnabled(z);
        }
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        Log.d("SmartDdsSwitchPreferenceController", "numActiveModemCount: " + activeModemCount);
        return activeModemCount > 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putSwitchValue(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), getPreferenceKey(), i);
    }

    private int getSwitchValue() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), getPreferenceKey(), 0);
    }
}
