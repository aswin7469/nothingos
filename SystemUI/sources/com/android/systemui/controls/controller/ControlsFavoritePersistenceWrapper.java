package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import com.android.systemui.backup.BackupHelper;
import java.p026io.BufferedInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import libcore.p030io.IoUtils;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;
import org.xmlpull.p032v1.XmlSerializer;

@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00032\b\u0010\u0010\u001a\u0004\u0018\u00010\u0007J\u0006\u0010\u0011\u001a\u00020\u000eJ\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013J\u0014\u0010\u0018\u001a\u00020\u000e2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper;", "", "file", "Ljava/io/File;", "executor", "Ljava/util/concurrent/Executor;", "backupManager", "Landroid/app/backup/BackupManager;", "(Ljava/io/File;Ljava/util/concurrent/Executor;Landroid/app/backup/BackupManager;)V", "fileExists", "", "getFileExists", "()Z", "changeFileAndBackupManager", "", "fileName", "newBackupManager", "deleteFile", "parseXml", "", "Lcom/android/systemui/controls/controller/StructureInfo;", "parser", "Lorg/xmlpull/v1/XmlPullParser;", "readFavorites", "storeFavorites", "structures", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsFavoritePersistenceWrapper.kt */
public final class ControlsFavoritePersistenceWrapper {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String FILE_NAME = "controls_favorites.xml";
    private static final String TAG = "ControlsFavoritePersistenceWrapper";
    private static final String TAG_COMPONENT = "component";
    private static final String TAG_CONTROL = "control";
    private static final String TAG_CONTROLS = "controls";
    private static final String TAG_ID = "id";
    private static final String TAG_STRUCTURE = "structure";
    private static final String TAG_STRUCTURES = "structures";
    private static final String TAG_SUBTITLE = "subtitle";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TYPE = "type";
    private static final String TAG_VERSION = "version";
    private static final int VERSION = 1;
    private BackupManager backupManager;
    private final Executor executor;
    private File file;

