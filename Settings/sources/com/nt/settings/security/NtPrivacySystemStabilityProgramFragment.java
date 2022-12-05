package com.nt.settings.security;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes2.dex */
public class NtPrivacySystemStabilityProgramFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_privacy_system_stability_program_fragment);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NtPrivacySystemStabProg";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_privacy_system_stability_program_fragment;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        InputStream openRawResource = getResources().openRawResource(getResources().getIdentifier(getResources().getString(R.string.nt_system_stability_program), "raw", getContext().getPackageName()));
        File cachedHtmlFile = getCachedHtmlFile(getContext());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(cachedHtmlFile);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openRawResource.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    openRawResource.close();
                    fileOutputStream.close();
                    showHtmlFromUri(FileProvider.getUriForFile(getContext(), "com.android.settings.files", cachedHtmlFile));
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("NtPrivacySystemStabProg", "@_@ ------ FileNotFoundException", e);
        } catch (IOException e2) {
            Log.e("NtPrivacySystemStabProg", "@_@ ------ IOException", e2);
        }
    }

    private File getCachedHtmlFile(Context context) {
        return new File(context.getCacheDir(), "nt_system_stability_program.html");
    }

    private void showHtmlFromUri(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        intent.putExtra("android.intent.extra.TITLE", getString(R.string.nt_privacy_system_stability_program_title));
        if ("content".equals(uri.getScheme())) {
            intent.addFlags(1);
        }
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.android.htmlviewer");
        try {
            startActivity(intent);
            finish();
        } catch (ActivityNotFoundException e) {
            Log.e("NtPrivacySystemStabProg", "Failed to find viewer", e);
        }
    }
}
