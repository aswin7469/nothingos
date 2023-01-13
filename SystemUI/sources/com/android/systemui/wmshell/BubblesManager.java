package com.android.systemui.wmshell;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.p019wm.shell.bubbles.Bubble;
import com.android.p019wm.shell.bubbles.BubbleEntry;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationChannelHelper;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@SysUISingleton
public class BubblesManager implements Dumpable {
    private static final String TAG = "Bubbles";
    private final IStatusBarService mBarService;
    /* access modifiers changed from: private */
    public final Bubbles mBubbles;
    /* access modifiers changed from: private */
    public final List<NotifCallback> mCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public final CommonNotifCollection mCommonNotifCollection;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final NotifPipeline mNotifPipeline;
    /* access modifiers changed from: private */
    public final NotificationLockscreenUserManager mNotifUserManager;
    /* access modifiers changed from: private */
    public final NotificationEntryManager mNotificationEntryManager;
    /* access modifiers changed from: private */
    public final NotificationGroupManagerLegacy mNotificationGroupManager;
    /* access modifiers changed from: private */
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final INotificationManager mNotificationManager;
    /* access modifiers changed from: private */
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    private final ShadeController mShadeController;
    /* access modifiers changed from: private */
    public final Executor mSysuiMainExecutor;
    private final Bubbles.SysuiProxy mSysuiProxy;
    private final NotificationVisibilityProvider mVisibilityProvider;

    public interface NotifCallback {
        void invalidateNotifications(String str);

        void maybeCancelSummary(NotificationEntry notificationEntry);

