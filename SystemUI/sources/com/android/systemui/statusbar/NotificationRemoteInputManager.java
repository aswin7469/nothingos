package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.util.DumpUtilsKt;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NotificationRemoteInputManager implements Dumpable {
    private static final boolean DEBUG = false;
    public static final boolean ENABLE_REMOTE_INPUT = SystemProperties.getBoolean("debug.enable_remote_input", true);
    public static boolean FORCE_REMOTE_INPUT_HISTORY = SystemProperties.getBoolean("debug.force_remoteinput_history", true);
    private static final String TAG = "NotifRemoteInputManager";
    protected IStatusBarService mBarService;
    protected Callback mCallback;
    /* access modifiers changed from: private */
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    /* access modifiers changed from: private */
    public final NotificationClickNotifier mClickNotifier;
    protected final Context mContext;
    private final List<RemoteInputController.Callback> mControllerCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public final NotificationEntryManager mEntryManager;
    private final RemoteViews.InteractionHandler mInteractionHandler = new RemoteViews.InteractionHandler() {
        public boolean onInteraction(View view, PendingIntent pendingIntent, RemoteViews.RemoteResponse remoteResponse) {
            boolean z;
            ((Optional) NotificationRemoteInputManager.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new NotificationRemoteInputManager$1$$ExternalSyntheticLambda0(view));
            NotificationEntry notificationForParent = getNotificationForParent(view.getParent());
            NotificationRemoteInputManager.this.mLogger.logInitialClick(notificationForParent, pendingIntent);
            if (handleRemoteInput(view, pendingIntent)) {
                NotificationRemoteInputManager.this.mLogger.logRemoteInputWasHandled(notificationForParent);
                return true;
            }
            logActionClick(view, notificationForParent, pendingIntent);
            try {
                ActivityManager.getService().resumeAppSwitches();
            } catch (RemoteException unused) {
            }
            Notification.Action actionFromView = getActionFromView(view, notificationForParent, pendingIntent);
            Callback callback = NotificationRemoteInputManager.this.mCallback;
            if (actionFromView == null) {
                z = false;
            } else {
                z = actionFromView.isAuthenticationRequired();
            }
            return callback.handleRemoteViewClick(view, pendingIntent, z, new NotificationRemoteInputManager$1$$ExternalSyntheticLambda1(this, remoteResponse, view, notificationForParent, pendingIntent));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onInteraction$1$com-android-systemui-statusbar-NotificationRemoteInputManager$1 */
        public /* synthetic */ boolean mo38826x7b0670ac(RemoteViews.RemoteResponse remoteResponse, View view, NotificationEntry notificationEntry, PendingIntent pendingIntent) {
            Pair launchOptions = remoteResponse.getLaunchOptions(view);
            NotificationRemoteInputManager.this.mLogger.logStartingIntentWithDefaultHandler(notificationEntry, pendingIntent);
            boolean startPendingIntent = RemoteViews.startPendingIntent(view, pendingIntent, launchOptions);
            if (startPendingIntent) {
                NotificationRemoteInputManager.this.releaseNotificationIfKeptForRemoteInputHistory(notificationEntry);
            }
            return startPendingIntent;
        }

        private Notification.Action getActionFromView(View view, NotificationEntry notificationEntry, PendingIntent pendingIntent) {
            Integer num = (Integer) view.getTag(16909280);
            if (num == null) {
                return null;
            }
            if (notificationEntry == null) {
                Log.w(NotificationRemoteInputManager.TAG, "Couldn't determine notification for click.");
                return null;
            }
            StatusBarNotification sbn = notificationEntry.getSbn();
            Notification.Action[] actionArr = sbn.getNotification().actions;
            if (actionArr == null || num.intValue() >= actionArr.length) {
                Log.w(NotificationRemoteInputManager.TAG, "statusBarNotification.getNotification().actions is null or invalid");
                return null;
            }
            Notification.Action action = sbn.getNotification().actions[num.intValue()];
            if (Objects.equals(action.actionIntent, pendingIntent)) {
                return action;
            }
            Log.w(NotificationRemoteInputManager.TAG, "actionIntent does not match");
            return null;
        }

        private void logActionClick(View view, NotificationEntry notificationEntry, PendingIntent pendingIntent) {
            Notification.Action actionFromView = getActionFromView(view, notificationEntry, pendingIntent);
            if (actionFromView != null) {
                ViewParent parent = view.getParent();
                NotificationRemoteInputManager.this.mClickNotifier.onNotificationActionClick(notificationEntry.getSbn().getKey(), (view.getId() != 16908718 || parent == null || !(parent instanceof ViewGroup)) ? -1 : ((ViewGroup) parent).indexOfChild(view), actionFromView, NotificationRemoteInputManager.this.mVisibilityProvider.obtain(notificationEntry, true), false);
            }
        }

        private NotificationEntry getNotificationForParent(ViewParent viewParent) {
            while (viewParent != null) {
                if (viewParent instanceof ExpandableNotificationRow) {
                    return ((ExpandableNotificationRow) viewParent).getEntry();
                }
                viewParent = viewParent.getParent();
            }
            return null;
        }

        private boolean handleRemoteInput(View view, PendingIntent pendingIntent) {
            if (NotificationRemoteInputManager.this.mCallback.shouldHandleRemoteInput(view, pendingIntent)) {
                return true;
            }
            Object tag = view.getTag(16909401);
            RemoteInput[] remoteInputArr = tag instanceof RemoteInput[] ? (RemoteInput[]) tag : null;
            if (remoteInputArr == null) {
                return false;
            }
            RemoteInput remoteInput = null;
            for (RemoteInput remoteInput2 : remoteInputArr) {
                if (remoteInput2.getAllowFreeFormInput()) {
                    remoteInput = remoteInput2;
                }
            }
            if (remoteInput == null) {
                return false;
            }
            return NotificationRemoteInputManager.this.activateRemoteInput(view, remoteInputArr, remoteInput, pendingIntent, (NotificationEntry.EditedSuggestionInfo) null);
        }
    };
    private final KeyguardManager mKeyguardManager;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    /* access modifiers changed from: private */
    public final ActionClickLogger mLogger;
    /* access modifiers changed from: private */
    public final Handler mMainHandler;
    protected final NotifPipelineFlags mNotifPipelineFlags;
    /* access modifiers changed from: private */
    public final RemoteInputNotificationRebuilder mRebuilder;
    protected RemoteInputController mRemoteInputController;
    /* access modifiers changed from: private */
    public RemoteInputListener mRemoteInputListener;
    private final RemoteInputUriController mRemoteInputUriController;
    /* access modifiers changed from: private */
    public final SmartReplyController mSmartReplyController;
    private final StatusBarStateController mStatusBarStateController;
    private final UserManager mUserManager;
    /* access modifiers changed from: private */
    public final NotificationVisibilityProvider mVisibilityProvider;

    public interface AuthBypassPredicate {
        boolean canSendRemoteInputWithoutBouncer();
    }

    public interface BouncerChecker {
        boolean showBouncerIfNecessary();
    }

    public interface Callback {
        boolean handleRemoteViewClick(View view, PendingIntent pendingIntent, boolean z, ClickHandler clickHandler);

        void onLockedRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view);

        void onLockedWorkRemoteInput(int i, ExpandableNotificationRow expandableNotificationRow, View view);

        void onMakeExpandedVisibleForRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view, boolean z, Runnable runnable);

        boolean shouldHandleRemoteInput(View view, PendingIntent pendingIntent);
    }

    public interface ClickHandler {
        boolean handleClick();
    }

    public interface RemoteInputListener {
        boolean isNotificationKeptForRemoteInputHistory(String str);

        void onPanelCollapsed();

        void onRemoteInputSent(NotificationEntry notificationEntry);

        void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry);

        void setRemoteInputController(RemoteInputController remoteInputController);
    }

    public NotificationRemoteInputManager(Context context, NotifPipelineFlags notifPipelineFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, SmartReplyController smartReplyController, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, Lazy<Optional<CentralSurfaces>> lazy, StatusBarStateController statusBarStateController, @Main Handler handler, RemoteInputUriController remoteInputUriController, NotificationClickNotifier notificationClickNotifier, ActionClickLogger actionClickLogger, DumpManager dumpManager) {
        this.mContext = context;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mSmartReplyController = smartReplyController;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mMainHandler = handler;
        this.mLogger = actionClickLogger;
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mRebuilder = remoteInputNotificationRebuilder;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            this.mRemoteInputListener = createLegacyRemoteInputLifetimeExtender(handler, notificationEntryManager, smartReplyController);
        }
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mStatusBarStateController = statusBarStateController;
        this.mRemoteInputUriController = remoteInputUriController;
        this.mClickNotifier = notificationClickNotifier;
        dumpManager.registerDumpable(this);
        notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
                if (z && notificationEntry != null) {
                    NotificationRemoteInputManager.this.onPerformRemoveNotification(notificationEntry, notificationEntry.getKey());
                }
            }
        });
    }

    public void setRemoteInputListener(RemoteInputListener remoteInputListener) {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            return;
        }
        if (this.mRemoteInputListener == null) {
            this.mRemoteInputListener = remoteInputListener;
            RemoteInputController remoteInputController = this.mRemoteInputController;
            if (remoteInputController != null) {
                remoteInputListener.setRemoteInputController(remoteInputController);
                return;
            }
            return;
        }
        throw new IllegalStateException("mRemoteInputListener is already set");
    }

    /* access modifiers changed from: protected */
    public LegacyRemoteInputLifetimeExtender createLegacyRemoteInputLifetimeExtender(Handler handler, NotificationEntryManager notificationEntryManager, SmartReplyController smartReplyController) {
        return new LegacyRemoteInputLifetimeExtender();
    }

    public void setUpWithCallback(Callback callback, RemoteInputController.Delegate delegate) {
        this.mCallback = callback;
        RemoteInputController remoteInputController = new RemoteInputController(delegate, this.mRemoteInputUriController);
        this.mRemoteInputController = remoteInputController;
        RemoteInputListener remoteInputListener = this.mRemoteInputListener;
        if (remoteInputListener != null) {
            remoteInputListener.setRemoteInputController(remoteInputController);
        }
        for (RemoteInputController.Callback addCallback : this.mControllerCallbacks) {
            this.mRemoteInputController.addCallback(addCallback);
        }
        this.mControllerCallbacks.clear();
        this.mRemoteInputController.addCallback(new RemoteInputController.Callback() {
            public void onRemoteInputSent(NotificationEntry notificationEntry) {
                if (NotificationRemoteInputManager.this.mRemoteInputListener != null) {
                    NotificationRemoteInputManager.this.mRemoteInputListener.onRemoteInputSent(notificationEntry);
                }
                try {
                    NotificationRemoteInputManager.this.mBarService.onNotificationDirectReplied(notificationEntry.getSbn().getKey());
                    if (notificationEntry.editedSuggestionInfo != null) {
                        NotificationRemoteInputManager.this.mBarService.onNotificationSmartReplySent(notificationEntry.getSbn().getKey(), notificationEntry.editedSuggestionInfo.index, notificationEntry.editedSuggestionInfo.originalText, NotificationLogger.getNotificationLocation(notificationEntry).toMetricsEventEnum(), !TextUtils.equals(notificationEntry.remoteInputText, notificationEntry.editedSuggestionInfo.originalText));
                    }
                } catch (RemoteException unused) {
                }
            }
        });
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mSmartReplyController.setCallback(new NotificationRemoteInputManager$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setUpWithCallback$0$com-android-systemui-statusbar-NotificationRemoteInputManager */
    public /* synthetic */ void mo38818x7995d1d3(NotificationEntry notificationEntry, CharSequence charSequence) {
        this.mEntryManager.updateNotification(this.mRebuilder.rebuildForSendingSmartReply(notificationEntry, charSequence), (NotificationListenerService.RankingMap) null);
    }

    public void addControllerCallback(RemoteInputController.Callback callback) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            remoteInputController.addCallback(callback);
        } else {
            this.mControllerCallbacks.add(callback);
        }
    }

    public void removeControllerCallback(RemoteInputController.Callback callback) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            remoteInputController.removeCallback(callback);
        } else {
            this.mControllerCallbacks.remove((Object) callback);
        }
    }

    public boolean activateRemoteInput(View view, RemoteInput[] remoteInputArr, RemoteInput remoteInput, PendingIntent pendingIntent, NotificationEntry.EditedSuggestionInfo editedSuggestionInfo) {
        return mo38816x794a631d(view, remoteInputArr, remoteInput, pendingIntent, editedSuggestionInfo, (String) null, (AuthBypassPredicate) null);
    }

    /* renamed from: activateRemoteInput */
    public boolean mo38816x794a631d(View view, RemoteInput[] remoteInputArr, RemoteInput remoteInput, PendingIntent pendingIntent, NotificationEntry.EditedSuggestionInfo editedSuggestionInfo, String str, AuthBypassPredicate authBypassPredicate) {
        RemoteInputView remoteInputView;
        RemoteInputView remoteInputView2;
        ExpandableNotificationRow expandableNotificationRow;
        View view2 = view;
        PendingIntent pendingIntent2 = pendingIntent;
        String str2 = str;
        ViewParent parent = view.getParent();
        while (true) {
            remoteInputView = null;
            if (parent == null) {
                remoteInputView2 = null;
                expandableNotificationRow = null;
                break;
            }
            if (parent instanceof View) {
                View view3 = (View) parent;
                if (view3.isRootNamespace()) {
                    remoteInputView2 = findRemoteInputView(view3);
                    expandableNotificationRow = (ExpandableNotificationRow) view3.getTag(C1894R.C1898id.row_tag_for_content_view);
                    break;
                }
            }
            parent = parent.getParent();
        }
        if (expandableNotificationRow == null) {
            return false;
        }
        expandableNotificationRow.setUserExpanded(true);
        boolean z = authBypassPredicate != null;
        if (!z && showBouncerForRemoteInput(view2, pendingIntent2, expandableNotificationRow)) {
            return true;
        }
        if (remoteInputView2 == null || remoteInputView2.isAttachedToWindow()) {
            remoteInputView = remoteInputView2;
        }
        if (remoteInputView == null && (remoteInputView = findRemoteInputView(expandableNotificationRow.getPrivateLayout().getExpandedChild())) == null) {
            return false;
        }
        if (remoteInputView == expandableNotificationRow.getPrivateLayout().getExpandedRemoteInput() && !expandableNotificationRow.getPrivateLayout().getExpandedChild().isShown()) {
            this.mCallback.onMakeExpandedVisibleForRemoteInput(expandableNotificationRow, view2, z, new NotificationRemoteInputManager$$ExternalSyntheticLambda1(this, view, remoteInputArr, remoteInput, pendingIntent, editedSuggestionInfo, str, authBypassPredicate));
            return true;
        } else if (!remoteInputView.isAttachedToWindow()) {
            return false;
        } else {
            int width = view.getWidth();
            if (view2 instanceof TextView) {
                TextView textView = (TextView) view2;
                if (textView.getLayout() != null) {
                    width = Math.min(width, ((int) textView.getLayout().getLineWidth(0)) + textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight());
                }
            }
            int left = view.getLeft() + (width / 2);
            int top = view.getTop() + (view.getHeight() / 2);
            int width2 = remoteInputView.getWidth();
            int height = remoteInputView.getHeight() - top;
            int i = width2 - left;
            remoteInputView.getController().setRevealParams(new RemoteInputView.RevealParams(left, top, Math.max(Math.max(left + top, left + height), Math.max(i + top, i + height))));
            remoteInputView.getController().setPendingIntent(pendingIntent2);
            remoteInputView.getController().setRemoteInput(remoteInput);
            remoteInputView.getController().setRemoteInputs(remoteInputArr);
            remoteInputView.getController().setEditedSuggestionInfo(editedSuggestionInfo);
            remoteInputView.focusAnimated();
            if (str2 != null) {
                remoteInputView.setEditTextContent(str2);
            }
            if (z) {
                remoteInputView.getController().setBouncerChecker(new NotificationRemoteInputManager$$ExternalSyntheticLambda2(this, authBypassPredicate, view, pendingIntent, expandableNotificationRow));
            }
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$activateRemoteInput$2$com-android-systemui-statusbar-NotificationRemoteInputManager */
    public /* synthetic */ boolean mo38817x6cd9e75e(AuthBypassPredicate authBypassPredicate, View view, PendingIntent pendingIntent, ExpandableNotificationRow expandableNotificationRow) {
        return !authBypassPredicate.canSendRemoteInputWithoutBouncer() && showBouncerForRemoteInput(view, pendingIntent, expandableNotificationRow);
    }

    private boolean showBouncerForRemoteInput(View view, PendingIntent pendingIntent, ExpandableNotificationRow expandableNotificationRow) {
        UserInfo profileParent;
        if (this.mLockscreenUserManager.shouldAllowLockscreenRemoteInput()) {
            return false;
        }
        int identifier = pendingIntent.getCreatorUserHandle().getIdentifier();
        boolean z = this.mUserManager.getUserInfo(identifier).isManagedProfile() && this.mKeyguardManager.isDeviceLocked(identifier);
        boolean z2 = z && (profileParent = this.mUserManager.getProfileParent(identifier)) != null && this.mKeyguardManager.isDeviceLocked(profileParent.id);
        if (this.mLockscreenUserManager.isLockscreenPublicMode(identifier) || this.mStatusBarStateController.getState() == 1) {
            if (!z || z2) {
                this.mCallback.onLockedRemoteInput(expandableNotificationRow, view);
            } else {
                this.mCallback.onLockedWorkRemoteInput(identifier, expandableNotificationRow, view);
            }
            return true;
        } else if (!z) {
            return false;
        } else {
            this.mCallback.onLockedWorkRemoteInput(identifier, expandableNotificationRow, view);
            return true;
        }
    }

    private RemoteInputView findRemoteInputView(View view) {
        if (view == null) {
            return null;
        }
        return (RemoteInputView) view.findViewWithTag(RemoteInputView.VIEW_TAG);
    }

    public ArrayList<NotificationLifetimeExtender> getLifetimeExtenders() {
        return ((LegacyRemoteInputLifetimeExtender) this.mRemoteInputListener).mLifetimeExtenders;
    }

    /* access modifiers changed from: package-private */
    public void onPerformRemoveNotification(NotificationEntry notificationEntry, String str) {
        ((LegacyRemoteInputLifetimeExtender) this.mRemoteInputListener).mKeysKeptForRemoteInputHistory.remove(str);
        cleanUpRemoteInputForUserRemoval(notificationEntry);
    }

    public void cleanUpRemoteInputForUserRemoval(NotificationEntry notificationEntry) {
        if (isRemoteInputActive(notificationEntry)) {
            notificationEntry.mRemoteEditImeVisible = false;
            this.mRemoteInputController.removeRemoteInput(notificationEntry, (Object) null);
        }
    }

    public void onPanelCollapsed() {
        RemoteInputListener remoteInputListener = this.mRemoteInputListener;
        if (remoteInputListener != null) {
            remoteInputListener.onPanelCollapsed();
        }
    }

    public boolean isNotificationKeptForRemoteInputHistory(String str) {
        RemoteInputListener remoteInputListener = this.mRemoteInputListener;
        return remoteInputListener != null && remoteInputListener.isNotificationKeptForRemoteInputHistory(str);
    }

    public boolean shouldKeepForRemoteInputHistory(NotificationEntry notificationEntry) {
        if (!FORCE_REMOTE_INPUT_HISTORY) {
            return false;
        }
        if (isSpinning(notificationEntry.getKey()) || notificationEntry.hasJustSentRemoteInput()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry) {
        RemoteInputListener remoteInputListener;
        if (notificationEntry != null && (remoteInputListener = this.mRemoteInputListener) != null) {
            remoteInputListener.releaseNotificationIfKeptForRemoteInputHistory(notificationEntry);
        }
    }

    public boolean shouldKeepForSmartReplyHistory(NotificationEntry notificationEntry) {
        if (!FORCE_REMOTE_INPUT_HISTORY) {
            return false;
        }
        return this.mSmartReplyController.isSendingSmartReply(notificationEntry.getKey());
    }

    public void checkRemoteInputOutside(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 4 && motionEvent.getX() == 0.0f && motionEvent.getY() == 0.0f && isRemoteInputActive()) {
            closeRemoteInputs();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        if (this.mRemoteInputController != null) {
            asIndenting.println("mRemoteInputController: " + this.mRemoteInputController);
            asIndenting.increaseIndent();
            this.mRemoteInputController.dump(asIndenting);
            asIndenting.decreaseIndent();
        }
        if (this.mRemoteInputListener instanceof Dumpable) {
            asIndenting.println("mRemoteInputListener: " + this.mRemoteInputListener.getClass().getSimpleName());
            asIndenting.increaseIndent();
            ((Dumpable) this.mRemoteInputListener).dump(asIndenting, strArr);
            asIndenting.decreaseIndent();
        }
    }

    public void bindRow(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.setRemoteInputController(this.mRemoteInputController);
    }

    public RemoteViews.InteractionHandler getRemoteViewsOnClickHandler() {
        return this.mInteractionHandler;
    }

    public boolean isRemoteInputActive() {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        return remoteInputController != null && remoteInputController.isRemoteInputActive();
    }

    public boolean isRemoteInputActive(NotificationEntry notificationEntry) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        return remoteInputController != null && remoteInputController.isRemoteInputActive(notificationEntry);
    }

    public boolean isSpinning(String str) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        return remoteInputController != null && remoteInputController.isSpinning(str);
    }

    public void closeRemoteInputs() {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            remoteInputController.closeRemoteInputs();
        }
    }

    protected class LegacyRemoteInputLifetimeExtender implements RemoteInputListener, Dumpable {
        private static final int REMOTE_INPUT_KEPT_ENTRY_AUTO_CANCEL_DELAY = 200;
        protected final ArraySet<NotificationEntry> mEntriesKeptForRemoteInputActive = new ArraySet<>();
        protected final ArraySet<String> mKeysKeptForRemoteInputHistory = new ArraySet<>();
        protected final ArrayList<NotificationLifetimeExtender> mLifetimeExtenders = new ArrayList<>();
        protected NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
        private RemoteInputController mRemoteInputController;

        LegacyRemoteInputLifetimeExtender() {
            addLifetimeExtenders();
        }

        /* access modifiers changed from: protected */
        public void addLifetimeExtenders() {
            this.mLifetimeExtenders.add(new RemoteInputHistoryExtender());
            this.mLifetimeExtenders.add(new SmartReplyHistoryExtender());
            this.mLifetimeExtenders.add(new RemoteInputActiveExtender());
        }

        public void setRemoteInputController(RemoteInputController remoteInputController) {
            this.mRemoteInputController = remoteInputController;
        }

        public void onRemoteInputSent(NotificationEntry notificationEntry) {
            if (NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY && isNotificationKeptForRemoteInputHistory(notificationEntry.getKey())) {
                this.mNotificationLifetimeFinishedCallback.onSafeToRemove(notificationEntry.getKey());
            } else if (this.mEntriesKeptForRemoteInputActive.contains(notificationEntry)) {
                NotificationRemoteInputManager.this.mMainHandler.postDelayed(new C2583x9a54ce8a(this, notificationEntry), 200);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRemoteInputSent$0$com-android-systemui-statusbar-NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender */
        public /* synthetic */ void mo38840x9dd93c8f(NotificationEntry notificationEntry) {
            if (this.mEntriesKeptForRemoteInputActive.remove(notificationEntry)) {
                this.mNotificationLifetimeFinishedCallback.onSafeToRemove(notificationEntry.getKey());
            }
        }

        public void onPanelCollapsed() {
            for (int i = 0; i < this.mEntriesKeptForRemoteInputActive.size(); i++) {
                NotificationEntry valueAt = this.mEntriesKeptForRemoteInputActive.valueAt(i);
                RemoteInputController remoteInputController = this.mRemoteInputController;
                if (remoteInputController != null) {
                    remoteInputController.removeRemoteInput(valueAt, (Object) null);
                }
                NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                if (notificationSafeToRemoveCallback != null) {
                    notificationSafeToRemoveCallback.onSafeToRemove(valueAt.getKey());
                }
            }
            this.mEntriesKeptForRemoteInputActive.clear();
        }

        public boolean isNotificationKeptForRemoteInputHistory(String str) {
            return this.mKeysKeptForRemoteInputHistory.contains(str);
        }

        public void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry) {
            String key = notificationEntry.getKey();
            if (isNotificationKeptForRemoteInputHistory(key)) {
                NotificationRemoteInputManager.this.mMainHandler.postDelayed(new C2584x9a54ce8b(this, key), 200);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$releaseNotificationIfKeptForRemoteInputHistory$1$com-android-systemui-statusbar-NotificationRemoteInputManager$LegacyRemoteInputLifetimeExtender */
        public /* synthetic */ void mo38841xaac6da19(String str) {
            if (isNotificationKeptForRemoteInputHistory(str)) {
                this.mNotificationLifetimeFinishedCallback.onSafeToRemove(str);
            }
        }

        public Set<NotificationEntry> getEntriesKeptForRemoteInputActive() {
            return this.mEntriesKeptForRemoteInputActive;
        }

        public void dump(PrintWriter printWriter, String[] strArr) {
            printWriter.println("LegacyRemoteInputLifetimeExtender:");
            printWriter.print("  mKeysKeptForRemoteInputHistory: ");
            printWriter.println((Object) this.mKeysKeptForRemoteInputHistory);
            printWriter.print("  mEntriesKeptForRemoteInputActive: ");
            printWriter.println((Object) this.mEntriesKeptForRemoteInputActive);
        }

        protected abstract class RemoteInputExtender implements NotificationLifetimeExtender {
            protected RemoteInputExtender() {
            }

            public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
                if (LegacyRemoteInputLifetimeExtender.this.mNotificationLifetimeFinishedCallback == null) {
                    LegacyRemoteInputLifetimeExtender.this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
                }
            }
        }

        protected class RemoteInputHistoryExtender extends RemoteInputExtender {
            protected RemoteInputHistoryExtender() {
                super();
            }

            public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.shouldKeepForRemoteInputHistory(notificationEntry);
            }

            public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                if (z) {
                    StatusBarNotification rebuildForRemoteInputReply = NotificationRemoteInputManager.this.mRebuilder.rebuildForRemoteInputReply(notificationEntry);
                    notificationEntry.onRemoteInputInserted();
                    if (rebuildForRemoteInputReply != null) {
                        NotificationRemoteInputManager.this.mEntryManager.updateNotification(rebuildForRemoteInputReply, (NotificationListenerService.RankingMap) null);
                        if (!notificationEntry.isRemoved()) {
                            if (Log.isLoggable(NotificationRemoteInputManager.TAG, 3)) {
                                Log.d(NotificationRemoteInputManager.TAG, "Keeping notification around after sending remote input " + notificationEntry.getKey());
                            }
                            LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.add(notificationEntry.getKey());
                            return;
                        }
                        return;
                    }
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.remove(notificationEntry.getKey());
            }
        }

        protected class SmartReplyHistoryExtender extends RemoteInputExtender {
            protected SmartReplyHistoryExtender() {
                super();
            }

            public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.shouldKeepForSmartReplyHistory(notificationEntry);
            }

            public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                if (z) {
                    StatusBarNotification rebuildForCanceledSmartReplies = NotificationRemoteInputManager.this.mRebuilder.rebuildForCanceledSmartReplies(notificationEntry);
                    if (rebuildForCanceledSmartReplies != null) {
                        NotificationRemoteInputManager.this.mEntryManager.updateNotification(rebuildForCanceledSmartReplies, (NotificationListenerService.RankingMap) null);
                        if (!notificationEntry.isRemoved()) {
                            if (Log.isLoggable(NotificationRemoteInputManager.TAG, 3)) {
                                Log.d(NotificationRemoteInputManager.TAG, "Keeping notification around after sending smart reply " + notificationEntry.getKey());
                            }
                            LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.add(notificationEntry.getKey());
                            return;
                        }
                        return;
                    }
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.remove(notificationEntry.getKey());
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
            }
        }

        protected class RemoteInputActiveExtender extends RemoteInputExtender {
            protected RemoteInputActiveExtender() {
                super();
            }

            public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.isRemoteInputActive(notificationEntry);
            }

            public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                if (z) {
                    if (Log.isLoggable(NotificationRemoteInputManager.TAG, 3)) {
                        Log.d(NotificationRemoteInputManager.TAG, "Keeping notification around while remote input active " + notificationEntry.getKey());
                    }
                    LegacyRemoteInputLifetimeExtender.this.mEntriesKeptForRemoteInputActive.add(notificationEntry);
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mEntriesKeptForRemoteInputActive.remove(notificationEntry);
            }
        }
    }
}
