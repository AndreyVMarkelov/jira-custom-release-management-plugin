package com.ideotechnologies.jira.plugin.webwork.action.conditions;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractJiraCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.ideotechnologies.jira.plugin.service.dao.CustomReleaseManagementPropertiesDAOService;


public class IsVersionOrReleaseIssueCondition extends AbstractJiraCondition {
	 private String releaseName;
	 private String versionName;

	@Override
	public boolean shouldDisplay(User arg0, JiraHelper jiraHelper) {
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
		
		  Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
		  String IssueTypeNameExpected = currentIssue.getIssueTypeObject().getName();
		  if(IssueTypeNameExpected.equalsIgnoreCase(releaseName) || IssueTypeNameExpected.equalsIgnoreCase(versionName)) return true;
		return false;
	}

}
