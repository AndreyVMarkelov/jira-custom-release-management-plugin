package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.index.IndexException;
import com.atlassian.jira.issue.index.IssueIndexManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.util.ImportUtils;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.ideotechnologies.jira.plugin.service.dao.CustomReleaseManagementPropertiesDAOService;
import org.ofbiz.core.entity.GenericEntityException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ReindexRelatedIssueAction extends JiraWebActionSupport {
	// private static final String PROPERTIES_FILE = "projects_map.properties";
	 private String releaseName;
	 private String versionName;
	 private String issueId;
	 
	 

	@Override
	public String doDefault() throws Exception {
	
		String reIndexInputProp = CustomReleaseManagementPropertiesDAOService.getReIndexInputPropertiesVaues();
		String[] reIndexInputArray = reIndexInputProp.split(",");
		ConstantsManager constantsManager = ComponentManager.getInstance().getConstantsManager();

		IssueType	issueTypeObject0 = constantsManager.getIssueTypeObject(reIndexInputArray[0].trim());
		IssueType	issueTypeObject1 = constantsManager.getIssueTypeObject(reIndexInputArray[1].trim());
			if(issueTypeObject0.isSubTask()){
				versionName = issueTypeObject0.getName();
				releaseName = issueTypeObject1.getName();
		}
			else{
			versionName = 	issueTypeObject1.getName();
			releaseName = issueTypeObject0.getName();
			}
		
		Map map = request.getParameterMap();
		Object[] sum = (Object[]) map.get("id");
		issueId = sum[0].toString();
		
		Issue issue= ComponentManager.getInstance().getIssueManager().getIssueObject(Long.valueOf(issueId));
		 if(issue.getIssueTypeObject().getName().equalsIgnoreCase(versionName)){
		 reIndexForVersions(issue);
	 }
	 if(issue.getIssueTypeObject().getName().equalsIgnoreCase(releaseName)){
		 reIndexForReleases(issue);
	 }
		
		return "success";
	}

	 
	 public void reIndexForVersions(Issue issue){


		 Map<String,String> prop = CustomReleaseManagementPropertiesDAOService.getInputPropertiesMap();
		 String summary = issue.getSummary();
		 String projectVersionKey = issue.getProjectObject().getKey();
		 String propValue = prop.get(projectVersionKey); // Recuperation of the mapped project key
		 if(propValue == null){
			 return;}
		 String[] arrayPropValue = propValue.split(",");
		 List<String> listPropValue =Arrays.asList(arrayPropValue);
		 List<Long> issueIds = null ;
		 List<Version> fixVersions;
		 ComponentManager componentManager = ComponentManager.getInstance();
		 for (String keyProjectValue : listPropValue) {
			 Project project = componentManager.getProjectManager().getProjectObjByKey(keyProjectValue.trim());
			 try {
				 if(project == null){return;}
				 issueIds = (List<Long>) componentManager.getIssueManager().getIssueIdsForProject(project.getId());
			 } catch (GenericEntityException e) {
				 e.printStackTrace();
			 }
			 if(issueIds.isEmpty())return ;
			 List<Issue> issuesSelected = new ArrayList<Issue>();
			 for (Long issueId : issueIds) {
				 Issue issueSelected = componentManager.getIssueManager().getIssueObject(issueId);
				 fixVersions = (List<Version>) issueSelected.getFixVersions();
				 if(!fixVersions.isEmpty() && fixVersions.get(0).getName().equals(summary)){
					 issuesSelected.add(issueSelected);
				 }
			 }

			 reIndexIssues(issuesSelected);
		 }

	 }
	 
	 public void reIndexForReleases(Issue issue){
		List<Issue> issueVersions = (List<Issue>) issue.getSubTaskObjects();
		if(!issueVersions.isEmpty()){
			for (Issue issueVersion : issueVersions) {
				reIndexForVersions(issueVersion);
			}
		}
	 }
	 public void reIndexIssue(final Issue issueObject){
		 IssueIndexManager issueIndexManager = ComponentManager.getInstance().getIndexManager();
		 if (ImportUtils.isIndexIssues()) {
             try {
				issueIndexManager.reIndex(issueObject);
			} catch (IndexException e) {
				e.printStackTrace();
			}
         } else {
             ImportUtils.setIndexIssues(true);
             try {
				issueIndexManager.reIndex(issueObject);
			} catch (IndexException e) {
				e.printStackTrace();
			}
             finally {
                 // Ensure we disable indexes again.
                 ImportUtils.setIndexIssues(false);
             }}
	 }
	 
	 public void reIndexIssues(final List<Issue> issueObjects){
		 IssueIndexManager issueIndexManager = ComponentManager.getInstance().getIndexManager();
		 if (ImportUtils.isIndexIssues()) {
             try {
				issueIndexManager.reIndexIssueObjects(issueObjects);
			} catch (IndexException e) {
				e.printStackTrace();
			}
         } else {
             ImportUtils.setIndexIssues(true);
             try {
				issueIndexManager.reIndexIssueObjects(issueObjects);
			} catch (IndexException e) {
				e.printStackTrace();
			}
             finally {
                 // Ensure we disable indexes again.
                 ImportUtils.setIndexIssues(false);
             }}
	 }
	 
	 @Override
	protected String doExecute() throws Exception {
		return redirect();
	}



	public String getIssueId(){
		 return issueId;
	 }

	 public String redirect(){
		 String issueId = request.getParameter("issueId");
		 String issueKey = ComponentManager.getInstance().getIssueManager().getIssueObject(Long.valueOf(issueId)).getKey();
		return returnCompleteWithInlineRedirect((new StringBuilder()).append("/browse/").append(issueKey).toString());
	 }
}
