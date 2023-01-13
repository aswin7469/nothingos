package com.android.systemui.statusbar.phone.userswitcher;

import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.UserInfoController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010!\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0007\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B3\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\u000b¢\u0006\u0002\u0010\rJ\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0002H\u0016J\u0006\u0010#\u001a\u00020!J%\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020&2\u000e\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130(H\u0016¢\u0006\u0002\u0010)J\b\u0010*\u001a\u00020!H\u0002J\b\u0010+\u001a\u00020!H\u0002J\u0010\u0010,\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0002H\u0016J\b\u0010-\u001a\u00020!H\u0002J\b\u0010.\u001a\u00020!H\u0002R\u000e\u0010\f\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\"\u0010\u0014\u001a\u0004\u0018\u00010\u00132\b\u0010\u000e\u001a\u0004\u0018\u00010\u0013@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u000e\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u0006/"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserInfoTracker;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/phone/userswitcher/CurrentUserChipInfoUpdatedListener;", "Lcom/android/systemui/Dumpable;", "userInfoController", "Lcom/android/systemui/statusbar/policy/UserInfoController;", "userManager", "Landroid/os/UserManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "mainExecutor", "Ljava/util/concurrent/Executor;", "backgroundExecutor", "(Lcom/android/systemui/statusbar/policy/UserInfoController;Landroid/os/UserManager;Lcom/android/systemui/dump/DumpManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)V", "<set-?>", "Landroid/graphics/drawable/Drawable;", "currentUserAvatar", "getCurrentUserAvatar", "()Landroid/graphics/drawable/Drawable;", "", "currentUserName", "getCurrentUserName", "()Ljava/lang/String;", "listeners", "", "listening", "", "userInfoChangedListener", "Lcom/android/systemui/statusbar/policy/UserInfoController$OnUserInfoChangedListener;", "userSwitcherEnabled", "getUserSwitcherEnabled", "()Z", "addCallback", "", "listener", "checkEnabled", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "notifyListenersSettingChanged", "notifyListenersUserInfoChanged", "removeCallback", "startListening", "stopListening", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarUserInfoTracker.kt */
public final class StatusBarUserInfoTracker implements CallbackController<CurrentUserChipInfoUpdatedListener>, Dumpable {
    private final Executor backgroundExecutor;
    private Drawable currentUserAvatar;
    private String currentUserName;
    private final DumpManager dumpManager;
    private final List<CurrentUserChipInfoUpdatedListener> listeners = new ArrayList();
    private boolean listening;
    private final Executor mainExecutor;
    private final UserInfoController.OnUserInfoChangedListener userInfoChangedListener = new StatusBarUserInfoTracker$$ExternalSyntheticLambda0(this);
    private final UserInfoController userInfoController;
    private final UserManager userManager;
    private boolean userSwitcherEnabled;

    @Inject
    public StatusBarUserInfoTracker(UserInfoController userInfoController2, UserManager userManager2, DumpManager dumpManager2, @Main Executor executor, @Background Executor executor2) {
        Intrinsics.checkNotNullParameter(userInfoController2, "userInfoController");
        Intrinsics.checkNotNullParameter(userManager2, "userManager");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(executor2, "backgroundExecutor");
        this.userInfoController = userInfoController2;
        this.userManager = userManager2;
        this.dumpManager = dumpManager2;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        dumpManager2.registerDumpable("StatusBarUserInfoTracker", this);
    }

    public final String getCurrentUserName() {
        return this.currentUserName;
    }

    public final Drawable getCurrentUserAvatar() {
        return this.currentUserAvatar;
    }

    public final boolean getUserSwitcherEnabled() {
        return this.userSwitcherEnabled;
    }

    /* access modifiers changed from: private */
    /* renamed from: userInfoChangedListener$lambda-0  reason: not valid java name */
    public static final void m3225userInfoChangedListener$lambda0(StatusBarUserInfoTracker statusBarUserInfoTracker, String str, Drawable drawable, String str2) {
        Intrinsics.checkNotNullParameter(statusBarUserInfoTracker, "this$0");
        statusBarUserInfoTracker.currentUserAvatar = drawable;
        statusBarUserInfoTracker.currentUserName = str;
        statusBarUserInfoTracker.notifyListenersUserInfoChanged();
    }

    public void addCallback(CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        Intrinsics.checkNotNullParameter(currentUserChipInfoUpdatedListener, "listener");
        if (this.listeners.isEmpty()) {
            startListening();
        }
        if (!this.listeners.contains(currentUserChipInfoUpdatedListener)) {
            this.listeners.add(currentUserChipInfoUpdatedListener);
        }
    }

    public void removeCallback(CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        Intrinsics.checkNotNullParameter(currentUserChipInfoUpdatedListener, "listener");
        this.listeners.remove((Object) currentUserChipInfoUpdatedListener);
        if (this.listeners.isEmpty()) {
            stopListening();
        }
    }

    private final void notifyListenersUserInfoChanged() {
        for (CurrentUserChipInfoUpdatedListener onCurrentUserChipInfoUpdated : this.listeners) {
            onCurrentUserChipInfoUpdated.onCurrentUserChipInfoUpdated();
        }
    }

    private final void notifyListenersSettingChanged() {
        for (CurrentUserChipInfoUpdatedListener onStatusBarUserSwitcherSettingChanged : this.listeners) {
            onStatusBarUserSwitcherSettingChanged.onStatusBarUserSwitcherSettingChanged(this.userSwitcherEnabled);
        }
    }

    private final void startListening() {
        this.listening = true;
        this.userInfoController.addCallback(this.userInfoChangedListener);
    }

    private final void stopListening() {
        this.listening = false;
        this.userInfoController.removeCallback(this.userInfoChangedListener);
    }

    public final void checkEnabled() {
        this.backgroundExecutor.execute(new StatusBarUserInfoTracker$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: checkEnabled$lambda-4  reason: not valid java name */
    public static final void m3223checkEnabled$lambda4(StatusBarUserInfoTracker statusBarUserInfoTracker) {
        Intrinsics.checkNotNullParameter(statusBarUserInfoTracker, "this$0");
        boolean z = statusBarUserInfoTracker.userSwitcherEnabled;
        boolean isUserSwitcherEnabled = statusBarUserInfoTracker.userManager.isUserSwitcherEnabled();
        statusBarUserInfoTracker.userSwitcherEnabled = isUserSwitcherEnabled;
        if (z != isUserSwitcherEnabled) {
            statusBarUserInfoTracker.mainExecutor.execute(new StatusBarUserInfoTracker$$ExternalSyntheticLambda1(statusBarUserInfoTracker));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: checkEnabled$lambda-4$lambda-3  reason: not valid java name */
    public static final void m3224checkEnabled$lambda4$lambda3(StatusBarUserInfoTracker statusBarUserInfoTracker) {
        Intrinsics.checkNotNullParameter(statusBarUserInfoTracker, "this$0");
        statusBarUserInfoTracker.notifyListenersSettingChanged();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("  userSwitcherEnabled=" + this.userSwitcherEnabled);
        printWriter.println("  listening=" + this.listening);
    }
}
