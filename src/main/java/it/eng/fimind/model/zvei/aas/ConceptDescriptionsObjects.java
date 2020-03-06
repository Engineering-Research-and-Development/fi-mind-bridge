package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class ConceptDescriptionsObjects {
	private Identification identification;
	private NameObject modelType;
	private List<EmbeddedDataSpecificationsElement> embeddedDataSpecifications;
	
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
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

}
