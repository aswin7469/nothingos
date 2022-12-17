package com.android.settings.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexableRaw;
import java.util.ArrayList;
import java.util.List;

public class FlashlightHandleActivity extends Activity {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean z) {
            ArrayList arrayList = new ArrayList();
            SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(context);
            int i = R$string.power_flashlight;
            searchIndexableRaw.title = context.getString(i);
            searchIndexableRaw.screenTitle = context.getString(i);
            searchIndexableRaw.keywords = context.getString(R$string.keywords_flashlight);
            searchIndexableRaw.intentTargetPackage = context.getPackageName();
            searchIndexableRaw.intentTargetClass = FlashlightHandleActivity.class.getName();
            searchIndexableRaw.intentAction = "android.intent.action.MAIN";
            searchIndexableRaw.key = "flashlight";
            arrayList.add(searchIndexableRaw);
            return arrayList;
        }

        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            if (!FlashlightSlice.isFlashlightAvailable(context)) {
                Log.i("FlashlightActivity", "Flashlight is unavailable");
                nonIndexableKeys.add("flashlight");
            }
            return nonIndexableKeys;
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent().getBooleanExtra("fallback_to_homepage", false)) {
            startActivity(new Intent("android.settings.SETTINGS"));
        }
        finish();
    }
}
