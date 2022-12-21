package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.util.ArraySet;
import android.util.Log;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class StatusBarSignalPolicy implements SignalCallback, SecurityController.SecurityControllerCallback, TunerService.Tunable {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "StatusBarSignalPolicy";
    private boolean mActivityEnabled;
    private ArrayList<CallIndicatorIconState> mCallIndicatorStates = new ArrayList<>();
    private final CarrierConfigTracker mCarrierConfigTracker;
    private final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler = Handler.getMain();
    private boolean mHideAirplane;
    private boolean mHideEthernet;
    private boolean mHideMobile;
    private boolean mHideWifi;
    private final StatusBarIconController mIconController;
    private boolean mInitialized;
    private boolean mIsAirplaneMode = false;
    private boolean mIsWifiEnabled = false;
    private ArrayList<MobileIconState> mMobileStates = new ArrayList<>();
    private final NetworkController mNetworkController;
    private final SecurityController mSecurityController;
    private final String mSlotAirplane;
    private final String mSlotCallStrength;
    private final String mSlotEthernet;
    private final String mSlotMobile;
    private final String mSlotNoCalling;
    private final String mSlotVpn;
    private final String mSlotWifi;
    private final TunerService mTunerService;
    private WifiIconState mWifiIconState = new WifiIconState();

    private int currentVpnIconId(boolean z) {
        return z ? C1893R.C1895drawable.stat_sys_branded_vpn : C1893R.C1895drawable.stat_sys_vpn_ic;
    }

    public void setMobileDataEnabled(boolean z) {
    }

    public void setNoSims(boolean z, boolean z2) {
    }

    @Inject
    public StatusBarSignalPolicy(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mIconController = statusBarIconController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mNetworkController = networkController;
        this.mSecurityController = securityController;
        this.mTunerService = tunerService;
        this.mFeatureFlags = featureFlags;
        this.mSlotAirplane = context.getString(17041553);
        this.mSlotMobile = context.getString(17041571);
        this.mSlotWifi = context.getString(17041587);
        this.mSlotEthernet = context.getString(17041564);
        this.mSlotVpn = context.getString(17041586);
        this.mSlotNoCalling = context.getString(17041574);
        this.mSlotCallStrength = context.getString(17041557);
        this.mActivityEnabled = context.getResources().getBoolean(C1893R.bool.config_showActivity);
    }

    public void init() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mTunerService.addTunable(this, StatusBarIconController.ICON_HIDE_LIST);
            this.mNetworkController.addCallback(this);
            this.mSecurityController.addCallback(this);
        }
    }

    public void destroy() {
        this.mTunerService.removeTunable(this);
        this.mNetworkController.removeCallback(this);
        this.mSecurityController.removeCallback(this);
    }

    /* access modifiers changed from: private */
    public void updateVpn() {
        boolean isVpnEnabled = this.mSecurityController.isVpnEnabled();
        this.mIconController.setIcon(this.mSlotVpn, currentVpnIconId(this.mSecurityController.isVpnBranded()), this.mContext.getResources().getString(C1893R.string.accessibility_vpn_on));
        this.mIconController.setIconVisibility(this.mSlotVpn, isVpnEnabled);
    }

    public void onStateChanged() {
        this.mHandler.post(new StatusBarSignalPolicy$$ExternalSyntheticLambda0(this));
    }

    public void onTuningChanged(String str, String str2) {
        if (StatusBarIconController.ICON_HIDE_LIST.equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, str2);
            boolean contains = iconHideList.contains(this.mSlotAirplane);
            boolean contains2 = iconHideList.contains(this.mSlotMobile);
            boolean contains3 = iconHideList.contains(this.mSlotWifi);
            boolean contains4 = iconHideList.contains(this.mSlotEthernet);
            if (contains != this.mHideAirplane || contains2 != this.mHideMobile || contains4 != this.mHideEthernet || contains3 != this.mHideWifi) {
                this.mHideAirplane = contains;
                this.mHideMobile = contains2;
                this.mHideEthernet = contains4;
                this.mHideWifi = contains3;
                this.mNetworkController.removeCallback(this);
                this.mNetworkController.addCallback(this);
            }
        }
    }

    public void setWifiIndicators(WifiIndicators wifiIndicators) {
        boolean z;
        if (DEBUG) {
            Log.d(TAG, "setWifiIndicators: " + wifiIndicators);
        }
        boolean z2 = false;
        boolean z3 = wifiIndicators.statusIcon.visible && !this.mHideWifi;
        boolean z4 = wifiIndicators.activityIn && this.mActivityEnabled && z3;
        boolean z5 = wifiIndicators.activityOut && this.mActivityEnabled && z3;
        this.mIsWifiEnabled = wifiIndicators.enabled;
        WifiIconState copy = this.mWifiIconState.copy();
        if (this.mWifiIconState.noDefaultNetwork && this.mWifiIconState.noNetworksAvailable && !this.mIsAirplaneMode) {
            copy.visible = true;
            copy.resId = C1893R.C1895drawable.ic_qs_no_internet_unavailable;
        } else if (!this.mWifiIconState.noDefaultNetwork || this.mWifiIconState.noNetworksAvailable || ((z = this.mIsAirplaneMode) && (!z || !this.mIsWifiEnabled))) {
            copy.visible = z3;
            copy.resId = wifiIndicators.statusIcon.icon;
            copy.activityIn = z4;
            copy.activityOut = z5;
            copy.contentDescription = wifiIndicators.statusIcon.contentDescription;
            MobileIconState firstMobileState = getFirstMobileState();
            if (!(firstMobileState == null || firstMobileState.typeId == 0)) {
                z2 = true;
            }
            copy.signalSpacerVisible = z2;
        } else {
            copy.visible = true;
            copy.resId = C1893R.C1895drawable.ic_qs_no_internet_available;
        }
        copy.slot = this.mSlotWifi;
        copy.airplaneSpacerVisible = this.mIsAirplaneMode;
        updateWifiIconWithState(copy);
        this.mWifiIconState = copy;
    }

    private void updateShowWifiSignalSpacer(WifiIconState wifiIconState) {
        MobileIconState firstMobileState = getFirstMobileState();
        wifiIconState.signalSpacerVisible = (firstMobileState == null || firstMobileState.typeId == 0) ? false : true;
    }

    private void updateWifiIconWithState(WifiIconState wifiIconState) {
        if (DEBUG) {
            Log.d(TAG, new StringBuilder("WifiIconState: ").append((Object) wifiIconState).toString() == null ? "" : wifiIconState.toString());
        }
        if (!wifiIconState.visible || wifiIconState.resId <= 0) {
            this.mIconController.setIconVisibility(this.mSlotWifi, false);
            return;
        }
        this.mIconController.setSignalIcon(this.mSlotWifi, wifiIconState);
        this.mIconController.setIconVisibility(this.mSlotWifi, true);
    }

    public void setCallIndicator(IconState iconState, int i) {
        if (DEBUG) {
            Log.d(TAG, "setCallIndicator: statusIcon = " + iconState + ",subId = " + i);
        }
        CallIndicatorIconState noCallingState = getNoCallingState(i);
        if (noCallingState != null) {
            if (iconState.icon == C1893R.C1895drawable.ic_qs_no_calling_sms) {
                noCallingState.isNoCalling = iconState.visible;
                noCallingState.noCallingDescription = iconState.contentDescription;
            } else {
                noCallingState.callStrengthResId = iconState.icon;
                noCallingState.callStrengthDescription = iconState.contentDescription;
            }
            if (this.mCarrierConfigTracker.getCallStrengthConfig(i)) {
                this.mIconController.setCallStrengthIcons(this.mSlotCallStrength, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
            } else {
                this.mIconController.removeIcon(this.mSlotCallStrength, i);
            }
            this.mIconController.setNoCallingIcons(this.mSlotNoCalling, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
        }
    }

    public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "setMobileDataIndicators: " + mobileDataIndicators);
        }
        MobileIconState state = getState(mobileDataIndicators.subId);
        if (state != null) {
            boolean z2 = true;
            boolean z3 = mobileDataIndicators.statusType != state.typeId && (mobileDataIndicators.statusType == 0 || state.typeId == 0);
            state.visible = mobileDataIndicators.statusIcon.visible && !this.mHideMobile;
            state.strengthId = mobileDataIndicators.statusIcon.icon;
            state.typeId = mobileDataIndicators.statusType;
            state.contentDescription = mobileDataIndicators.statusIcon.contentDescription;
            state.typeContentDescription = mobileDataIndicators.typeContentDescription;
            state.showTriangle = mobileDataIndicators.showTriangle;
            state.roaming = mobileDataIndicators.roaming;
            state.activityIn = mobileDataIndicators.activityIn && this.mActivityEnabled;
            if (!mobileDataIndicators.activityOut || !this.mActivityEnabled) {
                z2 = false;
            }
            state.activityOut = z2;
            state.volteId = mobileDataIndicators.volteIcon;
            if (z) {
                StringBuilder sb = new StringBuilder("MobileIconStates: ");
                ArrayList<MobileIconState> arrayList = this.mMobileStates;
                Log.d(TAG, sb.append(arrayList == null ? "" : arrayList.toString()).toString());
            }
            this.mIconController.setMobileIcons(this.mSlotMobile, MobileIconState.copyStates(this.mMobileStates));
            if (z3) {
                WifiIconState copy = this.mWifiIconState.copy();
                updateShowWifiSignalSpacer(copy);
                if (!Objects.equals(copy, this.mWifiIconState)) {
                    updateWifiIconWithState(copy);
                    this.mWifiIconState = copy;
                }
            }
        }
    }

    private CallIndicatorIconState getNoCallingState(int i) {
        Iterator<CallIndicatorIconState> it = this.mCallIndicatorStates.iterator();
        while (it.hasNext()) {
            CallIndicatorIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e(TAG, "Unexpected subscription " + i);
        return null;
    }

    private MobileIconState getState(int i) {
        Iterator<MobileIconState> it = this.mMobileStates.iterator();
        while (it.hasNext()) {
            MobileIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e(TAG, "Unexpected subscription " + i);
        return null;
    }

    private MobileIconState getFirstMobileState() {
        if (this.mMobileStates.size() > 0) {
            return this.mMobileStates.get(0);
        }
        return null;
    }

    public void setSubs(List<SubscriptionInfo> list) {
        boolean z;
        if (DEBUG) {
            Log.d(TAG, "setSubs: " + (list == null ? "" : list.toString()));
        }
        if (!hasCorrectSubs(list)) {
            this.mIconController.removeAllIconsForSlot(this.mSlotMobile);
            this.mIconController.removeAllIconsForSlot(this.mSlotNoCalling);
            this.mIconController.removeAllIconsForSlot(this.mSlotCallStrength);
            this.mMobileStates.clear();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mCallIndicatorStates);
            this.mCallIndicatorStates.clear();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                this.mMobileStates.add(new MobileIconState(list.get(i).getSubscriptionId()));
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) it.next();
                    if (callIndicatorIconState.subId == list.get(i).getSubscriptionId()) {
                        this.mCallIndicatorStates.add(callIndicatorIconState);
                        z = false;
                        break;
                    }
                }
                if (z) {
                    this.mCallIndicatorStates.add(new CallIndicatorIconState(list.get(i).getSubscriptionId()));
                }
            }
        }
    }

    private boolean hasCorrectSubs(List<SubscriptionInfo> list) {
        int size = list.size();
        if (size != this.mMobileStates.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.mMobileStates.get(i).subId != list.get(i).getSubscriptionId()) {
                return false;
            }
        }
        return true;
    }

    public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        boolean z4;
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            if (DEBUG) {
                Log.d(TAG, "setConnectivityStatus: noDefaultNetwork = " + z + ",noValidatedNetwork = " + z2 + ",noNetworksAvailable = " + z3);
            }
            WifiIconState copy = this.mWifiIconState.copy();
            copy.noDefaultNetwork = z;
            copy.noValidatedNetwork = z2;
            copy.noNetworksAvailable = z3;
            copy.slot = this.mSlotWifi;
            copy.airplaneSpacerVisible = this.mIsAirplaneMode;
            if (z && z3 && !this.mIsAirplaneMode) {
                copy.visible = true;
                copy.resId = C1893R.C1895drawable.ic_qs_no_internet_unavailable;
            } else if (!z || z3 || (z4 && (!(z4 = this.mIsAirplaneMode) || !this.mIsWifiEnabled))) {
                copy.visible = false;
                copy.resId = 0;
            } else {
                copy.visible = true;
                copy.resId = C1893R.C1895drawable.ic_qs_no_internet_available;
            }
            updateWifiIconWithState(copy);
            this.mWifiIconState = copy;
        }
    }

    public void setEthernetIndicators(IconState iconState) {
        if (iconState.visible) {
            boolean z = this.mHideEthernet;
        }
        int i = iconState.icon;
        String str = iconState.contentDescription;
        if (i > 0) {
            this.mIconController.setIcon(this.mSlotEthernet, i, str);
            this.mIconController.setIconVisibility(this.mSlotEthernet, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotEthernet, false);
    }

    public void setIsAirplaneMode(IconState iconState) {
        String str;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder("setIsAirplaneMode: icon = ");
            if (iconState == null) {
                str = "";
            } else {
                str = iconState.toString();
            }
            Log.d(TAG, sb.append(str).toString());
        }
        this.mIsAirplaneMode = iconState.visible && !this.mHideAirplane;
        int i = iconState.icon;
        String str2 = iconState.contentDescription;
        if (!this.mIsAirplaneMode || i <= 0) {
            this.mIconController.setIconVisibility(this.mSlotAirplane, false);
            return;
        }
        this.mIconController.setIcon(this.mSlotAirplane, i, str2);
        this.mIconController.setIconVisibility(this.mSlotAirplane, true);
    }

    public static class CallIndicatorIconState {
        public String callStrengthDescription;
        public int callStrengthResId;
        public boolean isNoCalling;
        public String noCallingDescription;
        public int noCallingResId;
        public int subId;

        private CallIndicatorIconState(int i) {
            this.subId = i;
            this.noCallingResId = C1893R.C1895drawable.ic_qs_no_calling_sms;
            this.callStrengthResId = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) obj;
            if (this.isNoCalling == callIndicatorIconState.isNoCalling && this.noCallingResId == callIndicatorIconState.noCallingResId && this.callStrengthResId == callIndicatorIconState.callStrengthResId && this.subId == callIndicatorIconState.subId && this.noCallingDescription == callIndicatorIconState.noCallingDescription && this.callStrengthDescription == callIndicatorIconState.callStrengthDescription) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.isNoCalling), Integer.valueOf(this.noCallingResId), Integer.valueOf(this.callStrengthResId), Integer.valueOf(this.subId), this.noCallingDescription, this.callStrengthDescription);
        }

        private void copyTo(CallIndicatorIconState callIndicatorIconState) {
            callIndicatorIconState.isNoCalling = this.isNoCalling;
            callIndicatorIconState.noCallingResId = this.noCallingResId;
            callIndicatorIconState.callStrengthResId = this.callStrengthResId;
            callIndicatorIconState.subId = this.subId;
            callIndicatorIconState.noCallingDescription = this.noCallingDescription;
            callIndicatorIconState.callStrengthDescription = this.callStrengthDescription;
        }

        /* access modifiers changed from: private */
        public static List<CallIndicatorIconState> copyStates(List<CallIndicatorIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (CallIndicatorIconState next : list) {
                CallIndicatorIconState callIndicatorIconState = new CallIndicatorIconState(next.subId);
                next.copyTo(callIndicatorIconState);
                arrayList.add(callIndicatorIconState);
            }
            return arrayList;
        }
    }

    private static abstract class SignalIconState {
        public boolean activityIn;
        public boolean activityOut;
        public String contentDescription;
        public String slot;
        public boolean visible;

        private SignalIconState() {
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SignalIconState signalIconState = (SignalIconState) obj;
            if (this.visible == signalIconState.visible && this.activityOut == signalIconState.activityOut && this.activityIn == signalIconState.activityIn && Objects.equals(this.contentDescription, signalIconState.contentDescription) && Objects.equals(this.slot, signalIconState.slot)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.visible), Boolean.valueOf(this.activityOut), this.slot);
        }

        /* access modifiers changed from: protected */
        public void copyTo(SignalIconState signalIconState) {
            signalIconState.visible = this.visible;
            signalIconState.activityIn = this.activityIn;
            signalIconState.activityOut = this.activityOut;
            signalIconState.slot = this.slot;
            signalIconState.contentDescription = this.contentDescription;
        }
    }

    public static class WifiIconState extends SignalIconState {
        public boolean airplaneSpacerVisible;
        public boolean noDefaultNetwork;
        public boolean noNetworksAvailable;
        public boolean noValidatedNetwork;
        public int resId;
        public boolean signalSpacerVisible;

        public WifiIconState() {
            super();
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            WifiIconState wifiIconState = (WifiIconState) obj;
            if (this.resId == wifiIconState.resId && this.airplaneSpacerVisible == wifiIconState.airplaneSpacerVisible && this.signalSpacerVisible == wifiIconState.signalSpacerVisible && this.noDefaultNetwork == wifiIconState.noDefaultNetwork && this.noValidatedNetwork == wifiIconState.noValidatedNetwork && this.noNetworksAvailable == wifiIconState.noNetworksAvailable) {
                return true;
            }
            return false;
        }

        public void copyTo(WifiIconState wifiIconState) {
            super.copyTo(wifiIconState);
            wifiIconState.resId = this.resId;
            wifiIconState.airplaneSpacerVisible = this.airplaneSpacerVisible;
            wifiIconState.signalSpacerVisible = this.signalSpacerVisible;
            wifiIconState.noDefaultNetwork = this.noDefaultNetwork;
            wifiIconState.noValidatedNetwork = this.noValidatedNetwork;
            wifiIconState.noNetworksAvailable = this.noNetworksAvailable;
        }

        public WifiIconState copy() {
            WifiIconState wifiIconState = new WifiIconState();
            copyTo(wifiIconState);
            return wifiIconState;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.resId), Boolean.valueOf(this.airplaneSpacerVisible), Boolean.valueOf(this.signalSpacerVisible), Boolean.valueOf(this.noDefaultNetwork), Boolean.valueOf(this.noValidatedNetwork), Boolean.valueOf(this.noNetworksAvailable));
        }

        public String toString() {
            return "WifiIconState(resId=" + this.resId + ", visible=" + this.visible + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    public static class MobileIconState extends SignalIconState {
        public boolean needsLeadingPadding;
        public boolean roaming;
        public boolean showTriangle;
        public int strengthId;
        public int subId;
        public CharSequence typeContentDescription;
        public int typeId;
        public int volteId;

        private MobileIconState(int i) {
            super();
            this.subId = i;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            MobileIconState mobileIconState = (MobileIconState) obj;
            if (this.subId == mobileIconState.subId && this.strengthId == mobileIconState.strengthId && this.typeId == mobileIconState.typeId && this.showTriangle == mobileIconState.showTriangle && this.roaming == mobileIconState.roaming && this.needsLeadingPadding == mobileIconState.needsLeadingPadding && Objects.equals(this.typeContentDescription, mobileIconState.typeContentDescription) && this.volteId == mobileIconState.volteId) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.subId), Integer.valueOf(this.strengthId), Integer.valueOf(this.typeId), Boolean.valueOf(this.showTriangle), Boolean.valueOf(this.roaming), Boolean.valueOf(this.needsLeadingPadding), this.typeContentDescription);
        }

        public MobileIconState copy() {
            MobileIconState mobileIconState = new MobileIconState(this.subId);
            copyTo(mobileIconState);
            return mobileIconState;
        }

        public void copyTo(MobileIconState mobileIconState) {
            super.copyTo(mobileIconState);
            mobileIconState.subId = this.subId;
            mobileIconState.strengthId = this.strengthId;
            mobileIconState.typeId = this.typeId;
            mobileIconState.showTriangle = this.showTriangle;
            mobileIconState.roaming = this.roaming;
            mobileIconState.needsLeadingPadding = this.needsLeadingPadding;
            mobileIconState.typeContentDescription = this.typeContentDescription;
            mobileIconState.volteId = this.volteId;
        }

        /* access modifiers changed from: private */
        public static List<MobileIconState> copyStates(List<MobileIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (MobileIconState next : list) {
                MobileIconState mobileIconState = new MobileIconState(next.subId);
                next.copyTo(mobileIconState);
                arrayList.add(mobileIconState);
            }
            return arrayList;
        }

        public String toString() {
            return "MobileIconState(subId=" + this.subId + ", strengthId=" + this.strengthId + ", showTriangle=" + this.showTriangle + ", roaming=" + this.roaming + ", typeId=" + this.typeId + ", volteId=" + this.volteId + ", visible=" + this.visible + NavigationBarInflaterView.KEY_CODE_END;
        }
    }
}
