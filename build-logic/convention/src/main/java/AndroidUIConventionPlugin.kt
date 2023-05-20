import com.lighthouse.convention.findVersionCatalog
import com.lighthouse.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

@Suppress("UNUSED")
class AndroidUIConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("beep.android.library")
            }

            val libs = findVersionCatalog()

            dependencies {
                implementation(project(":core"))
                implementation(project(":theme"))

                implementation(libs.findLibrary("androidx-core-ktx"))
                implementation(libs.findLibrary("androidx-appcompat"))
                implementation(libs.findLibrary("androidx-constraintlayout"))
                implementation(libs.findLibrary("androidx-fragment-ktx"))
                implementation(libs.findLibrary("android-material"))
            }
        }
    }
}
