package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import java.util.List;

public class ChartData {
    public List<NetworkStats.Bucket> detail;
    public List<NetworkStats.Bucket> detailDefault;
    public List<NetworkStats.Bucket> detailForeground;
    public List<NetworkStats.Bucket> network;
}
