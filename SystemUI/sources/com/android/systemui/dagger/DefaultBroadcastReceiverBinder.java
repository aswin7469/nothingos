package com.android.systemui.dagger;

import android.content.BroadcastReceiver;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.SmartActionsReceiver;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class DefaultBroadcastReceiverBinder {
    @IntoMap
    @ClassKey(ActionProxyReceiver.class)
    @Binds
    public abstract BroadcastReceiver bindActionProxyReceiver(ActionProxyReceiver actionProxyReceiver);

    @IntoMap
    @ClassKey(DeleteScreenshotReceiver.class)
    @Binds
    public abstract BroadcastReceiver bindDeleteScreenshotReceiver(DeleteScreenshotReceiver deleteScreenshotReceiver);

    @IntoMap
    @ClassKey(MediaOutputDialogReceiver.class)
    @Binds
    public abstract BroadcastReceiver bindMediaOutputDialogReceiver(MediaOutputDialogReceiver mediaOutputDialogReceiver);

    @IntoMap
    @ClassKey(PeopleSpaceWidgetPinnedReceiver.class)
    @Binds
    public abstract BroadcastReceiver bindPeopleSpaceWidgetPinnedReceiver(PeopleSpaceWidgetPinnedReceiver peopleSpaceWidgetPinnedReceiver);

    @IntoMap
    @ClassKey(PeopleSpaceWidgetProvider.class)
    @Binds
    public abstract BroadcastReceiver bindPeopleSpaceWidgetProvider(PeopleSpaceWidgetProvider peopleSpaceWidgetProvider);

    @IntoMap
    @ClassKey(SmartActionsReceiver.class)
    @Binds
    public abstract BroadcastReceiver bindSmartActionsReceiver(SmartActionsReceiver smartActionsReceiver);
}
