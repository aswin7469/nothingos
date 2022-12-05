package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.commandline.Command;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: AuthRippleController.kt */
/* loaded from: classes.dex */
final class AuthRippleController$onViewAttached$1 extends Lambda implements Function0<Command> {
    final /* synthetic */ AuthRippleController this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AuthRippleController$onViewAttached$1(AuthRippleController authRippleController) {
        super(0);
        this.this$0 = authRippleController;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    @NotNull
    /* renamed from: invoke */
    public final Command mo1951invoke() {
        return new AuthRippleController.AuthRippleCommand(this.this$0);
    }
}
