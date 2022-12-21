package com.android.systemui.util;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class SysuiLifecycle {
    private SysuiLifecycle() {
    }

    public static LifecycleOwner viewAttachLifecycle(View view) {
        return new ViewLifecycle(view);
    }

    private static class ViewLifecycle implements LifecycleOwner, View.OnAttachStateChangeListener {
        private final LifecycleRegistry mLifecycle;

        ViewLifecycle(View view) {
            LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
            this.mLifecycle = lifecycleRegistry;
            view.addOnAttachStateChangeListener(this);
            if (view.isAttachedToWindow()) {
                lifecycleRegistry.markState(Lifecycle.State.RESUMED);
            }
        }

        public Lifecycle getLifecycle() {
            return this.mLifecycle;
        }

        public void onViewAttachedToWindow(View view) {
            this.mLifecycle.markState(Lifecycle.State.RESUMED);
        }

        public void onViewDetachedFromWindow(View view) {
            this.mLifecycle.markState(Lifecycle.State.DESTROYED);
        }
    }
}
