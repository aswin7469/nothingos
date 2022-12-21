package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import java.p026io.PrintWriter;

public abstract class CoreStartable implements Dumpable {
    /* access modifiers changed from: protected */
    public final Context mContext;

    public void dump(PrintWriter printWriter, String[] strArr) {
    }

    /* access modifiers changed from: protected */
    public void onBootCompleted() {
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
    }

    public abstract void start();

    public CoreStartable(Context context) {
        this.mContext = context;
    }
}
