package it.eng.fimind.model.zvei.aas;

public class KeysElement {
	private String type;
	private String idType;
	private String value;
	private Boolean local;
	private Integer index;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getLocal() {
		return local;
	}
	public void setLocal(Boolean local) {
		this.local = local;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}

}
