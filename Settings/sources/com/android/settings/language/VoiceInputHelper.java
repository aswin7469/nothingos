package com.android.settings.language;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import java.util.ArrayList;
import java.util.List;

public final class VoiceInputHelper {
    final List<ResolveInfo> mAvailableRecognition;
    final ArrayList<RecognizerInfo> mAvailableRecognizerInfos = new ArrayList<>();
    final Context mContext;
    ComponentName mCurrentRecognizer;

    public static class BaseInfo implements Comparable<BaseInfo> {
        public final CharSequence mAppLabel;
        public final ComponentName mComponentName;
        public final String mKey;
        public final CharSequence mLabel;
        public final String mLabelStr;
        public final ServiceInfo mService;
        public final ComponentName mSettings;

        public BaseInfo(PackageManager packageManager, ServiceInfo serviceInfo, String str) {
            this.mService = serviceInfo;
            ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
            this.mComponentName = componentName;
            this.mKey = componentName.flattenToShortString();
            this.mSettings = str != null ? new ComponentName(serviceInfo.packageName, str) : null;
            CharSequence loadLabel = serviceInfo.loadLabel(packageManager);
            this.mLabel = loadLabel;
            this.mLabelStr = loadLabel.toString();
            this.mAppLabel = serviceInfo.applicationInfo.loadLabel(packageManager);
        }

        public int compareTo(BaseInfo baseInfo) {
            return this.mLabelStr.compareTo(baseInfo.mLabelStr);
        }
    }

    public static class RecognizerInfo extends BaseInfo {
        public final boolean mSelectableAsDefault;

        public RecognizerInfo(PackageManager packageManager, ServiceInfo serviceInfo, String str, boolean z) {
            super(packageManager, serviceInfo, str);
            this.mSelectableAsDefault = z;
        }
    }

