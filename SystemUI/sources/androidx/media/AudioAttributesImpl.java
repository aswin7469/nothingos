package androidx.media;

import androidx.versionedparcelable.VersionedParcelable;
/* loaded from: classes.dex */
public interface AudioAttributesImpl extends VersionedParcelable {

    /* loaded from: classes.dex */
    public interface Builder {
        AudioAttributesImpl build();

        /* renamed from: setLegacyStreamType */
        Builder mo123setLegacyStreamType(int streamType);
    }
}
