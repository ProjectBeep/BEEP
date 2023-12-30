plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.gifticondetail"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)

    implementation(projects.model)

    implementation(projects.navs)

    implementation(projects.auth)

    implementation(projects.data.data)

    implementation(projects.ui.dialog.confirmation)

    implementation(projects.library.textformat)
    implementation(projects.library.barcode)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.android.material)

    implementation(libs.glide)
}
