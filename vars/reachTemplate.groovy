#!/usr/bin/env groovy


/*
 * Common method to generate a new array based on the template.
 * @param {Array} builds: It's array's array, e.g. [['QA','https://link.com/qa']
 * @param {String} itemTemplate: Item template to generate New Array, It can use index to refer to the element in inner array, for example: {0} indicates QA, {1} indicates the link of https://link.com/qa
 * @return {Array} It will return string array
 */
def call(builds, itemTemplate) {
    def buffer = []

    for (buildItems in builds) {
        String finalString = itemTemplate
        for (int index = 0; index < buildItems.size(); index++) {
            finalString = finalString.replace("{${index}}", buildItems[index])
        }
        buffer << finalString
    }

    return buffer
}