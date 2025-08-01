pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "Onmoim"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:network")
include(":core:event")
include(":core:dispatcher")
include(":core:data")
include(":core:datastore")
include(":core:domain")
include(":core:helper")
include(":core:ui")
include(":core:designsystem")
include(":core:util")
include(":feature:login")
include(":feature:home")
include(":feature:category")
include(":feature:profile")
include(":feature:groups")
include(":feature:location")
