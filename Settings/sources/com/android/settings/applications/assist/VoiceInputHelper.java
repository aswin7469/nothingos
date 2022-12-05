package com.android.settings.applications.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class VoiceInputHelper {
    final List<ResolveInfo> mAvailableRecognition;
    final ArrayList<RecognizerInfo> mAvailableRecognizerInfos = new ArrayList<>();
    final Context mContext;
    ComponentName mCurrentRecognizer;

    /* loaded from: classes.dex */
    public static class BaseInfo implements Comparable {
        public final CharSequence appLabel;
        public final ComponentName componentName;
        public final String key;
        public final CharSequence label;
        public final String labelStr;
        public final ServiceInfo service;
        public final ComponentName settings;

        public BaseInfo(PackageManager packageManager, ServiceInfo serviceInfo, String str) {
            this.service = serviceInfo;
            ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
            this.componentName = componentName;
            this.key = componentName.flattenToShortString();
            this.settings = str != null ? new ComponentName(serviceInfo.packageName, str) : null;
            CharSequence loadLabel = serviceInfo.loadLabel(packageManager);
            this.label = loadLabel;
            this.labelStr = loadLabel.toString();
            this.appLabel = serviceInfo.applicationInfo.loadLabel(packageManager);
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            return this.labelStr.compareTo(((BaseInfo) obj).labelStr);
        }
    }

    /* loaded from: classes.dex */
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

    /* JADX WARN: Removed duplicated region for block: B:29:0x00d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildUi() {
        String str;
        XmlResourceParser loadXmlMetaData;
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "voice_recognition_service");
        if (string != null && !string.isEmpty()) {
            this.mCurrentRecognizer = ComponentName.unflattenFromString(string);
        } else {
            this.mCurrentRecognizer = null;
        }
        int size = this.mAvailableRecognition.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = this.mAvailableRecognition.get(i);
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
            ServiceInfo serviceInfo2 = resolveInfo.serviceInfo;
            boolean z = true;
            try {
                loadXmlMetaData = serviceInfo2.loadXmlMetaData(this.mContext.getPackageManager(), "android.speech");
            } catch (PackageManager.NameNotFoundException e) {
                e = e;
                str = null;
            } catch (IOException e2) {
                e = e2;
                str = null;
            } catch (XmlPullParserException e3) {
                e = e3;
                str = null;
            }
            if (loadXmlMetaData == null) {
                throw new XmlPullParserException("No android.speech meta-data for " + serviceInfo2.packageName);
            }
            try {
                Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(serviceInfo2.applicationInfo);
                AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
                while (true) {
                    int next = loadXmlMetaData.next();
                    if (next == 1 || next == 2) {
                        break;
                    }
                }
                if (!"recognition-service".equals(loadXmlMetaData.getName())) {
                    throw new XmlPullParserException("Meta-data does not start with recognition-service tag");
                }
                TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(asAttributeSet, R.styleable.RecognitionService);
                str = obtainAttributes.getString(0);
                try {
                    z = obtainAttributes.getBoolean(1, true);
                    obtainAttributes.recycle();
                    try {
                        loadXmlMetaData.close();
                    } catch (PackageManager.NameNotFoundException e4) {
                        e = e4;
                        Log.e("VoiceInputHelper", "error parsing recognition service meta-data", e);
                        if (!z) {
                        }
                        this.mAvailableRecognizerInfos.add(new RecognizerInfo(this.mContext.getPackageManager(), resolveInfo.serviceInfo, str, z));
                    } catch (IOException e5) {
                        e = e5;
                        Log.e("VoiceInputHelper", "error parsing recognition service meta-data", e);
                        if (!z) {
                        }
                        this.mAvailableRecognizerInfos.add(new RecognizerInfo(this.mContext.getPackageManager(), resolveInfo.serviceInfo, str, z));
                    } catch (XmlPullParserException e6) {
                        e = e6;
                        Log.e("VoiceInputHelper", "error parsing recognition service meta-data", e);
                        if (!z) {
                        }
                        this.mAvailableRecognizerInfos.add(new RecognizerInfo(this.mContext.getPackageManager(), resolveInfo.serviceInfo, str, z));
                    }
                    if (!z || componentName.equals(this.mCurrentRecognizer)) {
                        this.mAvailableRecognizerInfos.add(new RecognizerInfo(this.mContext.getPackageManager(), resolveInfo.serviceInfo, str, z));
                    }
                } catch (Throwable th) {
                    th = th;
                    if (loadXmlMetaData != null) {
                        try {
                            loadXmlMetaData.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                    break;
                }
            } catch (Throwable th3) {
                th = th3;
                str = null;
            }
        }
        Collections.sort(this.mAvailableRecognizerInfos);
    }
}
