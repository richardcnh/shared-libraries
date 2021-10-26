#!/usr/bin/env groovy

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
                "text": "${generateChangelogsForSlack()}",
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