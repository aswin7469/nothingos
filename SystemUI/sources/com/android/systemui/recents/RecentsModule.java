package com.android.systemui.recents;

import android.content.Context;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.ContextComponentHelper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class RecentsModule {
    @IntoMap
    @ClassKey(OverviewProxyRecentsImpl.class)
    @Binds
    public abstract RecentsImplementation bindOverviewProxyRecentsImpl(OverviewProxyRecentsImpl overviewProxyRecentsImpl);

    @Provides
    public static RecentsImplementation provideRecentsImpl(Context context, ContextComponentHelper contextComponentHelper) {
        String string = context.getString(C1893R.string.config_recentsComponent);
        if (string == null || string.length() == 0) {
            throw new RuntimeException("No recents component configured", (Throwable) null);
        }
        RecentsImplementation resolveRecents = contextComponentHelper.resolveRecents(string);
        if (resolveRecents != null) {
            return resolveRecents;
        }
        try {
            try {
                return (RecentsImplementation) context.getClassLoader().loadClass(string).newInstance();
            } catch (Throwable th) {
                throw new RuntimeException("Error creating recents component: " + string, th);
            }
        } catch (Throwable th2) {
            throw new RuntimeException("Error loading recents component: " + string, th2);
        }
    }
}
