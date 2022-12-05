package com.android.systemui.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.core.lifecycle.Lifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LifecycleActivity.kt */
/* loaded from: classes2.dex */
public class LifecycleActivity extends Activity implements LifecycleOwner {
    @NotNull
    private final Lifecycle lifecycle = new Lifecycle(this);

    @Override // androidx.lifecycle.LifecycleOwner
    @NotNull
    /* renamed from: getLifecycle  reason: collision with other method in class */
    public Lifecycle mo1437getLifecycle() {
        return this.lifecycle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle);
    }

    @Override // android.app.Activity
    public void onCreate(@Nullable Bundle bundle, @Nullable PersistableBundle persistableBundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle, persistableBundle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}
