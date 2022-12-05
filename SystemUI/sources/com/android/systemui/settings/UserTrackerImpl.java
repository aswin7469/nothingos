package com.android.systemui.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.Assert;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
/* compiled from: UserTrackerImpl.kt */
/* loaded from: classes.dex */
public final class UserTrackerImpl extends BroadcastReceiver implements UserTracker, Dumpable {
    @NotNull
    private final Handler backgroundHandler;
    @NotNull
    private final Context context;
    @NotNull
    private final DumpManager dumpManager;
    private boolean initialized;
    @NotNull
    private final SynchronizedDelegate userContext$delegate;
    @NotNull
    private final SynchronizedDelegate userHandle$delegate;
    @NotNull
    private final SynchronizedDelegate userId$delegate;
    @NotNull
    private final UserManager userManager;
    @NotNull
    public static final Companion Companion = new Companion(null);
    static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(UserTrackerImpl.class), "userId", "getUserId()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(UserTrackerImpl.class), "userHandle", "getUserHandle()Landroid/os/UserHandle;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(UserTrackerImpl.class), "userContext", "getUserContext()Landroid/content/Context;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(UserTrackerImpl.class), "userProfiles", "getUserProfiles()Ljava/util/List;"))};
    @NotNull
    private final Object mutex = new Object();
    @NotNull
    private final SynchronizedDelegate userProfiles$delegate = new SynchronizedDelegate(CollectionsKt.emptyList());
    @NotNull
    private final List<DataItem> callbacks = new ArrayList();

