package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J&\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nJ\u0016\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\nJ\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\bJ*\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0015J\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0019J\u0014\u0010\u001b\u001a\u00020\u00062\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dJ\u0016\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\u0018\u0010\"\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010#\u001a\u0004\u0018\u00010\u0015J\u0016\u0010$\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\u0016\u0010%\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\u0006\u0010&\u001a\u00020\u0006J\"\u0010'\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010(\u001a\u0004\u0018\u00010\u00152\b\u0010#\u001a\u0004\u0018\u00010\u0015J\"\u0010)\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0015J\u0006\u0010*\u001a\u00020\u0006J\u0016\u0010+\u001a\u00020\u00062\u0006\u0010,\u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\u0016\u0010-\u001a\u00020\u00062\u0006\u0010,\u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\"\u0010.\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010/\u001a\u0004\u0018\u0001002\b\u00101\u001a\u0004\u0018\u000100J\u0016\u00102\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\"\u00103\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u00104\u001a\u0004\u0018\u00010\n2\b\u00105\u001a\u0004\u0018\u00010\nJ\u0016\u00106\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\bJ\"\u00107\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u00108\u001a\u0004\u0018\u0001092\b\u0010:\u001a\u0004\u0018\u000109J\"\u0010;\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010<\u001a\u0004\u0018\u0001092\b\u0010=\u001a\u0004\u0018\u000109R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006>"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/listbuilder/ShadeListBuilderLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logDuplicateSummary", "", "buildId", "", "groupKey", "", "existingKey", "newKey", "logDuplicateTopLevelKey", "topLevelKey", "logEndBuildList", "topLevelEntries", "numChildren", "logEntryAttachStateChanged", "key", "prevParent", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "newParent", "logFilterChanged", "prevFilter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;", "newFilter", "logFinalList", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "logFinalizeFilterInvalidated", "name", "pipelineState", "logGroupPruningSuppressed", "keepingParent", "logNotifComparatorInvalidated", "logNotifSectionInvalidated", "logOnBuildList", "logParentChangeSuppressed", "suppressedParent", "logParentChanged", "logPipelineRunSuppressed", "logPreGroupFilterInvalidated", "filterName", "logPreRenderInvalidated", "logPromoterChanged", "prevPromoter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;", "newPromoter", "logPromoterInvalidated", "logPrunedReasonChanged", "prevReason", "newReason", "logReorderingAllowedInvalidated", "logSectionChangeSuppressed", "suppressedSection", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "assignedSection", "logSectionChanged", "prevSection", "newSection", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger {
    private final LogBuffer buffer;

    @Inject
    public ShadeListBuilderLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logOnBuildList() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logOnBuildList$2.INSTANCE));
    }

    public final void logEndBuildList(int i, int i2, int i3) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logEndBuildList$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setInt1(i2);
        obtain.setInt2(i3);
        logBuffer.commit(obtain);
    }

    public final void logPreRenderInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "filterName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPreRenderInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPreGroupFilterInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "filterName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPreGroupFilterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logReorderingAllowedInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logReorderingAllowedInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPromoterInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPromoterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifSectionInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logNotifSectionInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifComparatorInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logNotifComparatorInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logFinalizeFilterInvalidated(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalizeFilterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateSummary(int i, String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "groupKey");
        Intrinsics.checkNotNullParameter(str2, "existingKey");
        Intrinsics.checkNotNullParameter(str3, "newKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.WARNING, ShadeListBuilderLogger$logDuplicateSummary$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateTopLevelKey(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "topLevelKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.WARNING, ShadeListBuilderLogger$logDuplicateTopLevelKey$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logEntryAttachStateChanged(int i, String str, GroupEntry groupEntry, GroupEntry groupEntry2) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logEntryAttachStateChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        String str2 = null;
        obtain.setStr2(groupEntry != null ? groupEntry.getKey() : null);
        if (groupEntry2 != null) {
            str2 = groupEntry2.getKey();
        }
        obtain.setStr3(str2);
        logBuffer.commit(obtain);
    }

    public final void logParentChanged(int i, GroupEntry groupEntry, GroupEntry groupEntry2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logParentChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(groupEntry != null ? groupEntry.getKey() : null);
        if (groupEntry2 != null) {
            str = groupEntry2.getKey();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logParentChangeSuppressed(int i, GroupEntry groupEntry, GroupEntry groupEntry2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logParentChangeSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(groupEntry != null ? groupEntry.getKey() : null);
        if (groupEntry2 != null) {
            str = groupEntry2.getKey();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logGroupPruningSuppressed(int i, GroupEntry groupEntry) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logGroupPruningSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        if (groupEntry != null) {
            str = groupEntry.getKey();
        } else {
            str = null;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPrunedReasonChanged(int i, String str, String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPrunedReasonChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logFilterChanged(int i, NotifFilter notifFilter, NotifFilter notifFilter2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logFilterChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(notifFilter != null ? notifFilter.getName() : null);
        if (notifFilter2 != null) {
            str = notifFilter2.getName();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logPromoterChanged(int i, NotifPromoter notifPromoter, NotifPromoter notifPromoter2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPromoterChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(notifPromoter != null ? notifPromoter.getName() : null);
        if (notifPromoter2 != null) {
            str = notifPromoter2.getName();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logSectionChanged(int i, NotifSection notifSection, NotifSection notifSection2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logSectionChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(notifSection != null ? notifSection.getLabel() : null);
        if (notifSection2 != null) {
            str = notifSection2.getLabel();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logSectionChangeSuppressed(int i, NotifSection notifSection, NotifSection notifSection2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logSectionChangeSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        String str = null;
        obtain.setStr1(notifSection != null ? notifSection.getLabel() : null);
        if (notifSection2 != null) {
            str = notifSection2.getLabel();
        }
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logFinalList(List<? extends ListEntry> list) {
        Intrinsics.checkNotNullParameter(list, "entries");
        if (list.isEmpty()) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$2.INSTANCE));
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ListEntry listEntry = (ListEntry) list.get(i);
            LogBuffer logBuffer2 = this.buffer;
            LogMessage obtain = logBuffer2.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$4.INSTANCE);
            obtain.setInt1(i);
            obtain.setStr1(listEntry.getKey());
            logBuffer2.commit(obtain);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    LogBuffer logBuffer3 = this.buffer;
                    LogMessage obtain2 = logBuffer3.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$5$2.INSTANCE);
                    obtain2.setStr1(summary.getKey());
                    logBuffer3.commit(obtain2);
                }
                int size2 = groupEntry.getChildren().size();
                for (int i2 = 0; i2 < size2; i2++) {
                    LogBuffer logBuffer4 = this.buffer;
                    LogMessage obtain3 = logBuffer4.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$7.INSTANCE);
                    obtain3.setInt1(i2);
                    obtain3.setStr1(groupEntry.getChildren().get(i2).getKey());
                    logBuffer4.commit(obtain3);
                }
            }
        }
    }

    public final void logPipelineRunSuppressed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPipelineRunSuppressed$2.INSTANCE));
    }
}
