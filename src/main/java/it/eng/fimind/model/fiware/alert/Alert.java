package it.eng.fimind.model.fiware.alert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Address;
import it.eng.fimind.model.fiware.common.Location;

public class Alert {
	@NotNull(message = "{alert.null.id}")
	private String id;
	@NotNull(message = "{alert.null.type}")
	@Pattern(message = "{alert.wrong.type}", regexp = "Alert", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	@NotNull(message = "{alert.null.category}")
	@Pattern(message = "{alert.wrong.category}", regexp = "traffic|naturalDisaster|weather|environment|health|security|agriculture", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String category;
	@Pattern(message = "{alert.wrong.subCategory}", regexp = "trafficJam| carAccident| carWrongDirection| carStopped|pothole|roadClosed|roadWorks|hazardOnRoad|injuredBiker,"
			+ "												flood|tsunami|coastalEvent|earthquake,"
			+ "												rainfall|highTemperature|lowTemperature|heatWave|coldWave|ice|snow|wind|fog|tornado|tropicalCyclone|hurricane|snow|ice|thunderstorms|fireRisk|avalancheRisk|floodRisk,"
			+ "												airPollution|waterPollution|pollenConcentration,"
			+ "												asthmaAttack|bumpedPatient|fallenPatient|heartAttack,"
			+ "												suspiciousAction|robbery|assault|civilDisorder|buildingFire|forestFire,"
			+ "												noxiousWeed|snail|insect|rodent|bacteria|microbe|fungus,mite|virus|nematodes|irrigation|fertilisation", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String suCategory;
	private Location loation;
	private Address address;
	@NotNull(message = "{alert.null.dateIssued}")
	private String dateIssued;
	private String validFrom;
	private String validTo;
	private String description;
	@NotNull(message = "{alert.null.alertSource}")
	private String alertSource;
	private String data;
	@Pattern(message = "{alert.wrong.severity}", regexp = "informational|low|medium|high|critical", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String severity;
	
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSuCategory() {
		return suCategory;
	}
	public void setSuCategory(String suCategory) {
		this.suCategory = suCategory;
	}
	public Location getLoation() {
		return loation;
	}
	public void setLoation(Location loation) {
		this.loation = loation;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAlertSource() {
		return alertSource;
	}
	public void setAlertSource(String alertSource) {
		this.alertSource = alertSource;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}

}
