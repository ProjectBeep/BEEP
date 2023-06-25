plugins {
    id("beep.android.data")
}

android {
    namespace = "com.lighthouse.beep.data"
}

dependencies {
    implementation(projects.core)
    implementation(projects.library.recognizer)

    implementation(libs.androidx.paging.common.ktx)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.javax.inject)
}
