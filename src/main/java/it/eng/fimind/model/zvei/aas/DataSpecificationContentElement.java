package it.eng.fimind.model.zvei.aas;

import java.util.List;

public class DataSpecificationContentElement {
	private langStringObject preferredName;
	private String shortName;
	private String unit;
	private List<Object> sourceOfDefinition;
	private String dataType;
	private langStringObject definition;
	
	public langStringObject getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(langStringObject preferredName) {
		this.preferredName = preferredName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<Object> getSourceOfDefinition() {
		return sourceOfDefinition;
	}
	public void setSourceOfDefinition(List<Object> sourceOfDefinition) {
		this.sourceOfDefinition = sourceOfDefinition;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public langStringObject getDefinition() {
		return definition;
	}
	public void setDefinition(langStringObject definition) {
		this.definition = definition;
	}
	
}
