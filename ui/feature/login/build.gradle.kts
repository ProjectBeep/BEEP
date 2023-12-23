plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.login"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.data.data)

    implementation(projects.ui.designsystem.dotindicator)

    implementation(projects.permission)

    implementation(projects.auth)

    implementation(projects.navs)
}
