#!/usr/bin/env groovy

def call(String channel) {
    if (!channel) {
        error('Missing channel to send message to')

        return
    }


    slackSend(
        message: "$REPO_NAME (${env.BRANCH_NAME}) - Job Started (<${env.BUILD_URL}|${env.BUILD_DISPLAY_NAME}>)",
        channel: channel
    )
}