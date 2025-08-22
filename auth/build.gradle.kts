import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lighthouse.beep.auth"

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        buildConfigField(
            "String",
            "FIREBASE_CUSTOM_TOKEN_AUTH_URL",
            "\"https://authWithToken-f3yfujosoa-du.a.run.app\"",
        )

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            keystoreProperties.getProperty("google_server_client_id"),
        )

        buildConfigField(
            "String",
            "NAVER_LOGIN_CLIENT_ID",
            keystoreProperties.getProperty("naver_login_client_id"),
        )

        buildConfigField(
            "String",
            "NAVER_LOGIN_CLIENT_SECRET",
            keystoreProperties.getProperty("naver_login_client_secret"),
        )

        val kakaoNativeAppKey = keystoreProperties.getProperty("kakao_native_app_key")
        manifestPlaceholders["kakao_native_app_key"] = kakaoNativeAppKey

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"$kakaoNativeAppKey\"",
        )
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(projects.theme)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.kotlin.coroutine.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    implementation(libs.gms.play.services.auth)
    implementation(libs.naver.oauth)
    implementation(libs.kakao.sdk.user)
}
