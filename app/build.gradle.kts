@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.application.compose")
    id("beep.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lighthouse.beep"

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    defaultConfig {
        applicationId = "com.lighthouse.beep"
        versionCode = 1
        versionName = "1.0.0"

        val naverMapApiId = keystoreProperties.getProperty("naver_map_api_id")
        manifestPlaceholders["naver_map_api_id"] = naverMapApiId
    }

    signingConfigs {
        getByName("debug") {
            storeFile = File(rootDir, keystoreProperties.getProperty("debug_store_file_name"))
            storePassword = keystoreProperties.getProperty("debug_store_password")
            keyAlias = keystoreProperties.getProperty("debug_key_alias")
            keyPassword = keystoreProperties.getProperty("debug_key_password")
        }
        create("release") {
            storeFile = File(rootDir, keystoreProperties.getProperty("release_store_file_name"))
            storePassword = keystoreProperties.getProperty("release_store_password")
            keyAlias = keystoreProperties.getProperty("release_key_alias")
            keyPassword = keystoreProperties.getProperty("release_key_password")
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.model)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(projects.data.data)
    implementation(projects.data.remote)
    implementation(projects.data.local)

    implementation(projects.auth.auth)
    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(projects.ui.feature.guide)
    implementation(projects.ui.feature.login)
    implementation(projects.ui.feature.main)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.compose.material3.windowSizeClass)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.accompanist.systemuicontroller)
}

kapt {
    useBuildCache = true
}
