package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, mo64987d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RemoteInputCoordinator.kt */
final class RemoteInputCoordinatorKt$DEBUG$2 extends Lambda implements Function0<Boolean> {
    public static final RemoteInputCoordinatorKt$DEBUG$2 INSTANCE = new RemoteInputCoordinatorKt$DEBUG$2();

    RemoteInputCoordinatorKt$DEBUG$2() {
        super(0);
    }

    public final Boolean invoke() {
        return Boolean.valueOf(Log.isLoggable("RemoteInputCoordinator", 3));
    }
}
