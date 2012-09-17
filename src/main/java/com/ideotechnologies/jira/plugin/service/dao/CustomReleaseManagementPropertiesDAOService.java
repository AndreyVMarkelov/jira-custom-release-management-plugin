package com.ideotechnologies.jira.plugin.service.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.sal.api.websudo.WebSudoRequired;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;


public class CustomReleaseManagementPropertiesDAOService {
	
	private static final Long ENTITY_ID = 865326547L;
	
	/**
	 * PropertySet called to get acess with the database
	 * get propertySet or create one if doesn't exist
	 * @param entityId entity id which strore in database
	 * @return PropertySet
	 */
	 static PropertySet getPS() {
		HashMap ofbizArgs = new HashMap();
		ofbizArgs.put("delegator.name", "default");
		ofbizArgs.put("entityName", "CustomReleaseManagementPluginProperties");
		ofbizArgs.put("entityId", ENTITY_ID);
		PropertySet ofbizPs = PropertySetManager.getInstance("ofbiz", ofbizArgs);//ofbiz --> declarer dans le fichier propertyset.xml
																					//TODO verifier la bonne utilisation
		return ofbizPs;

	}
	
	public static Map<String,String> getInputPropertiesMap(){
		PropertySet ps = getPS();
		Map<String,String> dBMap = new HashMap<String, String>();
		List<String> keys = (List<String>) ps.getKeys();
		for (String key : keys) {
            if (!key.endsWith("_ext")) {
                dBMap.put(key, ps.getString(key));
            }
		}
		dBMap.remove("reIndexInputValues"); //Patch redendance (TODO ameliorer)
		return dBMap;
		
	}
    public static Map<String,String> getExtensionPropertiesMap(){
        PropertySet ps = getPS();
        Map<String,String> dBMap = new HashMap<String, String>();
        List<String> keys = (List<String>) ps.getKeys();
        for (String key : keys) {
            if (key.endsWith("_ext")) {
                dBMap.put(key, ps.getString(key));
            }
        }
        return dBMap;

    }
    public static void setInputPropertiesMap(Map<String,String> propertiesMap){
		PropertySet ps = getPS();
		Set<String> inputSet = propertiesMap.keySet();
		
		//Traitement remove keys
		Set<String> dBSet = getInputPropertiesMap().keySet();
		for (String inputDB : dBSet) {
			if(!inputSet.contains(inputDB)) ps.remove(inputDB);
		}
	
		for (String key : propertiesMap.keySet()) {
			ps.setString(key, propertiesMap.get(key));
		}
		
		
	}
    public static void setExtensionPropertiesMap(Map<String,String> propertiesMap){
        PropertySet ps = getPS();
        Set<String> inputSet = propertiesMap.keySet();

        //Traitement remove keys
        Set<String> dBSet = getExtensionPropertiesMap().keySet();
        for (String inputDB : dBSet) {
            if(!inputSet.contains(inputDB)) ps.remove(inputDB);
        }

        for (String key : propertiesMap.keySet()) {
            ps.setString(key, propertiesMap.get(key));
        }


    }

    public static void setReIndexInputPropertiesValues(String inputValues){
		PropertySet ps = getPS();
		ps.setString("reIndexInputValues", inputValues);
	}
	
	public static String getReIndexInputPropertiesVaues(){
		PropertySet ps = getPS();
		String inputValues = ps.getString("reIndexInputValues");
		if(inputValues == null)return "";
		return inputValues;
		
	}
}
