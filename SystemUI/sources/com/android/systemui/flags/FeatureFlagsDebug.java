package com.android.systemui.flags;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FlagListenable;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.util.settings.SecureSettings;
import java.lang.reflect.Field;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Named;
import kotlinx.coroutines.DebugKt;

@SysUISingleton
public class FeatureFlagsDebug implements FeatureFlags, Dumpable {
    static final String ALL_FLAGS = "all_flags";
    private static final String FLAG_COMMAND = "flag";
    private static final String TAG = "SysUIFlags";
    /* access modifiers changed from: private */
    public final Map<Integer, Flag<?>> mAllFlags;
    private final IStatusBarService mBarService;
    private final Map<Integer, Boolean> mBooleanFlagCache = new TreeMap();
    private final FlagManager mFlagManager;
    private final BroadcastReceiver mReceiver;
    private final Resources mResources;
    private final SecureSettings mSecureSettings;
    private final Map<Integer, String> mStringFlagCache = new TreeMap();
    private final SystemPropertiesHelper mSystemProperties;

    @Inject
    public FeatureFlagsDebug(FlagManager flagManager, Context context, SecureSettings secureSettings, SystemPropertiesHelper systemPropertiesHelper, @Main Resources resources, DumpManager dumpManager, @Named("all_flags") Map<Integer, Flag<?>> map, CommandRegistry commandRegistry, IStatusBarService iStatusBarService) {
        C21071 r4 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent == null ? null : intent.getAction();
                if (action != null) {
                    if (FlagManager.ACTION_SET_FLAG.equals(action)) {
                        handleSetFlag(intent.getExtras());
                    } else if (FlagManager.ACTION_GET_FLAGS.equals(action)) {
                        ArrayList arrayList = new ArrayList(FeatureFlagsDebug.this.mAllFlags.values());
                        ArrayList arrayList2 = new ArrayList();
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            ParcelableFlag<?> parcelableFlag = toParcelableFlag((Flag) it.next());
                            if (parcelableFlag != null) {
                                arrayList2.add(parcelableFlag);
                            }
                        }
                        Bundle resultExtras = getResultExtras(true);
                        if (resultExtras != null) {
                            resultExtras.putParcelableArrayList("flags", arrayList2);
                        }
                    }
                }
            }

            private void handleSetFlag(Bundle bundle) {
                Class<?> cls;
                if (bundle == null) {
                    Log.w(FeatureFlagsDebug.TAG, "No extras");
                    return;
                }
                int i = bundle.getInt("id");
                if (i <= 0) {
                    Log.w(FeatureFlagsDebug.TAG, "ID not set or less than  or equal to 0: " + i);
                } else if (!FeatureFlagsDebug.this.mAllFlags.containsKey(Integer.valueOf(i))) {
                    Log.w(FeatureFlagsDebug.TAG, "Tried to set unknown id: " + i);
                } else {
                    Flag flag = (Flag) FeatureFlagsDebug.this.mAllFlags.get(Integer.valueOf(i));
                    if (!bundle.containsKey("value")) {
                        FeatureFlagsDebug.this.eraseFlag(flag);
                        return;
                    }
                    Object obj = bundle.get("value");
                    try {
                        if (obj instanceof Boolean) {
                            FeatureFlagsDebug.this.setBooleanFlagInternal(flag, ((Boolean) obj).booleanValue());
                        } else if (obj instanceof String) {
                            FeatureFlagsDebug.this.setStringFlagInternal(flag, (String) obj);
                        } else {
                            throw new IllegalArgumentException("Unknown value type");
                        }
                    } catch (IllegalArgumentException unused) {
                        StringBuilder append = new StringBuilder("Unable to set ").append(flag.getId()).append(" of type ").append((Object) flag.getClass()).append(" to value of type ");
                        if (obj == null) {
                            cls = null;
                        } else {
                            cls = obj.getClass();
                        }
                        Log.w(FeatureFlagsDebug.TAG, append.append((Object) cls).toString());
                    }
                }
            }

            private ParcelableFlag<?> toParcelableFlag(Flag<?> flag) {
                if (flag instanceof BooleanFlag) {
                    return new BooleanFlag(flag.getId(), FeatureFlagsDebug.this.isEnabled((BooleanFlag) flag), flag.getTeamfood());
                }
                if (flag instanceof ResourceBooleanFlag) {
                    return new BooleanFlag(flag.getId(), FeatureFlagsDebug.this.isEnabled((ResourceBooleanFlag) flag), flag.getTeamfood());
                }
                if (flag instanceof SysPropBooleanFlag) {
                    return new BooleanFlag(flag.getId(), FeatureFlagsDebug.this.isEnabled((SysPropBooleanFlag) flag), false);
                }
                Log.w(FeatureFlagsDebug.TAG, "Unsupported Flag Type. Please file a bug.");
                return null;
            }
        };
        this.mReceiver = r4;
        this.mFlagManager = flagManager;
        this.mSecureSettings = secureSettings;
        this.mResources = resources;
        this.mSystemProperties = systemPropertiesHelper;
        this.mAllFlags = map;
        this.mBarService = iStatusBarService;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FlagManager.ACTION_SET_FLAG);
        intentFilter.addAction(FlagManager.ACTION_GET_FLAGS);
        flagManager.setOnSettingsChangedAction(new FeatureFlagsDebug$$ExternalSyntheticLambda0(this));
        flagManager.setClearCacheAction(new FeatureFlagsDebug$$ExternalSyntheticLambda1(this));
        context.registerReceiver(r4, intentFilter, (String) null, (Handler) null, 2);
        DumpManager dumpManager2 = dumpManager;
        dumpManager.registerDumpable(TAG, this);
        commandRegistry.registerCommand(FLAG_COMMAND, new FeatureFlagsDebug$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-flags-FeatureFlagsDebug  reason: not valid java name */
    public /* synthetic */ Command m2750lambda$new$0$comandroidsystemuiflagsFeatureFlagsDebug() {
        return new FlagCommand();
    }

    public boolean isEnabled(BooleanFlag booleanFlag) {
        int id = booleanFlag.getId();
        if (!this.mBooleanFlagCache.containsKey(Integer.valueOf(id))) {
            this.mBooleanFlagCache.put(Integer.valueOf(id), Boolean.valueOf(readFlagValue(id, booleanFlag.getDefault().booleanValue())));
        }
        return this.mBooleanFlagCache.get(Integer.valueOf(id)).booleanValue();
    }

    public boolean isEnabled(ResourceBooleanFlag resourceBooleanFlag) {
        int id = resourceBooleanFlag.getId();
        if (!this.mBooleanFlagCache.containsKey(Integer.valueOf(id))) {
            this.mBooleanFlagCache.put(Integer.valueOf(id), Boolean.valueOf(readFlagValue(id, this.mResources.getBoolean(resourceBooleanFlag.getResourceId()))));
        }
        return this.mBooleanFlagCache.get(Integer.valueOf(id)).booleanValue();
    }

    public boolean isEnabled(SysPropBooleanFlag sysPropBooleanFlag) {
        int id = sysPropBooleanFlag.getId();
        if (!this.mBooleanFlagCache.containsKey(Integer.valueOf(id))) {
            this.mBooleanFlagCache.put(Integer.valueOf(id), Boolean.valueOf(this.mSystemProperties.getBoolean(sysPropBooleanFlag.getName(), readFlagValue(id, sysPropBooleanFlag.getDefault().booleanValue()))));
        }
        return this.mBooleanFlagCache.get(Integer.valueOf(id)).booleanValue();
    }

    public String getString(StringFlag stringFlag) {
        int id = stringFlag.getId();
        if (!this.mStringFlagCache.containsKey(Integer.valueOf(id))) {
            this.mStringFlagCache.put(Integer.valueOf(id), (String) readFlagValue(id, stringFlag.getDefault(), StringFlagSerializer.INSTANCE));
        }
        return this.mStringFlagCache.get(Integer.valueOf(id));
    }

    public String getString(ResourceStringFlag resourceStringFlag) {
        int id = resourceStringFlag.getId();
        if (!this.mStringFlagCache.containsKey(Integer.valueOf(id))) {
            this.mStringFlagCache.put(Integer.valueOf(id), (String) readFlagValue(id, this.mResources.getString(resourceStringFlag.getResourceId()), StringFlagSerializer.INSTANCE));
        }
        return this.mStringFlagCache.get(Integer.valueOf(id));
    }

    private boolean readFlagValue(int i, boolean z) {
        Boolean bool = (Boolean) readFlagValueInternal(i, BooleanFlagSerializer.INSTANCE);
        if (z || bool != null || i == Flags.TEAMFOOD.getId() || !this.mAllFlags.containsKey(Integer.valueOf(i)) || !this.mAllFlags.get(Integer.valueOf(i)).getTeamfood()) {
            return bool == null ? z : bool.booleanValue();
        }
        return isEnabled(Flags.TEAMFOOD);
    }

    private <T> T readFlagValue(int i, T t, FlagSerializer<T> flagSerializer) {
        Objects.requireNonNull(t, "defaultValue");
        T readFlagValueInternal = readFlagValueInternal(i, flagSerializer);
        return readFlagValueInternal == null ? t : readFlagValueInternal;
    }

    private <T> T readFlagValueInternal(int i, FlagSerializer<T> flagSerializer) {
        try {
            return this.mFlagManager.readFlagValue(i, flagSerializer);
        } catch (Exception unused) {
            eraseInternal(i);
            return null;
        }
    }

    private <T> void setFlagValue(int i, T t, FlagSerializer<T> flagSerializer) {
        Objects.requireNonNull(t, "Cannot set a null value");
        if (Objects.equals(readFlagValueInternal(i, flagSerializer), t)) {
            Log.i(TAG, "Flag id " + i + " is already " + t);
            return;
        }
        String settingsData = flagSerializer.toSettingsData(t);
        if (settingsData == null) {
            Log.w(TAG, "Failed to set id " + i + " to " + t);
            return;
        }
        this.mSecureSettings.putStringForUser(this.mFlagManager.idToSettingsKey(i), settingsData, -2);
        Log.i(TAG, "Set id " + i + " to " + t);
        removeFromCache(i);
        this.mFlagManager.dispatchListenersAndMaybeRestart(i, new FeatureFlagsDebug$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public <T> void eraseFlag(Flag<T> flag) {
        if (flag instanceof SysPropFlag) {
            this.mSystemProperties.erase(((SysPropFlag) flag).getName());
            dispatchListenersAndMaybeRestart(flag.getId(), new FeatureFlagsDebug$$ExternalSyntheticLambda3(this));
            return;
        }
        eraseFlag(flag.getId());
    }

    private void eraseFlag(int i) {
        eraseInternal(i);
        removeFromCache(i);
        dispatchListenersAndMaybeRestart(i, new FeatureFlagsDebug$$ExternalSyntheticLambda0(this));
    }

    private void dispatchListenersAndMaybeRestart(int i, Consumer<Boolean> consumer) {
        this.mFlagManager.dispatchListenersAndMaybeRestart(i, consumer);
    }

    private void eraseInternal(int i) {
        this.mSecureSettings.putStringForUser(this.mFlagManager.idToSettingsKey(i), "", -2);
        Log.i(TAG, "Erase id " + i);
    }

    public void addListener(Flag<?> flag, FlagListenable.Listener listener) {
        this.mFlagManager.addListener(flag, listener);
    }

    public void removeListener(FlagListenable.Listener listener) {
        this.mFlagManager.removeListener(listener);
    }

    /* access modifiers changed from: private */
    public void restartSystemUI(boolean z) {
        if (z) {
            Log.i(TAG, "SystemUI Restart Suppressed");
            return;
        }
        Log.i(TAG, "Restarting SystemUI");
        System.exit(0);
    }

    /* access modifiers changed from: private */
    public void restartAndroid(boolean z) {
        if (z) {
            Log.i(TAG, "Android Restart Suppressed");
            return;
        }
        Log.i(TAG, "Restarting Android");
        try {
            this.mBarService.restart();
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void setBooleanFlagInternal(Flag<?> flag, boolean z) {
        if (flag instanceof BooleanFlag) {
            setFlagValue(flag.getId(), Boolean.valueOf(z), BooleanFlagSerializer.INSTANCE);
        } else if (flag instanceof ResourceBooleanFlag) {
            setFlagValue(flag.getId(), Boolean.valueOf(z), BooleanFlagSerializer.INSTANCE);
        } else if (flag instanceof SysPropBooleanFlag) {
            this.mSystemProperties.setBoolean(((SysPropBooleanFlag) flag).getName(), z);
            dispatchListenersAndMaybeRestart(flag.getId(), new FeatureFlagsDebug$$ExternalSyntheticLambda3(this));
        } else {
            throw new IllegalArgumentException("Unknown flag type");
        }
    }

    /* access modifiers changed from: private */
    public void setStringFlagInternal(Flag<?> flag, String str) {
        if (flag instanceof StringFlag) {
            setFlagValue(flag.getId(), str, StringFlagSerializer.INSTANCE);
        } else if (flag instanceof ResourceStringFlag) {
            setFlagValue(flag.getId(), str, StringFlagSerializer.INSTANCE);
        } else {
            throw new IllegalArgumentException("Unknown flag type");
        }
    }

    /* access modifiers changed from: private */
    public void removeFromCache(int i) {
        this.mBooleanFlagCache.remove(Integer.valueOf(i));
        this.mStringFlagCache.remove(Integer.valueOf(i));
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("can override: true");
        printWriter.println("booleans: " + this.mBooleanFlagCache.size());
        this.mBooleanFlagCache.forEach(new FeatureFlagsDebug$$ExternalSyntheticLambda4(printWriter));
        printWriter.println("Strings: " + this.mStringFlagCache.size());
        this.mStringFlagCache.forEach(new FeatureFlagsDebug$$ExternalSyntheticLambda5(printWriter));
    }

    class FlagCommand implements Command {
        private final List<String> mOffCommands = List.m1726of("false", "off", "0", "disable");
        private final List<String> mOnCommands = List.m1726of("true", DebugKt.DEBUG_PROPERTY_VALUE_ON, "1", "enabled");

        FlagCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            int i;
            boolean z;
            if (list.size() == 0) {
                printWriter.println("Error: no flag id supplied");
                help(printWriter);
                printWriter.println();
                printKnownFlags(printWriter);
            } else if (list.size() > 2) {
                printWriter.println("Invalid number of arguments.");
                help(printWriter);
            } else {
                try {
                    i = Integer.parseInt(list.get(0));
                    if (!FeatureFlagsDebug.this.mAllFlags.containsKey(Integer.valueOf(i))) {
                        printWriter.println("Unknown flag id: " + i);
                        printWriter.println();
                        printKnownFlags(printWriter);
                        return;
                    }
                } catch (NumberFormatException unused) {
                    i = flagNameToId(list.get(0));
                    if (i == 0) {
                        printWriter.println("Invalid flag. Must an integer id or flag name: " + list.get(0));
                        return;
                    }
                }
                Flag flag = (Flag) FeatureFlagsDebug.this.mAllFlags.get(Integer.valueOf(i));
                String lowerCase = list.size() == 2 ? list.get(1).toLowerCase() : "";
                if ("erase".equals(lowerCase) || "reset".equals(lowerCase)) {
                    FeatureFlagsDebug.this.eraseFlag(flag);
                    return;
                }
                if (list.size() == 1 || "toggle".equals(lowerCase)) {
                    boolean isBooleanFlagEnabled = isBooleanFlagEnabled(flag);
                    if (list.size() == 1) {
                        printWriter.println("Flag " + i + " is " + isBooleanFlagEnabled);
                        return;
                    }
                    z = !isBooleanFlagEnabled;
                } else {
                    z = this.mOnCommands.contains(lowerCase);
                    if (!z && !this.mOffCommands.contains(lowerCase)) {
                        printWriter.println("Invalid on/off argument supplied");
                        help(printWriter);
                        return;
                    }
                }
                printWriter.flush();
                FeatureFlagsDebug.this.setBooleanFlagInternal(flag, z);
            }
        }

        public void help(PrintWriter printWriter) {
            printWriter.println("Usage: adb shell cmd statusbar flag <id> [true|false|1|0|on|off|enable|disable|toggle|erase|reset]");
            printWriter.println("The id can either be a numeric integer or the corresponding field name");
            printWriter.println("If no argument is supplied after the id, the flags runtime value is output");
        }

        private boolean isBooleanFlagEnabled(Flag<?> flag) {
            if (flag instanceof BooleanFlag) {
                return FeatureFlagsDebug.this.isEnabled((BooleanFlag) flag);
            }
            if (flag instanceof ResourceBooleanFlag) {
                return FeatureFlagsDebug.this.isEnabled((ResourceBooleanFlag) flag);
            }
            if (flag instanceof SysPropFlag) {
                return FeatureFlagsDebug.this.isEnabled((SysPropBooleanFlag) flag);
            }
            return false;
        }

        private int flagNameToId(String str) {
            for (Field next : Flags.getFlagFields()) {
                if (str.equals(next.getName())) {
                    return fieldToId(next);
                }
            }
            return 0;
        }

        private int fieldToId(Field field) {
            try {
                return ((Flag) field.get((Object) null)).getId();
            } catch (IllegalAccessException unused) {
                return 0;
            }
        }

        private void printKnownFlags(PrintWriter printWriter) {
            List<Field> flagFields = Flags.getFlagFields();
            int i = 0;
            for (Field name : flagFields) {
                i = Math.max(i, name.getName().length());
            }
            printWriter.println("Known Flags:");
            printWriter.print("Flag Name");
            for (int i2 = 0; i2 < (i - 9) + 1; i2++) {
                printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            printWriter.println("ID   Enabled?");
            for (int i3 = 0; i3 < i; i3++) {
                printWriter.print("=");
            }
            printWriter.println(" ==== ========");
            for (Field next : flagFields) {
                int fieldToId = fieldToId(next);
                if (fieldToId != 0 && FeatureFlagsDebug.this.mAllFlags.containsKey(Integer.valueOf(fieldToId))) {
                    printWriter.print(next.getName());
                    int length = next.getName().length();
                    for (int i4 = 0; i4 < (i - length) + 1; i4++) {
                        printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    }
                    printWriter.printf("%-4d ", Integer.valueOf(fieldToId));
                    printWriter.println(isBooleanFlagEnabled((Flag) FeatureFlagsDebug.this.mAllFlags.get(Integer.valueOf(fieldToId))));
                }
            }
        }
    }
}
