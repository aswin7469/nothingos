package org.json;

import android.annotation.SystemApi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONStringer;

public class JSONObject {
    private static final Double NEGATIVE_ZERO = Double.valueOf(-0.0d);
    public static final Object NULL = new Object() {
        public boolean equals(Object obj) {
            return obj == this || obj == null;
        }

        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "null";
        }
    };
    private final LinkedHashMap<String, Object> nameValuePairs;

    public JSONObject() {
        this.nameValuePairs = new LinkedHashMap<>();
    }

    public JSONObject(Map map) {
        this();
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            if (str != null) {
                this.nameValuePairs.put(str, wrap(entry.getValue()));
            } else {
                throw new NullPointerException("key == null");
            }
        }
    }

    public JSONObject(JSONTokener jSONTokener) throws JSONException {
        Object nextValue = jSONTokener.nextValue();
        if (nextValue instanceof JSONObject) {
            this.nameValuePairs = ((JSONObject) nextValue).nameValuePairs;
            return;
        }
        throw JSON.typeMismatch(nextValue, "JSONObject");
    }

    public JSONObject(String str) throws JSONException {
        this(new JSONTokener(str));
    }

    public JSONObject(JSONObject jSONObject, String[] strArr) throws JSONException {
        this();
        for (String str : strArr) {
            Object opt = jSONObject.opt(str);
            if (opt != null) {
                this.nameValuePairs.put(str, opt);
            }
        }
    }

    public int length() {
        return this.nameValuePairs.size();
    }

    public JSONObject put(String str, boolean z) throws JSONException {
        this.nameValuePairs.put(checkName(str), Boolean.valueOf(z));
        return this;
    }

    public JSONObject put(String str, double d) throws JSONException {
        this.nameValuePairs.put(checkName(str), Double.valueOf(JSON.checkDouble(d)));
        return this;
    }

    public JSONObject put(String str, int i) throws JSONException {
        this.nameValuePairs.put(checkName(str), Integer.valueOf(i));
        return this;
    }

    public JSONObject put(String str, long j) throws JSONException {
        this.nameValuePairs.put(checkName(str), Long.valueOf(j));
        return this;
    }

    public JSONObject put(String str, Object obj) throws JSONException {
        if (obj == null) {
            this.nameValuePairs.remove(str);
            return this;
        }
        if (obj instanceof Number) {
            JSON.checkDouble(((Number) obj).doubleValue());
        }
        this.nameValuePairs.put(checkName(str), obj);
        return this;
    }

    public JSONObject putOpt(String str, Object obj) throws JSONException {
        return (str == null || obj == null) ? this : put(str, obj);
    }

    public JSONObject accumulate(String str, Object obj) throws JSONException {
        Object obj2 = this.nameValuePairs.get(checkName(str));
        if (obj2 == null) {
            return put(str, obj);
        }
        if (obj2 instanceof JSONArray) {
            ((JSONArray) obj2).checkedPut(obj);
        } else {
            JSONArray jSONArray = new JSONArray();
            jSONArray.checkedPut(obj2);
            jSONArray.checkedPut(obj);
            this.nameValuePairs.put(str, jSONArray);
        }
        return this;
    }

    public JSONObject append(String str, Object obj) throws JSONException {
        JSONArray jSONArray;
        Object obj2 = this.nameValuePairs.get(checkName(str));
        if (obj2 instanceof JSONArray) {
            jSONArray = (JSONArray) obj2;
        } else if (obj2 == null) {
            jSONArray = new JSONArray();
            this.nameValuePairs.put(str, jSONArray);
        } else {
            throw new JSONException("Key " + str + " is not a JSONArray");
        }
        jSONArray.checkedPut(obj);
        return this;
    }

    /* access modifiers changed from: package-private */
    public String checkName(String str) throws JSONException {
        if (str != null) {
            return str;
        }
        throw new JSONException("Names must be non-null");
    }

    public Object remove(String str) {
        return this.nameValuePairs.remove(str);
    }

    public boolean isNull(String str) {
        Object obj = this.nameValuePairs.get(str);
        return obj == null || obj == NULL;
    }

    public boolean has(String str) {
        return this.nameValuePairs.containsKey(str);
    }

    public Object get(String str) throws JSONException {
        Object obj = this.nameValuePairs.get(str);
        if (obj != null) {
            return obj;
        }
        throw new JSONException("No value for " + str);
    }

    public Object opt(String str) {
        return this.nameValuePairs.get(str);
    }

    public boolean getBoolean(String str) throws JSONException {
        Object obj = get(str);
        Boolean bool = JSON.toBoolean(obj);
        if (bool != null) {
            return bool.booleanValue();
        }
        throw JSON.typeMismatch(str, obj, "boolean");
    }

    public boolean optBoolean(String str) {
        return optBoolean(str, false);
    }

    public boolean optBoolean(String str, boolean z) {
        Boolean bool = JSON.toBoolean(opt(str));
        return bool != null ? bool.booleanValue() : z;
    }

    public double getDouble(String str) throws JSONException {
        Object obj = get(str);
        Double d = JSON.toDouble(obj);
        if (d != null) {
            return d.doubleValue();
        }
        throw JSON.typeMismatch(str, obj, "double");
    }

    public double optDouble(String str) {
        return optDouble(str, Double.NaN);
    }

    public double optDouble(String str, double d) {
        Double d2 = JSON.toDouble(opt(str));
        return d2 != null ? d2.doubleValue() : d;
    }

    public int getInt(String str) throws JSONException {
        Object obj = get(str);
        Integer integer = JSON.toInteger(obj);
        if (integer != null) {
            return integer.intValue();
        }
        throw JSON.typeMismatch(str, obj, "int");
    }

    public int optInt(String str) {
        return optInt(str, 0);
    }

    public int optInt(String str, int i) {
        Integer integer = JSON.toInteger(opt(str));
        return integer != null ? integer.intValue() : i;
    }

    public long getLong(String str) throws JSONException {
        Object obj = get(str);
        Long l = JSON.toLong(obj);
        if (l != null) {
            return l.longValue();
        }
        throw JSON.typeMismatch(str, obj, "long");
    }

    public long optLong(String str) {
        return optLong(str, 0);
    }

    public long optLong(String str, long j) {
        Long l = JSON.toLong(opt(str));
        return l != null ? l.longValue() : j;
    }

    public String getString(String str) throws JSONException {
        Object obj = get(str);
        String json = JSON.toString(obj);
        if (json != null) {
            return json;
        }
        throw JSON.typeMismatch(str, obj, "String");
    }

    public String optString(String str) {
        return optString(str, "");
    }

    public String optString(String str, String str2) {
        String json = JSON.toString(opt(str));
        return json != null ? json : str2;
    }

    public JSONArray getJSONArray(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw JSON.typeMismatch(str, obj, "JSONArray");
    }

    public JSONArray optJSONArray(String str) {
        Object opt = opt(str);
        if (opt instanceof JSONArray) {
            return (JSONArray) opt;
        }
        return null;
    }

    public JSONObject getJSONObject(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw JSON.typeMismatch(str, obj, "JSONObject");
    }

    public JSONObject optJSONObject(String str) {
        Object opt = opt(str);
        if (opt instanceof JSONObject) {
            return (JSONObject) opt;
        }
        return null;
    }

    public JSONArray toJSONArray(JSONArray jSONArray) throws JSONException {
        int length;
        JSONArray jSONArray2 = new JSONArray();
        if (jSONArray == null || (length = jSONArray.length()) == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            jSONArray2.put(opt(JSON.toString(jSONArray.opt(i))));
        }
        return jSONArray2;
    }

    public Iterator<String> keys() {
        return this.nameValuePairs.keySet().iterator();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Set<String> keySet() {
        return this.nameValuePairs.keySet();
    }

    public JSONArray names() {
        if (this.nameValuePairs.isEmpty()) {
            return null;
        }
        return new JSONArray((Collection) new ArrayList(this.nameValuePairs.keySet()));
    }

    public String toString() {
        try {
            JSONStringer jSONStringer = new JSONStringer();
            writeTo(jSONStringer);
            return jSONStringer.toString();
        } catch (JSONException unused) {
            return null;
        }
    }

    public String toString(int i) throws JSONException {
        JSONStringer jSONStringer = new JSONStringer(i);
        writeTo(jSONStringer);
        return jSONStringer.toString();
    }

    /* access modifiers changed from: package-private */
    public void writeTo(JSONStringer jSONStringer) throws JSONException {
        jSONStringer.object();
        for (Map.Entry next : this.nameValuePairs.entrySet()) {
            jSONStringer.key((String) next.getKey()).value(next.getValue());
        }
        jSONStringer.endObject();
    }

    public static String numberToString(Number number) throws JSONException {
        if (number != null) {
            double doubleValue = number.doubleValue();
            JSON.checkDouble(doubleValue);
            if (number.equals(NEGATIVE_ZERO)) {
                return "-0";
            }
            long longValue = number.longValue();
            if (doubleValue == ((double) longValue)) {
                return Long.toString(longValue);
            }
            return number.toString();
        }
        throw new JSONException("Number must be non-null");
    }

    public static String quote(String str) {
        if (str == null) {
            return "\"\"";
        }
        try {
            JSONStringer jSONStringer = new JSONStringer();
            jSONStringer.open(JSONStringer.Scope.NULL, "");
            jSONStringer.value((Object) str);
            jSONStringer.close(JSONStringer.Scope.NULL, JSONStringer.Scope.NULL, "");
            return jSONStringer.toString();
        } catch (JSONException unused) {
            throw new AssertionError();
        }
    }

    public static Object wrap(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if ((obj instanceof JSONArray) || (obj instanceof JSONObject) || obj.equals(NULL)) {
            return obj;
        }
        try {
            if (obj instanceof Collection) {
                return new JSONArray((Collection) obj);
            }
            if (obj.getClass().isArray()) {
                return new JSONArray(obj);
            }
            if (obj instanceof Map) {
                return new JSONObject((Map) obj);
            }
            if ((obj instanceof Boolean) || (obj instanceof Byte) || (obj instanceof Character) || (obj instanceof Double) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Short)) {
                return obj;
            }
            if (obj instanceof String) {
                return obj;
            }
            if (obj.getClass().getPackage().getName().startsWith("java.")) {
                return obj.toString();
            }
            return null;
        } catch (Exception unused) {
        }
    }
}
