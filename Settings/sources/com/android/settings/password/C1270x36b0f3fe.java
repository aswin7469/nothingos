package com.android.settings.password;

import android.content.Context;
import com.android.settings.password.ChooseLockPassword;
import java.util.function.Supplier;

/* renamed from: com.android.settings.password.ChooseLockPassword$ChooseLockPasswordFragment$Stage$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1270x36b0f3fe implements Supplier {
    public final /* synthetic */ ChooseLockPassword.ChooseLockPasswordFragment.Stage f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ C1270x36b0f3fe(ChooseLockPassword.ChooseLockPasswordFragment.Stage stage, Context context) {
        this.f$0 = stage;
        this.f$1 = context;
    }

    public final Object get() {
        return this.f$0.lambda$getHint$1(this.f$1);
    }
}
