package android.media;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public class NtRingtoneSyncWithVibrateUtils {
    private static final String ALARM_DEFAULT_PREFIX = "Default (";
    private static final String DIR_VIBRATE_FILE = "/system/etc/richtapresources/";
    private static final int MAX_DELAY_MS = 2000;
    private static final String SUBFIX_VIBRATE_FILE = ".he";
    private static final String TAG = "NtRingtoneSyncWithVibrateUtils";
    private static final int TYPE_ALARM = 2;
    private static final int TYPE_NOTIFICATION = 1;
    private static final int TYPE_RINGTONE = 0;
    private static NtRingtoneSyncWithVibrateUtils sInstance;
    private static List<String> sNeedNotPlayVibateRingtone;
    private static List<String> sNeedPlayVibateRingtone;
    private AACHapticUtils mAACHapticUtils;
    private AudioManager mAudioManager;
    private Context mContext;
    private Object mLock = new Object();
    private boolean mSupportRichTap;

    static {
        ArrayList arrayList = new ArrayList();
        sNeedNotPlayVibateRingtone = arrayList;
        arrayList.add("pneumatic");
        sNeedNotPlayVibateRingtone.add("abra");
        sNeedNotPlayVibateRingtone.add("plot");
        sNeedNotPlayVibateRingtone.add("beetle");
        sNeedNotPlayVibateRingtone.add("squirrels");
        sNeedNotPlayVibateRingtone.add("snaps");
        sNeedNotPlayVibateRingtone.add("radiate");
        sNeedNotPlayVibateRingtone.add("tennis");
        sNeedNotPlayVibateRingtone.add("coded");
        sNeedNotPlayVibateRingtone.add("scribble");
        sNeedNotPlayVibateRingtone.add("bedside");
        sNeedNotPlayVibateRingtone.add("frogs");
        sNeedNotPlayVibateRingtone.add("ramble");
        sNeedNotPlayVibateRingtone.add("coil");
        sNeedNotPlayVibateRingtone.add("kashio");
        sNeedNotPlayVibateRingtone.add("transmission");
        sNeedNotPlayVibateRingtone.add("nothing");
        sNeedNotPlayVibateRingtone.add("incoming");
        sNeedNotPlayVibateRingtone.add("munge");
        sNeedNotPlayVibateRingtone.add("prong");
        ArrayList arrayList2 = new ArrayList();
        sNeedPlayVibateRingtone = arrayList2;
        arrayList2.add("oi!");
        sNeedPlayVibateRingtone.add("bulb_one");
        sNeedPlayVibateRingtone.add("bulb_two");
        sNeedPlayVibateRingtone.add("guiro");
        sNeedPlayVibateRingtone.add("volley");
        sNeedPlayVibateRingtone.add("squiggle");
        sNeedPlayVibateRingtone.add("isolator");
        sNeedPlayVibateRingtone.add("gamma");
        sNeedPlayVibateRingtone.add("beak");
        sNeedPlayVibateRingtone.add("nope");
    }

    private static String getVibrateFilePath(String fileName) {
        return DIR_VIBRATE_FILE + fileName + ".he";
    }

    public static NtRingtoneSyncWithVibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NtRingtoneSyncWithVibrateUtils(context);
        }
        return sInstance;
    }

    public NtRingtoneSyncWithVibrateUtils(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void stopPlayRingtoneVibrate() {
        synchronized (this.mLock) {
            if (this.mAACHapticUtils != null) {
                Log.d(TAG, "[GJ_VIBRATE] playRingtoneVibrate: =========== STOP");
                this.mAACHapticUtils.stop();
                this.mAACHapticUtils.quit();
                this.mAACHapticUtils = null;
            }
        }
    }

    public void playNotificationRingtoneVibrate(MediaPlayer mediaPlayer, Ringtone ringtone, Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append("playNotificationRingtoneVibrate: uri = ");
        sb.append(uri != null ? uri.toString() : "");
        Log.d(TAG, sb.toString());
        playRingtoneVibrate(mediaPlayer, ringtone, uri, 0);
    }

    public void playRingtoneVibrate(MediaPlayer mediaPlayer, Ringtone ringtone, Uri uri, int duration) {
        String pkgName = getAppName(this.mContext, Process.myPid());
        int type = 0;
        if ("com.google.android.deskclock".equals(pkgName)) {
            return;
        }
        if (ringtone == null) {
            type = 1;
        }
        if (ringtone != null) {
            if (ringtone.getStreamType() != 5) {
                if (ringtone.getStreamType() == 2) {
                    type = 0;
                }
            } else {
                type = 1;
            }
        }
        if ("com.android.systemui".equals(pkgName)) {
            type = 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("playRingtoneVibrate: uri = ");
        sb.append(uri != null ? uri.toString() : "");
        sb.append(", pkgName = ");
        sb.append(pkgName);
        sb.append(", type = ");
        sb.append(type);
        Log.d(TAG, sb.toString());
        if (!checkVibEnable(pkgName)) {
            return;
        }
        checkAACHapticEngine();
        if (!this.mSupportRichTap) {
            Log.d(TAG, "playRingtoneVibrate: can not support RichTap !!!");
            return;
        }
        boolean z = false;
        if (type == 0 && Settings.System.getInt(this.mContext.getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0) == 0) {
            Log.d(TAG, "playRingtoneVibrate: ringing vibrate setting is off,  just return!");
        } else if (type == 1 && Settings.System.getInt(this.mContext.getContentResolver(), Settings.System.NOTIFICATION_VIBRATION_INTENSITY, 0) == 0) {
            Log.d(TAG, "playRingtoneVibrate: notification vibrate setting is off,  just return!");
        } else {
            if (uri != null && uri.toString().equals(Settings.System.DEFAULT_RINGTONE_URI.toString())) {
                uri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1);
            }
            if (uri != null && uri.toString().equals(Settings.System.DEFAULT_ALARM_ALERT_URI.toString())) {
                uri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 4);
            }
            if (uri != null && uri.toString().equals(Settings.System.DEFAULT_NOTIFICATION_URI.toString())) {
                uri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2);
            }
            if (type == 1) {
                z = true;
            }
            boolean isNotification = z;
            String vibrateFileContent = getVibrateFileContent(ringtone, uri, isNotification, pkgName);
            if (TextUtils.isEmpty(vibrateFileContent)) {
                return;
            }
            int loopCount = SystemProperties.getInt("sys.haptic.loop_count", Integer.MAX_VALUE);
            if (!"com.android.settings".equals(pkgName) && duration > 0 && (loopCount = MediaPlayer.ProvisioningThread.TIMEOUT_MS / duration) < 1) {
                loopCount = 1;
            }
            if (isNotification) {
                loopCount = 1;
            }
            synchronized (this.mLock) {
                if (this.mAACHapticUtils != null) {
                    Log.d(TAG, "[GJ_VIBRATE] playRingtoneVibrate: ==== (loopCount:" + loopCount + ") ======= START");
                    this.mAACHapticUtils.playPattern(vibrateFileContent, loopCount, 255);
                }
            }
        }
    }

    private String getVibrateFileContent(Ringtone ringtone, Uri uri, boolean isNotification, String pkgName) {
        String ringtoneName;
        StringBuilder sb = new StringBuilder();
        sb.append("getVibrateFileContent: uri = ");
        sb.append(uri != null ? uri.toString() : "");
        Log.d(TAG, sb.toString());
        if (isInternalRingtone(uri)) {
            if (ringtone == null) {
                ringtoneName = Ringtone.getTitle(this.mContext, uri, true, false);
            } else {
                ringtoneName = ringtone.getTitle(this.mContext);
            }
            Log.d(TAG, "getVibrateFileContent: ringtoneName = " + ringtoneName);
            if (ringtoneName.startsWith(ALARM_DEFAULT_PREFIX) && ringtoneName.endsWith(")")) {
                ringtoneName = ringtoneName.substring(ALARM_DEFAULT_PREFIX.length(), ringtoneName.length() - 1);
                Log.d(TAG, "getVibrateFileContent: (ALARM) format ringtoneName = " + ringtoneName);
            }
            if (ringtoneName.contains(" ")) {
                ringtoneName = ringtoneName.replace(" ", Session.SESSION_SEPARATION_CHAR_CHILD);
                Log.d(TAG, "getVibrateFileContent: (REPLCE BLANK) ringtoneName = " + ringtoneName);
            }
            if (sNeedNotPlayVibateRingtone.contains(ringtoneName)) {
                Log.d(TAG, "getVibrateFileContent: haptic ringtone, do not play acc vibration !!!");
                return "";
            } else if (sNeedPlayVibateRingtone.contains(ringtoneName)) {
                return getFileContent(getVibrateFilePath(ringtoneName));
            }
        }
        if ("com.android.settings".equals(pkgName)) {
            Log.w(TAG, "getVibrateFileContent: don't do AAC vibration for custom ringtone/notification in Settings App");
            return "";
        } else if (isNotification) {
            return getFileContent(getVibrateFilePath("common_notification1"));
        } else {
            return getFileContent(getVibrateFilePath("common_ringtone1"));
        }
    }

    private String getFileContent(String filePath) {
        Log.d(TAG, "[GJ_VIBRATE] getFileContent: filePath = " + filePath);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            try {
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        builder.append(line);
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (reader != null) {
                        reader.close();
                    }
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        String content = builder.toString();
        if (TextUtils.isEmpty(content)) {
            Log.e(TAG, "can not get file content, file:" + filePath);
        }
        return content;
    }

    private void checkAACHapticEngine() {
        synchronized (this.mLock) {
            if (this.mAACHapticUtils == null) {
                AACHapticUtils aACHapticUtils = AACHapticUtils.getInstance();
                this.mAACHapticUtils = aACHapticUtils;
                aACHapticUtils.init(this.mContext);
                this.mSupportRichTap = this.mAACHapticUtils.isSupportedRichTap();
            }
        }
    }

    public static boolean isInternalRingtone(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
    }

    public static boolean isExternalRingtoneUri(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    private static boolean isRingtoneUriInStorage(Uri ringtone, Uri storage) {
        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(ringtone);
        if (uriWithoutUserId == null) {
            return false;
        }
        return uriWithoutUserId.toString().startsWith(storage.toString());
    }

    public boolean checkVibEnable(String pkgName) {
        if ("system".equals(pkgName)) {
            Log.d(TAG, "checkVibEnable: AAC SDK not support to call in system process");
            return false;
        }
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
        long enableTimeMs = audioManager.getEnableDoVibrateTime(pkgName);
        long curTimeMs = System.currentTimeMillis();
        if (curTimeMs - enableTimeMs < 2000) {
            return true;
        }
        Log.d(TAG, "checkVibEnable: enableTimeMs = " + enableTimeMs + ", curTimeMs = " + curTimeMs + ", ret = false");
        return false;
    }

    public static String getAppName(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        long identity = Binder.clearCallingIdentity();
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        Binder.restoreCallingIdentity(identity);
        String name = "unknow";
        try {
            Iterator<ActivityManager.RunningAppProcessInfo> it = infos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ActivityManager.RunningAppProcessInfo info = it.next();
                if (info.pid == pid) {
                    name = info.processName;
                    break;
                }
            }
            name = name.split(SettingsStringUtil.DELIMITER)[0];
        } catch (Exception e) {
            Log.e(TAG, "getAppName: " + e.getMessage());
        }
        Log.d(TAG, "getAppName: pid = " + pid + ", name = " + name);
        return name;
    }
}
