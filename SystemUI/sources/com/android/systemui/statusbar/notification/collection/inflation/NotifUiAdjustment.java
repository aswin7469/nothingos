package com.android.systemui.statusbar.notification.collection.inflation;

import android.app.Notification;
import android.app.RemoteInput;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015BC\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n¢\u0006\u0002\u0010\rR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u000eR\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\f\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifUiAdjustment;", "", "key", "", "smartActions", "", "Landroid/app/Notification$Action;", "smartReplies", "", "isConversation", "", "isMinimized", "needsRedaction", "(Ljava/lang/String;Ljava/util/List;Ljava/util/List;ZZZ)V", "()Z", "getKey", "()Ljava/lang/String;", "getNeedsRedaction", "getSmartActions", "()Ljava/util/List;", "getSmartReplies", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifUiAdjustment.kt */
public final class NotifUiAdjustment {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final boolean isConversation;
    private final boolean isMinimized;
    private final String key;
    private final boolean needsRedaction;
    private final List<Notification.Action> smartActions;
    private final List<CharSequence> smartReplies;

    @JvmStatic
    public static final boolean needReinflate(NotifUiAdjustment notifUiAdjustment, NotifUiAdjustment notifUiAdjustment2) {
        return Companion.needReinflate(notifUiAdjustment, notifUiAdjustment2);
    }

    public NotifUiAdjustment(String str, List<? extends Notification.Action> list, List<? extends CharSequence> list2, boolean z, boolean z2, boolean z3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(list, "smartActions");
        Intrinsics.checkNotNullParameter(list2, "smartReplies");
        this.key = str;
        this.smartActions = list;
        this.smartReplies = list2;
        this.isConversation = z;
        this.isMinimized = z2;
        this.needsRedaction = z3;
    }

    public final String getKey() {
        return this.key;
    }

    public final List<Notification.Action> getSmartActions() {
        return this.smartActions;
    }

    public final List<CharSequence> getSmartReplies() {
        return this.smartReplies;
    }

    public final boolean isConversation() {
        return this.isConversation;
    }

    public final boolean isMinimized() {
        return this.isMinimized;
    }

    public final boolean getNeedsRedaction() {
        return this.needsRedaction;
    }

    @Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0002J-\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b2\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u0002¢\u0006\u0002\u0010\nJ-\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b2\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bH\u0002¢\u0006\u0002\u0010\fJ$\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\u0018\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0007¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifUiAdjustment$Companion;", "", "()V", "areDifferent", "", "first", "Landroid/graphics/drawable/Icon;", "second", "", "Landroid/app/RemoteInput;", "([Landroid/app/RemoteInput;[Landroid/app/RemoteInput;)Z", "", "([Ljava/lang/CharSequence;[Ljava/lang/CharSequence;)Z", "", "Landroid/app/Notification$Action;", "needReinflate", "oldAdjustment", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotifUiAdjustment;", "newAdjustment", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotifUiAdjustment.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final boolean needReinflate(NotifUiAdjustment notifUiAdjustment, NotifUiAdjustment notifUiAdjustment2) {
            Intrinsics.checkNotNullParameter(notifUiAdjustment, "oldAdjustment");
            Intrinsics.checkNotNullParameter(notifUiAdjustment2, "newAdjustment");
            if (notifUiAdjustment == notifUiAdjustment2) {
                return false;
            }
            return (notifUiAdjustment.isConversation() == notifUiAdjustment2.isConversation() && notifUiAdjustment.isMinimized() == notifUiAdjustment2.isMinimized() && notifUiAdjustment.getNeedsRedaction() == notifUiAdjustment2.getNeedsRedaction() && !areDifferent((List<? extends Notification.Action>) notifUiAdjustment.getSmartActions(), (List<? extends Notification.Action>) notifUiAdjustment2.getSmartActions()) && Intrinsics.areEqual((Object) notifUiAdjustment2.getSmartReplies(), (Object) notifUiAdjustment.getSmartReplies())) ? false : true;
        }

        private final boolean areDifferent(List<? extends Notification.Action> list, List<? extends Notification.Action> list2) {
            boolean z;
            if (list == list2) {
                return false;
            }
            if (list.size() == list2.size()) {
                for (Pair pair : SequencesKt.zip(CollectionsKt.asSequence(list), CollectionsKt.asSequence(list2))) {
                    if (!TextUtils.equals(((Notification.Action) pair.getFirst()).title, ((Notification.Action) pair.getSecond()).title) || NotifUiAdjustment.Companion.areDifferent(((Notification.Action) pair.getFirst()).getIcon(), ((Notification.Action) pair.getSecond()).getIcon()) || !Intrinsics.areEqual((Object) ((Notification.Action) pair.getFirst()).actionIntent, (Object) ((Notification.Action) pair.getSecond()).actionIntent) || NotifUiAdjustment.Companion.areDifferent(((Notification.Action) pair.getFirst()).getRemoteInputs(), ((Notification.Action) pair.getSecond()).getRemoteInputs())) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (z) {
                    }
                }
                return false;
            }
            return true;
        }

        private final boolean areDifferent(Icon icon, Icon icon2) {
            if (icon == icon2) {
                return false;
            }
            return icon == null || icon2 == null || !icon.sameAs(icon2);
        }

        private final boolean areDifferent(RemoteInput[] remoteInputArr, RemoteInput[] remoteInputArr2) {
            boolean z;
            if (remoteInputArr == remoteInputArr2) {
                return false;
            }
            if (remoteInputArr == null || remoteInputArr2 == null || remoteInputArr.length != remoteInputArr2.length) {
                return true;
            }
            for (Pair pair : SequencesKt.zip(ArraysKt.asSequence((T[]) remoteInputArr), ArraysKt.asSequence((T[]) remoteInputArr2))) {
                if (!TextUtils.equals(((RemoteInput) pair.getFirst()).getLabel(), ((RemoteInput) pair.getSecond()).getLabel()) || NotifUiAdjustment.Companion.areDifferent(((RemoteInput) pair.getFirst()).getChoices(), ((RemoteInput) pair.getSecond()).getChoices())) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
            return false;
        }

        private final boolean areDifferent(CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2) {
            if (charSequenceArr == charSequenceArr2) {
                return false;
            }
            if (charSequenceArr == null || charSequenceArr2 == null || charSequenceArr.length != charSequenceArr2.length) {
                return true;
            }
            for (Pair pair : SequencesKt.zip(ArraysKt.asSequence((T[]) charSequenceArr), ArraysKt.asSequence((T[]) charSequenceArr2))) {
                if (!TextUtils.equals((CharSequence) pair.getFirst(), (CharSequence) pair.getSecond())) {
                    return true;
                }
            }
            return false;
        }
    }
}
