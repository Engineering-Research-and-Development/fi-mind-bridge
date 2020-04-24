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
	
	public VariableDefinition.DataTypeEnum getVariableDefinitionDataType(String type){
		if(type.equalsIgnoreCase("String"))
			return VariableDefinition.DataTypeEnum.STRING;
		else if(type.equalsIgnoreCase("Double"))
			return VariableDefinition.DataTypeEnum.DOUBLE;
		else if(type.equalsIgnoreCase("Integer"))
			return VariableDefinition.DataTypeEnum.INT;
		else if(type.equalsIgnoreCase("Boolean"))
			return VariableDefinition.DataTypeEnum.BOOLEAN;
		else if(type.equalsIgnoreCase("Timestamp"))
			return VariableDefinition.DataTypeEnum.TIMESTAMP;
		return VariableDefinition.DataTypeEnum.STRING;
	}
	
	public Location fiLocAddrToMiLocation(it.eng.fimind.model.fiware.common.Location fiLocation, it.eng.fimind.model.fiware.common.Address fiAddress) {
		Location mindSphereLocation = new Location();
		if(fiLocation.getType().equals("Point")) {
			mindSphereLocation.setLatitude(BigDecimal.valueOf(Double.valueOf(fiLocation.getCoordinates().get(0).toString())));
			mindSphereLocation.setLongitude(BigDecimal.valueOf(Double.valueOf(fiLocation.getCoordinates().get(1).toString())));
		}
		mindSphereLocation.setCountry(fiAddress.getAddressCountry());
		mindSphereLocation.setRegion(fiAddress.getAddressRegion());
		mindSphereLocation.setLocality(fiAddress.getAddressLocality());
		mindSphereLocation.setStreetAddress(fiAddress.getStreetAddress());
		mindSphereLocation.setPostalCode(fiAddress.getPostalCode());
		return mindSphereLocation;	
	}
	
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
	
	public List<VariableDefinition> fiPropertiesToMiVariablesDefinitions(List<String> keys, List<String> values, List<String> dataTypes)
	{
		List<VariableDefinition> assetVariablesDefinition = new ArrayList<VariableDefinition>();
		
		for(int i=0;i<keys.size();i++) {
			VariableDefinition varDefinition = new VariableDefinition();
			varDefinition.setName(keys.get(i));
			varDefinition.setDataType(getVariableDefinitionDataType(dataTypes.get(i)));
			if(dataTypes.get(i).equalsIgnoreCase("String")) {
				varDefinition.setLength(255);
			}
			assetVariablesDefinition.add(varDefinition);
		}
		
		return assetVariablesDefinition;
	}
	
	public List<Variable> fiPropertiesToMiVariables(List<String> keys, List<String> values, List<String> dataTypes)
	{
		List<Variable> assetVariables = new ArrayList<Variable>();
		
		for(int i=0;i<keys.size();i++) {	
			Variable var = new Variable();			
			var.setName(keys.get(i));	
			if(dataTypes.get(i).equalsIgnoreCase("Timestamp")) {
				String timestamp = values.get(i);
				if(!timestamp.contains("\\."))
					var.setValue(timestamp);
				else
					var.setValue(timestamp.split("\\.")[0].concat("Z"));
			}else
				var.setValue(values.get(i));
			assetVariables.add(var);
		}
		
		return assetVariables;
	}
	
	public AspectType fiStateToMiAspectType(String id, String description, List<String> keys, List<String> values, List<String> dataTypes)
	{
		
		AspectType aspectType = new AspectType();
		
		aspectType.setName(id);
		aspectType.setDescription(description);
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> aspectVariables = new ArrayList<AspectVariable>();
		
		for(int i=0; i<keys.size();i++) {
			AspectVariable var = new AspectVariable();
			var.setName(keys.get(i));
			var.setUnit(values.get(i));
			var.setDataType(getAspectVariableDataType(dataTypes.get(i)));
			if(dataTypes.get(i).equalsIgnoreCase("String")) {
				var.setLength(255);
			}
			var.setSearchable(true);
			var.setQualityCode(true);
			aspectVariables.add(var);
		}
		
		aspectType.setVariables(aspectVariables);
		
		return aspectType;
	}
}

//You cannot use the following standard names: id, name, description, tenant, etag, scope, properties, propertySets, extends, variables, aspects, parentTypeId, timezone, type, parent
