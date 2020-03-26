package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class AssetAdministrationShellsObjects {
	private KeysObject derivedFrom;
	private KeysObject asset;
	private List<KeysObject> submodels;
	private List<ConceptDescriptionsElement> conceptDictionaries;
	private Identification identification;
	private Administration administration;
	private String idShort;
	private NameObject modelType;
	
	public KeysObject getDerivedFrom() {
		return derivedFrom;
	}
	public void setDerivedFrom(KeysObject derivedFrom) {
		this.derivedFrom = derivedFrom;
	}
	public KeysObject getAsset() {
		return asset;
	}
	public void setAsset(KeysObject asset) {
		this.asset = asset;
	}
	public List<KeysObject> getSubmodels() {
		return submodels;
	}
	public void setSubmodels(List<KeysObject> submodels) {
		this.submodels = submodels;
	}
	public List<ConceptDescriptionsElement> getConceptDictionaries() {
		return conceptDictionaries;
	}
	public void setConceptDictionaries(List<ConceptDescriptionsElement> conceptDictionaries) {
		this.conceptDictionaries = conceptDictionaries;
	}
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
	}
	public Administration getAdministration() {
		return administration;
	}
	public void setAdministration(Administration administration) {
		this.administration = administration;
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


}
