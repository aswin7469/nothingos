package com.nothing.tesla.service;

import java.util.ArrayList;
import java.util.List;

public class CmdObjectList {
    public List<CmdObject> cmdObjects = new ArrayList();

    public List<CmdObject> getCmdObjects() {
        return this.cmdObjects;
    }

    public void setCmdObjects(List<CmdObject> list) {
        this.cmdObjects = list;
    }
}
