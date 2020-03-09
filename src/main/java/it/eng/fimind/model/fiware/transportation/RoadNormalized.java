package it.eng.fimind.model.fiware.transportation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;

public class RoadNormalized {
	@NotNull(message = "{road.null.id}")
	private String id;
	@Pattern(message = "{road.wrong.type}", regexp = "Road", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute type;
	private Attribute source;
	private Attribute dataProvider;
	private Attribute dataCreated;
	private Attribute dataModified;
	@NotNull(message = "{road.null.name}")
	private Attribute name;
	private Attribute alternateName;
	private Attribute description;
	@NotNull(message = "{road.null.roadClass}")
	private Attribute roadClass;
	private RoadSegmentNormalized refRoadSegment;
	private LocationNormalized location;
	private Attribute length;
	private Attribute responsible;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Attribute getType() {
		return type;
	}
	public void setType(Attribute type) {
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
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public Attribute getRoadClass() {
		return roadClass;
	}
	public void setRoadClass(Attribute roadClass) {
		this.roadClass = roadClass;
	}
	public RoadSegmentNormalized getRefRoadSegment() {
		return refRoadSegment;
	}
	public void setRefRoadSegment(RoadSegmentNormalized refRoadSegment) {
		this.refRoadSegment = refRoadSegment;
	}
	public LocationNormalized getLocation() {
		return location;
	}
	public void setLocation(LocationNormalized location) {
		this.location = location;
	}
	public Attribute getLength() {
		return length;
	}
	public void setLength(Attribute length) {
		this.length = length;
	}
	public Attribute getResponsible() {
		return responsible;
	}
	public void setResponsible(Attribute responsible) {
		this.responsible = responsible;
	}
	
}
