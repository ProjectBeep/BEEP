plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.domain"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.auth.auth)

    implementation(libs.kotlin.coroutine.core)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.javax.inject)

//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.auth.ktx)
}
