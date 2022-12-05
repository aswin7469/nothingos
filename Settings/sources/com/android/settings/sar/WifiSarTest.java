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
import com.android.settings.R;
/* loaded from: classes.dex */
public class WifiSarTest extends Activity implements SensorEventListener {
    private AudioManager mAudioManager;
    private Switch mBtConnectedEnabler;
    private EditText mCountryCodeEditText;
    private Switch mEarpieceOnEnabler;
    private Button mGetCountryCodeButton;
    private Button mGetSarSetButton;
    private TextView mGetSarSetTextView;
    private Switch mManulMdoeEnabler;
    private Switch mModemActiveEnabler;
    private Switch mSarSensorNearEnabler;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Button mSetCountryCodeButton;
    private Button mStartSarTestButton;
    private Button mUpdateAllStatusButton;
    private Switch mWifi24GEnabler;
    private WifiManager mWifiManager;
    private int mWifiState;
    private boolean mIsModemActive = false;
    private boolean mIsBtConnected = false;
    private boolean mIsSarSensorNear = false;
    private boolean mIsEarpieceOn = false;
    private boolean mIsWifi24G = false;
    private boolean mSwitchingWifiManulMode = false;
    private boolean mIsStartTest = false;
    private boolean mIsSarSensorNearReal = false;
    private boolean mIsModemActiveReal = false;
    private boolean mPhone0Active = false;
    private boolean mPhone1Active = false;
    View.OnClickListener mUpdateAllListener = new View.OnClickListener() { // from class: com.android.settings.sar.WifiSarTest.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WifiSarTest.this.updateAllStatus();
        }
    };
    View.OnClickListener mGetSarListener = new View.OnClickListener() { // from class: com.android.settings.sar.WifiSarTest.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Log.d("WifiSarTest", "get sar button onClick");
            WifiSarTest.this.updateGetSarSetTextView();
        }
    };
    View.OnClickListener mGetCountryCodeListener = new View.OnClickListener() { // from class: com.android.settings.sar.WifiSarTest.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WifiSarTest.this.mWifiManager != null) {
                String countryCode = WifiSarTest.this.mWifiManager.getCountryCode();
                if (WifiSarTest.this.mCountryCodeEditText == null) {
                    return;
                }
                WifiSarTest.this.mCountryCodeEditText.setText(countryCode);
            }
        }
    };
    View.OnClickListener mSetCountryCodeListener = new View.OnClickListener() { // from class: com.android.settings.sar.WifiSarTest.4
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WifiSarTest.this.mCountryCodeEditText != null) {
                WifiSarTest wifiSarTest = WifiSarTest.this;
                wifiSarTest.doUpdateCountryCode(wifiSarTest.mCountryCodeEditText.getText().toString());
            }
        }
    };
    View.OnClickListener mStartListener = new View.OnClickListener() { // from class: com.android.settings.sar.WifiSarTest.5
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WifiSarTest.this.isManulModeOn()) {
                if (!WifiSarTest.this.mIsStartTest) {
                    WifiSarTest.this.setSarTestRunning(true);
                    WifiSarTest.this.mIsStartTest = true;
                    WifiSarTest.this.updateStartButtonStarted();
                    return;
                }
                WifiSarTest.this.setSarTestRunning(false);
                WifiSarTest.this.mIsStartTest = false;
                WifiSarTest.this.updateStartButtonStarted();
                return;
            }
            Toast.makeText(WifiSarTest.this.getApplicationContext(), "Enable Manul Mode First!!", 1).show();
        }
    };
    final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settings.sar.WifiSarTest.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean z = false;
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("wifi_state", 4);
                if (!WifiSarTest.this.mSwitchingWifiManulMode || 3 != intExtra) {
                    if (WifiSarTest.this.mSwitchingWifiManulMode && 1 == intExtra) {
                        WifiSarTest.this.enableWifi(true);
                    }
                } else {
                    Toast.makeText(WifiSarTest.this.getApplicationContext(), "Re-eable wifi completed!", 0).show();
                    WifiSarTest.this.setAllEnable(true);
                    WifiSarTest.this.mSwitchingWifiManulMode = false;
                }
                WifiSarTest.this.mWifiState = intExtra;
            } else if (!"android.intent.action.RRC_STATE".equals(action)) {
            } else {
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
                wifiSarTest.mIsModemActiveReal = z;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mManulModeChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.7
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
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
    CompoundButton.OnCheckedChangeListener mModemActiveChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.8
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsModemActive = z;
        }
    };
    CompoundButton.OnCheckedChangeListener mBtConnectedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.9
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsBtConnected = z;
        }
    };
    CompoundButton.OnCheckedChangeListener mSarSensorChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.10
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsSarSensorNear = z;
        }
    };
    CompoundButton.OnCheckedChangeListener mEarpieceOnChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.11
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsEarpieceOn = z;
        }
    };
    CompoundButton.OnCheckedChangeListener mWifi24GChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settings.sar.WifiSarTest.12
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            WifiSarTest.this.mIsWifi24G = z;
        }
    };

    @Override // android.hardware.SensorEventListener
    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.testing_wifi_sar);
        this.mManulMdoeEnabler = (Switch) findViewById(R.id.wifi_sar_manul_mode_sw);
        this.mModemActiveEnabler = (Switch) findViewById(R.id.wifi_sar_modem_active_sw);
        this.mBtConnectedEnabler = (Switch) findViewById(R.id.wifi_sar_bt_connected_sw);
        this.mSarSensorNearEnabler = (Switch) findViewById(R.id.wifi_sar_sensor_near_sw);
        this.mEarpieceOnEnabler = (Switch) findViewById(R.id.wifi_sar_earpiece_on_sw);
        this.mWifi24GEnabler = (Switch) findViewById(R.id.wifi_sar_wifi_band_sw);
        this.mGetSarSetTextView = (TextView) findViewById(R.id.wifi_sar_get_sar_set_tv);
        this.mGetSarSetButton = (Button) findViewById(R.id.wifi_sar_get_sar_set_bt);
        this.mUpdateAllStatusButton = (Button) findViewById(R.id.update_all_status);
        this.mCountryCodeEditText = (EditText) findViewById(R.id.country_code_et);
        this.mGetCountryCodeButton = (Button) findViewById(R.id.get_country_code_bt);
        this.mSetCountryCodeButton = (Button) findViewById(R.id.set_country_code_bt);
        this.mStartSarTestButton = (Button) findViewById(R.id.start);
        this.mWifiManager = (WifiManager) getSystemService(WifiManager.class);
        this.mAudioManager = (AudioManager) getSystemService(AudioManager.class);
    }

    @Override // android.app.Activity
    protected void onResume() {
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

    @Override // android.hardware.SensorEventListener
    public final void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr = sensorEvent.values;
        if (1.0d == fArr[0] || 1.0d == fArr[6]) {
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
        if (button == null) {
            return;
        }
        button.setOnClickListener(onClickListener);
    }

    private boolean isVoiceCallStreamActive() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            Log.i("WifiSarTest", "isVoiceCallStreamActive audiomanager is null");
            return false;
        }
        int mode = audioManager.getMode();
        return mode == 3 || mode == 2;
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
        return 2 == bluetoothAdapter.getConnectionState() || Settings.Global.getInt(getContentResolver(), "bluetooth_opp_state", -1) > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllStatus() {
        WifiManager wifiManager;
        WifiManager wifiManager2;
        boolean z;
        Switch r0 = this.mManulMdoeEnabler;
        if (r0 != null) {
            r0.setChecked(isManulModeOn());
        }
        Switch r02 = this.mModemActiveEnabler;
        if (r02 != null) {
            r02.setChecked(this.mIsModemActiveReal);
            this.mIsSarSensorNear = this.mIsSarSensorNearReal;
        }
        if (this.mBtConnectedEnabler != null) {
            if (isBtConnectState(BluetoothAdapter.getDefaultAdapter())) {
                this.mBtConnectedEnabler.setChecked(true);
            } else {
                this.mBtConnectedEnabler.setChecked(false);
            }
        }
        Switch r03 = this.mSarSensorNearEnabler;
        if (r03 != null) {
            r03.setChecked(this.mIsSarSensorNearReal);
            this.mIsSarSensorNear = this.mIsSarSensorNearReal;
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
        if (this.mWifi24GEnabler != null && (wifiManager2 = this.mWifiManager) != null) {
            WifiInfo connectionInfo = wifiManager2.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getSupplicantState() == SupplicantState.COMPLETED && connectionInfo.is24GHz()) {
                this.mWifi24GEnabler.setChecked(true);
            } else {
                this.mWifi24GEnabler.setChecked(false);
            }
        }
        updateGetSarSetTextView();
        if (this.mCountryCodeEditText == null || (wifiManager = this.mWifiManager) == null) {
            return;
        }
        this.mCountryCodeEditText.setText(wifiManager.getCountryCode());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGetSarSetTextView() {
        if (this.mGetSarSetTextView != null) {
            String string = Settings.Global.getString(getContentResolver(), "wifi_sar_curr_set");
            if ("5".equals(string)) {
                string = "No SAR Limit";
            }
            this.mGetSarSetTextView.setText(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doUpdateCountryCode(String str) {
        String trim;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                this.mWifiManager.setWifiEnabled(true);
                Toast.makeText(getApplicationContext(), "Enabling wifi", 0).show();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException unused) {
                }
            }
            this.mWifiManager.disconnect();
            if (this.mCountryCodeEditText == null || str == null || (trim = str.trim()) == null || trim.length() != 2) {
                return;
            }
            this.mWifiManager.setOverrideCountryCode(trim);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
            if ((!isBtConnected() && isModemActive()) || (isBtConnected() && !isModemActive())) {
                return (!isBtConnected() || !isWifi24G()) ? 1 : 0;
            } else if (isBtConnected() && isModemActive()) {
                return isWifi24G() ? 1 : 2;
            }
        } else if (!isBtConnected() && !isModemActive() && (isEarpieceOn() || isSarSensorNear())) {
            return 0;
        } else {
            if ((!isBtConnected() && isModemActive()) || (isBtConnected() && !isModemActive())) {
                return (!isBtConnected() || !isWifi24G()) ? 1 : 0;
            } else if (isBtConnected() && isModemActive()) {
                return isWifi24G() ? 1 : 2;
            }
        }
        return 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mReceiver);
        this.mSensorManager.unregisterListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAllEnable(boolean z) {
        Switch r0 = this.mManulMdoeEnabler;
        if (r0 != null) {
            r0.setEnabled(z);
        }
        Switch r02 = this.mModemActiveEnabler;
        if (r02 != null) {
            r02.setEnabled(z);
        }
        Switch r03 = this.mBtConnectedEnabler;
        if (r03 != null) {
            r03.setEnabled(z);
        }
        Switch r04 = this.mSarSensorNearEnabler;
        if (r04 != null) {
            r04.setEnabled(z);
        }
        Switch r05 = this.mEarpieceOnEnabler;
        if (r05 != null) {
            r05.setEnabled(z);
        }
        Switch r06 = this.mWifi24GEnabler;
        if (r06 != null) {
            r06.setEnabled(z);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void enableWifi(boolean z) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return;
        }
        wifiManager.setWifiEnabled(z);
    }

    private void upateSwitchListener(Switch r1, CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean z) {
        if (r1 == null) {
            return;
        }
        r1.setOnCheckedChangeListener(null);
        r1.setChecked(z);
        r1.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
