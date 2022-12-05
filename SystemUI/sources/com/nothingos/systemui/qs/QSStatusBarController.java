package com.nothingos.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.privacy.PrivacyChipEvent;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class QSStatusBarController {
    private final String mCameraSlot;
    private boolean mChipVisible;
    private Context mContext;
    private boolean mListening;
    private boolean mLocationIndicatorsEnabled;
    private final String mLocationSlot;
    private boolean mMicCameraIndicatorsEnabled;
    private final String mMicSlot;
    private boolean mPrivacyChipLogged;
    private final PrivacyDialogController mPrivacyDialogController;
    private final PrivacyItemController mPrivacyItemController;
    private final PrivacyLogger mPrivacyLogger;
    private final UiEventLogger mUiEventLogger;
    private ArrayMap<String, PrivacyDialogController.MicModeInfo> mMicModeInfoList = new ArrayMap<>();
    ArrayList<QSSControllerListener> mListeners = null;
    private BroadcastReceiver mMicReceiver = new BroadcastReceiver() { // from class: com.nothingos.systemui.qs.QSStatusBarController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.MIC_MODE_UPDATE_UI".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("name");
                boolean booleanExtra = intent.getBooleanExtra("show_ui", false);
                int intExtra = intent.getIntExtra("mic_mode", 0);
                boolean booleanExtra2 = intent.getBooleanExtra("speaker_mode", false);
                Log.i("QSStatusBarController", "pkg = " + stringExtra + ", showUI = " + booleanExtra + ",mic_mode = " + intExtra + ",speake_mode = " + booleanExtra2);
                if (!booleanExtra || TextUtils.isEmpty(stringExtra)) {
                    QSStatusBarController.this.mMicModeInfoList.remove(stringExtra);
                } else {
                    QSStatusBarController.this.mMicModeInfoList.put(stringExtra, new PrivacyDialogController.MicModeInfo(true, intExtra, booleanExtra2));
                }
            }
        }
    };
    private PrivacyItemController.Callback mPICCallback = new PrivacyItemController.Callback() { // from class: com.nothingos.systemui.qs.QSStatusBarController.2
        List<PrivacyItem> privacyItems;

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public void onPrivacyItemsChanged(List<PrivacyItem> list) {
            this.privacyItems = list;
            ArrayList<QSSControllerListener> arrayList = QSStatusBarController.this.mListeners;
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    QSStatusBarController.this.mListeners.get(i).setPrivacyList(list);
                }
            }
            QSStatusBarController.this.setChipVisibility(!list.isEmpty());
            if (list.size() == 0) {
                QSStatusBarController.this.mMicModeInfoList.clear();
            }
        }

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public void onFlagMicCameraChanged(boolean z) {
            if (QSStatusBarController.this.mMicCameraIndicatorsEnabled != z) {
                QSStatusBarController.this.mMicCameraIndicatorsEnabled = z;
                update();
            }
        }

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public void onFlagLocationChanged(boolean z) {
            if (QSStatusBarController.this.mLocationIndicatorsEnabled != z) {
                QSStatusBarController.this.mLocationIndicatorsEnabled = z;
                update();
            }
        }

        private void update() {
            QSStatusBarController.this.updatePrivacyIconSlots();
            QSStatusBarController.this.setChipVisibility(!this.privacyItems.isEmpty());
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.nothingos.systemui.qs.QSStatusBarController.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            QSStatusBarController.this.mUiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_CLICK);
            QSStatusBarController.this.mPrivacyDialogController.setMicModeInfo(QSStatusBarController.this.mMicModeInfoList);
            QSStatusBarController.this.mPrivacyDialogController.showDialog(QSStatusBarController.this.mContext);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface QSSControllerListener {
        void addIgnoredSlot(String... strArr);

        void removeIgnoredSlot(String... strArr);

        void setOnClickListener(View.OnClickListener onClickListener);

        void setPrivacyList(List<PrivacyItem> list);

        void setVisibility(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QSStatusBarController(Context context, PrivacyDialogController privacyDialogController, PrivacyItemController privacyItemController, UiEventLogger uiEventLogger, PrivacyLogger privacyLogger) {
        this.mContext = context;
        this.mPrivacyDialogController = privacyDialogController;
        this.mPrivacyItemController = privacyItemController;
        this.mUiEventLogger = uiEventLogger;
        this.mPrivacyLogger = privacyLogger;
        this.mCameraSlot = context.getResources().getString(17041441);
        this.mMicSlot = context.getResources().getString(17041453);
        this.mLocationSlot = context.getResources().getString(17041451);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onViewAttached(QSSControllerListener qSSControllerListener) {
        addListener(qSSControllerListener);
        qSSControllerListener.setOnClickListener(this.mOnClickListener);
        setListening(true);
        this.mMicCameraIndicatorsEnabled = this.mPrivacyItemController.getMicCameraAvailable();
        this.mLocationIndicatorsEnabled = this.mPrivacyItemController.getLocationAvailable();
        updatePrivacyIconSlots();
        setChipVisibility(this.mChipVisible);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onViewDetached(QSSControllerListener qSSControllerListener) {
        removeListener(qSSControllerListener);
        qSSControllerListener.setOnClickListener(null);
        setListening(false);
    }

    private void setListening(boolean z) {
        if (z == this.mListening) {
            return;
        }
        this.mListening = z;
        Log.i("QSStatusBarController", "setListening " + z);
        if (z) {
            this.mMicCameraIndicatorsEnabled = this.mPrivacyItemController.getMicCameraAvailable();
            this.mLocationIndicatorsEnabled = this.mPrivacyItemController.getLocationAvailable();
            this.mPrivacyItemController.addCallback(this.mPICCallback);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.MIC_MODE_UPDATE_UI");
            this.mContext.registerReceiver(this.mMicReceiver, intentFilter);
            return;
        }
        this.mPrivacyItemController.removeCallback(this.mPICCallback);
        this.mContext.unregisterReceiver(this.mMicReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChipVisibility(boolean z) {
        this.mChipVisible = z;
        if (z && getChipEnabled()) {
            this.mPrivacyLogger.logChipVisible(true);
            if (!this.mPrivacyChipLogged && this.mListening) {
                this.mPrivacyChipLogged = true;
                this.mUiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_VIEW);
            }
        } else {
            this.mPrivacyLogger.logChipVisible(false);
        }
        ArrayList<QSSControllerListener> arrayList = this.mListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mListeners.get(i).setVisibility(z ? 0 : 8);
            }
        }
        Log.i("QSStatusBarController", "setChipVisibility " + this.mChipVisible);
    }

    public void updatePrivacyIconSlots() {
        if (getChipEnabled()) {
            if (this.mMicCameraIndicatorsEnabled) {
                ArrayList<QSSControllerListener> arrayList = this.mListeners;
                if (arrayList != null) {
                    int size = arrayList.size();
                    for (int i = 0; i < size; i++) {
                        this.mListeners.get(i).addIgnoredSlot(this.mCameraSlot, this.mMicSlot);
                    }
                }
            } else {
                ArrayList<QSSControllerListener> arrayList2 = this.mListeners;
                if (arrayList2 != null) {
                    int size2 = arrayList2.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        this.mListeners.get(i2).removeIgnoredSlot(this.mCameraSlot, this.mMicSlot);
                    }
                }
            }
            if (this.mLocationIndicatorsEnabled) {
                ArrayList<QSSControllerListener> arrayList3 = this.mListeners;
                if (arrayList3 == null) {
                    return;
                }
                int size3 = arrayList3.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    this.mListeners.get(i3).addIgnoredSlot(this.mLocationSlot);
                }
                return;
            }
            ArrayList<QSSControllerListener> arrayList4 = this.mListeners;
            if (arrayList4 == null) {
                return;
            }
            int size4 = arrayList4.size();
            for (int i4 = 0; i4 < size4; i4++) {
                this.mListeners.get(i4).removeIgnoredSlot(this.mLocationSlot);
            }
            return;
        }
        ArrayList<QSSControllerListener> arrayList5 = this.mListeners;
        if (arrayList5 == null) {
            return;
        }
        int size5 = arrayList5.size();
        for (int i5 = 0; i5 < size5; i5++) {
            this.mListeners.get(i5).removeIgnoredSlot(this.mCameraSlot, this.mMicSlot, this.mLocationSlot);
        }
    }

    private boolean getChipEnabled() {
        return this.mMicCameraIndicatorsEnabled || this.mLocationIndicatorsEnabled;
    }

    private void addListener(QSSControllerListener qSSControllerListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(qSSControllerListener);
    }

    private void removeListener(QSSControllerListener qSSControllerListener) {
        ArrayList<QSSControllerListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(qSSControllerListener);
        if (this.mListeners.size() != 0) {
            return;
        }
        this.mListeners = null;
    }
}
