package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import android.view.View;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000g\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u001c\u0018\u00002\u00020\u0001BS\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0004\n\u0002\u0010\u001d¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewManager;", "", "context", "Landroid/content/Context;", "listContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "stackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "mediaContainerController", "Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;", "featureManager", "Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;", "sectionHeaderVisibilityProvider", "Lcom/android/systemui/statusbar/notification/SectionHeaderVisibilityProvider;", "nodeSpecBuilderLogger", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilderLogger;", "shadeViewDifferLogger", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDifferLogger;", "viewBarn", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewBarn;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;Lcom/android/systemui/statusbar/notification/SectionHeaderVisibilityProvider;Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilderLogger;Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDifferLogger;Lcom/android/systemui/statusbar/notification/collection/render/NotifViewBarn;)V", "rootController", "Lcom/android/systemui/statusbar/notification/collection/render/RootNodeController;", "specBuilder", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilder;", "viewDiffer", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDiffer;", "viewRenderer", "com/android/systemui/statusbar/notification/collection/render/ShadeViewManager$viewRenderer$1", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewManager$viewRenderer$1;", "attach", "", "renderStageManager", "Lcom/android/systemui/statusbar/notification/collection/render/RenderStageManager;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeViewManager.kt */
public final class ShadeViewManager {
    /* access modifiers changed from: private */
    public final RootNodeController rootController;
    /* access modifiers changed from: private */
    public final NodeSpecBuilder specBuilder;
    /* access modifiers changed from: private */
    public final NotifStackController stackController;
    /* access modifiers changed from: private */
    public final NotifViewBarn viewBarn;
    /* access modifiers changed from: private */
    public final ShadeViewDiffer viewDiffer;
    private final ShadeViewManager$viewRenderer$1 viewRenderer = new ShadeViewManager$viewRenderer$1(this);

    @AssistedInject
    public ShadeViewManager(Context context, @Assisted NotificationListContainer notificationListContainer, @Assisted NotifStackController notifStackController, MediaContainerController mediaContainerController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider, NodeSpecBuilderLogger nodeSpecBuilderLogger, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn) {
        Context context2 = context;
        NotificationListContainer notificationListContainer2 = notificationListContainer;
        ShadeViewDifferLogger shadeViewDifferLogger2 = shadeViewDifferLogger;
        NotifViewBarn notifViewBarn2 = notifViewBarn;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(notifStackController, "stackController");
        Intrinsics.checkNotNullParameter(mediaContainerController, "mediaContainerController");
        NotificationSectionsFeatureManager notificationSectionsFeatureManager2 = notificationSectionsFeatureManager;
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager2, "featureManager");
        SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider2 = sectionHeaderVisibilityProvider;
        Intrinsics.checkNotNullParameter(sectionHeaderVisibilityProvider2, "sectionHeaderVisibilityProvider");
        NodeSpecBuilderLogger nodeSpecBuilderLogger2 = nodeSpecBuilderLogger;
        Intrinsics.checkNotNullParameter(nodeSpecBuilderLogger2, "nodeSpecBuilderLogger");
        Intrinsics.checkNotNullParameter(shadeViewDifferLogger2, "shadeViewDifferLogger");
        Intrinsics.checkNotNullParameter(notifViewBarn2, "viewBarn");
        this.stackController = notifStackController;
        this.viewBarn = notifViewBarn2;
        RootNodeController rootNodeController = new RootNodeController(notificationListContainer, new View(context));
        this.rootController = rootNodeController;
        this.specBuilder = new NodeSpecBuilder(mediaContainerController, notificationSectionsFeatureManager2, sectionHeaderVisibilityProvider2, notifViewBarn2, nodeSpecBuilderLogger2);
        this.viewDiffer = new ShadeViewDiffer(rootNodeController, shadeViewDifferLogger2);
    }

    public final void attach(RenderStageManager renderStageManager) {
        Intrinsics.checkNotNullParameter(renderStageManager, "renderStageManager");
        renderStageManager.setViewRenderer(this.viewRenderer);
    }
}
