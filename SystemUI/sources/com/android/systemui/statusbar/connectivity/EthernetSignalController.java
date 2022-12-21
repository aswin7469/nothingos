package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon;
import java.util.BitSet;

public class EthernetSignalController extends SignalController<ConnectivityState, SignalIcon.IconGroup> {
    public EthernetSignalController(Context context, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        super("EthernetSignalController", context, 3, callbackHandler, networkControllerImpl);
        ConnectivityState connectivityState = this.mCurrentState;
        ConnectivityState connectivityState2 = this.mLastState;
        SignalIcon.IconGroup iconGroup = new SignalIcon.IconGroup("Ethernet Icons", EthernetIcons.ETHERNET_ICONS, (int[][]) null, AccessibilityContentDescriptions.ETHERNET_CONNECTION_VALUES, 0, 0, 0, 0, AccessibilityContentDescriptions.ETHERNET_CONNECTION_VALUES[0]);
        connectivityState2.iconGroup = iconGroup;
        connectivityState.iconGroup = iconGroup;
    }

    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        this.mCurrentState.connected = bitSet.get(this.mTransportType);
        super.updateConnectivity(bitSet, bitSet2);
    }

    public void notifyListeners(SignalCallback signalCallback) {
        signalCallback.setEthernetIndicators(new IconState(this.mCurrentState.connected, getCurrentIconId(), getTextIfExists(getContentDescription()).toString()));
    }

    public int getContentDescription() {
        if (this.mCurrentState.connected) {
            return getIcons().contentDesc[1];
        }
        return getIcons().discContentDesc;
    }

    public ConnectivityState cleanState() {
        return new ConnectivityState();
    }
}
