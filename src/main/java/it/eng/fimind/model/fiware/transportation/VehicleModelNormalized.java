package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;

public class VehicleModelNormalized {
	@NotNull(message = "{vehicle.null.id}")
	private String id;
	@NotNull(message = "{vehicle.null.type}")
	@Pattern(message = "{vehicle.wrong.type}", regexp = "Vehicle", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	private Attribute name;
	private Attribute description;
	@NotNull(message = "{vehicle.null.vehicleType}")
	private Attribute vehicleType;
	private Attribute brandName;
	private Attribute modelName;
	private Attribute manufacturerName;
	private Attribute vehicleModelDate;
	private Attribute cargoVolume;
	private Attribute fuelType;
	private Attribute fuelConsumption;
	private Attribute height;
	private Attribute width;
	private Attribute depth;
	private Attribute weight;	
	private Attribute vehicleEngine;
	private Attribute image;
	private Attribute url;
	private Attribute dateModified;
	private Attribute dateCreated;
	
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
	public Attribute getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(Attribute vehicleType) {
		this.vehicleType = vehicleType;
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
	public Attribute getVehicleModelDate() {
		return vehicleModelDate;
	}
	public void setVehicleModelDate(Attribute vehicleModelDate) {
		this.vehicleModelDate = vehicleModelDate;
	}
	public Attribute getCargoVolume() {
		return cargoVolume;
	}
	public void setCargoVolume(Attribute cargoVolume) {
		this.cargoVolume = cargoVolume;
	}
	public Attribute getFuelType() {
		return fuelType;
	}
	public void setFuelType(Attribute fuelType) {
		this.fuelType = fuelType;
	}
	public Attribute getFuelConsumption() {
		return fuelConsumption;
	}
	public void setFuelConsumption(Attribute fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
	public Attribute getHeight() {
		return height;
	}
	public void setHeight(Attribute height) {
		this.height = height;
	}
	public Attribute getWidth() {
		return width;
	}
	public void setWidth(Attribute width) {
		this.width = width;
	}
	public Attribute getDepth() {
		return depth;
	}
	public void setDepth(Attribute depth) {
		this.depth = depth;
	}
	public Attribute getWeight() {
		return weight;
	}
	public void setWeight(Attribute weight) {
		this.weight = weight;
	}
	public Attribute getVehicleEngine() {
		return vehicleEngine;
	}
	public void setVehicleEngine(Attribute vehicleEngine) {
		this.vehicleEngine = vehicleEngine;
	}
	public Attribute getImage() {
		return image;
	}
	public void setImage(Attribute image) {
		this.image = image;
	}
	public Attribute getUrl() {
		return url;
	}
	public void setUrl(Attribute url) {
		this.url = url;
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

}
