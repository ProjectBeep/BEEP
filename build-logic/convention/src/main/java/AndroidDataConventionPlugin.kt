import com.lighthouse.convention.findVersionCatalog
import com.lighthouse.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

@Suppress("UNUSED")
class AndroidDataConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("beep.android.library")
                apply("beep.android.hilt")
            }

            val libs = findVersionCatalog()

            dependencies {
                implementation(project(":model"))

                implementation(libs.findLibrary("kotlin-coroutine-core"))
                implementation(libs.findLibrary("javax-inject"))
            }
        }
    }
}
