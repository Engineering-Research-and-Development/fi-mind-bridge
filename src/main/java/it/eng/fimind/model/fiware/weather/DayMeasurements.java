package it.eng.fimind.model.fiware.weather;

public class DayMeasurements {
	private Double temperature;
	private Double feelsLikeTemperature;
	private Double relativeHumidity;
	
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

}
