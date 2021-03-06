/**
 * Name: close-issues.groovy
 * @author jalex
 * @version 1.0
 *
 * A Groovy script to automatically close issues in Done status after 5 days
 */

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.IssueService.TransitionValidationResult;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

log = Logger.getLogger("com.onresolve.jira.groovy.TransitionWorkflow");
log.setLevel(Level.INFO); // to debug script, update Level to Level.DEBUG
log.info("Starting process to close old done issues");
ComponentManager cm = ComponentManager.getInstance();

// Global variables to be updated before utilising the script
int daysSinceResolved = 5 ; // days to check for issue closure
int actionId = 701; // transition id from Done to Closed
String project = "TEST, JIRA"; // target project key. To include more than 1 project, include the project keys in quotes
String previousStatus = "Done"; // status to check before closing
String username = "admin"; // acting user who has the privilege to transition issues to Closed
String comment = "Automatically closing issue since it has been resolved for ${daysSinceResolved} days.";

// clause: in given project(s) && Done && resolved after 5 days
JqlClauseBuilder builder = JqlQueryBuilder.newClauseBuilder();
builder = builder.project().in(project).and().status(previousStatus);
builder = builder.and().not().resolutionDateAfter("-${daysSinceResolved}d");
Query query = builder.buildQuery();
log.debug("Query: ${query}");

User user = cm.getUserUtil().getUser(username);
PagerFilter filter = PagerFilter.getUnlimitedFilter();
Collection issues = cm.getSearchProvider().search(query,user,filter).getIssues();

if (issues.size() == 0)
    log.info("No issues in ${previousStatus} status for the past ${daysSinceResolved} days to close.");
else {
    log.info("Found ${issues.size()} issue(s) in ${previousStatus} status for the past ${daysSinceResolved} days in need of closure");
    cm.getJiraAuthenticationContext().setLoggedInUser(user);

    for (Issue issue in issues) {
        log.info("Closing ${issue.key}: ${issue.summary}");
        IssueInputParameters params = cm.getIssueService().newIssueInputParameters();
        params.setRetainExistingValuesWhenParameterNotProvided(true);
        if (comment) params.setComment(comment);
        log.debug("Action parameters: ${params.actionParameters}");

        TransitionValidationResult validation = cm.getIssueService().validateTransition(user,issue.getId(),actionId,params);
        log.debug("Addition inputs: ${validation.additionInputs}");
        if (validation.isValid()) {
            IssueResult result = cm.getIssueService().transition(user,validation);
            if (result.isValid()) log.debug("${result.errorCollection}");
            else log.error("${result.errorCollection}");
        } else {
            result = null;
            log.error("Errors: ${validation.errorCollection.errors}");
        }
    }

    log.info("Done closing ${issues.size()} issue(s)");
}