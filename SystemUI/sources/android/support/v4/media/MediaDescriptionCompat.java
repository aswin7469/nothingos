package android.support.v4.media;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class MediaDescriptionCompat implements Parcelable {
    public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator<MediaDescriptionCompat>() { // from class: android.support.v4.media.MediaDescriptionCompat.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MediaDescriptionCompat mo2createFromParcel(Parcel in) {
            if (Build.VERSION.SDK_INT < 21) {
                return new MediaDescriptionCompat(in);
            }
            return MediaDescriptionCompat.fromMediaDescription(MediaDescription.CREATOR.createFromParcel(in));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MediaDescriptionCompat[] mo3newArray(int size) {
            return new MediaDescriptionCompat[size];
        }
    };
    private final CharSequence mDescription;
    private MediaDescription mDescriptionFwk;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    MediaDescriptionCompat(String mediaId, CharSequence title, CharSequence subtitle, CharSequence description, Bitmap icon, Uri iconUri, Bundle extras, Uri mediaUri) {
        this.mMediaId = mediaId;
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mDescription = description;
        this.mIcon = icon;
        this.mIconUri = iconUri;
        this.mExtras = extras;
        this.mMediaUri = mediaUri;
    }

    MediaDescriptionCompat(Parcel in) {
        this.mMediaId = in.readString();
        this.mTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mSubtitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        ClassLoader classLoader = MediaDescriptionCompat.class.getClassLoader();
        this.mIcon = (Bitmap) in.readParcelable(classLoader);
        this.mIconUri = (Uri) in.readParcelable(classLoader);
        this.mExtras = in.readBundle(classLoader);
        this.mMediaUri = (Uri) in.readParcelable(classLoader);
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public Bitmap getIconBitmap() {
        return this.mIcon;
    }

    public Uri getIconUri() {
        return this.mIconUri;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT < 21) {
            dest.writeString(this.mMediaId);
            TextUtils.writeToParcel(this.mTitle, dest, flags);
            TextUtils.writeToParcel(this.mSubtitle, dest, flags);
            TextUtils.writeToParcel(this.mDescription, dest, flags);
            dest.writeParcelable(this.mIcon, flags);
            dest.writeParcelable(this.mIconUri, flags);
            dest.writeBundle(this.mExtras);
            dest.writeParcelable(this.mMediaUri, flags);
            return;
        }
        ((MediaDescription) getMediaDescription()).writeToParcel(dest, flags);
    }

    public String toString() {
        return ((Object) this.mTitle) + ", " + ((Object) this.mSubtitle) + ", " + ((Object) this.mDescription);
    }

    public Object getMediaDescription() {
        int i;
        MediaDescription mediaDescription = this.mDescriptionFwk;
        if (mediaDescription != null || (i = Build.VERSION.SDK_INT) < 21) {
            return mediaDescription;
        }
        MediaDescription.Builder builder = new MediaDescription.Builder();
        builder.setMediaId(this.mMediaId);
        builder.setTitle(this.mTitle);
        builder.setSubtitle(this.mSubtitle);
        builder.setDescription(this.mDescription);
        builder.setIconBitmap(this.mIcon);
        builder.setIconUri(this.mIconUri);
        Bundle bundle = this.mExtras;
        if (i < 23 && this.mMediaUri != null) {
            if (bundle == null) {
                bundle = new Bundle();
                bundle.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
            }
            bundle.putParcelable("android.support.v4.media.description.MEDIA_URI", this.mMediaUri);
        }
        builder.setExtras(bundle);
        if (i >= 23) {
            builder.setMediaUri(this.mMediaUri);
        }
        MediaDescription build = builder.build();
        this.mDescriptionFwk = build;
        return build;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static MediaDescriptionCompat fromMediaDescription(Object descriptionObj) {
        int i;
        Bundle bundle = null;
        if (descriptionObj == null || (i = Build.VERSION.SDK_INT) < 21) {
            return null;
        }
        Builder builder = new Builder();
        MediaDescription mediaDescription = (MediaDescription) descriptionObj;
        builder.setMediaId(mediaDescription.getMediaId());
        builder.setTitle(mediaDescription.getTitle());
        builder.setSubtitle(mediaDescription.getSubtitle());
        builder.setDescription(mediaDescription.getDescription());
        builder.setIconBitmap(mediaDescription.getIconBitmap());
        builder.setIconUri(mediaDescription.getIconUri());
        Bundle extras = mediaDescription.getExtras();
        if (extras != null) {
            extras = MediaSessionCompat.unparcelWithClassLoader(extras);
        }
        Uri uri = extras != null ? (Uri) extras.getParcelable("android.support.v4.media.description.MEDIA_URI") : null;
        if (uri != null) {
            if (!extras.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") || extras.size() != 2) {
                extras.remove("android.support.v4.media.description.MEDIA_URI");
                extras.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
            }
            builder.setExtras(bundle);
            if (uri == null) {
                builder.setMediaUri(uri);
            } else if (i >= 23) {
                builder.setMediaUri(mediaDescription.getMediaUri());
            }
            MediaDescriptionCompat build = builder.build();
            build.mDescriptionFwk = mediaDescription;
            return build;
        }
        bundle = extras;
        builder.setExtras(bundle);
        if (uri == null) {
        }
        MediaDescriptionCompat build2 = builder.build();
        build2.mDescriptionFwk = mediaDescription;
        return build2;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public Builder setMediaId(String mediaId) {
            this.mMediaId = mediaId;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setSubtitle(CharSequence subtitle) {
            this.mSubtitle = subtitle;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Builder setIconBitmap(Bitmap icon) {
            this.mIcon = icon;
            return this;
        }

        public Builder setIconUri(Uri iconUri) {
            this.mIconUri = iconUri;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public Builder setMediaUri(Uri mediaUri) {
            this.mMediaUri = mediaUri;
            return this;
        }

        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
        }
    }
}
