@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.application")
    id("beep.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lighthouse.beep"

    defaultConfig {
        applicationId = "com.lighthouse.beep"
        versionCode = 1
        versionName = "1.0.0"

        val naverMapApiId = gradleLocalProperties(rootDir).getProperty("naver_map_api_id")
        manifestPlaceholders["naver_map_api_id"] = naverMapApiId
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/LICENSE*")
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.auth)
    implementation(projects.common)
    implementation(projects.commonAndroid)
    implementation(projects.worker)
    implementation(projects.utilsLocation)
    implementation(projects.utilsRecognizer)
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.dataDatabase)
    implementation(projects.dataPreference)
    implementation(projects.dataRemote)
    implementation(projects.uiCommon)
    implementation(projects.uiIntro)

    implementation(libs.androidX.hilt.work)
    implementation(libs.androidX.work.runtime.ktx)

    implementation(libs.timber)
}

kapt {
    useBuildCache = true
}
