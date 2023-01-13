package com.android.systemui.tuner;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.shared.system.SysUiStatsLog;

public class PowerNotificationControlsFragment extends Fragment {
    private static final String KEY_SHOW_PNC = "show_importance_slider";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C1894R.layout.power_notification_controls_settings, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        String str;
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(C1894R.C1898id.switch_bar);
        final Switch switchR = (Switch) findViewById.findViewById(16908352);
        final TextView textView = (TextView) findViewById.findViewById(C1894R.C1898id.switch_text);
        switchR.setChecked(isEnabled());
        if (isEnabled()) {
            str = getString(C1894R.string.switch_bar_on);
        } else {
            str = getString(C1894R.string.switch_bar_off);
        }
        textView.setText(str);
        switchR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String str;
                boolean z = !PowerNotificationControlsFragment.this.isEnabled();
                MetricsLogger.action(PowerNotificationControlsFragment.this.getContext(), SysUiStatsLog.ACCESSIBILITY_FLOATING_MENU_UI_CHANGED, z);
                Settings.Secure.putInt(PowerNotificationControlsFragment.this.getContext().getContentResolver(), PowerNotificationControlsFragment.KEY_SHOW_PNC, z ? 1 : 0);
                switchR.setChecked(z);
                TextView textView = textView;
                if (z) {
                    str = PowerNotificationControlsFragment.this.getString(C1894R.string.switch_bar_on);
                } else {
                    str = PowerNotificationControlsFragment.this.getString(C1894R.string.switch_bar_off);
                }
                textView.setText(str);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MetricsLogger.visibility(getContext(), 392, true);
    }

    public void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 392, false);
    }

    /* access modifiers changed from: private */
    public boolean isEnabled() {
        return Settings.Secure.getInt(getContext().getContentResolver(), KEY_SHOW_PNC, 0) == 1;
    }
}
