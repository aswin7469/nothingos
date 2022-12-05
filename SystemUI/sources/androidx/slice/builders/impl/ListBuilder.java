package androidx.slice.builders.impl;

import androidx.slice.builders.ListBuilder;
/* loaded from: classes.dex */
public interface ListBuilder {
    void addRow(ListBuilder.RowBuilder impl);

    void setTtl(long ttl);
}
