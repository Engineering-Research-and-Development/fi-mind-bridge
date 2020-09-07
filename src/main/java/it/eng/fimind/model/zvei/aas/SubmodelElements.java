package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class SubmodelElements {
	private String mimeType;
	private Object value;
	private String valueId;
	private KeysObject hasDataSpecification;
	private KeysObject semanticId;
	private List<Object> constraints;
	private String idShort;
	private String category;
	private NameObject modelType;
	private ValueType valueType;
	private String kind;
	private String descriptions;
	private Boolean ordered;
	private Boolean allowDuplicates;
	
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getValueId() {
		return valueId;
	}
	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	public KeysObject getHasDataSpecification() {
		return hasDataSpecification;
	}
	public void setHasDataSpecification(KeysObject hasDataSpecification) {
		this.hasDataSpecification = hasDataSpecification;
	}
	public KeysObject getSemanticId() {
		return semanticId;
	}
	public void setSemanticId(KeysObject semanticId) {
		this.semanticId = semanticId;
	}
	public List<Object> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Object> constraints) {
		this.constraints = constraints;
	}
	public String getIdShort() {
		return idShort;
	}
	public void setIdShort(String idShort) {
		this.idShort = idShort;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public Boolean getOrdered() {
		return ordered;
	}
	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}
	public Boolean getAllowDuplicates() {
		return allowDuplicates;
	}
	public void setAllowDuplicates(Boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}
}
