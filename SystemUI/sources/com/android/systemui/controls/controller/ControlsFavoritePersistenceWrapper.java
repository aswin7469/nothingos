package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import com.android.systemui.backup.BackupHelper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import libcore.io.IoUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* compiled from: ControlsFavoritePersistenceWrapper.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritePersistenceWrapper {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private BackupManager backupManager;
    @NotNull
    private final Executor executor;
    @NotNull
    private File file;

    public ControlsFavoritePersistenceWrapper(@NotNull File file, @NotNull Executor executor, @Nullable BackupManager backupManager) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.file = file;
        this.executor = executor;
        this.backupManager = backupManager;
    }

    public /* synthetic */ ControlsFavoritePersistenceWrapper(File file, Executor executor, BackupManager backupManager, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(file, executor, (i & 4) != 0 ? null : backupManager);
    }

    /* compiled from: ControlsFavoritePersistenceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void changeFileAndBackupManager(@NotNull File fileName, @Nullable BackupManager backupManager) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        this.file = fileName;
        this.backupManager = backupManager;
    }

    public final boolean getFileExists() {
        return this.file.exists();
    }

    public final void deleteFile() {
        this.file.delete();
    }

    public final void storeFavorites(@NotNull final List<StructureInfo> structures) {
        Intrinsics.checkNotNullParameter(structures, "structures");
        if (!structures.isEmpty() || this.file.exists()) {
            this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper$storeFavorites$1
                /* JADX WARN: Code restructure failed: missing block: B:21:0x010a, code lost:
                    r11 = r11.this$0.backupManager;
                 */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public final void run() {
                    File file;
                    File file2;
                    boolean z;
                    BackupManager backupManager;
                    file = ControlsFavoritePersistenceWrapper.this.file;
                    Log.d("ControlsFavoritePersistenceWrapper", Intrinsics.stringPlus("Saving data to file: ", file));
                    file2 = ControlsFavoritePersistenceWrapper.this.file;
                    AtomicFile atomicFile = new AtomicFile(file2);
                    Object controlsDataLock = BackupHelper.Companion.getControlsDataLock();
                    List<StructureInfo> list = structures;
                    synchronized (controlsDataLock) {
                        try {
                            try {
                                FileOutputStream startWrite = atomicFile.startWrite();
                                z = true;
                                try {
                                    XmlSerializer newSerializer = Xml.newSerializer();
                                    newSerializer.setOutput(startWrite, "utf-8");
                                    newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                                    newSerializer.startDocument(null, Boolean.TRUE);
                                    newSerializer.startTag(null, "version");
                                    newSerializer.text("1");
                                    newSerializer.endTag(null, "version");
                                    newSerializer.startTag(null, "structures");
                                    for (StructureInfo structureInfo : list) {
                                        newSerializer.startTag(null, "structure");
                                        newSerializer.attribute(null, "component", structureInfo.getComponentName().flattenToString());
                                        newSerializer.attribute(null, "structure", structureInfo.getStructure().toString());
                                        newSerializer.startTag(null, "controls");
                                        for (ControlInfo controlInfo : structureInfo.getControls()) {
                                            newSerializer.startTag(null, "control");
                                            newSerializer.attribute(null, "id", controlInfo.getControlId());
                                            newSerializer.attribute(null, "title", controlInfo.getControlTitle().toString());
                                            newSerializer.attribute(null, "subtitle", controlInfo.getControlSubtitle().toString());
                                            newSerializer.attribute(null, "type", String.valueOf(controlInfo.getDeviceType()));
                                            newSerializer.endTag(null, "control");
                                        }
                                        newSerializer.endTag(null, "controls");
                                        newSerializer.endTag(null, "structure");
                                    }
                                    newSerializer.endTag(null, "structures");
                                    newSerializer.endDocument();
                                    atomicFile.finishWrite(startWrite);
                                } catch (Throwable unused) {
                                    Log.e("ControlsFavoritePersistenceWrapper", "Failed to write file, reverting to previous version");
                                    atomicFile.failWrite(startWrite);
                                    z = false;
                                }
                                IoUtils.closeQuietly(startWrite);
                            } catch (IOException e) {
                                Log.e("ControlsFavoritePersistenceWrapper", "Failed to start write file", e);
                                return;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    if (!z || backupManager == null) {
                        return;
                    }
                    backupManager.dataChanged();
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.io.BufferedInputStream, java.lang.AutoCloseable, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.Object, org.xmlpull.v1.XmlPullParser] */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper] */
    @NotNull
    public final List<StructureInfo> readFavorites() {
        List<StructureInfo> emptyList;
        List<StructureInfo> parseXml;
        List<StructureInfo> emptyList2;
        ?? exists = this.file.exists();
        if (exists == 0) {
            Log.d("ControlsFavoritePersistenceWrapper", "No favorites, returning empty list");
            emptyList2 = CollectionsKt__CollectionsKt.emptyList();
            return emptyList2;
        }
        try {
            try {
                exists = new BufferedInputStream(new FileInputStream(this.file));
                try {
                    Log.d("ControlsFavoritePersistenceWrapper", Intrinsics.stringPlus("Reading data from file: ", this.file));
                    synchronized (BackupHelper.Companion.getControlsDataLock()) {
                        ?? parser = Xml.newPullParser();
                        parser.setInput(exists, null);
                        Intrinsics.checkNotNullExpressionValue(parser, "parser");
                        parseXml = parseXml(parser);
                    }
                    return parseXml;
                } catch (IOException e) {
                    throw new IllegalStateException(Intrinsics.stringPlus("Failed parsing favorites file: ", this.file), e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalStateException(Intrinsics.stringPlus("Failed parsing favorites file: ", this.file), e2);
                }
            } catch (FileNotFoundException unused) {
                Log.i("ControlsFavoritePersistenceWrapper", "No file found");
                emptyList = CollectionsKt__CollectionsKt.emptyList();
                return emptyList;
            }
        } finally {
            IoUtils.closeQuietly((AutoCloseable) exists);
        }
    }

    private final List<StructureInfo> parseXml(XmlPullParser xmlPullParser) {
        List list;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ComponentName componentName = null;
        String str = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next != 1) {
                String name = xmlPullParser.getName();
                String str2 = "";
                if (name == null) {
                    name = str2;
                }
                if (next == 2 && Intrinsics.areEqual(name, "structure")) {
                    componentName = ComponentName.unflattenFromString(xmlPullParser.getAttributeValue(null, "component"));
                    str = xmlPullParser.getAttributeValue(null, "structure");
                    if (str == null) {
                        str = str2;
                    }
                } else if (next == 2 && Intrinsics.areEqual(name, "control")) {
                    String attributeValue = xmlPullParser.getAttributeValue(null, "id");
                    String attributeValue2 = xmlPullParser.getAttributeValue(null, "title");
                    String attributeValue3 = xmlPullParser.getAttributeValue(null, "subtitle");
                    if (attributeValue3 != null) {
                        str2 = attributeValue3;
                    }
                    String attributeValue4 = xmlPullParser.getAttributeValue(null, "type");
                    Integer valueOf = attributeValue4 == null ? null : Integer.valueOf(Integer.parseInt(attributeValue4));
                    if (attributeValue != null && attributeValue2 != null && valueOf != null) {
                        arrayList2.add(new ControlInfo(attributeValue, attributeValue2, str2, valueOf.intValue()));
                    }
                } else if (next == 3 && Intrinsics.areEqual(name, "structure")) {
                    Intrinsics.checkNotNull(componentName);
                    Intrinsics.checkNotNull(str);
                    list = CollectionsKt___CollectionsKt.toList(arrayList2);
                    arrayList.add(new StructureInfo(componentName, str, list));
                    arrayList2.clear();
                }
            } else {
                return arrayList;
            }
        }
    }
}
