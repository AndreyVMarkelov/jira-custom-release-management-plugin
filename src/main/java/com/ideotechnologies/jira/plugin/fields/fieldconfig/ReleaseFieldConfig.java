package com.ideotechnologies.jira.plugin.fields.fieldconfig;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

public class ReleaseFieldConfig implements FieldConfigItemType {

	@Override
	public String getBaseEditUrl() {
		return "EditReleaseSummaryCFConfig.jspa";
	}

	@Override
	public Object getConfigurationObject(Issue arg0, FieldConfig arg1) {
		return "Context  release input options";
	}

	@Override
	public String getDisplayName() {
		return "Release Input options";
	}

	@Override
	public String getDisplayNameKey() {
		return "Release Input options";
	}

	@Override
	public String getObjectKey() {
		return "ReleaseInputOption";
	}

	@Override
	public String getViewHtml(FieldConfig fieldConfig, FieldLayoutItem arg1) {
		Long projectId =  GenericValueDAO.getProjectId(fieldConfig.getId());
		Long issueTypeId =  GenericValueDAO.getIssueTypeId(fieldConfig.getId());
		return "<ul>" +
				"<li>Project Version Id : "+projectId+" </li>"+
				"<li>Issue Type Id : "+issueTypeId+" </li>"+
				"</ul>";
	}

}
