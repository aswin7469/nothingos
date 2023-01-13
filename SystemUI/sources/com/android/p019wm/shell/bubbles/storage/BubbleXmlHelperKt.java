package com.android.p019wm.shell.bubbles.storage;

import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.XmlUtils;
import java.nio.charset.StandardCharsets;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlSerializer;

@Metadata(mo65042d1 = {"\u0000B\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00102\u0006\u0010\u0013\u001a\u00020\u0014\u001a\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0002\u001a\"\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010\u001a\u0018\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0012H\u0002\u001a\u0016\u0010 \u001a\u0004\u0018\u00010\u0001*\u00020\u00172\u0006\u0010!\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\fXT¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"ATTR_DESIRED_HEIGHT", "", "ATTR_DESIRED_HEIGHT_RES_ID", "ATTR_KEY", "ATTR_LOCUS", "ATTR_PACKAGE", "ATTR_SHORTCUT_ID", "ATTR_TASK_ID", "ATTR_TITLE", "ATTR_USER_ID", "ATTR_VERSION", "CURRENT_VERSION", "", "TAG_BUBBLE", "TAG_BUBBLES", "readXml", "Landroid/util/SparseArray;", "", "Lcom/android/wm/shell/bubbles/storage/BubbleEntity;", "stream", "Ljava/io/InputStream;", "readXmlEntry", "parser", "Lorg/xmlpull/v1/XmlPullParser;", "writeXml", "", "Ljava/io/OutputStream;", "bubbles", "writeXmlEntry", "serializer", "Lorg/xmlpull/v1/XmlSerializer;", "bubble", "getAttributeWithName", "name", "WMShell_release"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.storage.BubbleXmlHelperKt */
/* compiled from: BubbleXmlHelper.kt */
public final class BubbleXmlHelperKt {
    private static final String ATTR_DESIRED_HEIGHT = "h";
    private static final String ATTR_DESIRED_HEIGHT_RES_ID = "hid";
    private static final String ATTR_KEY = "key";
    private static final String ATTR_LOCUS = "l";
    private static final String ATTR_PACKAGE = "pkg";
    private static final String ATTR_SHORTCUT_ID = "sid";
    private static final String ATTR_TASK_ID = "tid";
    private static final String ATTR_TITLE = "t";
    private static final String ATTR_USER_ID = "uid";
    private static final String ATTR_VERSION = "v";
    private static final int CURRENT_VERSION = 2;
    private static final String TAG_BUBBLE = "bb";
    private static final String TAG_BUBBLES = "bs";

    public static final void writeXml(OutputStream outputStream, SparseArray<List<BubbleEntity>> sparseArray) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream, "stream");
        Intrinsics.checkNotNullParameter(sparseArray, "bubbles");
        XmlSerializer fastXmlSerializer = new FastXmlSerializer();
        fastXmlSerializer.setOutput(outputStream, StandardCharsets.UTF_8.name());
        fastXmlSerializer.startDocument((String) null, true);
        fastXmlSerializer.startTag((String) null, TAG_BUBBLES);
        fastXmlSerializer.attribute((String) null, "v", "2");
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            int keyAt = sparseArray.keyAt(i);
            List<BubbleEntity> valueAt = sparseArray.valueAt(i);
            fastXmlSerializer.startTag((String) null, TAG_BUBBLES);
            fastXmlSerializer.attribute((String) null, "uid", String.valueOf(keyAt));
            Intrinsics.checkNotNullExpressionValue(valueAt, "v");
            for (BubbleEntity writeXmlEntry : valueAt) {
                writeXmlEntry(fastXmlSerializer, writeXmlEntry);
            }
            fastXmlSerializer.endTag((String) null, TAG_BUBBLES);
        }
        fastXmlSerializer.endTag((String) null, TAG_BUBBLES);
        fastXmlSerializer.endDocument();
    }

    private static final void writeXmlEntry(XmlSerializer xmlSerializer, BubbleEntity bubbleEntity) {
        try {
            xmlSerializer.startTag((String) null, TAG_BUBBLE);
            xmlSerializer.attribute((String) null, "uid", String.valueOf(bubbleEntity.getUserId()));
            xmlSerializer.attribute((String) null, "pkg", bubbleEntity.getPackageName());
            xmlSerializer.attribute((String) null, ATTR_SHORTCUT_ID, bubbleEntity.getShortcutId());
            xmlSerializer.attribute((String) null, "key", bubbleEntity.getKey());
            xmlSerializer.attribute((String) null, ATTR_DESIRED_HEIGHT, String.valueOf(bubbleEntity.getDesiredHeight()));
            xmlSerializer.attribute((String) null, ATTR_DESIRED_HEIGHT_RES_ID, String.valueOf(bubbleEntity.getDesiredHeightResId()));
            String title = bubbleEntity.getTitle();
            if (title != null) {
                xmlSerializer.attribute((String) null, ATTR_TITLE, title);
            }
            xmlSerializer.attribute((String) null, ATTR_TASK_ID, String.valueOf(bubbleEntity.getTaskId()));
            String locus = bubbleEntity.getLocus();
            if (locus != null) {
                xmlSerializer.attribute((String) null, ATTR_LOCUS, locus);
            }
            xmlSerializer.endTag((String) null, TAG_BUBBLE);
        } catch (IOException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static final SparseArray<List<BubbleEntity>> readXml(InputStream inputStream) {
        Intrinsics.checkNotNullParameter(inputStream, "stream");
        SparseArray<List<BubbleEntity>> sparseArray = new SparseArray<>();
        XmlPullParser newPullParser = Xml.newPullParser();
        Intrinsics.checkNotNullExpressionValue(newPullParser, "newPullParser()");
        newPullParser.setInput(inputStream, StandardCharsets.UTF_8.name());
        XmlUtils.beginDocument(newPullParser, TAG_BUBBLES);
        int depth = newPullParser.getDepth();
        String attributeWithName = getAttributeWithName(newPullParser, "v");
        if (attributeWithName != null) {
            int parseInt = Integer.parseInt(attributeWithName);
            if (parseInt == 1) {
                int depth2 = newPullParser.getDepth();
                List arrayList = new ArrayList();
                while (XmlUtils.nextElementWithin(newPullParser, depth2)) {
                    BubbleEntity readXmlEntry = readXmlEntry(newPullParser);
                    if (readXmlEntry != null && readXmlEntry.getUserId() == 0) {
                        arrayList.add(readXmlEntry);
                    }
                }
                if (!arrayList.isEmpty()) {
                    sparseArray.put(0, CollectionsKt.toList(arrayList));
                }
            } else if (parseInt == 2) {
                while (XmlUtils.nextElementWithin(newPullParser, depth)) {
                    String attributeWithName2 = getAttributeWithName(newPullParser, "uid");
                    if (attributeWithName2 != null) {
                        int depth3 = newPullParser.getDepth();
                        List arrayList2 = new ArrayList();
                        while (XmlUtils.nextElementWithin(newPullParser, depth3)) {
                            BubbleEntity readXmlEntry2 = readXmlEntry(newPullParser);
                            if (readXmlEntry2 != null) {
                                arrayList2.add(readXmlEntry2);
                            }
                        }
                        if (!arrayList2.isEmpty()) {
                            sparseArray.put(Integer.parseInt(attributeWithName2), CollectionsKt.toList(arrayList2));
                        }
                    }
                }
            }
        }
        return sparseArray;
    }

    private static final BubbleEntity readXmlEntry(XmlPullParser xmlPullParser) {
        String attributeWithName;
        String attributeWithName2;
        String attributeWithName3;
        while (xmlPullParser.getEventType() != 2) {
            xmlPullParser.next();
        }
        String attributeWithName4 = getAttributeWithName(xmlPullParser, "uid");
        if (attributeWithName4 != null) {
            int parseInt = Integer.parseInt(attributeWithName4);
            String attributeWithName5 = getAttributeWithName(xmlPullParser, "pkg");
            if (!(attributeWithName5 == null || (attributeWithName = getAttributeWithName(xmlPullParser, ATTR_SHORTCUT_ID)) == null || (attributeWithName2 = getAttributeWithName(xmlPullParser, "key")) == null || (attributeWithName3 = getAttributeWithName(xmlPullParser, ATTR_DESIRED_HEIGHT)) == null)) {
                int parseInt2 = Integer.parseInt(attributeWithName3);
                String attributeWithName6 = getAttributeWithName(xmlPullParser, ATTR_DESIRED_HEIGHT_RES_ID);
                if (attributeWithName6 != null) {
                    int parseInt3 = Integer.parseInt(attributeWithName6);
                    String attributeWithName7 = getAttributeWithName(xmlPullParser, ATTR_TITLE);
                    String attributeWithName8 = getAttributeWithName(xmlPullParser, ATTR_TASK_ID);
                    return new BubbleEntity(parseInt, attributeWithName5, attributeWithName, attributeWithName2, parseInt2, parseInt3, attributeWithName7, attributeWithName8 != null ? Integer.parseInt(attributeWithName8) : -1, getAttributeWithName(xmlPullParser, ATTR_LOCUS));
                }
            }
        }
        return null;
    }

    private static final String getAttributeWithName(XmlPullParser xmlPullParser, String str) {
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            if (Intrinsics.areEqual((Object) xmlPullParser.getAttributeName(i), (Object) str)) {
                return xmlPullParser.getAttributeValue(i);
            }
        }
        return null;
    }
}
