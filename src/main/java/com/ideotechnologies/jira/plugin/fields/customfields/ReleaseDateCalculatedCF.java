package com.ideotechnologies.jira.plugin.fields.customfields;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.DateField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.util.DateFieldFormat;
import com.ideotechnologies.jira.plugin.fields.fieldconfig.VersionDateCFConfig;
import com.ideotechnologies.jira.plugin.service.JiraBusinessService;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

import java.util.List;
import java.util.Map;

public class ReleaseDateCalculatedCF extends AbstractGenericCalculatedCF
		implements DateField {
private JiraBusinessService jiraBusinessService ;

	public ReleaseDateCalculatedCF() {
	super();
	this.jiraBusinessService = new JiraBusinessService();
}

	@Override
	public Object getValueFromIssue(CustomField field, Issue issue) {
		Issue issueSelected = null;
		Object customFieldValue = null ;

		
		if (issue == null) {//For reindexing
			return "";
		}

			/*/
			//log : Fix version not set
			if(getFirstFixVersionByIssue(issue)== null){return null;}*/

			//log : version issue related to current issue not found
			issueSelected = jiraBusinessService.getIssueSubtaskByFirstFixVersion(issue, field);
			if(issueSelected == null){
				return null;
				}
			/*
			// log : issue is not a subtask
			if(!issueSelected.isSubTask())return null;*/

			Issue parentIssue = issueSelected.getParentObject();
			//FieldConfig fieldConfig = field.getRelevantConfig(issue);
			//jiraBusinessService.getFieldValueFromIssue(parentIssue, fieldConfig)	;
			FieldConfig fieldConfig = field.getRelevantConfig(issue);

			//log warn: fieldconfig is null
			if(fieldConfig == null)return null; //Resolve anomalies about issue link
			
			CustomField cf = ComponentManager.getInstance().getCustomFieldManager().getCustomFieldObject(
					GenericValueDAO.getFieldToDisplayId(fieldConfig.getId()));

			//log : Custom field value not set
			if(!cf.hasValue(parentIssue)){
				return null;
			}
			else{customFieldValue = parentIssue.getCustomFieldValue(cf);}
		
			return customFieldValue;
	}

	@Override
	public Map<String, Object> getVelocityParameters(Issue issue,
			CustomField field, FieldLayoutItem fieldLayoutItem) {
		Map<String, Object> velocityParameters = super.getVelocityParameters(issue, field, fieldLayoutItem);
		DateFieldFormat dateFieldFormat = ComponentAccessor.getComponentOfType(DateFieldFormat.class);
		 velocityParameters.put("dateFieldFormat", dateFieldFormat);
		return velocityParameters;
	}

	@Override
	public List getConfigurationItemTypes() {
		final List<FieldConfigItemType> configItemType   = super.getConfigurationItemTypes();
		configItemType.add(new VersionDateCFConfig());
		return configItemType;
	}
	
}
