package com.android.systemui.settings;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
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
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

@Metadata(mo65042d1 = {"\u0000®\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 Y2\u00020\u00012\u00020\u00022\u00020\u0003:\u0002YZB'\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@H\u0016J\u0010\u0010A\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J%\u0010B\u001a\u00020<2\u0006\u0010C\u001a\u00020D2\u000e\u0010E\u001a\n\u0012\u0006\b\u0001\u0012\u00020G0FH\u0016¢\u0006\u0002\u0010HJ\b\u0010I\u001a\u00020<H\u0003J\u0010\u0010J\u001a\u00020<2\u0006\u0010K\u001a\u00020)H\u0003J\u000e\u0010L\u001a\u00020<2\u0006\u0010M\u001a\u00020)J$\u0010N\u001a\u00020<2\u0019\b\u0004\u0010O\u001a\u0013\u0012\u0004\u0012\u00020>\u0012\u0004\u0012\u00020<0P¢\u0006\u0002\bQH\bJ\u0018\u0010R\u001a\u00020<2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010S\u001a\u00020TH\u0016J\u0010\u0010U\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0016J\"\u0010V\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u000201040W2\u0006\u0010X\u001a\u00020)H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e8\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0011@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\u00020\u00188VX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR+\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00058V@RX\u0002¢\u0006\u0012\n\u0004\b \u0010!\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR+\u0010#\u001a\u00020\"2\u0006\u0010\u0010\u001a\u00020\"8V@RX\u0002¢\u0006\u0012\n\u0004\b(\u0010!\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R+\u0010*\u001a\u00020)2\u0006\u0010\u0010\u001a\u00020)8V@RX\u0002¢\u0006\u0012\n\u0004\b/\u0010!\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u0014\u00100\u001a\u0002018VX\u0004¢\u0006\u0006\u001a\u0004\b2\u00103R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R7\u00105\u001a\b\u0012\u0004\u0012\u000201042\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u000201048V@RX\u0002¢\u0006\u0012\n\u0004\b:\u0010!\u001a\u0004\b6\u00107\"\u0004\b8\u00109¨\u0006["}, mo65043d2 = {"Lcom/android/systemui/settings/UserTrackerImpl;", "Lcom/android/systemui/settings/UserTracker;", "Lcom/android/systemui/Dumpable;", "Landroid/content/BroadcastReceiver;", "context", "Landroid/content/Context;", "userManager", "Landroid/os/UserManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "backgroundHandler", "Landroid/os/Handler;", "(Landroid/content/Context;Landroid/os/UserManager;Lcom/android/systemui/dump/DumpManager;Landroid/os/Handler;)V", "callbacks", "", "Lcom/android/systemui/settings/DataItem;", "<set-?>", "", "initialized", "getInitialized", "()Z", "mutex", "", "userContentResolver", "Landroid/content/ContentResolver;", "getUserContentResolver", "()Landroid/content/ContentResolver;", "userContext", "getUserContext", "()Landroid/content/Context;", "setUserContext", "(Landroid/content/Context;)V", "userContext$delegate", "Lcom/android/systemui/settings/UserTrackerImpl$SynchronizedDelegate;", "Landroid/os/UserHandle;", "userHandle", "getUserHandle", "()Landroid/os/UserHandle;", "setUserHandle", "(Landroid/os/UserHandle;)V", "userHandle$delegate", "", "userId", "getUserId", "()I", "setUserId", "(I)V", "userId$delegate", "userInfo", "Landroid/content/pm/UserInfo;", "getUserInfo", "()Landroid/content/pm/UserInfo;", "", "userProfiles", "getUserProfiles", "()Ljava/util/List;", "setUserProfiles", "(Ljava/util/List;)V", "userProfiles$delegate", "addCallback", "", "callback", "Lcom/android/systemui/settings/UserTracker$Callback;", "executor", "Ljava/util/concurrent/Executor;", "createCurrentUserContext", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "handleProfilesChanged", "handleSwitchUser", "newUser", "initialize", "startingUser", "notifySubscribers", "action", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "onReceive", "intent", "Landroid/content/Intent;", "removeCallback", "setUserIdInternal", "Lkotlin/Pair;", "user", "Companion", "SynchronizedDelegate", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl extends BroadcastReceiver implements UserTracker, Dumpable {
    static final /* synthetic */ KProperty<Object>[] $$delegatedProperties;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "UserTrackerImpl";
    private final Handler backgroundHandler;
    private final List<DataItem> callbacks;
    private final Context context;
    private final DumpManager dumpManager;
    private boolean initialized;
    /* access modifiers changed from: private */
    public final Object mutex = new Object();
    private final SynchronizedDelegate userContext$delegate;
    private final SynchronizedDelegate userHandle$delegate;
    private final SynchronizedDelegate userId$delegate;
    private final UserManager userManager;
    private final SynchronizedDelegate userProfiles$delegate;

    public UserTrackerImpl(Context context2, UserManager userManager2, DumpManager dumpManager2, Handler handler) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(userManager2, "userManager");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(handler, "backgroundHandler");
        this.context = context2;
        this.userManager = userManager2;
        this.dumpManager = dumpManager2;
        this.backgroundHandler = handler;
        this.userId$delegate = new SynchronizedDelegate(Integer.valueOf(context2.getUserId()));
        UserHandle user = context2.getUser();
        Intrinsics.checkNotNullExpressionValue(user, "context.user");
        this.userHandle$delegate = new SynchronizedDelegate(user);
        this.userContext$delegate = new SynchronizedDelegate(context2);
        this.userProfiles$delegate = new SynchronizedDelegate(CollectionsKt.emptyList());
        this.callbacks = new ArrayList();
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/settings/UserTrackerImpl$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserTrackerImpl.kt */
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

    static {
        Class<UserTrackerImpl> cls = UserTrackerImpl.class;
        $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "userId", "getUserId()I", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "userHandle", "getUserHandle()Landroid/os/UserHandle;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "userContext", "getUserContext()Landroid/content/Context;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "userProfiles", "getUserProfiles()Ljava/util/List;", 0))};
    }

    private void setUserId(int i) {
        this.userId$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[0], Integer.valueOf(i));
    }

    public int getUserId() {
        return ((Number) this.userId$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[0])).intValue();
    }

    private void setUserHandle(UserHandle userHandle) {
        this.userHandle$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[1], userHandle);
    }

    public UserHandle getUserHandle() {
        return (UserHandle) this.userHandle$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[1]);
    }

    private void setUserContext(Context context2) {
        this.userContext$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[2], context2);
    }

    public Context getUserContext() {
        return (Context) this.userContext$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[2]);
    }

    public ContentResolver getUserContentResolver() {
        ContentResolver contentResolver = getUserContext().getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver, "userContext.contentResolver");
        return contentResolver;
    }

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
        this.userProfiles$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[3], list);
    }

    public List<UserInfo> getUserProfiles() {
        return (List) this.userProfiles$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[3]);
    }

    public final void initialize(int i) {
        if (!this.initialized) {
            this.initialized = true;
            setUserIdInternal(i);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNLOCKED");
            this.context.registerReceiverForAllUsers(this, intentFilter, (String) null, this.backgroundHandler);
            this.dumpManager.registerDumpable(TAG, this);
        }
    }

    public void onReceive(Context context2, Intent intent) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        String action = intent.getAction();
        if (action != null) {
            switch (action.hashCode()) {
                case -1462075554:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_UNLOCKED")) {
                        return;
                    }
                    break;
                case -1238404651:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        return;
                    }
                    break;
                case -864107122:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        return;
                    }
                    break;
                case 959232034:
                    if (action.equals("android.intent.action.USER_SWITCHED")) {
                        handleSwitchUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
                        return;
                    }
                    return;
                case 1051477093:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                        return;
                    }
                    break;
                default:
                    return;
            }
            handleProfilesChanged();
        }
    }

    public Context createCurrentUserContext(Context context2) {
        Context createContextAsUser;
        Intrinsics.checkNotNullParameter(context2, "context");
        synchronized (this.mutex) {
            createContextAsUser = context2.createContextAsUser(getUserHandle(), 0);
            Intrinsics.checkNotNullExpressionValue(createContextAsUser, "context.createContextAsUser(userHandle, 0)");
        }
        return createContextAsUser;
    }

    private final Pair<Context, List<UserInfo>> setUserIdInternal(int i) {
        List profiles = this.userManager.getProfiles(i);
        UserHandle userHandle = new UserHandle(i);
        Context createContextAsUser = this.context.createContextAsUser(userHandle, 0);
        synchronized (this.mutex) {
            setUserId(i);
            setUserHandle(userHandle);
            Intrinsics.checkNotNullExpressionValue(createContextAsUser, "ctx");
            setUserContext(createContextAsUser);
            Intrinsics.checkNotNullExpressionValue(profiles, "profiles");
            Iterable<UserInfo> iterable = profiles;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles((List) arrayList);
            Unit unit = Unit.INSTANCE;
        }
        return TuplesKt.m1802to(createContextAsUser, profiles);
    }

    private final void handleSwitchUser(int i) {
        List<DataItem> list;
        Assert.isNotMainThread();
        if (i == -10000) {
            Log.w(TAG, "handleSwitchUser - Couldn't get new id from intent");
        } else if (i != getUserId()) {
            Log.i(TAG, "Switching to user " + i);
            Pair<Context, List<UserInfo>> userIdInternal = setUserIdInternal(i);
            Context component1 = userIdInternal.component1();
            List component2 = userIdInternal.component2();
            synchronized (this.callbacks) {
                list = CollectionsKt.toList(this.callbacks);
            }
            for (DataItem dataItem : list) {
                if (dataItem.getCallback().get() != null) {
                    dataItem.getExecutor().execute(new UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1(dataItem, i, component1, component2));
                }
            }
        }
    }

    private final void handleProfilesChanged() {
        List<DataItem> list;
        Assert.isNotMainThread();
        List profiles = this.userManager.getProfiles(getUserId());
        synchronized (this.mutex) {
            Intrinsics.checkNotNullExpressionValue(profiles, "profiles");
            Iterable<UserInfo> iterable = profiles;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles((List) arrayList);
            Unit unit = Unit.INSTANCE;
        }
        synchronized (this.callbacks) {
            list = CollectionsKt.toList(this.callbacks);
        }
        for (DataItem dataItem : list) {
            if (dataItem.getCallback().get() != null) {
                dataItem.getExecutor().execute(new C2472xc58954eb(dataItem, profiles));
            }
        }
    }

    public void addCallback(UserTracker.Callback callback, Executor executor) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        Intrinsics.checkNotNullParameter(executor, "executor");
        synchronized (this.callbacks) {
            this.callbacks.add(new DataItem(new WeakReference(callback), executor));
        }
    }

    public void removeCallback(UserTracker.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        synchronized (this.callbacks) {
            this.callbacks.removeIf(new UserTrackerImpl$$ExternalSyntheticLambda0(callback));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-11$lambda-10  reason: not valid java name */
    public static final boolean m3025removeCallback$lambda11$lambda10(UserTracker.Callback callback, DataItem dataItem) {
        Intrinsics.checkNotNullParameter(callback, "$callback");
        Intrinsics.checkNotNullParameter(dataItem, "it");
        return dataItem.sameOrEmpty(callback);
    }

    private final void notifySubscribers(Function1<? super UserTracker.Callback, Unit> function1) {
        List<DataItem> list;
        synchronized (this.callbacks) {
            try {
                list = CollectionsKt.toList(this.callbacks);
                InlineMarker.finallyStart(1);
            } catch (Throwable th) {
                InlineMarker.finallyStart(1);
                InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        InlineMarker.finallyEnd(1);
        for (DataItem dataItem : list) {
            if (dataItem.getCallback().get() != null) {
                dataItem.getExecutor().execute(new UserTrackerImpl$notifySubscribers$1$1(dataItem, function1));
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        List<DataItem> list;
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Initialized: " + this.initialized);
        if (this.initialized) {
            printWriter.println("userId: " + getUserId());
            Iterable<UserInfo> userProfiles = getUserProfiles();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo fullString : userProfiles) {
                arrayList.add(fullString.toFullString());
            }
            printWriter.println("userProfiles: " + ((List) arrayList));
        }
        synchronized (this.callbacks) {
            list = CollectionsKt.toList(this.callbacks);
        }
        printWriter.println("Callbacks:");
        for (DataItem callback : list) {
            UserTracker.Callback callback2 = callback.getCallback().get();
            if (callback2 != null) {
                printWriter.println("  " + callback2);
            }
        }
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u0002H\u00010\u0003B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\"\u0010\b\u001a\u00028\u00002\u0006\u0010\t\u001a\u00020\u00042\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0002¢\u0006\u0002\u0010\fJ*\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u00042\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\u0005\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u000fR\u0010\u0010\u0005\u001a\u00028\u0000X\u000e¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/settings/UserTrackerImpl$SynchronizedDelegate;", "T", "", "Lkotlin/properties/ReadWriteProperty;", "Lcom/android/systemui/settings/UserTrackerImpl;", "value", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Lcom/android/systemui/settings/UserTrackerImpl;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "(Lcom/android/systemui/settings/UserTrackerImpl;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserTrackerImpl.kt */
    private static final class SynchronizedDelegate<T> implements ReadWriteProperty<UserTrackerImpl, T> {
        private T value;

        public SynchronizedDelegate(T t) {
            Intrinsics.checkNotNullParameter(t, "value");
            this.value = t;
        }

        public T getValue(UserTrackerImpl userTrackerImpl, KProperty<?> kProperty) {
            T t;
            Intrinsics.checkNotNullParameter(userTrackerImpl, "thisRef");
            Intrinsics.checkNotNullParameter(kProperty, "property");
            if (userTrackerImpl.getInitialized()) {
                synchronized (userTrackerImpl.mutex) {
                    t = this.value;
                }
                return t;
            }
            throw new IllegalStateException("Must initialize before getting " + kProperty.getName());
        }

        public void setValue(UserTrackerImpl userTrackerImpl, KProperty<?> kProperty, T t) {
            Intrinsics.checkNotNullParameter(userTrackerImpl, "thisRef");
            Intrinsics.checkNotNullParameter(kProperty, "property");
            Intrinsics.checkNotNullParameter(t, "value");
            synchronized (userTrackerImpl.mutex) {
                this.value = t;
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
