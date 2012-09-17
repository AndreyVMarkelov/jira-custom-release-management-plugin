package com.ideotechnologies.jira.plugin.fields.customfields;

import java.util.List;
import java.util.Map;

import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.ideotechnologies.jira.plugin.fields.fieldconfig.ReleaseFieldConfig;
import com.ideotechnologies.jira.plugin.service.JiraBusinessService;

public class ReleaseSummaryCalculatedCF extends AbstractGenericCalculatedCF {

private JiraBusinessService jiraBusinessService;
	public ReleaseSummaryCalculatedCF() {
		super();
		this.jiraBusinessService = new JiraBusinessService();
	}


	@Override
	public Object getValueFromIssue(CustomField field, Issue issue) {
		Issue issueSelected =null;
		if (issue == null) {
			return "";
		}
		
		//if(getFirstFixVersionByIssue(issue)== null){return null;}
		 issueSelected = jiraBusinessService.getIssueSubtaskByFirstFixVersion(issue, field);
		
		if(issueSelected == null){
			return "Version not found";
			}
		
		return issueSelected.getParentObject().getSummary();
	}
	
	
	@Override
	public List getConfigurationItemTypes() {
		final List<FieldConfigItemType> configItemType   = super.getConfigurationItemTypes();
		configItemType.add(new ReleaseFieldConfig());
		return configItemType;
	}

	

}
