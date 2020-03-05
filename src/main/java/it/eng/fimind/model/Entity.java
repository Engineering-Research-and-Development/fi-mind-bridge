package it.eng.fimind.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Entity {

    private String id;

    private String type;

    private Map<String, Attribute> attributes;

    public Entity() {
    }

    public Entity(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public Entity(String id, String type, Map<String, Attribute> attributes) {
        this(id, type);
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    @JsonAnySetter
    public void setAttributes(String key, Attribute attribute) {
        if (attributes == null) {
            attributes = new HashMap<String, Attribute>();
        }
        attributes.put(key, attribute);
    }

    @JsonIgnore
    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }
    
    
}
