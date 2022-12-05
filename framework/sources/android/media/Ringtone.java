package android.media;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.media.audiofx.HapticGenerator;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class Ringtone {
    private static final boolean LOGD = true;
    private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    private static final String TAG = "Ringtone";
    private final boolean mAllowRemote;
    private final AudioManager mAudioManager;
    private final Context mContext;
    private HapticGenerator mHapticGenerator;
    private MediaPlayer mLocalPlayer;
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;
    private String mTitle;
    private Uri mUri;
    private VolumeShaper mVolumeShaper;
    private VolumeShaper.Configuration mVolumeShaperConfig;
    private static final String[] MEDIA_COLUMNS = {"_id", "title"};
    private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList<>();
    private static String DESKCLOCK_APP_NAME = "com.google.android.deskclock";
    private static String SETTINGS_APP_PACKAGE_NAME = "com.android.settings";
    private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
    private OnMediaPlayerListener mMediaPlayerListener = null;
    private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setUsage(6).setContentType(4).build();
    private boolean mIsLooping = false;
    private float mVolume = 1.0f;
    private boolean mHapticGeneratorEnabled = false;
    private final Object mPlaybackSettingsLock = new Object();

    /* loaded from: classes2.dex */
    public interface OnMediaPlayerListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }

    public Ringtone(Context context, boolean allowRemote) {
        Binder binder = null;
        this.mContext = context;
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.mAudioManager = audioManager;
        this.mAllowRemote = allowRemote;
        this.mRemotePlayer = allowRemote ? audioManager.getRingtonePlayer() : null;
        this.mRemoteToken = allowRemote ? new Binder() : binder;
    }

    @Deprecated
    public void setStreamType(int streamType) {
        PlayerBase.deprecateStreamTypeForPlayback(streamType, TAG, "setStreamType()");
        setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build());
    }

    @Deprecated
    public int getStreamType() {
        return AudioAttributes.toLegacyStreamType(this.mAudioAttributes);
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes == null) {
            throw new IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
        }
        this.mAudioAttributes = attributes;
        setUri(this.mUri, this.mVolumeShaperConfig);
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public void setLooping(boolean looping) {
        synchronized (this.mPlaybackSettingsLock) {
            this.mIsLooping = looping;
            applyPlaybackProperties_sync();
        }
    }

    public boolean isLooping() {
        boolean z;
        synchronized (this.mPlaybackSettingsLock) {
            z = this.mIsLooping;
        }
        return z;
    }

    public void setVolume(float volume) {
        synchronized (this.mPlaybackSettingsLock) {
            if (volume < 0.0f) {
                volume = 0.0f;
            }
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            this.mVolume = volume;
            applyPlaybackProperties_sync();
        }
    }

    public float getVolume() {
        float f;
        synchronized (this.mPlaybackSettingsLock) {
            f = this.mVolume;
        }
        return f;
    }

    public boolean setHapticGeneratorEnabled(boolean enabled) {
        if (!HapticGenerator.isAvailable()) {
            return false;
        }
        synchronized (this.mPlaybackSettingsLock) {
            this.mHapticGeneratorEnabled = enabled;
            applyPlaybackProperties_sync();
        }
        return true;
    }

    public boolean isHapticGeneratorEnabled() {
        boolean z;
        synchronized (this.mPlaybackSettingsLock) {
            z = this.mHapticGeneratorEnabled;
        }
        return z;
    }

    private void applyPlaybackProperties_sync() {
        IRingtonePlayer iRingtonePlayer;
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.mVolume);
            this.mLocalPlayer.setLooping(this.mIsLooping);
            if (this.mHapticGenerator == null && this.mHapticGeneratorEnabled) {
                this.mHapticGenerator = HapticGenerator.create(this.mLocalPlayer.getAudioSessionId());
            }
            HapticGenerator hapticGenerator = this.mHapticGenerator;
            if (hapticGenerator != null) {
                hapticGenerator.setEnabled(this.mHapticGeneratorEnabled);
            }
        } else if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                iRingtonePlayer.setPlaybackProperties(this.mRemoteToken, this.mVolume, this.mIsLooping, this.mHapticGeneratorEnabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem setting playback properties: ", e);
            }
        } else {
            Log.w(TAG, "Neither local nor remote player available when applying playback properties");
        }
    }

    public String getTitle(Context context) {
        String str = this.mTitle;
        if (str != null) {
            return str;
        }
        String title = getTitle(context, this.mUri, true, this.mAllowRemote);
        this.mTitle = title;
        return title;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x006c, code lost:
        if (r11 != null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006e, code lost:
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0099, code lost:
        if (r7 != null) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009b, code lost:
        r7 = r12.getString(com.android.internal.R.string.ringtone_silent);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0096, code lost:
        if (r11 == null) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getTitle(Context context, Uri uri, boolean followSettingsUri, boolean allowRemote) {
        ContentResolver res = context.getContentResolver();
        String title = null;
        if (uri != null) {
            String authority = ContentProvider.getAuthorityWithoutUserId(uri.getAuthority());
            if (!"settings".equals(authority)) {
                Cursor cursor = null;
                try {
                    try {
                        if (DeviceConfig.NAMESPACE_MEDIA.equals(authority)) {
                            String mediaSelection = allowRemote ? null : MEDIA_SELECTION;
                            cursor = res.query(uri, MEDIA_COLUMNS, mediaSelection, null, null);
                            if (cursor != null && cursor.getCount() == 1) {
                                cursor.moveToFirst();
                                return cursor.getString(1);
                            }
                        }
                    } catch (SecurityException e) {
                        IRingtonePlayer mRemotePlayer = null;
                        if (allowRemote) {
                            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
                            mRemotePlayer = audioManager.getRingtonePlayer();
                        }
                        if (mRemotePlayer != null) {
                            try {
                                title = mRemotePlayer.getTitle(uri);
                            } catch (RemoteException e2) {
                            }
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (followSettingsUri) {
                Uri actualUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.getDefaultType(uri));
                String actualTitle = getTitle(context, actualUri, false, allowRemote);
                title = context.getString(R.string.ringtone_default_with_actual, actualTitle);
            }
        } else {
            title = context.getString(R.string.ringtone_silent);
        }
        if (title == null) {
            String title2 = context.getString(R.string.ringtone_unknown);
            return title2 == null ? "" : title2;
        }
        return title;
    }

    public void setUri(Uri uri) {
        setUri(uri, null);
    }

    public void setUri(Uri uri, VolumeShaper.Configuration volumeShaperConfig) {
        this.mVolumeShaperConfig = volumeShaperConfig;
        destroyLocalPlayer();
        this.mUri = uri;
        if (uri == null) {
            return;
        }
        this.mLocalPlayer = new MediaPlayer();
        try {
            checkDoAlarmVibrate();
            this.mLocalPlayer.setDataSource(this.mContext, this.mUri);
            this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
            synchronized (this.mPlaybackSettingsLock) {
                applyPlaybackProperties_sync();
            }
            VolumeShaper.Configuration configuration = this.mVolumeShaperConfig;
            if (configuration != null) {
                this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(configuration);
            }
            this.mLocalPlayer.prepare();
        } catch (IOException | SecurityException e) {
            destroyLocalPlayer();
            if (!this.mAllowRemote) {
                Log.w(TAG, "Remote playback not allowed: " + e);
            }
        }
        if (this.mLocalPlayer != null) {
            Log.d(TAG, "Successfully created local player");
        } else {
            Log.d(TAG, "Problem opening; delegating to remote player");
        }
    }

    private void checkDoAlarmVibrate() {
        String packageName = NtRingtoneSyncWithVibrateUtils.getAppName(this.mContext, Process.myPid());
        if (DESKCLOCK_APP_NAME.equals(packageName) && NtRingtoneSyncWithVibrateUtils.getInstance(this.mContext).checkVibEnable(DESKCLOCK_APP_NAME)) {
            Uri uri = this.mUri;
            if (uri.toString().equals(Settings.System.DEFAULT_ALARM_ALERT_URI.toString())) {
                uri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 4);
            }
            if (NtRingtoneSyncWithVibrateUtils.isInternalRingtone(uri)) {
                Log.d(TAG, "[ALARM_VIB] setUri: enable HAPTIC-OGG vib for alarm internel uri: " + uri);
                this.mAudioAttributes = new AudioAttributes.Builder(this.mAudioAttributes).setHapticChannelsMuted(false).build();
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                audioManager.setEnableDoVibrateTime(packageName, System.currentTimeMillis() + 200);
                return;
            }
            Log.d(TAG, "[ALARM_VIB] setUri: just play ANDROID vib for alarm externel uri: " + uri);
        }
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void play() {
        Uri uri;
        boolean looping;
        float volume;
        if (this.mLocalPlayer != null) {
            String packageName = NtRingtoneSyncWithVibrateUtils.getAppName(this.mContext, Process.myPid());
            boolean isHapticOnly = AudioManager.hasHapticChannels(this.mContext, this.mUri) && !this.mAudioAttributes.areHapticChannelsMuted() && this.mVolume == 0.0f;
            if (isHapticOnly || this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0 || (SETTINGS_APP_PACKAGE_NAME.equals(packageName) && this.mAudioManager.getRingerMode() != 0)) {
                startLocalPlayer();
            }
        } else if (this.mAllowRemote && this.mRemotePlayer != null && (uri = this.mUri) != null) {
            Uri canonicalUri = uri.getCanonicalUri();
            synchronized (this.mPlaybackSettingsLock) {
                looping = this.mIsLooping;
                volume = this.mVolume;
            }
            try {
                this.mRemotePlayer.playWithVolumeShaping(this.mRemoteToken, canonicalUri, this.mAudioAttributes, volume, looping, this.mVolumeShaperConfig);
            } catch (RemoteException e) {
                if (!playFallbackRingtone()) {
                    Log.w(TAG, "Problem playing ringtone: " + e);
                }
            }
        } else if (!playFallbackRingtone()) {
            Log.w(TAG, "Neither local nor remote playback available");
        }
    }

    public void stop() {
        IRingtonePlayer iRingtonePlayer;
        if (this.mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                iRingtonePlayer.stop(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem stopping ringtone: " + e);
            }
        }
    }

    private void destroyLocalPlayer() {
        if (this.mLocalPlayer != null) {
            HapticGenerator hapticGenerator = this.mHapticGenerator;
            if (hapticGenerator != null) {
                hapticGenerator.release();
                this.mHapticGenerator = null;
            }
            this.mLocalPlayer.setOnCompletionListener(null);
            this.mLocalPlayer.reset();
            this.mLocalPlayer.release();
            this.mLocalPlayer = null;
            this.mVolumeShaper = null;
            ArrayList<Ringtone> arrayList = sActiveRingtones;
            synchronized (arrayList) {
                arrayList.remove(this);
            }
            checkStopRingtoneVibrate();
        }
    }

    private void startLocalPlayer() {
        if (this.mLocalPlayer == null) {
            return;
        }
        ArrayList<Ringtone> arrayList = sActiveRingtones;
        synchronized (arrayList) {
            arrayList.add(this);
        }
        this.mLocalPlayer.setOnCompletionListener(this.mCompletionListener);
        this.mLocalPlayer.start();
        VolumeShaper volumeShaper = this.mVolumeShaper;
        if (volumeShaper != null) {
            volumeShaper.apply(VolumeShaper.Operation.PLAY);
        }
        checkPlayRingtoneVibrate();
    }

    public int getDuration() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public boolean isPlaying() {
        IRingtonePlayer iRingtonePlayer;
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                return iRingtonePlayer.isPlaying(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem checking ringtone: " + e);
                return false;
            }
        }
        Log.w(TAG, "Neither local nor remote playback available");
        return false;
    }

    private boolean playFallbackRingtone() {
        if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
            int ringtoneType = RingtoneManager.getDefaultType(this.mUri);
            if (ringtoneType == -1 || RingtoneManager.getActualDefaultRingtoneUri(this.mContext, ringtoneType) != null) {
                try {
                    AssetFileDescriptor afd = this.mContext.getResources().openRawResourceFd(R.raw.fallbackring);
                    if (afd != null) {
                        this.mLocalPlayer = new MediaPlayer();
                        if (afd.getDeclaredLength() < 0) {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor());
                        } else {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                        }
                        this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                        synchronized (this.mPlaybackSettingsLock) {
                            applyPlaybackProperties_sync();
                        }
                        VolumeShaper.Configuration configuration = this.mVolumeShaperConfig;
                        if (configuration != null) {
                            this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(configuration);
                        }
                        this.mLocalPlayer.prepare();
                        startLocalPlayer();
                        afd.close();
                        return true;
                    }
                    Log.e(TAG, "Could not load fallback ringtone");
                    return false;
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Fallback ringtone does not exist");
                    return false;
                } catch (IOException e2) {
                    destroyLocalPlayer();
                    Log.e(TAG, "Failed to open fallback ringtone");
                    return false;
                }
            }
            Log.w(TAG, "not playing fallback for " + this.mUri);
            return false;
        }
        return false;
    }

    void setTitle(String title) {
        this.mTitle = title;
    }

    private void checkPlayRingtoneVibrate() {
        NtRingtoneSyncWithVibrateUtils.getInstance(this.mContext).playRingtoneVibrate(this.mLocalPlayer, this, this.mUri, getDuration());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStopRingtoneVibrate() {
        NtRingtoneSyncWithVibrateUtils.getInstance(this.mContext).stopPlayRingtoneVibrate();
    }

    protected void finalize() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            checkStopRingtoneVibrate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        MyOnCompletionListener() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mp) {
            synchronized (Ringtone.sActiveRingtones) {
                Ringtone.sActiveRingtones.remove(Ringtone.this);
            }
            mp.setOnCompletionListener(null);
            Ringtone.this.checkStopRingtoneVibrate();
            if (Ringtone.this.mMediaPlayerListener != null) {
                Ringtone.this.mMediaPlayerListener.onCompletion(mp);
            }
        }
    }

    public void setOnMediaPlayerListener(OnMediaPlayerListener listener) {
        this.mMediaPlayerListener = listener;
    }
}
