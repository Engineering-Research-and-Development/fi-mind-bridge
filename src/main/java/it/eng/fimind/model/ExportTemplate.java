package it.eng.fimind.model;

import javax.validation.constraints.NotNull;

public class ExportTemplate {
	@NotNull(message = "{ocbExport.null.assetId}")
	private String assetId;
	@NotNull(message = "{ocbExport.null.fiwareService}")
	private String fiwareService;
	@NotNull(message = "{ocbExport.null.fiwareServicePath}")
	private String fiwareServicePath;
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getFiwareService() {
		return fiwareService;
	}
	public void setFiwareService(String fiwareService) {
		this.fiwareService = fiwareService;
	}
	public String getFiwareServicePath() {
		return fiwareServicePath;
	}
	public void setFiwareServicePath(String fiwareServicePath) {
		this.fiwareServicePath = fiwareServicePath;
	}

}
