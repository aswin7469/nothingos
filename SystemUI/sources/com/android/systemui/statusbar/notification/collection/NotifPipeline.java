package com.android.systemui.statusbar.notification.collection;

import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeSortListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000¾\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020 J\u000e\u0010!\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\"J\u000e\u0010#\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020$J\u000e\u0010%\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020&J\u000e\u0010'\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020(J\u000e\u0010)\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010*\u001a\u00020\u000f2\u0006\u0010+\u001a\u00020,J\u000e\u0010-\u001a\u00020\u000f2\u0006\u0010.\u001a\u00020/J\u000e\u00100\u001a\b\u0012\u0004\u0012\u00020201H\u0016J\u0012\u00103\u001a\u0004\u0018\u0001022\u0006\u00104\u001a\u000205H\u0016J\u0010\u00106\u001a\u0002072\b\u00108\u001a\u0004\u0018\u000105J\u0010\u00109\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0014\u0010:\u001a\u00020\u000f2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020=0<J\u0014\u0010>\u001a\u00020\u000f2\f\u0010?\u001a\b\u0012\u0004\u0012\u00020@0<J\u000e\u0010A\u001a\u00020\u000f2\u0006\u0010B\u001a\u00020CR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006D"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "notifPipelineFlags", "Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "mNotifCollection", "Lcom/android/systemui/statusbar/notification/collection/NotifCollection;", "mShadeListBuilder", "Lcom/android/systemui/statusbar/notification/collection/ShadeListBuilder;", "mRenderStageManager", "Lcom/android/systemui/statusbar/notification/collection/render/RenderStageManager;", "(Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;Lcom/android/systemui/statusbar/notification/collection/NotifCollection;Lcom/android/systemui/statusbar/notification/collection/ShadeListBuilder;Lcom/android/systemui/statusbar/notification/collection/render/RenderStageManager;)V", "isNewPipelineEnabled", "", "()Z", "addCollectionListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "addFinalizeFilter", "filter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;", "addNotificationDismissInterceptor", "interceptor", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifDismissInterceptor;", "addNotificationLifetimeExtender", "extender", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "addOnAfterRenderEntryListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderEntryListener;", "addOnAfterRenderGroupListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderGroupListener;", "addOnAfterRenderListListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderListListener;", "addOnBeforeFinalizeFilterListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeFinalizeFilterListener;", "addOnBeforeRenderListListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeRenderListListener;", "addOnBeforeSortListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeSortListener;", "addOnBeforeTransformGroupsListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeTransformGroupsListener;", "addPreGroupFilter", "addPreRenderInvalidator", "invalidator", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/Invalidator;", "addPromoter", "promoter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;", "getAllNotifs", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getEntry", "key", "", "getInternalNotifUpdater", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/InternalNotifUpdater;", "name", "removeCollectionListener", "setComparators", "comparators", "", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "setSections", "sections", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "setVisualStabilityManager", "notifStabilityManager", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifStabilityManager;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifPipeline.kt */
public final class NotifPipeline implements CommonNotifCollection {
    private final boolean isNewPipelineEnabled;
    private final NotifCollection mNotifCollection;
    private final RenderStageManager mRenderStageManager;
    private final ShadeListBuilder mShadeListBuilder;

    @Inject
    public NotifPipeline(NotifPipelineFlags notifPipelineFlags, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, RenderStageManager renderStageManager) {
        Intrinsics.checkNotNullParameter(notifPipelineFlags, "notifPipelineFlags");
        Intrinsics.checkNotNullParameter(notifCollection, "mNotifCollection");
        Intrinsics.checkNotNullParameter(shadeListBuilder, "mShadeListBuilder");
        Intrinsics.checkNotNullParameter(renderStageManager, "mRenderStageManager");
        this.mNotifCollection = notifCollection;
        this.mShadeListBuilder = shadeListBuilder;
        this.mRenderStageManager = renderStageManager;
        this.isNewPipelineEnabled = notifPipelineFlags.isNewPipelineEnabled();
    }

    public Collection<NotificationEntry> getAllNotifs() {
        Collection<NotificationEntry> allNotifs = this.mNotifCollection.getAllNotifs();
        Intrinsics.checkNotNullExpressionValue(allNotifs, "mNotifCollection.allNotifs");
        return allNotifs;
    }

