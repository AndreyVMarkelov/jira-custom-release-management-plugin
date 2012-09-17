package com.ideotechnologies.jira.plugin.workflow.validator;

import java.util.Collections;
import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;

public class WorkFlowVersionExistValidatorFactory extends
		AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {

	@Override
	public Map<String, ?> getDescriptorParams(Map<String, Object> arg0) {
		return Collections.EMPTY_MAP;
	}

	@Override
	protected void getVelocityParamsForEdit(Map<String, Object> arg0,
			AbstractDescriptor arg1) {


	}

	@Override
	protected void getVelocityParamsForInput(Map<String, Object> arg0) {


	}

	@Override
	protected void getVelocityParamsForView(Map<String, Object> arg0,
			AbstractDescriptor arg1) {

	}

}
