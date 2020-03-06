package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class AssetsObjects {
	private String idShort;
	private Identification identification;
	private String kind;
	private List<LangTextObject> descriptions;
	private NameObject modelType;
	
	public String getIdShort() {
		return idShort;
	}
	public void setIdShort(String idShort) {
		this.idShort = idShort;
	}
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
	public List<LangTextObject> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<LangTextObject> descriptions) {
		this.descriptions = descriptions;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}

}
