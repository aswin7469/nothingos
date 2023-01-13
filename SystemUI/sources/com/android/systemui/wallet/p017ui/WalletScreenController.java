package com.android.systemui.wallet.p017ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.SelectWalletCardRequest;
import android.service.quickaccesswallet.WalletCard;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1894R;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wallet.p017ui.WalletCardCarousel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/* renamed from: com.android.systemui.wallet.ui.WalletScreenController */
public class WalletScreenController implements WalletCardCarousel.OnSelectionListener, QuickAccessWalletClient.OnWalletCardsRetrievedCallback, KeyguardStateController.Callback {
    private static final int MAX_CARDS = 10;
    private static final String PREFS_WALLET_VIEW_HEIGHT = "wallet_view_height";
    private static final long SELECTION_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(30);
    private static final String TAG = "WalletScreenCtrl";
    private final ActivityStarter mActivityStarter;
    private final WalletCardCarousel mCardCarousel;
    private Context mContext;
    private final Executor mExecutor;
    private final FalsingManager mFalsingManager;
    private final Handler mHandler;
    boolean mIsDismissed;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final SharedPreferences mPrefs;
    String mSelectedCardId;
    private final Runnable mSelectionRunnable = new WalletScreenController$$ExternalSyntheticLambda2(this);
    private final UiEventLogger mUiEventLogger;
    private final QuickAccessWalletClient mWalletClient;
    /* access modifiers changed from: private */
    public final WalletView mWalletView;

    public WalletScreenController(Context context, WalletView walletView, QuickAccessWalletClient quickAccessWalletClient, ActivityStarter activityStarter, Executor executor, Handler handler, UserTracker userTracker, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mWalletClient = quickAccessWalletClient;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        this.mPrefs = userTracker.getUserContext().getSharedPreferences(TAG, 0);
        this.mWalletView = walletView;
        walletView.setMinimumHeight(getExpectedMinHeight());
        walletView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        WalletCardCarousel cardCarousel = walletView.getCardCarousel();
        this.mCardCarousel = cardCarousel;
        if (cardCarousel != null) {
            cardCarousel.setSelectionListener(this);
        }
    }

