package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Intent;
import com.android.internal.logging.InstanceId;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001BO\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0005HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\bHÆ\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\nHÆ\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\fHÆ\u0003J\t\u0010&\u001a\u00020\u000eHÆ\u0003J\t\u0010'\u001a\u00020\u0010HÆ\u0003Jc\u0010(\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010HÆ\u0001J\u0013\u0010)\u001a\u00020\u00052\b\u0010*\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0010\u0010+\u001a\u0004\u0018\u00010,2\u0006\u0010-\u001a\u00020.J\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\b0\nJ\t\u00100\u001a\u000201HÖ\u0001J\u0006\u00102\u001a\u00020\u0005J\t\u00103\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001c¨\u00064"}, mo65043d2 = {"Lcom/android/systemui/media/SmartspaceMediaData;", "", "targetId", "", "isActive", "", "packageName", "cardAction", "Landroid/app/smartspace/SmartspaceAction;", "recommendations", "", "dismissIntent", "Landroid/content/Intent;", "headphoneConnectionTimeMillis", "", "instanceId", "Lcom/android/internal/logging/InstanceId;", "(Ljava/lang/String;ZLjava/lang/String;Landroid/app/smartspace/SmartspaceAction;Ljava/util/List;Landroid/content/Intent;JLcom/android/internal/logging/InstanceId;)V", "getCardAction", "()Landroid/app/smartspace/SmartspaceAction;", "getDismissIntent", "()Landroid/content/Intent;", "getHeadphoneConnectionTimeMillis", "()J", "getInstanceId", "()Lcom/android/internal/logging/InstanceId;", "()Z", "getPackageName", "()Ljava/lang/String;", "getRecommendations", "()Ljava/util/List;", "getTargetId", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "getAppName", "", "context", "Landroid/content/Context;", "getValidRecommendations", "hashCode", "", "isValid", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartspaceMediaData.kt */
public final class SmartspaceMediaData {
    private final SmartspaceAction cardAction;
    private final Intent dismissIntent;
    private final long headphoneConnectionTimeMillis;
    private final InstanceId instanceId;
    private final boolean isActive;
    private final String packageName;
    private final List<SmartspaceAction> recommendations;
    private final String targetId;

    public static /* synthetic */ SmartspaceMediaData copy$default(SmartspaceMediaData smartspaceMediaData, String str, boolean z, String str2, SmartspaceAction smartspaceAction, List list, Intent intent, long j, InstanceId instanceId2, int i, Object obj) {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        int i2 = i;
        return smartspaceMediaData.copy((i2 & 1) != 0 ? smartspaceMediaData2.targetId : str, (i2 & 2) != 0 ? smartspaceMediaData2.isActive : z, (i2 & 4) != 0 ? smartspaceMediaData2.packageName : str2, (i2 & 8) != 0 ? smartspaceMediaData2.cardAction : smartspaceAction, (i2 & 16) != 0 ? smartspaceMediaData2.recommendations : list, (i2 & 32) != 0 ? smartspaceMediaData2.dismissIntent : intent, (i2 & 64) != 0 ? smartspaceMediaData2.headphoneConnectionTimeMillis : j, (i2 & 128) != 0 ? smartspaceMediaData2.instanceId : instanceId2);
    }

    public final String component1() {
        return this.targetId;
    }

    public final boolean component2() {
        return this.isActive;
    }

    public final String component3() {
        return this.packageName;
    }

    public final SmartspaceAction component4() {
        return this.cardAction;
    }

    public final List<SmartspaceAction> component5() {
        return this.recommendations;
    }

    public final Intent component6() {
        return this.dismissIntent;
    }

    public final long component7() {
        return this.headphoneConnectionTimeMillis;
    }

    public final InstanceId component8() {
        return this.instanceId;
    }

