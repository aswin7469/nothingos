package com.android.systemui.wallet.p017ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Bundle;
import android.os.Handler;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletServiceEvent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
import javax.inject.Inject;

/* renamed from: com.android.systemui.wallet.ui.WalletActivity */
public class WalletActivity extends LifecycleActivity implements QuickAccessWalletClient.WalletServiceEventListener {
    private static final String TAG = "WalletActivity";
    private final ActivityStarter mActivityStarter;
    private final Executor mExecutor;
    private FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final Handler mHandler;
    private boolean mHasRegisteredListener;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final UiEventLogger mUiEventLogger;
    private final UserTracker mUserTracker;
    private QuickAccessWalletClient mWalletClient;
    /* access modifiers changed from: private */
    public WalletScreenController mWalletScreenController;

    static /* synthetic */ boolean lambda$onCreate$2() {
        return false;
    }

    @Inject
    public WalletActivity(KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, ActivityStarter activityStarter, @Background Executor executor, @Main Handler handler, FalsingManager falsingManager, FalsingCollector falsingCollector, UserTracker userTracker, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarKeyguardViewManager statusBarKeyguardViewManager, UiEventLogger uiEventLogger) {
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mUserTracker = userTracker;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mUiEventLogger = uiEventLogger;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(Integer.MIN_VALUE);
        requestWindowFeature(1);
        setContentView(C1894R.layout.quick_access_wallet);
        Toolbar toolbar = (Toolbar) findViewById(C1894R.C1898id.action_bar);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(getHomeIndicatorDrawable());
        getActionBar().setHomeActionContentDescription(C1894R.string.accessibility_desc_close);
        WalletView walletView = (WalletView) requireViewById(C1894R.C1898id.wallet_view);
        this.mWalletClient = QuickAccessWalletClient.create(this, this.mExecutor);
        this.mWalletScreenController = new WalletScreenController(this, walletView, this.mWalletClient, this.mActivityStarter, this.mExecutor, this.mHandler, this.mUserTracker, this.mFalsingManager, this.mKeyguardUpdateMonitor, this.mKeyguardStateController, this.mUiEventLogger);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
                Log.d(WalletActivity.TAG, "Biometric running state has changed.");
                WalletActivity.this.mWalletScreenController.queryWalletCards();
            }
        };
        walletView.setFalsingCollector(this.mFalsingCollector);
        walletView.setShowWalletAppOnClickListener(new WalletActivity$$ExternalSyntheticLambda2(this));
        walletView.setDeviceLockedActionOnClickListener(new WalletActivity$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-android-systemui-wallet-ui-WalletActivity  reason: not valid java name */
    public /* synthetic */ void m3326lambda$onCreate$1$comandroidsystemuiwalletuiWalletActivity(View view) {
        if (this.mWalletClient.createWalletIntent() == null) {
            Log.w(TAG, "Unable to create wallet app intent.");
        } else if (!this.mFalsingManager.isFalseTap(1)) {
            if (this.mKeyguardStateController.isUnlocked()) {
                this.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
                this.mActivityStarter.startActivity(this.mWalletClient.createWalletIntent(), true);
                finish();
                return;
            }
            this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_SHOW_ALL_BUTTON);
            this.mKeyguardDismissUtil.executeWhenUnlocked(new WalletActivity$$ExternalSyntheticLambda1(this), false, true);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-wallet-ui-WalletActivity  reason: not valid java name */
    public /* synthetic */ boolean m3325lambda$onCreate$0$comandroidsystemuiwalletuiWalletActivity() {
        this.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
        this.mActivityStarter.startActivity(this.mWalletClient.createWalletIntent(), true);
        finish();
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$com-android-systemui-wallet-ui-WalletActivity  reason: not valid java name */
    public /* synthetic */ void m3327lambda$onCreate$3$comandroidsystemuiwalletuiWalletActivity(View view) {
        Log.d(TAG, "Wallet action button is clicked.");
        if (this.mFalsingManager.isFalseTap(1)) {
            Log.d(TAG, "False tap detected on wallet action button.");
            return;
        }
        this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_UNLOCK_BUTTON);
        this.mKeyguardDismissUtil.executeWhenUnlocked(new WalletActivity$$ExternalSyntheticLambda0(), false, false);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (!this.mHasRegisteredListener) {
            this.mWalletClient.addWalletServiceEventListener(this);
            this.mHasRegisteredListener = true;
        }
        this.mKeyguardStateController.addCallback(this.mWalletScreenController);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mWalletScreenController.queryWalletCards();
        this.mKeyguardViewManager.requestFp(true, Utils.getColorAttrDefaultColor(this, 17956900));
        this.mKeyguardViewManager.requestFace(true);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mKeyguardViewManager.requestFp(false, -1);
        this.mKeyguardViewManager.requestFace(false);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C1894R.C1900menu.wallet_activity_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onWalletServiceEvent(WalletServiceEvent walletServiceEvent) {
        int eventType = walletServiceEvent.getEventType();
        if (eventType == 1) {
            return;
        }
        if (eventType != 2) {
            Log.w(TAG, "onWalletServiceEvent: Unknown event type");
        } else {
            this.mWalletScreenController.queryWalletCards();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId != C1894R.C1898id.wallet_lockscreen_settings) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            this.mActivityStarter.startActivity(new Intent("android.settings.LOCK_SCREEN_SETTINGS").addFlags(335544320), true);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mKeyguardStateController.removeCallback(this.mWalletScreenController);
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mKeyguardUpdateMonitorCallback;
        if (keyguardUpdateMonitorCallback != null) {
            this.mKeyguardUpdateMonitor.removeCallback(keyguardUpdateMonitorCallback);
        }
        this.mWalletScreenController.onDismissed();
        this.mWalletClient.removeWalletServiceEventListener(this);
        this.mHasRegisteredListener = false;
        super.onDestroy();
    }

    private Drawable getHomeIndicatorDrawable() {
        Drawable drawable = getDrawable(C1894R.C1896drawable.ic_close);
        drawable.setTint(getColor(C1894R.C1895color.material_dynamic_neutral70));
        return drawable;
    }
}
