import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.auth.naver"

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

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
    implementation(libs.naver.oauth)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
