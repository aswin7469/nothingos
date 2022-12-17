package com.android.settings;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.TrustedCredentialsDialogBuilder;
import com.android.settings.TrustedCredentialsSettings;
import com.android.settings.core.InstrumentedFragment;
import com.nothing.p006ui.support.NtCustSwitch;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntConsumer;

public class TrustedCredentialsFragment extends InstrumentedFragment implements TrustedCredentialsDialogBuilder.DelegateInterface {
    /* access modifiers changed from: private */
    public final Set<AdapterData.AliasLoader> mAliasLoaders = new ArraySet(2);
    /* access modifiers changed from: private */
    public AliasOperation mAliasOperation;
    private ArraySet<Integer> mConfirmedCredentialUsers;
    private IntConsumer mConfirmingCredentialListener;
    private int mConfirmingCredentialUser;
    /* access modifiers changed from: private */
    public DevicePolicyManager mDevicePolicyManager;
    /* access modifiers changed from: private */
    public ViewGroup mFragmentView;
    /* access modifiers changed from: private */
    public GroupAdapter mGroupAdapter;
    /* access modifiers changed from: private */
    @GuardedBy({"mKeyChainConnectionByProfileId"})
    public final SparseArray<KeyChain.KeyChainConnection> mKeyChainConnectionByProfileId = new SparseArray<>();
    private KeyguardManager mKeyguardManager;
    /* access modifiers changed from: private */
    public int mTrustAllCaUserId;
    /* access modifiers changed from: private */
    public UserManager mUserManager;
    private final BroadcastReceiver mWorkProfileChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.MANAGED_PROFILE_AVAILABLE".equals(action) || "android.intent.action.MANAGED_PROFILE_UNAVAILABLE".equals(action) || "android.intent.action.MANAGED_PROFILE_UNLOCKED".equals(action)) {
                TrustedCredentialsFragment.this.mGroupAdapter.load();
            }
        }
    };

    public int getMetricsCategory() {
        return 92;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mDevicePolicyManager = (DevicePolicyManager) activity.getSystemService(DevicePolicyManager.class);
        this.mUserManager = (UserManager) activity.getSystemService(UserManager.class);
        this.mKeyguardManager = (KeyguardManager) activity.getSystemService(KeyguardManager.class);
        this.mTrustAllCaUserId = activity.getIntent().getIntExtra("ARG_SHOW_NEW_FOR_USER", -10000);
        this.mConfirmedCredentialUsers = new ArraySet<>(2);
        this.mConfirmingCredentialUser = -10000;
        if (bundle != null) {
            this.mConfirmingCredentialUser = bundle.getInt("ConfirmingCredentialUser", -10000);
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("ConfirmedCredentialUsers");
            if (integerArrayList != null) {
                this.mConfirmedCredentialUsers.addAll(integerArrayList);
            }
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNLOCKED");
        activity.registerReceiver(this.mWorkProfileChangedReceiver, intentFilter);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putIntegerArrayList("ConfirmedCredentialUsers", new ArrayList(this.mConfirmedCredentialUsers));
        bundle.putInt("ConfirmingCredentialUser", this.mConfirmingCredentialUser);
        this.mGroupAdapter.saveState(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle bundle2;
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R$layout.trusted_credentials, viewGroup, false);
        this.mFragmentView = viewGroup2;
        ViewGroup viewGroup3 = (ViewGroup) viewGroup2.findViewById(R$id.content);
        GroupAdapter groupAdapter = new GroupAdapter(requireArguments().getInt("tab") == 0 ? TrustedCredentialsSettings.Tab.SYSTEM : TrustedCredentialsSettings.Tab.USER);
        this.mGroupAdapter = groupAdapter;
        int groupCount = groupAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            if (bundle == null) {
                bundle2 = null;
            } else {
                bundle2 = bundle.getBundle(this.mGroupAdapter.getKey(i));
            }
            createChildView(layoutInflater, viewGroup3, bundle2, i);
        }
        return this.mFragmentView;
    }

    private void createChildView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, int i) {
        boolean isManagedProfile = this.mGroupAdapter.getUserInfoByGroup(i).isManagedProfile();
        ChildAdapter createChildAdapter = this.mGroupAdapter.createChildAdapter(i);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R$layout.trusted_credential_list_container, viewGroup, false);
        createChildAdapter.setContainerView(linearLayout, bundle);
        int groupCount = this.mGroupAdapter.getGroupCount();
        boolean z = true;
        createChildAdapter.showHeader(groupCount > 1);
        createChildAdapter.showDivider(isManagedProfile);
        if (groupCount > 2 && isManagedProfile) {
            z = false;
        }
        createChildAdapter.setExpandIfAvailable(z, bundle);
        if (isManagedProfile) {
            viewGroup.addView(linearLayout);
        } else {
            viewGroup.addView(linearLayout, 0);
        }
    }

    public void onResume() {
        super.onResume();
        this.mFragmentView.requestLayout();
    }

    public void onDestroy() {
        getActivity().unregisterReceiver(this.mWorkProfileChangedReceiver);
        for (AdapterData.AliasLoader cancel : this.mAliasLoaders) {
            cancel.cancel(true);
        }
        this.mAliasLoaders.clear();
        AliasOperation aliasOperation = this.mAliasOperation;
        if (aliasOperation != null) {
            aliasOperation.cancel(true);
            this.mAliasOperation = null;
        }
        closeKeyChainConnections();
        super.onDestroy();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            int i3 = this.mConfirmingCredentialUser;
            IntConsumer intConsumer = this.mConfirmingCredentialListener;
            this.mConfirmingCredentialUser = -10000;
            this.mConfirmingCredentialListener = null;
            if (i2 == -1) {
                this.mConfirmedCredentialUsers.add(Integer.valueOf(i3));
                if (intConsumer != null) {
                    intConsumer.accept(i3);
                }
            }
        }
    }

    private void closeKeyChainConnections() {
        synchronized (this.mKeyChainConnectionByProfileId) {
            int size = this.mKeyChainConnectionByProfileId.size();
            for (int i = 0; i < size; i++) {
                this.mKeyChainConnectionByProfileId.valueAt(i).close();
            }
            this.mKeyChainConnectionByProfileId.clear();
        }
    }

    /* access modifiers changed from: private */
    public boolean startConfirmCredential(int i) {
        Intent createConfirmDeviceCredentialIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent((CharSequence) null, (CharSequence) null, i);
        if (createConfirmDeviceCredentialIntent == null) {
            return false;
        }
        this.mConfirmingCredentialUser = i;
        startActivityForResult(createConfirmDeviceCredentialIntent, 1);
        return true;
    }

    private class GroupAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
        private final ArrayList<ChildAdapter> mChildAdapters;
        private final AdapterData mData;

        public long getChildId(int i, int i2) {
            return (long) i2;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

        private GroupAdapter(TrustedCredentialsSettings.Tab tab) {
            this.mChildAdapters = new ArrayList<>();
            this.mData = new AdapterData(tab, this);
            load();
        }

        public int getGroupCount() {
            return this.mData.mCertHoldersByUserId.size();
        }

        public int getChildrenCount(int i) {
            List list = (List) this.mData.mCertHoldersByUserId.valueAt(i);
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        public UserHandle getGroup(int i) {
            return new UserHandle(this.mData.mCertHoldersByUserId.keyAt(i));
        }

        public CertHolder getChild(int i, int i2) {
            return (CertHolder) ((List) this.mData.mCertHoldersByUserId.get(getUserIdByGroup(i))).get(i2);
        }

        public long getGroupId(int i) {
            return (long) getUserIdByGroup(i);
        }

        private int getUserIdByGroup(int i) {
            return this.mData.mCertHoldersByUserId.keyAt(i);
        }

        public UserInfo getUserInfoByGroup(int i) {
            return TrustedCredentialsFragment.this.mUserManager.getUserInfo(getUserIdByGroup(i));
        }

        public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = Utils.inflateCategoryHeader((LayoutInflater) TrustedCredentialsFragment.this.getActivity().getSystemService("layout_inflater"), viewGroup);
            }
            TextView textView = (TextView) view.findViewById(16908310);
            if (getUserInfoByGroup(i).isManagedProfile()) {
                textView.setText(TrustedCredentialsFragment.this.mDevicePolicyManager.getResources().getString("Settings.WORK_CATEGORY_HEADER", new C0565x3a554d2e(this)));
            } else {
                textView.setText(TrustedCredentialsFragment.this.mDevicePolicyManager.getResources().getString("Settings.PERSONAL_CATEGORY_HEADER", new C0566x3a554d2f(this)));
            }
            textView.setTextAlignment(6);
            return view;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getGroupView$0() {
            return TrustedCredentialsFragment.this.getString(R$string.category_work);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getGroupView$1() {
            return TrustedCredentialsFragment.this.getString(R$string.category_personal);
        }

        public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
            return getViewForCertificate(getChild(i, i2), this.mData.mTab, view, viewGroup);
        }

        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
            TrustedCredentialsFragment.this.showCertDialog(getChild(i, i2));
            return true;
        }

        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
            return !checkGroupExpandableAndStartWarningActivity(i);
        }

        public void load() {
            AdapterData adapterData = this.mData;
            Objects.requireNonNull(adapterData);
            new AdapterData.AliasLoader().execute(new Void[0]);
        }

        public void remove(CertHolder certHolder) {
            this.mData.remove(certHolder);
        }

        /* access modifiers changed from: package-private */
        public ChildAdapter createChildAdapter(int i) {
            ChildAdapter childAdapter = new ChildAdapter(this, i);
            this.mChildAdapters.add(childAdapter);
            return childAdapter;
        }

        public boolean checkGroupExpandableAndStartWarningActivity(int i) {
            return checkGroupExpandableAndStartWarningActivity(i, true);
        }

        public boolean checkGroupExpandableAndStartWarningActivity(int i, boolean z) {
            UserHandle group = getGroup(i);
            int identifier = group.getIdentifier();
            if (TrustedCredentialsFragment.this.mUserManager.isQuietModeEnabled(group)) {
                if (z) {
                    TrustedCredentialsFragment.this.getActivity().startActivity(UnlaunchableAppActivity.createInQuietModeDialogIntent(identifier));
                }
                return false;
            } else if (TrustedCredentialsFragment.this.mUserManager.isUserUnlocked(group) || !new LockPatternUtils(TrustedCredentialsFragment.this.getActivity()).isSeparateProfileChallengeEnabled(identifier)) {
                return true;
            } else {
                if (z) {
                    boolean unused = TrustedCredentialsFragment.this.startConfirmCredential(identifier);
                }
                return false;
            }
        }

        private View getViewForCertificate(CertHolder certHolder, TrustedCredentialsSettings.Tab tab, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = LayoutInflater.from(TrustedCredentialsFragment.this.getActivity()).inflate(R$layout.trusted_credential, viewGroup, false);
                view2.setTag(viewHolder);
                viewHolder.mSubjectPrimaryView = (TextView) view2.findViewById(R$id.trusted_credential_subject_primary);
                viewHolder.mSubjectSecondaryView = (TextView) view2.findViewById(R$id.trusted_credential_subject_secondary);
                viewHolder.mSwitch = (NtCustSwitch) view2.findViewById(R$id.trusted_credential_status);
                viewHolder.mSwitch.setOnClickListener(new C0567x3a554d30(this));
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mSubjectPrimaryView.setText(certHolder.mSubjectPrimary);
            viewHolder.mSubjectSecondaryView.setText(certHolder.mSubjectSecondary);
            if (tab.mSwitch) {
                viewHolder.mSwitch.setChecked(!certHolder.mDeleted);
                viewHolder.mSwitch.setEnabled(!TrustedCredentialsFragment.this.mUserManager.hasUserRestriction("no_config_credentials", new UserHandle(certHolder.mProfileId)));
                viewHolder.mSwitch.setVisibility(0);
                viewHolder.mSwitch.setTag(certHolder);
            }
            return view2;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getViewForCertificate$2(View view) {
            TrustedCredentialsFragment.this.removeOrInstallCert((CertHolder) view.getTag());
        }

        /* access modifiers changed from: private */
        public void saveState(Bundle bundle) {
            int size = this.mChildAdapters.size();
            for (int i = 0; i < size; i++) {
                bundle.putBundle(getKey(i), this.mChildAdapters.get(i).saveState());
            }
        }

        /* access modifiers changed from: private */
        public String getKey(int i) {
            return "Group" + getUserIdByGroup(i);
        }

        private class ViewHolder {
            /* access modifiers changed from: private */
            public TextView mSubjectPrimaryView;
            /* access modifiers changed from: private */
            public TextView mSubjectSecondaryView;
            /* access modifiers changed from: private */
            public NtCustSwitch mSwitch;

            private ViewHolder() {
            }
        }
    }

    private class ChildAdapter extends BaseAdapter implements View.OnClickListener, AdapterView.OnItemClickListener {
        private LinearLayout mContainerView;
        private final int[] mEmptyStateSet;
        private final int[] mGroupExpandedStateSet;
        private final int mGroupPosition;
        private ViewGroup mHeaderView;
        private final LinearLayout.LayoutParams mHideContainerLayoutParams;
        private final LinearLayout.LayoutParams mHideListLayoutParams;
        private ImageView mIndicatorView;
        private boolean mIsListExpanded;
        private ListView mListView;
        private final DataSetObserver mObserver;
        private final GroupAdapter mParent;
        private final LinearLayout.LayoutParams mShowLayoutParams;

        private ChildAdapter(GroupAdapter groupAdapter, int i) {
            this.mGroupExpandedStateSet = new int[]{16842920};
            this.mEmptyStateSet = new int[0];
            this.mHideContainerLayoutParams = new LinearLayout.LayoutParams(-1, -2, 0.0f);
            this.mHideListLayoutParams = new LinearLayout.LayoutParams(-1, 0);
            this.mShowLayoutParams = new LinearLayout.LayoutParams(-1, -1, 1.0f);
            C05641 r0 = new DataSetObserver() {
                public void onChanged() {
                    super.onChanged();
                    ChildAdapter.super.notifyDataSetChanged();
                }

                public void onInvalidated() {
                    super.onInvalidated();
                    ChildAdapter.super.notifyDataSetInvalidated();
                }
            };
            this.mObserver = r0;
            this.mIsListExpanded = true;
            this.mParent = groupAdapter;
            this.mGroupPosition = i;
            groupAdapter.registerDataSetObserver(r0);
        }

        public int getCount() {
            return this.mParent.getChildrenCount(this.mGroupPosition);
        }

        public CertHolder getItem(int i) {
            return this.mParent.getChild(this.mGroupPosition, i);
        }

        public long getItemId(int i) {
            return this.mParent.getChildId(this.mGroupPosition, i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return this.mParent.getChildView(this.mGroupPosition, i, false, view, viewGroup);
        }

        public void notifyDataSetChanged() {
            this.mParent.notifyDataSetChanged();
        }

        public void notifyDataSetInvalidated() {
            this.mParent.notifyDataSetInvalidated();
        }

        public void onClick(View view) {
            this.mIsListExpanded = checkGroupExpandableAndStartWarningActivity() && !this.mIsListExpanded;
            refreshViews();
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            TrustedCredentialsFragment.this.showCertDialog(getItem(i));
        }

        public void setContainerView(LinearLayout linearLayout, Bundle bundle) {
            SparseArray sparseParcelableArray;
            this.mContainerView = linearLayout;
            linearLayout.setSaveFromParentEnabled(false);
            ListView listView = (ListView) this.mContainerView.findViewById(R$id.cert_list);
            this.mListView = listView;
            listView.setAdapter(this);
            this.mListView.setOnItemClickListener(this);
            this.mListView.setItemsCanFocus(true);
            ViewGroup viewGroup = (ViewGroup) this.mContainerView.findViewById(R$id.header_view);
            this.mHeaderView = viewGroup;
            viewGroup.setOnClickListener(this);
            ImageView imageView = (ImageView) this.mHeaderView.findViewById(R$id.group_indicator);
            this.mIndicatorView = imageView;
            imageView.setImageDrawable(getGroupIndicator());
            FrameLayout frameLayout = (FrameLayout) this.mHeaderView.findViewById(R$id.header_content_container);
            frameLayout.addView(this.mParent.getGroupView(this.mGroupPosition, true, (View) null, frameLayout));
            if (bundle != null && (sparseParcelableArray = bundle.getSparseParcelableArray("Container", Parcelable.class)) != null) {
                this.mContainerView.restoreHierarchyState(sparseParcelableArray);
            }
        }

        public void showHeader(boolean z) {
            this.mHeaderView.setVisibility(z ? 0 : 8);
        }

        public void showDivider(boolean z) {
            this.mHeaderView.findViewById(R$id.header_divider).setVisibility(z ? 0 : 8);
        }

        public void setExpandIfAvailable(boolean z, Bundle bundle) {
            if (bundle != null) {
                z = bundle.getBoolean("IsListExpanded");
            }
            boolean z2 = false;
            if (z && this.mParent.checkGroupExpandableAndStartWarningActivity(this.mGroupPosition, false)) {
                z2 = true;
            }
            this.mIsListExpanded = z2;
            refreshViews();
        }

        private boolean checkGroupExpandableAndStartWarningActivity() {
            return this.mParent.checkGroupExpandableAndStartWarningActivity(this.mGroupPosition);
        }

        private void refreshViews() {
            int[] iArr;
            LinearLayout.LayoutParams layoutParams;
            LinearLayout.LayoutParams layoutParams2;
            ImageView imageView = this.mIndicatorView;
            if (this.mIsListExpanded) {
                iArr = this.mGroupExpandedStateSet;
            } else {
                iArr = this.mEmptyStateSet;
            }
            imageView.setImageState(iArr, false);
            ListView listView = this.mListView;
            if (this.mIsListExpanded) {
                layoutParams = this.mShowLayoutParams;
            } else {
                layoutParams = this.mHideListLayoutParams;
            }
            listView.setLayoutParams(layoutParams);
            LinearLayout linearLayout = this.mContainerView;
            if (this.mIsListExpanded) {
                layoutParams2 = this.mShowLayoutParams;
            } else {
                layoutParams2 = this.mHideContainerLayoutParams;
            }
            linearLayout.setLayoutParams(layoutParams2);
        }

        private Drawable getGroupIndicator() {
            TypedArray obtainStyledAttributes = TrustedCredentialsFragment.this.getActivity().obtainStyledAttributes((AttributeSet) null, R.styleable.ExpandableListView, 16842863, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }

        /* access modifiers changed from: private */
        public Bundle saveState() {
            Bundle bundle = new Bundle();
            SparseArray sparseArray = new SparseArray();
            this.mContainerView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("Container", sparseArray);
            bundle.putBoolean("IsListExpanded", this.mIsListExpanded);
            return bundle;
        }
    }

    private class AdapterData {
        /* access modifiers changed from: private */
        public final GroupAdapter mAdapter;
        /* access modifiers changed from: private */
        public final SparseArray<List<CertHolder>> mCertHoldersByUserId;
        /* access modifiers changed from: private */
        public final TrustedCredentialsSettings.Tab mTab;

        private AdapterData(TrustedCredentialsSettings.Tab tab, GroupAdapter groupAdapter) {
            this.mCertHoldersByUserId = new SparseArray<>();
            this.mAdapter = groupAdapter;
            this.mTab = tab;
        }

        private class AliasLoader extends AsyncTask<Void, Integer, SparseArray<List<CertHolder>>> {
            private View mContentView;
            private Context mContext;
            private ProgressBar mProgressBar;

            AliasLoader() {
                this.mContext = TrustedCredentialsFragment.this.getActivity();
                TrustedCredentialsFragment.this.mAliasLoaders.add(this);
                for (UserHandle identifier : TrustedCredentialsFragment.this.mUserManager.getUserProfiles()) {
                    AdapterData.this.mCertHoldersByUserId.put(identifier.getIdentifier(), new ArrayList());
                }
            }

            private boolean shouldSkipProfile(UserHandle userHandle) {
                return TrustedCredentialsFragment.this.mUserManager.isQuietModeEnabled(userHandle) || !TrustedCredentialsFragment.this.mUserManager.isUserUnlocked(userHandle.getIdentifier());
            }

            /* access modifiers changed from: protected */
            public void onPreExecute() {
                this.mProgressBar = (ProgressBar) TrustedCredentialsFragment.this.mFragmentView.findViewById(R$id.progress);
                this.mContentView = TrustedCredentialsFragment.this.mFragmentView.findViewById(R$id.content);
                this.mProgressBar.setVisibility(0);
                this.mContentView.setVisibility(8);
            }

            /* access modifiers changed from: protected */
            public SparseArray<List<CertHolder>> doInBackground(Void... voidArr) {
                Iterator<UserHandle> it;
                SparseArray<List<CertHolder>> sparseArray = new SparseArray<>();
                try {
                    synchronized (TrustedCredentialsFragment.this.mKeyChainConnectionByProfileId) {
                        List<UserHandle> userProfiles = TrustedCredentialsFragment.this.mUserManager.getUserProfiles();
                        SparseArray sparseArray2 = new SparseArray(userProfiles.size());
                        int i = 0;
                        for (UserHandle next : userProfiles) {
                            int identifier = next.getIdentifier();
                            if (!shouldSkipProfile(next)) {
                                KeyChain.KeyChainConnection bindAsUser = KeyChain.bindAsUser(this.mContext, next);
                                TrustedCredentialsFragment.this.mKeyChainConnectionByProfileId.put(identifier, bindAsUser);
                                List<String> aliases = AdapterData.this.mTab.getAliases(bindAsUser.getService());
                                if (isCancelled()) {
                                    SparseArray<List<CertHolder>> sparseArray3 = new SparseArray<>();
                                    return sparseArray3;
                                }
                                i += aliases.size();
                                sparseArray2.put(identifier, aliases);
                            }
                        }
                        Iterator<UserHandle> it2 = userProfiles.iterator();
                        int i2 = 0;
                        while (it2.hasNext()) {
                            UserHandle next2 = it2.next();
                            int identifier2 = next2.getIdentifier();
                            List<String> list = (List) sparseArray2.get(identifier2);
                            if (isCancelled()) {
                                SparseArray<List<CertHolder>> sparseArray4 = new SparseArray<>();
                                return sparseArray4;
                            }
                            KeyChain.KeyChainConnection keyChainConnection = (KeyChain.KeyChainConnection) TrustedCredentialsFragment.this.mKeyChainConnectionByProfileId.get(identifier2);
                            if (!shouldSkipProfile(next2) && list != null) {
                                if (keyChainConnection != null) {
                                    IKeyChainService service = keyChainConnection.getService();
                                    ArrayList arrayList = new ArrayList(i);
                                    for (String str : list) {
                                        X509Certificate certificate = KeyChain.toCertificate(service.getEncodedCaCertificate(str, true));
                                        CertHolder certHolder = r9;
                                        Iterator<UserHandle> it3 = it2;
                                        ArrayList arrayList2 = arrayList;
                                        CertHolder certHolder2 = new CertHolder(service, AdapterData.this.mAdapter, AdapterData.this.mTab, str, certificate, identifier2);
                                        arrayList2.add(certHolder);
                                        i2++;
                                        publishProgress(new Integer[]{Integer.valueOf(i2), Integer.valueOf(i)});
                                        arrayList = arrayList2;
                                        it2 = it3;
                                        identifier2 = identifier2;
                                    }
                                    it = it2;
                                    ArrayList arrayList3 = arrayList;
                                    Collections.sort(arrayList3);
                                    sparseArray.put(identifier2, arrayList3);
                                    it2 = it;
                                }
                            }
                            it = it2;
                            sparseArray.put(identifier2, new ArrayList(0));
                            it2 = it;
                        }
                        return sparseArray;
                    }
                } catch (RemoteException e) {
                    Log.e("TrustedCredentialsFragment", "Remote exception while loading aliases.", e);
                    return new SparseArray<>();
                } catch (InterruptedException e2) {
                    Log.e("TrustedCredentialsFragment", "InterruptedException while loading aliases.", e2);
                    return new SparseArray<>();
                }
            }

            /* access modifiers changed from: protected */
            public void onProgressUpdate(Integer... numArr) {
                int intValue = numArr[0].intValue();
                int intValue2 = numArr[1].intValue();
                if (intValue2 != this.mProgressBar.getMax()) {
                    this.mProgressBar.setMax(intValue2);
                }
                this.mProgressBar.setProgress(intValue);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(SparseArray<List<CertHolder>> sparseArray) {
                AdapterData.this.mCertHoldersByUserId.clear();
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    AdapterData.this.mCertHoldersByUserId.put(sparseArray.keyAt(i), sparseArray.valueAt(i));
                }
                AdapterData.this.mAdapter.notifyDataSetChanged();
                this.mProgressBar.setVisibility(8);
                this.mContentView.setVisibility(0);
                this.mProgressBar.setProgress(0);
                TrustedCredentialsFragment.this.mAliasLoaders.remove(this);
                showTrustAllCaDialogIfNeeded();
            }

            private boolean isUserTabAndTrustAllCertMode() {
                return TrustedCredentialsFragment.this.isTrustAllCaCertModeInProgress() && AdapterData.this.mTab == TrustedCredentialsSettings.Tab.USER;
            }

            private void showTrustAllCaDialogIfNeeded() {
                List<CertHolder> list;
                if (isUserTabAndTrustAllCertMode() && (list = (List) AdapterData.this.mCertHoldersByUserId.get(TrustedCredentialsFragment.this.mTrustAllCaUserId)) != null) {
                    ArrayList arrayList = new ArrayList();
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
                    for (CertHolder certHolder : list) {
                        if (certHolder != null && !devicePolicyManager.isCaCertApproved(certHolder.mAlias, TrustedCredentialsFragment.this.mTrustAllCaUserId)) {
                            arrayList.add(certHolder);
                        }
                    }
                    if (arrayList.size() == 0) {
                        Log.w("TrustedCredentialsFragment", "no cert is pending approval for user " + TrustedCredentialsFragment.this.mTrustAllCaUserId);
                        return;
                    }
                    TrustedCredentialsFragment.this.showTrustAllCaDialog(arrayList);
                }
            }
        }

        public void remove(CertHolder certHolder) {
            List list;
            SparseArray<List<CertHolder>> sparseArray = this.mCertHoldersByUserId;
            if (sparseArray != null && (list = sparseArray.get(certHolder.mProfileId)) != null) {
                list.remove(certHolder);
            }
        }
    }

    static class CertHolder implements Comparable<CertHolder> {
        /* access modifiers changed from: private */
        public final GroupAdapter mAdapter;
        /* access modifiers changed from: private */
        public final String mAlias;
        /* access modifiers changed from: private */
        public boolean mDeleted;
        public int mProfileId;
        private final IKeyChainService mService;
        private final SslCertificate mSslCert;
        /* access modifiers changed from: private */
        public final String mSubjectPrimary;
        /* access modifiers changed from: private */
        public final String mSubjectSecondary;
        /* access modifiers changed from: private */
        public final TrustedCredentialsSettings.Tab mTab;
        /* access modifiers changed from: private */
        public final X509Certificate mX509Cert;

        private CertHolder(IKeyChainService iKeyChainService, GroupAdapter groupAdapter, TrustedCredentialsSettings.Tab tab, String str, X509Certificate x509Certificate, int i) {
            this.mProfileId = i;
            this.mService = iKeyChainService;
            this.mAdapter = groupAdapter;
            this.mTab = tab;
            this.mAlias = str;
            this.mX509Cert = x509Certificate;
            SslCertificate sslCertificate = new SslCertificate(x509Certificate);
            this.mSslCert = sslCertificate;
            String cName = sslCertificate.getIssuedTo().getCName();
            String oName = sslCertificate.getIssuedTo().getOName();
            String uName = sslCertificate.getIssuedTo().getUName();
            if (!oName.isEmpty()) {
                if (!cName.isEmpty()) {
                    this.mSubjectPrimary = oName;
                    this.mSubjectSecondary = cName;
                } else {
                    this.mSubjectPrimary = oName;
                    this.mSubjectSecondary = uName;
                }
            } else if (!cName.isEmpty()) {
                this.mSubjectPrimary = cName;
                this.mSubjectSecondary = "";
            } else {
                this.mSubjectPrimary = sslCertificate.getIssuedTo().getDName();
                this.mSubjectSecondary = "";
            }
            try {
                this.mDeleted = tab.deleted(iKeyChainService, str);
            } catch (RemoteException e) {
                Log.e("TrustedCredentialsFragment", "Remote exception while checking if alias " + this.mAlias + " is deleted.", e);
                this.mDeleted = false;
            }
        }

        public int compareTo(CertHolder certHolder) {
            int compareToIgnoreCase = this.mSubjectPrimary.compareToIgnoreCase(certHolder.mSubjectPrimary);
            if (compareToIgnoreCase != 0) {
                return compareToIgnoreCase;
            }
            return this.mSubjectSecondary.compareToIgnoreCase(certHolder.mSubjectSecondary);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CertHolder)) {
                return false;
            }
            return this.mAlias.equals(((CertHolder) obj).mAlias);
        }

        public int hashCode() {
            return this.mAlias.hashCode();
        }

        public int getUserId() {
            return this.mProfileId;
        }

        public String getAlias() {
            return this.mAlias;
        }

        public boolean isSystemCert() {
            return this.mTab == TrustedCredentialsSettings.Tab.SYSTEM;
        }

        public boolean isDeleted() {
            return this.mDeleted;
        }
    }

    /* access modifiers changed from: private */
    public boolean isTrustAllCaCertModeInProgress() {
        return this.mTrustAllCaUserId != -10000;
    }

    /* access modifiers changed from: private */
    public void showTrustAllCaDialog(List<CertHolder> list) {
        new TrustedCredentialsDialogBuilder(getActivity(), this).setCertHolders((CertHolder[]) list.toArray(new CertHolder[list.size()])).setOnDismissListener(new TrustedCredentialsFragment$$ExternalSyntheticLambda0(this)).show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showTrustAllCaDialog$0(DialogInterface dialogInterface) {
        getActivity().getIntent().removeExtra("ARG_SHOW_NEW_FOR_USER");
        this.mTrustAllCaUserId = -10000;
    }

    /* access modifiers changed from: private */
    public void showCertDialog(CertHolder certHolder) {
        new TrustedCredentialsDialogBuilder(getActivity(), this).setCertHolder(certHolder).show();
    }

    public List<X509Certificate> getX509CertsFromCertHolder(CertHolder certHolder) {
        ArrayList arrayList = null;
        try {
            synchronized (this.mKeyChainConnectionByProfileId) {
                try {
                    IKeyChainService service = this.mKeyChainConnectionByProfileId.get(certHolder.mProfileId).getService();
                    List<String> caCertificateChainAliases = service.getCaCertificateChainAliases(certHolder.mAlias, true);
                    ArrayList arrayList2 = new ArrayList(caCertificateChainAliases.size());
                    try {
                        for (String encodedCaCertificate : caCertificateChainAliases) {
                            arrayList2.add(KeyChain.toCertificate(service.getEncodedCaCertificate(encodedCaCertificate, true)));
                        }
                        return arrayList2;
                    } catch (Throwable th) {
                        th = th;
                        arrayList = arrayList2;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.e("TrustedCredentialsFragment", "RemoteException while retrieving certificate chain for root " + certHolder.mAlias, e);
            return arrayList;
        }
    }

    public void removeOrInstallCert(CertHolder certHolder) {
        new AliasOperation(certHolder).execute(new Void[0]);
    }

    public boolean startConfirmCredentialIfNotConfirmed(int i, IntConsumer intConsumer) {
        if (this.mConfirmedCredentialUsers.contains(Integer.valueOf(i))) {
            return false;
        }
        boolean startConfirmCredential = startConfirmCredential(i);
        if (startConfirmCredential) {
            this.mConfirmingCredentialListener = intConsumer;
        }
        return startConfirmCredential;
    }

    private class AliasOperation extends AsyncTask<Void, Void, Boolean> {
        private final CertHolder mCertHolder;

        private AliasOperation(CertHolder certHolder) {
            this.mCertHolder = certHolder;
            TrustedCredentialsFragment.this.mAliasOperation = this;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            try {
                synchronized (TrustedCredentialsFragment.this.mKeyChainConnectionByProfileId) {
                    IKeyChainService service = ((KeyChain.KeyChainConnection) TrustedCredentialsFragment.this.mKeyChainConnectionByProfileId.get(this.mCertHolder.mProfileId)).getService();
                    if (this.mCertHolder.mDeleted) {
                        service.installCaCertificate(this.mCertHolder.mX509Cert.getEncoded());
                        Boolean bool = Boolean.TRUE;
                        return bool;
                    }
                    Boolean valueOf = Boolean.valueOf(service.deleteCaCertificate(this.mCertHolder.mAlias));
                    return valueOf;
                }
            } catch (RemoteException | IllegalStateException | SecurityException | CertificateEncodingException e) {
                Log.w("TrustedCredentialsFragment", "Error while toggling alias " + this.mCertHolder.mAlias, e);
                return Boolean.FALSE;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                if (this.mCertHolder.mTab.mSwitch) {
                    CertHolder certHolder = this.mCertHolder;
                    certHolder.mDeleted = !certHolder.mDeleted;
                } else {
                    this.mCertHolder.mAdapter.remove(this.mCertHolder);
                }
                this.mCertHolder.mAdapter.notifyDataSetChanged();
            } else {
                this.mCertHolder.mAdapter.load();
            }
            TrustedCredentialsFragment.this.mAliasOperation = null;
        }
    }
}
