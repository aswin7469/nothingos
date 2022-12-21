package androidx.slice.compat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.collection.ArraySet;
import androidx.core.util.ObjectsCompat;
import androidx.slice.SliceSpec;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompatPinnedList {
    private static final long BOOT_THRESHOLD = 2000;
    private static final String LAST_BOOT = "last_boot";
    private static final String PIN_PREFIX = "pinned_";
    private static final String SPEC_NAME_PREFIX = "spec_names_";
    private static final String SPEC_REV_PREFIX = "spec_revs_";
    private final Context mContext;
    private final String mPrefsName;

    public CompatPinnedList(Context context, String str) {
        this.mContext = context;
        this.mPrefsName = str;
    }

    private SharedPreferences getPrefs() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(this.mPrefsName, 0);
        long j = sharedPreferences.getLong(LAST_BOOT, 0);
        long bootTime = getBootTime();
        if (Math.abs(j - bootTime) > 2000) {
            sharedPreferences.edit().clear().putLong(LAST_BOOT, bootTime).apply();
        }
        return sharedPreferences;
    }

    public List<Uri> getPinnedSlices() {
        ArrayList arrayList = new ArrayList();
        for (String next : getPrefs().getAll().keySet()) {
            if (next.startsWith(PIN_PREFIX)) {
                Uri parse = Uri.parse(next.substring(7));
                if (!getPins(parse).isEmpty()) {
                    arrayList.add(parse);
                }
            }
        }
        return arrayList;
    }

    private Set<String> getPins(Uri uri) {
        return getPrefs().getStringSet(PIN_PREFIX + uri.toString(), new ArraySet());
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:14:0x0061=Splitter:B:14:0x0061, B:21:0x0079=Splitter:B:21:0x0079} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized androidx.collection.ArraySet<androidx.slice.SliceSpec> getSpecs(android.net.Uri r7) {
        /*
            r6 = this;
            java.lang.String r0 = "spec_revs_"
            java.lang.String r1 = "spec_names_"
            monitor-enter(r6)
            androidx.collection.ArraySet r2 = new androidx.collection.ArraySet     // Catch:{ all -> 0x0080 }
            r2.<init>()     // Catch:{ all -> 0x0080 }
            android.content.SharedPreferences r3 = r6.getPrefs()     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            r4.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0080 }
            java.lang.String r1 = r7.toString()     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r1 = r4.append((java.lang.String) r1)     // Catch:{ all -> 0x0080 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0080 }
            r4 = 0
            java.lang.String r1 = r3.getString(r1, r4)     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0080 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r7 = r5.append((java.lang.String) r7)     // Catch:{ all -> 0x0080 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0080 }
            java.lang.String r7 = r3.getString(r7, r4)     // Catch:{ all -> 0x0080 }
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0080 }
            if (r0 != 0) goto L_0x0079
            boolean r0 = android.text.TextUtils.isEmpty(r7)     // Catch:{ all -> 0x0080 }
            if (r0 == 0) goto L_0x0048
            goto L_0x0079
        L_0x0048:
            java.lang.String r0 = ","
            r3 = -1
            java.lang.String[] r0 = r1.split(r0, r3)     // Catch:{ all -> 0x0080 }
            java.lang.String r1 = ","
            java.lang.String[] r7 = r7.split(r1, r3)     // Catch:{ all -> 0x0080 }
            int r1 = r0.length     // Catch:{ all -> 0x0080 }
            int r3 = r7.length     // Catch:{ all -> 0x0080 }
            if (r1 == r3) goto L_0x0060
            androidx.collection.ArraySet r7 = new androidx.collection.ArraySet     // Catch:{ all -> 0x0080 }
            r7.<init>()     // Catch:{ all -> 0x0080 }
            monitor-exit(r6)
            return r7
        L_0x0060:
            r1 = 0
        L_0x0061:
            int r3 = r0.length     // Catch:{ all -> 0x0080 }
            if (r1 >= r3) goto L_0x0077
            androidx.slice.SliceSpec r3 = new androidx.slice.SliceSpec     // Catch:{ all -> 0x0080 }
            r4 = r0[r1]     // Catch:{ all -> 0x0080 }
            r5 = r7[r1]     // Catch:{ all -> 0x0080 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ all -> 0x0080 }
            r3.<init>(r4, r5)     // Catch:{ all -> 0x0080 }
            r2.add(r3)     // Catch:{ all -> 0x0080 }
            int r1 = r1 + 1
            goto L_0x0061
        L_0x0077:
            monitor-exit(r6)
            return r2
        L_0x0079:
            androidx.collection.ArraySet r7 = new androidx.collection.ArraySet     // Catch:{ all -> 0x0080 }
            r7.<init>()     // Catch:{ all -> 0x0080 }
            monitor-exit(r6)
            return r7
        L_0x0080:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.compat.CompatPinnedList.getSpecs(android.net.Uri):androidx.collection.ArraySet");
    }

    private void setPins(Uri uri, Set<String> set) {
        getPrefs().edit().putStringSet(PIN_PREFIX + uri.toString(), set).apply();
    }

    private void setSpecs(Uri uri, ArraySet<SliceSpec> arraySet) {
        String[] strArr = new String[arraySet.size()];
        String[] strArr2 = new String[arraySet.size()];
        for (int i = 0; i < arraySet.size(); i++) {
            strArr[i] = arraySet.valueAt(i).getType();
            strArr2[i] = String.valueOf(arraySet.valueAt(i).getRevision());
        }
        getPrefs().edit().putString(SPEC_NAME_PREFIX + uri.toString(), TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, strArr)).putString(SPEC_REV_PREFIX + uri.toString(), TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, strArr2)).apply();
    }

    /* access modifiers changed from: protected */
    public long getBootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public synchronized boolean addPin(Uri uri, String str, Set<SliceSpec> set) {
        boolean isEmpty;
        Set<String> pins = getPins(uri);
        isEmpty = pins.isEmpty();
        pins.add(str);
        setPins(uri, pins);
        if (isEmpty) {
            setSpecs(uri, new ArraySet(set));
        } else {
            setSpecs(uri, mergeSpecs(getSpecs(uri), set));
        }
        return isEmpty;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0029, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean removePin(android.net.Uri r4, java.lang.String r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.Set r0 = r3.getPins(r4)     // Catch:{ all -> 0x002c }
            boolean r1 = r0.isEmpty()     // Catch:{ all -> 0x002c }
            r2 = 0
            if (r1 != 0) goto L_0x002a
            boolean r1 = r0.contains(r5)     // Catch:{ all -> 0x002c }
            if (r1 != 0) goto L_0x0013
            goto L_0x002a
        L_0x0013:
            r0.remove(r5)     // Catch:{ all -> 0x002c }
            r3.setPins(r4, r0)     // Catch:{ all -> 0x002c }
            androidx.collection.ArraySet r5 = new androidx.collection.ArraySet     // Catch:{ all -> 0x002c }
            r5.<init>()     // Catch:{ all -> 0x002c }
            r3.setSpecs(r4, r5)     // Catch:{ all -> 0x002c }
            int r4 = r0.size()     // Catch:{ all -> 0x002c }
            if (r4 != 0) goto L_0x0028
            r2 = 1
        L_0x0028:
            monitor-exit(r3)
            return r2
        L_0x002a:
            monitor-exit(r3)
            return r2
        L_0x002c:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.compat.CompatPinnedList.removePin(android.net.Uri, java.lang.String):boolean");
    }

    private static ArraySet<SliceSpec> mergeSpecs(ArraySet<SliceSpec> arraySet, Set<SliceSpec> set) {
        int i;
        int i2 = 0;
        while (i2 < arraySet.size()) {
            SliceSpec valueAt = arraySet.valueAt(i2);
            SliceSpec findSpec = findSpec(set, valueAt.getType());
            if (findSpec == null) {
                i = i2 - 1;
                arraySet.removeAt(i2);
            } else if (findSpec.getRevision() < valueAt.getRevision()) {
                i = i2 - 1;
                arraySet.removeAt(i2);
                arraySet.add(findSpec);
            } else {
                i2++;
            }
            i2 = i;
            i2++;
        }
        return arraySet;
    }

    private static SliceSpec findSpec(Set<SliceSpec> set, String str) {
        for (SliceSpec next : set) {
            if (ObjectsCompat.equals(next.getType(), str)) {
                return next;
            }
        }
        return null;
    }
}
