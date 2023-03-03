plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.domain"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.common)

    implementation(libs.kotlin.coroutine.core)
    implementation(libs.androidX.paging.common.ktx)
    implementation(libs.javax.inject)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
