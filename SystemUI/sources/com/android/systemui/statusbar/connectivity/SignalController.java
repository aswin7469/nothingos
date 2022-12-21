package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.util.Log;
import com.android.settingslib.SignalIcon;
import com.android.settingslib.SignalIcon.IconGroup;
import com.android.systemui.dump.DumpsysTableLogger;
import com.android.systemui.statusbar.connectivity.ConnectivityState;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public abstract class SignalController<T extends ConnectivityState, I extends SignalIcon.IconGroup> {
    protected static final boolean CHATTY = NetworkControllerImpl.CHATTY;
    protected static final boolean DEBUG = NetworkControllerImpl.DEBUG;
    static final int HISTORY_SIZE = 64;
    static final boolean RECORD_HISTORY = true;
    private final CallbackHandler mCallbackHandler;
    protected final Context mContext;
    protected final T mCurrentState = cleanState();
    private final ConnectivityState[] mHistory = new ConnectivityState[64];
    private int mHistoryIndex;
    protected final T mLastState = cleanState();
    protected final NetworkControllerImpl mNetworkController;
    protected final String mTag;
    protected final int mTransportType;

    /* access modifiers changed from: protected */
    public abstract T cleanState();

    /* access modifiers changed from: package-private */
    public abstract void notifyListeners(SignalCallback signalCallback);

    public SignalController(String str, Context context, int i, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        this.mTag = "NetworkController." + str;
        this.mNetworkController = networkControllerImpl;
        this.mTransportType = i;
        this.mContext = context;
        this.mCallbackHandler = callbackHandler;
        for (int i2 = 0; i2 < 64; i2++) {
            this.mHistory[i2] = cleanState();
        }
    }

    public T getState() {
        return this.mCurrentState;
    }

    /* access modifiers changed from: package-private */
    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        this.mCurrentState.inetCondition = bitSet2.get(this.mTransportType) ? 1 : 0;
        notifyListenersIfNecessary();
    }

    public void resetLastState() {
        this.mCurrentState.copyFrom(this.mLastState);
    }

    /* access modifiers changed from: package-private */
    public boolean isDirty() {
        if (this.mLastState.equals(this.mCurrentState)) {
            return false;
        }
        if (!DEBUG) {
            return true;
        }
        Log.d(this.mTag, "Change in state from: " + this.mLastState + "\n\tto: " + this.mCurrentState);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void saveLastState() {
        recordLastState();
        this.mCurrentState.time = System.currentTimeMillis();
        this.mLastState.copyFrom(this.mCurrentState);
    }

    public int getQsCurrentIconId() {
        if (this.mCurrentState.connected) {
            return getIcons().qsIcons[this.mCurrentState.inetCondition][this.mCurrentState.level];
        }
        if (this.mCurrentState.enabled) {
            return getIcons().qsDiscState;
        }
        return getIcons().qsNullState;
    }

    public int getCurrentIconId() {
        if (this.mCurrentState.connected) {
            return getIcons().sbIcons[this.mCurrentState.inetCondition][this.mCurrentState.level];
        }
        if (this.mCurrentState.enabled) {
            return getIcons().sbDiscState;
        }
        return getIcons().sbNullState;
    }

    public int getContentDescription() {
        if (this.mCurrentState.connected) {
            return getIcons().contentDesc[this.mCurrentState.level];
        }
        return getIcons().discContentDesc;
    }

    /* access modifiers changed from: package-private */
    public void notifyListenersIfNecessary() {
        if (isDirty()) {
            saveLastState();
            notifyListeners();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyCallStateChange(IconState iconState, int i) {
        this.mCallbackHandler.setCallIndicator(iconState, i);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTextIfExists(int i) {
        return i != 0 ? this.mContext.getText(i) : "";
    }

    /* access modifiers changed from: protected */
    public I getIcons() {
        return this.mCurrentState.iconGroup;
    }

    /* access modifiers changed from: protected */
    public void recordLastState() {
        this.mHistory[this.mHistoryIndex].copyFrom(this.mLastState);
        this.mHistoryIndex = (this.mHistoryIndex + 1) % 64;
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("  - " + this.mTag + " -----");
        printWriter.println("  Current State: " + this.mCurrentState);
        List<ConnectivityState> orderedHistoryExcludingCurrentState = getOrderedHistoryExcludingCurrentState();
        int i = 0;
        while (i < orderedHistoryExcludingCurrentState.size()) {
            int i2 = i + 1;
            printWriter.println("  Previous State(" + i2 + "): " + this.mHistory[i]);
            i = i2;
        }
    }

    /* access modifiers changed from: package-private */
    public List<ConnectivityState> getOrderedHistoryExcludingCurrentState() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mHistory[i2].time != 0) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 < (this.mHistoryIndex + 64) - i) {
                return arrayList;
            }
            arrayList.add(this.mHistory[i3 & 63]);
        }
    }

    /* access modifiers changed from: package-private */
    public List<ConnectivityState> getOrderedHistory() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCurrentState);
        arrayList.addAll(getOrderedHistoryExcludingCurrentState());
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void dumpTableData(PrintWriter printWriter) {
        ArrayList arrayList = new ArrayList();
        List<ConnectivityState> orderedHistory = getOrderedHistory();
        for (int i = 0; i < orderedHistory.size(); i++) {
            arrayList.add(orderedHistory.get(i).tableData());
        }
        new DumpsysTableLogger(this.mTag, this.mCurrentState.tableColumns(), arrayList).printTableData(printWriter);
    }

    /* access modifiers changed from: package-private */
    public final void notifyListeners() {
        notifyListeners(this.mCallbackHandler);
    }
}
