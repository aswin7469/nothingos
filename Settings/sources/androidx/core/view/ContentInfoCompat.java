package androidx.core.view;

import android.content.ClipData;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.util.Preconditions;
/* loaded from: classes.dex */
public final class ContentInfoCompat {
    final ClipData mClip;
    final Bundle mExtras;
    final int mFlags;
    final Uri mLinkUri;
    final int mSource;

    static String sourceToString(int source) {
        return source != 0 ? source != 1 ? source != 2 ? source != 3 ? String.valueOf(source) : "SOURCE_DRAG_AND_DROP" : "SOURCE_INPUT_METHOD" : "SOURCE_CLIPBOARD" : "SOURCE_APP";
    }

    static String flagsToString(int flags) {
        return (flags & 1) != 0 ? "FLAG_CONVERT_TO_PLAIN_TEXT" : String.valueOf(flags);
    }

    ContentInfoCompat(Builder b) {
        this.mClip = (ClipData) Preconditions.checkNotNull(b.mClip);
        this.mSource = Preconditions.checkArgumentInRange(b.mSource, 0, 3, "source");
        this.mFlags = Preconditions.checkFlagsArgument(b.mFlags, 1);
        this.mLinkUri = b.mLinkUri;
        this.mExtras = b.mExtras;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("ContentInfoCompat{clip=");
        sb.append(this.mClip.getDescription());
        sb.append(", source=");
        sb.append(sourceToString(this.mSource));
        sb.append(", flags=");
        sb.append(flagsToString(this.mFlags));
        String str2 = "";
        if (this.mLinkUri == null) {
            str = str2;
        } else {
            str = ", hasLinkUri(" + this.mLinkUri.toString().length() + ")";
        }
        sb.append(str);
        if (this.mExtras != null) {
            str2 = ", hasExtras";
        }
        sb.append(str2);
        sb.append("}");
        return sb.toString();
    }

    public ClipData getClip() {
        return this.mClip;
    }

    public int getSource() {
        return this.mSource;
    }

    public int getFlags() {
        return this.mFlags;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        ClipData mClip;
        Bundle mExtras;
        int mFlags;
        Uri mLinkUri;
        int mSource;

        public Builder(ClipData clip, int source) {
            this.mClip = clip;
            this.mSource = source;
        }

        public Builder setFlags(int flags) {
            this.mFlags = flags;
            return this;
        }

        public Builder setLinkUri(Uri linkUri) {
            this.mLinkUri = linkUri;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public ContentInfoCompat build() {
            return new ContentInfoCompat(this);
        }
    }
}
