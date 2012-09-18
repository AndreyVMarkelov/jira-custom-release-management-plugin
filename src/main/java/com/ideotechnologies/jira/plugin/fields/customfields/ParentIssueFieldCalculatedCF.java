package com.ideotechnologies.jira.plugin.fields.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.project.version.Version;
import com.ideotechnologies.jira.plugin.fields.fieldconfig.ParentFieldCFConfig;
import com.ideotechnologies.jira.plugin.service.JiraBusinessService;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;

import java.util.List;

public class ParentIssueFieldCalculatedCF extends AbstractGenericCalculatedCF {

private JiraBusinessService jiraBusinessService;
	public ParentIssueFieldCalculatedCF() {
		super();
		this.jiraBusinessService = new JiraBusinessService();
		
	}

	@Override
	public Object getValueFromIssue(CustomField field, Issue issue) {
		
		if (issue == null) {//For reindexing
			return "";
		}
		FieldConfig fieldConfig = field.getRelevantConfig(issue);
		if(fieldConfig == null)return null;
		Long parentInputOption = GenericValueDAO.getOptionFieldType(fieldConfig.getId());
		int pInputOption = parentInputOption.intValue();
		if(!issue.isSubTask())return null;
		Issue parentIssue = issue.getParentObject() ;
		
		switch (pInputOption) {
		case 0:
		String parentSummary =parentIssue.getSummary();
			return parentSummary;
			
		case 1 : 
			String parentKey = parentIssue.getKey();//TODO rename variable,not parent Id but parent key
			return parentKey;
			
		case 2 : 
			List<Version> parentFixVersions = (List<Version>) parentIssue.getFixVersions();
			if(parentFixVersions.isEmpty())return null;
			return parentFixVersions.get(0).getName();
			
		case 3 ://Warn,Ne traite pas les custumfield de type date et user evol...
			 parentIssue = issue.getParentObject();
			 Object customFieldValue = jiraBusinessService.getFieldValueFromIssue(parentIssue, fieldConfig);
			/*CustomField cf = ComponentManager.getInstance().getCustomFieldManager().getCustomFieldObject(
					GenericValueDAO.getFieldToDisplayId(fieldConfig.getId()));
			if(!cf.hasValue(parentIssue)){
				
				return null; // case where parent custom field value is not set
			}
			else {return parentIssue.getCustomFieldValue(cf).toString();}*/
			 //TODO optimiser extraire le toString et le traiter ds le template
			if(customFieldValue == null) return null;
			return customFieldValue.toString();
		default:
			break;
		}
		return null;
	}

	@Override
	public List getConfigurationItemTypes() {
		final List<FieldConfigItemType> configItemType   = super.getConfigurationItemTypes();
		configItemType.add(new ParentFieldCFConfig());
		return configItemType;
	}

}
