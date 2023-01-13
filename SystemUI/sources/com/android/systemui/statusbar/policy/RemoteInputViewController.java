package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.icu.text.PluralRules;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H&J\b\u0010'\u001a\u00020$H&J\b\u0010(\u001a\u00020$H&J\b\u0010)\u001a\u00020$H&J\u0010\u0010*\u001a\u00020$2\u0006\u0010%\u001a\u00020&H&J\u0012\u0010+\u001a\u00020$2\b\u0010,\u001a\u0004\u0018\u00010-H&J\u0010\u0010.\u001a\u00020$2\u0006\u0010/\u001a\u00020\u0000H\u0016J\b\u00100\u001a\u00020$H&J\u001d\u00101\u001a\u00020\t2\u000e\u00102\u001a\n\u0012\u0004\u0012\u000203\u0018\u00010\u0018H&¢\u0006\u0002\u00104R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0012\u0010\b\u001a\u00020\tX¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\nR\u001a\u0010\u000b\u001a\u0004\u0018\u00010\fX¦\u000e¢\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0012X¦\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R \u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u0018X¦\u000e¢\u0006\f\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u0004\u0018\u00010\u001eX¦\u000e¢\u0006\f\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u00065À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/RemoteInputViewController;", "", "bouncerChecker", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;", "getBouncerChecker", "()Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;", "setBouncerChecker", "(Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;)V", "isActive", "", "()Z", "pendingIntent", "Landroid/app/PendingIntent;", "getPendingIntent", "()Landroid/app/PendingIntent;", "setPendingIntent", "(Landroid/app/PendingIntent;)V", "remoteInput", "Landroid/app/RemoteInput;", "getRemoteInput", "()Landroid/app/RemoteInput;", "setRemoteInput", "(Landroid/app/RemoteInput;)V", "remoteInputs", "", "getRemoteInputs", "()[Landroid/app/RemoteInput;", "setRemoteInputs", "([Landroid/app/RemoteInput;)V", "revealParams", "Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;", "getRevealParams", "()Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;", "setRevealParams", "(Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;)V", "addOnSendRemoteInputListener", "", "listener", "Lcom/android/systemui/statusbar/policy/OnSendRemoteInputListener;", "bind", "close", "focus", "removeOnSendRemoteInputListener", "setEditedSuggestionInfo", "info", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry$EditedSuggestionInfo;", "stealFocusFrom", "other", "unbind", "updatePendingIntentFromActions", "actions", "Landroid/app/Notification$Action;", "([Landroid/app/Notification$Action;)Z", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RemoteInputViewController.kt */
public interface RemoteInputViewController {
    void addOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener);

    void bind();

    void close();

    void focus();

    NotificationRemoteInputManager.BouncerChecker getBouncerChecker();

    PendingIntent getPendingIntent();

    RemoteInput getRemoteInput();

    RemoteInput[] getRemoteInputs();

    RemoteInputView.RevealParams getRevealParams();

    boolean isActive();

    void removeOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener);

    void setBouncerChecker(NotificationRemoteInputManager.BouncerChecker bouncerChecker);

    void setEditedSuggestionInfo(NotificationEntry.EditedSuggestionInfo editedSuggestionInfo);

    void setPendingIntent(PendingIntent pendingIntent);

    void setRemoteInput(RemoteInput remoteInput);

    void setRemoteInputs(RemoteInput[] remoteInputArr);

    void setRevealParams(RemoteInputView.RevealParams revealParams);

    void unbind();

    boolean updatePendingIntentFromActions(Notification.Action[] actionArr);

    void stealFocusFrom(RemoteInputViewController remoteInputViewController) {
        Intrinsics.checkNotNullParameter(remoteInputViewController, PluralRules.KEYWORD_OTHER);
        remoteInputViewController.close();
        setRemoteInput(remoteInputViewController.getRemoteInput());
        setRemoteInputs(remoteInputViewController.getRemoteInputs());
        setRevealParams(remoteInputViewController.getRevealParams());
        setPendingIntent(remoteInputViewController.getPendingIntent());
        focus();
    }
}
