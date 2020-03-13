package it.eng.fimind.model.fiware.weather;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;

public class WeatherObservedNormalized {
	@NotNull(message = "{weatherobserved.null.id}")
	private String id;
	@Pattern(message = "{weatherobserved.wrong.type}", regexp = "WeatherObserved", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute dataProvider;
	private Attribute dateModified;
	private Attribute dateCreated;
	private Attribute name;
	private LocationNormalized location;
	private AddressNormalized address;
	@NotNull(message = "{weatherobserved.null.dateObserved}")
	private Attribute dateObserved;
	private Attribute source;
	private Attribute refDevice;
	private Attribute refPointOfInterest;
	private Attribute weatherType;
	private Attribute dewPoint;
	private Attribute visibility;
	private Attribute temperature;
	private Attribute relativeHumidity;
	private Attribute precipitation;
	private Attribute windDirection;
	private Attribute windSpeed;
	private Attribute atmosphericPressure;
	@Pattern(message = "{weatherobserved.wrong.pressureTendency}", regexp = "raising|falling|steady", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute pressureTendency;
	private Attribute solarRadiation;
	private Attribute illuminance;
	private Attribute streamGauge;
	private Attribute snowHeight;
	
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
	public Attribute getName() {
		return name;
	}
	public void setName(Attribute name) {
		this.name = name;
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
	public Attribute getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(Attribute dateObserved) {
		this.dateObserved = dateObserved;
	}
	public Attribute getSource() {
		return source;
	}
	public void setSource(Attribute source) {
		this.source = source;
	}
	public Attribute getRefDevice() {
		return refDevice;
	}
	public void setRefDevice(Attribute refDevice) {
		this.refDevice = refDevice;
	}
	public Attribute getRefPointOfInterest() {
		return refPointOfInterest;
	}
	public void setRefPointOfInterest(Attribute refPointOfInterest) {
		this.refPointOfInterest = refPointOfInterest;
	}
	public Attribute getWeatherType() {
		return weatherType;
	}
	public void setWeatherType(Attribute weatherType) {
		this.weatherType = weatherType;
	}
	public Attribute getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(Attribute dewPoint) {
		this.dewPoint = dewPoint;
	}
	public Attribute getVisibility() {
		return visibility;
	}
	public void setVisibility(Attribute visibility) {
		this.visibility = visibility;
	}
	public Attribute getTemperature() {
		return temperature;
	}
	public void setTemperature(Attribute temperature) {
		this.temperature = temperature;
	}
	public Attribute getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Attribute relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Attribute getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(Attribute precipitation) {
		this.precipitation = precipitation;
	}
	public Attribute getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(Attribute windDirection) {
		this.windDirection = windDirection;
	}
	public Attribute getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Attribute windSpeed) {
		this.windSpeed = windSpeed;
	}
	public Attribute getAtmosphericPressure() {
		return atmosphericPressure;
	}
	public void setAtmosphericPressure(Attribute atmosphericPressure) {
		this.atmosphericPressure = atmosphericPressure;
	}
	public Attribute getPressureTendency() {
		return pressureTendency;
	}
	public void setPressureTendency(Attribute pressureTendency) {
		this.pressureTendency = pressureTendency;
	}
	public Attribute getSolarRadiation() {
		return solarRadiation;
	}
	public void setSolarRadiation(Attribute solarRadiation) {
		this.solarRadiation = solarRadiation;
	}
	public Attribute getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(Attribute illuminance) {
		this.illuminance = illuminance;
	}
	public Attribute getStreamGauge() {
		return streamGauge;
	}
	public void setStreamGauge(Attribute streamGauge) {
		this.streamGauge = streamGauge;
	}
	public Attribute getSnowHeight() {
		return snowHeight;
	}
	public void setSnowHeight(Attribute snowHeight) {
		this.snowHeight = snowHeight;
	}

}
