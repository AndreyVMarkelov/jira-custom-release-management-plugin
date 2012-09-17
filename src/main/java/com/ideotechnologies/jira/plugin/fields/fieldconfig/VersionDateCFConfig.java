package com.ideotechnologies.jira.plugin.fields.fieldconfig;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.project.Project;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

public class VersionDateCFConfig implements FieldConfigItemType {

	@Override
	public String getBaseEditUrl() {
		return "EditVersionDateCFConfig.jspa";
	}

	@Override
	public Object getConfigurationObject(Issue arg0, FieldConfig arg1) {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Context input options";
	}

	@Override
	public String getDisplayNameKey() {
		return "Input options";
	}

	@Override
	public String getObjectKey() {
		return "inputOptions";
	}

	@Override
	public String getViewHtml(FieldConfig fieldConfig, FieldLayoutItem arg1) {
		Long projectId =  GenericValueDAO.getProjectId(fieldConfig.getId());
		Long issueTypeId =  GenericValueDAO.getIssueTypeId(fieldConfig.getId());
		Long fieldId=  GenericValueDAO.getFieldToDisplayId(fieldConfig.getId());
		return "<ul>" +
				"<li>Project Version Id : "+projectId+" </li>"+
				"<li>Issue Type Id : "+issueTypeId+" </li>"+
				"<li>Field Id to display : "+fieldId+" </li>"+
				"</ul>";
	}

}
