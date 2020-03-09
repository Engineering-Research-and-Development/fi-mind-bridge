package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;
import it.eng.fimind.model.fiware.common.MultiAttribute;

public class RoadSegmentNormalized {
	@NotNull(message = "{roadsegment.null.id}")
	private String id;
	@Pattern(message = "{roadsegment.wrong.type}", regexp = "RoadSegment", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute dataProvider;
	private Attribute dataCreated;
	private Attribute dataModified;
	private Attribute source;
	@NotNull(message = "{roadsegment.null.name}")
	private Attribute name;
	private Attribute alternateName;
	@NotNull(message = "{roadsegment.null.refRoad}")
	private RoadNormalized refRoad;
	private LocationNormalized location;
	private LocationNormalized startPoint;
	private LocationNormalized endPoint;
	private Attribute startKilometer;
	private Attribute endKilometer;
	@NotNull(message = "{roadsegment.null.allowedVehicleType}")
	@Pattern(message = "{roadsegment.wrong.allowedVehicleType}", regexp = "agriculturalVehicle|bicycle|bus|car|caravan|carWithCaravan|carWithTrailer|constructionOrMaintenanceVehicle|lorry|moped|motorcycle|motorcycleWithSideCar|motorscooter|tanker|trailer|van|anyVehicle", flags=Pattern.Flag.CASE_INSENSITIVE)
	private MultiAttribute allowedVehicleType;
	private Attribute totalLaneNumber;
	private Attribute length;
	private Attribute maximumAllowedSpeed;
	private Attribute minimumAllowedSpeed;
	private Attribute maximumAllowedHeight;
	private Attribute maximumAllowedWeight;
	private Attribute width;
	private MultiAttribute laneUsage;
	@Pattern(message = "{roadsegment.wrong.category}", regexp = "oneway|toll|link", flags=Pattern.Flag.CASE_INSENSITIVE)
	private MultiAttribute category;
	
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
	public Attribute getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(Attribute dataProvider) {
		this.dataProvider = dataProvider;
	}
	public Attribute getDataCreated() {
		return dataCreated;
	}
	public void setDataCreated(Attribute dataCreated) {
		this.dataCreated = dataCreated;
	}
	public Attribute getDataModified() {
		return dataModified;
	}
	public void setDataModified(Attribute dataModified) {
		this.dataModified = dataModified;
	}
	public Attribute getSource() {
		return source;
	}
	public void setSource(Attribute source) {
		this.source = source;
	}
	public Attribute getName() {
		return name;
	}
	public void setName(Attribute name) {
		this.name = name;
	}
	public Attribute getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(Attribute alternateName) {
		this.alternateName = alternateName;
	}
	public RoadNormalized getRefRoad() {
		return refRoad;
	}
	public void setRefRoad(RoadNormalized refRoad) {
		this.refRoad = refRoad;
	}
	public LocationNormalized getLocation() {
		return location;
	}
	public void setLocation(LocationNormalized location) {
		this.location = location;
	}
	public LocationNormalized getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(LocationNormalized startPoint) {
		this.startPoint = startPoint;
	}
	public LocationNormalized getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(LocationNormalized endPoint) {
		this.endPoint = endPoint;
	}
	public Attribute getStartKilometer() {
		return startKilometer;
	}
	public void setStartKilometer(Attribute startKilometer) {
		this.startKilometer = startKilometer;
	}
	public Attribute getEndKilometer() {
		return endKilometer;
	}
	public void setEndKilometer(Attribute endKilometer) {
		this.endKilometer = endKilometer;
	}
	public MultiAttribute getAllowedVehicleType() {
		return allowedVehicleType;
	}
	public void setAllowedVehicleType(MultiAttribute allowedVehicleType) {
		this.allowedVehicleType = allowedVehicleType;
	}
	public Attribute getTotalLaneNumber() {
		return totalLaneNumber;
	}
	public void setTotalLaneNumber(Attribute totalLaneNumber) {
		this.totalLaneNumber = totalLaneNumber;
	}
	public Attribute getLength() {
		return length;
	}
	public void setLength(Attribute length) {
		this.length = length;
	}
	public Attribute getMaximumAllowedSpeed() {
		return maximumAllowedSpeed;
	}
	public void setMaximumAllowedSpeed(Attribute maximumAllowedSpeed) {
		this.maximumAllowedSpeed = maximumAllowedSpeed;
	}
	public Attribute getMinimumAllowedSpeed() {
		return minimumAllowedSpeed;
	}
	public void setMinimumAllowedSpeed(Attribute minimumAllowedSpeed) {
		this.minimumAllowedSpeed = minimumAllowedSpeed;
	}
	public Attribute getMaximumAllowedHeight() {
		return maximumAllowedHeight;
	}
	public void setMaximumAllowedHeight(Attribute maximumAllowedHeight) {
		this.maximumAllowedHeight = maximumAllowedHeight;
	}
	public Attribute getMaximumAllowedWeight() {
		return maximumAllowedWeight;
	}
	public void setMaximumAllowedWeight(Attribute maximumAllowedWeight) {
		this.maximumAllowedWeight = maximumAllowedWeight;
	}
	public Attribute getWidth() {
		return width;
	}
	public void setWidth(Attribute width) {
		this.width = width;
	}
	public MultiAttribute getLaneUsage() {
		return laneUsage;
	}
	public void setLaneUsage(MultiAttribute laneUsage) {
		this.laneUsage = laneUsage;
	}
	public MultiAttribute getCategory() {
		return category;
	}
	public void setCategory(MultiAttribute category) {
		this.category = category;
	}

}
