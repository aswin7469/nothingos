package com.android.systemui.statusbar.notification.stack;

import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ConvenienceExtensionsKt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000­\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005*\u0001\u001d\u0018\u0000 \\2\u00020\u0001:\u0003\\]^Bg\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0001\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0001\u0010\u0012\u001a\u00020\u0011\u0012\b\b\u0001\u0010\u0013\u001a\u00020\u0011\u0012\b\b\u0001\u0010\u0014\u001a\u00020\u0011¢\u0006\u0002\u0010\u0015J\u001a\u00103\u001a\u00020#2\u0006\u00104\u001a\u0002052\b\u00106\u001a\u0004\u0018\u000105H\u0016J\u0011\u00107\u001a\b\u0012\u0004\u0012\u00020908¢\u0006\u0002\u0010:J%\u0010;\u001a\b\u0012\u0004\u0012\u0002H=0<\"\b\b\u0000\u0010=*\u00020>2\u0006\u0010?\u001a\u0002H=H\u0002¢\u0006\u0002\u0010@J%\u0010A\u001a\b\u0012\u0004\u0012\u0002H=0<\"\b\b\u0000\u0010=*\u00020B2\u0006\u0010?\u001a\u0002H=H\u0002¢\u0006\u0002\u0010CJ\u0019\u0010D\u001a\u0004\u0018\u00010E2\b\u00104\u001a\u0004\u0018\u000105H\u0002¢\u0006\u0002\u0010FJ\u000e\u0010G\u001a\u00020H2\u0006\u0010+\u001a\u00020,J\u001b\u0010I\u001a\u00020H2\f\u0010J\u001a\b\u0012\u0004\u0012\u00020908H\u0002¢\u0006\u0002\u0010KJ\u0018\u0010L\u001a\u00020H2\u0006\u0010M\u001a\u00020E2\u0006\u0010N\u001a\u000205H\u0002J\b\u0010O\u001a\u00020HH\u0002J\u0006\u0010P\u001a\u00020HJ\u000e\u0010Q\u001a\u00020H2\u0006\u0010R\u001a\u00020EJ'\u0010S\u001a\u00020#2\f\u0010J\u001a\b\u0012\u0004\u0012\u000209082\f\u0010T\u001a\b\u0012\u0004\u0012\u00020B0U¢\u0006\u0002\u0010VJ\u000f\u0010W\u001a\u0004\u0018\u00010HH\u0007¢\u0006\u0002\u0010XJ\u0015\u0010W\u001a\u0004\u0018\u00010H2\u0006\u0010Y\u001a\u00020Z¢\u0006\u0002\u0010[R\u000e\u0010\u0013\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u00178FX\u0004¢\u0006\f\u0012\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0004\n\u0002\u0010\u001eR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001f\u001a\u0004\u0018\u00010\u00178FX\u0004¢\u0006\f\u0012\u0004\b \u0010\u0019\u001a\u0004\b!\u0010\u001bR\u000e\u0010\"\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\u00020#8BX\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010&\u001a\u0004\u0018\u00010'8FX\u0004¢\u0006\f\u0012\u0004\b(\u0010\u0019\u001a\u0004\b)\u0010*R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010-\u001a\u0004\u0018\u00010\u00178FX\u0004¢\u0006\f\u0012\u0004\b.\u0010\u0019\u001a\u0004\b/\u0010\u001bR\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u00100\u001a\u0004\u0018\u00010\u00178FX\u0004¢\u0006\f\u0012\u0004\b1\u0010\u0019\u001a\u0004\b2\u0010\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006_"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager;", "Lcom/android/systemui/statusbar/notification/stack/StackScrollAlgorithm$SectionProvider;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "keyguardMediaController", "Lcom/android/systemui/media/KeyguardMediaController;", "sectionsFeatureManager", "Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;", "logger", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsLogger;", "notifPipelineFlags", "Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "mediaContainerController", "Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;", "incomingHeaderController", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "peopleHeaderController", "alertingHeaderController", "silentHeaderController", "(Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/media/KeyguardMediaController;Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsLogger;Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;)V", "alertingHeaderView", "Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "getAlertingHeaderView$annotations", "()V", "getAlertingHeaderView", "()Lcom/android/systemui/statusbar/notification/stack/SectionHeaderView;", "configurationListener", "com/android/systemui/statusbar/notification/stack/NotificationSectionsManager$configurationListener$1", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$configurationListener$1;", "incomingHeaderView", "getIncomingHeaderView$annotations", "getIncomingHeaderView", "initialized", "", "isUsingMultipleSections", "()Z", "mediaControlsView", "Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "getMediaControlsView$annotations", "getMediaControlsView", "()Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "parent", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;", "peopleHeaderView", "getPeopleHeaderView$annotations", "getPeopleHeaderView", "silentHeaderView", "getSilentHeaderView$annotations", "getSilentHeaderView", "beginsSection", "view", "Landroid/view/View;", "previous", "createSectionsForBuckets", "", "Lcom/android/systemui/statusbar/notification/stack/NotificationSection;", "()[Lcom/android/systemui/statusbar/notification/stack/NotificationSection;", "decorViewHeaderState", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "T", "Lcom/android/systemui/statusbar/notification/row/StackScrollerDecorView;", "header", "(Lcom/android/systemui/statusbar/notification/row/StackScrollerDecorView;)Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "expandableViewHeaderState", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "(Lcom/android/systemui/statusbar/notification/row/ExpandableView;)Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "getBucket", "", "(Landroid/view/View;)Ljava/lang/Integer;", "initialize", "", "logSections", "sections", "([Lcom/android/systemui/statusbar/notification/stack/NotificationSection;)V", "logShadeChild", "i", "child", "logShadeContents", "reinflateViews", "setHeaderForegroundColor", "color", "updateFirstAndLastViewsForAllSections", "children", "", "([Lcom/android/systemui/statusbar/notification/stack/NotificationSection;Ljava/util/List;)Z", "updateSectionBoundaries", "()Lkotlin/Unit;", "reason", "", "(Ljava/lang/String;)Lkotlin/Unit;", "Companion", "SectionBounds", "SectionUpdateState", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager implements StackScrollAlgorithm.SectionProvider {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG = false;
    private static final String TAG = "NotifSectionsManager";
    private final SectionHeaderController alertingHeaderController;
    private final ConfigurationController configurationController;
    private final NotificationSectionsManager$configurationListener$1 configurationListener = new NotificationSectionsManager$configurationListener$1(this);
    private final SectionHeaderController incomingHeaderController;
    private boolean initialized;
    private final KeyguardMediaController keyguardMediaController;
    private final NotificationSectionsLogger logger;
    private final MediaContainerController mediaContainerController;
    /* access modifiers changed from: private */
    public final NotifPipelineFlags notifPipelineFlags;
    /* access modifiers changed from: private */
    public NotificationStackScrollLayout parent;
    private final SectionHeaderController peopleHeaderController;
    private final NotificationSectionsFeatureManager sectionsFeatureManager;
    private final SectionHeaderController silentHeaderController;
    private final StatusBarStateController statusBarStateController;

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\bb\u0018\u0000*\n\b\u0000\u0010\u0001 \u0001*\u00020\u00022\u00020\u0003J\b\u0010\u0010\u001a\u00020\u0011H&R\u001a\u0010\u0004\u001a\u0004\u0018\u00010\u0005X¦\u000e¢\u0006\f\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u0004\u0018\u00010\u0005X¦\u000e¢\u0006\f\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\tø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0012À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "T", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "", "currentPosition", "", "getCurrentPosition", "()Ljava/lang/Integer;", "setCurrentPosition", "(Ljava/lang/Integer;)V", "header", "getHeader", "()Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "targetPosition", "getTargetPosition", "setTargetPosition", "adjustViewPosition", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationSectionsManager.kt */
    private interface SectionUpdateState<T extends ExpandableView> {
        void adjustViewPosition();

        Integer getCurrentPosition();

        T getHeader();

        Integer getTargetPosition();

        void setCurrentPosition(Integer num);

        void setTargetPosition(Integer num);
    }

    public static /* synthetic */ void getAlertingHeaderView$annotations() {
    }

    public static /* synthetic */ void getIncomingHeaderView$annotations() {
    }

    public static /* synthetic */ void getMediaControlsView$annotations() {
    }

    public static /* synthetic */ void getPeopleHeaderView$annotations() {
    }

    public static /* synthetic */ void getSilentHeaderView$annotations() {
    }

    @Inject
    public NotificationSectionsManager(StatusBarStateController statusBarStateController2, ConfigurationController configurationController2, KeyguardMediaController keyguardMediaController2, NotificationSectionsFeatureManager notificationSectionsFeatureManager, NotificationSectionsLogger notificationSectionsLogger, NotifPipelineFlags notifPipelineFlags2, MediaContainerController mediaContainerController2, SectionHeaderController sectionHeaderController, SectionHeaderController sectionHeaderController2, SectionHeaderController sectionHeaderController3, SectionHeaderController sectionHeaderController4) {
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardMediaController2, "keyguardMediaController");
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(notificationSectionsLogger, "logger");
        Intrinsics.checkNotNullParameter(notifPipelineFlags2, "notifPipelineFlags");
        Intrinsics.checkNotNullParameter(mediaContainerController2, "mediaContainerController");
        Intrinsics.checkNotNullParameter(sectionHeaderController, "incomingHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController2, "peopleHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController3, "alertingHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController4, "silentHeaderController");
        this.statusBarStateController = statusBarStateController2;
        this.configurationController = configurationController2;
        this.keyguardMediaController = keyguardMediaController2;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.logger = notificationSectionsLogger;
        this.notifPipelineFlags = notifPipelineFlags2;
        this.mediaContainerController = mediaContainerController2;
        this.incomingHeaderController = sectionHeaderController;
        this.peopleHeaderController = sectionHeaderController2;
        this.alertingHeaderController = sectionHeaderController3;
        this.silentHeaderController = sectionHeaderController4;
    }

    public final SectionHeaderView getSilentHeaderView() {
        return this.silentHeaderController.getHeaderView();
    }

    public final SectionHeaderView getAlertingHeaderView() {
        return this.alertingHeaderController.getHeaderView();
    }

    public final SectionHeaderView getIncomingHeaderView() {
        return this.incomingHeaderController.getHeaderView();
    }

    public final SectionHeaderView getPeopleHeaderView() {
        return this.peopleHeaderController.getHeaderView();
    }

    public final MediaContainerView getMediaControlsView() {
        return this.mediaContainerController.getMediaContainerView();
    }

    public final void initialize(NotificationStackScrollLayout notificationStackScrollLayout) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "parent");
        if (!this.initialized) {
            this.initialized = true;
            this.parent = notificationStackScrollLayout;
            reinflateViews();
            this.configurationController.addCallback(this.configurationListener);
            return;
        }
        throw new IllegalStateException("NotificationSectionsManager already initialized".toString());
    }

    public final NotificationSection[] createSectionsForBuckets() {
        int[] notificationBuckets = this.sectionsFeatureManager.getNotificationBuckets();
        Collection arrayList = new ArrayList(notificationBuckets.length);
        for (int i : notificationBuckets) {
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                notificationStackScrollLayout = null;
            }
            arrayList.add(new NotificationSection(notificationStackScrollLayout, i));
        }
        Object[] array = ((List) arrayList).toArray((T[]) new NotificationSection[0]);
        Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        return (NotificationSection[]) array;
    }

    public final void reinflateViews() {
        SectionHeaderController sectionHeaderController = this.silentHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        NotificationStackScrollLayout notificationStackScrollLayout2 = null;
        if (notificationStackScrollLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            notificationStackScrollLayout = null;
        }
        sectionHeaderController.reinflateView(notificationStackScrollLayout);
        SectionHeaderController sectionHeaderController2 = this.alertingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
        if (notificationStackScrollLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            notificationStackScrollLayout3 = null;
        }
        sectionHeaderController2.reinflateView(notificationStackScrollLayout3);
        SectionHeaderController sectionHeaderController3 = this.peopleHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout4 = this.parent;
        if (notificationStackScrollLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            notificationStackScrollLayout4 = null;
        }
        sectionHeaderController3.reinflateView(notificationStackScrollLayout4);
        SectionHeaderController sectionHeaderController4 = this.incomingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout5 = this.parent;
        if (notificationStackScrollLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            notificationStackScrollLayout5 = null;
        }
        sectionHeaderController4.reinflateView(notificationStackScrollLayout5);
        MediaContainerController mediaContainerController2 = this.mediaContainerController;
        NotificationStackScrollLayout notificationStackScrollLayout6 = this.parent;
        if (notificationStackScrollLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            notificationStackScrollLayout2 = notificationStackScrollLayout6;
        }
        mediaContainerController2.reinflateView(notificationStackScrollLayout2);
        this.keyguardMediaController.attachSinglePaneContainer(getMediaControlsView());
    }

    public boolean beginsSection(View view, View view2) {
        Intrinsics.checkNotNullParameter(view, "view");
        return view == getSilentHeaderView() || view == getMediaControlsView() || view == getPeopleHeaderView() || view == getAlertingHeaderView() || view == getIncomingHeaderView() || !Intrinsics.areEqual((Object) getBucket(view), (Object) getBucket(view2));
    }

    /* access modifiers changed from: private */
    public final Integer getBucket(View view) {
        if (view == getSilentHeaderView()) {
            return 6;
        }
        if (view == getIncomingHeaderView()) {
            return 2;
        }
        if (view == getMediaControlsView()) {
            return 1;
        }
        if (view == getPeopleHeaderView()) {
            return 4;
        }
        if (view == getAlertingHeaderView()) {
            return 5;
        }
        if (view instanceof ExpandableNotificationRow) {
            return Integer.valueOf(((ExpandableNotificationRow) view).getEntry().getBucket());
        }
        return null;
    }

    private final void logShadeChild(int i, View view) {
        if (view == getIncomingHeaderView()) {
            this.logger.logIncomingHeader(i);
        } else if (view == getMediaControlsView()) {
            this.logger.logMediaControls(i);
        } else if (view == getPeopleHeaderView()) {
            this.logger.logConversationsHeader(i);
        } else if (view == getAlertingHeaderView()) {
            this.logger.logAlertingHeader(i);
        } else if (view == getSilentHeaderView()) {
            this.logger.logSilentHeader(i);
        } else if (!(view instanceof ExpandableNotificationRow)) {
            this.logger.logOther(i, view.getClass());
        } else {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            boolean isHeadsUp = expandableNotificationRow.isHeadsUp();
            int bucket = expandableNotificationRow.getEntry().getBucket();
            if (bucket == 2) {
                this.logger.logHeadsUp(i, isHeadsUp);
            } else if (bucket == 4) {
                this.logger.logConversation(i, isHeadsUp);
            } else if (bucket == 5) {
                this.logger.logAlerting(i, isHeadsUp);
            } else if (bucket == 6) {
                this.logger.logSilent(i, isHeadsUp);
            }
        }
    }

    private final boolean isUsingMultipleSections() {
        return this.sectionsFeatureManager.getNumberOfBuckets() > 1;
    }

    public final Unit updateSectionBoundaries() {
        return updateSectionBoundaries("test");
    }

    private final <T extends ExpandableView> SectionUpdateState<T> expandableViewHeaderState(T t) {
        return new NotificationSectionsManager$expandableViewHeaderState$1(t, this);
    }

    private final <T extends StackScrollerDecorView> SectionUpdateState<T> decorViewHeaderState(T t) {
        this.notifPipelineFlags.checkLegacyPipelineEnabled();
        return new NotificationSectionsManager$decorViewHeaderState$1(expandableViewHeaderState((ExpandableView) t), t);
    }

    /* renamed from: updateSectionBoundaries$lambda-16$getSectionState  reason: not valid java name */
    private static final SectionUpdateState<ExpandableView> m3152updateSectionBoundaries$lambda16$getSectionState(NotificationSectionsManager notificationSectionsManager, SectionUpdateState<MediaContainerView> sectionUpdateState, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState2, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState3, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState4, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState5, View view) {
        if (view == notificationSectionsManager.getMediaControlsView()) {
            return sectionUpdateState;
        }
        if (view == notificationSectionsManager.getIncomingHeaderView()) {
            return sectionUpdateState2;
        }
        if (view == notificationSectionsManager.getPeopleHeaderView()) {
            return sectionUpdateState3;
        }
        if (view == notificationSectionsManager.getAlertingHeaderView()) {
            return sectionUpdateState4;
        }
        if (view == notificationSectionsManager.getSilentHeaderView()) {
            return sectionUpdateState5;
        }
        return null;
    }

    @Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b2\u0018\u00002\u00020\u0001:\u0003\r\u000e\u000fB\u0007\b\u0004¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ \u0010\n\u001a\u00020\u0007*\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u0002\u0001\u0003\u0010\u0011\u0012¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds;", "", "()V", "addNotif", "notif", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "updateSection", "", "section", "Lcom/android/systemui/statusbar/notification/stack/NotificationSection;", "setFirstAndLastVisibleChildren", "first", "last", "Many", "None", "One", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$Many;", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$One;", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$None;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationSectionsManager.kt */
    private static abstract class SectionBounds {
        public /* synthetic */ SectionBounds(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private SectionBounds() {
        }

        @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$Many;", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds;", "first", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "last", "(Lcom/android/systemui/statusbar/notification/row/ExpandableView;Lcom/android/systemui/statusbar/notification/row/ExpandableView;)V", "getFirst", "()Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "getLast", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: NotificationSectionsManager.kt */
        public static final class Many extends SectionBounds {
            private final ExpandableView first;
            private final ExpandableView last;

            public static /* synthetic */ Many copy$default(Many many, ExpandableView expandableView, ExpandableView expandableView2, int i, Object obj) {
                if ((i & 1) != 0) {
                    expandableView = many.first;
                }
                if ((i & 2) != 0) {
                    expandableView2 = many.last;
                }
                return many.copy(expandableView, expandableView2);
            }

            public final ExpandableView component1() {
                return this.first;
            }

            public final ExpandableView component2() {
                return this.last;
            }

            public final Many copy(ExpandableView expandableView, ExpandableView expandableView2) {
                Intrinsics.checkNotNullParameter(expandableView, "first");
                Intrinsics.checkNotNullParameter(expandableView2, "last");
                return new Many(expandableView, expandableView2);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Many)) {
                    return false;
                }
                Many many = (Many) obj;
                return Intrinsics.areEqual((Object) this.first, (Object) many.first) && Intrinsics.areEqual((Object) this.last, (Object) many.last);
            }

            public int hashCode() {
                return (this.first.hashCode() * 31) + this.last.hashCode();
            }

            public String toString() {
                return "Many(first=" + this.first + ", last=" + this.last + ')';
            }

            public final ExpandableView getFirst() {
                return this.first;
            }

            public final ExpandableView getLast() {
                return this.last;
            }

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public Many(ExpandableView expandableView, ExpandableView expandableView2) {
                super((DefaultConstructorMarker) null);
                Intrinsics.checkNotNullParameter(expandableView, "first");
                Intrinsics.checkNotNullParameter(expandableView2, "last");
                this.first = expandableView;
                this.last = expandableView2;
            }
        }

        @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$One;", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds;", "lone", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "(Lcom/android/systemui/statusbar/notification/row/ExpandableView;)V", "getLone", "()Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: NotificationSectionsManager.kt */
        public static final class One extends SectionBounds {
            private final ExpandableView lone;

            public static /* synthetic */ One copy$default(One one, ExpandableView expandableView, int i, Object obj) {
                if ((i & 1) != 0) {
                    expandableView = one.lone;
                }
                return one.copy(expandableView);
            }

            public final ExpandableView component1() {
                return this.lone;
            }

            public final One copy(ExpandableView expandableView) {
                Intrinsics.checkNotNullParameter(expandableView, "lone");
                return new One(expandableView);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof One) && Intrinsics.areEqual((Object) this.lone, (Object) ((One) obj).lone);
            }

            public int hashCode() {
                return this.lone.hashCode();
            }

            public String toString() {
                return "One(lone=" + this.lone + ')';
            }

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public One(ExpandableView expandableView) {
                super((DefaultConstructorMarker) null);
                Intrinsics.checkNotNullParameter(expandableView, "lone");
                this.lone = expandableView;
            }

            public final ExpandableView getLone() {
                return this.lone;
            }
        }

        @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds$None;", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionBounds;", "()V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: NotificationSectionsManager.kt */
        public static final class None extends SectionBounds {
            public static final None INSTANCE = new None();

            private None() {
                super((DefaultConstructorMarker) null);
            }
        }

        public final SectionBounds addNotif(ExpandableView expandableView) {
            Intrinsics.checkNotNullParameter(expandableView, "notif");
            if (this instanceof None) {
                return new One(expandableView);
            }
            if (this instanceof One) {
                return new Many(((One) this).getLone(), expandableView);
            }
            if (this instanceof Many) {
                return Many.copy$default((Many) this, (ExpandableView) null, expandableView, 1, (Object) null);
            }
            throw new NoWhenBranchMatchedException();
        }

        public final boolean updateSection(NotificationSection notificationSection) {
            Intrinsics.checkNotNullParameter(notificationSection, "section");
            if (this instanceof None) {
                return setFirstAndLastVisibleChildren(notificationSection, (ExpandableView) null, (ExpandableView) null);
            }
            if (this instanceof One) {
                One one = (One) this;
                return setFirstAndLastVisibleChildren(notificationSection, one.getLone(), one.getLone());
            } else if (this instanceof Many) {
                Many many = (Many) this;
                return setFirstAndLastVisibleChildren(notificationSection, many.getFirst(), many.getLast());
            } else {
                throw new NoWhenBranchMatchedException();
            }
        }

        private final boolean setFirstAndLastVisibleChildren(NotificationSection notificationSection, ExpandableView expandableView, ExpandableView expandableView2) {
            return notificationSection.setFirstVisibleChild(expandableView) || notificationSection.setLastVisibleChild(expandableView2);
        }
    }

    public final boolean updateFirstAndLastViewsForAllSections(NotificationSection[] notificationSectionArr, List<? extends ExpandableView> list) {
        SparseArray sparseArray;
        Intrinsics.checkNotNullParameter(notificationSectionArr, "sections");
        Intrinsics.checkNotNullParameter(list, "children");
        Grouping notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 = new C2794x74c1ef3e(CollectionsKt.asSequence(list), this);
        SectionBounds.None none = SectionBounds.None.INSTANCE;
        int length = notificationSectionArr.length;
        if (length < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(length);
        }
        Iterator sourceIterator = notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.sourceIterator();
        while (sourceIterator.hasNext()) {
            Object next = sourceIterator.next();
            int intValue = ((Number) notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.keyOf(next)).intValue();
            Object obj = sparseArray.get(intValue);
            if (obj == null) {
                obj = none;
            }
            sparseArray.put(intValue, ((SectionBounds) obj).addNotif((ExpandableView) next));
        }
        boolean z = false;
        for (NotificationSection notificationSection : notificationSectionArr) {
            SectionBounds sectionBounds = (SectionBounds) sparseArray.get(notificationSection.getBucket());
            if (sectionBounds == null) {
                sectionBounds = SectionBounds.None.INSTANCE;
            } else {
                Intrinsics.checkNotNullExpressionValue(sectionBounds, "sectionBounds[section.bu…et] ?: SectionBounds.None");
            }
            z = sectionBounds.updateSection(notificationSection) || z;
        }
        return z;
    }

    private final void logSections(NotificationSection[] notificationSectionArr) {
        String str;
        int length = notificationSectionArr.length;
        for (int i = 0; i < length; i++) {
            NotificationSection notificationSection = notificationSectionArr[i];
            ExpandableView firstVisibleChild = notificationSection.getFirstVisibleChild();
            String str2 = "(null)";
            if (firstVisibleChild == null) {
                str = str2;
            } else if (firstVisibleChild instanceof ExpandableNotificationRow) {
                str = ((ExpandableNotificationRow) firstVisibleChild).getEntry().getKey();
            } else {
                str = Integer.toHexString(System.identityHashCode(firstVisibleChild));
            }
            ExpandableView lastVisibleChild = notificationSection.getLastVisibleChild();
            if (lastVisibleChild != null) {
                if (lastVisibleChild instanceof ExpandableNotificationRow) {
                    str2 = ((ExpandableNotificationRow) lastVisibleChild).getEntry().getKey();
                } else {
                    str2 = Integer.toHexString(System.identityHashCode(lastVisibleChild));
                }
            }
            Log.d(TAG, "updateSections: f=" + str + " s=" + i);
            Log.d(TAG, "updateSections: l=" + str2 + " s=" + i);
        }
    }

    public final void setHeaderForegroundColor(int i) {
        SectionHeaderView peopleHeaderView = getPeopleHeaderView();
        if (peopleHeaderView != null) {
            peopleHeaderView.setForegroundColor(i);
        }
        SectionHeaderView silentHeaderView = getSilentHeaderView();
        if (silentHeaderView != null) {
            silentHeaderView.setForegroundColor(i);
        }
        SectionHeaderView alertingHeaderView = getAlertingHeaderView();
        if (alertingHeaderView != null) {
            alertingHeaderView.setForegroundColor(i);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$Companion;", "", "()V", "DEBUG", "", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationSectionsManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void logShadeContents() {
        Trace.beginSection("NotifSectionsManager.logShadeContents");
        try {
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                notificationStackScrollLayout = null;
            }
            int i = 0;
            for (View next : ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout)) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                logShadeChild(i, next);
                i = i2;
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x019a A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x019b A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01b5 A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01b7 A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013a A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x016f A[Catch:{ all -> 0x02a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x017f A[ADDED_TO_REGION, Catch:{ all -> 0x02a3 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlin.Unit updateSectionBoundaries(java.lang.String r24) {
        /*
            r23 = this;
            r0 = r23
            r1 = r24
            java.lang.String r2 = "reason"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r2)
            java.lang.String r2 = "NotifSectionsManager.update"
            android.os.Trace.beginSection(r2)
            com.android.systemui.statusbar.notification.NotifPipelineFlags r2 = r0.notifPipelineFlags     // Catch:{ all -> 0x02a3 }
            r2.checkLegacyPipelineEnabled()     // Catch:{ all -> 0x02a3 }
            boolean r2 = r23.isUsingMultipleSections()     // Catch:{ all -> 0x02a3 }
            if (r2 != 0) goto L_0x001c
            goto L_0x029d
        L_0x001c:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            r2.logStartSectionUpdate(r1)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.plugins.statusbar.StatusBarStateController r1 = r0.statusBarStateController     // Catch:{ all -> 0x02a3 }
            int r1 = r1.getState()     // Catch:{ all -> 0x02a3 }
            r8 = 0
            r9 = 1
            if (r1 == r9) goto L_0x002d
            r10 = r9
            goto L_0x002e
        L_0x002d:
            r10 = r8
        L_0x002e:
            com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager r1 = r0.sectionsFeatureManager     // Catch:{ all -> 0x02a3 }
            boolean r11 = r1.isMediaControlsEnabled()     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.MediaContainerView r1 = r23.getMediaControlsView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0042
            com.android.systemui.statusbar.notification.row.ExpandableView r1 = (com.android.systemui.statusbar.notification.row.ExpandableView) r1     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.expandableViewHeaderState(r1)     // Catch:{ all -> 0x02a3 }
            r13 = r1
            goto L_0x0043
        L_0x0042:
            r13 = 0
        L_0x0043:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r23.getIncomingHeaderView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0051
            com.android.systemui.statusbar.notification.row.StackScrollerDecorView r1 = (com.android.systemui.statusbar.notification.row.StackScrollerDecorView) r1     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x02a3 }
            r14 = r1
            goto L_0x0052
        L_0x0051:
            r14 = 0
        L_0x0052:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r23.getPeopleHeaderView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0060
            com.android.systemui.statusbar.notification.row.StackScrollerDecorView r1 = (com.android.systemui.statusbar.notification.row.StackScrollerDecorView) r1     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x02a3 }
            r15 = r1
            goto L_0x0061
        L_0x0060:
            r15 = 0
        L_0x0061:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r23.getAlertingHeaderView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0070
            com.android.systemui.statusbar.notification.row.StackScrollerDecorView r1 = (com.android.systemui.statusbar.notification.row.StackScrollerDecorView) r1     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x02a3 }
            r16 = r1
            goto L_0x0072
        L_0x0070:
            r16 = 0
        L_0x0072:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r23.getSilentHeaderView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0080
            com.android.systemui.statusbar.notification.row.StackScrollerDecorView r1 = (com.android.systemui.statusbar.notification.row.StackScrollerDecorView) r1     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x02a3 }
            r7 = r1
            goto L_0x0081
        L_0x0080:
            r7 = 0
        L_0x0081:
            r1 = 5
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState[] r1 = new com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState[r1]     // Catch:{ all -> 0x02a3 }
            r1[r8] = r13     // Catch:{ all -> 0x02a3 }
            r1[r9] = r14     // Catch:{ all -> 0x02a3 }
            r6 = 2
            r1[r6] = r15     // Catch:{ all -> 0x02a3 }
            r2 = 3
            r1[r2] = r16     // Catch:{ all -> 0x02a3 }
            r5 = 4
            r1[r5] = r7     // Catch:{ all -> 0x02a3 }
            kotlin.sequences.Sequence r1 = kotlin.sequences.SequencesKt.sequenceOf(r1)     // Catch:{ all -> 0x02a3 }
            kotlin.sequences.Sequence r4 = kotlin.sequences.SequencesKt.filterNotNull(r1)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r0.parent     // Catch:{ all -> 0x02a3 }
            java.lang.String r17 = "parent"
            if (r1 != 0) goto L_0x00a4
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r17)     // Catch:{ all -> 0x02a3 }
            r1 = 0
        L_0x00a4:
            int r1 = r1.getChildCount()     // Catch:{ all -> 0x02a3 }
            int r1 = r1 - r9
            r3 = r1
            r19 = r8
            r20 = r19
            r18 = 0
        L_0x00b0:
            r1 = -2
            if (r1 >= r3) goto L_0x01e0
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r0.parent     // Catch:{ all -> 0x02a3 }
            if (r1 != 0) goto L_0x00bb
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r17)     // Catch:{ all -> 0x02a3 }
            r1 = 0
        L_0x00bb:
            android.view.View r2 = r1.getChildAt(r3)     // Catch:{ all -> 0x02a3 }
            if (r2 == 0) goto L_0x0114
            r0.logShadeChild(r3, r2)     // Catch:{ all -> 0x02a3 }
            r1 = r23
            r24 = r2
            r2 = r13
            r21 = r3
            r3 = r14
            r12 = r4
            r4 = r15
            r8 = r5
            r5 = r16
            r8 = r6
            r6 = r7
            r22 = r7
            r7 = r24
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = m3152updateSectionBoundaries$lambda16$getSectionState(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x011c
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r21)     // Catch:{ all -> 0x02a3 }
            r1.setCurrentPosition(r2)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateSectionBoundaries$1$1$1$1 r2 = new com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateSectionBoundaries$1$1$1$1     // Catch:{ all -> 0x02a3 }
            r2.<init>(r1)     // Catch:{ all -> 0x02a3 }
            kotlin.jvm.functions.Function1 r2 = (kotlin.jvm.functions.Function1) r2     // Catch:{ all -> 0x02a3 }
            kotlin.sequences.Sequence r1 = com.android.systemui.util.ConvenienceExtensionsKt.takeUntil(r12, r2)     // Catch:{ all -> 0x02a3 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x02a3 }
        L_0x00f3:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x02a3 }
            if (r2 == 0) goto L_0x011c
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r2 = (com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState) r2     // Catch:{ all -> 0x02a3 }
            java.lang.Integer r3 = r2.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r3 == 0) goto L_0x010f
            int r3 = r3.intValue()     // Catch:{ all -> 0x02a3 }
            int r3 = r3 - r9
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)     // Catch:{ all -> 0x02a3 }
            goto L_0x0110
        L_0x010f:
            r3 = 0
        L_0x0110:
            r2.setTargetPosition(r3)     // Catch:{ all -> 0x02a3 }
            goto L_0x00f3
        L_0x0114:
            r24 = r2
            r21 = r3
            r12 = r4
            r8 = r6
            r22 = r7
        L_0x011c:
            r1 = r24
            boolean r2 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow     // Catch:{ all -> 0x02a3 }
            if (r2 == 0) goto L_0x0126
            r2 = r1
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r2 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r2     // Catch:{ all -> 0x02a3 }
            goto L_0x0127
        L_0x0126:
            r2 = 0
        L_0x0127:
            if (r2 == 0) goto L_0x0137
            int r3 = r2.getVisibility()     // Catch:{ all -> 0x02a3 }
            r4 = 8
            if (r3 != r4) goto L_0x0133
            r3 = r9
            goto L_0x0134
        L_0x0133:
            r3 = 0
        L_0x0134:
            if (r3 != 0) goto L_0x0137
            goto L_0x0138
        L_0x0137:
            r2 = 0
        L_0x0138:
            if (r19 != 0) goto L_0x016b
            if (r18 == 0) goto L_0x0164
            r3 = r18
            java.lang.Number r3 = (java.lang.Number) r3     // Catch:{ all -> 0x02a3 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x02a3 }
            if (r2 == 0) goto L_0x015a
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r2.getEntry()     // Catch:{ all -> 0x02a3 }
            if (r4 == 0) goto L_0x015a
            int r4 = r4.getBucket()     // Catch:{ all -> 0x02a3 }
            if (r3 >= r4) goto L_0x0154
            r3 = r9
            goto L_0x0155
        L_0x0154:
            r3 = 0
        L_0x0155:
            java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)     // Catch:{ all -> 0x02a3 }
            goto L_0x015b
        L_0x015a:
            r3 = 0
        L_0x015b:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf((boolean) r9)     // Catch:{ all -> 0x02a3 }
            boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r4)     // Catch:{ all -> 0x02a3 }
            goto L_0x0165
        L_0x0164:
            r3 = 0
        L_0x0165:
            if (r3 == 0) goto L_0x0168
            goto L_0x016b
        L_0x0168:
            r19 = 0
            goto L_0x016d
        L_0x016b:
            r19 = r9
        L_0x016d:
            if (r19 == 0) goto L_0x017d
            if (r2 == 0) goto L_0x0176
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r2.getEntry()     // Catch:{ all -> 0x02a3 }
            goto L_0x0177
        L_0x0176:
            r3 = 0
        L_0x0177:
            if (r3 != 0) goto L_0x017a
            goto L_0x017d
        L_0x017a:
            r3.setBucket(r8)     // Catch:{ all -> 0x02a3 }
        L_0x017d:
            if (r18 == 0) goto L_0x0193
            if (r1 == 0) goto L_0x0191
            if (r2 == 0) goto L_0x0193
            com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r2.getEntry()     // Catch:{ all -> 0x02a3 }
            int r1 = r1.getBucket()     // Catch:{ all -> 0x02a3 }
            int r3 = r18.intValue()     // Catch:{ all -> 0x02a3 }
            if (r3 == r1) goto L_0x0193
        L_0x0191:
            r1 = r9
            goto L_0x0194
        L_0x0193:
            r1 = 0
        L_0x0194:
            if (r1 == 0) goto L_0x01b1
            if (r10 == 0) goto L_0x01b1
            if (r18 != 0) goto L_0x019b
            goto L_0x01b1
        L_0x019b:
            int r1 = r18.intValue()     // Catch:{ all -> 0x02a3 }
            r3 = 6
            if (r1 != r3) goto L_0x01b1
            r1 = r22
            if (r1 != 0) goto L_0x01a7
            goto L_0x01b3
        L_0x01a7:
            int r3 = r21 + 1
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)     // Catch:{ all -> 0x02a3 }
            r1.setTargetPosition(r3)     // Catch:{ all -> 0x02a3 }
            goto L_0x01b3
        L_0x01b1:
            r1 = r22
        L_0x01b3:
            if (r2 != 0) goto L_0x01b7
            r4 = 4
            goto L_0x01d7
        L_0x01b7:
            if (r20 != 0) goto L_0x01c8
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r2.getEntry()     // Catch:{ all -> 0x02a3 }
            int r3 = r3.getBucket()     // Catch:{ all -> 0x02a3 }
            r4 = 4
            if (r3 != r4) goto L_0x01c5
            goto L_0x01c9
        L_0x01c5:
            r20 = 0
            goto L_0x01cb
        L_0x01c8:
            r4 = 4
        L_0x01c9:
            r20 = r9
        L_0x01cb:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r2.getEntry()     // Catch:{ all -> 0x02a3 }
            int r2 = r2.getBucket()     // Catch:{ all -> 0x02a3 }
            java.lang.Integer r18 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x02a3 }
        L_0x01d7:
            int r3 = r21 + -1
            r7 = r1
            r5 = r4
            r6 = r8
            r4 = r12
            r8 = 0
            goto L_0x00b0
        L_0x01e0:
            r12 = r4
            r8 = r6
            r1 = r7
            if (r13 != 0) goto L_0x01e6
            goto L_0x01f2
        L_0x01e6:
            if (r11 == 0) goto L_0x01ee
            r2 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x02a3 }
            goto L_0x01ef
        L_0x01ee:
            r2 = 0
        L_0x01ef:
            r13.setTargetPosition(r2)     // Catch:{ all -> 0x02a3 }
        L_0x01f2:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            java.lang.String r3 = "New header target positions:"
            r2.logStr(r3)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            r3 = -1
            if (r13 == 0) goto L_0x0209
            java.lang.Integer r4 = r13.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r4 == 0) goto L_0x0209
            int r4 = r4.intValue()     // Catch:{ all -> 0x02a3 }
            goto L_0x020a
        L_0x0209:
            r4 = r3
        L_0x020a:
            r2.logMediaControls(r4)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            if (r14 == 0) goto L_0x021c
            java.lang.Integer r4 = r14.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r4 == 0) goto L_0x021c
            int r4 = r4.intValue()     // Catch:{ all -> 0x02a3 }
            goto L_0x021d
        L_0x021c:
            r4 = r3
        L_0x021d:
            r2.logIncomingHeader(r4)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            if (r15 == 0) goto L_0x022f
            java.lang.Integer r4 = r15.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r4 == 0) goto L_0x022f
            int r4 = r4.intValue()     // Catch:{ all -> 0x02a3 }
            goto L_0x0230
        L_0x022f:
            r4 = r3
        L_0x0230:
            r2.logConversationsHeader(r4)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            if (r16 == 0) goto L_0x0242
            java.lang.Integer r4 = r16.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r4 == 0) goto L_0x0242
            int r4 = r4.intValue()     // Catch:{ all -> 0x02a3 }
            goto L_0x0243
        L_0x0242:
            r4 = r3
        L_0x0243:
            r2.logAlertingHeader(r4)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r2 = r0.logger     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0254
            java.lang.Integer r1 = r1.getTargetPosition()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x0254
            int r3 = r1.intValue()     // Catch:{ all -> 0x02a3 }
        L_0x0254:
            r2.logSilentHeader(r3)     // Catch:{ all -> 0x02a3 }
            java.lang.Iterable r1 = kotlin.sequences.SequencesKt.asIterable(r12)     // Catch:{ all -> 0x02a3 }
            java.util.List r1 = kotlin.collections.CollectionsKt.reversed(r1)     // Catch:{ all -> 0x02a3 }
            java.lang.Iterable r1 = (java.lang.Iterable) r1     // Catch:{ all -> 0x02a3 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x02a3 }
        L_0x0265:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x02a3 }
            if (r2 == 0) goto L_0x0275
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r2 = (com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState) r2     // Catch:{ all -> 0x02a3 }
            r2.adjustViewPosition()     // Catch:{ all -> 0x02a3 }
            goto L_0x0265
        L_0x0275:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r1 = r0.logger     // Catch:{ all -> 0x02a3 }
            java.lang.String r2 = "Final order:"
            r1.logStr(r2)     // Catch:{ all -> 0x02a3 }
            r23.logShadeContents()     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r1 = r0.logger     // Catch:{ all -> 0x02a3 }
            java.lang.String r2 = "Section boundary update complete"
            r1.logStr(r2)     // Catch:{ all -> 0x02a3 }
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r23.getSilentHeaderView()     // Catch:{ all -> 0x02a3 }
            if (r1 == 0) goto L_0x029d
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r0.parent     // Catch:{ all -> 0x02a3 }
            if (r0 != 0) goto L_0x0295
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r17)     // Catch:{ all -> 0x02a3 }
            r12 = 0
            goto L_0x0296
        L_0x0295:
            r12 = r0
        L_0x0296:
            boolean r0 = r12.hasActiveClearableNotifications(r8)     // Catch:{ all -> 0x02a3 }
            r1.setClearSectionButtonEnabled(r0)     // Catch:{ all -> 0x02a3 }
        L_0x029d:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x02a3 }
            android.os.Trace.endSection()
            return r0
        L_0x02a3:
            r0 = move-exception
            android.os.Trace.endSection()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.updateSectionBoundaries(java.lang.String):kotlin.Unit");
    }
}
