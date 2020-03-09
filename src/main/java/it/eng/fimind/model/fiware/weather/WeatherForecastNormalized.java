package it.eng.fimind.model.fiware.weather;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;

public class WeatherForecastNormalized {
	@NotNull(message = "{weatherforecast.null.id}")
	private String id;
	@Pattern(message = "{weatherforecast.wrong.type}", regexp = "WeatherForecast", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute dataProvider;
	private Attribute dataModified;
	private Attribute dataCreated;
	private Attribute name;
	private LocationNormalized location;
	private AddressNormalized address;
	@NotNull(message = "{weatherforecast.null.dateRetrieved}")
	private Attribute dateRetrieved;
	@NotNull(message = "{weatherforecast.null.dateIssued}")
	private Attribute dateIssued;
	private Attribute validity;
	private Attribute validFrom;
	private Attribute validTo;
	private Attribute source;
	private Attribute refPointOfInterest;
	@Pattern(message = "{weatherforecast.wrong.weatherType}", regexp = "clearNight|sunnyDay|slightlyCloudy|partlyCloudy|mist|fog|highClouds|cloudy|veryCloudy|overcast|lightRainShower|drizzle|lightRain|heavyRainShower|heavyRain|sleetShower|sleet|hailShower|hail|shower|lightSnow|snow|heavySnowShower|heavySnow|thunderShower|thunder", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute weatherType;
	@Pattern(message = "{weatherforecast.wrong.visibility}", regexp = "veryPoor|poor|moderate|good|veryGood|excellent", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute visibility;
	private Attribute temperature;
	private Attribute feelsLikeTemperature;
	private Attribute relativeHumidity;
	private Attribute precipitationProbability;
	private Attribute windDirection;
	private Attribute windSpeed;
	private DayMeasurementsNormalized dayMinimum;
	private DayMeasurementsNormalized dayMaximum;
	private Attribute uVIndexMax;
	
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
	public Attribute getDataModified() {
		return dataModified;
	}
	public void setDataModified(Attribute dataModified) {
		this.dataModified = dataModified;
	}
	public Attribute getDataCreated() {
		return dataCreated;
	}
	public void setDataCreated(Attribute dataCreated) {
		this.dataCreated = dataCreated;
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
	public Attribute getDateRetrieved() {
		return dateRetrieved;
	}
	public void setDateRetrieved(Attribute dateRetrieved) {
		this.dateRetrieved = dateRetrieved;
	}
	public Attribute getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(Attribute dateIssued) {
		this.dateIssued = dateIssued;
	}
	public Attribute getValidity() {
		return validity;
	}
	public void setValidity(Attribute validity) {
		this.validity = validity;
	}
	public Attribute getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Attribute validFrom) {
		this.validFrom = validFrom;
	}
	public Attribute getValidTo() {
		return validTo;
	}
	public void setValidTo(Attribute validTo) {
		this.validTo = validTo;
	}
	public Attribute getSource() {
		return source;
	}
	public void setSource(Attribute source) {
		this.source = source;
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
	public Attribute getFeelsLikeTemperature() {
		return feelsLikeTemperature;
	}
	public void setFeelsLikeTemperature(Attribute feelsLikeTemperature) {
		this.feelsLikeTemperature = feelsLikeTemperature;
	}
	public Attribute getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Attribute relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Attribute getPrecipitationProbability() {
		return precipitationProbability;
	}
	public void setPrecipitationProbability(Attribute precipitationProbability) {
		this.precipitationProbability = precipitationProbability;
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
	public DayMeasurementsNormalized getDayMinimum() {
		return dayMinimum;
	}
	public void setDayMinimum(DayMeasurementsNormalized dayMinimum) {
		this.dayMinimum = dayMinimum;
	}
	public DayMeasurementsNormalized getDayMaximum() {
		return dayMaximum;
	}
	public void setDayMaximum(DayMeasurementsNormalized dayMaximum) {
		this.dayMaximum = dayMaximum;
	}
	public Attribute getuVIndexMax() {
		return uVIndexMax;
	}
	public void setuVIndexMax(Attribute uVIndexMax) {
		this.uVIndexMax = uVIndexMax;
	}

}
