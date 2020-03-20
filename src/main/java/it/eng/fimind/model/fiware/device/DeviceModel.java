package it.eng.fimind.model.fiware.device;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DeviceModel {
	@NotNull(message = "{device.null.id}")
	private String id;
	@Pattern(message = "{device.wrong.type}", regexp = "DeviceModel", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	@NotNull(message = "{device.null.category}")
//	@Pattern(message = "{device.wrong.category}", regexp = "sensor|actuator|meter|HVAC|network|multimedia|implement|irrSystem|irrSection|endgun", flags=Pattern.Flag.CASE_INSENSITIVE)
	private List<String> category;
	private String deviceClass;
	@NotNull(message = "{device.null.controlledProperty}")
//	@Pattern(message = "{device.wrong.controlledProperty}", regexp = "temperature|humidity|light|motion|fillingLevel|occupancy|power|pressure|smoke|"
//																	+ "energy|airPollution|noiseLevel|weatherConditions|precipitation|windSpeed|windDirection|"
//																	+ "atmosphericPressure|solarRadiation|depth|pH|conductivity|conductance|tss|tds|turbidity|"
//																	+ "salinity|orp|cdom|waterPollution|location|speed|heading|weight|waterConsumption|gasComsumption|"
//																	+ "electricityConsumption|soilMoisture|trafficFlow|eatingActivity|milking|movementActivity", flags=Pattern.Flag.CASE_INSENSITIVE)
	private List<String> controlledProperty;
	private List<String> function ;
	private List<String> supportedProtocol;
	private List<String> supportedUnits;
	@Pattern(message = "{device.wrong.energyLimitationClass}", regexp = "E0|E1|E2|E9", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String energyLimitationClass;
	@NotNull(message = "{device.null.brandName}")
	private String brandName;
	@NotNull(message = "{device.null.modelName}")
	private String modelName;
	@NotNull(message = "{device.null.manufacturerName}")
	private String manufacturerName;
	@NotNull(message = "{device.null.name}")
	private String name;
	private String description;
	private String documentation;
	private String image;
	private String dateModified;
	private String dateCreated;
	
	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html
	private List<String> owner;
	private List<String> annotations;
	private String alternateName;
	private String color;
	
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getDeviceClass() {
		return deviceClass;
	}
	public void setDeviceClass(String deviceClass) {
		this.deviceClass = deviceClass;
	}
	public List<String> getControlledProperty() {
		return controlledProperty;
	}
	public void setControlledProperty(List<String> controlledProperty) {
		this.controlledProperty = controlledProperty;
	}
	public List<String> getFunction() {
		return function;
	}
	public void setFunction(List<String> function) {
		this.function = function;
	}
	public List<String> getSupportedProtocol() {
		return supportedProtocol;
	}
	public void setSupportedProtocol(List<String> supportedProtocol) {
		this.supportedProtocol = supportedProtocol;
	}
	public List<String> getSupportedUnits() {
		return supportedUnits;
	}
	public void setSupportedUnits(List<String> supportedUnits) {
		this.supportedUnits = supportedUnits;
	}
	public String getEnergyLimitationClass() {
		return energyLimitationClass;
	}
	public void setEnergyLimitationClass(String energyLimitationClass) {
		this.energyLimitationClass = energyLimitationClass;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html
	public List<String> getOwner() {
		return owner;
	}
	public void setOwner(List<String> owner) {
		this.owner = owner;
	}
	public List<String> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}
	public String getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	
}
