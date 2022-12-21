package com.android.settingslib.datetime;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.icu.text.TimeZoneFormat;
import android.icu.text.TimeZoneNames;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import androidx.core.text.BidiFormatter;
import androidx.core.text.TextDirectionHeuristicsCompat;
import com.android.i18n.timezone.CountryTimeZones;
import com.android.i18n.timezone.TimeZoneFinder;
import com.android.settingslib.C1757R;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.xmlpull.p032v1.XmlPullParserException;

public class ZoneGetter {
    @Deprecated
    public static final String KEY_DISPLAYNAME = "name";
    public static final String KEY_DISPLAY_LABEL = "display_label";
    @Deprecated
    public static final String KEY_GMT = "gmt";
    public static final String KEY_ID = "id";
    public static final String KEY_OFFSET = "offset";
    public static final String KEY_OFFSET_LABEL = "offset_label";
    private static final String TAG = "ZoneGetter";
    private static final String XMLTAG_TIMEZONE = "timezone";

    public static CharSequence getTimeZoneOffsetAndName(Context context, TimeZone timeZone, Date date) {
        Locale locale = context.getResources().getConfiguration().locale;
        CharSequence gmtOffsetText = getGmtOffsetText(TimeZoneFormat.getInstance(locale), locale, timeZone, date);
        String zoneLongName = getZoneLongName(TimeZoneNames.getInstance(locale), timeZone, date);
        if (zoneLongName == null) {
            return gmtOffsetText;
        }
        return TextUtils.concat(new CharSequence[]{gmtOffsetText, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, zoneLongName});
    }

