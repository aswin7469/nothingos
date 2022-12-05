package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
/* loaded from: classes.dex */
public class ScreenRecordTile extends QSTileImpl<QSTile.BooleanState> implements RecordingController.RecordingStateChangeCallback {
    private Callback mCallback;
    private RecordingController mController;
    private KeyguardDismissUtil mKeyguardDismissUtil;
    private long mMillisUntilFinished = 0;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public ScreenRecordTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RecordingController recordingController, KeyguardDismissUtil keyguardDismissUtil) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mCallback = callback;
        this.mController = recordingController;
        recordingController.observe((LifecycleOwner) this, (ScreenRecordTile) callback);
        this.mKeyguardDismissUtil = keyguardDismissUtil;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = this.mContext.getString(R$string.quick_settings_screen_record_label);
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (this.mController.isStarting()) {
            cancelCountdown();
        } else if (this.mController.isRecording()) {
            stopRecording();
        } else {
            this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ScreenRecordTile.this.lambda$handleClick$0();
                }
            });
        }
        refreshState();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isStarting = this.mController.isStarting();
        boolean isRecording = this.mController.isRecording();
        booleanState.value = isRecording || isStarting;
        booleanState.state = (isRecording || isStarting) ? 2 : 1;
        booleanState.label = this.mContext.getString(R$string.quick_settings_screen_record_label);
        booleanState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_screenrecord);
        booleanState.forceExpandIcon = booleanState.state == 1;
        if (isRecording) {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_screen_record_stop);
        } else if (isStarting) {
            booleanState.secondaryLabel = String.format("%d...", Integer.valueOf((int) Math.floorDiv(this.mMillisUntilFinished + 500, 1000)));
        } else {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_screen_record_start);
        }
        booleanState.contentDescription = TextUtils.isEmpty(booleanState.secondaryLabel) ? booleanState.label : TextUtils.concat(booleanState.label, ", ", booleanState.secondaryLabel);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_screen_record_label);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showPrompt */
    public void lambda$handleClick$0() {
        getHost().collapsePanels();
        final Intent promptIntent = this.mController.getPromptIntent();
        this.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda0
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                boolean lambda$showPrompt$1;
                lambda$showPrompt$1 = ScreenRecordTile.this.lambda$showPrompt$1(promptIntent);
                return lambda$showPrompt$1;
            }
        }, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showPrompt$1(Intent intent) {
        this.mHost.getUserContext().startActivity(intent);
        return false;
    }

    private void cancelCountdown() {
        Log.d("ScreenRecordTile", "Cancelling countdown");
        this.mController.cancelCountdown();
    }

    private void stopRecording() {
        this.mController.stopRecording();
    }

    /* loaded from: classes.dex */
    private final class Callback implements RecordingController.RecordingStateChangeCallback {
        private Callback() {
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public void onCountdown(long j) {
            ScreenRecordTile.this.mMillisUntilFinished = j;
            ScreenRecordTile.this.refreshState();
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public void onCountdownEnd() {
            ScreenRecordTile.this.refreshState();
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public void onRecordingStart() {
            ScreenRecordTile.this.refreshState();
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public void onRecordingEnd() {
            ScreenRecordTile.this.refreshState();
        }
    }
}
