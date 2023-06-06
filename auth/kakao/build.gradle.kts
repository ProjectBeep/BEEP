plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.auth.kakao"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
}
