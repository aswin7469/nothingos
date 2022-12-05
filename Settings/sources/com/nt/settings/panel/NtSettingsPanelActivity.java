package com.nt.settings.panel;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NtSettingsPanelActivity extends FragmentActivity {
    final Bundle mBundle = new Bundle();
    boolean mForceCreation = false;
    SettingsPanelFragment mPanelFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getApplicationContext().getTheme().rebase();
        createOrUpdatePanel(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        createOrUpdatePanel(this.mForceCreation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mForceCreation = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        SettingsPanelFragment settingsPanelFragment = this.mPanelFragment;
        if (settingsPanelFragment == null || settingsPanelFragment.isPanelCreating()) {
            return;
        }
        this.mForceCreation = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mForceCreation = true;
    }

    private void createOrUpdatePanel(boolean z) {
        Intent intent = getIntent();
        if (intent == null) {
            Log.e("NtSettingsPanelActivity", "Null intent, closing Panel Activity");
            finish();
            return;
        }
        String action = intent.getAction();
        this.mBundle.putString("NT_PANEL_TYPE_ARGUMENT", action);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        int i = R.id.main_content;
        Fragment findFragmentById = supportFragmentManager.findFragmentById(i);
        if (!z && findFragmentById != null && (findFragmentById instanceof SettingsPanelFragment)) {
            SettingsPanelFragment settingsPanelFragment = (SettingsPanelFragment) findFragmentById;
            this.mPanelFragment = settingsPanelFragment;
            if (settingsPanelFragment.isPanelCreating()) {
                Log.w("NtSettingsPanelActivity", "A panel is creating, skip " + action);
                return;
            }
            Bundle arguments = findFragmentById.getArguments();
            if (arguments != null && TextUtils.equals(action, arguments.getString("NT_PANEL_TYPE_ARGUMENT"))) {
                Log.w("NtSettingsPanelActivity", "Panel is showing the same action, skip " + action);
                return;
            }
            this.mPanelFragment.setArguments(new Bundle(this.mBundle));
            this.mPanelFragment.updatePanelWithAnimation();
            return;
        }
        setContentView(R.layout.activity_settings_panel);
        Window window = getWindow();
        window.setGravity(80);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        int i2 = (int) (point.y * 0.618f);
        int i3 = (int) (point.x * 1.0f);
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        if (configuration.orientation == 2) {
            i2 = (int) (point.y * 1.372f);
            i3 = (int) (point.x * 0.45f);
            Log.d("NtSettingsPanelActivity", "mPanelFragment configuration:" + configuration.orientation);
        }
        Log.d("NtSettingsPanelActivity", "mPanelFragment height:" + i2 + "  width:" + i3);
        window.setLayout(i3, i2);
        window.setDecorFitsSystemWindows(false);
        window.getDecorView().setSystemUiVisibility(4864);
        SettingsPanelFragment settingsPanelFragment2 = new SettingsPanelFragment();
        this.mPanelFragment = settingsPanelFragment2;
        settingsPanelFragment2.setArguments(new Bundle(this.mBundle));
        supportFragmentManager.beginTransaction().add(i, this.mPanelFragment).commit();
    }
}
