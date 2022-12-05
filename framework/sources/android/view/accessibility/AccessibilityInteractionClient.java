package android.view.accessibility;

import android.accessibilityservice.IAccessibilityServiceConnection;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityCache;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes3.dex */
public final class AccessibilityInteractionClient extends IAccessibilityInteractionConnectionCallback.Stub {
    public static final String CALL_STACK = "call_stack";
    private static final boolean CHECK_INTEGRITY = true;
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AccessibilityInteractionClient";
    public static final int NO_ID = -1;
    private static final long TIMEOUT_INTERACTION_MILLIS = 5000;
    private final AccessibilityManager mAccessibilityManager;
    private volatile int mCallingUid;
    private int mConnectionIdWaitingForPrefetchResult;
    private AccessibilityNodeInfo mFindAccessibilityNodeInfoResult;
    private List<AccessibilityNodeInfo> mFindAccessibilityNodeInfosResult;
    private final Object mInstanceLock;
    private volatile int mInteractionId;
    private final AtomicInteger mInteractionIdCounter;
    private int mInteractionIdWaitingForPrefetchResult;
    private String[] mPackageNamesForNextPrefetchResult;
    private boolean mPerformAccessibilityActionResult;
    private Message mSameThreadMessage;
    private static final long DISABLE_PREFETCHING_FOR_SCROLLING_MILLIS = (long) (ViewConfiguration.getSendRecurringAccessibilityEventsInterval() * 1.5d);
    private static final Object sStaticLock = new Object();
    private static final LongSparseArray<AccessibilityInteractionClient> sClients = new LongSparseArray<>();
    private static final SparseArray<IAccessibilityServiceConnection> sConnectionCache = new SparseArray<>();
    private static final SparseLongArray sScrollingWindows = new SparseLongArray();
    private static AccessibilityCache sAccessibilityCache = new AccessibilityCache(new AccessibilityCache.AccessibilityNodeRefresher());

    public static AccessibilityInteractionClient getInstance() {
        long threadId = Thread.currentThread().getId();
        return getInstanceForThread(threadId);
    }

    public static AccessibilityInteractionClient getInstanceForThread(long threadId) {
        AccessibilityInteractionClient client;
        synchronized (sStaticLock) {
            LongSparseArray<AccessibilityInteractionClient> longSparseArray = sClients;
            client = longSparseArray.get(threadId);
            if (client == null) {
                client = new AccessibilityInteractionClient();
                longSparseArray.put(threadId, client);
            }
        }
        return client;
    }

    public static AccessibilityInteractionClient getInstance(Context context) {
        long threadId = Thread.currentThread().getId();
        if (context != null) {
            return getInstanceForThread(threadId, context);
        }
        return getInstanceForThread(threadId);
    }

    public static AccessibilityInteractionClient getInstanceForThread(long threadId, Context context) {
        AccessibilityInteractionClient client;
        synchronized (sStaticLock) {
            LongSparseArray<AccessibilityInteractionClient> longSparseArray = sClients;
            client = longSparseArray.get(threadId);
            if (client == null) {
                client = new AccessibilityInteractionClient(context);
                longSparseArray.put(threadId, client);
            }
        }
        return client;
    }

    public static IAccessibilityServiceConnection getConnection(int connectionId) {
        IAccessibilityServiceConnection iAccessibilityServiceConnection;
        SparseArray<IAccessibilityServiceConnection> sparseArray = sConnectionCache;
        synchronized (sparseArray) {
            iAccessibilityServiceConnection = sparseArray.get(connectionId);
        }
        return iAccessibilityServiceConnection;
    }

    public static void addConnection(int connectionId, IAccessibilityServiceConnection connection) {
        SparseArray<IAccessibilityServiceConnection> sparseArray = sConnectionCache;
        synchronized (sparseArray) {
            sparseArray.put(connectionId, connection);
        }
    }

    public static void removeConnection(int connectionId) {
        SparseArray<IAccessibilityServiceConnection> sparseArray = sConnectionCache;
        synchronized (sparseArray) {
            sparseArray.remove(connectionId);
        }
    }

    public static void setCache(AccessibilityCache cache) {
        sAccessibilityCache = cache;
    }

