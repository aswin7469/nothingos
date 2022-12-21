package com.android.p019wm.shell.transition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.ITransitionPlayer;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionMetrics;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.transition.IShellTransitions;
import java.util.ArrayList;
import java.util.Arrays;

/* renamed from: com.android.wm.shell.transition.Transitions */
public class Transitions implements RemoteCallable<Transitions> {
    public static final boolean ENABLE_SHELL_TRANSITIONS;
    public static final boolean SHELL_TRANSITIONS_ROTATION;
    static final String TAG = "ShellTransitions";
    public static final int TRANSIT_EXIT_PIP = 13;
    public static final int TRANSIT_EXIT_PIP_TO_SPLIT = 14;
    public static final int TRANSIT_REMOVE_PIP = 15;
    public static final int TRANSIT_SPLIT_DISMISS = 19;
    public static final int TRANSIT_SPLIT_DISMISS_SNAP = 18;
    public static final int TRANSIT_SPLIT_SCREEN_OPEN_TO_SIDE = 17;
    public static final int TRANSIT_SPLIT_SCREEN_PAIR_OPEN = 16;
    private final ArrayList<ActiveTransition> mActiveTransitions;
    private final ShellExecutor mAnimExecutor;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final DisplayController mDisplayController;
    private final ArrayList<TransitionHandler> mHandlers;
    private final ShellTransitionImpl mImpl;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final WindowOrganizer mOrganizer;
    private final TransitionPlayerImpl mPlayerImpl;
    /* access modifiers changed from: private */
    public final RemoteTransitionHandler mRemoteTransitionHandler;
    private final ArrayList<Runnable> mRunWhenIdleQueue;
    /* access modifiers changed from: private */
    public float mTransitionAnimationScaleSetting;

