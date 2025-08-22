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

        // Kakao 인증 URL 비활성화
        // buildConfigField(
        //     "String",
        //     "FIREBASE_KAKAO_AUTH_URL",
        //     "\"https://authwithkakao-f3yfujosoa-du.a.run.app\"",
        // )

        // Kakao App Key 비활성화
        // manifestPlaceholders["kakao_native_app_key"] =
        //     keystoreProperties.getProperty("kakao_native_app_key")

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

        // Naver 인증 URL 비활성화
        // buildConfigField(
        //     "String",
        //     "FIREBASE_NAVER_AUTH_URL",
        //     "\"https://authwithnaver-f3yfujosoa-du.a.run.app\"",
        // )

        // Naver 로그인 클라이언트 ID/SECRET 비활성화
        // buildConfigField(
        //     "String",
        //     "NAVER_LOGIN_CLIENT_ID",
        //     keystoreProperties.getProperty("naver_login_client_id"),
        // )
        //
        // buildConfigField(
        //     "String",
        //     "NAVER_LOGIN_CLIENT_SECRET",
        //     keystoreProperties.getProperty("naver_login_client_secret"),
        // )
    }

    buildFeatures.buildConfig = true
}

dependencies {
//    implementation(projects.authGoogle)
//    implementation(projects.uiCommon)

    // Kakao, Naver SDK 의존성 비활성화
    // implementation(libs.kakao.sdk.user)
    // implementation(libs.naver.oauth)
    implementation(libs.gms.play.services.auth)

    implementation(projects.library.network)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
