package com.android.settings.network.telephony;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import com.android.settings.R;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.network.MobileNetworkSummaryStatus$$ExternalSyntheticLambda3;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.helper.SelectableSubscriptions;
import com.android.settings.network.helper.SubscriptionAnnotation;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class MobileNetworkActivity extends SettingsBaseActivity implements ProxySubscriptionManager.OnActiveSubscriptionChangedListener {
    static final String MOBILE_SETTINGS_TAG = "mobile_settings:";
    static final int SUB_ID_NULL = Integer.MIN_VALUE;
    private int mCurSubscriptionId = SUB_ID_NULL;
    private boolean mPendingSubscriptionChange = true;
    ProxySubscriptionManager mProxySubscriptionMgr;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        validate(intent);
        setIntent(intent);
        int i = this.mCurSubscriptionId;
        if (intent != null) {
            i = intent.getIntExtra("android.provider.extra.SUB_ID", SUB_ID_NULL);
        }
        SubscriptionInfo subscriptionOrDefault = getSubscriptionOrDefault(i);
        if (subscriptionOrDefault == null) {
            Log.d("MobileNetworkActivity", "Invalid subId request " + this.mCurSubscriptionId + " -> " + i);
            return;
        }
        int i2 = this.mCurSubscriptionId;
        updateSubscriptions(subscriptionOrDefault, null);
        if (this.mCurSubscriptionId != i2 || !doesIntentContainOptInAction(intent)) {
            removeContactDiscoveryDialog(i2);
        }
        if (!doesIntentContainOptInAction(intent)) {
            return;
        }
        maybeShowContactDiscoveryDialog(subscriptionOrDefault);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.core.SettingsBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        int intExtra;
        super.onCreate(bundle);
        if (!((UserManager) getSystemService(UserManager.class)).isAdminUser()) {
            finish();
            return;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setVisibility(0);
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        getProxySubscriptionManager().setLifecycle(mo959getLifecycle());
        Intent intent = getIntent();
        validate(intent);
        if (bundle != null) {
            intExtra = bundle.getInt("android.provider.extra.SUB_ID", SUB_ID_NULL);
        } else {
            intExtra = intent != null ? intent.getIntExtra("android.provider.extra.SUB_ID", SUB_ID_NULL) : SUB_ID_NULL;
        }
        this.mCurSubscriptionId = intExtra;
        if (intExtra == SUB_ID_NULL || intExtra == -1) {
            this.mCurSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        }
        if (this.mCurSubscriptionId == -1) {
            Iterator<SubscriptionInfo> it = SubscriptionUtil.getAvailableSubscriptions(this).iterator();
            if (it.hasNext()) {
                this.mCurSubscriptionId = it.next().getSubscriptionId();
            }
        }
        registerActiveSubscriptionsListener();
        SubscriptionInfo subscriptionOrDefault = getSubscriptionOrDefault(this.mCurSubscriptionId);
        if (subscriptionOrDefault == null) {
            Log.d("MobileNetworkActivity", "Invalid subId request " + this.mCurSubscriptionId);
            tryToFinishActivity();
            return;
        }
        maybeShowContactDiscoveryDialog(subscriptionOrDefault);
        updateSubscriptions(subscriptionOrDefault, null);
    }

    ProxySubscriptionManager getProxySubscriptionManager() {
        if (this.mProxySubscriptionMgr == null) {
            this.mProxySubscriptionMgr = ProxySubscriptionManager.getInstance(this);
        }
        return this.mProxySubscriptionMgr;
    }

    void registerActiveSubscriptionsListener() {
        getProxySubscriptionManager().addActiveSubscriptionsListener(this);
    }

    @Override // com.android.settings.network.ProxySubscriptionManager.OnActiveSubscriptionChangedListener
    public void onChanged() {
        this.mPendingSubscriptionChange = false;
        if (this.mCurSubscriptionId == SUB_ID_NULL) {
            return;
        }
        if (!mo959getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            this.mPendingSubscriptionChange = true;
            return;
        }
        SubscriptionInfo subscription = getSubscription(this.mCurSubscriptionId, null);
        if (subscription != null) {
            if (this.mCurSubscriptionId != subscription.getSubscriptionId()) {
                removeContactDiscoveryDialog(this.mCurSubscriptionId);
                updateSubscriptions(subscription, null);
                return;
            }
            updateTitleAndNavigation(subscription);
            return;
        }
        Log.w("MobileNetworkActivity", "subId missing: " + this.mCurSubscriptionId);
        if (!mo959getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            this.mPendingSubscriptionChange = true;
        } else {
            tryToFinishActivity();
        }
    }

    protected void runSubscriptionUpdate(Runnable runnable) {
        SubscriptionInfo subscription = getSubscription(this.mCurSubscriptionId, null);
        if (subscription == null) {
            tryToFinishActivity();
            return;
        }
        if (this.mCurSubscriptionId != subscription.getSubscriptionId()) {
            removeContactDiscoveryDialog(this.mCurSubscriptionId);
            updateSubscriptions(subscription, null);
        }
        runnable.run();
    }

    protected void tryToFinishActivity() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        getProxySubscriptionManager().setLifecycle(mo959getLifecycle());
        if (this.mPendingSubscriptionChange) {
            this.mPendingSubscriptionChange = false;
            runSubscriptionUpdate(new Runnable() { // from class: com.android.settings.network.telephony.MobileNetworkActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MobileNetworkActivity.this.lambda$onStart$0();
                }
            });
            return;
        }
        super.onStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0() {
        super.onStart();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        if (this.mPendingSubscriptionChange) {
            this.mPendingSubscriptionChange = false;
            runSubscriptionUpdate(new Runnable() { // from class: com.android.settings.network.telephony.MobileNetworkActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MobileNetworkActivity.this.lambda$onResume$1();
                }
            });
            return;
        }
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$1() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ProxySubscriptionManager proxySubscriptionManager = this.mProxySubscriptionMgr;
        if (proxySubscriptionManager == null) {
            return;
        }
        proxySubscriptionManager.removeActiveSubscriptionsListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        saveInstanceState(bundle);
    }

    void saveInstanceState(Bundle bundle) {
        bundle.putInt("android.provider.extra.SUB_ID", this.mCurSubscriptionId);
    }

    private void updateTitleAndNavigation(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo != null) {
            setTitle(SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this));
        }
    }

    void updateSubscriptions(SubscriptionInfo subscriptionInfo, Bundle bundle) {
        if (subscriptionInfo == null) {
            return;
        }
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        updateTitleAndNavigation(subscriptionInfo);
        if (bundle == null) {
            switchFragment(subscriptionInfo);
        }
        this.mCurSubscriptionId = subscriptionId;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: defaultSubscriptionSelection */
    public SubscriptionAnnotation lambda$getSubscriptionOrDefault$2(List<SubscriptionAnnotation> list) {
        if (list == null) {
            return null;
        }
        return list.stream().filter(MobileNetworkSummaryStatus$$ExternalSyntheticLambda3.INSTANCE).filter(MobileNetworkActivity$$ExternalSyntheticLambda4.INSTANCE).findFirst().orElse(null);
    }

    protected SubscriptionInfo getSubscriptionOrDefault(int i) {
        return getSubscription(i, i != SUB_ID_NULL ? null : new Function() { // from class: com.android.settings.network.telephony.MobileNetworkActivity$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SubscriptionAnnotation lambda$getSubscriptionOrDefault$2;
                lambda$getSubscriptionOrDefault$2 = MobileNetworkActivity.this.lambda$getSubscriptionOrDefault$2((List) obj);
                return lambda$getSubscriptionOrDefault$2;
            }
        });
    }

    protected SubscriptionInfo getSubscription(final int i, Function<List<SubscriptionAnnotation>, SubscriptionAnnotation> function) {
        List<SubscriptionAnnotation> call = new SelectableSubscriptions(this, true).call();
        Log.d("MobileNetworkActivity", "get subId=" + i + " from " + call);
        SubscriptionAnnotation orElse = call.stream().filter(MobileNetworkSummaryStatus$$ExternalSyntheticLambda3.INSTANCE).filter(new Predicate() { // from class: com.android.settings.network.telephony.MobileNetworkActivity$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getSubscription$3;
                lambda$getSubscription$3 = MobileNetworkActivity.lambda$getSubscription$3(i, (SubscriptionAnnotation) obj);
                return lambda$getSubscription$3;
            }
        }).findFirst().orElse(null);
        if (orElse == null && function != null) {
            orElse = function.apply(call);
        }
        if (orElse == null) {
            return null;
        }
        return orElse.getSubInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSubscription$3(int i, SubscriptionAnnotation subscriptionAnnotation) {
        return subscriptionAnnotation.getSubscriptionId() == i;
    }

    SubscriptionInfo getSubscriptionForSubId(int i) {
        return SubscriptionUtil.getAvailableSubscription(this, getProxySubscriptionManager(), i);
    }

    void switchFragment(SubscriptionInfo subscriptionInfo) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putInt("android.provider.extra.SUB_ID", subscriptionId);
        if (intent != null && "android.settings.MMS_MESSAGE_SETTING".equals(intent.getAction())) {
            bundle.putString(":settings:fragment_args_key", "mms_message");
        }
        String buildFragmentTag = buildFragmentTag(subscriptionId);
        if (supportFragmentManager.findFragmentByTag(buildFragmentTag) != null) {
            Log.d("MobileNetworkActivity", "Construct fragment: " + buildFragmentTag);
        }
        MobileNetworkSettings mobileNetworkSettings = new MobileNetworkSettings();
        mobileNetworkSettings.setArguments(bundle);
        beginTransaction.replace(R.id.content_frame, mobileNetworkSettings, buildFragmentTag);
        beginTransaction.commitAllowingStateLoss();
    }

    private void removeContactDiscoveryDialog(int i) {
        ContactDiscoveryDialogFragment contactDiscoveryFragment = getContactDiscoveryFragment(i);
        if (contactDiscoveryFragment != null) {
            contactDiscoveryFragment.dismiss();
        }
    }

    private ContactDiscoveryDialogFragment getContactDiscoveryFragment(int i) {
        return (ContactDiscoveryDialogFragment) getSupportFragmentManager().findFragmentByTag(ContactDiscoveryDialogFragment.getFragmentTag(i));
    }

    private void maybeShowContactDiscoveryDialog(SubscriptionInfo subscriptionInfo) {
        int i;
        CharSequence charSequence;
        if (subscriptionInfo != null) {
            i = subscriptionInfo.getSubscriptionId();
            charSequence = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this);
        } else {
            i = -1;
            charSequence = "";
        }
        boolean z = doesIntentContainOptInAction(getIntent()) && MobileNetworkUtils.isContactDiscoveryVisible(this, i) && !MobileNetworkUtils.isContactDiscoveryEnabled(this, i);
        ContactDiscoveryDialogFragment contactDiscoveryFragment = getContactDiscoveryFragment(i);
        if (z) {
            if (contactDiscoveryFragment == null) {
                contactDiscoveryFragment = ContactDiscoveryDialogFragment.newInstance(i, charSequence);
            }
            if (contactDiscoveryFragment.isAdded()) {
                return;
            }
            contactDiscoveryFragment.show(getSupportFragmentManager(), ContactDiscoveryDialogFragment.getFragmentTag(i));
        }
    }

    private boolean doesIntentContainOptInAction(Intent intent) {
        return TextUtils.equals(intent != null ? intent.getAction() : null, "android.telephony.ims.action.SHOW_CAPABILITY_DISCOVERY_OPT_IN");
    }

    private void validate(Intent intent) {
        if (!doesIntentContainOptInAction(intent) || SUB_ID_NULL != intent.getIntExtra("android.provider.extra.SUB_ID", SUB_ID_NULL)) {
            return;
        }
        throw new IllegalArgumentException("Intent with action SHOW_CAPABILITY_DISCOVERY_OPT_IN must also include the extra Settings#EXTRA_SUB_ID");
    }

    String buildFragmentTag(int i) {
        return MOBILE_SETTINGS_TAG + i;
    }
}
