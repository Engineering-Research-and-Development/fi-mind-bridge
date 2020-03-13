package it.eng.fimind.model.fiware.device;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.MultiAttribute;

public class DeviceModelNormalized {
	@NotNull(message = "{device.null.id}")
	private String id;
	@Pattern(message = "{device.wrong.type}", regexp = "DeviceModel", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	@NotNull(message = "{device.null.category}")
	private MultiAttribute category;
	private Attribute deviceClass;
	@NotNull(message = "{device.null.controlledProperty}")
	private MultiAttribute controlledProperty;
	private MultiAttribute function ;
	private MultiAttribute supportedProtocol;
	private MultiAttribute supportedUnits;
	private Attribute energyLimitationClass;
	@NotNull(message = "{device.null.brandName}")
	private Attribute brandName;
	@NotNull(message = "{device.null.modelName}")
	private Attribute modelName;
	@NotNull(message = "{device.null.manufacturerName}")
	private Attribute manufacturerName;
	@NotNull(message = "{device.null.name}")
	private Attribute name;
	private Attribute description;
	private Attribute documentation;
	private Attribute image;
	private Attribute dateModified;
	private Attribute dateCreated;
	
	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html
	private MultiAttribute owner;
	private MultiAttribute annotations;
	private Attribute alternateName;
	private Attribute color;
	
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
	public Attribute getSource() {
		return source;
	}
	public void setSource(Attribute source) {
		this.source = source;
	}
	public Attribute getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(Attribute dataProvider) {
		this.dataProvider = dataProvider;
	}
	public MultiAttribute getCategory() {
		return category;
	}
	public void setCategory(MultiAttribute category) {
		this.category = category;
	}
	public Attribute getDeviceClass() {
		return deviceClass;
	}
	public void setDeviceClass(Attribute deviceClass) {
		this.deviceClass = deviceClass;
	}
	public MultiAttribute getControlledProperty() {
		return controlledProperty;
	}
	public void setControlledProperty(MultiAttribute controlledProperty) {
		this.controlledProperty = controlledProperty;
	}
	public MultiAttribute getFunction() {
		return function;
	}
	public void setFunction(MultiAttribute function) {
		this.function = function;
	}
	public MultiAttribute getSupportedProtocol() {
		return supportedProtocol;
	}
	public void setSupportedProtocol(MultiAttribute supportedProtocol) {
		this.supportedProtocol = supportedProtocol;
	}
	public MultiAttribute getSupportedUnits() {
		return supportedUnits;
	}
	public void setSupportedUnits(MultiAttribute supportedUnits) {
		this.supportedUnits = supportedUnits;
	}
	public Attribute getEnergyLimitationClass() {
		return energyLimitationClass;
	}
	public void setEnergyLimitationClass(Attribute energyLimitationClass) {
		this.energyLimitationClass = energyLimitationClass;
	}
	public Attribute getBrandName() {
		return brandName;
	}
	public void setBrandName(Attribute brandName) {
		this.brandName = brandName;
	}
	public Attribute getModelName() {
		return modelName;
	}
	public void setModelName(Attribute modelName) {
		this.modelName = modelName;
	}
	public Attribute getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(Attribute manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public Attribute getName() {
		return name;
	}
	public void setName(Attribute name) {
		this.name = name;
	}
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public Attribute getDocumentation() {
		return documentation;
	}
	public void setDocumentation(Attribute documentation) {
		this.documentation = documentation;
	}
	public Attribute getImage() {
		return image;
	}
	public void setImage(Attribute image) {
		this.image = image;
	}
	public Attribute getDateModified() {
		return dateModified;
	}
	public void setDateModified(Attribute dateModified) {
		this.dateModified = dateModified;
	}
	public Attribute getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Attribute dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html
	public MultiAttribute getOwner() {
		return owner;
	}
	public void setOwner(MultiAttribute owner) {
		this.owner = owner;
	}
	public MultiAttribute getAnnotations() {
		return annotations;
	}
	public void setAnnotations(MultiAttribute annotations) {
		this.annotations = annotations;
	}
	public Attribute getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(Attribute alternateName) {
		this.alternateName = alternateName;
	}
	public Attribute getColor() {
		return color;
	}
	public void setColor(Attribute color) {
		this.color = color;
	}

}
