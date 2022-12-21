package com.android.systemui.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.systemui.dagger.qualifiers.Background;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class DeleteScreenshotReceiver extends BroadcastReceiver {
    private final Executor mBackgroundExecutor;
    private final ScreenshotSmartActions mScreenshotSmartActions;

    @Inject
    public DeleteScreenshotReceiver(ScreenshotSmartActions screenshotSmartActions, @Background Executor executor) {
        this.mScreenshotSmartActions = screenshotSmartActions;
        this.mBackgroundExecutor = executor;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("android:screenshot_uri_id")) {
            this.mBackgroundExecutor.execute(new DeleteScreenshotReceiver$$ExternalSyntheticLambda0(context, Uri.parse(intent.getStringExtra("android:screenshot_uri_id"))));
            if (intent.getBooleanExtra("android:smart_actions_enabled", false)) {
                this.mScreenshotSmartActions.notifyScreenshotAction(context, intent.getStringExtra("android:screenshot_id"), "Delete", false, (Intent) null);
            }
        }
    }
}
