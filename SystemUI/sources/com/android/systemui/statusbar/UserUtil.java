package com.android.systemui.statusbar;

import android.content.Context;
import android.content.DialogInterface;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.UserSwitcherController;

public class UserUtil {
    public static void deleteUserWithPrompt(Context context, int i, UserSwitcherController userSwitcherController) {
        new RemoveUserDialog(context, i, userSwitcherController).show();
    }

    private static final class RemoveUserDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        private final int mUserId;
        private final UserSwitcherController mUserSwitcherController;

        public RemoveUserDialog(Context context, int i, UserSwitcherController userSwitcherController) {
            super(context);
            setTitle(C1894R.string.user_remove_user_title);
            setMessage(context.getString(C1894R.string.user_remove_user_message));
            setButton(-3, context.getString(17039360), this);
            setButton(-1, context.getString(C1894R.string.user_remove_user_remove), this);
            setCanceledOnTouchOutside(false);
            this.mUserId = i;
            this.mUserSwitcherController = userSwitcherController;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -3) {
                cancel();
                return;
            }
            dismiss();
            this.mUserSwitcherController.removeUserId(this.mUserId);
        }
    }
}
