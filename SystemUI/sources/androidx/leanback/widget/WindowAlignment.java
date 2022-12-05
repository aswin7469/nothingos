package androidx.leanback.widget;
/* loaded from: classes.dex */
class WindowAlignment {
    public final Axis horizontal;
    private Axis mMainAxis;
    private int mOrientation = 0;
    private Axis mSecondAxis;
    public final Axis vertical;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowAlignment() {
        Axis axis = new Axis("vertical");
        this.vertical = axis;
        Axis axis2 = new Axis("horizontal");
        this.horizontal = axis2;
        this.mMainAxis = axis2;
        this.mSecondAxis = axis;
    }

    /* loaded from: classes.dex */
    public static class Axis {
        private int mMaxEdge;
        private int mMaxScroll;
        private int mMinEdge;
        private int mMinScroll;
        private int mPaddingMax;
        private int mPaddingMin;
        private boolean mReversedFlow;
        private int mSize;
        private int mPreferredKeyLine = 2;
        private int mWindowAlignment = 3;
        private int mWindowAlignmentOffset = 0;
        private float mWindowAlignmentOffsetPercent = 50.0f;

        final int calculateScrollToKeyLine(int viewCenterPosition, int keyLine) {
            return viewCenterPosition - keyLine;
        }

        public Axis(String name) {
            reset();
        }

        public final void setWindowAlignment(int windowAlignment) {
            this.mWindowAlignment = windowAlignment;
        }

        final boolean isPreferKeylineOverHighEdge() {
            return (this.mPreferredKeyLine & 2) != 0;
        }

        final boolean isPreferKeylineOverLowEdge() {
            return (this.mPreferredKeyLine & 1) != 0;
        }

        public final int getMinScroll() {
            return this.mMinScroll;
        }

        public final void invalidateScrollMin() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMinScroll = Integer.MIN_VALUE;
        }

        public final int getMaxScroll() {
            return this.mMaxScroll;
        }

        public final void invalidateScrollMax() {
            this.mMaxEdge = Integer.MAX_VALUE;
            this.mMaxScroll = Integer.MAX_VALUE;
        }

