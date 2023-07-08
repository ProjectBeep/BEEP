plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.lighthouse.beep.data.local"

    packaging {
        resources.excludes.add("google/protobuf/field_mask.proto")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(libs.kotlin.coroutine.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.core.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.javax.inject)

    implementation(libs.google.protobuf.javalite)
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
