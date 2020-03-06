package it.eng.fimind.model.zvei.aas;

public class SubmodelElements {
	private String idShort;
	private NameObject modelType;
	private ValueType valueType;
	private KeysObject semanticId;
	private String category;
	
	public String getIdShort() {
		return idShort;
	}
	public void setIdShort(String idShort) {
		this.idShort = idShort;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	public KeysObject getSemanticId() {
		return semanticId;
	}
	public void setSemanticId(KeysObject semanticId) {
		this.semanticId = semanticId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
