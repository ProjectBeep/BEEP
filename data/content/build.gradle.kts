plugins {
    id("beep.android.data")
}

android {
    namespace = "com.lighthouse.beep.data.content"
}

dependencies {
    implementation(projects.core)
    implementation(projects.data.data)

    implementation(libs.androidx.paging.common.ktx)
}
