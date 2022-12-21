package com.android.wifi.p018x.com.android.modules.utils.build;

import android.os.Build;
import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
import java.util.Set;

/* renamed from: com.android.wifi.x.com.android.modules.utils.build.UnboundedSdkLevel */
public final class UnboundedSdkLevel {
    private static final SparseArray<Set<String>> PREVIOUS_CODENAMES;
    private static final UnboundedSdkLevel sInstance;
    private final String mCodename;
    private final boolean mIsReleaseBuild;
    private final Set<String> mKnownCodenames;
    private final int mSdkInt;

    public static boolean isAtLeast(String str) {
        return sInstance.isAtLeastInternal(str);
    }

    public static boolean isAtMost(String str) {
        return sInstance.isAtMostInternal(str);
    }

    static {
        Set set;
        SparseArray<Set<String>> sparseArray = new SparseArray<>(4);
        PREVIOUS_CODENAMES = sparseArray;
        sparseArray.put(29, Set.m1751of("Q"));
        sparseArray.put(30, Set.m1752of("Q", "R"));
        sparseArray.put(31, Set.m1753of("Q", "R", ExifInterface.LATITUDE_SOUTH));
        sparseArray.put(32, Set.m1754of("Q", "R", ExifInterface.LATITUDE_SOUTH, "Sv2"));
        int i = Build.VERSION.SDK_INT;
        String str = Build.VERSION.CODENAME;
        if (SdkLevel.isAtLeastT()) {
            set = Build.VERSION.KNOWN_CODENAMES;
        } else {
            set = sparseArray.get(Build.VERSION.SDK_INT);
        }
        sInstance = new UnboundedSdkLevel(i, str, set);
    }

    UnboundedSdkLevel(int i, String str, Set<String> set) {
        this.mSdkInt = i;
        this.mCodename = str;
        this.mIsReleaseBuild = "REL".equals(str);
        this.mKnownCodenames = set;
    }

    /* access modifiers changed from: package-private */
    public boolean isAtLeastInternal(String str) {
        String removeFingerprint = removeFingerprint(str);
        if (this.mIsReleaseBuild) {
            if (isCodename(removeFingerprint)) {
                if (!this.mKnownCodenames.contains(removeFingerprint)) {
                    return false;
                }
                throw new IllegalArgumentException("Artifact with a known codename " + removeFingerprint + " must be recompiled with a finalized integer version.");
            } else if (this.mSdkInt >= Integer.parseInt(removeFingerprint)) {
                return true;
            } else {
                return false;
            }
        } else if (isCodename(removeFingerprint)) {
            return this.mKnownCodenames.contains(removeFingerprint);
        } else {
            if (this.mSdkInt >= Integer.parseInt(removeFingerprint)) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAtMostInternal(String str) {
        String removeFingerprint = removeFingerprint(str);
        if (this.mIsReleaseBuild) {
            if (isCodename(removeFingerprint)) {
                if (!this.mKnownCodenames.contains(removeFingerprint)) {
                    return true;
                }
                throw new IllegalArgumentException("Artifact with a known codename " + removeFingerprint + " must be recompiled with a finalized integer version.");
            } else if (this.mSdkInt <= Integer.parseInt(removeFingerprint)) {
                return true;
            } else {
                return false;
            }
        } else if (isCodename(removeFingerprint)) {
            if (!this.mKnownCodenames.contains(removeFingerprint) || this.mCodename.equals(removeFingerprint)) {
                return true;
            }
            return false;
        } else if (this.mSdkInt < Integer.parseInt(removeFingerprint)) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r2.indexOf(46);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String removeFingerprint(java.lang.String r2) {
        /*
            r1 = this;
            boolean r1 = r1.isCodename(r2)
            if (r1 == 0) goto L_0x0015
            r1 = 46
            int r1 = r2.indexOf((int) r1)
            r0 = -1
            if (r1 == r0) goto L_0x0015
            r0 = 0
            java.lang.String r1 = r2.substring(r0, r1)
            return r1
        L_0x0015:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifi.p018x.com.android.modules.utils.build.UnboundedSdkLevel.removeFingerprint(java.lang.String):java.lang.String");
    }

    private boolean isCodename(String str) {
        if (str.length() != 0) {
            return Character.isUpperCase(str.charAt(0));
        }
        throw new IllegalArgumentException();
    }
}
