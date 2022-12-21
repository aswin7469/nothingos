package com.nothing.experience.utils;

import android.content.Context;
import com.nothing.experience.App;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.network.Http;
import com.nothing.experience.network.IDataListener;
import com.nothing.experience.network.ResponseBean;
import com.nothing.experience.service.DataQuery;
import java.util.List;

public class DataSyncTask {
    private static final String MAX_ITEM_LIMITED = "1000";
    private Context mContext;
    /* access modifiers changed from: private */
    public DataQuery mQuery;

    public DataSyncTask(Context context) {
        this.mContext = context;
        this.mQuery = new DataQuery(context);
    }

    private boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public void syncActivation() {
        if (!((Boolean) App.GLOBAL_SETTINGS.get(SharedPreferencesUtils.KEY_ACTIVE_REPORTED, false)).booleanValue()) {
            this.mQuery.queryActivationItemByLimited(new DataSyncTask$$ExternalSyntheticLambda0(this), "1");
        }
    }

    public void uploadWhenReady() {
        syncActivation();
        App.UPLOAD_TYPE = ((Integer) App.GLOBAL_SETTINGS.get(SharedPreferencesUtils.KEY_REPORT_TYPE, 0)).intValue();
        if (App.UPLOAD_TYPE == 0) {
            this.mQuery.queryProductItemByLimited(new DataSyncTask$$ExternalSyntheticLambda1(this), MAX_ITEM_LIMITED);
            App.GLOBAL_SETTINGS.set(SharedPreferencesUtils.KEY_REPORT_TYPE, 2);
            return;
        }
        this.mQuery.queryQualityItemByLimited(new DataSyncTask$$ExternalSyntheticLambda2(this), MAX_ITEM_LIMITED);
        App.GLOBAL_SETTINGS.set(SharedPreferencesUtils.KEY_REPORT_TYPE, 0);
    }

    /* renamed from: uploadData */
    public void mo13967xbe4845d2(final LocalData localData) {
        Http.resetUrl();
        if (notEmpty(localData.events)) {
            LogUtil.m42d("upload data before " + localData);
            Http.sendJsonRequest(localData, Http.sUrlMapping.get(localData.dataType), ResponseBean.class, new IDataListener<ResponseBean>() {
                public /* synthetic */ void onFailure(int i, String str) {
                    IDataListener.CC.$default$onFailure(this, i, str);
                }

                public /* synthetic */ void onSuccess(Object obj) {
                    IDataListener.CC.$default$onSuccess(this, obj);
                }

                public void onSuccess() {
                    LogUtil.m42d("upload data after " + localData + " success");
                    if (localData.dataType != 1) {
                        DataSyncTask.this.mQuery.deleteUploadedData(localData);
                    } else {
                        App.GLOBAL_SETTINGS.set(SharedPreferencesUtils.KEY_ACTIVE_REPORTED, true);
                    }
                }
            });
        }
    }
}
