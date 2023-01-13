package com.nothing.systemui.navigationbar;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;
import com.android.systemui.shared.system.QuickStepContract;
import com.nothing.systemui.util.NTLogUtil;

public class NavigationModeControllerEx {
    private static final String TAG = "NavigationModeControllerEx";
    private Context mCurrentUserContext;

    public void updateNavModeOverlay(Context context, IOverlayManager iOverlayManager, int i) {
        this.mCurrentUserContext = context;
        int userId = context.getUserId();
        if (i == 2) {
            try {
                if (!isOverlayInfoEnabled(QuickStepContract.NAV_BAR_MODE_GESTURAL_OVERLAY, userId, iOverlayManager)) {
                    NTLogUtil.m1686d(TAG, "switch nav mode overlay to gestural for usr:" + userId);
                    iOverlayManager.setEnabledExclusiveInCategory(QuickStepContract.NAV_BAR_MODE_GESTURAL_OVERLAY, userId);
                    return;
                }
            } catch (RemoteException e) {
                NTLogUtil.m1686d(TAG, "Fail to enable nav bar overlay for userId: " + userId + " | " + e);
                return;
            }
        }
        if (i == 0 && !isOverlayInfoEnabled(QuickStepContract.NAV_BAR_MODE_3BUTTON_OVERLAY, userId, iOverlayManager)) {
            NTLogUtil.m1686d(TAG, "switch nav mode overlay to 3 button for usr:" + userId);
            iOverlayManager.setEnabledExclusiveInCategory(QuickStepContract.NAV_BAR_MODE_3BUTTON_OVERLAY, userId);
        }
    }

    private boolean isOverlayInfoEnabled(String str, int i, IOverlayManager iOverlayManager) {
        try {
            OverlayInfo overlayInfo = iOverlayManager.getOverlayInfo(str, i);
            if (overlayInfo == null) {
                NTLogUtil.m1686d(TAG, "overlay info is null, apk: " + str + " | userId = " + i);
            }
            if (overlayInfo == null || !overlayInfo.isEnabled()) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            NTLogUtil.m1686d(TAG, "Fail to get overlay apk for userId: " + i + " | " + e);
            return false;
        }
    }
}