    public void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        this.mNotifCollection.addCollectionListener(notifCollectionListener);
    }

    public void removeCollectionListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        this.mNotifCollection.removeCollectionListener(notifCollectionListener);
    }

    public NotificationEntry getEntry(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        return this.mNotifCollection.getEntry(str);
    }

    public final boolean isNewPipelineEnabled() {
        return this.isNewPipelineEnabled;
    }

    public final void addNotificationLifetimeExtender(NotifLifetimeExtender notifLifetimeExtender) {
        Intrinsics.checkNotNullParameter(notifLifetimeExtender, "extender");
        this.mNotifCollection.addNotificationLifetimeExtender(notifLifetimeExtender);
    }

    public final void addNotificationDismissInterceptor(NotifDismissInterceptor notifDismissInterceptor) {
        Intrinsics.checkNotNullParameter(notifDismissInterceptor, "interceptor");
        this.mNotifCollection.addNotificationDismissInterceptor(notifDismissInterceptor);
    }

    public final void addPreGroupFilter(NotifFilter notifFilter) {
        Intrinsics.checkNotNullParameter(notifFilter, SliceBroadcastRelay.EXTRA_FILTER);
        this.mShadeListBuilder.addPreGroupFilter(notifFilter);
    }

    public final void addOnBeforeTransformGroupsListener(OnBeforeTransformGroupsListener onBeforeTransformGroupsListener) {
        Intrinsics.checkNotNullParameter(onBeforeTransformGroupsListener, "listener");
        this.mShadeListBuilder.addOnBeforeTransformGroupsListener(onBeforeTransformGroupsListener);
    }

    public final void addPromoter(NotifPromoter notifPromoter) {
        Intrinsics.checkNotNullParameter(notifPromoter, "promoter");
        this.mShadeListBuilder.addPromoter(notifPromoter);
    }

    public final void addOnBeforeSortListener(OnBeforeSortListener onBeforeSortListener) {
        Intrinsics.checkNotNullParameter(onBeforeSortListener, "listener");
        this.mShadeListBuilder.addOnBeforeSortListener(onBeforeSortListener);
    }

    public final void setSections(List<? extends NotifSectioner> list) {
        Intrinsics.checkNotNullParameter(list, "sections");
        this.mShadeListBuilder.setSectioners(list);
    }

    public final void setVisualStabilityManager(NotifStabilityManager notifStabilityManager) {
        Intrinsics.checkNotNullParameter(notifStabilityManager, "notifStabilityManager");
        this.mShadeListBuilder.setNotifStabilityManager(notifStabilityManager);
    }

    public final void setComparators(List<? extends NotifComparator> list) {
        Intrinsics.checkNotNullParameter(list, "comparators");
        this.mShadeListBuilder.setComparators(list);
    }

    public final void addOnBeforeFinalizeFilterListener(OnBeforeFinalizeFilterListener onBeforeFinalizeFilterListener) {
        Intrinsics.checkNotNullParameter(onBeforeFinalizeFilterListener, "listener");
        this.mShadeListBuilder.addOnBeforeFinalizeFilterListener(onBeforeFinalizeFilterListener);
    }

    public final void addFinalizeFilter(NotifFilter notifFilter) {
        Intrinsics.checkNotNullParameter(notifFilter, SliceBroadcastRelay.EXTRA_FILTER);
        this.mShadeListBuilder.addFinalizeFilter(notifFilter);
    }

    public final void addOnBeforeRenderListListener(OnBeforeRenderListListener onBeforeRenderListListener) {
        Intrinsics.checkNotNullParameter(onBeforeRenderListListener, "listener");
        this.mShadeListBuilder.addOnBeforeRenderListListener(onBeforeRenderListListener);
    }

    public final void addPreRenderInvalidator(Invalidator invalidator) {
        Intrinsics.checkNotNullParameter(invalidator, "invalidator");
        this.mShadeListBuilder.addPreRenderInvalidator(invalidator);
    }

    public final void addOnAfterRenderListListener(OnAfterRenderListListener onAfterRenderListListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderListListener, "listener");
        this.mRenderStageManager.addOnAfterRenderListListener(onAfterRenderListListener);
    }

    public final void addOnAfterRenderGroupListener(OnAfterRenderGroupListener onAfterRenderGroupListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderGroupListener, "listener");
        this.mRenderStageManager.addOnAfterRenderGroupListener(onAfterRenderGroupListener);
    }

    public final void addOnAfterRenderEntryListener(OnAfterRenderEntryListener onAfterRenderEntryListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderEntryListener, "listener");
        this.mRenderStageManager.addOnAfterRenderEntryListener(onAfterRenderEntryListener);
    }

    public final InternalNotifUpdater getInternalNotifUpdater(String str) {
        InternalNotifUpdater internalNotifUpdater = this.mNotifCollection.getInternalNotifUpdater(str);
        Intrinsics.checkNotNullExpressionValue(internalNotifUpdater, "mNotifCollection.getInternalNotifUpdater(name)");
        return internalNotifUpdater;
    }
}
