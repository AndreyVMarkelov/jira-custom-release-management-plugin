package com.ideotechnologies.jira.plugin.fields.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.project.version.Version;
import com.ideotechnologies.jira.plugin.fields.fieldconfig.ReleaseFieldConfig;
import com.ideotechnologies.jira.plugin.service.JiraBusinessService;

import java.util.List;

public class ReleaseSummaryCalculatedCF extends AbstractGenericCalculatedCF {

private JiraBusinessService jiraBusinessService;
	public ReleaseSummaryCalculatedCF() {
		super();
		this.jiraBusinessService = new JiraBusinessService();
	}


	@Override
    public Object getValueFromIssue(CustomField field, Issue issue) {
        Issue issueSelected =null;
        Issue parentSelected =null;
        Version fixVersion;
        if (issue == null) {
            return "";
        }

        //if(getFirstFixVersionByIssue(issue)== null){return null;}
        issueSelected = jiraBusinessService.getIssueSubtaskByFirstFixVersion(issue, field);

        if(issueSelected == null){

            fixVersion = jiraBusinessService.getFirstFixVersionByIssue(issue);
            if (fixVersion != null)
                return fixVersion.toString();
            else
                return "";
        }
        else {
            return issueSelected.getParentObject().getSummary();
        }
    }

	
	@Override
	public List getConfigurationItemTypes() {
		final List<FieldConfigItemType> configItemType   = super.getConfigurationItemTypes();
		configItemType.add(new ReleaseFieldConfig());
		return configItemType;
	}

	

}
