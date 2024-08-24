plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.fredy.askquestions.auth"
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(projects.core)
    implementation(projects.theme)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.messaging)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Coil
    implementation(libs.coil.compose)

    // Advanced Log
    implementation(libs.timber)

    // Additional UI dependencies
    implementation(libs.androidx.material.icons.extended)

//    // AI dependencies
//    implementation(libs.generativeai)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose.android)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Google
    // Firebase
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.messaging.ktx)
//    implementation(libs.firebase.firestore.ktx)
//    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.auth.ktx)
    //
//    implementation(libs.google.firebase.messaging.ktx)

    // Google apis
    implementation(libs.play.services.auth)
    implementation("com.google.api-client:google-api-client-android:1.26.0") {
        exclude(group = "org.apache.httpcomponents")
//        exclude(module = "guava-jdk5")
    }
    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0") {
        exclude(group = "org.apache.httpcomponents")
//        exclude(module = "guava-jdk5")
    }

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Retrofit networking
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.converter.gson)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

}