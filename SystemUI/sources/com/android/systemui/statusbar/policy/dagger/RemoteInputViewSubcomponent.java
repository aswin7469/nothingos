package com.android.systemui.statusbar.policy.dagger;

import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import dagger.BindsInstance;
import dagger.Subcomponent;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001:\u0001\u0006R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/dagger/RemoteInputViewSubcomponent;", "", "controller", "Lcom/android/systemui/statusbar/policy/RemoteInputViewController;", "getController", "()Lcom/android/systemui/statusbar/policy/RemoteInputViewController;", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Subcomponent(modules = {InternalRemoteInputViewModule.class})
/* compiled from: RemoteInput.kt */
public interface RemoteInputViewSubcomponent {

    @Subcomponent.Factory
    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/dagger/RemoteInputViewSubcomponent$Factory;", "", "create", "Lcom/android/systemui/statusbar/policy/dagger/RemoteInputViewSubcomponent;", "view", "Lcom/android/systemui/statusbar/policy/RemoteInputView;", "remoteInputController", "Lcom/android/systemui/statusbar/RemoteInputController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: RemoteInput.kt */
    public interface Factory {
        RemoteInputViewSubcomponent create(@BindsInstance RemoteInputView remoteInputView, @BindsInstance RemoteInputController remoteInputController);
    }

    RemoteInputViewController getController();
}
