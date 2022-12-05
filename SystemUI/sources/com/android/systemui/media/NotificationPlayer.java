package com.android.systemui.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.lang.Thread;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class NotificationPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    @GuardedBy({"mQueueAudioFocusLock"})
    private AudioManager mAudioManagerWithAudioFocus;
    @GuardedBy({"mCompletionHandlingLock"})
    private CreationAndCompletionThread mCompletionThread;
    @GuardedBy({"mCompletionHandlingLock"})
    private Looper mLooper;
    @GuardedBy({"mPlayerLock"})
    private MediaPlayer mPlayer;
    private String mTag;
    @GuardedBy({"mCmdQueue"})
    private CmdThread mThread;
    @GuardedBy({"mCmdQueue"})
    private PowerManager.WakeLock mWakeLock;
    private final LinkedList<Command> mCmdQueue = new LinkedList<>();
    private final Object mCompletionHandlingLock = new Object();
    private final Object mPlayerLock = new Object();
    private final Object mQueueAudioFocusLock = new Object();
    private int mNotificationRampTimeMs = 0;
    private int mState = 2;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Command {
        AudioAttributes attributes;
        int code;
        Context context;
        boolean looping;
        long requestTime;
        Uri uri;

        private Command() {
        }

        public String toString() {
            return "{ code=" + this.code + " looping=" + this.looping + " attributes=" + this.attributes + " uri=" + this.uri + " }";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class CreationAndCompletionThread extends Thread {
        public Command mCmd;

        public CreationAndCompletionThread(Command command) {
            this.mCmd = command;
        }

        /* JADX WARN: Removed duplicated region for block: B:42:0x0103 A[Catch: all -> 0x0124, TRY_ENTER, TryCatch #3 {, blocks: (B:4:0x000d, B:7:0x001a, B:10:0x001f, B:12:0x0025, B:13:0x003a, B:15:0x0064, B:17:0x006a, B:19:0x0078, B:21:0x007e, B:22:0x0084, B:35:0x00b1, B:38:0x00b2, B:58:0x00be, B:39:0x00c9, B:40:0x00fc, B:41:0x0102, B:45:0x0111, B:48:0x0116, B:49:0x0119, B:52:0x011c, B:53:0x011f, B:61:0x00d3, B:62:0x00d8, B:42:0x0103, B:43:0x010e), top: B:3:0x000d }] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            MediaPlayer mediaPlayer;
            MediaPlayer mediaPlayer2;
            Looper.prepare();
            NotificationPlayer.this.mLooper = Looper.myLooper();
            synchronized (this) {
                AudioManager audioManager = (AudioManager) this.mCmd.context.getSystemService("audio");
                MediaPlayer mediaPlayer3 = null;
                try {
                    mediaPlayer = new MediaPlayer();
                } catch (Exception e) {
                    e = e;
                    mediaPlayer = null;
                }
                try {
                    Command command = this.mCmd;
                    if (command.attributes == null) {
                        command.attributes = new AudioAttributes.Builder().setUsage(5).setContentType(4).build();
                    }
                    mediaPlayer.setAudioAttributes(this.mCmd.attributes);
                    Command command2 = this.mCmd;
                    mediaPlayer.setDataSource(command2.context, command2.uri);
                    mediaPlayer.setLooping(this.mCmd.looping);
                    mediaPlayer.setOnCompletionListener(NotificationPlayer.this);
                    mediaPlayer.setOnErrorListener(NotificationPlayer.this);
                    mediaPlayer.prepare();
                    Uri uri = this.mCmd.uri;
                    if (uri != null && uri.getEncodedPath() != null && this.mCmd.uri.getEncodedPath().length() > 0 && !audioManager.isMusicActiveRemotely()) {
                        synchronized (NotificationPlayer.this.mQueueAudioFocusLock) {
                            if (NotificationPlayer.this.mAudioManagerWithAudioFocus == null) {
                                int i = 3;
                                Command command3 = this.mCmd;
                                if (command3.looping) {
                                    i = 1;
                                }
                                NotificationPlayer.this.mNotificationRampTimeMs = audioManager.getFocusRampTimeMs(i, command3.attributes);
                                audioManager.requestAudioFocus(null, this.mCmd.attributes, i, 0);
                                NotificationPlayer.this.mAudioManagerWithAudioFocus = audioManager;
                            }
                        }
                    }
                    try {
                        Thread.sleep(NotificationPlayer.this.mNotificationRampTimeMs);
                    } catch (InterruptedException e2) {
                        Log.e(NotificationPlayer.this.mTag, "Exception while sleeping to sync notification playback with ducking", e2);
                    }
                    mediaPlayer.start();
                } catch (Exception e3) {
                    e = e3;
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    } else {
                        mediaPlayer3 = mediaPlayer;
                    }
                    Log.w(NotificationPlayer.this.mTag, "error loading sound for " + this.mCmd.uri, e);
                    NotificationPlayer.this.abandonAudioFocusAfterError();
                    mediaPlayer = mediaPlayer3;
                    synchronized (NotificationPlayer.this.mPlayerLock) {
                    }
                }
                synchronized (NotificationPlayer.this.mPlayerLock) {
                    mediaPlayer2 = NotificationPlayer.this.mPlayer;
                    NotificationPlayer.this.mPlayer = mediaPlayer;
                }
                if (mediaPlayer2 != null) {
                    mediaPlayer2.pause();
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException unused) {
                    }
                    mediaPlayer2.release();
                }
                notify();
            }
            Looper.loop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abandonAudioFocusAfterError() {
        synchronized (this.mQueueAudioFocusLock) {
            AudioManager audioManager = this.mAudioManagerWithAudioFocus;
            if (audioManager != null) {
                audioManager.abandonAudioFocus(null);
                this.mAudioManagerWithAudioFocus = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSound(Command command) {
        try {
            synchronized (this.mCompletionHandlingLock) {
                Looper looper = this.mLooper;
                if (looper != null && looper.getThread().getState() != Thread.State.TERMINATED) {
                    this.mLooper.quit();
                }
                CreationAndCompletionThread creationAndCompletionThread = new CreationAndCompletionThread(command);
                this.mCompletionThread = creationAndCompletionThread;
                synchronized (creationAndCompletionThread) {
                    this.mCompletionThread.start();
                    this.mCompletionThread.wait();
                }
            }
            long uptimeMillis = SystemClock.uptimeMillis() - command.requestTime;
            if (uptimeMillis <= 1000) {
                return;
            }
            String str = this.mTag;
            Log.w(str, "Notification sound delayed by " + uptimeMillis + "msecs");
        } catch (Exception e) {
            String str2 = this.mTag;
            Log.w(str2, "error loading sound for " + command.uri, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class CmdThread extends Thread {
        CmdThread() {
            super("NotificationPlayer-" + NotificationPlayer.this.mTag);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Command command;
            MediaPlayer mediaPlayer;
            while (true) {
                synchronized (NotificationPlayer.this.mCmdQueue) {
                    command = (Command) NotificationPlayer.this.mCmdQueue.removeFirst();
                }
                int i = command.code;
                if (i == 1) {
                    NotificationPlayer.this.startSound(command);
                } else if (i == 2) {
                    synchronized (NotificationPlayer.this.mPlayerLock) {
                        mediaPlayer = NotificationPlayer.this.mPlayer;
                        NotificationPlayer.this.mPlayer = null;
                    }
                    if (mediaPlayer == null) {
                        Log.w(NotificationPlayer.this.mTag, "STOP command without a player");
                    } else {
                        long uptimeMillis = SystemClock.uptimeMillis() - command.requestTime;
                        if (uptimeMillis > 1000) {
                            String str = NotificationPlayer.this.mTag;
                            Log.w(str, "Notification stop delayed by " + uptimeMillis + "msecs");
                        }
                        try {
                            mediaPlayer.stop();
                        } catch (Exception unused) {
                        }
                        mediaPlayer.release();
                        synchronized (NotificationPlayer.this.mQueueAudioFocusLock) {
                            if (NotificationPlayer.this.mAudioManagerWithAudioFocus != null) {
                                NotificationPlayer.this.mAudioManagerWithAudioFocus.abandonAudioFocus(null);
                                NotificationPlayer.this.mAudioManagerWithAudioFocus = null;
                            }
                        }
                        synchronized (NotificationPlayer.this.mCompletionHandlingLock) {
                            if (NotificationPlayer.this.mLooper != null && NotificationPlayer.this.mLooper.getThread().getState() != Thread.State.TERMINATED) {
                                NotificationPlayer.this.mLooper.quit();
                            }
                        }
                    }
                }
                synchronized (NotificationPlayer.this.mCmdQueue) {
                    if (NotificationPlayer.this.mCmdQueue.size() == 0) {
                        NotificationPlayer.this.mThread = null;
                        NotificationPlayer.this.releaseWakeLock();
                        return;
                    }
                }
            }
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this.mQueueAudioFocusLock) {
            AudioManager audioManager = this.mAudioManagerWithAudioFocus;
            if (audioManager != null) {
                audioManager.abandonAudioFocus(null);
                this.mAudioManagerWithAudioFocus = null;
            }
        }
        synchronized (this.mCmdQueue) {
            synchronized (this.mCompletionHandlingLock) {
                if (this.mCmdQueue.size() == 0) {
                    Looper looper = this.mLooper;
                    if (looper != null) {
                        looper.quit();
                    }
                    this.mCompletionThread = null;
                }
            }
        }
        synchronized (this.mPlayerLock) {
            if (mediaPlayer == this.mPlayer) {
                this.mPlayer = null;
            }
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        String str = this.mTag;
        Log.e(str, "error " + i + " (extra=" + i2 + ") playing notification");
        onCompletion(mediaPlayer);
        return true;
    }

    public NotificationPlayer(String str) {
        if (str != null) {
            this.mTag = str;
        } else {
            this.mTag = "NotificationPlayer";
        }
    }

    public void play(Context context, Uri uri, boolean z, AudioAttributes audioAttributes) {
        Command command = new Command();
        command.requestTime = SystemClock.uptimeMillis();
        command.code = 1;
        command.context = context;
        command.uri = uri;
        command.looping = z;
        command.attributes = audioAttributes;
        synchronized (this.mCmdQueue) {
            enqueueLocked(command);
            this.mState = 1;
        }
    }

    public void stop() {
        synchronized (this.mCmdQueue) {
            if (this.mState != 2) {
                Command command = new Command();
                command.requestTime = SystemClock.uptimeMillis();
                command.code = 2;
                enqueueLocked(command);
                this.mState = 2;
            }
        }
    }

    @GuardedBy({"mCmdQueue"})
    private void enqueueLocked(Command command) {
        this.mCmdQueue.add(command);
        if (this.mThread == null) {
            acquireWakeLock();
            CmdThread cmdThread = new CmdThread();
            this.mThread = cmdThread;
            cmdThread.start();
        }
    }

    public void setUsesWakeLock(Context context) {
        synchronized (this.mCmdQueue) {
            if (this.mWakeLock != null || this.mThread != null) {
                throw new RuntimeException("assertion failed mWakeLock=" + this.mWakeLock + " mThread=" + this.mThread);
            }
            this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, this.mTag);
        }
    }

    @GuardedBy({"mCmdQueue"})
    private void acquireWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mCmdQueue"})
    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            wakeLock.release();
        }
    }
}
