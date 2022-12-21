package com.nothing.sdk.onlineconfig;

import android.content.Context;
import com.nothing.sdk.utils.ClassReflection;
import com.nothing.sdk.utils.MethodReflection;
import java.lang.reflect.Constructor;
import org.json.JSONArray;

public class ConfigGrabber {
    private static final String CONFIG_GRABBER = "com.nothing.onlineconfig.ConfigGrabber";
    private Object mConfigGrabberInst;

    public ConfigGrabber(Context context, String str) {
        try {
            Constructor<?> findConstructor = ClassReflection.findConstructor(CONFIG_GRABBER, Context.class, String.class);
            findConstructor.setAccessible(true);
            this.mConfigGrabberInst = findConstructor.newInstance(context, str);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public JSONArray grabConfig() {
        return (JSONArray) MethodReflection.invokeMethod(MethodReflection.findMethod(ClassReflection.findClass(CONFIG_GRABBER), "grabConfig"), this.mConfigGrabberInst);
    }
}
