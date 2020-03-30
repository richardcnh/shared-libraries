#!/usr/bin/env groovy


/*
 * Send build start message with specified format
 *
 * @param {String} channel, It indicates which Slack channel(s) you would like to post
 *
 * Dependencies:
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

    slackSend(
        message: "$REPO_NAME (${env.BRANCH_NAME}) - Job Started (<${env.BUILD_URL}|${env.BUILD_DISPLAY_NAME}>)",
        channel: channel
    )
}