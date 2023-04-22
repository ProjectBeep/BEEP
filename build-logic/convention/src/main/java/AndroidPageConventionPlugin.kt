import com.lighthouse.convention.findVersionCatalog
import com.lighthouse.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

@Suppress("UNUSED")
class AndroidPageConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("beep.android.library")
                apply("beep.android.hilt")
            }

            val libs = findVersionCatalog()

            dependencies {
                implementation(project(":core:model"))
                implementation(project(":core:common"))
                implementation(project(":domain"))

                implementation(libs.findLibrary("androidx-core-ktx"))
                implementation(libs.findLibrary("androidx-appcompat"))
                implementation(libs.findLibrary("androidx-constraintlayout"))
                implementation(libs.findLibrary("androidx-fragment-ktx"))
                implementation(libs.findLibrary("android-material"))
            }
        }
    }
}