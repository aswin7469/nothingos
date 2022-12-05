package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import java.util.List;
/* loaded from: classes.dex */
public interface IMediaControllerCallback extends IInterface {
    void onCaptioningEnabledChanged(boolean enabled) throws RemoteException;

    void onEvent(String event, Bundle extras) throws RemoteException;

    void onExtrasChanged(Bundle extras) throws RemoteException;

    void onMetadataChanged(MediaMetadataCompat metadata) throws RemoteException;

    void onPlaybackStateChanged(PlaybackStateCompat state) throws RemoteException;

    void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) throws RemoteException;

    void onQueueTitleChanged(CharSequence title) throws RemoteException;

    void onRepeatModeChanged(int repeatMode) throws RemoteException;

    void onSessionDestroyed() throws RemoteException;

    void onSessionReady() throws RemoteException;

    void onShuffleModeChanged(int shuffleMode) throws RemoteException;

    void onShuffleModeChangedRemoved(boolean enabled) throws RemoteException;

    void onVolumeInfoChanged(ParcelableVolumeInfo info) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMediaControllerCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "android.support.v4.media.session.IMediaControllerCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString("android.support.v4.media.session.IMediaControllerCallback");
                return true;
            }
            boolean z = false;
            Bundle bundle = null;
            ParcelableVolumeInfo parcelableVolumeInfo = null;
            Bundle bundle2 = null;
            CharSequence charSequence = null;
            MediaMetadataCompat mediaMetadataCompat = null;
            PlaybackStateCompat playbackStateCompat = null;
            switch (code) {
                case 1:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    String readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    onEvent(readString, bundle);
                    return true;
                case 2:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    onSessionDestroyed();
                    return true;
                case 3:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        playbackStateCompat = PlaybackStateCompat.CREATOR.createFromParcel(data);
                    }
                    onPlaybackStateChanged(playbackStateCompat);
                    return true;
                case 4:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        mediaMetadataCompat = MediaMetadataCompat.CREATOR.createFromParcel(data);
                    }
                    onMetadataChanged(mediaMetadataCompat);
                    return true;
                case 5:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    onQueueChanged(data.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
                    return true;
                case 6:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        charSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    }
                    onQueueTitleChanged(charSequence);
                    return true;
                case 7:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    onExtrasChanged(bundle2);
                    return true;
                case 8:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        parcelableVolumeInfo = ParcelableVolumeInfo.CREATOR.createFromParcel(data);
                    }
                    onVolumeInfoChanged(parcelableVolumeInfo);
                    return true;
                case 9:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    onRepeatModeChanged(data.readInt());
                    return true;
                case 10:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    onShuffleModeChangedRemoved(z);
                    return true;
                case 11:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    onCaptioningEnabledChanged(z);
                    return true;
                case 12:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    onShuffleModeChanged(data.readInt());
                    return true;
                case 13:
                    data.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    onSessionReady();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }
}
