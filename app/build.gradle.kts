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

    defaultConfig {
        applicationId = "com.lighthouse.beep"
        versionCode = 1
        versionName = "1.0.0"

        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        val naverMapApiId = keystoreProperties.getProperty("naver_map_api_id")
        manifestPlaceholders["naver_map_api_id"] = naverMapApiId
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
    implementation(projects.data.content)
    implementation(projects.data.database)
    implementation(projects.data.datastore)
    implementation(projects.data.encryptedpreference)
    implementation(projects.data.remote)
    implementation(projects.auth.auth)
    implementation(projects.auth.google)

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

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
}

kapt {
    useBuildCache = true
}
