package com.nothing.experience.datas;

import java.util.List;

public class Event {
    public AppInfo app_info;
    public String event_id;
    public String event_name;
    public List<EventParam> event_params;
    public String event_timestamp;

    public String toString() {
        return "Event{event_timestamp='" + this.event_timestamp + '\'' + ", event_id='" + this.event_id + '\'' + ", event_name='" + this.event_name + '\'' + ", event_params=" + this.event_params + '}';
    }
}
