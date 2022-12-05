package com.android.settings.slices;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.accessibility.AccessibilitySlicePreferenceController;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceXmlParserUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceData;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
class SliceDataConverter {
    private Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public SliceDataConverter(Context context) {
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public List<SliceData> getSliceData() {
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData searchIndexableData : FeatureFactory.getFactory(this.mContext).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues()) {
            String name = searchIndexableData.getTargetClass().getName();
            Indexable$SearchIndexProvider searchIndexProvider = searchIndexableData.getSearchIndexProvider();
            if (searchIndexProvider == null) {
                Log.e("SliceDataConverter", name + " dose not implement Search Index Provider");
            } else {
                arrayList.addAll(getSliceDataFromProvider(searchIndexProvider, name));
            }
        }
        arrayList.addAll(getAccessibilitySliceData());
        return arrayList;
    }

    private List<SliceData> getSliceDataFromProvider(Indexable$SearchIndexProvider indexable$SearchIndexProvider, String str) {
        ArrayList arrayList = new ArrayList();
        List<SearchIndexableResource> xmlResourcesToIndex = indexable$SearchIndexProvider.getXmlResourcesToIndex(this.mContext, true);
        if (xmlResourcesToIndex == null) {
            return arrayList;
        }
        for (SearchIndexableResource searchIndexableResource : xmlResourcesToIndex) {
            int i = searchIndexableResource.xmlResId;
            if (i == 0) {
                Log.e("SliceDataConverter", str + " provides invalid XML (0) in search provider.");
            } else {
                arrayList.addAll(getSliceDataFromXML(i, str));
            }
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0126, code lost:
        if (0 == 0) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0165, code lost:
        if (0 == 0) goto L29;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x016d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<SliceData> getSliceDataFromXML(int i, String str) {
        String name;
        Iterator<Bundle> it;
        ArrayList arrayList = new ArrayList();
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                try {
                    xmlResourceParser = this.mContext.getResources().getXml(i);
                    while (true) {
                        int next = xmlResourceParser.next();
                        if (next == 1 || next == 2) {
                            break;
                        }
                    }
                    name = xmlResourceParser.getName();
                } catch (Throwable th) {
                    th = th;
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    throw th;
                }
            } catch (Exception e) {
                Log.w("SliceDataConverter", "Get slice data from XML failed ", e);
                this.mMetricsFeatureProvider.action(0, 1727, 0, str + "_", 1);
            }
        } catch (Resources.NotFoundException | IOException | XmlPullParserException e2) {
            try {
                Log.w("SliceDataConverter", "Error parsing PreferenceScreen: ", e2);
                this.mMetricsFeatureProvider.action(0, 1726, 0, str, 1);
                if (0 != 0) {
                    xmlResourceParser.close();
                }
            } catch (Throwable th2) {
                th = th2;
                xmlResourceParser = null;
                if (xmlResourceParser != null) {
                }
                throw th;
            }
        } catch (SliceData.InvalidSliceDataException e3) {
            Log.w("SliceDataConverter", "Invalid data when building SliceData for " + str, e3);
            this.mMetricsFeatureProvider.action(0, 1725, 0, "", 1);
        }
        if (!"PreferenceScreen".equals(name)) {
            throw new RuntimeException("XML document must start with <PreferenceScreen> tag; found" + name + " at " + xmlResourceParser.getPositionDescription());
        }
        String dataTitle = PreferenceXmlParserUtils.getDataTitle(this.mContext, Xml.asAttributeSet(xmlResourceParser));
        Iterator<Bundle> it2 = PreferenceXmlParserUtils.extractMetadata(this.mContext, i, 2174).iterator();
        while (it2.hasNext()) {
            Bundle next2 = it2.next();
            String string = next2.getString("controller");
            if (!TextUtils.isEmpty(string)) {
                String string2 = next2.getString("key");
                BasePreferenceController preferenceController = SliceBuilderUtils.getPreferenceController(this.mContext, string, string2);
                if (preferenceController.isSliceable() && preferenceController.isAvailable()) {
                    String string3 = next2.getString("title");
                    String string4 = next2.getString("summary");
                    int i2 = next2.getInt("icon");
                    int sliceType = preferenceController.getSliceType();
                    it = it2;
                    arrayList.add(new SliceData.Builder().setKey(string2).setUri(preferenceController.getSliceUri()).setTitle(string3).setSummary(string4).setIcon(i2).setScreenTitle(dataTitle).setPreferenceControllerClassName(string).setFragmentName(str).setSliceType(sliceType).setUnavailableSliceSubtitle(next2.getString("unavailable_slice_subtitle")).setIsPublicSlice(preferenceController.isPublicSlice()).build());
                    it2 = it;
                }
            }
            it = it2;
            it2 = it;
        }
        xmlResourceParser.close();
        return arrayList;
    }

    private List<SliceData> getAccessibilitySliceData() {
        ArrayList arrayList = new ArrayList();
        String name = AccessibilitySlicePreferenceController.class.getName();
        String name2 = AccessibilitySettings.class.getName();
        SliceData.Builder preferenceControllerClassName = new SliceData.Builder().setFragmentName(name2).setScreenTitle(this.mContext.getText(R.string.accessibility_settings)).setPreferenceControllerClassName(name);
        HashSet hashSet = new HashSet();
        Collections.addAll(hashSet, this.mContext.getResources().getStringArray(R.array.config_settings_slices_accessibility_components));
        List<AccessibilityServiceInfo> accessibilityServiceInfoList = getAccessibilityServiceInfoList();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (AccessibilityServiceInfo accessibilityServiceInfo : accessibilityServiceInfoList) {
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            String flattenToString = new ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToString();
            if (hashSet.contains(flattenToString)) {
                String charSequence = resolveInfo.loadLabel(packageManager).toString();
                int iconResource = resolveInfo.getIconResource();
                if (iconResource == 0) {
                    iconResource = R.drawable.ic_accessibility_generic;
                }
                preferenceControllerClassName.setKey(flattenToString).setTitle(charSequence).setUri(new Uri.Builder().scheme("content").authority("com.android.settings.slices").appendPath("action").appendPath(flattenToString).build()).setIcon(iconResource).setSliceType(1);
                try {
                    arrayList.add(preferenceControllerClassName.build());
                } catch (SliceData.InvalidSliceDataException e) {
                    Log.w("SliceDataConverter", "Invalid data when building a11y SliceData for " + flattenToString, e);
                }
            }
        }
        return arrayList;
    }

    List<AccessibilityServiceInfo> getAccessibilityServiceInfoList() {
        return AccessibilityManager.getInstance(this.mContext).getInstalledAccessibilityServiceList();
    }
}
