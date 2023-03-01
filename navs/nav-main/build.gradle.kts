@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.navs.main"
}

dependencies {
    implementation(projects.core)

    implementation(libs.androidX.fragment.ktx)

    implementation(libs.androidX.core.ktx)
}
