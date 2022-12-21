package com.android.systemui.smartspace;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0001\tJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/smartspace/SmartspacePrecondition;", "", "addListener", "", "listener", "Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;", "conditionsMet", "", "removeListener", "Listener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartspacePrecondition.kt */
public interface SmartspacePrecondition {

    @Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;", "", "onCriteriaChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SmartspacePrecondition.kt */
    public interface Listener {
        void onCriteriaChanged();
    }

    void addListener(Listener listener);

    boolean conditionsMet();

    void removeListener(Listener listener);
}
