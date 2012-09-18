package com.ideotechnologies.jira.plugin.workflow.function;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.index.IndexException;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.ideotechnologies.jira.plugin.service.dao.CustomReleaseManagementPropertiesDAOService;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ofbiz.core.entity.GenericValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: stephane.genin
 * Date: 25/06/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorSubTaskCreation extends AbstractJiraFunctionProvider {

    protected static Log log = LogFactory.getLog(ValidatorSubTaskCreation.class);

    protected final IssueFactory issueFactory;
    protected final ConstantsManager constantsManager;
    protected final IssueManager issueManager;
    protected final JiraAuthenticationContext authenticationContext;
    protected final SubTaskManager subTaskManager;

    public ValidatorSubTaskCreation(IssueFactory issueFactory, ConstantsManager constantsManager, IssueManager issueManager, JiraAuthenticationContext authenticationContext, SubTaskManager subTaskManager) {
        this.issueFactory = issueFactory;
        this.constantsManager = constantsManager;
        this.issueManager = issueManager;
        this.authenticationContext = authenticationContext;
        this.subTaskManager = subTaskManager;
    }


    @Override
    public void execute(Map transcientVars, Map args, PropertySet ps) throws WorkflowException {
        //To change body of implemented methods use File | Settings | File Templates.
        // MutableIssue issue = getIssue(transcientVars);
        MutableIssue issue = getIssue(transcientVars);

        Map<String,String> prop = CustomReleaseManagementPropertiesDAOService.getExtensionPropertiesMap();
        String projectVersionKey = issue.getProjectObject().getKey();

        String childSummary=null;

        String extension = prop.get(projectVersionKey+"_ext"); //Recuperation de l'extension du projet mappé

        //Issue originalIssue=getIssue(transcientVars);
        //Issue parentIssue=originalIssue;
        if ((extension != null) && (extension.toUpperCase().compareTo("NONE") != 0)) {

            Issue parentIssue=getIssue(transcientVars);

            if (parentIssue.getIssueTypeObject().isSubTask()){
                log.debug("Parent issue is a subtask, so can't create subtask for : " + parentIssue.getKey());
                return;
            }

            MutableIssue issueObject = issueFactory.getIssue();
            issueObject.setProjectId(parentIssue.getProjectObject().getId());

            issueObject.setIssueType(constantsManager.getIssueTypeObject((String)args.get("subIssueTypeId")).getGenericValue());
            issueObject.setPriority(parentIssue.getPriority());
            issueObject.setReporter(parentIssue.getReporterUser());

            childSummary=parentIssue.getSummary()+extension;
            issueObject.setSummary(childSummary);

            Map<String,Object> params = new HashMap<String, Object>();
            params.put("issue",issueObject);

            try {
                GenericValue subTask=issueManager.createIssue(authenticationContext.getLoggedInUser(),params);
                GenericValue parent=parentIssue.getGenericValue();
                subTaskManager.createSubTaskIssueLink(parent,subTask,authenticationContext.getLoggedInUser());
                ComponentManager.getInstance().getIndexManager().reIndex(subTask);
            } catch (CreateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IndexException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
