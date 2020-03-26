package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class AssetsObjects {
	private Identification identification;
	private String idShort;
	private NameObject modelType;
	private String kind;
	private List<LangTextObject> descriptions;
	
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
	public List<LangTextObject> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<LangTextObject> descriptions) {
		this.descriptions = descriptions;
	}
	
}
