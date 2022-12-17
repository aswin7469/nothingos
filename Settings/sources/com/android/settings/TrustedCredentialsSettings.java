package com.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.security.IKeyChainService;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.android.settings.dashboard.DashboardFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class TrustedCredentialsSettings extends DashboardFragment {
    static final ImmutableList<Tab> TABS = ImmutableList.m27of(Tab.SYSTEM, Tab.USER);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TrustedCredentialsSettings";
    }

    public int getMetricsCategory() {
        return 92;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().setTitle(R$string.trusted_credentials);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.placeholder_preference_screen;
    }

    public void onViewCreated(View view, Bundle bundle) {
        View findViewById = view.findViewById(R$id.tab_container);
        findViewById.setVisibility(0);
        ViewPager2 viewPager2 = (ViewPager2) findViewById.findViewById(R$id.view_pager);
        viewPager2.setAdapter(new FragmentAdapter(this));
        viewPager2.setUserInputEnabled(false);
        Intent intent = getActivity().getIntent();
        if (intent != null && "com.android.settings.TRUSTED_CREDENTIALS_USER".equals(intent.getAction())) {
            viewPager2.setCurrentItem(TABS.indexOf(Tab.USER), false);
        }
        new TabLayoutMediator((TabLayout) findViewById.findViewById(R$id.tabs), viewPager2, false, false, new TrustedCredentialsSettings$$ExternalSyntheticLambda0()).attach();
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        FragmentAdapter(Fragment fragment) {
            super(fragment);
        }

        public Fragment createFragment(int i) {
            TrustedCredentialsFragment trustedCredentialsFragment = new TrustedCredentialsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("tab", i);
            trustedCredentialsFragment.setArguments(bundle);
            return trustedCredentialsFragment;
        }

        public int getItemCount() {
            return TrustedCredentialsSettings.TABS.size();
        }
    }

    enum Tab {
        SYSTEM(R$string.trusted_credentials_system_tab, true),
        USER(R$string.trusted_credentials_user_tab, false);
        
        /* access modifiers changed from: private */
        public final int mLabel;
        final boolean mSwitch;

        private Tab(int i, boolean z) {
            this.mLabel = i;
            this.mSwitch = z;
        }

        /* access modifiers changed from: package-private */
        public List<String> getAliases(IKeyChainService iKeyChainService) throws RemoteException {
            int i = C05681.$SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab[ordinal()];
            if (i == 1) {
                return iKeyChainService.getSystemCaAliases().getList();
            }
            if (i == 2) {
                return iKeyChainService.getUserCaAliases().getList();
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: package-private */
        public boolean deleted(IKeyChainService iKeyChainService, String str) throws RemoteException {
            int i = C05681.$SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab[ordinal()];
            if (i == 1) {
                return !iKeyChainService.containsCaAlias(str);
            }
            if (i == 2) {
                return false;
            }
            throw new AssertionError();
        }
    }

    /* renamed from: com.android.settings.TrustedCredentialsSettings$1 */
    static /* synthetic */ class C05681 {
        static final /* synthetic */ int[] $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.android.settings.TrustedCredentialsSettings$Tab[] r0 = com.android.settings.TrustedCredentialsSettings.Tab.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab = r0
                com.android.settings.TrustedCredentialsSettings$Tab r1 = com.android.settings.TrustedCredentialsSettings.Tab.SYSTEM     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.TrustedCredentialsSettings$Tab r1 = com.android.settings.TrustedCredentialsSettings.Tab.USER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.TrustedCredentialsSettings.C05681.<clinit>():void");
        }
    }
}
