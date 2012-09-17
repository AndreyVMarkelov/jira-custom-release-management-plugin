package com.ideotechnologies.jira.plugin.service;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;
import org.ofbiz.core.entity.GenericEntityException;

import java.util.List;

/**
 * @author ideotechnologies.com
 * @since 1.1
 * @version 2.1
 * @description This class provide all business method for modules in this plugins,
 * as a service on 3-tiers system.This allows to separate treatment related to 
 * the Custom Release Management business logic of the other treatments.
 */
public class JiraBusinessService {

	private ComponentManager componentManager ;
    protected JiraBusinessService jiraBusinessService;

	public JiraBusinessService(){
        //this.componentManager = ComponentManager.getInstance();
	}

	/**
	 * Get instance of customFieldManager 
	 * @return customFieldManager
	 */
	public  CustomFieldManager getCustomFieldManager(){
//        if (componentManager == null) {
//           componentManager = ComponentManager.getInstance();
//        }
//		return componentManager.getCustomFieldManager();
        return ComponentManager.getInstance().getCustomFieldManager();
	}

	/**
	 * Get field value for the given issue and fieldConfig object
	 * @param issueSelected
	 * @param fieldConfig
	 * @return Object Can type it on Velocity template,according to Transport Object
	 */
	public Object getFieldValueFromIssue(Issue issueSelected, FieldConfig fieldConfig){
		Object customFieldValue = null;;
		// FieldConfig fieldConfig = field.getRelevantConfig(issueSelected);
		if(fieldConfig == null)return null; //Resolve anomalies about issue link
		CustomField cf = getCustomFieldManager().getCustomFieldObject(
				GenericValueDAO.getFieldToDisplayId(fieldConfig.getId()));
		//TODO controle de la valuer de cf (si null traiter l'exception et log)
		if(!cf.hasValue(issueSelected)){
			return null;
		}
		else{customFieldValue = issueSelected.getCustomFieldValue(cf);
		}

		return customFieldValue;
	}


	/**
	 * Get the Issue  located in the project Version ,
	 * corresponding to the current custumField (parameter) FixVersion
	 * @param issue current issue
	 * @param field current custom field
	 * @return the matched issue
	 * @throws GenericEntityException
	 */
	public Issue getIssueSubtaskByFirstFixVersion(Issue issue,CustomField field) {
		//TODO scinder la methode en plusieurs petites methodes
		Issue issueSelected = null;
		Version fixVersion;
		//TODO log pertinent avant l'envoi de la valeur null
		//log :Fix version non renseignee
		if((fixVersion = getFirstFixVersionByIssue(issue))== null){return null;}
		//Version fixVersion = getFirstFixVersionByIssue(issue);
		//if(fixVersion==null){return "None";}
		FieldConfig fieldConfig = field.getRelevantConfig(issue);
		Project projectVersion = getProjectVersion(fieldConfig);
		if(projectVersion == null){return null;}
		List<Long> issueIds = null;
		try {
			issueIds = (List<Long>) ComponentManager.getInstance().getIssueManager().getIssueIdsForProject(projectVersion.getId());
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Long issueId : issueIds){
			Issue issueExpected = ComponentManager.getInstance().getIssueManager().getIssueObject(issueId);
			if(Long.valueOf(issueExpected.getIssueTypeObject().getId()).equals(GenericValueDAO.getIssueTypeId(fieldConfig.getId()))){
				if(issueExpected.getSummary().equals(fixVersion.getName())){
					//TODO log pertinent avant l'envoi de la valeur null
					//log : L'issue n'est pas une subtask
					if(!issueExpected.isSubTask()) return null;
					issueSelected = issueExpected;
					break;
				}
			}
		}
		return issueSelected;
	}
	/**
	 * Get the fixVersion given the issue
	 * @param issue
	 * @return Version object
	 */
	public Version getFirstFixVersionByIssue(Issue issue){
		List<Version> fixVersions = (List<Version>) issue.getFixVersions();
		if(!fixVersions.isEmpty()){
			return fixVersions.get(0);}
		return null;
	}


	/**
	 * Get the project Version associated with the fieldConfig
	 * @param fieldConfig
	 * @return Project object
	 */
	public Project getProjectVersion(FieldConfig fieldConfig){	
		if(fieldConfig == null) return null;
		Long  projectId = GenericValueDAO.getProjectId(fieldConfig.getId());
		Project projectVersion = ComponentManager.getInstance().getProjectManager().
				getProjectObj(projectId);
		return projectVersion;
	}

}
