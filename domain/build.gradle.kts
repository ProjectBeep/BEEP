plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.domain"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.model)
    implementation(projects.auth.auth)

    implementation(projects.data.data)

    implementation(projects.library.recognizer)

    implementation(libs.kotlin.coroutine.core)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.javax.inject)
}
