plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.lighthouse.beep.auth"

    defaultConfig {
        buildConfigField(
            "String",
            "FIREBASE_CUSTOM_TOKEN_AUTH_URL",
            "\"https://authWithToken-f3yfujosoa-du.a.run.app\"",
        )
    }

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
