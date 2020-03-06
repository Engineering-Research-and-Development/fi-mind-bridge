package it.eng.fimind.model.zvei.aas;

public class DataSpecificationContent {
	private LangTextObject preferredName;
	private String shortName;
	private KeysObject unitId;
	private String valueFormat;
	
	public LangTextObject getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(LangTextObject preferredName) {
		this.preferredName = preferredName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public KeysObject getUnitId() {
		return unitId;
	}
	public void setUnitId(KeysObject unitId) {
		this.unitId = unitId;
	}
	public String getValueFormat() {
		return valueFormat;
	}
	public void setValueFormat(String valueFormat) {
		this.valueFormat = valueFormat;
	}

}
