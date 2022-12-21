package com.android.settingslib.license;

import android.content.Context;
import com.android.settingslib.utils.AsyncLoader;
import java.p026io.File;

public class LicenseHtmlLoader extends AsyncLoader<File> {
    private static final String TAG = "LicenseHtmlLoader";
    private Context mContext;

    /* access modifiers changed from: protected */
    public void onDiscardResult(File file) {
    }

    public LicenseHtmlLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    public File loadInBackground() {
        return new LicenseHtmlLoaderCompat(this.mContext).loadInBackground();
    }
}
