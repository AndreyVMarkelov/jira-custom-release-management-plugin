package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.websudo.WebSudoRequired;
import com.ideotechnologies.jira.plugin.service.dao.CustomReleaseManagementPropertiesDAOService;

import java.util.HashMap;
import java.util.Map;

@WebSudoRequired
public class ConfigureCustomReleaseManagementPluginAction extends JiraWebActionSupport {
	
private Map<String,String> mapInputs;
private Map<String,String> mapExtensions;

    @Override
	public String doDefault() throws Exception {
		if(!isSystemAdministrator()) return "permissionviolation";
		setMapInputs(CustomReleaseManagementPropertiesDAOService.getInputPropertiesMap());
        setMapExtensions(CustomReleaseManagementPropertiesDAOService.getExtensionPropertiesMap());
		return "input";
	}
	
/*public Map<String,String> getMapInputs(){
	Map<String,String> map = new HashMap<String,String>();
	map.put("test1", "azerty1");
	map.put("test2", "azerty2");
	map.put("test3", "azerty3");
	return map;
	
}*/
	
@Override
protected String doExecute() throws Exception {
	
	if(!isSystemAdministrator()) return "permissionviolation";
	
	Map<String,String> updatedMap = new HashMap<String, String>();
    Map<String,String> updatedExtMap = new HashMap<String, String>(); // Map that stores project extensions for version creation

    int idCount = 0;
	int paramRowsLength = 200;
	String reIndexInputValues = request.getParameter("issuetypes_name");
	for (int i = 0; i < (paramRowsLength/2); i++) { // '/2' --> lines of a set of 2 parameters
		
	if(request.getParameter("input_"+idCount+"_0") != null){
		updatedMap.put(request.getParameter("input_"+idCount+"_0"), request.getParameter("input_"+idCount+"_1"));
        updatedExtMap.put(request.getParameter("input_"+idCount+"_0")+"_ext", request.getParameter("input_"+idCount+"_2"));
    }
	idCount++;
	}
	CustomReleaseManagementPropertiesDAOService.setInputPropertiesMap(updatedMap);
    CustomReleaseManagementPropertiesDAOService.setExtensionPropertiesMap(updatedExtMap);
    CustomReleaseManagementPropertiesDAOService.setReIndexInputPropertiesValues(reIndexInputValues);
	return doDefault();
}


public Map<String, String> getMapInputs() {
	return mapInputs;
}
public Map<String, String> getMapExtensions() {
        return mapExtensions;
    }

public void setMapInputs(Map<String, String> mapInputs) {
	this.mapInputs = mapInputs;
}
public void setMapExtensions(Map<String, String> mapExtensions) {
        this.mapExtensions = mapExtensions;
    }

public String getReIndexInputValues(){
	return CustomReleaseManagementPropertiesDAOService.getReIndexInputPropertiesVaues();
   }
}
