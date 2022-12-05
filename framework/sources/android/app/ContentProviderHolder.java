package android.app;

import android.content.ContentProviderNative;
import android.content.IContentProvider;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class ContentProviderHolder implements Parcelable {
    public static final Parcelable.Creator<ContentProviderHolder> CREATOR = new Parcelable.Creator<ContentProviderHolder>() { // from class: android.app.ContentProviderHolder.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ContentProviderHolder mo3559createFromParcel(Parcel source) {
            return new ContentProviderHolder(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ContentProviderHolder[] mo3560newArray(int size) {
            return new ContentProviderHolder[size];
        }
    };
    public IBinder connection;
    public final ProviderInfo info;
    public boolean mLocal;
    public boolean noReleaseNeeded;
    public IContentProvider provider;

    public ContentProviderHolder(ProviderInfo _info) {
        this.info = _info;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.info.writeToParcel(dest, 0);
        IContentProvider iContentProvider = this.provider;
        if (iContentProvider != null) {
            dest.writeStrongBinder(iContentProvider.asBinder());
        } else {
            dest.writeStrongBinder(null);
        }
        dest.writeStrongBinder(this.connection);
        dest.writeInt(this.noReleaseNeeded ? 1 : 0);
        dest.writeInt(this.mLocal ? 1 : 0);
    }

    private ContentProviderHolder(Parcel source) {
        this.info = ProviderInfo.CREATOR.mo3559createFromParcel(source);
        this.provider = ContentProviderNative.asInterface(source.readStrongBinder());
        this.connection = source.readStrongBinder();
        boolean z = true;
        this.noReleaseNeeded = source.readInt() != 0;
        this.mLocal = source.readInt() == 0 ? false : z;
    }
}
