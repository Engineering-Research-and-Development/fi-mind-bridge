package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class AssetAdministrationShellsObjects {
	private Identification identification;
	private KeysObject asset;
	private List<KeysObject> submodels;
	private List<ViewsElement> views;
	private Administration administration;
	private NameObject modelType;
	private List<ConceptDescriptionsElement> conceptDictionaries;
	
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
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
	public List<ViewsElement> getViews() {
		return views;
	}
	public void setViews(List<ViewsElement> views) {
		this.views = views;
	}
	public Administration getAdministration() {
		return administration;
	}
	public void setAdministration(Administration administration) {
		this.administration = administration;
	}
	public NameObject getModelType() {
		return modelType;
	}
	public void setModelType(NameObject modelType) {
		this.modelType = modelType;
	}
	public List<ConceptDescriptionsElement> getConceptDictionaries() {
		return conceptDictionaries;
	}
	public void setConceptDictionaries(List<ConceptDescriptionsElement> conceptDictionaries) {
		this.conceptDictionaries = conceptDictionaries;
	}

}
