#!/usr/bin/env groovy

/*
 * Get all email addresses we would like to send message
 *
 * @param {String} squadName One of value in core, local, guest, registry
 * @return {Array} Email address list
 *
 * No Dependencies
 */
def call(String squadName) {
    def SQUAD_EMAIL_TEMPLATE = "android-planner-${squad}-build.group@xogrp.com"
    def ANDROID_TEAM_EMAIL = "android-planner-betas.group@xogrp.com"
    def ALL_VALID_SQUADS = ['core', 'guest', 'local', 'registry']
    def squads = ALL_VALID_SQUADS
    def emails = []

    if (squadName) {
        squads = [squadName]
    }

    for (squad in squads) {
        emails << SQUAD_EMAIL_TEMPLATE.replace("{0}", squad)
    }

    if (ALL_VALID_SQUADS.size() == emails.size()) {
        squads << ANDROID_TEAM_EMAIL
    }

    return emails
}