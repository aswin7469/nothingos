package com.android.systemui.dump;

import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005\u0012\u0016\u0010\u0006\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\u00030\u0005j\u0002`\u00070\u0005¢\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u000e\u0010\u0010\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0006\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\u00030\u0005j\u0002`\u00070\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/dump/DumpsysTableLogger;", "", "sectionName", "", "columns", "", "rows", "Lcom/android/systemui/dump/Row;", "(Ljava/lang/String;Ljava/util/List;Ljava/util/List;)V", "printData", "", "pw", "Ljava/io/PrintWriter;", "printSchema", "printSectionEnd", "printSectionStart", "printTableData", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpsysTableLogger.kt */
public final class DumpsysTableLogger {
    private final List<String> columns;
    private final List<List<String>> rows;
    private final String sectionName;

    public DumpsysTableLogger(String str, List<String> list, List<? extends List<String>> list2) {
        Intrinsics.checkNotNullParameter(str, "sectionName");
        Intrinsics.checkNotNullParameter(list, "columns");
        Intrinsics.checkNotNullParameter(list2, "rows");
        this.sectionName = str;
        this.columns = list;
        this.rows = list2;
    }

    public final void printTableData(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printSectionStart(printWriter);
        printSchema(printWriter);
        printData(printWriter);
        printSectionEnd(printWriter);
    }

    private final void printSectionStart(PrintWriter printWriter) {
        printWriter.println("SystemUI TableSection START: " + this.sectionName);
        printWriter.println("version 1");
    }

    private final void printSectionEnd(PrintWriter printWriter) {
        printWriter.println("SystemUI TableSection END: " + this.sectionName);
    }

    private final void printSchema(PrintWriter printWriter) {
        printWriter.println(CollectionsKt.joinToString$default(this.columns, "|", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
    }

    private final void printData(PrintWriter printWriter) {
        int size = this.columns.size();
        Collection arrayList = new ArrayList();
        for (Object next : this.rows) {
            if (((List) next).size() == size) {
                arrayList.add(next);
            }
        }
        for (List joinToString$default : (List) arrayList) {
            printWriter.println(CollectionsKt.joinToString$default(joinToString$default, "|", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
        }
    }
}
