package com.android.internal.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
/* loaded from: classes4.dex */
public final class NotificationAccessConfirmationActivityContract {
    private static final ComponentName COMPONENT_NAME = new ComponentName("com.android.settings", "com.android.settings.notification.NotificationAccessConfirmationActivity");
    public static final String EXTRA_COMPONENT_NAME = "component_name";
    public static final String EXTRA_USER_ID = "user_id";

    public static Intent launcherIntent(Context context, int userId, ComponentName component) {
        return new Intent().setComponent(COMPONENT_NAME).putExtra("user_id", userId).putExtra(EXTRA_COMPONENT_NAME, component);
    }
}
