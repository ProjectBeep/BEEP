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

    println(rootDir.absolutePath)

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
//    implementation(projects.core)
//    implementation(projects.worker)
//    implementation(projects.utilsLocation)
//    implementation(projects.utilsRecognizer)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(projects.data.data)
    implementation(projects.data.remote)
    implementation(projects.data.local)

    implementation(projects.auth.auth)
    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(projects.ui.page.intro)
//    implementation(projects.ui.page.main)

//    implementation(projects.navApp)
//    implementation(projects.navMain)
//    implementation(projects.uiCoffee)
//    implementation(projects.uiCommon)
//    implementation(projects.uiIntro)
//    implementation(projects.uiMain)
//    implementation(projects.uiGifticonlist)
//    implementation(projects.uiHome)
//    implementation(projects.uiOpensourcelicense)
//    implementation(projects.uiPersonalinfopolicy)
//    implementation(projects.uiSecurity)
//    implementation(projects.uiSetting)
//    implementation(projects.uiTermsofuse)
//    implementation(projects.uiUsedgifticon)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.compose.material3.windowSizeClass)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.accompanist.systemuicontroller)

//    implementation(libs.androidx.hilt.work)
//    implementation(libs.androidx.work.runtime.ktx)
}

kapt {
    useBuildCache = true
}
