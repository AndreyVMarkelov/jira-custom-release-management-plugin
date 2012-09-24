package com.ideotechnologies.jira.plugin.workflow.validator;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.ideotechnologies.jira.plugin.event.listener.IssueCreatedUpdatedListener;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;
import org.ofbiz.core.entity.GenericEntityException;
import webwork.util.ClassLoaderUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ValidatorIsVersionExist implements Validator {
	 private static final String PROPERTIES_FILE = "projects_map.properties";
	
	 
	 public Properties getProjectKeyProperties(){
		 Properties properties = new Properties();
		 InputStream stream = ClassLoaderUtils.getResourceAsStream(PROPERTIES_FILE, IssueCreatedUpdatedListener.class);
		 if( stream == null )
			{
				URL resource = ClassLoaderUtils.getResource( PROPERTIES_FILE, IssueCreatedUpdatedListener.class );
				File propFile = new File( resource.getFile() );
				try {
					stream = new FileInputStream( propFile );
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		 if (stream != null)
			{
				try {
					properties.load( stream );
				} catch (IOException e) {
					e.printStackTrace();
				}
	 }
		 try {
			stream.close(); // check if necessary
		} catch (IOException e) {
			e.printStackTrace();
		}
	return properties;	 
}
	 
	 @Override
	public void validate(Map transientVars, Map arg1, PropertySet ps)
			throws InvalidInputException, WorkflowException {
	
		List<Long>  issueVersionIds = null ;
		IssueManager issueManager = null ;
		Issue issueExpected = (Issue) transientVars.get("issue");
		Project project = issueExpected.getProjectObject();
		 Properties prop = getProjectKeyProperties();  // Load the property file
		 Set<String> projectVersions = prop.stringPropertyNames();
		 String selectedProjectVersionKey = null;
		 for (String projectVersion : projectVersions) {
			 String propValue = prop.getProperty(projectVersion);
			 if(propValue == null){return;}
			 String[] arrayPropValue = propValue.split(",");
			 List<String> listPropValue =Arrays.asList(arrayPropValue);
			 if(listPropValue.contains(project.getKey())){
				 selectedProjectVersionKey = projectVersion;
				 break;}
				 
		}
		 if(selectedProjectVersionKey == null)return;
		 
	
		 issueManager = ComponentManager.getInstance().getIssueManager();
		ProjectManager projectManager = ComponentManager.getInstance().getProjectManager();
		Project projectVersion = projectManager.getProjectObjByKey(selectedProjectVersionKey);
		if(projectVersion == null)return; // Case where the linked project version key doesn't exist
		try {
			issueVersionIds = (List<Long>) issueManager.getIssueIdsForProject(projectVersion.getId());
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		List<Version> versionsList = (List<Version>) issueExpected.getFixVersions();
		if(versionsList.isEmpty())return;
		
		 for (Long issueVersionId : issueVersionIds) {
			 Issue projectVersionIssue = issueManager.getIssueObject(issueVersionId);
			if(projectVersionIssue.getSummary().equals(versionsList.get(0).getName())){
				if(projectVersionIssue.isSubTask()){return;}
			}
		}
		 throw new InvalidInputException("Version \""+versionsList.get(0).getName()+"\" must exist in project \""+projectVersion.getName()+"\" !" +
		 		" Contact your PDM.");
		
		
	}

}