    public VoiceInputHelper(Context context) {
        this.mContext = context;
        this.mAvailableRecognition = context.getPackageManager().queryIntentServices(new Intent("android.speech.RecognitionService"), 128);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b5 A[SYNTHETIC, Splitter:B:35:0x00b5] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00bd A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void buildUi() {
        /*
            r14 = this;
            java.lang.String r0 = "error parsing recognition service meta-data"
            java.lang.String r1 = "VoiceInputHelper"
            android.content.Context r2 = r14.mContext
            android.content.ContentResolver r2 = r2.getContentResolver()
            java.lang.String r3 = "voice_recognition_service"
            java.lang.String r2 = android.provider.Settings.Secure.getString(r2, r3)
            r3 = 0
            if (r2 == 0) goto L_0x0021
            boolean r4 = r2.isEmpty()
            if (r4 != 0) goto L_0x0021
            android.content.ComponentName r2 = android.content.ComponentName.unflattenFromString(r2)
            r14.mCurrentRecognizer = r2
            goto L_0x0023
        L_0x0021:
            r14.mCurrentRecognizer = r3
        L_0x0023:
            java.util.List<android.content.pm.ResolveInfo> r2 = r14.mAvailableRecognition
            int r2 = r2.size()
            r4 = 0
            r5 = r4
        L_0x002b:
            if (r5 >= r2) goto L_0x00f5
            java.util.List<android.content.pm.ResolveInfo> r6 = r14.mAvailableRecognition
            java.lang.Object r6 = r6.get(r5)
            android.content.pm.ResolveInfo r6 = (android.content.pm.ResolveInfo) r6
            android.content.ComponentName r7 = new android.content.ComponentName
            android.content.pm.ServiceInfo r8 = r6.serviceInfo
            java.lang.String r9 = r8.packageName
            java.lang.String r8 = r8.name
            r7.<init>(r9, r8)
            android.content.pm.ServiceInfo r8 = r6.serviceInfo
            r9 = 1
            android.content.Context r10 = r14.mContext     // Catch:{ XmlPullParserException -> 0x00d0, IOException -> 0x00ca, NameNotFoundException -> 0x00c4 }
            android.content.pm.PackageManager r10 = r10.getPackageManager()     // Catch:{ XmlPullParserException -> 0x00d0, IOException -> 0x00ca, NameNotFoundException -> 0x00c4 }
            java.lang.String r11 = "android.speech"
            android.content.res.XmlResourceParser r10 = r8.loadXmlMetaData(r10, r11)     // Catch:{ XmlPullParserException -> 0x00d0, IOException -> 0x00ca, NameNotFoundException -> 0x00c4 }
            if (r10 == 0) goto L_0x009a
            android.content.Context r11 = r14.mContext     // Catch:{ all -> 0x0097 }
            android.content.pm.PackageManager r11 = r11.getPackageManager()     // Catch:{ all -> 0x0097 }
            android.content.pm.ApplicationInfo r8 = r8.applicationInfo     // Catch:{ all -> 0x0097 }
            android.content.res.Resources r8 = r11.getResourcesForApplication(r8)     // Catch:{ all -> 0x0097 }
            android.util.AttributeSet r11 = android.util.Xml.asAttributeSet(r10)     // Catch:{ all -> 0x0097 }
        L_0x0061:
            int r12 = r10.next()     // Catch:{ all -> 0x0097 }
            if (r12 == r9) goto L_0x006b
            r13 = 2
            if (r12 == r13) goto L_0x006b
            goto L_0x0061
        L_0x006b:
            java.lang.String r12 = r10.getName()     // Catch:{ all -> 0x0097 }
            java.lang.String r13 = "recognition-service"
            boolean r12 = r13.equals(r12)     // Catch:{ all -> 0x0097 }
            if (r12 == 0) goto L_0x008f
            int[] r12 = com.android.internal.R.styleable.RecognitionService     // Catch:{ all -> 0x0097 }
            android.content.res.TypedArray r8 = r8.obtainAttributes(r11, r12)     // Catch:{ all -> 0x0097 }
            java.lang.String r11 = r8.getString(r4)     // Catch:{ all -> 0x0097 }
            boolean r9 = r8.getBoolean(r9, r9)     // Catch:{ all -> 0x008d }
            r8.recycle()     // Catch:{ all -> 0x008d }
            r10.close()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00c0, NameNotFoundException -> 0x00be }
            goto L_0x00d5
        L_0x008d:
            r8 = move-exception
            goto L_0x00b3
        L_0x008f:
            org.xmlpull.v1.XmlPullParserException r8 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ all -> 0x0097 }
            java.lang.String r11 = "Meta-data does not start with recognition-service tag"
            r8.<init>(r11)     // Catch:{ all -> 0x0097 }
            throw r8     // Catch:{ all -> 0x0097 }
        L_0x0097:
            r8 = move-exception
            r11 = r3
            goto L_0x00b3
        L_0x009a:
            org.xmlpull.v1.XmlPullParserException r11 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ all -> 0x0097 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0097 }
            r12.<init>()     // Catch:{ all -> 0x0097 }
            java.lang.String r13 = "No android.speech meta-data for "
            r12.append(r13)     // Catch:{ all -> 0x0097 }
            java.lang.String r8 = r8.packageName     // Catch:{ all -> 0x0097 }
            r12.append(r8)     // Catch:{ all -> 0x0097 }
            java.lang.String r8 = r12.toString()     // Catch:{ all -> 0x0097 }
            r11.<init>(r8)     // Catch:{ all -> 0x0097 }
            throw r11     // Catch:{ all -> 0x0097 }
        L_0x00b3:
            if (r10 == 0) goto L_0x00bd
            r10.close()     // Catch:{ all -> 0x00b9 }
            goto L_0x00bd
        L_0x00b9:
            r10 = move-exception
            r8.addSuppressed(r10)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00c0, NameNotFoundException -> 0x00be }
        L_0x00bd:
            throw r8     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00c0, NameNotFoundException -> 0x00be }
        L_0x00be:
            r8 = move-exception
            goto L_0x00c6
        L_0x00c0:
            r8 = move-exception
            goto L_0x00cc
        L_0x00c2:
            r8 = move-exception
            goto L_0x00d2
        L_0x00c4:
            r8 = move-exception
            r11 = r3
        L_0x00c6:
            android.util.Log.e(r1, r0, r8)
            goto L_0x00d5
        L_0x00ca:
            r8 = move-exception
            r11 = r3
        L_0x00cc:
            android.util.Log.e(r1, r0, r8)
            goto L_0x00d5
        L_0x00d0:
            r8 = move-exception
            r11 = r3
        L_0x00d2:
            android.util.Log.e(r1, r0, r8)
        L_0x00d5:
            if (r9 != 0) goto L_0x00df
            android.content.ComponentName r8 = r14.mCurrentRecognizer
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x00f1
        L_0x00df:
            java.util.ArrayList<com.android.settings.language.VoiceInputHelper$RecognizerInfo> r7 = r14.mAvailableRecognizerInfos
            com.android.settings.language.VoiceInputHelper$RecognizerInfo r8 = new com.android.settings.language.VoiceInputHelper$RecognizerInfo
            android.content.Context r10 = r14.mContext
            android.content.pm.PackageManager r10 = r10.getPackageManager()
            android.content.pm.ServiceInfo r6 = r6.serviceInfo
            r8.<init>(r10, r6, r11, r9)
            r7.add(r8)
        L_0x00f1:
            int r5 = r5 + 1
            goto L_0x002b
        L_0x00f5:
            java.util.ArrayList<com.android.settings.language.VoiceInputHelper$RecognizerInfo> r14 = r14.mAvailableRecognizerInfos
            java.util.Collections.sort(r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.language.VoiceInputHelper.buildUi():void");
    }
}
