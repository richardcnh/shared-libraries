#!/usr/bin/env groovy


/*
 * Generate changelogs for Email
 *
 * @return {Array} array of changelog
 */
def changelogsForEmail() {
    def buffer = []
    def commitBaseUrl = "https://github.com/tkww/Planner-Android/commit/"
    buffer << '<ul>'

    for (changeSet in currentBuild.changeSets) {
        for (entry in changeSet.items) {
            buffer << "<li><a href=\"${commitBaseUrl + entry.commitId}\">${entry.commitId.substring(0, 7)}</a> by ${entry.author.displayName} on ${new Date(entry.timestamp).format("MM/dd/yyyy HH:mm:ss")}: ${entry.msg}</li>"
        }
    }

    buffer << '</ul>'

    return buffer;
}

/*
 * Generate email content after build successed.
 *
 * @param {Array} executedBuilds, e.g. [["QA", "https://applinks.com/qa.apk"]]
 *
 * Dependencies:
 *     - BRANCH_NAME
 */
def call(executedBuilds) {
    // def executedBuilds = [["${buildType}", "${apkURI}"]]
    def EMAIL_LINK_TEMPLATE = "<a href=\"{1}\">{0}</a>"

    return """
<p>Please click the link(s) below to download the apk file(s) build on ${env.BRANCH_NAME}.</p>
${reachTemplate(executedBuilds, EMAIL_LINK_TEMPLATE).join('<br/>\n')}
<p>Changes:</p>
${changelogsForEmail().join('\n')}
"""
}