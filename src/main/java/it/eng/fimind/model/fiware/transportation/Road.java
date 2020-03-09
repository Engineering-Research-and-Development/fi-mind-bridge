package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Location;

public class Road {
	@NotNull(message = "{road.null.id}")
	private String id;
	@Pattern(message = "{road.wrong.type}", regexp = "Road", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	private String dataCreated;
	private String dataModified;
	@NotNull(message = "{road.null.name}")
	private String name;
	private String alternateName;
	private String description;
	@NotNull(message = "{road.null.roadClass}")
	private String roadClass;
	private RoadSegment refRoadSegment;
	private Location location;
	private Double length;
	private String responsible;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRoadClass() {
		return roadClass;
	}
	public void setRoadClass(String roadClass) {
		this.roadClass = roadClass;
	}
	public RoadSegment getRefRoadSegment() {
		return refRoadSegment;
	}
	public void setRefRoadSegment(RoadSegment refRoadSegment) {
		this.refRoadSegment = refRoadSegment;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

}
