@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.application")
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
        create("release") {
            storeFile = File(rootDir, keystoreProperties.getProperty("release_store_file_name"))
            storePassword = keystoreProperties.getProperty("release_store_password")
            keyAlias = keystoreProperties.getProperty("release_key_alias")
            keyPassword = keystoreProperties.getProperty("release_key_password")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE*")
    }
}

dependencies {
//    implementation(projects.core)
//    implementation(projects.worker)
//    implementation(projects.utilsLocation)
//    implementation(projects.utilsRecognizer)
    implementation(projects.theme)
    implementation(projects.domain)
    implementation(projects.data.data)
    implementation(projects.data.remote)
    implementation(projects.data.local)
    implementation(projects.auth.auth)
    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)
    implementation(projects.ui.core)
    implementation(projects.ui.page.intro)
    implementation(projects.ui.page.main)

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

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.fragment.ktx)

//    implementation(libs.androidx.hilt.work)
//    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
}

kapt {
    useBuildCache = true
}
