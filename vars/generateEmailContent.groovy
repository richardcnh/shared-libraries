#!/usr/bin/env groovy

/*
 * Generate email content after build successed.
 *
 * @param {String} buildType, e.g. "QA"
 * @param {String} apkURI, e.g. "https://applinks.com/qa.apk"
 *
 * Dependencies:
 *     - BRANCH_NAME
 */
def call(buildType, apkURI) {
    def executedBuilds = [["${buildType}", "${apkURI}"]]
    def EMAIL_LINK_TEMPLATE = "<a href=\"{1}\">{0}</a>"

    return """
<p>Please click the link(s) below to download the apk file(s) build on ${env.BRANCH_NAME}.</p>
${reachTemplate(executedBuilds, EMAIL_LINK_TEMPLATE).join('<br/>\n')}
<p>Changes:</p>
${generateChangelogsForEmail()}
"""
}