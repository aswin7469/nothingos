package com.android.systemui.plugins;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.PluginModule;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginEnabler;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginManagerImpl;
import com.android.systemui.shared.plugins.PluginPrefs;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import com.android.systemui.smartspace.dagger.SmartspaceViewComponent;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(includes = {GlobalConcurrencyModule.class})
public abstract class PluginsModule {
    public static final String PLUGIN_DEBUG = "plugin_debug";
    public static final String PLUGIN_PRIVILEGED = "plugin_privileged";
    public static final String PLUGIN_THREAD = "plugin_thread";

    /* access modifiers changed from: package-private */
    @Binds
    public abstract PluginEnabler bindsPluginEnablerImpl(PluginEnablerImpl pluginEnablerImpl);

    @Provides
    @Named("plugin_debug")
    static boolean providesPluginDebug() {
        return Build.IS_DEBUGGABLE;
    }

    @Singleton
    @Provides
    static PluginInstance.Factory providesPluginInstanceFactory(@Named("plugin_privileged") List<String> list, @Named("plugin_debug") boolean z) {
        return new PluginInstance.Factory(PluginModule.class.getClassLoader(), new PluginInstance.InstanceFactory(), new PluginInstance.VersionChecker(), list, z);
    }

    @Singleton
    @Provides
    static PluginActionManager.Factory providePluginInstanceManagerFactory(Context context, PackageManager packageManager, @Main Executor executor, @Named("plugin_thread") Executor executor2, NotificationManager notificationManager, PluginEnabler pluginEnabler, @Named("plugin_privileged") List<String> list, PluginInstance.Factory factory) {
        return new PluginActionManager.Factory(context, packageManager, executor, executor2, notificationManager, pluginEnabler, list, factory);
    }

    @Singleton
    @Provides
    @Named("plugin_thread")
    static Executor providesPluginExecutor(ThreadFactory threadFactory) {
        return threadFactory.buildExecutorOnNewThread(SmartspaceViewComponent.SmartspaceViewModule.PLUGIN);
    }

    @Singleton
    @Provides
    static PluginManager providesPluginManager(Context context, PluginActionManager.Factory factory, @Named("plugin_debug") boolean z, UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager, PluginEnabler pluginEnabler, PluginPrefs pluginPrefs, @Named("plugin_privileged") List<String> list) {
        return new PluginManagerImpl(context, factory, z, uncaughtExceptionPreHandlerManager, pluginEnabler, pluginPrefs, list);
    }

    @Provides
    static PluginPrefs providesPluginPrefs(Context context) {
        return new PluginPrefs(context);
    }

    @Provides
    @Named("plugin_privileged")
    static List<String> providesPrivilegedPlugins(Context context) {
        return Arrays.asList(context.getResources().getStringArray(C1894R.array.config_pluginWhitelist));
    }
}
