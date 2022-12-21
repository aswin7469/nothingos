package com.android.systemui.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.IRingtonePlayer;
import android.media.Ringtone;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import java.p026io.IOException;
import java.p026io.PrintWriter;
import java.util.HashMap;
import javax.inject.Inject;

@SysUISingleton
public class RingtonePlayer extends CoreStartable {
    private static final boolean LOGD = false;
    private static final String TAG = "RingtonePlayer";
    /* access modifiers changed from: private */
    public final NotificationPlayer mAsyncPlayer = new NotificationPlayer(TAG);
    private IAudioService mAudioService;
    private IRingtonePlayer mCallback = new IRingtonePlayer.Stub() {
        public void play(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z) throws RemoteException {
            playWithVolumeShaping(iBinder, uri, audioAttributes, f, z, (VolumeShaper.Configuration) null);
        }

        public void playWithVolumeShaping(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z, VolumeShaper.Configuration configuration) throws RemoteException {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = (Client) RingtonePlayer.this.mClients.get(iBinder);
                if (client == null) {
                    client = new Client(iBinder, uri, Binder.getCallingUserHandle(), audioAttributes, configuration);
                    iBinder.linkToDeath(client, 0);
                    RingtonePlayer.this.mClients.put(iBinder, client);
                }
            }
            client.mRingtone.setLooping(z);
            client.mRingtone.setVolume(f);
            client.mRingtone.play();
        }

        public void stop(IBinder iBinder) {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = (Client) RingtonePlayer.this.mClients.remove(iBinder);
            }
            if (client != null) {
                client.mToken.unlinkToDeath(client, 0);
                client.mRingtone.stop();
            }
        }

        public boolean isPlaying(IBinder iBinder) {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = (Client) RingtonePlayer.this.mClients.get(iBinder);
            }
            if (client != null) {
                return client.mRingtone.isPlaying();
            }
            return false;
        }

        public void setPlaybackProperties(IBinder iBinder, float f, boolean z, boolean z2) {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = (Client) RingtonePlayer.this.mClients.get(iBinder);
            }
            if (client != null) {
                client.mRingtone.setVolume(f);
                client.mRingtone.setLooping(z);
                client.mRingtone.setHapticGeneratorEnabled(z2);
            }
        }

        public void playAsync(Uri uri, UserHandle userHandle, boolean z, AudioAttributes audioAttributes) {
            if (Binder.getCallingUid() == 1000) {
                if (UserHandle.ALL.equals(userHandle)) {
                    userHandle = UserHandle.SYSTEM;
                }
                RingtonePlayer.this.mAsyncPlayer.play(RingtonePlayer.this.getContextForUser(userHandle), uri, z, audioAttributes);
                return;
            }
            throw new SecurityException("Async playback only available from system UID.");
        }

        public void stopAsync() {
            if (Binder.getCallingUid() == 1000) {
                RingtonePlayer.this.mAsyncPlayer.stop();
                return;
            }
            throw new SecurityException("Async playback only available from system UID.");
        }

        public String getTitle(Uri uri) {
            return Ringtone.getTitle(RingtonePlayer.this.getContextForUser(Binder.getCallingUserHandle()), uri, false, false);
        }

        public ParcelFileDescriptor openRingtone(Uri uri) {
            ContentResolver contentResolver = RingtonePlayer.this.getContextForUser(Binder.getCallingUserHandle()).getContentResolver();
            if (uri.toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                Cursor query = contentResolver.query(uri, new String[]{"is_ringtone", "is_alarm", "is_notification"}, (String) null, (String[]) null, (String) null);
                try {
                    if (query.moveToFirst() && (query.getInt(0) != 0 || query.getInt(1) != 0 || query.getInt(2) != 0)) {
                        ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
                        if (query != null) {
                            query.close();
                        }
                        return openFileDescriptor;
                    } else if (query != null) {
                        query.close();
                    }
                } catch (IOException e) {
                    throw new SecurityException((Throwable) e);
                } catch (Throwable th) {
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            throw new SecurityException("Uri is not ringtone, alarm, or notification: " + uri);
        }
    };
    /* access modifiers changed from: private */
    public final HashMap<IBinder, Client> mClients = new HashMap<>();

    @Inject
    public RingtonePlayer(Context context) {
        super(context);
    }

    public void start() {
        this.mAsyncPlayer.setUsesWakeLock(this.mContext);
        IAudioService asInterface = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
        this.mAudioService = asInterface;
        try {
            asInterface.setRingtonePlayer(this.mCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "Problem registering RingtonePlayer: " + e);
        }
    }

    private class Client implements IBinder.DeathRecipient {
        /* access modifiers changed from: private */
        public final Ringtone mRingtone;
        /* access modifiers changed from: private */
        public final IBinder mToken;

        public Client(RingtonePlayer ringtonePlayer, IBinder iBinder, Uri uri, UserHandle userHandle, AudioAttributes audioAttributes) {
            this(iBinder, uri, userHandle, audioAttributes, (VolumeShaper.Configuration) null);
        }

        Client(IBinder iBinder, Uri uri, UserHandle userHandle, AudioAttributes audioAttributes, VolumeShaper.Configuration configuration) {
            this.mToken = iBinder;
            Ringtone ringtone = new Ringtone(RingtonePlayer.this.getContextForUser(userHandle), false);
            this.mRingtone = ringtone;
            ringtone.setAudioAttributes(audioAttributes);
            ringtone.setUri(uri, configuration);
        }

        public void binderDied() {
            synchronized (RingtonePlayer.this.mClients) {
                RingtonePlayer.this.mClients.remove(this.mToken);
            }
            this.mRingtone.stop();
        }
    }

    /* access modifiers changed from: private */
    public Context getContextForUser(UserHandle userHandle) {
        try {
            return this.mContext.createPackageContextAsUser(this.mContext.getPackageName(), 0, userHandle);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Clients:");
        synchronized (this.mClients) {
            for (Client next : this.mClients.values()) {
                printWriter.print("  mToken=");
                printWriter.print((Object) next.mToken);
                printWriter.print(" mUri=");
                printWriter.println((Object) next.mRingtone.getUri());
            }
        }
    }
}
