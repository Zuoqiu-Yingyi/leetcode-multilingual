/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.9/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // kotlin v1.9.25
    // alias(libs.plugins.diktat)
    alias(libs.plugins.spotless)

    // Apply the java-library plugin for API and implementation separation.
    "java-library"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Use the JUnit 5 integration.
    testImplementation(libs.junit.jupiter.engine)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)

    // REF: https://kotlinlang.org/docs/reflection.html#jvm-dependency
    implementation(kotlin("reflect"))
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

// REF: https://diktat.saveourtool.com/
// diktat {
//     // diktatConfigFile = rootProject.file("diktat-analysis.yml")
//     inputs { include("src/**/*.kt") }
//     // debug = true
// }


// REF: https://github.com/diffplug/spotless/tree/main/plugin-gradle#kotlin
configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        // by default the target is every '.kt' and '.kts` file in the java sourcesets

        // REF: https://github.com/diffplug/spotless/tree/main/plugin-gradle#diktat
        // diktat("2.0.0")

        // REF: https://github.com/diffplug/spotless/tree/main/plugin-gradle#ktlint
        ktlint("1.3.1")
            .setEditorConfigPath("$projectDir/../../../../.editorconfig")
            .editorConfigOverride(
                // REF: https://pinterest.github.io/ktlint/latest/rules/standard/
                mapOf(
                    "ktlint_standard_class-naming" to "disabled",
                    "ktlint_standard_filename" to "disabled",
                    "ktlint_standard_max-line-length" to "disabled",
                    "ktlint_standard_package-name" to "disabled",
                    "ktlint_standard_property-naming" to "disabled",
                )
            )

        // make sure every file has the following copyright header.
        // optionally, Spotless can set copyright years by digging
        // through git history (see "license" section below)
        // licenseHeader '/* (C)$YEAR */'
    }
}
