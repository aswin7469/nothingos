package com.nothing.systemui.statusbar.connectivity;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.feature.MmTelFeature;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.settingslib.SignalIcon;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.MobileState;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.nothing.systemui.mobile.TelephonyIconsEx;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.utils.NTImageUtil;
import java.util.Objects;

public class MobileSignalControllerEx {
    private static final int CARRIER_BOUYGUES = 1487;
    static int CIDJIO = 2018;
    static final String TAG = "MobileSignalControllerEx";
    public static final int TYPE_5G_NR_CA = 3;
    private final Rect mClipOutRectLeftTop = new Rect();
    private final Rect mClipOutRectRightBottom = new Rect();
    private Context mContext;
    private int[] mImsRegistrationTech = {-1, -1};
    private boolean[] mIsVoiceOverCellularImsEnabled = {false, false};
    private boolean[] mIsVowifiEnabled = {false, false};
    private boolean[] mLastIsVoiceOverCellularImsEnabled = {false, false};
    private boolean[] mLastIsVowifiEnabled = {false, false};
    private MmTelFeature.MmTelCapabilities[] mMmTelCapabilities = {null, null};
    private SubscriptionManager mSubscriptionManager;
    String mTag;

    public void init(String str, Context context) {
        this.mTag = TAG + str;
        this.mContext = context;
        if (this.mSubscriptionManager == null) {
            this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        }
    }

