package com.nothing.systemui.keyguard.weather;

import android.text.TextUtils;
import com.android.systemui.C1894R;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("highTemp")
    private int mHighTemp;
    @SerializedName("iconType")
    private int mIconType;
    @SerializedName("lastUpdateTime")
    private long mLastUpdateTime;
    @SerializedName("locationKey")
    private String mLocationKey;
    @SerializedName("locationType")
    private int mLocationType = 0;
    @SerializedName("lowTemp")
    private int mLowTemp;
    @SerializedName("phrase")
    private String mPhrase;
    @SerializedName("temp")
    private int mTemp;

    public int getWeatherIcon(int i) {
        switch (i) {
            case 3:
            case 4:
            case 6:
                return C1894R.C1896drawable.ic_mostly_cloudy_glance;
            case 5:
                return C1894R.C1896drawable.ic_hazy_sunshine_glance;
            case 7:
                return C1894R.C1896drawable.ic_cloudy_glance;
            case 8:
                return C1894R.C1896drawable.ic_overcast_glance;
            case 11:
                return C1894R.C1896drawable.ic_fog_glance;
            case 12:
            case 13:
            case 14:
            case 18:
            case 39:
            case 40:
                return C1894R.C1896drawable.ic_rainny_glance;
            case 15:
            case 16:
            case 17:
            case 41:
            case 42:
                return C1894R.C1896drawable.ic_thunderstorm_glance;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 26:
            case 29:
            case 43:
            case 44:
                return C1894R.C1896drawable.ic_snow_glance;
            case 25:
                return C1894R.C1896drawable.ic_sleet_glance;
            case 32:
                return C1894R.C1896drawable.ic_windy_glance;
            case 33:
            case 34:
                return C1894R.C1896drawable.ic_clear_night_glance;
            case 35:
            case 36:
            case 38:
                return C1894R.C1896drawable.ic_cloudy_night_glance;
            case 37:
                return C1894R.C1896drawable.ic_hazy_night_glance;
            default:
                return C1894R.C1896drawable.ic_sunny_glance;
        }
    }

    public static WeatherData fromJson(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (WeatherData) new Gson().fromJson(str, WeatherData.class);
    }

    public String toJson() {
        return new Gson().toJson((Object) this);
    }

    public String getPhrase() {
        return this.mPhrase;
    }

    public void setPhrase(String str) {
        this.mPhrase = str;
    }

    public int getLowTemp() {
        return this.mLowTemp;
    }

    public void setLowTemp(int i) {
        this.mLowTemp = i;
    }

    public int getHighTemp() {
        return this.mHighTemp;
    }

    public void setHighTemp(int i) {
        this.mHighTemp = i;
    }

    public int getTemp() {
        return this.mTemp;
    }

    public void setTemp(int i) {
        this.mTemp = i;
    }

    public int getIconType() {
        return this.mIconType;
    }

    public void setIconType(int i) {
        this.mIconType = i;
    }

    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public void setLastUpdateTime(long j) {
        this.mLastUpdateTime = j;
    }

    public String getLocationKey() {
        return this.mLocationKey;
    }

    public void setLocationKey(String str) {
        this.mLocationKey = str;
    }

    public int getLocationType() {
        return this.mLocationType;
    }

    public void setLocationType(int i) {
        this.mLocationType = i;
    }
}
