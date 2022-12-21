package com.android.systemui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.p012qs.QSUserSwitcherEvent;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.concurrent.Executor;

public class GuestResumeSessionReceiver extends BroadcastReceiver {
    public static final String SETTING_GUEST_HAS_LOGGED_IN = "systemui.guest_has_logged_in";
    private static final String TAG = "GuestResumeSessionReceiver";
    public AlertDialog mNewSessionDialog;
    private final SecureSettings mSecureSettings;
    private final UiEventLogger mUiEventLogger;
    private final UserSwitcherController mUserSwitcherController;
    private final UserTracker mUserTracker;

    public GuestResumeSessionReceiver(UserSwitcherController userSwitcherController, UserTracker userTracker, UiEventLogger uiEventLogger, SecureSettings secureSettings) {
        this.mUserSwitcherController = userSwitcherController;
        this.mUserTracker = userTracker;
        this.mUiEventLogger = uiEventLogger;
        this.mSecureSettings = secureSettings;
    }

    public void register(BroadcastDispatcher broadcastDispatcher) {
        broadcastDispatcher.registerReceiver(this, new IntentFilter("android.intent.action.USER_SWITCHED"), (Executor) null, UserHandle.SYSTEM);
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
            cancelDialog();
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (intExtra == -10000) {
                Log.e(TAG, intent + " sent to GuestResumeSessionReceiver without EXTRA_USER_HANDLE");
            } else if (this.mUserTracker.getUserInfo().isGuest()) {
                if (this.mSecureSettings.getIntForUser(SETTING_GUEST_HAS_LOGGED_IN, 0, intExtra) != 0) {
                    ResetSessionDialog resetSessionDialog = new ResetSessionDialog(context, this.mUserSwitcherController, this.mUiEventLogger, intExtra);
                    this.mNewSessionDialog = resetSessionDialog;
                    resetSessionDialog.show();
                    return;
                }
                this.mSecureSettings.putIntForUser(SETTING_GUEST_HAS_LOGGED_IN, 1, intExtra);
            }
        }
    }

    private void cancelDialog() {
        AlertDialog alertDialog = this.mNewSessionDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mNewSessionDialog.cancel();
            this.mNewSessionDialog = null;
        }
    }

    public static class ResetSessionDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        public static final int BUTTON_DONTWIPE = -1;
        public static final int BUTTON_WIPE = -2;
        private final UiEventLogger mUiEventLogger;
        private final int mUserId;
        private final UserSwitcherController mUserSwitcherController;

        ResetSessionDialog(Context context, UserSwitcherController userSwitcherController, UiEventLogger uiEventLogger, int i) {
            super(context, false);
            setTitle(context.getString(C1893R.string.guest_wipe_session_title));
            setMessage(context.getString(C1893R.string.guest_wipe_session_message));
            setCanceledOnTouchOutside(false);
            setButton(-2, context.getString(C1893R.string.guest_wipe_session_wipe), this);
            setButton(-1, context.getString(C1893R.string.guest_wipe_session_dontwipe), this);
            this.mUserSwitcherController = userSwitcherController;
            this.mUiEventLogger = uiEventLogger;
            this.mUserId = i;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -2) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_WIPE);
                this.mUserSwitcherController.removeGuestUser(this.mUserId, -10000);
                dismiss();
            } else if (i == -1) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_CONTINUE);
                cancel();
            }
        }
    }
}
