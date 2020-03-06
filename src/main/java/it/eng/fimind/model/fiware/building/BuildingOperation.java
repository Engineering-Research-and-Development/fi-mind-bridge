package it.eng.fimind.model.fiware.building;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BuildingOperation {
	@NotNull(message = "{buildingOperation.null.id}")
	private String id;
	@Pattern(message = "{buildingOperation.wrong.type}", regexp = "BuildingOperation", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String type;
	private String source;
	private String dataProvider;
	private String dateModified;
	private String dateCreated;
	private String description;
	@NotNull(message = "{buildingOperation.null.refBuilding}")
	private String refBuilding;
	@NotNull(message = "{buildingOperation.null.refOperator}")
	private String refOperator;
	private String operationType;
	private String status;
	private String result;
	private List<String> operationSequence;
	private List<String> refRelatedBuildingOperation;
	@NotNull(message = "{buildingOperation.null.startDate}")
	private String startDate;
	@NotNull(message = "{buildingOperation.null.endDate}")
	private String 	endDate;
	private String dateStarted;
	private String dateFinished;
	private List<String> refRelatedDeviceOperation;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRefBuilding() {
		return refBuilding;
	}
	public void setRefBuilding(String refBuilding) {
		this.refBuilding = refBuilding;
	}
	public String getRefOperator() {
		return refOperator;
	}
	public void setRefOperator(String refOperator) {
		this.refOperator = refOperator;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<String> getOperationSequence() {
		return operationSequence;
	}
	public void setOperationSequence(List<String> operationSequence) {
		this.operationSequence = operationSequence;
	}
	public List<String> getRefRelatedBuildingOperation() {
		return refRelatedBuildingOperation;
	}
	public void setRefRelatedBuildingOperation(List<String> refRelatedBuildingOperation) {
		this.refRelatedBuildingOperation = refRelatedBuildingOperation;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDateStarted() {
		return dateStarted;
	}
	public void setDateStarted(String dateStarted) {
		this.dateStarted = dateStarted;
	}
	public String getDateFinished() {
		return dateFinished;
	}
	public void setDateFinished(String dateFinished) {
		this.dateFinished = dateFinished;
	}
	public List<String> getRefRelatedDeviceOperation() {
		return refRelatedDeviceOperation;
	}
	public void setRefRelatedDeviceOperation(List<String> refRelatedDeviceOperation) {
		this.refRelatedDeviceOperation = refRelatedDeviceOperation;
	}
	
}
