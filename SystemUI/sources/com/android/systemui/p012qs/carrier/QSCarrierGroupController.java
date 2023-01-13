package com.android.systemui.p012qs.carrier;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.keyguard.CarrierTextManager;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.function.Consumer;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController */
public class QSCarrierGroupController {
    private static final int SIM_SLOTS = 3;
    private static final String TAG = "QSCarrierGroup";
    private final ActivityStarter mActivityStarter;
    private final Handler mBgHandler;
    private final Callback mCallback;
    /* access modifiers changed from: private */
    public final CarrierConfigTracker mCarrierConfigTracker;
    private View[] mCarrierDividers;
    private QSCarrier[] mCarrierGroups;
    private final CarrierTextManager mCarrierTextManager;
    /* access modifiers changed from: private */
    public final CellSignalState[] mInfos;
    private boolean mIsSingleCarrier;
    /* access modifiers changed from: private */
    public int[] mLastSignalLevel;
    /* access modifiers changed from: private */
    public String[] mLastSignalLevelDescription;
    private boolean mListening;
    /* access modifiers changed from: private */
    public C2358H mMainHandler;
    private final NetworkController mNetworkController;
    private final TextView mNoSimTextView;
    private OnSingleCarrierChangedListener mOnSingleCarrierChangedListener;
    /* access modifiers changed from: private */
    public final boolean mProviderModel;
    private final SignalCallback mSignalCallback;
    private final SlotIndexResolver mSlotIndexResolver;

    @FunctionalInterface
    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$OnSingleCarrierChangedListener */
    public interface OnSingleCarrierChangedListener {
        void onSingleCarrierChanged(boolean z);
    }

    @FunctionalInterface
    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$SlotIndexResolver */
    public interface SlotIndexResolver {
        int getSlotIndex(int i);
    }

    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$Callback */
    private static class Callback implements CarrierTextManager.CarrierTextCallback {
        private C2358H mHandler;

        Callback(C2358H h) {
            this.mHandler = h;
        }

