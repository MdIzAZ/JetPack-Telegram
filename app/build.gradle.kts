plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // kotlin("android")
    kotlin("kapt") // For annotation processing (Hilt)

    id("dagger.hilt.android.plugin") // Hilt plugin

}

android {
    namespace = "com.kroy.ssediotor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kroy.sseditor"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "v2.0.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    applicationVariants.all {
        outputs.all {
            this as com.android.build.gradle.internal.api.ApkVariantOutputImpl

            val name = "SSEditor"

            val apkName = name + "_" + defaultConfig.versionName + ".apk"

            outputFileName = apkName
        }
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation ("com.google.android.material:material:1.12.0")

    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Navigation dependencies (Compose)
    implementation("androidx.navigation:navigation-compose:2.8.1")
    // Jetpack Navigation for Compose


    // Hilt Jetpack Compose integration
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Coroutines dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel dependencies (for Compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")

    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For JSON serialization

    // OkHttp (optional, but commonly used with Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("io.coil-kt:coil-compose:2.2.1")

    implementation ("androidx.fragment:fragment-ktx:1.8.3")

    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // Icon Pack
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // latest as of July 2025




}