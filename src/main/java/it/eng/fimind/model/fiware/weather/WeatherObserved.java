package it.eng.fimind.model.fiware.weather;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Address;
import it.eng.fimind.model.fiware.common.Location;

public class WeatherObserved {
	@NotNull(message = "{weatherobserved.null.id}")
	private String id;
	@Pattern(message = "{weatherobserved.wrong.type}", regexp = "WeatherObserved", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String dataProvider;
	private String dateModified;
	private String dateCreated;
	private String name;
	private Location location;
	private Address address;
	@NotNull(message = "{weatherobserved.null.dateObserved}")
	private String dateObserved;
	private String source;
	private String refDevice;
	private String refPointOfInterest;
	@Pattern(message = "{weatherobserved.wrong.weatherType}", regexp = "clearNight|sunnyDay|slightlyCloudy|partlyCloudy|mist|fog|highClouds|cloudy|veryCloudy|overcast|lightRainShower|drizzle|lightRain|heavyRainShower|heavyRain|sleetShower|sleet|hailShower|hail|shower|lightSnow|snow|heavySnowShower|heavySnow|thunderShower|thunder", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String weatherType;
	private Double dewPoint;
	@Pattern(message = "{weatherobserved.wrong.visibility}", regexp = "veryPoor|poor|moderate|good|veryGood|excellent", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String visibility;
	private Double temperature;
	private Double relativeHumidity;
	private Double precipitation;
	private Double windDirection;
	private Double windSpeed;
	private Double atmosphericPressure;
	@Pattern(message = "{weatherobserved.wrong.pressureTendency}", regexp = "raising|falling|steady", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String pressureTendency;
	private Double solarRadiation;
	private Double illuminance;
	private Double streamGauge;
	private Double snowHeight;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(String dateObserved) {
		this.dateObserved = dateObserved;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRefDevice() {
		return refDevice;
	}
	public void setRefDevice(String refDevice) {
		this.refDevice = refDevice;
	}
	public String getRefPointOfInterest() {
		return refPointOfInterest;
	}
	public void setRefPointOfInterest(String refPointOfInterest) {
		this.refPointOfInterest = refPointOfInterest;
	}
	public String getWeatherType() {
		return weatherType;
	}
	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}
	public Double getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(Double dewPoint) {
		this.dewPoint = dewPoint;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Double getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(Double precipitation) {
		this.precipitation = precipitation;
	}
	public Double getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}
	public Double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public Double getAtmosphericPressure() {
		return atmosphericPressure;
	}
	public void setAtmosphericPressure(Double atmosphericPressure) {
		this.atmosphericPressure = atmosphericPressure;
	}
	public String getPressureTendency() {
		return pressureTendency;
	}
	public void setPressureTendency(String pressureTendency) {
		this.pressureTendency = pressureTendency;
	}
	public Double getSolarRadiation() {
		return solarRadiation;
	}
	public void setSolarRadiation(Double solarRadiation) {
		this.solarRadiation = solarRadiation;
	}
	public Double getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(Double illuminance) {
		this.illuminance = illuminance;
	}
	public Double getStreamGauge() {
		return streamGauge;
	}
	public void setStreamGauge(Double streamGauge) {
		this.streamGauge = streamGauge;
	}
	public Double getSnowHeight() {
		return snowHeight;
	}
	public void setSnowHeight(Double snowHeight) {
		this.snowHeight = snowHeight;
	}

}
