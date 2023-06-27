plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.core"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
}
