plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.gallery"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.ui)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(projects.navs)
}
