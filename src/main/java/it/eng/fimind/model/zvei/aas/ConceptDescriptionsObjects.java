package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class ConceptDescriptionsObjects {
	private String idShort;
	private Identification identification;
	private VerRevObject administration;
	private NameObject modelType;
	private List<EmbeddedDataSpecificationsElement> embeddedDataSpecifications;
	private List<KeysObject> isCaseOf;
	
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
	public VerRevObject getAdministration() {
		return administration;
	}
	public void setAdministration(VerRevObject administration) {
		this.administration = administration;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}
	public List<EmbeddedDataSpecificationsElement> getEmbeddedDataSpecifications() {
		return embeddedDataSpecifications;
	}
	public void setEmbeddedDataSpecifications(List<EmbeddedDataSpecificationsElement> embeddedDataSpecifications) {
		this.embeddedDataSpecifications = embeddedDataSpecifications;
	}
	public List<KeysObject> getIsCaseOf() {
		return isCaseOf;
	}
	public void setIsCaseOf(List<KeysObject> isCaseOf) {
		this.isCaseOf = isCaseOf;
	}

}
