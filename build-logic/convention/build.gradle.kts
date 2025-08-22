plugins {
    `kotlin-dsl`
}

group = "com.lighthouse.beep.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "beep.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "beep.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "beep.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidUI") {
            id = "beep.android.ui"
            implementationClass = "AndroidUIConventionPlugin"
        }
        register("androidDesignSystem") {
            id = "beep.android.design.system"
            implementationClass = "AndroidDesignSystemConventionPlugin"
        }
        register("androidPage") {
            id = "beep.android.page"
            implementationClass = "AndroidPageConventionPlugin"
        }
        register("androidDialog") {
            id = "beep.android.dialog"
            implementationClass = "AndroidDialogConventionPlugin"
        }
        register("androidData") {
            id = "beep.android.data"
            implementationClass = "AndroidDataConventionPlugin"
        }
    }
}
