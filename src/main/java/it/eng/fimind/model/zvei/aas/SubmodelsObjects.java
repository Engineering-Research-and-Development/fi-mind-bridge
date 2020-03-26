package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class SubmodelsObjects {
	private KeysObject semanticId;
	private List<Object> qualifiers;
	private Identification identification;
	private String idShort;
	private String category;
	private NameObject modelType;
	private String kind;
	private List<SubmodelElements> submodelElements;
	
	public KeysObject getSemanticId() {
		return semanticId;
	}
	public void setSemanticId(KeysObject semanticId) {
		this.semanticId = semanticId;
	}
	public List<Object> getQualifiers() {
		return qualifiers;
	}
	public void setQualifiers(List<Object> qualifiers) {
		this.qualifiers = qualifiers;
	}
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
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
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public List<SubmodelElements> getSubmodelElements() {
		return submodelElements;
	}
	public void setSubmodelElements(List<SubmodelElements> submodelElements) {
		this.submodelElements = submodelElements;
	}


}
