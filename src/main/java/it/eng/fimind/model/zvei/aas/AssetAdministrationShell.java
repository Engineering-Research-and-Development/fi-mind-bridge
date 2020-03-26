package it.eng.fimind.model.zvei.aas;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AssetAdministrationShell {
	@NotNull(message = "{assetAdministrationShell.null.id}")
	private String id;
	@NotNull(message = "{assetAdministrationShell.null.type}")
	@Pattern(message = "{assetAdministrationShell.wrong.type}", regexp = "AssetAdministrationShell", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String schemaLocation;
	private List<AssetAdministrationShellsObjects> assetAdministrationShells;
	private List<SubmodelsObjects> submodels;
	private List<AssetsObjects> assets;
	private List<ConceptDescriptionsObjects> conceptDescriptions;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSchemaLocation() {
		return schemaLocation;
	}
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	public List<AssetAdministrationShellsObjects> getAssetAdministrationShells() {
		return assetAdministrationShells;
	}
	public void setAssetAdministrationShells(List<AssetAdministrationShellsObjects> assetAdministrationShells) {
		this.assetAdministrationShells = assetAdministrationShells;
	}
	public List<SubmodelsObjects> getSubmodels() {
		return submodels;
	}
	public void setSubmodels(List<SubmodelsObjects> submodels) {
		this.submodels = submodels;
	}
	public List<AssetsObjects> getAssets() {
		return assets;
	}
	public void setAssets(List<AssetsObjects> assets) {
		this.assets = assets;
	}
	public List<ConceptDescriptionsObjects> getConceptDescriptions() {
		return conceptDescriptions;
	}
	public void setConceptDescriptions(List<ConceptDescriptionsObjects> conceptDescriptions) {
		this.conceptDescriptions = conceptDescriptions;
	}

}
