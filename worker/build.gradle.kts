@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.worker"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.commonAndroid)
    implementation(projects.domain)

    implementation(libs.androidX.hilt.work)
    implementation(libs.androidX.work.runtime.ktx)

    implementation(libs.timber)
}

// JUnit5
// tasks.withType<Test> {
//    useJUnitPlatform()
// }
