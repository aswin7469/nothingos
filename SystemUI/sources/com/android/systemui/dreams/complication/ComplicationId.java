package com.android.systemui.dreams.complication;

public class ComplicationId {
    private int mId;

    public static class Factory {
        private int mNextId;

        /* access modifiers changed from: package-private */
        public ComplicationId getNextId() {
            int i = this.mNextId;
            this.mNextId = i + 1;
            return new ComplicationId(i);
        }
    }

    private ComplicationId(int i) {
        this.mId = i;
    }

    public String toString() {
        return "ComplicationId{mId=" + this.mId + "}";
    }
}
