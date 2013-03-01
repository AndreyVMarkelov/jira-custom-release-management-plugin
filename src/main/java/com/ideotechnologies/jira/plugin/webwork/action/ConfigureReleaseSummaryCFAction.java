package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.config.managedconfiguration.ManagedConfigurationItemService;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.web.action.admin.customfields.AbstractEditConfigurationItemAction;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;
import com.ideotechnologies.jira.plugin.utils.helpers.UtilsHelper;

import java.util.Collection;
import java.util.Map;

public class ConfigureReleaseSummaryCFAction extends AbstractEditConfigurationItemAction {

    protected ConfigureReleaseSummaryCFAction(ManagedConfigurationItemService managedConfigurationItemService) {
        super(managedConfigurationItemService);
    }

    @Override
	protected String doExecute() throws Exception {
		
		String projectIdInput = request.getParameter("projectVersionId");
		String issueTypeIdInput = request.getParameter("issueTypeId");
		
		//Validation Test
		if (projectIdInput != null && issueTypeIdInput != null  ) {
			if(UtilsHelper.isNumber(projectIdInput) && UtilsHelper.isNumber(issueTypeIdInput)){
			FieldConfig fieldConfig = getFieldConfig();
			
			//Store the input values 
			GenericValueDAO.setProjectId(fieldConfig.getId(), Long.valueOf(projectIdInput));
			GenericValueDAO.setIssueTypeId(fieldConfig.getId(), Long.valueOf(issueTypeIdInput));
			
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

    @Override
    public Collection<String> getErrorMessages() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getErrors() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
