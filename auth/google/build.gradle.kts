import java.io.FileInputStream
import java.util.Properties

plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.auth.google"

    defaultConfig {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            keystoreProperties.getProperty("google_server_client_id"),
        )
    }

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.auth.auth)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.gms.play.services.auth)
}
