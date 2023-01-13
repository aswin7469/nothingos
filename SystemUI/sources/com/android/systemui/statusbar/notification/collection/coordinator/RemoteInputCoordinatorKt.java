package com.android.systemui.statusbar.notification.collection.coordinator;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\"\u001b\u0010\u0000\u001a\u00020\u00018BX\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"DEBUG", "", "getDEBUG", "()Z", "DEBUG$delegate", "Lkotlin/Lazy;", "REMOTE_INPUT_ACTIVE_EXTENDER_AUTO_CANCEL_DELAY", "", "REMOTE_INPUT_EXTENDER_RELEASE_DELAY", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinatorKt {
    private static final Lazy DEBUG$delegate = LazyKt.lazy(RemoteInputCoordinatorKt$DEBUG$2.INSTANCE);
    private static final long REMOTE_INPUT_ACTIVE_EXTENDER_AUTO_CANCEL_DELAY = 500;
    private static final long REMOTE_INPUT_EXTENDER_RELEASE_DELAY = 200;
    private static final String TAG = "RemoteInputCoordinator";

    /* access modifiers changed from: private */
    public static final boolean getDEBUG() {
        return ((Boolean) DEBUG$delegate.getValue()).booleanValue();
    }
}
