package com.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0001\u000bJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/smartspace/SmartspaceTargetFilter;", "", "addListener", "", "listener", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "filterSmartspaceTarget", "", "t", "Landroid/app/smartspace/SmartspaceTarget;", "removeListener", "Listener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartspaceTargetFilter.kt */
public interface SmartspaceTargetFilter {

    @Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "", "onCriteriaChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SmartspaceTargetFilter.kt */
    public interface Listener {
        void onCriteriaChanged();
    }

    void addListener(Listener listener);

    boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget);

    void removeListener(Listener listener);
}
