package it.eng.fimind.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;

public class MindSphereMapper {
	
	public Location fiLocationToMiLocation(it.eng.fimind.model.fiware.common.Location fiLocation) {
		Location mindSphereLocation = new Location();
		if(fiLocation.getType().equals("Point")) {
			mindSphereLocation.setLatitude((BigDecimal) fiLocation.getCoordinates().get(0));
			mindSphereLocation.setLongitude((BigDecimal) fiLocation.getCoordinates().get(1));
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
	
	public List<Variable> fiPropertiesToMiVariables(List<String> keys, List<String> values)
	{
		
		List<Variable> assetVariables = new ArrayList<Variable>();
		
		for(int i=0;i<keys.size();i++) {
			Variable mindSphereVariable = new Variable();
			mindSphereVariable.setName(keys.get(i));
			mindSphereVariable.setValue(values.get(i));
			assetVariables.add(mindSphereVariable);
		}
		
		return assetVariables;
	}
	
	public AspectType fiStateToMiAspectType(String id, String description, List<String> keys, List<String> values)
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
			var.setDataType(DataTypeEnum.STRING);
			var.setLength(20);
			var.setUnit(values.get(i));
			var.setSearchable(true);
			var.setQualityCode(true);
			aspectVariables.add(var);
		}
				
		aspectType.setVariables(aspectVariables);
		
		return aspectType;
	}
}