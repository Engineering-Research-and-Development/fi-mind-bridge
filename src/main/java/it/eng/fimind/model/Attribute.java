package it.eng.fimind.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Attribute Model
 */
public class Attribute {

    private Object value;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String type;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonIgnore
    private Map<String, Metadata> metadata;

    public Attribute() {
    }

    public Attribute(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Metadata> getMetadata() {
        if (metadata == null) {
            return Collections.emptyMap();
        }
        return metadata;
    }

    public void setMetadata(Map<String, Metadata> metadata) {
        this.metadata = metadata;
    }

    @JsonIgnore
    public void addMetadata(String key, Metadata metadata) {
        if (this.metadata == null) {
            this.metadata = new HashMap<String, Metadata>();
        }
        this.metadata.put(key, metadata);
    }
}