package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.LocationNormalized;

public class TrafficFlowObservedNormalized {
	@NotNull(message = "{trafficflowobserved.null.id}")
	private String id;
	@NotNull(message = "{trafficflowobserved.null.type}")
	@Pattern(message = "{trafficflowobserved.wrong.type}", regexp = "TrafficFlowObserved", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	@NotNull(message = "{trafficflowobserved.null.vehicleType}")
	private Attribute vehicleType;
	private Attribute vehicleSubType;
	private Attribute dataProvider;
	private LocationNormalized location;
	private AddressNormalized address;
	private Attribute refRoadSegment;
	private Attribute dateModified;
	private Attribute laneId;
	private Attribute dateObserved;
	private Attribute dateObservedFrom;
	private Attribute dateObservedTo;
	private Attribute dateCreated;
	private Attribute name;
	private Attribute description;
	private Attribute intensity;
	private Attribute occupancy;
	private Attribute averageVehicleSpeed;
	private Attribute averageVehicleLength;
	private Attribute congested;
	private Attribute averageHeadwayTime;
	private Attribute averageGapDistance;
	private Attribute laneDirection;
	private Attribute reversedLane;
	
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
	public Attribute getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(Attribute vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Attribute getVehicleSubType() {
		return vehicleSubType;
	}
	public void setVehicleSubType(Attribute vehicleSubType) {
		this.vehicleSubType = vehicleSubType;
	}
	public Attribute getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(Attribute dataProvider) {
		this.dataProvider = dataProvider;
	}
	public LocationNormalized getLocation() {
		return location;
	}
	public void setLocation(LocationNormalized location) {
		this.location = location;
	}
	public AddressNormalized getAddress() {
		return address;
	}
	public void setAddress(AddressNormalized address) {
		this.address = address;
	}
	public Attribute getRefRoadSegment() {
		return refRoadSegment;
	}
	public void setRefRoadSegment(Attribute refRoadSegment) {
		this.refRoadSegment = refRoadSegment;
	}
	public Attribute getDateModified() {
		return dateModified;
	}
	public void setDateModified(Attribute dateModified) {
		this.dateModified = dateModified;
	}
	public Attribute getLaneId() {
		return laneId;
	}
	public void setLaneId(Attribute laneId) {
		this.laneId = laneId;
	}
	public Attribute getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(Attribute dateObserved) {
		this.dateObserved = dateObserved;
	}
	public Attribute getDateObservedFrom() {
		return dateObservedFrom;
	}
	public void setDateObservedFrom(Attribute dateObservedFrom) {
		this.dateObservedFrom = dateObservedFrom;
	}
	public Attribute getDateObservedTo() {
		return dateObservedTo;
	}
	public void setDateObservedTo(Attribute dateObservedTo) {
		this.dateObservedTo = dateObservedTo;
	}
	public Attribute getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Attribute dateCreated) {
		this.dateCreated = dateCreated;
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
	public Attribute getIntensity() {
		return intensity;
	}
	public void setIntensity(Attribute intensity) {
		this.intensity = intensity;
	}
	public Attribute getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(Attribute occupancy) {
		this.occupancy = occupancy;
	}
	public Attribute getAverageVehicleSpeed() {
		return averageVehicleSpeed;
	}
	public void setAverageVehicleSpeed(Attribute averageVehicleSpeed) {
		this.averageVehicleSpeed = averageVehicleSpeed;
	}
	public Attribute getAverageVehicleLength() {
		return averageVehicleLength;
	}
	public void setAverageVehicleLength(Attribute averageVehicleLength) {
		this.averageVehicleLength = averageVehicleLength;
	}
	public Attribute getCongested() {
		return congested;
	}
	public void setCongested(Attribute congested) {
		this.congested = congested;
	}
	public Attribute getAverageHeadwayTime() {
		return averageHeadwayTime;
	}
	public void setAverageHeadwayTime(Attribute averageHeadwayTime) {
		this.averageHeadwayTime = averageHeadwayTime;
	}
	public Attribute getAverageGapDistance() {
		return averageGapDistance;
	}
	public void setAverageGapDistance(Attribute averageGapDistance) {
		this.averageGapDistance = averageGapDistance;
	}
	public Attribute getLaneDirection() {
		return laneDirection;
	}
	public void setLaneDirection(Attribute laneDirection) {
		this.laneDirection = laneDirection;
	}
	public Attribute getReversedLane() {
		return reversedLane;
	}
	public void setReversedLane(Attribute reversedLane) {
		this.reversedLane = reversedLane;
	}

}
