#!/usr/bin/env groovy

/*
 * Generate changelogs for Slack
 *
 * @param {Integer} maxLogCount Define the maximum changelog lines, default value is 9
 * @return {Array} array of changelog
 *
 * Dependencies
 *     - currentBuild
 *     - REPO_NAME
 *     - BRANCH_NAME
 *     - BRANCH_NUMBER
 */
def changelogsForSlack(Integer maxLogCount = 9) {
    def buffer = []
    def PIVOTAL_TICKET_PATTERN = /(\#(\d{9}))/
    def JIRA_TICKET_PATTERN = /([A-Z]{3,10}\-\d{4,})/
    def PIVOTAL_BASE_URL = 'https://www.pivotaltracker.com/story/show/'
    def JIRA_BASE_URL = 'https://theknotww.atlassian.net/browse/'

    def sets = currentBuild.changeSets
    def previousFailedBuild = currentBuild.previousFailedBuild

    if (previousFailedBuild != null && currentBuild.getNumber() == previousFailedBuild.getNumber() + 1) {
        sets.addAll(previousFailedBuild.changeSets)
    }

    for (changeSet in sets) {
        for (entry in changeSet.items) {
            def finalMessage = "${entry.msg} by ${entry.author.displayName} (<${changeSet.browser.url}commit/${entry.commitId}|${entry.commitId.substring(0, 7)}>)"

            if (finalMessage.find(PIVOTAL_TICKET_PATTERN)) {
                finalMessage = finalMessage.replaceAll(PIVOTAL_TICKET_PATTERN, "<${PIVOTAL_BASE_URL}\$2|\$1>")
            }

            if (finalMessage.find(JIRA_TICKET_PATTERN)) {
                finalMessage = finalMessage.replaceAll(JIRA_TICKET_PATTERN, "<${JIRA_BASE_URL}\$1|\$1>")
            }

            // TODO: We will skip the commits without story id, if you don't want to do like that, just remove this line
            // if(finalMessage == entry.msg) continue

            buffer << finalMessage
        }
    }

    if (buffer.size() == 0) buffer << "(No changelogs)"

    // Avoid throwing an exception when there are no enough elements in the array
    if (maxLogCount > buffer.size()) maxLogCount = buffer.size()

    // TODO: This method is ok for groovy, but throw an error when using like this, the error is: "Scripts not permitted to use staticMethod"
    // return buffer[-maxLogCount..-1].reverse() // latest 10 commits

    return buffer
}

/*
 * Generate changelogs with JIRA & github links after build successed.
 *
 */
def call() {
    return "${changelogsForSlack().join('\n')}"
}