package com.nothing.settings.privacy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class DataCollectionProgramFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.data_collection_program_fragment);

    public String getLogTag() {
        return "NothingAgreement";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getPreferenceScreenResId() {
        Log.d(getLogTag(), "DataCollectionProgramFragment getPreferenceScreenResId");
        return R$xml.data_collection_program_fragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(getLogTag(), "DataCollectionProgramFragment onCreate");
        showPrograms();
    }

    private void showPrograms() {
        Intent intent = new Intent("nothing.intent.action.ACTION_VIEW_AGREEMENT");
        intent.putExtra("ACTION_KEY_LOAD_URL_TYPE", "LOAD_URL_USER_EXPERIENCE_PROGRAM");
        intent.setPackage("com.nothing.agreement");
        try {
            startActivity(intent);
            finish();
        } catch (ActivityNotFoundException e) {
            Log.e(getLogTag(), "Failed to find viewer", e);
        }
    }
}
