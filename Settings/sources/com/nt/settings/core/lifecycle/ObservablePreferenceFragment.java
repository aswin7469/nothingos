package com.nt.settings.core.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
/* loaded from: classes2.dex */
public abstract class ObservablePreferenceFragment extends PreferenceFragment {
    private final Lifecycle mLifecycle = new Lifecycle();

    public abstract void onCreatePreferences(Bundle bundle, String str);

    @Override // android.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mLifecycle.onAttach(context);
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        this.mLifecycle.onCreate(bundle);
        super.onCreate(bundle);
        onCreatePreferences(bundle, "");
    }

    @Override // android.preference.PreferenceFragment
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        this.mLifecycle.setPreferenceScreen(preferenceScreen);
        super.setPreferenceScreen(preferenceScreen);
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mLifecycle.onSaveInstanceState(bundle);
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        this.mLifecycle.onStart();
        super.onStart();
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        this.mLifecycle.onStop();
        super.onStop();
    }

    @Override // android.app.Fragment
    public void onResume() {
        this.mLifecycle.onResume();
        super.onResume();
    }

    @Override // android.app.Fragment
    public void onPause() {
        this.mLifecycle.onPause();
        super.onPause();
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        this.mLifecycle.onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        this.mLifecycle.onPrepareOptionsMenu(menu);
        super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean onOptionsItemSelected = this.mLifecycle.onOptionsItemSelected(menuItem);
        return !onOptionsItemSelected ? super.onOptionsItemSelected(menuItem) : onOptionsItemSelected;
    }
}
