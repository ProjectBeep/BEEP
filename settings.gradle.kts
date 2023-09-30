@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}
rootProject.name = "BEEP"

include(":app")
include(":core:common")
include(":core:ui")
include(":navs")
include(":model")
include(":theme")
include(":domain")
include(":data:data")
include(":data:remote")
include(":data:local")
include(":library:barcode")
include(":library:permission")
include(":library:recognizer")
include(":auth:auth")
include(":auth:google")
include(":auth:kakao")
include(":auth:naver")
include(":ui:designsystem:cropview")
include(":ui:dialog:confirmation")
include(":ui:dialog:textinput")
include(":ui:feature:login")
include(":ui:feature:home")
include(":ui:feature:gallery")
include(":ui:feature:editor")
