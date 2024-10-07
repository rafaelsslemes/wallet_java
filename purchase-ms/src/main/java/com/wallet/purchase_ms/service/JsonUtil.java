package com.wallet.purchase_ms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

 public class JsonUtil {

    static String parseObjectToJsonString(Object payload){

        ObjectMapper objectMapper = new ObjectMapper();

        String parsed;
        try {
            parsed = objectMapper.writeValueAsString(payload);
            return parsed;
        } catch (JsonProcessingException e) {
            // TODO: tratar casos em que a conversão falha
            return null;
        }
    }

    static <T> T parseJsonToObject(String json, Class<T> classType){

        ObjectMapper objectMapper = new ObjectMapper();

        T parsed;
        
        try {
            parsed = objectMapper.readValue(json, classType);
        } catch (JsonMappingException e) {
            return null;
        } catch (JsonProcessingException e) {
            return null;
        }
        return parsed;
        
    }
}
