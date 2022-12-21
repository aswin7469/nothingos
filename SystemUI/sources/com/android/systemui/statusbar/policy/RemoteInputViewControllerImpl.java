package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000´\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020\u001dH\u0016J\b\u0010F\u001a\u00020DH\u0016J\b\u0010G\u001a\u00020DH\u0016J\b\u0010H\u001a\u00020DH\u0016J\u0010\u0010I\u001a\u00020J2\u0006\u0010(\u001a\u00020'H\u0002J \u0010K\u001a\u00020J2\u0006\u0010(\u001a\u00020'2\u0006\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020OH\u0002J\u0010\u0010P\u001a\u00020J2\u0006\u0010(\u001a\u00020'H\u0002J\u0010\u0010Q\u001a\u00020D2\u0006\u0010E\u001a\u00020\u001dH\u0016J\u0018\u0010R\u001a\u00020D2\u0006\u0010 \u001a\u00020!2\u0006\u0010S\u001a\u00020JH\u0002J\u0012\u0010T\u001a\u00020D2\b\u0010U\u001a\u0004\u0018\u00010VH\u0016J\b\u0010W\u001a\u00020DH\u0016J\u001d\u0010X\u001a\u00020\u00162\u000e\u0010Y\u001a\n\u0012\u0004\u0012\u00020Z\u0018\u000102H\u0016¢\u0006\u0002\u0010[R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R(\u0010(\u001a\u0004\u0018\u00010'2\b\u0010&\u001a\u0004\u0018\u00010'@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010-\u001a\u00020.8BX\u0004¢\u0006\u0006\u001a\u0004\b/\u00100R$\u00101\u001a\n\u0012\u0004\u0012\u00020'\u0018\u000102X\u000e¢\u0006\u0010\n\u0002\u00107\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001c\u00108\u001a\n :*\u0004\u0018\u000109098BX\u0004¢\u0006\u0006\u001a\u0004\b;\u0010<R(\u0010>\u001a\u0004\u0018\u00010=2\b\u0010&\u001a\u0004\u0018\u00010=@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\\"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/RemoteInputViewControllerImpl;", "Lcom/android/systemui/statusbar/policy/RemoteInputViewController;", "view", "Lcom/android/systemui/statusbar/policy/RemoteInputView;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "remoteInputQuickSettingsDisabler", "Lcom/android/systemui/statusbar/policy/RemoteInputQuickSettingsDisabler;", "remoteInputController", "Lcom/android/systemui/statusbar/RemoteInputController;", "shortcutManager", "Landroid/content/pm/ShortcutManager;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/systemui/statusbar/policy/RemoteInputView;Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;Lcom/android/systemui/statusbar/policy/RemoteInputQuickSettingsDisabler;Lcom/android/systemui/statusbar/RemoteInputController;Landroid/content/pm/ShortcutManager;Lcom/android/internal/logging/UiEventLogger;)V", "bouncerChecker", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;", "getBouncerChecker", "()Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;", "setBouncerChecker", "(Lcom/android/systemui/statusbar/NotificationRemoteInputManager$BouncerChecker;)V", "isActive", "", "()Z", "isBound", "onFocusChangeListener", "Landroid/view/View$OnFocusChangeListener;", "onSendListeners", "Landroid/util/ArraySet;", "Lcom/android/systemui/statusbar/policy/OnSendRemoteInputListener;", "onSendRemoteInputListener", "Ljava/lang/Runnable;", "pendingIntent", "Landroid/app/PendingIntent;", "getPendingIntent", "()Landroid/app/PendingIntent;", "setPendingIntent", "(Landroid/app/PendingIntent;)V", "value", "Landroid/app/RemoteInput;", "remoteInput", "getRemoteInput", "()Landroid/app/RemoteInput;", "setRemoteInput", "(Landroid/app/RemoteInput;)V", "remoteInputResultsSource", "", "getRemoteInputResultsSource", "()I", "remoteInputs", "", "getRemoteInputs", "()[Landroid/app/RemoteInput;", "setRemoteInputs", "([Landroid/app/RemoteInput;)V", "[Landroid/app/RemoteInput;", "resources", "Landroid/content/res/Resources;", "kotlin.jvm.PlatformType", "getResources", "()Landroid/content/res/Resources;", "Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;", "revealParams", "getRevealParams", "()Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;", "setRevealParams", "(Lcom/android/systemui/statusbar/policy/RemoteInputView$RevealParams;)V", "addOnSendRemoteInputListener", "", "listener", "bind", "close", "focus", "prepareRemoteInput", "Landroid/content/Intent;", "prepareRemoteInputFromData", "contentType", "", "data", "Landroid/net/Uri;", "prepareRemoteInputFromText", "removeOnSendRemoteInputListener", "sendRemoteInput", "intent", "setEditedSuggestionInfo", "info", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry$EditedSuggestionInfo;", "unbind", "updatePendingIntentFromActions", "actions", "Landroid/app/Notification$Action;", "([Landroid/app/Notification$Action;)Z", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RemoteInputViewController.kt */
public final class RemoteInputViewControllerImpl implements RemoteInputViewController {
    private NotificationRemoteInputManager.BouncerChecker bouncerChecker;
    private final NotificationEntry entry;
    private boolean isBound;
    private final View.OnFocusChangeListener onFocusChangeListener = new RemoteInputViewControllerImpl$$ExternalSyntheticLambda0(this);
    private final ArraySet<OnSendRemoteInputListener> onSendListeners = new ArraySet<>();
    private final Runnable onSendRemoteInputListener = new RemoteInputViewControllerImpl$$ExternalSyntheticLambda1(this);
    private PendingIntent pendingIntent;
    private RemoteInput remoteInput;
    private final RemoteInputController remoteInputController;
    private final RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler;
    private RemoteInput[] remoteInputs;
    private RemoteInputView.RevealParams revealParams;
    private final ShortcutManager shortcutManager;
    private final UiEventLogger uiEventLogger;
    private final RemoteInputView view;

    @Inject
    public RemoteInputViewControllerImpl(RemoteInputView remoteInputView, NotificationEntry notificationEntry, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler2, RemoteInputController remoteInputController2, ShortcutManager shortcutManager2, UiEventLogger uiEventLogger2) {
        Intrinsics.checkNotNullParameter(remoteInputView, "view");
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(remoteInputQuickSettingsDisabler2, "remoteInputQuickSettingsDisabler");
        Intrinsics.checkNotNullParameter(remoteInputController2, "remoteInputController");
        Intrinsics.checkNotNullParameter(shortcutManager2, "shortcutManager");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        this.view = remoteInputView;
        this.entry = notificationEntry;
        this.remoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler2;
        this.remoteInputController = remoteInputController2;
        this.shortcutManager = shortcutManager2;
        this.uiEventLogger = uiEventLogger2;
    }

    private final Resources getResources() {
        return this.view.getResources();
    }

    public NotificationRemoteInputManager.BouncerChecker getBouncerChecker() {
        return this.bouncerChecker;
    }

    public void setBouncerChecker(NotificationRemoteInputManager.BouncerChecker bouncerChecker2) {
        this.bouncerChecker = bouncerChecker2;
    }

    public RemoteInput getRemoteInput() {
        return this.remoteInput;
    }

    public void setRemoteInput(RemoteInput remoteInput2) {
        this.remoteInput = remoteInput2;
        if (remoteInput2 != null) {
            if (!this.isBound) {
                remoteInput2 = null;
            }
            if (remoteInput2 != null) {
                this.view.setHintText(remoteInput2.getLabel());
                this.view.setSupportedMimeTypes(remoteInput2.getAllowedDataTypes());
            }
        }
    }

    public PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent2) {
        this.pendingIntent = pendingIntent2;
    }

    public RemoteInput[] getRemoteInputs() {
        return this.remoteInputs;
    }

    public void setRemoteInputs(RemoteInput[] remoteInputArr) {
        this.remoteInputs = remoteInputArr;
    }

    public RemoteInputView.RevealParams getRevealParams() {
        return this.revealParams;
    }

    public void setRevealParams(RemoteInputView.RevealParams revealParams2) {
        this.revealParams = revealParams2;
        if (this.isBound) {
            this.view.setRevealParameters(revealParams2);
        }
    }

    public boolean isActive() {
        return this.view.isActive();
    }

    public void bind() {
        if (!this.isBound) {
            this.isBound = true;
            RemoteInput remoteInput2 = getRemoteInput();
            if (remoteInput2 != null) {
                this.view.setHintText(remoteInput2.getLabel());
                this.view.setSupportedMimeTypes(remoteInput2.getAllowedDataTypes());
            }
            this.view.setRevealParameters(getRevealParams());
            this.view.addOnEditTextFocusChangedListener(this.onFocusChangeListener);
            this.view.addOnSendRemoteInputListener(this.onSendRemoteInputListener);
        }
    }

    public void unbind() {
        if (this.isBound) {
            this.isBound = false;
            this.view.removeOnEditTextFocusChangedListener(this.onFocusChangeListener);
            this.view.removeOnSendRemoteInputListener(this.onSendRemoteInputListener);
        }
    }

    public void setEditedSuggestionInfo(NotificationEntry.EditedSuggestionInfo editedSuggestionInfo) {
        this.entry.editedSuggestionInfo = editedSuggestionInfo;
        if (editedSuggestionInfo != null) {
            this.entry.remoteInputText = editedSuggestionInfo.originalText;
            this.entry.remoteInputAttachment = null;
        }
    }

    public boolean updatePendingIntentFromActions(Notification.Action[] actionArr) {
        RemoteInput[] remoteInputs2;
        RemoteInput remoteInput2;
        if (actionArr == null) {
            return false;
        }
        PendingIntent pendingIntent2 = getPendingIntent();
        Intent intent = pendingIntent2 != null ? pendingIntent2.getIntent() : null;
        if (intent == null) {
            return false;
        }
        Iterator it = ArrayIteratorKt.iterator(actionArr);
        while (it.hasNext()) {
            Notification.Action action = (Notification.Action) it.next();
            PendingIntent pendingIntent3 = action.actionIntent;
            if (!(pendingIntent3 == null || (remoteInputs2 = action.getRemoteInputs()) == null || !intent.filterEquals(pendingIntent3.getIntent()))) {
                int length = remoteInputs2.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        remoteInput2 = null;
                        break;
                    }
                    remoteInput2 = remoteInputs2[i];
                    if (remoteInput2.getAllowFreeFormInput()) {
                        break;
                    }
                    i++;
                }
                if (remoteInput2 != null) {
                    setPendingIntent(pendingIntent3);
                    setRemoteInput(remoteInput2);
                    setRemoteInputs(remoteInputs2);
                    setEditedSuggestionInfo((NotificationEntry.EditedSuggestionInfo) null);
                    return true;
                }
            }
        }
        return false;
    }

    public void addOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener2) {
        Intrinsics.checkNotNullParameter(onSendRemoteInputListener2, "listener");
        this.onSendListeners.add(onSendRemoteInputListener2);
    }

    public void removeOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener2) {
        Intrinsics.checkNotNullParameter(onSendRemoteInputListener2, "listener");
        this.onSendListeners.remove(onSendRemoteInputListener2);
    }

    public void close() {
        this.view.close();
    }

    public void focus() {
        this.view.focus();
    }

    /* access modifiers changed from: private */
    /* renamed from: onFocusChangeListener$lambda-4  reason: not valid java name */
    public static final void m3239onFocusChangeListener$lambda4(RemoteInputViewControllerImpl remoteInputViewControllerImpl, View view2, boolean z) {
        Intrinsics.checkNotNullParameter(remoteInputViewControllerImpl, "this$0");
        remoteInputViewControllerImpl.remoteInputQuickSettingsDisabler.setRemoteInputActive(z);
    }

    /* access modifiers changed from: private */
    /* renamed from: onSendRemoteInputListener$lambda-7  reason: not valid java name */
    public static final void m3240onSendRemoteInputListener$lambda7(RemoteInputViewControllerImpl remoteInputViewControllerImpl) {
        Intrinsics.checkNotNullParameter(remoteInputViewControllerImpl, "this$0");
        RemoteInput remoteInput2 = remoteInputViewControllerImpl.getRemoteInput();
        if (remoteInput2 == null) {
            Log.e("RemoteInput", "cannot send remote input, RemoteInput data is null");
            return;
        }
        PendingIntent pendingIntent2 = remoteInputViewControllerImpl.getPendingIntent();
        if (pendingIntent2 == null) {
            Log.e("RemoteInput", "cannot send remote input, PendingIntent is null");
        } else {
            remoteInputViewControllerImpl.sendRemoteInput(pendingIntent2, remoteInputViewControllerImpl.prepareRemoteInput(remoteInput2));
        }
    }

    private final void sendRemoteInput(PendingIntent pendingIntent2, Intent intent) {
        NotificationRemoteInputManager.BouncerChecker bouncerChecker2 = getBouncerChecker();
        if (bouncerChecker2 != null && bouncerChecker2.showBouncerIfNecessary()) {
            this.view.hideIme();
            for (OnSendRemoteInputListener onSendRequestBounced : CollectionsKt.toList(this.onSendListeners)) {
                onSendRequestBounced.onSendRequestBounced();
            }
            return;
        }
        this.view.startSending();
        this.entry.lastRemoteInputSent = SystemClock.elapsedRealtime();
        this.entry.mRemoteEditImeAnimatingAway = true;
        this.remoteInputController.addSpinning(this.entry.getKey(), this.view.mToken);
        this.remoteInputController.removeRemoteInput(this.entry, this.view.mToken);
        this.remoteInputController.remoteInputSent(this.entry);
        this.entry.setHasSentReply();
        for (OnSendRemoteInputListener onSendRemoteInput : CollectionsKt.toList(this.onSendListeners)) {
            onSendRemoteInput.onSendRemoteInput();
        }
        this.shortcutManager.onApplicationActive(this.entry.getSbn().getPackageName(), this.entry.getSbn().getUser().getIdentifier());
        this.uiEventLogger.logWithInstanceId(RemoteInputView.NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_SEND, this.entry.getSbn().getUid(), this.entry.getSbn().getPackageName(), this.entry.getSbn().getInstanceId());
        try {
            pendingIntent2.send(this.view.getContext(), 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Log.i("RemoteInput", "Unable to send remote input result", e);
            this.uiEventLogger.logWithInstanceId(RemoteInputView.NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_FAILURE, this.entry.getSbn().getUid(), this.entry.getSbn().getPackageName(), this.entry.getSbn().getInstanceId());
        }
        this.view.clearAttachment();
    }

    private final Intent prepareRemoteInput(RemoteInput remoteInput2) {
        if (this.entry.remoteInputAttachment == null) {
            return prepareRemoteInputFromText(remoteInput2);
        }
        String str = this.entry.remoteInputMimeType;
        Intrinsics.checkNotNullExpressionValue(str, "entry.remoteInputMimeType");
        Uri uri = this.entry.remoteInputUri;
        Intrinsics.checkNotNullExpressionValue(uri, "entry.remoteInputUri");
        return prepareRemoteInputFromData(remoteInput2, str, uri);
    }

    private final Intent prepareRemoteInputFromText(RemoteInput remoteInput2) {
        Bundle bundle = new Bundle();
        bundle.putString(remoteInput2.getResultKey(), this.view.getText().toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(getRemoteInputs(), addFlags, bundle);
        this.entry.remoteInputText = this.view.getText();
        this.view.clearAttachment();
        this.entry.remoteInputUri = null;
        this.entry.remoteInputMimeType = null;
        RemoteInput.setResultsSource(addFlags, getRemoteInputResultsSource());
        Intrinsics.checkNotNullExpressionValue(addFlags, "fillInIntent");
        return addFlags;
    }

    private final Intent prepareRemoteInputFromData(RemoteInput remoteInput2, String str, Uri uri) {
        Map hashMap = new HashMap();
        hashMap.put(str, uri);
        this.remoteInputController.grantInlineReplyUriPermission(this.entry.getSbn(), uri);
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addDataResultToIntent(remoteInput2, addFlags, hashMap);
        Bundle bundle = new Bundle();
        bundle.putString(remoteInput2.getResultKey(), this.view.getText().toString());
        RemoteInput.addResultsToIntent(getRemoteInputs(), addFlags, bundle);
        CharSequence label = this.entry.remoteInputAttachment.getClip().getDescription().getLabel();
        Intrinsics.checkNotNullExpressionValue(label, "entry.remoteInputAttachment.clip.description.label");
        if (TextUtils.isEmpty(label)) {
            label = getResources().getString(C1893R.string.remote_input_image_insertion_text);
        }
        if (!TextUtils.isEmpty(this.view.getText())) {
            label = "\"" + label + "\" " + this.view.getText();
        }
        this.entry.remoteInputText = label;
        RemoteInput.setResultsSource(addFlags, getRemoteInputResultsSource());
        Intrinsics.checkNotNullExpressionValue(addFlags, "fillInIntent");
        return addFlags;
    }

    private final int getRemoteInputResultsSource() {
        return this.entry.editedSuggestionInfo != null ? 1 : 0;
    }
}
