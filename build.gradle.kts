buildscript {
    dependencies {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
        val gmsOosVersion = libs.findVersion("gms-oss-licenses-plugin").get()
        classpath("com.google.android.gms:oss-licenses-plugin:$gmsOosVersion")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