        void reset() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMaxEdge = Integer.MAX_VALUE;
        }

        public final boolean isMinUnknown() {
            return this.mMinEdge == Integer.MIN_VALUE;
        }

        public final boolean isMaxUnknown() {
            return this.mMaxEdge == Integer.MAX_VALUE;
        }

        public final void setSize(int size) {
            this.mSize = size;
        }

        public final int getSize() {
            return this.mSize;
        }

        public final void setPadding(int paddingMin, int paddingMax) {
            this.mPaddingMin = paddingMin;
            this.mPaddingMax = paddingMax;
        }

        public final int getPaddingMin() {
            return this.mPaddingMin;
        }

        public final int getPaddingMax() {
            return this.mPaddingMax;
        }

        public final int getClientSize() {
            return (this.mSize - this.mPaddingMin) - this.mPaddingMax;
        }

        final int calculateKeyline() {
            if (!this.mReversedFlow) {
                int i = this.mWindowAlignmentOffset;
                if (i < 0) {
                    i += this.mSize;
                }
                float f = this.mWindowAlignmentOffsetPercent;
                return f != -1.0f ? i + ((int) ((this.mSize * f) / 100.0f)) : i;
            }
            int i2 = this.mWindowAlignmentOffset;
            int i3 = i2 >= 0 ? this.mSize - i2 : -i2;
            float f2 = this.mWindowAlignmentOffsetPercent;
            return f2 != -1.0f ? i3 - ((int) ((this.mSize * f2) / 100.0f)) : i3;
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0048, code lost:
            r4.mMaxScroll = (r4.mMaxEdge - r4.mPaddingMin) - r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:8:0x0027, code lost:
            r4.mMinScroll = r4.mMinEdge - r4.mPaddingMin;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final void updateMinMax(int minEdge, int maxEdge, int minChildViewCenter, int maxChildViewCenter) {
            this.mMinEdge = minEdge;
            this.mMaxEdge = maxEdge;
            int clientSize = getClientSize();
            int calculateKeyline = calculateKeyline();
            boolean isMinUnknown = isMinUnknown();
            boolean isMaxUnknown = isMaxUnknown();
            if (!isMinUnknown) {
                if (!this.mReversedFlow) {
                    this.mMinScroll = calculateScrollToKeyLine(minChildViewCenter, calculateKeyline);
                } else {
                    this.mMinScroll = calculateScrollToKeyLine(minChildViewCenter, calculateKeyline);
                }
            }
            if (!isMaxUnknown) {
                if (!this.mReversedFlow) {
                    this.mMaxScroll = calculateScrollToKeyLine(maxChildViewCenter, calculateKeyline);
                } else {
                    this.mMaxScroll = calculateScrollToKeyLine(maxChildViewCenter, calculateKeyline);
                }
            }
            if (isMaxUnknown || isMinUnknown) {
                return;
            }
            if (!this.mReversedFlow) {
                int i = this.mWindowAlignment;
                if ((i & 1) != 0) {
                    if (isPreferKeylineOverLowEdge()) {
                        this.mMinScroll = Math.min(this.mMinScroll, calculateScrollToKeyLine(maxChildViewCenter, calculateKeyline));
                    }
                    this.mMaxScroll = Math.max(this.mMinScroll, this.mMaxScroll);
                    return;
                } else if ((i & 2) == 0) {
                    return;
                } else {
                    if (isPreferKeylineOverHighEdge()) {
                        this.mMaxScroll = Math.max(this.mMaxScroll, calculateScrollToKeyLine(minChildViewCenter, calculateKeyline));
                    }
                    this.mMinScroll = Math.min(this.mMinScroll, this.mMaxScroll);
                    return;
                }
            }
            int i2 = this.mWindowAlignment;
            if ((i2 & 1) != 0) {
                if (isPreferKeylineOverLowEdge()) {
                    this.mMaxScroll = Math.max(this.mMaxScroll, calculateScrollToKeyLine(minChildViewCenter, calculateKeyline));
                }
                this.mMinScroll = Math.min(this.mMinScroll, this.mMaxScroll);
            } else if ((i2 & 2) == 0) {
            } else {
                if (isPreferKeylineOverHighEdge()) {
                    this.mMinScroll = Math.min(this.mMinScroll, calculateScrollToKeyLine(maxChildViewCenter, calculateKeyline));
                }
                this.mMaxScroll = Math.max(this.mMinScroll, this.mMaxScroll);
            }
        }

        public final int getScroll(int viewCenter) {
            int i;
            int i2;
            int size = getSize();
            int calculateKeyline = calculateKeyline();
            boolean isMinUnknown = isMinUnknown();
            boolean isMaxUnknown = isMaxUnknown();
            if (!isMinUnknown) {
                int i3 = this.mPaddingMin;
                int i4 = calculateKeyline - i3;
                if (this.mReversedFlow ? (this.mWindowAlignment & 2) != 0 : (this.mWindowAlignment & 1) != 0) {
                    int i5 = this.mMinEdge;
                    if (viewCenter - i5 <= i4) {
                        int i6 = i5 - i3;
                        return (isMaxUnknown || i6 <= (i2 = this.mMaxScroll)) ? i6 : i2;
                    }
                }
            }
            if (!isMaxUnknown) {
                int i7 = this.mPaddingMax;
                int i8 = (size - calculateKeyline) - i7;
                if (this.mReversedFlow ? (this.mWindowAlignment & 1) != 0 : (this.mWindowAlignment & 2) != 0) {
                    int i9 = this.mMaxEdge;
                    if (i9 - viewCenter <= i8) {
                        int i10 = i9 - (size - i7);
                        return (isMinUnknown || i10 >= (i = this.mMinScroll)) ? i10 : i;
                    }
                }
            }
            return calculateScrollToKeyLine(viewCenter, calculateKeyline);
        }

        public final void setReversedFlow(boolean reversedFlow) {
            this.mReversedFlow = reversedFlow;
        }

        public String toString() {
            return " min:" + this.mMinEdge + " " + this.mMinScroll + " max:" + this.mMaxEdge + " " + this.mMaxScroll;
        }
    }

    public final Axis mainAxis() {
        return this.mMainAxis;
    }

    public final Axis secondAxis() {
        return this.mSecondAxis;
    }

    public final void setOrientation(int orientation) {
        this.mOrientation = orientation;
        if (orientation == 0) {
            this.mMainAxis = this.horizontal;
            this.mSecondAxis = this.vertical;
            return;
        }
        this.mMainAxis = this.vertical;
        this.mSecondAxis = this.horizontal;
    }

    public final void reset() {
        mainAxis().reset();
    }

    public String toString() {
        return "horizontal=" + this.horizontal + "; vertical=" + this.vertical;
    }
}
