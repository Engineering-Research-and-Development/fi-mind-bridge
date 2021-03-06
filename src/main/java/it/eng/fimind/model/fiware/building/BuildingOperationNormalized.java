package it.eng.fimind.model.fiware.building;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.eng.fimind.model.fiware.common.Attribute;
import it.eng.fimind.model.fiware.common.MultiAttribute;

public class BuildingOperationNormalized {
	@NotNull(message = "{buildingOperation.null.id}")
	private String id;
	@Pattern(message = "{buildingOperation.wrong.type}", regexp = "BuildingOperation", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private Attribute source;
	private Attribute dataProvider;
	private Attribute dateModified;
	private Attribute dateCreated;
	private Attribute description;
	@NotNull(message = "{buildingOperation.null.refBuilding}")
	private Attribute refBuilding;
	@NotNull(message = "{buildingOperation.null.refOperator}")
	private Attribute refOperator;
	private Attribute operationType;
	private Attribute status;
	private Attribute result;
	private MultiAttribute operationSequence;
	private MultiAttribute refRelatedBuildingOperation;
	@NotNull(message = "{buildingOperation.null.startDate}")
	private Attribute startDate;
	@NotNull(message = "{buildingOperation.null.endDate}")
	private Attribute 	endDate;
	private Attribute dateStarted;
	private Attribute dateFinished;
	private MultiAttribute refRelatedDeviceOperation;
	
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
	public Attribute getDescription() {
		return description;
	}
	public void setDescription(Attribute description) {
		this.description = description;
	}
	public Attribute getRefBuilding() {
		return refBuilding;
	}
	public void setRefBuilding(Attribute refBuilding) {
		this.refBuilding = refBuilding;
	}
	public Attribute getRefOperator() {
		return refOperator;
	}
	public void setRefOperator(Attribute refOperator) {
		this.refOperator = refOperator;
	}
	public Attribute getOperationType() {
		return operationType;
	}
	public void setOperationType(Attribute operationType) {
		this.operationType = operationType;
	}
	public Attribute getStatus() {
		return status;
	}
	public void setStatus(Attribute status) {
		this.status = status;
	}
	public Attribute getResult() {
		return result;
	}
	public void setResult(Attribute result) {
		this.result = result;
	}
	public MultiAttribute getOperationSequence() {
		return operationSequence;
	}
	public void setOperationSequence(MultiAttribute operationSequence) {
		this.operationSequence = operationSequence;
	}
	public MultiAttribute getRefRelatedBuildingOperation() {
		return refRelatedBuildingOperation;
	}
	public void setRefRelatedBuildingOperation(MultiAttribute refRelatedBuildingOperation) {
		this.refRelatedBuildingOperation = refRelatedBuildingOperation;
	}
	public Attribute getStartDate() {
		return startDate;
	}
	public void setStartDate(Attribute startDate) {
		this.startDate = startDate;
	}
	public Attribute getEndDate() {
		return endDate;
	}
	public void setEndDate(Attribute endDate) {
		this.endDate = endDate;
	}
	public Attribute getDateStarted() {
		return dateStarted;
	}
	public void setDateStarted(Attribute dateStarted) {
		this.dateStarted = dateStarted;
	}
	public Attribute getDateFinished() {
		return dateFinished;
	}
	public void setDateFinished(Attribute dateFinished) {
		this.dateFinished = dateFinished;
	}
	public MultiAttribute getRefRelatedDeviceOperation() {
		return refRelatedDeviceOperation;
	}
	public void setRefRelatedDeviceOperation(MultiAttribute refRelatedDeviceOperation) {
		this.refRelatedDeviceOperation = refRelatedDeviceOperation;
	}

}
