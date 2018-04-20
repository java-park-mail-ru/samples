package resources.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceRef extends Resource {
    private String path;

    @JsonCreator
    public ResourceRef(@JsonProperty("path") String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