        public void updateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
            this.mHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
        }
    }

    private QSCarrierGroupController(QSCarrierGroup qSCarrierGroup, ActivityStarter activityStarter, @Background Handler handler, @Main Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
        QSCarrierGroup qSCarrierGroup2 = qSCarrierGroup;
        Context context2 = context;
        this.mInfos = new CellSignalState[3];
        this.mCarrierDividers = new View[2];
        this.mCarrierGroups = new QSCarrier[3];
        this.mLastSignalLevel = new int[3];
        this.mLastSignalLevelDescription = new String[3];
        this.mSignalCallback = new SignalCallback() {
            public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
                if (!QSCarrierGroupController.this.mProviderModel) {
                    int slotIndex = QSCarrierGroupController.this.getSlotIndex(mobileDataIndicators.subId);
                    if (slotIndex >= 3) {
                        Log.w(QSCarrierGroupController.TAG, "setMobileDataIndicators - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e(QSCarrierGroupController.TAG, "Invalid SIM slot index for subscription: " + mobileDataIndicators.subId);
                    } else {
                        QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(mobileDataIndicators.statusIcon.visible, mobileDataIndicators.statusIcon.icon, mobileDataIndicators.statusIcon.contentDescription, mobileDataIndicators.typeContentDescription.toString(), mobileDataIndicators.roaming, QSCarrierGroupController.this.mProviderModel);
                        QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                    }
                }
            }

            public void setCallIndicator(IconState iconState, int i) {
                if (QSCarrierGroupController.this.mProviderModel) {
                    int slotIndex = QSCarrierGroupController.this.getSlotIndex(i);
                    if (slotIndex >= 3) {
                        Log.w(QSCarrierGroupController.TAG, "setMobileDataIndicators - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e(QSCarrierGroupController.TAG, "Invalid SIM slot index for subscription: " + i);
                    } else {
                        boolean callStrengthConfig = QSCarrierGroupController.this.mCarrierConfigTracker.getCallStrengthConfig(i);
                        if (iconState.icon == C1894R.C1896drawable.ic_qs_no_calling_sms) {
                            if (iconState.visible) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, iconState.icon, iconState.contentDescription, "", false, QSCarrierGroupController.this.mProviderModel);
                            } else if (callStrengthConfig) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, QSCarrierGroupController.this.mLastSignalLevel[slotIndex], QSCarrierGroupController.this.mLastSignalLevelDescription[slotIndex], "", false, QSCarrierGroupController.this.mProviderModel);
                            } else {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, C1894R.C1896drawable.ic_qs_sim_card, "", "", false, QSCarrierGroupController.this.mProviderModel);
                            }
                            QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                            return;
                        }
                        QSCarrierGroupController.this.mLastSignalLevel[slotIndex] = iconState.icon;
                        QSCarrierGroupController.this.mLastSignalLevelDescription[slotIndex] = iconState.contentDescription;
                        if (QSCarrierGroupController.this.mInfos[slotIndex].mobileSignalIconId != C1894R.C1896drawable.ic_qs_no_calling_sms) {
                            if (callStrengthConfig) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, iconState.icon, iconState.contentDescription, "", false, QSCarrierGroupController.this.mProviderModel);
                            } else {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, C1894R.C1896drawable.ic_qs_sim_card, "", "", false, QSCarrierGroupController.this.mProviderModel);
                            }
                            QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                        }
                    }
                }
            }

            public void setNoSims(boolean z, boolean z2) {
                if (z) {
                    for (int i = 0; i < 3; i++) {
                        QSCarrierGroupController.this.mInfos[i] = QSCarrierGroupController.this.mInfos[i].changeVisibility(false);
                    }
                }
                QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
            }
        };
        if (featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            this.mProviderModel = true;
        } else {
            this.mProviderModel = false;
        }
        this.mActivityStarter = activityStarter;
        this.mBgHandler = handler;
        this.mNetworkController = networkController;
        this.mCarrierTextManager = builder.setShowAirplaneMode(false).setShowMissingSim(false).build();
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mSlotIndexResolver = slotIndexResolver;
        QSCarrierGroupController$$ExternalSyntheticLambda0 qSCarrierGroupController$$ExternalSyntheticLambda0 = new QSCarrierGroupController$$ExternalSyntheticLambda0(this);
        qSCarrierGroup2.setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda0);
        TextView noSimTextView = qSCarrierGroup.getNoSimTextView();
        this.mNoSimTextView = noSimTextView;
        noSimTextView.setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda0);
        this.mMainHandler = new C2358H(looper, new QSCarrierGroupController$$ExternalSyntheticLambda1(this), new QSCarrierGroupController$$ExternalSyntheticLambda2(this));
        this.mCallback = new Callback(this.mMainHandler);
        this.mCarrierGroups[0] = qSCarrierGroup.getCarrier1View();
        this.mCarrierGroups[1] = qSCarrierGroup.getCarrier2View();
        this.mCarrierGroups[2] = qSCarrierGroup.getCarrier3View();
        this.mCarrierDividers[0] = qSCarrierGroup.getCarrierDivider1();
        this.mCarrierDividers[1] = qSCarrierGroup.getCarrierDivider2();
        for (int i = 0; i < 3; i++) {
            this.mInfos[i] = new CellSignalState(true, C1894R.C1896drawable.ic_qs_no_calling_sms, context2.getText(AccessibilityContentDescriptions.NO_CALLING).toString(), "", false, this.mProviderModel);
            this.mLastSignalLevel[i] = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
            this.mLastSignalLevelDescription[i] = context2.getText(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[0]).toString();
            this.mCarrierGroups[i].setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda0);
        }
        this.mIsSingleCarrier = computeIsSingleCarrier();
        qSCarrierGroup2.setImportantForAccessibility(1);
        qSCarrierGroup2.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
            }

            public void onViewDetachedFromWindow(View view) {
                QSCarrierGroupController.this.setListening(false);
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-carrier-QSCarrierGroupController */
    public /* synthetic */ void mo36468xe730b13a(View view) {
        if (view.isVisibleToUser()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIRELESS_SETTINGS"), 0);
        }
    }

    /* access modifiers changed from: protected */
    public int getSlotIndex(int i) {
        return this.mSlotIndexResolver.getSlotIndex(i);
    }

    public void setOnSingleCarrierChangedListener(OnSingleCarrierChangedListener onSingleCarrierChangedListener) {
        this.mOnSingleCarrierChangedListener = onSingleCarrierChangedListener;
    }

    public boolean isSingleCarrier() {
        return this.mIsSingleCarrier;
    }

    private boolean computeIsSingleCarrier() {
        int i = 0;
        for (int i2 = 0; i2 < 3; i2++) {
            if (this.mInfos[i2].visible) {
                i++;
            }
        }
        return i == 1;
    }

    public void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            this.mBgHandler.post(new QSCarrierGroupController$$ExternalSyntheticLambda3(this));
        }
    }

    /* access modifiers changed from: private */
    public void updateListeners() {
        if (this.mListening) {
            if (this.mNetworkController.hasVoiceCallingFeature()) {
                this.mNetworkController.addCallback(this.mSignalCallback);
            }
            this.mCarrierTextManager.setListening(this.mCallback);
            return;
        }
        this.mNetworkController.removeCallback(this.mSignalCallback);
        this.mCarrierTextManager.setListening((CarrierTextManager.CarrierTextCallback) null);
    }

    /* access modifiers changed from: private */
    public void handleUpdateState() {
        if (!this.mMainHandler.getLooper().isCurrentThread()) {
            this.mMainHandler.obtainMessage(1).sendToTarget();
            return;
        }
        boolean computeIsSingleCarrier = computeIsSingleCarrier();
        int i = 0;
        if (computeIsSingleCarrier) {
            for (int i2 = 0; i2 < 3; i2++) {
                if (this.mInfos[i2].visible && this.mInfos[i2].mobileSignalIconId == C1894R.C1896drawable.ic_qs_sim_card) {
                    this.mInfos[i2] = new CellSignalState(true, C1894R.C1896drawable.ic_blank, "", "", false, this.mProviderModel);
                }
            }
        }
        for (int i3 = 0; i3 < 3; i3++) {
            this.mCarrierGroups[i3].updateState(this.mInfos[i3], computeIsSingleCarrier);
        }
        this.mCarrierDividers[0].setVisibility((!this.mInfos[0].visible || !this.mInfos[1].visible) ? 8 : 0);
        View view = this.mCarrierDividers[1];
        if ((!this.mInfos[1].visible || !this.mInfos[2].visible) && (!this.mInfos[0].visible || !this.mInfos[2].visible)) {
            i = 8;
        }
        view.setVisibility(i);
        if (this.mIsSingleCarrier != computeIsSingleCarrier) {
            this.mIsSingleCarrier = computeIsSingleCarrier;
            OnSingleCarrierChangedListener onSingleCarrierChangedListener = this.mOnSingleCarrierChangedListener;
            if (onSingleCarrierChangedListener != null) {
                onSingleCarrierChangedListener.onSingleCarrierChanged(computeIsSingleCarrier);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleUpdateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
        if (!this.mMainHandler.getLooper().isCurrentThread()) {
            this.mMainHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
            return;
        }
        this.mNoSimTextView.setVisibility(8);
        if (carrierTextCallbackInfo.airplaneMode || !carrierTextCallbackInfo.anySimReady) {
            for (int i = 0; i < 3; i++) {
                CellSignalState[] cellSignalStateArr = this.mInfos;
                cellSignalStateArr[i] = cellSignalStateArr[i].changeVisibility(false);
                this.mCarrierGroups[i].setCarrierText("");
                this.mCarrierGroups[i].setVisibility(8);
            }
            this.mNoSimTextView.setText(carrierTextCallbackInfo.carrierText);
            if (!TextUtils.isEmpty(carrierTextCallbackInfo.carrierText)) {
                this.mNoSimTextView.setVisibility(0);
            }
        } else {
            boolean[] zArr = new boolean[3];
            if (carrierTextCallbackInfo.listOfCarriers.length == carrierTextCallbackInfo.subscriptionIds.length) {
                int i2 = 0;
                while (i2 < 3 && i2 < carrierTextCallbackInfo.listOfCarriers.length) {
                    int slotIndex = getSlotIndex(carrierTextCallbackInfo.subscriptionIds[i2]);
                    if (slotIndex >= 3) {
                        Log.w(TAG, "updateInfoCarrier - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e(TAG, "Invalid SIM slot index for subscription: " + carrierTextCallbackInfo.subscriptionIds[i2]);
                    } else {
                        CellSignalState[] cellSignalStateArr2 = this.mInfos;
                        cellSignalStateArr2[slotIndex] = cellSignalStateArr2[slotIndex].changeVisibility(true);
                        zArr[slotIndex] = true;
                        this.mCarrierGroups[slotIndex].setCarrierText(carrierTextCallbackInfo.listOfCarriers[i2].toString().trim());
                        this.mCarrierGroups[slotIndex].setVisibility(0);
                    }
                    i2++;
                }
                for (int i3 = 0; i3 < 3; i3++) {
                    if (!zArr[i3]) {
                        CellSignalState[] cellSignalStateArr3 = this.mInfos;
                        cellSignalStateArr3[i3] = cellSignalStateArr3[i3].changeVisibility(false);
                        this.mCarrierGroups[i3].setVisibility(8);
                    }
                }
            } else {
                Log.e(TAG, "Carrier information arrays not of same length");
            }
        }
        handleUpdateState();
    }

    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$H */
    private static class C2358H extends Handler {
        static final int MSG_UPDATE_CARRIER_INFO = 0;
        static final int MSG_UPDATE_STATE = 1;
        private Consumer<CarrierTextManager.CarrierTextCallbackInfo> mUpdateCarrierInfo;
        private Runnable mUpdateState;

        C2358H(Looper looper, Consumer<CarrierTextManager.CarrierTextCallbackInfo> consumer, Runnable runnable) {
            super(looper);
            this.mUpdateCarrierInfo = consumer;
            this.mUpdateState = runnable;
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.mUpdateCarrierInfo.accept((CarrierTextManager.CarrierTextCallbackInfo) message.obj);
            } else if (i != 1) {
                super.handleMessage(message);
            } else {
                this.mUpdateState.run();
            }
        }
    }

    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$Builder */
    public static class Builder {
        private final ActivityStarter mActivityStarter;
        private final CarrierConfigTracker mCarrierConfigTracker;
        private final CarrierTextManager.Builder mCarrierTextControllerBuilder;
        private final Context mContext;
        private final FeatureFlags mFeatureFlags;
        private final Handler mHandler;
        private final Looper mLooper;
        private final NetworkController mNetworkController;
        private final SlotIndexResolver mSlotIndexResolver;
        private QSCarrierGroup mView;

        @Inject
        public Builder(ActivityStarter activityStarter, @Background Handler handler, @Main Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
            this.mActivityStarter = activityStarter;
            this.mHandler = handler;
            this.mLooper = looper;
            this.mNetworkController = networkController;
            this.mCarrierTextControllerBuilder = builder;
            this.mContext = context;
            this.mCarrierConfigTracker = carrierConfigTracker;
            this.mFeatureFlags = featureFlags;
            this.mSlotIndexResolver = slotIndexResolver;
        }

        public Builder setQSCarrierGroup(QSCarrierGroup qSCarrierGroup) {
            this.mView = qSCarrierGroup;
            return this;
        }

        public QSCarrierGroupController build() {
            return new QSCarrierGroupController(this.mView, this.mActivityStarter, this.mHandler, this.mLooper, this.mNetworkController, this.mCarrierTextControllerBuilder, this.mContext, this.mCarrierConfigTracker, this.mFeatureFlags, this.mSlotIndexResolver);
        }
    }

    @SysUISingleton
    /* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$SubscriptionManagerSlotIndexResolver */
    public static class SubscriptionManagerSlotIndexResolver implements SlotIndexResolver {
        public int getSlotIndex(int i) {
            return SubscriptionManager.getSlotIndex(i);
        }
    }
}
