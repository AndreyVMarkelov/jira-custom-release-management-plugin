package com.ideotechnologies.jira.plugin.fields.customfields;

import java.util.List;

import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

public abstract class AbstractGenericCalculatedCF extends CalculatedCFType {

	@Override
	public Object getSingularObjectFromString(String arg0)
			throws FieldValidationException {
		return null;
	}

	
	@Override
	public String getStringFromSingularObject(Object arg0) {
		return null;
	}

	@Override
	public Object getValueFromIssue(CustomField arg0, Issue arg1) {
		return "";
	}
	
	
}
