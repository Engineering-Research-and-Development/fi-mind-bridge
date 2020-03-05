package it.eng.fimind.model.fiware.common;

import java.util.List;

public class MultiAttribute {
	private String type;
	private List<Object> value;
	private Metadata metadata;
	
	public MultiAttribute() {}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<Object> getValue() {
		return value;
	}

	public void setValue(List<Object> value) {
		this.value = value;
	}
	
		


}
