package com.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
/* loaded from: classes.dex */
public class SmqSettings {
    private Context mContext;
    private SharedPreferences mSmqPreferences;

    public SmqSettings(Context context) {
        this.mContext = context;
        new DBReadAsyncTask(this.mContext).execute(new Void[0]);
        this.mSmqPreferences = this.mContext.getSharedPreferences("smqpreferences", 0);
    }
}
