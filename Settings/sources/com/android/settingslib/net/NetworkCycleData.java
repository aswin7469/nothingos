package com.android.settingslib.net;
/* loaded from: classes.dex */
public class NetworkCycleData {
    private long mEndTime;
    private long mStartTime;
    private long mTotalUsage;

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public long getTotalUsage() {
        return this.mTotalUsage;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private NetworkCycleData mObject = new NetworkCycleData();

        public Builder setStartTime(long j) {
            mo607getObject().mStartTime = j;
            return this;
        }

        public Builder setEndTime(long j) {
            mo607getObject().mEndTime = j;
            return this;
        }

        public Builder setTotalUsage(long j) {
            mo607getObject().mTotalUsage = j;
            return this;
        }

        /* renamed from: getObject */
        protected NetworkCycleData mo607getObject() {
            return this.mObject;
        }

        /* renamed from: build */
        public NetworkCycleData mo606build() {
            return mo607getObject();
        }
    }
}
