package com.android.systemui.statusbar.notification.collection.coordinator;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bb\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "", "removeNotification", "", "key", "", "releaseImmediately", "", "updateNotification", "alert", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
interface HunMutator {
    void removeNotification(String str, boolean z);

    void updateNotification(String str, boolean z);
}
