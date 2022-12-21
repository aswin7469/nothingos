package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AndroidInternalsModule {
    @Singleton
    @Provides
    public LockPatternUtils provideLockPatternUtils(Context context) {
        return new LockPatternUtils(context);
    }

    @Singleton
    @Provides
    public MetricsLogger provideMetricsLogger() {
        return new MetricsLogger();
    }

    @Provides
    public NotificationMessagingUtil provideNotificationMessagingUtil(Context context) {
        return new NotificationMessagingUtil(context);
    }

    @Singleton
    @Provides
    static UiEventLogger provideUiEventLogger() {
        return new UiEventLoggerImpl();
    }
}
