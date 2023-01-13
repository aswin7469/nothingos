package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletCard;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import java.util.List;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.QuickAccessWalletTile */
public class QuickAccessWalletTile extends QSTileImpl<QSTile.State> {
    private static final String FEATURE_CHROME_OS = "org.chromium.arc";
    private static final String TAG = "QuickAccessWalletTile";
    private final WalletCardRetriever mCardRetriever = new WalletCardRetriever();
    Drawable mCardViewDrawable;
    private final QuickAccessWalletController mController;
    /* access modifiers changed from: private */
    public boolean mIsWalletUpdating = true;
    private final KeyguardStateController mKeyguardStateController;
    private final CharSequence mLabel = this.mContext.getString(C1894R.string.wallet_title);
    private final PackageManager mPackageManager;
    private final SecureSettings mSecureSettings;
    /* access modifiers changed from: private */
    public WalletCard mSelectedCard;

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    @Inject
    public QuickAccessWalletTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, KeyguardStateController keyguardStateController, PackageManager packageManager, SecureSettings secureSettings, QuickAccessWalletController quickAccessWalletController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mController = quickAccessWalletController;
        this.mKeyguardStateController = keyguardStateController;
        this.mPackageManager = packageManager;
        this.mSecureSettings = secureSettings;
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    /* access modifiers changed from: protected */
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (z) {
            this.mController.setupWalletChangeObservers(this.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
            if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
                Log.i(TAG, "QAW service is unavailable, recreating the wallet client.");
                this.mController.reCreateWalletClient();
            }
            this.mController.queryWalletCards(this.mCardRetriever);
        }
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mUiHandler.post(new QuickAccessWalletTile$$ExternalSyntheticLambda0(this, controller));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$0$com-android-systemui-qs-tiles-QuickAccessWalletTile */
    public /* synthetic */ void mo36904xdf3da830(ActivityLaunchAnimator.Controller controller) {
        this.mController.startQuickAccessUiIntent(this.mActivityStarter, controller, this.mSelectedCard != null);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.State state, Object obj) {
        QSTile.Icon icon;
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        if (serviceLabel == null) {
            serviceLabel = this.mContext.getString(C1894R.string.wallet_title);
        }
        state.label = serviceLabel;
        state.contentDescription = state.label;
        Drawable tileIcon = this.mController.getWalletClient().getTileIcon();
        if (tileIcon == null) {
            icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_wallet_lockscreen);
        } else {
            icon = new QSTileImpl.DrawableIcon(tileIcon);
        }
        state.icon = icon;
        int i = 1;
        boolean z = !this.mKeyguardStateController.isUnlocked();
        if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
            state.state = 0;
            state.secondaryLabel = null;
            state.sideViewCustomDrawable = null;
            return;
        }
        if (this.mSelectedCard != null) {
            if (!z) {
                i = 2;
            }
            state.state = i;
            state.secondaryLabel = this.mSelectedCard.getContentDescription();
            state.sideViewCustomDrawable = this.mCardViewDrawable;
        } else {
            state.state = 1;
            state.secondaryLabel = this.mContext.getString(this.mIsWalletUpdating ? C1894R.string.wallet_secondary_label_updating : C1894R.string.wallet_secondary_label_no_card);
            state.sideViewCustomDrawable = null;
        }
        state.stateDescription = state.secondaryLabel;
    }

    public boolean isAvailable() {
        return this.mPackageManager.hasSystemFeature("android.hardware.nfc.hce") && !this.mPackageManager.hasSystemFeature(FEATURE_CHROME_OS) && this.mSecureSettings.getStringForUser("nfc_payment_default_component", -2) != null;
    }

    public CharSequence getTileLabel() {
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        return serviceLabel == null ? this.mContext.getString(C1894R.string.wallet_title) : serviceLabel;
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
        this.mController.unregisterWalletChangeObservers(QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
    }

    /* renamed from: com.android.systemui.qs.tiles.QuickAccessWalletTile$WalletCardRetriever */
    private class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        private WalletCardRetriever() {
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            Log.i(QuickAccessWalletTile.TAG, "Successfully retrieved wallet cards.");
            boolean unused = QuickAccessWalletTile.this.mIsWalletUpdating = false;
            List walletCards = getWalletCardsResponse.getWalletCards();
            if (walletCards.isEmpty()) {
                Log.d(QuickAccessWalletTile.TAG, "No wallet cards exist.");
                QuickAccessWalletTile.this.mCardViewDrawable = null;
                WalletCard unused2 = QuickAccessWalletTile.this.mSelectedCard = null;
                QuickAccessWalletTile.this.refreshState();
                return;
            }
            int selectedIndex = getWalletCardsResponse.getSelectedIndex();
            if (selectedIndex >= walletCards.size()) {
                Log.w(QuickAccessWalletTile.TAG, "Error retrieving cards: Invalid selected card index.");
                WalletCard unused3 = QuickAccessWalletTile.this.mSelectedCard = null;
                QuickAccessWalletTile.this.mCardViewDrawable = null;
                return;
            }
            WalletCard unused4 = QuickAccessWalletTile.this.mSelectedCard = (WalletCard) walletCards.get(selectedIndex);
            QuickAccessWalletTile quickAccessWalletTile = QuickAccessWalletTile.this;
            quickAccessWalletTile.mCardViewDrawable = quickAccessWalletTile.mSelectedCard.getCardImage().loadDrawable(QuickAccessWalletTile.this.mContext);
            QuickAccessWalletTile.this.refreshState();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            boolean unused = QuickAccessWalletTile.this.mIsWalletUpdating = false;
            QuickAccessWalletTile.this.mCardViewDrawable = null;
            WalletCard unused2 = QuickAccessWalletTile.this.mSelectedCard = null;
            QuickAccessWalletTile.this.refreshState();
        }
    }
}