    public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
        if (!this.mIsDismissed) {
            Log.i(TAG, "Successfully retrieved wallet cards.");
            List<WalletCard> walletCards = getWalletCardsResponse.getWalletCards();
            ArrayList arrayList = new ArrayList(walletCards.size());
            for (WalletCard qAWalletCardViewInfo : walletCards) {
                arrayList.add(new QAWalletCardViewInfo(this.mContext, qAWalletCardViewInfo));
            }
            this.mHandler.post(new WalletScreenController$$ExternalSyntheticLambda0(this, arrayList, getWalletCardsResponse));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onWalletCardsRetrieved$0$com-android-systemui-wallet-ui-WalletScreenController */
    public /* synthetic */ void mo47475xe1b09808(List list, GetWalletCardsResponse getWalletCardsResponse) {
        if (!this.mIsDismissed) {
            if (list.isEmpty()) {
                showEmptyStateView();
            } else {
                int selectedIndex = getWalletCardsResponse.getSelectedIndex();
                if (selectedIndex >= list.size()) {
                    Log.w(TAG, "Invalid selected card index, showing empty state.");
                    showEmptyStateView();
                } else {
                    this.mWalletView.showCardCarousel(list, selectedIndex, true ^ this.mKeyguardStateController.isUnlocked(), this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning());
                }
            }
            this.mUiEventLogger.log(WalletUiEvent.QAW_IMPRESSION);
            removeMinHeightAndRecordHeightOnLayout();
        }
    }

    public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
        this.mHandler.post(new WalletScreenController$$ExternalSyntheticLambda1(this, getWalletCardsError));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onWalletCardRetrievalError$1$com-android-systemui-wallet-ui-WalletScreenController */
    public /* synthetic */ void mo47474xfa1098a8(GetWalletCardsError getWalletCardsError) {
        if (!this.mIsDismissed) {
            this.mWalletView.showErrorMessage(getWalletCardsError.getMessage());
        }
    }

    public void onKeyguardFadingAwayChanged() {
        queryWalletCards();
    }

    public void onUnlockedChanged() {
        queryWalletCards();
    }

    public void onUncenteredClick(int i) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mCardCarousel.smoothScrollToPosition(i);
        }
    }

    public void onCardSelected(WalletCardViewInfo walletCardViewInfo) {
        if (!this.mIsDismissed) {
            String str = this.mSelectedCardId;
            if (str != null && !str.equals(walletCardViewInfo.getCardId())) {
                this.mUiEventLogger.log(WalletUiEvent.QAW_CHANGE_CARD);
            }
            this.mSelectedCardId = walletCardViewInfo.getCardId();
            selectCard();
        }
    }

    /* access modifiers changed from: private */
    public void selectCard() {
        this.mHandler.removeCallbacks(this.mSelectionRunnable);
        String str = this.mSelectedCardId;
        if (!this.mIsDismissed && str != null) {
            this.mWalletClient.selectWalletCard(new SelectWalletCardRequest(str));
            this.mHandler.postDelayed(this.mSelectionRunnable, SELECTION_DELAY_MILLIS);
        }
    }

    public void onCardClicked(WalletCardViewInfo walletCardViewInfo) {
        if (!this.mFalsingManager.isFalseTap(1) && (walletCardViewInfo instanceof QAWalletCardViewInfo)) {
            QAWalletCardViewInfo qAWalletCardViewInfo = (QAWalletCardViewInfo) walletCardViewInfo;
            if (qAWalletCardViewInfo.mWalletCard != null && qAWalletCardViewInfo.mWalletCard.getPendingIntent() != null) {
                if (!this.mKeyguardStateController.isUnlocked()) {
                    this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_CARD_CLICK);
                }
                this.mUiEventLogger.log(WalletUiEvent.QAW_CLICK_CARD);
                this.mActivityStarter.startPendingIntentDismissingKeyguard(walletCardViewInfo.getPendingIntent());
            }
        }
    }

    public void queryWalletCards() {
        if (!this.mIsDismissed) {
            int cardWidthPx = this.mCardCarousel.getCardWidthPx();
            int cardHeightPx = this.mCardCarousel.getCardHeightPx();
            if (cardWidthPx != 0 && cardHeightPx != 0) {
                this.mWalletView.show();
                this.mWalletView.hideErrorMessage();
                this.mWalletClient.getWalletCards(this.mExecutor, new GetWalletCardsRequest(cardWidthPx, cardHeightPx, this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.wallet_screen_header_icon_size), 10), this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onDismissed() {
        if (!this.mIsDismissed) {
            this.mIsDismissed = true;
            this.mSelectedCardId = null;
            this.mHandler.removeCallbacks(this.mSelectionRunnable);
            this.mWalletClient.notifyWalletDismissed();
            this.mWalletView.animateDismissal();
            this.mContext = null;
        }
    }

    private void showEmptyStateView() {
        Drawable logo = this.mWalletClient.getLogo();
        CharSequence serviceLabel = this.mWalletClient.getServiceLabel();
        CharSequence shortcutLongLabel = this.mWalletClient.getShortcutLongLabel();
        Intent createWalletIntent = this.mWalletClient.createWalletIntent();
        if (logo == null || TextUtils.isEmpty(serviceLabel) || TextUtils.isEmpty(shortcutLongLabel) || createWalletIntent == null) {
            Log.w(TAG, "QuickAccessWalletService manifest entry mis-configured");
            this.mWalletView.hide();
            this.mPrefs.edit().putInt(PREFS_WALLET_VIEW_HEIGHT, 0).apply();
            return;
        }
        this.mWalletView.showEmptyStateView(logo, serviceLabel, shortcutLongLabel, new WalletScreenController$$ExternalSyntheticLambda3(this, createWalletIntent));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showEmptyStateView$2$com-android-systemui-wallet-ui-WalletScreenController */
    public /* synthetic */ void mo47476xc1231213(Intent intent, View view) {
        this.mActivityStarter.startActivity(intent, true);
    }

    private int getExpectedMinHeight() {
        int i = this.mPrefs.getInt(PREFS_WALLET_VIEW_HEIGHT, -1);
        return i == -1 ? this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.min_wallet_empty_height) : i;
    }

    private void removeMinHeightAndRecordHeightOnLayout() {
        this.mWalletView.setMinimumHeight(0);
        this.mWalletView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                WalletScreenController.this.mWalletView.removeOnLayoutChangeListener(this);
                WalletScreenController.this.mPrefs.edit().putInt(WalletScreenController.PREFS_WALLET_VIEW_HEIGHT, i4 - i2).apply();
            }
        });
    }

    /* renamed from: com.android.systemui.wallet.ui.WalletScreenController$QAWalletCardViewInfo */
    static class QAWalletCardViewInfo implements WalletCardViewInfo {
        private final Drawable mCardDrawable;
        private final Drawable mIconDrawable;
        /* access modifiers changed from: private */
        public final WalletCard mWalletCard;

        QAWalletCardViewInfo(Context context, WalletCard walletCard) {
            Drawable drawable;
            this.mWalletCard = walletCard;
            this.mCardDrawable = walletCard.getCardImage().loadDrawable(context);
            Icon cardIcon = walletCard.getCardIcon();
            if (cardIcon == null) {
                drawable = null;
            } else {
                drawable = cardIcon.loadDrawable(context);
            }
            this.mIconDrawable = drawable;
        }

        public String getCardId() {
            return this.mWalletCard.getCardId();
        }

        public Drawable getCardDrawable() {
            return this.mCardDrawable;
        }

        public CharSequence getContentDescription() {
            return this.mWalletCard.getContentDescription();
        }

        public Drawable getIcon() {
            return this.mIconDrawable;
        }

        public CharSequence getLabel() {
            CharSequence cardLabel = this.mWalletCard.getCardLabel();
            return cardLabel == null ? "" : cardLabel;
        }

        public PendingIntent getPendingIntent() {
            return this.mWalletCard.getPendingIntent();
        }
    }
}
