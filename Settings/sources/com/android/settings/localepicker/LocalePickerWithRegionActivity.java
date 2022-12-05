package com.android.settings.localepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.android.internal.app.LocalePickerWithRegion;
import com.android.internal.app.LocaleStore;
import java.io.Serializable;
/* loaded from: classes.dex */
public class LocalePickerWithRegionActivity extends Activity implements LocalePickerWithRegion.LocaleSelectedListener {
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().setTransition(4097).replace(16908290, LocalePickerWithRegion.createLanguagePicker(this, this, false)).addToBackStack("localeListEditor").commit();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            handleBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onLocaleSelected(LocaleStore.LocaleInfo localeInfo) {
        Intent intent = new Intent();
        intent.putExtra("localeInfo", (Serializable) localeInfo);
        setResult(-1, intent);
        finish();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        handleBackPressed();
    }

    private void handleBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
            return;
        }
        setResult(0);
        finish();
    }
}
