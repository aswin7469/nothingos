package com.android.keyguard.clock;

import android.graphics.Bitmap;
import java.util.function.Supplier;
/* loaded from: classes.dex */
final class ClockInfo {
    private final String mId;
    private final String mName;
    private final Supplier<Bitmap> mPreview;
    private final Supplier<Bitmap> mThumbnail;
    private final Supplier<String> mTitle;

    private ClockInfo(String str, Supplier<String> supplier, String str2, Supplier<Bitmap> supplier2, Supplier<Bitmap> supplier3) {
        this.mName = str;
        this.mTitle = supplier;
        this.mId = str2;
        this.mThumbnail = supplier2;
        this.mPreview = supplier3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getName() {
        return this.mName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getTitle() {
        return this.mTitle.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getId() {
        return this.mId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getThumbnail() {
        return this.mThumbnail.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getPreview() {
        return this.mPreview.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Builder builder() {
        return new Builder();
    }

    /* loaded from: classes.dex */
    static class Builder {
        private String mId;
        private String mName;
        private Supplier<Bitmap> mPreview;
        private Supplier<Bitmap> mThumbnail;
        private Supplier<String> mTitle;

        Builder() {
        }

        public ClockInfo build() {
            return new ClockInfo(this.mName, this.mTitle, this.mId, this.mThumbnail, this.mPreview);
        }

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder setTitle(Supplier<String> supplier) {
            this.mTitle = supplier;
            return this;
        }

        public Builder setId(String str) {
            this.mId = str;
            return this;
        }

        public Builder setThumbnail(Supplier<Bitmap> supplier) {
            this.mThumbnail = supplier;
            return this;
        }

        public Builder setPreview(Supplier<Bitmap> supplier) {
            this.mPreview = supplier;
            return this;
        }
    }
}