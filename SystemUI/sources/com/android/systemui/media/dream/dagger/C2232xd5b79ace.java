package com.android.systemui.media.dream.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.systemui.media.dream.dagger.MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory */
public final class C2232xd5b79ace implements Factory<ComplicationLayoutParams> {
    public ComplicationLayoutParams get() {
        return provideLayoutParams();
    }

    public static C2232xd5b79ace create() {
        return InstanceHolder.INSTANCE;
    }

    public static ComplicationLayoutParams provideLayoutParams() {
        return (ComplicationLayoutParams) Preconditions.checkNotNullFromProvides(MediaComplicationComponent.MediaComplicationModule.provideLayoutParams());
    }

    /* renamed from: com.android.systemui.media.dream.dagger.MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final C2232xd5b79ace INSTANCE = new C2232xd5b79ace();

        private InstanceHolder() {
        }
    }
}
