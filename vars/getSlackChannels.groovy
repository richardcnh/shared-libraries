#!/usr/bin/env groovy

def call(squadName) {
    def SLACK_CHANNEL_SQUAD_TEMPLATE = "#sdc-mobile-{0}"
    def SLACK_CHANNEL_ANDROID_APK = "#android-builds"
    def ALL_VALID_SQUADS = ['core', 'guest', 'local', 'registry']
    def squads = ALL_VALID_SQUADS
    def channels = []

    if (squadName) {
        squads = [squadName]
    }

    for (squad in squads) {
        channels << SLACK_CHANNEL_SQUAD_TEMPLATE.replace("{0}", squad)
    }

    if (ALL_VALID_SQUADS.size() == channels.size()) {
        squads << SLACK_CHANNEL_ANDROID_APK
    }

    return channels
}