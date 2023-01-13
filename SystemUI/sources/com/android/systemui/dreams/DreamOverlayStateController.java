package com.android.systemui.dreams;

import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;

@SysUISingleton
public class DreamOverlayStateController implements CallbackController<Callback> {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int OP_CLEAR_STATE = 1;
    private static final int OP_SET_STATE = 2;
    public static final int STATE_DREAM_OVERLAY_ACTIVE = 1;
    private static final String TAG = "DreamOverlayStateCtlr";
    private int mAvailableComplicationTypes = 0;
    private final ArrayList<Callback> mCallbacks = new ArrayList<>();
    private final Collection<Complication> mComplications = new HashSet();
    private final Executor mExecutor;
    private boolean mShouldShowComplications = false;
    private int mState;

    public interface Callback {
        void onAvailableComplicationTypesChanged() {
        }

        void onComplicationsChanged() {
        }

        void onStateChanged() {
        }
    }

    public static /* synthetic */ HashSet $r8$lambda$4_p_WrSG7a55MBc3xyT1qI7CInQ() {
        return new HashSet();
    }

    @Inject
    public DreamOverlayStateController(@Main Executor executor) {
        this.mExecutor = executor;
    }

    public void addComplication(Complication complication) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda7(this, complication));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addComplication$1$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32534x16d6304e(Complication complication) {
        if (this.mComplications.add(complication)) {
            this.mCallbacks.stream().forEach(new DreamOverlayStateController$$ExternalSyntheticLambda5());
        }
    }

    public void removeComplication(Complication complication) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda12(this, complication));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeComplication$3$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32538x404e664d(Complication complication) {
        if (this.mComplications.remove(complication)) {
            this.mCallbacks.stream().forEach(new DreamOverlayStateController$$ExternalSyntheticLambda3());
        }
    }

    public Collection<Complication> getComplications() {
        return getComplications(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getComplications$4$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ boolean mo32535x6071a1ab(Complication complication) {
        int requiredTypeAvailability = complication.getRequiredTypeAvailability();
        if (this.mShouldShowComplications) {
            if ((getAvailableComplicationTypes() & requiredTypeAvailability) == requiredTypeAvailability) {
                return true;
            }
            return false;
        } else if (requiredTypeAvailability == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Collection<Complication> getComplications(boolean z) {
        Collection<Complication> collection;
        if (z) {
            collection = (Collection) this.mComplications.stream().filter(new DreamOverlayStateController$$ExternalSyntheticLambda10(this)).collect(Collectors.toCollection(new DreamOverlayStateController$$ExternalSyntheticLambda11()));
        } else {
            collection = this.mComplications;
        }
        return Collections.unmodifiableCollection(collection);
    }

    private void notifyCallbacks(Consumer<Callback> consumer) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda8(this, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$notifyCallbacks$5$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32536xd88d64d8(Consumer consumer) {
        Iterator<Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    public void addCallback(Callback callback) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda2(this, callback));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCallback$6$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32533x1974fc9a(Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
            if (!this.mComplications.isEmpty()) {
                callback.onComplicationsChanged();
            }
        }
    }

    public void removeCallback(Callback callback) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda9(this, callback));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeCallback$7$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32537x9c5cb018(Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        this.mCallbacks.remove((Object) callback);
    }

    public boolean isOverlayActive() {
        return containsState(1);
    }

    private boolean containsState(int i) {
        return (this.mState & i) != 0;
    }

    private void modifyState(int i, int i2) {
        int i3 = this.mState;
        if (i == 1) {
            this.mState = (~i2) & i3;
        } else if (i == 2) {
            this.mState = i3 | i2;
        }
        if (i3 != this.mState) {
            notifyCallbacks(new DreamOverlayStateController$$ExternalSyntheticLambda1());
        }
    }

    public void setOverlayActive(boolean z) {
        modifyState(z ? 2 : 1, 1);
    }

    public int getAvailableComplicationTypes() {
        return this.mAvailableComplicationTypes;
    }

    public void setAvailableComplicationTypes(int i) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda6(this, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setAvailableComplicationTypes$9$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32539x8393206b(int i) {
        this.mAvailableComplicationTypes = i;
        this.mCallbacks.forEach(new DreamOverlayStateController$$ExternalSyntheticLambda0());
    }

    public boolean getShouldShowComplications() {
        return this.mShouldShowComplications;
    }

    public void setShouldShowComplications(boolean z) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda4(this, z));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setShouldShowComplications$10$com-android-systemui-dreams-DreamOverlayStateController */
    public /* synthetic */ void mo32540x9c0d6222(boolean z) {
        this.mShouldShowComplications = z;
        this.mCallbacks.forEach(new DreamOverlayStateController$$ExternalSyntheticLambda0());
    }
}
