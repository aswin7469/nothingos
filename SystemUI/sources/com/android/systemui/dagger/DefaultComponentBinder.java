package com.android.systemui.dagger;

import dagger.Module;

@Module(includes = {DefaultActivityBinder.class, DefaultBroadcastReceiverBinder.class, DefaultServiceBinder.class})
public abstract class DefaultComponentBinder {
}
