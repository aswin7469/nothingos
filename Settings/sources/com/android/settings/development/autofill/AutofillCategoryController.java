package com.android.settings.development.autofill;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.autofill.AutofillManager;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class AutofillCategoryController extends DeveloperOptionsPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private ContentObserver mSettingsObserver;

    public String getPreferenceKey() {
        return "debug_autofill_category";
    }

    public AutofillCategoryController(Context context, Lifecycle lifecycle) {
        super(context);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        this.mSettingsObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Uri uri, int i) {
                AutofillCategoryController.this.mHandler.postDelayed(new AutofillCategoryController$1$$ExternalSyntheticLambda0(this), 2000);
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onChange$0() {
                AutofillCategoryController.this.mPreference.notifyDependencyChange(AutofillCategoryController.this.shouldDisableDependents());
            }
        };
        this.mContentResolver = context.getContentResolver();
    }

    public void onStart() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("autofill_service"), false, this.mSettingsObserver);
    }

    public void onStop() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
    }

    private boolean isAutofillEnabled() {
        AutofillManager autofillManager = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
        boolean z = autofillManager != null && autofillManager.isEnabled();
        Log.v("AutofillCategoryController", "isAutofillEnabled(): " + z);
        return z;
    }

    /* access modifiers changed from: private */
    public boolean shouldDisableDependents() {
        boolean z = !isAutofillEnabled();
        Log.v("AutofillCategoryController", "shouldDisableDependents(): " + z);
        return z;
    }
}
