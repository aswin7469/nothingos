package com.android.settings.dashboard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R$dimen;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.activityembedding.ActivityEmbeddingUtils;
import com.android.settings.dashboard.profileselector.ProfileSelectDialog;
import com.android.settings.homepage.TopLevelHighlightMixin;
import com.android.settings.homepage.TopLevelSettings;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.drawer.ActivityTile;
import com.android.settingslib.drawer.DashboardCategory;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.drawer.TileUtils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AdaptiveIcon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DashboardFeatureProviderImpl implements DashboardFeatureProvider {
    private final CategoryManager mCategoryManager;
    protected final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final PackageManager mPackageManager;

    public DashboardFeatureProviderImpl(Context context) {
        this.mContext = context.getApplicationContext();
        this.mCategoryManager = CategoryManager.get(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mPackageManager = context.getPackageManager();
    }

    public DashboardCategory getTilesForCategory(String str) {
        return this.mCategoryManager.getTilesByCategory(this.mContext, str);
    }

    public List<DashboardCategory> getAllCategories() {
        return this.mCategoryManager.getCategories(this.mContext);
    }

    public String getDashboardKeyForTile(Tile tile) {
        if (tile == null) {
            return null;
        }
        if (tile.hasKey()) {
            return tile.getKey(this.mContext);
        }
        return "dashboard_tile_pref_" + tile.getIntent().getComponent().getClassName();
    }

    public List<DynamicDataObserver> bindPreferenceToTileAndGetObservers(FragmentActivity fragmentActivity, DashboardFragment dashboardFragment, boolean z, Preference preference, Tile tile, String str, int i) {
        String str2;
        String str3;
        Preference preference2 = preference;
        Tile tile2 = tile;
        int i2 = i;
        if (preference2 == null) {
            return null;
        }
        if (!TextUtils.isEmpty(str)) {
            preference2.setKey(str);
        } else {
            String str4 = str;
            preference2.setKey(getDashboardKeyForTile(tile2));
        }
        ArrayList arrayList = new ArrayList();
        DynamicDataObserver bindTitleAndGetObserver = bindTitleAndGetObserver(preference2, tile2);
        if (bindTitleAndGetObserver != null) {
            arrayList.add(bindTitleAndGetObserver);
        }
        DynamicDataObserver bindSummaryAndGetObserver = bindSummaryAndGetObserver(preference2, tile2);
        if (bindSummaryAndGetObserver != null) {
            arrayList.add(bindSummaryAndGetObserver);
        }
        DynamicDataObserver bindSwitchAndGetObserver = bindSwitchAndGetObserver(preference2, tile2);
        if (bindSwitchAndGetObserver != null) {
            arrayList.add(bindSwitchAndGetObserver);
        }
        bindIcon(preference2, tile2, z);
        if (tile2 instanceof ActivityTile) {
            int metricsCategory = dashboardFragment.getMetricsCategory();
            Bundle metaData = tile.getMetaData();
            if (metaData != null) {
                str2 = metaData.getString("com.android.settings.FRAGMENT_CLASS");
                str3 = metaData.getString("com.android.settings.intent.action");
            } else {
                str3 = null;
                str2 = null;
            }
            if (!TextUtils.isEmpty(str2)) {
                preference2.setFragment(str2);
            } else {
                Intent intent = new Intent(tile.getIntent());
                intent.putExtra(":settings:source_metrics", metricsCategory);
                if (str3 != null) {
                    intent.setAction(str3);
                }
                if (dashboardFragment instanceof TopLevelSettings) {
                    ActivityEmbeddingRulesController.registerTwoPanePairRuleForSettingsHome(this.mContext, new ComponentName(tile.getPackageName(), tile.getComponentName()), str3, true);
                }
                preference2.setOnPreferenceClickListener(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda1(this, dashboardFragment, str, fragmentActivity, tile, intent, metricsCategory));
            }
        }
        if (tile.hasOrder()) {
            String packageName = fragmentActivity.getPackageName();
            int order = tile.getOrder();
            if (TextUtils.equals(packageName, tile.getIntent().getComponent().getPackageName()) || i2 == Integer.MAX_VALUE) {
                preference2.setOrder(order);
            } else {
                preference2.setOrder(order + i2);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$bindPreferenceToTileAndGetObservers$0(DashboardFragment dashboardFragment, String str, FragmentActivity fragmentActivity, Tile tile, Intent intent, int i, Preference preference) {
        boolean z;
        TopLevelHighlightMixin topLevelHighlightMixin;
        if (!(dashboardFragment instanceof TopLevelSettings) || !ActivityEmbeddingUtils.isEmbeddingActivityEnabled(this.mContext)) {
            topLevelHighlightMixin = null;
            z = false;
        } else {
            TopLevelSettings topLevelSettings = (TopLevelSettings) dashboardFragment;
            topLevelHighlightMixin = topLevelSettings.getHighlightMixin();
            z = topLevelSettings.isDuplicateClick(preference);
            topLevelSettings.setHighlightPreferenceKey(str);
        }
        FragmentActivity fragmentActivity2 = fragmentActivity;
        Tile tile2 = tile;
        Intent intent2 = intent;
        int i2 = i;
        launchIntentOrSelectProfile(fragmentActivity2, tile2, intent2, i2, topLevelHighlightMixin, z);
        return true;
    }

    private DynamicDataObserver createDynamicDataObserver(final String str, final Uri uri, final Preference preference) {
        return new DynamicDataObserver() {
            public Uri getUri() {
                return uri;
            }

            public void onDataChanged() {
                String str = str;
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -2097433649:
                        if (str.equals("getDynamicTitle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1844463779:
                        if (str.equals("getDynamicSummary")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 162535197:
                        if (str.equals("isChecked")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        DashboardFeatureProviderImpl.this.refreshTitle(uri, preference, this);
                        return;
                    case 1:
                        DashboardFeatureProviderImpl.this.refreshSummary(uri, preference, this);
                        return;
                    case 2:
                        DashboardFeatureProviderImpl.this.refreshSwitch(uri, preference, this);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    private DynamicDataObserver bindTitleAndGetObserver(Preference preference, Tile tile) {
        CharSequence title = tile.getTitle(this.mContext.getApplicationContext());
        if (title != null) {
            preference.setTitle(title);
            return null;
        } else if (tile.getMetaData() == null || !tile.getMetaData().containsKey("com.android.settings.title_uri")) {
            return null;
        } else {
            preference.setTitle(R$string.summary_placeholder);
            return createDynamicDataObserver("getDynamicTitle", TileUtils.getCompleteUri(tile, "com.android.settings.title_uri", "getDynamicTitle"), preference);
        }
    }

    /* access modifiers changed from: private */
    public void refreshTitle(Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        ThreadUtils.postOnBackgroundThread((Runnable) new DashboardFeatureProviderImpl$$ExternalSyntheticLambda7(this, uri, preference, dynamicDataObserver));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshTitle$2(Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        String textFromUri = TileUtils.getTextFromUri(this.mContext, uri, new ArrayMap(), "com.android.settings.title");
        if (!TextUtils.equals(textFromUri, preference.getTitle())) {
            dynamicDataObserver.post(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda11(preference, textFromUri));
        }
    }

    private DynamicDataObserver bindSummaryAndGetObserver(Preference preference, Tile tile) {
        CharSequence summary = tile.getSummary(this.mContext);
        if (summary != null) {
            preference.setSummary(summary);
            return null;
        } else if (tile.getMetaData() == null || !tile.getMetaData().containsKey("com.android.settings.summary_uri")) {
            return null;
        } else {
            preference.setSummary(R$string.summary_placeholder);
            return createDynamicDataObserver("getDynamicSummary", TileUtils.getCompleteUri(tile, "com.android.settings.summary_uri", "getDynamicSummary"), preference);
        }
    }

    /* access modifiers changed from: private */
    public void refreshSummary(Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        ThreadUtils.postOnBackgroundThread((Runnable) new DashboardFeatureProviderImpl$$ExternalSyntheticLambda6(this, uri, preference, dynamicDataObserver));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSummary$4(Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        String textFromUri = TileUtils.getTextFromUri(this.mContext, uri, new ArrayMap(), "com.android.settings.summary");
        if (!TextUtils.equals(textFromUri, preference.getSummary())) {
            dynamicDataObserver.post(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda9(preference, textFromUri));
        }
    }

    private DynamicDataObserver bindSwitchAndGetObserver(Preference preference, Tile tile) {
        if (!tile.hasSwitch()) {
            return null;
        }
        preference.setOnPreferenceChangeListener(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda3(this, TileUtils.getCompleteUri(tile, "com.android.settings.switch_uri", "onCheckedChanged")));
        Uri completeUri = TileUtils.getCompleteUri(tile, "com.android.settings.switch_uri", "isChecked");
        setSwitchEnabled(preference, false);
        return createDynamicDataObserver("isChecked", completeUri, preference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$bindSwitchAndGetObserver$5(Uri uri, Preference preference, Object obj) {
        onCheckedChanged(uri, preference, ((Boolean) obj).booleanValue());
        return true;
    }

    private void onCheckedChanged(Uri uri, Preference preference, boolean z) {
        setSwitchEnabled(preference, false);
        ThreadUtils.postOnBackgroundThread((Runnable) new DashboardFeatureProviderImpl$$ExternalSyntheticLambda4(this, uri, z, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckedChanged$7(Uri uri, boolean z, Preference preference) {
        ThreadUtils.postOnMainThread(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda8(this, preference, TileUtils.putBooleanToUriAndGetResult(this.mContext, uri, new ArrayMap(), "checked_state", z), z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckedChanged$6(Preference preference, Bundle bundle, boolean z) {
        setSwitchEnabled(preference, true);
        if (bundle.getBoolean("set_checked_error")) {
            setSwitchChecked(preference, !z);
            String string = bundle.getString("set_checked_error_message");
            if (!TextUtils.isEmpty(string)) {
                Toast.makeText(this.mContext, string, 0).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshSwitch(Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        ThreadUtils.postOnBackgroundThread((Runnable) new DashboardFeatureProviderImpl$$ExternalSyntheticLambda5(this, uri, dynamicDataObserver, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSwitch$9(Uri uri, DynamicDataObserver dynamicDataObserver, Preference preference) {
        dynamicDataObserver.post(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda10(this, preference, TileUtils.getBooleanFromUri(this.mContext, uri, new ArrayMap(), "checked_state")));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSwitch$8(Preference preference, boolean z) {
        setSwitchChecked(preference, z);
        setSwitchEnabled(preference, true);
    }

    private void setSwitchChecked(Preference preference, boolean z) {
        if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setChecked(z);
        } else if (preference instanceof SwitchPreference) {
            ((SwitchPreference) preference).setChecked(z);
        }
    }

    private void setSwitchEnabled(Preference preference, boolean z) {
        if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setSwitchEnabled(z);
        } else {
            preference.setEnabled(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void bindIcon(Preference preference, Tile tile, boolean z) {
        if (tile.getMetaData() == null || !tile.getMetaData().containsKey("com.android.settings.icon_uri")) {
            Icon icon = tile.getIcon(preference.getContext());
            if (icon != null) {
                setPreferenceIcon(preference, tile, z, tile.getPackageName(), icon);
                return;
            }
            return;
        }
        if (preference != null && preference.getIcon() == null) {
            setPreferenceIcon(preference, tile, z, this.mContext.getPackageName(), Icon.createWithResource(this.mContext, 17170445));
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new DashboardFeatureProviderImpl$$ExternalSyntheticLambda0(this, tile, preference, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindIcon$11(Tile tile, Preference preference, boolean z) {
        String str;
        Intent intent = tile.getIntent();
        if (!TextUtils.isEmpty(intent.getPackage())) {
            str = intent.getPackage();
        } else {
            str = intent.getComponent() != null ? intent.getComponent().getPackageName() : null;
        }
        ArrayMap arrayMap = new ArrayMap();
        Uri completeUri = TileUtils.getCompleteUri(tile, "com.android.settings.icon_uri", "getProviderIcon");
        Pair<String, Integer> iconFromUri = TileUtils.getIconFromUri(this.mContext, str, completeUri, arrayMap);
        if (iconFromUri == null) {
            Log.w("DashboardFeatureImpl", "Failed to get icon from uri " + completeUri);
            return;
        }
        ThreadUtils.postOnMainThread(new DashboardFeatureProviderImpl$$ExternalSyntheticLambda2(this, preference, tile, z, iconFromUri, Icon.createWithResource((String) iconFromUri.first, ((Integer) iconFromUri.second).intValue())));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindIcon$10(Preference preference, Tile tile, boolean z, Pair pair, Icon icon) {
        setPreferenceIcon(preference, tile, z, (String) pair.first, icon);
    }

    private void setPreferenceIcon(Preference preference, Tile tile, boolean z, String str, Icon icon) {
        Drawable loadDrawable = icon.loadDrawable(preference.getContext());
        if (loadDrawable == null) {
            Log.w("DashboardFeatureImpl", "Set null preference icon for: " + str);
            preference.setIcon((Drawable) null);
            return;
        }
        if (TextUtils.equals(tile.getCategory(), "com.android.settings.category.ia.homepage")) {
            loadDrawable.setTint(Utils.getHomepageIconColor(preference.getContext()));
        } else if (z && !TextUtils.equals(this.mContext.getPackageName(), str)) {
            AdaptiveIcon adaptiveIcon = new AdaptiveIcon(this.mContext, loadDrawable, R$dimen.dashboard_tile_foreground_image_inset);
            adaptiveIcon.setBackgroundColor(this.mContext, tile);
            loadDrawable = adaptiveIcon;
        }
        preference.setIcon(loadDrawable);
    }

    private void launchIntentOrSelectProfile(FragmentActivity fragmentActivity, Tile tile, Intent intent, int i, TopLevelHighlightMixin topLevelHighlightMixin, boolean z) {
        if (!isIntentResolvable(intent)) {
            Log.w("DashboardFeatureImpl", "Cannot resolve intent, skipping. " + intent);
            return;
        }
        ProfileSelectDialog.updateUserHandlesIfNeeded(this.mContext, tile);
        if (tile.userHandle == null || tile.isPrimaryProfileOnly()) {
            if (!z) {
                this.mMetricsFeatureProvider.logStartedIntent(intent, i);
                fragmentActivity.startActivity(intent);
            }
        } else if (tile.userHandle.size() != 1) {
            UserHandle userHandle = (UserHandle) intent.getParcelableExtra("android.intent.extra.USER");
            if (userHandle == null || !tile.userHandle.contains(userHandle)) {
                List<UserHandle> resolvableUsers = getResolvableUsers(intent, tile);
                if (resolvableUsers.size() != 1) {
                    this.mMetricsFeatureProvider.logStartedIntent(intent, i);
                    ProfileSelectDialog.show(fragmentActivity.getSupportFragmentManager(), tile, i, topLevelHighlightMixin, topLevelHighlightMixin, topLevelHighlightMixin);
                } else if (!z) {
                    this.mMetricsFeatureProvider.logStartedIntent(intent, i);
                    fragmentActivity.startActivityAsUser(intent, resolvableUsers.get(0));
                }
            } else if (!z) {
                this.mMetricsFeatureProvider.logStartedIntent(intent, i);
                fragmentActivity.startActivityAsUser(intent, userHandle);
            }
        } else if (!z) {
            this.mMetricsFeatureProvider.logStartedIntent(intent, i);
            fragmentActivity.startActivityAsUser(intent, tile.userHandle.get(0));
        }
    }

    private boolean isIntentResolvable(Intent intent) {
        return this.mPackageManager.resolveActivity(intent, 0) != null;
    }

    private List<UserHandle> getResolvableUsers(Intent intent, Tile tile) {
        ArrayList arrayList = new ArrayList();
        Iterator<UserHandle> it = tile.userHandle.iterator();
        while (it.hasNext()) {
            UserHandle next = it.next();
            if (this.mPackageManager.resolveActivityAsUser(intent, 0, next.getIdentifier()) != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
