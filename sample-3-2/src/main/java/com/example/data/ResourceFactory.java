package com.example.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@SuppressWarnings({"LocalCanBeFinal", "OverlyBroadCatchBlock", "SameParameterValue"})
@Service
public class ResourceFactory {

    private final ObjectMapper objectMapper;

    public ResourceFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param path - path to resource
     * @return Resource
     * @throws ResourceException - if cant find or parse the resource
     */
    public Resource get(String path) {
        return get(path, Resource.class);
    }

    public <T extends Resource> T get(String path, Class<T> clazz) {
        T resource;
        try {
            resource = objectMapper.readValue(Resources.getResource(path), clazz);
        } catch (IOException e) {
            throw new ResourceException("Failed constructing resource object " + path + " of type " + clazz.getName(), e);
        }

        return resource;
    }
}
