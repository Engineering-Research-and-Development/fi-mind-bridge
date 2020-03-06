package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class ViewsElement {
	private String idShort;
	private List<KeysObject> containedElements;
	private NameObject modelType;
	
	public String getIdShort() {
		return idShort;
	}
	public void setIdShort(String idShort) {
		this.idShort = idShort;
	}
	public List<KeysObject> getContainedElements() {
		return containedElements;
	}
	public void setContainedElements(List<KeysObject> containedElements) {
		this.containedElements = containedElements;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}

}
