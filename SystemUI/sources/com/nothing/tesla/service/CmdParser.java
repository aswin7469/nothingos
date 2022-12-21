package com.nothing.tesla.service;

import android.text.TextUtils;
import com.google.gson.Gson;

public class CmdParser {
    public static CmdObjectList parseCmd(String str) {
        Gson gson = new Gson();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (CmdObjectList) gson.fromJson(str, CmdObjectList.class);
    }
}
