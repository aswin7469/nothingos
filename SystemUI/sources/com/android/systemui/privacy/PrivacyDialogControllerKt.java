package com.android.systemui.privacy;

import android.content.Context;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogControllerKt {
    @NotNull
    private static final PrivacyDialogControllerKt$defaultDialogProvider$1 defaultDialogProvider = new PrivacyDialogController.DialogProvider() { // from class: com.android.systemui.privacy.PrivacyDialogControllerKt$defaultDialogProvider$1
        @Override // com.android.systemui.privacy.PrivacyDialogController.DialogProvider
        @NotNull
        public PrivacyDialog makeDialog(@NotNull Context context, @NotNull List<PrivacyDialog.PrivacyElement> list, @NotNull Function2<? super String, ? super Integer, Unit> starter) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(starter, "starter");
            return new PrivacyDialog(context, list, starter);
        }
    };
}
