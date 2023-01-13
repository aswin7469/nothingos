package com.android.systemui.util.condition;

import android.util.Log;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.condition.Condition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class Monitor implements CallbackController<Callback> {
    private boolean mAllConditionsMet = false;
    private final ArrayList<Callback> mCallbacks = new ArrayList<>();
    private final Condition.Callback mConditionCallback = new Condition.Callback() {
        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConditionChanged$0$com-android-systemui-util-condition-Monitor$1 */
        public /* synthetic */ void mo46989x452b1c27() {
            Monitor.this.updateConditionMetState();
        }

        public void onConditionChanged(Condition condition) {
            Monitor.this.mExecutor.execute(new Monitor$1$$ExternalSyntheticLambda0(this));
        }
    };
    private final Set<Condition> mConditions;
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private boolean mHaveConditionsStarted = false;
    private final String mTag = getClass().getSimpleName();

    public interface Callback {
        void onConditionsChanged(boolean z);
    }

    @Inject
    public Monitor(Executor executor, Set<Condition> set, Set<Callback> set2) {
        HashSet hashSet = new HashSet();
        this.mConditions = hashSet;
        this.mExecutor = executor;
        if (set != null) {
            hashSet.addAll(set);
        }
        if (set2 != null) {
            for (Callback addCallbackLocked : set2) {
                m3313lambda$addCallback$4$comandroidsystemuiutilconditionMonitor(addCallbackLocked);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateConditionMetState() {
        boolean z;
        Collection collection = (Collection) this.mConditions.stream().filter(new Monitor$$ExternalSyntheticLambda4()).collect(Collectors.toSet());
        if (collection.isEmpty()) {
            collection = this.mConditions;
        }
        if (collection.isEmpty()) {
            z = true;
        } else {
            z = collection.stream().map(new Monitor$$ExternalSyntheticLambda5()).allMatch(new Monitor$$ExternalSyntheticLambda6());
        }
        if (z != this.mAllConditionsMet) {
            if (shouldLog()) {
                Log.d(this.mTag, "all conditions met: " + z);
            }
            this.mAllConditionsMet = z;
            Iterator<Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                Callback next = it.next();
                if (next == null) {
                    it.remove();
                } else {
                    next.onConditionsChanged(this.mAllConditionsMet);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addConditionLocked */
    public void mo46983xac6d6b3d(Condition condition) {
        this.mConditions.add(condition);
        if (this.mHaveConditionsStarted) {
            condition.addCallback(this.mConditionCallback);
            updateConditionMetState();
        }
    }

    public void addCondition(Condition condition) {
        this.mExecutor.execute(new Monitor$$ExternalSyntheticLambda8(this, condition));
    }

    public void removeCondition(Condition condition) {
        this.mExecutor.execute(new Monitor$$ExternalSyntheticLambda0(this, condition));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeCondition$2$com-android-systemui-util-condition-Monitor */
    public /* synthetic */ void mo46986x48bc2f61(Condition condition) {
        this.mConditions.remove(condition);
        if (this.mHaveConditionsStarted) {
            condition.removeCallback(this.mConditionCallback);
            updateConditionMetState();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addCallbackLocked */
    public void m3313lambda$addCallback$4$comandroidsystemuiutilconditionMonitor(Callback callback) {
        if (!this.mCallbacks.contains(callback)) {
            if (shouldLog()) {
                Log.d(this.mTag, "adding callback");
            }
            this.mCallbacks.add(callback);
            callback.onConditionsChanged(this.mAllConditionsMet);
            if (!this.mHaveConditionsStarted) {
                if (shouldLog()) {
                    Log.d(this.mTag, "starting all conditions");
                }
                this.mConditions.forEach(new Monitor$$ExternalSyntheticLambda7(this));
                updateConditionMetState();
                this.mHaveConditionsStarted = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCallbackLocked$3$com-android-systemui-util-condition-Monitor */
    public /* synthetic */ void mo46982xa85fcf1b(Condition condition) {
        condition.addCallback(this.mConditionCallback);
    }

    public void addCallback(Callback callback) {
        this.mExecutor.execute(new Monitor$$ExternalSyntheticLambda3(this, callback));
    }

    public void removeCallback(Callback callback) {
        this.mExecutor.execute(new Monitor$$ExternalSyntheticLambda1(this, callback));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeCallback$6$com-android-systemui-util-condition-Monitor */
    public /* synthetic */ void mo46985x9e1341b1(Callback callback) {
        if (shouldLog()) {
            Log.d(this.mTag, "removing callback");
        }
        Iterator<Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            Callback next = it.next();
            if (next == null || next == callback) {
                it.remove();
            }
        }
        if (this.mCallbacks.isEmpty() && this.mHaveConditionsStarted) {
            if (shouldLog()) {
                Log.d(this.mTag, "stopping all conditions");
            }
            this.mConditions.forEach(new Monitor$$ExternalSyntheticLambda2(this));
            this.mAllConditionsMet = false;
            this.mHaveConditionsStarted = false;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeCallback$5$com-android-systemui-util-condition-Monitor */
    public /* synthetic */ void mo46984x3727f30(Condition condition) {
        condition.removeCallback(this.mConditionCallback);
    }

    private boolean shouldLog() {
        return Log.isLoggable(this.mTag, 3);
    }
}
