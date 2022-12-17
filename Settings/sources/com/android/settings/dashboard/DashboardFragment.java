package com.android.settings.dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$array;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.bluetooth.BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.CategoryMixin;
import com.android.settings.core.PreferenceControllerListHelper;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.drawer.DashboardCategory;
import com.android.settingslib.drawer.ProviderTile;
import com.android.settingslib.drawer.Tile;
import com.nothing.p006ui.support.NtCustSwitchPreference;
import com.nothing.settings.utils.NtSettingsVibrateUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class DashboardFragment extends SettingsPreferenceFragment implements CategoryMixin.CategoryListener, PreferenceGroup.OnExpandButtonClickListener, BasePreferenceController.UiBlockListener {
    public static final String CATEGORY = "category";
    private static final String TAG = "DashboardFragment";
    private static final long TIMEOUT_MILLIS = 50;
    UiBlockerController mBlockerController;
    private final List<AbstractPreferenceController> mControllers = new ArrayList();
    private DashboardFeatureProvider mDashboardFeatureProvider;
    final ArrayMap<String, List<DynamicDataObserver>> mDashboardTilePrefKeys = new ArrayMap<>();
    private boolean mListeningToCategoryChange;
    private DashboardTilePlaceholderPreferenceController mPlaceholderPreferenceController;
    private final Map<Class, List<AbstractPreferenceController>> mPreferenceControllers = new ArrayMap();
    private final List<DynamicDataObserver> mRegisteredObservers = new ArrayList();
    private List<String> mSuppressInjectedTileKeys;

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract String getLogTag();

    /* access modifiers changed from: protected */
    public abstract int getPreferenceScreenResId();

    /* access modifiers changed from: protected */
    public boolean shouldForceRoundedIcon() {
        return false;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSuppressInjectedTileKeys = Arrays.asList(context.getResources().getStringArray(R$array.config_suppress_injected_tile_keys));
        this.mDashboardFeatureProvider = FeatureFactory.getFactory(context).getDashboardFeatureProvider(context);
        List<AbstractPreferenceController> createPreferenceControllers = createPreferenceControllers(context);
        List<BasePreferenceController> filterControllers = PreferenceControllerListHelper.filterControllers(PreferenceControllerListHelper.getPreferenceControllersFromXml(context, getPreferenceScreenResId()), createPreferenceControllers);
        if (createPreferenceControllers != null) {
            this.mControllers.addAll(createPreferenceControllers);
        }
        this.mControllers.addAll(filterControllers);
        filterControllers.forEach(new DashboardFragment$$ExternalSyntheticLambda14(getSettingsLifecycle()));
        this.mControllers.forEach(new DashboardFragment$$ExternalSyntheticLambda15(getMetricsCategory()));
        DashboardTilePlaceholderPreferenceController dashboardTilePlaceholderPreferenceController = new DashboardTilePlaceholderPreferenceController(context);
        this.mPlaceholderPreferenceController = dashboardTilePlaceholderPreferenceController;
        this.mControllers.add(dashboardTilePlaceholderPreferenceController);
        for (AbstractPreferenceController addPreferenceController : this.mControllers) {
            addPreferenceController(addPreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onAttach$0(Lifecycle lifecycle, BasePreferenceController basePreferenceController) {
        if (basePreferenceController instanceof LifecycleObserver) {
            lifecycle.addObserver((LifecycleObserver) basePreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onAttach$1(int i, AbstractPreferenceController abstractPreferenceController) {
        if (abstractPreferenceController instanceof BasePreferenceController) {
            ((BasePreferenceController) abstractPreferenceController).setMetricsCategory(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkUiBlocker(List<AbstractPreferenceController> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        list.forEach(new DashboardFragment$$ExternalSyntheticLambda1(this, arrayList, arrayList2));
        if (!arrayList.isEmpty()) {
            UiBlockerController uiBlockerController = new UiBlockerController(arrayList);
            this.mBlockerController = uiBlockerController;
            uiBlockerController.start(new DashboardFragment$$ExternalSyntheticLambda2(this, arrayList2));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$2(List list, List list2, AbstractPreferenceController abstractPreferenceController) {
        if ((abstractPreferenceController instanceof BasePreferenceController.UiBlocker) && abstractPreferenceController.isAvailable()) {
            BasePreferenceController basePreferenceController = (BasePreferenceController) abstractPreferenceController;
            basePreferenceController.setUiBlockListener(this);
            list.add(abstractPreferenceController.getPreferenceKey());
            list2.add(basePreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$4(List list) {
        updatePreferenceVisibility(this.mPreferenceControllers);
        list.forEach(new DashboardFragment$$ExternalSyntheticLambda0());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getPreferenceManager().setPreferenceComparisonCallback(new PreferenceManager.SimplePreferenceComparisonCallback());
        if (bundle != null) {
            updatePreferenceStates();
        }
    }

    public void onCategoriesChanged(Set<String> set) {
        String categoryKey = getCategoryKey();
        if (this.mDashboardFeatureProvider.getTilesForCategory(categoryKey) != null) {
            if (set == null) {
                refreshDashboardTiles(getLogTag());
            } else if (set.contains(categoryKey)) {
                Log.i(TAG, "refresh tiles for " + categoryKey);
                refreshDashboardTiles(getLogTag());
            }
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        checkUiBlocker(this.mControllers);
        refreshAllPreferences(getLogTag());
        this.mControllers.stream().map(new DashboardFragment$$ExternalSyntheticLambda9(this)).filter(new DashboardFragment$$ExternalSyntheticLambda10()).forEach(new DashboardFragment$$ExternalSyntheticLambda11(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Preference lambda$onCreatePreferences$5(AbstractPreferenceController abstractPreferenceController) {
        return findPreference(abstractPreferenceController.getPreferenceKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePreferences$6(Preference preference) {
        preference.getExtras().putInt(CATEGORY, getMetricsCategory());
    }

    public void onStart() {
        super.onStart();
        if (this.mDashboardFeatureProvider.getTilesForCategory(getCategoryKey()) != null) {
            FragmentActivity activity = getActivity();
            if (activity instanceof CategoryMixin.CategoryHandler) {
                this.mListeningToCategoryChange = true;
                ((CategoryMixin.CategoryHandler) activity).getCategoryMixin().addCategoryListener(this);
            }
            this.mDashboardTilePrefKeys.values().stream().filter(new DashboardFragment$$ExternalSyntheticLambda4()).flatMap(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2()).forEach(new DashboardFragment$$ExternalSyntheticLambda5(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$7(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        if (!this.mRegisteredObservers.contains(dynamicDataObserver)) {
            lambda$registerDynamicDataObservers$11(contentResolver, dynamicDataObserver);
        }
    }

    public void onResume() {
        super.onResume();
        updatePreferenceStates();
        writeElapsedTimeMetric(1729, "isParalleledControllers:false");
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference instanceof SwitchPreference) {
            NtSettingsVibrateUtils.getInstance(getPrefContext()).playSwitchVibrate();
        }
        for (List<AbstractPreferenceController> it : this.mPreferenceControllers.values()) {
            Iterator it2 = it.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (((AbstractPreferenceController) it2.next()).handlePreferenceTreeClick(preference)) {
                        writePreferenceClickMetric(preference);
                        return true;
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void onStop() {
        super.onStop();
        unregisterDynamicDataObservers(new ArrayList(this.mRegisteredObservers));
        if (this.mListeningToCategoryChange) {
            FragmentActivity activity = getActivity();
            if (activity instanceof CategoryMixin.CategoryHandler) {
                ((CategoryMixin.CategoryHandler) activity).getCategoryMixin().removeCategoryListener(this);
            }
            this.mListeningToCategoryChange = false;
        }
    }

    public void onExpandButtonClick() {
        this.mMetricsFeatureProvider.action(0, 834, getMetricsCategory(), (String) null, 0);
    }

    /* access modifiers changed from: protected */
    public <T extends AbstractPreferenceController> T use(Class<T> cls) {
        List list = this.mPreferenceControllers.get(cls);
        if (list == null) {
            return null;
        }
        if (list.size() > 1) {
            Log.w(TAG, "Multiple controllers of Class " + cls.getSimpleName() + " found, returning first one.");
        }
        return (AbstractPreferenceController) list.get(0);
    }

    /* access modifiers changed from: protected */
    public <T extends AbstractPreferenceController> List<T> useAll(Class<T> cls) {
        return this.mPreferenceControllers.getOrDefault(cls, Collections.emptyList());
    }

    /* access modifiers changed from: protected */
    public void addPreferenceController(AbstractPreferenceController abstractPreferenceController) {
        if (this.mPreferenceControllers.get(abstractPreferenceController.getClass()) == null) {
            this.mPreferenceControllers.put(abstractPreferenceController.getClass(), new ArrayList());
        }
        this.mPreferenceControllers.get(abstractPreferenceController.getClass()).add(abstractPreferenceController);
    }

    public String getCategoryKey() {
        return DashboardFragmentRegistry.PARENT_TO_CATEGORY_KEY_MAP.get(getClass().getName());
    }

    /* access modifiers changed from: protected */
    public boolean displayTile(Tile tile) {
        if (this.mSuppressInjectedTileKeys == null || !tile.hasKey()) {
            return true;
        }
        return !this.mSuppressInjectedTileKeys.contains(tile.getKey(getContext()));
    }

    private void displayResourceTiles() {
        int preferenceScreenResId = getPreferenceScreenResId();
        if (preferenceScreenResId > 0) {
            addPreferencesFromResource(preferenceScreenResId);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.setOnExpandButtonClickListener(this);
            displayResourceTilesToScreen(preferenceScreen);
        }
    }

    /* access modifiers changed from: protected */
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        this.mPreferenceControllers.values().stream().flatMap(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2()).forEach(new DashboardFragment$$ExternalSyntheticLambda13(preferenceScreen));
    }

    /* access modifiers changed from: protected */
    public Collection<List<AbstractPreferenceController>> getPreferenceControllers() {
        return this.mPreferenceControllers.values();
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceStates() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        for (List<AbstractPreferenceController> it : this.mPreferenceControllers.values()) {
            for (AbstractPreferenceController abstractPreferenceController : it) {
                if (abstractPreferenceController.isAvailable()) {
                    String preferenceKey = abstractPreferenceController.getPreferenceKey();
                    if (TextUtils.isEmpty(preferenceKey)) {
                        Log.d(TAG, String.format("Preference key is %s in Controller %s", new Object[]{preferenceKey, abstractPreferenceController.getClass().getSimpleName()}));
                    } else {
                        Preference findPreference = preferenceScreen.findPreference(preferenceKey);
                        if (findPreference == null) {
                            Log.d(TAG, String.format("Cannot find preference with key %s in Controller %s", new Object[]{preferenceKey, abstractPreferenceController.getClass().getSimpleName()}));
                        } else {
                            abstractPreferenceController.updateState(findPreference);
                        }
                    }
                }
            }
        }
    }

    private void refreshAllPreferences(String str) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        displayResourceTiles();
        refreshDashboardTiles(str);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Log.d(str, "All preferences added, reporting fully drawn");
            activity.reportFullyDrawn();
        }
        updatePreferenceVisibility(this.mPreferenceControllers);
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceVisibility(Map<Class, List<AbstractPreferenceController>> map) {
        UiBlockerController uiBlockerController;
        if (getPreferenceScreen() != null && map != null && (uiBlockerController = this.mBlockerController) != null) {
            boolean isBlockerFinished = uiBlockerController.isBlockerFinished();
            for (List<AbstractPreferenceController> it : map.values()) {
                for (AbstractPreferenceController abstractPreferenceController : it) {
                    Preference findPreference = findPreference(abstractPreferenceController.getPreferenceKey());
                    if (findPreference != null) {
                        boolean z = true;
                        if (abstractPreferenceController instanceof BasePreferenceController.UiBlocker) {
                            boolean savedPrefVisibility = ((BasePreferenceController) abstractPreferenceController).getSavedPrefVisibility();
                            if (!isBlockerFinished || !abstractPreferenceController.isAvailable() || !savedPrefVisibility) {
                                z = false;
                            }
                            findPreference.setVisible(z);
                        } else {
                            if (!isBlockerFinished || !abstractPreferenceController.isAvailable()) {
                                z = false;
                            }
                            findPreference.setVisible(z);
                        }
                    }
                }
            }
        }
    }

    private void refreshDashboardTiles(String str) {
        boolean z;
        List<DynamicDataObserver> list;
        String str2 = str;
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        DashboardCategory tilesForCategory = this.mDashboardFeatureProvider.getTilesForCategory(getCategoryKey());
        if (tilesForCategory == null) {
            Log.d(str2, "NO dashboard tiles for " + str2);
            return;
        }
        List<Tile> tiles = tilesForCategory.getTiles();
        if (tiles == null) {
            Log.d(str2, "tile list is empty, skipping category " + tilesForCategory.key);
            return;
        }
        ArrayMap arrayMap = new ArrayMap(this.mDashboardTilePrefKeys);
        boolean shouldForceRoundedIcon = shouldForceRoundedIcon();
        ArrayList arrayList = new ArrayList();
        for (Tile next : tiles) {
            String dashboardKeyForTile = this.mDashboardFeatureProvider.getDashboardKeyForTile(next);
            if (TextUtils.isEmpty(dashboardKeyForTile)) {
                Log.d(str2, "tile does not contain a key, skipping " + next);
            } else if (displayTile(next)) {
                if (this.mDashboardTilePrefKeys.containsKey(dashboardKeyForTile)) {
                    Preference findPreference = preferenceScreen.findPreference(dashboardKeyForTile);
                    list = this.mDashboardFeatureProvider.bindPreferenceToTileAndGetObservers(getActivity(), this, shouldForceRoundedIcon, findPreference, next, dashboardKeyForTile, this.mPlaceholderPreferenceController.getOrder());
                    z = shouldForceRoundedIcon;
                } else {
                    Preference createPreference = createPreference(next);
                    z = shouldForceRoundedIcon;
                    list = this.mDashboardFeatureProvider.bindPreferenceToTileAndGetObservers(getActivity(), this, shouldForceRoundedIcon, createPreference, next, dashboardKeyForTile, this.mPlaceholderPreferenceController.getOrder());
                    preferenceScreen.addPreference(createPreference);
                    registerDynamicDataObservers(list);
                    this.mDashboardTilePrefKeys.put(dashboardKeyForTile, list);
                }
                if (list != null) {
                    arrayList.addAll(list);
                }
                arrayMap.remove(dashboardKeyForTile);
                shouldForceRoundedIcon = z;
            }
        }
        for (Map.Entry entry : arrayMap.entrySet()) {
            String str3 = (String) entry.getKey();
            this.mDashboardTilePrefKeys.remove(str3);
            Preference findPreference2 = preferenceScreen.findPreference(str3);
            if (findPreference2 != null) {
                preferenceScreen.removePreference(findPreference2);
            }
            unregisterDynamicDataObservers((List) entry.getValue());
        }
        if (!arrayList.isEmpty()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            new Thread(new DashboardFragment$$ExternalSyntheticLambda7(this, arrayList, countDownLatch)).start();
            Log.d(str2, "Start waiting observers");
            awaitObserverLatch(countDownLatch);
            Log.d(str2, "Stop waiting observers");
            arrayList.forEach(new DashboardFragment$$ExternalSyntheticLambda8());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshDashboardTiles$10(List list, CountDownLatch countDownLatch) {
        list.forEach(new DashboardFragment$$ExternalSyntheticLambda3(this));
        countDownLatch.countDown();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshDashboardTiles$9(DynamicDataObserver dynamicDataObserver) {
        awaitObserverLatch(dynamicDataObserver.getCountDownLatch());
    }

    public void onBlockerWorkFinished(BasePreferenceController basePreferenceController) {
        this.mBlockerController.countDown(basePreferenceController.getPreferenceKey());
        basePreferenceController.setUiBlockerFinished(this.mBlockerController.isBlockerFinished());
    }

    /* access modifiers changed from: protected */
    public Preference createPreference(Tile tile) {
        if (tile instanceof ProviderTile) {
            return new NtCustSwitchPreference(getPrefContext());
        }
        if (tile.hasSwitch()) {
            return new PrimarySwitchPreference(getPrefContext());
        }
        return new Preference(getPrefContext());
    }

    /* access modifiers changed from: package-private */
    public void registerDynamicDataObservers(List<DynamicDataObserver> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(new DashboardFragment$$ExternalSyntheticLambda12(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: registerDynamicDataObserver */
    public void lambda$registerDynamicDataObservers$11(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        Log.d(TAG, "register observer: @" + Integer.toHexString(dynamicDataObserver.hashCode()) + ", uri: " + dynamicDataObserver.getUri());
        contentResolver.registerContentObserver(dynamicDataObserver.getUri(), false, dynamicDataObserver);
        this.mRegisteredObservers.add(dynamicDataObserver);
    }

    private void unregisterDynamicDataObservers(List<DynamicDataObserver> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(new DashboardFragment$$ExternalSyntheticLambda6(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterDynamicDataObservers$12(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        Log.d(TAG, "unregister observer: @" + Integer.toHexString(dynamicDataObserver.hashCode()) + ", uri: " + dynamicDataObserver.getUri());
        this.mRegisteredObservers.remove(dynamicDataObserver);
        contentResolver.unregisterContentObserver(dynamicDataObserver);
    }

    private void awaitObserverLatch(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
        }
    }
}
