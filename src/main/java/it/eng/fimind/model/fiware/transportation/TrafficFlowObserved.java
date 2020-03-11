package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Address;
import it.eng.fimind.model.fiware.common.Location;

public class TrafficFlowObserved {
	@NotNull(message = "{trafficflowobserved.null.id}")
	private String id;
	@NotNull(message = "{trafficflowobserved.null.type}")
	@Pattern(message = "{trafficflowobserved.wrong.type}", regexp = "TrafficFlowObserved", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	@NotNull(message = "{trafficflowobserved.null.vehicleType}")
	@Pattern(message = "{trafficflowobserved.wrong.vehicleType}", regexp = "agriculturalVehicle|bicycle|bus|minibus|car|caravan|tram|tanker|carWithCaravan|carWithTrailer|lorry|moped|tanker|motorcycle|motorcycleWithSideCar|motorscooter|trailer|van|caravan|constructionOrMaintenanceVehicle"
			+ 			"trolley|binTrolley|sweepingMachine|cleaningTrolley", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String vehicleType;
	private String vehicleSubType;
	private String dataProvider;
	private Location location;
	private Address address;
	private RoadSegment refRoadSegment;
	private String dateModified;
	private Integer laneId;
	private String dateObserved;
	private String dateObservedFrom;
	private String dateObservedTo;
	private String dateCreated;
	private String name;
	private String description;
	private Integer intensity;
	private Integer occupancy;
	private Double averageVehicleSpeed;
	private Double averageVehicleLength;
	private Boolean congested;
	private Double averageHeadwayTime;
	private Double averageGapDistance;
	private String laneDirection;
	private Boolean reversedLane;
	
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
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVehicleSubType() {
		return vehicleSubType;
	}
	public void setVehicleSubType(String vehicleSubType) {
		this.vehicleSubType = vehicleSubType;
	}
	public String getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public RoadSegment getRefRoadSegment() {
		return refRoadSegment;
	}
	public void setRefRoadSegment(RoadSegment refRoadSegment) {
		this.refRoadSegment = refRoadSegment;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	public Integer getLaneId() {
		return laneId;
	}
	public void setLaneId(Integer laneId) {
		this.laneId = laneId;
	}
	public String getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(String dateObserved) {
		this.dateObserved = dateObserved;
	}
	public String getDateObservedFrom() {
		return dateObservedFrom;
	}
	public void setDateObservedFrom(String dateObservedFrom) {
		this.dateObservedFrom = dateObservedFrom;
	}
	public String getDateObservedTo() {
		return dateObservedTo;
	}
	public void setDateObservedTo(String dateObservedTo) {
		this.dateObservedTo = dateObservedTo;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
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
	public Integer getIntensity() {
		return intensity;
	}
	public void setIntensity(Integer intensity) {
		this.intensity = intensity;
	}
	public Integer getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(Integer occupancy) {
		this.occupancy = occupancy;
	}
	public Double getAverageVehicleSpeed() {
		return averageVehicleSpeed;
	}
	public void setAverageVehicleSpeed(Double averageVehicleSpeed) {
		this.averageVehicleSpeed = averageVehicleSpeed;
	}
	public Double getAverageVehicleLength() {
		return averageVehicleLength;
	}
	public void setAverageVehicleLength(Double averageVehicleLength) {
		this.averageVehicleLength = averageVehicleLength;
	}
	public Boolean getCongested() {
		return congested;
	}
	public void setCongested(Boolean congested) {
		this.congested = congested;
	}
	public Double getAverageHeadwayTime() {
		return averageHeadwayTime;
	}
	public void setAverageHeadwayTime(Double averageHeadwayTime) {
		this.averageHeadwayTime = averageHeadwayTime;
	}
	public Double getAverageGapDistance() {
		return averageGapDistance;
	}
	public void setAverageGapDistance(Double averageGapDistance) {
		this.averageGapDistance = averageGapDistance;
	}
	public String getLaneDirection() {
		return laneDirection;
	}
	public void setLaneDirection(String laneDirection) {
		this.laneDirection = laneDirection;
	}
	public Boolean getReversedLane() {
		return reversedLane;
	}
	public void setReversedLane(Boolean reversedLane) {
		this.reversedLane = reversedLane;
	}

}
