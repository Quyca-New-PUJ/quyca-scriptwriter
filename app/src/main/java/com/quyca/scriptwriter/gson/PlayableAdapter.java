package com.quyca.scriptwriter.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.quyca.scriptwriter.model.Playable;

import java.lang.reflect.Type;

/**
 * The type Playable adapter. It implements JsonSerializer interface to facilitate the serialziation of the subtypes of a playable.
 */
public class PlayableAdapter implements JsonSerializer<Playable>, JsonDeserializer<Playable> {
    @Override
    public JsonElement serialize(Playable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Playable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}