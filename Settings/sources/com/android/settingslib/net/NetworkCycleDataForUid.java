package com.android.settingslib.net;

import com.android.settingslib.net.NetworkCycleData;
/* loaded from: classes.dex */
public class NetworkCycleDataForUid extends NetworkCycleData {
    private long mBackgroudUsage;
    private long mForegroudUsage;

    private NetworkCycleDataForUid() {
    }

    public long getBackgroudUsage() {
        return this.mBackgroudUsage;
    }

    public long getForegroudUsage() {
        return this.mForegroudUsage;
    }

    /* loaded from: classes.dex */
    public static class Builder extends NetworkCycleData.Builder {
        private NetworkCycleDataForUid mObject = new NetworkCycleDataForUid();

        public Builder setBackgroundUsage(long j) {
            mo607getObject().mBackgroudUsage = j;
            return this;
        }

        public Builder setForegroundUsage(long j) {
            mo607getObject().mForegroudUsage = j;
            return this;
        }

        @Override // com.android.settingslib.net.NetworkCycleData.Builder
        /* renamed from: getObject  reason: collision with other method in class */
        public NetworkCycleDataForUid mo607getObject() {
            return this.mObject;
        }

        @Override // com.android.settingslib.net.NetworkCycleData.Builder
        /* renamed from: build  reason: collision with other method in class */
        public NetworkCycleDataForUid mo606build() {
            return mo607getObject();
        }
    }
}
