buildscript {
    dependencies {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
        val gmsVersion = libs.findVersion("gms-google-services").get()
        val gmsOosVersion = libs.findVersion("gms-oss-licenses-plugin").get()
        val crashlyticsVersion = libs.findVersion("firebase-crashlytics-gradle").get()
        classpath("com.google.gms:google-services:$gmsVersion")
        classpath("com.google.android.gms:oss-licenses-plugin:$gmsOosVersion")
        classpath("com.google.firebase:firebase-crashlytics-gradle:$crashlyticsVersion")
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
