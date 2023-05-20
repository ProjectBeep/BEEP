plugins {
    id("beep.android.data")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lighthouse.beep.data.database"
}

dependencies {
    implementation(projects.data.data)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.core.ktx)

    implementation(libs.kotlin.coroutine.core)

    ksp(libs.androidx.room.compiler)
}
