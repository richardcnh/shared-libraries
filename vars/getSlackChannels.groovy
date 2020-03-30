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
    def ANDROID_APK_SLACK_CHANNEL = "#android-builds"
    def ALL_VALID_SQUADS = ['core', 'guest', 'local', 'registry']
    def squads = ALL_VALID_SQUADS
    def channels = []

    if (squadName) {
        squads = [squadName]
    }

    for (squad in squads) {
        channels << SQUAD_SLACK_CHANNEL_TEMPLATE.replace("{0}", squad)
    }

    if (ALL_VALID_SQUADS.size() == channels.size()) {
        squads << ANDROID_APK_SLACK_CHANNEL
    }

    return channels
}