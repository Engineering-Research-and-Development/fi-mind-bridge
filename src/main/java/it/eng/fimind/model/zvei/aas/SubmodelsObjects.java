package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class SubmodelsObjects {
	private Identification identification;
	private String kind;
	private KeysObject semanticId;
	private List<SubmodelElements> submodelElements;
	private NameObject modelType;
	
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public KeysObject getSemanticId() {
		return semanticId;
	}
	public void setSemanticId(KeysObject semanticId) {
		this.semanticId = semanticId;
	}
	public List<SubmodelElements> getSubmodelElements() {
		return submodelElements;
	}
	public void setSubmodelElements(List<SubmodelElements> submodelElements) {
		this.submodelElements = submodelElements;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}

}
