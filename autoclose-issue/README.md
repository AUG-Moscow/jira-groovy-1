# Auto Close Issues After Set Period of Days

This is a groovy script that checks and perform auto closure of issues after a set period of time. The script is set up as a Groovy service, which runs every day at midnight.

When the service kick starts, it will execute a JQL statement to check for issues to close. The results of this JQL statement may differ, depending on your project setup.

On a basic level, it is set to check for **issues from projects _TEST_ and _JIRA_, which are marked as Done for more than 5 days, should be closed**.

If you would like to adjust this script for your own instance use, you can modify the following areas:

    int daysSinceResolved = 5 ; // days to check for issue closure
    int actionId = 701; // transition id from Done to Closed
    String project = "TEST, JIRA"; // target project key. To include more than 1 project, include the project keys in quotes
    String previousStatus = "Done"; // status to check before closing
    String username = "admin"; // acting user who has the privilege to transition issues to Closed
    
For more information on this script, you can refer to my site post: [Automatically Close Issues in Done Status After 5 Days in JIRA](http://justinalex.com/automatically-close-issues-in-done-status-after-5-days/) 