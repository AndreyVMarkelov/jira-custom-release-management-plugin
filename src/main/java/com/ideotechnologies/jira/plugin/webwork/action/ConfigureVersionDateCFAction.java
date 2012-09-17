package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.web.action.admin.customfields.AbstractEditConfigurationItemAction;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;
import com.ideotechnologies.jira.plugin.utils.helpers.UtilsHelper;

public class ConfigureVersionDateCFAction extends AbstractEditConfigurationItemAction {

	@Override
	protected String doExecute() throws Exception {

		String projectIdInput = request.getParameter("projectVersionId");
		String issueTypeIdInput = request.getParameter("issueTypeId");
		String fieldIdInput = request.getParameter("fieldId");
		
			//Validation Test
		if (projectIdInput != null && issueTypeIdInput != null && fieldIdInput != null ) {
			if(UtilsHelper.isNumber(projectIdInput) && UtilsHelper.isNumber(issueTypeIdInput) && UtilsHelper.isNumber(fieldIdInput)){
			FieldConfig fieldConfig = getFieldConfig();
			
			//Store the input values 
			GenericValueDAO.setProjectId(fieldConfig.getId(), Long.valueOf(projectIdInput));
			GenericValueDAO.setIssueTypeId(fieldConfig.getId(), Long.valueOf(issueTypeIdInput));
			GenericValueDAO.setFieldToDisplayId(fieldConfig.getId(), Long.valueOf(fieldIdInput));
			
			// Redirect to the custom field configuration screen
			setReturnUrl("/secure/admin/ConfigureCustomField!default.jspa?customFieldId="
					+ getFieldConfig().getCustomField().getIdAsLong().toString());

			return getRedirect("not used");
		}
			}
		return "input";
	}
	public String getProjectId(){
		return GenericValueDAO.getProjectId(getFieldConfigId()).toString();

	}
	public String getIssueTypeId(){
		return GenericValueDAO.getIssueTypeId(getFieldConfigId()).toString();

	}

	public String getFieldId(){
		return GenericValueDAO.getFieldToDisplayId(getFieldConfigId()).toString();

	}
	
}
