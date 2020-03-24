package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VehicleModel {
	@NotNull(message = "{vehicle.null.id}")
	private String id;
	@NotNull(message = "{vehicle.null.type}")
	@Pattern(message = "{vehicle.wrong.type}", regexp = "VehicleModel", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	private String name;
	private String description;
	@NotNull(message = "{vehicle.null.vehicleType}")
	@Pattern(message = "{vehicle.wrong.vehicleType}", regexp = "agriculturalVehicle|bicycle|bus|minibus|car|caravan|tram|tanker|carWithCaravan|carWithTrailer|lorry|moped|tanker|motorcycle|motorcycleWithSideCar|motorscooter|trailer|van|caravan|constructionOrMaintenanceVehicle"
						+ "trolley|binTrolley|sweepingMachine|cleaningTrolley", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String vehicleType;
	private String brandName;
	private String modelName;
	private String manufacturerName;
	private String vehicleModelDate;
	private Double cargoVolume;
	@Pattern(message = "{vehicle.wrong.fuelType}", regexp = "gasoline|petrol(unleaded)|petrol(leaded)|petrol|diesel|electric|hydrogen|lpg|autogas|cng|biodiesel ethanol|hybrid electric/petrol|hybrid electric/diesel|other", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String fuelType;
	private Double fuelConsumption;
	private Double height;
	private Double width;
	private Double depth;
	private Double weight;	
	private String vehicleEngine;
	private String image;
	private String url;
	private String dateModified;
	private String dateCreated;
	
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
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
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
	public String getVehicleModelDate() {
		return vehicleModelDate;
	}
	public void setVehicleModelDate(String vehicleModelDate) {
		this.vehicleModelDate = vehicleModelDate;
	}
	public Double getCargoVolume() {
		return cargoVolume;
	}
	public void setCargoVolume(Double cargoVolume) {
		this.cargoVolume = cargoVolume;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public Double getFuelConsumption() {
		return fuelConsumption;
	}
	public void setFuelConsumption(Double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getDepth() {
		return depth;
	}
	public void setDepth(Double depth) {
		this.depth = depth;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getVehicleEngine() {
		return vehicleEngine;
	}
	public void setVehicleEngine(String vehicleEngine) {
		this.vehicleEngine = vehicleEngine;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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

}
