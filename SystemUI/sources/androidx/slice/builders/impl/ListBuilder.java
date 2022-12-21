package androidx.slice.builders.impl;

import android.app.PendingIntent;
import android.os.PersistableBundle;
import androidx.slice.builders.GridRowBuilder;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SelectionBuilder;
import androidx.slice.builders.SliceAction;
import java.time.Duration;
import java.util.Set;

public interface ListBuilder {
    void addAction(SliceAction sliceAction);

    void addGridRow(GridRowBuilder gridRowBuilder);

    void addInputRange(ListBuilder.InputRangeBuilder inputRangeBuilder);

    void addRange(ListBuilder.RangeBuilder rangeBuilder);

    void addRating(ListBuilder.RatingBuilder ratingBuilder);

    void addRow(ListBuilder.RowBuilder rowBuilder);

    void addSelection(SelectionBuilder selectionBuilder);

    void setColor(int i);

    void setHeader(ListBuilder.HeaderBuilder headerBuilder);

    void setHostExtras(PersistableBundle persistableBundle);

    void setIsError(boolean z);

    void setKeywords(Set<String> set);

    void setLayoutDirection(int i);

    void setSeeMoreAction(PendingIntent pendingIntent);

    void setSeeMoreRow(ListBuilder.RowBuilder rowBuilder);

    void setTtl(long j);

    void setTtl(Duration duration);
}
