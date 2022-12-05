package com.nothingos.keyguard.weather;

import android.text.TextUtils;
import com.android.systemui.R$drawable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes2.dex */
public class WeatherData {
    @SerializedName("iconType")
    private int mIconType;
    @SerializedName("lastUpdateTime")
    private long mLastUpdateTime;
    @SerializedName("locationKey")
    private String mLocationKey;
    @SerializedName("locationType")
    private int mLocationType = 0;
    @SerializedName("phrase")
    private String mPhrase;
    @SerializedName("temp")
    private int mTemp;

    public static WeatherData fromJson(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (WeatherData) new Gson().fromJson(str, (Class<Object>) WeatherData.class);
    }

    public String getPhrase() {
        return this.mPhrase;
    }

    public int getTemp() {
        return this.mTemp;
    }

    public int getIconType() {
        return this.mIconType;
    }

    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public String getLocationKey() {
        return this.mLocationKey;
    }

    public int getWeatherIcon(int i) {
        switch (i) {
            case 3:
            case 4:
            case 6:
                return R$drawable.ic_mostly_cloudy_glance;
            case 5:
                return R$drawable.ic_hazy_sunshine_glance;
            case 7:
                return R$drawable.ic_cloudy_glance;
            case 8:
                return R$drawable.ic_overcast_glance;
            case 9:
            case 10:
            case 24:
            case 27:
            case 28:
            case 30:
            case 31:
            default:
                return R$drawable.ic_sunny_glance;
            case 11:
                return R$drawable.ic_fog_glance;
            case 12:
            case 13:
            case 14:
            case 18:
            case 39:
            case 40:
                return R$drawable.ic_rainny_glance;
            case 15:
            case 16:
            case 17:
            case 41:
            case 42:
                return R$drawable.ic_thunderstorm_glance;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 26:
            case 29:
            case 43:
            case 44:
                return R$drawable.ic_snow_glance;
            case 25:
                return R$drawable.ic_sleet_glance;
            case 32:
                return R$drawable.ic_windy_glance;
            case 33:
            case 34:
                return R$drawable.ic_clear_night_glance;
            case 35:
            case 36:
            case 38:
                return R$drawable.ic_cloudy_night_glance;
            case 37:
                return R$drawable.ic_hazy_night_glance;
        }
    }
}
