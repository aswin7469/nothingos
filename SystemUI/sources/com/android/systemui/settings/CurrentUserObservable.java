package com.android.systemui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
/* loaded from: classes.dex */
public class CurrentUserObservable {
    private final MutableLiveData<Integer> mCurrentUser = new MutableLiveData<Integer>() { // from class: com.android.systemui.settings.CurrentUserObservable.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.LiveData
        public void onActive() {
            super.onActive();
            CurrentUserObservable.this.mTracker.startTracking();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.LiveData
        public void onInactive() {
            super.onInactive();
            CurrentUserObservable.this.mTracker.stopTracking();
        }
    };
    private final CurrentUserTracker mTracker;

    public CurrentUserObservable(BroadcastDispatcher broadcastDispatcher) {
        this.mTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.settings.CurrentUserObservable.2
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                CurrentUserObservable.this.mCurrentUser.setValue(Integer.valueOf(i));
            }
        };
    }

    public LiveData<Integer> getCurrentUser() {
        if (this.mCurrentUser.mo1438getValue() == null) {
            this.mCurrentUser.setValue(Integer.valueOf(this.mTracker.getCurrentUserId()));
        }
        return this.mCurrentUser;
    }
}
