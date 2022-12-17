package com.nothing.settings.glyphs.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class ResultPickHelper {
    public static void startMySoundSelector(Activity activity, String str, Uri uri, int i, int i2, int i3) {
        Log.d("GlyphsPickResultHelper", "startMySoundSelector " + uri + " soundOnly " + i2);
        Intent intent = new Intent("com.nothing.settings.ACTION_MY_SOUND");
        intent.putExtra("page_title", str);
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("android.intent.extra.ringtone.TYPE", i);
        intent.putExtra("key_sound_only", i2);
        activity.startActivityForResult(intent, i3);
    }

    public static void startNtSoundSelector(Activity activity, String str, String str2, Uri uri, int i, int i2, int i3) {
        Log.d("GlyphsPickResultHelper", "startNtSoundSelector " + uri + " soundOnly " + i2);
        Intent intent = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
        intent.putExtra("contact_name", str2);
        intent.putExtra("page_title", str);
        intent.putExtra("android.intent.extra.ringtone.TYPE", i);
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("key_sound_only", i2);
        activity.startActivityForResult(intent, i3);
    }

    public static void startRingtoneSoundSelector(Activity activity, String str, String str2, Uri uri, int i, int i2, int i3) {
        Log.d("GlyphsPickResultHelper", "startRingtoneSoundSelector " + uri + " soundOnly " + i2);
        Intent intent = new Intent("com.nothing.settings.ACTION_RINGTONES_SELECTOR");
        intent.putExtra("contact_name", str2);
        intent.putExtra("page_title", str);
        intent.putExtra("android.intent.extra.ringtone.TYPE", i);
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("key_sound_only", i2);
        activity.startActivityForResult(intent, i3);
    }

    public static void setResult(Activity activity, Uri uri, int i) {
        if (uri != null) {
            uri = Uri.parse(UrlUtil.addUrlParams(uri.toString(), "soundOnly", String.valueOf(i)));
        }
        Log.d("GlyphsPickResultHelper", "uri " + uri + " soundOnly " + i);
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("android.intent.extra.ringtone.PICKED_URI", uri);
        intent.putExtra("key_sound_only", i);
        intent.setData(uri);
        activity.setResult(-1, intent);
        activity.finish();
    }
}
