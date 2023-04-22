import com.lighthouse.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

@Suppress("UNUSED")
class AndroidDialogConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("beep.android.design.system")
            }

            dependencies {
                implementation(project(":model"))
                implementation(project(":domain"))
            }
        }
    }
}