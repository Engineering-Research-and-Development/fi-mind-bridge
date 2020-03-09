package it.eng.fimind.model.fiware.weather;

import it.eng.fimind.model.fiware.common.Attribute;

public class DayMeasurementsNormalized {
	private Attribute temperature;
	private Attribute feelsLikeTemperature;
	private Attribute relativeHumidity;
	
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

}
