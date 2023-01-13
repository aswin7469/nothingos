package com.android.systemui.statusbar;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.Context;
import android.net.Uri;
import android.os.SystemProperties;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.util.DumpUtilsKt;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RemoteInputController {
    private static final boolean ENABLE_REMOTE_INPUT = SystemProperties.getBoolean("debug.enable_remote_input", true);
    private final ArrayList<Callback> mCallbacks = new ArrayList<>(3);
    private final Delegate mDelegate;
    private final ArrayList<Pair<WeakReference<NotificationEntry>, Object>> mOpen = new ArrayList<>();
    private final RemoteInputUriController mRemoteInputUriController;
    private final ArrayMap<String, Object> mSpinning = new ArrayMap<>();

    public interface Callback {
        void onRemoteInputActive(boolean z) {
        }

        void onRemoteInputSent(NotificationEntry notificationEntry) {
        }
    }

    public interface Delegate {
        void lockScrollTo(NotificationEntry notificationEntry);

        void requestDisallowLongPressAndDismiss();

        void setRemoteInputActive(NotificationEntry notificationEntry, boolean z);
    }

    public RemoteInputController(Delegate delegate, RemoteInputUriController remoteInputUriController) {
        this.mDelegate = delegate;
        this.mRemoteInputUriController = remoteInputUriController;
    }

    public static void processForRemoteInput(Notification notification, Context context) {
        RemoteInput[] remoteInputs;
        if (!ENABLE_REMOTE_INPUT || notification.extras == null || !notification.extras.containsKey("android.wearable.EXTENSIONS")) {
            return;
        }
        if (notification.actions == null || notification.actions.length == 0) {
            List<Notification.Action> actions = new Notification.WearableExtender(notification).getActions();
            int size = actions.size();
            Notification.Action action = null;
            for (int i = 0; i < size; i++) {
                Notification.Action action2 = actions.get(i);
                if (!(action2 == null || (remoteInputs = action2.getRemoteInputs()) == null)) {
                    int length = remoteInputs.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (remoteInputs[i2].getAllowFreeFormInput()) {
                            action = action2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (action != null) {
                        break;
                    }
                }
            }
            if (action != null) {
                Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(context, notification);
                recoverBuilder.setActions(new Notification.Action[]{action});
                recoverBuilder.build();
            }
        }
    }

    public void addRemoteInput(NotificationEntry notificationEntry, Object obj) {
        Objects.requireNonNull(notificationEntry);
        Objects.requireNonNull(obj);
        boolean isRemoteInputActive = isRemoteInputActive(notificationEntry);
        if (!pruneWeakThenRemoveAndContains(notificationEntry, (NotificationEntry) null, obj)) {
            this.mOpen.add(new Pair(new WeakReference(notificationEntry), obj));
        }
        if (!isRemoteInputActive) {
            apply(notificationEntry);
        }
    }

    public void removeRemoteInput(NotificationEntry notificationEntry, Object obj) {
        Objects.requireNonNull(notificationEntry);
        if ((!notificationEntry.mRemoteEditImeVisible || !notificationEntry.mRemoteEditImeAnimatingAway) && isRemoteInputActive(notificationEntry)) {
            pruneWeakThenRemoveAndContains((NotificationEntry) null, notificationEntry, obj);
            apply(notificationEntry);
        }
    }

    public void addSpinning(String str, Object obj) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(obj);
        this.mSpinning.put(str, obj);
    }

    public void removeSpinning(String str, Object obj) {
        Objects.requireNonNull(str);
        if (obj == null || this.mSpinning.get(str) == obj) {
            this.mSpinning.remove(str);
        }
    }

    public boolean isSpinning(String str) {
        return this.mSpinning.containsKey(str);
    }

    public boolean isSpinning(String str, Object obj) {
        return this.mSpinning.get(str) == obj;
    }

    private void apply(NotificationEntry notificationEntry) {
        this.mDelegate.setRemoteInputActive(notificationEntry, isRemoteInputActive(notificationEntry));
        boolean isRemoteInputActive = isRemoteInputActive();
        int size = this.mCallbacks.size();
        for (int i = 0; i < size; i++) {
            this.mCallbacks.get(i).onRemoteInputActive(isRemoteInputActive);
        }
    }

    public boolean isRemoteInputActive(NotificationEntry notificationEntry) {
        return pruneWeakThenRemoveAndContains(notificationEntry, (NotificationEntry) null, (Object) null);
    }

    public boolean isRemoteInputActive() {
        pruneWeakThenRemoveAndContains((NotificationEntry) null, (NotificationEntry) null, (Object) null);
        return !this.mOpen.isEmpty();
    }

    private boolean pruneWeakThenRemoveAndContains(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, Object obj) {
        boolean z = false;
        for (int size = this.mOpen.size() - 1; size >= 0; size--) {
            NotificationEntry notificationEntry3 = (NotificationEntry) ((WeakReference) this.mOpen.get(size).first).get();
            Object obj2 = this.mOpen.get(size).second;
            boolean z2 = obj == null || obj2 == obj;
            if (notificationEntry3 == null || (notificationEntry3 == notificationEntry2 && z2)) {
                this.mOpen.remove(size);
            } else if (notificationEntry3 == notificationEntry) {
                if (obj == null || obj == obj2) {
                    z = true;
                } else {
                    this.mOpen.remove(size);
                }
            }
        }
        return z;
    }

    public void addCallback(Callback callback) {
        Objects.requireNonNull(callback);
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove((Object) callback);
    }

    public void remoteInputSent(NotificationEntry notificationEntry) {
        int size = this.mCallbacks.size();
        for (int i = 0; i < size; i++) {
            this.mCallbacks.get(i).onRemoteInputSent(notificationEntry);
        }
    }

    public void closeRemoteInputs() {
        if (this.mOpen.size() != 0) {
            ArrayList arrayList = new ArrayList(this.mOpen.size());
            for (int size = this.mOpen.size() - 1; size >= 0; size--) {
                NotificationEntry notificationEntry = (NotificationEntry) ((WeakReference) this.mOpen.get(size).first).get();
                if (notificationEntry != null && notificationEntry.rowExists()) {
                    arrayList.add(notificationEntry);
                }
            }
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                NotificationEntry notificationEntry2 = (NotificationEntry) arrayList.get(size2);
                if (notificationEntry2.rowExists()) {
                    notificationEntry2.closeRemoteInput();
                }
            }
        }
    }

    public void requestDisallowLongPressAndDismiss() {
        this.mDelegate.requestDisallowLongPressAndDismiss();
    }

    public void lockScrollTo(NotificationEntry notificationEntry) {
        this.mDelegate.lockScrollTo(notificationEntry);
    }

    public void grantInlineReplyUriPermission(StatusBarNotification statusBarNotification, Uri uri) {
        this.mRemoteInputUriController.grantInlineReplyUriPermission(statusBarNotification, uri);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("isRemoteInputActive: ");
        indentingPrintWriter.println(isRemoteInputActive());
        indentingPrintWriter.println("mOpen: " + this.mOpen.size());
        DumpUtilsKt.withIncreasedIndent(indentingPrintWriter, (Runnable) new RemoteInputController$$ExternalSyntheticLambda0(this, indentingPrintWriter));
        indentingPrintWriter.println("mSpinning: " + this.mSpinning.size());
        DumpUtilsKt.withIncreasedIndent(indentingPrintWriter, (Runnable) new RemoteInputController$$ExternalSyntheticLambda1(this, indentingPrintWriter));
        indentingPrintWriter.println(this.mSpinning);
        indentingPrintWriter.print("mDelegate: ");
        indentingPrintWriter.println(this.mDelegate);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$dump$0$com-android-systemui-statusbar-RemoteInputController */
    public /* synthetic */ void mo39013xfac918bb(IndentingPrintWriter indentingPrintWriter) {
        String str;
        Iterator<Pair<WeakReference<NotificationEntry>, Object>> it = this.mOpen.iterator();
        while (it.hasNext()) {
            NotificationEntry notificationEntry = (NotificationEntry) ((WeakReference) it.next().first).get();
            if (notificationEntry == null) {
                str = "???";
            } else {
                str = notificationEntry.getKey();
            }
            indentingPrintWriter.println(str);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$dump$1$com-android-systemui-statusbar-RemoteInputController */
    public /* synthetic */ void mo39014x14e4975a(IndentingPrintWriter indentingPrintWriter) {
        for (String println : this.mSpinning.keySet()) {
            indentingPrintWriter.println(println);
        }
    }
}
