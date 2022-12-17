package com.nothing.settings.glyphs.utils;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;

public class GlyphsLedHelper {
    private AudioManager mAudioManager;
    private int mRingtoneType;
    private boolean mSoundOnly;

    public GlyphsLedHelper(Context context, int i) {
        init(context);
        this.mRingtoneType = i;
    }

    private void init(Context context) {
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void setSoundOnly(boolean z) {
        Log.d("GlyphsLedHelper", "setSoundOnly " + z);
        this.mSoundOnly = z;
    }

    public String getContactLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "nt_lights:light_type=ringtone;light_uri=content://media" + uri.getPath() + ";light_state=on";
    }

    public String getLedStopParams(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("nt_lights:light_type=");
        sb.append(i == 1 ? "ringtone" : "notification");
        sb.append(";");
        sb.append("light_uri=");
        sb.append(";");
        sb.append("light_state=off");
        return sb.toString();
    }

    public String getLedSoundOnlyParams(Uri uri, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("LED=OFF,TYPE=");
        sb.append(this.mRingtoneType == 1 ? "" : "NOTIFICATION");
        sb.append(",NT_LED_URI=content://media");
        sb.append(uri.getPath());
        sb.append("?title=");
        sb.append(str);
        return sb.toString();
    }

    public String getNotificationLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "nt_lights:light_type=notification;light_uri=content://media" + uri.getPath() + ";light_state=on";
    }

    public void stop() {
        if (this.mAudioManager != null) {
            Log.d("GlyphsLedHelper", "LED STOP");
            this.mAudioManager.setParameters(getLedStopParams(this.mRingtoneType));
        }
    }

    public void play(Uri uri, String str) {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            new Throwable("mAudioManager is null");
        } else if (this.mSoundOnly) {
            audioManager.setParameters(getLedSoundOnlyParams(uri, str));
        } else {
            audioManager.setParameters(getLedStopParams(this.mRingtoneType));
            this.mAudioManager.setParameters(this.mRingtoneType == 1 ? getContactLedStartParams(uri, str) : getNotificationLedStartParams(uri, str));
        }
    }
}
