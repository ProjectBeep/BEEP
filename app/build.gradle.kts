@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.application")
    id("beep.android.hilt")
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
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
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")

//            isDebuggable = false
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE*")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.model)
    implementation(projects.theme)

    implementation(projects.data.data)
    implementation(projects.data.remote)
    implementation(projects.data.local)

    implementation(projects.auth)

    implementation(projects.ui.feature.login)
    implementation(projects.ui.feature.home)
    implementation(projects.ui.feature.gallery)
    implementation(projects.ui.feature.editor)
    implementation(projects.ui.feature.archive)
    implementation(projects.ui.feature.setting)

    implementation(projects.navs)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
}

kapt {
    useBuildCache = true
}
