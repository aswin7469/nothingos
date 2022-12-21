package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.shared.plugins.PluginPrefs;

public class TunerFragment extends PreferenceFragment {
    private static final String[] DEBUG_ONLY = {"nav_bar", "lockscreen", "picture_in_picture"};
    private static final String KEY_BATTERY_PCT = "battery_pct";
    private static final CharSequence KEY_DOZE = "doze";
    private static final String KEY_PLUGINS = "plugins";
    private static final int MENU_REMOVE = 2;
    public static final String SETTING_SEEN_TUNER_WARNING = "seen_tuner_warning";
    private static final String TAG = "TunerFragment";
    private static final String WARNING_TAG = "tuner_warning";
    private final TunerService mTunerService;

    public TunerFragment(TunerService tunerService) {
        this.mTunerService = tunerService;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(C1893R.C1901xml.tuner_prefs);
        if (!PluginPrefs.hasPlugins(getContext())) {
            getPreferenceScreen().removePreference(findPreference(KEY_PLUGINS));
        }
        if (!alwaysOnAvailable()) {
            getPreferenceScreen().removePreference(findPreference(KEY_DOZE));
        }
        if (!Build.IS_DEBUGGABLE) {
            int i = 0;
            while (true) {
                String[] strArr = DEBUG_ONLY;
                if (i >= strArr.length) {
                    break;
                }
                Preference findPreference = findPreference(strArr[i]);
                if (findPreference != null) {
                    getPreferenceScreen().removePreference(findPreference);
                }
                i++;
            }
        }
        if (Settings.Secure.getInt(getContext().getContentResolver(), SETTING_SEEN_TUNER_WARNING, 0) == 0 && getFragmentManager().findFragmentByTag(WARNING_TAG) == null) {
            new TunerWarningFragment().show(getFragmentManager(), WARNING_TAG);
        }
    }

    private boolean alwaysOnAvailable() {
        return new AmbientDisplayConfiguration(getContext()).alwaysOnAvailable();
    }

    public void onResume() {
        super.onResume();
        getActivity().setTitle(C1893R.string.system_ui_tuner);
        MetricsLogger.visibility(getContext(), 227, true);
    }

    public void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 227, false);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 2, 0, C1893R.string.remove_from_settings);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 2) {
            this.mTunerService.showResetRequest(new TunerFragment$$ExternalSyntheticLambda0(this));
            return true;
        } else if (itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            getActivity().finish();
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onOptionsItemSelected$0$com-android-systemui-tuner-TunerFragment */
    public /* synthetic */ void mo46448x848f5a5f() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public static class TunerWarningFragment extends DialogFragment {
        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getContext()).setTitle(C1893R.string.tuner_warning_title).setMessage(C1893R.string.tuner_warning).setPositiveButton(C1893R.string.got_it, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Settings.Secure.putInt(TunerWarningFragment.this.getContext().getContentResolver(), TunerFragment.SETTING_SEEN_TUNER_WARNING, 1);
                }
            }).show();
        }
    }
}
