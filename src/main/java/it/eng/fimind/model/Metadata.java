package it.eng.fimind.model;

/**
 * Metadata Model
 */
public class Metadata {

    private Object value;

    private String type;

    public Metadata() {
    }

    public Metadata(Object value) {
        this.value = value;
    }

    public Metadata(String type, Object value) {
        this(value);
        this.type = type;
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
}
