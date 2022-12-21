package com.android.systemui.statusbar.notification.row;

import android.app.Notification;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.CancellationSignal;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.android.internal.widget.ImageMessageConsumer;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.InflationTask;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder;
import com.android.systemui.statusbar.policy.SmartReplyStateInflater;
import com.android.systemui.util.Assert;
import java.util.HashMap;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class NotificationContentInflater implements NotificationRowContentBinder {
    public static final String TAG = "NotifContentInflater";
    private final Executor mBgExecutor;
    private final ConversationNotificationProcessor mConversationProcessor;
    private boolean mInflateSynchronously = false;
    private final boolean mIsMediaInQS;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private final NotifRemoteViewCache mRemoteViewCache;
    private final SmartReplyStateInflater mSmartReplyStateInflater;

    @Inject
    NotificationContentInflater(NotifRemoteViewCache notifRemoteViewCache, NotificationRemoteInputManager notificationRemoteInputManager, ConversationNotificationProcessor conversationNotificationProcessor, MediaFeatureFlag mediaFeatureFlag, @Background Executor executor, SmartReplyStateInflater smartReplyStateInflater) {
        this.mRemoteViewCache = notifRemoteViewCache;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mConversationProcessor = conversationNotificationProcessor;
        this.mIsMediaInQS = mediaFeatureFlag.getEnabled();
        this.mBgExecutor = executor;
        this.mSmartReplyStateInflater = smartReplyStateInflater;
    }

    public void bindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i, NotificationRowContentBinder.BindParams bindParams, boolean z, NotificationRowContentBinder.InflationCallback inflationCallback) {
        NotificationRowContentBinder.BindParams bindParams2 = bindParams;
        if (!expandableNotificationRow.isRemoved()) {
            expandableNotificationRow.getImageResolver().preloadImages(notificationEntry.getSbn().getNotification());
            if (z) {
                this.mRemoteViewCache.clearCache(notificationEntry);
            } else {
                NotificationEntry notificationEntry2 = notificationEntry;
            }
            cancelContentViewFrees(expandableNotificationRow, i);
            Executor executor = this.mBgExecutor;
            boolean z2 = this.mInflateSynchronously;
            NotifRemoteViewCache notifRemoteViewCache = this.mRemoteViewCache;
            ConversationNotificationProcessor conversationNotificationProcessor = this.mConversationProcessor;
            boolean z3 = bindParams2.isLowPriority;
            boolean z4 = bindParams2.usesIncreasedHeight;
            boolean z5 = bindParams2.usesIncreasedHeadsUpHeight;
            RemoteViews.InteractionHandler remoteViewsOnClickHandler = this.mRemoteInputManager.getRemoteViewsOnClickHandler();
            boolean z6 = this.mIsMediaInQS;
            boolean z7 = z6;
            AsyncInflationTask asyncInflationTask = r3;
            AsyncInflationTask asyncInflationTask2 = new AsyncInflationTask(executor, z2, i, notifRemoteViewCache, notificationEntry, conversationNotificationProcessor, expandableNotificationRow, z3, z4, z5, inflationCallback, remoteViewsOnClickHandler, z7, this.mSmartReplyStateInflater);
            if (this.mInflateSynchronously) {
                AsyncInflationTask asyncInflationTask3 = asyncInflationTask;
                asyncInflationTask3.onPostExecute(asyncInflationTask3.doInBackground(new Void[0]));
                return;
            }
            asyncInflationTask.executeOnExecutor(this.mBgExecutor, new Void[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public InflationProgress inflateNotificationViews(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, NotificationRowContentBinder.BindParams bindParams, boolean z, int i, Notification.Builder builder, Context context, SmartReplyStateInflater smartReplyStateInflater) {
        NotificationRowContentBinder.BindParams bindParams2 = bindParams;
        int i2 = i;
        NotificationEntry notificationEntry2 = notificationEntry;
        Context context2 = context;
        InflationProgress inflateSmartReplyViews = inflateSmartReplyViews(createRemoteViews(i, builder, bindParams2.isLowPriority, bindParams2.usesIncreasedHeight, bindParams2.usesIncreasedHeadsUpHeight, context), i2, notificationEntry2, expandableNotificationRow.getContext(), context2, expandableNotificationRow.getExistingSmartReplyState(), smartReplyStateInflater);
        apply(this.mBgExecutor, z, inflateSmartReplyViews, i, this.mRemoteViewCache, notificationEntry, expandableNotificationRow, this.mRemoteInputManager.getRemoteViewsOnClickHandler(), (NotificationRowContentBinder.InflationCallback) null);
        return inflateSmartReplyViews;
    }

    public void cancelBind(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        notificationEntry.abortTask();
    }

    public void unbindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i) {
        int i2 = 1;
        while (i != 0) {
            if ((i & i2) != 0) {
                freeNotificationView(notificationEntry, expandableNotificationRow, i2);
            }
            i &= ~i2;
            i2 <<= 1;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$freeNotificationView$0$com-android-systemui-statusbar-notification-row-NotificationContentInflater */
    public /* synthetic */ void mo41417xc53fc971(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        expandableNotificationRow.getPrivateLayout().setContractedChild((View) null);
        this.mRemoteViewCache.removeCachedView(notificationEntry, 1);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$freeNotificationView$1$com-android-systemui-statusbar-notification-row-NotificationContentInflater */
    public /* synthetic */ void mo41418xc60e47f2(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        expandableNotificationRow.getPrivateLayout().setExpandedChild((View) null);
        this.mRemoteViewCache.removeCachedView(notificationEntry, 2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$freeNotificationView$2$com-android-systemui-statusbar-notification-row-NotificationContentInflater */
    public /* synthetic */ void mo41419xc6dcc673(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        expandableNotificationRow.getPrivateLayout().setHeadsUpChild((View) null);
        this.mRemoteViewCache.removeCachedView(notificationEntry, 4);
        expandableNotificationRow.getPrivateLayout().setHeadsUpInflatedSmartReplies((InflatedSmartReplyViewHolder) null);
    }

    private void freeNotificationView(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i) {
        if (i == 1) {
            expandableNotificationRow.getPrivateLayout().performWhenContentInactive(0, new NotificationContentInflater$$ExternalSyntheticLambda1(this, expandableNotificationRow, notificationEntry));
        } else if (i == 2) {
            expandableNotificationRow.getPrivateLayout().performWhenContentInactive(1, new NotificationContentInflater$$ExternalSyntheticLambda2(this, expandableNotificationRow, notificationEntry));
        } else if (i == 4) {
            expandableNotificationRow.getPrivateLayout().performWhenContentInactive(2, new NotificationContentInflater$$ExternalSyntheticLambda3(this, expandableNotificationRow, notificationEntry));
        } else if (i == 8) {
            expandableNotificationRow.getPublicLayout().performWhenContentInactive(0, new NotificationContentInflater$$ExternalSyntheticLambda4(this, expandableNotificationRow, notificationEntry));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$freeNotificationView$3$com-android-systemui-statusbar-notification-row-NotificationContentInflater */
    public /* synthetic */ void mo41420xc7ab44f4(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        expandableNotificationRow.getPublicLayout().setContractedChild((View) null);
        this.mRemoteViewCache.removeCachedView(notificationEntry, 8);
    }

    private void cancelContentViewFrees(ExpandableNotificationRow expandableNotificationRow, int i) {
        if ((i & 1) != 0) {
            expandableNotificationRow.getPrivateLayout().removeContentInactiveRunnable(0);
        }
        if ((i & 2) != 0) {
            expandableNotificationRow.getPrivateLayout().removeContentInactiveRunnable(1);
        }
        if ((i & 4) != 0) {
            expandableNotificationRow.getPrivateLayout().removeContentInactiveRunnable(2);
        }
        if ((i & 8) != 0) {
            expandableNotificationRow.getPublicLayout().removeContentInactiveRunnable(0);
        }
    }

    /* access modifiers changed from: private */
    public static InflationProgress inflateSmartReplyViews(InflationProgress inflationProgress, int i, NotificationEntry notificationEntry, Context context, Context context2, InflatedSmartReplyState inflatedSmartReplyState, SmartReplyStateInflater smartReplyStateInflater) {
        InflationProgress inflationProgress2 = inflationProgress;
        boolean z = false;
        boolean z2 = ((i & 1) == 0 || inflationProgress.newContentView == null) ? false : true;
        boolean z3 = ((i & 2) == 0 || inflationProgress.newExpandedView == null) ? false : true;
        if (!((i & 4) == 0 || inflationProgress.newHeadsUpView == null)) {
            z = true;
        }
        if (z2 || z3 || z) {
            NotificationEntry notificationEntry2 = notificationEntry;
            InflatedSmartReplyState unused = inflationProgress.inflatedSmartReplyState = smartReplyStateInflater.inflateSmartReplyState(notificationEntry);
        } else {
            NotificationEntry notificationEntry3 = notificationEntry;
            SmartReplyStateInflater smartReplyStateInflater2 = smartReplyStateInflater;
        }
        if (z3) {
            InflatedSmartReplyViewHolder unused2 = inflationProgress.expandedInflatedSmartReplies = smartReplyStateInflater.inflateSmartReplyViewHolder(context, context2, notificationEntry, inflatedSmartReplyState, inflationProgress.inflatedSmartReplyState);
        }
        if (z) {
            InflatedSmartReplyViewHolder unused3 = inflationProgress.headsUpInflatedSmartReplies = smartReplyStateInflater.inflateSmartReplyViewHolder(context, context2, notificationEntry, inflatedSmartReplyState, inflationProgress.inflatedSmartReplyState);
        }
        return inflationProgress2;
    }

    /* access modifiers changed from: private */
    public static InflationProgress createRemoteViews(int i, Notification.Builder builder, boolean z, boolean z2, boolean z3, Context context) {
        InflationProgress inflationProgress = new InflationProgress();
        if ((i & 1) != 0) {
            RemoteViews unused = inflationProgress.newContentView = createContentView(builder, z, z2);
        }
        if ((i & 2) != 0) {
            RemoteViews unused2 = inflationProgress.newExpandedView = createExpandedView(builder, z);
        }
        if ((i & 4) != 0) {
            RemoteViews unused3 = inflationProgress.newHeadsUpView = builder.createHeadsUpContentView(z3);
        }
        if ((i & 8) != 0) {
            RemoteViews unused4 = inflationProgress.newPublicView = builder.makePublicContentView(z);
        }
        inflationProgress.packageContext = context;
        CharSequence unused5 = inflationProgress.headsUpStatusBarText = builder.getHeadsUpStatusBarText(false);
        CharSequence unused6 = inflationProgress.headsUpStatusBarTextPublic = builder.getHeadsUpStatusBarText(true);
        return inflationProgress;
    }

    /* access modifiers changed from: private */
    public static CancellationSignal apply(Executor executor, boolean z, InflationProgress inflationProgress, int i, NotifRemoteViewCache notifRemoteViewCache, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, RemoteViews.InteractionHandler interactionHandler, NotificationRowContentBinder.InflationCallback inflationCallback) {
        NotificationContentView notificationContentView;
        NotificationContentView notificationContentView2;
        HashMap hashMap;
        final InflationProgress inflationProgress2 = inflationProgress;
        NotifRemoteViewCache notifRemoteViewCache2 = notifRemoteViewCache;
        NotificationEntry notificationEntry2 = notificationEntry;
        NotificationContentView privateLayout = expandableNotificationRow.getPrivateLayout();
        NotificationContentView publicLayout = expandableNotificationRow.getPublicLayout();
        HashMap hashMap2 = new HashMap();
        if ((i & 1) != 0) {
            hashMap = hashMap2;
            notificationContentView2 = publicLayout;
            notificationContentView = privateLayout;
            applyRemoteView(executor, z, inflationProgress, i, 1, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newContentView, notifRemoteViewCache2.getCachedView(notificationEntry2, 1)), interactionHandler, inflationCallback, privateLayout, privateLayout.getContractedChild(), privateLayout.getVisibleWrapper(0), hashMap, new ApplyCallback() {
                public void setResultView(View view) {
                    View unused = InflationProgress.this.inflatedContentView = view;
                }

                public RemoteViews getRemoteView() {
                    return InflationProgress.this.newContentView;
                }
            });
        } else {
            hashMap = hashMap2;
            notificationContentView2 = publicLayout;
            notificationContentView = privateLayout;
        }
        if ((i & 2) != 0 && inflationProgress.newExpandedView != null) {
            final InflationProgress inflationProgress3 = inflationProgress;
            applyRemoteView(executor, z, inflationProgress, i, 2, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newExpandedView, notifRemoteViewCache.getCachedView(notificationEntry, 2)), interactionHandler, inflationCallback, notificationContentView, notificationContentView.getExpandedChild(), notificationContentView.getVisibleWrapper(1), hashMap, new ApplyCallback() {
                public void setResultView(View view) {
                    View unused = InflationProgress.this.inflatedExpandedView = view;
                }

                public RemoteViews getRemoteView() {
                    return InflationProgress.this.newExpandedView;
                }
            });
        }
        if (!((i & 4) == 0 || inflationProgress.newHeadsUpView == null)) {
            final InflationProgress inflationProgress4 = inflationProgress;
            NotificationContentView notificationContentView3 = notificationContentView;
            applyRemoteView(executor, z, inflationProgress, i, 4, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newHeadsUpView, notifRemoteViewCache.getCachedView(notificationEntry, 4)), interactionHandler, inflationCallback, notificationContentView3, notificationContentView.getHeadsUpChild(), notificationContentView3.getVisibleWrapper(2), hashMap, new ApplyCallback() {
                public void setResultView(View view) {
                    View unused = InflationProgress.this.inflatedHeadsUpView = view;
                }

                public RemoteViews getRemoteView() {
                    return InflationProgress.this.newHeadsUpView;
                }
            });
        }
        if ((i & 8) != 0) {
            final InflationProgress inflationProgress5 = inflationProgress;
            NotificationContentView notificationContentView4 = notificationContentView2;
            applyRemoteView(executor, z, inflationProgress, i, 8, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newPublicView, notifRemoteViewCache.getCachedView(notificationEntry, 8)), interactionHandler, inflationCallback, notificationContentView4, notificationContentView2.getContractedChild(), notificationContentView4.getVisibleWrapper(0), hashMap, new ApplyCallback() {
                public void setResultView(View view) {
                    View unused = InflationProgress.this.inflatedPublicView = view;
                }

                public RemoteViews getRemoteView() {
                    return InflationProgress.this.newPublicView;
                }
            });
        }
        finishIfDone(inflationProgress, i, notifRemoteViewCache, hashMap, inflationCallback, notificationEntry, expandableNotificationRow);
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new NotificationContentInflater$$ExternalSyntheticLambda5(hashMap));
        return cancellationSignal;
    }

    static void applyRemoteView(Executor executor, boolean z, InflationProgress inflationProgress, int i, int i2, NotifRemoteViewCache notifRemoteViewCache, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, boolean z2, RemoteViews.InteractionHandler interactionHandler, NotificationRowContentBinder.InflationCallback inflationCallback, NotificationContentView notificationContentView, View view, NotificationViewWrapper notificationViewWrapper, HashMap<Integer, CancellationSignal> hashMap, ApplyCallback applyCallback) {
        CancellationSignal cancellationSignal;
        InflationProgress inflationProgress2 = inflationProgress;
        RemoteViews.InteractionHandler interactionHandler2 = interactionHandler;
        HashMap<Integer, CancellationSignal> hashMap2 = hashMap;
        RemoteViews remoteView = applyCallback.getRemoteView();
        if (!z) {
            NotificationRowContentBinder.InflationCallback inflationCallback2 = inflationCallback;
            NotificationContentView notificationContentView2 = notificationContentView;
            View view2 = view;
            final ApplyCallback applyCallback2 = applyCallback;
            final ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
            final boolean z3 = z2;
            final NotificationViewWrapper notificationViewWrapper2 = notificationViewWrapper;
            final HashMap<Integer, CancellationSignal> hashMap3 = hashMap;
            final int i3 = i2;
            final InflationProgress inflationProgress3 = inflationProgress;
            final int i4 = i;
            final NotifRemoteViewCache notifRemoteViewCache2 = notifRemoteViewCache;
            final NotificationRowContentBinder.InflationCallback inflationCallback3 = inflationCallback;
            final NotificationEntry notificationEntry2 = notificationEntry;
            RemoteViews remoteViews = remoteView;
            final View view3 = view;
            final RemoteViews remoteViews2 = remoteViews;
            final NotificationContentView notificationContentView3 = notificationContentView;
            final RemoteViews.InteractionHandler interactionHandler3 = interactionHandler;
            C27585 r1 = new RemoteViews.OnViewAppliedListener() {
                public void onViewInflated(View view) {
                    if (view instanceof ImageMessageConsumer) {
                        ((ImageMessageConsumer) view).setImageResolver(ExpandableNotificationRow.this.getImageResolver());
                    }
                }

                public void onViewApplied(View view) {
                    if (z3) {
                        view.setIsRootNamespace(true);
                        applyCallback2.setResultView(view);
                    } else {
                        NotificationViewWrapper notificationViewWrapper = notificationViewWrapper2;
                        if (notificationViewWrapper != null) {
                            notificationViewWrapper.onReinflated();
                        }
                    }
                    hashMap3.remove(Integer.valueOf(i3));
                    boolean unused = NotificationContentInflater.finishIfDone(inflationProgress3, i4, notifRemoteViewCache2, hashMap3, inflationCallback3, notificationEntry2, ExpandableNotificationRow.this);
                }

                public void onError(Exception exc) {
                    try {
                        View view = view3;
                        if (z3) {
                            view = remoteViews2.apply(inflationProgress3.packageContext, notificationContentView3, interactionHandler3);
                        } else {
                            remoteViews2.reapply(inflationProgress3.packageContext, view3, interactionHandler3);
                        }
                        Log.wtf(NotificationContentInflater.TAG, "Async Inflation failed but normal inflation finished normally.", exc);
                        onViewApplied(view);
                    } catch (Exception unused) {
                        hashMap3.remove(Integer.valueOf(i3));
                        NotificationContentInflater.handleInflationError(hashMap3, exc, ExpandableNotificationRow.this.getEntry(), inflationCallback3);
                    }
                }
            };
            if (z2) {
                cancellationSignal = remoteViews.applyAsync(inflationProgress2.packageContext, notificationContentView, executor, r1, interactionHandler);
            } else {
                cancellationSignal = remoteViews.reapplyAsync(inflationProgress2.packageContext, view, executor, r1, interactionHandler);
            }
            hashMap.put(Integer.valueOf(i2), cancellationSignal);
        } else if (z2) {
            try {
                View apply = remoteView.apply(inflationProgress2.packageContext, notificationContentView, interactionHandler2);
                apply.setIsRootNamespace(true);
                applyCallback.setResultView(apply);
            } catch (Exception e) {
                handleInflationError(hashMap2, e, expandableNotificationRow.getEntry(), inflationCallback);
                hashMap2.put(Integer.valueOf(i2), new CancellationSignal());
            }
        } else {
            remoteView.reapply(inflationProgress2.packageContext, view, interactionHandler2);
            notificationViewWrapper.onReinflated();
        }
    }

    /* access modifiers changed from: private */
    public static void handleInflationError(HashMap<Integer, CancellationSignal> hashMap, Exception exc, NotificationEntry notificationEntry, NotificationRowContentBinder.InflationCallback inflationCallback) {
        Assert.isMainThread();
        hashMap.values().forEach(new NotificationContentInflater$$ExternalSyntheticLambda0());
        if (inflationCallback != null) {
            inflationCallback.handleInflationException(notificationEntry, exc);
        }
    }

    /* access modifiers changed from: private */
    public static boolean finishIfDone(InflationProgress inflationProgress, int i, NotifRemoteViewCache notifRemoteViewCache, HashMap<Integer, CancellationSignal> hashMap, NotificationRowContentBinder.InflationCallback inflationCallback, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        Assert.isMainThread();
        NotificationContentView privateLayout = expandableNotificationRow.getPrivateLayout();
        NotificationContentView publicLayout = expandableNotificationRow.getPublicLayout();
        boolean z = false;
        if (!hashMap.isEmpty()) {
            return false;
        }
        if ((i & 1) != 0) {
            if (inflationProgress.inflatedContentView != null) {
                privateLayout.setContractedChild(inflationProgress.inflatedContentView);
                notifRemoteViewCache.putCachedView(notificationEntry, 1, inflationProgress.newContentView);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 1)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 1, inflationProgress.newContentView);
            }
        }
        if ((i & 2) != 0) {
            if (inflationProgress.inflatedExpandedView != null) {
                privateLayout.setExpandedChild(inflationProgress.inflatedExpandedView);
                notifRemoteViewCache.putCachedView(notificationEntry, 2, inflationProgress.newExpandedView);
            } else if (inflationProgress.newExpandedView == null) {
                privateLayout.setExpandedChild((View) null);
                notifRemoteViewCache.removeCachedView(notificationEntry, 2);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 2)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 2, inflationProgress.newExpandedView);
            }
            if (inflationProgress.newExpandedView != null) {
                privateLayout.setExpandedInflatedSmartReplies(inflationProgress.expandedInflatedSmartReplies);
            } else {
                privateLayout.setExpandedInflatedSmartReplies((InflatedSmartReplyViewHolder) null);
            }
            if (inflationProgress.newExpandedView != null) {
                z = true;
            }
            expandableNotificationRow.setExpandable(z);
        }
        if ((i & 4) != 0) {
            if (inflationProgress.inflatedHeadsUpView != null) {
                privateLayout.setHeadsUpChild(inflationProgress.inflatedHeadsUpView);
                notifRemoteViewCache.putCachedView(notificationEntry, 4, inflationProgress.newHeadsUpView);
            } else if (inflationProgress.newHeadsUpView == null) {
                privateLayout.setHeadsUpChild((View) null);
                notifRemoteViewCache.removeCachedView(notificationEntry, 4);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 4)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 4, inflationProgress.newHeadsUpView);
            }
            if (inflationProgress.newHeadsUpView != null) {
                privateLayout.setHeadsUpInflatedSmartReplies(inflationProgress.headsUpInflatedSmartReplies);
            } else {
                privateLayout.setHeadsUpInflatedSmartReplies((InflatedSmartReplyViewHolder) null);
            }
        }
        privateLayout.setInflatedSmartReplyState(inflationProgress.inflatedSmartReplyState);
        if ((i & 8) != 0) {
            if (inflationProgress.inflatedPublicView != null) {
                publicLayout.setContractedChild(inflationProgress.inflatedPublicView);
                notifRemoteViewCache.putCachedView(notificationEntry, 8, inflationProgress.newPublicView);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 8)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 8, inflationProgress.newPublicView);
            }
        }
        notificationEntry.headsUpStatusBarText = inflationProgress.headsUpStatusBarText;
        notificationEntry.headsUpStatusBarTextPublic = inflationProgress.headsUpStatusBarTextPublic;
        if (inflationCallback != null) {
            inflationCallback.onAsyncInflationFinished(notificationEntry);
        }
        return true;
    }

    private static RemoteViews createExpandedView(Notification.Builder builder, boolean z) {
        RemoteViews createBigContentView = builder.createBigContentView();
        if (createBigContentView != null) {
            return createBigContentView;
        }
        if (!z) {
            return null;
        }
        RemoteViews createContentView = builder.createContentView();
        Notification.Builder.makeHeaderExpanded(createContentView);
        return createContentView;
    }

    private static RemoteViews createContentView(Notification.Builder builder, boolean z, boolean z2) {
        if (z) {
            return builder.makeLowPriorityContentView(false);
        }
        return builder.createContentView(z2);
    }

    static boolean canReapplyRemoteView(RemoteViews remoteViews, RemoteViews remoteViews2) {
        if (remoteViews == null && remoteViews2 == null) {
            return true;
        }
        if (remoteViews == null || remoteViews2 == null || remoteViews2.getPackage() == null || remoteViews.getPackage() == null || !remoteViews.getPackage().equals(remoteViews2.getPackage()) || remoteViews.getLayoutId() != remoteViews2.getLayoutId() || remoteViews2.hasFlags(1)) {
            return false;
        }
        return true;
    }

    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    public static class AsyncInflationTask extends AsyncTask<Void, Void, InflationProgress> implements NotificationRowContentBinder.InflationCallback, InflationTask {
        private final Executor mBgExecutor;
        private final NotificationRowContentBinder.InflationCallback mCallback;
        private CancellationSignal mCancellationSignal;
        private final Context mContext;
        private final ConversationNotificationProcessor mConversationProcessor;
        private final NotificationEntry mEntry;
        private Exception mError;
        private final boolean mInflateSynchronously;
        private final boolean mIsLowPriority;
        private final boolean mIsMediaInQS;
        private final int mReInflateFlags;
        private final NotifRemoteViewCache mRemoteViewCache;
        private RemoteViews.InteractionHandler mRemoteViewClickHandler;
        private ExpandableNotificationRow mRow;
        private final SmartReplyStateInflater mSmartRepliesInflater;
        private final boolean mUsesIncreasedHeadsUpHeight;
        private final boolean mUsesIncreasedHeight;

        private AsyncInflationTask(Executor executor, boolean z, int i, NotifRemoteViewCache notifRemoteViewCache, NotificationEntry notificationEntry, ConversationNotificationProcessor conversationNotificationProcessor, ExpandableNotificationRow expandableNotificationRow, boolean z2, boolean z3, boolean z4, NotificationRowContentBinder.InflationCallback inflationCallback, RemoteViews.InteractionHandler interactionHandler, boolean z5, SmartReplyStateInflater smartReplyStateInflater) {
            this.mEntry = notificationEntry;
            this.mRow = expandableNotificationRow;
            this.mBgExecutor = executor;
            this.mInflateSynchronously = z;
            this.mReInflateFlags = i;
            this.mRemoteViewCache = notifRemoteViewCache;
            this.mSmartRepliesInflater = smartReplyStateInflater;
            this.mContext = expandableNotificationRow.getContext();
            this.mIsLowPriority = z2;
            this.mUsesIncreasedHeight = z3;
            this.mUsesIncreasedHeadsUpHeight = z4;
            this.mRemoteViewClickHandler = interactionHandler;
            this.mCallback = inflationCallback;
            this.mConversationProcessor = conversationNotificationProcessor;
            this.mIsMediaInQS = z5;
            notificationEntry.setInflationTask(this);
        }

        public int getReInflateFlags() {
            return this.mReInflateFlags;
        }

        /* access modifiers changed from: package-private */
        public void updateApplicationInfo(StatusBarNotification statusBarNotification) {
            try {
                Notification.addFieldsFromContext(this.mContext.getPackageManager().getApplicationInfoAsUser(statusBarNotification.getPackageName(), 8192, UserHandle.getUserId(statusBarNotification.getUid())), statusBarNotification.getNotification());
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }

        /* access modifiers changed from: protected */
        public InflationProgress doInBackground(Void... voidArr) {
            try {
                StatusBarNotification sbn = this.mEntry.getSbn();
                updateApplicationInfo(sbn);
                Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(this.mContext, sbn.getNotification());
                Context packageContext = sbn.getPackageContext(this.mContext);
                if (recoverBuilder.usesTemplate()) {
                    packageContext = new RtlEnabledContext(packageContext);
                }
                if (this.mEntry.getRanking().isConversation()) {
                    this.mConversationProcessor.processNotification(this.mEntry, recoverBuilder);
                }
                return NotificationContentInflater.inflateSmartReplyViews(NotificationContentInflater.createRemoteViews(this.mReInflateFlags, recoverBuilder, this.mIsLowPriority, this.mUsesIncreasedHeight, this.mUsesIncreasedHeadsUpHeight, packageContext), this.mReInflateFlags, this.mEntry, this.mContext, packageContext, this.mRow.getExistingSmartReplyState(), this.mSmartRepliesInflater);
            } catch (Exception e) {
                this.mError = e;
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(InflationProgress inflationProgress) {
            Exception exc = this.mError;
            if (exc == null) {
                this.mCancellationSignal = NotificationContentInflater.apply(this.mBgExecutor, this.mInflateSynchronously, inflationProgress, this.mReInflateFlags, this.mRemoteViewCache, this.mEntry, this.mRow, this.mRemoteViewClickHandler, this);
                return;
            }
            handleError(exc);
        }

        private void handleError(Exception exc) {
            this.mEntry.onInflationTaskFinished();
            StatusBarNotification sbn = this.mEntry.getSbn();
            Log.e(CentralSurfaces.TAG, "couldn't inflate view for notification " + (sbn.getPackageName() + "/0x" + Integer.toHexString(sbn.getId())), exc);
            NotificationRowContentBinder.InflationCallback inflationCallback = this.mCallback;
            if (inflationCallback != null) {
                inflationCallback.handleInflationException(this.mRow.getEntry(), new InflationException("Couldn't inflate contentViews" + exc));
            }
        }

        public void abort() {
            cancel(true);
            CancellationSignal cancellationSignal = this.mCancellationSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
            }
        }

        public void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
            handleError(exc);
        }

        public void onAsyncInflationFinished(NotificationEntry notificationEntry) {
            this.mEntry.onInflationTaskFinished();
            this.mRow.onNotificationUpdated();
            NotificationRowContentBinder.InflationCallback inflationCallback = this.mCallback;
            if (inflationCallback != null) {
                inflationCallback.onAsyncInflationFinished(this.mEntry);
            }
            this.mRow.getImageResolver().purgeCache();
        }

        private static class RtlEnabledContext extends ContextWrapper {
            private RtlEnabledContext(Context context) {
                super(context);
            }

            public ApplicationInfo getApplicationInfo() {
                ApplicationInfo applicationInfo = super.getApplicationInfo();
                applicationInfo.flags |= 4194304;
                return applicationInfo;
            }
        }
    }

    static class InflationProgress {
        /* access modifiers changed from: private */
        public InflatedSmartReplyViewHolder expandedInflatedSmartReplies;
        /* access modifiers changed from: private */
        public InflatedSmartReplyViewHolder headsUpInflatedSmartReplies;
        /* access modifiers changed from: private */
        public CharSequence headsUpStatusBarText;
        /* access modifiers changed from: private */
        public CharSequence headsUpStatusBarTextPublic;
        /* access modifiers changed from: private */
        public View inflatedContentView;
        /* access modifiers changed from: private */
        public View inflatedExpandedView;
        /* access modifiers changed from: private */
        public View inflatedHeadsUpView;
        /* access modifiers changed from: private */
        public View inflatedPublicView;
        /* access modifiers changed from: private */
        public InflatedSmartReplyState inflatedSmartReplyState;
        /* access modifiers changed from: private */
        public RemoteViews newContentView;
        /* access modifiers changed from: private */
        public RemoteViews newExpandedView;
        /* access modifiers changed from: private */
        public RemoteViews newHeadsUpView;
        /* access modifiers changed from: private */
        public RemoteViews newPublicView;
        Context packageContext;

        InflationProgress() {
        }
    }

    static abstract class ApplyCallback {
        public abstract RemoteViews getRemoteView();

        public abstract void setResultView(View view);

        ApplyCallback() {
        }
    }
}
