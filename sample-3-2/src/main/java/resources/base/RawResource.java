package resources.base;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class RawResource {

    private String type;

    private Map<String, Object> properties = new HashMap<>();

    public RawResource(@JsonProperty("type") String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @JsonAnySetter
    public void anySet(String name, Object value) {
        properties.put(name, value);
    }

    @JsonAnyGetter
    public Object anyGet(String name) {
        return properties.get(name);
    }
}
