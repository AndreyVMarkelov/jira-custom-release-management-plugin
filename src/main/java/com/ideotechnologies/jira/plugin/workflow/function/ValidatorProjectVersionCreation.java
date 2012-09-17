package com.ideotechnologies.jira.plugin.workflow.function;

import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.ideotechnologies.jira.plugin.service.dao.CustomReleaseManagementPropertiesDAOService;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: stephane.genin
 * Date: 26/06/12
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */

public class ValidatorProjectVersionCreation extends AbstractJiraFunctionProvider {

    protected final VersionManager versionManager;
    protected final ProjectManager projectManager;

    public ValidatorProjectVersionCreation(VersionManager versionManager, ProjectManager projectManager) {
        this.versionManager = versionManager;
        this.projectManager = projectManager;
    }

    @Override
    public void execute(Map transcientVars, Map map1, PropertySet propertySet) throws WorkflowException {
        //To change body of implemented methods use File | Settings | File Templates.
        MutableIssue issue = getIssue(transcientVars);

        Long projectID = issue.getProjectObject().getId();
        String summary=issue.getSummary();

        Version version;

        Map<String,String> prop = CustomReleaseManagementPropertiesDAOService.getInputPropertiesMap();
        String projectVersionKey = issue.getProjectObject().getKey();
        String propValue = prop.get(projectVersionKey); //Recuperation de la key du projet mappé
        if(propValue == null){
            return;
        }
        String[] arrayPropValue = propValue.split(",");
        List<String> listPropValue = Arrays.asList(arrayPropValue);


        for (String keyProjectValue : listPropValue) {
            if (projectManager.getProjectObjByKey(keyProjectValue.trim()) != null) {
                Long mainProjectID = projectManager.getProjectObjByKey(keyProjectValue.trim()).getId();

                version=versionManager.getVersion(mainProjectID,summary);

                if (version == null) {
                    try {
                        version=versionManager.createVersion(summary,null,summary,mainProjectID,null);
                    }  catch (CreateException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }

    }
}
