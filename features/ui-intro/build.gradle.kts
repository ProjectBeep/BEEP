@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.features.intro"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.common)
    implementation(projects.commonAndroid)
    implementation(projects.uiCommon)

    implementation(libs.timber)
}
