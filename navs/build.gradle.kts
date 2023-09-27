plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.navs"
}

dependencies {
    implementation(projects.model)
}