    /* renamed from: com.android.wm.shell.transition.Transitions$TransitionFinishCallback */
    public interface TransitionFinishCallback {
        void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback);
    }

    /* renamed from: com.android.wm.shell.transition.Transitions$TransitionHandler */
    public interface TransitionHandler {
        WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo);

        void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, TransitionFinishCallback transitionFinishCallback) {
        }

        void onTransitionMerged(IBinder iBinder) {
        }

        void setAnimScaleSetting(float f) {
        }

        boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, TransitionFinishCallback transitionFinishCallback);
    }

    public static boolean isClosingType(int i) {
        return i == 2 || i == 4;
    }

    public static boolean isOpeningType(int i) {
        return i == 1 || i == 3 || i == 7;
    }

    static {
        boolean z = false;
        boolean z2 = SystemProperties.getBoolean("persist.wm.debug.shell_transit", false);
        ENABLE_SHELL_TRANSITIONS = z2;
        if (z2 && SystemProperties.getBoolean("persist.wm.debug.shell_transit_rotate", false)) {
            z = true;
        }
        SHELL_TRANSITIONS_ROTATION = z;
    }

    /* renamed from: com.android.wm.shell.transition.Transitions$ActiveTransition */
    private static final class ActiveTransition {
        boolean mAborted;
        SurfaceControl.Transaction mFinishT;
        TransitionHandler mHandler;
        TransitionInfo mInfo;
        boolean mMerged;
        SurfaceControl.Transaction mStartT;
        /* access modifiers changed from: package-private */
        public IBinder mToken;

        private ActiveTransition() {
        }
    }

    public Transitions(WindowOrganizer windowOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        ShellExecutor shellExecutor3 = shellExecutor;
        this.mImpl = new ShellTransitionImpl();
        ArrayList<TransitionHandler> arrayList = new ArrayList<>();
        this.mHandlers = arrayList;
        this.mRunWhenIdleQueue = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = windowOrganizer;
        this.mContext = context;
        this.mMainExecutor = shellExecutor3;
        ShellExecutor shellExecutor4 = shellExecutor2;
        this.mAnimExecutor = shellExecutor4;
        this.mDisplayController = displayController;
        this.mPlayerImpl = new TransitionPlayerImpl();
        arrayList.add(new DefaultTransitionHandler(displayController, transactionPool, context, shellExecutor, handler, shellExecutor4));
        RemoteTransitionHandler remoteTransitionHandler = new RemoteTransitionHandler(shellExecutor3);
        this.mRemoteTransitionHandler = remoteTransitionHandler;
        arrayList.add(remoteTransitionHandler);
        ContentResolver contentResolver = context.getContentResolver();
        float f = Settings.Global.getFloat(contentResolver, "transition_animation_scale", context.getResources().getFloat(17105063));
        this.mTransitionAnimationScaleSetting = f;
        dispatchAnimScaleSetting(f);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("transition_animation_scale"), false, new SettingsObserver());
    }

    private Transitions() {
        this.mImpl = new ShellTransitionImpl();
        this.mHandlers = new ArrayList<>();
        this.mRunWhenIdleQueue = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = null;
        this.mContext = null;
        this.mMainExecutor = null;
        this.mAnimExecutor = null;
        this.mDisplayController = null;
        this.mPlayerImpl = null;
        this.mRemoteTransitionHandler = null;
    }

    public ShellTransitions asRemoteTransitions() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    /* access modifiers changed from: private */
    public void dispatchAnimScaleSetting(float f) {
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            this.mHandlers.get(size).setAnimScaleSetting(f);
        }
    }

    public void register(ShellTaskOrganizer shellTaskOrganizer) {
        TransitionPlayerImpl transitionPlayerImpl = this.mPlayerImpl;
        if (transitionPlayerImpl != null) {
            shellTaskOrganizer.registerTransitionPlayer(transitionPlayerImpl);
            TransitionMetrics.getInstance();
        }
    }

    public void addHandler(TransitionHandler transitionHandler) {
        this.mHandlers.add(transitionHandler);
    }

    public ShellExecutor getMainExecutor() {
        return this.mMainExecutor;
    }

    public ShellExecutor getAnimExecutor() {
        return this.mAnimExecutor;
    }

    /* access modifiers changed from: package-private */
    public void replaceDefaultHandlerForTest(TransitionHandler transitionHandler) {
        this.mHandlers.set(0, transitionHandler);
    }

    public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
        this.mRemoteTransitionHandler.addFiltered(transitionFilter, remoteTransition);
    }

    public void unregisterRemote(RemoteTransition remoteTransition) {
        this.mRemoteTransitionHandler.removeFiltered(remoteTransition);
    }

    public void runOnIdle(Runnable runnable) {
        if (this.mActiveTransitions.isEmpty()) {
            runnable.run();
        } else {
            this.mRunWhenIdleQueue.add(runnable);
        }
    }

    private static void setupStartState(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (!TransitionInfo.isIndependent(change, transitionInfo)) {
                if (mode == 1 || mode == 3 || mode == 6) {
                    transaction.show(leash);
                    transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                    transaction.setAlpha(leash, 1.0f);
                    transaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
            } else if (mode == 1 || mode == 3) {
                transaction.show(leash);
                transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                if (isOpeningType && (change.getFlags() & 8) == 0) {
                    transaction.setAlpha(leash, 0.0f);
                    transaction2.setAlpha(leash, 1.0f);
                }
            } else if ((mode == 2 || mode == 4) && (change.getFlags() & 2) == 0) {
                transaction2.hide(leash);
            }
        }
    }

    private static void setupAnimHierarchy(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        if (transitionInfo.getRootLeash().isValid()) {
            transaction.show(transitionInfo.getRootLeash());
        }
        int size = transitionInfo.getChanges().size();
        for (int size2 = transitionInfo.getChanges().size() - 1; size2 >= 0; size2--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
            if (TransitionInfo.isIndependent(change, transitionInfo)) {
                if (!(change.getParent() != null)) {
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setPosition(leash, (float) (change.getStartAbsBounds().left - transitionInfo.getRootOffset().x), (float) (change.getStartAbsBounds().top - transitionInfo.getRootOffset().y));
                }
                if (mode == 1 || mode == 3) {
                    if (isOpeningType) {
                        transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                    } else {
                        transaction.setLayer(leash, size - size2);
                    }
                } else if (mode != 2 && mode != 4) {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                } else if (isOpeningType) {
                    transaction.setLayer(leash, size - size2);
                } else {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                }
            }
        }
    }

    private int findActiveTransition(IBinder iBinder) {
        for (int size = this.mActiveTransitions.size() - 1; size >= 0; size--) {
            if (this.mActiveTransitions.get(size).mToken == iBinder) {
                return size;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean z = true;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "onTransitionReady %s: %s", new Object[]{iBinder, transitionInfo});
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            throw new IllegalStateException("Got transitionReady for non-active transition " + iBinder + ". expecting one of " + Arrays.toString(this.mActiveTransitions.stream().map(new Transitions$$ExternalSyntheticLambda2()).toArray()));
        } else if (!transitionInfo.getRootLeash().isValid()) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Invalid root leash (%s): %s", new Object[]{iBinder, transitionInfo});
            transaction.apply();
            onAbort(iBinder);
        } else {
            int size = transitionInfo.getChanges().size();
            if (size == 2) {
                int i = size - 1;
                boolean z2 = false;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                    if (change.getTaskInfo() != null) {
                        z = false;
                        break;
                    }
                    if ((change.getFlags() & 8) != 0) {
                        z2 = true;
                    }
                    i--;
                }
                if (z && z2) {
                    transaction.apply();
                    onAbort(iBinder);
                    return;
                }
            }
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mInfo = transitionInfo;
            activeTransition.mStartT = transaction;
            activeTransition.mFinishT = transaction2;
            setupStartState(activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT);
            if (findActiveTransition > 0) {
                attemptMergeTransition(this.mActiveTransitions.get(0), activeTransition);
            } else {
                playTransition(activeTransition);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void attemptMergeTransition(ActiveTransition activeTransition, ActiveTransition activeTransition2) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Transition %s ready while another transition %s is still animating. Notify the animating transition in case they can be merged", new Object[]{activeTransition2.mToken, activeTransition.mToken});
        activeTransition.mHandler.mergeAnimation(activeTransition2.mToken, activeTransition2.mInfo, activeTransition2.mStartT, activeTransition.mToken, new Transitions$$ExternalSyntheticLambda0(this, activeTransition2));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$attemptMergeTransition$1$com-android-wm-shell-transition-Transitions */
    public /* synthetic */ void mo51283x97bba396(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    /* access modifiers changed from: package-private */
    public boolean startAnimation(ActiveTransition activeTransition, TransitionHandler transitionHandler) {
        return transitionHandler.startAnimation(activeTransition.mToken, activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT, new Transitions$$ExternalSyntheticLambda1(this, activeTransition));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startAnimation$2$com-android-wm-shell-transition-Transitions */
    public /* synthetic */ void mo51284xe84873b9(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    /* access modifiers changed from: package-private */
    public void playTransition(ActiveTransition activeTransition) {
        setupAnimHierarchy(activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT);
        if (activeTransition.mHandler != null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " try firstHandler %s", new Object[]{activeTransition.mHandler});
            if (startAnimation(activeTransition, activeTransition.mHandler)) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " animated by firstHandler", new Object[0]);
                return;
            }
        }
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            if (this.mHandlers.get(size) != activeTransition.mHandler) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " try handler %s", new Object[]{this.mHandlers.get(size)});
                if (startAnimation(activeTransition, this.mHandlers.get(size))) {
                    ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " animated by %s", new Object[]{this.mHandlers.get(size)});
                    activeTransition.mHandler = this.mHandlers.get(size);
                    return;
                }
            }
        }
        throw new IllegalStateException("This shouldn't happen, maybe the default handler is broken.");
    }

    private void onAbort(IBinder iBinder) {
        onFinish(iBinder, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null, true);
    }

    private void onFinish(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(iBinder, windowContainerTransaction, windowContainerTransactionCallback, false);
    }

    private void onFinish(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback, boolean z) {
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            Log.e(TAG, "Trying to finish a non-running transition. Either remote crashed or  a handler didn't properly deal with a merge.", new RuntimeException());
            return;
        }
        if (findActiveTransition > 0) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Transition was merged (abort=%b: %s", new Object[]{Boolean.valueOf(z), iBinder});
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mMerged = true;
            activeTransition.mAborted = z;
            if (activeTransition.mHandler != null) {
                activeTransition.mHandler.onTransitionMerged(activeTransition.mToken);
                return;
            }
            return;
        }
        this.mActiveTransitions.get(findActiveTransition).mAborted = z;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Transition animation finished (abort=%b), notifying core %s", new Object[]{Boolean.valueOf(z), iBinder});
        SurfaceControl.Transaction transaction = this.mActiveTransitions.get(findActiveTransition).mFinishT;
        int i = findActiveTransition + 1;
        while (i < this.mActiveTransitions.size() && this.mActiveTransitions.get(i).mMerged && this.mActiveTransitions.get(i).mStartT != null) {
            if (transaction == null) {
                transaction = new SurfaceControl.Transaction();
            }
            transaction.merge(this.mActiveTransitions.get(i).mStartT);
            transaction.merge(this.mActiveTransitions.get(i).mFinishT);
            i++;
        }
        if (transaction != null) {
            transaction.apply();
        }
        this.mActiveTransitions.remove(findActiveTransition);
        this.mOrganizer.finishTransition(iBinder, windowContainerTransaction, windowContainerTransactionCallback);
        while (findActiveTransition < this.mActiveTransitions.size() && this.mActiveTransitions.get(findActiveTransition).mMerged) {
            this.mOrganizer.finishTransition(this.mActiveTransitions.remove(findActiveTransition).mToken, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        while (this.mActiveTransitions.size() > findActiveTransition && this.mActiveTransitions.get(findActiveTransition).mAborted) {
            this.mOrganizer.finishTransition(this.mActiveTransitions.remove(findActiveTransition).mToken, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        if (this.mActiveTransitions.size() <= findActiveTransition) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "All active transition animations finished", new Object[0]);
            for (int i2 = 0; i2 < this.mRunWhenIdleQueue.size(); i2++) {
                this.mRunWhenIdleQueue.get(i2).run();
            }
            this.mRunWhenIdleQueue.clear();
            return;
        }
        ActiveTransition activeTransition2 = this.mActiveTransitions.get(findActiveTransition);
        if (activeTransition2.mInfo == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Pending transition after one finished, but it isn't ready yet.", new Object[0]);
            return;
        }
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Pending transitions after one finished, so start the next one.", new Object[0]);
        playTransition(activeTransition2);
        int findActiveTransition2 = findActiveTransition(activeTransition2.mToken);
        if (findActiveTransition2 >= 0) {
            int i3 = findActiveTransition2 + 1;
            while (i3 < this.mActiveTransitions.size()) {
                ActiveTransition activeTransition3 = this.mActiveTransitions.get(i3);
                if (!activeTransition3.mAborted) {
                    if (!activeTransition3.mMerged) {
                        attemptMergeTransition(activeTransition2, activeTransition3);
                        i3 = findActiveTransition(activeTransition3.mToken);
                        if (i3 < 0) {
                            return;
                        }
                    } else {
                        throw new IllegalStateException("Can't merge a transition after not-merging a preceding one.");
                    }
                }
                i3++;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Transition requested: %s %s", new Object[]{iBinder, transitionRequestInfo});
        if (findActiveTransition(iBinder) < 0) {
            WindowContainerTransaction windowContainerTransaction = null;
            ActiveTransition activeTransition = new ActiveTransition();
            int size = this.mHandlers.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                windowContainerTransaction = this.mHandlers.get(size).handleRequest(iBinder, transitionRequestInfo);
                if (windowContainerTransaction != null) {
                    activeTransition.mHandler = this.mHandlers.get(size);
                    break;
                }
                size--;
            }
            if (transitionRequestInfo.getDisplayChange() != null) {
                TransitionRequestInfo.DisplayChange displayChange = transitionRequestInfo.getDisplayChange();
                if (displayChange.getEndRotation() != displayChange.getStartRotation()) {
                    if (windowContainerTransaction == null) {
                        windowContainerTransaction = new WindowContainerTransaction();
                    }
                    this.mDisplayController.getChangeController().dispatchOnRotateDisplay(windowContainerTransaction, displayChange.getDisplayId(), displayChange.getStartRotation(), displayChange.getEndRotation());
                }
            }
            activeTransition.mToken = this.mOrganizer.startTransition(transitionRequestInfo.getType(), iBinder, windowContainerTransaction);
            this.mActiveTransitions.add(activeTransition);
            return;
        }
        throw new RuntimeException("Transition already started " + iBinder);
    }

    public IBinder startTransition(int i, WindowContainerTransaction windowContainerTransaction, TransitionHandler transitionHandler) {
        ActiveTransition activeTransition = new ActiveTransition();
        activeTransition.mHandler = transitionHandler;
        activeTransition.mToken = this.mOrganizer.startTransition(i, (IBinder) null, windowContainerTransaction);
        this.mActiveTransitions.add(activeTransition);
        return activeTransition.mToken;
    }

    /* renamed from: com.android.wm.shell.transition.Transitions$TransitionPlayerImpl */
    private class TransitionPlayerImpl extends ITransitionPlayer.Stub {
        private TransitionPlayerImpl() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onTransitionReady$0$com-android-wm-shell-transition-Transitions$TransitionPlayerImpl */
        public /* synthetic */ void mo51300x7172b0d0(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
            Transitions.this.onTransitionReady(iBinder, transitionInfo, transaction, transaction2);
        }

        public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0(this, iBinder, transitionInfo, transaction, transaction2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$requestStartTransition$1$com-android-wm-shell-transition-Transitions$TransitionPlayerImpl */
        public /* synthetic */ void mo51301x5912739c(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
            Transitions.this.requestStartTransition(iBinder, transitionRequestInfo);
        }

        public void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda1(this, iBinder, transitionRequestInfo));
        }
    }

    @ExternalThread
    /* renamed from: com.android.wm.shell.transition.Transitions$ShellTransitionImpl */
    private class ShellTransitionImpl implements ShellTransitions {
        private IShellTransitionsImpl mIShellTransitions;

        private ShellTransitionImpl() {
        }

        public IShellTransitions createExternalInterface() {
            IShellTransitionsImpl iShellTransitionsImpl = this.mIShellTransitions;
            if (iShellTransitionsImpl != null) {
                iShellTransitionsImpl.invalidate();
            }
            IShellTransitionsImpl iShellTransitionsImpl2 = new IShellTransitionsImpl(Transitions.this);
            this.mIShellTransitions = iShellTransitionsImpl2;
            return iShellTransitionsImpl2;
        }

        public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            Transitions.this.mMainExecutor.execute(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(this, transitionFilter, remoteTransition));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerRemote$0$com-android-wm-shell-transition-Transitions$ShellTransitionImpl */
        public /* synthetic */ void mo51298x153a0b9f(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            Transitions.this.mRemoteTransitionHandler.addFiltered(transitionFilter, remoteTransition);
        }

        public void unregisterRemote(RemoteTransition remoteTransition) {
            Transitions.this.mMainExecutor.execute(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda1(this, remoteTransition));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterRemote$1$com-android-wm-shell-transition-Transitions$ShellTransitionImpl */
        public /* synthetic */ void mo51299x95961cf9(RemoteTransition remoteTransition) {
            Transitions.this.mRemoteTransitionHandler.removeFiltered(remoteTransition);
        }
    }

    /* renamed from: com.android.wm.shell.transition.Transitions$IShellTransitionsImpl */
    private static class IShellTransitionsImpl extends IShellTransitions.Stub {
        private Transitions mTransitions;

        IShellTransitionsImpl(Transitions transitions) {
            this.mTransitions = transitions;
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mTransitions = null;
        }

        public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "registerRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0(transitionFilter, remoteTransition));
        }

        public void unregisterRemote(RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "unregisterRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(remoteTransition));
        }
    }

    /* renamed from: com.android.wm.shell.transition.Transitions$SettingsObserver */
    private class SettingsObserver extends ContentObserver {
        SettingsObserver() {
            super((Handler) null);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            Transitions transitions = Transitions.this;
            float unused = transitions.mTransitionAnimationScaleSetting = Settings.Global.getFloat(transitions.mContext.getContentResolver(), "transition_animation_scale", Transitions.this.mTransitionAnimationScaleSetting);
            Transitions.this.mMainExecutor.execute(new Transitions$SettingsObserver$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onChange$0$com-android-wm-shell-transition-Transitions$SettingsObserver */
        public /* synthetic */ void mo51296x7f68e1f9() {
            Transitions transitions = Transitions.this;
            transitions.dispatchAnimScaleSetting(transitions.mTransitionAnimationScaleSetting);
        }
    }
}
