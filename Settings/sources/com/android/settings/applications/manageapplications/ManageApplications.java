package com.android.settings.applications.manageapplications;

import android.app.ActivityManager;
import android.app.usage.IUsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceFrameLayout;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.compat.IPlatformCompat;
import com.android.settings.R$id;
import com.android.settings.R$integer;
import com.android.settings.R$layout;
import com.android.settings.R$menu;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.applications.AppInfoBase;
import com.android.settings.applications.AppStateAlarmsAndRemindersBridge;
import com.android.settings.applications.AppStateAppOpsBridge;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.applications.AppStateInstallAppsBridge;
import com.android.settings.applications.AppStateLocaleBridge;
import com.android.settings.applications.AppStateManageExternalStorageBridge;
import com.android.settings.applications.AppStateMediaManagementAppsBridge;
import com.android.settings.applications.AppStateNotificationBridge;
import com.android.settings.applications.AppStateOverlayBridge;
import com.android.settings.applications.AppStatePowerBridge;
import com.android.settings.applications.AppStateUsageBridge;
import com.android.settings.applications.AppStateWriteSettingsBridge;
import com.android.settings.applications.AppStorageSettings;
import com.android.settings.applications.UsageAccessDetails;
import com.android.settings.applications.appinfo.AlarmsAndRemindersDetails;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.applications.appinfo.AppLocaleDetails;
import com.android.settings.applications.appinfo.DrawOverlayDetails;
import com.android.settings.applications.appinfo.ExternalSourcesDetails;
import com.android.settings.applications.appinfo.ManageExternalStorageDetails;
import com.android.settings.applications.appinfo.MediaManagementAppsDetails;
import com.android.settings.applications.appinfo.WriteSettingsDetails;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.fuelgauge.HighPowerDetail;
import com.android.settings.localepicker.AppLocalePickerActivity;
import com.android.settings.notification.ConfigureNotificationSettings;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.AppNotificationSettings;
import com.android.settings.widget.LoadingViewController;
import com.android.settings.wifi.AppStateChangeWifiStateBridge;
import com.android.settings.wifi.ChangeWifiStateDetails;
import com.android.settingslib.applications.AppIconCacheManager;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.SettingsSpinnerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.nothing.settings.glyphs.notification.AppNotificationConversationFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ManageApplications extends InstrumentedFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    static final boolean DEBUG = Build.IS_DEBUGGABLE;
    static final String EXTRA_EXPAND_SEARCH_VIEW = "expand_search_view";
    public static final Set<Integer> LIST_TYPES_WITH_INSTANT = new ArraySet(Arrays.asList(new Integer[]{0, 3}));
    private static String mIconPackPackageName = null;
    private AppBarLayout mAppBarLayout;
    private ApplicationsAdapter mApplications;
    private ApplicationsState mApplicationsState;
    /* access modifiers changed from: private */
    public String mCurrentPkgName;
    private int mCurrentUid;
    /* access modifiers changed from: private */
    public View mEmptyView;
    boolean mExpandSearch;
    /* access modifiers changed from: private */
    public AppFilterItem mFilter;
    FilterSpinnerAdapter mFilterAdapter;
    /* access modifiers changed from: private */
    public Spinner mFilterSpinner;
    /* access modifiers changed from: private */
    public int mFilterType;
    CharSequence mInvalidSizeStr;
    private boolean mIsPersonalOnly;
    private boolean mIsWorkOnly;
    public int mListType;
    /* access modifiers changed from: private */
    public View mLoadingContainer;
    /* access modifiers changed from: private */
    public NotificationBackend mNotificationBackend;
    private Menu mOptionsMenu;
    RecyclerView mRecyclerView;
    private ResetAppsHelper mResetAppsHelper;
    private View mRootView;
    /* access modifiers changed from: private */
    public SearchView mSearchView;
    /* access modifiers changed from: private */
    public boolean mShowSystem;
    int mSortOrder = R$id.sort_order_alpha;
    View mSpinnerHeader;
    private int mStorageType;
    /* access modifiers changed from: private */
    public IUsageStatsManager mUsageStatsManager;
    /* access modifiers changed from: private */
    public UserManager mUserManager;
    private String mVolumeUuid;
    private int mWorkUserId;

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    public void onCreate(Bundle bundle) {
        Class<Settings.NotificationReviewPermissionsActivity> cls = Settings.NotificationReviewPermissionsActivity.class;
        super.onCreate(bundle);
        boolean z = true;
        setHasOptionsMenu(true);
        FragmentActivity activity = getActivity();
        this.mUserManager = (UserManager) activity.getSystemService(UserManager.class);
        this.mApplicationsState = ApplicationsState.getInstance(activity.getApplication());
        Intent intent = activity.getIntent();
        Bundle arguments = getArguments();
        int titleResId = getTitleResId(intent, arguments);
        String string = arguments != null ? arguments.getString("classname") : null;
        if (string == null) {
            string = intent.getComponent().getClassName();
        }
        if (string.equals(Settings.StorageUseActivity.class.getName())) {
            if (arguments == null || !arguments.containsKey("volumeUuid")) {
                this.mListType = 0;
            } else {
                this.mVolumeUuid = arguments.getString("volumeUuid");
                this.mStorageType = arguments.getInt("storageType", 0);
                this.mListType = 3;
            }
            this.mSortOrder = R$id.sort_order_size;
        } else if (string.equals(Settings.UsageAccessSettingsActivity.class.getName())) {
            this.mListType = 4;
        } else if (string.equals(Settings.HighPowerApplicationsActivity.class.getName())) {
            this.mListType = 5;
            this.mShowSystem = true;
        } else if (string.equals(Settings.OverlaySettingsActivity.class.getName())) {
            this.mListType = 6;
            reportIfRestrictedSawIntent(intent);
        } else if (string.equals(Settings.WriteSettingsActivity.class.getName())) {
            this.mListType = 7;
        } else if (string.equals(Settings.ManageExternalSourcesActivity.class.getName())) {
            this.mListType = 8;
        } else if (string.equals(Settings.GamesStorageActivity.class.getName())) {
            this.mListType = 9;
            this.mSortOrder = R$id.sort_order_size;
        } else if (string.equals(Settings.ChangeWifiStateActivity.class.getName())) {
            this.mListType = 10;
        } else if (string.equals(Settings.ManageExternalStorageActivity.class.getName())) {
            this.mListType = 11;
        } else if (string.equals(Settings.MediaManagementAppsActivity.class.getName())) {
            this.mListType = 13;
        } else if (string.equals(Settings.AlarmsAndRemindersActivity.class.getName())) {
            this.mListType = 12;
        } else if (string.equals(Settings.NotificationAppListActivity.class.getName()) || string.equals(cls.getName())) {
            this.mListType = 1;
            this.mUsageStatsManager = IUsageStatsManager.Stub.asInterface(ServiceManager.getService("usagestats"));
            this.mNotificationBackend = new NotificationBackend();
            this.mSortOrder = R$id.sort_order_recent_notification;
            if (string.equals(cls.getName())) {
                Settings.Global.putInt(getContext().getContentResolver(), "review_permissions_notification_state", 1);
            }
        } else if (string.equals(AppLocaleDetails.class.getName())) {
            this.mListType = 14;
        } else if (string.equals(Settings.GlyphsNotificationAppListActivity.class.getName())) {
            this.mListType = 100;
            this.mShowSystem = false;
            this.mUsageStatsManager = IUsageStatsManager.Stub.asInterface(ServiceManager.getService("usagestats"));
            this.mNotificationBackend = new NotificationBackend();
            this.mSortOrder = R$id.sort_order_recent_notification;
        } else {
            this.mListType = 0;
        }
        AppFilterRegistry instance = AppFilterRegistry.getInstance();
        this.mFilter = instance.get(instance.getDefaultFilterType(this.mListType));
        this.mIsPersonalOnly = arguments != null && arguments.getInt("profile") == 1;
        if (arguments == null || arguments.getInt("profile") != 2) {
            z = false;
        }
        this.mIsWorkOnly = z;
        int i = arguments != null ? arguments.getInt("workId") : UserHandle.myUserId();
        this.mWorkUserId = i;
        if (this.mIsWorkOnly && i == UserHandle.myUserId()) {
            this.mWorkUserId = Utils.getManagedProfileId(this.mUserManager, UserHandle.myUserId());
        }
        this.mExpandSearch = activity.getIntent().getBooleanExtra(EXTRA_EXPAND_SEARCH_VIEW, false);
        if (bundle != null) {
            this.mSortOrder = bundle.getInt("sortOrder", this.mSortOrder);
            this.mShowSystem = bundle.getBoolean("showSystem", this.mShowSystem);
            this.mFilterType = bundle.getInt("filterType", 4);
            this.mExpandSearch = bundle.getBoolean(EXTRA_EXPAND_SEARCH_VIEW);
        }
        this.mInvalidSizeStr = activity.getText(R$string.invalid_size_value);
        this.mResetAppsHelper = new ResetAppsHelper(activity);
        if (titleResId > 0) {
            activity.setTitle(titleResId);
        }
    }

    private void reportIfRestrictedSawIntent(Intent intent) {
        try {
            Uri data = intent.getData();
            if (data == null) {
                return;
            }
            if (TextUtils.equals("package", data.getScheme())) {
                int launchedFromUid = ActivityManager.getService().getLaunchedFromUid(getActivity().getActivityToken());
                if (launchedFromUid == -1) {
                    Log.w("ManageApplications", "Error obtaining calling uid");
                    return;
                }
                IPlatformCompat asInterface = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
                if (asInterface == null) {
                    Log.w("ManageApplications", "Error obtaining IPlatformCompat service");
                } else {
                    asInterface.reportChangeByUid(135920175, launchedFromUid);
                }
            }
        } catch (RemoteException e) {
            Log.w("ManageApplications", "Error reporting SAW intent restriction", e);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mListType != 6 || Utils.isSystemAlertWindowEnabled(getContext())) {
            View inflate = layoutInflater.inflate(R$layout.manage_applications_apps, (ViewGroup) null);
            this.mRootView = inflate;
            this.mLoadingContainer = inflate.findViewById(R$id.loading_container);
            this.mEmptyView = this.mRootView.findViewById(16908292);
            this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R$id.apps_list);
            ApplicationsAdapter applicationsAdapter = new ApplicationsAdapter(this.mApplicationsState, this, this.mFilter, bundle);
            this.mApplications = applicationsAdapter;
            if (bundle != null) {
                applicationsAdapter.mHasReceivedLoadEntries = bundle.getBoolean("hasEntries", false);
                this.mApplications.mHasReceivedBridgeCallback = bundle.getBoolean("hasBridge", false);
            }
            this.mRecyclerView.setItemAnimator((RecyclerView.ItemAnimator) null);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.mRecyclerView.setAdapter(this.mApplications);
            if (viewGroup instanceof PreferenceFrameLayout) {
                this.mRootView.getLayoutParams().removeBorders = true;
            }
            createHeader();
            this.mResetAppsHelper.onRestoreInstanceState(bundle);
            this.mAppBarLayout = (AppBarLayout) getActivity().findViewById(R$id.app_bar);
            disableToolBarScrollableBehavior();
            return this.mRootView;
        }
        this.mRootView = layoutInflater.inflate(R$layout.manage_applications_apps_unsupported, (ViewGroup) null);
        setHasOptionsMenu(false);
        return this.mRootView;
    }

    /* access modifiers changed from: package-private */
    public void createHeader() {
        FragmentActivity activity = getActivity();
        FrameLayout frameLayout = (FrameLayout) this.mRootView.findViewById(R$id.pinned_header);
        View inflate = activity.getLayoutInflater().inflate(R$layout.manage_apps_filter_spinner, frameLayout, false);
        this.mSpinnerHeader = inflate;
        this.mFilterSpinner = (Spinner) inflate.findViewById(R$id.filter_spinner);
        FilterSpinnerAdapter filterSpinnerAdapter = new FilterSpinnerAdapter(this);
        this.mFilterAdapter = filterSpinnerAdapter;
        this.mFilterSpinner.setAdapter(filterSpinnerAdapter);
        this.mFilterSpinner.setOnItemSelectedListener(this);
        frameLayout.addView(this.mSpinnerHeader, 0);
        this.mFilterAdapter.enableFilter(AppFilterRegistry.getInstance().getDefaultFilterType(this.mListType));
        if (DEBUG) {
            Log.d("ManageApplications", "ListType:" + this.mListType);
        }
        if (this.mListType == 0 && UserManager.get(getActivity()).getUserProfiles().size() > 1 && !this.mIsWorkOnly && !this.mIsPersonalOnly) {
            this.mFilterAdapter.enableFilter(8);
            this.mFilterAdapter.enableFilter(9);
        }
        if (this.mListType == 1) {
            this.mFilterAdapter.enableFilter(2);
            this.mFilterAdapter.enableFilter(3);
            this.mFilterAdapter.enableFilter(16);
            this.mFilterAdapter.enableFilter(4);
        }
        if (this.mListType == 5) {
            this.mFilterAdapter.enableFilter(1);
        }
        if (this.mListType == 100) {
            this.mFilterAdapter.enableFilter(2);
            this.mFilterAdapter.enableFilter(3);
            this.mFilterAdapter.enableFilter(4);
        }
        setCompositeFilter();
    }

    static ApplicationsState.AppFilter getCompositeFilter(int i, int i2, String str) {
        ApplicationsState.VolumeFilter volumeFilter = new ApplicationsState.VolumeFilter(str);
        if (i == 3) {
            return i2 == 0 ? new ApplicationsState.CompoundFilter(ApplicationsState.FILTER_APPS_EXCEPT_GAMES, volumeFilter) : volumeFilter;
        }
        if (i == 9) {
            return new ApplicationsState.CompoundFilter(ApplicationsState.FILTER_GAMES, volumeFilter);
        }
        return null;
    }

    public int getMetricsCategory() {
        int i = this.mListType;
        if (i == 0) {
            return 65;
        }
        if (i == 1 || i == 100) {
            return 133;
        }
        switch (i) {
            case 3:
                return 182;
            case 4:
                return 95;
            case 5:
                return 184;
            case 6:
            case 7:
                return 221;
            case 8:
                return 808;
            case 9:
                return 838;
            case 10:
                return 338;
            case 11:
                return 1822;
            case 12:
                return 1869;
            case 13:
                return 1874;
            case 14:
                return 1911;
            default:
                return 0;
        }
    }

    public void onStart() {
        super.onStart();
        updateView();
        ApplicationsAdapter applicationsAdapter = this.mApplications;
        if (applicationsAdapter != null) {
            applicationsAdapter.resume(this.mSortOrder);
            this.mApplications.updateLoading();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mResetAppsHelper.onSaveInstanceState(bundle);
        bundle.putInt("sortOrder", this.mSortOrder);
        bundle.putInt("filterType", this.mFilter.getFilterType());
        bundle.putBoolean("showSystem", this.mShowSystem);
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            bundle.putBoolean(EXTRA_EXPAND_SEARCH_VIEW, !searchView.isIconified());
        }
        ApplicationsAdapter applicationsAdapter = this.mApplications;
        if (applicationsAdapter != null) {
            bundle.putBoolean("hasEntries", applicationsAdapter.mHasReceivedLoadEntries);
            bundle.putBoolean("hasBridge", this.mApplications.mHasReceivedBridgeCallback);
            this.mApplications.onSaveInstanceState(bundle);
        }
    }

    public void onStop() {
        super.onStop();
        ApplicationsAdapter applicationsAdapter = this.mApplications;
        if (applicationsAdapter != null) {
            applicationsAdapter.pause();
        }
        this.mResetAppsHelper.stop();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ApplicationsAdapter applicationsAdapter = this.mApplications;
        if (applicationsAdapter != null) {
            applicationsAdapter.release();
        }
        this.mRootView = null;
        try {
            AppIconCacheManager.getInstance();
            AppIconCacheManager.release();
        } catch (Exception unused) {
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String str;
        if (i == 1 && (str = this.mCurrentPkgName) != null) {
            int i3 = this.mListType;
            if (i3 == 1) {
                this.mApplications.mExtraInfoBridge.forceUpdate(this.mCurrentPkgName, this.mCurrentUid);
            } else if (i3 == 5 || i3 == 6 || i3 == 7) {
                this.mApplications.mExtraInfoBridge.forceUpdate(this.mCurrentPkgName, this.mCurrentUid);
            } else if (i3 != 100) {
                this.mApplicationsState.requestSize(str, UserHandle.getUserId(this.mCurrentUid));
            }
        }
    }

    private void setCompositeFilter() {
        ApplicationsState.CompoundFilter compositeFilter = getCompositeFilter(this.mListType, this.mStorageType, this.mVolumeUuid);
        if (compositeFilter == null) {
            compositeFilter = this.mFilter.getFilter();
        }
        if (this.mIsWorkOnly) {
            compositeFilter = new ApplicationsState.CompoundFilter(compositeFilter, ApplicationsState.FILTER_WORK);
        }
        if (this.mIsPersonalOnly) {
            compositeFilter = new ApplicationsState.CompoundFilter(compositeFilter, ApplicationsState.FILTER_PERSONAL);
        }
        this.mApplications.setCompositeFilter(compositeFilter);
    }

    private void startApplicationDetailsActivity() {
        int i = this.mListType;
        if (i == 1) {
            startAppInfoFragment(AppNotificationSettings.class, R$string.notifications_title);
        } else if (i != 100) {
            switch (i) {
                case 3:
                    startAppInfoFragment(AppStorageSettings.class, R$string.storage_settings);
                    return;
                case 4:
                    startAppInfoFragment(UsageAccessDetails.class, R$string.usage_access);
                    return;
                case 5:
                    HighPowerDetail.show(this, this.mCurrentUid, this.mCurrentPkgName, 1);
                    return;
                case 6:
                    startAppInfoFragment(DrawOverlayDetails.class, R$string.overlay_settings);
                    return;
                case 7:
                    startAppInfoFragment(WriteSettingsDetails.class, R$string.write_system_settings);
                    return;
                case 8:
                    startAppInfoFragment(ExternalSourcesDetails.class, R$string.install_other_apps);
                    return;
                case 9:
                    startAppInfoFragment(AppStorageSettings.class, R$string.game_storage_settings);
                    return;
                case 10:
                    startAppInfoFragment(ChangeWifiStateDetails.class, R$string.change_wifi_state_title);
                    return;
                case 11:
                    startAppInfoFragment(ManageExternalStorageDetails.class, R$string.manage_external_storage_title);
                    return;
                case 12:
                    startAppInfoFragment(AlarmsAndRemindersDetails.class, R$string.alarms_and_reminders_label);
                    return;
                case 13:
                    startAppInfoFragment(MediaManagementAppsDetails.class, R$string.media_management_apps_title);
                    return;
                case 14:
                    Intent intent = new Intent(getContext(), AppLocalePickerActivity.class);
                    intent.setData(Uri.parse("package:" + this.mCurrentPkgName));
                    intent.putExtra("uid", this.mCurrentUid);
                    startActivity(intent);
                    return;
                default:
                    startAppInfoFragment(AppInfoDashboardFragment.class, R$string.application_info_label);
                    return;
            }
        } else {
            Log.d("ManageApplications", "start Notification Details glyphs");
            startAppInfoFragment(AppNotificationConversationFragment.class, R$string.notifications_title);
        }
    }

    private void startAppInfoFragment(Class<?> cls, int i) {
        AppInfoBase.startAppInfoFragment(cls, getString(i), this.mCurrentPkgName, this.mCurrentUid, this, 1, getMetricsCategory());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (getActivity() != null) {
            this.mOptionsMenu = menu;
            menuInflater.inflate(R$menu.manage_apps, menu);
            MenuItem findItem = menu.findItem(R$id.search_app_list_menu);
            if (findItem != null) {
                findItem.setOnActionExpandListener(this);
                SearchView searchView = (SearchView) findItem.getActionView();
                this.mSearchView = searchView;
                searchView.setQueryHint(getText(R$string.search_settings));
                this.mSearchView.setOnQueryTextListener(this);
                if (this.mExpandSearch) {
                    findItem.expandActionView();
                }
            }
            updateOptionsMenu();
        }
    }

    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, false);
        return true;
    }

    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, true);
        return true;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        updateOptionsMenu();
    }

    public void onDestroyOptionsMenu() {
        this.mOptionsMenu = null;
    }

    /* access modifiers changed from: package-private */
    public void updateOptionsMenu() {
        int i;
        int i2;
        Menu menu = this.mOptionsMenu;
        if (menu != null) {
            menu.findItem(R$id.advanced).setVisible(false);
            Menu menu2 = this.mOptionsMenu;
            int i3 = R$id.sort_order_alpha;
            menu2.findItem(i3).setVisible(this.mListType == 3 && this.mSortOrder != i3);
            Menu menu3 = this.mOptionsMenu;
            int i4 = R$id.sort_order_size;
            menu3.findItem(i4).setVisible(this.mListType == 3 && this.mSortOrder != i4);
            Menu menu4 = this.mOptionsMenu;
            int i5 = R$id.show_system;
            menu4.findItem(i5).setVisible((this.mShowSystem || (i2 = this.mListType) == 5 || i2 == 14) ? false : true);
            Menu menu5 = this.mOptionsMenu;
            int i6 = R$id.hide_system;
            menu5.findItem(i6).setVisible((!this.mShowSystem || (i = this.mListType) == 5 || i == 14) ? false : true);
            this.mOptionsMenu.findItem(R$id.reset_app_preferences).setVisible(this.mListType == 0);
            this.mOptionsMenu.findItem(R$id.sort_order_recent_notification).setVisible(false);
            this.mOptionsMenu.findItem(R$id.sort_order_frequent_notification).setVisible(false);
            MenuItem findItem = this.mOptionsMenu.findItem(11);
            if (findItem != null) {
                findItem.setVisible(false);
            }
            MenuItem findItem2 = this.mOptionsMenu.findItem(i5);
            MenuItem findItem3 = this.mOptionsMenu.findItem(i6);
            if (isGlyphsNeeded()) {
                findItem2.setVisible(false);
                findItem3.setVisible(false);
                if (findItem != null) {
                    findItem.setVisible(true);
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        int itemId2 = menuItem.getItemId();
        if (itemId2 == R$id.sort_order_alpha || itemId2 == R$id.sort_order_size) {
            ApplicationsAdapter applicationsAdapter = this.mApplications;
            if (applicationsAdapter != null) {
                applicationsAdapter.rebuild(itemId, false);
            }
        } else if (itemId2 == R$id.show_system || itemId2 == R$id.hide_system) {
            this.mShowSystem = !this.mShowSystem;
            this.mApplications.rebuild();
        } else if (itemId2 == R$id.reset_app_preferences) {
            this.mResetAppsHelper.buildResetDialog();
            return true;
        } else if (itemId2 != R$id.advanced) {
            return false;
        } else {
            if (this.mListType == 1) {
                new SubSettingLauncher(getContext()).setDestination(ConfigureNotificationSettings.class.getName()).setTitleRes(R$string.configure_notification_settings).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 2).launch();
            } else {
                startActivityForResult(new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS"), 2);
            }
            return true;
        }
        updateOptionsMenu();
        return true;
    }

    public void onClick(View view) {
        if (this.mApplications != null) {
            int applicationPosition = ApplicationsAdapter.getApplicationPosition(this.mListType, this.mRecyclerView.getChildAdapterPosition(view));
            if (applicationPosition == -1) {
                Log.w("ManageApplications", "Cannot find position for child, skipping onClick handling");
            } else if (this.mApplications.getApplicationCount() > applicationPosition) {
                ApplicationInfo applicationInfo = this.mApplications.getAppEntry(applicationPosition).info;
                this.mCurrentPkgName = applicationInfo.packageName;
                this.mCurrentUid = applicationInfo.uid;
                startApplicationDetailsActivity();
                ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, true);
            }
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        this.mFilter = this.mFilterAdapter.getFilter(i);
        setCompositeFilter();
        this.mApplications.setFilter(this.mFilter);
        if (DEBUG) {
            Log.d("ManageApplications", "Selecting filter " + getContext().getText(this.mFilter.getTitle()));
        }
    }

    public boolean onQueryTextChange(String str) {
        this.mApplications.filterSearch(str);
        return false;
    }

    public void updateView() {
        updateOptionsMenu();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.invalidateOptionsMenu();
        }
    }

    public void setHasDisabled(boolean z) {
        if (this.mListType == 0) {
            this.mFilterAdapter.setFilterEnabled(5, z);
            this.mFilterAdapter.setFilterEnabled(7, z);
        }
    }

    public void setHasInstant(boolean z) {
        if (LIST_TYPES_WITH_INSTANT.contains(Integer.valueOf(this.mListType))) {
            this.mFilterAdapter.setFilterEnabled(6, z);
        }
    }

    public void setHasGlyphsApps(boolean z) {
        this.mFilterAdapter.setFilterEnabled(5, z);
    }

    private void disableToolBarScrollableBehavior() {
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        ((CoordinatorLayout.LayoutParams) this.mAppBarLayout.getLayoutParams()).setBehavior(behavior);
    }

    public static int getTitleResId(Intent intent, Bundle bundle) {
        int i = R$string.all_apps;
        int intExtra = intent.getIntExtra(":settings:show_fragment_title_resid", i);
        String string = bundle != null ? bundle.getString("classname") : null;
        if (string == null) {
            string = intent.getComponent().getClassName();
        }
        if (string.equals(Settings.UsageAccessSettingsActivity.class.getName())) {
            return R$string.usage_access;
        }
        if (string.equals(Settings.HighPowerApplicationsActivity.class.getName())) {
            return R$string.high_power_apps;
        }
        if (string.equals(Settings.OverlaySettingsActivity.class.getName())) {
            return R$string.system_alert_window_settings;
        }
        if (string.equals(Settings.WriteSettingsActivity.class.getName())) {
            return R$string.write_settings;
        }
        if (string.equals(Settings.ManageExternalSourcesActivity.class.getName())) {
            return R$string.install_other_apps;
        }
        if (string.equals(Settings.ChangeWifiStateActivity.class.getName())) {
            return R$string.change_wifi_state_title;
        }
        if (string.equals(Settings.ManageExternalStorageActivity.class.getName())) {
            return R$string.manage_external_storage_title;
        }
        if (string.equals(Settings.MediaManagementAppsActivity.class.getName())) {
            return R$string.media_management_apps_title;
        }
        if (string.equals(Settings.AlarmsAndRemindersActivity.class.getName())) {
            return R$string.alarms_and_reminders_title;
        }
        if (string.equals(Settings.NotificationAppListActivity.class.getName()) || string.equals(Settings.NotificationReviewPermissionsActivity.class.getName())) {
            return R$string.app_notifications_title;
        }
        if (string.equals(AppLocaleDetails.class.getName())) {
            return R$string.app_locales_picker_menu_title;
        }
        if (string.equals(Settings.GlyphsNotificationAppListActivity.class.getName())) {
            return R$string.nt_glyphs_notification_select_app_title;
        }
        return intExtra == -1 ? i : intExtra;
    }

    static class FilterSpinnerAdapter extends SettingsSpinnerAdapter<CharSequence> {
        private final Context mContext;
        private final ArrayList<AppFilterItem> mFilterOptions = new ArrayList<>();
        private final ManageApplications mManageApplications;

        public FilterSpinnerAdapter(ManageApplications manageApplications) {
            super(manageApplications.getContext());
            this.mContext = manageApplications.getContext();
            this.mManageApplications = manageApplications;
        }

        public AppFilterItem getFilter(int i) {
            return this.mFilterOptions.get(i);
        }

        public void setFilterEnabled(int i, boolean z) {
            if (z) {
                enableFilter(i);
            } else {
                disableFilter(i);
            }
        }

        public void enableFilter(int i) {
            int indexOf;
            AppFilterItem appFilterItem = AppFilterRegistry.getInstance().get(i);
            if (!this.mFilterOptions.contains(appFilterItem)) {
                boolean z = ManageApplications.DEBUG;
                if (z) {
                    Log.d("ManageApplications", "Enabling filter " + this.mContext.getText(appFilterItem.getTitle()) + " filterType:" + i);
                }
                this.mFilterOptions.add(appFilterItem);
                Collections.sort(this.mFilterOptions);
                updateFilterView(this.mFilterOptions.size() > 1);
                notifyDataSetChanged();
                if (this.mFilterOptions.size() == 1) {
                    if (z) {
                        Log.d("ManageApplications", "Auto selecting filter " + appFilterItem + " " + this.mContext.getText(appFilterItem.getTitle()));
                    }
                    this.mManageApplications.mFilterSpinner.setSelection(0);
                    this.mManageApplications.onItemSelected((AdapterView<?>) null, (View) null, 0, 0);
                }
                if (this.mFilterOptions.size() > 1 && (indexOf = this.mFilterOptions.indexOf(AppFilterRegistry.getInstance().get(this.mManageApplications.mFilterType))) != -1) {
                    this.mManageApplications.mFilterSpinner.setSelection(indexOf);
                    this.mManageApplications.onItemSelected((AdapterView<?>) null, (View) null, indexOf, 0);
                }
            }
        }

        public void disableFilter(int i) {
            AppFilterItem appFilterItem = AppFilterRegistry.getInstance().get(i);
            if (this.mFilterOptions.remove(appFilterItem)) {
                boolean z = ManageApplications.DEBUG;
                if (z) {
                    Log.d("ManageApplications", "Disabling filter " + appFilterItem + " " + this.mContext.getText(appFilterItem.getTitle()));
                }
                Collections.sort(this.mFilterOptions);
                boolean z2 = true;
                if (this.mFilterOptions.size() <= 1) {
                    z2 = false;
                }
                updateFilterView(z2);
                notifyDataSetChanged();
                if (this.mManageApplications.mFilter == appFilterItem && this.mFilterOptions.size() > 0) {
                    if (z) {
                        Log.d("ManageApplications", "Auto selecting filter " + this.mFilterOptions.get(0) + this.mContext.getText(this.mFilterOptions.get(0).getTitle()));
                    }
                    this.mManageApplications.mFilterSpinner.setSelection(0);
                    this.mManageApplications.onItemSelected((AdapterView<?>) null, (View) null, 0, 0);
                }
            }
        }

        public int getCount() {
            return this.mFilterOptions.size();
        }

        public CharSequence getItem(int i) {
            return this.mContext.getText(this.mFilterOptions.get(i).getTitle());
        }

        /* access modifiers changed from: package-private */
        public void updateFilterView(boolean z) {
            if (ManageApplications.DEBUG) {
                Log.d("ManageApplications", "updateFilterView " + z);
            }
            if (z) {
                this.mManageApplications.mSpinnerHeader.setVisibility(0);
            } else {
                this.mManageApplications.mSpinnerHeader.setVisibility(8);
            }
        }
    }

    static class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationViewHolder> implements ApplicationsState.Callbacks, AppStateBaseBridge.Callback {
        private AppFilterItem mAppFilter;
        private PowerAllowlistBackend mBackend;
        private ApplicationsState.AppFilter mCompositeFilter;
        private final Context mContext;
        /* access modifiers changed from: private */
        public ArrayList<ApplicationsState.AppEntry> mEntries;
        /* access modifiers changed from: private */
        public final AppStateBaseBridge mExtraInfoBridge;
        /* access modifiers changed from: private */
        public boolean mHasReceivedBridgeCallback;
        /* access modifiers changed from: private */
        public boolean mHasReceivedLoadEntries;
        private final IconDrawableFactory mIconDrawableFactory;
        private Map<String, Boolean> mIconPackChangedList = new HashMap();
        private int mLastIndex = -1;
        private int mLastSortMode = -1;
        private final LoadingViewController mLoadingViewController;
        private final ManageApplications mManageApplications;
        OnScrollListener mOnScrollListener;
        /* access modifiers changed from: private */
        public ArrayList<ApplicationsState.AppEntry> mOriginalEntries;
        private RecyclerView mRecyclerView;
        private boolean mResumed;
        private SearchFilter mSearchFilter;
        private final ApplicationsState.Session mSession;
        private final ApplicationsState mState;
        private int mWhichSize = 0;

        public static int getApplicationPosition(int i, int i2) {
            if (i != 14) {
                return i2;
            }
            if (i2 > 0) {
                return i2 - 1;
            }
            return -1;
        }

        public void onPackageIconChanged() {
        }

        public ApplicationsAdapter(ApplicationsState applicationsState, ManageApplications manageApplications, AppFilterItem appFilterItem, Bundle bundle) {
            setHasStableIds(true);
            this.mState = applicationsState;
            this.mSession = applicationsState.newSession(this);
            this.mManageApplications = manageApplications;
            this.mLoadingViewController = new LoadingViewController(manageApplications.mLoadingContainer, manageApplications.mRecyclerView, manageApplications.mEmptyView);
            FragmentActivity activity = manageApplications.getActivity();
            this.mContext = activity;
            this.mIconDrawableFactory = IconDrawableFactory.newInstance(activity);
            this.mAppFilter = appFilterItem;
            PowerAllowlistBackend instance = PowerAllowlistBackend.getInstance(activity);
            this.mBackend = instance;
            int i = manageApplications.mListType;
            if (i == 1) {
                this.mExtraInfoBridge = new AppStateNotificationBridge(activity, applicationsState, this, manageApplications.mUsageStatsManager, manageApplications.mUserManager, manageApplications.mNotificationBackend);
            } else if (i == 4) {
                this.mExtraInfoBridge = new AppStateUsageBridge(activity, applicationsState, this);
            } else if (i == 5) {
                instance.refreshList();
                this.mExtraInfoBridge = new AppStatePowerBridge(activity, applicationsState, this);
            } else if (i == 6) {
                this.mExtraInfoBridge = new AppStateOverlayBridge(activity, applicationsState, this);
            } else if (i == 7) {
                this.mExtraInfoBridge = new AppStateWriteSettingsBridge(activity, applicationsState, this);
            } else if (i == 8) {
                this.mExtraInfoBridge = new AppStateInstallAppsBridge(activity, applicationsState, this);
            } else if (i == 10) {
                this.mExtraInfoBridge = new AppStateChangeWifiStateBridge(activity, applicationsState, this);
            } else if (i == 11) {
                this.mExtraInfoBridge = new AppStateManageExternalStorageBridge(activity, applicationsState, this);
            } else if (i == 12) {
                this.mExtraInfoBridge = new AppStateAlarmsAndRemindersBridge(activity, applicationsState, this);
            } else if (i == 13) {
                this.mExtraInfoBridge = new AppStateMediaManagementAppsBridge(activity, applicationsState, this);
            } else if (i == 14) {
                this.mExtraInfoBridge = new AppStateLocaleBridge(activity, applicationsState, this);
            } else if (i == 100) {
                this.mExtraInfoBridge = new AppStateNotificationBridge(activity, applicationsState, this, manageApplications.mUsageStatsManager, manageApplications.mUserManager, manageApplications.mNotificationBackend);
            } else {
                this.mExtraInfoBridge = null;
            }
            if (bundle != null) {
                this.mLastIndex = bundle.getInt("state_last_scroll_index");
            }
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            this.mRecyclerView = recyclerView;
            OnScrollListener onScrollListener = new OnScrollListener(this);
            this.mOnScrollListener = onScrollListener;
            this.mRecyclerView.addOnScrollListener(onScrollListener);
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
            this.mOnScrollListener = null;
            this.mRecyclerView = null;
        }

        public void setCompositeFilter(ApplicationsState.AppFilter appFilter) {
            this.mCompositeFilter = appFilter;
            rebuild();
        }

        public void setIconChanged(boolean z) {
            if (z) {
                this.mIconPackChangedList.clear();
            }
        }

        public void setFilter(AppFilterItem appFilterItem) {
            this.mAppFilter = appFilterItem;
            int i = this.mManageApplications.mListType;
            if (i != 1 && i != 100) {
                rebuild();
            } else if (3 == appFilterItem.getFilterType()) {
                rebuild(R$id.sort_order_frequent_notification, false);
            } else if (2 == appFilterItem.getFilterType()) {
                rebuild(R$id.sort_order_recent_notification, false);
            } else if (16 == appFilterItem.getFilterType()) {
                rebuild(R$id.sort_order_alpha, true);
            } else {
                rebuild(R$id.sort_order_alpha, true);
            }
        }

        public void resume(int i) {
            if (ManageApplications.DEBUG) {
                Log.i("ManageApplications", "Resume!  mResumed=" + this.mResumed);
            }
            if (!this.mResumed) {
                this.mResumed = true;
                this.mSession.onResume();
                this.mLastSortMode = i;
                AppStateBaseBridge appStateBaseBridge = this.mExtraInfoBridge;
                if (appStateBaseBridge != null) {
                    appStateBaseBridge.resume(false);
                }
                rebuild();
                return;
            }
            rebuild(i, false);
        }

        public void pause() {
            if (this.mResumed) {
                this.mResumed = false;
                this.mSession.onPause();
                AppStateBaseBridge appStateBaseBridge = this.mExtraInfoBridge;
                if (appStateBaseBridge != null) {
                    appStateBaseBridge.pause();
                }
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            bundle.putInt("state_last_scroll_index", ((LinearLayoutManager) this.mManageApplications.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        }

        public void release() {
            this.mSession.onDestroy();
            AppStateBaseBridge appStateBaseBridge = this.mExtraInfoBridge;
            if (appStateBaseBridge != null) {
                appStateBaseBridge.release();
            }
        }

        public void rebuild(int i, boolean z) {
            if (i != this.mLastSortMode || z) {
                this.mManageApplications.mSortOrder = i;
                this.mLastSortMode = i;
                rebuild();
            }
        }

        public ApplicationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            int i2 = this.mManageApplications.mListType;
            if (i2 == 14 && i == 2) {
                view = ApplicationViewHolder.newHeader(viewGroup, R$string.desc_app_locale_selection_supported);
            } else if (i2 == 1) {
                view = ApplicationViewHolder.newView(viewGroup, true);
            } else if (i2 == 100) {
                view = ApplicationViewHolder.newGlyphsView(viewGroup, true);
            } else {
                view = ApplicationViewHolder.newView(viewGroup, false);
            }
            return new ApplicationViewHolder(view);
        }

        public int getItemViewType(int i) {
            return (i == 0 && this.mManageApplications.mListType == 14) ? 2 : 0;
        }

        public void rebuild() {
            Comparator<ApplicationsState.AppEntry> comparator;
            ApplicationsState.CompoundFilter compoundFilter;
            boolean z = false;
            if (this.mHasReceivedLoadEntries && (this.mExtraInfoBridge == null || this.mHasReceivedBridgeCallback)) {
                if (Environment.isExternalStorageEmulated()) {
                    this.mWhichSize = 0;
                } else {
                    this.mWhichSize = 1;
                }
                ApplicationsState.CompoundFilter filter = this.mAppFilter.getFilter();
                ApplicationsState.AppFilter appFilter = this.mCompositeFilter;
                if (appFilter != null) {
                    filter = new ApplicationsState.CompoundFilter(filter, appFilter);
                }
                if (!this.mManageApplications.mShowSystem) {
                    if (ManageApplications.LIST_TYPES_WITH_INSTANT.contains(Integer.valueOf(this.mManageApplications.mListType))) {
                        compoundFilter = new ApplicationsState.CompoundFilter(filter, ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER_AND_INSTANT);
                    } else {
                        compoundFilter = new ApplicationsState.CompoundFilter(filter, ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER);
                    }
                    filter = compoundFilter;
                }
                int i = this.mLastSortMode;
                if (i == R$id.sort_order_size) {
                    int i2 = this.mWhichSize;
                    if (i2 == 1) {
                        comparator = ApplicationsState.INTERNAL_SIZE_COMPARATOR;
                    } else if (i2 != 2) {
                        comparator = ApplicationsState.SIZE_COMPARATOR;
                    } else {
                        comparator = ApplicationsState.EXTERNAL_SIZE_COMPARATOR;
                    }
                } else if (i == R$id.sort_order_recent_notification) {
                    comparator = AppStateNotificationBridge.RECENT_NOTIFICATION_COMPARATOR;
                } else if (i == R$id.sort_order_frequent_notification) {
                    comparator = AppStateNotificationBridge.FREQUENCY_NOTIFICATION_COMPARATOR;
                } else {
                    comparator = ApplicationsState.ALPHA_COMPARATOR;
                }
                ThreadUtils.postOnBackgroundThread((Runnable) new ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda0(this, new ApplicationsState.CompoundFilter(filter, ApplicationsState.FILTER_NOT_HIDE), comparator));
            } else if (ManageApplications.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not rebuilding until all the app entries loaded. !mHasReceivedLoadEntries=");
                sb.append(!this.mHasReceivedLoadEntries);
                sb.append(" !mExtraInfoBridgeNull=");
                if (this.mExtraInfoBridge != null) {
                    z = true;
                }
                sb.append(z);
                sb.append(" !mHasReceivedBridgeCallback=");
                sb.append(!this.mHasReceivedBridgeCallback);
                Log.d("ManageApplications", sb.toString());
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$rebuild$0(ApplicationsState.AppFilter appFilter, Comparator comparator) {
            this.mSession.rebuild(appFilter, comparator, false);
        }

        /* access modifiers changed from: package-private */
        public void filterSearch(String str) {
            if (this.mSearchFilter == null) {
                this.mSearchFilter = new SearchFilter();
            }
            if (this.mOriginalEntries == null) {
                Log.w("ManageApplications", "Apps haven't loaded completely yet, so nothing can be filtered");
            } else {
                this.mSearchFilter.filter(str);
            }
        }

        private static boolean packageNameEquals(PackageItemInfo packageItemInfo, PackageItemInfo packageItemInfo2) {
            String str;
            String str2;
            if (packageItemInfo == null || packageItemInfo2 == null || (str = packageItemInfo.packageName) == null || (str2 = packageItemInfo2.packageName) == null) {
                return false;
            }
            return str.equals(str2);
        }

        private ArrayList<ApplicationsState.AppEntry> removeDuplicateIgnoringUser(ArrayList<ApplicationsState.AppEntry> arrayList) {
            int size = arrayList.size();
            ArrayList<ApplicationsState.AppEntry> arrayList2 = new ArrayList<>(size);
            ApplicationInfo applicationInfo = null;
            int i = 0;
            while (i < size) {
                ApplicationsState.AppEntry appEntry = arrayList.get(i);
                ApplicationInfo applicationInfo2 = appEntry.info;
                if (!packageNameEquals(applicationInfo, applicationInfo2)) {
                    arrayList2.add(appEntry);
                }
                i++;
                applicationInfo = applicationInfo2;
            }
            arrayList2.trimToSize();
            return arrayList2;
        }

        public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
            if (ManageApplications.DEBUG) {
                Log.d("ManageApplications", "onRebuildComplete size=" + arrayList.size());
            }
            Context context = this.mContext;
            AppUtils.preloadTopIcons(context, arrayList, context.getResources().getInteger(R$integer.config_num_visible_app_icons));
            int filterType = this.mAppFilter.getFilterType();
            if (filterType == 0 || filterType == 1) {
                arrayList = removeDuplicateIgnoringUser(arrayList);
            }
            this.mEntries = arrayList;
            this.mOriginalEntries = arrayList;
            notifyDataSetChanged();
            if (getItemCount() == 0) {
                this.mLoadingViewController.showEmpty(false);
            } else {
                this.mLoadingViewController.showContent(false);
                if (this.mManageApplications.mSearchView != null && this.mManageApplications.mSearchView.isVisibleToUser()) {
                    CharSequence query = this.mManageApplications.mSearchView.getQuery();
                    if (!TextUtils.isEmpty(query)) {
                        filterSearch(query.toString());
                    }
                }
            }
            if (this.mLastIndex != -1 && getItemCount() > this.mLastIndex) {
                this.mManageApplications.mRecyclerView.getLayoutManager().scrollToPosition(this.mLastIndex);
                this.mLastIndex = -1;
            }
            ManageApplications manageApplications = this.mManageApplications;
            if (manageApplications.mListType != 4) {
                manageApplications.setHasDisabled(this.mState.haveDisabledApps());
                this.mManageApplications.setHasInstant(this.mState.haveInstantApps());
                Log.d("ManageApplications", "mManageApplications.mListType:" + this.mManageApplications.mListType);
                ManageApplications manageApplications2 = this.mManageApplications;
                if (manageApplications2.mListType == 100) {
                    manageApplications2.setHasGlyphsApps(true);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void updateLoading() {
            if (this.mHasReceivedLoadEntries && this.mSession.getAllApps().size() != 0) {
                this.mLoadingViewController.showContent(false);
            } else {
                this.mLoadingViewController.showLoadingViewDelayed();
            }
        }

        public void onExtraInfoUpdated() {
            this.mHasReceivedBridgeCallback = true;
            rebuild();
        }

        public void onRunningStateChanged(boolean z) {
            this.mManageApplications.getActivity().setProgressBarIndeterminateVisibility(z);
        }

        public void onPackageListChanged() {
            rebuild();
        }

        public void onLoadEntriesCompleted() {
            this.mHasReceivedLoadEntries = true;
            rebuild();
        }

        public void onPackageSizeChanged(String str) {
            ArrayList<ApplicationsState.AppEntry> arrayList = this.mEntries;
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ApplicationInfo applicationInfo = this.mEntries.get(i).info;
                    if (applicationInfo != null || TextUtils.equals(str, applicationInfo.packageName)) {
                        if (TextUtils.equals(this.mManageApplications.mCurrentPkgName, applicationInfo.packageName)) {
                            rebuild();
                            return;
                        }
                        this.mOnScrollListener.postNotifyItemChange(i);
                    }
                }
            }
        }

        public void onLauncherInfoChanged() {
            if (!this.mManageApplications.mShowSystem) {
                rebuild();
            }
        }

        public void onAllSizesComputed() {
            if (this.mLastSortMode == R$id.sort_order_size) {
                rebuild();
            }
        }

        public int getItemCount() {
            int applicationCount = getApplicationCount();
            return (applicationCount == 0 || this.mManageApplications.mListType != 14) ? applicationCount : applicationCount + 1;
        }

        public int getApplicationCount() {
            ArrayList<ApplicationsState.AppEntry> arrayList = this.mEntries;
            if (arrayList != null) {
                return arrayList.size();
            }
            return 0;
        }

        public ApplicationsState.AppEntry getAppEntry(int i) {
            return this.mEntries.get(i);
        }

        public long getItemId(int i) {
            int applicationPosition = getApplicationPosition(this.mManageApplications.mListType, i);
            if (applicationPosition == this.mEntries.size() || applicationPosition == -1) {
                return -1;
            }
            return this.mEntries.get(applicationPosition).f224id;
        }

        public boolean isEnabled(int i) {
            int i2;
            int applicationPosition;
            int itemViewType = getItemViewType(i);
            if (itemViewType == 1 || itemViewType == 2 || (i2 = this.mManageApplications.mListType) != 5 || (applicationPosition = getApplicationPosition(i2, i)) == -1) {
                return true;
            }
            ApplicationsState.AppEntry appEntry = this.mEntries.get(applicationPosition);
            if (this.mBackend.isSysAllowlisted(appEntry.info.packageName) || this.mBackend.isDefaultActiveApp(appEntry.info.packageName)) {
                return false;
            }
            return true;
        }

        public void onBindViewHolder(ApplicationViewHolder applicationViewHolder, int i) {
            int applicationPosition;
            if (getItemViewType(i) != 2 && (applicationPosition = getApplicationPosition(this.mManageApplications.mListType, i)) != -1) {
                ApplicationsState.AppEntry appEntry = this.mEntries.get(applicationPosition);
                synchronized (appEntry) {
                    this.mState.ensureLabelDescription(appEntry);
                    applicationViewHolder.setTitle(appEntry.label, appEntry.labelDescription);
                    boolean booleanValue = this.mIconPackChangedList.getOrDefault(appEntry.label, Boolean.TRUE).booleanValue();
                    Log.d("ManageApplications", "onBindViewHolder label:" + appEntry.label + ", get:" + booleanValue);
                    if (booleanValue) {
                        appEntry.icon = com.android.settingslib.Utils.getBadgedIcon(this.mContext, appEntry.info);
                        this.mIconPackChangedList.put(appEntry.label, Boolean.FALSE);
                        applicationViewHolder.setIcon(appEntry.icon);
                    } else {
                        updateIcon(applicationViewHolder, appEntry);
                    }
                    updateSummary(applicationViewHolder, appEntry);
                    updateSwitch(applicationViewHolder, appEntry);
                    applicationViewHolder.updateDisableView(appEntry.info);
                }
                applicationViewHolder.setEnabled(isEnabled(i));
                applicationViewHolder.itemView.setOnClickListener(this.mManageApplications);
            }
        }

        private void updateIcon(ApplicationViewHolder applicationViewHolder, ApplicationsState.AppEntry appEntry) {
            Drawable iconFromCache = AppUtils.getIconFromCache(appEntry);
            if (iconFromCache == null || !appEntry.mounted) {
                ThreadUtils.postOnBackgroundThread((Runnable) new ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda1(this, appEntry, applicationViewHolder));
            } else {
                applicationViewHolder.setIcon(iconFromCache);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$updateIcon$2(ApplicationsState.AppEntry appEntry, ApplicationViewHolder applicationViewHolder) {
            Drawable icon = AppUtils.getIcon(this.mContext, appEntry);
            if (icon != null) {
                ThreadUtils.postOnMainThread(new ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda2(applicationViewHolder, icon));
            }
        }

        private void updateSummary(ApplicationViewHolder applicationViewHolder, ApplicationsState.AppEntry appEntry) {
            int i;
            ManageApplications manageApplications = this.mManageApplications;
            int i2 = manageApplications.mListType;
            if (i2 == 1 || i2 == 100) {
                Object obj = appEntry.extraInfo;
                if (obj == null || !(obj instanceof AppStateNotificationBridge.NotificationsSentState)) {
                    applicationViewHolder.setSummary((CharSequence) null);
                } else {
                    applicationViewHolder.setSummary(AppStateNotificationBridge.getSummary(this.mContext, (AppStateNotificationBridge.NotificationsSentState) obj, this.mLastSortMode));
                }
            } else {
                switch (i2) {
                    case 4:
                        Object obj2 = appEntry.extraInfo;
                        if (obj2 == null || !(obj2 instanceof AppStateAppOpsBridge.PermissionState)) {
                            applicationViewHolder.setSummary((CharSequence) null);
                            return;
                        }
                        if (new AppStateUsageBridge.UsageState((AppStateAppOpsBridge.PermissionState) obj2).isPermissible()) {
                            i = R$string.app_permission_summary_allowed;
                        } else {
                            i = R$string.app_permission_summary_not_allowed;
                        }
                        applicationViewHolder.setSummary(i);
                        return;
                    case 5:
                        applicationViewHolder.setSummary(HighPowerDetail.getSummary(this.mContext, appEntry));
                        return;
                    case 6:
                        applicationViewHolder.setSummary(DrawOverlayDetails.getSummary(this.mContext, appEntry));
                        return;
                    case 7:
                        applicationViewHolder.setSummary(WriteSettingsDetails.getSummary(this.mContext, appEntry));
                        return;
                    case 8:
                        applicationViewHolder.setSummary(ExternalSourcesDetails.getPreferenceSummary(this.mContext, appEntry));
                        return;
                    default:
                        switch (i2) {
                            case 10:
                                applicationViewHolder.setSummary(ChangeWifiStateDetails.getSummary(this.mContext, appEntry));
                                return;
                            case 11:
                                applicationViewHolder.setSummary(ManageExternalStorageDetails.getSummary(this.mContext, appEntry));
                                return;
                            case 12:
                                applicationViewHolder.setSummary(AlarmsAndRemindersDetails.getSummary(this.mContext, appEntry));
                                return;
                            case 13:
                                applicationViewHolder.setSummary(MediaManagementAppsDetails.getSummary(this.mContext, appEntry));
                                return;
                            case 14:
                                applicationViewHolder.setSummary(AppLocaleDetails.getSummary(this.mContext, appEntry));
                                return;
                            default:
                                applicationViewHolder.updateSizeText(appEntry, manageApplications.mInvalidSizeStr, this.mWhichSize);
                                return;
                        }
                }
            }
        }

        private void updateSwitch(ApplicationViewHolder applicationViewHolder, ApplicationsState.AppEntry appEntry) {
            if (this.mManageApplications.mListType == 1) {
                applicationViewHolder.updateSwitch(((AppStateNotificationBridge) this.mExtraInfoBridge).getSwitchOnCheckedListener(appEntry), AppStateNotificationBridge.enableSwitch(appEntry), AppStateNotificationBridge.checkSwitch(appEntry));
                Object obj = appEntry.extraInfo;
                if (obj == null || !(obj instanceof AppStateNotificationBridge.NotificationsSentState)) {
                    applicationViewHolder.setSummary((CharSequence) null);
                } else {
                    applicationViewHolder.setSummary(AppStateNotificationBridge.getSummary(this.mContext, (AppStateNotificationBridge.NotificationsSentState) obj, this.mLastSortMode));
                }
            }
        }

        public static class OnScrollListener extends RecyclerView.OnScrollListener {
            private ApplicationsAdapter mAdapter;
            private boolean mDelayNotifyDataChange;
            private int mScrollState = 0;

            public OnScrollListener(ApplicationsAdapter applicationsAdapter) {
                this.mAdapter = applicationsAdapter;
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                this.mScrollState = i;
                if (i == 0 && this.mDelayNotifyDataChange) {
                    this.mDelayNotifyDataChange = false;
                    this.mAdapter.notifyDataSetChanged();
                }
            }

            public void postNotifyItemChange(int i) {
                if (this.mScrollState == 0) {
                    this.mAdapter.notifyItemChanged(i);
                } else {
                    this.mDelayNotifyDataChange = true;
                }
            }
        }

        private class SearchFilter extends Filter {
            private SearchFilter() {
            }

            /* access modifiers changed from: protected */
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                ArrayList arrayList;
                if (TextUtils.isEmpty(charSequence)) {
                    arrayList = ApplicationsAdapter.this.mOriginalEntries;
                } else {
                    ArrayList arrayList2 = new ArrayList();
                    Iterator it = ApplicationsAdapter.this.mOriginalEntries.iterator();
                    while (it.hasNext()) {
                        ApplicationsState.AppEntry appEntry = (ApplicationsState.AppEntry) it.next();
                        if (appEntry.label.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList2.add(appEntry);
                        }
                    }
                    arrayList = arrayList2;
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                ApplicationsAdapter.this.mEntries = (ArrayList) filterResults.values;
                ApplicationsAdapter.this.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isGlyphsNeeded() {
        return this.mListType == 100;
    }

    public void onResume() {
        super.onResume();
        if (this.mApplications != null) {
            String string = Settings.System.getString(getActivity().getContentResolver(), "nothing_icon_pack");
            Log.d("ManageApplications", "onResume packageName:" + string + ", mIconPackPackageName:" + mIconPackPackageName);
            if (!TextUtils.equals(mIconPackPackageName, string)) {
                this.mApplications.setIconChanged(true);
                mIconPackPackageName = string;
                return;
            }
            this.mApplications.setIconChanged(false);
        }
    }
}
