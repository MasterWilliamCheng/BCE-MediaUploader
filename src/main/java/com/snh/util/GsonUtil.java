package com.snh.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 * Created by snh007 on 2016/12/2.
 */
public class GsonUtil {

    public static final JsonParser jsonParser = new JsonParser();

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss sss").create();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static JsonElement toJsonElement(Object src) {
        return gson.toJsonTree(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