    private AccessibilityInteractionClient() {
        this.mInteractionIdCounter = new AtomicInteger();
        this.mInstanceLock = new Object();
        this.mInteractionId = -1;
        this.mCallingUid = -1;
        this.mInteractionIdWaitingForPrefetchResult = -1;
        this.mAccessibilityManager = null;
    }

    private AccessibilityInteractionClient(Context context) {
        this.mInteractionIdCounter = new AtomicInteger();
        this.mInstanceLock = new Object();
        this.mInteractionId = -1;
        this.mCallingUid = -1;
        this.mInteractionIdWaitingForPrefetchResult = -1;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
    }

    public void setSameThreadMessage(Message message) {
        synchronized (this.mInstanceLock) {
            this.mSameThreadMessage = message;
            this.mInstanceLock.notifyAll();
        }
    }

    public AccessibilityNodeInfo getRootInActiveWindow(int connectionId) {
        return findAccessibilityNodeInfoByAccessibilityId(connectionId, Integer.MAX_VALUE, AccessibilityNodeInfo.ROOT_NODE_ID, false, 4, (Bundle) null);
    }

    public AccessibilityWindowInfo getWindow(int connectionId, int accessibilityWindowId) {
        return getWindow(connectionId, accessibilityWindowId, false);
    }

    public AccessibilityWindowInfo getWindow(int connectionId, int accessibilityWindowId, boolean bypassCache) {
        AccessibilityWindowInfo window;
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                if (!bypassCache && (window = sAccessibilityCache.getWindow(accessibilityWindowId)) != null) {
                    return window;
                }
                long identityToken = Binder.clearCallingIdentity();
                AccessibilityWindowInfo window2 = connection.getWindow(accessibilityWindowId);
                Binder.restoreCallingIdentity(identityToken);
                if (window2 != null) {
                    if (!bypassCache) {
                        sAccessibilityCache.addWindow(window2);
                    }
                    return window2;
                }
                return null;
            }
            return null;
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while calling remote getWindow", re);
            return null;
        }
    }

    public List<AccessibilityWindowInfo> getWindows(int connectionId) {
        SparseArray<List<AccessibilityWindowInfo>> windows = getWindowsOnAllDisplays(connectionId);
        if (windows.size() > 0) {
            return windows.valueAt(0);
        }
        return Collections.emptyList();
    }

    public SparseArray<List<AccessibilityWindowInfo>> getWindowsOnAllDisplays(int connectionId) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                SparseArray<List<AccessibilityWindowInfo>> windows = sAccessibilityCache.getWindowsOnAllDisplays();
                if (windows != null) {
                    return windows;
                }
                long identityToken = Binder.clearCallingIdentity();
                SparseArray<List<AccessibilityWindowInfo>> windows2 = connection.getWindows();
                Binder.restoreCallingIdentity(identityToken);
                if (windows2 != null) {
                    sAccessibilityCache.setWindowsOnAllDisplays(windows2);
                    return windows2;
                }
            }
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while calling remote getWindowsOnAllDisplays", re);
        }
        SparseArray<List<AccessibilityWindowInfo>> emptyWindows = new SparseArray<>();
        return emptyWindows;
    }

    public AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int connectionId, IBinder leashToken, long accessibilityNodeId, boolean bypassCache, int prefetchFlags, Bundle arguments) {
        if (leashToken == null) {
            return null;
        }
        int windowId = -1;
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                windowId = connection.getWindowIdForLeashToken(leashToken);
            }
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while calling remote getWindowIdForLeashToken", re);
        }
        if (windowId != -1) {
            return findAccessibilityNodeInfoByAccessibilityId(connectionId, windowId, accessibilityNodeId, bypassCache, prefetchFlags, arguments);
        }
        return null;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(5:(6:(3:76|77|(13:79|17|18|19|20|21|22|23|24|25|26|27|(6:29|(2:31|(9:33|34|35|36|37|38|39|40|41)(1:58))(1:59)|42|(1:45)|46|47)(2:60|61)))|24|25|26|27|(0)(0))|20|21|22|23) */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0132, code lost:
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0088 A[Catch: RemoteException -> 0x011f, TryCatch #6 {RemoteException -> 0x011f, blocks: (B:27:0x0081, B:29:0x0088, B:31:0x0090, B:33:0x0096), top: B:26:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0115  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int connectionId, int accessibilityWindowId, long accessibilityNodeId, boolean bypassCache, int prefetchFlags, Bundle arguments) {
        int prefetchFlags2;
        int prefetchFlags3;
        long identityToken;
        String[] packageNames;
        int interactionId;
        int prefetchFlags4;
        if ((prefetchFlags & 2) == 0 || (prefetchFlags & 1) != 0) {
            try {
                IAccessibilityServiceConnection connection = getConnection(connectionId);
                if (connection == null) {
                    return null;
                }
                if (!bypassCache) {
                    try {
                        AccessibilityNodeInfo cachedInfo = sAccessibilityCache.getNode(accessibilityWindowId, accessibilityNodeId);
                        if (cachedInfo != null) {
                            return cachedInfo;
                        }
                        prefetchFlags2 = prefetchFlags;
                    } catch (RemoteException e) {
                        re = e;
                        Log.e(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByAccessibilityId", re);
                        return null;
                    }
                } else {
                    prefetchFlags2 = prefetchFlags & (-8);
                }
                try {
                    try {
                        try {
                            try {
                                if ((prefetchFlags2 & 7) != 0) {
                                    try {
                                        if (isWindowScrolling(accessibilityWindowId)) {
                                            prefetchFlags3 = prefetchFlags2 & (-8);
                                            int interactionId2 = this.mInteractionIdCounter.getAndIncrement();
                                            identityToken = Binder.clearCallingIdentity();
                                            int prefetchFlags5 = prefetchFlags3;
                                            packageNames = connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityWindowId, accessibilityNodeId, interactionId2, this, prefetchFlags3, Thread.currentThread().getId(), arguments);
                                            Binder.restoreCallingIdentity(identityToken);
                                            if (packageNames != null) {
                                                return null;
                                            }
                                            AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(interactionId2);
                                            AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                                            if (accessibilityManager == null) {
                                                interactionId = interactionId2;
                                                prefetchFlags4 = prefetchFlags5;
                                            } else if (accessibilityManager.isAccessibilityTracingEnabled()) {
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("InteractionId:");
                                                sb.append(interactionId2);
                                                sb.append(";Result: ");
                                                sb.append(info);
                                                sb.append(";connectionId=");
                                                sb.append(connectionId);
                                                sb.append(";accessibilityWindowId=");
                                                sb.append(accessibilityWindowId);
                                                sb.append(";accessibilityNodeId=");
                                                interactionId = interactionId2;
                                                try {
                                                    sb.append(accessibilityNodeId);
                                                    sb.append(";bypassCache=");
                                                    sb.append(bypassCache);
                                                    sb.append(";prefetchFlags=");
                                                    prefetchFlags4 = prefetchFlags5;
                                                } catch (RemoteException e2) {
                                                    re = e2;
                                                    Log.e(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByAccessibilityId", re);
                                                    return null;
                                                }
                                                try {
                                                    sb.append(prefetchFlags4);
                                                    sb.append(";arguments=");
                                                    sb.append(arguments);
                                                    logTrace(connection, "findAccessibilityNodeInfoByAccessibilityId", sb.toString());
                                                } catch (RemoteException e3) {
                                                    re = e3;
                                                    Log.e(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByAccessibilityId", re);
                                                    return null;
                                                }
                                            } else {
                                                interactionId = interactionId2;
                                                prefetchFlags4 = prefetchFlags5;
                                            }
                                            if ((prefetchFlags4 & 7) != 0 && info != null) {
                                                setInteractionWaitingForPrefetchResult(interactionId, connectionId, packageNames);
                                            }
                                            finalizeAndCacheAccessibilityNodeInfo(info, connectionId, bypassCache, packageNames);
                                            return info;
                                        }
                                    } catch (RemoteException e4) {
                                        re = e4;
                                        Log.e(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByAccessibilityId", re);
                                        return null;
                                    }
                                }
                                Binder.restoreCallingIdentity(identityToken);
                                if (packageNames != null) {
                                }
                            } catch (RemoteException e5) {
                                re = e5;
                            }
                            packageNames = connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityWindowId, accessibilityNodeId, interactionId2, this, prefetchFlags3, Thread.currentThread().getId(), arguments);
                        } catch (Throwable th) {
                            th = th;
                            Binder.restoreCallingIdentity(identityToken);
                            throw th;
                        }
                        int prefetchFlags52 = prefetchFlags3;
                    } catch (RemoteException e6) {
                        re = e6;
                    }
                    int interactionId22 = this.mInteractionIdCounter.getAndIncrement();
                    identityToken = Binder.clearCallingIdentity();
                } catch (RemoteException e7) {
                    re = e7;
                }
                prefetchFlags3 = prefetchFlags2;
            } catch (RemoteException e8) {
                re = e8;
            }
        } else {
            throw new IllegalArgumentException("FLAG_PREFETCH_SIBLINGS requires FLAG_PREFETCH_PREDECESSORS");
        }
    }

    private void setInteractionWaitingForPrefetchResult(int interactionId, int connectionId, String[] packageNames) {
        synchronized (this.mInstanceLock) {
            this.mInteractionIdWaitingForPrefetchResult = interactionId;
            this.mConnectionIdWaitingForPrefetchResult = connectionId;
            this.mPackageNamesForNextPrefetchResult = packageNames;
        }
    }

    private static String idToString(int accessibilityWindowId, long accessibilityNodeId) {
        return accessibilityWindowId + "/" + AccessibilityNodeInfo.idToString(accessibilityNodeId);
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(int connectionId, int accessibilityWindowId, long accessibilityNodeId, String viewId) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                int interactionId = this.mInteractionIdCounter.getAndIncrement();
                long identityToken = Binder.clearCallingIdentity();
                try {
                    String[] packageNames = connection.findAccessibilityNodeInfosByViewId(accessibilityWindowId, accessibilityNodeId, viewId, interactionId, this, Thread.currentThread().getId());
                    Binder.restoreCallingIdentity(identityToken);
                    if (packageNames != null) {
                        List<AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(interactionId);
                        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                        if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("InteractionId=");
                            sb.append(interactionId);
                            sb.append(":Result: ");
                            sb.append(infos);
                            sb.append(";connectionId=");
                            sb.append(connectionId);
                            sb.append(";accessibilityWindowId=");
                            try {
                                sb.append(accessibilityWindowId);
                                sb.append(";accessibilityNodeId=");
                            } catch (RemoteException e) {
                                re = e;
                                Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByViewIdInActiveWindow", re);
                                return Collections.emptyList();
                            }
                            try {
                                sb.append(accessibilityNodeId);
                                sb.append(";viewId=");
                                sb.append(viewId);
                                logTrace(connection, "findAccessibilityNodeInfosByViewId", sb.toString());
                            } catch (RemoteException e2) {
                                re = e2;
                                Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByViewIdInActiveWindow", re);
                                return Collections.emptyList();
                            }
                        }
                        if (infos != null) {
                            finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, false, packageNames);
                            return infos;
                        }
                    }
                } catch (RemoteException e3) {
                    re = e3;
                    Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfoByViewIdInActiveWindow", re);
                    return Collections.emptyList();
                }
            }
        } catch (RemoteException e4) {
            re = e4;
        }
        return Collections.emptyList();
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(int connectionId, int accessibilityWindowId, long accessibilityNodeId, String text) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                int interactionId = this.mInteractionIdCounter.getAndIncrement();
                long identityToken = Binder.clearCallingIdentity();
                try {
                    String[] packageNames = connection.findAccessibilityNodeInfosByText(accessibilityWindowId, accessibilityNodeId, text, interactionId, this, Thread.currentThread().getId());
                    Binder.restoreCallingIdentity(identityToken);
                    if (packageNames != null) {
                        List<AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(interactionId);
                        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                        if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("InteractionId=");
                            sb.append(interactionId);
                            sb.append(":Result: ");
                            sb.append(infos);
                            sb.append(";connectionId=");
                            sb.append(connectionId);
                            sb.append(";accessibilityWindowId=");
                            try {
                                sb.append(accessibilityWindowId);
                                sb.append(";accessibilityNodeId=");
                            } catch (RemoteException e) {
                                re = e;
                                Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfosByViewText", re);
                                return Collections.emptyList();
                            }
                            try {
                                sb.append(accessibilityNodeId);
                                sb.append(";text=");
                                sb.append(text);
                                logTrace(connection, "findAccessibilityNodeInfosByText", sb.toString());
                            } catch (RemoteException e2) {
                                re = e2;
                                Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfosByViewText", re);
                                return Collections.emptyList();
                            }
                        }
                        if (infos != null) {
                            finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, false, packageNames);
                            return infos;
                        }
                    }
                } catch (RemoteException e3) {
                    re = e3;
                    Log.w(LOG_TAG, "Error while calling remote findAccessibilityNodeInfosByViewText", re);
                    return Collections.emptyList();
                }
            }
        } catch (RemoteException e4) {
            re = e4;
        }
        return Collections.emptyList();
    }

    public AccessibilityNodeInfo findFocus(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int focusType) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                int interactionId = this.mInteractionIdCounter.getAndIncrement();
                long identityToken = Binder.clearCallingIdentity();
                try {
                    String[] packageNames = connection.findFocus(accessibilityWindowId, accessibilityNodeId, focusType, interactionId, this, Thread.currentThread().getId());
                    Binder.restoreCallingIdentity(identityToken);
                    if (packageNames != null) {
                        AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(interactionId);
                        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                        if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("InteractionId=");
                            sb.append(interactionId);
                            sb.append(":Result: ");
                            sb.append(info);
                            sb.append(";connectionId=");
                            sb.append(connectionId);
                            sb.append(";accessibilityWindowId=");
                            try {
                                sb.append(accessibilityWindowId);
                                sb.append(";accessibilityNodeId=");
                            } catch (RemoteException e) {
                                re = e;
                                Log.w(LOG_TAG, "Error while calling remote findFocus", re);
                                return null;
                            }
                            try {
                                sb.append(accessibilityNodeId);
                                sb.append(";focusType=");
                                sb.append(focusType);
                                logTrace(connection, "findFocus", sb.toString());
                            } catch (RemoteException e2) {
                                re = e2;
                                Log.w(LOG_TAG, "Error while calling remote findFocus", re);
                                return null;
                            }
                        }
                        finalizeAndCacheAccessibilityNodeInfo(info, connectionId, false, packageNames);
                        return info;
                    }
                    return null;
                } catch (RemoteException e3) {
                    re = e3;
                    Log.w(LOG_TAG, "Error while calling remote findFocus", re);
                    return null;
                }
            }
            return null;
        } catch (RemoteException e4) {
            re = e4;
        }
    }

    public AccessibilityNodeInfo focusSearch(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int direction) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                int interactionId = this.mInteractionIdCounter.getAndIncrement();
                long identityToken = Binder.clearCallingIdentity();
                try {
                    String[] packageNames = connection.focusSearch(accessibilityWindowId, accessibilityNodeId, direction, interactionId, this, Thread.currentThread().getId());
                    Binder.restoreCallingIdentity(identityToken);
                    if (packageNames != null) {
                        AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(interactionId);
                        finalizeAndCacheAccessibilityNodeInfo(info, connectionId, false, packageNames);
                        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                        if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("InteractionId=");
                            sb.append(interactionId);
                            sb.append(":Result: ");
                            sb.append(info);
                            sb.append(";connectionId=");
                            sb.append(connectionId);
                            sb.append(";accessibilityWindowId=");
                            try {
                                sb.append(accessibilityWindowId);
                                sb.append(";accessibilityNodeId=");
                            } catch (RemoteException e) {
                                re = e;
                                Log.w(LOG_TAG, "Error while calling remote accessibilityFocusSearch", re);
                                return null;
                            }
                            try {
                                sb.append(accessibilityNodeId);
                                sb.append(";direction=");
                                sb.append(direction);
                                logTrace(connection, "focusSearch", sb.toString());
                            } catch (RemoteException e2) {
                                re = e2;
                                Log.w(LOG_TAG, "Error while calling remote accessibilityFocusSearch", re);
                                return null;
                            }
                        }
                        return info;
                    }
                    return null;
                } catch (RemoteException e3) {
                    re = e3;
                    Log.w(LOG_TAG, "Error while calling remote accessibilityFocusSearch", re);
                    return null;
                }
            }
            return null;
        } catch (RemoteException e4) {
            re = e4;
        }
    }

    public boolean performAccessibilityAction(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int action, Bundle arguments) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                int interactionId = this.mInteractionIdCounter.getAndIncrement();
                long identityToken = Binder.clearCallingIdentity();
                try {
                    boolean success = connection.performAccessibilityAction(accessibilityWindowId, accessibilityNodeId, action, arguments, interactionId, this, Thread.currentThread().getId());
                    Binder.restoreCallingIdentity(identityToken);
                    if (success) {
                        boolean result = getPerformAccessibilityActionResultAndClear(interactionId);
                        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                        if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("InteractionId=");
                            sb.append(interactionId);
                            sb.append(":Result: ");
                            sb.append(result);
                            sb.append(";connectionId=");
                            try {
                                sb.append(connectionId);
                                sb.append(";accessibilityWindowId=");
                                try {
                                    sb.append(accessibilityWindowId);
                                    sb.append(";accessibilityNodeId=");
                                } catch (RemoteException e) {
                                    re = e;
                                    Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
                                    return false;
                                }
                            } catch (RemoteException e2) {
                                re = e2;
                                Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
                                return false;
                            }
                            try {
                                sb.append(accessibilityNodeId);
                                sb.append(";action=");
                                try {
                                    sb.append(action);
                                    sb.append(";arguments=");
                                    sb.append(arguments);
                                    logTrace(connection, "performAccessibilityAction", sb.toString());
                                } catch (RemoteException e3) {
                                    re = e3;
                                    Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
                                    return false;
                                }
                            } catch (RemoteException e4) {
                                re = e4;
                                Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
                                return false;
                            }
                        }
                        return result;
                    }
                    return false;
                } catch (RemoteException e5) {
                    re = e5;
                    Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
                    return false;
                }
            }
            return false;
        } catch (RemoteException e6) {
            re = e6;
        }
    }

    public void clearCache() {
        sAccessibilityCache.clear();
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case 4096:
                updateScrollingWindow(event.getWindowId(), SystemClock.uptimeMillis());
                break;
            case 4194304:
                if (event.getWindowChanges() == 2) {
                    deleteScrollingWindow(event.getWindowId());
                    break;
                }
                break;
        }
        sAccessibilityCache.onAccessibilityEvent(event);
    }

    private AccessibilityNodeInfo getFindAccessibilityNodeInfoResultAndClear(int interactionId) {
        AccessibilityNodeInfo result;
        synchronized (this.mInstanceLock) {
            boolean success = waitForResultTimedLocked(interactionId);
            result = success ? this.mFindAccessibilityNodeInfoResult : null;
            clearResultLocked();
        }
        return result;
    }

    @Override // android.view.accessibility.IAccessibilityInteractionConnectionCallback
    public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo info, int interactionId) {
        synchronized (this.mInstanceLock) {
            if (interactionId > this.mInteractionId) {
                this.mFindAccessibilityNodeInfoResult = info;
                this.mInteractionId = interactionId;
                this.mCallingUid = Binder.getCallingUid();
            }
            this.mInstanceLock.notifyAll();
        }
    }

    private List<AccessibilityNodeInfo> getFindAccessibilityNodeInfosResultAndClear(int interactionId) {
        List<AccessibilityNodeInfo> result;
        synchronized (this.mInstanceLock) {
            boolean success = waitForResultTimedLocked(interactionId);
            if (success) {
                result = this.mFindAccessibilityNodeInfosResult;
            } else {
                result = Collections.emptyList();
            }
            clearResultLocked();
            if (Build.IS_DEBUGGABLE) {
                checkFindAccessibilityNodeInfoResultIntegrity(result);
            }
        }
        return result;
    }

    @Override // android.view.accessibility.IAccessibilityInteractionConnectionCallback
    public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> infos, int interactionId) {
        synchronized (this.mInstanceLock) {
            if (interactionId > this.mInteractionId) {
                if (infos != null) {
                    boolean isIpcCall = Binder.getCallingPid() != Process.myPid();
                    if (!isIpcCall) {
                        this.mFindAccessibilityNodeInfosResult = new ArrayList(infos);
                    } else {
                        this.mFindAccessibilityNodeInfosResult = infos;
                    }
                } else {
                    this.mFindAccessibilityNodeInfosResult = Collections.emptyList();
                }
                this.mInteractionId = interactionId;
                this.mCallingUid = Binder.getCallingUid();
            }
            this.mInstanceLock.notifyAll();
        }
    }

    @Override // android.view.accessibility.IAccessibilityInteractionConnectionCallback
    public void setPrefetchAccessibilityNodeInfoResult(List<AccessibilityNodeInfo> infos, int interactionId) {
        int interactionIdWaitingForPrefetchResultCopy = -1;
        int connectionIdWaitingForPrefetchResultCopy = -1;
        String[] packageNamesForNextPrefetchResultCopy = null;
        if (infos.isEmpty()) {
            return;
        }
        synchronized (this.mInstanceLock) {
            int i = this.mInteractionIdWaitingForPrefetchResult;
            if (i == interactionId) {
                interactionIdWaitingForPrefetchResultCopy = i;
                connectionIdWaitingForPrefetchResultCopy = this.mConnectionIdWaitingForPrefetchResult;
                String[] strArr = this.mPackageNamesForNextPrefetchResult;
                if (strArr != null) {
                    packageNamesForNextPrefetchResultCopy = new String[strArr.length];
                    int i2 = 0;
                    while (true) {
                        String[] strArr2 = this.mPackageNamesForNextPrefetchResult;
                        if (i2 >= strArr2.length) {
                            break;
                        }
                        packageNamesForNextPrefetchResultCopy[i2] = strArr2[i2];
                        i2++;
                    }
                }
            }
        }
        if (interactionIdWaitingForPrefetchResultCopy == interactionId) {
            finalizeAndCacheAccessibilityNodeInfos(infos, connectionIdWaitingForPrefetchResultCopy, false, packageNamesForNextPrefetchResultCopy);
            AccessibilityManager accessibilityManager = this.mAccessibilityManager;
            if (accessibilityManager != null && accessibilityManager.isAccessibilityTracingEnabled()) {
                IAccessibilityServiceConnection connection = getConnection(connectionIdWaitingForPrefetchResultCopy);
                logTrace(connection, "setPrefetchAccessibilityNodeInfoResult", "InteractionId:" + interactionId + ";Result: " + infos + ";connectionId=" + connectionIdWaitingForPrefetchResultCopy, Binder.getCallingUid());
            }
        }
    }

    private boolean getPerformAccessibilityActionResultAndClear(int interactionId) {
        boolean result;
        synchronized (this.mInstanceLock) {
            boolean success = waitForResultTimedLocked(interactionId);
            result = success ? this.mPerformAccessibilityActionResult : false;
            clearResultLocked();
        }
        return result;
    }

    @Override // android.view.accessibility.IAccessibilityInteractionConnectionCallback
    public void setPerformAccessibilityActionResult(boolean succeeded, int interactionId) {
        synchronized (this.mInstanceLock) {
            if (interactionId > this.mInteractionId) {
                this.mPerformAccessibilityActionResult = succeeded;
                this.mInteractionId = interactionId;
                this.mCallingUid = Binder.getCallingUid();
            }
            this.mInstanceLock.notifyAll();
        }
    }

    private void clearResultLocked() {
        this.mInteractionId = -1;
        this.mFindAccessibilityNodeInfoResult = null;
        this.mFindAccessibilityNodeInfosResult = null;
        this.mPerformAccessibilityActionResult = false;
    }

    private boolean waitForResultTimedLocked(int interactionId) {
        long startTimeMillis = SystemClock.uptimeMillis();
        while (true) {
            try {
                Message sameProcessMessage = getSameProcessMessageAndClear();
                if (sameProcessMessage != null) {
                    sameProcessMessage.getTarget().handleMessage(sameProcessMessage);
                }
            } catch (InterruptedException e) {
            }
            if (this.mInteractionId == interactionId) {
                return true;
            }
            if (this.mInteractionId > interactionId) {
                return false;
            }
            long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
            long waitTimeMillis = 5000 - elapsedTimeMillis;
            if (waitTimeMillis <= 0) {
                return false;
            }
            this.mInstanceLock.wait(waitTimeMillis);
        }
    }

    private void finalizeAndCacheAccessibilityNodeInfo(AccessibilityNodeInfo info, int connectionId, boolean bypassCache, String[] packageNames) {
        CharSequence packageName;
        if (info != null) {
            info.setConnectionId(connectionId);
            if (!ArrayUtils.isEmpty(packageNames) && ((packageName = info.getPackageName()) == null || !ArrayUtils.contains(packageNames, packageName.toString()))) {
                info.setPackageName(packageNames[0]);
            }
            info.setSealed(true);
            if (!bypassCache) {
                sAccessibilityCache.add(info);
            }
        }
    }

    private void finalizeAndCacheAccessibilityNodeInfos(List<AccessibilityNodeInfo> infos, int connectionId, boolean bypassCache, String[] packageNames) {
        if (infos != null) {
            int infosCount = infos.size();
            for (int i = 0; i < infosCount; i++) {
                AccessibilityNodeInfo info = infos.get(i);
                finalizeAndCacheAccessibilityNodeInfo(info, connectionId, bypassCache, packageNames);
            }
        }
    }

    private Message getSameProcessMessageAndClear() {
        Message result;
        synchronized (this.mInstanceLock) {
            result = this.mSameThreadMessage;
            this.mSameThreadMessage = null;
        }
        return result;
    }

    private void checkFindAccessibilityNodeInfoResultIntegrity(List<AccessibilityNodeInfo> infos) {
        if (infos.size() == 0) {
            return;
        }
        AccessibilityNodeInfo root = infos.get(0);
        int infoCount = infos.size();
        for (int i = 1; i < infoCount; i++) {
            int j = i;
            while (true) {
                if (j < infoCount) {
                    AccessibilityNodeInfo candidate = infos.get(j);
                    if (root.getParentNodeId() != candidate.getSourceNodeId()) {
                        j++;
                    } else {
                        root = candidate;
                        break;
                    }
                }
            }
        }
        if (root == null) {
            Log.e(LOG_TAG, "No root.");
        }
        HashSet<AccessibilityNodeInfo> seen = new HashSet<>();
        Queue<AccessibilityNodeInfo> fringe = new LinkedList<>();
        fringe.add(root);
        while (!fringe.isEmpty()) {
            AccessibilityNodeInfo current = fringe.poll();
            if (!seen.add(current)) {
                Log.e(LOG_TAG, "Duplicate node.");
                return;
            }
            int childCount = current.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                long childId = current.getChildId(i2);
                for (int j2 = 0; j2 < infoCount; j2++) {
                    AccessibilityNodeInfo child = infos.get(j2);
                    if (child.getSourceNodeId() == childId) {
                        fringe.add(child);
                    }
                }
            }
        }
        int disconnectedCount = infos.size() - seen.size();
        if (disconnectedCount > 0) {
            Log.e(LOG_TAG, disconnectedCount + " Disconnected nodes.");
        }
    }

    private void updateScrollingWindow(int windowId, long uptimeMillis) {
        SparseLongArray sparseLongArray = sScrollingWindows;
        synchronized (sparseLongArray) {
            sparseLongArray.put(windowId, uptimeMillis);
        }
    }

    private void deleteScrollingWindow(int windowId) {
        SparseLongArray sparseLongArray = sScrollingWindows;
        synchronized (sparseLongArray) {
            sparseLongArray.delete(windowId);
        }
    }

    private boolean isWindowScrolling(int windowId) {
        SparseLongArray sparseLongArray = sScrollingWindows;
        synchronized (sparseLongArray) {
            long latestScrollingTime = sparseLongArray.get(windowId);
            if (latestScrollingTime == 0) {
                return false;
            }
            long currentUptime = SystemClock.uptimeMillis();
            if (currentUptime > DISABLE_PREFETCHING_FOR_SCROLLING_MILLIS + latestScrollingTime) {
                sparseLongArray.delete(windowId);
                return false;
            }
            return true;
        }
    }

    private void logTrace(IAccessibilityServiceConnection connection, String method, String params, int callingUid) {
        try {
            Bundle b = new Bundle();
            ArrayList<StackTraceElement> callStack = new ArrayList<>(Arrays.asList(Thread.currentThread().getStackTrace()));
            b.putSerializable(CALL_STACK, callStack);
            long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            StringBuilder sb = new StringBuilder();
            sb.append("AccessibilityInteractionClient.callback for ");
            try {
                sb.append(method);
                connection.logTrace(elapsedRealtimeNanos, sb.toString(), params, Process.myPid(), Thread.currentThread().getId(), callingUid, b);
            } catch (RemoteException e) {
                e = e;
                Log.e(LOG_TAG, "Failed to log trace. " + e);
            }
        } catch (RemoteException e2) {
            e = e2;
        }
    }

    private void logTrace(IAccessibilityServiceConnection connection, String method, String params) {
        logTrace(connection, method, params, this.mCallingUid);
    }
}
