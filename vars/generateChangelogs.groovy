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
def changelogs(Integer maxLogCount = 9) {
    def buffer = []
    // def PIVOTAL_TICKET_PATTERN = /(\#(\d{9}))/
    // def JIRA_TICKET_PATTERN = /([A-Z]{3,10}\-\d{4,})/
    // def PIVOTAL_BASE_URL = 'https://www.pivotaltracker.com/story/show/'
    // def JIRA_BASE_URL = 'https://theknotww.atlassian.net/browse/'

    def sets = currentBuild.changeSets
    def previousFailedBuild = currentBuild.previousFailedBuild

    if (previousFailedBuild != null && currentBuild.getNumber() == previousFailedBuild.getNumber() + 1) {
        sets.addAll(previousFailedBuild.changeSets)
    }

    for (changeSet in sets) {
        if (buffer.size() > maxLogCount) break

        for (entry in changeSet.items) {
            if (buffer.size() > maxLogCount) break

            buffer << "- ${entry.msg} by ${entry.author.displayName}"
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
    return "${changelogs().join('\n')}"
}