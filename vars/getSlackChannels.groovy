#!/usr/bin/env groovy

/*
 * Get all slack channels we would like to post message
 *
 * @param {String} squadName One of value in core, local, guest, registry
 * @return {Array} Slack channel list
 *
 * No Dependencies
 */
def call(String squadName) {
    def SQUAD_SLACK_CHANNEL_TEMPLATE = "#sdc-mobile-{0}"
    def ANDROID_TEAM_SLACK_CHANNEL = "#android_planner_betas"
    def ALL_VALID_SQUADS = ['core', 'guest', 'local', 'registry']
    def squads = []
    def channels = []

    if (squadName) {
        squads = [squadName]
    }

    for (squad in squads) {
        channels << SQUAD_SLACK_CHANNEL_TEMPLATE.replace("{0}", squad)
    }

    // // If all tk mobile squads are notified, we should also notify in the public notification channel.
    // if (ALL_VALID_SQUADS.size() == channels.size()) {
    //     channels << ANDROID_TEAM_SLACK_CHANNEL
    // }

    // If there is no specific channels to be notified, we should notify the public notification channel
    if (channels.size() <= 0) {
        channels << ANDROID_TEAM_SLACK_CHANNEL
    }

    return channels
}