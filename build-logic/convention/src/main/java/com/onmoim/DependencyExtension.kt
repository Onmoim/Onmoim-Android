package com.onmoim

import org.gradle.api.Project

fun Set<Project>.filterProject(
    body: (target: Project) -> Unit,
) {
    forEach { project ->
        if (project.name != "app" && project.buildFile.isFile) {
            body(project)
        }
    }
}