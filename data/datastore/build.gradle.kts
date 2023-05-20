plugins {
    id("beep.android.data")
}

android {
    namespace = "com.lighthouse.beep.data.datastore"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.javax.inject)
}
