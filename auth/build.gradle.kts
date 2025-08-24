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
    implementation(projects.domain)

    implementation(projects.theme)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlin.coroutine.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    // Google Sign-In - Credential Manager (Android 14+ 권장)
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    
    // 비활성화된 소셜 로그인 (사용하지 않음)
    // implementation(libs.naver.oauth)
    // implementation(libs.kakao.sdk.user)
    
    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.test:core:1.5.0")
}
