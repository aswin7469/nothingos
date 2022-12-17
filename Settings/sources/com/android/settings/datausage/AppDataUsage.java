package com.android.settings.datausage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import android.view.View;
import android.widget.AdapterView;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$xml;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settingslib.AppItem;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.net.NetworkCycleDataForUid;
import com.android.settingslib.net.NetworkCycleDataForUidLoader;
import com.android.settingslib.net.UidDetail;
import com.android.settingslib.net.UidDetailProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppDataUsage extends DataUsageBaseFragment implements Preference.OnPreferenceChangeListener, DataSaverBackend.Listener {
    /* access modifiers changed from: private */
    public AppItem mAppItem;
    /* access modifiers changed from: private */
    public PreferenceCategory mAppList;
    private final LoaderManager.LoaderCallbacks<ArraySet<Preference>> mAppPrefCallbacks = new LoaderManager.LoaderCallbacks<ArraySet<Preference>>() {
        public void onLoaderReset(Loader<ArraySet<Preference>> loader) {
        }

        public Loader<ArraySet<Preference>> onCreateLoader(int i, Bundle bundle) {
            return new AppPrefLoader(AppDataUsage.this.getPrefContext(), AppDataUsage.this.mPackages, AppDataUsage.this.getPackageManager());
        }

        public void onLoadFinished(Loader<ArraySet<Preference>> loader, ArraySet<Preference> arraySet) {
            if (arraySet != null && AppDataUsage.this.mAppList != null) {
                Iterator<Preference> it = arraySet.iterator();
                while (it.hasNext()) {
                    AppDataUsage.this.mAppList.addPreference(it.next());
                }
            }
        }
    };
    private Preference mAppSettings;
    private Intent mAppSettingsIntent;
    private Preference mBackgroundUsage;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public SpinnerPreference mCycle;
    /* access modifiers changed from: private */
    public CycleAdapter mCycleAdapter;
    private AdapterView.OnItemSelectedListener mCycleListener = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            AppDataUsage.this.bindData(i);
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<Long> mCycles;
    private DataSaverBackend mDataSaverBackend;
    private Preference mForegroundUsage;
    private Drawable mIcon;
    /* access modifiers changed from: private */
    public boolean mIsLoading;
    CharSequence mLabel;
    private PackageManager mPackageManager;
    String mPackageName;
    /* access modifiers changed from: private */
    public final ArraySet<String> mPackages = new ArraySet<>();
    private RestrictedSwitchPreference mRestrictBackground;
    /* access modifiers changed from: private */
    public long mSelectedCycle;
    NetworkTemplate mTemplate;
    private Preference mTotalUsage;
    final LoaderManager.LoaderCallbacks<List<NetworkCycleDataForUid>> mUidDataCallbacks = new LoaderManager.LoaderCallbacks<List<NetworkCycleDataForUid>>() {
        public void onLoaderReset(Loader<List<NetworkCycleDataForUid>> loader) {
        }

        public Loader<List<NetworkCycleDataForUid>> onCreateLoader(int i, Bundle bundle) {
            NetworkCycleDataForUidLoader.Builder builder = NetworkCycleDataForUidLoader.builder(AppDataUsage.this.mContext);
            builder.setRetrieveDetail(true).setNetworkTemplate(AppDataUsage.this.mTemplate);
            for (int i2 = 0; i2 < AppDataUsage.this.mAppItem.uids.size(); i2++) {
                builder.addUid(AppDataUsage.this.mAppItem.uids.keyAt(i2));
            }
            if (AppDataUsage.this.mCycles != null) {
                builder.setCycles(AppDataUsage.this.mCycles);
            }
            return builder.build();
        }

        public void onLoadFinished(Loader<List<NetworkCycleDataForUid>> loader, List<NetworkCycleDataForUid> list) {
            AppDataUsage.this.mUsageData = list;
            AppDataUsage.this.mCycleAdapter.updateCycleList(list);
            if (AppDataUsage.this.mSelectedCycle > 0) {
                int size = list.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        i = 0;
                        break;
                    } else if (list.get(i).getEndTime() == AppDataUsage.this.mSelectedCycle) {
                        break;
                    } else {
                        i++;
                    }
                }
                if (i > 0) {
                    AppDataUsage.this.mCycle.setSelection(i);
                }
                AppDataUsage.this.bindData(i);
            } else {
                AppDataUsage.this.bindData(0);
            }
            AppDataUsage.this.mIsLoading = false;
        }
    };
    private RestrictedSwitchPreference mUnrestrictedData;
    /* access modifiers changed from: private */
    public List<NetworkCycleDataForUid> mUsageData;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppDataUsage";
    }

    public int getMetricsCategory() {
        return 343;
    }

    public void onDataSaverChanged(boolean z) {
    }

    public void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        this.mContext = getContext();
        this.mPackageManager = getPackageManager();
        Bundle arguments = getArguments();
        this.mAppItem = arguments != null ? (AppItem) arguments.getParcelable("app_item") : null;
        this.mTemplate = arguments != null ? (NetworkTemplate) arguments.getParcelable("network_template") : null;
        this.mCycles = arguments != null ? (ArrayList) arguments.getSerializable("network_cycles") : null;
        this.mSelectedCycle = arguments != null ? arguments.getLong("selected_cycle") : 0;
        if (this.mTemplate == null) {
            this.mTemplate = DataUsageUtils.getDefaultTemplate(this.mContext, SubscriptionManager.getDefaultDataSubscriptionId());
        }
        boolean z = false;
        if (this.mAppItem == null) {
            if (arguments != null) {
                i = arguments.getInt("uid", -1);
            } else {
                i = getActivity().getIntent().getIntExtra("uid", -1);
            }
            if (i == -1) {
                getActivity().finish();
            } else {
                addUid(i);
                AppItem appItem = new AppItem(i);
                this.mAppItem = appItem;
                appItem.addUid(i);
            }
        } else {
            for (int i2 = 0; i2 < this.mAppItem.uids.size(); i2++) {
                addUid(this.mAppItem.uids.keyAt(i2));
            }
        }
        int i3 = this.mAppItem.key;
        if (i3 > 0 && UserHandle.isApp(i3)) {
            int sdkSandboxUid = Process.toSdkSandboxUid(this.mAppItem.key);
            if (!this.mAppItem.uids.get(sdkSandboxUid)) {
                this.mAppItem.addUid(sdkSandboxUid);
            }
        }
        this.mTotalUsage = findPreference("total_usage");
        this.mForegroundUsage = findPreference("foreground_usage");
        this.mBackgroundUsage = findPreference("background_usage");
        initCycle();
        UidDetailProvider uidDetailProvider = getUidDetailProvider();
        int i4 = this.mAppItem.key;
        if (i4 > 0) {
            if (!UserHandle.isApp(i4)) {
                UidDetail uidDetail = uidDetailProvider.getUidDetail(this.mAppItem.key, true);
                this.mIcon = uidDetail.icon;
                this.mLabel = uidDetail.label;
                removePreference("unrestricted_data_saver");
                removePreference("restrict_background");
            } else {
                if (this.mPackages.size() != 0) {
                    try {
                        ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(this.mPackages.valueAt(0), 0, UserHandle.getUserId(this.mAppItem.key));
                        this.mIcon = IconDrawableFactory.newInstance(getActivity()).getBadgedIcon(applicationInfoAsUser);
                        this.mLabel = applicationInfoAsUser.loadLabel(this.mPackageManager);
                        this.mPackageName = applicationInfoAsUser.packageName;
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
                RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) findPreference("restrict_background");
                this.mRestrictBackground = restrictedSwitchPreference;
                restrictedSwitchPreference.setOnPreferenceChangeListener(this);
                RestrictedSwitchPreference restrictedSwitchPreference2 = (RestrictedSwitchPreference) findPreference("unrestricted_data_saver");
                this.mUnrestrictedData = restrictedSwitchPreference2;
                restrictedSwitchPreference2.setOnPreferenceChangeListener(this);
            }
            this.mDataSaverBackend = new DataSaverBackend(this.mContext);
            this.mAppSettings = findPreference("app_settings");
            Intent intent = new Intent("android.intent.action.MANAGE_NETWORK_USAGE");
            this.mAppSettingsIntent = intent;
            intent.addCategory("android.intent.category.DEFAULT");
            PackageManager packageManager = getPackageManager();
            Iterator<String> it = this.mPackages.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                this.mAppSettingsIntent.setPackage(it.next());
                if (packageManager.resolveActivity(this.mAppSettingsIntent, 0) != null) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                removePreference("app_settings");
                this.mAppSettings = null;
            }
            if (this.mPackages.size() > 1) {
                this.mAppList = (PreferenceCategory) findPreference("app_list");
                LoaderManager.getInstance(this).restartLoader(3, Bundle.EMPTY, this.mAppPrefCallbacks);
            } else {
                removePreference("app_list");
            }
        } else {
            FragmentActivity activity = getActivity();
            UidDetail uidDetail2 = uidDetailProvider.getUidDetail(this.mAppItem.key, true);
            this.mIcon = uidDetail2.icon;
            this.mLabel = uidDetail2.label;
            this.mPackageName = activity.getPackageName();
            removePreference("unrestricted_data_saver");
            removePreference("app_settings");
            removePreference("restrict_background");
            removePreference("app_list");
        }
        addEntityHeader();
    }

    public void onResume() {
        super.onResume();
        this.mIsLoading = true;
        getListView().setItemAnimator((RecyclerView.ItemAnimator) null);
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            dataSaverBackend.addListener(this);
        }
        LoaderManager.getInstance(this).restartLoader(2, (Bundle) null, this.mUidDataCallbacks);
        updatePrefs();
    }

    public void onPause() {
        super.onPause();
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            dataSaverBackend.remListener(this);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mRestrictBackground) {
            this.mDataSaverBackend.setIsDenylisted(this.mAppItem.key, this.mPackageName, !((Boolean) obj).booleanValue());
            updatePrefs();
            return true;
        } else if (preference != this.mUnrestrictedData) {
            return false;
        } else {
            this.mDataSaverBackend.setIsAllowlisted(this.mAppItem.key, this.mPackageName, ((Boolean) obj).booleanValue());
            return true;
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mAppSettings) {
            return super.onPreferenceTreeClick(preference);
        }
        getActivity().startActivityAsUser(this.mAppSettingsIntent, new UserHandle(UserHandle.getUserId(this.mAppItem.key)));
        return true;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.app_data_usage;
    }

    /* access modifiers changed from: package-private */
    public void updatePrefs() {
        updatePrefs(getAppRestrictBackground(), getUnrestrictData());
    }

    /* access modifiers changed from: package-private */
    public UidDetailProvider getUidDetailProvider() {
        return new UidDetailProvider(this.mContext);
    }

    private void initCycle() {
        this.mCycle = (SpinnerPreference) findPreference("cycle");
        CycleAdapter cycleAdapter = new CycleAdapter(this.mContext, this.mCycle, this.mCycleListener);
        this.mCycleAdapter = cycleAdapter;
        ArrayList<Long> arrayList = this.mCycles;
        if (arrayList != null) {
            cycleAdapter.setInitialCycleList(arrayList, this.mSelectedCycle);
            this.mCycle.setHasCycles(true);
        }
    }

    private void setBackPreferenceListAnimatorIfLoaded() {
        if (!this.mIsLoading) {
            RecyclerView listView = getListView();
            if (listView.getItemAnimator() == null) {
                listView.setItemAnimator(new DefaultItemAnimator());
            }
        }
    }

    private void updatePrefs(boolean z, boolean z2) {
        setBackPreferenceListAnimatorIfLoaded();
        RestrictedLockUtils.EnforcedAdmin checkIfMeteredDataRestricted = RestrictedLockUtilsInternal.checkIfMeteredDataRestricted(this.mContext, this.mPackageName, UserHandle.getUserId(this.mAppItem.key));
        RestrictedSwitchPreference restrictedSwitchPreference = this.mRestrictBackground;
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setChecked(!z);
            this.mRestrictBackground.setDisabledByAdmin(checkIfMeteredDataRestricted);
        }
        RestrictedSwitchPreference restrictedSwitchPreference2 = this.mUnrestrictedData;
        if (restrictedSwitchPreference2 == null) {
            return;
        }
        if (z) {
            restrictedSwitchPreference2.setVisible(false);
            return;
        }
        restrictedSwitchPreference2.setVisible(true);
        this.mUnrestrictedData.setChecked(z2);
        this.mUnrestrictedData.setDisabledByAdmin(checkIfMeteredDataRestricted);
    }

    private void addUid(int i) {
        if (Process.isSdkSandboxUid(i)) {
            i = Process.getAppUidForSdkSandboxUid(i);
        }
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (packagesForUid != null) {
            for (String add : packagesForUid) {
                this.mPackages.add(add);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void bindData(int i) {
        long j;
        long j2;
        List<NetworkCycleDataForUid> list = this.mUsageData;
        if (list == null || i >= list.size()) {
            j2 = 0;
            this.mCycle.setHasCycles(false);
            j = 0;
        } else {
            this.mCycle.setHasCycles(true);
            NetworkCycleDataForUid networkCycleDataForUid = this.mUsageData.get(i);
            j2 = networkCycleDataForUid.getBackgroudUsage();
            j = networkCycleDataForUid.getForegroudUsage();
        }
        this.mTotalUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j2 + j));
        this.mForegroundUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j));
        this.mBackgroundUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j2));
    }

    private boolean getAppRestrictBackground() {
        return (this.services.mPolicyManager.getUidPolicy(this.mAppItem.key) & 1) != 0;
    }

    private boolean getUnrestrictData() {
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            return dataSaverBackend.isAllowlisted(this.mAppItem.key);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addEntityHeader() {
        /*
            r8 = this;
            android.util.ArraySet<java.lang.String> r0 = r8.mPackages
            int r0 = r0.size()
            r1 = 0
            r2 = 0
            if (r0 == 0) goto L_0x0013
            android.util.ArraySet<java.lang.String> r0 = r8.mPackages
            java.lang.Object r0 = r0.valueAt(r2)
            java.lang.String r0 = (java.lang.String) r0
            goto L_0x0014
        L_0x0013:
            r0 = r1
        L_0x0014:
            if (r0 == 0) goto L_0x003b
            android.content.pm.PackageManager r3 = r8.mPackageManager     // Catch:{ NameNotFoundException -> 0x0025 }
            com.android.settingslib.AppItem r4 = r8.mAppItem     // Catch:{ NameNotFoundException -> 0x0025 }
            int r4 = r4.key     // Catch:{ NameNotFoundException -> 0x0025 }
            int r4 = android.os.UserHandle.getUserId(r4)     // Catch:{ NameNotFoundException -> 0x0025 }
            int r3 = r3.getPackageUidAsUser(r0, r4)     // Catch:{ NameNotFoundException -> 0x0025 }
            goto L_0x003c
        L_0x0025:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Skipping UID because cannot find package "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "AppDataUsage"
            android.util.Log.w(r4, r3)
        L_0x003b:
            r3 = r2
        L_0x003c:
            com.android.settingslib.AppItem r4 = r8.mAppItem
            int r4 = r4.key
            if (r4 <= 0) goto L_0x0044
            r4 = 1
            goto L_0x0045
        L_0x0044:
            r4 = r2
        L_0x0045:
            androidx.fragment.app.FragmentActivity r5 = r8.getActivity()
            com.android.settings.widget.EntityHeaderController r1 = com.android.settings.widget.EntityHeaderController.newInstance(r5, r8, r1)
            androidx.recyclerview.widget.RecyclerView r6 = r8.getListView()
            com.android.settingslib.core.lifecycle.Lifecycle r7 = r8.getSettingsLifecycle()
            com.android.settings.widget.EntityHeaderController r1 = r1.setRecyclerView(r6, r7)
            com.android.settings.widget.EntityHeaderController r1 = r1.setUid(r3)
            com.android.settings.widget.EntityHeaderController r1 = r1.setHasAppInfoLink(r4)
            com.android.settings.widget.EntityHeaderController r1 = r1.setButtonActions(r2, r2)
            android.graphics.drawable.Drawable r2 = r8.mIcon
            com.android.settings.widget.EntityHeaderController r1 = r1.setIcon((android.graphics.drawable.Drawable) r2)
            java.lang.CharSequence r2 = r8.mLabel
            com.android.settings.widget.EntityHeaderController r1 = r1.setLabel((java.lang.CharSequence) r2)
            com.android.settings.widget.EntityHeaderController r0 = r1.setPackageName(r0)
            android.content.Context r1 = r8.getPrefContext()
            com.android.settingslib.widget.LayoutPreference r0 = r0.done((android.app.Activity) r5, (android.content.Context) r1)
            androidx.preference.PreferenceScreen r8 = r8.getPreferenceScreen()
            r8.addPreference(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.datausage.AppDataUsage.addEntityHeader():void");
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
        if (this.mAppItem.uids.get(i, false)) {
            updatePrefs(getAppRestrictBackground(), z);
        }
    }

    public void onDenylistStatusChanged(int i, boolean z) {
        if (this.mAppItem.uids.get(i, false)) {
            updatePrefs(z, getUnrestrictData());
        }
    }
}
