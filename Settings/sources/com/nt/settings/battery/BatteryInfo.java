package com.nt.settings.battery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IPowerManager;
import android.os.Message;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.TextView;
import com.android.internal.app.IBatteryStats;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class BatteryInfo extends Activity {
    private IBatteryStats mBatteryStats;
    private TextView mHealth;
    private IntentFilter mIntentFilter;
    private TextView mLevel;
    private TextView mPower;
    private TextView mScale;
    private IPowerManager mScreenStats;
    private TextView mStatus;
    private TextView mTechnology;
    private TextView mTemperature;
    private TextView mUptime;
    private TextView mVoltage;
    private Handler mHandler = new Handler() { // from class: com.nt.settings.battery.BatteryInfo.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            BatteryInfo.this.updateBatteryStats();
            sendEmptyMessageDelayed(1, 1000L);
        }
    };
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.nt.settings.battery.BatteryInfo.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String string;
            if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                int intExtra = intent.getIntExtra("plugged", 0);
                TextView textView = BatteryInfo.this.mLevel;
                textView.setText("" + intent.getIntExtra("level", 0));
                TextView textView2 = BatteryInfo.this.mScale;
                textView2.setText("" + intent.getIntExtra("scale", 0));
                intent.getIntExtra("voltage", 0);
                TextView textView3 = BatteryInfo.this.mVoltage;
                textView3.setText("" + intent.getIntExtra("voltage", 0) + " " + BatteryInfo.this.getString(R.string.battery_info_voltage_units));
                TextView textView4 = BatteryInfo.this.mTemperature;
                textView4.setText("" + BatteryInfo.this.tenthsToFixedString(intent.getIntExtra("temperature", 0)) + BatteryInfo.this.getString(R.string.battery_info_temperature_units));
                TextView textView5 = BatteryInfo.this.mTechnology;
                textView5.setText("" + intent.getStringExtra("technology"));
                TextView textView6 = BatteryInfo.this.mStatus;
                BatteryInfo batteryInfo = BatteryInfo.this;
                textView6.setText(batteryInfo.getBatteryStatus(batteryInfo.getResources(), intent));
                if (intExtra == 0) {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_unplugged));
                } else if (intExtra == 1) {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_ac));
                } else if (intExtra == 2) {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_usb));
                } else if (intExtra == 3) {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_ac_usb));
                } else if (intExtra != 4) {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_unknown));
                } else {
                    BatteryInfo.this.mPower.setText(BatteryInfo.this.getString(R.string.battery_info_power_wireless));
                }
                int intExtra2 = intent.getIntExtra("health", 1);
                if (intExtra2 == 2) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_good);
                } else if (intExtra2 == 3) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_overheat);
                } else if (intExtra2 == 4) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_dead);
                } else if (intExtra2 == 5) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_over_voltage);
                } else if (intExtra2 == 6) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_unspecified_failure);
                } else if (intExtra2 == 7) {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_cold);
                } else {
                    string = BatteryInfo.this.getString(R.string.battery_info_health_unknown);
                }
                BatteryInfo.this.mHealth.setText(string);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public final String tenthsToFixedString(int i) {
        int i2 = i / 10;
        return Integer.toString(i2) + "." + Math.abs(i - (i2 * 10));
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.battery_info);
        setTitle(getString(R.string.battery_info_label));
        IntentFilter intentFilter = new IntentFilter();
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.mStatus = (TextView) findViewById(R.id.status);
        this.mPower = (TextView) findViewById(R.id.power);
        this.mLevel = (TextView) findViewById(R.id.level);
        this.mScale = (TextView) findViewById(R.id.scale);
        this.mHealth = (TextView) findViewById(R.id.health);
        this.mTechnology = (TextView) findViewById(R.id.technology);
        this.mVoltage = (TextView) findViewById(R.id.voltage);
        this.mTemperature = (TextView) findViewById(R.id.temperature);
        this.mUptime = (TextView) findViewById(R.id.uptime);
        this.mBatteryStats = IBatteryStats.Stub.asInterface(ServiceManager.getService("batteryinfo"));
        this.mScreenStats = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
        registerReceiver(this.mIntentReceiver, this.mIntentFilter);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        unregisterReceiver(this.mIntentReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatteryStats() {
        this.mUptime.setText(DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000));
    }

    public String getBatteryStatus(Resources resources, Intent intent) {
        int i;
        int intExtra = intent.getIntExtra("plugged", 0);
        int intExtra2 = intent.getIntExtra("status", 1);
        if (intExtra2 != 2) {
            if (intExtra2 == 3) {
                return resources.getString(R.string.battery_info_status_discharging);
            }
            if (intExtra2 == 4) {
                return resources.getString(R.string.battery_info_status_not_charging);
            }
            if (intExtra2 == 5) {
                return resources.getString(R.string.battery_info_status_full);
            }
            return resources.getString(R.string.battery_info_status_unknown);
        }
        String string = resources.getString(R.string.battery_info_status_charging);
        if (intExtra <= 0) {
            return string;
        }
        if (intExtra == 1) {
            i = R.string.battery_info_status_charging_ac;
        } else if (intExtra == 2) {
            i = R.string.battery_info_status_charging_usb;
        } else {
            i = R.string.battery_info_status_charging_wireless;
        }
        return string + " " + resources.getString(i);
    }
}
