package com.nt.settings.glyphs.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;
import com.nt.settings.glyphs.widget.bean.GlyphsLedAnimPoint;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/* loaded from: classes2.dex */
public class GlyphsLedDataProvider {
    public static LruCache sCaches = new LruCache(50);

    public List<GlyphsLedAnimPoint> getLedAnim(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<GlyphsLedAnimPoint> list = (List) sCaches.get(str);
        return list == null ? readLedAnim(context, str) : list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List<GlyphsLedAnimPoint> readLedAnim(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(context.getApplicationContext().getAssets().open(str)));
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine == null) {
                                break;
                            }
                            GlyphsLedAnimPoint glyphsLedAnimPoint = new GlyphsLedAnimPoint();
                            StringTokenizer stringTokenizer = new StringTokenizer(readLine, ",");
                            int i = 0;
                            while (stringTokenizer.hasMoreTokens()) {
                                double doubleValue = Double.valueOf(stringTokenizer.nextToken()).doubleValue();
                                if (doubleValue == 0.0d) {
                                    glyphsLedAnimPoint.setLedValue(i, 0);
                                } else {
                                    glyphsLedAnimPoint.setLedValue(i, (int) (((((doubleValue * 40.0d) / 4096.0d) + 60.0d) * 255.0d) / 100.0d));
                                }
                                i++;
                            }
                            arrayList.add(glyphsLedAnimPoint);
                        } catch (FileNotFoundException e) {
                            e = e;
                            bufferedReader = bufferedReader2;
                            e.printStackTrace();
                            if (bufferedReader == null) {
                                return arrayList;
                            }
                            bufferedReader.close();
                            bufferedReader = bufferedReader;
                            return arrayList;
                        } catch (IOException e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                            e.printStackTrace();
                            if (bufferedReader == null) {
                                return arrayList;
                            }
                            bufferedReader.close();
                            bufferedReader = bufferedReader;
                            return arrayList;
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            if (bufferedReader == null) {
                                return arrayList;
                            }
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                            throw th;
                        }
                    }
                    LruCache lruCache = sCaches;
                    lruCache.put(str, arrayList);
                    bufferedReader2.close();
                    bufferedReader = lruCache;
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (FileNotFoundException e5) {
                e = e5;
            } catch (IOException e6) {
                e = e6;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
