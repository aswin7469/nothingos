package com.android.systemui.tuner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.fragments.FragmentService;
import javax.inject.Inject;

public class TunerActivity extends Activity implements PreferenceFragment.OnPreferenceStartFragmentCallback, PreferenceFragment.OnPreferenceStartScreenCallback {
    private static final String TAG_TUNER = "tuner";
    private final DemoModeController mDemoModeController;
    private final TunerService mTunerService;

    @Inject
    TunerActivity(DemoModeController demoModeController, TunerService tunerService) {
        this.mDemoModeController = demoModeController;
        this.mTunerService = tunerService;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Fragment fragment;
        super.onCreate(bundle);
        setTheme(2132018078);
        getWindow().addFlags(Integer.MIN_VALUE);
        boolean z = true;
        requestWindowFeature(1);
        setContentView(C1894R.layout.tuner_activity);
        Toolbar toolbar = (Toolbar) findViewById(C1894R.C1898id.action_bar);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        if (getFragmentManager().findFragmentByTag(TAG_TUNER) == null) {
            String action = getIntent().getAction();
            if (action == null || !action.equals("com.android.settings.action.DEMO_MODE")) {
                z = false;
            }
            if (z) {
                fragment = new DemoModeFragment(this.mDemoModeController);
            } else {
                fragment = new TunerFragment(this.mTunerService);
            }
            getFragmentManager().beginTransaction().replace(C1894R.C1898id.content_frame, fragment, TAG_TUNER).commit();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Dependency.destroy(FragmentService.class, new TunerActivity$$ExternalSyntheticLambda0());
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onMenuItemSelected(i, menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    public boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference) {
        try {
            Fragment fragment = (Fragment) Class.forName(preference.getFragment()).newInstance();
            Bundle bundle = new Bundle(1);
            bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preference.getKey());
            fragment.setArguments(bundle);
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            setTitle(preference.getTitle());
            beginTransaction.replace(C1894R.C1898id.content_frame, fragment);
            beginTransaction.addToBackStack("PreferenceFragment");
            beginTransaction.commit();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Log.d("TunerActivity", "Problem launching fragment", e);
            return false;
        }
    }

    public boolean onPreferenceStartScreen(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        SubSettingsFragment subSettingsFragment = new SubSettingsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preferenceScreen.getKey());
        subSettingsFragment.setArguments(bundle);
        subSettingsFragment.setTargetFragment(preferenceFragment, 0);
        beginTransaction.replace(C1894R.C1898id.content_frame, subSettingsFragment);
        beginTransaction.addToBackStack("PreferenceFragment");
        beginTransaction.commit();
        return true;
    }

    public static class SubSettingsFragment extends PreferenceFragment {
        private PreferenceScreen mParentScreen;

        public void onCreatePreferences(Bundle bundle, String str) {
            this.mParentScreen = (PreferenceScreen) ((PreferenceFragment) getTargetFragment()).getPreferenceScreen().findPreference(str);
            PreferenceScreen createPreferenceScreen = getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext());
            setPreferenceScreen(createPreferenceScreen);
            while (this.mParentScreen.getPreferenceCount() > 0) {
                Preference preference = this.mParentScreen.getPreference(0);
                this.mParentScreen.removePreference(preference);
                createPreferenceScreen.addPreference(preference);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            while (preferenceScreen.getPreferenceCount() > 0) {
                Preference preference = preferenceScreen.getPreference(0);
                preferenceScreen.removePreference(preference);
                this.mParentScreen.addPreference(preference);
            }
        }
    }
}
