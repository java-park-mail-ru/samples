package resources.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class ResourceFactory {

    private final ObjectMapper objectMapper;
    private final Path basePath;

    public ResourceFactory(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
        this.basePath = Paths.get("src/main/resources");
    }

    /**
     * Get resource by path.
     * @param path - path to resource
     * @return Resource
     * @throws ResourceException - if cant find or parse the resource
     */
    public Resource get(String path) {
        return get(path, Resource.class);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public <T extends Resource> T get(String path, Class<T> clazz) {
        return get(Paths.get(path), clazz);
    }

    public <T extends Resource> T get(Path path, Class<T> clazz) {

        final Map<String, Object> jsonMap = substituteReferencesInMap(getJsonMapByPath(path));

        try {
            return objectMapper.convertValue(jsonMap, clazz);
        } catch (IllegalArgumentException e) {
            throw new ResourceException("Failed constructing resource object " + path + " of type " + clazz.getName(), e);
        }
    }

    private Map<String, Object> getJsonMapByPath(Path path) {
        try {
            return objectMapper.readValue(this.basePath.resolve(path).toRealPath().toFile(), new MapTypeReference());
        } catch (IOException e) {
            throw new ResourceException("Failed reading json by path " + path, e);
        }
    }


    private List<Object> substituteReferencesInList(List<Object> jsonList) {
        ListIterator<Object> objectListIterator = jsonList.listIterator();
        while (objectListIterator.hasNext()) {
            Object next = objectListIterator.next();
            objectListIterator.set(substituteReferences(next));
        }
        return jsonList;
    }

    private Object substituteReferences(Object json) {
        if (json instanceof List) {
            return substituteReferencesInList((List<Object>) json);
        }
        if (json instanceof Map) {
            return substituteReferencesInMap((Map<String, Object>) json);
        }
        return json;
    }

    private Map<String, Object> substituteReferencesInMap(Map<String, Object> jsonMap) {
        if (!jsonMap.containsKey("type")) {
            return jsonMap;
        }

        if (jsonMap.get("type").equals(ResourceRef.class.getSimpleName())) {

            final ResourceRef resourceRef = objectMapper.convertValue(jsonMap, ResourceRef.class);
            return getJsonMapByPath(Paths.get(resourceRef.getPath()));
        }
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            entry.setValue(substituteReferences(entry.getValue()));
        }
        return jsonMap;
    }

    private static class MapTypeReference extends TypeReference<Map<String, Object>> {
    }


}
