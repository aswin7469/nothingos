package com.android.systemui.clipboardoverlay;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.screenshot.TimeoutHandler;
import javax.inject.Inject;

@SysUISingleton
public class ClipboardOverlayControllerFactory {
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastSender mBroadcastSender;
    private final UiEventLogger mUiEventLogger;

    @Inject
    public ClipboardOverlayControllerFactory(BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, UiEventLogger uiEventLogger) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBroadcastSender = broadcastSender;
        this.mUiEventLogger = uiEventLogger;
    }

    public ClipboardOverlayController create(Context context) {
        return new ClipboardOverlayController(context, this.mBroadcastDispatcher, this.mBroadcastSender, new TimeoutHandler(context), this.mUiEventLogger);
    }
}
