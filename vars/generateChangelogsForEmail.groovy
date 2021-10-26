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
 * Generate changelogs with JIRA & github links after build successed.
 *
 */
def call() {
    return "${changelogsForSlack().join('\n')}"
}