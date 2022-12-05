package com.android.settings.search;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.search.SearchIndexableResources;
import com.google.android.setupcompat.util.WizardManagerHelper;
/* loaded from: classes.dex */
public interface SearchFeatureProvider {
    Intent buildSearchIntent(Context context, int i);

    SearchIndexableResources getSearchIndexableResources();

    void verifyLaunchSearchResultPageCaller(Context context, ComponentName componentName) throws SecurityException, IllegalArgumentException;

    default String getSettingsIntelligencePkgName(Context context) {
        return context.getString(R.string.config_settingsintelligence_package_name);
    }

    default void initSearchToolbar(final Activity activity, Toolbar toolbar, final int i) {
        if (activity == null || toolbar == null) {
            return;
        }
        if (!WizardManagerHelper.isDeviceProvisioned(activity) || !Utils.isPackageEnabled(activity, getSettingsIntelligencePkgName(activity)) || WizardManagerHelper.isAnySetupWizard(activity.getIntent())) {
            ViewGroup viewGroup = (ViewGroup) toolbar.getParent();
            if (viewGroup == null) {
                return;
            }
            viewGroup.setVisibility(8);
            return;
        }
        View navigationView = toolbar.getNavigationView();
        navigationView.setClickable(false);
        navigationView.setImportantForAccessibility(2);
        navigationView.setBackground(null);
        toolbar.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.search.SearchFeatureProvider$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SearchFeatureProvider.this.lambda$initSearchToolbar$0(activity, i, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* synthetic */ default void lambda$initSearchToolbar$0(Activity activity, int i, View view) {
        Context applicationContext = activity.getApplicationContext();
        Intent buildSearchIntent = buildSearchIntent(applicationContext, i);
        if (activity.getPackageManager().queryIntentActivities(buildSearchIntent, 65536).isEmpty()) {
            return;
        }
        FeatureFactory.getFactory(applicationContext).getSlicesFeatureProvider().indexSliceDataAsync(applicationContext);
        FeatureFactory.getFactory(applicationContext).getMetricsFeatureProvider().logSettingsTileClick("homepage_search_bar", i);
        activity.startActivityForResult(buildSearchIntent, 501, ActivityOptions.makeSceneTransitionAnimation(activity, new Pair[0]).toBundle());
    }
}
