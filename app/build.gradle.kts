plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // ✅ Apply kapt manually
    id("org.jetbrains.kotlin.kapt")     // ✅ kapt for annotation processing
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)               // ✅ Hilt plugin

}
hilt {
    enableAggregatingTask = false
}
android {
    compileSdk = 35
    namespace = "com.example.texttranslator"

    defaultConfig {
        applicationId = "com.example.texttranslator"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled=true

    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt.lib)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup:javapoet:1.13.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.5")

    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.lifecycle:lifecycle-process:2.7.0")


    implementation ("androidx.compose.material:material-icons-extended:1.6.7")
    implementation ("androidx.multidex:multidex:2.0.1")

    implementation ("com.airbnb.android:lottie-compose:6.0.0")

    // ML Kit Translation
    implementation ("com.google.mlkit:translate:17.0.1")
    implementation ("com.google.mlkit:language-id:17.0.4")
    implementation ("com.google.mlkit:text-recognition:16.0.0")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")


    implementation ("com.google.code.gson:gson:2.10.1")



    /*    implementation("com.google.dagger:hilt-android:2.48")
        kapt("com.google.dagger:hilt-android-compiler:2.48")

        implementation(libs.room.runtime)
        implementation(libs.room.ktx)
        kapt(libs.room.compiler)


        implementation ("androidx.room:room-runtime:2.6.1")
        kapt ("androidx.room:room-compiler:2.6.1")
        implementation ("androidx.room:room-ktx:2.6.1")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")*/


    /*
        // Room components
        implementation ("androidx.room:room-runtime:2.6.1")

        kapt("androidx.room:room-compiler:2.6.1") //


        // Kotlin extensions and coroutines support
        implementation ("androidx.room:room-ktx:2.6.1")

    // Lifecycle ViewModel & LiveData (already likely present)
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Optional - for Java 8+ features (e.g., Instant, LocalDate)
        implementation ("androidx.room:room-guava:2.6.1")*/







}
