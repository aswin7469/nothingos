package androidx.leanback.widget;

public class PageRow extends Row {
    public final boolean isRenderedAsRowView() {
        return false;
    }

    public PageRow(HeaderItem headerItem) {
        super(headerItem);
    }
}
