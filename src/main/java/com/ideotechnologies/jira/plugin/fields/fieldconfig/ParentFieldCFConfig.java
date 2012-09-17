package com.ideotechnologies.jira.plugin.fields.fieldconfig;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

public class ParentFieldCFConfig implements FieldConfigItemType {

	@Override
	public String getBaseEditUrl() {
		
		return "EditParentFieldCFConfig.jspa";
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
		return "parentfield-input-option";
	}

	@Override
	public String getViewHtml(FieldConfig fieldConfig, FieldLayoutItem arg1) {
		List<String> optionList = new ArrayList<String>();
		optionList.add("Summary");
		optionList.add("Parent Key");
		optionList.add("Fix Version");
		optionList.add("Parent CustomField");
		Long selectedOption = GenericValueDAO.getOptionFieldType(fieldConfig.getId());
		Long parentCFId = GenericValueDAO.getFieldToDisplayId(fieldConfig.getId());
		switch (selectedOption.intValue()) {
		case 0:
		case 1:
		case 2:
			return "<p>Type of parent field to display : "+optionList.get(selectedOption.intValue())+"  </p>";
		case 3 :
			return "<p>Type of parent field to display : "+optionList.get(selectedOption.intValue())+"  </p>"+
		"<p>Input parent custom field id : "+parentCFId+"</p>";
		default:
			break;
		}
		return "Type of parent field to display : No input value";
	}

}