    public final SmartspaceMediaData copy(String str, boolean z, String str2, SmartspaceAction smartspaceAction, List<SmartspaceAction> list, Intent intent, long j, InstanceId instanceId2) {
        Intrinsics.checkNotNullParameter(str, "targetId");
        Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        List<SmartspaceAction> list2 = list;
        Intrinsics.checkNotNullParameter(list2, "recommendations");
        InstanceId instanceId3 = instanceId2;
        Intrinsics.checkNotNullParameter(instanceId3, "instanceId");
        return new SmartspaceMediaData(str, z, str2, smartspaceAction, list2, intent, j, instanceId3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmartspaceMediaData)) {
            return false;
        }
        SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) obj;
        return Intrinsics.areEqual((Object) this.targetId, (Object) smartspaceMediaData.targetId) && this.isActive == smartspaceMediaData.isActive && Intrinsics.areEqual((Object) this.packageName, (Object) smartspaceMediaData.packageName) && Intrinsics.areEqual((Object) this.cardAction, (Object) smartspaceMediaData.cardAction) && Intrinsics.areEqual((Object) this.recommendations, (Object) smartspaceMediaData.recommendations) && Intrinsics.areEqual((Object) this.dismissIntent, (Object) smartspaceMediaData.dismissIntent) && this.headphoneConnectionTimeMillis == smartspaceMediaData.headphoneConnectionTimeMillis && Intrinsics.areEqual((Object) this.instanceId, (Object) smartspaceMediaData.instanceId);
    }

    public int hashCode() {
        int hashCode = this.targetId.hashCode() * 31;
        boolean z = this.isActive;
        if (z) {
            z = true;
        }
        int hashCode2 = (((hashCode + (z ? 1 : 0)) * 31) + this.packageName.hashCode()) * 31;
        SmartspaceAction smartspaceAction = this.cardAction;
        int i = 0;
        int hashCode3 = (((hashCode2 + (smartspaceAction == null ? 0 : smartspaceAction.hashCode())) * 31) + this.recommendations.hashCode()) * 31;
        Intent intent = this.dismissIntent;
        if (intent != null) {
            i = intent.hashCode();
        }
        return ((((hashCode3 + i) * 31) + Long.hashCode(this.headphoneConnectionTimeMillis)) * 31) + this.instanceId.hashCode();
    }

    public String toString() {
        return "SmartspaceMediaData(targetId=" + this.targetId + ", isActive=" + this.isActive + ", packageName=" + this.packageName + ", cardAction=" + this.cardAction + ", recommendations=" + this.recommendations + ", dismissIntent=" + this.dismissIntent + ", headphoneConnectionTimeMillis=" + this.headphoneConnectionTimeMillis + ", instanceId=" + this.instanceId + ')';
    }

    public SmartspaceMediaData(String str, boolean z, String str2, SmartspaceAction smartspaceAction, List<SmartspaceAction> list, Intent intent, long j, InstanceId instanceId2) {
        Intrinsics.checkNotNullParameter(str, "targetId");
        Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(list, "recommendations");
        Intrinsics.checkNotNullParameter(instanceId2, "instanceId");
        this.targetId = str;
        this.isActive = z;
        this.packageName = str2;
        this.cardAction = smartspaceAction;
        this.recommendations = list;
        this.dismissIntent = intent;
        this.headphoneConnectionTimeMillis = j;
        this.instanceId = instanceId2;
    }

    public final String getTargetId() {
        return this.targetId;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final SmartspaceAction getCardAction() {
        return this.cardAction;
    }

    public final List<SmartspaceAction> getRecommendations() {
        return this.recommendations;
    }

    public final Intent getDismissIntent() {
        return this.dismissIntent;
    }

    public final long getHeadphoneConnectionTimeMillis() {
        return this.headphoneConnectionTimeMillis;
    }

    public final InstanceId getInstanceId() {
        return this.instanceId;
    }

    public final boolean isValid() {
        return getValidRecommendations().size() >= 3;
    }

    public final List<SmartspaceAction> getValidRecommendations() {
        Collection arrayList = new ArrayList();
        for (Object next : this.recommendations) {
            if (((SmartspaceAction) next).getIcon() != null) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        r0 = (r0 = r0.getIntent()).getExtras();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.CharSequence getAppName(android.content.Context r6) {
        /*
            r5 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            android.app.smartspace.SmartspaceAction r0 = r5.cardAction
            r1 = 0
            if (r0 == 0) goto L_0x001d
            android.content.Intent r0 = r0.getIntent()
            if (r0 == 0) goto L_0x001d
            android.os.Bundle r0 = r0.getExtras()
            if (r0 == 0) goto L_0x001d
            java.lang.String r2 = "KEY_SMARTSPACE_APP_NAME"
            java.lang.String r0 = r0.getString(r2)
            goto L_0x001e
        L_0x001d:
            r0 = r1
        L_0x001e:
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x0027
            return r0
        L_0x0027:
            android.content.pm.PackageManager r6 = r6.getPackageManager()
            java.lang.String r0 = r5.packageName
            android.content.Intent r0 = r6.getLaunchIntentForPackage(r0)
            r2 = 0
            if (r0 == 0) goto L_0x003d
            android.content.pm.ActivityInfo r5 = r0.resolveActivityInfo(r6, r2)
            java.lang.CharSequence r5 = r5.loadLabel(r6)
            return r5
        L_0x003d:
            java.lang.String r0 = com.android.systemui.media.SmartspaceMediaDataKt.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Package "
            r3.<init>((java.lang.String) r4)
            java.lang.String r4 = r5.packageName
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.String r4 = " does not have a main launcher activity. Fallback to full app name"
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.w(r0, r3)
            java.lang.String r5 = r5.packageName     // Catch:{ NameNotFoundException -> 0x0066 }
            android.content.pm.ApplicationInfo r5 = r6.getApplicationInfo(r5, r2)     // Catch:{ NameNotFoundException -> 0x0066 }
            java.lang.CharSequence r1 = r6.getApplicationLabel(r5)     // Catch:{ NameNotFoundException -> 0x0066 }
            goto L_0x0069
        L_0x0066:
            r5 = r1
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5
        L_0x0069:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.SmartspaceMediaData.getAppName(android.content.Context):java.lang.CharSequence");
    }
}
