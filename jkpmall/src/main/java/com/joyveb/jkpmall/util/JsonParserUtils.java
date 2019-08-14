package com.joyveb.jkpmall.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @author yangqiju on 2017/3/28.
 */
public final class JsonParserUtils {

    private static JsonParser parser = new JsonParser();
    private static Gson gson = new GsonBuilder().create();
    private JsonParserUtils() {
    }

    /**
     * Parses the specified JSON string into a parse tree
     *
     * @param json JSON text
     * @return a parse tree of {@link JsonElement}s corresponding to the specified JSON
     * @throws JsonSyntaxException if the specified text is not valid JSON
     * @since 1.3
     */
    public static JsonElement parse(String json) throws JsonSyntaxException {
        return parser.parse(json);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
