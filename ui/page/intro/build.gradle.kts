import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.page")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.lighthouse.beep.ui.page.intro"

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        buildConfigField(
            "String",
            "FIREBASE_KAKAO_AUTH_URL",
            "\"https://authwithkakao-f3yfujosoa-du.a.run.app\"",
        )

        manifestPlaceholders["kakao_native_app_key"] =
            keystoreProperties.getProperty("kakao_native_app_key")

        buildConfigField(
            "String",
            "FIREBASE_GOOGLE_AUTH_URL",
            "\"https://authwithgoogle-f3yfujosoa-du.a.run.app\"",
        )

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            keystoreProperties.getProperty("google_server_client_id"),
        )

        buildConfigField(
            "String",
            "FIREBASE_NAVER_AUTH_URL",
            "\"https://authwithnaver-f3yfujosoa-du.a.run.app\"",
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
    }

    buildFeatures.buildConfig = true
}

dependencies {
//    implementation(projects.authGoogle)
//    implementation(projects.uiCommon)

    implementation(libs.kakao.sdk.user)
    implementation(libs.naver.oauth)
    implementation(libs.gms.play.services.auth)
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
