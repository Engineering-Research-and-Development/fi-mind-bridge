package it.eng.fimind.model.fiware.transportation;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Location;

public class RoadSegment {
	@NotNull(message = "{roadsegment.null.id}")
	private String id;
	@Pattern(message = "{roadsegment.wrong.type}", regexp = "RoadSegment", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String dataProvider;
	private String dataCreated;
	private String dataModified;
	private String source;
	@NotNull(message = "{roadsegment.null.name}")
	private String name;
	private String alternateName;
	@NotNull(message = "{roadsegment.null.refRoad}")
	private Road refRoad;
	private Location location;
	private Location startPoint;
	private Location endPoint;
	private Double startKilometer;
	private Double endKilometer;
	@NotNull(message = "{roadsegment.null.allowedVehicleType}")
	@Pattern(message = "{roadsegment.wrong.allowedVehicleType}", regexp = "agriculturalVehicle|bicycle|bus|car|caravan|carWithCaravan|carWithTrailer|constructionOrMaintenanceVehicle|lorry|moped|motorcycle|motorcycleWithSideCar|motorscooter|tanker|trailer|van|anyVehicle", flags=Pattern.Flag.CASE_INSENSITIVE)
	private List<String> allowedVehicleType;
	private Integer totalLaneNumber;
	private Double length;
	private Double maximumAllowedSpeed;
	private Double minimumAllowedSpeed;
	private Double maximumAllowedHeight;
	private Double maximumAllowedWeight;
	private Double width;
	private List<String> laneUsage;
	@Pattern(message = "{roadsegment.wrong.category}", regexp = "oneway|toll|link", flags=Pattern.Flag.CASE_INSENSITIVE)
	private List<String> category;
	
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
	public String getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}
	public String getDataCreated() {
		return dataCreated;
	}
	public void setDataCreated(String dataCreated) {
		this.dataCreated = dataCreated;
	}
	public String getDataModified() {
		return dataModified;
	}
	public void setDataModified(String dataModified) {
		this.dataModified = dataModified;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}
	public Road getRefRoad() {
		return refRoad;
	}
	public void setRefRoad(Road refRoad) {
		this.refRoad = refRoad;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Location getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Location startPoint) {
		this.startPoint = startPoint;
	}
	public Location getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Location endPoint) {
		this.endPoint = endPoint;
	}
	public Double getStartKilometer() {
		return startKilometer;
	}
	public void setStartKilometer(Double startKilometer) {
		this.startKilometer = startKilometer;
	}
	public Double getEndKilometer() {
		return endKilometer;
	}
	public void setEndKilometer(Double endKilometer) {
		this.endKilometer = endKilometer;
	}
	public List<String> getAllowedVehicleType() {
		return allowedVehicleType;
	}
	public void setAllowedVehicleType(List<String> allowedVehicleType) {
		this.allowedVehicleType = allowedVehicleType;
	}
	public Integer getTotalLaneNumber() {
		return totalLaneNumber;
	}
	public void setTotalLaneNumber(Integer totalLaneNumber) {
		this.totalLaneNumber = totalLaneNumber;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getMaximumAllowedSpeed() {
		return maximumAllowedSpeed;
	}
	public void setMaximumAllowedSpeed(Double maximumAllowedSpeed) {
		this.maximumAllowedSpeed = maximumAllowedSpeed;
	}
	public Double getMinimumAllowedSpeed() {
		return minimumAllowedSpeed;
	}
	public void setMinimumAllowedSpeed(Double minimumAllowedSpeed) {
		this.minimumAllowedSpeed = minimumAllowedSpeed;
	}
	public Double getMaximumAllowedHeight() {
		return maximumAllowedHeight;
	}
	public void setMaximumAllowedHeight(Double maximumAllowedHeight) {
		this.maximumAllowedHeight = maximumAllowedHeight;
	}
	public Double getMaximumAllowedWeight() {
		return maximumAllowedWeight;
	}
	public void setMaximumAllowedWeight(Double maximumAllowedWeight) {
		this.maximumAllowedWeight = maximumAllowedWeight;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public List<String> getLaneUsage() {
		return laneUsage;
	}
	public void setLaneUsage(List<String> laneUsage) {
		this.laneUsage = laneUsage;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}

}	

