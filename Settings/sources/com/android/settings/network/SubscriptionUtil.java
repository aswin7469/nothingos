package com.android.settings.network;

import android.content.Context;
import android.os.ParcelUuid;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.MccTable;
import com.android.internal.util.CollectionUtils;
import com.android.settings.R$string;
import com.android.settings.network.helper.SelectableSubscriptions;
import com.android.settings.network.helper.SubscriptionAnnotation;
import com.android.settings.network.telephony.DeleteEuiccSubscriptionDialogActivity;
import com.android.settings.network.telephony.ToggleSubscriptionDialogActivity;
import com.android.settingslib.DeviceInfoUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubscriptionUtil {
    private static List<SubscriptionInfo> sActiveResultsForTesting;
    private static List<SubscriptionInfo> sAvailableResultsForTesting;

    public static boolean showToggleForPhysicalSim(SubscriptionManager subscriptionManager) {
        return true;
    }

    public static void setAvailableSubscriptionsForTesting(List<SubscriptionInfo> list) {
        sAvailableResultsForTesting = list;
    }

    public static void setActiveSubscriptionsForTesting(List<SubscriptionInfo> list) {
        sActiveResultsForTesting = list;
    }

    public static List<SubscriptionInfo> getActiveSubscriptions(SubscriptionManager subscriptionManager) {
        List<SubscriptionInfo> list = sActiveResultsForTesting;
        if (list != null) {
            return list;
        }
        if (subscriptionManager == null) {
            return Collections.emptyList();
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        return activeSubscriptionInfoList == null ? new ArrayList() : activeSubscriptionInfoList;
    }

    static boolean isInactiveInsertedPSim(UiccSlotInfo uiccSlotInfo) {
        if (uiccSlotInfo != null && !uiccSlotInfo.getIsEuicc() && !((UiccPortInfo) uiccSlotInfo.getPorts().stream().findFirst().get()).isActive() && uiccSlotInfo.getCardStateInfo() == 2) {
            return true;
        }
        return false;
    }

    public static List<SubscriptionInfo> getAvailableSubscriptions(Context context) {
        List<SubscriptionInfo> list = sAvailableResultsForTesting;
        if (list != null) {
            return list;
        }
        return new ArrayList(CollectionUtils.emptyIfNull(getSelectableSubscriptionInfoList(context)));
    }

    public static SubscriptionInfo getAvailableSubscription(Context context, ProxySubscriptionManager proxySubscriptionManager, int i) {
        SubscriptionInfo accessibleSubscriptionInfo = proxySubscriptionManager.getAccessibleSubscriptionInfo(i);
        if (accessibleSubscriptionInfo == null) {
            return null;
        }
        ParcelUuid groupUuid = accessibleSubscriptionInfo.getGroupUuid();
        if (groupUuid == null || isPrimarySubscriptionWithinSameUuid(getUiccSlotsInfo(context), groupUuid, proxySubscriptionManager.getAccessibleSubscriptionsInfo(), i)) {
            return accessibleSubscriptionInfo;
        }
        return null;
    }

    private static UiccSlotInfo[] getUiccSlotsInfo(Context context) {
        return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getUiccSlotsInfo();
    }

    private static boolean isPrimarySubscriptionWithinSameUuid(UiccSlotInfo[] uiccSlotInfoArr, ParcelUuid parcelUuid, List<SubscriptionInfo> list, int i) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (SubscriptionInfo next : list) {
            if (parcelUuid.equals(next.getGroupUuid())) {
                if (!next.isEmbedded()) {
                    arrayList.add(next);
                } else {
                    if (!next.isOpportunistic()) {
                        arrayList2.add(next);
                    }
                    if (next.getSimSlotIndex() != -1) {
                        arrayList3.add(next);
                    } else {
                        arrayList4.add(next);
                    }
                }
            }
        }
        if (uiccSlotInfoArr != null && arrayList.size() > 0) {
            SubscriptionInfo searchForSubscriptionId = searchForSubscriptionId(arrayList, i);
            if (searchForSubscriptionId == null) {
                return false;
            }
            for (UiccSlotInfo uiccSlotInfo : uiccSlotInfoArr) {
                if (uiccSlotInfo != null && !uiccSlotInfo.getIsEuicc() && ((UiccPortInfo) uiccSlotInfo.getPorts().stream().findFirst().get()).getLogicalSlotIndex() == searchForSubscriptionId.getSimSlotIndex()) {
                    return true;
                }
            }
            return false;
        } else if (arrayList2.size() > 0) {
            Iterator it = arrayList2.iterator();
            int i2 = 0;
            boolean z = false;
            while (it.hasNext()) {
                SubscriptionInfo subscriptionInfo = (SubscriptionInfo) it.next();
                boolean z2 = subscriptionInfo.getSubscriptionId() == i;
                if (subscriptionInfo.getSimSlotIndex() == -1) {
                    z |= z2;
                } else if (z2) {
                    return true;
                } else {
                    i2++;
                }
            }
            if (i2 > 0) {
                return false;
            }
            return z;
        } else if (arrayList.size() > 0) {
            return false;
        } else {
            if (arrayList3.size() > 0) {
                if (((SubscriptionInfo) arrayList3.get(0)).getSubscriptionId() == i) {
                    return true;
                }
                return false;
            } else if (((SubscriptionInfo) arrayList4.get(0)).getSubscriptionId() == i) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static SubscriptionInfo searchForSubscriptionId(List<SubscriptionInfo> list, int i) {
        for (SubscriptionInfo next : list) {
            if (next.getSubscriptionId() == i) {
                return next;
            }
        }
        return null;
    }

    public static Map<Integer, CharSequence> getUniqueSubscriptionDisplayNames(Context context) {
        SubscriptionUtil$$ExternalSyntheticLambda10 subscriptionUtil$$ExternalSyntheticLambda10 = new SubscriptionUtil$$ExternalSyntheticLambda10(context);
        HashSet hashSet = new HashSet();
        SubscriptionUtil$$ExternalSyntheticLambda13 subscriptionUtil$$ExternalSyntheticLambda13 = new SubscriptionUtil$$ExternalSyntheticLambda13(subscriptionUtil$$ExternalSyntheticLambda10, (Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda10.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda11(hashSet)).map(new SubscriptionUtil$$ExternalSyntheticLambda12()).collect(Collectors.toSet()), context);
        hashSet.clear();
        return (Map) ((Stream) subscriptionUtil$$ExternalSyntheticLambda13.get()).map(new SubscriptionUtil$$ExternalSyntheticLambda16((Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda13.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda14(hashSet)).map(new SubscriptionUtil$$ExternalSyntheticLambda15()).collect(Collectors.toSet()))).collect(Collectors.toMap(new SubscriptionUtil$$ExternalSyntheticLambda17(), new SubscriptionUtil$$ExternalSyntheticLambda18()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$0(SubscriptionInfo subscriptionInfo) {
        return (subscriptionInfo == null || subscriptionInfo.getDisplayName() == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$1(Context context, SubscriptionInfo subscriptionInfo) {
        String str;
        AnonymousClass1DisplayInfo r0 = new Object() {
            public CharSequence originalName;
            public SubscriptionInfo subscriptionInfo;
            public CharSequence uniqueName;
        };
        r0.subscriptionInfo = subscriptionInfo;
        String charSequence = subscriptionInfo.getDisplayName().toString();
        if (TextUtils.equals(charSequence, "CARD")) {
            str = context.getResources().getString(R$string.sim_card);
        } else {
            str = charSequence.trim();
        }
        r0.originalName = str;
        return r0;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$3(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.originalName);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$5(Set set, Context context, AnonymousClass1DisplayInfo r3) {
        if (set.contains(r3.originalName)) {
            String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(context, r3.subscriptionInfo);
            if (bidiFormattedPhoneNumber == null) {
                bidiFormattedPhoneNumber = "";
            } else if (bidiFormattedPhoneNumber.length() > 4) {
                bidiFormattedPhoneNumber = bidiFormattedPhoneNumber.substring(bidiFormattedPhoneNumber.length() - 4);
            }
            if (TextUtils.isEmpty(bidiFormattedPhoneNumber)) {
                r3.uniqueName = r3.originalName;
            } else {
                r3.uniqueName = r3.originalName + " " + bidiFormattedPhoneNumber;
            }
        } else {
            r3.uniqueName = r3.originalName;
        }
        return r3;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$7(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.uniqueName);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$9(Set set, AnonymousClass1DisplayInfo r2) {
        if (set.contains(r2.uniqueName)) {
            r2.uniqueName = r2.originalName + " " + r2.subscriptionInfo.getSubscriptionId();
        }
        return r2;
    }

    public static CharSequence getUniqueSubscriptionDisplayName(Integer num, Context context) {
        return getUniqueSubscriptionDisplayNames(context).getOrDefault(num, "");
    }

    public static CharSequence getUniqueSubscriptionDisplayName(SubscriptionInfo subscriptionInfo, Context context) {
        return subscriptionInfo == null ? "" : getUniqueSubscriptionDisplayName(Integer.valueOf(subscriptionInfo.getSubscriptionId()), context);
    }

    public static String getDisplayName(SubscriptionInfo subscriptionInfo) {
        CharSequence displayName = subscriptionInfo.getDisplayName();
        return displayName != null ? displayName.toString() : "";
    }

    public static int getPhoneId(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null || (activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(i)) == null) {
            return -1;
        }
        return activeSubscriptionInfo.getSimSlotIndex();
    }

    public static List<SubscriptionInfo> getSelectableSubscriptionInfoList(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        List<SubscriptionInfo> availableSubscriptionInfoList = subscriptionManager.getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (isSubscriptionVisible(subscriptionManager, context, subscriptionInfo)) {
                ParcelUuid groupUuid = subscriptionInfo.getGroupUuid();
                if (groupUuid == null) {
                    arrayList.add(subscriptionInfo);
                } else if (!hashMap.containsKey(groupUuid) || (((SubscriptionInfo) hashMap.get(groupUuid)).getSimSlotIndex() == -1 && subscriptionInfo.getSimSlotIndex() != -1)) {
                    arrayList.remove(hashMap.get(groupUuid));
                    arrayList.add(subscriptionInfo);
                    hashMap.put(groupUuid, subscriptionInfo);
                }
            }
        }
        return arrayList;
    }

    public static void startToggleSubscriptionDialogActivity(Context context, int i, boolean z) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            Log.i("SubscriptionUtil", "Unable to toggle subscription due to invalid subscription ID.");
        } else {
            context.startActivity(ToggleSubscriptionDialogActivity.getIntent(context, i, z));
        }
    }

    public static void startDeleteEuiccSubscriptionDialogActivity(Context context, int i) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            Log.i("SubscriptionUtil", "Unable to delete subscription due to invalid subscription ID.");
        } else {
            context.startActivity(DeleteEuiccSubscriptionDialogActivity.getIntent(context, i));
        }
    }

    public static SubscriptionInfo getSubById(SubscriptionManager subscriptionManager, int i) {
        if (i == -1) {
            return null;
        }
        return (SubscriptionInfo) subscriptionManager.getAllSubscriptionInfoList().stream().filter(new SubscriptionUtil$$ExternalSyntheticLambda0(i)).findFirst().orElse((Object) null);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSubById$12(int i, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getSubscriptionId() == i;
    }

    public static boolean isSubscriptionVisible(SubscriptionManager subscriptionManager, Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            return false;
        }
        if (subscriptionInfo.getGroupUuid() == null || !subscriptionInfo.isOpportunistic()) {
            return true;
        }
        if (((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).hasCarrierPrivileges() || subscriptionManager.canManageSubscription(subscriptionInfo)) {
            return true;
        }
        return false;
    }

    public static List<SubscriptionInfo> findAllSubscriptionsInGroup(SubscriptionManager subscriptionManager, int i) {
        SubscriptionInfo subById = getSubById(subscriptionManager, i);
        if (subById == null) {
            return Collections.emptyList();
        }
        ParcelUuid groupUuid = subById.getGroupUuid();
        List availableSubscriptionInfoList = subscriptionManager.getAvailableSubscriptionInfoList();
        return (availableSubscriptionInfoList == null || availableSubscriptionInfoList.isEmpty() || groupUuid == null) ? Collections.singletonList(subById) : (List) availableSubscriptionInfoList.stream().filter(new SubscriptionUtil$$ExternalSyntheticLambda25(groupUuid)).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAllSubscriptionsInGroup$13(ParcelUuid parcelUuid, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.isEmbedded() && parcelUuid.equals(subscriptionInfo.getGroupUuid());
    }

    public static String getFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            Log.e("SubscriptionUtil", "Invalid subscription.");
            return null;
        }
        String phoneNumber = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getPhoneNumber(subscriptionInfo.getSubscriptionId());
        if (TextUtils.isEmpty(phoneNumber)) {
            return null;
        }
        return PhoneNumberUtils.formatNumber(phoneNumber, MccTable.countryCodeForMcc(subscriptionInfo.getMccString()));
    }

    public static SubscriptionInfo getFirstRemovableSubscription(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        List<UiccCardInfo> uiccCardsInfo = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getUiccCardsInfo();
        if (uiccCardsInfo == null) {
            Log.w("SubscriptionUtil", "UICC cards info list is empty.");
            return null;
        }
        List<SubscriptionInfo> allSubscriptionInfoList = subscriptionManager.getAllSubscriptionInfoList();
        if (allSubscriptionInfoList == null) {
            Log.w("SubscriptionUtil", "All subscription info list is empty.");
            return null;
        }
        for (UiccCardInfo next : uiccCardsInfo) {
            if (next == null) {
                Log.w("SubscriptionUtil", "Got null card.");
            } else if (!next.isRemovable() || next.getCardId() == -1) {
                Log.i("SubscriptionUtil", "Skip embedded card or invalid cardId on slot: " + next.getPhysicalSlotIndex());
            } else {
                Log.i("SubscriptionUtil", "Target removable cardId :" + next.getCardId());
                for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
                    if (next.getCardId() == subscriptionInfo.getCardId()) {
                        return subscriptionInfo;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static CharSequence getDefaultSimConfig(Context context, int i) {
        boolean z = i == getDefaultVoiceSubscriptionId();
        boolean z2 = i == getDefaultSmsSubscriptionId();
        boolean z3 = i == getDefaultDataSubscriptionId();
        if (!z3 && !z && !z2) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (z3) {
            sb.append(getResForDefaultConfig(context, R$string.default_active_sim_mobile_data));
            sb.append(", ");
        }
        if (z) {
            sb.append(getResForDefaultConfig(context, R$string.default_active_sim_calls));
            sb.append(", ");
        }
        if (z2) {
            sb.append(getResForDefaultConfig(context, R$string.default_active_sim_sms));
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        return context.getResources().getString(R$string.sim_category_default_active_sim, new Object[]{sb});
    }

    private static String getResForDefaultConfig(Context context, int i) {
        return context.getResources().getString(i);
    }

    private static int getDefaultVoiceSubscriptionId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    private static int getDefaultSmsSubscriptionId() {
        return SubscriptionManager.getDefaultSmsSubscriptionId();
    }

    private static int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    /* access modifiers changed from: private */
    public static SubscriptionAnnotation getDefaultSubscriptionSelection(List<SubscriptionAnnotation> list) {
        if (list == null) {
            return null;
        }
        return (SubscriptionAnnotation) list.stream().filter(new MobileNetworkSummaryStatus$$ExternalSyntheticLambda3()).filter(new SubscriptionUtil$$ExternalSyntheticLambda27()).findFirst().orElse((Object) null);
    }

    public static SubscriptionInfo getSubscriptionOrDefault(Context context, int i) {
        Function function;
        if (i != -1) {
            function = null;
        } else {
            new SubscriptionUtil$$ExternalSyntheticLambda19
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: CONSTRUCTOR  (r0v2 ? I:com.android.settings.network.SubscriptionUtil$$ExternalSyntheticLambda19) =  call: com.android.settings.network.SubscriptionUtil$$ExternalSyntheticLambda19.<init>():void type: CONSTRUCTOR in method: com.android.settings.network.SubscriptionUtil.getSubscriptionOrDefault(android.content.Context, int):android.telephony.SubscriptionInfo, dex: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:156)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:485)
                	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
                	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r0v2 ?
                	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:189)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:620)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	... 34 more
                */
            /*
                r0 = -1
                if (r2 == r0) goto L_0x0005
                r0 = 0
                goto L_0x000a
            L_0x0005:
                com.android.settings.network.SubscriptionUtil$$ExternalSyntheticLambda19 r0 = new com.android.settings.network.SubscriptionUtil$$ExternalSyntheticLambda19
                r0.<init>()
            L_0x000a:
                android.telephony.SubscriptionInfo r1 = getSubscription(r1, r2, r0)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.SubscriptionUtil.getSubscriptionOrDefault(android.content.Context, int):android.telephony.SubscriptionInfo");
        }

        private static SubscriptionInfo getSubscription(Context context, int i, Function<List<SubscriptionAnnotation>, SubscriptionAnnotation> function) {
            List call = new SelectableSubscriptions(context, true).call();
            Log.d("SubscriptionUtil", "get subId=" + i + " from " + call);
            SubscriptionAnnotation subscriptionAnnotation = (SubscriptionAnnotation) call.stream().filter(new MobileNetworkSummaryStatus$$ExternalSyntheticLambda3()).filter(new SubscriptionUtil$$ExternalSyntheticLambda28(i)).findFirst().orElse((Object) null);
            if (subscriptionAnnotation == null && function != null) {
                subscriptionAnnotation = function.apply(call);
            }
            if (subscriptionAnnotation == null) {
                return null;
            }
            return subscriptionAnnotation.getSubInfo();
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getSubscription$15(int i, SubscriptionAnnotation subscriptionAnnotation) {
            return subscriptionAnnotation.getSubscriptionId() == i;
        }

        public static CharSequence getNtUniqueSubscriptionDisplayName(SubscriptionInfo subscriptionInfo, Context context, boolean z) {
            return subscriptionInfo == null ? "" : getNtUniqueSubscriptionDisplayName(Integer.valueOf(subscriptionInfo.getSubscriptionId()), context, z);
        }

        public static CharSequence getNtUniqueSubscriptionDisplayName(Integer num, Context context, boolean z) {
            return getNtUniqueSubscriptionDisplayNames(context, z).getOrDefault(num, "");
        }

        public static Map<Integer, CharSequence> getNtUniqueSubscriptionDisplayNames(Context context, boolean z) {
            SubscriptionUtil$$ExternalSyntheticLambda1 subscriptionUtil$$ExternalSyntheticLambda1 = new SubscriptionUtil$$ExternalSyntheticLambda1(context);
            HashSet hashSet = new HashSet();
            Set set = (Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda1.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda2(hashSet)).map(new SubscriptionUtil$$ExternalSyntheticLambda3()).collect(Collectors.toSet());
            SubscriptionUtil$$ExternalSyntheticLambda4 subscriptionUtil$$ExternalSyntheticLambda4 = new SubscriptionUtil$$ExternalSyntheticLambda4(subscriptionUtil$$ExternalSyntheticLambda1, z, context);
            hashSet.clear();
            return (Map) ((Stream) subscriptionUtil$$ExternalSyntheticLambda4.get()).map(new SubscriptionUtil$$ExternalSyntheticLambda7((Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda4.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda5(hashSet)).map(new SubscriptionUtil$$ExternalSyntheticLambda6()).collect(Collectors.toSet()))).collect(Collectors.toMap(new SubscriptionUtil$$ExternalSyntheticLambda8(), new SubscriptionUtil$$ExternalSyntheticLambda9()));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getNtUniqueSubscriptionDisplayNames$16(SubscriptionInfo subscriptionInfo) {
            return (subscriptionInfo == null || subscriptionInfo.getDisplayName() == null) ? false : true;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ AnonymousClass2DisplayInfo lambda$getNtUniqueSubscriptionDisplayNames$17(Context context, SubscriptionInfo subscriptionInfo) {
            String str;
            AnonymousClass2DisplayInfo r0 = new Object() {
                public CharSequence originalName;
                public SubscriptionInfo subscriptionInfo;
                public CharSequence uniqueName;
            };
            r0.subscriptionInfo = subscriptionInfo;
            String charSequence = subscriptionInfo.getDisplayName().toString();
            if (TextUtils.equals(charSequence, "CARD")) {
                str = context.getResources().getString(R$string.sim_card);
            } else {
                str = charSequence.trim();
            }
            r0.originalName = str;
            return r0;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getNtUniqueSubscriptionDisplayNames$19(Set set, AnonymousClass2DisplayInfo r1) {
            return !set.add(r1.originalName);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ AnonymousClass2DisplayInfo lambda$getNtUniqueSubscriptionDisplayNames$21(boolean z, Context context, AnonymousClass2DisplayInfo r3) {
            if (z) {
                String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(context, r3.subscriptionInfo);
                if (bidiFormattedPhoneNumber == null) {
                    bidiFormattedPhoneNumber = "";
                } else if (bidiFormattedPhoneNumber.length() > 4) {
                    bidiFormattedPhoneNumber = bidiFormattedPhoneNumber.substring(bidiFormattedPhoneNumber.length() - 4);
                }
                if (TextUtils.isEmpty(bidiFormattedPhoneNumber)) {
                    r3.uniqueName = r3.originalName;
                } else {
                    r3.uniqueName = r3.originalName + " " + bidiFormattedPhoneNumber;
                }
            } else {
                r3.uniqueName = r3.originalName;
            }
            return r3;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getNtUniqueSubscriptionDisplayNames$23(Set set, AnonymousClass2DisplayInfo r1) {
            return !set.add(r1.uniqueName);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ AnonymousClass2DisplayInfo lambda$getNtUniqueSubscriptionDisplayNames$25(Set set, AnonymousClass2DisplayInfo r2) {
            if (set.contains(r2.uniqueName)) {
                r2.uniqueName = r2.originalName + " " + r2.subscriptionInfo.getSubscriptionId();
            }
            return r2;
        }
    }
