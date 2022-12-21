package com.android.systemui.plugins;

import android.util.ArrayMap;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.PluginDependency;
import com.android.systemui.shared.plugins.PluginManager;
import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PluginDependencyProvider extends PluginDependency.DependencyProvider {
    private final ArrayMap<Class<?>, Object> mDependencies = new ArrayMap<>();
    private final Lazy<PluginManager> mManagerLazy;

    @Inject
    public PluginDependencyProvider(Lazy<PluginManager> lazy) {
        this.mManagerLazy = lazy;
        PluginDependency.sProvider = this;
    }

    public <T> void allowPluginDependency(Class<T> cls) {
        allowPluginDependency(cls, Dependency.get(cls));
    }

    public <T> void allowPluginDependency(Class<T> cls, T t) {
        synchronized (this.mDependencies) {
            this.mDependencies.put(cls, t);
        }
    }

    /* access modifiers changed from: package-private */
    public <T> T get(Plugin plugin, Class<T> cls) {
        T t;
        if (this.mManagerLazy.get().dependsOn(plugin, cls)) {
            synchronized (this.mDependencies) {
                if (this.mDependencies.containsKey(cls)) {
                    t = this.mDependencies.get(cls);
                } else {
                    throw new IllegalArgumentException("Unknown dependency " + cls);
                }
            }
            return t;
        }
        throw new IllegalArgumentException(plugin.getClass() + " does not depend on " + cls);
    }
}
