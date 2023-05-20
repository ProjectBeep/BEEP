import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.data")
}

android {
    namespace = "com.lighthouse.beep.data.remote"

    @Suppress("UnstableApiUsage")
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        val kakaoSearchId = keystoreProperties.getProperty("kakao_search_id")
        buildConfigField("String", "kakaoSearchId", kakaoSearchId)
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)
}
