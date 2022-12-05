package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.media.session.MediaSession;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import androidx.versionedparcelable.VersionedParcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class MediaSessionCompat {
    private final MediaSessionImpl mImpl;

    /* loaded from: classes.dex */
    interface MediaSessionImpl {
        Token getSessionToken();
    }

    /* loaded from: classes.dex */
    public interface OnActiveChangeListener {
    }

    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public static void ensureClassLoader(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }

    public static Bundle unparcelWithClassLoader(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        ensureClassLoader(bundle);
        try {
            bundle.isEmpty();
            return bundle;
        } catch (BadParcelableException unused) {
            Log.e("MediaSessionCompat", "Could not unparcel the data.");
            return null;
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class Token implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() { // from class: android.support.v4.media.session.MediaSessionCompat.Token.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Token mo12createFromParcel(Parcel in) {
                Object readStrongBinder;
                if (Build.VERSION.SDK_INT >= 21) {
                    readStrongBinder = in.readParcelable(null);
                } else {
                    readStrongBinder = in.readStrongBinder();
                }
                return new Token(readStrongBinder);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Token[] mo13newArray(int size) {
                return new Token[size];
            }
        };
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private final Object mLock;
        private VersionedParcelable mSession2Token;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        Token(Object inner) {
            this(inner, null, null);
        }

        Token(Object inner, IMediaSession extraBinder, VersionedParcelable session2Token) {
            this.mLock = new Object();
            this.mInner = inner;
            this.mExtraBinder = extraBinder;
            this.mSession2Token = session2Token;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            if (Build.VERSION.SDK_INT >= 21) {
                dest.writeParcelable((Parcelable) this.mInner, flags);
            } else {
                dest.writeStrongBinder((IBinder) this.mInner);
            }
        }

        public int hashCode() {
            Object obj = this.mInner;
            if (obj == null) {
                return 0;
            }
            return obj.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Token)) {
                return false;
            }
            Token token = (Token) obj;
            Object obj2 = this.mInner;
            if (obj2 == null) {
                return token.mInner == null;
            }
            Object obj3 = token.mInner;
            if (obj3 != null) {
                return obj2.equals(obj3);
            }
            return false;
        }

        public Object getToken() {
            return this.mInner;
        }

        public IMediaSession getExtraBinder() {
            IMediaSession iMediaSession;
            synchronized (this.mLock) {
                iMediaSession = this.mExtraBinder;
            }
            return iMediaSession;
        }

        public void setExtraBinder(IMediaSession extraBinder) {
            synchronized (this.mLock) {
                this.mExtraBinder = extraBinder;
            }
        }

        public void setSession2Token(VersionedParcelable session2Token) {
            synchronized (this.mLock) {
                this.mSession2Token = session2Token;
            }
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class QueueItem implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>() { // from class: android.support.v4.media.session.MediaSessionCompat.QueueItem.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public QueueItem mo8createFromParcel(Parcel p) {
                return new QueueItem(p);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public QueueItem[] mo9newArray(int size) {
                return new QueueItem[size];
            }
        };
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private MediaSession.QueueItem mItemFwk;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        private QueueItem(MediaSession.QueueItem queueItem, MediaDescriptionCompat description, long id) {
            if (description != null) {
                if (id == -1) {
                    throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
                }
                this.mDescription = description;
                this.mId = id;
                this.mItemFwk = queueItem;
                return;
            }
            throw new IllegalArgumentException("Description cannot be null");
        }

        QueueItem(Parcel in) {
            this.mDescription = MediaDescriptionCompat.CREATOR.createFromParcel(in);
            this.mId = in.readLong();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            this.mDescription.writeToParcel(dest, flags);
            dest.writeLong(this.mId);
        }

        public static QueueItem fromQueueItem(Object queueItem) {
            if (queueItem == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            MediaSession.QueueItem queueItem2 = (MediaSession.QueueItem) queueItem;
            return new QueueItem(queueItem2, MediaDescriptionCompat.fromMediaDescription(queueItem2.getDescription()), queueItem2.getQueueId());
        }

        public static List<QueueItem> fromQueueItemList(List<?> itemList) {
            if (itemList == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<?> it = itemList.iterator();
            while (it.hasNext()) {
                arrayList.add(fromQueueItem(it.next()));
            }
            return arrayList;
        }

        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class ResultReceiverWrapper implements Parcelable {
        public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator<ResultReceiverWrapper>() { // from class: android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public ResultReceiverWrapper mo10createFromParcel(Parcel p) {
                return new ResultReceiverWrapper(p);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public ResultReceiverWrapper[] mo11newArray(int size) {
                return new ResultReceiverWrapper[size];
            }
        };
        ResultReceiver mResultReceiver;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        ResultReceiverWrapper(Parcel in) {
            this.mResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(in);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            this.mResultReceiver.writeToParcel(dest, flags);
        }
    }
}