        void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i);
    }

    public static BubblesManager create(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, Executor executor) {
        if (!optional.isPresent()) {
            return null;
        }
        return new BubblesManager(context, optional.get(), notificationShadeWindowController, keyguardStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationVisibilityProvider, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, commonNotifCollection, notifPipeline, sysUiState, notifPipelineFlags, dumpManager, executor);
    }

    BubblesManager(Context context, final Bubbles bubbles, NotificationShadeWindowController notificationShadeWindowController, final KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, Executor executor) {
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        NotificationLockscreenUserManager notificationLockscreenUserManager2 = notificationLockscreenUserManager;
        final Executor executor2 = executor;
        this.mContext = context;
        this.mBubbles = bubbles;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mShadeController = shadeController;
        this.mNotificationManager = iNotificationManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mNotifUserManager = notificationLockscreenUserManager2;
        this.mNotificationGroupManager = notificationGroupManagerLegacy;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mCommonNotifCollection = commonNotifCollection;
        this.mNotifPipeline = notifPipeline;
        this.mSysuiMainExecutor = executor2;
        this.mBarService = iStatusBarService == null ? IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar")) : iStatusBarService;
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            setupNotifPipeline();
        } else {
            setupNEM();
        }
        dumpManager.registerDumpable("Bubbles", this);
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardShowingChanged() {
                bubbles.onStatusBarStateChanged(!keyguardStateController.isShowing() && !keyguardStateController.isOccluded());
            }
        });
        ConfigurationController configurationController2 = configurationController;
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                BubblesManager.this.mBubbles.onConfigChanged(configuration);
            }

            public void onUiModeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }

            public void onThemeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }
        });
        zenModeController.addCallback(new ZenModeController.Callback() {
            public void onZenChanged(int i) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }

            public void onConfigChanged(ZenModeConfig zenModeConfig) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }
        });
        notificationLockscreenUserManager2.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener() {
            public void onUserChanged(int i) {
                BubblesManager.this.mBubbles.onUserChanged(i);
            }

            public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
                BubblesManager.this.mBubbles.onCurrentProfilesChanged(sparseArray);
            }

            public void onUserRemoved(int i) {
                BubblesManager.this.mBubbles.onUserRemoved(i);
            }
        });
        final SysUiState sysUiState2 = sysUiState;
        C33235 r2 = new Bubbles.SysuiProxy() {
            public void isNotificationShadeExpand(Consumer<Boolean> consumer) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda10(this, consumer));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$isNotificationShadeExpand$0$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47539x44d3fe27(Consumer consumer) {
                consumer.accept(Boolean.valueOf(BubblesManager.this.mNotificationShadeWindowController.getPanelExpanded()));
            }

            public void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda6(this, str, consumer));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$getPendingOrActiveEntry$1$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47537x36666bbe(String str, Consumer consumer) {
                BubbleEntry bubbleEntry;
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry == null) {
                    bubbleEntry = null;
                } else {
                    bubbleEntry = BubblesManager.notifToBubbleEntry(entry);
                }
                consumer.accept(bubbleEntry);
            }

            public void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda9(this, arraySet, consumer));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$getShouldRestoredEntries$2$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47538x236e7d02(ArraySet arraySet, Consumer consumer) {
                ArrayList arrayList = new ArrayList();
                for (NotificationEntry next : BubblesManager.this.mCommonNotifCollection.getAllNotifs()) {
                    if (BubblesManager.this.mNotifUserManager.isCurrentProfile(next.getSbn().getUserId()) && arraySet.contains(next.getKey()) && BubblesManager.this.mNotificationInterruptStateProvider.shouldBubbleUp(next) && next.isBubble()) {
                        arrayList.add(BubblesManager.notifToBubbleEntry(next));
                    }
                }
                consumer.accept(arrayList);
            }

            public void setNotificationInterruption(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda5(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$setNotificationInterruption$3$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47548xc680b14a(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null && entry.getImportance() >= 4) {
                    entry.setInterruption();
                }
            }

            public void requestNotificationShadeTopUi(boolean z, String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda2(this, z, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$requestNotificationShadeTopUi$4$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47547x9733de4f(boolean z, String str) {
                BubblesManager.this.mNotificationShadeWindowController.setRequestTopUi(z, str);
            }

            public void notifyRemoveNotification(String str, int i) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda1(this, str, i));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$notifyRemoveNotification$5$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47542xe8f2c84c(String str, int i) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    for (NotifCallback removeNotification : BubblesManager.this.mCallbacks) {
                        removeNotification.removeNotification(entry, BubblesManager.this.getDismissedByUserStats(entry, true), i);
                    }
                }
            }

            public void notifyInvalidateNotifications(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda7(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$notifyInvalidateNotifications$6$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47540xd424cb5f(String str) {
                for (NotifCallback invalidateNotifications : BubblesManager.this.mCallbacks) {
                    invalidateNotifications.invalidateNotifications(str);
                }
            }

            public void notifyMaybeCancelSummary(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda0(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$notifyMaybeCancelSummary$7$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47541x4d542783(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    for (NotifCallback maybeCancelSummary : BubblesManager.this.mCallbacks) {
                        maybeCancelSummary.maybeCancelSummary(entry);
                    }
                }
            }

            public void removeNotificationEntry(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda13(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$removeNotificationEntry$8$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47546xfbf16460(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.mNotificationGroupManager.onEntryRemoved(entry);
                }
            }

            public void updateNotificationBubbleButton(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda11(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$updateNotificationBubbleButton$9$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47549x23fe562a(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null && entry.getRow() != null) {
                    entry.getRow().updateBubbleButton();
                }
            }

            public void updateNotificationSuppression(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda4(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$updateNotificationSuppression$10$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47550xedeae2a7(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.mNotificationGroupManager.updateSuppression(entry);
                }
            }

            public void onStackExpandChanged(boolean z) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda3(this, sysUiState2, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStackExpandChanged$11$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47544xb1dbe87c(SysUiState sysUiState, boolean z) {
                sysUiState.setFlag(16384, z).commitUpdate(BubblesManager.this.mContext.getDisplayId());
                if (!z) {
                    sysUiState.setFlag(8388608, false).commitUpdate(BubblesManager.this.mContext.getDisplayId());
                }
            }

            public void onManageMenuExpandChanged(boolean z) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda8(this, sysUiState2, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onManageMenuExpandChanged$12$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47543xf713ad11(SysUiState sysUiState, boolean z) {
                sysUiState.setFlag(8388608, z).commitUpdate(BubblesManager.this.mContext.getDisplayId());
            }

            public void onUnbubbleConversation(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda12(this, str));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onUnbubbleConversation$13$com-android-systemui-wmshell-BubblesManager$5 */
            public /* synthetic */ void mo47545x1a7cee48(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.onUserChangedBubble(entry, false);
                }
            }
        };
        this.mSysuiProxy = r2;
        bubbles.setSysuiProxy(r2);
    }

    private void setupNEM() {
        this.mNotificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                BubblesManager.this.onEntryRemoved(notificationEntry);
            }

            public void onNotificationRankingUpdated(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }

            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                BubblesManager.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
        });
        this.mNotificationEntryManager.addNotificationRemoveInterceptor(new BubblesManager$$ExternalSyntheticLambda0(this));
        this.mNotificationGroupManager.registerGroupChangeListener(new NotificationGroupManagerLegacy.OnGroupChangeListener() {
            public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
                String groupKey = notificationGroup.summary != null ? notificationGroup.summary.getSbn().getGroupKey() : null;
                if (!z && groupKey != null) {
                    BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(groupKey, (Consumer<String>) null, (Executor) null);
                }
            }
        });
        addNotifCallback(new NotifCallback() {
            public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
                BubblesManager.this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), dismissedByUserStats, i);
            }

            public void invalidateNotifications(String str) {
                BubblesManager.this.mNotificationEntryManager.updateNotifications(str);
            }

            public void maybeCancelSummary(NotificationEntry notificationEntry) {
                BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(notificationEntry.getSbn().getGroupKey(), new BubblesManager$8$$ExternalSyntheticLambda0(this), BubblesManager.this.mSysuiMainExecutor);
                NotificationEntry logicalGroupSummary = BubblesManager.this.mNotificationGroupManager.getLogicalGroupSummary(notificationEntry);
                if (logicalGroupSummary != null) {
                    ArrayList<NotificationEntry> logicalChildren = BubblesManager.this.mNotificationGroupManager.getLogicalChildren(logicalGroupSummary.getSbn());
                    if (logicalGroupSummary.getKey().equals(notificationEntry.getKey())) {
                        return;
                    }
                    if (logicalChildren == null || logicalChildren.isEmpty()) {
                        BubblesManager.this.mNotificationEntryManager.performRemoveNotification(logicalGroupSummary.getSbn(), BubblesManager.this.getDismissedByUserStats(logicalGroupSummary, false), 0);
                    }
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$maybeCancelSummary$0$com-android-systemui-wmshell-BubblesManager$8 */
            public /* synthetic */ void mo47562xe0003056(String str) {
                NotificationEntry activeNotificationUnfiltered = BubblesManager.this.mNotificationEntryManager.getActiveNotificationUnfiltered(str);
                if (activeNotificationUnfiltered != null) {
                    BubblesManager.this.mNotificationEntryManager.performRemoveNotification(activeNotificationUnfiltered.getSbn(), BubblesManager.this.getDismissedByUserStats(activeNotificationUnfiltered, false), 0);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupNEM$0$com-android-systemui-wmshell-BubblesManager  reason: not valid java name */
    public /* synthetic */ boolean m3329lambda$setupNEM$0$comandroidsystemuiwmshellBubblesManager(String str, NotificationEntry notificationEntry, int i) {
        boolean z = true;
        boolean z2 = i == 3;
        boolean z3 = i == 2 || i == 1;
        boolean z4 = i == 8 || i == 9;
        boolean z5 = i == 12;
        if ((notificationEntry == null || !notificationEntry.isRowDismissed() || z4) && !z2 && !z3 && !z5) {
            z = false;
        }
        if (z) {
            return handleDismissalInterception(notificationEntry);
        }
        return false;
    }

    private void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
            public void onEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            public void onEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                if (i == 8 || i == 9) {
                    BubblesManager.this.onEntryRemoved(notificationEntry);
                }
            }

            public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }

            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                BubblesManager.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onEntryAdded(NotificationEntry notificationEntry) {
        if (this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry) && notificationEntry.isBubble()) {
            this.mBubbles.onEntryAdded(notifToBubbleEntry(notificationEntry));
        }
    }

    /* access modifiers changed from: package-private */
    public void onEntryUpdated(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryUpdated(notifToBubbleEntry(notificationEntry), this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry));
    }

    /* access modifiers changed from: package-private */
    public void onEntryRemoved(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryRemoved(notifToBubbleEntry(notificationEntry));
    }

    /* access modifiers changed from: package-private */
    public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        String[] orderedKeys = rankingMap.getOrderedKeys();
        HashMap hashMap = new HashMap();
        for (String str : orderedKeys) {
            NotificationEntry entry = this.mCommonNotifCollection.getEntry(str);
            hashMap.put(str, new Pair(entry != null ? notifToBubbleEntry(entry) : null, Boolean.valueOf(entry != null ? this.mNotificationInterruptStateProvider.shouldBubbleUp(entry) : false)));
        }
        this.mBubbles.onRankingUpdated(rankingMap, hashMap);
    }

    /* access modifiers changed from: package-private */
    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        this.mBubbles.onNotificationChannelModified(str, userHandle, notificationChannel, i);
    }

    /* access modifiers changed from: private */
    public DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry, boolean z) {
        return new DismissedByUserStats(3, 1, this.mVisibilityProvider.obtain(notificationEntry, z));
    }

    public boolean handleDismissalInterception(NotificationEntry notificationEntry) {
        ArrayList arrayList;
        if (notificationEntry == null) {
            return false;
        }
        List<NotificationEntry> attachedNotifChildren = notificationEntry.getAttachedNotifChildren();
        if (attachedNotifChildren != null) {
            arrayList = new ArrayList();
            for (int i = 0; i < attachedNotifChildren.size(); i++) {
                arrayList.add(notifToBubbleEntry(attachedNotifChildren.get(i)));
            }
        } else {
            arrayList = null;
        }
        return this.mBubbles.handleDismissalInterception(notifToBubbleEntry(notificationEntry), arrayList, new BubblesManager$$ExternalSyntheticLambda1(this, attachedNotifChildren, notificationEntry), this.mSysuiMainExecutor);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleDismissalInterception$1$com-android-systemui-wmshell-BubblesManager */
    public /* synthetic */ void mo47525x200aa1f8(List list, NotificationEntry notificationEntry, int i) {
        if (i >= 0) {
            for (NotifCallback removeNotification : this.mCallbacks) {
                removeNotification.removeNotification((NotificationEntry) list.get(i), getDismissedByUserStats((NotificationEntry) list.get(i), true), 12);
            }
            return;
        }
        this.mNotificationGroupManager.onEntryRemoved(notificationEntry);
    }

    public void expandStackAndSelectBubble(NotificationEntry notificationEntry) {
        this.mBubbles.expandStackAndSelectBubble(notifToBubbleEntry(notificationEntry));
    }

    public void expandStackAndSelectBubble(Bubble bubble) {
        this.mBubbles.expandStackAndSelectBubble(bubble);
    }

    public Bubble getBubbleWithShortcutId(String str) {
        return this.mBubbles.getBubbleWithShortcutId(str);
    }

    public void addNotifCallback(NotifCallback notifCallback) {
        this.mCallbacks.add(notifCallback);
    }

    public void onUserSetImportantConversation(NotificationEntry notificationEntry) {
        if (notificationEntry.getBubbleMetadata() != null) {
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), true, 2);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            this.mShadeController.collapsePanel(true);
            if (notificationEntry.getRow() != null) {
                notificationEntry.getRow().updateBubbleButton();
            }
        }
    }

    public void onUserChangedBubble(NotificationEntry notificationEntry, boolean z) {
        NotificationChannel channel = notificationEntry.getChannel();
        String packageName = notificationEntry.getSbn().getPackageName();
        int uid = notificationEntry.getSbn().getUid();
        if (channel != null && packageName != null) {
            notificationEntry.setFlagBubble(z);
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), z, 3);
            } catch (RemoteException unused) {
            }
            NotificationChannel createConversationChannelIfNeeded = NotificationChannelHelper.createConversationChannelIfNeeded(this.mContext, this.mNotificationManager, notificationEntry, channel);
            createConversationChannelIfNeeded.setAllowBubbles(z);
            try {
                int bubblePreferenceForPackage = this.mNotificationManager.getBubblePreferenceForPackage(packageName, uid);
                if (z && bubblePreferenceForPackage == 0) {
                    this.mNotificationManager.setBubblesAllowed(packageName, uid, 2);
                }
                this.mNotificationManager.updateNotificationChannelForPackage(packageName, uid, createConversationChannelIfNeeded);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            if (z) {
                this.mShadeController.collapsePanel(true);
                if (notificationEntry.getRow() != null) {
                    notificationEntry.getRow().updateBubbleButton();
                }
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mBubbles.dump(printWriter, strArr);
    }

    public static boolean areBubblesEnabled(Context context, UserHandle userHandle) {
        if (userHandle.getIdentifier() < 0) {
            if (Settings.Secure.getInt(context.getContentResolver(), "notification_bubbles", 0) == 1) {
                return true;
            }
            return false;
        } else if (Settings.Secure.getIntForUser(context.getContentResolver(), "notification_bubbles", 0, userHandle.getIdentifier()) == 1) {
            return true;
        } else {
            return false;
        }
    }

    static BubbleEntry notifToBubbleEntry(NotificationEntry notificationEntry) {
        return new BubbleEntry(notificationEntry.getSbn(), notificationEntry.getRanking(), notificationEntry.isDismissable(), notificationEntry.shouldSuppressNotificationDot(), notificationEntry.shouldSuppressNotificationList(), notificationEntry.shouldSuppressPeek());
    }
}
