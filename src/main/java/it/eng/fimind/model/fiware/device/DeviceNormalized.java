package it.eng.fimind.model.fiware.device;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.AddressNormalized;
import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.LocationNormalized;
import it.eng.fimind.model.fiware.common.MultiAttribute;

public class DeviceNormalized {
	@NotNull(message = "{device.null.id}")
	@NotNull(message = "{device.null.id}")
	private String id;
	@NotNull(message = "{device.null.type}")
	@Pattern(message = "{device.wrong.type}", regexp = "Device", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	@NotNull(message = "{device.null.category}")
	private MultiAttribute category;
	@NotNull(message = "{device.null.controlledProperty}")
	private MultiAttribute controlledProperty;
	private MultiAttribute controlledAsset;
	private Attribute mnc;
	private Attribute mcc;
	private MultiAttribute macAddress;
	private MultiAttribute ipAddress;
	private MultiAttribute supportedProtocol;
	private Attribute configuration;
	private LocationNormalized location;
	private Attribute name;
	private Attribute description;
	private Attribute dateInstalled;
	private Attribute dateFirstUsed;
	private Attribute dateManufactured;
	private Attribute hardwareVersion;
	private Attribute softwareVersion;
	private Attribute firmwareVersion;
	private Attribute osVersion;
	private Attribute dateLastCalibration;
	private Attribute serialNumber;
	private Attribute provider;
	private Attribute refDeviceModel;
	private Attribute batteryLevel;
	private Attribute rssi;
	private Attribute deviceState;
	private Attribute dateLastValueReported;
	private Attribute value;
	private Attribute dateModified;
	private Attribute dateCreated;
	private MultiAttribute owner;

	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/Device/doc/spec/index.html
	private Attribute alternateName;
	private AddressNormalized address;
	
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
	public MultiAttribute getCategory() {
		return category;
	}
	public void setCategory(MultiAttribute category) {
		this.category = category;
	}
	public MultiAttribute getControlledProperty() {
		return controlledProperty;
	}
	public void setControlledProperty(MultiAttribute controlledProperty) {
		this.controlledProperty = controlledProperty;
	}
	public MultiAttribute getControlledAsset() {
		return controlledAsset;
	}
	public void setControlledAsset(MultiAttribute controlledAsset) {
		this.controlledAsset = controlledAsset;
	}
	public Attribute getMnc() {
		return mnc;
	}
	public void setMnc(Attribute mnc) {
		this.mnc = mnc;
	}
	public Attribute getMcc() {
		return mcc;
	}
	public void setMcc(Attribute mcc) {
		this.mcc = mcc;
	}
	public MultiAttribute getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(MultiAttribute macAddress) {
		this.macAddress = macAddress;
	}
	public MultiAttribute getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(MultiAttribute ipAddress) {
		this.ipAddress = ipAddress;
	}
	public MultiAttribute getSupportedProtocol() {
		return supportedProtocol;
	}
	public void setSupportedProtocol(MultiAttribute supportedProtocol) {
		this.supportedProtocol = supportedProtocol;
	}
	public Attribute getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Attribute configuration) {
		this.configuration = configuration;
	}
	public LocationNormalized getLocation() {
		return location;
	}
	public void setLocation(LocationNormalized location) {
		this.location = location;
	}
	public Attribute getName() {
		return name;
	}
	public void setName(Attribute name) {
		this.name = name;
	}
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public Attribute getDateInstalled() {
		return dateInstalled;
	}
	public void setDateInstalled(Attribute dateInstalled) {
		this.dateInstalled = dateInstalled;
	}
	public Attribute getDateFirstUsed() {
		return dateFirstUsed;
	}
	public void setDateFirstUsed(Attribute dateFirstUsed) {
		this.dateFirstUsed = dateFirstUsed;
	}
	public Attribute getDateManufactured() {
		return dateManufactured;
	}
	public void setDateManufactured(Attribute dateManufactured) {
		this.dateManufactured = dateManufactured;
	}
	public Attribute getHardwareVersion() {
		return hardwareVersion;
	}
	public void setHardwareVersion(Attribute hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	public Attribute getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(Attribute softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public Attribute getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(Attribute firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public Attribute getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(Attribute osVersion) {
		this.osVersion = osVersion;
	}
	public Attribute getDateLastCalibration() {
		return dateLastCalibration;
	}
	public void setDateLastCalibration(Attribute dateLastCalibration) {
		this.dateLastCalibration = dateLastCalibration;
	}
	public Attribute getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Attribute serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Attribute getProvider() {
		return provider;
	}
	public void setProvider(Attribute provider) {
		this.provider = provider;
	}
	public Attribute getRefDeviceModel() {
		return refDeviceModel;
	}
	public void setRefDeviceModel(Attribute refDeviceModel) {
		this.refDeviceModel = refDeviceModel;
	}
	public Attribute getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(Attribute batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public Attribute getRssi() {
		return rssi;
	}
	public void setRssi(Attribute rssi) {
		this.rssi = rssi;
	}
	public Attribute getDeviceState() {
		return deviceState;
	}
	public void setDeviceState(Attribute deviceState) {
		this.deviceState = deviceState;
	}
	public Attribute getDateLastValueReported() {
		return dateLastValueReported;
	}
	public void setDateLastValueReported(Attribute dateLastValueReported) {
		this.dateLastValueReported = dateLastValueReported;
	}
	public Attribute getValue() {
		return value;
	}
	public void setValue(Attribute value) {
		this.value = value;
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
	
	// not present in the standard https://fiware-datamodels.readthedocs.io/en/latest/Device/Device/doc/spec/index.html
	public Attribute getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(Attribute alternateName) {
		this.alternateName = alternateName;
	}
	public AddressNormalized getAddress() {
		return address;
	}
	public void setAddress(AddressNormalized address) {
		this.address = address;
	}
	
}
