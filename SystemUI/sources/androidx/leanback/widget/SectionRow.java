package androidx.leanback.widget;

public class SectionRow extends Row {
    public final boolean isRenderedAsRowView() {
        return false;
    }

    public SectionRow(HeaderItem headerItem) {
        super(headerItem);
    }

    public SectionRow(long j, String str) {
        super(new HeaderItem(j, str));
    }

    public SectionRow(String str) {
        super(new HeaderItem(str));
    }
}
