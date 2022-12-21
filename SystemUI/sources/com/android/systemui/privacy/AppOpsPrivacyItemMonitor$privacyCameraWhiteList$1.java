package com.android.systemui.privacy;

import java.util.ArrayList;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u00010\u0002`\u0003¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/privacy/AppOpsPrivacyItemMonitor$privacyCameraWhiteList$1", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$privacyCameraWhiteList$1 extends ArrayList<String> {
    AppOpsPrivacyItemMonitor$privacyCameraWhiteList$1() {
        add("com.nt.facerecognition");
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj == null ? true : obj instanceof String)) {
            return false;
        }
        return contains((String) obj);
    }

    public /* bridge */ boolean contains(String str) {
        return super.contains(str);
    }

    public /* bridge */ int getSize() {
        return super.size();
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj == null ? true : obj instanceof String)) {
            return -1;
        }
        return indexOf((String) obj);
    }

    public /* bridge */ int indexOf(String str) {
        return super.indexOf(str);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj == null ? true : obj instanceof String)) {
            return -1;
        }
        return lastIndexOf((String) obj);
    }

    public /* bridge */ int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }

    public final /* bridge */ String remove(int i) {
        return removeAt(i);
    }

    public final /* bridge */ boolean remove(Object obj) {
        if (!(obj == null ? true : obj instanceof String)) {
            return false;
        }
        return remove((String) obj);
    }

    public /* bridge */ boolean remove(String str) {
        return super.remove((Object) str);
    }

    public /* bridge */ String removeAt(int i) {
        return (String) super.remove(i);
    }

    public final /* bridge */ int size() {
        return getSize();
    }
}
