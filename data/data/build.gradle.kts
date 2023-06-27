plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.data"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)

    implementation(libs.androidx.paging.common.ktx)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.javax.inject)
}
