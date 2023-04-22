package com.lighthouse.convention

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import java.util.Optional
import org.gradle.api.artifacts.ProjectDependency

internal fun <T> DependencyHandler.implementation(
    dependencyNotation: Optional<Provider<T>>
): Dependency? = add("implementation", dependencyNotation.get())

internal fun DependencyHandler.implementation(
    projectDependency: ProjectDependency
): Dependency? = add("implementation", projectDependency)

internal fun <T> DependencyHandler.kapt(
    dependencyNotation: Optional<Provider<T>>
): Dependency? = add("kapt", dependencyNotation.get())