    public static List<Map<String, Object>> getZonesList(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        Date date = new Date();
        TimeZoneNames instance = TimeZoneNames.getInstance(locale);
        ZoneGetterData zoneGetterData = new ZoneGetterData(context);
        boolean shouldUseExemplarLocationForLocalNames = shouldUseExemplarLocationForLocalNames(zoneGetterData, instance);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < zoneGetterData.zoneCount; i++) {
            TimeZone timeZone = zoneGetterData.timeZones[i];
            CharSequence charSequence = zoneGetterData.gmtOffsetTexts[i];
            CharSequence timeZoneDisplayName = getTimeZoneDisplayName(zoneGetterData, instance, shouldUseExemplarLocationForLocalNames, timeZone, zoneGetterData.olsonIdsToDisplay[i]);
            if (TextUtils.isEmpty(timeZoneDisplayName)) {
                timeZoneDisplayName = charSequence;
            }
            arrayList.add(createDisplayEntry(timeZone, charSequence, timeZoneDisplayName, timeZone.getOffset(date.getTime())));
        }
        return arrayList;
    }

    private static Map<String, Object> createDisplayEntry(TimeZone timeZone, CharSequence charSequence, CharSequence charSequence2, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("id", timeZone.getID());
        hashMap.put(KEY_DISPLAYNAME, charSequence2.toString());
        hashMap.put(KEY_DISPLAY_LABEL, charSequence2);
        hashMap.put(KEY_GMT, charSequence.toString());
        hashMap.put(KEY_OFFSET_LABEL, charSequence);
        hashMap.put(KEY_OFFSET, Integer.valueOf(i));
        return hashMap;
    }

    /* access modifiers changed from: private */
    public static List<String> readTimezonesToDisplay(Context context) {
        XmlResourceParser xml;
        ArrayList arrayList = new ArrayList();
        try {
            xml = context.getResources().getXml(C1757R.C1761xml.timezones);
            while (xml.next() != 2) {
            }
            xml.next();
            while (xml.getEventType() != 3) {
                while (xml.getEventType() != 2) {
                    if (xml.getEventType() == 1) {
                        if (xml != null) {
                            xml.close();
                        }
                        return arrayList;
                    }
                    xml.next();
                }
                if (xml.getName().equals(XMLTAG_TIMEZONE)) {
                    arrayList.add(xml.getAttributeValue(0));
                }
                while (xml.getEventType() != 3) {
                    xml.next();
                }
                xml.next();
            }
            if (xml != null) {
                xml.close();
            }
        } catch (XmlPullParserException unused) {
            Log.e(TAG, "Ill-formatted timezones.xml file");
        } catch (IOException unused2) {
            Log.e(TAG, "Unable to read timezones.xml file");
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        return arrayList;
        throw th;
    }

    private static boolean shouldUseExemplarLocationForLocalNames(ZoneGetterData zoneGetterData, TimeZoneNames timeZoneNames) {
        HashSet hashSet = new HashSet();
        Date date = new Date();
        for (int i = 0; i < zoneGetterData.zoneCount; i++) {
            if (zoneGetterData.localZoneIds.contains(zoneGetterData.olsonIdsToDisplay[i])) {
                CharSequence zoneLongName = getZoneLongName(timeZoneNames, zoneGetterData.timeZones[i], date);
                if (zoneLongName == null) {
                    zoneLongName = zoneGetterData.gmtOffsetTexts[i];
                }
                if (!hashSet.add(zoneLongName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static CharSequence getTimeZoneDisplayName(ZoneGetterData zoneGetterData, TimeZoneNames timeZoneNames, boolean z, TimeZone timeZone, String str) {
        Date date = new Date();
        if (zoneGetterData.localZoneIds.contains(str) && !z) {
            return getZoneLongName(timeZoneNames, timeZone, date);
        }
        String canonicalID = android.icu.util.TimeZone.getCanonicalID(timeZone.getID());
        if (canonicalID == null) {
            canonicalID = timeZone.getID();
        }
        String exemplarLocationName = timeZoneNames.getExemplarLocationName(canonicalID);
        return (exemplarLocationName == null || exemplarLocationName.isEmpty()) ? getZoneLongName(timeZoneNames, timeZone, date) : exemplarLocationName;
    }

    private static String getZoneLongName(TimeZoneNames timeZoneNames, TimeZone timeZone, Date date) {
        TimeZoneNames.NameType nameType;
        if (timeZone.inDaylightTime(date)) {
            nameType = TimeZoneNames.NameType.LONG_DAYLIGHT;
        } else {
            nameType = TimeZoneNames.NameType.LONG_STANDARD;
        }
        return timeZoneNames.getDisplayName(getCanonicalZoneId(timeZone), nameType, date.getTime());
    }

    private static String getCanonicalZoneId(TimeZone timeZone) {
        String id = timeZone.getID();
        String canonicalID = android.icu.util.TimeZone.getCanonicalID(id);
        return canonicalID != null ? canonicalID : id;
    }

    private static void appendWithTtsSpan(SpannableStringBuilder spannableStringBuilder, CharSequence charSequence, TtsSpan ttsSpan) {
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(charSequence);
        spannableStringBuilder.setSpan(ttsSpan, length, spannableStringBuilder.length(), 0);
    }

    private static String formatDigits(int i, int i2, String str) {
        int i3 = i / 10;
        int i4 = i % 10;
        StringBuilder sb = new StringBuilder(i2);
        if (i >= 10 || i2 == 2) {
            sb.append(str.charAt(i3));
        }
        sb.append(str.charAt(i4));
        return sb.toString();
    }

    public static CharSequence getGmtOffsetText(TimeZoneFormat timeZoneFormat, Locale locale, TimeZone timeZone, Date date) {
        String str;
        String str2;
        TimeZoneFormat.GMTOffsetPatternType gMTOffsetPatternType;
        int i;
        int i2;
        String str3;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String gMTPattern = timeZoneFormat.getGMTPattern();
        int indexOf = gMTPattern.indexOf("{0}");
        boolean z = false;
        if (indexOf == -1) {
            str2 = "GMT";
            str = "";
        } else {
            String substring = gMTPattern.substring(0, indexOf);
            str = gMTPattern.substring(indexOf + 3);
            str2 = substring;
        }
        if (!str2.isEmpty()) {
            appendWithTtsSpan(spannableStringBuilder, str2, new TtsSpan.TextBuilder(str2).build());
        }
        int offset = timeZone.getOffset(date.getTime());
        if (offset < 0) {
            offset = -offset;
            gMTOffsetPatternType = TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HM;
        } else {
            gMTOffsetPatternType = TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HM;
        }
        String gMTOffsetPattern = timeZoneFormat.getGMTOffsetPattern(gMTOffsetPatternType);
        String gMTOffsetDigits = timeZoneFormat.getGMTOffsetDigits();
        long j = (long) offset;
        int i3 = (int) (j / 3600000);
        int abs = Math.abs((int) (j / 60000)) % 60;
        int i4 = 0;
        while (i4 < gMTOffsetPattern.length()) {
            char charAt = gMTOffsetPattern.charAt(i4);
            if (charAt == '+' || charAt == '-' || charAt == 8722) {
                String valueOf = String.valueOf(charAt);
                appendWithTtsSpan(spannableStringBuilder, valueOf, new TtsSpan.VerbatimBuilder(valueOf).build());
            } else if (charAt == 'H' || charAt == 'm') {
                int i5 = i4 + 1;
                if (i5 >= gMTOffsetPattern.length() || gMTOffsetPattern.charAt(i5) != charAt) {
                    i5 = i4;
                    i = 1;
                } else {
                    i = 2;
                }
                if (charAt == 'H') {
                    str3 = "hour";
                    i2 = i3;
                } else {
                    str3 = "minute";
                    i2 = abs;
                }
                appendWithTtsSpan(spannableStringBuilder, formatDigits(i2, i, gMTOffsetDigits), new TtsSpan.MeasureBuilder().setNumber((long) i2).setUnit(str3).build());
                i4 = i5;
            } else {
                spannableStringBuilder.append(charAt);
            }
            i4++;
        }
        if (!str.isEmpty()) {
            appendWithTtsSpan(spannableStringBuilder, str, new TtsSpan.TextBuilder(str).build());
        }
        SpannableString spannableString = new SpannableString(spannableStringBuilder);
        BidiFormatter instance = BidiFormatter.getInstance();
        if (TextUtils.getLayoutDirectionFromLocale(locale) == 1) {
            z = true;
        }
        return instance.unicodeWrap((CharSequence) spannableString, z ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR);
    }

    public static final class ZoneGetterData {
        public final CharSequence[] gmtOffsetTexts;
        public final Set<String> localZoneIds;
        public final String[] olsonIdsToDisplay;
        public final TimeZone[] timeZones;
        public final int zoneCount;

        public ZoneGetterData(Context context) {
            HashSet hashSet;
            Locale locale = context.getResources().getConfiguration().locale;
            TimeZoneFormat instance = TimeZoneFormat.getInstance(locale);
            Date date = new Date();
            List access$000 = ZoneGetter.readTimezonesToDisplay(context);
            int size = access$000.size();
            this.zoneCount = size;
            this.olsonIdsToDisplay = new String[size];
            this.timeZones = new TimeZone[size];
            this.gmtOffsetTexts = new CharSequence[size];
            for (int i = 0; i < this.zoneCount; i++) {
                String str = (String) access$000.get(i);
                this.olsonIdsToDisplay[i] = str;
                TimeZone timeZone = TimeZone.getTimeZone(str);
                this.timeZones[i] = timeZone;
                this.gmtOffsetTexts[i] = ZoneGetter.getGmtOffsetText(instance, locale, timeZone, date);
            }
            List<String> lookupTimeZoneIdsByCountry = lookupTimeZoneIdsByCountry(locale.getCountry());
            if (lookupTimeZoneIdsByCountry == null) {
                hashSet = new HashSet();
            }
            this.localZoneIds = hashSet;
        }

        public List<String> lookupTimeZoneIdsByCountry(String str) {
            CountryTimeZones lookupCountryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(str);
            if (lookupCountryTimeZones == null) {
                return null;
            }
            return extractTimeZoneIds(lookupCountryTimeZones.getTimeZoneMappings());
        }

        private static List<String> extractTimeZoneIds(List<CountryTimeZones.TimeZoneMapping> list) {
            ArrayList arrayList = new ArrayList(list.size());
            for (CountryTimeZones.TimeZoneMapping timeZoneId : list) {
                arrayList.add(timeZoneId.getTimeZoneId());
            }
            return Collections.unmodifiableList(arrayList);
        }
    }
}
