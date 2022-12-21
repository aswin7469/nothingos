package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Singleton
@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0016J\b\u0010\f\u001a\u00020\nH\u0016J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/keyguard/LifecycleScreenStatusProvider;", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider;", "Lcom/android/systemui/keyguard/ScreenLifecycle$Observer;", "screenLifecycle", "Lcom/android/systemui/keyguard/ScreenLifecycle;", "(Lcom/android/systemui/keyguard/ScreenLifecycle;)V", "listeners", "", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider$ScreenListener;", "addCallback", "", "listener", "onScreenTurnedOn", "removeCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LifecycleScreenStatusProvider.kt */
public final class LifecycleScreenStatusProvider implements ScreenStatusProvider, ScreenLifecycle.Observer {
    private final List<ScreenStatusProvider.ScreenListener> listeners = new ArrayList();

    @Inject
    public LifecycleScreenStatusProvider(ScreenLifecycle screenLifecycle) {
        Intrinsics.checkNotNullParameter(screenLifecycle, "screenLifecycle");
        screenLifecycle.addObserver(this);
    }

    public void removeCallback(ScreenStatusProvider.ScreenListener screenListener) {
        Intrinsics.checkNotNullParameter(screenListener, "listener");
        this.listeners.remove((Object) screenListener);
    }

    public void addCallback(ScreenStatusProvider.ScreenListener screenListener) {
        Intrinsics.checkNotNullParameter(screenListener, "listener");
        this.listeners.add(screenListener);
    }

    public void onScreenTurnedOn() {
        for (ScreenStatusProvider.ScreenListener onScreenTurnedOn : this.listeners) {
            onScreenTurnedOn.onScreenTurnedOn();
        }
    }
}
