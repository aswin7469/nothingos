package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.util.concurrency.ThreadFactory;
/* loaded from: classes.dex */
public interface GlobalRootComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        /* renamed from: build */
        GlobalRootComponent mo1380build();

        /* renamed from: context */
        Builder mo1381context(Context context);
    }

    ThreadFactory createThreadFactory();

    /* renamed from: getSysUIComponent */
    SysUIComponent.Builder mo1378getSysUIComponent();

    /* renamed from: getWMComponentBuilder */
    WMComponent.Builder mo1379getWMComponentBuilder();
}
