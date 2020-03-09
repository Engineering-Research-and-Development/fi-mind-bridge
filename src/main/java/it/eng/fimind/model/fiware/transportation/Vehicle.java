package it.eng.fimind.model.fiware.transportation;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Location;

public class Vehicle {
	@NotNull(message = "{vehicle.null.id}")
	private String id;
	@NotNull(message = "{vehicle.null.type}")
	@Pattern(message = "{vehicle.wrong.type}", regexp = "Vehicle", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	private String name;
	private String description;
	@NotNull(message = "{vehicle.null.vehicleType}")
	@Pattern(message = "{vehicle.wrong.vehicleType}", regexp = "agriculturalVehicle|bicycle|bus|minibus|car|caravan|tram|tanker|carWithCaravan|carWithTrailer|lorry|moped|tanker|motorcycle|motorcycleWithSideCar|motorscooter|trailer|van|caravan|constructionOrMaintenanceVehicle"
			+ 			"trolley|binTrolley|sweepingMachine|cleaningTrolley", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String vehicleType;
	@NotNull(message = "{vehicle.null.category}")
	@Pattern(message = "{vehicle.wrong.category}", regexp = "public|private|municipalServices|specialUsage|tracked|nonTracked", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String category;
	private Location location;
	private Location previousLocation;
	private Double speed;
	private Double heading;
	private Double cargoWeight;
	private String vehicleIdentificationNumber;
	private String vehiclePlateIdentifier;
	private String fleetVehicleId;
	private String dateVehicleFirstRegistered;
	private String dateFirstUsed;
	private String purchaseDate;
	private String mileageFromOdometer;
	private String vehicleConfiguration;
	private String color;
	private String owner;
	private List<String> feature;
	private List<String> serviceProvided;
	private String vehicleSpecialUsage;
	private String refVehicleModel;
	private String areaServed;
	@Pattern(message = "{vehicle.wrong.serviceStatus}", regexp = "parked|onRoute|broken|outOfService", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String serviceStatus;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Location getPreviousLocation() {
		return previousLocation;
	}
	public void setPreviousLocation(Location previousLocation) {
		this.previousLocation = previousLocation;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public Double getHeading() {
		return heading;
	}
	public void setHeading(Double heading) {
		this.heading = heading;
	}
	public Double getCargoWeight() {
		return cargoWeight;
	}
	public void setCargoWeight(Double cargoWeight) {
		this.cargoWeight = cargoWeight;
	}
	public String getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}
	public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}
	public String getVehiclePlateIdentifier() {
		return vehiclePlateIdentifier;
	}
	public void setVehiclePlateIdentifier(String vehiclePlateIdentifier) {
		this.vehiclePlateIdentifier = vehiclePlateIdentifier;
	}
	public String getFleetVehicleId() {
		return fleetVehicleId;
	}
	public void setFleetVehicleId(String fleetVehicleId) {
		this.fleetVehicleId = fleetVehicleId;
	}
	public String getDateVehicleFirstRegistered() {
		return dateVehicleFirstRegistered;
	}
	public void setDateVehicleFirstRegistered(String dateVehicleFirstRegistered) {
		this.dateVehicleFirstRegistered = dateVehicleFirstRegistered;
	}
	public String getDateFirstUsed() {
		return dateFirstUsed;
	}
	public void setDateFirstUsed(String dateFirstUsed) {
		this.dateFirstUsed = dateFirstUsed;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getMileageFromOdometer() {
		return mileageFromOdometer;
	}
	public void setMileageFromOdometer(String mileageFromOdometer) {
		this.mileageFromOdometer = mileageFromOdometer;
	}
	public String getVehicleConfiguration() {
		return vehicleConfiguration;
	}
	public void setVehicleConfiguration(String vehicleConfiguration) {
		this.vehicleConfiguration = vehicleConfiguration;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<String> getFeature() {
		return feature;
	}
	public void setFeature(List<String> feature) {
		this.feature = feature;
	}
	public List<String> getServiceProvided() {
		return serviceProvided;
	}
	public void setServiceProvided(List<String> serviceProvided) {
		this.serviceProvided = serviceProvided;
	}
	public String getVehicleSpecialUsage() {
		return vehicleSpecialUsage;
	}
	public void setVehicleSpecialUsage(String vehicleSpecialUsage) {
		this.vehicleSpecialUsage = vehicleSpecialUsage;
	}
	public String getRefVehicleModel() {
		return refVehicleModel;
	}
	public void setRefVehicleModel(String refVehicleModel) {
		this.refVehicleModel = refVehicleModel;
	}
	public String getAreaServed() {
		return areaServed;
	}
	public void setAreaServed(String areaServed) {
		this.areaServed = areaServed;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
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
