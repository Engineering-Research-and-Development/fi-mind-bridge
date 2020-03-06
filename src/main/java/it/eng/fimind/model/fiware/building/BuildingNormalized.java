package it.eng.fimind.model.fiware.building;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;
import it.eng.fimind.model.fiware.common.MultiAttribute;
import it.eng.fimind.model.fiware.common.OpeningHoursNormalized;

public class BuildingNormalized {
	@NotNull(message = "{building.null.id}")
	private String id;
	@Pattern(message = "{building.wrong.type}", regexp = "Building", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	private Attribute dateModified;
	private Attribute dateCreated;
	private MultiAttribute owner;
	@NotNull(message = "{building.null.category}")
	private Attribute category;
	private LocationNormalized location;
	private LocationNormalized containedInPlace;
	@NotNull(message = "{building.null.address}")
	private AddressNormalized address;
	private Attribute description;
	private MultiAttribute occupier ;
	private Attribute  floorsAboveGround;
	private Attribute floorsBelowGround ;
	private Attribute mapUrl;
	private OpeningHoursNormalized openingHours;

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
	public MultiAttribute getOwner() {
		return owner;
	}
	public void setOwner(MultiAttribute owner) {
		this.owner = owner;
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
	public LocationNormalized getContainedInPlace() {
		return containedInPlace;
	}
	public void setContainedInPlace(LocationNormalized containedInPlace) {
		this.containedInPlace = containedInPlace;
	}
	public AddressNormalized getAddress() {
		return address;
	}
	public void setAddress(AddressNormalized address) {
		this.address = address;
	}
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public MultiAttribute getOccupier() {
		return occupier;
	}
	public void setOccupier(MultiAttribute occupier) {
		this.occupier = occupier;
	}
	public Attribute getFloorsAboveGround() {
		return floorsAboveGround;
	}
	public void setFloorsAboveGround(Attribute floorsAboveGround) {
		this.floorsAboveGround = floorsAboveGround;
	}
	public Attribute getFloorsBelowGround() {
		return floorsBelowGround;
	}
	public void setFloorsBelowGround(Attribute floorsBelowGround) {
		this.floorsBelowGround = floorsBelowGround;
	}
	public Attribute getMapUrl() {
		return mapUrl;
	}
	public void setMapUrl(Attribute mapUrl) {
		this.mapUrl = mapUrl;
	}
	public OpeningHoursNormalized getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(OpeningHoursNormalized openingHours) {
		this.openingHours = openingHours;
	}

}
