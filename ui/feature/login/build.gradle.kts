plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.login"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.ui)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(projects.library.permission)

    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.coil)
    implementation(libs.coil.svg)
}
