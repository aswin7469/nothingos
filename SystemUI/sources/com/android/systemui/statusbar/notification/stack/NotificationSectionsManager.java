package com.android.systemui.statusbar.notification.stack;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$layout;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ConvenienceExtensionsKt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager implements StackScrollAlgorithm.SectionProvider {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final SectionHeaderController alertingHeaderController;
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final NotificationSectionsManager$configurationListener$1 configurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$configurationListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onLocaleListChanged() {
            NotificationStackScrollLayout notificationStackScrollLayout;
            NotificationSectionsManager notificationSectionsManager = NotificationSectionsManager.this;
            notificationStackScrollLayout = notificationSectionsManager.parent;
            if (notificationStackScrollLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
            LayoutInflater from = LayoutInflater.from(notificationStackScrollLayout.getContext());
            Intrinsics.checkNotNullExpressionValue(from, "from(parent.context)");
            notificationSectionsManager.reinflateViews(from);
        }
    };
    @NotNull
    private final SectionHeaderController incomingHeaderController;
    private boolean initialized;
    @NotNull
    private final KeyguardMediaController keyguardMediaController;
    @NotNull
    private final NotificationSectionsLogger logger;
    @Nullable
    private MediaHeaderView mediaControlsView;
    private NotificationStackScrollLayout parent;
    @NotNull
    private final SectionHeaderController peopleHeaderController;
    @NotNull
    private final NotificationSectionsFeatureManager sectionsFeatureManager;
    @NotNull
    private final SectionHeaderController silentHeaderController;
    @NotNull
    private final StatusBarStateController statusBarStateController;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public interface SectionUpdateState<T extends ExpandableView> {
        void adjustViewPosition();

        @Nullable
        Integer getCurrentPosition();

        @Nullable
        Integer getTargetPosition();

        void setCurrentPosition(@Nullable Integer num);

        void setTargetPosition(@Nullable Integer num);
    }

    @VisibleForTesting
    public static /* synthetic */ void getAlertingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getIncomingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPeopleHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSilentHeaderView$annotations() {
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$configurationListener$1] */
    public NotificationSectionsManager(@NotNull StatusBarStateController statusBarStateController, @NotNull ConfigurationController configurationController, @NotNull KeyguardMediaController keyguardMediaController, @NotNull NotificationSectionsFeatureManager sectionsFeatureManager, @NotNull NotificationSectionsLogger logger, @NotNull SectionHeaderController incomingHeaderController, @NotNull SectionHeaderController peopleHeaderController, @NotNull SectionHeaderController alertingHeaderController, @NotNull SectionHeaderController silentHeaderController) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardMediaController, "keyguardMediaController");
        Intrinsics.checkNotNullParameter(sectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(incomingHeaderController, "incomingHeaderController");
        Intrinsics.checkNotNullParameter(peopleHeaderController, "peopleHeaderController");
        Intrinsics.checkNotNullParameter(alertingHeaderController, "alertingHeaderController");
        Intrinsics.checkNotNullParameter(silentHeaderController, "silentHeaderController");
        this.statusBarStateController = statusBarStateController;
        this.configurationController = configurationController;
        this.keyguardMediaController = keyguardMediaController;
        this.sectionsFeatureManager = sectionsFeatureManager;
        this.logger = logger;
        this.incomingHeaderController = incomingHeaderController;
        this.peopleHeaderController = peopleHeaderController;
        this.alertingHeaderController = alertingHeaderController;
        this.silentHeaderController = silentHeaderController;
    }

    @Nullable
    public final SectionHeaderView getSilentHeaderView() {
        return this.silentHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getAlertingHeaderView() {
        return this.alertingHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getIncomingHeaderView() {
        return this.incomingHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getPeopleHeaderView() {
        return this.peopleHeaderController.getHeaderView();
    }

    @VisibleForTesting
    @Nullable
    public final MediaHeaderView getMediaControlsView() {
        return this.mediaControlsView;
    }

    public final void initialize(@NotNull NotificationStackScrollLayout parent, @NotNull LayoutInflater layoutInflater) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        if (!(!this.initialized)) {
            throw new IllegalStateException("NotificationSectionsManager already initialized".toString());
        }
        this.initialized = true;
        this.parent = parent;
        reinflateViews(layoutInflater);
        this.configurationController.addCallback(this.configurationListener);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final <T extends ExpandableView> T reinflateView(T t, LayoutInflater layoutInflater, int i) {
        int indexOfChild;
        NotificationStackScrollLayout notificationStackScrollLayout;
        if (t != null) {
            ViewGroup transientContainer = t.getTransientContainer();
            if (transientContainer != null) {
                transientContainer.removeView(t);
            }
            ViewParent parent = t.getParent();
            NotificationStackScrollLayout notificationStackScrollLayout2 = this.parent;
            if (notificationStackScrollLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            } else if (parent == notificationStackScrollLayout2) {
                if (notificationStackScrollLayout2 != null) {
                    indexOfChild = notificationStackScrollLayout2.indexOfChild(t);
                    NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
                    if (notificationStackScrollLayout3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                    notificationStackScrollLayout3.removeView(t);
                    notificationStackScrollLayout = this.parent;
                    if (notificationStackScrollLayout != null) {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                    View inflate = layoutInflater.inflate(i, (ViewGroup) notificationStackScrollLayout, false);
                    Objects.requireNonNull(inflate, "null cannot be cast to non-null type T of com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.reinflateView");
                    T t2 = (T) inflate;
                    if (indexOfChild != -1) {
                        NotificationStackScrollLayout notificationStackScrollLayout4 = this.parent;
                        if (notificationStackScrollLayout4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("parent");
                            throw null;
                        }
                        notificationStackScrollLayout4.addView(t2, indexOfChild);
                    }
                    return t2;
                }
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        }
        indexOfChild = -1;
        notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout != null) {
        }
    }

    @NotNull
    public final NotificationSection[] createSectionsForBuckets() {
        int[] notificationBuckets = this.sectionsFeatureManager.getNotificationBuckets();
        ArrayList arrayList = new ArrayList(notificationBuckets.length);
        for (int i : notificationBuckets) {
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
            arrayList.add(new NotificationSection(notificationStackScrollLayout, i));
        }
        Object[] array = arrayList.toArray(new NotificationSection[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
        return (NotificationSection[]) array;
    }

    public final void reinflateViews(@NotNull LayoutInflater layoutInflater) {
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        SectionHeaderController sectionHeaderController = this.silentHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        sectionHeaderController.reinflateView(notificationStackScrollLayout);
        SectionHeaderController sectionHeaderController2 = this.alertingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout2 = this.parent;
        if (notificationStackScrollLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        sectionHeaderController2.reinflateView(notificationStackScrollLayout2);
        SectionHeaderController sectionHeaderController3 = this.peopleHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
        if (notificationStackScrollLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        sectionHeaderController3.reinflateView(notificationStackScrollLayout3);
        SectionHeaderController sectionHeaderController4 = this.incomingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout4 = this.parent;
        if (notificationStackScrollLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        sectionHeaderController4.reinflateView(notificationStackScrollLayout4);
        MediaHeaderView mediaHeaderView = (MediaHeaderView) reinflateView(this.mediaControlsView, layoutInflater, R$layout.keyguard_media_header);
        this.mediaControlsView = mediaHeaderView;
        this.keyguardMediaController.attachSinglePaneContainer(mediaHeaderView);
    }

    @Override // com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm.SectionProvider
    public boolean beginsSection(@NotNull View view, @Nullable View view2) {
        Intrinsics.checkNotNullParameter(view, "view");
        return view == getSilentHeaderView() || view == this.mediaControlsView || view == getPeopleHeaderView() || view == getAlertingHeaderView() || view == getIncomingHeaderView() || !Intrinsics.areEqual(getBucket(view), getBucket(view2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Integer getBucket(View view) {
        if (view == getSilentHeaderView()) {
            return 6;
        }
        if (view == getIncomingHeaderView()) {
            return 2;
        }
        if (view == this.mediaControlsView) {
            return 1;
        }
        if (view == getPeopleHeaderView()) {
            return 4;
        }
        if (view == getAlertingHeaderView()) {
            return 5;
        }
        if (!(view instanceof ExpandableNotificationRow)) {
            return null;
        }
        return Integer.valueOf(((ExpandableNotificationRow) view).getEntry().getBucket());
    }

    private final void logShadeChild(int i, View view) {
        if (view == getIncomingHeaderView()) {
            this.logger.logIncomingHeader(i);
        } else if (view == this.mediaControlsView) {
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
            } else if (bucket != 6) {
            } else {
                this.logger.logSilent(i, isHeadsUp);
            }
        }
    }

    private final void logShadeContents() {
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        int i = 0;
        for (View view : ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout)) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            logShadeChild(i, view);
            i = i2;
        }
    }

    private final boolean isUsingMultipleSections() {
        return this.sectionsFeatureManager.getNumberOfBuckets() > 1;
    }

    @VisibleForTesting
    public final void updateSectionBoundaries() {
        updateSectionBoundaries("test");
    }

    private final <T extends ExpandableView> SectionUpdateState<T> expandableViewHeaderState(final T t) {
        return (SectionUpdateState<T>) new SectionUpdateState<T>(this) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$expandableViewHeaderState$1
            @Nullable
            private Integer currentPosition;
            @NotNull
            private final ExpandableView header;
            @Nullable
            private Integer targetPosition;
            final /* synthetic */ NotificationSectionsManager this$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect types in method signature: (TT;Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager;)V */
            {
                this.this$0 = this;
                this.header = ExpandableView.this;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            @Nullable
            public Integer getCurrentPosition() {
                return this.currentPosition;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setCurrentPosition(@Nullable Integer num) {
                this.currentPosition = num;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            @Nullable
            public Integer getTargetPosition() {
                return this.targetPosition;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setTargetPosition(@Nullable Integer num) {
                this.targetPosition = num;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void adjustViewPosition() {
                NotificationStackScrollLayout notificationStackScrollLayout;
                NotificationStackScrollLayout notificationStackScrollLayout2;
                NotificationStackScrollLayout notificationStackScrollLayout3;
                Integer targetPosition = getTargetPosition();
                Integer currentPosition = getCurrentPosition();
                if (targetPosition == null) {
                    if (currentPosition == null) {
                        return;
                    }
                    notificationStackScrollLayout3 = this.this$0.parent;
                    if (notificationStackScrollLayout3 != null) {
                        notificationStackScrollLayout3.removeView(ExpandableView.this);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                } else if (currentPosition != null) {
                    notificationStackScrollLayout = this.this$0.parent;
                    if (notificationStackScrollLayout != null) {
                        notificationStackScrollLayout.changeViewPosition(ExpandableView.this, targetPosition.intValue());
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                } else {
                    ViewGroup transientContainer = ExpandableView.this.getTransientContainer();
                    if (transientContainer != null) {
                        transientContainer.removeTransientView(ExpandableView.this);
                    }
                    ExpandableView.this.setTransientContainer(null);
                    notificationStackScrollLayout2 = this.this$0.parent;
                    if (notificationStackScrollLayout2 != null) {
                        notificationStackScrollLayout2.addView(ExpandableView.this, targetPosition.intValue());
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                }
            }
        };
    }

    private final <T extends StackScrollerDecorView> SectionUpdateState<T> decorViewHeaderState(final T t) {
        final SectionUpdateState expandableViewHeaderState = expandableViewHeaderState(t);
        return (SectionUpdateState<T>) new SectionUpdateState<T>(t) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$decorViewHeaderState$1
            private final /* synthetic */ NotificationSectionsManager.SectionUpdateState<T> $$delegate_0;
            final /* synthetic */ StackScrollerDecorView $header;

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            @Nullable
            public Integer getCurrentPosition() {
                return this.$$delegate_0.getCurrentPosition();
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            @Nullable
            public Integer getTargetPosition() {
                return this.$$delegate_0.getTargetPosition();
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setCurrentPosition(@Nullable Integer num) {
                this.$$delegate_0.setCurrentPosition(num);
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setTargetPosition(@Nullable Integer num) {
                this.$$delegate_0.setTargetPosition(num);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect types in method signature: (Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState<+TT;>;TT;)V */
            {
                this.$header = t;
                this.$$delegate_0 = NotificationSectionsManager.SectionUpdateState.this;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void adjustViewPosition() {
                NotificationSectionsManager.SectionUpdateState.this.adjustViewPosition();
                if (getTargetPosition() == null || getCurrentPosition() != null) {
                    return;
                }
                this.$header.setContentVisible(true);
            }
        };
    }

    /* JADX WARN: Code restructure failed: missing block: B:142:0x0118, code lost:
        if ((r1.getVisibility() == 8) == false) goto L38;
     */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x015f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0176 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01bc A[LOOP:0: B:28:0x0096->B:67:0x01bc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01cd A[EDGE_INSN: B:68:0x01cd->B:69:0x01cd ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void updateSectionBoundaries(@NotNull String reason) {
        int i;
        Sequence sequence;
        int i2;
        SectionUpdateState sectionUpdateState;
        Integer targetPosition;
        Integer targetPosition2;
        Integer targetPosition3;
        Integer targetPosition4;
        List<SectionUpdateState> reversed;
        Integer targetPosition5;
        int i3;
        int i4;
        SectionUpdateState sectionUpdateState2;
        View view;
        Boolean valueOf;
        Intrinsics.checkNotNullParameter(reason, "reason");
        if (!isUsingMultipleSections()) {
            return;
        }
        this.logger.logStartSectionUpdate(reason);
        boolean z = this.statusBarStateController.getState() != 1;
        boolean isMediaControlsEnabled = this.sectionsFeatureManager.isMediaControlsEnabled();
        MediaHeaderView mediaHeaderView = this.mediaControlsView;
        SectionUpdateState expandableViewHeaderState = mediaHeaderView == null ? null : expandableViewHeaderState(mediaHeaderView);
        SectionHeaderView incomingHeaderView = getIncomingHeaderView();
        SectionUpdateState decorViewHeaderState = incomingHeaderView == null ? null : decorViewHeaderState(incomingHeaderView);
        SectionHeaderView peopleHeaderView = getPeopleHeaderView();
        SectionUpdateState decorViewHeaderState2 = peopleHeaderView == null ? null : decorViewHeaderState(peopleHeaderView);
        SectionHeaderView alertingHeaderView = getAlertingHeaderView();
        SectionUpdateState decorViewHeaderState3 = alertingHeaderView == null ? null : decorViewHeaderState(alertingHeaderView);
        SectionHeaderView silentHeaderView = getSilentHeaderView();
        SectionUpdateState decorViewHeaderState4 = silentHeaderView == null ? null : decorViewHeaderState(silentHeaderView);
        Sequence filterNotNull = SequencesKt.filterNotNull(SequencesKt.sequenceOf(expandableViewHeaderState, decorViewHeaderState, decorViewHeaderState2, decorViewHeaderState3, decorViewHeaderState4));
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        int childCount = notificationStackScrollLayout.getChildCount() - 1;
        if (-1 <= childCount) {
            int i5 = childCount;
            boolean z2 = false;
            boolean z3 = false;
            Integer num = null;
            while (true) {
                int i6 = i5 - 1;
                NotificationStackScrollLayout notificationStackScrollLayout2 = this.parent;
                if (notificationStackScrollLayout2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                    throw null;
                }
                View childAt = notificationStackScrollLayout2.getChildAt(i5);
                if (childAt == null) {
                    i3 = i6;
                    i4 = i5;
                    view = childAt;
                    sequence = filterNotNull;
                    sectionUpdateState2 = decorViewHeaderState4;
                } else {
                    logShadeChild(i5, childAt);
                    i3 = i6;
                    i4 = i5;
                    sequence = filterNotNull;
                    sectionUpdateState2 = decorViewHeaderState4;
                    SectionUpdateState<ExpandableView> updateSectionBoundaries$getSectionState = updateSectionBoundaries$getSectionState(this, expandableViewHeaderState, decorViewHeaderState, decorViewHeaderState2, decorViewHeaderState3, decorViewHeaderState4, childAt);
                    if (updateSectionBoundaries$getSectionState != null) {
                        updateSectionBoundaries$getSectionState.setCurrentPosition(Integer.valueOf(i4));
                        for (SectionUpdateState sectionUpdateState3 : ConvenienceExtensionsKt.takeUntil(sequence, new NotificationSectionsManager$updateSectionBoundaries$1$1$1(updateSectionBoundaries$getSectionState))) {
                            Integer targetPosition6 = sectionUpdateState3.getTargetPosition();
                            sectionUpdateState3.setTargetPosition(targetPosition6 == null ? null : Integer.valueOf(targetPosition6.intValue() - 1));
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    view = childAt;
                }
                ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
                if (expandableNotificationRow != null) {
                }
                expandableNotificationRow = null;
                if (!z2) {
                    if (num != null) {
                        int intValue = num.intValue();
                        NotificationEntry entry = expandableNotificationRow == null ? null : expandableNotificationRow.getEntry();
                        if (entry != null) {
                            valueOf = Boolean.valueOf(intValue < entry.getBucket());
                            if (!Intrinsics.areEqual(valueOf, Boolean.TRUE)) {
                                z2 = false;
                                if (z2) {
                                    NotificationEntry entry2 = expandableNotificationRow == null ? null : expandableNotificationRow.getEntry();
                                    if (entry2 != null) {
                                        i2 = 2;
                                        entry2.setBucket(2);
                                        if ((num == null && (view == null || !(expandableNotificationRow == null || num.intValue() == expandableNotificationRow.getEntry().getBucket()))) || !z || num == null || num.intValue() != 6) {
                                            sectionUpdateState = sectionUpdateState2;
                                        } else {
                                            sectionUpdateState = sectionUpdateState2;
                                            if (sectionUpdateState != null) {
                                                sectionUpdateState.setTargetPosition(Integer.valueOf(i4 + 1));
                                            }
                                        }
                                        if (expandableNotificationRow == null) {
                                            i = -1;
                                        } else {
                                            if (!z3 && expandableNotificationRow.getEntry().getBucket() != 4) {
                                                z3 = false;
                                                num = Integer.valueOf(expandableNotificationRow.getEntry().getBucket());
                                                i = -1;
                                            }
                                            z3 = true;
                                            num = Integer.valueOf(expandableNotificationRow.getEntry().getBucket());
                                            i = -1;
                                        }
                                        if (i > i3) {
                                            break;
                                        }
                                        decorViewHeaderState4 = sectionUpdateState;
                                        i5 = i3;
                                        filterNotNull = sequence;
                                    }
                                }
                                i2 = 2;
                                if (num == null && (view == null || !(expandableNotificationRow == null || num.intValue() == expandableNotificationRow.getEntry().getBucket()))) {
                                }
                                sectionUpdateState = sectionUpdateState2;
                                if (expandableNotificationRow == null) {
                                }
                                if (i > i3) {
                                }
                            }
                        }
                    }
                    valueOf = null;
                    if (!Intrinsics.areEqual(valueOf, Boolean.TRUE)) {
                    }
                }
                z2 = true;
                if (z2) {
                }
                i2 = 2;
                if (num == null && (view == null || !(expandableNotificationRow == null || num.intValue() == expandableNotificationRow.getEntry().getBucket()))) {
                }
                sectionUpdateState = sectionUpdateState2;
                if (expandableNotificationRow == null) {
                }
                if (i > i3) {
                }
            }
        } else {
            i = -1;
            sequence = filterNotNull;
            i2 = 2;
            sectionUpdateState = decorViewHeaderState4;
        }
        if (expandableViewHeaderState != null) {
            expandableViewHeaderState.setTargetPosition(isMediaControlsEnabled ? 0 : null);
        }
        this.logger.logStr("New header target positions:");
        this.logger.logMediaControls((expandableViewHeaderState == null || (targetPosition = expandableViewHeaderState.getTargetPosition()) == null) ? i : targetPosition.intValue());
        this.logger.logIncomingHeader((decorViewHeaderState == null || (targetPosition2 = decorViewHeaderState.getTargetPosition()) == null) ? i : targetPosition2.intValue());
        this.logger.logConversationsHeader((decorViewHeaderState2 == null || (targetPosition3 = decorViewHeaderState2.getTargetPosition()) == null) ? i : targetPosition3.intValue());
        this.logger.logAlertingHeader((decorViewHeaderState3 == null || (targetPosition4 = decorViewHeaderState3.getTargetPosition()) == null) ? i : targetPosition4.intValue());
        NotificationSectionsLogger notificationSectionsLogger = this.logger;
        if (sectionUpdateState != null && (targetPosition5 = sectionUpdateState.getTargetPosition()) != null) {
            i = targetPosition5.intValue();
        }
        notificationSectionsLogger.logSilentHeader(i);
        reversed = CollectionsKt___CollectionsKt.reversed(SequencesKt.asIterable(sequence));
        for (SectionUpdateState sectionUpdateState4 : reversed) {
            sectionUpdateState4.adjustViewPosition();
        }
        this.logger.logStr("Final order:");
        logShadeContents();
        this.logger.logStr("Section boundary update complete");
        SectionHeaderView silentHeaderView2 = getSilentHeaderView();
        if (silentHeaderView2 == null) {
            return;
        }
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
        if (notificationStackScrollLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        silentHeaderView2.setAreThereDismissableGentleNotifs(notificationStackScrollLayout3.hasActiveClearableNotifications(i2));
        Unit unit2 = Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final SectionUpdateState<ExpandableView> updateSectionBoundaries$getSectionState(NotificationSectionsManager notificationSectionsManager, SectionUpdateState<? extends MediaHeaderView> sectionUpdateState, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState2, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState3, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState4, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState5, View view) {
        if (view == notificationSectionsManager.mediaControlsView) {
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
        if (view != notificationSectionsManager.getSilentHeaderView()) {
            return null;
        }
        return sectionUpdateState5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public static abstract class SectionBounds {
        public /* synthetic */ SectionBounds(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private SectionBounds() {
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class Many extends SectionBounds {
            @NotNull
            private final ExpandableView first;
            @NotNull
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

            @NotNull
            public final Many copy(@NotNull ExpandableView first, @NotNull ExpandableView last) {
                Intrinsics.checkNotNullParameter(first, "first");
                Intrinsics.checkNotNullParameter(last, "last");
                return new Many(first, last);
            }

            public boolean equals(@Nullable Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Many)) {
                    return false;
                }
                Many many = (Many) obj;
                return Intrinsics.areEqual(this.first, many.first) && Intrinsics.areEqual(this.last, many.last);
            }

            public int hashCode() {
                return (this.first.hashCode() * 31) + this.last.hashCode();
            }

            @NotNull
            public String toString() {
                return "Many(first=" + this.first + ", last=" + this.last + ')';
            }

            @NotNull
            public final ExpandableView getFirst() {
                return this.first;
            }

            @NotNull
            public final ExpandableView getLast() {
                return this.last;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Many(@NotNull ExpandableView first, @NotNull ExpandableView last) {
                super(null);
                Intrinsics.checkNotNullParameter(first, "first");
                Intrinsics.checkNotNullParameter(last, "last");
                this.first = first;
                this.last = last;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class One extends SectionBounds {
            @NotNull
            private final ExpandableView lone;

            public boolean equals(@Nullable Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof One) && Intrinsics.areEqual(this.lone, ((One) obj).lone);
            }

            public int hashCode() {
                return this.lone.hashCode();
            }

            @NotNull
            public String toString() {
                return "One(lone=" + this.lone + ')';
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public One(@NotNull ExpandableView lone) {
                super(null);
                Intrinsics.checkNotNullParameter(lone, "lone");
                this.lone = lone;
            }

            @NotNull
            public final ExpandableView getLone() {
                return this.lone;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class None extends SectionBounds {
            @NotNull
            public static final None INSTANCE = new None();

            private None() {
                super(null);
            }
        }

        @NotNull
        public final SectionBounds addNotif(@NotNull ExpandableView notif) {
            Intrinsics.checkNotNullParameter(notif, "notif");
            if (this instanceof None) {
                return new One(notif);
            }
            if (this instanceof One) {
                return new Many(((One) this).getLone(), notif);
            }
            if (!(this instanceof Many)) {
                throw new NoWhenBranchMatchedException();
            }
            return Many.copy$default((Many) this, null, notif, 1, null);
        }

        public final boolean updateSection(@NotNull NotificationSection section) {
            Intrinsics.checkNotNullParameter(section, "section");
            if (this instanceof None) {
                return setFirstAndLastVisibleChildren(section, null, null);
            }
            if (this instanceof One) {
                One one = (One) this;
                return setFirstAndLastVisibleChildren(section, one.getLone(), one.getLone());
            } else if (!(this instanceof Many)) {
                throw new NoWhenBranchMatchedException();
            } else {
                Many many = (Many) this;
                return setFirstAndLastVisibleChildren(section, many.getFirst(), many.getLast());
            }
        }

        private final boolean setFirstAndLastVisibleChildren(NotificationSection notificationSection, ExpandableView expandableView, ExpandableView expandableView2) {
            return notificationSection.setFirstVisibleChild(expandableView) || notificationSection.setLastVisibleChild(expandableView2);
        }
    }

    public final boolean updateFirstAndLastViewsForAllSections(@NotNull NotificationSection[] sections, @NotNull List<? extends ExpandableView> children) {
        final Sequence asSequence;
        SparseArray sparseArray;
        Intrinsics.checkNotNullParameter(sections, "sections");
        Intrinsics.checkNotNullParameter(children, "children");
        asSequence = CollectionsKt___CollectionsKt.asSequence(children);
        Grouping<ExpandableView, Integer> grouping = new Grouping<ExpandableView, Integer>() { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1
            @Override // kotlin.collections.Grouping
            @NotNull
            public Iterator<ExpandableView> sourceIterator() {
                return Sequence.this.iterator();
            }

            @Override // kotlin.collections.Grouping
            public Integer keyOf(ExpandableView expandableView) {
                Integer bucket;
                bucket = this.getBucket(expandableView);
                if (bucket != null) {
                    return Integer.valueOf(bucket.intValue());
                }
                throw new IllegalArgumentException("Cannot find section bucket for view");
            }
        };
        SectionBounds.None none = SectionBounds.None.INSTANCE;
        int length = sections.length;
        if (length < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(length);
        }
        Iterator<ExpandableView> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            ExpandableView next = sourceIterator.next();
            int intValue = grouping.keyOf(next).intValue();
            Object obj = sparseArray.get(intValue);
            if (obj == null) {
                obj = none;
            }
            sparseArray.put(intValue, ((SectionBounds) obj).addNotif(next));
        }
        boolean z = false;
        for (NotificationSection notificationSection : sections) {
            SectionBounds sectionBounds = (SectionBounds) sparseArray.get(notificationSection.getBucket());
            if (sectionBounds == null) {
                sectionBounds = SectionBounds.None.INSTANCE;
            }
            z = sectionBounds.updateSection(notificationSection) || z;
        }
        return z;
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
        if (alertingHeaderView == null) {
            return;
        }
        alertingHeaderView.setForegroundColor(i);
    }

    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
