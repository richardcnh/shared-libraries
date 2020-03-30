#!/usr/bin/env groovy

/*
 * Retrieve squad name from specified branch name
 *
 * @param {String} branchName Current built branch name
 * @return {String} squad name or empty string if can't find one
 */
def call(String branchName = '') {
    def BRANCH_DEV_PREFIX = 'dev-'
    def BRANCH_FEATURE_PREFIX = 'features/'

    branchName = branchName ?: env.BRANCH_NAME

    if (branchName.startsWith(BRANCH_DEV_PREFIX)) {
        return branchName.substring(BRANCH_DEV_PREFIX.length())
    } else if (branchName.startsWith(BRANCH_FEATURE_PREFIX)) {
        return branchName.substring(BRANCH_FEATURE_PREFIX.length(), branchName.indexOf('-'))
    }

    return ''
}