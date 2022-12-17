package com.android.settings.sar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.nothing.p006ui.support.NtCustSwitch;

public class WifiSarTest extends Activity implements SensorEventListener {
    private AudioManager mAudioManager;
    CompoundButton.OnCheckedChangeListener mBtConnectedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsBtConnected = z;
        }
    };
    private NtCustSwitch mBtConnectedEnabler;
    /* access modifiers changed from: private */
    public EditText mCountryCodeEditText;
    CompoundButton.OnCheckedChangeListener mEarpieceOnChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsEarpieceOn = z;
        }
    };
    private NtCustSwitch mEarpieceOnEnabler;
    private Button mGetCountryCodeButton;
    View.OnClickListener mGetCountryCodeListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (WifiSarTest.this.mWifiManager != null) {
                String countryCode = WifiSarTest.this.mWifiManager.getCountryCode();
                if (WifiSarTest.this.mCountryCodeEditText != null) {
                    WifiSarTest.this.mCountryCodeEditText.setText(countryCode);
                }
            }
        }
    };
    View.OnClickListener mGetSarListener = new View.OnClickListener() {
        public void onClick(View view) {
            Log.d("WifiSarTest", "get sar button onClick");
            WifiSarTest.this.updateGetSarSetTextView();
        }
    };
    private Button mGetSarSetButton;
    private TextView mGetSarSetTextView;
    /* access modifiers changed from: private */
    public boolean mIsBtConnected = false;
    /* access modifiers changed from: private */
    public boolean mIsEarpieceOn = false;
    /* access modifiers changed from: private */
    public boolean mIsModemActive = false;
    /* access modifiers changed from: private */
    public boolean mIsSarSensorNear = false;
    private boolean mIsSarSensorNearReal = false;
    /* access modifiers changed from: private */
    public boolean mIsStartTest = false;
    /* access modifiers changed from: private */
    public boolean mIsWifi24G = false;
    private NtCustSwitch mManulMdoeEnabler;
    CompoundButton.OnCheckedChangeListener mManulModeChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            SystemProperties.set("debug.wifi.sar.manul", z ? "1" : "0");
            Toast.makeText(WifiSarTest.this.getApplicationContext(), "Need to Re-enable wifi, wati a moments", 0).show();
            WifiSarTest.this.mSwitchingWifiManulMode = true;
            if (1 == WifiSarTest.this.mWifiState) {
                WifiSarTest.this.enableWifi(true);
            } else {
                WifiSarTest.this.enableWifi(false);
            }
            WifiSarTest.this.setAllEnable(false);
        }
    };
    CompoundButton.OnCheckedChangeListener mModemActiveChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsModemActive = z;
        }
    };
    private NtCustSwitch mModemActiveEnabler;
    /* access modifiers changed from: private */
    public boolean mPhone0Active = false;
    /* access modifiers changed from: private */
    public boolean mPhone1Active = false;
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean z = false;
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("wifi_state", 4);
                if (WifiSarTest.this.mSwitchingWifiManulMode && 3 == intExtra) {
                    Toast.makeText(WifiSarTest.this.getApplicationContext(), "Re-eable wifi completed!", 0).show();
                    WifiSarTest.this.setAllEnable(true);
                    WifiSarTest.this.mSwitchingWifiManulMode = false;
                } else if (WifiSarTest.this.mSwitchingWifiManulMode && 1 == intExtra) {
                    WifiSarTest.this.enableWifi(true);
                }
                WifiSarTest.this.mWifiState = intExtra;
            } else if ("android.intent.action.RRC_STATE".equals(action)) {
                int intExtra2 = intent.getIntExtra("phone", -1);
                int intExtra3 = intent.getIntExtra("state", -1);
                Log.d("WifiSarTest", "RRC_STATE phone id: " + intExtra2 + ", state: " + intExtra3);
                if (intExtra2 == 0) {
                    if (1 == intExtra3) {
                        WifiSarTest.this.mPhone0Active = true;
                    } else if (intExtra3 == 0) {
                        WifiSarTest.this.mPhone0Active = false;
                    }
                } else if (1 == intExtra2) {
                    if (1 == intExtra3) {
                        WifiSarTest.this.mPhone1Active = true;
                    } else if (intExtra3 == 0) {
                        WifiSarTest.this.mPhone1Active = false;
                    }
                }
                WifiSarTest wifiSarTest = WifiSarTest.this;
                if (wifiSarTest.mPhone0Active || WifiSarTest.this.mPhone1Active) {
                    z = true;
                }
                wifiSarTest.mIsModemActive = z;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mSarSensorChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsSarSensorNear = z;
        }
    };
    private NtCustSwitch mSarSensorNearEnabler;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Button mSetCountryCodeButton;
    View.OnClickListener mSetCountryCodeListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (WifiSarTest.this.mCountryCodeEditText != null) {
                WifiSarTest wifiSarTest = WifiSarTest.this;
                wifiSarTest.doUpdateCountryCode(wifiSarTest.mCountryCodeEditText.getText().toString());
            }
        }
    };
    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!WifiSarTest.this.isManulModeOn()) {
                Toast.makeText(WifiSarTest.this.getApplicationContext(), "Enable Manul Mode First!!", 1).show();
            } else if (!WifiSarTest.this.mIsStartTest) {
                WifiSarTest.this.setSarTestRunning(true);
                WifiSarTest.this.mIsStartTest = true;
                WifiSarTest.this.updateStartButtonStarted();
            } else {
                WifiSarTest.this.setSarTestRunning(false);
                WifiSarTest.this.mIsStartTest = false;
                WifiSarTest.this.updateStartButtonStarted();
            }
        }
    };
    private Button mStartSarTestButton;
    /* access modifiers changed from: private */
    public boolean mSwitchingWifiManulMode = false;
    View.OnClickListener mUpdateAllListener = new View.OnClickListener() {
        public void onClick(View view) {
            WifiSarTest.this.updateAllStatus();
        }
    };
    private Button mUpdateAllStatusButton;
    CompoundButton.OnCheckedChangeListener mWifi24GChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsWifi24G = z;
        }
    };
    private NtCustSwitch mWifi24GEnabler;
    /* access modifiers changed from: private */
    public WifiManager mWifiManager;
    /* access modifiers changed from: private */
    public int mWifiState;

    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.testing_wifi_sar);
        this.mManulMdoeEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_manul_mode_sw);
        this.mModemActiveEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_modem_active_sw);
        this.mBtConnectedEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_bt_connected_sw);
        this.mSarSensorNearEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_sensor_near_sw);
        this.mEarpieceOnEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_earpiece_on_sw);
        this.mWifi24GEnabler = (NtCustSwitch) findViewById(R$id.wifi_sar_wifi_band_sw);
        this.mGetSarSetTextView = (TextView) findViewById(R$id.wifi_sar_get_sar_set_tv);
        this.mGetSarSetButton = (Button) findViewById(R$id.wifi_sar_get_sar_set_bt);
        this.mUpdateAllStatusButton = (Button) findViewById(R$id.update_all_status);
        this.mCountryCodeEditText = (EditText) findViewById(R$id.country_code_et);
        this.mGetCountryCodeButton = (Button) findViewById(R$id.get_country_code_bt);
        this.mSetCountryCodeButton = (Button) findViewById(R$id.set_country_code_bt);
        this.mStartSarTestButton = (Button) findViewById(R$id.start);
        this.mWifiManager = (WifiManager) getSystemService(WifiManager.class);
        this.mAudioManager = (AudioManager) getSystemService(AudioManager.class);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.RRC_STATE");
        registerReceiver(this.mReceiver, intentFilter);
        upateSwitchListener(this.mManulMdoeEnabler, this.mManulModeChangeListener, isManulModeOn());
        upateSwitchListener(this.mModemActiveEnabler, this.mModemActiveChangeListener, isModemActive());
        upateSwitchListener(this.mBtConnectedEnabler, this.mBtConnectedChangeListener, isBtConnected());
        upateSwitchListener(this.mSarSensorNearEnabler, this.mSarSensorChangeListener, isSarSensorNear());
        upateSwitchListener(this.mEarpieceOnEnabler, this.mEarpieceOnChangeListener, isEarpieceOn());
        upateSwitchListener(this.mWifi24GEnabler, this.mWifi24GChangeListener, isWifi24G());
        updateButtonListener(this.mGetSarSetButton, this.mGetSarListener);
        updateButtonListener(this.mGetCountryCodeButton, this.mGetCountryCodeListener);
        updateButtonListener(this.mSetCountryCodeButton, this.mSetCountryCodeListener);
        updateButtonListener(this.mStartSarTestButton, this.mStartListener);
        updateButtonListener(this.mUpdateAllStatusButton, this.mUpdateAllListener);
        registerSarSensorListeners();
    }

    public final void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr = sensorEvent.values;
        if (1.0d == ((double) fArr[0]) || 1.0d == ((double) fArr[6])) {
            this.mIsSarSensorNearReal = true;
        } else {
            this.mIsSarSensorNearReal = false;
        }
        Log.d("WifiSarTest", "mIsSarSensorNearReal: " + this.mIsSarSensorNearReal);
    }

    private void registerSarSensorListeners() {
        SensorManager sensorManager = (SensorManager) getSystemService("sensor");
        this.mSensorManager = sensorManager;
        Sensor defaultSensor = sensorManager.getDefaultSensor(33171005);
        this.mSensor = defaultSensor;
        if (defaultSensor != null) {
            Log.d("WifiSarTest", "getDefaultSensor succ");
            this.mSensorManager.registerListener(this, this.mSensor, 3);
            return;
        }
        Log.d("WifiSarTest", "getDefaultSensor failed");
    }

    private void updateButtonListener(Button button, View.OnClickListener onClickListener) {
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }

    private boolean isVoiceCallStreamActive() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            Log.i("WifiSarTest", "isVoiceCallStreamActive audiomanager is null");
            return false;
        }
        int mode = audioManager.getMode();
        if (mode == 3 || mode == 2) {
            return true;
        }
        return false;
    }

    private boolean isVoiceCallOnEarpiece() {
        for (AudioDeviceAttributes audioDeviceAttributes : this.mAudioManager.getDevicesForAttributes(new AudioAttributes.Builder().setUsage(2).build())) {
            if (audioDeviceAttributes.getRole() == 2 && audioDeviceAttributes.getType() == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isBtConnectState(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            return false;
        }
        if (2 == bluetoothAdapter.getConnectionState() || Settings.Global.getInt(getContentResolver(), "bluetooth_opp_state", -1) > 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateAllStatus() {
        WifiManager wifiManager;
        WifiManager wifiManager2;
        boolean z;
        NtCustSwitch ntCustSwitch = this.mManulMdoeEnabler;
        if (ntCustSwitch != null) {
            ntCustSwitch.setChecked(isManulModeOn());
        }
        NtCustSwitch ntCustSwitch2 = this.mModemActiveEnabler;
        if (ntCustSwitch2 != null) {
            ntCustSwitch2.setChecked(this.mIsModemActive);
        }
        if (this.mBtConnectedEnabler != null) {
            if (isBtConnectState(BluetoothAdapter.getDefaultAdapter())) {
                this.mBtConnectedEnabler.setChecked(true);
            } else {
                this.mBtConnectedEnabler.setChecked(false);
            }
        }
        NtCustSwitch ntCustSwitch3 = this.mSarSensorNearEnabler;
        if (ntCustSwitch3 != null) {
            ntCustSwitch3.setChecked(this.mIsSarSensorNearReal);
        }
        if (this.mEarpieceOnEnabler != null) {
            if (isVoiceCallStreamActive()) {
                z = isVoiceCallOnEarpiece();
                Log.d("WifiSarTest", "EarPiece active = " + z);
            } else {
                z = false;
            }
            this.mEarpieceOnEnabler.setChecked(z);
        }
        if (!(this.mWifi24GEnabler == null || (wifiManager2 = this.mWifiManager) == null)) {
            WifiInfo connectionInfo = wifiManager2.getConnectionInfo();
            if (connectionInfo == null || connectionInfo.getSupplicantState() != SupplicantState.COMPLETED || !connectionInfo.is24GHz()) {
                this.mWifi24GEnabler.setChecked(false);
            } else {
                this.mWifi24GEnabler.setChecked(true);
            }
        }
        updateGetSarSetTextView();
        if (this.mCountryCodeEditText != null && (wifiManager = this.mWifiManager) != null) {
            this.mCountryCodeEditText.setText(wifiManager.getCountryCode());
        }
    }

    /* access modifiers changed from: private */
    public void updateGetSarSetTextView() {
        if (this.mGetSarSetTextView != null) {
            String string = Settings.Global.getString(getContentResolver(), "wifi_sar_curr_set");
            if ("5".equals(string)) {
                string = "No SAR Limit";
            }
            this.mGetSarSetTextView.setText(string);
        }
    }

    /* access modifiers changed from: private */
    public void doUpdateCountryCode(String str) {
        String trim;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                this.mWifiManager.setWifiEnabled(true);
                Toast.makeText(getApplicationContext(), "Enabling wifi", 0).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException unused) {
                }
            }
            this.mWifiManager.disconnect();
            if (this.mCountryCodeEditText != null && str != null && (trim = str.trim()) != null && trim.length() == 2) {
                this.mWifiManager.setOverrideCountryCode(trim);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateStartButtonStarted() {
        Button button = this.mStartSarTestButton;
        if (button != null) {
            button.setText(this.mIsStartTest ? "STOP SAR TEST" : "START SAR TEST");
        }
    }

    private int calSarSet() {
        if (!isManulModeOn()) {
            Toast.makeText(getApplicationContext(), "Enable Manul Mode First!!", 1).show();
            return 5;
        }
        EditText editText = this.mCountryCodeEditText;
        String obj = editText != null ? editText.getText().toString() : null;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            String countryCode = wifiManager.getCountryCode();
            if (obj != null && !obj.equals(countryCode)) {
                doUpdateCountryCode(obj);
            }
        }
        if (3 != this.mWifiState) {
            Toast.makeText(getApplicationContext(), "Enable Wifi First!!", 1).show();
            return 5;
        }
        Log.d("WifiSarTest", "countryCodeFromUi:" + obj + ",isEarpieceOn:" + isEarpieceOn() + ",isBtConnected: " + isBtConnected() + ",isModemActive:" + isModemActive() + ",isWifi24G:" + isWifi24G() + ",isSarSensorNear: " + isSarSensorNear());
        if ("IN".equals(obj)) {
            if (!isEarpieceOn()) {
                return 5;
            }
            if (!isBtConnected() && !isModemActive()) {
                return 0;
            }
            if ((isBtConnected() || !isModemActive()) && (!isBtConnected() || isModemActive())) {
                if (isBtConnected() && isModemActive()) {
                    if (isWifi24G()) {
                        return 1;
                    }
                    return 2;
                }
            } else if (!isBtConnected() || !isWifi24G()) {
                return 1;
            } else {
                return 0;
            }
        } else if (!isBtConnected() && !isModemActive() && (isEarpieceOn() || isSarSensorNear())) {
            return 0;
        } else {
            if ((isBtConnected() || !isModemActive()) && (!isBtConnected() || isModemActive())) {
                if (isBtConnected() && isModemActive()) {
                    if (isWifi24G()) {
                        return 1;
                    }
                    return 2;
                }
            } else if (!isBtConnected() || !isWifi24G()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 5;
    }

    /* access modifiers changed from: private */
    public void setSarTestRunning(boolean z) {
        int i;
        if (z) {
            i = calSarSet();
            Log.d("WifiSarTest", "start sarSet: " + i);
        } else {
            Log.d("WifiSarTest", "stop sar test");
            i = 5;
        }
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.setSarSet(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mReceiver);
        this.mSensorManager.unregisterListener(this);
    }

    /* access modifiers changed from: private */
    public void setAllEnable(boolean z) {
        NtCustSwitch ntCustSwitch = this.mManulMdoeEnabler;
        if (ntCustSwitch != null) {
            ntCustSwitch.setEnabled(z);
        }
        NtCustSwitch ntCustSwitch2 = this.mModemActiveEnabler;
        if (ntCustSwitch2 != null) {
            ntCustSwitch2.setEnabled(z);
        }
        NtCustSwitch ntCustSwitch3 = this.mBtConnectedEnabler;
        if (ntCustSwitch3 != null) {
            ntCustSwitch3.setEnabled(z);
        }
        NtCustSwitch ntCustSwitch4 = this.mSarSensorNearEnabler;
        if (ntCustSwitch4 != null) {
            ntCustSwitch4.setEnabled(z);
        }
        NtCustSwitch ntCustSwitch5 = this.mEarpieceOnEnabler;
        if (ntCustSwitch5 != null) {
            ntCustSwitch5.setEnabled(z);
        }
        NtCustSwitch ntCustSwitch6 = this.mWifi24GEnabler;
        if (ntCustSwitch6 != null) {
            ntCustSwitch6.setEnabled(z);
        }
        Button button = this.mGetSarSetButton;
        if (button != null) {
            button.setEnabled(z);
        }
        Button button2 = this.mGetCountryCodeButton;
        if (button2 != null) {
            button2.setEnabled(z);
        }
        Button button3 = this.mSetCountryCodeButton;
        if (button3 != null) {
            button3.setEnabled(z);
        }
        Button button4 = this.mStartSarTestButton;
        if (button4 != null) {
            button4.setEnabled(z);
        }
    }

    /* access modifiers changed from: private */
    public void enableWifi(boolean z) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(z);
        }
    }

    private void upateSwitchListener(Switch switchR, CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean z) {
        if (switchR != null) {
            switchR.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
            switchR.setChecked(z);
            switchR.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    /* access modifiers changed from: private */
    public boolean isManulModeOn() {
        return "1".equals(SystemProperties.get("debug.wifi.sar.manul", "0"));
    }

    private boolean isModemActive() {
        return this.mIsModemActive;
    }

    private boolean isBtConnected() {
        return this.mIsBtConnected;
    }

    private boolean isSarSensorNear() {
        return this.mIsSarSensorNear;
    }

    private boolean isEarpieceOn() {
        return this.mIsEarpieceOn;
    }

    private boolean isWifi24G() {
        return this.mIsWifi24G;
    }
}
