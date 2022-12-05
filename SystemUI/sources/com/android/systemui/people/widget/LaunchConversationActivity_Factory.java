package com.android.systemui.people.widget;

import android.os.UserManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.wmshell.BubblesManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LaunchConversationActivity_Factory implements Factory<LaunchConversationActivity> {
    private final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<UserManager> userManagerProvider;

    public LaunchConversationActivity_Factory(Provider<NotificationEntryManager> provider, Provider<Optional<BubblesManager>> provider2, Provider<UserManager> provider3, Provider<CommandQueue> provider4) {
        this.notificationEntryManagerProvider = provider;
        this.bubblesManagerOptionalProvider = provider2;
        this.userManagerProvider = provider3;
        this.commandQueueProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LaunchConversationActivity mo1933get() {
        return newInstance(this.notificationEntryManagerProvider.mo1933get(), this.bubblesManagerOptionalProvider.mo1933get(), this.userManagerProvider.mo1933get(), this.commandQueueProvider.mo1933get());
    }

    public static LaunchConversationActivity_Factory create(Provider<NotificationEntryManager> provider, Provider<Optional<BubblesManager>> provider2, Provider<UserManager> provider3, Provider<CommandQueue> provider4) {
        return new LaunchConversationActivity_Factory(provider, provider2, provider3, provider4);
    }

    public static LaunchConversationActivity newInstance(NotificationEntryManager notificationEntryManager, Optional<BubblesManager> optional, UserManager userManager, CommandQueue commandQueue) {
        return new LaunchConversationActivity(notificationEntryManager, optional, userManager, commandQueue);
    }
}
