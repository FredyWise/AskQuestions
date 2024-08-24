import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.android.library) apply false
//    alias(libs.plugins.dagger.hilt.android.plugin) apply false
//    alias(libs.plugins.kotlin.kapt) apply false
}

fun BaseExtension.defaultConfig() {

    compileSdkVersion(34)

    defaultConfig {
        minSdk = 29
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun PluginContainer.applyDefaultConfigs(project: Project){
    whenPluginAdded{
        when (this) {
            is AppPlugin -> {
                project.extensions.getByType<AppExtension>().apply {
                    defaultConfig()
                }
            }
            is LibraryPlugin -> {
                project.extensions.getByType<LibraryExtension>().apply {
                    defaultConfig()
                }
            }
            is JavaPlugin -> {
                project.extensions.getByType<JavaPluginExtension>().apply {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}

buildscript {

//    ext{
//        compose_ui_version = "1.1.1"
//    }

    dependencies {
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

subprojects {
    project.plugins.applyDefaultConfigs(project)

    project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions{
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                listOf("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
            )
        }
    }
}