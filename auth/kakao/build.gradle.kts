import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.auth.kakao"

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        val kakaoNativeAppKey = keystoreProperties.getProperty("kakao_native_app_key")
        manifestPlaceholders["kakao_native_app_key"] = kakaoNativeAppKey

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"$kakaoNativeAppKey\"",
        )
    }

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(libs.kakao.sdk.user)

    implementation(libs.androidx.fragment.ktx)
}
