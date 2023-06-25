plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lighthouse.beep.data.local"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.core.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.javax.inject)
}
