#!/usr/bin/env groovy

/*
 * Generate friendly file name based on current branch name
 * @param {String} buildType: It indicate which environment we would like to build app for
 * Remarks:
 *   As current logic, we only want to handle the branch name with prefix "features/",
 *   but I would like to extend it to something like "angan/190326-refactor_timeline",
 * Some examples as below:
 *   angan/190326-refactor_timeline => Planner-qa-190326-refactor_timeline-1.apk
 *   dev-core => Planner-stg-core-1.apk
 *   features/core-deep-link => Planner-stg-core-deep-link-1.apk
 *   test-deep-link => Planner-stg-test-deep-link-1.apk
 */
def call(String buildType) {
    def branchName = env.BRANCH_NAME
    // Analyse to get squad name
    def squadName = getSquadName(branchName)
    // Analyse to get feature description,
    // feature description should include squad name
    def sections = branchName.split('/')
    // TODO: Since this description will be used in file name, so we should consider to filter some special words.
    def featureDescription = sections[sections.size() - 1].replaceFirst(/^(local|guest|core|registry)\-/, '')

    return "Planner-${buildType}-${squadName + (squadName ? '-' : '')}${featureDescription + (featureDescription ? '-' : '')}${env.BUILD_NUMBER}.apk"
}