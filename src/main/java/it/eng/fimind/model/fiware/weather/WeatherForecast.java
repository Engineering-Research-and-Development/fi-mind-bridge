package it.eng.fimind.model.fiware.weather;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Address;
import it.eng.fimind.model.fiware.common.Location;

public class WeatherForecast {
	@NotNull(message = "{weatherforecast.null.id}")
	private String id;
	@Pattern(message = "{weatherforecast.wrong.type}", regexp = "WeatherForecast", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String dataProvider;
	private String dataModified;
	private String dataCreated;
	private String name;
	private Location location;
	private Address address;
	@NotNull(message = "{weatherforecast.null.dateRetrieved}")
	private String dateRetrieved;
	@NotNull(message = "{weatherforecast.null.dateIssued}")
	private String dateIssued;
	private String validity;
	private String validFrom;
	private String validTo;
	private String source;
	private String refPointOfInterest;
	@Pattern(message = "{weatherforecast.wrong.weatherType}", regexp = "clearNight|sunnyDay|slightlyCloudy|partlyCloudy|mist|fog|highClouds|cloudy|veryCloudy|overcast|lightRainShower|drizzle|lightRain|heavyRainShower|heavyRain|sleetShower|sleet|hailShower|hail|shower|lightSnow|snow|heavySnowShower|heavySnow|thunderShower|thunder", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String weatherType;
	@Pattern(message = "{weatherforecast.wrong.visibility}", regexp = "veryPoor|poor|moderate|good|veryGood|excellent", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String visibility;
	private Double temperature;
	private Double feelsLikeTemperature;
	private Double relativeHumidity;
	private Double precipitationProbability;
	private Double windDirection;
	private Double windSpeed;
	private DayMeasurements dayMinimum;
	private DayMeasurements dayMaximum;
	private Double uVIndexMax;
	
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
	public String getDataModified() {
		return dataModified;
	}
	public void setDataModified(String dataModified) {
		this.dataModified = dataModified;
	}
	public String getDataCreated() {
		return dataCreated;
	}
	public void setDataCreated(String dataCreated) {
		this.dataCreated = dataCreated;
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
	public String getDateRetrieved() {
		return dateRetrieved;
	}
	public void setDateRetrieved(String dateRetrieved) {
		this.dateRetrieved = dateRetrieved;
	}
	public String getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public Double getFeelsLikeTemperature() {
		return feelsLikeTemperature;
	}
	public void setFeelsLikeTemperature(Double feelsLikeTemperature) {
		this.feelsLikeTemperature = feelsLikeTemperature;
	}
	public Double getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Double getPrecipitationProbability() {
		return precipitationProbability;
	}
	public void setPrecipitationProbability(Double precipitationProbability) {
		this.precipitationProbability = precipitationProbability;
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
	public DayMeasurements getDayMinimum() {
		return dayMinimum;
	}
	public void setDayMinimum(DayMeasurements dayMinimum) {
		this.dayMinimum = dayMinimum;
	}
	public DayMeasurements getDayMaximum() {
		return dayMaximum;
	}
	public void setDayMaximum(DayMeasurements dayMaximum) {
		this.dayMaximum = dayMaximum;
	}
	public Double getuVIndexMax() {
		return uVIndexMax;
	}
	public void setuVIndexMax(Double uVIndexMax) {
		this.uVIndexMax = uVIndexMax;
	}


}
