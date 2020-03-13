package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;
import it.eng.fimind.model.fiware.common.MultiAttribute;

public class VehicleNormalized {
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
	@NotNull(message = "{vehicle.null.category}")
	private Attribute category;
	private LocationNormalized location;
	private LocationNormalized previousLocation;
	private Attribute speed;
	private Attribute heading;
	private Attribute cargoWeight;
	private Attribute vehicleIdentificationNumber;
	private Attribute vehiclePlateIdentifier;
	private Attribute fleetVehicleId;
	private Attribute dateVehicleFirstRegistered;
	private Attribute dateFirstUsed;
	private Attribute purchaseDate;
	private Attribute mileageFromOdometer;
	private Attribute vehicleConfiguration;
	private Attribute color;
	private Attribute owner;
	private MultiAttribute feature;
	private MultiAttribute serviceProvided;
	private Attribute vehicleSpecialUsage;
	private Attribute refVehicleModel;
	private Attribute areaServed;
	private Attribute serviceStatus;
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
	public Attribute getCategory() {
		return category;
	}
	public void setCategory(Attribute category) {
		this.category = category;
	}
	public LocationNormalized getLocation() {
		return location;
	}
	public void setLocation(LocationNormalized location) {
		this.location = location;
	}
	public LocationNormalized getPreviousLocation() {
		return previousLocation;
	}
	public void setPreviousLocation(LocationNormalized previousLocation) {
		this.previousLocation = previousLocation;
	}
	public Attribute getSpeed() {
		return speed;
	}
	public void setSpeed(Attribute speed) {
		this.speed = speed;
	}
	public Attribute getHeading() {
		return heading;
	}
	public void setHeading(Attribute heading) {
		this.heading = heading;
	}
	public Attribute getCargoWeight() {
		return cargoWeight;
	}
	public void setCargoWeight(Attribute cargoWeight) {
		this.cargoWeight = cargoWeight;
	}
	public Attribute getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}
	public void setVehicleIdentificationNumber(Attribute vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}
	public Attribute getVehiclePlateIdentifier() {
		return vehiclePlateIdentifier;
	}
	public void setVehiclePlateIdentifier(Attribute vehiclePlateIdentifier) {
		this.vehiclePlateIdentifier = vehiclePlateIdentifier;
	}
	public Attribute getFleetVehicleId() {
		return fleetVehicleId;
	}
	public void setFleetVehicleId(Attribute fleetVehicleId) {
		this.fleetVehicleId = fleetVehicleId;
	}
	public Attribute getDateVehicleFirstRegistered() {
		return dateVehicleFirstRegistered;
	}
	public void setDateVehicleFirstRegistered(Attribute dateVehicleFirstRegistered) {
		this.dateVehicleFirstRegistered = dateVehicleFirstRegistered;
	}
	public Attribute getDateFirstUsed() {
		return dateFirstUsed;
	}
	public void setDateFirstUsed(Attribute dateFirstUsed) {
		this.dateFirstUsed = dateFirstUsed;
	}
	public Attribute getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Attribute purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Attribute getMileageFromOdometer() {
		return mileageFromOdometer;
	}
	public void setMileageFromOdometer(Attribute mileageFromOdometer) {
		this.mileageFromOdometer = mileageFromOdometer;
	}
	public Attribute getVehicleConfiguration() {
		return vehicleConfiguration;
	}
	public void setVehicleConfiguration(Attribute vehicleConfiguration) {
		this.vehicleConfiguration = vehicleConfiguration;
	}
	public Attribute getColor() {
		return color;
	}
	public void setColor(Attribute color) {
		this.color = color;
	}
	public Attribute getOwner() {
		return owner;
	}
	public void setOwner(Attribute owner) {
		this.owner = owner;
	}
	public MultiAttribute getFeature() {
		return feature;
	}
	public void setFeature(MultiAttribute feature) {
		this.feature = feature;
	}
	public MultiAttribute getServiceProvided() {
		return serviceProvided;
	}
	public void setServiceProvided(MultiAttribute serviceProvided) {
		this.serviceProvided = serviceProvided;
	}
	public Attribute getVehicleSpecialUsage() {
		return vehicleSpecialUsage;
	}
	public void setVehicleSpecialUsage(Attribute vehicleSpecialUsage) {
		this.vehicleSpecialUsage = vehicleSpecialUsage;
	}
	public Attribute getRefVehicleModel() {
		return refVehicleModel;
	}
	public void setRefVehicleModel(Attribute refVehicleModel) {
		this.refVehicleModel = refVehicleModel;
	}
	public Attribute getAreaServed() {
		return areaServed;
	}
	public void setAreaServed(Attribute areaServed) {
		this.areaServed = areaServed;
	}
	public Attribute getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(Attribute serviceStatus) {
		this.serviceStatus = serviceStatus;
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
