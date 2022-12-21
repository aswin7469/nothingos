package com.android.systemui.util.condition;

import android.util.Log;
import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Condition implements CallbackController<Callback> {
    private final ArrayList<WeakReference<Callback>> mCallbacks = new ArrayList<>();
    private boolean mIsConditionMet = false;
    private boolean mOverriding = false;
    private boolean mStarted = false;
    private final String mTag = getClass().getSimpleName();

    public interface Callback {
        void onConditionChanged(Condition condition);
    }

    /* access modifiers changed from: protected */
    public abstract void start();

    /* access modifiers changed from: protected */
    public abstract void stop();

    public void setOverriding(boolean z) {
        this.mOverriding = z;
        updateCondition(this.mIsConditionMet);
    }

    public boolean isOverridingCondition() {
        return this.mOverriding;
    }

    public void addCallback(Callback callback) {
        if (shouldLog()) {
            Log.d(this.mTag, "adding callback");
        }
        this.mCallbacks.add(new WeakReference(callback));
        if (this.mStarted) {
            callback.onConditionChanged(this);
            return;
        }
        start();
        this.mStarted = true;
    }

    public void removeCallback(Callback callback) {
        if (shouldLog()) {
            Log.d(this.mTag, "removing callback");
        }
        Iterator<WeakReference<Callback>> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            Callback callback2 = (Callback) it.next().get();
            if (callback2 == null || callback2 == callback) {
                it.remove();
            }
        }
        if (this.mCallbacks.isEmpty() && this.mStarted) {
            stop();
            this.mStarted = false;
        }
    }

    /* access modifiers changed from: protected */
    public void updateCondition(boolean z) {
        if (this.mIsConditionMet != z) {
            if (shouldLog()) {
                Log.d(this.mTag, "updating condition to " + z);
            }
            this.mIsConditionMet = z;
            Iterator<WeakReference<Callback>> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                Callback callback = (Callback) it.next().get();
                if (callback == null) {
                    it.remove();
                } else {
                    callback.onConditionChanged(this);
                }
            }
        }
    }

    public boolean isConditionMet() {
        return this.mIsConditionMet;
    }

    private boolean shouldLog() {
        return Log.isLoggable(this.mTag, 3);
    }
}
