package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.config.managedconfiguration.ManagedConfigurationItemService;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.web.action.admin.customfields.AbstractEditConfigurationItemAction;
import com.ideotechnologies.jira.plugin.service.dao.GenericValueDAO;
import com.ideotechnologies.jira.plugin.utils.helpers.UtilsHelper;

import java.util.Collection;
import java.util.Map;

public class ConfigureParentFieldCFAction extends
        AbstractEditConfigurationItemAction {

    protected ConfigureParentFieldCFAction(ManagedConfigurationItemService managedConfigurationItemService) {
        super(managedConfigurationItemService);
    }

    @Override
    protected String doExecute() throws Exception {
        String optionSelectedInput = request.getParameter("parentOptionFieldTypeId");
        String fieldTypeIdInput = request.getParameter("parentCFTypeId");
        if(optionSelectedInput == null)return "input";
        int optionValue = Integer.parseInt(optionSelectedInput);
        FieldConfig fieldConfig = getFieldConfig();
        switch (optionValue) {
            case 0 :
            case 1 :
            case 2 :
                //Store the input values
                GenericValueDAO.setOptionFieldType(fieldConfig.getId(), Long.valueOf(optionSelectedInput));
                break;

            default:
                if (fieldTypeIdInput == null || !UtilsHelper.isNumber(fieldTypeIdInput) ) {
                    return "input";
                }
                GenericValueDAO.setOptionFieldType(fieldConfig.getId(), Long.valueOf(optionSelectedInput));
                GenericValueDAO.setFieldToDisplayId(fieldConfig.getId(), Long.valueOf(fieldTypeIdInput));
                break;
        }
        // Redirect to the custom field configuration screen
        setReturnUrl("/secure/admin/ConfigureCustomField!default.jspa?customFieldId="
                + getFieldConfig().getCustomField().getIdAsLong().toString());

        return getRedirect("not used");
    }

    public String getFieldId(){
        return GenericValueDAO.getFieldToDisplayId(getFieldConfigId()).toString();

    }
    public String getOptionFieldType(){
        return GenericValueDAO.getOptionFieldType(getFieldConfigId()).toString();
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
