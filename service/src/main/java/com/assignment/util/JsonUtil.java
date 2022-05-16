package com.assignment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String contentString, Class<T> clazz) {
        try {
            return objectMapper.readValue(contentString, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeToJson(Object content) {
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
