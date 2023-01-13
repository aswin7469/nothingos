package com.nothing.systemui.assist;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AssistManagerEx_Factory implements Factory<AssistManagerEx> {
    private final Provider<Context> contextProvider;

    public AssistManagerEx_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public AssistManagerEx get() {
        return newInstance(this.contextProvider.get());
    }

    public static AssistManagerEx_Factory create(Provider<Context> provider) {
        return new AssistManagerEx_Factory(provider);
    }

    public static AssistManagerEx newInstance(Context context) {
        return new AssistManagerEx(context);
    }
}
