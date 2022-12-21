package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.dagger.NotificationLog;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JT\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b2\u0016\u0010\n\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b2\u0014\u0010\r\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u000e0\u000b2\u000e\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilderLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logBuildNodeSpec", "", "oldSections", "", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "newHeaders", "", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "newCounts", "", "newSectionOrder", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NodeSpecBuilderLogger.kt */
public final class NodeSpecBuilderLogger {
    private final LogBuffer buffer;

    public final void logBuildNodeSpec(Set<NotifSection> set, Map<NotifSection, ? extends NodeController> map, Map<NotifSection, Integer> map2, List<NotifSection> list) {
        Intrinsics.checkNotNullParameter(set, "oldSections");
        Intrinsics.checkNotNullParameter(map, "newHeaders");
        Intrinsics.checkNotNullParameter(map2, "newCounts");
        Intrinsics.checkNotNullParameter(list, "newSectionOrder");
    }

    @Inject
    public NodeSpecBuilderLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }
}
