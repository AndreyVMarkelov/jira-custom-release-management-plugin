package com.ideotechnologies.jira.plugin.workflow.function;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
//import sun.security.x509.Extension;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: stephane.genin
 * Date: 25/06/12
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class WorkFlowAutoSubTaskCreationFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory  {

    public static final String SUBISSUETYPEID = "subIssueTypeId";

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> stringObjectMap) {
        stringObjectMap.put(SUBISSUETYPEID,17);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> stringObjectMap, AbstractDescriptor abstractDescriptor) {
        stringObjectMap.put(SUBISSUETYPEID,getSubIssueTypeId(abstractDescriptor));
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> stringObjectMap, AbstractDescriptor abstractDescriptor) {
        stringObjectMap.put(SUBISSUETYPEID,getSubIssueTypeId(abstractDescriptor));
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, ?> getDescriptorParams(Map stringObjectMap) {
        if (stringObjectMap !=null) {

            return EasyMap.build(SUBISSUETYPEID,extractSingleParam(stringObjectMap,SUBISSUETYPEID));
        }
        return EasyMap.build(SUBISSUETYPEID,17);
    }

    private String getSubIssueTypeId(AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a function descriptor.");
        }
        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;

        String subIssueTypeId = (String)functionDescriptor.getArgs().get(SUBISSUETYPEID);

        return subIssueTypeId;
    }

}
