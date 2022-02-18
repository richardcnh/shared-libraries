#!/usr/bin/env groovy

/*
 * Send build message with new changelogs
 *
 * @param {String} channel, It indicates which Slack channel(s) you would like to post
 * @param {String} buildType, It indicates current build type, such as Debug, Stg, Release.
 *
 * Dependencies:
 *     - BRANCH_NAME
 *     - BUILD_NUMBER
 */
def call(channel, buildType = "Debug") {
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
                "text": "${generateChangelogsForSlack(20)}",
                "fields": [
                    [
                        "title": "App Name",
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
                        "title": "Build Type",
                        "value": "${buildType}",
                        "short": true
                    ]
                ]
            ]
        ]
    )
}