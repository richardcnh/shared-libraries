#!/usr/bin/env groovy

def call(channel) {
    if (!channel) {
        error('Missing channel to send message to')
        return
    }

    def elapsedTime = durationString.replace(' and counting', '');
    def slackColor = result == 'SUCCESS' ? 'good' : 'danger';

    slackSend(
        color: slackColor,
        message: "$REPO_NAME (${env.BRANCH_NAME}) - ${currentBuild.result} after ${currentBuild.elapsedTime} (<${env.BUILD_URL}|${env.BUILD_DISPLAY_NAME}>)",
        channel: channel
    )
}