    public UserTrackerImpl(@NotNull Context context, @NotNull UserManager userManager, @NotNull DumpManager dumpManager, @NotNull Handler backgroundHandler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(userManager, "userManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(backgroundHandler, "backgroundHandler");
        this.context = context;
        this.userManager = userManager;
        this.dumpManager = dumpManager;
        this.backgroundHandler = backgroundHandler;
        this.userId$delegate = new SynchronizedDelegate(Integer.valueOf(context.getUserId()));
        UserHandle user = context.getUser();
        Intrinsics.checkNotNullExpressionValue(user, "context.user");
        this.userHandle$delegate = new SynchronizedDelegate(user);
        this.userContext$delegate = new SynchronizedDelegate(context);
    }

    /* compiled from: UserTrackerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean getInitialized() {
        return this.initialized;
    }

    private void setUserId(int i) {
        this.userId$delegate.setValue(this, $$delegatedProperties[0], Integer.valueOf(i));
    }

    @Override // com.android.systemui.settings.UserTracker
    public int getUserId() {
        return ((Number) this.userId$delegate.getValue(this, $$delegatedProperties[0])).intValue();
    }

    private void setUserHandle(UserHandle userHandle) {
        this.userHandle$delegate.setValue(this, $$delegatedProperties[1], userHandle);
    }

    @Override // com.android.systemui.settings.UserTracker
    @NotNull
    public UserHandle getUserHandle() {
        return (UserHandle) this.userHandle$delegate.getValue(this, $$delegatedProperties[1]);
    }

    private void setUserContext(Context context) {
        this.userContext$delegate.setValue(this, $$delegatedProperties[2], context);
    }

    @Override // com.android.systemui.settings.UserContextProvider
    @NotNull
    public Context getUserContext() {
        return (Context) this.userContext$delegate.getValue(this, $$delegatedProperties[2]);
    }

    @Override // com.android.systemui.settings.UserTracker
    @NotNull
    public UserInfo getUserInfo() {
        boolean z;
        int userId = getUserId();
        for (UserInfo userInfo : getUserProfiles()) {
            if (userInfo.id == userId) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                return userInfo;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    private void setUserProfiles(List<? extends UserInfo> list) {
        this.userProfiles$delegate.setValue(this, $$delegatedProperties[3], list);
    }

    @Override // com.android.systemui.settings.UserTracker
    @NotNull
    public List<UserInfo> getUserProfiles() {
        return (List) this.userProfiles$delegate.getValue(this, $$delegatedProperties[3]);
    }

    public final void initialize(int i) {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        setUserIdInternal(i);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.context.registerReceiverForAllUsers(this, intentFilter, null, this.backgroundHandler);
        this.dumpManager.registerDumpable("UserTrackerImpl", this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        String action = intent.getAction();
        if (action != null) {
            int hashCode = action.hashCode();
            if (hashCode != -1238404651) {
                if (hashCode != -864107122) {
                    if (hashCode != 959232034 || !action.equals("android.intent.action.USER_SWITCHED")) {
                        return;
                    }
                    handleSwitchUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
                    return;
                } else if (!action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                    return;
                }
            } else if (!action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                return;
            }
            handleProfilesChanged();
        }
    }

    private final Pair<Context, List<UserInfo>> setUserIdInternal(int i) {
        List<UserInfo> profiles = this.userManager.getProfiles(i);
        UserHandle userHandle = new UserHandle(i);
        Context ctx = this.context.createContextAsUser(userHandle, 0);
        synchronized (this.mutex) {
            setUserId(i);
            setUserHandle(userHandle);
            Intrinsics.checkNotNullExpressionValue(ctx, "ctx");
            setUserContext(ctx);
            Intrinsics.checkNotNullExpressionValue(profiles, "profiles");
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(profiles, 10));
            for (UserInfo userInfo : profiles) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles(arrayList);
            Unit unit = Unit.INSTANCE;
        }
        return TuplesKt.to(ctx, profiles);
    }

    private final void handleSwitchUser(final int i) {
        List<DataItem> list;
        Assert.isNotMainThread();
        if (i == -10000) {
            Log.w("UserTrackerImpl", "handleSwitchUser - Couldn't get new id from intent");
        } else if (i != getUserId()) {
            Log.i("UserTrackerImpl", Intrinsics.stringPlus("Switching to user ", Integer.valueOf(i)));
            Pair<Context, List<UserInfo>> userIdInternal = setUserIdInternal(i);
            final Context component1 = userIdInternal.component1();
            final List<UserInfo> component2 = userIdInternal.component2();
            synchronized (this.callbacks) {
                list = CollectionsKt.toList(this.callbacks);
            }
            for (final DataItem dataItem : list) {
                if (dataItem.getCallback().get() != null) {
                    dataItem.getExecutor().execute(new Runnable() { // from class: com.android.systemui.settings.UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            UserTracker.Callback callback = DataItem.this.getCallback().get();
                            if (callback == null) {
                                return;
                            }
                            callback.onUserChanged(i, component1);
                            callback.onProfilesChanged(component2);
                        }
                    });
                }
            }
        }
    }

    private final void handleProfilesChanged() {
        List<DataItem> list;
        Assert.isNotMainThread();
        final List<UserInfo> profiles = this.userManager.getProfiles(getUserId());
        synchronized (this.mutex) {
            Intrinsics.checkNotNullExpressionValue(profiles, "profiles");
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(profiles, 10));
            for (UserInfo userInfo : profiles) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles(arrayList);
            Unit unit = Unit.INSTANCE;
        }
        synchronized (this.callbacks) {
            list = CollectionsKt.toList(this.callbacks);
        }
        for (final DataItem dataItem : list) {
            if (dataItem.getCallback().get() != null) {
                dataItem.getExecutor().execute(new Runnable() { // from class: com.android.systemui.settings.UserTrackerImpl$handleProfilesChanged$$inlined$notifySubscribers$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserTracker.Callback callback = DataItem.this.getCallback().get();
                        if (callback == null) {
                            return;
                        }
                        List profiles2 = profiles;
                        Intrinsics.checkNotNullExpressionValue(profiles2, "profiles");
                        callback.onProfilesChanged(profiles);
                    }
                });
            }
        }
    }

    @Override // com.android.systemui.settings.UserTracker
    public void addCallback(@NotNull UserTracker.Callback callback, @NotNull Executor executor) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        Intrinsics.checkNotNullParameter(executor, "executor");
        synchronized (this.callbacks) {
            this.callbacks.add(new DataItem(new WeakReference(callback), executor));
        }
    }

    @Override // com.android.systemui.settings.UserTracker
    public void removeCallback(@NotNull final UserTracker.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        synchronized (this.callbacks) {
            this.callbacks.removeIf(new Predicate<DataItem>() { // from class: com.android.systemui.settings.UserTrackerImpl$removeCallback$1$1
                @Override // java.util.function.Predicate
                public final boolean test(@NotNull DataItem it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return it.sameOrEmpty(UserTracker.Callback.this);
                }
            });
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        List<DataItem> list;
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("Initialized: ", Boolean.valueOf(this.initialized)));
        if (this.initialized) {
            pw.println(Intrinsics.stringPlus("userId: ", Integer.valueOf(getUserId())));
            List<UserInfo> userProfiles = getUserProfiles();
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo userInfo : userProfiles) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            pw.println(Intrinsics.stringPlus("userProfiles: ", arrayList));
        }
        synchronized (this.callbacks) {
            list = CollectionsKt.toList(this.callbacks);
        }
        pw.println("Callbacks:");
        for (DataItem dataItem : list) {
            UserTracker.Callback callback = dataItem.getCallback().get();
            if (callback != null) {
                pw.println(Intrinsics.stringPlus("  ", callback));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UserTrackerImpl.kt */
    /* loaded from: classes.dex */
    public static final class SynchronizedDelegate<T> {
        @NotNull
        private T value;

        public SynchronizedDelegate(@NotNull T value) {
            Intrinsics.checkNotNullParameter(value, "value");
            this.value = value;
        }

        @NotNull
        public T getValue(@NotNull UserTrackerImpl thisRef, @NotNull KProperty<?> property) {
            T t;
            Intrinsics.checkNotNullParameter(thisRef, "thisRef");
            Intrinsics.checkNotNullParameter(property, "property");
            if (thisRef.getInitialized()) {
                synchronized (thisRef.mutex) {
                    t = this.value;
                }
                return t;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Must initialize before getting ", property.getName()));
        }

        public void setValue(@NotNull UserTrackerImpl thisRef, @NotNull KProperty<?> property, @NotNull T value) {
            Intrinsics.checkNotNullParameter(thisRef, "thisRef");
            Intrinsics.checkNotNullParameter(property, "property");
            Intrinsics.checkNotNullParameter(value, "value");
            synchronized (thisRef.mutex) {
                this.value = value;
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
