package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.Context;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.render.NotifRowController;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0016\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/RowAppearanceCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "context", "Landroid/content/Context;", "mAssistantFeedbackController", "Lcom/android/systemui/statusbar/notification/AssistantFeedbackController;", "mSectionClassifier", "Lcom/android/systemui/statusbar/notification/SectionClassifier;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/notification/AssistantFeedbackController;Lcom/android/systemui/statusbar/notification/SectionClassifier;)V", "entryToExpand", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "mAlwaysExpandNonGroupedNotification", "", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "onAfterRenderEntry", "entry", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NotifRowController;", "onBeforeRenderList", "list", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: RowAppearanceCoordinator.kt */
public final class RowAppearanceCoordinator implements Coordinator {
    private NotificationEntry entryToExpand;
    private final boolean mAlwaysExpandNonGroupedNotification;
    private AssistantFeedbackController mAssistantFeedbackController;
    private SectionClassifier mSectionClassifier;

    @Inject
    public RowAppearanceCoordinator(Context context, AssistantFeedbackController assistantFeedbackController, SectionClassifier sectionClassifier) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(assistantFeedbackController, "mAssistantFeedbackController");
        Intrinsics.checkNotNullParameter(sectionClassifier, "mSectionClassifier");
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mSectionClassifier = sectionClassifier;
        this.mAlwaysExpandNonGroupedNotification = context.getResources().getBoolean(C1894R.bool.config_alwaysExpandNonGroupedNotifications);
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addOnBeforeRenderListListener(new RowAppearanceCoordinator$$ExternalSyntheticLambda0(this));
        notifPipeline.addOnAfterRenderEntryListener(new RowAppearanceCoordinator$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public final void onBeforeRenderList(List<? extends ListEntry> list) {
        NotificationEntry representativeEntry;
        ListEntry listEntry = (ListEntry) CollectionsKt.firstOrNull(list);
        NotificationEntry notificationEntry = null;
        if (!(listEntry == null || (representativeEntry = listEntry.getRepresentativeEntry()) == null)) {
            SectionClassifier sectionClassifier = this.mSectionClassifier;
            NotifSection section = representativeEntry.getSection();
            Intrinsics.checkNotNull(section);
            if (!sectionClassifier.isMinimizedSection(section)) {
                notificationEntry = representativeEntry;
            }
        }
        this.entryToExpand = notificationEntry;
    }

    /* access modifiers changed from: private */
    public final void onAfterRenderEntry(NotificationEntry notificationEntry, NotifRowController notifRowController) {
        notifRowController.setSystemExpanded(this.mAlwaysExpandNonGroupedNotification || Intrinsics.areEqual((Object) notificationEntry, (Object) this.entryToExpand));
        notifRowController.setFeedbackIcon(this.mAssistantFeedbackController.getFeedbackIcon(notificationEntry));
        notifRowController.setLastAudiblyAlertedMs(notificationEntry.getLastAudiblyAlertedMs());
    }
}
