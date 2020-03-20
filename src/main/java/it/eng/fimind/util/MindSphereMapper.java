package it.eng.fimind.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;

public class MindSphereMapper {
	
	public Location fiLocationToMiLocation(it.eng.fimind.model.fiware.common.Location fiLocation) {
		Location mindSphereLocation = new Location();
		if(fiLocation.getType().equals("Point")) {
			mindSphereLocation.setLatitude(BigDecimal.valueOf(Double.valueOf(fiLocation.getCoordinates().get(0).toString())));
			mindSphereLocation.setLongitude(BigDecimal.valueOf(Double.valueOf(fiLocation.getCoordinates().get(1).toString())));
		}
		return mindSphereLocation;	
	}
	
	public Location fiAddressToMiLocation(it.eng.fimind.model.fiware.common.Address fiAddress) {
		Location mindSphereLocation = new Location();
		mindSphereLocation.setCountry(fiAddress.getAddressCountry());
		mindSphereLocation.setRegion(fiAddress.getAddressRegion());
		mindSphereLocation.setLocality(fiAddress.getAddressLocality());
		mindSphereLocation.setStreetAddress(fiAddress.getStreetAddress());
		mindSphereLocation.setPostalCode(fiAddress.getPostalCode());
		return mindSphereLocation;	
	}
	
	public List<VariableDefinition> fiPropertiesToMiVariablesDefinitions(List<String> keys, List<String> values)
	{
		
		List<VariableDefinition> assetVariablesDefinition = new ArrayList<VariableDefinition>();
		
		for(int i=0;i<keys.size();i++) {
			if(keys.get(i) == null) continue;

			VariableDefinition varDefinition = new VariableDefinition();
			varDefinition.setName(keys.get(i));
			varDefinition.setDataType(VariableDefinition.DataTypeEnum.STRING);
			//mindSphereVariableDefinition.setLength(128);
			assetVariablesDefinition.add(varDefinition);
		}
		
		return assetVariablesDefinition;
	}
	
	public List<Variable> fiPropertiesToMiVariables(List<String> keys, List<String> values)
	{
		
		List<Variable> assetVariables = new ArrayList<Variable>();
		
		for(int i=0;i<keys.size();i++) {	
			if(keys.get(i) == null) continue;

			Variable var = new Variable();			
			var.setName(keys.get(i));			
			var.setValue((values.get(i) == null) ? "" : values.get(i));
			assetVariables.add(var);
		}
		
		return assetVariables;
	}
	
	public AspectVariable.DataTypeEnum getAspectVariableDataType(String type){
		if(type.equalsIgnoreCase("String"))
			return AspectVariable.DataTypeEnum.STRING;
		else if(type.equalsIgnoreCase("Double"))
			return AspectVariable.DataTypeEnum.DOUBLE;
		else if(type.equalsIgnoreCase("Integer"))
			return AspectVariable.DataTypeEnum.INT;
		else if(type.equalsIgnoreCase("Boolean"))
			return AspectVariable.DataTypeEnum.BOOLEAN;
		else if(type.equalsIgnoreCase("Timestamp"))
			return AspectVariable.DataTypeEnum.TIMESTAMP;
		return AspectVariable.DataTypeEnum.STRING;
	}
	
	public AspectType fiStateToMiAspectType(String id, String description, List<String> keys, List<String> values, List<String> dataTypes)
	{
		
		AspectType aspectType = new AspectType();
		
		aspectType.setName(id+"Aspect");
		aspectType.setDescription(description);
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> aspectVariables = new ArrayList<AspectVariable>();
		
		for(int i=0; i<keys.size();i++) {
			AspectVariable var = new AspectVariable();
			var.setName(keys.get(i));
			var.setUnit(values.get(i));
			var.setDataType(getAspectVariableDataType(dataTypes.get(i)));
			//var.setLength(128);
			var.setSearchable(true);
			var.setQualityCode(true);
			aspectVariables.add(var);
		}
				
		aspectType.setVariables(aspectVariables);
		
		return aspectType;
	}
}