    public static boolean shouldDisplayDataType(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            return false;
        }
        return Objects.equals(Integer.valueOf(subscriptionInfo.getCarrierId()), Integer.valueOf((int) CARRIER_BOUYGUES));
    }

    public MobileMappings.Config customConfig(MobileMappings.Config config) {
        config.showVolteIcon = true;
        config.showVowifiIcon = true;
        Log.d(this.mTag, "customConfig showVolteIcon=" + config.showVolteIcon + " showVowifiIcon=" + config.showVowifiIcon);
        return config;
    }

    private void refreshVoiceOverCellularImsEnabled(MmTelFeature.MmTelCapabilities mmTelCapabilities, int i) {
        if (i < this.mIsVoiceOverCellularImsEnabled.length) {
            boolean z = true;
            boolean isImsCapabilityInCacheAvailable = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 0, i);
            boolean isImsCapabilityInCacheAvailable2 = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 3, i);
            boolean[] zArr = this.mIsVoiceOverCellularImsEnabled;
            if (!isImsCapabilityInCacheAvailable && !isImsCapabilityInCacheAvailable2) {
                z = false;
            }
            zArr[i] = z;
            NTLogUtil.m1686d(this.mTag, "refreshVoiceOverCellularImsEnabled phoneId" + i + " isVoiceAndLte=" + isImsCapabilityInCacheAvailable + " isVoiceAndNr=" + isImsCapabilityInCacheAvailable2);
        }
    }

    public void updateMmTelCapabilities(MmTelFeature.MmTelCapabilities mmTelCapabilities, int i) {
        MmTelFeature.MmTelCapabilities[] mmTelCapabilitiesArr = this.mMmTelCapabilities;
        if (i < mmTelCapabilitiesArr.length) {
            mmTelCapabilitiesArr[i] = mmTelCapabilities;
            refreshVoiceOverCellularImsEnabled(mmTelCapabilities, i);
            refreshVowifiEnabled(this.mMmTelCapabilities[i], i);
        }
    }

    public void updateImsRegistrationTech(int i, int i2) {
        int[] iArr = this.mImsRegistrationTech;
        if (i2 < iArr.length) {
            iArr[i2] = i;
            refreshVoiceOverCellularImsEnabled(this.mMmTelCapabilities[i2], i2);
            refreshVowifiEnabled(this.mMmTelCapabilities[i2], i2);
        }
    }

    private void refreshVowifiEnabled(MmTelFeature.MmTelCapabilities mmTelCapabilities, int i) {
        if (i < this.mIsVowifiEnabled.length) {
            boolean z = true;
            boolean isImsCapabilityInCacheAvailable = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 1, i);
            boolean isImsCapabilityInCacheAvailable2 = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 2, i);
            boolean[] zArr = this.mIsVowifiEnabled;
            if (!isImsCapabilityInCacheAvailable && !isImsCapabilityInCacheAvailable2) {
                z = false;
            }
            zArr[i] = z;
            Log.d(this.mTag, "refreshVowifiEnabled:" + i + " isVoiceAndIwlan=" + isImsCapabilityInCacheAvailable + " isVoiceAndCrossSim=" + isImsCapabilityInCacheAvailable2);
        }
    }

    public boolean isVowifiEnabled(int i) {
        boolean[] zArr = this.mIsVowifiEnabled;
        if (i < zArr.length) {
            return zArr[i];
        }
        return false;
    }

    public boolean isVoiceOverCellularImsEnabled(int i) {
        if (i < this.mIsVowifiEnabled.length) {
            return this.mIsVoiceOverCellularImsEnabled[i];
        }
        return false;
    }

    private boolean isImsCapabilityInCacheAvailable(MmTelFeature.MmTelCapabilities mmTelCapabilities, int i, int i2, int i3) {
        int[] iArr = this.mImsRegistrationTech;
        if (i3 < iArr.length && iArr[i3] == i2 && mmTelCapabilities != null && mmTelCapabilities.isCapable(i)) {
            return true;
        }
        return false;
    }

    public void saveExtraLastState() {
        this.mLastIsVoiceOverCellularImsEnabled = this.mIsVoiceOverCellularImsEnabled;
        this.mLastIsVowifiEnabled = this.mIsVowifiEnabled;
    }

    public boolean checkExtraDirty() {
        boolean z = (this.mIsVoiceOverCellularImsEnabled == this.mLastIsVoiceOverCellularImsEnabled && this.mIsVowifiEnabled == this.mLastIsVowifiEnabled) ? false : true;
        if (z) {
            Log.d(this.mTag, "extraDirty:; isVoiceOverCellularImsEnabled:" + this.mIsVoiceOverCellularImsEnabled + ", last:" + this.mLastIsVoiceOverCellularImsEnabled + "; isVowifiEnabled:" + this.mIsVowifiEnabled + ", last:" + this.mLastIsVowifiEnabled);
        }
        return z;
    }

    public void initSidePadding(SignalDrawable signalDrawable, View view, View view2) {
        if (signalDrawable == null || view == null || view2 == null) {
            Log.e(this.mTag, "icons is null");
            return;
        }
        int intrinsicWidth = (int) (((float) signalDrawable.getIntrinsicWidth()) / 12.0f);
        int i = intrinsicWidth >> 1;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (!(marginLayoutParams.getMarginStart() == intrinsicWidth && marginLayoutParams.topMargin == intrinsicWidth)) {
            marginLayoutParams.setMarginStart(intrinsicWidth);
            marginLayoutParams.topMargin = intrinsicWidth;
            marginLayoutParams.setMarginEnd(i);
            marginLayoutParams.bottomMargin = i;
            view.resolveLayoutParams();
        }
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
        if (marginLayoutParams2.getMarginStart() != intrinsicWidth || marginLayoutParams2.topMargin != i) {
            Log.e(this.mTag, "initSidePadding:" + intrinsicWidth + ":" + i);
            marginLayoutParams2.setMarginStart(intrinsicWidth);
            marginLayoutParams2.topMargin = i;
            marginLayoutParams2.setMarginEnd(intrinsicWidth);
            marginLayoutParams2.bottomMargin = intrinsicWidth;
            view2.resolveLayoutParams();
        }
    }

    public Drawable checkClipOutRect(Context context, SignalDrawable signalDrawable, ImageView imageView, View view, View view2) {
        if (context == null || signalDrawable == null || imageView == null || view == null || view2 == null) {
            return null;
        }
        if (view.getVisibility() == 0) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            this.mClipOutRectLeftTop.set(0, 0, marginLayoutParams.getMarginStart() + marginLayoutParams.getMarginEnd() + view.getMeasuredWidth(), marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + view.getMeasuredHeight());
        } else {
            this.mClipOutRectLeftTop.setEmpty();
        }
        if (view2.getVisibility() == 0) {
            int measuredWidth = imageView.getMeasuredWidth();
            int measuredHeight = imageView.getMeasuredHeight();
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
            this.mClipOutRectRightBottom.set(measuredWidth - ((marginLayoutParams2.getMarginStart() + marginLayoutParams2.getMarginEnd()) + view2.getMeasuredWidth()), measuredHeight - ((marginLayoutParams2.topMargin + marginLayoutParams2.bottomMargin) + view2.getMeasuredHeight()), measuredWidth, measuredHeight);
        } else {
            this.mClipOutRectRightBottom.setEmpty();
        }
        return NTImageUtil.setClipOutRect(context, signalDrawable, this.mClipOutRectLeftTop, this.mClipOutRectRightBottom, imageView.isLayoutRtl());
    }

    public void logCurrentState(MobileState mobileState, SignalIcon.MobileIconGroup mobileIconGroup, MobileMappings.Config config, MobileDataIndicators mobileDataIndicators) {
        NTLogUtil.m1688i(this.mTag, "notifyListeners mobileState=" + mobileState.toString() + " dataDisabled=" + mobileState.isDataDisabledOrNotDefault() + " Icons=" + mobileIconGroup + " icons.dataType=" + mobileIconGroup.dataType + " mConfig.alwaysShowDataRatIcon=" + config.alwaysShowDataRatIcon + " mobileDataIndicators=" + mobileDataIndicators);
    }

    public void addOnSubscriptionsChangedListener(SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener) {
        if (this.mSubscriptionManager != null) {
            NTLogUtil.m1688i(this.mTag, "addOnSubscriptionsChangedListener");
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mContext.getMainExecutor(), onSubscriptionsChangedListener);
        }
    }

    public void removeOnSubscriptionsChangedListener(MySubscriptionsChangedListener mySubscriptionsChangedListener) {
        if (this.mSubscriptionManager != null) {
            NTLogUtil.m1688i(this.mTag, "removeOnSubscriptionsChangedListener");
            mySubscriptionsChangedListener.removeRunnable();
            this.mSubscriptionManager.removeOnSubscriptionsChangedListener(mySubscriptionsChangedListener);
        }
    }

    public static class MySubscriptionsChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        Context mContext;
        Handler mHandler;
        Runnable mImsRegisterRunnable;
        int mPhonId;
        SubscriptionInfo mSubscriptionInfo;

        public MySubscriptionsChangedListener(int i, SubscriptionInfo subscriptionInfo, Runnable runnable, Context context, Handler handler) {
            this.mPhonId = i;
            this.mSubscriptionInfo = subscriptionInfo;
            this.mImsRegisterRunnable = runnable;
            this.mContext = context;
            this.mHandler = handler;
        }

        public void onSubscriptionsChanged() {
            boolean isSubIdChange = MobileSignalControllerEx.isSubIdChange(this.mPhonId, this.mSubscriptionInfo, this.mContext);
            NTLogUtil.m1688i("MySubscriptionsChangedListener", "onSubscriptionsChanged=" + this.mPhonId + ":" + isSubIdChange);
            if (isSubIdChange) {
                this.mHandler.postDelayed(this.mImsRegisterRunnable, 500);
            }
        }

        public void removeRunnable() {
            this.mHandler.removeCallbacks(this.mImsRegisterRunnable);
        }
    }

    public static boolean isSubIdChange(int i, SubscriptionInfo subscriptionInfo, Context context) {
        int i2 = -1;
        int subscriptionId = subscriptionInfo != null ? subscriptionInfo.getSubscriptionId() : -1;
        SubscriptionInfo phoneSubscriptionInfo = getPhoneSubscriptionInfo(i, context);
        if (phoneSubscriptionInfo != null) {
            i2 = phoneSubscriptionInfo.getSubscriptionId();
        }
        NTLogUtil.m1688i(TAG, "isSubIdChange=" + subscriptionId + ":" + i2);
        return i2 != subscriptionId;
    }

    public static SubscriptionInfo getPhoneSubscriptionInfo(int i, Context context) {
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(i);
        NTLogUtil.m1688i(TAG, "getPhoneSubscriptionInfo:" + activeSubscriptionInfoForSimSlotIndex);
        return activeSubscriptionInfoForSimSlotIndex;
    }

    public SignalIcon.MobileIconGroup getMobileIconGroup(SignalIcon.MobileIconGroup mobileIconGroup, SubscriptionInfo subscriptionInfo, TelephonyManager telephonyManager, int i, ServiceState serviceState, FiveGServiceClient.FiveGServiceState fiveGServiceState) {
        if (!isJIOSim(telephonyManager, i, subscriptionInfo)) {
            return mobileIconGroup;
        }
        if (!(serviceState != null && serviceState.getDataNetworkType() == 20)) {
            return mobileIconGroup;
        }
        boolean isFiveGNr = isFiveGNr(serviceState);
        boolean isFiveGNrCA = isFiveGNrCA(fiveGServiceState);
        NTLogUtil.m1688i(getTag(i), "updateTelephony show5G_plus:" + isFiveGNr + " show5G_plus_plus:" + isFiveGNrCA);
        if (isFiveGNrCA) {
            return TelephonyIconsEx.NR_5G_PLUS_PLUS;
        }
        if (isFiveGNr) {
            return TelephonyIconsEx.NR_5G_PLUS;
        }
        return TelephonyIcons.FIVE_G_BASIC;
    }

    private boolean isFiveGNr(ServiceState serviceState) {
        return serviceState.getDataNetworkType() == 20;
    }

    private boolean isFiveGNrCA(FiveGServiceClient.FiveGServiceState fiveGServiceState) {
        return fiveGServiceState != null && fiveGServiceState.isNrIconTypeValid() && fiveGServiceState.getNrIconType() == 3;
    }

    private String getMccMnc(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo != null) {
            return subscriptionInfo.getMccString() + subscriptionInfo.getMncString();
        }
        return null;
    }

    private boolean isJIOSim(TelephonyManager telephonyManager, int i, SubscriptionInfo subscriptionInfo) {
        String mccMnc = getMccMnc(subscriptionInfo);
        int carrierIdFromSimMccMnc = telephonyManager.getCarrierIdFromSimMccMnc();
        NTLogUtil.m1688i(getTag(i), "isJioSim cid:" + carrierIdFromSimMccMnc + "mccmnc:" + mccMnc);
        return carrierIdFromSimMccMnc == CIDJIO;
    }

    private String getTag(int i) {
        return "MobileSignalControllerEx(" + i + NavigationBarInflaterView.KEY_CODE_END;
    }
}
