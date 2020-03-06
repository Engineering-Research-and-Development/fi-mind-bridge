package it.eng.fimind.model.fiware.alert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;

public class AlertNormalized {
	@NotNull(message = "{alert.null.id}")
	private String id;
	@NotNull(message = "{alert.null.type}")
	@Pattern(message = "{alert.wrong.type}", regexp = "Alert", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	@NotNull(message = "{alert.null.category}")
	@Pattern(message = "{alert.wrong.category}", regexp = "traffic|naturalDisaster|weather|environment|health|security|agriculture", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute category;
	@Pattern(message = "{alert.wrong.subCategory}", regexp = "trafficJam| carAccident| carWrongDirection| carStopped|pothole|roadClosed|roadWorks|hazardOnRoad|injuredBiker,"
			+ "												flood|tsunami|coastalEvent|earthquake,"
			+ "												rainfall|highTemperature|lowTemperature|heatWave|coldWave|ice|snow|wind|fog|tornado|tropicalCyclone|hurricane|snow|ice|thunderstorms|fireRisk|avalancheRisk|floodRisk,"
			+ "												airPollution|waterPollution|pollenConcentration,"
			+ "												asthmaAttack|bumpedPatient|fallenPatient|heartAttack,"
			+ "												suspiciousAction|robbery|assault|civilDisorder|buildingFire|forestFire,"
			+ "												noxiousWeed|snail|insect|rodent|bacteria|microbe|fungus,mite|virus|nematodes|irrigation|fertilisation", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute suCategory;
	private LocationNormalized loation;
	private AddressNormalized address;
	@NotNull(message = "{alert.null.dateIssued}")
	private Attribute dateIssued;
	private Attribute validFrom;
	private Attribute validTo;
	private Attribute description;
	@NotNull(message = "{alert.null.alertSource}")
	private Attribute alertSource;
	private Attribute data;
	@Pattern(message = "{alert.wrong.severity}", regexp = "informational|low|medium|high|critical", flags=Pattern.Flag.CASE_INSENSITIVE)
	private Attribute severity;
	
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
	public Attribute getCategory() {
		return category;
	}
	public void setCategory(Attribute category) {
		this.category = category;
	}
	public Attribute getSuCategory() {
		return suCategory;
	}
	public void setSuCategory(Attribute suCategory) {
		this.suCategory = suCategory;
	}
	public LocationNormalized getLoation() {
		return loation;
	}
	public void setLoation(LocationNormalized loation) {
		this.loation = loation;
	}
	public AddressNormalized getAddress() {
		return address;
	}
	public void setAddress(AddressNormalized address) {
		this.address = address;
	}
	public Attribute getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(Attribute dateIssued) {
		this.dateIssued = dateIssued;
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
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public Attribute getAlertSource() {
		return alertSource;
	}
	public void setAlertSource(Attribute alertSource) {
		this.alertSource = alertSource;
	}
	public Attribute getData() {
		return data;
	}
	public void setData(Attribute data) {
		this.data = data;
	}
	public Attribute getSeverity() {
		return severity;
	}
	public void setSeverity(Attribute severity) {
		this.severity = severity;
	}
	
}