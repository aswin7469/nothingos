package com.android.settingslib.license;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
class LicenseHtmlGeneratorFromXml {
    private final List<File> mXmlFiles;
    private final Map<String, Set<String>> mFileNameToContentIdMap = new HashMap();
    private final Map<String, String> mContentIdToFileContentMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ContentIdAndFileNames {
        final String mContentId;
        final List<String> mFileNameList = new ArrayList();

        ContentIdAndFileNames(String str) {
            this.mContentId = str;
        }
    }

    private LicenseHtmlGeneratorFromXml(List<File> list) {
        this.mXmlFiles = list;
    }

    public static boolean generateHtml(List<File> list, File file, String str) {
        return new LicenseHtmlGeneratorFromXml(list).generateHtml(file, str);
    }

    private boolean generateHtml(File file, String str) {
        PrintWriter printWriter;
        for (File file2 : this.mXmlFiles) {
            parse(file2);
        }
        if (!this.mFileNameToContentIdMap.isEmpty() && !this.mContentIdToFileContentMap.isEmpty()) {
            PrintWriter printWriter2 = null;
            try {
                printWriter = new PrintWriter(file);
            } catch (FileNotFoundException | SecurityException e) {
                e = e;
            }
            try {
                generateHtml(this.mFileNameToContentIdMap, this.mContentIdToFileContentMap, printWriter, str);
                printWriter.flush();
                printWriter.close();
                return true;
            } catch (FileNotFoundException | SecurityException e2) {
                e = e2;
                printWriter2 = printWriter;
                Log.e("LicenseGeneratorFromXml", "Failed to generate " + file, e);
                if (printWriter2 != null) {
                    printWriter2.close();
                }
                return false;
            }
        }
        return false;
    }

    private void parse(File file) {
        InputStreamReader fileReader;
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }
        InputStreamReader inputStreamReader = null;
        try {
            if (file.getName().endsWith(".gz")) {
                fileReader = new InputStreamReader(new GZIPInputStream(new FileInputStream(file)));
            } else {
                fileReader = new FileReader(file);
            }
            inputStreamReader = fileReader;
            parse(inputStreamReader, this.mFileNameToContentIdMap, this.mContentIdToFileContentMap);
            inputStreamReader.close();
        } catch (IOException | XmlPullParserException e) {
            Log.e("LicenseGeneratorFromXml", "Failed to parse " + file, e);
            if (inputStreamReader == null) {
                return;
            }
            try {
                inputStreamReader.close();
            } catch (IOException unused) {
                Log.w("LicenseGeneratorFromXml", "Failed to close " + file);
            }
        }
    }

    static void parse(InputStreamReader inputStreamReader, Map<String, Set<String>> map, Map<String, String> map2) throws XmlPullParserException, IOException {
        HashMap hashMap = new HashMap();
        Map<? extends String, ? extends String> hashMap2 = new HashMap<>();
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(inputStreamReader);
        newPullParser.nextTag();
        newPullParser.require(2, "", "licenses");
        for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
            if (eventType == 2) {
                if ("file-name".equals(newPullParser.getName())) {
                    String attributeValue = newPullParser.getAttributeValue("", "contentId");
                    if (!TextUtils.isEmpty(attributeValue)) {
                        String trim = readText(newPullParser).trim();
                        if (!TextUtils.isEmpty(trim)) {
                            ((Set) hashMap.computeIfAbsent(trim, LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda1.INSTANCE)).add(attributeValue);
                        }
                    }
                } else if ("file-content".equals(newPullParser.getName())) {
                    String attributeValue2 = newPullParser.getAttributeValue("", "contentId");
                    if (!TextUtils.isEmpty(attributeValue2) && !map2.containsKey(attributeValue2) && !hashMap2.containsKey(attributeValue2)) {
                        String readText = readText(newPullParser);
                        if (!TextUtils.isEmpty(readText)) {
                            hashMap2.put(attributeValue2, readText);
                        }
                    }
                }
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            map.merge((String) entry.getKey(), (Set) entry.getValue(), LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda0.INSTANCE);
        }
        map2.putAll(hashMap2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Set lambda$parse$0(String str) {
        return new HashSet();
    }

    private static String readText(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        StringBuffer stringBuffer = new StringBuffer();
        int next = xmlPullParser.next();
        while (next == 4) {
            stringBuffer.append(xmlPullParser.getText());
            next = xmlPullParser.next();
        }
        return stringBuffer.toString();
    }

    static void generateHtml(Map<String, Set<String>> map, Map<String, String> map2, PrintWriter printWriter, String str) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.addAll(map.keySet());
        Collections.sort(arrayList);
        printWriter.println("<html><head>\n<style type=\"text/css\">\nbody { padding: 0; font-family: sans-serif; }\n.same-license { background-color: #eeeeee;\n                border-top: 20px solid white;\n                padding: 10px; }\n.label { font-weight: bold; }\n.file-list { margin-left: 1em; color: blue; }\n</style>\n</head><body topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\">\n<div class=\"toc\">\n<ul>");
        if (!TextUtils.isEmpty(str)) {
            printWriter.println(str);
        }
        HashMap hashMap = new HashMap();
        ArrayList<ContentIdAndFileNames> arrayList2 = new ArrayList();
        int i = 0;
        for (String str2 : arrayList) {
            for (String str3 : map.get(str2)) {
                if (!hashMap.containsKey(str3)) {
                    hashMap.put(str3, Integer.valueOf(i));
                    arrayList2.add(new ContentIdAndFileNames(str3));
                    i++;
                }
                int intValue = ((Integer) hashMap.get(str3)).intValue();
                ((ContentIdAndFileNames) arrayList2.get(intValue)).mFileNameList.add(str2);
                printWriter.format("<li><a href=\"#id%d\">%s</a></li>\n", Integer.valueOf(intValue), str2);
            }
        }
        printWriter.println("</ul>\n</div><!-- table of contents -->\n<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        int i2 = 0;
        for (ContentIdAndFileNames contentIdAndFileNames : arrayList2) {
            printWriter.format("<tr id=\"id%d\"><td class=\"same-license\">\n", Integer.valueOf(i2));
            printWriter.println("<div class=\"label\">Notices for file(s):</div>");
            printWriter.println("<div class=\"file-list\">");
            Iterator<String> it = contentIdAndFileNames.mFileNameList.iterator();
            while (it.hasNext()) {
                printWriter.format("%s <br/>\n", it.next());
            }
            printWriter.println("</div><!-- file-list -->");
            printWriter.println("<pre class=\"license-text\">");
            printWriter.println(map2.get(contentIdAndFileNames.mContentId));
            printWriter.println("</pre><!-- license-text -->");
            printWriter.println("</td></tr><!-- same-license -->");
            i2++;
        }
        printWriter.println("</table></body></html>");
    }
}