    public ControlsFavoritePersistenceWrapper(File file2, Executor executor2, BackupManager backupManager2) {
        Intrinsics.checkNotNullParameter(file2, "file");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        this.file = file2;
        this.executor = executor2;
        this.backupManager = backupManager2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ControlsFavoritePersistenceWrapper(File file2, Executor executor2, BackupManager backupManager2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(file2, executor2, (i & 4) != 0 ? null : backupManager2);
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper$Companion;", "", "()V", "FILE_NAME", "", "TAG", "TAG_COMPONENT", "TAG_CONTROL", "TAG_CONTROLS", "TAG_ID", "TAG_STRUCTURE", "TAG_STRUCTURES", "TAG_SUBTITLE", "TAG_TITLE", "TAG_TYPE", "TAG_VERSION", "VERSION", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsFavoritePersistenceWrapper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void changeFileAndBackupManager(File file2, BackupManager backupManager2) {
        Intrinsics.checkNotNullParameter(file2, "fileName");
        this.file = file2;
        this.backupManager = backupManager2;
    }

    public final boolean getFileExists() {
        return this.file.exists();
    }

    public final void deleteFile() {
        this.file.delete();
    }

    public final void storeFavorites(List<StructureInfo> list) {
        Intrinsics.checkNotNullParameter(list, TAG_STRUCTURES);
        if (!list.isEmpty() || this.file.exists()) {
            this.executor.execute(new ControlsFavoritePersistenceWrapper$$ExternalSyntheticLambda0(this, list));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: storeFavorites$lambda-4  reason: not valid java name */
    public static final void m2625storeFavorites$lambda4(ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper, List list) {
        boolean z;
        BackupManager backupManager2;
        Intrinsics.checkNotNullParameter(controlsFavoritePersistenceWrapper, "this$0");
        Intrinsics.checkNotNullParameter(list, "$structures");
        Log.d(TAG, "Saving data to file: " + controlsFavoritePersistenceWrapper.file);
        AtomicFile atomicFile = new AtomicFile(controlsFavoritePersistenceWrapper.file);
        synchronized (BackupHelper.Companion.getControlsDataLock()) {
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    XmlSerializer newSerializer = Xml.newSerializer();
                    newSerializer.setOutput(startWrite, "utf-8");
                    z = true;
                    newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    newSerializer.startDocument((String) null, true);
                    newSerializer.startTag((String) null, "version");
                    newSerializer.text("1");
                    newSerializer.endTag((String) null, "version");
                    newSerializer.startTag((String) null, TAG_STRUCTURES);
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        StructureInfo structureInfo = (StructureInfo) it.next();
                        newSerializer.startTag((String) null, TAG_STRUCTURE);
                        newSerializer.attribute((String) null, TAG_COMPONENT, structureInfo.getComponentName().flattenToString());
                        newSerializer.attribute((String) null, TAG_STRUCTURE, structureInfo.getStructure().toString());
                        newSerializer.startTag((String) null, "controls");
                        for (ControlInfo controlInfo : structureInfo.getControls()) {
                            newSerializer.startTag((String) null, TAG_CONTROL);
                            newSerializer.attribute((String) null, "id", controlInfo.getControlId());
                            newSerializer.attribute((String) null, TAG_TITLE, controlInfo.getControlTitle().toString());
                            newSerializer.attribute((String) null, TAG_SUBTITLE, controlInfo.getControlSubtitle().toString());
                            newSerializer.attribute((String) null, TAG_TYPE, String.valueOf(controlInfo.getDeviceType()));
                            newSerializer.endTag((String) null, TAG_CONTROL);
                        }
                        newSerializer.endTag((String) null, "controls");
                        newSerializer.endTag((String) null, TAG_STRUCTURE);
                    }
                    newSerializer.endTag((String) null, TAG_STRUCTURES);
                    newSerializer.endDocument();
                    atomicFile.finishWrite(startWrite);
                    IoUtils.closeQuietly((AutoCloseable) startWrite);
                } catch (Throwable th) {
                    throw th;
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to start write file", e);
                return;
            }
        }
        if (z && (backupManager2 = controlsFavoritePersistenceWrapper.backupManager) != null) {
            backupManager2.dataChanged();
        }
    }

    public final List<StructureInfo> readFavorites() {
        List<StructureInfo> parseXml;
        if (!this.file.exists()) {
            Log.d(TAG, "No favorites, returning empty list");
            return CollectionsKt.emptyList();
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
            try {
                Log.d(TAG, "Reading data from file: " + this.file);
                synchronized (BackupHelper.Companion.getControlsDataLock()) {
                    XmlPullParser newPullParser = Xml.newPullParser();
                    newPullParser.setInput(bufferedInputStream, (String) null);
                    Intrinsics.checkNotNullExpressionValue(newPullParser, "parser");
                    parseXml = parseXml(newPullParser);
                }
                IoUtils.closeQuietly((AutoCloseable) bufferedInputStream);
                return parseXml;
            } catch (XmlPullParserException e) {
                throw new IllegalStateException("Failed parsing favorites file: " + this.file, e);
            } catch (IOException e2) {
                try {
                    throw new IllegalStateException("Failed parsing favorites file: " + this.file, e2);
                } catch (Throwable th) {
                    IoUtils.closeQuietly((AutoCloseable) bufferedInputStream);
                    throw th;
                }
            }
        } catch (FileNotFoundException unused) {
            Log.i(TAG, "No file found");
            return CollectionsKt.emptyList();
        }
    }

    private final List<StructureInfo> parseXml(XmlPullParser xmlPullParser) {
        List<StructureInfo> arrayList = new ArrayList<>();
        List arrayList2 = new ArrayList();
        ComponentName componentName = null;
        CharSequence charSequence = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return arrayList;
            }
            String name = xmlPullParser.getName();
            String str = "";
            if (name == null) {
                name = str;
            }
            if (next == 2 && Intrinsics.areEqual((Object) name, (Object) TAG_STRUCTURE)) {
                componentName = ComponentName.unflattenFromString(xmlPullParser.getAttributeValue((String) null, TAG_COMPONENT));
                String attributeValue = xmlPullParser.getAttributeValue((String) null, TAG_STRUCTURE);
                charSequence = attributeValue != null ? attributeValue : str;
            } else if (next == 2 && Intrinsics.areEqual((Object) name, (Object) TAG_CONTROL)) {
                String attributeValue2 = xmlPullParser.getAttributeValue((String) null, "id");
                String attributeValue3 = xmlPullParser.getAttributeValue((String) null, TAG_TITLE);
                String attributeValue4 = xmlPullParser.getAttributeValue((String) null, TAG_SUBTITLE);
                if (attributeValue4 != null) {
                    str = attributeValue4;
                }
                String attributeValue5 = xmlPullParser.getAttributeValue((String) null, TAG_TYPE);
                Integer valueOf = attributeValue5 != null ? Integer.valueOf(Integer.parseInt(attributeValue5)) : null;
                if (!(attributeValue2 == null || attributeValue3 == null || valueOf == null)) {
                    arrayList2.add(new ControlInfo(attributeValue2, attributeValue3, str, valueOf.intValue()));
                }
            } else if (next == 3 && Intrinsics.areEqual((Object) name, (Object) TAG_STRUCTURE)) {
                Intrinsics.checkNotNull(componentName);
                Intrinsics.checkNotNull(charSequence);
                arrayList.add(new StructureInfo(componentName, charSequence, CollectionsKt.toList(arrayList2)));
                arrayList2.clear();
            }
        }
    }
}
