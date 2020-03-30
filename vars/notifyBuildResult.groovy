#!/usr/bin/env groovy

/*
 * Send build result message with specified format
 *
 * @param {String} channel, It indicates which Slack channel(s) you would like to post
 *
 * Dependencies:
 *     - currentBuild
 *     - REPO_NAME
 *     - BRANCH_NAME
 *     - BUILD_URL
 *     - BUILD_DISPLAY_NAME
 */
def call(String channel) {
    if (!channel) {
        error('Missing channel to send message to')
        return
    }

    def elapsedTime = currentBuild.durationString.replace(' and counting', '');
    def slackColor = currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger';

    slackSend(
        color: slackColor,
        message: "$REPO_NAME (${env.BRANCH_NAME}) - ${currentBuild.currentResult} after $elapsedTime (<${env.BUILD_URL}|${env.BUILD_DISPLAY_NAME}>)",
        channel: channel
    )
}