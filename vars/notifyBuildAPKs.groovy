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

    for (changeSet in currentBuild.changeSets) {
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
 * Send build message with APK links
 *
 * @param {String} channel, It indicates which Slack channel(s) you would like to post
 * @param {String} buildType, e.g. "QA"
 * @param {String} apkURI, e.g. "https://applinks.com/qa.apk"
 *
 * Dependencies:
 *     - BRANCH_NAME
 *     - BUILD_NUMBER
 */
def call(channel, buildType, apkURI) {
    def SLACK_APK_TEMPLATE = """<{1}|Download {0} App>"""
    def executedBuilds = [["${buildType}", "${apkURI}"]]

    if (!channel) {
        error('Missing channel to send message to')

        return
    }

    slackSend(
        channel: channel,
        "attachments": [
            [
                "mrkdwn_in": ["text"],
                "color": "#36a64f",
                "pretext": "${buildType} App Build Succeeded!",
                // "author_name": "Richard Cai",
                "title": "Changelogs",
                // "title_link": "https://www.theknot.com",
                "text": "${changelogsForSlack().join('\n')}",
                "fields": [
                    [
                        "title": "App",
                        "value": "${env.REPO_NAME}",
                        "short": true
                    ],
                    [
                        "title": "Branch",
                        "value": "${env.BRANCH_NAME}",
                        "short": true
                    ],
                    [
                        "title": "Build Number",
                        "value": "${env.BUILD_NUMBER}",
                        "short": true
                    ],
                    [
                        "title": "Artifacts",
                        "value": "${reachTemplate(executedBuilds, SLACK_APK_TEMPLATE).join('\n')}",
                        "short": false
                    ]
                ]
            ]
        ]
    )
}