package com.nothing.experience.datas;

import android.util.SparseArray;
import java.util.List;

public class LocalData {
    public static final int TYPE_ACTIVATION = 1;
    public static final int TYPE_PRODUCT = 0;
    public static final int TYPE_QUALITY = 2;
    public static SparseArray<String> sTypeTableMapping;
    public transient int dataType;
    public DeviceInfo device;
    public List<Event> events;
    public String network_type;
    public String operator_1;
    public String operator_2;
    public String platform;
    public String project_name;
    public String sim_country_code;
    public String user_pseudo_id;

    static {
        SparseArray<String> sparseArray = new SparseArray<>();
        sTypeTableMapping = sparseArray;
        sparseArray.put(2, DataHelper.TABLE_QUALITY);
        sTypeTableMapping.put(0, DataHelper.TABLE_PRODUCT);
        sTypeTableMapping.put(1, DataHelper.TABLE_ACTIVATION);
    }

    public String toString() {
        return "LocalData{project_name='" + this.project_name + '\'' + ", network_type='" + this.network_type + '\'' + ", events=" + this.events + ", device=" + this.device + ", platform='" + this.platform + '\'' + ", sim_country_code='" + this.sim_country_code + '\'' + ", operator_1='" + this.operator_1 + '\'' + ", operator_2='" + this.operator_2 + '\'' + ", dataType=" + this.dataType + '}';
    }
}
